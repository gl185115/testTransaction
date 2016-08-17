/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.report.helper;

import java.io.UnsupportedEncodingException;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.networkreceipt.helper.NetPrintService;
import ncr.res.mobilepos.report.constants.ReportConstants;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterIF;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

/**
 * Class for print Financial report.
 *
 * @author Administrator
 */
public class FinancialReportPrint extends NetPrintService {

    /** The NetPrinterIF instance. */
    private NetPrinterIF printerIF;
    /** The Debug Logger Trace Printer. */
    private Trace.Printer tp;

    /**
     * Instantiates a new financial report print.
     *
     * @param printerInfo
     *            the printer info
     */
    public FinancialReportPrint(final NetPrinterInfo printerInfo) {
        super("Report");
        printerIF = new NetPrinterIF(printerInfo);
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Print Financial Report by Net Printer.
     *
     * @param reportLines
     *            the report lines
     * @return the int
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    synchronized public final int printFinancialReport(final List<String> reportLines)
            throws UnsupportedEncodingException {
        tp.methodEnter("printFinancialReport");
        int result = 0;
        byte[] text = getByteForPrinter(reportLines);

        if (!printerIF.connectPrinter()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            tp.println(NWPRNT_ERRMSG_CONNECT);
            logErr("printFinancialReport", NWPRNT_ERR_CONNECT, NWPRNT_ERRMSG_CONNECT);
            return result;
        }

        if (!printerIF.clearBuffer()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            tp.println(NWPRNT_ERRMSG_CLEAR);
            logErr("printFinancialReport", NWPRNT_ERR_CLEAR, NWPRNT_ERRMSG_CLEAR);
            return result;
        }

        if (!printerIF.initPrinter()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            tp.println(NWPRNT_ERRMSG_INIT);
            logErr("printFinancialReport", NWPRNT_ERR_INIT, NWPRNT_ERRMSG_INIT);
            return result;
        }

        printerIF.writeText(text);

        int printResult = printerIF.print();
        if (printResult != 0) {
            printerIF.close();
            if (printResult == 2) {
                result = ResultBase.RESNETRECPT_ERROR_COVER_OPEN;
                tp.println("Opening of Printer cover failed.");
            } else if (printResult == 1) {
                result = ResultBase.RESNETRECPT_ERROR_PAPER_EXHAUSTED;
                tp.println("Paper Exhausted.");
            } else if (printResult == 99) {
                logErr("printFinancialReport", NWPRNT_ERR_TEXT, NWPRNT_ERRMSG_TEXT);
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
            } else {
                result = ResultBase.RESNETRECPT_ERROR_OTHER;
                tp.println("Printer fails to print.");
            }
            logPrintErr("printFinancialReport", printResult);
            return result;
        }

        // cut paper
        if (!printerIF.cutPaper()) {
            printerIF.close();
            result = ResultBase.RESNETRECPT_ERROR_NOTFOUND;
            tp.println("Printer cut paper failed.");
            logErr("printFinancialReport", NWPRNT_ERR_CUT, NWPRNT_ERRMSG_CUT);
            return result;
        }

        printerIF.close();
        tp.methodExit(result);
        return result;
    }

    /**
     * Gets the byte for printer.
     *
     * @param reportLines
     *            the report lines
     * @return the byte for printer
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    private byte[] getByteForPrinter(final List<String> reportLines)
            throws UnsupportedEncodingException {
        byte[] report = new byte[reportLines.size() * ReportConstants.PARAM40];
        byte[] endLine = {ReportConstants.PARAM0A};
        int index = 0;
        for (String line : reportLines) {
            byte[] lineBits = line.getBytes("MS932");
            byte[] temp = new byte[lineBits.length + 1];
            System.arraycopy(lineBits, 0, temp, 0, lineBits.length);
            System.arraycopy(endLine, 0, temp, lineBits.length, endLine.length);
            System.arraycopy(temp, 0, report, index, temp.length);
            index = index + temp.length;
        }
        return report;
    }
}
