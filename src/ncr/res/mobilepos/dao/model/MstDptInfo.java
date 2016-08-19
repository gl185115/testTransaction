package ncr.res.mobilepos.dao.model;

public class MstDptInfo {

    private String companyId;
    private String storeId;
    private String dpt;
    private String dptName;
    private String dptNameLocal;
    private String dptKanaName;

    private int taxType;
    private int taxRate;
    private int discountType;
    private int exceptionFlag;

    private int subNum1;
    private int subNum2;
    private int subNum3;
    private int subNum4;

    private String status;

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

    public String getDpt() {
        return dpt;
    }

    public void setDpt(String dpt) {
        this.dpt = dpt;
    }

    public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public String getDptNameLocal() {
        return dptNameLocal;
    }

    public void setDptNameLocal(String dptNameLocal) {
        this.dptNameLocal = dptNameLocal;
    }

    public String getDptKanaName() {
        return dptKanaName;
    }

    public void setDptKanaName(String dptKanaName) {
        this.dptKanaName = dptKanaName;
    }

    public int getTaxType() {
        return taxType;
    }

    public void setTaxType(int taxType) {
        this.taxType = taxType;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public int getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(int exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    public int getSubNum1() {
        return subNum1;
    }

    public void setSubNum1(int subNum1) {
        this.subNum1 = subNum1;
    }

    public int getSubNum2() {
        return subNum2;
    }

    public void setSubNum2(int subNum2) {
        this.subNum2 = subNum2;
    }

    public int getSubNum3() {
        return subNum3;
    }

    public void setSubNum3(int subNum3) {
        this.subNum3 = subNum3;
    }

    public int getSubNum4() {
        return subNum4;
    }

    public void setSubNum4(int subNum4) {
        this.subNum4 = subNum4;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
