package ncr.res.mobilepos.point.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.point.model.CashingUnit;
import ncr.res.mobilepos.point.model.ItemPointRate;
import ncr.res.mobilepos.point.model.PointInfo;
import ncr.res.mobilepos.point.model.TranPointRate;

public interface IPointDAO {

    List<ItemPointRate> getItemPointRate(String companyId, String storeId, String businessdate, String deptcode, String groupcode, String brandId, String barCode) throws Exception;
    List<TranPointRate> getTranPointRate(String companyId, String storeId, String businessdate) throws Exception;
    List<PointInfo> getPointInfoList(String companyId, String storeId, String businessdate) throws Exception;
    List<PointInfo> getTranPointInfoList(String companyId, String storeId, String businessdate) throws Exception;

    CashingUnit getCashingUnitInfo(String companyId, String businessdate) throws DaoException;
}
