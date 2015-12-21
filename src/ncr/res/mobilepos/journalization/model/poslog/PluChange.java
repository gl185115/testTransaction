package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PluChange Model Object.
 *
 * <P>An PluChange Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PluChange")
public class PluChange {

    /** The BeforeSlipLineNo. */
    @XmlElement(name = "BeforeSlipLineNo")
    private String beforeSlipLineNo;

    /** The BeforeDeptCode. */
    @XmlElement(name = "BeforeDeptCode")
    private String beforeDeptCode;
    
    /** The BeforeMdInternal. */
    @XmlElement(name = "BeforeMdInternal")
    private String beforeMdInternal;
    
    /** The BeforeMdName. */
    @XmlElement(name = "BeforeMdName")
    private String beforeMdName;
    
    /** The BeforeSku. */
    @XmlElement(name = "BeforeSku")
    private String beforeSku;
    
    /** The BeforeSalesPrice. */
    @XmlElement(name = "BeforeSalesPrice")
    private int beforeSalesPrice;
    
    /** The BeforeSalesAmt. */
    @XmlElement(name = "BeforeSalesAmt")
    private int beforeSalesAmt;
    
    /** The BeforeItemCnt. */
    @XmlElement(name = "BeforeItemCnt")
    private int beforeItemCnt;
    
    /** The BeforeTaxType. */
    @XmlElement(name = "BeforeTaxType")
    private String beforeTaxType;
    
    /** The BeforeColorId. */
    @XmlElement(name = "BeforeColorId")
    private String beforeColorId;
    
    /** The BeforeSizeId. */
    @XmlElement(name = "BeforeSizeId")
    private String beforeSizeId;
    
    /** The AfterMdInternal. */
    @XmlElement(name = "AfterMdInternal")
    private String afterMdInternal;
    
    /** The AfterMdName. */
    @XmlElement(name = "AfterMdName")
    private String afterMdName;
    
    /** The AfterSku. */
    @XmlElement(name = "AfterSku")
    private String afterSku;
    
    /** The AfterSalesPrice. */
    @XmlElement(name = "AfterSalesPrice")
    private int afterSalesPrice;
    
    /** The AfterSalesAmt. */
    @XmlElement(name = "AfterSalesAmt")
    private int afterSalesAmt;
    
    /** The AfterTaxType. */
    @XmlElement(name = "AfterTaxType")
    private String afterTaxType;
    
    /** The AfterColorId. */
    @XmlElement(name = "AfterColorId")
    private String afterColorId;
    
    /** The AfterColorName. */
    @XmlElement(name = "AfterColorName")
    private String afterColorName;
    
    /** The AfterSizeId. */
    @XmlElement(name = "AfterSizeId")
    private String afterSizeId;
    
    /** The AfterSizeName. */
    @XmlElement(name = "AfterSizeName")
    private String afterSizeName;
    
    /** The PendingItemCnt. */
    @XmlElement(name = "PendingItemCnt")
    private int pendingItemCnt;
    
    /** The ItemCnt. */
    @XmlElement(name = "ItemCnt")
    private int itemCnt;
    
    public final String getBeforeSlipLineNo() {
        return beforeSlipLineNo;
    }

    public final void setBeforeSlipLineNo(String beforeSlipLineNo) {
        this.beforeSlipLineNo = beforeSlipLineNo;
    }

    public final String getBeforeMdInternal() {
        return beforeMdInternal;
    }

    public final void setBeforeMdInternal(String beforeMdInternal) {
        this.beforeMdInternal = beforeMdInternal;
    }

    public final String getBeforeMdName() {
        return beforeMdName;
    }

    public final void setBeforeMdName(String beforeMdName) {
        this.beforeMdName = beforeMdName;
    }

    public final String getBeforeSku() {
        return beforeSku;
    }

    public final void setBeforeSku(String beforeSku) {
        this.beforeSku = beforeSku;
    }

    public final int getBeforeSalesPrice() {
        return beforeSalesPrice;
    }

    public final void setBeforeSalesPrice(int beforeSalesPrice) {
        this.beforeSalesPrice = beforeSalesPrice;
    }

    public final int getBeforeSalesAmt() {
        return beforeSalesAmt;
    }

    public final void setBeforeSalesAmt(int beforeSalesAmt) {
        this.beforeSalesAmt = beforeSalesAmt;
    }

    public final int getBeforeItemCnt() {
        return beforeItemCnt;
    }

    public final void setBeforeItemCnt(int beforeItemCnt) {
        this.beforeItemCnt = beforeItemCnt;
    }

    public final String getBeforeTaxType() {
        return beforeTaxType;
    }

    public final void setBeforeTaxType(String beforeTaxType) {
        this.beforeTaxType = beforeTaxType;
    }

    public final String getBeforeColorId() {
        return beforeColorId;
    }

    public final void setBeforeColorId(String beforeColorId) {
        this.beforeColorId = beforeColorId;
    }

    public final String getBeforeSizeId() {
        return beforeSizeId;
    }

    public final void setBeforeSizeId(String beforeSizeId) {
        this.beforeSizeId = beforeSizeId;
    }

    public final String getAfterMdInternal() {
        return afterMdInternal;
    }

    public final void setAfterMdInternal(String afterMdInternal) {
        this.afterMdInternal = afterMdInternal;
    }

    public final String getAfterMdName() {
        return afterMdName;
    }

    public final void setAfterMdName(String afterMdName) {
        this.afterMdName = afterMdName;
    }

    public final String getAfterSku() {
        return afterSku;
    }

    public final void setAfterSku(String afterSku) {
        this.afterSku = afterSku;
    }

    public final int getAfterSalesPrice() {
        return afterSalesPrice;
    }

    public final void setAfterSalesPrice(int afterSalesPrice) {
        this.afterSalesPrice = afterSalesPrice;
    }

    public final int getAfterSalesAmt() {
        return afterSalesAmt;
    }

    public final void setAfterSalesAmt(int afterSalesAmt) {
        this.afterSalesAmt = afterSalesAmt;
    }

    public final String getAfterTaxType() {
        return afterTaxType;
    }

    public final void setAfterTaxType(String afterTaxType) {
        this.afterTaxType = afterTaxType;
    }

    public final String getAfterColorId() {
        return afterColorId;
    }

    public final void setAfterColorId(String afterColorId) {
        this.afterColorId = afterColorId;
    }

    public final String getAfterSizeId() {
        return afterSizeId;
    }

    public final void setAfterSizeId(String afterSizeId) {
        this.afterSizeId = afterSizeId;
    }

    public final int getPendingItemCnt() {
        return pendingItemCnt;
    }

    public final void setPendingItemCnt(int pendingItemCnt) {
        this.pendingItemCnt = pendingItemCnt;
    }

    public final int getItemCnt() {
        return itemCnt;
    }

    public final void setItemCnt(int itemCnt) {
        this.itemCnt = itemCnt;
    }

    public final String getBeforeDeptCode() {
        return beforeDeptCode;
    }

    public final void setBeforeDeptCode(String beforeDeptCode) {
        this.beforeDeptCode = beforeDeptCode;
    }

    public final String getAfterColorName() {
        return afterColorName;
    }

    public final void setAfterColorName(String afterColorName) {
        this.afterColorName = afterColorName;
    }

    public final String getAfterSizeName() {
        return afterSizeName;
    }

    public final void setAfterSizeName(String afterSizeName) {
        this.afterSizeName = afterSizeName;
    }
}
