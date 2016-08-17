package ncr.res.mobilepos.buyadditionalinfo.dao;

import java.util.List;

import ncr.res.mobilepos.buyadditionalinfo.model.BuyadditionalInfo;
import ncr.res.mobilepos.exception.DaoException;

public interface IBuyadditionalInfoDAO {

    List<BuyadditionalInfo> getBuyadditionalInfo(String companyId, String storeId) throws DaoException;

}
