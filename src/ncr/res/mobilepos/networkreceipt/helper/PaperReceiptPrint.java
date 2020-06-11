package ncr.res.mobilepos.networkreceipt.helper;

/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2015.01.20      FENGSHA       80mmの紙変更を対応,字体圧縮しない
 * 1.02            2015.02.10      FENGSHA       領収書レシートの80mm紙変更対応
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.simpleprinterdriver.IPrinter;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterIF;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;
import ncr.res.mobilepos.simpleprinterdriver.UsbPrinterIF;

/**
 * class that handles paper receipt printing.
 *
 */
public class PaperReceiptPrint extends NetPrintService {
    /**
     * class instance of the text to print.
     */
    private List<byte[]> receiptText;
    private IPrinter printer;
    /**
     * class instance of the path to doc tax stamp.
     */
    private String docTaxStampPath;
    /**
     * class instance of the path of logo file.
     */
    private String logoPath;

    //1.02 2015.02.10 FENGSHA ADD START
    /**
     * class instance of the path to sign bmp path.
     */
    private String MiscBmpPath;
  //1.02 2015.02.10 FENGSHA ADD END

    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private String storeId;

    private String terminalId;

    private String txId;

    private boolean haveDocTaxStamp;

    private int angle = 1;

    /**
     * the class instance of the debug trace printer.
     */
    private Trace.Printer tp;


    public static final byte[] LOGOBYTES = new byte[] { 108, 111, 103, 111 };
    public static final byte[] OTHERBYTES = new byte[] { 111, 116, 104, 101 };
    public static final byte[] SIGNBYTES = new byte[] {83,105,103,110};
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "PprRcpt";
    /**
     * constructor.
     *
     * @param receiptLines - the List of byte arrays of the text to print
     * @param servletContext - instance of the servlet context
     * @param printerInfo - instance of NetPrinterInfor to initialize the
     *            NetPrinterIF
     * @param docTaxStampPathToSet - the path to the doc tax stamp
     */
    public PaperReceiptPrint(final List<byte[]> receiptLines,
            final ServletContext servletContext,
            final NetPrinterInfo printerInfo, final String docTaxStampPathToSet) {
        super(PROG_NAME);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.receiptText = receiptLines;
        new NetPrinterIF(printerInfo);
        this.docTaxStampPath = docTaxStampPathToSet;
    }

    public PaperReceiptPrint(final List<byte[]> receiptLines,
            final NetPrinterInfo printerInfo, final String logoPath,
            final String docTaxStampPath, final boolean haveDocTax,
            final String storeid, final String terminalid, final String txid) {
        super(PROG_NAME);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.receiptText = receiptLines;
        if (StringUtility.isIPformat(printerInfo.getUrl())) {
            this.printer = new NetPrinterIF(printerInfo);
        } else {
            this.printer = new UsbPrinterIF(printerInfo.getUrl());
        }
        this.logoPath = logoPath;
        this.docTaxStampPath = docTaxStampPath;
        this.haveDocTaxStamp = haveDocTax;
        this.storeId = storeid;
        this.terminalId = terminalid;
        this.txId = txid;
    }

    public PaperReceiptPrint(final List<byte[]> receiptLines,
            final NetPrinterInfo printerInfo, final String logoPath,
            final String docTaxStampPath, final boolean haveDocTax,
            final String MiscBmpPath,
            final String direction, final String storeid,
            final String terminalid, final String txid) {
        super(PROG_NAME);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.receiptText = receiptLines;
        if (StringUtility.isIPformat(printerInfo.getUrl())) {
            this.printer = new NetPrinterIF(printerInfo);
        } else {
            this.printer = new UsbPrinterIF(printerInfo.getUrl());
        }
        this.logoPath = logoPath;
        this.docTaxStampPath = docTaxStampPath;
        this.haveDocTaxStamp = haveDocTax;
        this.MiscBmpPath = MiscBmpPath;
        this.setAngle(direction);
        this.storeId = storeid;
        this.terminalId = terminalid;
        this.txId = txid;
    }
    /**
     * print the receipt.
     *
     * @return result code of the operation
     * @throws IOException - thrown when there is an error in Input/Output
     */
    synchronized public final int printReceipt() throws IOException {

        int result = ResultBase.RESNETRECPT_OK;

        byte[] logoBMP = getBmpLocalFileData(logoPath);
        byte[] docTaxStampBMP = null;
        //1.02 2015.02.10 FENGSHA ADD START
        byte[] signBMP = getBmpLocalFileData(MiscBmpPath);
        //1.02 2015.02.10 FENGSHA ADD END
        //1.01 2015/01/20 FENGSHA ADD START
        String printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            printPaperLength = (String) env.lookup("Printpaperlength");
        } catch (NamingException e) {
            printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        }
        //1.01 2015/01/20 FENGSHA ADD END
        if (haveDocTaxStamp) {
            docTaxStampBMP = getBmpLocalFileData(docTaxStampPath);
        }
        // connect to printer
        if (!printer.connectPrinter()) {
            printer.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printReceipt", NWPRNT_ERR_CONNECT, NWPRNT_ERRMSG_CONNECT);
            return result;
        }

