package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TenderControlTransaction Model Object.
 *
 * <P>A TenderControlTransaction Node in POSLog XML.
 *
 * <P>The TenderControlTransaction node is under Transaction Node.
 * And holds the cash drawer transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderControlTransaction")
public class TenderControlTransaction {

    /**
     * Attribute that tells the version
     */
    @XmlAttribute(name = "Version")
    private String version;

    /**
     * Element for start of day and end of day
     */
    @XmlElement(name = "TillSettle")
    private TillSettle tillSettle;

    /**
     * Element that tells the loan details
     */
    @XmlElement(name = "TenderLoan")
    private TenderLoan tenderLoan;

    /**
     * Element that tells the pickup details
     */
    @XmlElement(name = "TenderPickup")
    private TenderPickup tenderPickup;

    /**
     * Element that tells TenderExchange
     */
    @XmlElement(name = "TenderExchange")
    private TenderExchange tenderExchange;

    /**
     * Element that tells the pickup details
     */
    @XmlElement(name = "CashToDrawer")
    private CashToDrawer cashToDrawer;
        
    /**
     * Element that tells the Guarantee details
     */
    @XmlElement(name = "Guarantee")
    private Guarantee guarantee;
    
    /**
     * dayPart
     */
    @XmlElement(name = "DayPart")
    private String dayPart;
    /**
     * Gets the dayPart of the tender control transaction
     *
     * @return              The dayPart of the tender control transaction
     */
    public final String getDayPart() {
        return dayPart;
    }
    /**
     *  Associate Poslog Element
     */
    @XmlElement(name = "Associate")
    private Associate associate;
    
    /**
     * @return the associate
     */
    public final Associate getAssociate() {
        return associate;
    }
    /**
     * @param associate the associate to set
     */
    public final void setAssociate(Associate associate) {
        this.associate = associate;
    }
    
    /**
     * Sets the dayPart of the tender control transaction
     *
     * @param dayPart       The new version to set
     */
    public final void setDayPart(String dayPart) {
        this.dayPart = dayPart;
    }

    /**
     * Gets the version of the tender control transaction
     *
     * @return          	The version of the tender control transaction
     */
    public final String getVersion() {
        return version;
    }

    /**
     * Sets the version of the tender control transaction
     *
     * @param version		The new version to set
     */
    public final void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the till settle details of the tender control transaction
     *
     * @return          	The till settle details of the tender control transaction
     */
    public final TillSettle getTillSettle() {
        return tillSettle;
    }

    /**
     * Sets the till settle details of the tender control transaction
     *
     * @param tillSettle	The new till settle details to set
     */
    public final void setTillSettle(TillSettle tillSettle) {
        this.tillSettle = tillSettle;
    }

    /**
     * Gets the loan details of the tender control transaction
     *
     * @return          	The loan details of the tender control transaction
     */
    public final TenderLoan getTenderLoan() {
        return tenderLoan;
    }

    /**
     * Sets the loan details of the tender control transaction
     *
     * @param tenderLoan	The new loan details to set
     */
    public final void setTenderLoan(TenderLoan tenderLoan) {
        this.tenderLoan = tenderLoan;
    }

    /**
     * Gets the pickup details of the tender control transaction
     *
     * @return          	The pickup details of the tender control transaction
     */
    public final TenderPickup getTenderPickup() {
        return tenderPickup;
    }

    /**
     * Sets the pickup details of the tender control transaction
     *
     * @param tenderPickup	The new pickup details to set
     */
    public final void setTenderPickup(TenderPickup tenderPickup) {
        this.tenderPickup = tenderPickup;
    }

    /**
     * Gets the tenderExchange of the tender control transaction
     *
     * @return              The tenderExchange of the tender control transaction
     */
    public final TenderExchange getTenderExchange() {
        return tenderExchange;
    }
    /**
     * Sets the tenderExchange of the tender control transaction
     *
     * @param tenderExchange
     */
    public final void setTenderExchange(TenderExchange tenderExchange) {
        this.tenderExchange = tenderExchange;
    }

    /**
     * Gets the cashToDrawer details of the tender control transaction
     *
     * @return          	The cashToDrawer details of the tender control transaction
     */
    public final CashToDrawer getCashToDrawer() {
        return cashToDrawer;
    }
    
    /**
     * Sets the cashToDrawer details of the tender control transaction
     *
     * @param cashToDrawer	The new cashToDrawer details to set
     */
    public final void setCashToDrawer(CashToDrawer cashToDrawer) {
        this.cashToDrawer = cashToDrawer;
    }
    
    /**
     * Gets the guarantee details of the tender control transaction
     * @return     The guarantee details of the tender control transaction
     */
    public final Guarantee getGuarantee() {
    	return guarantee;
    }
    
    /**
     * Sets the guarantee details of the tender control transaction
     * @param guaranteeToSet     The guarantee details of the tender control transaction
     */
    public final void setGuarantee(Guarantee guaranteeToSet) {
    	this.guarantee = guaranteeToSet;
    }
}
