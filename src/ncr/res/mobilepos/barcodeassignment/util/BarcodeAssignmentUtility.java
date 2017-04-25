package ncr.res.mobilepos.barcodeassignment.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.barcodeassignment.model.ItemSale;

public class BarcodeAssignmentUtility {
    private static final String BARCODE_TYPE = "double";

    /**
     * Checks if barcode includes multiple lines.
     * @param inputCode
     * @return true:multiple lines, false:single line.
     */
    public static boolean isMultiLineBarcode(String inputCode) {
    	return inputCode.trim().contains(" ");
	}

    /**
     * Split barcode into arrays. Array contains 2 codes.
     * @param inputCode
     * @return 2 length array.
     */
    public static String[] splitDoubleBarcode(String inputCode) {
    	String[] splitCodes = inputCode.trim().split(" ");
        if(splitCodes.length > 2) {
            return Arrays.copyOfRange(splitCodes, 0, 2);
        }
        return splitCodes;
	}

    /**
     * Checks if given code is for CCode.
     * CCode request has 2 lines and 2nd line has less than or equals to 4 digits.
     * @param inputCode
     * @return
     */
	public static boolean isWithCCode(String inputCode) {
		if(isMultiLineBarcode(inputCode)) {
			String[] codes = splitDoubleBarcode(inputCode);
			if(codes[1].length() < 5) {
				return true;
			}
		}
		return false;
	}

	/**
     * Returns if the given barcode is the first line of double barcode.
     * @param inputCode code to check
     * @return true if it is a part of double barcode.
     */
    public static boolean isPartOfDoubleBarcode(String inputCode, BarcodeAssignment barcodeAssignment) {
        List<ItemSale> saleAssignmentItems  = barcodeAssignment.getSale().getSales();
        // Picks up double barcode AssignmentItems and check matching.
        for (ItemSale saleAssignmentItem : saleAssignmentItems) {
            if (BARCODE_TYPE.equals(saleAssignmentItem.getType()) &&
					matchCodeWithItemFormat(inputCode, saleAssignmentItem)) {
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if inputCode match with item format.
     * @param inputCode
     * @param assignmentItem
     * @return true: if it matches.
     */
    public static boolean matchCodeWithItemFormat(String inputCode, ItemSale assignmentItem) {
        for(String formatRegEx : assignmentItem.getFormat()) {
            Pattern pattern = Pattern.compile(formatRegEx);
            Matcher matcher = pattern.matcher(inputCode);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

	/**
	 * Checks if checkCode match with item.
	 * @param inputCode
	 * @param assignmentItem
	 * @return true: if it matches.
	 */
	public static boolean matchCodeWithItemNextFormat(String inputCode, ItemSale assignmentItem) {
		if(assignmentItem.getNextFormat() == null) {
			return false;
		}
		for(String formatRegEx : assignmentItem.getNextFormat()) {
			Pattern pattern = Pattern.compile(formatRegEx);
			Matcher matcher = pattern.matcher(inputCode);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Returns Assignment Id from given single barcode.
	 * @param singleCode Single Code
	 * @param barcodeAssignment barcode assignment information
	 * @return barcode assignment id, specification of barcode
	 */
	public static String getBarcodeAssignmentItemIdWithSingleBarcode(
			String singleCode, BarcodeAssignment barcodeAssignment) {
		List<ItemSale> saleAssignmentItems = barcodeAssignment.getSale().getSales();
		for (ItemSale assignmentItem : saleAssignmentItems) {
			if(matchCodeWithItemFormat(singleCode, assignmentItem)) {
				return assignmentItem.getId();
			}
		}
		return "";
	}

	/**
	 * Returns Assignment Id from given barcode.
	 * @param inputCode this could be double or single barcode
	 * If the code matches with format, then returns its ID.
	 * @param barcodeAssignment 	barcode assignment information
	 * @return barcode assignment id, specification of barcode
	 */
	public static String getBarcodeAssignmentItemId(String inputCode, BarcodeAssignment barcodeAssignment) {
		if(isMultiLineBarcode(inputCode)) {
			String[] inputDoubleBarcode = splitDoubleBarcode(inputCode);
			return getBarcodeAssignmentItemIdWithDoubleBarcode(inputDoubleBarcode, barcodeAssignment);
		}
		return getBarcodeAssignmentItemIdWithSingleBarcode(inputCode, barcodeAssignment);
	}

    /**
     * Returns Assignment Id from given double barcode.
	 * If first code matches with format AND second matches with next-format, then returns its ID.
     * @param doubleCode Double Barcode, this must be 2 length array.
	 * @param barcodeAssignment 	barcode assignment information
	 * @return barcode assignment id, specification of barcode
     */
	public static String getBarcodeAssignmentItemIdWithDoubleBarcode(
			String[] doubleCode, BarcodeAssignment barcodeAssignment) {
		List<ItemSale> saleAssignmentItems = barcodeAssignment.getSale().getSales();
		for (ItemSale assignmentItem : saleAssignmentItems) {
			// If first-line matches with format and second-line matches with next line, then return Id.
			if(matchCodeWithItemFormat(doubleCode[0], assignmentItem) &&
					matchCodeWithItemNextFormat(doubleCode[1], assignmentItem)) {
				return assignmentItem.getId();
			}
		}
		return "";
	}

}