        // Clear the Buffer of the Printer
        if (!printer.clearBuffer()) {
            printer.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printReceipt", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
            return result;
        }

        // initialize the printer
        if (!printer.initPrinter()) {
            printer.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printReceipt", NWPRNT_ERR_INIT, NWPRNT_ERRMSG_INIT);
            return result;
        }

        // print logo image
        if (logoBMP != null) {
          //1.01 2015/01/20 FENGSHA MOD START
            boolean writeBmpReturn = false;
            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                writeBmpReturn =  printer.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER53);
            } else{
                writeBmpReturn =  printer.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER80);
            }
           // if (!printer.writeBmp(logoBMP, 1, 1)) {
            if(!writeBmpReturn){
          //1.01 2015/01/20 FENGSHA MOD END
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NG;
                logErr("printReceipt", NWPRNT_ERR_BITMAP, NWPRNT_ERRMSG_BITMAP);
                return result;
            }
        } else {
            LOGGER.logAlert(PROG_NAME, "PaperReceiptPrint.printReceipt()",
                    Logger.RES_EXCEP_FILENOTFOUND, "Failed to get Logo file.\n"
                            + "StoreId = " + storeId + "; TerminalId = "
                            + terminalId + "; TXID = " + txId);
        }

        // print receipt header
        printer.writeText(receiptText.get(0));
        // print receipt header
        printer.writeText(receiptText.get(1));

        // print doc tax stamp
        if (haveDocTaxStamp) {
            if (docTaxStampBMP != null) {
              //1.01 2015/01/20 FENGSHA MOD START
                boolean writeBmpReturn = false;
                if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                    writeBmpReturn =  printer.writeBmp(docTaxStampBMP, 1, IPrinter.FLG_PAPER53);
                } else{
                    writeBmpReturn =  printer.writeBmp(docTaxStampBMP, 1, IPrinter.FLG_PAPER80);
                }
                if(!writeBmpReturn){
                //if (!printer.writeBmp(docTaxStampBMP, 1, 1)) {
              //1.01 2015/01/20 FENGSHA MOD END
                    printer.close();
                    result = ResultBase.RESNETRECPT_ERROR_NG;
                    logErr("printReceipt", NWPRNT_ERR_BITMAP,
                            NWPRNT_ERRMSG_BITMAP);
                    return result;
                }
            } else {
                LOGGER.logAlert(PROG_NAME, "PaperReceiptPrint.printReceipt()",
                        Logger.RES_EXCEP_FILENOTFOUND,
                        "Failed to get doc tax stamp file.\n" + "StoreId = "
                                + storeId + "; TerminalId = " + terminalId
                                + "; TXID = " + txId);
            }
        }

        //1.02 2015.02.10 FENGSHA ADD START
        if(signBMP != null){
            boolean writeBmpReturn = false;
            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                writeBmpReturn =  printer.writeBmp(signBMP, 1, IPrinter.FLG_PAPER53);
            } else{
                writeBmpReturn =  printer.writeBmp(signBMP, 1, IPrinter.FLG_PAPER80);
            }
            if(!writeBmpReturn){
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NG;
                logErr("printReceipt", NWPRNT_ERR_BITMAP,
                        NWPRNT_ERRMSG_BITMAP);
                return result;
        }
        }else {
            LOGGER.logAlert(PROG_NAME, "PaperReceiptPrint.printReceipt()",
                    Logger.RES_EXCEP_FILENOTFOUND,
                    "Failed to get sign bmp file.\n" + "StoreId = "
                            + storeId + "; TerminalId = " + terminalId
                            + "; TXID = " + txId);
        }
        //1.02 2015.02.10 FENGSHA ADD END
        // print receipt credit payment
        if (receiptText.get(2) != null) {
            printer.writeText(receiptText.get(2));
        }

        // print receipt footer
        printer.writeText(receiptText.get(3));

        //Add paper-cut command to print data.
        printer.writeText(IPrinter.CUT_CMD);

        int printResult = printer.print();
        if (printResult != 0) {
            printer.close();
            if (printResult == 2) {
                result = ResultBase.RESNETRECPT_ERROR_COVER_OPEN;
            } else if (printResult == 1) {
                result = ResultBase.RESNETRECPT_ERROR_PAPER_EXHAUSTED;
            } else if (printResult == 99) {
                logErr("printReceipt", NWPRNT_ERR_TEXT, NWPRNT_ERRMSG_TEXT);
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
            } else {
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
            }
            logPrintErr("printReceipt", printResult);
            return result;
        }

        // cut paper
        /*if (!printer.cutPaper()) {
            printer.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printReceipt", NWPRNT_ERR_CUT, NWPRNT_ERRMSG_CUT);
            return result;
        }
         */
        printer.close();

        return result;
    }

    /**
     * execute receipt print.
     *
     * @param list receipt list
     * @return result code of the operation
     * @throws Exception
     */
    synchronized public final int printAllReceipt(List<List<byte[]>> receipts)
            throws Exception {
        int result = ResultBase.RESNETRECPT_OK;
        String functionName = "printAllReceipt";
        tp.methodEnter(functionName);
        try {
            byte[] logoBMP = getBmpLocalFileData(logoPath);
            byte[] docTaxStampBMP = null;
            byte[] miscBmpPath = getBmpLocalFileData(MiscBmpPath);
            tp.println("begin decide paper length");
            String printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                printPaperLength = (String) env.lookup("Printpaperlength");
            } catch (NamingException e) {
                printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
            }
            tp.println("end decide paper length");
            if (haveDocTaxStamp) {
            	tp.println("begin get Bmp local filedata");
                docTaxStampBMP = getBmpLocalFileData(docTaxStampPath);
                tp.println("end get Bmp local filedata");
            }
            tp.println("begin connect to printer");
            if (!printer.connectPrinter()) {
            	tp.println("failed connect to printer");
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                logErr("printAllReceipt", NWPRNT_ERR_CONNECT, NWPRNT_ERRMSG_CONNECT);
                return tp.methodExit(result);
            }
            tp.println("end connect to printer");

            tp.println("begin clear the buffer of printer");
            if (!printer.clearBuffer()) {
            	tp.println("failed clear the buffer of printer");
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                logErr("printAllReceipt", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
                return tp.methodExit(result);
            }
            tp.println("end clear the buffer of printer");

            tp.println("begin initialize the printer");
            if (!printer.initPrinter()) {
            	tp.println("failed initialize the printer");
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                logErr("printAllReceipt", NWPRNT_ERR_INIT, NWPRNT_ERRMSG_INIT);
                return tp.methodExit(result);
            }
            tp.println("end initialize the printer");

            for (List<byte[]> reList : receipts) {

            	tp.println("begin clear the buffer of the printer");
                if (!printer.clearBuffer()) {
                	tp.println("failed clear the buffer of the printer");
                    printer.close();
                    result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                    logErr("printAllReceipt", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
                    return tp.methodExit(result);
                }
                tp.println("end clear the buffer of the printer");

                for (byte[] line : reList) {
                    if (line != null && Arrays.equals(LOGOBYTES, line)) {
                        if (logoBMP != null) {
                        	tp.println("begin get logoBmp bitmap for printer");
                            boolean writeBmpReturn = false;
                            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                                writeBmpReturn =  printer.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER53);
                            } else{
                                writeBmpReturn =  printer.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER80);
                            }
                            if(!writeBmpReturn){
                            	tp.println("failed get logoBmp bitmap for printer");
                                printer.close();
                                result = ResultBase.RESNETRECPT_ERROR_NG;
                                logErr("printAllReceipt", NWPRNT_ERR_BITMAP,
                                        NWPRNT_ERRMSG_BITMAP);
                                return tp.methodExit(result);
                            }
                         tp.println("end get logoBmp bitmap for printer");
                        } else {
                            LOGGER.logAlert(PROG_NAME,
                                    "PaperReceiptPrint.printAllReceipt()",
                                    Logger.RES_EXCEP_FILENOTFOUND,
                                    "Failed to get Logo file.\n" + "StoreId = "
                                            + storeId + "; TerminalId = "
                                            + terminalId + "; TXID = " + txId);
                        }
                    } else if (line != null && Arrays.equals(OTHERBYTES, line)) {
                        if (docTaxStampBMP != null) {
                        	tp.println("begin get docTaxStampBMP bitmap for printer");
                            boolean writeBmpReturn = false;
                            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                                writeBmpReturn =  printer.writeBmp(docTaxStampBMP, this.getAngle(), IPrinter.FLG_PAPER53);
                            } else{
                                writeBmpReturn =  printer.writeBmp(docTaxStampBMP, this.getAngle(), IPrinter.FLG_PAPER80);
                            }
                            if(!writeBmpReturn){
                            	tp.println("failed get docTaxStampBMP bitmap for printer");
                                printer.close();
                                result = ResultBase.RESNETRECPT_ERROR_NG;
                                logErr("printReceipt", NWPRNT_ERR_BITMAP,
                                        NWPRNT_ERRMSG_BITMAP);
                                return tp.methodExit(result);
                            }
                         tp.println("end get docTaxStampBMP bitmap for printer");
                        } else if (!haveDocTaxStamp) {

                        } else {
                            LOGGER.logAlert(PROG_NAME,
                                    "PaperReceiptPrint.printReceipt()",
                                    Logger.RES_EXCEP_FILENOTFOUND,
                                    "Failed to get doc tax stamp file.\n"
                                            + "StoreId = " + storeId
                                            + "; TerminalId = " + terminalId
                                            + "; TXID = " + txId);
                        }
                    }
                    else if (line != null && Arrays.equals(SIGNBYTES, line)){
                        if(miscBmpPath != null){
                        	tp.println("begin get miscBmpPath bitmap for printer");
                            boolean writeBmpReturn = false;
                            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                                writeBmpReturn =  printer.writeBmp(miscBmpPath, this.getAngle(), IPrinter.FLG_PAPER53);
                            } else{
                                writeBmpReturn =  printer.writeBmp(miscBmpPath, this.getAngle(), IPrinter.FLG_PAPER80);
                            }
                            if(!writeBmpReturn){
                            	    tp.println("failed get miscBmpPath bitmap for printer");
                                    printer.close();
                                    result = ResultBase.RESNETRECPT_ERROR_NG;
                                    logErr("printReceipt", NWPRNT_ERR_BITMAP,
                                            NWPRNT_ERRMSG_BITMAP);
                                    return tp.methodExit(result);
                                }
                         tp.println("end get miscBmpPath bitmap for printer");
                        }else{
                            LOGGER.logAlert(PROG_NAME,
                                    "PaperReceiptPrint.printReceipt()",
                                    Logger.RES_EXCEP_FILENOTFOUND,
                                    "Failed to get sign bmp file.\n"
                                            + "StoreId = " + storeId
                                            + "; TerminalId = " + terminalId
                                            + "; TXID = " + txId);
                        }
                    }
                    else {
                        printer.writeText(line);
                    }
                }

                //Add paper-cut command to print data.
                printer.writeText(IPrinter.CUT_CMD);

                tp.println("begin send print data to printer");
                int printResult = printer.print();
                if (printResult != 0) {
                	tp.println("failed send print data to printer");
                    printer.close();
                    if (printResult == 2) {
                        result = ResultBase.RESNETRECPT_ERROR_COVER_OPEN;
                    } else if (printResult == 1) {
                        result = ResultBase.RESNETRECPT_ERROR_PAPER_EXHAUSTED;
                    } else if (printResult == 99) {
                        logErr("printReceipt", NWPRNT_ERR_TEXT, NWPRNT_ERRMSG_TEXT);
                        result = ResultBase.RESNETRECPT_ERROR_OTHER;
                    } else {
                        result = ResultBase.RESNETRECPT_ERROR_OTHER;
                    }
                    logPrintErr("printAllReceipt", printResult);
                    return tp.methodExit(result);
                }
                tp.println("end send print data to printer");

                /*tp.println("begin cut paper");
                if (!printer.cutPaper()) {
                	tp.println("failed cut paper");
                    printer.close();
                    result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                    logErr("printAllReceipt", NWPRNT_ERR_CUT, NWPRNT_ERRMSG_CUT);
                    return tp.methodExit(result);
                }
                tp.println("end cut paper");*/
            }

            printer.close();
        } catch (Exception e) {
            printer.close();
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    "printAllReceipt: Failed to print receipt.", e);
            throw e;
        }

        return tp.methodExit(result);
    }

    /**
     * execute receipt print.
     *
     * @param list receipt list
     * @param boolean papercut to include papercut command
     * @return result code of the operation
     * @throws Exception
     */
    synchronized public final int printAllReceipt(List<List<byte[]>> receipts, boolean papercut)
            throws Exception {
        int result = ResultBase.RESNETRECPT_OK;
        int icount = 0;
        String functionName = "printAllReceipt";
        tp.methodEnter(functionName);
        try {
            byte[] logoBMP = getBmpLocalFileData(logoPath);
            byte[] docTaxStampBMP = null;
            byte[] miscBmpPath = getBmpLocalFileData(MiscBmpPath);
            tp.println("begin decide paper length");
            String printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
            try {
                Context env = (Context) new InitialContext().lookup("java:comp/env");
                printPaperLength = (String) env.lookup("Printpaperlength");
            } catch (NamingException e) {
                printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
            }
            tp.println("end decide paper length");
            if (haveDocTaxStamp) {
            	tp.println("begin get Bmp local filedata");
                docTaxStampBMP = getBmpLocalFileData(docTaxStampPath);
                tp.println("end get Bmp local filedata");
            }
            tp.println("begin connect to printer");
            if (!printer.connectPrinter()) {
            	tp.println("failed connect to printer");
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                logErr("printAllReceipt", NWPRNT_ERR_CONNECT, NWPRNT_ERRMSG_CONNECT);
                return tp.methodExit(result);
            }
            tp.println("end connect to printer");

            tp.println("begin clear the buffer of printer");
            if (!printer.clearBuffer()) {
            	tp.println("failed clear the buffer of printer");
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                logErr("printAllReceipt", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
                return tp.methodExit(result);
            }
            tp.println("end clear the buffer of printer");

            tp.println("begin initialize the printer");
            if (!printer.initPrinter()) {
            	tp.println("failed initialize the printer");
                printer.close();
                result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                logErr("printAllReceipt", NWPRNT_ERR_INIT, NWPRNT_ERRMSG_INIT);
                return tp.methodExit(result);
            }
            tp.println("end initialize the printer");

            for (List<byte[]> reList : receipts) {

            	tp.println("begin clear the buffer of the printer");
                if (!printer.clearBuffer()) {
                	tp.println("failed clear the buffer of the printer");
                    printer.close();
                    result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                    logErr("printAllReceipt", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
                    return tp.methodExit(result);
                }
                tp.println("end clear the buffer of the printer");

                for (byte[] line : reList) {
                    if (line != null && Arrays.equals(LOGOBYTES, line)) {
                        if (logoBMP != null) {
                        	tp.println("begin get logoBmp bitmap for printer");
                            boolean writeBmpReturn = false;
                            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                                writeBmpReturn =  printer.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER53);
                            } else{
                                writeBmpReturn =  printer.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER80);
                            }
                            if(!writeBmpReturn){
                            	tp.println("failed get logoBmp bitmap for printer");
                                printer.close();
                                result = ResultBase.RESNETRECPT_ERROR_NG;
                                logErr("printAllReceipt", NWPRNT_ERR_BITMAP,
                                        NWPRNT_ERRMSG_BITMAP);
                                return tp.methodExit(result);
                            }
                         tp.println("end get logoBmp bitmap for printer");
                        } else {
                            LOGGER.logAlert(PROG_NAME,
                                    "PaperReceiptPrint.printAllReceipt()",
                                    Logger.RES_EXCEP_FILENOTFOUND,
                                    "Failed to get Logo file.\n" + "StoreId = "
                                            + storeId + "; TerminalId = "
                                            + terminalId + "; TXID = " + txId);
                        }
                    } else if (line != null && Arrays.equals(OTHERBYTES, line)) {
                        if (docTaxStampBMP != null) {
                        	tp.println("begin get docTaxStampBMP bitmap for printer");
                            boolean writeBmpReturn = false;
                            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                                writeBmpReturn =  printer.writeBmp(docTaxStampBMP, this.getAngle(), IPrinter.FLG_PAPER53);
                            } else{
                                writeBmpReturn =  printer.writeBmp(docTaxStampBMP, this.getAngle(), IPrinter.FLG_PAPER80);
                            }
                            if(!writeBmpReturn){
                            	tp.println("failed get docTaxStampBMP bitmap for printer");
                                printer.close();
                                result = ResultBase.RESNETRECPT_ERROR_NG;
                                logErr("printReceipt", NWPRNT_ERR_BITMAP,
                                        NWPRNT_ERRMSG_BITMAP);
                                return tp.methodExit(result);
                            }
                         tp.println("end get docTaxStampBMP bitmap for printer");
                        } else if (!haveDocTaxStamp) {

                        } else {
                            LOGGER.logAlert(PROG_NAME,
                                    "PaperReceiptPrint.printReceipt()",
                                    Logger.RES_EXCEP_FILENOTFOUND,
                                    "Failed to get doc tax stamp file.\n"
                                            + "StoreId = " + storeId
                                            + "; TerminalId = " + terminalId
                                            + "; TXID = " + txId);
                        }
                    }
                    else if (line != null && Arrays.equals(SIGNBYTES, line)){
                        if(miscBmpPath != null){
                        	tp.println("begin get miscBmpPath bitmap for printer");
                            boolean writeBmpReturn = false;
                            if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
                                writeBmpReturn =  printer.writeBmp(miscBmpPath, this.getAngle(), IPrinter.FLG_PAPER53);
                            } else{
                                writeBmpReturn =  printer.writeBmp(miscBmpPath, this.getAngle(), IPrinter.FLG_PAPER80);
                            }
                            if(!writeBmpReturn){
                            	    tp.println("failed get miscBmpPath bitmap for printer");
                                    printer.close();
                                    result = ResultBase.RESNETRECPT_ERROR_NG;
                                    logErr("printReceipt", NWPRNT_ERR_BITMAP,
                                            NWPRNT_ERRMSG_BITMAP);
                                    return tp.methodExit(result);
                                }
                         tp.println("end get miscBmpPath bitmap for printer");
                        }else{
                            LOGGER.logAlert(PROG_NAME,
                                    "PaperReceiptPrint.printReceipt()",
                                    Logger.RES_EXCEP_FILENOTFOUND,
                                    "Failed to get sign bmp file.\n"
                                            + "StoreId = " + storeId
                                            + "; TerminalId = " + terminalId
                                            + "; TXID = " + txId);
                        }
                    }
                    else {
                        printer.writeText(line);
                    }
                }

                icount++;
                //Add paper-cut command to print data.
                if (papercut) {
                	printer.writeText(IPrinter.CUT_CMD);
                }

                if (receipts.size()==icount) {
                	printer.writeText(IPrinter.CUT_CMD);
                }

                tp.println("begin send print data to printer");
                int printResult = printer.print();
                if (printResult != 0) {
                	tp.println("failed send print data to printer");
                    printer.close();
                    if (printResult == 2) {
                        result = ResultBase.RESNETRECPT_ERROR_COVER_OPEN;
                    } else if (printResult == 1) {
                        result = ResultBase.RESNETRECPT_ERROR_PAPER_EXHAUSTED;
                    } else if (printResult == 99) {
                        logErr("printReceipt", NWPRNT_ERR_TEXT, NWPRNT_ERRMSG_TEXT);
                        result = ResultBase.RESNETRECPT_ERROR_OTHER;
                    } else {
                        result = ResultBase.RESNETRECPT_ERROR_OTHER;
                    }
                    logPrintErr("printAllReceipt", printResult);
                    return tp.methodExit(result);
                }
                tp.println("end send print data to printer");

                /*tp.println("begin cut paper");
                if (!printer.cutPaper()) {
                	tp.println("failed cut paper");
                    printer.close();
                    result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
                    logErr("printAllReceipt", NWPRNT_ERR_CUT, NWPRNT_ERRMSG_CUT);
                    return tp.methodExit(result);
                }
                tp.println("end cut paper");*/
            }

            printer.close();
        } catch (Exception e) {
            printer.close();
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    "printAllReceipt: Failed to print receipt.", e);
            throw e;
        }

        return tp.methodExit(result);
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        if(angle != null && angle.contains("v")){
            this.angle = 90;
        }else{
            this.angle = 1 ;
        }
    }

}
