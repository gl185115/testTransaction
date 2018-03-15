package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 *
 * TotalMeasures Model Object.
 *
 * <P>A TotalMeasures Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TotalMeasures")
public class TotalMeasures {

    /**
     * saleLineItemCount
     */
    @XmlElement(name = "SaleLineItemCount")
    private String saleLineItemCount;

    /**
     * noSaleTransactionCount
     */
    @XmlElement(name = "NoSaleTransactionCount")
    private String noSaleTransactionCount;

    /**
     * LineItemOverrideCount
     */
    @XmlElement(name = "LineItemOverrideCount")
    private String lineItemOverrideCount;

    /**
     * weightedLineItemCount
     */
    @XmlElement(name = "WeightedLineItemCount")
    private String weightedLineItemCount;

    /**
     * lineItemKeyedCount
     */
    @XmlElement(name = "LineItemKeyedCount")
    private String lineItemKeyedCount;

    /**
     * lineItemKeyedPercentage
     */
    @XmlElement(name = "LineItemKeyedPercentage")
    private String lineItemKeyedPercentage;

    /**
     * lineItemScannedCount
     */
    @XmlElement(name = "LineItemScannedCount")
    private String lineItemScannedCount;

    /**
     * lineItemScannedPercentage
     */
    @XmlElement(name = "LineItemScannedPercentage")
    private String lineItemScannedPercentage;

    /**
     * lineItemOpenDepartmentCount
     */
    @XmlElement(name = "LineItemOpenDepartmentCount")
    private String lineItemOpenDepartmentCount;

    /**
     * lineItemOpenDepartmentPercentage
     */
    @XmlElement(name = "LineItemOpenDepartmentPercentage")
    private String lineItemOpenDepartmentPercentage;

    /**
     * ringTime
     */
    @XmlElement(name = "RingTime")
    private String ringTime;

    /**
     * idleTime
     */
    @XmlElement(name = "IdleTime")
    private String idleTime;

    /**
     * lockTIme
     */
    @XmlElement(name = "LockTIme")
    private String lockTIme;

    /**
     * tenderTime
     */
    @XmlElement(name = "TenderTime")
    private String tenderTime;

    /**
     * @return the saleLineItemCount
     */
    public String getSaleLineItemCount() {
        return saleLineItemCount;
    }

    /**
     * @param saleLineItemCount the saleLineItemCount to set
     */
    public void setSaleLineItemCount(String saleLineItemCount) {
        this.saleLineItemCount = saleLineItemCount;
    }

    /**
     * @return the noSaleTransactionCount
     */
    public String getNoSaleTransactionCount() {
        return noSaleTransactionCount;
    }

    /**
     * @param noSaleTransactionCount the noSaleTransactionCount to set
     */
    public void setNoSaleTransactionCount(String noSaleTransactionCount) {
        this.noSaleTransactionCount = noSaleTransactionCount;
    }

    /**
     * @return the lineItemOverrideCount
     */
    public String getLineItemOverrideCount() {
        return lineItemOverrideCount;
    }

    /**
     * @param lineItemOverrideCount the lineItemOverrideCount to set
     */
    public void setLineItemOverrideCount(String lineItemOverrideCount) {
        this.lineItemOverrideCount = lineItemOverrideCount;
    }

    /**
     * @return the weightedLineItemCount
     */
    public String getWeightedLineItemCount() {
        return weightedLineItemCount;
    }

    /**
     * @param weightedLineItemCount the weightedLineItemCount to set
     */
    public void setWeightedLineItemCount(String weightedLineItemCount) {
        this.weightedLineItemCount = weightedLineItemCount;
    }

    /**
     * @return the lineItemKeyedCount
     */
    public String getLineItemKeyedCount() {
        return lineItemKeyedCount;
    }

    /**
     * @param lineItemKeyedCount the lineItemKeyedCount to set
     */
    public void setLineItemKeyedCount(String lineItemKeyedCount) {
        this.lineItemKeyedCount = lineItemKeyedCount;
    }

    /**
     * @return the lineItemKeyedPercentage
     */
    public String getLineItemKeyedPercentage() {
        return lineItemKeyedPercentage;
    }

    /**
     * @param lineItemKeyedPercentage the lineItemKeyedPercentage to set
     */
    public void setLineItemKeyedPercentage(String lineItemKeyedPercentage) {
        this.lineItemKeyedPercentage = lineItemKeyedPercentage;
    }

    /**
     * @return the lineItemScannedCount
     */
    public String getLineItemScannedCount() {
        return lineItemScannedCount;
    }

    /**
     * @param lineItemScannedCount the lineItemScannedCount to set
     */
    public void setLineItemScannedCount(String lineItemScannedCount) {
        this.lineItemScannedCount = lineItemScannedCount;
    }

    /**
     * @return the lineItemScannedPercentage
     */
    public String getLineItemScannedPercentage() {
        return lineItemScannedPercentage;
    }

    /**
     * @param lineItemScannedPercentage the lineItemScannedPercentage to set
     */
    public void setLineItemScannedPercentage(String lineItemScannedPercentage) {
        this.lineItemScannedPercentage = lineItemScannedPercentage;
    }

    /**
     * @return the lineItemOpenDepartmentCount
     */
    public String getLineItemOpenDepartmentCount() {
        return lineItemOpenDepartmentCount;
    }

    /**
     * @param lineItemOpenDepartmentCount the lineItemOpenDepartmentCount to set
     */
    public void setLineItemOpenDepartmentCount(String lineItemOpenDepartmentCount) {
        this.lineItemOpenDepartmentCount = lineItemOpenDepartmentCount;
    }

    /**
     * @return the lineItemOpenDepartmentPercentage
     */
    public String getLineItemOpenDepartmentPercentage() {
        return lineItemOpenDepartmentPercentage;
    }

    /**
     * @param lineItemOpenDepartmentPercentage the lineItemOpenDepartmentPercentage to set
     */
    public void setLineItemOpenDepartmentPercentage(
            String lineItemOpenDepartmentPercentage) {
        this.lineItemOpenDepartmentPercentage = lineItemOpenDepartmentPercentage;
    }

    /**
     * @return the ringTime
     */
    public String getRingTime() {
        return ringTime;
    }

    /**
     * @param ringTime the ringTime to set
     */
    public void setRingTime(String ringTime) {
        this.ringTime = ringTime;
    }

    /**
     * @return the idleTime
     */
    public String getIdleTime() {
        return idleTime;
    }

    /**
     * @param idleTime the idleTime to set
     */
    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
    }

    /**
     * @return the lockTIme
     */
    public String getLockTIme() {
        return lockTIme;
    }

    /**
     * @param lockTIme the lockTIme to set
     */
    public void setLockTIme(String lockTIme) {
        this.lockTIme = lockTIme;
    }

    /**
     * @return the tenderTime
     */
    public String getTenderTime() {
        return tenderTime;
    }

    /**
     * @param tenderTime the tenderTime to set
     */
    public void setTenderTime(String tenderTime) {
        this.tenderTime = tenderTime;
    }

}
