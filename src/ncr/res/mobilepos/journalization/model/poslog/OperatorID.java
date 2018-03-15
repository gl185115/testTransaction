package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 *
 * OperatorID Model Object.
 *
 * <P>A OperatorID Node in POSLog XML.
 *
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OperatorID")
public class OperatorID {
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
     * OperatorName.
     */
    @XmlAttribute(name = "OperatorName")
    private String operatorName;

    /**
     * OperatorNameKana.
     */
    @XmlAttribute(name = "OperatorNameKana")
    private String operatorNameKana;

    /**
     * OperatorGroup.
     */
    @XmlAttribute(name = "OperatorGroup")
    private String operatorGroup;

    /**
     * @return the operatorName
     */
    public final String getOperatorName() {
    	return this.operatorName;
    }

    /**
     * @param operatorName the operatorName to set
     */
    public final void setOperatorName(String operatorName) {
    	  this.operatorName = operatorName;
    }

    /**
     * @return the operatorNameKana
     */
    public final String getOperatorNameKana() {
    	return this.operatorNameKana;
    }

    /**
     * @param operatorNameKana the operatorNameKana to set
     */
    public final void setOperatorNameKana(String operatorNameKana) {
    	this.operatorNameKana = operatorNameKana;
    }

    /**
     * @return the operatorNameKana
     */
    public final String getOperatorGroup() {
    	return this.operatorGroup;
    }

    /**
     * @param operatorGroup the operatorGroup to set
     */
    public final void setOperatorGroup(String operatorGroup) {
    	this.operatorGroup = operatorGroup;
    }

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
