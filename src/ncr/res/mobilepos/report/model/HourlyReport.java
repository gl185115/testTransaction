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
//1.01 2014.12.12  FENGSHA   売上表を対応 DEL START
//import ncr.res.mobilepos.report.constants.ReportConstants;
//1.01 2014.12.12  FENGSHA   売上表を対応 DEL END
/**
* 改定履歴
* バージョン         改定日付               担当者名           改定内容
* 1.01               2014.12.12             FENGSHA           売上表を対応
*/

/**
 * HourlyReport Class is a Model representation of the Hourly Report.
 */
public class HourlyReport extends Report {

    /**
     * Construct for hourly report.
     */
    //1.01 2014.12.12  FENGSHA   売上表を対応 MOD START
    //public HourlyReport(final String deviceNo, final String operatorNo) {
    public HourlyReport() {
    //1.01 2014.12.12  FENGSHA   売上表を対応 MOD END
        addColumn("Time Zone");
        addColumn("Customers");
        addColumn("Items");
        addColumn("Amount");
    //1.01 2014.12.12  FENGSHA   売上表を対応 DEL START
        //addHeader("Device No", deviceNo, ReportConstants.REPORTHEADERPOS_LEFT);
        //addHeader("Operator No", operatorNo,
         //       ReportConstants.REPORTHEADERPOS_RIGHT);
    //1.01 2014.12.12  FENGSHA   売上表を対応 DEL END

    }
}
