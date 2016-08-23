package ncr.res.mobilepos.dao.model;

/**
 * Model for inner joined table of MST_PRICE_URGENT_INFO_FORSTORE and MST_PRICE_URGENT_STORE_FORSTORE.
 */
public class MstPriceUrgentForStore {

    private long sequenceNo;
    private String companyId;
    private String recordId;
    private String sku;
    private String colorId;
    private long newPrice;
    private long oldPrice;
    private String targetStoreType;
    private String storeId;

    public long getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(long sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public long getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(long newPrice) {
        this.newPrice = newPrice;
    }

    public long getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getTargetStoreType() {
        return targetStoreType;
    }

    public void setTargetStoreType(String targetStoreType) {
        this.targetStoreType = targetStoreType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

}
