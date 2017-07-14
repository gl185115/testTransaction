package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds the EOD element.
 * @author RES
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "POSEOD")
public class PosEod {

    /**
     * Accountability
     */
    @XmlAttribute(name = "Accountability")
    private String accountability;

    /**
     * @return operatorAccountability
     */
    public String getAccountability() {
        return this.accountability;
    }

    /**
     * @param accountability
     */
    public void setAccountability(String accountability) {
        this.accountability = accountability;
    }

    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Accountability : ").append(this.accountability).append(crlf);
        return str.toString();
    }
}
