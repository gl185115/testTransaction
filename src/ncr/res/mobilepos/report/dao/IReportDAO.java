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
    
    
//1.02 FENGSHA 2014.11.27 売上表を対応 ADD START

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

//1.02 FENGSHA 2014.11.27 売上表を対応 ADD END

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
