package ncr.res.mobilepos.networkreceipt.helper;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2015.01.20      FENGSHA       80mmの紙変更を対応,字体圧縮しない
 */
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.simpleprinterdriver.IPrinter;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterIF;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

public class VoidReceiptPrint extends NetPrintService{
    /**
     * class instance of NetPrinterIF.
     */
    private NetPrinterIF printerIF;
    private String logoPath;
    /**
     * Constructor.
     * Create net printer interface
     * @param printerInfo - a NetPrinterInfo class used
     * to initialize NetPrinterIF
     */
    public VoidReceiptPrint(final NetPrinterInfo printerInfo,
            final String path) {
        super("NtwkRcpt");
        printerIF = new NetPrinterIF(printerInfo);
        logoPath = path;
    }
    /**
     * print the credit slip.
     * @param text - array of bytes to print
     * @return result code of the request
     */
    synchronized public final int printVoidReceipt(final byte[] text) {
        int result = 0;
        byte[] logoBMP = getBmpLocalFileData(logoPath);
        //1.01 2015/01/20 FENGSHA ADD START
        String printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            printPaperLength = (String) env.lookup("Printpaperlength");
        } catch (NamingException e) {
            printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        }
        //1.01 2015/01/20 FENGSHA ADD END
        if (!printerIF.connectPrinter()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printVoidReceipt", NWPRNT_ERR_CONNECT, NWPRNT_ERRMSG_CONNECT);
            return result;
        }
        if (!printerIF.clearBuffer()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printVoidReceipt", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
            return result;
        }

        //initialize the printer
        if (!printerIF.initPrinter()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printVoidReceipt", NWPRNT_ERR_INIT, NWPRNT_ERRMSG_INIT);
            return result;
        }
        //print logo image
        //1.01 2015/01/20 FENGSHA MOD START
        boolean writeBmpReturn = false;
        if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
            writeBmpReturn =  printerIF.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER53);
        } else{
            writeBmpReturn =  printerIF.writeBmp(logoBMP, 1, IPrinter.FLG_PAPER80);
        }

        //if ((logoBMP != null) && (!printerIF.writeBmp(logoBMP, 1, 1))) {
        if ((logoBMP != null) && (!writeBmpReturn)) {
        //1.01 2015/01/20 FENGSHA MOD END
                printerIF.close();
                result = ResultBase.RESNETRECPT_ERROR_NG;
                logErr("printVoidReceipt", NWPRNT_ERR_BITMAP, NWPRNT_ERRMSG_BITMAP);
                return result;
        }
        printerIF.writeText(text);

        int printResult = printerIF.print();
        if (printResult != 0) {
            printerIF.close();
            if (printResult == 2) {
                result = ResultBase.RESNETRECPT_ERROR_COVER_OPEN;
            } else if (printResult == 1) {
                result = ResultBase.RESNETRECPT_ERROR_PAPER_EXHAUSTED;
            } else if (printResult == 99) {
                logErr("printVoidReceipt", NWPRNT_ERR_TEXT, NWPRNT_ERRMSG_TEXT);
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
            } else {
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
            }
            logPrintErr("printVoidReceipt", printResult);
            return result;
        }

        //cut paper
        if (!printerIF.cutPaper()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printVoidReceipt", NWPRNT_ERR_CUT, NWPRNT_ERRMSG_CUT);
            return result;
        }

        printerIF.close();

        return result;
    }
}
