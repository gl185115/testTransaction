package ncr.res.mobilepos.creditpayment.dao;

import ncr.res.mobilepos.creditpayment.model.CreditPaymentDataList;
import ncr.res.mobilepos.exception.DaoException;

/**
 * ICreditPaymentDAO is a DAO interface for credit payment.
 */
public interface ICreditPaymentDAO {
    /**
     * Saves the Information of Credit Payment in the database.
     *
     */
	CreditPaymentDataList getCreditPayment() throws DaoException;
}
