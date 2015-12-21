/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SalesClerkReport
 *
 * Model for the Department Report
 *
 * del Rio, Rica Marie M.
 */

package ncr.res.mobilepos.report.model;

import ncr.res.mobilepos.report.constants.ReportConstants;

/**
 * SalesClerkReport class is a Model for Sales Clerk Sales Report.
 */
public class SalesClerkReport extends Report {

    /**
     * Custom constructor.
     *
     * @param deviceNo
     *            the device no
     * @param operatorNo
     *            the operator no
     */
    public SalesClerkReport(final String deviceNo, final String operatorNo) {
        this.addHeader(ReportConstants.REPORTHEADER_DEVNUMBER, deviceNo,
                ReportConstants.REPORTHEADERPOS_LEFT);
        this.addHeader(ReportConstants.REPORTHEADER_OPNUMBER, operatorNo,
                ReportConstants.REPORTHEADERPOS_RIGHT);
        this.addColumn(ReportConstants.COLHEADER_NUMBER);
        this.addColumn(ReportConstants.COLHEADER_NAME);
        this.addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        this.addColumn(ReportConstants.COLHEADER_ITEMS);
        this.addColumn(ReportConstants.COLHEADER_AMOUNT);
    }

    /**
     * Add value to operator number.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToOperNumber(final String data) {
        this.addDataToColumn(0, data);
        return true;
    }

    /**
     * Add value to name.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToName(final String data) {
        this.addDataToColumn(ReportConstants.PARAM1, data);
        return true;
    }

    /**
     * Add value to sales customers.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToSalesCustomers(final String data) {
        this.addDataToColumn(ReportConstants.PARAM2, data);
        return true;
    }

    /**
     * Add value to sales item.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToSalesItems(final String data) {
        this.addDataToColumn(ReportConstants.PARAM3, data);
        return true;
    }

    /**
     * Add value to sales amount.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToSalesAmount(final String data) {
        this.addDataToColumn(ReportConstants.PARAM4, data);
        return true;
    }
}
