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
 * SalesManReport class is a Model for Sales Man Report.
 */
public class SalesManReport extends Report {

    /**
     * Custom constructor.
     */
    public SalesManReport() {
        this.addColumn(ReportConstants.COLHEADER_NUMBER);
        this.addColumn(ReportConstants.COLHEADER_NAME);
        this.addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        this.addColumn(ReportConstants.COLHEADER_ITEMS);
        this.addColumn(ReportConstants.COLHEADER_AMOUNT);
    }
    /**
     * Add value to sales man number.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToSalManNumber(final String data) {
        this.addDataToColumn(ReportConstants.PARAM0, data);
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
