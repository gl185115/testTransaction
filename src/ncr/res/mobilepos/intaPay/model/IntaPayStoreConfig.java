package ncr.res.mobilepos.intaPay.model;

public class IntaPayStoreConfig {
    private String aPIKey;
    private String mchCode;
    /**
     * @return the aPIKey
     */
    public String getAPIKey() {
        return aPIKey;
    }
    /**
     * @param aPIKey the aPIKey to set
     */
    public void setAPIKey(String aPIKey) {
        this.aPIKey = aPIKey;
    }
    /**
     * @return the mchCode
     */
    public String getMchCode() {
        return mchCode;
    }
    /**
     * @param mchCode the mchCode to set
     */
    public void setMchCode(String mchCode) {
        this.mchCode = mchCode;
    }

}
