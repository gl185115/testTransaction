package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import ncr.res.mobilepos.helper.EnumConst.XmlAlign;
import ncr.res.mobilepos.helper.EnumConst.XmlTag;
import ncr.res.mobilepos.networkreceipt.model.ItemMode;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;
import ncr.res.mobilepos.simpleprinterdriver.IPrinter;

/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2015.02.10      FENGSHA       領収書レシートの80mm紙変更対応
 */
public class FormatSummaryReceiptByXML extends Formatter {
    public final static boolean TAXDOC_EXIST = false;
    private final static int BOTH_OBJECT_NULL = 0;
    private final static int EITHER_OBJECT_NULL = 9;
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "FormatSummaryReceiptByXML";
    /**
     * the class instance of the debug trace printer.
     */
    // print summary receipt by vertical
    private static final String VERTICAL = "v";
    // print summary receipt by horizontal
    private static final String HORIZONTAL = "h";
    // default page mode
    private boolean pageModeFlag = false;
    private String xmlPath;
    // the recipt width
    private int paperWidth;
    private int propertyLength = 0;
    private String printDirection = VERTICAL;
    private int addLineCommandByteCount = 0;
    public FormatSummaryReceiptByXML(ReceiptMode receipt, String xmlPath)
            throws IllegalArgumentException, SecurityException,
            UnsupportedEncodingException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ParseException {
        DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.xmlPath = xmlPath;
        this.receipt = receipt;
        try {
            this.format();
        } catch (NamingException e) {
            LOGGER.logAlert(PROG_NAME, PROG_NAME+".Constructor", Logger.RES_EXCEP_GENERAL, "Failed to format receipt. "+e.getMessage());
        }
    }

	public FormatSummaryReceiptByXML() {}
    
    /**
     * select print direction
     */
    private void selectPrintDirection(int high, int align, int feeNum,
            String text) {
        //command of cancel underline
        byte[] cancelUnderline = new byte[] { 0x1b, 0x16, 0x00 };
        //command for underline
        byte[] underline = new byte[] { 0x1b, 0x16, 0x01 };
        //judge the direction of printing receipt
        if (!VERTICAL.equals(this.printDirection)) {
            super.addLineToReceipt(high, align, feeNum, text);
        } else {
            //1:center 2:right
            if (align == 1) {
                text = new String(underline)
                        + this.getBlanks((this.paperWidth - this.propertyLength) / 2)
                        + new String(cancelUnderline)
                        + text ;
            } else if (align == 2) {
                text = new String(underline)
                        + this.getBlanks(this.paperWidth - this.propertyLength)
                        + new String(cancelUnderline)
                        + text ;
            }
            super.addLineToReceipt(high, feeNum, text);
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
     * @throws NamingException
     *
     */
    private void format() throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, UnsupportedEncodingException,
            ParseException, NamingException {

        SAXReader sr = new SAXReader();
        try {
            Document doc = sr.read(new File(this.xmlPath));
            for (Iterator<?> it = doc.getRootElement().elementIterator(); it
                    .hasNext();) {
                Element root = (Element) it.next();
                //search for witch receipt template used for printing
                if ("true".equalsIgnoreCase(root.attributeValue("used"))) {
                    this.paperWidth = StringUtility.convNullToZero(root
                            .attributeValue("pagerWidth"));
                    if (!VERTICAL.equalsIgnoreCase(root.attributeValue("type"))) {
                        printDirection = HORIZONTAL;
                        this.receipt.setDirection(printDirection);
                        this.resolveLines(root);
                    } else {
                        printDirection = VERTICAL;
                         this.docTaxIsFirst(root);
                        this.receipt.setDirection(printDirection);
                        super.convertPageMode(this.paperWidth, false);
                        this.resolveLines(root);
                    }
                    break;
                }
            }
        } catch (DocumentException e) {
            LOGGER.logError(PROG_NAME, PROG_NAME+".format()",
                    Logger.RES_EXCEP_GENERAL,
                    "DocumentException:" + e.getMessage());
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

        //1.01 2015.02.10 FENGSHA ADD START
        boolean isCompressed = false;
        String printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            printPaperLength = (String) env.lookup("Printpaperlength");
        } catch (NamingException e) {
            printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        }
        if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
            isCompressed = true;
        }else{
            isCompressed = false;
        }
        //1.01 2015.02.10 FENGSHA ADD END
        //1.01 2015.02.10 FENGSHA MOD START
//        if(this.pageModeFlag){
//            super.convertPageMode(this.paperWidth);
//        }
        if(this.pageModeFlag){
            super.convertPageMode(this.paperWidth,isCompressed);
        }
       //1.01 2015.02.10 FENGSHA MOD END
        for (Iterator<?> it = lines.elementIterator(); it.hasNext();) {

            Element line = (Element) it.next();
            switch (XmlTag.valueOf(line.getName())) {
            case LineItems:
                this.resolveLineItems(line);
                break;
            case Line:
                this.propertyLength = 0;
                this.resolveLineElement(line, this.receipt);
                break;
            // 2014/07/09 bmpの添加を対応する。START
            case LineImageBmp:
                super.convertStandardMode();
                this.addImageBmp(line);
                if(!this.pageModeFlag){
                    this.pageModeFlag = true;
                    //1.01 2015.02.10 FENGSHA MOD START
                    //super.convertPageMode(this.paperWidth);
                    super.convertPageMode(this.paperWidth,isCompressed);
                  //1.01 2015.02.10 FENGSHA MOD END
                }
                break;
            // 2014/07/09 bmpの添加を対応する。END
            default:
                break;
            }

        }
        super.convertStandardMode();
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
            this.addLineCommandByteCount = 0 ;
            int size = StringUtility
                    .convNullToZero(line.attributeValue("size"));
            int align = XmlAlign.valueOf(line.attributeValue("align"))
                    .getValue();
            int feeNum = StringUtility.convNullToZero(line
                    .attributeValue("feedNum"));
            StringBuilder sb = new StringBuilder();
            int length = this.paperWidth * 76 / 125;
            for (Iterator<?> it = line.elementIterator(); it.hasNext();) {
                Element item = (Element) it.next();
                sb.append(this.resolveLineItems(item, obj, length,size));
                length = this.paperWidth * 76 / 125;
                // the command of fontsize length is three
                //20140806
                length = length - sb.toString().getBytes("MS932").length
                        + addLineCommandByteCount;
            }
            if (!StringUtility.isNullOrEmpty(sb.toString())) {
                selectPrintDirection(size, align, feeNum, sb.toString());
            }
        }
    }

