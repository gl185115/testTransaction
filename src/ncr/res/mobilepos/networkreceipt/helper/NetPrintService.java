package ncr.res.mobilepos.networkreceipt.helper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ncr.res.mobilepos.helper.Logger;

public class NetPrintService {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    
    private String progName;
    
    /**
     * Failed to connect to network printer.
     */
    public static final String NWPRNT_ERR_CONNECT = "41";
    public static final String NWPRNT_ERRMSG_CONNECT = "Failed to connect to network printer.";
    /**
     * Failed to clear printer buffer.
     */
    public static final String NWPRNT_ERR_CLEAR = "42";
    public static final String NWPRNT_ERRMSG_CLEAR = "Failed to clear printer buffer.";
    /**
     * Failed to initialize the printer.
     */
    public static final String NWPRNT_ERR_INIT = "43";
    public static final String NWPRNT_ERRMSG_INIT = "Failed to initialize the printer.";
    /**
     * Failed to write bitmap.
     */
    public static final String NWPRNT_ERR_BITMAP = "44";
    public static final String NWPRNT_ERRMSG_BITMAP = "Failed to convert bitmap format.";
    /**
     * Failed to write text.
     */
    public static final String NWPRNT_ERR_TEXT = "45";
    public static final String NWPRNT_ERRMSG_TEXT = "Failed to write binary data via socket.";
    /**
     * Failed to cut paper.
     */
    public static final String NWPRNT_ERR_CUT = "46";
    public static final String NWPRNT_ERRMSG_CUT = "Failed to cut paper.";
    /**
     * Failed to print.
     */
    public static final String NWPRNT_ERR_PRINT = "47";
    /**
     * Error message when error number is 1.
     */
    public static final String NWPRNT_ERR_PRINT_PAPER = "51";
    public static final String NWPRNT_ERRMSG_PRINT_PAPER = "Receipt Paper Exhausted.";
    /**
     * Error message when error number is 2.
     */
    public static final String NWPRNT_ERR_PRINT_COVER = "52";
    public static final String NWPRNT_ERRMSG_PRINT_COVER = "Cover Opened.";
    /**
     * Error message when error number is 3.
     */
    public static final String NWPRNT_ERR_PRINT_STOP_PRINT = "53";
    public static final String NWPRNT_ERRMSG_PRINT_STOP_PRINT =
            "Printing stopped due to paper condition.";
    /**
     * Error message when error number is 4.
     */
    public static final String NWPRNT_ERR_PRINT_ERR_COMDITION = "54";
    public static final String NWPRNT_ERRMSG_PRINT_ERR_COMDITION =
        "Error condition exists in the printer.";
    /**
     * Error message when error number is 5.
     */
    public static final String NWPRNT_ERR_PRINT_KNIFE = "55";
    public static final String NWPRNT_ERRMSG_PRINT_KNIFE = "Knife error occurred.";
    /**
     * Error message when error number is 6.
     */
    public static final String NWPRNT_ERR_PRINT_UNRECOVERABLE = "56";
    public static final String NWPRNT_ERRMSG_PRINT_UNRECOVERABLE =
        "Unrecoverable error occurred.";
    /**
     * Error message when error number is 7.
     */
    public static final String NWPRNT_ERR_PRINT_POWER = "57";
    public static final String NWPRNT_ERRMSG_PRINT_POWER =
            "Thermal print head temp./power supply voltage are out of range.";

    public NetPrintService(String progName) {
        this.progName = progName;
    }
    /**
     * Log error to IOW.
     */
    public void logErr(String funcName, String code, String errMsg) {
        LOGGER.logAlert(progName, funcName, code, errMsg);
    }
    /**
     * Log error to IOW when print receipt.
     */
    public void logPrintErr(String funcName, int errNum) {
        switch (errNum) {
        case 1:
            LOGGER.logAlert(funcName, funcName, NWPRNT_ERR_PRINT_PAPER, NWPRNT_ERRMSG_PRINT_PAPER);
            break;
        case 2:
            LOGGER.logAlert(funcName, funcName, NWPRNT_ERR_PRINT_COVER, NWPRNT_ERRMSG_PRINT_COVER);
            break;
        case 3:
            LOGGER.logAlert(funcName, funcName, NWPRNT_ERR_PRINT_STOP_PRINT, NWPRNT_ERRMSG_PRINT_STOP_PRINT);
            break;
        case 4:
            LOGGER.logAlert(funcName, funcName, NWPRNT_ERR_PRINT_ERR_COMDITION, NWPRNT_ERRMSG_PRINT_ERR_COMDITION);
            break;
        case 5:
            LOGGER.logAlert(funcName, funcName, NWPRNT_ERR_PRINT_KNIFE, NWPRNT_ERRMSG_PRINT_KNIFE);
            break;
        case 6:
            LOGGER.logAlert(funcName, funcName, NWPRNT_ERR_PRINT_UNRECOVERABLE, NWPRNT_ERRMSG_PRINT_UNRECOVERABLE);
            break;
        case 7:
            LOGGER.logAlert(funcName, funcName, NWPRNT_ERR_PRINT_POWER, NWPRNT_ERRMSG_PRINT_POWER);
            break;
        default:{
        	//perform default operations here if available 
        	}
        }
    }
    /**
     * get the image from the path.
     * @param path - path to bmp resource
     * @return byte array - the byte array equivalent of
     * the image
     * @throws IOException - thrown when an error occurs
     * during Input/Output
     */
	public byte[] getBmpLocalFileData(final String path) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		if (path == null || "".equals(path)) {
			return new byte[0];
		}
		File file = new File(path);
		if (!file.exists()) {
			return new byte[0];
		}
		if (!file.getName().toLowerCase().contains(".bmp")) {
			return new byte[0];
		}
		try {
			InputStream is = new FileInputStream(file);

			OutputStream os = new BufferedOutputStream(b);
			int c;
			while (is != null && (c = is.read()) != -1) {
				os.write(c);
			}
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		} catch (FileNotFoundException e) {
			LOGGER.logAlert(progName, Logger.RES_EXCEP_FILENOTFOUND,
					"getBmpLocalFileData: Failed to get file in " + path, e);
			return new byte[0];
		} catch (IOException e) {
			LOGGER.logAlert(progName, Logger.RES_EXCEP_IO,
					"getBmpLocalFileData: Failed to get file in " + path, e);
			return new byte[0];
		} catch (Exception e) {
			LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL,
					"getBmpLocalFileData: Failed to get file in " + path, e);
			return new byte[0];
		}
		return b.toByteArray();
	}
}
