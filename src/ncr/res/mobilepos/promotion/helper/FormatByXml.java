package ncr.res.mobilepos.promotion.helper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.barcodeassignment.model.ItemSale;

public class FormatByXml {
	
    private static final String BARCODE_TYPE = "double";
	
    private static BarcodeAssignment barcodeAssignment;
    
	public FormatByXml() {
		super();
		barcodeAssignment = BarcodeAssignmentFactory.getInstance();
	}
	
	/**
     * Returns if the given barcode belongs to 'Double' item category.
     * @param checkCode code to check
     * @return true if it is double barcode.
     */
    public final boolean isDoubleBarcode(String checkCode) {
        List<ItemSale> saleAssignmentItems  = barcodeAssignment.getSale().getSales();
        // Picks up double barcode AssignmentItems and check matching.
        for (ItemSale saleAssignmentItem : saleAssignmentItems) {
            if (BARCODE_TYPE.equals(saleAssignmentItem.getType()) &&
                    matchCodeWithItem(checkCode, saleAssignmentItem)) {
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if checkCode match with item.
     * @param checkCode
     * @param assignmentItem
     * @return true: match
     */
    private boolean matchCodeWithItem(String checkCode, ItemSale assignmentItem) {
        for(String formatRegEx : assignmentItem.getFormat()) {
            Pattern pattern = Pattern.compile(formatRegEx);
            Matcher matcher = pattern.matcher(checkCode);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
	
    /**
     * get Varieties name and method
     * @param itemId : UI side commodity code
     * @return Barcode : Varieties
     *                   
     */
	public final String getVarietiesName(String itemId) {

		String varietiesName = null;
		List<ItemSale> itemSaleList = barcodeAssignment.getSale().getSales();
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
