/**
 * 
 */
/**
 * @author xxi
 *
 */
package ncr.res.mobilepos.cashAbstract.dao;


import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.xebioapi.model.JSONData;

public interface ICashAbstractDAO {
    /**
     * 
     * @param companyId,
     *            storeId
     * @return OtherTender
     * @throws DaoException
     */
    public JSONData getcashAbstract(String companyId, String storeId, String cashFlowDirection) throws DaoException;
}