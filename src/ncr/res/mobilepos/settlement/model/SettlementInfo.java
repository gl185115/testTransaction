package ncr.res.mobilepos.settlement.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    public final void setCreditInfo(CreditInfo creditInfo) {
    	this.creditInfo = creditInfo;
    }

    @ApiModelProperty(value="クレジット情報", notes="クレジット情報")
    public final CreditInfo getCreditInfo() {
    	return creditInfo;
    }

    public final void setVoucherList(List<VoucherInfo> voucherList) {
    	this.voucherList = voucherList;
    }

    @ApiModelProperty(value="商品券リスト", notes="商品券リスト")
    public final List<VoucherInfo> getVoucherList() {
    	return voucherList;
    }

    /**
     * @return the txCount
     */
    @ApiModelProperty(value="取引数", notes="取引数")
    public int getTxCount() {
        return txCount;
    }

    /**
     * @param txCount the txCount to set
     */
    public void setTxCount(int txCount) {
        this.txCount = txCount;
    }

    @Override
    public final String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(super.toString()).append("; ");
    	sb.append("CreditInfo: ").append(creditInfo).append("; ");
    	sb.append("VoucherList: ").append(voucherList).append("; ");
    	sb.append("TxCount: ").append(txCount).append("; ");
    	return sb.toString();
    }
}
