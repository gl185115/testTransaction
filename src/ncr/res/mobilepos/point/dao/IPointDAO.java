package ncr.res.mobilepos.point.dao;

import java.util.List;

import ncr.res.mobilepos.point.model.ItemPointRate;
import ncr.res.mobilepos.point.model.TranPointRate;

public interface IPointDAO {

    List<ItemPointRate> getItemPointRate(String companyId, String storeId, String businessdate, String deptcode, String groupcode, String brandId, String sku) throws Exception;
    List<TranPointRate> getTranPointRate(String companyId, String storeId, String businessdate) throws Exception;

}
