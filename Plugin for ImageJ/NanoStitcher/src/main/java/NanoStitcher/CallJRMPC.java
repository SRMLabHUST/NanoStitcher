package NanoStitcher;

import CallJRMPC.Class1;
import com.mathworks.toolbox.javabuilder.*;

public class CallJRMPC {

    //    private static Method methodInstance;
    private static Class1 class1;

    private static void setup() throws MWException {
//        methodInstance = new Method();
        class1 = new Class1();
    }

    //    /**
//     * Sample code for {@link Method#matchMethod(int, Object...)}.
//     */
    public static void matchMethodExample(int Image_len, int Image_wid, int Edge, String DataDirPath, String movePath) {
        MWArray Image_lengthIn = null;
        MWArray Image_widthIn = null;
        MWArray EdgeSizeIn = null;
        MWArray pathIn = null;
        MWArray movepathIn = null;
        Object[] results = null;
        try {
            double Image_lengthInData = Image_len;
            Image_lengthIn = new MWNumericArray(Image_lengthInData, MWClassID.DOUBLE);
            double Image_widthInData = Image_wid;
            Image_widthIn = new MWNumericArray(Image_widthInData, MWClassID.DOUBLE);
            double EdgeSizeInData = Edge;
            EdgeSizeIn = new MWNumericArray(EdgeSizeInData, MWClassID.DOUBLE);
            String pathInData = DataDirPath;
            pathIn = new MWCharArray(pathInData);
            String movepathInData = movePath;
            movepathIn = new MWCharArray(movepathInData);
//            results = methodInstance.matchMethod(Image_lengthIn, Image_widthIn, EdgeSizeIn, pathIn, movepathIn);
            results = class1.matchMethod(Image_lengthIn, Image_widthIn, EdgeSizeIn, pathIn, movepathIn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Dispose of native resources
            MWArray.disposeArray(Image_lengthIn);
            MWArray.disposeArray(Image_widthIn);
            MWArray.disposeArray(EdgeSizeIn);
            MWArray.disposeArray(pathIn);
            MWArray.disposeArray(movepathIn);
            MWArray.disposeArray(results);
        }
    }

    public static void begin(int Image_len, int Image_wid, int Edge, String DataDirPath, String movePath) {
        try {
            setup();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            matchMethodExample(Image_len, Image_wid, Edge, DataDirPath, movePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            // Dispose of native resources
//            methodInstance.dispose();
            class1.dispose();
        }
    }

}