    // 2014/07/09 bmpの添加を対応する。START
    /**
     * BMP解析
     *
     * @param line
     */
    private void addImageBmp(Element line) {
        String typeName = line.attributeValue("name");
        this.addBmp(typeName);
    }

    // 2014/07/09 bmpの添加を対応する。END
    private String addUnderline(Element line) {
        int underlineNumber = StringUtility.convNullToZero(line
                .attributeValue("value"));
        byte[] b = new byte[] { 0x1C, 0x2D, (byte) underlineNumber };
        return new String(b);
    }

    private String cancelUnderline() {
        byte[] b = new byte[] { 0x1C, 0x2D, 0x00 };
        return new String(b);
    }

    private String lineLabel(Element item, boolean isCompressed,
            int lineFontSize) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("");
        String property = item.attributeValue("property");

        String labelSize = item.attributeValue("size");
        byte[] labelByte  = property.getBytes();
        if (labelSize != null && !"".equals(labelSize)){
            int tempFontSize = StringUtility.convNullToZero(labelSize);
            labelByte = super.cmdCopy(setCharHeight(tempFontSize),labelByte);
        } else {
            labelByte = super.cmdCopy(setCharHeight(lineFontSize),labelByte);
        }
        addLineCommandByteCount += 3;

        if (!StringUtility.isNullOrEmpty(property)) {
            if (!isCompressed) {
                this.propertyLength += item.attributeValue("property")
                        .getBytes("MS932").length;
            } else {
                this.propertyLength += item.attributeValue("property")
                        .getBytes("MS932").length * 42 / 32;
            }
            sb.append(new String(labelByte));
        }

