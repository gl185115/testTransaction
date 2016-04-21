/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IReport
 *
 * Interface for the DAO used for Reports
 *
 * Meneses, Chris Niven
 */

package ncr.res.mobilepos.report.dao;

import java.sql.SQLException;
import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.report.model.DailyReport;
import ncr.res.mobilepos.report.model.DailyReportItems;
import ncr.res.mobilepos.report.model.Details;
import ncr.res.mobilepos.report.model.DrawerFinancialReport;
import ncr.res.mobilepos.report.model.FinancialReport;
import ncr.res.mobilepos.report.model.ReportItems;
import ncr.res.mobilepos.report.model.TotalAmount;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;
/**
 * 改定履歴
 * バージョン      改定日付      担当者名       改定内容
 * 1.01            2014.10.21    MAJINHUI       レポート出力を対応
 * 1.02            2014.11.27    FENGSHA        売上表を対応
 * 1.03            2014.12.8     MAJINHUI       レポート出力を対応
 * 1.04            2014.12.29    MAJINHUI       会計レポート出力を対応
 * 1.05            2015.1.21     MAJINHUI       点検・精算レポート出力を対応
 */

/**
 * IReportDAO is a DAO Interface for Reports.
 */
public interface IReportDAO {
    // 1.03 2014.12.8 MAJINHUI レポート出力を対応　　　 START
    /**
     * New implementation for Generate account report.
     *
     * @param storeNo
     *            the store id
     * @param tillid
     *            the till id
     * @param businessDayDate
     *            the business Day Date
     * @param language
     *            the language
     * @return the report items
     * @throws DaoException
     *             Thrown when process fails
     * @throws SQLException
     *             Thrown when SQL error occurs.
     */
    ReportItems generateAccountancyReport(String storeid, String tillid,
            String businessDayDate, String language) throws DaoException;

    // 1.03 2014.12.8 MAJINHUI レポート出力を対応　　　 END
    //1.05 2015.1.21 MAJINHUI 点検・精算レポート出力を対応 add start
    String getActData(String storeid, String tillid,
            String businessDayDate, String language)throws DaoException;
    //1.05 2015.1.21 MAJINHUI 点検・精算レポート出力を対応 add end
    // 1.04 2014.12.29 MAJINHUI 会計レポート出力を対応　ADD START
    /**
     * get store telephone number
     *
     * @param storeNo
     *            the store id
     * @return the store telephone number
     * @throws DaoException
     *             Thrown when process fails
     */
    String getStoreTel(String storeNo) throws DaoException;

    // 1.04 2014.12.29 MAJINHUI 会計レポート出力を対応　ADD END

    /**
     * Generates the Sales Hourly Report of the current device and operator.
     *
     * @param deviceNo
     *            Device number
     * @param operatorNo
     *            Operator number
     * @param businessdaydate
     *            Business day date
     * @param storeNo
     *            Store number
     * @param startTime
     *            StartTime; To use switchTime or default(12)
     * @return HTML formatted string of the generated report
     * @throws DaoException
     *             Thrown when process fails.
     */
    String generateHourlyReport(String deviceNo, String operatorNo,
            String businessdaydate, String storeNo, String startTime)
            throws DaoException;

    /**
     * New implementation for Generate hourly report.
     *
     * @param businessdaydate
     *            the Business day date
     * @param storeNo
     *            the store no
     * @param startTime
     *            the start time
     * @return the report items
     * @throws DaoException
     *             Thrown when process fails.
     */
    ReportItems generateHourlyReportNew(
  // 1.02  2014.11.27 FENGSHA   売上表を対応 DEL START
            //String deviceNo, String operatorNo,
  // 1.02  2014.11.27 FENGSHA   売上表を対応 DEL END
            String businessdaydate, String storeNo, String startTime)
            throws DaoException;


