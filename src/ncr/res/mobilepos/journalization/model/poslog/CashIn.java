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
@XmlRootElement(name = "CashIn")
public class CashIn {
    /**
     * CashInAmount
     */
    @XmlElement(name = "CashInAmount")
    private String cashInAmount;

	/**
     * CashInType
     */
    @XmlElement(name = "CashInType")
    private String cashInType;
    
    /**
     * CashInName
     */
    @XmlElement(name = "CashInName")
    private String cashInName;
    
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
    
    /**
     * OriginalCashInType
     */
    @XmlElement(name = "OriginalCashInType")
    private String originalCashInType;
    
    /**
     * OriginalCashInName
     */
    @XmlElement(name = "OriginalCashInName")
    private String originalCashInName;
    
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
     * @return the cashInAmount
     */
	public final String getCashInAmount() {
		return cashInAmount;
	}
    
    /**
     * @param cashInAmount the cashInAmount to set
     */
	public final void setCashInAmount(String cashInAmount) {
		this.cashInAmount = cashInAmount;
	}
	
    /**
     * @return the cashInType
     */
	public final String getCashInType() {
		return cashInType;
	}
    
    /**
     * @param cashInType the cashInType to set
     */
	public final void setCashInType(String cashInType) {
		this.cashInType = cashInType;
	}
	
    /**
     * @return the cashInName
     */
	public final String getCashInName() {
		return cashInName;
	}
    
    /**
     * @param cashInName the cashInName to set
     */
	public final void setCashInName(String cashInName) {
		this.cashInName = cashInName;
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
    
    /**
     * @return the originalCashInType
     */
    public final String getOriginalCashInType() {
        return originalCashInType;
    }

    /**
     * @param originalCashInType the originalCashInType to set
     */
    public final void setOriginalCashInType(String originalCashInType) {
        this.originalCashInType = originalCashInType;
    }
    
    /**
     * @return the originalCashInName
     */
    public final String getOriginalCashInName() {
        return originalCashInName;
    }

    /**
     * @param originalCashInName the originalCashInName to set
     */
    public final void setOriginalCashInName(String originalCashInName) {
        this.originalCashInName = originalCashInName;
    }
}
