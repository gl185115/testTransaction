package ncr.res.mobilepos.intaPay.model;

public class RefundQueryModel extends BaseModel {

    // �����X�ԋ��g�����U�N�V����ID
    private String mch_refund_id;

    // ISC�ԋ��g�����U�N�V����ID
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
