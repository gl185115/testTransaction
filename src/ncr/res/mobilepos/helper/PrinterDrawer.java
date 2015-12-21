/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import ncr.res.mobilepos.simpleprinterdriver.IPrinter;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterIF;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;
import ncr.res.mobilepos.simpleprinterdriver.UsbPrinterIF;

/**
 * @author Administrator
 *
 */
public class PrinterDrawer {
    /**
     * The Command of opening Drawer.
     */
    private static final byte[] DRAWER_OPEN_COMMAND =
        {(byte) 0x1b, (byte) 0x70, (byte) 0x00,
        (byte) 0x05, (byte) 0x00, (byte) 0x05};

    /**
     * class instance of the printer name.
     */
    private String printerName;
    /**
     * class instance of the net printer if.
     */
    private IPrinter netPrinter;
    /**
     * class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    /**
     * Constructor.
     * @param printerNameToSet - printer name
     */
    public PrinterDrawer(final String printerNameToSet) {
        this.printerName = printerNameToSet;
    }

    /**
     * Helper function that would get the printer service.
     * @return printerService
     */
    private PrintService getPrinterService() {
    	PrintService printService = null;
    	
        if (printerName == null || printerName.isEmpty()) {
        	// do nothing
        } else {        
	        PrintService[] printServices =
	            PrintServiceLookup.lookupPrintServices(null, null);	        
	        for (int i = 0; i < printServices.length; i++) {
	            if (printServices[i].getName().equals(printerName)) {
	                printService = printServices[i];
	                break;
	            }
	        }
        } 
        
        if (printService == null) {
            LOGGER.logAlert("Drawer", "PrinrterDrawer.getPrinterService",
                    Logger.RES_EXCEP_GENERAL,
                    "Can not get Printer Service using"
                    + " the gived printer name.");
        }
        
        return printService;
    }
    
    /**
     * Constructor.
     * @param netPrinterInfo - instance of NetPrinterInfo to
     * be used to initialize the NetPrinterIF
     */
    public PrinterDrawer(final NetPrinterInfo netPrinterInfo) {
        if (StringUtility.isIPformat(netPrinterInfo.getUrl())) {
            this.netPrinter = new NetPrinterIF(netPrinterInfo);
        } else {
            this.netPrinter = new UsbPrinterIF(netPrinterInfo.getUrl());
        }
    }

    /**
     * Open the printer drawer.
     * @return true if successful, false if not
     */
    public final boolean openPrinterDrawer() {
    	boolean result = true;
        LOGGER.logFunctionEntry("Drawer", "PrinrterDrawer.openPrinterDrawer",
                "Thread" + this.hashCode(), "PrinterName" + printerName);

        InputStream is = new ByteArrayInputStream(DRAWER_OPEN_COMMAND);
        
        PrintService printService = getPrinterService();
        if (printService == null) {
        	return false;
        }
        
        DocPrintJob job = printService.createPrintJob();
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

        Doc doc = new SimpleDoc(is, flavor, null);
        try {
            job.print(doc, null);
            is.close();
        } catch (PrintException e) {
            LOGGER.logAlert("Drawer", "PrinrterDrawer.openPrinterDrawer",
                    Logger.RES_EXCEP_GENERAL,
                    "PrintException:" + e.getMessage());
            result = false;
        } catch (IOException e) {
            LOGGER.logAlert("Drawer", "PrinrterDrawer.openPrinterDrawer",
                    Logger.RES_EXCEP_GENERAL,
                    "IoException:" + e.getMessage());
            result = false;
        }

        return result;
    }

    /**
     * Open Drawer without Windows Driver.
     * @return true if successful, false if not
     */
    public final boolean openDrawer() {
        //connect to printer
        if (!netPrinter.connectPrinter()) {
            netPrinter.close();
            return false;
        }

        if (!netPrinter.clearBuffer()) {
            netPrinter.close();
            return false;
        }

        if (!netPrinter.openDrawer()) {
            netPrinter.close();
            return false;
        }

        netPrinter.close();
        return true;
    }
}
