package ncr.res.mobilepos.uiconfig.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfo;
import ncr.res.mobilepos.uiconfig.model.store.StoreEntry;

/**
 * IUiConfigCommonDAO is a DAO interface for uiconfig.
 */
public interface IUiConfigCommonDAO {
	
	/**
     * @return return the Company information
     *
     * @throws DaoException
     *            Thrown when process fails.
     */
	List<CompanyInfo> getCompanyInfo() throws DaoException;
	
	/**
     * @return return the Company information
     *
     * @throws DaoException
     *            Thrown when process fails.
     */
	List<StoreEntry> getTableStoreIn(String companyId) throws DaoException;


}
