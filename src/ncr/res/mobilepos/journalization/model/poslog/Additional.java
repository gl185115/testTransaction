package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Additional")
public class Additional {
    /**
     * AmountForPoints
     */
	@XmlElement(name = "AmountForPoints")
	private int amountForPoints;

    /**
     * QuantityForPoints
     */
    @XmlElement(name = "QuantityForPoints")
    private int quantityForPoints;

    /**
     * CorrectionPoints
     */
    @XmlElement(name = "CorrectionPoints")
    private int correctionPoints;

    /**
     * AmountForCash
     */
    @XmlElement(name = "AmountForCash")
    private int amountForCash;

    /**
     * BasicPointForCash
     */
    @XmlElement(name = "BasicPointForCash")
    private int basicPointForCash;

    /**
     * PointForCash
     */
    @XmlElement(name = "PointForCash")
    private int pointForCash;

    /**
     * CorrectionPointsForCash
     */
    @XmlElement(name = "CorrectionPointsForCash")
    private int correctionPointsForCash;

    /**
     * AmountForAffiliate
     */
    @XmlElement(name = "AmountForAffiliate")
    private int amountForAffiliate;

    /**
     * BasicPointForAffiliate
     */
    @XmlElement(name = "BasicPointForAffiliate")
    private int basicPointForAffiliate;

    /**
     * PointForAffiliate
     */
    @XmlElement(name = "PointForAffiliate")
    private int pointForAffiliate;

    /**
     * CorrectionPointsForAffiliate
     */
    @XmlElement(name = "CorrectionPointsForAffiliate")
    private int correctionPointsForAffiliate;

    /**
     * AmountForNonAffiliate
     */
    @XmlElement(name = "AmountForNonAffiliate")
    private int amountForNonAffiliate;

    /**
     * BasicPointForNonAffiliate
     */
    @XmlElement(name = "BasicPointForNonAffiliate")
    private int basicPointForNonAffiliate;

    /**
     * PointForNonAffiliate
     */
    @XmlElement(name = "PointForNonAffiliate")
    private int pointForNonAffiliate;

    /**
     * CorrectionPointsForNonAffiliate
     */
    @XmlElement(name = "CorrectionPointsForNonAffiliate")
    private int correctionPointsForNonAffiliate;

    /**
     * @return the amountForPoints
     */
    public final int getAmountForPoints() {
        return amountForPoints;
    }

    /**
     * @param amountForPoints the amountForPoints to set
     */
    public final void setAmountForPoints(int amountForPoints) {
        this.amountForPoints = amountForPoints;
    }

    /**
     * @return the quantityForPoints
     */
    public final int getQuantityForPoints() {
        return quantityForPoints;
    }

    /**
     * @param quantityForPoints the quantityForPoints to set
     */
    public final void setQuantityForPoints(int quantityForPoints) {
        this.quantityForPoints = quantityForPoints;
    }

    /**
     * @return the correctionPoints
     */
    public final int getCorrectionPoints() {
        return correctionPoints;
    }

    /**
     * @param correctionPoints the correctionPoints to set
     */
    public final void setCorrectionPoints(int correctionPoints) {
        this.correctionPoints = correctionPoints;
    }

    /**
     * @return the amountForCash
     */
    public final int getAmountForCash() {
        return amountForCash;
    }

    /**
     * @param amountForCash the amountForCash to set
     */
    public final void setAmountForCash(int amountForCash) {
        this.amountForCash = amountForCash;
    }

    /**
     * @return the basicPointForCash
     */
    public final int getBasicPointForCash() {
        return basicPointForCash;
    }

    /**
     * @param basicPointForCash the basicPointForCash to set
     */
    public final void setBasicPointForCash(int basicPointForCash) {
        this.basicPointForCash = basicPointForCash;
    }

    /**
     * @return the pointForCash
     */
    public final int getPointForCash() {
        return pointForCash;
    }

    /**
     * @param pointForCash the pointForCash to set
     */
    public final void setPointForCash(int pointForCash) {
        this.pointForCash = pointForCash;
    }

    /**
     * @return the correctionPointsForCash
     */
    public final int getCorrectionPointsForCash() {
        return correctionPointsForCash;
    }

    /**
     * @param correctionPointsForCash the correctionPointsForCash to set
     */
    public final void setCorrectionPointsForCash(int correctionPointsForCash) {
        this.correctionPointsForCash = correctionPointsForCash;
    }

    /**
     * @return the amountForAffiliate
     */
    public final int getAmountForAffiliate() {
        return amountForAffiliate;
    }

    /**
     * @param amountForAffiliate the amountForAffiliate to set
     */
    public final void setAmountForAffiliate(int amountForAffiliate) {
        this.amountForAffiliate = amountForAffiliate;
    }

    /**
     * @return the basicPointForAffiliate
     */
    public final int getBasicPointForAffiliate() {
        return basicPointForAffiliate;
    }

    /**
     * @param basicPointForAffiliate the basicPointForAffiliate to set
     */
    public final void setBasicPointForAffiliate(int basicPointForAffiliate) {
        this.basicPointForAffiliate = basicPointForAffiliate;
    }

    /**
     * @return the pointForAffiliate
     */
    public final int getPointForAffiliate() {
        return pointForAffiliate;
    }

    /**
     * @param pointForAffiliate the pointForAffiliate to set
     */
    public final void setPointForAffiliate(int pointForAffiliate) {
        this.pointForAffiliate = pointForAffiliate;
    }

    /**
     * @return the correctionPointsForAffiliate
     */
    public final int getCorrectionPointsForAffiliate() {
        return correctionPointsForAffiliate;
    }

    /**
     * @param correctionPointsForAffiliate the correctionPointsForAffiliate to set
     */
    public final void setCorrectionPointsForAffiliate(int correctionPointsForAffiliate) {
        this.correctionPointsForAffiliate = correctionPointsForAffiliate;
    }

    /**
     * @return the amountForNonAffiliate
     */
    public final int getAmountForNonAffiliate() {
        return amountForNonAffiliate;
    }

    /**
     * @param amountForNonAffiliate the amountForNonAffiliate to set
     */
    public final void setAmountForNonAffiliate(int amountForNonAffiliate) {
        this.amountForNonAffiliate = amountForNonAffiliate;
    }

    /**
     * @return the basicPointForNonAffiliate
     */
    public final int getBasicPointForNonAffiliate() {
        return basicPointForNonAffiliate;
    }

    /**
     * @param basicPointForNonAffiliate the basicPointForNonAffiliate to set
     */
    public final void setBasicPointForNonAffiliate(int basicPointForNonAffiliate) {
        this.basicPointForNonAffiliate = basicPointForNonAffiliate;
    }

    /**
     * @return the pointForNonAffiliate
     */
    public final int getPointForNonAffiliate() {
        return pointForNonAffiliate;
    }

    /**
     * @param pointForNonAffiliate the pointForNonAffiliate to set
     */
    public final void setPointForNonAffiliate(int pointForNonAffiliate) {
        this.pointForNonAffiliate = pointForNonAffiliate;
    }

    /**
     * @return the correctionPointsForNonAffiliate
     */
    public final int getCorrectionPointsForNonAffiliate() {
        return correctionPointsForNonAffiliate;
    }

    /**
     * @param correctionPointsForNonAffiliate the correctionPointsForNonAffiliate to set
     */
    public final void setCorrectionPointsForNonAffiliate(int correctionPointsForNonAffiliate) {
        this.correctionPointsForNonAffiliate = correctionPointsForNonAffiliate;
    }

}