package ncr.res.mobilepos.intaPay.model;

public class RefundQueryModel extends BaseModel {

    // 加盟店返金トランザクションID
    private String mch_refund_id;

    // ISC返金トランザクションID
    private String isc_refund_id;

    public String getMch_refund_id() {
        return mch_refund_id;
    }

    public void setMch_refund_id(String mch_refund_id) {
        this.mch_refund_id = mch_refund_id;
    }

    public String getIsc_refund_id() {
        return isc_refund_id;
    }

    public void setIsc_refund_id(String isc_refund_id) {
        this.isc_refund_id = isc_refund_id;
    }
}
