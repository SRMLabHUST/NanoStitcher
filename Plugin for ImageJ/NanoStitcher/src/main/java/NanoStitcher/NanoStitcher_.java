package NanoStitcher;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageWindow;
import ij.io.FileSaver;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import javax.swing.*;
import java.awt.image.ColorModel;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;

public class NanoStitcher_ implements PlugInFilter {
    // parameters from imageJ
    public ImagePlus CurImagePlus;
    public ImageProcessor CurImageProcessor;
    public ImageStack CurImageStack;

    // image info
    public int ImageWidthI, ImageHighI, FrameNum;
    public int SRImageWidthI,SRImageHighI;

    public String RawImgName;


    // display of super resolution image
    // for 2d image, it's a float image, 3d is a color(RGB) image
    public ImagePlus CurSRImagePlus;
    public volatile ImageWindow CurSRImageWindow;
    public FloatProcessor CurSRImageProcessor2D;
    public ColorProcessor CurSRImageProcessor3D;
    public ColorModel hotCM;

    //public ImageLocalizationThread LocThread=null;



    // receive rendered image thread
    public String SRImgName;
    public RecSRImgThread recImgThread;

    public NanoStitcher_Configurator MyConfigurator;


    short [] BatchedImgDat;
    int BatchedImgNum;
    int PixelNumI;



    public StatInfoDisplay CurStatInfoDisplay=null;
    public volatile int StatDispSelI;


    // wrapper of CPP & CUDA library
    public native static void lm_SetLocPara(float KAdc, float Offset, float QE, int ROISize, int LocType, int MultiEmitterFitEn, int WLEEn, int ConsecutiveFitEn, float ConsecFilterRadius, float RawPixelSize, float RenderPixelZoom, float SNR_th);
    public native static void lm_SetStatInfSelection(int DispSel, int SpatialResolutionEn);
    public native static int lm_GetMaxDispVal();
    public native static float [] lm_GetSMLMImage();
    public native static void lm_SetSpatialResolutionInf(float StructureSize);
    public native static int [] lm_GetStatInfImageSize();
    public native static int [] lm_GetStatInfImage(int n);
    public native static int lm_IsLocFinish();
    // rerend image
    public native static void lm_SetRerendImagePara(String ImageName, int IsDriftCorrectionI, int DriftCorrGroupFrameNum);
    public native static void lm_StartRerend();
    public native static int [] lm_GetRerendImageInf();
    public native static void lm_ReleaseRerendResource();
    public native static void lm_CombineLocarryForImagej(int length, int width, String DistancePath, String newPicturePath, String datasetDirPath);

    @Override
    public int setup(String string, ImagePlus ip) {
        System.out.println("NanoStitcher_ setup");
        // initial
        System.loadLibrary("NanoStitcher_render");
        CurImagePlus=ip;


        if(CurImagePlus!=null)
        {
            CurImageStack = CurImagePlus.getImageStack();

            ImageWidthI = CurImagePlus.getWidth();// width of raw PALM image
            ImageHighI = CurImagePlus.getHeight();// height of raw PALM image
            FrameNum = CurImagePlus.getImageStackSize();// total frames number of raw PALM image
            RawImgName = CurImagePlus.getTitle();


            // batch processing
            BatchedImgNum = (2048 * 2048 / ImageWidthI / ImageHighI);
            BatchedImgNum = (BatchedImgNum / 2) * 2;

            if(BatchedImgNum<1)BatchedImgNum=1;
            if(BatchedImgNum>FrameNum)BatchedImgNum=FrameNum;

            // BatchedImgNum=1;

            BatchedImgDat = new short[BatchedImgNum*ImageWidthI*ImageHighI];
            PixelNumI=ImageWidthI*ImageHighI;

        }

        CurSRImagePlus=new ImagePlus();

        MyConfigurator=new NanoStitcher_Configurator(this);
        MyConfigurator.setVisible(true);

        hotCM = NanoStitcher_Parameters.GetHotColorModel();

        return NO_IMAGE_REQUIRED + DOES_16 + NO_CHANGES + SUPPORTS_MASKING; //open statement
    }

    @Override
    public void run(ImageProcessor ip) {
        CurImageProcessor = ip;

    }

