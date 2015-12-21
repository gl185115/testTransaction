package ncr.res.mobilepos.nationalityinfo.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.nationalityinfo.model.NationalityInfo;

public interface INationalityInfoDAO {

    List<NationalityInfo> getNationalityInfo(String companyId, String storeId) throws DaoException;

}
