package ncr.res.mobilepos.networkreceipt.dao;

import java.sql.ResultSet;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;

public interface IPastelPortLogDAO {
	
	public ResultSet getPastelPortLogData(final String storeId, final String txId, final String txDate) throws DaoException;

}
