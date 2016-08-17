package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 * 
 * BarOperatorName Model Object.
 *
 * <P>A BarOperatorName Node in POSLog XML.
 *
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "barOperatorName")
public class BarOperatorName {
    /**
     *
     */
    @XmlValue
    private String value;

    /**
     * amount.
     */
    @XmlAttribute(name = "barOperatorType")
    private String barOperatorType;

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the barOperatorType
     */
    public String getBarOperatorType() {
        return barOperatorType;
    }

    /**
     * @param barOperatorType the barOperatorType to set
     */
    public void setBarOperatorType(String barOperatorType) {
        this.barOperatorType = barOperatorType;
    }

}
