package ncr.res.mobilepos.simpleprinterdriver.priflexpro;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.simpleprinterdriver.BmpInfo;

/**
 * {@link PriFlexProBTPrinter} Class encapsulates the conversion of BitMap image
 * to NV Image Data.
 * @author wc250040
 *
 */
public class PriFlexProBTPrinter {

    /**
     * The paths of image files.
     */
    private List<String> imgPaths;
    /**
     * Total defined NV bit image of PriFlexProBTPrinter is 192k bytes.
     */
    private static final int NV_IMG_MAX_LEN = 192000; //192 * 1000
    /** The Trace Printer. */
    private Trace.Printer tp;
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "PriFlexProBTPrinter";
    /**
     * Constructor for multiple images.
     * @param paths The Set of Path for the BitMap images.
     */
    public PriFlexProBTPrinter(final List<String> paths) {
        this.imgPaths = paths;
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
    }
    /**
     * Constructor for only one image.
     * @param path  The Path of the Bitmap Image.
     */
    public PriFlexProBTPrinter(final String path) {
        this.imgPaths = new ArrayList<String>();
        imgPaths.add(path);
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
    }

    /**
     * Get the defined NV Image Data.
     * @return  The byte of NV Image Data.
     * @throws IOException  The Exception thrown when error occur.
     */
    public final synchronized byte[] getDefinedNVImgData() throws IOException {
        tp.methodEnter("getDefinedNVImgData");
        byte[] printingData = null;
        try {
            byte[] nvImgCMD = {0x1c, 0x71, (byte) imgPaths.size()};
            List<byte[]> nvImgDatas = new ArrayList<byte[]>();
            int printingDataSize = 0;
            for (String path : imgPaths) {
                // Get binary data of image.
                byte[] imgRawData = getBmpResourceData(path);
                if (imgRawData == null || imgRawData.length == 0) {
                    tp.println("There are no image found.");
                    return new byte[0];
                }
                BmpInfo bmpInfo = new BmpInfo(imgRawData, 1);
                // Check the image is bitmap or not.
                if (!bmpInfo.checkBitmap()) {
                    tp.println("The image Logo is not in BitMap format.");
                    return new byte[0];
                }
                // Initialize the bitmap data.
                if (!bmpInfo.init()) {
                    tp.println("The bitmap "
                            + "image logo was not initialize properly.");
                    return new byte[0];
                }
                // Get the NV bit image data.
                byte[] nvImgData = bmpInfo.getNVImagePriFlexPro();
                nvImgDatas.add(nvImgData);
                printingDataSize += nvImgData.length + nvImgCMD.length;
                if (printingDataSize > NV_IMG_MAX_LEN) {
                    tp.println("The NV image file does not conform with the "
                            + "printing data size.");
                    return new byte[0];
                }
            }
            printingData = new byte[printingDataSize];
            int index = 0;
            for (int i = 0; i < nvImgDatas.size(); i++) {
                byte[] buffer = nvImgDatas.get(i);
                nvImgCMD[2] = (byte)(i + 1);
                System.arraycopy(nvImgCMD, 0, printingData, index, nvImgCMD.length);
                index += nvImgCMD.length;
                System.arraycopy(buffer, 0, printingData, index, buffer.length);
                index += buffer.length;
            }
        } catch (IOException e) {
        	LOGGER.logAlert(PROG_NAME, PROG_NAME+".getDefinedNVImgData", Logger.RES_EXCEP_IO, "Failed to get image. "+e.getMessage());
           throw e;
        } finally {
            tp.methodExit();
        }
        return printingData;
    }

    /**
     * Get the binary data of image from the path.
     * @param path - the path of bitmap file.
     * @return byte array - binary data of bitmap.
     * @throws IOException - thrown when an error occurs
     * during Input/Output
     */
    private byte[] getBmpResourceData(final String path) throws IOException {
    	String functionName = PROG_NAME+"getBmpResourceData";
        // Check the parameter
        if (StringUtility.isNullOrEmpty(path)) {
            return new byte[0];
        }
        // Get image from local file system.
        InputStream is = null;
        ByteArrayOutputStream b = null;
        OutputStream os = null;
        try {
        	is = new FileInputStream(path);
        	b = new ByteArrayOutputStream();
        	os = new BufferedOutputStream(b);
                
        	int c;
            while (is != null && (c = is.read()) != -1) {
                os.write(c);
            }
            
        } catch (Exception e) {
        	LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL, "Failed to get bmp resource data. "+e.getMessage());
        } finally {
        	 try {
                 if (os != null) {
                     os.close();
                 }
                 if (is != null) {
                     is.close();
                 }
             } catch (Exception e) {
            	 LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL, "Failed to close resources. "+e.getMessage());
             } finally {
                 os = null;
                 is = null;
             }
        	
        	
        }
       
        return b.toByteArray();
    }
}
