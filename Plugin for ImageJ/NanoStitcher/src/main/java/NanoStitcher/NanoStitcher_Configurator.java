package NanoStitcher;



import ij.io.OpenDialog;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * @author Nash
 */
public class NanoStitcher_Configurator extends JFrame {

    public String DisPath = null;
    public String newPicPath = null;
    public String datasetDirPath = null;
    public int Image_Length = 0;
    public int Image_Width = 0;
    public int EdgeSize = 0;

    public NanoStitcher_Parameters.LocalizationPara LocPara;

    public String RerendLocDataPath = null;
    public int RerendMode = 0; // 0: single file, 1: dir

    public NanoStitcher_ nanoStitcherRender;

    public int DriftCorrEnableI = 1;
    public int DriftCorrGroupFrameNum = 1000;

    final String LocParafileName = "NanoStitcher_Configurator.NanoStitcher\\NanoStitcher_IJ_para.properties";
    volatile public String ResultsFilePath = "D:\\NanoStitcher.txt";

    public NanoStitcher_Configurator() {
        System.out.println("NanoStitcher_Configurator has para");
        initComponents();

        LocPara = new NanoStitcher_Parameters.LocalizationPara();

        iniContant();
    }

    public NanoStitcher_Configurator(NanoStitcher_ fa_NanoStitcherRender) {
        System.out.println("NanoStitcher_Configurator no para");
        initComponents();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NanoStitcher");

        nanoStitcherRender = fa_NanoStitcherRender;

        LocPara = new NanoStitcher_Parameters.LocalizationPara();

        iniContant();
    }

    private void jButton_LoadLocfile(ActionEvent e) {
        OpenDialog dlgDialog = new OpenDialog("open localization result file");

        String filePath = dlgDialog.getPath();
        jTextField_LocDataPath.setText(filePath);
        jTextField_LocDataDir.setText("");
        RerendLocDataPath = filePath;

        RerendMode = 0;
    }

    private void jButton_LoadLocDir(ActionEvent e) {
        String Path = jTextField_LocDataDir.getText();

        JFileChooser jf;

        if(Path.length()>5)
        {
            jf = new JFileChooser(Path);
        }else
        {
            jf = new JFileChooser();
        }


        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jf.showDialog(null,null);
        File fi = jf.getSelectedFile();

        RerendLocDataPath = fi.getAbsolutePath();


        jTextField_LocDataPath.setText("");
        jTextField_LocDataDir.setText(RerendLocDataPath);

        RerendMode = 1;
    }

    private void jButton_Render(ActionEvent e) {
        if(RerendLocDataPath==null)return;

        if(RerendLocDataPath.length() < 2 )return;

        if(RerendMode == 0)
        {
            if(!RerendLocDataPath.endsWith(".txt"))
            {
                return;
            }
        }

        DriftCorrEnableI = 1;
        DriftCorrGroupFrameNum = 1000;


        DisableRerend();


        try {
            nanoStitcherRender.StartRerend();

        } catch (Exception ex) {
            System.out.println("nanoStitcherRender has error");
        }

    }

    public void EnableRerend(){
        System.out.println("EnableRerend");
        jButton_Render.setEnabled(true);
    }

    public void DisableRerend()
    {

        System.out.println("DisableRerend");
        jButton_Render.setEnabled(false);
    }

    public NanoStitcher_Parameters.LocalizationPara GetLocalizationPara()
    {

        LocPara.RawImgPixelSize = Float.parseFloat(jTextField_RawPixelSize.getText());
        LocPara.RenderingPixelSize = Float.parseFloat(jTextField_RenderPixelSize.getText());
        LocPara.RenderingPixelZoom = LocPara.RawImgPixelSize/LocPara.RenderingPixelSize;

        LocPara.ConsecutiveFitEn = 0;
        LocPara.ConsecFilterRadius = 80;
        ResultsFilePath = jTextField_LocDataRes.getText();

        return LocPara;

    }

