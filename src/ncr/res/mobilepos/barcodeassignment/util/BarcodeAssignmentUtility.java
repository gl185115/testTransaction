package ncr.res.mobilepos.barcodeassignment.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.barcodeassignment.model.ItemSale;

/**
 * BarcodeAssignmentUtility.
 * This class hosts utility methods to handle double barcode.
 */
public class BarcodeAssignmentUtility {
    /**
     * Double barcode constant.
     */
    private static final String DOUBLE_BARCODE_ITEMTYPE = "double";
    /**
     * Double barcode length.
     */
    private static final int MAX_RETURN_ARRAY_LENGTH = 2;
    /**
     * Double barcode delimiter.
     */
    private static final String DOUBLE_BARCODE_DELIMITER = " ";
    /**
     * CCode length.
     */
    private static final int CCODE_LENGTH = 4;

    /**
     * Prevents it from instantiation.
     */
    private BarcodeAssignmentUtility() {
    }

    /**
     * Checks if barcode includes multiple lines.
     *
     * @return true:multiple lines, false:single line.
     */
    public static boolean isMultiLineBarcode(String inputCode) {
        return inputCode.trim().contains(DOUBLE_BARCODE_DELIMITER);
    }

    /**
     * Split barcode into arrays.
     * If inputCode is able to split into more than 2 codes, returns first 2 codes.
     * If inputCode only has one code, returns 1length array.
     *
     * @return Array with split codes.
     */
    public static String[] splitDoubleBarcode(String inputCode) {
        String[] splitCodes = inputCode.trim().split(DOUBLE_BARCODE_DELIMITER);
        if (splitCodes.length > MAX_RETURN_ARRAY_LENGTH) {
            return Arrays.copyOfRange(splitCodes, 0, MAX_RETURN_ARRAY_LENGTH);
        }
        return splitCodes;
    }

    /**
     * Checks if given code is for CCode.
     * CCode request has 2 lines and 2nd line has less than or equals to 4 digits.
     *
     * @return true if given code has multiple codes and 2nd line length is less than or equals to 4.
     */
    public static boolean isWithCCode(String inputCode) {
        if (isMultiLineBarcode(inputCode)) {
            String[] codes = splitDoubleBarcode(inputCode);
            if (codes[1].length() == CCODE_LENGTH) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the given barcode is the first line of double barcode.
     *
     * @param inputCode code to check
     * @return true if it is a part of double barcode.
     */
    public static boolean isPartOfDoubleBarcode(String inputCode, BarcodeAssignment barcodeAssignment) {
        List<ItemSale> saleAssignmentItems = barcodeAssignment.getSale().getSales();
        // Picks up double barcode AssignmentItems and check matching.
        for (ItemSale saleAssignmentItem : saleAssignmentItems) {
            if (DOUBLE_BARCODE_ITEMTYPE.equals(saleAssignmentItem.getType()) &&
                    matchCodeWithItemFormat(inputCode, saleAssignmentItem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if inputCode match with item format.
     *
     * @return true: if it matches.
     */
    public static boolean matchCodeWithItemFormat(String inputCode, ItemSale assignmentItem) {
        for (String formatRegEx : assignmentItem.getFormat()) {
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
     *
     * @return true: if it matches.
     */
    public static boolean matchCodeWithItemNextFormat(String inputCode, ItemSale assignmentItem) {
        if (assignmentItem.getNextFormat() == null) {
            return false;
        }
        for (String formatRegEx : assignmentItem.getNextFormat()) {
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
     * Input singleCode can match with a part of Double Assignment.
     *
     * @param singleCode        Single Code
     * @param barcodeAssignment barcode assignment information
     * @return matched ItemSale, null for unmatched.
     */
    public static ItemSale getBarcodeAssignmentItemIdWithSingleBarcode(
            String singleCode, BarcodeAssignment barcodeAssignment) {
        List<ItemSale> saleAssignmentItems = barcodeAssignment.getSale().getSales();
        for (ItemSale assignmentItem : saleAssignmentItems) {
            if (matchCodeWithItemFormat(singleCode, assignmentItem)) {
                return assignmentItem;
            }
        }
        return null;
    }

    /**
     * Returns Assignment Id from given double barcode.
     * If first code matches with format AND second matches with next-format, then returns its ID.
     *
     * @param doubleCode        Double Barcode, this must be 2 length array.
     * @param barcodeAssignment barcode assignment information
     * @return matched ItemSale, null for unmatched.
     */
    public static ItemSale getBarcodeAssignmentItemIdWithDoubleBarcode(
            String[] doubleCode, BarcodeAssignment barcodeAssignment) {
        List<ItemSale> saleAssignmentItems = barcodeAssignment.getSale().getSales();
        for (ItemSale assignmentItem : saleAssignmentItems) {
            // If first-line matches with format and second-line matches with next line, then return Id.
            if (matchCodeWithItemFormat(doubleCode[0], assignmentItem) &&
                    matchCodeWithItemNextFormat(doubleCode[1], assignmentItem)) {
                return assignmentItem;
            }
        }
        return null;
    }

    /**
     * Returns Assignment Id from given barcode.
     * If the code matches with format, then returns its ID.
     * Input SingleCode can match with a part of Double Assignment.
     *
     * @param inputCode         this could be double or single barcode
     * @param barcodeAssignment barcode assignment information
     * @return barcode assignment id, specification of barcode. Blank string for unmatched.
     */
    public static String getBarcodeAssignmentItemId(String inputCode, BarcodeAssignment barcodeAssignment) {
        ItemSale matchedItem;
        if (isMultiLineBarcode(inputCode)) {
            String[] inputDoubleBarcode = splitDoubleBarcode(inputCode);
            matchedItem = getBarcodeAssignmentItemIdWithDoubleBarcode(inputDoubleBarcode, barcodeAssignment);
        } else {
            matchedItem = getBarcodeAssignmentItemIdWithSingleBarcode(inputCode, barcodeAssignment);
        }
        return matchedItem == null ? "" : matchedItem.getId();
    }


}
