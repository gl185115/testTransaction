package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 * 
 * barEmployee Model Object.
 *
 * <P>A barEmployee Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "barEmployee")
public class BarEmployee {
    /**
     * barEmployeeID.é–àıî‘çÜ
     */
    @XmlElement(name = "barEmployeeID")
    private String barEmployeeID;

    /**
     * @return the barEmployeeID
     */
    public String getBarEmployeeID() {
        return barEmployeeID;
    }

    /**
     * @param barEmployeeID the barEmployeeID to set
     */
    public void setBarEmployeeID(String barEmployeeID) {
        this.barEmployeeID = barEmployeeID;
    }

}
