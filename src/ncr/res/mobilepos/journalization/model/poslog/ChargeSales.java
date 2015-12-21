package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ChargeSales Model Object.
 *
 * <P>A ChargeSales Node in POSLog XML.
 *
 * <P>The ChargeSales node is under RetailTransaction Node.
 * And holds the ChargeSales transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ChargeSales")
public class ChargeSales {
    @XmlElement(name = "SlipNo")
    private String slipNo;
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;
    @XmlElement(name = "WorkstationID")
    private String workstationID;
    @XmlElement(name = "BusinessDayDate")
    private String businessDayDate;
    @XmlElement(name = "ChageSalesAmt")
    private String chageSalesAmt;
    @XmlElement(name = "PaymentAmt")
    private String paymentAmt;
    @XmlElement(name = "PreviousBalance")
    private String previousBalance;
    @XmlElement(name = "CurPaymentAmt")
    private String curPaymentAmt;
    public String getSlipNo() {
        return slipNo;
    }
    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }
    public String getRetailStoreID() {
        return retailStoreID;
    }
    public void setRetailStoreID(String retailStoreID) {
        this.retailStoreID = retailStoreID;
    }
    public String getWorkstationID() {
        return workstationID;
    }
    public void setWorkstationID(String workstationID) {
        this.workstationID = workstationID;
    }
    public String getBusinessDayDate() {
        return businessDayDate;
    }
    public void setBusinessDayDate(String businessDayDate) {
        this.businessDayDate = businessDayDate;
    }
    public String getChageSalesAmt() {
        return chageSalesAmt;
    }
    public void setChageSalesAmt(String chageSalesAmt) {
        this.chageSalesAmt = chageSalesAmt;
    }
    public String getPaymentAmt() {
        return paymentAmt;
    }
    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }
    public String getPreviousBalance() {
        return previousBalance;
    }
    public void setPreviousBalance(String previousBalance) {
        this.previousBalance = previousBalance;
    }
    public String getCurPaymentAmt() {
        return curPaymentAmt;
    }
    public void setCurPaymentAmt(String curPaymentAmt) {
        this.curPaymentAmt = curPaymentAmt;
    }
    @Override
    public String toString() {
        return "ChargeSales [slipNo=" + slipNo + ", retailStoreID=" + retailStoreID + ", workstationID=" + workstationID
                + ", businessDayDate=" + businessDayDate + ", chageSalesAmt=" + chageSalesAmt + ", paymentAmt="
                + paymentAmt + ", previousBalance=" + previousBalance + ", curPaymentAmt=" + curPaymentAmt + "]";
    }
    
}