/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * HourlyReport
 *
 * Model for the Hourly Report
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.model;
//1.01 2014.12.12  FENGSHA   ����\��Ή� DEL START
//import ncr.res.mobilepos.report.constants.ReportConstants;
//1.01 2014.12.12  FENGSHA   ����\��Ή� DEL END
/**
* ���藚��
* �o�[�W����         ������t               �S���Җ�           ������e
* 1.01               2014.12.12             FENGSHA           ����\��Ή�
*/

/**
 * HourlyReport Class is a Model representation of the Hourly Report.
 */
public class HourlyReport extends Report {

    /**
     * Construct for hourly report.
     */
    //1.01 2014.12.12  FENGSHA   ����\��Ή� MOD START
    //public HourlyReport(final String deviceNo, final String operatorNo) {
    public HourlyReport() {
    //1.01 2014.12.12  FENGSHA   ����\��Ή� MOD END
        addColumn("Time Zone");
        addColumn("Customers");
        addColumn("Items");
        addColumn("Amount");
    //1.01 2014.12.12  FENGSHA   ����\��Ή� DEL START
        //addHeader("Device No", deviceNo, ReportConstants.REPORTHEADERPOS_LEFT);
        //addHeader("Operator No", operatorNo,
         //       ReportConstants.REPORTHEADERPOS_RIGHT);
    //1.01 2014.12.12  FENGSHA   ����\��Ή� DEL END

    }
}
