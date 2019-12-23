/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IForwardItemListDAO
 *
 * IForwardItemListDAO is a DAO Interface for
 *  Transfer transactions between smart phone and POS.
 *
 */
package ncr.res.mobilepos.selfmode.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.selfmode.model.SelfMode;
import ncr.res.mobilepos.selfmode.model.SelfModeInfo;

/**
 * IForwardItemListDAO is a DAO Interface for Transfer transactions between
 * smart phone and POS.
 */
public interface ISelfModeDAO {
	ResultBase updateStatus(SelfMode selfmode) throws DaoException;

	SelfModeInfo getStatus(String companyId, String retailStoreId, String workstationId) throws DaoException;

}
