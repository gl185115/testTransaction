package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds the Operator sign on element.
 * @author RES
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OperatorSignOn")
public class OperatorSignOn {
	
	/**
	 * Operator Accountability.
	 */
	@XmlAttribute(name = "Accountability")
    private String accountability;

    @XmlElement(name = "SoftwareVersion")
    String softwareVersion;

    /**
     * @return operatorAccountability
     */
    public String getAccountability() {
    	return this.accountability;
    }
    
    /**
     * @param operatorAccountability
     */
    public void setAccountability(String accountability) {
    	this.accountability = accountability;
    }

    /**
     * get SoftwareVersion object.
     * return SoftwareVersion string (JSON)
     */
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    /**
     * set SoftwareVersion object.
     * @param value SoftwareVersion string (JSON object)
     */
    public void setSoftwareVersion(String value) {
        softwareVersion = value;
    }
	
    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuffer str = new StringBuffer();
        String crlf = "\r\n";
        str.append("Accountability : ").append(this.accountability).append(crlf);
        return str.toString();
    }
}
