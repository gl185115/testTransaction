package ncr.res.mobilepos.uiconfig.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfo;

/**
 * IUiConfigCommonDAO is a DAO interface for uiconfig.
 */
public interface IUiConfigCommonDAO {
	
	List<CompanyInfo> getCompanyInfo() throws DaoException;
}
