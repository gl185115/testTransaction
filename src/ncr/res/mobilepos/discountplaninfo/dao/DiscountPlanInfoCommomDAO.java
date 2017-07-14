package ncr.res.mobilepos.discountplaninfo.dao;

import java.util.ArrayList;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.discountplaninfo.model.SubtotalDiscountInfo;

public interface DiscountPlanInfoCommomDAO {
	/**
     * @param SubtotalDiscount
     * @return the SubtotalDiscount information
     * @throws DaoException
     *             Thrown when process fails.
     */
    ArrayList<SubtotalDiscountInfo> getSubtotalDiscount()
            throws DaoException;
}
