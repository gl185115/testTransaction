package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "StoredValueFund")
public class StoredValueFund {
    /**
     * The private member variable that will hold the type code of StoredValueFund.
     */
    @XmlAttribute(name = "TypeCode")
    private String typeCode;

    /**
     * The private member variable that will hold the Action of StoredValueFund.
     */
    @XmlAttribute(name = "Action")
    private String action;

    /**
     * Previous balance of brand.
     */
    @XmlElement(name = "Instrument")
    private Instrument instrument;


    /**
     * @return instrument
     */
    public Instrument getInstrument() {
        return instrument;
    }

    /**
     * @param instrument セットする instrument
     */
    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    /**
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action セットする action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return typeCode
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode セットする typeCode
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
