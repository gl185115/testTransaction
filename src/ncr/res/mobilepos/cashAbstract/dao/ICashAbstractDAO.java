/**
 * 
 */
/**
 * @author xxi
 *
 */
package ncr.res.mobilepos.cashAbstract.dao;


import ncr.res.mobilepos.cashAbstract.model.DispensingCodeList;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public interface ICashAbstractDAO {
    /**
     * 
     * @param companyId,
     *            storeId
     * @return OtherTender
     * @throws DaoException
     */
    public JSONData getcashAbstract(String companyId, String storeId, String cashFlowDirection, String tenderId, String tenderType) throws DaoException;
    
    /**
     * 
     * @param companyId,
     *            retailStoreId
     * @return DispensingCodeList
     * @throws DaoException
     */
    public DispensingCodeList getDispensingCreditCode(String companyId,String retailStoreId) throws DaoException;
}