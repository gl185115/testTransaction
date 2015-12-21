/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SalesTargetMarketReport
 *
 * Model for the SalesTarget Market Report
 *
 * del Rio, Rica Marie M.
 */

package ncr.res.mobilepos.report.model;

import ncr.res.mobilepos.report.constants.ReportConstants;

/**
 * SalesTargetMarketReport class is a Model for Target Market Sales Report.
 */
public class SalesTargetMarketReport extends Report {

    /**
     * Custom constructor.
     *
     * @param deviceNo
     *            the device no
     * @param operatorNo
     *            the operator no
     */
    public SalesTargetMarketReport() {
        this.addColumn(ReportConstants.COLHEADER_NUMBER);
        this.addColumn(ReportConstants.COLHEADER_NAME);
        this.addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        this.addColumn(ReportConstants.COLHEADER_ITEMS);
        this.addColumn(ReportConstants.COLHEADER_AMOUNT);
    }

    /**
     * Add value to target market number.
     *
     * @param data
     *            the data
     * @return true, if successful
     */
    public final boolean addDataToGuestNumber(final String data) {
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
