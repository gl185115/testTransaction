/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* ReportResource
*
* Web Service that provides Reports for the Mobile POS
*
* Campos, Carlos
*/
package ncr.res.mobilepos.queuebuster.dao;

import java.sql.Connection;
import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuebuster.model.BusteredTransaction;
import ncr.res.mobilepos.queuebuster.model.CashDrawer;
import ncr.res.mobilepos.queuebuster.model.ResumedTransaction;
import ncr.res.mobilepos.queuebuster.model.SuspendData;

/**
 * A DAO Interface for QueueBuster implementation.
 */
public interface IQueueBusterDAO {

    /**
     * Method used to save a transaction in the Queue.
     *
     * @param posLog			The posLog object
     * @param posLogXml			The posLog xml
     * @param queue				The queue id
     * @return                    The number of rows affected
     * @throws Exception        The Exception thrown when saving
     *                              of the poslog xml transaction fail
     */
    int saveTransactionToQueue(PosLog posLog, String posLogXml, String queue)
            throws DaoException;

    /**
     * Select a transaction from the Queue by specifying its
     * RetailStoreID, Queue, WorkstationID, and SequenceNumber.
     *
     * @param retailStoreId     The RetailStoreID
     * @param queue             The Queue
     * @param workStationId     The Terminal ID
     * @param sequenceNumber    The Sequence Number
     * @param businessDayDate	The BusinessDayDate
     * @param trainingFlag
     * @return                  The response object containing the resumed transaction
     * @throws Exception        The Exception when
     *                              the Selecting a transaction failed
     */
    ResumedTransaction selectTransactionFromQueue(String companyId, String retailStoreId, String queue,
			String workStationId, String sequenceNumber, String businessDayDate, int trainingFlag)
    throws DaoException;

    /**
     * Method used to get the oldest transaction from the Queue.
     *
     * @param retailStoreID       The Retail Store ID
     * @param queue               The Queue
     * @return the POSLog         xml of the oldest transaction in the Queue
     * @throws Exception        The exception thrown when the method fails
     */
    String selectOldestTransactionFromQueue(String retailStoreID,
            String queue) throws DaoException;

	/**
	 * List suspended transactions of the specified queue and store.
	 *
	 * @param retailStoreID
	 *            The StoreId of the transactions was made.
	 * @param queue
	 *            The QueueId of the transactions to be resumed.
	 * @param workstationId
	 *             The StationId of the transactions was made.
	 * @param trainingFlag
	 *             The trainingFlag of the transactions was made.
	 * @return The List of Suspended Transaction
	 * @throws Exception
	 *             The exception thrown when error occur.
	 */
	List<BusteredTransaction> listTransactionFromQueue(String queue, String companyId,
	        String retailStoreID, String workstationId, int trainingFlag) throws DaoException;

    //FOR DISNEY STORE QUEUE BUSTER ==========================

    /**
     * Method used to forward a transaction to the Queue.
     * @param poslogxml            The POSLog Xml of the transaction
     * @return                    The number of rows affected
     * @throws Exception        The Exception thrown when saving
     *                              of the poslog xml transaction fail
     */
    int forwardTransactionToQueue(String poslogxml) throws Exception;

    /**
     * Method used to update the status of a suspended transaction
     *  in the Queue from the POS.
     * @param retailstoreid     The RetailStoreID
     * @param txdate            The Date of the transaction
     * @param workstationid     The Terminal ID
     * @param txid              The Sequence Number
     * @param methodCode        The numeric equivalent of the request
     * @return                  0 if success and non-zero if not
     * @throws Exception        The Exception thrown when saving
     *                              of the poslog xml transaction fail
     */
    int updateQueuedTransactionStatus(String retailstoreid, String txdate,
            String workstationid, String txid, int methodCode) throws DaoException;

    /**
     * Select a transaction from the Queue by specifying its
     * RetailStoreID, Queue, WorkstationID, and SequenceNumber.
     *
     * This DAO method was intended for Disney Store Specification.
     *
     * @param retailStoreId     The RetailStoreID
     * @param workstationid     The Terminal ID
     * @param txid              The Sequence Number
     * @param txdate            The BusinessDate
     * @return                  The POSLog XML String
     *                              of the searched transaction
     * @throws Exception        The Exception when
     *                              the Selecting a transaction failed
     */
    SuspendData selectSuspendTransactionFromQueue(String retailStoreId,
            String txdate, String workstationid,
            String txid) throws DaoException;

    /**
     * determines if the change status/request to the
     * Queued Transaction is valid.
     *
     * Get request      : initial only.
     * Cancel request   : forwarded status only.
     * Complete request : forwarded status only.
     *
     * @param retailStoreId     The RetailStoreID
     * @param workstationid     The Terminal ID
     * @param txid              The Sequence Number
     * @param txdate            The BusinessDate
     * @param statusToSet       The status/request desired to set
     *                              the queued transaction to
     * @return                  0 if valid, non-zero if not
     * @throws Exception        The Exception when
     *                              the Selecting a transaction failed
     */
    int validateRequestToQueuedTransaction(String retailStoreId,
            String txdate, String workstationid,
            String txid, int statusToSet) throws DaoException;

	/**
     * Web Method call for Deleting ForWard Item.
     * @param storeId
     * @param businessDayDate
     * @return The JSON object that holds the
     *         ForWard Item Count for the Web Method.
     */
	ResultBase deleteForwardItem(String companyId, String storeId,
			String businessDayDate) throws DaoException;

	/**
	 * get the previous amount of SOD
	 * @param companyId
	 * @param storeId
	 * @param businessDate
	 * @return integer
	 * @throws DaoException
	 */
	CashDrawer getPreviousAmount(String companyId, String storeId) throws DaoException;

	/**
	 * update the SOD during journalization/ saving of poslog.
	 * update the sod amount
	 * @Param Connection
	 * @param cashDrawer
	 * @throws DaoException
	 */
	@Deprecated
	void updatePreviousAmount(Connection conn, CashDrawer cashDrawer) throws DaoException;

}
