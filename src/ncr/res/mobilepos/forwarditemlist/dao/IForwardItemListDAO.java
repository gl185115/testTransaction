/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IForwardItemListDAO
 *
 * IForwardItemListDAO is a DAO Interface for
 *  Transfer transactions between smart phone and POS.
 *
 */
package ncr.res.mobilepos.forwarditemlist.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.forwarditemlist.model.ForwardvoidListInfo;
import ncr.res.mobilepos.journalization.model.PosLogResp;

/**
 * IForwardItemListDAO is a DAO Interface for
 * Transfer transactions between smart phone and POS.
 */
public interface IForwardItemListDAO {

    /**
     * Get Shopping Cart Data in the TXL_FORWARD_ITEM table
     * by store id and terminal id and txdate.
     *
     * @param storeid Store number of POS
     * @param terminalid Terminal number of POS
     * @param txdate Business date of POS
     *
     * @return Shopping Cart Data
     * @exception DaoException The exception thrown when error occur.
     */
    String getShoppingCartData(String storeid, String terminalid,
            String txdate)throws DaoException;

    /**
     * Get the Forward Count data XML in the TXL_FORWARD_ITEM table
     * by store id and terminal id and txdate.
     *
     * @param storeid Store number of POS
     * @param terminalid Terminal number of POS
     * @param txdate Business date of POS
     *
     * @return ForwardCountData
     * @exception DaoException The exception thrown when error occur.
     */
    ForwardCountData getForwardCountData(String storeid,
            String terminalid, String txdate)throws DaoException;

    /**
     * Upload the Item Forward data to DB(TXL_FORWARD_ITEM).
     *
     * @param deviceNo   Device number
     * @param terminalNo The Terminal Number
     * @param poslogXml  PosLog in XML format
     * @exception DaoException The exception thrown when error occur.
     * @return The POS Log Response {@see PosLogResp}
     */
    PosLogResp uploadItemForwardData(String deviceNo, String terminalNo,
            String poslogXml) throws DaoException;

    /**
     * Get the Forward Item Count in the TXL_FORWARD_ITEM table
     * by company id and store id and business date
     *  and workstation id and queue and training flag.
     * @param companyId
     * @param storeId
     * @param businessDayDate
     * @param workstationId
     * @param queue
	 * @param trainingFlag
     * @exception DaoException The exception thrown when error occur.
     * @return Forward Item Count
     */
	String selectForwardItemCount(String companyId, String storeId,
            String businessDayDate, String workstationId, String queue, String trainingFlag) throws DaoException;

	 /**
	    * 呼出取消データ情報一覧取得
	    * @param companyId
	    * @param retailStoreId
	    * @param workStationId
	    * @param trainingFlag
	    * @return一覧リスト
	    * @throws DaoException
	    */
	List<ForwardvoidListInfo> getForwardResumeVoidList (String companyId, String retailStoreId, String workStationId, String trainingFlag) throws DaoException;

	/**
	    * 前捌保留件数取得（精算機ごと）
	    * @param companyId
	    * @param retailStoreId
	    * @param cashierId
	    * @param trainingFlag
	    * @return Forward Count With Cashier
	    * @throws DaoException
	    */
	String getForwardCountWithCashier(String companyId, String retailStoreId, String cashierId, String trainingFlag) throws DaoException;
}
