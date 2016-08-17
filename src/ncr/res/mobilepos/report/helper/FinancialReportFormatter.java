/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.report.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.PrintFormatter;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.report.constants.ReportConstants;
import ncr.res.mobilepos.report.model.DrawerFinancialReport;
import ncr.res.mobilepos.report.model.FinancialReport;

/**
 * Class for Financial report format.
 *
 * @author Administrator
 */
public class FinancialReportFormatter extends PrintFormatter {

    /** The language. */
    private String language;

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /** Now one line 35 digits single-digit char in default print font. */
    private static final int LINE_MAX = 35;

    /**
     * Instantiates a new financial report formatter.
     *
     * @param languageToSet
     *            the language to set.
     */
    public FinancialReportFormatter(final String languageToSet) {
        super(LINE_MAX);
        this.language = languageToSet;
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Get Financial report text format.
     *
     * @param financialReport
     *            the financial report
     * @return the financial report text
     */
    public final List<String> getFinancialReportText(
            final FinancialReport financialReport) {

        tp.methodEnter("getFinancialReportText");
        tp.println("financialReport", financialReport.toString());

        Locale locale = null;
        if ("".equals(language) || language == null || "ja".equals(language)) {
            locale = Locale.JAPAN;
        } else {
            locale = Locale.ENGLISH;
        }
        // Get the Resource Bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        // Get the business date in customer format
        String businessDate = "";
        if (locale == Locale.JAPAN) {
            businessDate = StringUtility.formatDBDate(
                    financialReport.getBusinessDate(), "yyyy'”N'M'ŒŽ'd'“ú'",
                    Locale.JAPAN);
        } else {
            businessDate = StringUtility.formatDBDate(
                    financialReport.getBusinessDate(), "MMMM d,yyyy",
                    Locale.ENGLISH);
        }

        List<String> reportLines = new ArrayList<String>();

        addItemStrLeft(reportLines, rb.getString("storeid"),
                financialReport.getStoreid(), ReportConstants.PARAM14);
        addItemStrLeft(reportLines, rb.getString("storename"),
                financialReport.getStoreName(), ReportConstants.PARAM14);
        addItemStrLeft(reportLines, rb.getString("deviceid"),
                financialReport.getDeviceNo(), ReportConstants.PARAM14);
        addItemStrLeft(reportLines, rb.getString("businessdate"), businessDate,
                ReportConstants.PARAM14);

        addSeparator(reportLines, " ");

        // Add Sales Revenue
        addTextLeftAlign(reportLines, rb.getString("salesrevenue"));
        addItemStr(reportLines, " " + rb.getString("grosssales"),
                StringUtility.getCurrencySymbol(financialReport
                        .getGrossSalesRevenue().longValue()));
        addItemStr(
                reportLines,
                " " + rb.getString("discount"),
                "-"
                        + StringUtility.getCurrencySymbol(financialReport
                                .getDiscountSale().longValue()));
        addItemStr(
                reportLines,
                " " + rb.getString("return"),
                "-"
                        + StringUtility.getCurrencySymbol(financialReport
                                .getReturnSale().longValue()));
        addItemStr(
                reportLines,
                " " + rb.getString("void"),
                "-"
                        + StringUtility.getCurrencySymbol(financialReport
                                .getVoidSale().longValue()));
        addSeparator(reportLines, "-");
        addItemStr(reportLines, " " + rb.getString("netsales"),
                StringUtility.getCurrencySymbol(financialReport.getNetRevenue()
                        .longValue()));
        // Add Tender
        addSeparator(reportLines, " ");
        addTextLeftAlign(reportLines, rb.getString("tender"));
        addItemStr(reportLines, " " + rb.getString("cash"),
                StringUtility.getCurrencySymbol(financialReport.getCash()
                        .longValue()));
        addItemStr(reportLines, " " + rb.getString("creditcard"),
                StringUtility.getCurrencySymbol(financialReport.getCreditCard()
                        .longValue()));

        addItemStr(reportLines, " " + rb.getString("miscellaneous"),
                StringUtility.getCurrencySymbol(0));
        addSeparator(reportLines, "-");
        addItemStr(reportLines, " " + rb.getString("total"),
                StringUtility.getCurrencySymbol(financialReport.getTotalSale()
                        .longValue()));

        tp.methodExit();

        return reportLines;
    }

    /**
     * Get Drawer Financial Report format.
     *
     * @param report
     *            the report
     * @return the drawer financial report text
     */
    public final List<String> getDrawerFinancialReportText(
            final DrawerFinancialReport report) {
        List<String> reportLines = new ArrayList<String>();

        tp.methodEnter("getDrawerFinancialReportText");
        tp.println("report", report.toString());

        Locale locale = null;
        if ("".equals(language) || language == null || "ja".equals(language)) {
            locale = Locale.JAPAN;
        } else {
            locale = Locale.ENGLISH;
        }

        // Get the Resource Bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        String businessDate = "";
        if (locale == Locale.JAPAN) {
            businessDate = StringUtility.formatDBDate(report.getBusinessDate(),
                    "yyyy'”N'M'ŒŽ'd'“ú'", Locale.JAPAN);
        } else {
            businessDate = StringUtility.formatDBDate(report.getBusinessDate(),
                    "MMMM d,yyyy", Locale.ENGLISH);
        }

        addItemStrLeft(reportLines, rb.getString("storeid"),
                report.getStoreid(), ReportConstants.PARAM14);
        addItemStrLeft(reportLines, rb.getString("storename"),
                report.getStoreName(), ReportConstants.PARAM14);
        addItemStrLeft(reportLines, rb.getString("businessdate"), businessDate,
                ReportConstants.PARAM14);
        addItemStrLeft(reportLines, rb.getString("drawerat"),
                "PRT#" + report.getPrinterID(), ReportConstants.PARAM14);

        addSeparator(reportLines, " ");

        // Add Sales Revenue
        addTextLeftAlign(reportLines, rb.getString("salesrevenue"));
        addItemStr(reportLines, " " + rb.getString("grosssales"),
                StringUtility.getCurrencyFormat(language,
                        report.getGrossSalesRevenue().longValue()));
        addItemStr(
                reportLines,
                " " + rb.getString("discount"),
                "-"
                        + StringUtility.getCurrencyFormat(language, report
                                .getDiscountSale().longValue()));
        addItemStr(
                reportLines,
                " " + rb.getString("void"),
                "-"
                        + StringUtility.getCurrencyFormat(language,
                                report.getVoidSale().longValue()));
        addSeparator(reportLines, "-");
        addItemStr(reportLines, " " + rb.getString("netsales"),
                StringUtility.getCurrencyFormat(language, report.getNetRevenue()
                        .longValue()));

        addSeparator(reportLines, " ");

        addTextLeftAlign(reportLines, rb.getString("tender"));
        addItemStr(reportLines, " " + rb.getString("cash"),
                StringUtility.getCurrencyFormat(language,
                        report.getCash().longValue()));
        addItemStr(reportLines, " " + rb.getString("creditcard"),
                StringUtility.getCurrencyFormat(language, report.getCreditCard()
                        .longValue()));
        addItemStr(reportLines, " " + rb.getString("miscellaneous"),
                StringUtility.getCurrencyFormat(language,
                        report.getMiscellaneous()
                        .longValue()));
        addSeparator(reportLines, "-");
        addItemStr(reportLines, " " + rb.getString("total"),
                StringUtility.getCurrencyFormat(language, report.getTotalSale()
                        .longValue()));

        tp.methodExit();

        return reportLines;
    }
}
