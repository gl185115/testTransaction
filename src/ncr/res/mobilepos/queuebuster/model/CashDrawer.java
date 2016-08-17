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
	
    @ApiModelProperty(value="端末番号", notes="端末番号")
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

    @ApiModelProperty(value="営業日", notes="営業日")
	public String getBusinessDayDate() {
		return businessDayDate;
	}

	public void setBusinessDayDate(String businessDate) {
		this.businessDayDate = businessDate;
	}

    @ApiModelProperty(value="担当者コード", notes="担当者コード")
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

    @ApiModelProperty(value="会社コード", notes="会社コード")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    @ApiModelProperty(value="手にした現金", notes="手にした現金")
	public String getCashOnHand() {
		return cashOnHand;
	}

	public void setCashOnHand(String cashOnHand) {
		this.cashOnHand = cashOnHand;
	}

    @ApiModelProperty(value="ドロワーコード", notes="ドロワーコード")
	public String getTillId() {
		return tillId;
	}

	public void setTillId(String tillId) {
		this.tillId = tillId;
	}

    @ApiModelProperty(value="店舗コード", notes="店舗コード")
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
