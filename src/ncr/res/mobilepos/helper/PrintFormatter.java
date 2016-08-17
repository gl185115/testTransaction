package ncr.res.mobilepos.helper;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * handles print formatting.
 *
 */
public class PrintFormatter {

    /**
     * maximum number of lines.
     */
    private int linemax = 35;

    /**
     * constructor.
     * @param lineMax - maximum amount of lines to set
     */
    public PrintFormatter(final int lineMax) {
        this.linemax = lineMax;
    }

    /**
     * Add Separator.
     * @param printLines - list of lines to be printed
     * @param sep - the separator to add
     */
    public final void addSeparator(
            final List<String> printLines, final String sep) {
        if (" ".equals(sep) || "".equals(sep) || sep == null) {
            printLines.add(" ");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < linemax; i++) {
                sb.append(sep);
            }
            printLines.add(sb.toString());
        }
    }

    /**
     * Add text in Left Align.
     * @param printLines - list of lines to be printed
     * @param text - text to be added
     */
    public final void addTextLeftAlign(
            final List<String> printLines, final String text) {
        if (text == null) {
            printLines.add("");
        } else {
            printLines.add(text);
        }
    }

    /**
     * Add text in Center Align.
     * @param printLines - list of lines to be printed
     * @param text - text to be added
     */
    public final void addTextCenterAlign(
            final List<String> printLines, final String text) {
        if ("".equals(text) || text == null) {
            return;
        }
        String center;
        try {
            center = toCenter(text, linemax);
        } catch (UnsupportedEncodingException e) {
            center = text;
        }
        printLines.add(center);
        return;
    }

    /**
     * Add text in Right Align.
     * @param printLines - list of lines to be printed
     * @param text - text to be added
     */
    public final void addTextRightAlign(
            final List<String> printLines, final String text) {
        if ("".equals(text) || text == null) {
            printLines.add(text);
            return;
        }
        String right = "";
        try {
            right = toRight(text, linemax);
        } catch (UnsupportedEncodingException e) {
            right = text;
        }
        printLines.add(right);
        return;
    }

    /**
     * Add Item and value.
     * Item is left align and value is right align
     * @param printLines - list of lines to be printed
     * @param item - item to be added
     * @param value - value to be added
     */
    public final void addItemStr(final List<String> printLines,
            final String item, final String value) {
        String actitem = item;
        String actValue = value;
        if (item == null) {
            actitem = "";
        }
        if (value == null) {
            actValue = "";
        }
        try {
            if ((actitem.getBytes("MS932").length
                    + actValue.getBytes("MS932").length) > linemax) {
                printLines.add(actitem);
                if (actValue.getBytes("MS932").length > linemax) {
                    printLines.add(actValue);
                } else {
                    printLines.add(toRight(actValue, linemax));
                }
            } else {
                int num = linemax
                    - (actitem.getBytes("MS932").length
                            + actValue.getBytes("MS932").length);
                printLines.add(actitem + getSpaces(num) + actValue);
            }
        } catch (UnsupportedEncodingException e) {
            printLines.add(actitem);
            printLines.add(actValue);
        }
        return;
    }

    /**
     * Add Item and Value.
     * Item is left align and value is left align like TD left align
     * @param printLines - list of lines to be printed
     * @param item - item to be added
     * @param value - value to be added
     * @param align - integer code of alignment desired
     */
    public final void addItemStrLeft(final List<String> printLines,
            final String item, final String value, final int align) {
        String actitem = item;
        String actValue = value;
        if (item == null) {
            actitem = "";
        }
        if (value == null) {
            actValue = "";
        }
        try {
            if (align < actitem.getBytes("MS932").length) {
                printLines.add(actitem);
                printLines.add(getSpaces(align) + actValue);
            } else {
                printLines.add(actitem + getSpaces(align
                        - actitem.getBytes("MS932").length) + actValue);
            }
        } catch (UnsupportedEncodingException e) {
            printLines.add(actitem);
            printLines.add(actValue);
        }
        return;
    }
    
    public final void addUnderbar(final List<String> printLines,
            final String item) {
        String actitem = item;
        if (item == null) {
            actitem = "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            int itemLen = actitem.getBytes("MS932").length;
            for (int i = 0; i < linemax - itemLen; i++) {
                sb.append("_");
            }
            printLines.add(actitem + sb.toString());
        } catch (UnsupportedEncodingException e) {
            printLines.add(actitem);
        }
        return;
    }

    /**
     * get byte array for printer.
     * @param lines - list of lines to be printed
     * @return byte array format of the lines that were passed
     * @throws UnsupportedEncodingException - thrown when encountering
     * an unsupported encoding
     */
    public final byte[] getByteArrayForPrinter(final List<String> lines)
    throws UnsupportedEncodingException {
        byte[] text = new byte[lines.size() * (linemax + 5)];
        byte[] endLine = {0x0A};
        int index = 0;
        for (String line : lines) {
            byte[] lineBits = line.getBytes("MS932");
            byte[] temp = new byte[lineBits.length + 1];
            System.arraycopy(lineBits, 0, temp, 0, lineBits.length);
            System.arraycopy(endLine, 0, temp, lineBits.length, endLine.length);
            System.arraycopy(temp, 0, text, index, temp.length);
            index = index + temp.length;
        }
        return text;
    }

    /**
     * Get the formatted price value in yen currency.
     *
     * @param amt to format in yen currency
     *
     * @return string of cultures currency symbol e.g. \( 1,000)
     */
    public final String getCurrencySymbol(final double amt) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        DecimalFormat df = (DecimalFormat) nf;
        return df.format(amt);
    }

    /**
     * Get the formatted price value in yen currency.
     *
     * @param amt to format in yen currency
     * @param language en/ja used to determine what symbol and
     *          if decimal is used
     * @return string of cultures currency symbol e.g. \( 1,000)
     */
    public final String getCurrencySymbol(
            final String language, final double amt) {
        Locale locale = Formatter.getLanguage(language);
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        double amount = amt;
        if (!"ja".equals(language)) {
            amount = amt / 100;
        }
        return df.format(amount);
    }
    
    /**
     * get the formatted price value with comma.
     * @param amt - amount to format
     * @param language - en/ja determines whether to use decimal
     * @return e.g. 1000->1,000
     */
    public String getNumberWithComma(
            final String language, final double amt) {
        DecimalFormat df = new DecimalFormat();
        double amount = amt;
        if (!"ja".equals(language)) {
            df.applyPattern("###,###,##0.00");
            amount = amt / 100;
        }
        return df.format(amount);
    }

    /**
     * move data to center.
     * @param data - the data to move.
     * @param digits - number of digits of the data
     * @return String - the centered string
     * @throws UnsupportedEncodingException - thrown when encountering
     * an unsupported encoding
     */
    private String toCenter(final String data, final int digits)
    throws UnsupportedEncodingException {
        String center = null;
        int num = (digits - data.getBytes("MS932").length) / 2;
        center = getSpaces(num) + data;

        return center;
    }

    /**
     * move data to right.
     * @param data - the data to move.
     * @param digits - number of digits of the data
     * @return String - the centered string
     * @throws UnsupportedEncodingException - thrown when encountering
     * an unsupported encoding
     */
    private String toRight(final String data, final int digits)
    throws UnsupportedEncodingException {
        String right = null;
        int num = digits - data.getBytes("MS932").length;
        if (num < 0) {
            return data;
        }
        right = getSpaces(num) + data;

        return right;
    }

    /**
     * retrieve a string with the specified number
     * of spaces.
     * @param num - number of spaces
     * @return String - string with spaces
     */
    public String getSpaces(final int num) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < num; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }
    
    /**
     * Helper method of getReceiptDateTime
     * 
     * @param receiptCal
     * @param rb
     * @return
     */
    private String getWeek(Calendar receiptCal, ResourceBundle rb ) {
    	String week = null;
		switch (receiptCal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			week = rb.getString("recptsunday");
			break;
		case Calendar.MONDAY:
			week = rb.getString("recptmonday");
			break;
		case Calendar.TUESDAY:
			week = rb.getString("recpttuesday");
			break;
		case Calendar.WEDNESDAY:
			week = rb.getString("recptwednesday");
			break;
		case Calendar.THURSDAY:
			week = rb.getString("recptthursday");
			break;
		case Calendar.FRIDAY:
			week = rb.getString("recptfriday");
			break;
		case Calendar.SATURDAY:
			week = rb.getString("recptsaturday");
			break;
		default:
			break;
		}
        
        return week;
    }
    /**
     * gets the localized date time for the receipt.
     * @param dateTime - date/time string to localize
     * @param language - language to use
     * @return localized and formated date string
     */
    public final String getReceiptDateTime(
            final String dateTime, final String language) {
        //add receipt date time  format: YYYY”NMMŒŽDD“ú(ŒŽ) hh:mm:ss
        if (dateTime == null || "".equals(dateTime)) {
            return dateTime;
        }
        String receiptDateTime = null;
        Locale locale = Formatter.getLanguage(language);
        //get Resource bundle
        ResourceBundle rb = ResourceBundle.getBundle("label", locale);

        String[] tmp = dateTime.split("T");
        String[] date = tmp[0].split("-");

        String week = null;

        Calendar receiptCal = Calendar.getInstance();
        receiptCal.clear();
        receiptCal.set(Integer.valueOf(date[0]), Integer.valueOf(date[1])
                - 1, Integer.valueOf(date[2]));
        week = getWeek(receiptCal, rb);
        receiptDateTime = date[0] + rb.getString("recptyear")
                        + date[1] + rb.getString("recptmonth")
                        + date[2] + rb.getString("recptday")
                        + "(" + week + ") "
                        + tmp[1];

        return receiptDateTime;
    }
}
