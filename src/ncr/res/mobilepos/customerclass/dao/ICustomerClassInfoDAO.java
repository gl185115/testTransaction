package ncr.res.mobilepos.customerclass.dao;

import java.util.List;

import ncr.res.mobilepos.customerclass.model.CustomerClassInfo;
import ncr.res.mobilepos.exception.DaoException;

public interface ICustomerClassInfoDAO {

	List<CustomerClassInfo> getCustomerClassInfo(String companyId, String storeId) throws DaoException;

}
