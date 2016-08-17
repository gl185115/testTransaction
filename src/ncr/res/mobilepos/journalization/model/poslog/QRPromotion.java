/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * QRPromotion
 *
 * Model Class for QRPromotion
 *
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * QRPromotion Model Object.
 *
 * <P>A QRPromotion Node in POSLog XML.
 *
 * <P>The QRPromotion node is under Sale Node.
 * And mainly holds the information of the QRPromotion
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "QRPromotion")
public class QRPromotion {
    /**
     * QRPromotionId
     */
    @XmlElement(name = "QRPromotionId")
    private String qrPromotionId;

    /**
     * QRPromotionName
     */
    @XmlElement(name="QRPromotionName")
    private String qrPromotionName;

    /**
     * QRBmpFileCnt
     */
    @XmlElement(name="QRBmpFileCnt")
    private String qrBmpFileCnt;

    /**
     * QRBmpFileName
     */
    @XmlElement(name="QRBmpFileName")
    private String qrBmpFileName;

    /**
     * QRMinimumPrice
     */
    @XmlElement(name="QRMinimumPrice")
    private String qrMinimumPrice;

    /**
     * QROutputType
     */
    @XmlElement(name="QROutputType")
    private String qrOutputType;

    /**
     * QROutputTargetValue
     */
    @XmlElement(name="QROutputTargetValue")
    private String qrOutputTargetValue;

    /**
     * @return the qrPromotionId
     */
    public final String getQRPromotionId() {
        return qrPromotionId;
    }

    /**
     * @param qrPromotionId the qrPromotionId to set
     */
    public final void setQRPromotionId(final String qrPromotionId) {
        this.qrPromotionId = qrPromotionId;
    }

    /**
     * @return the qrPromotionName
     */
    public final String getQRPromotionName() {
        return qrPromotionName;
    }

    /**
     * @param qrPromotionName the qrPromotionName to set
     */
    public final void setQRPromotionName(final String qrPromotionName) {
        this.qrPromotionName = qrPromotionName;
    }

    /**
     * @return the qrBmpFileCnt
     */
    public final String getQRBmpFileCnt() {
        return qrBmpFileCnt;
    }

    /**
     * @param qrBmpFileCnt the qrBmpFileCnt to set
     */
    public final void setQRBmpFileCnt(final String qrBmpFileCnt) {
        this.qrBmpFileCnt = qrBmpFileCnt;
    }

    /**
     * @return the qrBmpFileName
     */
    public final String getQRBmpFileName() {
        return qrBmpFileName;
    }

    /**
     * @param qrBmpFileName the qrBmpFileName to set
     */
    public final void setQRBmpFileName(final String qrBmpFileName) {
        this.qrBmpFileName = qrBmpFileName;
    }

    /**
     * @return the qrMinimumPrice
     */
    public final String getQRMinimumPrice() {
        return qrMinimumPrice;
    }

    /**
     * @param qrMinimumPrice the qrMinimumPrice to set
     */
    public final void setQRMinimumPrice(final String qrMinimumPrice) {
        this.qrMinimumPrice = qrMinimumPrice;
    }

    /**
     * @return the qrOutputType
     */
    public final String getQROutputType() {
        return qrOutputType;
    }

    /**
     * @param qrOutputType the qrOutputType to set
     */
    public final void setQROutputType(final String qrOutputType) {
        this.qrOutputType = qrOutputType;
    }

    /**
     * @return the qrOutputTargetValue
     */
    public final String getQROutputTargetValue() {
        return qrOutputTargetValue;
    }

    /**
     * @param qrOutputTargetValue the qrOutputTargetValue to set
     */
    public final void setQROutputTargetValue(final String qrOutputTargetValue) {
        this.qrOutputTargetValue = qrOutputTargetValue;
    }
}