    private void InitSRDisplay()
    {

        // for display rendered image, pre create them since create takes a lot of time
        if(MyConfigurator.LocPara.LocType == 0)
        {
            CurSRImageProcessor2D = new FloatProcessor(SRImageWidthI, SRImageHighI);
            CurSRImageProcessor3D = null;
        }
        else
        {
            CurSRImageProcessor2D = null;
            CurSRImageProcessor3D = new ColorProcessor(SRImageWidthI, SRImageHighI);
        }
    }

    public void StartRerend() throws InterruptedException, FileNotFoundException
    {

        System.out.println("StarRerend");
//        JOptionPane.showMessageDialog(null, "begin to rend", "loc finish!", JOptionPane.PLAIN_MESSAGE);

        RerendThread_Main RerendThread_Entry = new RerendThread_Main();
        RerendThread_Entry.start();

        System.out.println("StarRerend END");
    }



    public class RerendThread_Main extends Thread
    {
        @Override
        public void run()
        {
            System.out.println("[RerendThread_Main].run");
            RerendThread CurRerendThread;

            MyConfigurator.GetLocalizationPara();
            MyConfigurator.SendLocalizationPara();

            CurSRImagePlus.setTitle("Rerend image");

            try {
                if(MyConfigurator.RerendMode==0)
                {
                    CurRerendThread = new RerendThread(MyConfigurator.RerendLocDataPath);
                    CurRerendThread.start();
                    System.out.println("CurRerendThread.start 1");
                    CurRerendThread.join();

                }
                else
                {
                    ArrayList<String> FilesList = NanoStitcher_Parameters.ListTxtFiles(MyConfigurator.RerendLocDataPath);

                    for(int i = 0; i < FilesList.size(); i ++)
                    {
                        String FullFilePath = FilesList.get(i);

                        CurRerendThread = new RerendThread(FullFilePath);
                        CurRerendThread.start();
                        CurRerendThread.join();
                    }
                }

                MyConfigurator.EnableRerend();

                JOptionPane.showMessageDialog(null, "rend finish", "rend finish!", JOptionPane.PLAIN_MESSAGE);

            } catch (Exception ex) {

            }
        }
    }

    public class RerendThread extends Thread
    {
        String LocFileName;

        RerendThread(String iLocFileName)
        {
            LocFileName = iLocFileName;
        }

        @Override
        public void run()
        {
            int [] ImgInf;


            lm_SetRerendImagePara(LocFileName, MyConfigurator.DriftCorrEnableI, MyConfigurator.DriftCorrGroupFrameNum);
            System.out.println(LocFileName + "," + MyConfigurator.DriftCorrEnableI + "," + MyConfigurator.DriftCorrGroupFrameNum);
            lm_StartRerend();


            while(true)
            {
                ImgInf = lm_GetRerendImageInf();

                if(ImgInf[0]>0)
                {
                    break;
                }
            }


            // get image size from cpp codes
            ImageWidthI = ImgInf[1];
            ImageHighI = ImgInf[2];
            SRImageWidthI = ImgInf[3];
            SRImageHighI = ImgInf[4];


            InitSRDisplay();

            try {

                // receive rendered image
                recImgThread = new RecSRImgThread();
                recImgThread.start();
                recImgThread.join();

            } catch (Exception ex) {

            }

            //           while(recImgThread.isAlive());

            FileSaver ResultTifSaver = new FileSaver(CurSRImagePlus);
            String SaveImgName_PostFix = String.format("_rerend%.2fnm.tif", MyConfigurator.LocPara.RenderingPixelSize);

            String SaveImgName = LocFileName.substring(0, LocFileName.length()-4) + SaveImgName_PostFix;
            System.out.println(SaveImgName);
            ResultTifSaver.saveAsTiff(SaveImgName);


            lm_ReleaseRerendResource();

        }
    }

