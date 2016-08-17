/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * DepartmentReport
 *
 * Model for the Department Report
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.model;

import ncr.res.mobilepos.report.constants.ReportConstants;

/**
 * ���藚��
 * �o�[�W����         ������t               �S���Җ�           ������e
 * 1.01               2014.12.12             FENGSHA           ����\��Ή�
 */
/**

/**
 * DepartmentReport class is a Model for Department Sales Report.
 */
public class DepartmentReport extends Report {

    /**
     * Initializes DepartmentReport.
     */
    //1.01 2014.12.12  FENGSHA   ����\��Ή� MOD START
    //public DepartmentReport(final String deviceNo, final String operatorNo) {
    public DepartmentReport() {
    //1.01 2014.12.12  FENGSHA   ����\��Ή� MOD END
    //1.01 2014.12.12  FENGSHA   ����\��Ή� DEL START
        /* Add Report headers */
        //this.addHeader(ReportConstants.REPORTHEADER_DEVNUMBER, deviceNo,
                //ReportConstants.REPORTHEADERPOS_LEFT);
        //this.addHeader(ReportConstants.REPORTHEADER_OPNUMBER, operatorNo,
                //ReportConstants.REPORTHEADERPOS_RIGHT);
    //1.01 2014.12.12  FENGSHA   ����\��Ή� DEL END

        /* Add Columns specific to Department Sales Report */
        this.addColumn(ReportConstants.COLHEADER_NUMBER);
        this.addColumn(ReportConstants.COLHEADER_NAME);
        this.addColumn(ReportConstants.COLHEADER_CUSTOMERS);
        this.addColumn(ReportConstants.COLHEADER_ITEMS);
        this.addColumn(ReportConstants.COLHEADER_AMOUNT);

    }

    /**
     * Adds data to Department number.
     *
     * @param data
     *            Department number data to add
     * @return true if success
     */
    public final boolean addDataToDPTNum(final String data) {
        this.addDataToColumn(0, data);
        return true;
    }

    /**
     * Adds data to Department name.
     *
     * @param data
     *            Department name data to add
     * @return true if success
     */
    public final boolean addDataToDPTName(final String data) {
        this.addDataToColumn(ReportConstants.PARAM1, data);
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
        this.addDataToColumn(ReportConstants.PARAM2, data);
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
        this.addDataToColumn(ReportConstants.PARAM3, data);
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
        this.addDataToColumn(ReportConstants.PARAM4, data);
        return true;
    }
}
