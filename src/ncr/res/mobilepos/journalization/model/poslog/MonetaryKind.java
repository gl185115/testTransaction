package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MonetaryKind")
public class MonetaryKind {
	
    /**
     * Element for Monetary kind name
     */
    @XmlElement(name = "Kind")
    private String kind;

    /**
     * Element for Quantity of this kind
     */
    @XmlElement(name = "Quantity")
    private String quantity;

    /***
     * Element for Rolls of this kind
     */
    @XmlElement(name = "Rolls")
    private String rolls;
    
    /**
     * Gets the kind of the monetanykind
     *
     * @return                  The kind of the monetanykind
     */
    public final String getKind() {
        return kind;
    }

    /**
     * Sets the kind  of the monetanykind
     *
     * @param kind   The new kind  to set
     */
    public final void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * Gets the quatity of the monetanykind
     *
     * @return                  The quatity of the monetanykind
     */
    public final String getQuantity() {
        return quantity;
    }

    /**
     * Sets the quatity  of the monetanykind
     *
     * @param quatity   The new quatity  to set
     */
    public final void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
    public MonetaryKind(){
       kind = null;
       quantity = null;
    }
    
    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Quantity : ").append(this.quantity).append(crlf)
        .append("Kind : ").append(this.kind).append(crlf);
        return str.toString();
    }
    
    /***
     * Sets the rolls of the monetarykind
     * 
     * @param rolls
     */
    public final void setRolls(String rolls) {
    	this.rolls = rolls;
    }
    
    /***
     * Gets the rolls for the monetarykind
     * @return
     */
    public final String getRolls() {
    	return this.rolls;
    }

}
