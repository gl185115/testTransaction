package ncr.res.mobilepos.constant;

/**
 * Final Constant Value for LineItem.Tax's Attribute TaxType.
 * This cannot be instantiated as it is in private constructor,
 * And cannot be sub-classed as it is a final class.
 */
public final class TaxTypes {
	private TaxTypes(){		
	}
	/**
	 * Sales TaxType
	 */
	public static final String SALES = "Sales";
	/**
	 * VAT TaxType
	 */
	public static final String VAT = "VAT";
}
