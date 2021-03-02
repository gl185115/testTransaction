package ncr.res.mobilepos.intaPay.model;

import ncr.res.mobilepos.intaPay.constants.IntaPayConstants;

public class IntaPaySystemConfig {

    private Boolean certuse = IntaPayConstants.DEFAULT_CERTUSE;
    private Boolean debug = IntaPayConstants.DEFAULT_DEBUG_FLAG;
    private String gateway_server_url = IntaPayConstants.DEFAULT_GATEWAY_SERVER_URL;
    private String certpw = IntaPayConstants.DEFAULT_CERTPW;
    private String certfilepath = IntaPayConstants.DEFAULT_CERTFILEPATH;
    private String apiKey = IntaPayConstants.DEFAULT_API_KEY;
    private String mchCode = IntaPayConstants.DEFAULT_MCH_CODE;
    private String bodyMessage = IntaPayConstants.DEFAULT_BODY;
    private String device_info = IntaPayConstants.DEFAULT_DEVICE_INFO;
    /**
     * @return the certuse
     */
    public Boolean isCertuse() {
        return certuse;
    }
    /**
     * @param certuse the certuse to set
     */
    public void setCertuse(Boolean certuse) {
        this.certuse = certuse;
    }
    
    /**
     * @return the debugFlag
     */
    public Boolean isDebug() {
        return debug;
    }

    /**
     * @param debugFlag the debugFlag to set
     */
    public void setDebugFlag(Boolean debug) {
        this.debug = debug;
    }

    /**
     * @return the gateway_server_url
     */
    public String getGateway_server_url() {
        return gateway_server_url;
    }
    /**
     * @param gateway_server_url the gateway_server_url to set
     */
    public void setGateway_server_url(String gateway_server_url) {
        this.gateway_server_url = gateway_server_url;
    }
    /**
     * @return the certpw
     */
    public String getCertpw() {
        return certpw;
    }
    /**
     * @param certpw the certpw to set
     */
    public void setCertpw(String certpw) {
        this.certpw = certpw;
    }
    /**
     * @return the certfilepath
     */
    public String getCertfilepath() {
        return certfilepath;
    }
    /**
     * @param certfilepath the certfilepath to set
     */
    public void setCertfilepath(String certfilepath) {
        this.certfilepath = certfilepath;
    }
    /**
     * @return the apiKey
     */
    public String getApiKey() {
        return apiKey;
    }
    /**
     * @param apiKey the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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

    /**
     * @return the bodyMessage
     */
    public String getBodyMessage() {
        return bodyMessage;
    }

    /**
     * @param bodyMessage the bodyMessage to set
     */
    public void setBodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }

    /**
     * @return the device_info
     */
    public String getDevice_info() {
        return device_info;
    }

    /**
     * @param device_info the device_info to set
     */
    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

}
