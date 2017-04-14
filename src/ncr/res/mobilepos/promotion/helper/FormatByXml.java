package ncr.res.mobilepos.promotion.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.systemconfiguration.model.ItemSale;

public class FormatByXml {
	
    private static final String BARCODE_TYPE = "double";
	
	public FormatByXml() {
		super();
	}
	
	/**
     * judge commodity is The two section?
     * @param itemId : UI side commodity code
     * @return 
     *                   
     */
	public final boolean varietiesJudge(String itemId) {

		boolean flag = false;
		List<ItemSale> items = GlobalConstant.getBarCode().getSale().getSales();
		List<ItemSale> itemList = new ArrayList<ItemSale>();
		List<String> format = null;

		for (ItemSale item : items) {
			if (BARCODE_TYPE.equals(item.getType())) {
				itemList.add(item);
			}
		}

		for (ItemSale item : itemList) {
			format = item.getFormat();
			Pattern pattern = null;
			Matcher matcher = null;
			String regEx = "";

			for (int i = 0; i < format.size(); i++) {
				regEx = format.get(i);
				pattern = Pattern.compile(regEx);
				matcher = pattern.matcher(itemId);

				if (matcher.matches()) {
					flag = true;
					
					return flag;
				}
			}
		}
		return flag;
	}
	
    /**
     * get Varieties name and method
     * @param itemId : UI side commodity code
     * @return Barcode : Varieties
     *                   
     */
	public final String getVarietiesName(String itemId) {

		String varietiesName = null;
		List<ItemSale> itemSaleList = GlobalConstant.getBarCode().getSale().getSales();
		List<String> formatList = null;
		String nextItem = "";
		String oneItem = "";
		boolean flag = false;
		String[] twoItem = itemId.split(" ");
		if (twoItem.length >= 2) {
			nextItem = twoItem[1];
			oneItem = twoItem[0];
		} else {
			oneItem = itemId;
		}

		for (ItemSale item : itemSaleList) {
			formatList = item.getFormat();
			List<String> nextFomartList = null;
			Pattern patternNextFormat = null;
			Matcher matcherNextFormat = null;
			Pattern pattern = null;
			Matcher matcher = null;

			for (String format : formatList) {
				pattern = Pattern.compile(format);
				matcher = pattern.matcher(oneItem);

				if (matcher.matches()) {
					flag = true;
					break;
				}
			}

			if (flag) {
				if (item.getNextFormat() != null) {
					nextFomartList = item.getNextFormat();
					for (String nextFormat : nextFomartList) {
						patternNextFormat = Pattern.compile(nextFormat);
						matcherNextFormat = patternNextFormat.matcher(nextItem);

						if (matcherNextFormat.matches()) {
							break;
						}
					}
				}
				varietiesName = item.getId();
				break;
			}
		}

		return varietiesName;
	}
	
}
