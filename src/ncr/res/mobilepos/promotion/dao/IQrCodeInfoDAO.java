package ncr.res.mobilepos.promotion.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;

public interface IQrCodeInfoDAO {

	/**
	 * Get PromotionId from MST_QRCODE_INFO and MST_QRCODE_STORE
	 * 
	 * @param companyId ,
	 *        CCode
	 * @return List<QrCodeInfo>.
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	List<QrCodeInfo> getQrCodeInfoList(String companyId, String code, String dayDate) 
			throws DaoException;

	/**
	 * Check customerId in MST_QRCODE_QRCODE_MEMBERID
	 * 
	 * @param companyId ,
	 *        promotionId,
	 *        customerId
	 * @return rightCustomerId
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	String getCustomerQrCodeInfoList(String companyId, String promotionId, String customerId) 
			throws DaoException;
}