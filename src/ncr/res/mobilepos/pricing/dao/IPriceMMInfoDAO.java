package ncr.res.mobilepos.pricing.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;

public interface IPriceMMInfoDAO {

	/**
	 * Get PriceMMInfo from MST_PRICE_MM_INFO,MST_PRICE_MM_DETAIL and MST_PRICE_MM_STORE.
	 *
	 * @param companyId ,
	 *        storeId ,
	 *        dayDate ,
	 *        mdInternal
	 * @return List<PriceMMInfo>.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
    List<PriceMMInfo> getPriceMMInfoList(String companyId, String storeId, String dayDate, String mdInternal)
			throws DaoException;
}