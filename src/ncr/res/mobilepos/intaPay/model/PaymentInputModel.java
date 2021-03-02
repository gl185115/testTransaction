package ncr.res.mobilepos.intaPay.model;

public class PaymentInputModel extends BaseModel {

    private String mch_transaction_id;

    private String device_info;

    private String total_fee;

    private String goods_tag;

    private String auth_code;

    private String unique_distinction_id;

    private String body;

    public String getMch_transaction_id() {
        return mch_transaction_id;
    }

    public void setMch_transaction_id(String mch_transaction_id) {
        this.mch_transaction_id = mch_transaction_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public Integer getTotal_fee() {
        return Integer.valueOf(total_fee);
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getGoods_tag() {
        return goods_tag;
    }

    public void setGoods_tag(String goods_tag) {
        this.goods_tag = goods_tag;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public void setUnique_distinction_id(String unique_distinction_id) {
        this.unique_distinction_id = unique_distinction_id;
    }

    public String getUnique_distinction_id() {
        return unique_distinction_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
