//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package NanoStitcher;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class NanoStitcher_Parameters {
    public NanoStitcher_Parameters() {
    }

    public static String SelectDisk() {
        int DiskNum = 1;
        String[] DiskName = new String[]{"D", "E", "F", "G", "H", "I", "J", "C"};

        for(int i = 0; i < 8; ++i) {
            String CurDiskName = DiskName[i] + ":\\";
            File f = new File(CurDiskName);
            if (f.exists()) {
                return CurDiskName;
            }
        }

        return null;
    }

    public static ArrayList<String> ListTxtFiles(String FolderPath) throws FileNotFoundException {
        ArrayList<String> FilesList = new ArrayList();
        File directory = new File(FolderPath);
        if (directory.isDirectory()) {
            File[] filelist = directory.listFiles();

            for(int i = 0; i < filelist.length; ++i) {
                if (!filelist[i].isDirectory()) {
                    String CurFilePath = filelist[i].getAbsolutePath();
                    if (CurFilePath.endsWith(".txt")) {
                        FilesList.add(CurFilePath);
                    }
                }
            }
        }

        return FilesList;
    }

    public static ColorModel GetHotColorModel() {
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];

        int q;
        for(q = 0; q < 85; ++q) {
            r[q] = (byte)(3 * q);
            g[q] = 0;
            b[q] = 0;
        }

        for(q = 85; q < 170; ++q) {
            r[q] = -1;
            g[q] = (byte)(3 * (q - 85));
            b[q] = 0;
        }

        for(q = 170; q < 256; ++q) {
            r[q] = -1;
            g[q] = -1;
            b[q] = (byte)(3 * (q - 170));
        }

        return new IndexColorModel(3, 256, r, g, b);
    }

    public static class LocalizationPara {
        float Kadc = 0.45F;
        float Offset = 100.0F;
        float QE = 0.72F;
        int RegionSize = 7;
        float RawImgPixelSize = 10.0F;
        float RenderingPixelSize = 10.0F;
        float RenderingPixelZoom = 1.0F;
        int LocType = 0;
        int MultiEmitterFitEn = 0;
        int WLEEn = 1;
        int ConsecutiveFitEn = 0;
        float ConsecFilterRadius = 80.0F;
        float MinZDepth = -500.0F;
        float MaxZDepth = 500.0F;
        float ZDepthCorrFactor = 1.0F;
        float p4_XGY = 0.0F;
        float p3_XGY = 0.0F;
        float p2_XGY = 0.0F;
        float p1_XGY = 1.0F;
        float p0_XGY = 0.0F;
        float p4_XLY = 0.0F;
        float p3_XLY = 0.0F;
        float p2_XLY = 0.0F;
        float p1_XLY = 1.0F;
        float p0_XLY = 0.0F;
        int RotationType = 0;
        float MeanDistance = 10.0F;
        float DistanceTh = 1.5F;
        float SNR_th = 5.0F;
        float StructureSize2D = 40.0F;
        int StatDispSel = 0;
        int SpatialResolutionEn = 0;

        public LocalizationPara() {
        }
    }
}
