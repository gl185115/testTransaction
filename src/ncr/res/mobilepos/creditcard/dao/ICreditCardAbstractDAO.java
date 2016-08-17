/**
 * 
 */
/**
 * @author wml
 *
 */
package ncr.res.mobilepos.creditcard.dao;


import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.xebioapi.model.JSONData;

public interface ICreditCardAbstractDAO {
    /**
     * 
     * @param companyId,
     * @return CreditCardCompayInfo
     * @throws DaoException
     */
    public JSONData getCreditCardCompayInfo(String companyId) throws DaoException;
}