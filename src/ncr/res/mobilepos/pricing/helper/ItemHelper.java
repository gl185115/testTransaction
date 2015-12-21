/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2014.12.31      ZHOUCHUNXIN        商品情報取得
 */
package ncr.res.mobilepos.pricing.helper;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.pricing.model.Item;

/**
 * Helper Class for Item.
 * @author CC185102
 *
 */
public final class ItemHelper {
    /** The Default Constructor. */
    private ItemHelper() {    	
    }
    /**
     * Calculate the Tax Amount of Sale of the item.
     * @param item the target Sale.
     * @param salesprice The Item's salesprice
     * @param rounding the Rounding.
     * @return The Tax Amount
     */
	public static long calculateRegularSalesUnitPrice(final Item item,
			final long salesprice, final int rounding) {

		switch (item.getTaxType()) {
			case Item.TAX_EXCLUDED:
				return getSalesPriceWithTaxRounding(salesprice, item.getTaxRate(),
						rounding);
			case Item.TAX_INCLUDED:
				 /* 1.01 2014.12.31 商品情報取得 DEL START*/
				//return getSalesPriceWithTaxRounding(salesprice, item.getTaxRate(),
				//		rounding);
				 /* 1.01 2014.12.31 商品情報取得 DEL END*/
			case Item.TAX_FREE:
		default:
			return salesprice;
		}

	}
    /**
     * Calculate the Sales Unit Price of the item.
     * @param item  The target Item.
     * @param regSalesUnitPrice The Item's Regular Sales Unit Price.
     * @param rounding  The Price include Tax.
     * @return The Sales Unit Price
     */
    public static long calculateSalesUnitPrice(final Item item,
            final long regSalesUnitPrice, final int rounding) {
        
    	switch (item.getTaxType()) {
			case Item.TAX_EXCLUDED:
				return getSalesUnitPriceWithTaxRounding(regSalesUnitPrice,
						item.getTaxRate(), rounding);
			case Item.TAX_INCLUDED:
				return getSalesUnitPriceWithTaxRounding(regSalesUnitPrice,
						item.getTaxRate(), rounding);
			case Item.TAX_FREE:
        default:
            return regSalesUnitPrice;
      }
    }
    
    public static double calculateActualSalesPrice(final Item item) {
    	double actualSalesPrice = item.getRegularSalesUnitPrice();
    	if (item.getDiscountType() == 0) { // if item is discountable
    		if (item.getDiscountFlag() == 0) { // discount by amount
    			if (item.getDiscountAmount() <= actualSalesPrice) {
    				actualSalesPrice -= item.getDiscountAmount();
    			}
    		} else if (item.getDiscountFlag() == 1) { // discount by rate/percent
    			double discountAmount = Math.floor(
    					actualSalesPrice * item.getDiscount() / 100);
    			actualSalesPrice -= discountAmount;
    		}
    	}
    	
    	return actualSalesPrice;
    }
    
    /**
     *  Code extrated from calculateSalesUnitPrice
     * @param salesprice
     * @param taxRate
     * @param rounding
     * @return
     */
	private static long getSalesPriceWithTaxRounding(long salesprice, int taxRate,
			int rounding) {

		double salespricewithtaxAmount = salesprice + salesprice
				* (Double.valueOf(taxRate) / GlobalConstant.PERCENT);
		switch (rounding) {
			case Item.ROUND_OFF:
				return (long) Math.round(salespricewithtaxAmount);
			case Item.ROUND_UP:
				return (long) Math.ceil(salespricewithtaxAmount);
			case Item.ROUND_DOWN:
		default:
			return (long) Math.floor(salespricewithtaxAmount);
		}

	}	
	
	/**	
	 * Code extrated from calculateSalesUnitPrice
	 * @param regSalesUnitPrice
	 * @param taxRate
	 * @param rounding
	 * @return
	 */
	private static long getSalesUnitPriceWithTaxRounding(long regSalesUnitPrice, int taxRate,
			int rounding) {

		double salesprice =
                (regSalesUnitPrice * GlobalConstant.PERCENT)
                / (double) (GlobalConstant.PERCENT + taxRate);
        switch (Integer.valueOf(rounding)) {
	        case Item.ROUND_OFF:
	                return (long) Math.round(salesprice);
	        case Item.ROUND_UP:
	            return (long) Math.floor(salesprice);
	        case Item.ROUND_DOWN:
        default:
            return (long) Math.ceil(salesprice);
        }

	}
    
}
