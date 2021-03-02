package ncr.res.mobilepos.intaPay.model;

public class PaymentQueryModel extends BaseModel {

    // 加盟店支払トランザクションID
    private String mch_transaction_id;

    // ISC支払トランザクションID
    private String isc_transaction_id;

    public String getMCH_transaction_id() {
        return mch_transaction_id;
    }

    public void setMCH_transaction_id(String mch_transaction_id) {
        this.mch_transaction_id = mch_transaction_id;
    }

    public String getISC_transaction_id() {
        return isc_transaction_id;
    }

    public void setISC_transaction_id(String isc_transaction_id) {
        this.isc_transaction_id = isc_transaction_id;
    }
}