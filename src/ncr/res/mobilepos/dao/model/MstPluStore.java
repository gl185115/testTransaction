package ncr.res.mobilepos.dao.model;

public class MstPluStore {
    private String companyId;
    private String storeId;
    private String mdInternal;
    private long salesPrice;

    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMdInternal() {
        return mdInternal;
    }
    public void setMdInternal(String mdInternal) {
        this.mdInternal = mdInternal;
    }

    public double getSalesPrice() {
        return salesPrice;
    }
    public void setSalesPrice(long salesPrice) {
        this.salesPrice = salesPrice;
    }
}
