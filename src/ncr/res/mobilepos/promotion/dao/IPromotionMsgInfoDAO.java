package ncr.res.mobilepos.promotion.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.promotion.model.PromotionMsgInfo;

public interface IPromotionMsgInfoDAO {
	/**
	 * Get PromotionMsg from MST_PROMOTIONMSG_INFO, MST_PROMOTIONMSG_DETAIL and MST_PROMOTIONMSG_STORE.
	 *
	 * @param companyId ,
	 *         storeId ,
	 *         dayDate
	 * @return List<PromotionMsgInfo>.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	List<PromotionMsgInfo> getPromotionMsgInfoList(String companyId, String storeId, String dayDate) throws DaoException;

	/**
	 * Get dpt from MST_PROMOTIONMSG_DPT
	 *
	 * @param companyId ,
	 *         recordId
	 * @return dpt code list
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	List<String> getPromotionMsgDptList(String companyId, int recordId) throws DaoException;
}
