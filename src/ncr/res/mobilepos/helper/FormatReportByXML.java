package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncr.res.mobilepos.helper.EnumConst.XmlAlign;
import ncr.res.mobilepos.helper.EnumConst.XmlTag;
import ncr.res.mobilepos.report.model.ItemMode;
import ncr.res.mobilepos.report.model.ReportMode;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class FormatReportByXML extends Formatter {
    private int paperWidth ;
    private final static int BOTH_OBJECT_NULL = 0;
    private final static int EITHER_OBJECT_NULL = 9;
    /**
     * the class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /** layout xml path **/
    private String xmlPath = "";
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "ReportFmt";

    public FormatReportByXML() {
        DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    public FormatReportByXML(String path) {
        DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.xmlPath = path;
    }

    public FormatReportByXML(ReportMode report, String xmlPath)
            throws IllegalArgumentException, SecurityException,
            UnsupportedEncodingException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ParseException {
        DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.report = report;
        this.xmlPath = xmlPath;
        this.format();
    }

    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     * @throws ParseException
     *
     */
    private void format() throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, UnsupportedEncodingException, ParseException {

        if (this.xmlPath == null || "".equals(this.xmlPath)) {
            LOGGER.logAlert(PROG_NAME, "format()", Logger.RES_EXCEP_GENERAL,
                    "xml path is null.");
            return;
        }

        SAXReader sr = new SAXReader();
        try {
            Document doc = sr.read(new File(this.xmlPath));
            for (Iterator<?> it = doc.getRootElement().elementIterator(); it
                    .hasNext();) {
                Element root = (Element) it.next();
                if (root.attributeValue("type").equalsIgnoreCase(
                        report.getReportType())) {
                    this.paperWidth = StringUtility.convNullToZero(root
                            .attributeValue("pagerWidth"));
                    String language = this.report.getLanguage();
                    Locale locale = null;
                    if (StringUtility.isNullOrEmpty(language)
                            || !"en".equals(language)) {
                        locale = Locale.JAPANESE;
                    } else {
                        locale = Locale.ENGLISH;
                    }
                    this.rb = ResourceBundle.getBundle("label", locale);
                    this.resolveLines(root);
                    break;
                }
            }
        }catch (DocumentException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    "format: Failed to format layout xml file.", e);
        }
    }

    /**
     *
     * @param line
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws UnsupportedEncodingException
     * @throws ParseException
     */
    private void lineItems(final Element line) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, UnsupportedEncodingException, ParseException {
        List<ItemMode> itemList = this.report.getRptlist();
        for (ItemMode itemMode : itemList) {
            for (Iterator<?> lineItem = line.elementIterator(); lineItem
                    .hasNext();) {
                Element item = (Element) lineItem.next();
                this.resolveLineElement(item, itemMode);
            }
        }
    }
    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     * @throws ParseException
     *
     */
    private void resolveLines(Element lines) throws IllegalArgumentException,
            SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            UnsupportedEncodingException, ParseException {

        for (Iterator<?> it = lines.elementIterator(); it.hasNext();) {

            Element line = (Element) it.next();

            switch (XmlTag.valueOf(line.getName())) {
            case LineItems:

                lineItems(line);
                break;
            case Line:

                this.resolveLineElement(line, this.report);
                break;
            case Barcode:

                this.addBarcode(line, this.report);
                break;
            case LineImageBmp:

                this.addImageBmp(line);
                break;
            default:
                break;
            }
        }
    }

    /**
     * Helper method of resolveLineElement
     *
     * @param remarksColumnFlg
     * @param remarksColumnAlign
     * @param sb
     * @param size
     * @param align
     * @param feeNum
     * @throws UnsupportedEncodingException
     */
    private void resolveData(String remarksColumnFlg,
            int remarksColumnAlign, StringBuilder sb, int size,
            int align, int feeNum) throws UnsupportedEncodingException {
        if (!StringUtility.isNullOrEmpty(sb.toString())) {

            if ("true".equalsIgnoreCase(remarksColumnFlg)) {
                if (remarksColumnAlign == 2) {
                    if ("en".equals(this.report.getLanguage())) {
                        this.addRemarksColumn(0, sb.toString());
                    } else {
                        this.addRemarksColumn(1, sb.toString());
                    }
                } else if (remarksColumnAlign == 3) {
                    if ("en".equals(this.report.getLanguage())) {
                        this.addRemarksColumn(1, sb.toString());
                    } else {
                        this.addRemarksColumn(0, sb.toString());
                    }
                } else {
                    this.addRemarksColumn(remarksColumnAlign, sb.toString());
                }
            } else if (sb.indexOf("|") > 0) {

                String[] lines = sb.toString().split("\\u007C");

                for (String string : lines) {
                    this.addLineToReceipt(size, align, 0, string);
                }
            } else {
                this.addLineToReceipt(size, align, feeNum, sb.toString());
            }

        }
    }

    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     * @throws ParseException
     *
     */
    private void resolveLineElement(Element line, Object obj)
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, UnsupportedEncodingException, ParseException {

        if (line.elements().isEmpty()) {
            if ("true"
                    .equalsIgnoreCase(line.attributeValue("remarksColumnFlg"))) {
                this.addRemarksColumn(0, "");
            } else if (!"0".equals(line.attributeValue("feedNum"))) {
                addLineToReceipt(this.setFeedLines(StringUtility
                        .convNullToZero(line.attributeValue("feedNum"))));
            }
        } else if (!line.elements().isEmpty()) {

            int size = StringUtility
                    .convNullToZero(line.attributeValue("size"));
            int align = XmlAlign.valueOf(line.attributeValue("align"))
                    .getValue();
            int feeNum = StringUtility.convNullToZero(line
                    .attributeValue("feedNum"));
            String remarksColumnFlg = StringUtility.convNullToEmpty(line
                    .attributeValue("remarksColumnFlg"));
            int remarksColumnAlign = StringUtility.convNullToZero(line
                    .attributeValue("remarksColumnAlign"));

            StringBuilder sb = new StringBuilder();

            int length = this.lineMaxLimit;
            for (Iterator<?> it = line.elementIterator(); it.hasNext();) {

                Element item = (Element) it.next();

                sb.append(this.resolveLineItems(item, obj, length));

                length = this.lineMaxLimit;
                length -= sb.toString().getBytes("MS932").length;
            }
            resolveData(remarksColumnFlg, remarksColumnAlign, sb, size,
                    align, feeNum);
        }
    }

    /**
     * BMP解析
     * @param line
     */
    private void addImageBmp(Element line){
        String typeName = line.attributeValue("name");
        this.addBmp(typeName);
    }

    /**
     * Helper method of ResolveLineItems
     *
     * @param item
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private String lineItems(Element item, Object obj)
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException {
        StringBuilder sb = new StringBuilder("");
        String fixedLength = item.attributeValue("fixedLength");
        String appendSign = item.attributeValue("appendSign");
        String appendWay = item.attributeValue("appendWay");
        String property = item.attributeValue("property");
        if (!StringUtility.isNullOrEmpty(property)) {
            if (!StringUtility.isNullOrEmpty(fixedLength)
                    && !StringUtility.isNullOrEmpty(appendSign)) {
                String invokeMethodStr = this.invokeMethod(property, obj);
                sb.append(this.appendSignForString(invokeMethodStr, appendSign,
                        appendWay, Integer.valueOf(fixedLength)));
            } else {
                sb.append(this.invokeMethod(property, obj));
            }
        }
        return sb.toString();
    }

    /**
     * Helper method of ResolveLineItems
     *
     * @param item
     * @return sb
     */
    private String lineLabel(Element item) {
        StringBuilder sb = new StringBuilder("");
        String fixedLength = item.attributeValue("fixedLength");
        String appendSign = item.attributeValue("appendSign");
        String appendWay = item.attributeValue("appendWay");
        String property = item.attributeValue("property");
        if (!StringUtility.isNullOrEmpty(property)) {
            if (!StringUtility.isNullOrEmpty(fixedLength)
                    && !StringUtility.isNullOrEmpty(appendSign)) {
                String invokeMethodStr = this.getLableProperty(property);
                sb.append(this.appendSignForString(invokeMethodStr, appendSign,
                        appendWay, Integer.valueOf(fixedLength)));
            } else {
                sb.append(this.getLableProperty(property));
            }
        }
        return sb.toString();
    }

    /**
     * Helper method of ResolveLineItems
     *
     * @param item
     * @return sb
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NumberFormatException
     */
    private String linePrice(Element item, Object obj)
            throws NumberFormatException, IllegalArgumentException,
            SecurityException, IllegalAccessException,
            InvocationTargetException {
        String priceAfterChange = null;
        StringBuilder sb = new StringBuilder("");
        String fixedLength = item.attributeValue("fixedLength");
        String appendSign = item.attributeValue("appendSign");
        String appendWay = item.attributeValue("appendWay");
        String property = item.attributeValue("property");
        String unitFlg = item.attributeValue("unitFlg");
        int minus = 1;

        if (Boolean.valueOf(item.attributeValue("isMinus"))) {
            minus = -1;
        }

        double price = Double.valueOf(this.invokeMethod(property, obj));

        if (unitFlg != null && Boolean.valueOf(unitFlg)) {
            priceAfterChange = this.getCurrencySymbol(price);
        }else if(!StringUtility.isNullOrEmpty(fixedLength) &&
                !StringUtility.isNullOrEmpty(appendSign)){
            DecimalFormat decimalFormat = new DecimalFormat();
            priceAfterChange = this.appendSignForPrice(decimalFormat.format(price),
                    appendSign, appendWay, Integer.valueOf(fixedLength));
        }else{
            priceAfterChange = this.getAmountByMode(price, minus);
        }
        sb.append(priceAfterChange);
        return sb.toString();
    }

    /**
     * Helper method of ResolveLineItems
     *
     * @param item
     * @return sb
     */
    private String lineSign(Element item) {

        StringBuilder sb = new StringBuilder("");
        int slength = StringUtility.convNullToZero(item
                .attributeValue("length"));
        String sign = item.attributeValue("value");
        if ("null".equalsIgnoreCase(sign)) {
            this.receiptCmd.add(0, null);
        } else if ("\\n".equals(sign)) {
            sb.append("\n" + new String(this.setCharHeight(0))
                    + new String(this.getAlign(1)));
        } else {
            sb.append(this.getSign(slength, sign));
        }

        return sb.toString();
    }

    /**
     * Helper method of ResolveLineItems
     * @param conElement
     * @param obj
     * @param length
     * @return
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws ParseException
     */
    private String lineCondition(Element conElement, Object obj, int length)
            throws UnsupportedEncodingException, IllegalArgumentException,
            SecurityException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ParseException {
        StringBuilder sb = new StringBuilder("");
        int tempLen = length;
        for (Iterator<?> it = conElement.elementIterator(); it
                .hasNext();) {
            Element conItem = (Element) it.next();
            sb.append(this.resolveLineItems(conItem, obj, tempLen));
            tempLen = length;
            tempLen -= sb.toString().getBytes("MS932").length;
        }

        return sb.toString();
    }

    /**
     * Helper method of ResolveLineItems
     *
     * @param leftMargin
     * @param sb
     * @param length
     * @return
     * @throws UnsupportedEncodingException
     */
    private String containsMaxLeftMargin(String leftMargin, StringBuilder sb, int length)
            throws UnsupportedEncodingException {
        if (leftMargin.contains("max")) {
            int itemSize = sb.toString().getBytes("MS932").length;
            if (leftMargin.length() > 4) {
                sb.insert(
                        0,
                        this.getBlanks(length - itemSize
                                - Integer.parseInt(leftMargin.substring(4))));
            } else {
                sb.insert(0, this.getBlanks(length - itemSize));
            }
        } else {
            sb.insert(0,
                    this.getBlanks(StringUtility.convNullToZero(leftMargin)));
        }
        return sb.toString();
    }

    /**
     *
     * @throws UnsupportedEncodingException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws ParseException
     *
     */
    private String resolveLineItems(Element item, Object obj, int length)
            throws UnsupportedEncodingException, IllegalArgumentException,
            SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ParseException {

        StringBuilder sb = new StringBuilder();

        String leftMargin = "0";
        switch (XmlTag.valueOf(item.getName())) {
        case Item:
            leftMargin = item.attributeValue("leftMargin");
            sb.append(lineItems(item, obj));
            break;
        case Lable:
            leftMargin = item.attributeValue("leftMargin");
            sb.append(lineLabel(item));
            break;
        case Price:
            leftMargin = item.attributeValue("leftMargin");
            sb.append(linePrice(item, obj));
            break;
        case Sign:
            leftMargin = item.attributeValue("leftMargin");
            sb.append(lineSign(item));
            break;
        case Condition:
            if (this.judgment(item, obj)) {
                sb.append(this.stringElementForXMLcondition(item, obj, length));
            }
            break;
        case Date:
            leftMargin = item.attributeValue("leftMargin");
            sb.append(this.dateFormat(item, obj));
            break;
        default:
            break;
        }

        return containsMaxLeftMargin(leftMargin, sb, length);
    }
    /**
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws ParseException
     *
     */
    private String dateFormat(Element item, Object obj)
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException, ParseException {

        SimpleDateFormat formatTo = new SimpleDateFormat(
                item.attributeValue("formatTo"));

        if ("true".equals(item.attributeValue("isSysDate"))) {

            return formatTo.format(new Date());
        } else {
            String date = this.invokeMethod(item.attributeValue("property"),
                    obj);
            SimpleDateFormat formatFrom = new SimpleDateFormat(
                    item.attributeValue("formatFrom"));

            return formatTo.format(formatFrom.parse(date));
        }

    }

    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     * @throws ParseException
     *
     */
    private void addBarcode(Element barcodeElement, Object obj)
            throws UnsupportedEncodingException, IllegalArgumentException,
            SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ParseException {

        StringBuilder sb = new StringBuilder();
        for (Iterator<?> it = barcodeElement.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();

            sb.append(this.resolveLineItems(element, obj, 0));
        }

        if (sb.length() > 0) {
            byte[] barcode = this.getBarCode(
                    XmlAlign.valueOf(barcodeElement.attributeValue("align"))
                            .getValue(), StringUtility
                            .convNullToZero(barcodeElement
                                    .attributeValue("width")), StringUtility
                            .convNullToZero(barcodeElement
                                    .attributeValue("high")), StringUtility
                            .convNullToZero(barcodeElement
                                    .attributeValue("pitch")), StringUtility
                            .convNullToZero(barcodeElement
                                    .attributeValue("posit")), StringUtility
                            .convNullToZero(barcodeElement
                                    .attributeValue("type")), sb.toString());

            this.receiptCmd.add(barcode);
        }

    }

    /**
     * Helper method of evalueCondition
     *
     * @param condition1
     * @param condition2
     * @return
     */
    private boolean equal(String condition1, String condition2) {

        boolean result = false;
        if (this.compareTo(condition1, condition2) == BOTH_OBJECT_NULL) {
            result = true;
        }

        return result;
    }

    /**
     * Helper method of evalueCondition
     *
     * @param condition1
     * @param condition2
     * @return
     */
    private boolean notEqual(String condition1, String condition2) {

        boolean result = false;
        if (this.compareTo(condition1, condition2) != BOTH_OBJECT_NULL) {
            result = true;
        }

        return result;
    }

    private boolean greaterThan(String condition1, String condition2) {

        boolean result = false;
        if (this.compareTo(condition1, condition2) > BOTH_OBJECT_NULL
                && this.compareTo(condition1, condition2) != EITHER_OBJECT_NULL) {
            result = true;
        }

        return result;
    }

    /**
     * @param condition1
     * @param condition2
     * @return
     */
    private boolean lessthan(String condition1, String condition2) {

        boolean result = false;
        if (this.compareTo(condition1, condition2) < BOTH_OBJECT_NULL) {
            result = true;
        }

        return result;
    }

    /**
     * @param condition1
     * @param condition2
     * @return
     */
    private boolean greaterEqual(String condition1, String condition2) {

        boolean result = false;
        if (this.compareTo(condition1, condition2) >= BOTH_OBJECT_NULL
                && this.compareTo(condition1, condition2) != EITHER_OBJECT_NULL) {
            result = true;
        }

        return result;
    }

    /**
     * @param condition1
     * @param condition2
     * @return
     */
    private boolean lessthanEqual(String condition1, String condition2) {

        boolean result = false;
        if (this.compareTo(condition1, condition2) <= BOTH_OBJECT_NULL) {
            result = true;
        }

        return result;
    }

    /**
     * Helper method of judgment
     * @param con
     * @param condition1
     * @param condition2
     * @return
     */
    private boolean evaluateCondition(Element con,
            String condition1, String condition2) {
        boolean res = false;
        switch (XmlTag.valueOf(con.getName())) {
        case eq:

            res = equal(condition1, condition2);

            break;
        case ne:

            res = notEqual(condition1, condition2);

            break;
        case gt:

            res = greaterThan(condition1, condition2);

            break;
        case lt:

            res = lessthan(condition1, condition2);

            break;
        case ge:

            res = greaterEqual(condition1, condition2);

            break;
        case le:

            res = lessthanEqual(condition1, condition2);

            break;
        default:
            res = false;
        }

        return res;
    }

    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     *
     */
    private boolean judgment(Element condition, Object obj)
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        String property = condition.attributeValue("property");
        String condition1 = "";
        String condition2 = "";
        String conProperty = "";
        String value = "";

        boolean res = false;

        if (!StringUtility.isNullOrEmpty(property)) {
            condition1 = this.invokeMethod(property, obj);
        }

        for (Iterator<?> it = condition.elementIterator(); it.hasNext();) {

            Element con = (Element) it.next();

            conProperty = con.attributeValue("property");
            value = con.attributeValue("value");
            if (!StringUtility.isNullOrEmpty(value)) {
                condition2 = value;
            } else if (!StringUtility.isNullOrEmpty(conProperty)) {
                condition2 = this.invokeMethod(conProperty, obj);
            }

            res = evaluateCondition(con, condition1, condition2);
            if (!res) {
               break;
            }
        }

        return res;
    }

    /**
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     *
     */
    private String invokeMethod(String name, Object obj)
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException {

        StringBuilder sb = new StringBuilder();
        Object res = null;

        sb.append("get");
        sb.append(name.substring(0, 1).toUpperCase());
        sb.append(name.substring(1));

        try {
            res = obj.getClass().getMethod(sb.toString()).invoke(obj);
        } catch (NoSuchMethodException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    "invokeMethod: Failed to call method " + sb.toString(), e);
        }

        if (res != null) {
            sb.setLength(0);
            sb.append(res);
            return sb.toString();
        } else {
            return "";
        }

    }

    /**
     *
     */
    private String getLableProperty(String property) {

        String res = "";

        try {
        	res = this.rb.getString(property);
        } catch (MissingResourceException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    "getLableProperty: Failed to get label property "
                            + property, e);
            return res;
        }

        return res;
    }

    /**
     *compare
     */
    private int compareTo(String obj1, String obj2) {

        int result = BOTH_OBJECT_NULL;
        if (StringUtility.isNullOrEmpty(obj1)
                && StringUtility.isNullOrEmpty(obj2)) {
            result =  BOTH_OBJECT_NULL;
        } else if (StringUtility.isNullOrEmpty(obj1)
                || StringUtility.isNullOrEmpty(obj2)) {
            result = EITHER_OBJECT_NULL;
        } else {
            if (obj1.getClass().isInstance(obj2)) {
                if (obj1.toString().matches("[0]*[.]?[0]*")
                        && obj2.toString().matches("[0]*[.]?[0]*")) {
                    result = BOTH_OBJECT_NULL;
                } else {
                    result = obj1.toString().toLowerCase().compareTo(obj2.toString().toLowerCase());
                }
            } else {
                result = EITHER_OBJECT_NULL;
            }
        }

        return result;
    }

    /**
     * @param item
     * @return
     * @throws ParseException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     */
    private String stringElementForXMLcondition(Element item, Object obj, int length)
            throws UnsupportedEncodingException, IllegalArgumentException,
            SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ParseException {
        Element conElement = (Element) item.elements().get(
                item.elements().size() - 1);
        return lineCondition(conElement, obj, length);
    }

    /**
     * @param originalValue
     * @param appendValue
     * @param the add sign direction
     * @param fixLength
     * @return builder.toString
     */
    private String appendSignForPrice(String originalValue, String appendValue,
            String direction, int fixLength) {
        StringBuilder builder = new StringBuilder();
        String regx = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regx);
        int length = originalValue.getBytes().length;
        int munus = fixLength - length;
        char[] originalValueChar = originalValue.toCharArray();
        // 負の状況
        if ('-' == originalValueChar[0]) {
            // 実際の長さはfixedlength超え
            if (munus < 0) {
                originalValue = '-' + originalValue.substring(
                        Math.abs(munus) + 1, originalValue.length());
                Matcher matcher = pattern
                        .matcher(originalValue.substring(1, 2));
                int appendSpaceNumber = 0;
                // 追加のスペースの数
                appendSpaceNumber = originalValue.getBytes().length - fixLength;
                if (!matcher.matches()) {
                    originalValue = '-' + originalValue.substring(2,
                            originalValue.length());
                    appendSpaceNumber = originalValue.getBytes().length
                            - fixLength;
                    if (originalValue.getBytes().length == fixLength) {
                        builder.append(originalValue);
                        return builder.toString();
                    } else {
                        // スペースを追加のfunction
                        return addSpaceToOriginalValue(originalValue,
                                appendValue, direction, fixLength,
                                appendSpaceNumber);
                    }
                } else {
                    if (originalValue.getBytes().length == fixLength) {
                        builder.append(originalValue);
                        return builder.toString();
                    } else {
                        // スペースを追加のfunction
                        return addSpaceToOriginalValue(originalValue,
                                appendValue, direction, fixLength, munus);
                    }
                }
            } else if (munus == 0) {// 実際の長さと等しいfixedlength
                builder.append(originalValue);
                return builder.toString();
            } else {
                // スペースを追加のfunction
                return addSpaceToOriginalValue(originalValue, appendValue,
                        direction, fixLength, munus);
            }
        }
        // 負以外の場合
        else {
            // 実際の長さはfixedlength超え
            if (munus < 0) {
                originalValue = originalValue.substring(Math.abs(munus),
                        originalValue.length());
                Matcher matcher = pattern
                        .matcher(originalValue.substring(0, 1));
                if (matcher.matches()) {
                    builder.append(originalValue);
                    return builder.toString();
                } else {
                    originalValue = originalValue.substring(1,
                            originalValue.length());
                    builder.append(originalValue);
                    return builder.toString();
                }
            } else if (munus == 0) {// 実際の長さと等しいfixedlength
                builder.append(originalValue);
                return builder.toString();
            } else {
                // スペースを追加のfunction
                return addSpaceToOriginalValue(originalValue, appendValue,
                        direction, fixLength, munus);
            }
        }
    }

    /**
     * @param originalValue
     * @param appendValue
     * @param the add sign direction
     * @param fixLength
     * @return builder.toString
     */
    private String appendSignForString(String originalValue,
            String appendValue, String direction, int fixLength) {
        StringBuilder builder = new StringBuilder();
        int length = originalValue.getBytes().length;
        int munus = fixLength - length;
        // 実際の長さはfixedlength超え
        if (munus < 0) {
            originalValue = substrOriginalValue(originalValue, fixLength,
                    fixLength);
            int appendSpaceNumber = originalValue.getBytes().length - fixLength;
            if (appendSpaceNumber < 0) {
                // スペースを追加のfunction
                return addSpaceToOriginalValue(originalValue, appendValue,
                        direction, fixLength, appendSpaceNumber);
            }
            return originalValue;
        } else if (munus == 0) {// 実際の長さと等しいfixedlength
            builder.append(originalValue);
            return builder.toString();
        } else {
            // スペースを追加のfunction
            return addSpaceToOriginalValue(originalValue, appendValue,
                    direction, fixLength, munus);
        }
    }

    /**
     * @param originalValue
     * @param appendValue
     * @param the add sign direction
     * @param fixLength
     * @param appendSpaceNumber
     * @return the originalValue after add space
     */
    private String addSpaceToOriginalValue(String originalValue,
            String appendValue, String direction, int fixLength,
            int appendSpaceNumber) {
        StringBuilder builder = new StringBuilder();
        // 右にスペースを追加し
        if (!"left".equalsIgnoreCase(direction)) {
            builder.append(originalValue);
            for (int i = 0; i < Math.abs(appendSpaceNumber); i++) {
                builder.append(appendValue);
            }
            return builder.toString();
        } else {
            // 左にスペースを追加し
            for (int i = 0; i < Math.abs(appendSpaceNumber); i++) {
                builder.insert(0, appendValue);
            }
            builder.append(originalValue);
            return builder.toString();
        }

    }

    /**
     * @param originalValue
     * @param fixLength
     * @return the originalValue after substring
     */
    private String substrOriginalValue(String originalValue, int fixLength,
            int len) {
        if (!StringUtility.isNullOrEmpty(originalValue)) {
            originalValue = new String(originalValue.getBytes());
            if (fixLength > 0 && fixLength < originalValue.getBytes().length) {
                StringBuffer buff = new StringBuffer();
                char originalValueToChar;
                for (int i = 0; i < fixLength; i++) {
                    originalValueToChar = originalValue.charAt(i);
                    if (((buff.toString().getBytes().length == len - 1) && (String
                            .valueOf(originalValueToChar).getBytes().length > 1))
                            || (buff.toString().getBytes().length == len)) {
                        return new String(buff.toString().getBytes());
                    }
                    buff.append(originalValueToChar);
                    if (String.valueOf(originalValueToChar).getBytes().length > 1) {
                        --fixLength;
                    }
                }
                return new String(buff.toString().getBytes());
            }
        }
        return originalValue;
    }
}
