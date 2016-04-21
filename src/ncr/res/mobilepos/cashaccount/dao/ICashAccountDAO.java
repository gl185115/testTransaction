package ncr.res.mobilepos.cashaccount.dao;

import ncr.res.mobilepos.cashaccount.model.GetCashBalance;
import ncr.res.mobilepos.exception.DaoException;

public interface ICashAccountDAO {
	
    /**
    * Get Cash Balance information
    * 
    * @param tillId				till id / drawer id
    * @param storeId			store id
    * @param businessDayDate	business day date
    * 
    * @return GetCashBalance
    * 
    * @throws DaoException
    */
	GetCashBalance getCashBalance(String tillId, String storeId, 
			String businessDayDate) throws DaoException;
	
	GetCashBalance getCashBalance(final String companyId, final String storeId,
            final String terminalId, final String businessDate, final int trainingFlag, final String dataType, 
            final String itemLevel1, final String itemLevel2) throws DaoException;

}
