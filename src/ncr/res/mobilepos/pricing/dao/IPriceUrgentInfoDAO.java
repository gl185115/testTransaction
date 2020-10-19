package ncr.res.mobilepos.pricing.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.PriceUrgentInfo;

public interface IPriceUrgentInfoDAO {

	/**
	 * Get PriceUrgentInfo from .
	 *
	 * @param companyId ,
	 *        storeId ,
	 *        mdInternal
	 * @return List<PriceUrgentInfo>.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
    List<PriceUrgentInfo> getPriceUrgentInfoList(String companyId, String storeId, String mdInternal)
			throws DaoException;
}