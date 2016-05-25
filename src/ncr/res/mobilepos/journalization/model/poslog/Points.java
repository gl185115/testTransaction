package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Points Model Object.
 *
 * <P>An Points Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Points")
public class Points {    
    /**
     * BasicPointRate
     */
    @XmlElement(name = "BasicPointRate")
    private int basicPointRate;
    /**
     * (換算レート)
     * PointRate
     */
    @XmlElement(name = "PointRate")
    private String pointRate;
    /**
     * PointRateChange
     */
    @XmlElement(name = "PointRateChange")
    private int pointRateChange;
    /**
     * CorrectionPoints (獲得ポイント) ※ 明細単位
     */
    @XmlElement(name = "CorrectionPoints")
    private String correctionPoints;
    /**
     * AmountForPoints
     */
    @XmlElement(name = "AmountForPoints")
    private int amountForPoints;
    /**
     * AdjustFlag
     */
    @XmlElement(name = "AdjustFlag")
    private int adjustFlag;
    /**
     * Detail
     */
    @XmlElement(name = "Detail")
    private Detail detail;
	/**
     * @return the pointRate
     */
    public final String getPointRate() {
        return pointRate;
    }
    /**
     * @param pointRate the pointRate to set
     */
    public final void setPointRate(final String pointRate) {
        this.pointRate = pointRate;
    }
    /**
     * @return the pointRateChange
     */
    public final int getPointRateChange() {
        return pointRateChange;
    }
    /**
     * @param pointRateChange the pointRateChange to set
     */
    public final void setPointRateChange(final int pointRateChange) {
        this.pointRateChange = pointRateChange;
    }
    /**
     * @return the correctionPoints
     */
    public final String getCorrectionPoints() {
        return correctionPoints;
    }
    /**
     * @param correctionPoints the correctionPoints to set
     */
    public final void setCorrectionPoints(final String correctionPoints) {
        this.correctionPoints = correctionPoints;
    }
    /**
     * @return the basicPointRate
     */
    public final int getBasicPointRate() {
        return basicPointRate;
    }
    /**
     * @param basicPointRate the basicPointRate to set
     */
    public final void setBasicPointRate(final int basicPointRate) {
        this.basicPointRate = basicPointRate;
    }
    /**
     * @return the amountForPoints
     */
    public final int getAmountForPoints() {
        return amountForPoints;
    }
    /**
     * @param amountForPoints the amountForPoints to set
     */
    public final void setAmountForPoints(final int amountForPoints) {
        this.amountForPoints = amountForPoints;
    }
    /**
     * @return the adjustFlag
     */
    public final int getAdjustFlag() {
        return adjustFlag;
    }
    /**
     * @param adjustFlag the adjustFlag to set
     */
    public final void setAdjustFlag(final int adjustFlag) {
        this.adjustFlag = adjustFlag;
    }
    /**
     * @return the detail
     */
    public final Detail getDetail() {
        return detail;
    }
    /**
     * @param detail the detail to set
     */
    public final void setDetail(final Detail detail) {
        this.detail = detail;
    }
}
