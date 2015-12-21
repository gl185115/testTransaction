/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.report.helper;

import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.report.constants.ReportConstants;
import ncr.res.mobilepos.report.model.FinancialReport;

/**
 * The Class for print Financial Report.
 *
 * @author Administrator
 */
public class FinancialReportPrinter {

    /** The locale. */
    private Locale locale = Locale.JAPANESE;

    /** The jps financial report. */
    private JpsFinancialReport jpsFinancialReport;

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * Instantiates a new financial report printer.
     *
     * @param language
     *            the language
     * @param printerName
     *            the printer name
     */
    public FinancialReportPrinter(final String language,
            final String printerName) {
        this.jpsFinancialReport = new JpsFinancialReport(printerName);
        if ("en".equals(language)) {
            locale = Locale.ENGLISH;
        }
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Prints the financial report.
     *
     * @param financialReport
     *            the financial report
     * @return true, if successful
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     * @throws PrinterException
     *             the printer exception
     */
    public final boolean printFinancialReport(
            final FinancialReport financialReport)
            throws UnsupportedEncodingException, PrinterException {

        tp.methodEnter("printFinancialReport");
        tp.println("financialReport", financialReport.toString());

        Font font10 = new Font("‚l‚r ƒSƒVƒbƒN", Font.PLAIN, ReportConstants.PARAM10);
        Font font12 = new Font("‚l‚r ƒSƒVƒbƒN", Font.PLAIN, ReportConstants.PARAM12);

        // Get the Resource Bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        // Get the business date in customer format
        String businessDate = "";
        if (locale == Locale.JAPANESE) {
            businessDate = StringUtility.formatDBDate(
                    financialReport.getBusinessDate(), "yyyy'”N'M'ŒŽ'd'“ú'",
                    Locale.JAPAN);
        } else {
            businessDate = StringUtility.formatDBDate(
                    financialReport.getBusinessDate(), "MMMM d,yyyy",
                    Locale.ENGLISH);
        }

        boolean result = true;

        // Add header
        result = result
                && jpsFinancialReport.addItemStrLeft(rb.getString("storeid"),
                        financialReport.getStoreid(), ReportConstants.PARAM14,
                            font10);
        result = result
                && jpsFinancialReport.addItemStrLeft(rb.getString("storename"),
                        financialReport.getStoreName(), ReportConstants.PARAM14,
                            font10);
        result = result
                && jpsFinancialReport.addItemStrLeft(rb.getString("deviceid"),
                        financialReport.getDeviceNo(), ReportConstants.PARAM14,
                            font10);
        result = result
                && jpsFinancialReport.addItemStrLeft(
                        rb.getString("businessdate"), businessDate,
                            ReportConstants.PARAM14, font10);

        jpsFinancialReport.addSeparator("", font12);
        // Add Sales Revenue
        result = result
                && jpsFinancialReport.addText(rb.getString("salesrevenue"), 1,
                        font12);
        result = result
                && jpsFinancialReport.addItemStr(
                        " " + rb.getString("grosssales"), StringUtility
                                .getCurrencySymbol(financialReport
                                        .getGrossSalesRevenue().longValue()),
                        font12);
        result = result
                && jpsFinancialReport.addItemStr(" " + rb.getString("discount"),
                                "-" + StringUtility.getCurrencySymbol(
                                        financialReport.getDiscountSale()
                                            .longValue()), font12);
        result = result
                && jpsFinancialReport.addItemStr(
                        " " + rb.getString("return"), "-"
                            + StringUtility.getCurrencySymbol(financialReport
                                    .getReturnSale().longValue()),
                        font12);
        result = result
                && jpsFinancialReport.addItemStr(
                        " " + rb.getString("void"), "-" + StringUtility
                            .getCurrencySymbol(financialReport.getVoidSale()
                                    .longValue()),
                        font12);
        jpsFinancialReport.addSeparator("-", font10);
        result = result
                && jpsFinancialReport.addItemStr(
                        " " + rb.getString("netsales"), StringUtility
                                .getCurrencySymbol(financialReport
                                        .getNetRevenue().longValue()), font12);
        // Add Tender
        jpsFinancialReport.addSeparator("", font12);
        result = result
                && jpsFinancialReport
                        .addText(rb.getString("tender"), 1, font12);
        result = result
                && jpsFinancialReport.addItemStr(" " + rb.getString("cash"),
                        StringUtility.getCurrencySymbol(financialReport
                                .getCash().longValue()), font12);
        result = result
                && jpsFinancialReport.addItemStr(
                        " " + rb.getString("creditcard"), StringUtility
                                .getCurrencySymbol(financialReport
                                        .getCreditCard().longValue()), font12);
        result = result
                && jpsFinancialReport.addItemStr(
                        " " + rb.getString("miscellaneous"),
                        StringUtility.getCurrencySymbol(0), font12);
        jpsFinancialReport.addSeparator("-", font10);
        result = result
                && jpsFinancialReport.addItemStr(" " + rb.getString("total"),
                        StringUtility.getCurrencySymbol(financialReport
                                .getTotalSale().longValue()), font12);

        tp.methodExit(result);
        return result;
    }
}
