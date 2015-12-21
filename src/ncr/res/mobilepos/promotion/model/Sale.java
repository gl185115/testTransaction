package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.pricing.model.Item;

/**
 * Sale Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Sale")
public class Sale extends Item {
    
    public static final int DISC_AMOUNT = 0;
    public static final int DISC_RATE = 1;
    
    public static final  int MUST_BUY = 1;
    
    @XmlElement(name = "ItemEntryID")
    private String itemEntryId;
    
    @XmlElement(name = "ItemIDType")
    private String itemIdType;
    
    @XmlElement(name = "Quantity")
    private int quantity;
    
    @XmlElement(name = "ExtendedAmount")
    private double extendedAmount;
    
    @XmlElement(name = "InputType")
    private int inputType;
    
    @XmlElement(name = "OldSku")
    private String oldSku;
    
    @XmlElement(name = "SizeCode")
    private String sizeCode;
    
    @XmlElement(name = "ColorCode")
    private String colorCode;
    
    private boolean isPriceOverride;
    
    private double discountRate;
    
    private int discountCount;
    
    private int rewardId;
    
    public Sale() {
        this.setDiscount(0);
        this.isPriceOverride = false;
    }
    /**
     * Constructor accepting an {@link Item}.
     * @param item The item to copy values from
     */
    public Sale(final Item item) {
        super(item);
        this.discountRate = item.getDiscount();
        this.isPriceOverride = false;
    }
    
    public final boolean isPriceOverride() {
        return isPriceOverride;
    }
    
    public final void setPriceOverride(boolean isPriceOverride) {
        this.isPriceOverride = isPriceOverride;
    }
    
    public final String getItemEntryId() {
        return itemEntryId;
    }
    
    public final void setItemEntryId(final String itemEntryId) {
        this.itemEntryId = itemEntryId;
    }
    
    public final String getItemIdType() {
        return itemIdType;
    }
    
    public final void setItemIdType(final String itemIdType) {
        this.itemIdType = itemIdType;
    }
    
    public final int getQuantity() {
        return quantity;
    }
    
    public final void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
    
    public final double getExtendedAmount() {
        return extendedAmount;
    }
    
    public final void setExtendedAmount(final double extendedAmount) {
        this.extendedAmount = extendedAmount;
    }
    
    public final int getDiscountCount() {
        return discountCount;
    }
    
    public final void setDiscountCount(final int discountCount) {
        this.discountCount = discountCount;
    }
    
    public final int getRewardId() {
        return rewardId;
    }
    
    public final void setRewardId(final int rewardId) {
        this.rewardId = rewardId;
    }
    
    public final double getDiscountRate() {
        return discountRate;
    }
    
    public final void setDiscountRate(final double discountRate) {
        this.discountRate = discountRate;
    }
    
    public final int getInputType() {
        return inputType;
    }
    
    public final void setInputType(final int inputType) {
        this.inputType = inputType;
    }
    
    //-------
    public final String getOldSku() {
         return oldSku;
     }
     public final void setOldSku(final String oldSku) {
         this.oldSku = oldSku;
     }
     public final String getSizeCode() {
         return sizeCode;
     }
     public final void setSizeCode(final String sizeCode) {
         this.sizeCode = sizeCode;
     }
     public final String getColorCode() {
         return colorCode;
     }
     public final void setColorCode(final String colorCode) {
         this.colorCode = colorCode;
     }
    //-----
    @Override
    public final Object clone() {
        Sale cloneSale = new Sale();
        cloneSale.setQuantity(this.getQuantity());
        cloneSale.setRegularSalesUnitPrice(this.getRegularSalesUnitPrice());
        cloneSale.setActualSalesUnitPrice(this.getActualSalesUnitPrice());
        cloneSale.setDepartment(this.getDepartment());
        cloneSale.setDescription(this.getDescription());
        cloneSale.setDiscountCount(this.getDiscountCount());
        cloneSale.setDiscount(this.getDiscount());
        cloneSale.setDiscountable(this.getDiscountable());
        cloneSale.setDiscountAmount(this.getDiscountAmount());
        cloneSale.setDiscountFlag(this.getDiscountFlag());
        cloneSale.setDiscountRate(this.getDiscountRate());
        cloneSale.setExtendedAmount(this.getExtendedAmount());
        cloneSale.setItemClass(this.getItemClass());
        cloneSale.setItemEntryId(this.getItemEntryId());
        cloneSale.setItemId(this.getItemId());
        cloneSale.setItemIdType(this.getItemIdType());
        cloneSale.setLine(this.getLine());
        cloneSale.setMixMatchCode(this.getMixMatchCode());
        cloneSale.setRewardId(this.getRewardId());
        cloneSale.setMustBuyFlag(this.getMustBuyFlag());
        cloneSale.setTaxRate(this.getTaxRate());
        cloneSale.setTaxType(this.getTaxType());
        cloneSale.setDiscountType(this.getDiscountType());
        cloneSale.setSubInt10(this.getSubInt10());
        cloneSale.setPriceOverride(this.isPriceOverride());
        cloneSale.setNonSales(this.getNonSales());
        cloneSale.setInputType(this.getInputType());
        cloneSale.setAgeRestrictedFlag(this.getAgeRestrictedFlag());
        cloneSale.setCouponFlag(this.getCouponFlag());
        
        cloneSale.setOldSku(this.getOldSku());
        cloneSale.setColorCode(this.colorCode);
        cloneSale.setSizeCode(this.sizeCode);
        
        return cloneSale;
    }
}