    /**
     * Generates the Sales SalesClerk Report of the current device and operator.
     *
     * @param deviceNo
     *            Device number
     * @param operatorNo
     *            Operator number
     * @param businessDaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @return HTML formatted string of the generated report
     * @throws DaoException
     *             Thrown when process fails.
     */
    String generateSalesClerkReport(String deviceNo, String operatorNo,
            String businessDaydate, String storeNo) throws DaoException;

    /**
     * Generate sales clerk report new.
     *
     * @param deviceNo
     *            the device number
     * @param operatorNo
     *            the operator number
     * @param businessDaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @return the report items
     * @throws DaoException
     *             Thrown when process fails.
     */
    ReportItems generateSalesClerkReportNew(String deviceNo, String operatorNo,
            String businessDaydate, String storeNo) throws DaoException;

    /**
     * Generates the Sales Department Report of the current device and operator.
     *
     * @param deviceNo
     *            Device number
     * @param operatorNo
     *            Operator number
     * @param businessdaydate
     *            the business day date
     * @param storeNo
     *            Store number
     * @return HTML formatted string of the generated report
     * @throws DaoException
     *             Thrown when process fails.
     */
    String generateDepartmentReport(String deviceNo, String operatorNo,
            String businessdaydate, String storeNo) throws DaoException;

    /**
     * Generates the Sales Detail Report according to the details specified.
     *
     * @param details
     *            Contains the details to generate report from
     * @return HTML formatted string of the generated report
     * @throws DaoException
     *             Thrown when process fails.
     */
    String generateDetailReport(Details details) throws DaoException;

    /**
     * Generate financial report new.
     *
     * @param deviceNo
     *            the device number
     * @param businessDate
     *            the business date
     * @param storeNo
     *            the store number
     * @return the financial report
     * @throws DaoException
     *             thrown when process fails.
     * @throws SQLException
     *             thrown when SQL error occurs.
     */
    FinancialReport generateFinancialReportNew(String deviceNo,
            String businessDate, String storeNo) throws Exception;

    /**
     * Generates the Drawer Financial Report.
     *
     * @param printerID
     *            Printer ID
     * @param businessDate
     *            Date of the details where the drawer financial report be
     *            generated from
     * @param storeNo
     *            Store number
     * @return DrawerFinancialReportObject
     * @throws DaoException
     *             thrown when process fails.
     * @throws SQLException
     *             thrown when SQL error occurs.
     */
    DrawerFinancialReport generateDrawerFinancialReport(String printerID,
            String businessDate, String storeNo) throws Exception;

    /**
     * Get the current device's Financial Report Object.
     *
     * @param deviceNo
     *            the device number
     * @param businessDate
     *            the business date
     * @param storeNo
     *            the store number
     * @return the financial report obj
     * @throws DaoException
     *             thrown when process fails.
     * @throws SQLException
     *             thrown when SQL error occurs.
     */
    FinancialReport getFinancialReportObj(String deviceNo, String businessDate,
            String storeNo) throws Exception;

    /**
     * Generate department report new.
     *
     * @param businessdaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @return the report items
     * @throws DaoException
     *             thrown when process fails.
     */
    ReportItems generateDepartmentReportNew(
//1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
            //String deviceNo, String operatorNo,
//1.02 FENGSHA 2014.11.27 売上表を対応 DEL END
            String businessdaydate, String storeNo,String deparmentNo) throws DaoException;
//1.02 FENGSHA 2014.11.27 売上表を対応 ADD START
    /**
     * Generate group report new.
     *
     * @param businessdaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @return the report items
     * @throws DaoException
     *             thrown when process fails.
     */
    ReportItems generateGroupReportNew(String businessdaydate, String storeNo)
            throws DaoException;

    /**
     * Generate sales clerk report new.
     *
     * @param deviceNo
     *            the device number
     * @param operatorNo
     *            the operator number
     * @param businessDaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @return the report items
     * @throws DaoException
     *             Thrown when process fails.
     */
    ReportItems generateClerkProductReportNew( String operatorNo,
           String businessDaydate, String storeNo) throws DaoException;

