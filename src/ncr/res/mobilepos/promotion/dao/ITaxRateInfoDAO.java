package ncr.res.mobilepos.promotion.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.TaxRateInfo;

public interface ITaxRateInfoDAO {

	/**
	 * Get PromotionId from MST_TAXRATE
	 *
	 * @param bussinessDate
	 * @return List<TaxRateInfo>.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	List<TaxRateInfo> getTaxRateInfoList(String bussinessDate) throws DaoException;
}