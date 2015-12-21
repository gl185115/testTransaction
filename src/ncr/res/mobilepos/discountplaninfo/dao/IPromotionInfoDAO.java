package ncr.res.mobilepos.discountplaninfo.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.xebioapi.model.JSONData;

public interface IPromotionInfoDAO {
    /**
     * @param companyId
     * @param storeId
     * @param discountReason
     * @param discountBarcodeType
     * @param partialFlag
     * @param priceDiscountFlag
     * @param rateDiscountFlag
     * @return promotion
     * @throws DaoException
     */
    public JSONData getPromotionInfo(String companyId, String storeId, String discountReason,
            String discountBarcodeType, String partialFlag, String priceDiscountFlag, String rateDiscountFlag)
                    throws DaoException;
}