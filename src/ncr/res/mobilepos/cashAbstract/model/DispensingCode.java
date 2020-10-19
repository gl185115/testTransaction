package ncr.res.mobilepos.cashAbstract.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlRootElement(name = "DispensingCode")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="DispensingCode")
public class DispensingCode {
	/**
     * CashFlowType number.
     */
    @XmlElement(name = "CashFlowType")
    private String cashFlowType;
    
	/**
     * RemarksCol.
     */
    @XmlElement(name = "RemarksCol")
    private String remarksCol;

	public String getCashFlowType() {
		return cashFlowType;
	}

	public void setCashFlowType(String cashFlowType) {
		this.cashFlowType = cashFlowType;
	}

	public String getRemarksCol() {
		return remarksCol;
	}

	public void setRemarksCol(String remarksCol) {
		this.remarksCol = remarksCol;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
        str.append("{CashFlowType :").append(cashFlowType).append(",")
        	.append("RemarksCol: ").append(remarksCol).append("}");
		return str.toString();
	}
    
}
