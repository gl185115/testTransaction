package ncr.res.mobilepos.point.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Point")
public class CashingUnit extends ResultBase{
    @XmlElement(name = "CashingUnit")
    private String cashingUnit;
    
    public final void setCashingUnit(String cashingUnit) {
        this.cashingUnit = cashingUnit;
    }
    
    public final String getCashingUnit() {
        return cashingUnit;
    }
}
