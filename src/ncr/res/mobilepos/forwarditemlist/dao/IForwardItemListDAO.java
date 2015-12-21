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

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
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
}
