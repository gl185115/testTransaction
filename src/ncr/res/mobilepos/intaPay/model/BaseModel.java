package ncr.res.mobilepos.intaPay.model;

public class BaseModel {

    private String machant_id;

    private String nonce_str;

    private String sign_type;

    private String sign;

    public String getMachant_id() {
        return machant_id;
    }

    public void setMachant_id(String machant_id) {
        this.machant_id = machant_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
