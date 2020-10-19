package ncr.res.mobilepos.pricing.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.PricePromInfo;

public interface IPricePromInfoDAO {

	/**
	 * Get PricePromInfo from MST_PRICE_PROM_INFO,MST_PRICE_PROM_DETAIL and MST_PRICE_PROM_STORE.
	 *
	 * @param companyId ,
	 *         storeId ,
	 *         dayDate ,
	 *         itemId ,
	 *         dpt ,
	 *         line ,
	 *         classId
	 * @return List<PricePromInfo>.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
    List<PricePromInfo> getPricePromInfoList(String companyId, String storeId, String dayDate, String itemId, String dpt, String line, String classId)
			throws DaoException;
    List<PricePromInfo> getPricePromInfoDpt(String companyId, String storeId, String businessDate)
			throws DaoException;
    
}

