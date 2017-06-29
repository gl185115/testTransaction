/** ADD BGN 情報伝達機能 **/
package ncr.res.mobilepos.posmailinfo.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public interface IPOSMailInfoDAO {
	/**
     * @param companyId
     * @param storeId
     * @param workstationId
     * @param businessDate
     * @return posMailInfo
     * @throws DaoException
     */
	public JSONData getPOSMailInfo(String companyId, String retailStoreId, String workstationId, String businessDate) throws DaoException;
}
/** ADD END 情報伝達機能 **/