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
 * ���藚��
 * �o�[�W����      ������t      �S���Җ�       ������e
 * 1.01            2014.10.21    MAJINHUI       ���|�[�g�o�͂�Ή�
 * 1.02            2014.11.27    FENGSHA        ����\��Ή�
 * 1.03            2014.12.8     MAJINHUI       ���|�[�g�o�͂�Ή�
 * 1.04            2014.12.29    MAJINHUI       ��v���|�[�g�o�͂�Ή�
 * 1.05            2015.1.21     MAJINHUI       �_���E���Z���|�[�g�o�͂�Ή�
 */

/**
 * IReportDAO is a DAO Interface for Reports.
 */
public interface IReportDAO {
    // 1.03 2014.12.8 MAJINHUI ���|�[�g�o�͂�Ή��@�@�@ START
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

    // 1.03 2014.12.8 MAJINHUI ���|�[�g�o�͂�Ή��@�@�@ END
    //1.05 2015.1.21 MAJINHUI �_���E���Z���|�[�g�o�͂�Ή� add start
    String getActData(String storeid, String tillid,
            String businessDayDate, String language)throws DaoException;
    //1.05 2015.1.21 MAJINHUI �_���E���Z���|�[�g�o�͂�Ή� add end
    // 1.04 2014.12.29 MAJINHUI ��v���|�[�g�o�͂�Ή��@ADD START
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

    // 1.04 2014.12.29 MAJINHUI ��v���|�[�g�o�͂�Ή��@ADD END
    
    
//1.02 FENGSHA 2014.11.27 ����\��Ή� ADD START

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

//1.02 FENGSHA 2014.11.27 ����\��Ή� ADD END

    /**
     * �T�[�o�[�v�Z�����ݍ����z���擾.
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