    void SendLocalizationPara()
    {
        GetLocalizationPara();

        System.out.println(LocPara.Kadc + " " + LocPara.Offset + " " + LocPara.QE + " " + LocPara.RegionSize + " " + LocPara.LocType + " " + LocPara.MultiEmitterFitEn + " " + LocPara.WLEEn + " " + LocPara.ConsecutiveFitEn + " " + LocPara.ConsecFilterRadius + " " + LocPara.RawImgPixelSize + " " + LocPara.RenderingPixelZoom + " " + LocPara.SNR_th);
        System.out.println(LocPara.MinZDepth + " " + LocPara.MaxZDepth + " " + LocPara.ZDepthCorrFactor + " " + LocPara.p4_XGY + " " +  LocPara.p3_XGY + " " +  LocPara.p2_XGY + " " +  LocPara.p1_XGY + " " +  LocPara.p0_XGY + " " +  LocPara.p4_XLY + " " +  LocPara.p3_XLY + " " +  LocPara.p2_XLY + " " +  LocPara.p1_XLY + " " +  LocPara.p0_XLY);
        System.out.println(LocPara.RotationType + " " + LocPara.MeanDistance + " " + LocPara.DistanceTh);

        NanoStitcher_.lm_SetLocPara(LocPara.Kadc,LocPara.Offset,LocPara.QE,LocPara.RegionSize,LocPara.LocType, LocPara.MultiEmitterFitEn, LocPara.WLEEn, LocPara.ConsecutiveFitEn, LocPara.ConsecFilterRadius, LocPara.RawImgPixelSize,LocPara.RenderingPixelZoom,LocPara.SNR_th);

        NanoStitcher_.lm_SetStatInfSelection(LocPara.StatDispSel, LocPara.SpatialResolutionEn);
        System.out.println(LocPara.StatDispSel + " " + LocPara.SpatialResolutionEn);
        NanoStitcher_.lm_SetSpatialResolutionInf(LocPara.StructureSize2D);
        System.out.println(LocPara.StructureSize2D);


        System.out.println("SendLocalizationPara");
    }

    public void iniContant(){
        System.out.println("iniContant");


        jTextField_RawPixelSize.setText(Float.toString(LocPara.RawImgPixelSize));
        jTextField_RenderPixelSize.setText(Float.toString(LocPara.RenderingPixelSize));
        jTextField_SNR_th.setText(Float.toString(LocPara.SNR_th));

        if(!IsSavePathValid(ResultsFilePath))
        {
            ResultsFilePath = NanoStitcher_Parameters.SelectDisk();
        }

        jTextField_LocDataRes.setText(ResultsFilePath);

        System.out.println("ResultsFilePath");
    }


    public String GetParaFileSavePath()
    {
        System.out.println("GetParaFileSavePath");
        String ParaFilePath = NanoStitcher_Parameters.SelectDisk();
        return ParaFilePath + LocParafileName;
    }

    public boolean IsSavePathValid(String iPath)
    {
        System.out.println("IsSavePathValid");
        File f = new File(iPath);
        boolean Valid = f.exists();

        return Valid;
    }

    private void jButton_datasetDir(ActionEvent e) {
        System.out.println("jButton_datasetDir");

        System.out.println("jButton_stitching_res");
        String Path = jTextField_LocDataDir.getText();

        JFileChooser jf;

        if(Path.length()>5)
        {
            jf = new JFileChooser(Path);
        }else
        {
            jf = new JFileChooser();
        }


        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jf.showDialog(null,null);
        File fi = jf.getSelectedFile();

        String filePath = fi.getAbsolutePath();


        jTextField_LocDataset.setText(filePath);
        datasetDirPath = filePath;

    }

    private void jButton_offset_storage(ActionEvent e) {
        System.out.println("jButton_offset_storage");

        String Path = jTextField_Loc_Offset_Storage.getText();

        JFileChooser jf;

        if(Path.length()>5)
        {
            jf = new JFileChooser(Path);
        }else
        {
            jf = new JFileChooser();
        }


        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jf.showDialog(null,null);
        File fi = jf.getSelectedFile();

        String newPicDirPath = fi.getAbsolutePath();



        jTextField_Loc_Offset_Storage.setText(newPicDirPath);
    }

