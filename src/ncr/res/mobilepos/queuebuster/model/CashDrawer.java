package ncr.res.mobilepos.queuebuster.model;

import ncr.res.mobilepos.model.ResultBase;

public class CashDrawer extends ResultBase {
	
	private String companyId;
	
	private String cashOnHand;
	
	private String tillId;
	
	private String storeId;
	
	private String businessDayDate;
	
	private String operatorId;
	
	private String terminalId;
	
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getBusinessDayDate() {
		return businessDayDate;
	}

	public void setBusinessDayDate(String businessDate) {
		this.businessDayDate = businessDate;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCashOnHand() {
		return cashOnHand;
	}

	public void setCashOnHand(String cashOnHand) {
		this.cashOnHand = cashOnHand;
	}

	public String getTillId() {
		return tillId;
	}

	public void setTillId(String tillId) {
		this.tillId = tillId;
	}

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
