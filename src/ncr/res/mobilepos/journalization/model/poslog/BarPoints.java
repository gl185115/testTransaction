package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 * 
 * barPoints Model Object.
 *
 * <P>An barPoints Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "barPoints")
public class BarPoints {
    /**
     * (換算レート)
     * barPointRate
     */
    @XmlElement(name = "barPointRate")
    private String barPointRate;
    /**
     * CorrectionPoints (獲得ポイント) ※ 明細単位
     * barCorrectionPoints
     */
    @XmlElement(name = "barCorrectionPoints")
    private String barCorrectionPoints;
    
    /**
     * @return the barPointRate
     */
    public String getBarPointRate() {
        return barPointRate;
    }
    
    /**
     * @param barPointRate the barPointRate to set
     */
    public void setBarPointRate(String barPointRate) {
        this.barPointRate = barPointRate;
    }
    
    /**
     * @return the barCorrectionPoints
     */
    public String getBarCorrectionPoints() {
        return barCorrectionPoints;
    }
    
    /**
     * @param barCorrectionPoints the barCorrectionPoints to set
     */
    public void setBarCorrectionPoints(String barCorrectionPoints) {
        this.barCorrectionPoints = barCorrectionPoints;
    }
    
}
