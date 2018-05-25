/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IPosLogDAO
 *
 * Interface in DAO for PosLog
 *
 * Rica Marie M. del Rio
 */

package ncr.res.mobilepos.journalization.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.naming.NamingException;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.JournalizationException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.exception.TillException;
import ncr.res.mobilepos.journalization.model.PointPosted;
import ncr.res.mobilepos.journalization.model.SearchForwardPosLog;
import ncr.res.mobilepos.journalization.model.poslog.AdditionalInformation;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.TransactionSearch;

/**
 * IPosLogDAO is a DAO interface for POSLog Journal.
 */
public interface IPosLogDAO {
    /**
     * Do the POSLog Journalization of any type of transaction.
     *
     * @param posLog         The POSLog which holds the transaction
     *                        to be processed.
     * @param posLogXml      The same POSLog but in xml format.
     *
     * @return               Return the number of rows affected in the database.
     * @throws Exception     The exception thrown when the process fail.
     */
	void savePOSLog(PosLog posLog, String posLogXml, int trainingMode)
			throws DaoException, JournalizationException, TillException, SQLStatementException,
					ParseException, NamingException;

	/**
     * Get the transaction POSLog XML in the TXL_POSLOG table.
     * by specifying the company id, trainingmode, transaction number.
     *
     * @param companyid        The company ID
     * @param terminalid       The Terminal ID
     * @param storeid          The Store ID
     * @param traininflag      Training mode flag
     * @param txid             The Transaction Number
     * @return                 The POSLog XML, else,
     *                            empty string with transaction not found.
     * @throws DaoException    Exception thrown when setting up
     *                            the prepared Statement fails.
     */
	String getPOSLogTransaction(String companyid, String storeid , String workstationid,
			                    String businessdate, String txid, int trainingflag, String txtype) throws DaoException;

	/**
     * Gets a POSLog XML by specifying the transaction number.
     *
     * @param txDeviceNumber        The ID of the device used to
     *                                perform the target transaction
     * @param    txNumber             The transaction Number
     * @return                    The corresponding POSLog Xml of
     *                                the given Transaction
     * @throws    DaoException     The Exception when  getting of
     *                                the POSLog XML failed.
     */
	String getTransaction(String txDeviceNumber, String txNumber) throws DaoException;

	/**
     * Get the BussinessDate from Administrator Database.
     * @param switchTime          The SwitchTime. Can be an empty string.
     * @return The BussinessDate  The Business Date.
     * @throws DaoException The exception thrown when error occur.
     */
	String getBussinessDate(String companyId, String storeId, String switchTime) throws DaoException;

	/**
     * Search transactions.
     *
     * @param limit					search results limit
     * @param from					search point (0: initial)
     * @param line                  journal line of index:0
     * @param storeId				the store id
     * @param deviceId				the device id
     * @param itemName				the item name
     * @param itemId				the item id
     * @param sequenceNumberFrom	the from/start transaction/sequence number
     * @param sequenceNumberTo		the to/end transaction/sequence number
     * @param businessDate			the business date
     * @param fromDate				the from/start date
     * @param fromTime				the from/start time
     * @param toDate				the to/end date
     * @param toTime				the to/end time
     * @param type					the transaction type
     *
     * @return 						the list of poslogxml string.
     *
     * @throws DaoException			thrown when database error occurs.
     * @throws ParseException		thrown when search key is not numeric
     * 									or valid date format.
     */
	List<TransactionSearch> searchTransactions(String limit, String from, String line,
	        String storeId, String deviceId, String itemName, String subCode4, String itemId,
	        String sequenceNumberFrom, String sequenceNumberTo, String businessDate, String fromDate,
			String fromTime, String toDate, String toTime, String type, String companyId, int trainingMode)
					throws Exception;

    /**
     * 前捌商品明細 PosLog 保存
     * @param posLog
     * @param posLogXml
     * @param queue
     * @param total
     * @return
     * @throws DaoException
     */
    public int saveForwardPosLog(PosLog posLog, String posLogXml, String queue, String total) throws DaoException;

    /**
     * 前捌商品明細 PosLog 検索
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param SequenceNumber
     * @param Queue
     * @param BusinessDayDate
     * @param TrainingFlag
     * @return 前捌登録 PosLog
     * @throws DaoException
     */
    public SearchForwardPosLog getForwardItemsPosLog(String CompanyId, String RetailStoreId,
            String WorkstationId, String SequenceNumber, String Queue,
            String BusinessDayDate, String TrainingFlag) throws DaoException;

    public String getLastPayTxPoslog(String companyId, String storeId,
    		String terminalId, String businessDate,
    		int trainingFlag) throws Exception;

    public String getLastBalancingTxPoslog(String companyId, String storeId,
    		String terminalId, String businessDate,
    		int trainingFlag) throws Exception;

    public AdditionalInformation getVoidedAndReturned(String companyid, String storeid, String workstationid,
            String businessdate, String txid, int trainingflag, String txtype) throws DaoException;

    /**
     * 取引ロックステータスの更新とチエック
     * @param Type     validate? lock? unlock?
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param SequenceNumber
     * @param BusinessDayDate
     * @param TrainingFlag
     * @param callType
     * @param appId
     * @param opeCode
     * @return ResultBase
     */
    public int getOrUpdLockStatus(String companyId, String retailStoreId, String workstationId, String businessDayDate,
            int sequenceNumber, int trainingFlag, String callType, String appId, String opeCode, String type) throws Exception;

    /**
     * additional info for transaction to identify
     * if the sales is already post pointed.
     * @param companyid
     * @param storeid
     * @param workstationid
     * @param businessdate
     * @param txid
     * @param trainingflag
     * @return
     * @throws DaoException
     */
    public PointPosted isPointPosted(String companyid, String storeid, String workstationid,
            String businessdate, String txid, int trainingflag) throws DaoException;

    public int getSummaryReceiptCount(String companyid,String retailStoreID,String workStationID,String sequenceNo,String businessDayDate)
            throws SQLException, SQLStatementException, DaoException;

    /**
     * タグ番号で前捌商品明細 PosLog 保存
     * @param posLog
     * @param posLogXml
     * @param queue
     * @param tag
     * @param total
     * @return
     * @throws DaoException
     */
    public int saveForwardPosLogIncludeTag(PosLog posLog, String posLogXml, String queue, String tag, String total) throws DaoException;
	
	/**
     * タグ番号で前捌商品明細 PosLog 検索
     * @param companyId
     * @param retailStoreId
     * @param queue
     * @param businessDayDate
     * @param tag
     * @return 前捌登録 PosLog
     * @throws DaoException
     */
    public SearchForwardPosLog getForwardItemsPosLogWithTag(String companyId, String retailStoreId,
    		String queue, String businessDayDate, String tag) throws DaoException;
}
