package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.pricing.model.Item;

/**
 * Sale Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Sale")
@ApiModel(value="Sale")
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

    @XmlElement(name = "ItemForm")
    private String itemForm;
    
    private boolean isPriceOverride;
    
    private double discountRate;
    
    private int discountCount;
    
    private int rewardId;
    
    @XmlElement(name = "DiscountClass")
    private int discountClass;
    
    @XmlElement(name = "DiscountAmt")
    private int discountAmt;
    
    @XmlElement(name = "DptDiscountRate")
    private Double dptDiscountRate;
    
    @XmlElement(name = "DptPromotionNo")
    private String dptPromotionNo;
    
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
    
    @ApiModelProperty(value="???????G???g???R?[?h", notes="???????G???g???R?[?h")
    public final String getItemEntryId() {
        return itemEntryId;
    }
    
    public final void setItemEntryId(final String itemEntryId) {
        this.itemEntryId = itemEntryId;
    }
    
    @ApiModelProperty(value="?A?C?e???^?C?v?R?[?h", notes="?A?C?e???^?C?v?R?[?h")
    public final String getItemIdType() {
        return itemIdType;
    }
    
    public final void setItemIdType(final String itemIdType) {
        this.itemIdType = itemIdType;
    }
    
    @ApiModelProperty(value="????", notes="????")
    public final int getQuantity() {
        return quantity;
    }
    
    public final void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
    
    @ApiModelProperty(value="?g????", notes="?g????")
    public final double getExtendedAmount() {
        return extendedAmount;
    }
    
    public final void setExtendedAmount(final double extendedAmount) {
        this.extendedAmount = extendedAmount;
    }
    
    @ApiModelProperty(value="??????", notes="??????")
    public final int getDiscountCount() {
        return discountCount;
    }
    
    public final void setDiscountCount(final int discountCount) {
        this.discountCount = discountCount;
    }
    
    @ApiModelProperty(value="?????R?[?h", notes="?????R?[?h")
    public final int getRewardId() {
        return rewardId;
    }
    
    public final void setRewardId(final int rewardId) {
        this.rewardId = rewardId;
    }
    
    @ApiModelProperty(value="??????", notes="??????")
    public final double getDiscountRate() {
        return discountRate;
    }
    
    public final void setDiscountRate(final double discountRate) {
        this.discountRate = discountRate;
    }
    
    @ApiModelProperty(value="?????^?C?v", notes="?????^?C?v")
    public final int getInputType() {
        return inputType;
    }
    
    public final void setInputType(final int inputType) {
        this.inputType = inputType;
    }
    
    //-------
    @ApiModelProperty(value="???????i??", notes="???????i??")
    public final String getOldSku() {
         return oldSku;
     }
     public final void setOldSku(final String oldSku) {
         this.oldSku = oldSku;
     }
     
     @ApiModelProperty(value="?R?[?h?T?C?Y", notes="?R?[?h?T?C?Y")
     public final String getSizeCode() {
         return sizeCode;
     }
     public final void setSizeCode(final String sizeCode) {
         this.sizeCode = sizeCode;
     }
     
     @ApiModelProperty(value="?R?[?h???F", notes="?R?[?h???F")
     public final String getColorCode() {
         return colorCode;
     }
     public final void setColorCode(final String colorCode) {
         this.colorCode = colorCode;
     }

    @ApiModelProperty(value="?????`??", notes="?????`??")
    public String getItemForm() {
		return itemForm;
	}
	public void setItemForm(String itemForm) {
		this.itemForm = itemForm;
	}
	
    public int getDiscountClass() {
        return discountClass;
    }
    public void setDiscountClass(int discountClass) {
        this.discountClass = discountClass;
    }
    public int getDiscountAmt() {
        return discountAmt;
    }
    public void setDiscountAmt(int discountAmt) {
        this.discountAmt = discountAmt;
    }
    public Double getDptDiscountRate() {
        return dptDiscountRate;
    }
    public void setDptDiscountRate(Double dptDiscountRate) {
        this.dptDiscountRate = dptDiscountRate;
    }
    public String getDptPromotionNo() {
        return dptPromotionNo;
    }
    public void setDptPromotionNo(String dptPromotionNo) {
        this.dptPromotionNo = dptPromotionNo;
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
    
    public Sale(final Sale info)
    {
        super(info);
        this.itemEntryId = info.getItemEntryId();
        this.itemIdType = info.getItemIdType();
        this.quantity = info.getQuantity();
        this.extendedAmount = info.getExtendedAmount();
        this.inputType = info.getInputType();
        this.oldSku = info.getOldSku();
        this.sizeCode = info.getSizeCode();
        this.colorCode = info.getColorCode();
        this.itemForm = info.getItemForm();
        this.isPriceOverride = info.isPriceOverride();
        this.discountRate = info.getDiscountRate();
        this.discountCount = info.getDiscountCount();
        this.rewardId = info.getRewardId();
        this.discountClass = info.getDiscountClass();
        this.discountAmt = info.getDiscountAmt();
        this.dptDiscountRate = info.getDptDiscountRate();
        this.dptPromotionNo = info.getDptPromotionNo();
    }
  
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        String clrf = "; ";
        str.append("itemEntryId :").append(itemEntryId).append(clrf)
        .append("itemIdType :").append(itemIdType).append(clrf)
        .append("quantity :").append(quantity).append(clrf)
        .append("extendedAmount :").append(extendedAmount).append(clrf)
        .append("inputType :").append(inputType).append(clrf)
        .append("oldSku :").append(oldSku).append(clrf)
        .append("sizeCode :").append(sizeCode).append(clrf)
        .append("colorCode :").append(colorCode).append(clrf)
        .append("itemForm :").append(itemForm).append(clrf)
        .append("isPriceOverride :").append(isPriceOverride).append(clrf)
        .append("discountRate :").append(discountRate).append(clrf)
        .append("discountCount :").append(discountCount).append(clrf)
        .append("rewardId :").append(rewardId).append(clrf)
        .append("discountClass :").append(discountClass).append(clrf)
        .append("discountAmt :").append(discountAmt).append(clrf)
        .append("dptDiscountRate :").append(dptDiscountRate).append(clrf)
        .append("dptPromotionNo :").append(dptPromotionNo).append(clrf);
        return super.toString() + str.toString();
    }
}
