package ncr.res.mobilepos.networkreceipt.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TxPrintType")
public class TxPrintTypes {
	
	@XmlElement(name = "txType")
	private String txType;
	@XmlElement(name = "printStoreReceipt")
	private String printStoreReceipt;
	@XmlElement(name = "printNonCashReceipt")
	private String printNonCashReceipt;
	@XmlElement(name = "printCashReceipt")
	private String printCashReceipt;
	
	public String getTxType() {
		return this.txType;
	}
	public void setTxType(String value) {
		this.txType = value;
	}
	
	public String getPrintStoreReceipt() {
		return this.printStoreReceipt;
	}
	public void setPrintStoreReceipt(String value) {
		this.printStoreReceipt = value;
	}
	
	public String getPrintNonCashReceipt(){
		return this.printNonCashReceipt;
	}
	public void setPrintNonCashReceipt(String value) {
		this.printNonCashReceipt = value;
	}
	
	public String getPrintCashReceipt() {
		return this.printCashReceipt;
	}
	public void setPrintDCashReceipt(String value) {
		this.printCashReceipt = value;
	}
	
}