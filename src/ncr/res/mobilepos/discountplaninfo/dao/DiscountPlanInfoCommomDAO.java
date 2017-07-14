package ncr.res.mobilepos.discountplaninfo.dao;

import java.util.ArrayList;

import ncr.res.mobilepos.discountplaninfo.model.SubtotalDiscountInfo;
import ncr.res.mobilepos.exception.DaoException;

public interface DiscountPlanInfoCommomDAO {
	/**
     * @param SubtotalDiscount
     * @param companyId
     * @param storeId
     * @return the SubtotalDiscount information
     * @throws DaoException
     *             Thrown when process fails.
     */
    ArrayList<SubtotalDiscountInfo> getSubtotalDiscount(String companyId, String storeId)
            throws DaoException;
}
