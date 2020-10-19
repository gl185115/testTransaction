package ncr.res.mobilepos.pricing.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.UrgentChangeItemInfo;

public interface IUrgentChangePriceInfoDAO {

	/**
	 * Get UrgentChangePriceInfo from MST_PLU,MST_PLU_URGENT,MST_PRICE_PROM_INFO,MST_PRICE_PROM_DETAIL and MST_PRICE_PROM_STORE.
	 *
	 * @param  companyId ,
	 *         storeId ,
	 *         mdInternal ,
	 *         bizDate ,
	 *         bizDateTime
	 * @return UrgentChangeItemInfo.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	UrgentChangeItemInfo getUrgentChangePriceInfo(String companyId,String storeId,String mdInternal,String bizDate,String bizDateTime)
			throws DaoException;
}

