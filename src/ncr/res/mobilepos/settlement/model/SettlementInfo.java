package ncr.res.mobilepos.settlement.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SettlementInfo")
@ApiModel(value="SettlementInfo")
public class SettlementInfo extends ResultBase {
    @XmlElement(name = "CreditInfo")
    private CreditInfo creditInfo;
    @XmlElement(name = "VoucherList")
    private List<VoucherInfo> voucherList;
    @XmlElement(name = "TxCount")
    private int txCount;
    @XmlElement(name = "PaymentAmtList")
    private List<PaymentAmtInfo> paymentAmtList;
    
    public final void setCreditInfo(CreditInfo creditInfo) {
    	this.creditInfo = creditInfo;
    }
    
    @ApiModelProperty(value="�N���W�b�g���", notes="�N���W�b�g���")
    public final CreditInfo getCreditInfo() {
    	return creditInfo;
    }
    
    public final void setVoucherList(List<VoucherInfo> voucherList) {
    	this.voucherList = voucherList;
    }
    
    @ApiModelProperty(value="���i�����X�g", notes="���i�����X�g")
    public final List<VoucherInfo> getVoucherList() {
    	return voucherList;
    }
    
    /**
     * @return the txCount
     */
    @ApiModelProperty(value="�����", notes="�����")
    public int getTxCount() {
        return txCount;
    }

    /**
     * @param txCount the txCount to set
     */
    public void setTxCount(int txCount) {
        this.txCount = txCount;
    }
    
    public final void setPaymentAmtList(List<PaymentAmtInfo> paymentAmtList) {
        this.paymentAmtList = paymentAmtList;
    }
    
    @ApiModelProperty(value="����W�v���z���X�g", notes="����W�v���z���X�g")
    public final List<PaymentAmtInfo> getPaymentAmtList() {
        return paymentAmtList;
    }

    @Override
    public final String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(super.toString()).append("; ");
    	sb.append("CreditInfo: ").append(creditInfo).append("; ");
    	sb.append("VoucherList: ").append(voucherList).append("; ");
    	sb.append("TxCount: ").append(txCount).append("; ");
    	sb.append("PaymentAmtList: ").append(paymentAmtList).append("; ");
    	return sb.toString();
    }
}
