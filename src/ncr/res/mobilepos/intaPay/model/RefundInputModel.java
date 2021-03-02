package ncr.res.mobilepos.intaPay.model;

public class RefundInputModel extends BaseModel {

    // 加盟店支払トランザクションID
    private String mch_transaction_id;

    // ISC支払トランザクションID
    private String isc_transaction_id;

    // 加盟店返金トランザクションID
    private String mch_refund_id;

    // 請求返金金額
    private String refund_fee;

    // 二重取引チェック文字列
    private String unique_distinction_id;

    public String getMch_transaction_id() {
        return mch_transaction_id;
    }

    public void setMch_transaction_id(String mch_transaction_id) {
        this.mch_transaction_id = mch_transaction_id;
    }

    public String getMch_refund_id() {
        return mch_refund_id;
    }

    public void setMch_refund_id(String mch_refund_id) {
        this.mch_refund_id = mch_refund_id;
    }

    public Integer getRefund_fee() {
        return Integer.valueOf(refund_fee);
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getUnique_distinction_id() {
        return unique_distinction_id;
    }

    public void setUnique_distinction_id(String unique_distinction_id) {
        this.unique_distinction_id = unique_distinction_id;
    }

    public String getIsc_transaction_id() {
        return isc_transaction_id;
    }

    public void setIsc_transaction_id(String isc_transaction_id) {
        this.isc_transaction_id = isc_transaction_id;
    }
}
