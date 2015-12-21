/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * DepartmentReport
 *
 * Model for the Detail Report
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.model;

import ncr.res.mobilepos.report.constants.ReportConstants;

/**
 * DepartmentReport class is a Model for Detail Sales Report.
 */
public class DetailReport extends Report {

    /**
     * Instantiates a new detail report.
     *
     * @param bizDate
     *            the biz date
     * @param storeNo
     *            the store no
     * @param dpt
     *            the dpt
     * @param deviceNo
     *            the device no
     */
    public DetailReport(final String bizDate, final String storeNo,
            final String dpt, final String deviceNo) {
        /* Add Report headers */
        this.addHeader(ReportConstants.REPORTHEADER_BUSINESSDATE, bizDate,
                ReportConstants.REPORTHEADERPOS_LEFT);
        this.addHeader(ReportConstants.REPORTHEADER_STORENUMBER, storeNo,
                ReportConstants.REPORTHEADERPOS_LEFT);
        this.addHeader(ReportConstants.REPORTHEADER_DEPARTMENT, dpt,
                ReportConstants.REPORTHEADERPOS_LEFT);
        this.addHeader(ReportConstants.REPORTHEADER_DEVNUMBER, deviceNo,
                ReportConstants.REPORTHEADERPOS_LEFT);

        /* Add Columns specific to Department Sales Report */
        this.addColumn(ReportConstants.COLHEADER_TIMEZONE);
        this.addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        this.addColumn(ReportConstants.COLHEADER_ITEMS);
        this.addColumn(ReportConstants.COLHEADER_AMOUNT);

    }

    /**
     * Adds data to TimeZone.
     *
     * @param data
     *            TimeZone data to add
     * @return true if success
     */
    public final boolean addDataToTimeZone(final String data) {
        this.addDataToColumn(ReportConstants.PARAM0, data);
        return true;
    }

    /**
     * Adds data to number of Customers.
     *
     * @param data
     *            Number of Customers data to add
     * @return true if success
     */
    public final boolean addDataToCustomers(final String data) {
        this.addDataToColumn(ReportConstants.PARAM1, data);
        return true;
    }

    /**
     * Adds data to number of purchased Items.
     *
     * @param data
     *            Number of purchased Items data to add
     * @return true if success
     */
    public final boolean addDataToItems(final String data) {
        this.addDataToColumn(ReportConstants.PARAM2, data);
        return true;
    }

    /**
     * Adds data to total sales Amount.
     *
     * @param data
     *            Total sales Amount data to add
     * @return true if success
     */
    public final boolean addDataToAmount(final String data) {
        this.addDataToColumn(ReportConstants.PARAM3, data);
        return true;
    }

}
