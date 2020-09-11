
package ncr.res.mobilepos.pricing.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.promotion.model.PmItemInfo;

public interface IPMInfoDAO {
	
	
	/**
	 *  get the PM info
	 * @param companyId 会社コード
	 * @param storeId 店舗コード
	 * @param mdInternal itemId
	 * @param businessDate current day
	 * @return PM info
	 * @throws DaoException exception
	 */
	List<PmItemInfo> getPmItemInfo(String companyId, String storeId, String mdInternal, String businessDate) throws DaoException;

}