    private void jButton_stitching_res(ActionEvent e) {
        System.out.println("jButton_stitching_res");
        String Path = jTextField_LocDataDir.getText();

        JFileChooser jf;

        if(Path.length()>5)
        {
            jf = new JFileChooser(Path);
        }else
        {
            jf = new JFileChooser();
        }


        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jf.showDialog(null,null);
        File fi = jf.getSelectedFile();

        String newPicDirPath = fi.getAbsolutePath();


        jTextField_LocDataRes.setText(newPicDirPath);
    }

    private void jButton_Stitching(ActionEvent e) {
        System.out.println("jButton_Stitching");
//        DisPath
//        newPicPath
//        datasetDirPath

        //判断文本框空值


        //getFirstInf();

        DisPath = jTextField_Loc_Offset_Storage.getText();
        newPicPath = jTextField_LocDataRes.getText();
        datasetDirPath = jTextField_LocDataset.getText();
        if (!IsSavePathValid(DisPath)) {
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(null, "please input Offset Storage", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            return;
        } else if (!IsSavePathValid(newPicPath)) {
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(null, "please input Stitch result's path", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            return;
        } else if (!IsSavePathValid(datasetDirPath)) {
            Object[] options = {"OK"};
            JOptionPane.showOptionDialog(null, "please input dataset's path", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            return;
        }

        DisPath = DisPath+"\\" + textField_Len.getText()+"x"+textField_Wid.getText()+"minTree.txt";
        newPicPath = newPicPath + "\\" + "NanoStitchResule" + ".txt";
        datasetDirPath = datasetDirPath + "\\";
        Image_Length= Integer.parseInt(textField_Len.getText());
        Image_Width = Integer.parseInt(textField_Wid.getText());
        EdgeSize = Integer.parseInt(textField_Edge.getText());

        System.out.println(Image_Length);
        System.out.println(Image_Width);
        System.out.println(EdgeSize);
        System.out.println(DisPath);
        System.out.println(newPicPath);
        System.out.println(datasetDirPath);

        jButton_Stitching.setEnabled(false);
        //CallMatlab.begin(Image_Length, Image_Width, EdgeSize, datasetDirPath, DisPath);

        CallJRMPC.begin(Image_Length, Image_Width, EdgeSize, datasetDirPath, DisPath);
        NanoStitcher_.lm_CombineLocarryForImagej(Image_Length, Image_Width, DisPath, newPicPath, datasetDirPath);
        jButton_Stitching.setEnabled(true);

    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        jTextField_LocDataset = new JTextField();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        textField_Len = new JTextField();
        textField_Wid = new JTextField();
        label6 = new JLabel();
        textField_Edge = new JTextField();
        label7 = new JLabel();
        jTextField_Loc_Offset_Storage = new JTextField();
        label8 = new JLabel();
        jTextField_LocDataRes = new JTextField();
        jButton_Stitching = new JButton();
        jButton_dataset = new JButton();
        jButton_offset_storage = new JButton();
        jButton_stitching_res = new JButton();
        panel2 = new JPanel();
        label9 = new JLabel();
        label10 = new JLabel();
        jTextField_LocDataPath = new JTextField();
        jTextField_LocDataDir = new JTextField();
        jButton_LoadLocfile = new JButton();
        jButton_LoadLocDir = new JButton();
        jButton_Render = new JButton();
        label1 = new JLabel();
        label2 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        jTextField_SNR_th = new JTextField();
        jTextField_RenderPixelSize = new JTextField();
        jTextField_RawPixelSize = new JTextField();
        label14 = new JLabel();
        label15 = new JLabel();

        //======== this ========
        setTitle("NanoStitching");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(null);

            //======== tabbedPane1 ========
            {
                tabbedPane1.setPreferredSize(null);

                //======== panel1 ========
                {
                    panel1.setMaximumSize(new Dimension(33000, 33000));
                    panel1.setLayout(null);
                    panel1.add(jTextField_LocDataset);
                    jTextField_LocDataset.setBounds(20, 35, 330, 30);

                    //---- label3 ----
                    label3.setText("Dataset path:");
                    panel1.add(label3);
                    label3.setBounds(35, 15, 85, label3.getPreferredSize().height);

                    //---- label4 ----
                    label4.setText("Number of localization list in the x_direction:");
                    panel1.add(label4);
                    label4.setBounds(new Rectangle(new Point(35, 80), label4.getPreferredSize()));

                    //---- label5 ----
                    label5.setText("Number of localization list in the y_direction:");
                    panel1.add(label5);
                    label5.setBounds(new Rectangle(new Point(35, 115), label5.getPreferredSize()));
                    panel1.add(textField_Len);
                    textField_Len.setBounds(300, 75, 60, 30);
                    panel1.add(textField_Wid);
                    textField_Wid.setBounds(300, 110, 60, 30);

                    //---- label6 ----
                    label6.setText("Extent of overlap between datasets:");
                    panel1.add(label6);
                    label6.setBounds(new Rectangle(new Point(35, 150), label6.getPreferredSize()));
                    panel1.add(textField_Edge);
                    textField_Edge.setBounds(300, 145, 60, 30);

                    //---- label7 ----
                    label7.setText("Offset storage location:");
                    panel1.add(label7);
                    label7.setBounds(new Rectangle(new Point(35, 195), label7.getPreferredSize()));
                    panel1.add(jTextField_Loc_Offset_Storage);
                    jTextField_Loc_Offset_Storage.setBounds(20, 220, 330, 30);

                    //---- label8 ----
                    label8.setText("Storage location for stitching results:");
                    panel1.add(label8);
                    label8.setBounds(new Rectangle(new Point(35, 260), label8.getPreferredSize()));
                    panel1.add(jTextField_LocDataRes);
                    jTextField_LocDataRes.setBounds(20, 280, 330, 30);

                    //---- jButton_Stitching ----
                    jButton_Stitching.setText("Stitching");
                    jButton_Stitching.addActionListener(e -> jButton_Stitching(e));
                    panel1.add(jButton_Stitching);
                    jButton_Stitching.setBounds(210, 335, 125, 35);

                    //---- jButton_dataset ----
                    jButton_dataset.setText("Load dataset dir");
                    jButton_dataset.addActionListener(e -> jButton_datasetDir(e));
                    panel1.add(jButton_dataset);
                    jButton_dataset.setBounds(365, 35, 170, 30);

                    //---- jButton_offset_storage ----
                    jButton_offset_storage.setText("Offset storage location");
                    jButton_offset_storage.addActionListener(e -> jButton_offset_storage(e));
                    panel1.add(jButton_offset_storage);
                    jButton_offset_storage.setBounds(365, 220, 170, 30);

                    //---- jButton_stitching_res ----
                    jButton_stitching_res.setText("Storage stitching results");
                    jButton_stitching_res.addActionListener(e -> jButton_stitching_res(e));
                    panel1.add(jButton_stitching_res);
                    jButton_stitching_res.setBounds(365, 280, 170, 30);
                }
                tabbedPane1.addTab("Offset", panel1);

                //======== panel2 ========
                {
                    panel2.setLayout(null);

                    //---- label9 ----
                    label9.setText("Single file rendering:");
                    panel2.add(label9);
                    label9.setBounds(new Rectangle(new Point(30, 215), label9.getPreferredSize()));

                    //---- label10 ----
                    label10.setText("Multi-files rendering:");
                    panel2.add(label10);
                    label10.setBounds(new Rectangle(new Point(30, 275), label10.getPreferredSize()));
                    panel2.add(jTextField_LocDataPath);
                    jTextField_LocDataPath.setBounds(25, 240, 300, 30);
                    panel2.add(jTextField_LocDataDir);
                    jTextField_LocDataDir.setBounds(25, 300, 300, 30);

                    //---- jButton_LoadLocfile ----
                    jButton_LoadLocfile.setText("Load localization result file");
                    jButton_LoadLocfile.addActionListener(e -> {
                        jButton_LoadLocfile(e);
                    });
                    panel2.add(jButton_LoadLocfile);
                    jButton_LoadLocfile.setBounds(340, 240, 190, jButton_LoadLocfile.getPreferredSize().height);

                    //---- jButton_LoadLocDir ----
                    jButton_LoadLocDir.setText("Load localization results dir");
                    jButton_LoadLocDir.addActionListener(e -> jButton_LoadLocDir(e));
                    panel2.add(jButton_LoadLocDir);
                    jButton_LoadLocDir.setBounds(new Rectangle(new Point(340, 300), jButton_LoadLocDir.getPreferredSize()));

                    //---- jButton_Render ----
                    jButton_Render.setText("Render");
                    jButton_Render.addActionListener(e -> jButton_Render(e));
                    panel2.add(jButton_Render);
                    jButton_Render.setBounds(230, 335, 85, 40);

                    //---- label1 ----
                    label1.setText("Render ROI size (pixel):");
                    panel2.add(label1);
                    label1.setBounds(new Rectangle(new Point(30, 25), label1.getPreferredSize()));

                    //---- label2 ----
                    label2.setText("Raw image pixel size(nm):");
                    panel2.add(label2);
                    label2.setBounds(new Rectangle(new Point(30, 95), label2.getPreferredSize()));

                    //---- label11 ----
                    label11.setText("Rendering pixel size(nm):");
                    panel2.add(label11);
                    label11.setBounds(new Rectangle(new Point(30, 130), label11.getPreferredSize()));

                    //---- label12 ----
                    label12.setText("Rendering SNR threshold:");
                    panel2.add(label12);
                    label12.setBounds(new Rectangle(new Point(30, 165), label12.getPreferredSize()));

                    //---- label13 ----
                    label13.setText("Render type:");
                    panel2.add(label13);
                    label13.setBounds(new Rectangle(new Point(30, 60), label13.getPreferredSize()));
                    panel2.add(jTextField_SNR_th);
                    jTextField_SNR_th.setBounds(210, 160, 65, 30);
                    panel2.add(jTextField_RenderPixelSize);
                    jTextField_RenderPixelSize.setBounds(210, 125, 65, 30);
                    panel2.add(jTextField_RawPixelSize);
                    jTextField_RawPixelSize.setBounds(210, 90, 65, 30);

                    //---- label14 ----
                    label14.setText("7");
                    panel2.add(label14);
                    label14.setBounds(215, 20, 65, 30);

                    //---- label15 ----
                    label15.setText("Gaussian 2D");
                    panel2.add(label15);
                    label15.setBounds(215, 50, 85, 30);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panel2.getComponentCount(); i++) {
                            Rectangle bounds = panel2.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panel2.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panel2.setMinimumSize(preferredSize);
                        panel2.setPreferredSize(preferredSize);
                    }
                }
                tabbedPane1.addTab("Render", panel2);
            }
            dialogPane.add(tabbedPane1);
            tabbedPane1.setBounds(10, 10, 554, 419);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < dialogPane.getComponentCount(); i++) {
                    Rectangle bounds = dialogPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = dialogPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                dialogPane.setMinimumSize(preferredSize);
                dialogPane.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(dialogPane);
        dialogPane.setBounds(0, 0, 578, 443);

        contentPane.setPreferredSize(new Dimension(600, 490));
        setSize(600, 490);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    //    public static void main(String[] args) {
//        new NanoStitcher_Configurator().setVisible(true);
//    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField jTextField_LocDataset;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JTextField textField_Len;
    private JTextField textField_Wid;
    private JLabel label6;
    private JTextField textField_Edge;
    private JLabel label7;
    private JTextField jTextField_Loc_Offset_Storage;
    private JLabel label8;
    private JTextField jTextField_LocDataRes;
    private JButton jButton_Stitching;
    private JButton jButton_dataset;
    private JButton jButton_offset_storage;
    private JButton jButton_stitching_res;
    private JPanel panel2;
    private JLabel label9;
    private JLabel label10;
    private JTextField jTextField_LocDataPath;
    private JTextField jTextField_LocDataDir;
    private JButton jButton_LoadLocfile;
    private JButton jButton_LoadLocDir;
    private JButton jButton_Render;
    private JLabel label1;
    private JLabel label2;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JTextField jTextField_SNR_th;
    private JTextField jTextField_RenderPixelSize;
    private JTextField jTextField_RawPixelSize;
    private JLabel label14;
    private JLabel label15;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