    public class RecSRImgThread extends Thread
    {
        @Override
        public void run()
        {

            boolean IsBreakB = false;
            boolean IsDispaly = false;

            while(true)
            {
                if(lm_IsLocFinish()!=0)
                {
                    IsBreakB = true;
                }

                if(MyConfigurator.LocPara.LocType == 0)
                {
                    // get 2d display rended image
                    float RecImgF[] = lm_GetSMLMImage();

                    if(RecImgF.length>10)
                    {

                        CurSRImageProcessor2D.setPixels(RecImgF);
                        CurSRImageProcessor2D.setColorModel(hotCM);
                        CurSRImagePlus.setProcessor(CurSRImageProcessor2D);
                        CurSRImagePlus.updateImage();
                        CurSRImagePlus.setDisplayRange(0, lm_GetMaxDispVal());


                        if(IsDispaly==false)
                        {
                            IsDispaly = true;
                            CurSRImageWindow = new ImageWindow(CurSRImagePlus);
                        }
                        CurSRImagePlus.draw();
                        CurSRImagePlus.updateImage();
                        CurSRImagePlus.setDisplayRange(0, lm_GetMaxDispVal());
                    }
                }


                if(CurStatInfoDisplay==null)
                {
                    CurStatInfoDisplay = new StatInfoDisplay(StatDispSelI);
                }


                // get current para set
                MyConfigurator.LocPara = MyConfigurator.GetLocalizationPara();
                StatDispSelI = MyConfigurator.LocPara.StatDispSel;

                CurStatInfoDisplay.UpdateDisplay(StatDispSelI);


                if(IsBreakB)break;

                try {
                    sleep(2000);
                } catch (InterruptedException ex) {

                }
            }
        }
    }

    public class StatInfoDisplay
    {
        // define in CPP codes
        int AxesImgWidth = 890;
        int AxesImgHigh = 560;

        public final int DispInfNum = 15;

        public ColorProcessor [] DispColorProcessor = new ColorProcessor[DispInfNum];
        public ImagePlus      [] DispImagePlus = new ImagePlus[DispInfNum];
        public ImageWindow    [] DispImageWindow = new ImageWindow[DispInfNum];

        String [] DispInfName={
                "Total photon distribution",
                "Localization precision distribution",
                "SNR (peak photon to background) distribution",
                "PSF width (Pixel) distribution",

                "Total photon variation",
                "Localization precision variation",
                "Ontime variation",
                "SNR variation",
                "PSF width variation",
                "Localization density 2D variation",
                "Background variation",

                "Spatial resolution variation (nm)",
                "Nyquist resolution variation (nm)",
                "Dimension FD variation",
                "Localization density FD variation"

        };

        public void UpdateDisplay(int DispSel)
        {
            int curselI=0x0001;
            int cntI;

            for(cntI=0;cntI<DispInfNum;cntI++)
            {
                if((DispSel&curselI)!=0)
                {
                    int RecImgI[] = lm_GetStatInfImage(cntI);


                    DispColorProcessor[cntI].setPixels(RecImgI);
                    DispImagePlus[cntI].setProcessor(DispColorProcessor[cntI]);
                    DispImagePlus[cntI].updateImage();
                    DispImagePlus[cntI].draw();

                    if(DispImageWindow[cntI].isClosed())
                    {
                        DispImageWindow[cntI] = new ImageWindow(DispImagePlus[cntI] );
                    }
                    if(DispImageWindow[cntI].isVisible()==false)
                    {
                        DispImageWindow[cntI].setVisible(true);
                    }
                }
                else
                {
                    DispImageWindow[cntI].setVisible(false);
                }
                curselI<<=1;
            }
        }

        public StatInfoDisplay(int DispSel)
        {
            int ImageSize[] = lm_GetStatInfImageSize();

            AxesImgWidth = ImageSize[0];
            AxesImgHigh = ImageSize[1];


            int curselI=0x0001;
            int cntI;
            for(cntI=0;cntI<DispInfNum;cntI++)
            {
                DispColorProcessor[cntI] = new ColorProcessor(AxesImgWidth, AxesImgHigh);
                DispImagePlus[cntI] = new ImagePlus(DispInfName[cntI], DispColorProcessor[cntI]);
                DispImageWindow[cntI] = new ImageWindow(DispImagePlus[cntI] );

                if((DispSel&curselI)!=0)
                {
                    DispImageWindow[cntI].setVisible(true);
                }
                else
                {
                    DispImageWindow[cntI].setVisible(false);
                }
                curselI<<=1;

            }
        }
    }
//    public NanoStitcher_(){
//        setup("a", null);
//    }
//
//    public static void main(String[] args) {
//        URL a = ClassLoader.getSystemResource("");
//        System.out.println(a);
//        NanoStitcher_ nanoStitcherRender = new NanoStitcher_();
//        //nanoStitcherRender.getClass().getClassLoader().getResource("");
//    }

}