    /**
     * * find store name.
     *
     * @param storeNo
     *            the store number
     * @return the store name
     * @throws DaoException
     *
     */
    String findStoreName(String storeNo) throws DaoException;

    /**
     * Get the Department name
     *
     * @param storeNo
     *            the store number
     * @param departmentNo
     *            the department number
     * @return dptName
     *            the departmentName String
     * @throws DaoException
     *             thrown when process fails.
     */
    String findDepartmentName(String storeNo, String departmentNo) throws DaoException;

    /**
     * * find operator name.
     *
     * @param operatorNo
     *            the operatorNo number
     * @return the operator name
     * @throws DaoException
     *
     */
    String findOperatorName(String operatorNo) throws DaoException;

    /**
     * Generates the Store Report of the current device and operator.
     *
     * @param businessdaydate
     *            Business day date
     * @param storeNo
     *            Store number
     * @throws DaoException
     *             Thrown when process fails.
     */
    ReportItems generateStoreReportNew(String businessdaydate, String storeNo)
            throws DaoException;
    /**
     * Generates the Item Report of the current device and operator.
     *
     * @param businessdaydate
     *            Business day date
     * @param storeNo
     *            Store number
     * @param departmentNo
     *            Department number
     * @throws DaoException
     *             Thrown when process fails.
     */
    ReportItems generateItemReportNew(String businessdaydate, String storeNo, String departmentNo)
            throws DaoException;
    /**
     * Generates the Department and Hourly Report of the current device and operator.
     * @param deviceNo
     *            Device number
     * @param operatorNo
     *            Operator number
     * @param businessdaydate
     *            Business day date
     * @param storeNo
     *            Store number
     *@param departmentNo
     *            Department number
     *@param startTime
     *            startTime
     * @throws DaoException
     *             Thrown when process fails.
     */
    ReportItems generateDivHourlyReportNew(String businessdaydate,
                               String storeNo,String departmentNo)
            throws DaoException;
    /**
     * Generates the Target Market  Report of the current device and operator.
     *
     * @param deviceNo
     *            Device number
     * @param operatorNo
     *            Operator number
     * @param businessdaydate
     *            Business day date
     * @param storeNo
     *            Store number
     * @throws DaoException
     *             Thrown when process fails.
     */
    ReportItems generateTargetMarketReportNew(String businessdaydate, String storeNo)
            throws DaoException;

    /**
     * Generates the Sales Man  Report of the current device and operator.
     * @param operatorNo
     *            Operator number
     * @param businessdaydate
     *            Business day date
     * @param storeNo
     *            Store number
     * @throws DaoException
     *             Thrown when process fails.
     *
     */
    ReportItems generateSalesManReportNew(String operatorNo,
             String businessdaydate, String storeNo)
             throws DaoException;
//1.02 FENGSHA 2014.11.27 売上表を対応 ADD END

    /**
     * Get net printer info.
     *
     * @param storeid
     *            the store identifier string.
     * @param printerid
     *            the printer identifier string.
     * @return the printer info
     * @throws DaoException
     *            thrown when process fails.
     */
    NetPrinterInfo getPrinterInfo(String storeid, String printerid)
            throws DaoException;

    /**
     * サーバー計算した在高金額を取得.
     *
     * @param storeId
     *            the store number.
     * @param tillId
     *            the till number.
     * @param businessDate
     *            the business day date
     * @return the total amount
     * @throws DaoException
     *            thrown when process fails.
     */
	TotalAmount getTotalAmount(String storeId, String tillId,
			String businessDate)throws DaoException;
	
	/**
	 * get daily report items
	 * @param companyid
	 * @param storeId
	 * @return
	 * @throws DaoException
	 */
	List<DailyReport> getDailyReportItems(final String companyId, final String storeId,
	        final String terminalId, final String businessDate, final int trainingFlag) throws DaoException;
	
}