        return sb.toString();
    }

    /**
     * Helper method of resolveLineItems
     *
     * @param item
     * @param obj
     * @param isCompressed
     * @param lineFontSize
     * @return sb
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnsupportedEncodingException
     */
    private String lineItem(Element item, Object obj, boolean isCompressed,
            int lineFontSize) throws NumberFormatException,
            IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder("");
        String property = "";
        String itemText = "";

      if (null !=item.getTextTrim() && !"".equals(item.getTextTrim())){
          property = item.getTextTrim();
          itemText = this.calculate(property, obj) + "";
      } else {
          property = item.attributeValue("property");
          if (!StringUtility.isNullOrEmpty(property)) {
              itemText = "" + this.invokeMethod(property, obj);
          }
      }

      String itemSize = item.attributeValue("size");
      byte[] itemByte  = itemText.getBytes();
      if (itemSize != null && !"".equals(itemSize)) {
          int tempFontSize = StringUtility.convNullToZero(itemSize);
          itemByte = super.cmdCopy(setCharHeight(tempFontSize),itemByte);
      } else {
          itemByte = super.cmdCopy(setCharHeight(lineFontSize),itemByte);
      }
      addLineCommandByteCount += 3;
      if (!isCompressed) {
          this.propertyLength += itemText.getBytes("MS932").length * 42 / 32;
      }
      sb.append(new String(itemByte));

      return sb.toString();
    }

    /**
     * Helper method of resolveLineItems
     *
     * @param item
     * @param obj
     * @param isCompressed
     * @param lineFontSize
     * @return sb
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnsupportedEncodingException
     */
    private String linePrice(Element item, Object obj, boolean isCompressed,
            int lineFontSize) throws NumberFormatException, IllegalArgumentException,
            SecurityException, IllegalAccessException, InvocationTargetException,
            UnsupportedEncodingException {
        double price = 0 ;
        String property = "";
        StringBuilder sb = new StringBuilder("");
        String fixedLength = item.attributeValue("fixedLength");
        String appendSign = item.attributeValue("appendSign");
        String appendWay = item.attributeValue("appendWay");
        if (null !=item.getTextTrim() && !"".equals(item.getTextTrim())){
            property = item.getTextTrim();
            price = this.calculate(property, obj);
        } else {
            property = item.attributeValue("property");
            price = Double.valueOf(this.invokeMethod(property, obj));
        }

        String unitFlg = item.attributeValue("unitFlg");

        int minus = 1;
        if (Boolean.valueOf(item.attributeValue("isMinus"))) {
            minus = -1;
        }

        String priceText = null ;
        if (unitFlg != null && Boolean.valueOf(unitFlg)) {
            priceText = super.getJASymbol(price);
        } else {
            priceText = this.getAmountByMode(price, minus);
        }
        if (!StringUtility.isNullOrEmpty(fixedLength)
                && !StringUtility.isNullOrEmpty(appendSign)) {
        	priceText = this.appendSignForString(priceText, appendSign,
                    appendWay, Integer.valueOf(fixedLength));
        }
        String priceSize = item.attributeValue("size");
        byte[] priceByte  = priceText.getBytes();
        if (priceSize != null && !"".equals(priceSize)){
            int tempFontSize = StringUtility.convNullToZero(priceSize);
            priceByte = super.cmdCopy(setCharHeight(tempFontSize),priceByte);
           
        } else {
            priceByte = super.cmdCopy(setCharHeight(lineFontSize),priceByte);
        }
        addLineCommandByteCount+=3;
        sb.append(new String(priceByte));

        if (!isCompressed) {
        	 if(StringUtility.convNullToZero(priceSize) < 17 && 
        			 StringUtility.convNullToZero(priceSize) > 10){
        		 //TODO
        		 this.propertyLength = this.propertyLength + 
        				 priceText.getBytes("MS932").length * 
        						 (StringUtility.convNullToZero(priceSize) / 16 + 1) ;
             }else{
            	 this.propertyLength += priceText.getBytes("MS932").length;
             }
        } else {
            this.propertyLength += priceText.getBytes("MS932").length * 42 / 32;
        }

        return sb.toString();
    }

    /**
     * Helper method of resolveLineItems
     *
     * @param item
     * @param isCompressed
     * @param lineFontSize
     * @return
     * @throws UnsupportedEncodingException
     */
    private String lineSign(Element item, boolean isCompressed,
            int lineFontSize) throws UnsupportedEncodingException {
//        String property = "";
        StringBuilder sb = new StringBuilder("");
        int slength = StringUtility.convNullToZero(item
                .attributeValue("length"));
        String sign = item.attributeValue("value");
        String text = this.getSign(slength, sign);

        String signSize = item.attributeValue("size");
        byte[] signByte  = text.getBytes();
        if (signSize != null && !"".equals(signSize)){
            int tempFontSize = StringUtility.convNullToZero(signSize);
            signByte = super.cmdCopy(setCharHeight(tempFontSize),signByte);
        } else {
            signByte = super.cmdCopy(setCharHeight(lineFontSize),signByte);
        }
        addLineCommandByteCount+=3;
        sb.append(new String(signByte));

        if (!isCompressed) {
            this.propertyLength += text.getBytes("MS932").length;
        } else {
            this.propertyLength += text.getBytes("MS932").length * 42 / 32;
        }

        return sb.toString();
    }

    /**
     * Helper method of resolveLineItems
     *
     * @param conElement
     * @param obj
     * @param lineFontSize
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
    private String lineCondition(Element conElement, Object obj,
            int lineFontSize, int length) throws UnsupportedEncodingException,
            IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ParseException {
        StringBuilder sb = new StringBuilder("");
        int tempLen = length;
        for (Iterator<?> it = conElement.elementIterator(); it
                .hasNext();) {
            Element conItem = (Element) it.next();
            sb.append(this.resolveLineItems(conItem, obj, tempLen, lineFontSize));
            tempLen = length - sb.toString().getBytes("MS932").length;
        }

        return sb.toString();
    }

    /**
     * Helper method of resolveLineItems
     *
     * @param item
     * @param obj
     * @param lineFontSize
     * @param isCompressed
     * @return
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    private String lineDate(Element item, Object obj, int lineFontSize,
             boolean isCompressed) throws UnsupportedEncodingException,
             IllegalArgumentException, SecurityException, IllegalAccessException,
             InvocationTargetException, ParseException {
        StringBuilder sb = new StringBuilder("");
        String dateSize = item.attributeValue("size");
        byte[] dateByte  = this.dateFormat(item, obj).getBytes();

        if (dateSize != null && !"".equals(dateSize)){
            int tempFontSize = StringUtility.convNullToZero(dateSize);
            dateByte = super.cmdCopy(setCharHeight(tempFontSize),dateByte);
        } else {
            dateByte = super.cmdCopy(setCharHeight(lineFontSize),dateByte);
        }
        addLineCommandByteCount += 3;

        sb.append(new String(dateByte));

        if (!isCompressed) {
            this.propertyLength += this.dateFormat(item, obj).getBytes("MS932").length;
        } else {
            this.propertyLength += this.dateFormat(item, obj).getBytes("MS932").length * 42 / 32;
        }

        return sb.toString();
    }

    /**
     * Helper method of resolveLineItems
     *
     * @param item
     * @param obj
     * @return
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws ParseException
     */
    private String lineUnderline(Element item, Object obj, int lineFontSize) throws UnsupportedEncodingException,
                   IllegalArgumentException, SecurityException, IllegalAccessException,
                   InvocationTargetException, NoSuchMethodException, ParseException {
        StringBuilder sb = new StringBuilder("");
        int underlineElement = item.elements().size();
        if (underlineElement >= 0) {
            sb.append(this.addUnderline(item));
            int templength = this.paperWidth * 76 / 125;

            for (Iterator<?> it = item.elementIterator(); it.hasNext();) {

                Element underItem = (Element) it.next();

                sb.append(this.resolveLineItems(underItem, obj, templength, lineFontSize));

                templength = this.paperWidth * 76 / 125;
                templength -= sb.toString().getBytes("MS932").length ;
            }
        }

        return sb.toString();
    }

    /**
     * Helper method of resolveLineItems
     * @param sb
     * @param isLeft
     * @param margin
     * @param index
     * @return
     * @throws UnsupportedEncodingException
     */
    private String lineContainsMaxMargin(StringBuilder sb, boolean isLeft, String margin,
            int index) throws UnsupportedEncodingException {

        if (!isLeft) {
            String text = "";
            if(margin.contains("max")){
                if (margin.length() > 4) {
                    text = this.getBlanks(this.paperWidth * 76 / 125 - this.propertyLength
                            - Integer.parseInt(margin.substring(4)));
                    sb.append(text);
                } else if (margin.length() == 3 && "max".equalsIgnoreCase(margin)){
                    text = this.getBlanks(this.paperWidth * 76 / 125 - this.propertyLength);
                    sb.insert(0, text);
                }
            } else {
                text = this.getBlanks(StringUtility.convNullToZero(margin));
                sb.append(text);
            }
            this.propertyLength += text.getBytes("MS932").length;
        } else {
            if (margin.contains("max")) {
                if (margin.length() > 4) {
                    String text = this.getBlanks(this.paperWidth * 76 / 125 - this.propertyLength
                            - Integer.parseInt(margin.substring(4)));
                    sb.insert(index, text);
                } else {
                    String text = this.getBlanks(this.paperWidth * 76 / 125 - this.propertyLength);
                    sb.insert(0, text);
                    this.propertyLength += text.getBytes("MS932").length;
                }
            } else {
                String text = this.getBlanks(StringUtility
                        .convNullToZero(margin));
                sb.insert(index, text);

                this.propertyLength = this.propertyLength + text.getBytes("MS932").length;
            }
        }

        return sb.toString();
    }

    /**
     *  Helper method of resolveLineItems
     *
     * @param item
     * @param margin
     * @return
     */
    private String lineMargin(Element item, String margin) {
        if ( this.isLeftMargin(item)) {
            margin = item.attributeValue("leftMargin");
        } else {
            margin = item.attributeValue("rightMargin");
        }
        if (margin == null || "".equals(margin)) {
            margin = "0";
        }

        return margin;
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
    private String resolveLineItems(Element item, Object obj, int length,int lineFontSize)
            throws UnsupportedEncodingException, IllegalArgumentException,
            SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException, ParseException {

        StringBuilder sb = new StringBuilder();

        boolean isCompressed = false;
        String margin = null;
        int index = 0;

        boolean isLeft = this.isLeftMargin(item);
        margin = lineMargin(item, margin);
        //1.01 2015.02.10 FENGSHA MOD START
        //isCompressed = Boolean.valueOf(item.attributeValue("compressed"));
        String printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            printPaperLength = (String) env.lookup("Printpaperlength");
        } catch (NamingException e) {
            printPaperLength = String.valueOf(IPrinter.FLG_PAPER80);
        }
        if(String.valueOf(IPrinter.FLG_PAPER53).equals(printPaperLength)){
            isCompressed = true;
        }else{
            isCompressed = false;
        }
      //1.01 2015.02.10 FENGSHA MOD END
        if("h".equalsIgnoreCase(this.printDirection)){
            isCompressed = true;
        }

        switch (XmlTag.valueOf(item.getName())) {

        case Item:
            sb.append(lineItem(item, obj, isCompressed,
                    lineFontSize));
            break;
        case Lable:
            sb.append(lineLabel(item, isCompressed,
                      lineFontSize));
            break;
        case Price:
            sb.append(linePrice(item, obj, isCompressed,
                      lineFontSize));
            break;
        case Sign:
            sb.append(lineSign(item, isCompressed,
                       lineFontSize));
            break;
        case Condition:
            if (this.judgment(item, obj)) {
                sb.append(this.stringElementForXMLcondition(item, obj, lineFontSize,length));
            }
            break;
        case Date:
            sb.append(lineDate(item, obj, lineFontSize,
                       isCompressed));
            break;
        case Underline:
            sb.append(lineUnderline(item, obj, lineFontSize));
            sb.append(this.cancelUnderline());
            break;
        default:
            break;
        }

        return this.setCompressed(isCompressed,
                lineContainsMaxMargin(sb, isLeft, margin, index));
    }

    protected String setCompressed(boolean flag,String text){
        if (flag) {
            this.addLineCommandByteCount+=6;
            return new String(this.cmdCopy(new byte[]{0x1B,0x16,0x01},
                    text.getBytes(),new byte[]{0x1B,0x16,0x00}));
        } else {
            return text;
        }
    }

    private double calculate(String sCalculate,Object obj) throws
    NumberFormatException, IllegalArgumentException,
    SecurityException, IllegalAccessException, InvocationTargetException{
        char[] marks = new char[]{'+','-','*','/'};
        char mark = ' ';
        int markIndex  = 0 ;
        double price = 0;
        for(int i = 0 ; i < marks.length ; i ++){
            if(sCalculate.indexOf(marks[i]) > 0){
                markIndex = sCalculate.indexOf(marks[i]);
                mark = marks[i];
                break;
            }
        }
        if(markIndex > 0){
            String method1 = sCalculate.substring(0, markIndex).trim();
            String str1 = sCalculate.substring(markIndex + 1).trim();
            double dou1 = Double.valueOf(this.invokeMethod(method1, obj));
            double dou2 = Double.valueOf(this.invokeMethod(str1, obj));
            switch(mark){
            case '+':
                price = dou1 + dou2;
                break;
            case '-':
                price = dou1 - dou2;
                break;
            case '*':
                price = dou1 * dou2;
                break;
            case '/':
                price = dou1 / dou2;
                break;
            default:
                price = 0;
            }
        }else{
            price = Double.valueOf(this.invokeMethod(sCalculate, obj));
        }
        return price;
        }
    private boolean isLeftMargin(Element item) {
        String stringLeftMargin = item.attributeValue("leftMargin");
        return (stringLeftMargin!= null) ? true : false;
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

        String result = "";
        SimpleDateFormat formatTo = new SimpleDateFormat(
                item.attributeValue("formatTo"));

        if ("true".equals(item.attributeValue("isSysDate"))) {

            result = formatTo.format(new Date());
        } else {
            String date = this.invokeMethod(item.attributeValue("property"),
                    obj);
            SimpleDateFormat formatFrom = new SimpleDateFormat(
                    item.attributeValue("formatFrom"));

            result = formatTo.format(formatFrom.parse(date));

        }

        return result;
    }

    /**
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

    /**
     * @param condition1
     * @param condition2
     * @return
     */
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
    private boolean lessThan(String condition1, String condition2) {

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
    private boolean geaterThanEqual(String condition1, String condition2) {

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
    private boolean lessThanEqual(String condition1, String condition2) {

        boolean result = false;

        if (this.compareTo(condition1, condition2) <= BOTH_OBJECT_NULL) {
            result = true;
        }

        return result;
    }

    /**
     * @param con
     * @param condition1
     * @param condition2
     * @return
     */
    private boolean evaluateCondition(Element con, String condition1,
                                      String condition2) {
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

            res = lessThan(condition1, condition2);
            break;
        case ge:

            res = geaterThanEqual(condition1, condition2);
            break;
        case le:

            res = lessThanEqual(condition1, condition2);
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
            LOGGER.logAlert(PROG_NAME, PROG_NAME+".invokeMethod",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get method to invoke. " + e.getMessage());
        }

        if (!StringUtility.isNullOrEmpty(res)) {
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
    private int compareTo(Object obj1, Object obj2) {
        int result = BOTH_OBJECT_NULL;
        if (StringUtility.isNullOrEmpty(obj1)
                && StringUtility.isNullOrEmpty(obj2)) {
            result = BOTH_OBJECT_NULL;
        } else if (StringUtility.isNullOrEmpty(obj1)
                || StringUtility.isNullOrEmpty(obj2)) {
            result = EITHER_OBJECT_NULL;
        } else {
            if (obj1.getClass().isInstance(obj2)) {
                if (obj1.toString().matches("[0]*[.]?[0]*")
                        && obj2.toString().matches("[0]*[.]?[0]*")) {
                    result = BOTH_OBJECT_NULL;
                }else{
                    result = obj1.toString().toLowerCase().compareTo(obj2.toString().toLowerCase());
                }
            } else {
                result =  EITHER_OBJECT_NULL;
            }
        }
        return result;
    }

    public String getPrintDirection() {
        return printDirection;
    }

    public void setPrintDirection(String printDirection) {
        this.printDirection = printDirection;
    }


    private String stringElementForXMLcondition(Element item, Object obj,
            int lineFontSize, int length) throws UnsupportedEncodingException,
            IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ParseException {
        Element conElement = (Element) item.elements().get(
                item.elements().size() - 1);
        return lineCondition(conElement, obj, lineFontSize, length);
    }

    private void resolveLineItems(Element line)
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, UnsupportedEncodingException, ParseException {
        List<ItemMode> itemList = this.receipt.getItemList();
        int iAllMmQuantity = 0;
        for (ItemMode itemMode : itemList) {
            if (!StringUtility.isNullOrEmpty(itemMode.getMmID())) {
                iAllMmQuantity += itemMode.getMmQuantity();
            }
            if (itemMode.getMmSequence() != 0
                    && itemMode.getMmSequence() == itemMode.getMmSize()) {
                itemMode.setAllMmQuantity(String.valueOf(iAllMmQuantity));
                iAllMmQuantity = 0;
            } else {
                itemMode.setAllMmQuantity("");
            }
            for (Iterator<?> lineItem = line.elementIterator(); lineItem
                    .hasNext();) {

                Element item = (Element) lineItem.next();
                this.resolveLineElement(item, itemMode);
            }
        }
    }
    private boolean docTaxIsFirst(Element root){
        Element element = (Element) root.elements().get(0);
        String firstName = element.getName();
        if("LineImageBmp".equalsIgnoreCase(firstName)){
            this.pageModeFlag = false;
        }else{
            this.pageModeFlag = true;
        }
        return this.pageModeFlag;
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
}
