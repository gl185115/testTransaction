package ncr.res.mobilepos.networkreceipt.helper;

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterIF;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

/**
 * class that handles credit slip printing.
 *
 */
public class CreditSlipPrint extends NetPrintService{
    /**
     * class instance of NetPrinterIF.
     */
    private NetPrinterIF printerIF;

    /**
     * Constructor.
     * Create net printer interface
     * @param printerInfo - a NetPrinterInfo class used
     * to initialize NetPrinterIF
     */
    public CreditSlipPrint(final NetPrinterInfo printerInfo) {
        super("NtwkRcpt");
        printerIF = new NetPrinterIF(printerInfo);
    }

    /**
     * print the credit slip.
     * @param text - array of bytes to print
     * @return result code of the request
     */
    synchronized public final int printCreditSlip(final byte[] text) {
        int result = 0;
        if (!printerIF.connectPrinter()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printCreditSlip", NWPRNT_ERR_CONNECT, NWPRNT_ERRMSG_CONNECT);
            return result;
        }
        if (!printerIF.clearBuffer()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printCreditSlip", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
            return result;
        }

        //initialize the printer
        if (!printerIF.initPrinter()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printCreditSlip", NWPRNT_ERR_INIT, NWPRNT_ERRMSG_INIT);
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
                logErr("printCreditSlip", NWPRNT_ERR_TEXT, NWPRNT_ERRMSG_TEXT);
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
            } else {
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
            }
            logPrintErr("printCreditSlip", printResult);
            return result;
        }

        //cut paper
        if (!printerIF.cutPaper()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            logErr("printCreditSlip", NWPRNT_ERR_CUT, NWPRNT_ERRMSG_CUT);
            return result;
        }

        printerIF.close();

        return result;
    }
}
