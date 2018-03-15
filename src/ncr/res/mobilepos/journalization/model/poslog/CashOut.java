package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * CashIn Model Object.
 *
 * <P>An CashIn Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CashOut")
public class CashOut {
    /**
     * CashOutAmount
     */
    @XmlElement(name = "CashOutAmount")
    private String cashOutAmount;

	/**
     * CashOutType
     */
    @XmlElement(name = "CashOutType")
    private String cashOutType;
    
    /**
     * CashOutName
     */
    @XmlElement(name = "CashOutName")
    private String cashOutName;
    
    /**
     * StampType
     */
    @XmlElement(name = "StampType")
    private String stampType;
    
    /**
     * RemarksId
     */
    @XmlElement(name = "RemarksId")
    private String remarksId;
    
    /**
     * RemarksCol
     */
    @XmlElement(name = "RemarksCol")
    private String remarksCol;
    
    /**
     * RemarksNote
     */
    @XmlElement(name = "RemarksNote")
    private String remarksNote;
    
    @XmlElement(name = "Count")
    private String count;
    
    @XmlElement(name = "Amount")
    private Amount amount;
    
    public final Amount getAmount() {
    	return this.amount;
    }
    public final void setAmount(Amount amt) {
    	this.amount = amt;
    }
    
    public final String getCount() {
    	return this.count;
    }
    public final void setCount(String cnt) {
    	this.count = cnt;
    }
    
    /**
     * @return the cashOutAmount
     */
	public final String getCashOutAmount() {
		return cashOutAmount;
	}
    
    /**
     * @param cashOutAmount the cashOutAmount to set
     */
	public final void setCashOutAmount(String cashOutAmount) {
		this.cashOutAmount = cashOutAmount;
	}
	
    /**
     * @return the cashOutType
     */
	public final String getCashOutType() {
		return cashOutType;
	}
    
    /**
     * @param cashOutType the cashOutType to set
     */
	public final void setCashOutType(String cashOutType) {
		this.cashOutType = cashOutType;
	}
	
    /**
     * @return the cashOutName
     */
	public final String getCashOutName() {
		return cashOutName;
	}
    
    /**
     * @param cashOutName the cashOutName to set
     */
	public final void setCashOutName(String cashOutName) {
		this.cashOutName = cashOutName;
	}
    
    /**
     * @return the stampType
     */
	public final String getStampType() {
		return stampType;
	}
    
    /**
     * @param stampType the stampType to set
     */
	public final void setStampType(String stampType) {
		this.stampType = stampType;
	}
	
    /**
     * @return the remarksId
     */
	public final String getRemarksId() {
		return remarksId;
	}
    
    /**
     * @param remarksId the remarksId to set
     */
	public final void setRemarksId(String remarksId) {
		this.remarksId = remarksId;
	}
	
    /**
     * @return the remarksCol
     */
	public final String getRemarksCol() {
		return remarksCol;
	}
    
    /**
     * @param remarksCol the remarksCol to set
     */
	public final void setRemarksCol(String remarksCol) {
		this.remarksCol = remarksCol;
	}
	
    /**
     * @return the remarksNote
     */
	public final String getRemarksNote() {
		return remarksNote;
	}

    /**
     * @param remarksNote the remarksNote to set
     */
	public final void setRemarksNote(String remarksNote) {
		this.remarksNote = remarksNote;
	}
}
