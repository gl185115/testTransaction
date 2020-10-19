package ncr.res.mobilepos.selfmode.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SelfMode")
@ApiModel(value = "SelfMode")
public class SelfMode {
	@XmlElement(name = "CompanyId")
	private String companyId = null;

	@XmlElement(name = "RetailStoreId")
	private String retailStoreId = null;

	@XmlElement(name = "WorkstationId")
	private String workstationId = null;

	@XmlElement(name = "Training")
	private int training = 0;

	@XmlElement(name = "Status")
	private int status = 0;

	@XmlElement(name = "Detail")
	private int detail = 0;

	@XmlElement(name = "Printer")
	private int printer = 0;

	@XmlElement(name = "CashChanger")
	private int cashChanger = 0;

	@XmlElement(name = "CashChangerCount")
	private String cashChangerCount = null;

	@XmlElement(name = "CashChangerCountStatus")
	private String cashChangerCountStatus = null;

	@XmlElement(name = "Message")
	private String message = null;

	@XmlElement(name = "Alert")
	private int alert = 0;

	@XmlElement(name = "UpdateDateTime")
	private String updateDateTime = null;
	
	@XmlElement(name = "Total")
	private String total = null;
	
	@XmlElement(name = "ItemCount")
	private String itemCount = null;
	
	@ApiModelProperty(value = "企業コード", notes = "企業コード")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty(value = "店舗番号", notes = "店舗番号")
	public String getRetailStoreId() {
		return retailStoreId;
	}

	public void setRetailStoreId(String retailStoreId) {
		this.retailStoreId = retailStoreId;
	}

	@ApiModelProperty(value = "レジ番号", notes = "レジ番号")
	public String getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(String workstationId) {
		this.workstationId = workstationId;
	}

	@ApiModelProperty(value = "トレーニングモード状態（テーブル定義参照）", notes = "トレーニングモード状態（テーブル定義参照）")
	public int getTraining() {
		return training;
	}

	public void setTraining(int training) {
		this.training = training;
	}

	@ApiModelProperty(value = "セルフモード状態（テーブル定義参照）", notes = "セルフモード状態（テーブル定義参照）")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@ApiModelProperty(value = "セルフモード状態詳細（テーブル定義参照）", notes = "セルフモード状態詳細（テーブル定義参照）")
	public int getDetail() {
		return detail;
	}

	public void setDetail(int detail) {
		this.detail = detail;
	}

	@ApiModelProperty(value = "プリンター状態（テーブル定義参照）", notes = "プリンター状態（テーブル定義参照）")
	public int getPrinter() {
		return printer;
	}

	public void setPrinter(int printer) {
		this.printer = printer;
	}

	@ApiModelProperty(value = "釣銭機状態（テーブル定義参照）", notes = "釣銭機状態（テーブル定義参照）")
	public int getCashChanger() {
		return cashChanger;
	}

	public void setCashChanger(int cashChanger) {
		this.cashChanger = cashChanger;
	}

	@ApiModelProperty(value = "釣銭機在高 （テーブル定義参照）", notes = "釣銭機在高 （テーブル定義参照）")
	public String getCashChangerCount() {
		return cashChangerCount;
	}

	public void setCashChangerCount(String cashChangerCount) {
		this.cashChangerCount = cashChangerCount;
	}

	@ApiModelProperty(value = "釣銭機在高状態 （テーブル定義参照）", notes = "釣銭機在高状態 （テーブル定義参照）")
	public String getCashChangerCountStatus() {
		return cashChangerCountStatus;
	}

	public void setCashChangerCountStatus(String cashChangerCountStatus) {
		this.cashChangerCountStatus = cashChangerCountStatus;
	}

	@ApiModelProperty(value = "通知文言", notes = "通知文言")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@ApiModelProperty(value = "警告フラグ", notes = "警告フラグ")
	public int getAlert() {
		return alert;
	}

	public void setAlert(int alert) {
		this.alert = alert;
	}

	@ApiModelProperty(value = "システム日時", notes = "システム日時")
	public String getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(String updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	
	@ApiModelProperty(value = "合計", notes = "合計")
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	@ApiModelProperty(value = "点数", notes = "点数")
	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	@Override
	public String toString() {
		return "SelfMode [companyId=" + companyId + ", retailStoreId=" + retailStoreId + ", workstationId="
				+ workstationId + ", status=" + status + ", detail=" + detail + ", printer=" + printer
				+ ", cashChanger=" + cashChanger + ", cashChangerCount=" + cashChangerCount
				+ ", cashChangerCountStatus=" + cashChangerCountStatus + ", message=" + message + ", alert=" + alert
				+ ", updateDateTime=" + updateDateTime + ", total=" + total + ", itemCount=" + itemCount + "]";
	}

}
