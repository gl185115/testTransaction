package ncr.res.mobilepos.queuebuster.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "CashDrawer")
@ApiModel(value="CashDrawer")
public class CashDrawer extends ResultBase {
	
	private String companyId;
	
	private String cashOnHand;
	
	private String tillId;
	
	private String storeId;
	
	private String businessDayDate;
	
	private String operatorId;
	
	private String terminalId;
	
    @ApiModelProperty(value="�[���ԍ�", notes="�[���ԍ�")
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

    @ApiModelProperty(value="�c�Ɠ�", notes="�c�Ɠ�")
	public String getBusinessDayDate() {
		return businessDayDate;
	}

	public void setBusinessDayDate(String businessDate) {
		this.businessDayDate = businessDate;
	}

    @ApiModelProperty(value="�S���҃R�[�h", notes="�S���҃R�[�h")
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    @ApiModelProperty(value="��ɂ�������", notes="��ɂ�������")
	public String getCashOnHand() {
		return cashOnHand;
	}

	public void setCashOnHand(String cashOnHand) {
		this.cashOnHand = cashOnHand;
	}

    @ApiModelProperty(value="�h�����[�R�[�h", notes="�h�����[�R�[�h")
	public String getTillId() {
		return tillId;
	}

	public void setTillId(String tillId) {
		this.tillId = tillId;
	}

    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Override
	public String toString() {
		return "CashDrawer [companyId=" + companyId + ", cashOnHand=" + cashOnHand + ", tillId=" + tillId + ", storeId="
				+ storeId + ", businessDate=" + businessDayDate + ", operatorId=" + operatorId + "]";
	}

	
	

}
