package ncr.res.mobilepos.helper;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.res.mobilepos.report.model.ReportMode;
import ncr.res.mobilepos.simpleprinterdriver.IPrinter;
import ncr.res.mobilepos.networkreceipt.model.EmuConst;
import ncr.res.mobilepos.networkreceipt.model.EmuConst.BarcodeType;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;
/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01            2014.10.21    MAJINHUI        レポート出力を対応
 * 1.02            2015.02.10    FENGSHA         領収書レシートの80mm紙変更対応
 */

public class Formatter {

    /** class instance of PaperReceiptContent. */
    protected ReceiptMode receipt;

    /* 1.01 2014.10.21　  レポート出力を対応 　ADD START */
    /** class instance of printSalesReport. */
    protected ReportMode report;
    /* 1.01 2014.10.21　  レポート出力を対応　 　ADD END */

    /** maximum line number of small receipt. */
    protected int lineMaxLimit = 40;

    /** resource bundle. */
    protected ResourceBundle rb;

    /** receipt commands list. */
    protected List<byte[]> receiptCmd = new ArrayList<byte[]>();

    /** signature column. */
    protected static final byte[] SIGN_COLUMN = {32, 95, 95, 95, 95, 95, 95,
            95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95,
            95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 32, 10,
            124, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 124, 10, 124, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
            32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 124, 10,
            124, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95,
            95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95, 95,
            95, 95, 95, 95, 95, 95, 124, 10};
    
    private int receiptPaperWidth = IPrinter.FLG_PAPER80 ;
    /**
     * return specified locale.
     * @param language ja, en or other supported language code.
     */
    public static Locale getLanguage(String language) {
        Locale locale = null;
        if (language == null || !"en".equals(language)) {
            locale = Locale.JAPANESE;
        } else {
            locale = Locale.ENGLISH;
        }
        
        return locale;
    }

    protected Formatter() {
    	String width = null;
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            width = (String) env.lookup("Printpaperlength");
        } catch (NamingException e) {
        	width = String.valueOf(IPrinter.FLG_PAPER80);
        }
        this.setReceiptPaperWidth(StringUtility.convNullToZero(width));
    }

    /**
     * get receipt command list
     *
     * @return receipt commands list
     */
    public List<byte[]> getReceipt() {

        return this.receiptCmd;
    }
    /* 1.01 2014.10.21　 レポート出力を対応 　ADD START */
    /**
     * get receipt command list
     *
     * @return receipt commands list
     */
    public List<byte[]> getReport() {
        return this.receiptCmd;
    }

    /* 1.01 2014.10.21　  レポート出力を対応 　ADD END */
    protected void addLineToReceipt(int high,  int feeNum , String text){
        byte[] line = this.cmdCopy(this.getLeftMargin(),this.setCharHeight(high),
                text.getBytes(),new byte[]{0x0A});
        this.receiptCmd.add(line);
        if (feeNum > 0) {
            byte[] feedNo = this.setFeedLines(feeNum);
            this.receiptCmd.add(feedNo);
        }
    }
    /**
     * add one line to receipt command list.
     *
     * @param high
     * @param align 0:left; 1:center; 2:right
     * @param feeNum feed n print lines
     */
    protected void addLineToReceipt(int high, int align, int feeNum, String text) {
    	
        byte[] line = this.cmdCopy(this.getLeftMargin(),this.setCharHeight(high),
                this.getAlign(align), text.getBytes(), new byte[] {0x0A});

        this.receiptCmd.add(line);

        // feed feeNum lines
        if (feeNum > 0) {
            byte[] feedNo = this.setFeedLines(feeNum);
            this.receiptCmd.add(feedNo);
        }
    }
    
    protected void addLineToReceipt(byte[] lines) {
        this.receiptCmd.add(lines);
    }
    /**
     * pageMode設定。
     */
    //1.02 2015.02.10 FENGSHA MOD START
    //  protected void convertPageMode(int width){
           //int paperWidth = width * 53 / 42 ;
     protected void convertPageMode(int width,boolean isCompressed){
        int paperWidth = 0;
//        if(isCompressed == true){
//            paperWidth = width * 53 / 42 ;
//        }else{
//            paperWidth = width * 53 / 44 ;
//        }
      //1.02 2015.02.10 FENGSHA MOD END
        byte[] pagemode = new byte[]{0x1B, 0x4C};
        byte[] direction = new byte[]{0x1B, 0x54, 0x03};
        byte[] pageArea;
        int n1 = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0, n6 = 0, n7 = 0, n8 = 0;
        
        n5 = (int) (80 * 203 / 25.4) % 256;
        n6 = (int) (80 * 203 / 25.4) / 256;
        n7 = (int) (width * 203 / 25.4) % 256;
        n8 = (int) (width * 203 / 25.4) / 256;

        pageArea = new byte[]{0x1B, 0x57,(byte) n1,(byte) n2,(byte) n3,
                (byte) n4,(byte) n5,(byte) n6,(byte) n7,(byte) n8};
        this.receiptCmd.add(pagemode);
        this.receiptCmd.add(direction);
        this.receiptCmd.add(pageArea);
    }
    /**
     * compressed
     */
    protected String setCompressed(boolean flag,String text){
        if(flag){
            return new String(this.cmdCopy(new byte[]{0x1B,0x16,0x01},
                    text.getBytes(),new byte[]{0x1B,0x16,0x00}));
        }else{
            return text;
        }
    }
    /**
     * standardMode設定。
     */
    protected void convertStandardMode(){
        this.receiptCmd.add(new byte[]{0x0A});
        this.receiptCmd.add(new byte[]{0x0C});
    }
    // 2014/07/09 bmpの添加を対応する。START
    /**
     * BMPのバイトを設定する。
     * @param name
     */
    protected void addBmp(String name) {
        byte[] feedNo = this.setBmp(name);
        this.receiptCmd.add(feedNo);
    }

    /**
     * BMPのバイトを取得する。
     * @param name
     * @return
     */
    protected byte[] setBmp(String name) {
        byte[] bmpByte = null;
        if ("logo".equals(name)) {
            bmpByte = new byte[] { 108, 111, 103, 111 };
        } else if ("DocTax".equals(name)){
            bmpByte = new byte[] { 111, 116, 104, 101 };
        }
        //1.02 2015.02.10 FENGSHA ADD START
        else if("Misc".equals(name)){
            bmpByte = new byte[] { 83,105,103,110 };
        }
      //1.02 2015.02.10 FENGSHA ADD END
        return bmpByte;
    }
    // 2014/07/09 bmpの添加を対応する。END

    /**
     * format line.
     *
     * @param texts
     * @param colum
     * @return line
     *
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    protected String formatLine(int start, int colum, String... texts)
            throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        sb.append(this.getBlanks(start));
        int length = this.lineMaxLimit - start;
        int size = 0;
        int index = 0;

        for (String text : texts) {
            if (colum <= 0) {
                sb.append(text);
            } else {
                if (size != 0) {
                    if (index + 1 == texts.length) {
                        if (size + text.getBytes("MS932").length <= length) {
                            sb.append(this.getBlanks(length - size
                                    - text.getBytes("MS932").length));
                        }
                        sb.append(text);
                        break;
                    } else {
                        if (size + this.getBlanks(colum).length()
                                + text.getBytes("MS932").length < length) {
                            sb.append(this.getBlanks(colum));
                            size += this.getBlanks(colum).length();
                        }
                    }
                }
                sb.append(text);
                size += text.getBytes("MS932").length;
            }

            index++;
        }

        return sb.toString();
    }

    /**
     * returns a string with the specified number of spaces.
     *
     * @param length the number of spaces
     * @return String with spaces
     */
    protected String getBlanks(int length) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(" ");
        }

        return sb.toString();
    }

    /**
     * get the formatted price value with comma.
     *
     * @param money
     * @param minusMode
     * @param flg true:add '\'
     * @return e.g. -1000 -> - [\] 1,000
     */
    protected String getAmountByMode(double money, int minusMode) {

        return this.getNumberWithComma(money * minusMode);
    }

    /**
     * Add remarks column
     *
     * @param align 0:left, 1:right
     * @param text
     *
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    protected void addRemarksColumn(int align, String text)
            throws UnsupportedEncodingException {

        String tmp = this.getBlanks(this.lineMaxLimit
                - text.getBytes("MS932").length);

        tmp = align == 1 ? tmp + text : text + tmp;

        this.receiptCmd.add(new byte[] {0x1c, 0x2d, 2});
        this.addLineToReceipt(0, 0, 1, tmp);
        this.receiptCmd.add(new byte[] {0x1c, 0x2d, 0});
    }

    /**
     * feed num print lines
     *
     * @param num lines
     * @return command
     */
    protected byte[] setFeedLines(int num) {

        byte defNum = 0;

        if (num > 0 && num < 127) {
            defNum = Integer.valueOf(num - 1).byteValue();
        }

        return new byte[] {0x14, defNum, 0x0A};
    }

    /**
     * get barcode by position & pitch & height & type
     *
     * @param align 0:left align, 1:center align, 2:right align, default:999
     * @param width 1:1, 2:2, 3:3, 4:5, 5:5, default:999
     * @param height 1～255 default:999
     * @param pitch 0:15.2 CPI, 1:19 CPI, default:999
     * @param position 0:not printed, 1:above, 2:below, 3:both, default:999
     * @param type 0:UPC-A, 1:UPC-E, 2:JAN13 (EAN13), 3:JAN8 (EAN8), 4:Code 39,
     *            5:Interleaved 2 of 5(ITF), 6:CODABAR, 10:PDF 417
     * @param barcode
     *
     */
    protected byte[] getBarCode(int align, int width, int height, int pitch,
            int position, BarcodeType type, String barcode) {

        if (StringUtility.isNullOrEmpty(barcode)) {
            return new byte[0];
        }

        byte[] res = this.cmdCopy(this.getAlign(align), this.getWidth(width),
                this.getHeight(height), this.getPitch(pitch),
                this.getPosition(position), this.printBarcode(type, barcode));

        return res;
    }

    /**
     * get barcode by position & pitch & height & type
     *
     * @param align 0:left align, 1:center align, 2:right align, default:999
     * @param width 1:1, 2:2, 3:3, 4:5, 5:5, default:999
     * @param height 1～255 default:999
     * @param pitch 0:15.2 CPI, 1:19 CPI, default:999
     * @param position 0:not printed, 1:above, 2:below, 3:both, default:999
     * @param type 0:UPC-A, 1:UPC-E, 2:JAN13 (EAN13), 3:JAN8 (EAN8), 4:Code 39,
     *            5:Interleaved 2 of 5(ITF), 6:CODABAR, 10:PDF 417
     * @param barcode
     *
     */
    protected byte[] getBarCode(int align, int width, int height, int pitch,
            int position, int type, String barcode) {

        if (barcode == null) {
            return new byte[0];
        }

        byte[] res = new byte[] {};

        for (BarcodeType code : BarcodeType.values()) {
            if (code.getValue().intValue() == type) {
                res = this.cmdCopy(this.getAlign(align), this.getWidth(width),
                        this.getHeight(height), this.getPitch(pitch),
                        this.getPosition(position),
                        this.printBarcode(code, barcode));
                break;
            }
        }

        return res;
    }

    /**
     * commands copy
     *
     * @param cmds
     * @return command
     */
    protected byte[] cmdCopy(byte[]... cmds) {

        int count = 0;
        int index = 0;
        byte[] cmd = null;
        boolean flg = true;

        for (byte[] bs : cmds) {
            if (bs.length != 0) {
                if (flg) {
                    for (byte[] bss : cmds) {
                        count += bss.length;
                    }
                    flg = false;
                    cmd = new byte[count];
                }

                System.arraycopy(bs, 0, cmd, index, bs.length);

                index += bs.length;
            }
        }

        return cmd;
    }

    /**
     * get align
     */
    protected byte[] getAlign(int align) {

        byte defAlign = 0;

        if (!(align < 0 || align > 2)) {
            defAlign = new Integer(align).byteValue();
        }

        byte[] cmd = {0x1b, 0x61, defAlign};

        return cmd;
    }
    /**
     * set left margin
     */
    protected byte[] getLeftMargin(){
    	if(this.getReceiptPaperWidth() == IPrinter.FLG_PAPER80){
    		return new byte[]{0x1d, 0x4c, (byte)20, (byte)0};
    	}else{
    		return new byte[]{0x1d, 0x4c, (byte)16, (byte)0};
    	}
    }
    /**
     * get width
     */
    private byte[] getWidth(int width) {

        byte defWidth = 3;

        if (!(width < 1 || width > 5)) {
            defWidth = new Integer(width).byteValue();
        }

        byte[] cmd = {0x1d, 0x77, defWidth};

        return cmd;
    }

    /**
     * get height
     */
    private byte[] getHeight(int height) {

        byte defHeight = new Integer(162).byteValue();

        if (!(height < 1 || height > 255)) {
            defHeight = new Integer(height).byteValue();
        }

        byte[] cmd = {0x1d, 0x68, defHeight};

        return cmd;
    }

    /**
     * get pitch
     */
    private byte[] getPitch(int pitch) {

        byte defPitch = 0;

        if (!(0 > pitch || pitch > 1)) {
            defPitch = new Integer(pitch).byteValue();
        }

        byte[] cmd = {0x1d, 0x66, defPitch};

        return cmd;
    }

    /**
     * get position
     */
    private byte[] getPosition(int position) {

        byte defPosition = 0;

        if (!(0 > position || position > 3)) {
            defPosition = new Integer(position).byteValue();
        }

        byte[] cmd = {0x1d, 0x48, defPosition};

        return cmd;
    }

    /**
     * print bar code
     *
     * @param barcode
     * @return result
     */
    private byte[] printBarcode(BarcodeType type, String barcode) {

        if (!typeCheck(type, barcode)) {
            return new byte[] {0x00};
        }
        //start modify by mlwang 2014/09/11
        byte[] cmd = null;
        byte[] res = null;
        //code128c's byte
        byte[] code128c;
        //NW-7 or ITF
        if(type.getValue() == EmuConst.BarcodeType.CODEBAR.getValue()
                || type.getValue() == EmuConst.BarcodeType.INTERLEAVED2OF5.
                getValue()){
            cmd =new byte []{0x1d, 0x6b, type.getValue().byteValue(), 0x00};
            res = new byte[cmd.length + barcode.getBytes().length];
            System.arraycopy(cmd, 0, res, 0, cmd.length);
            System.arraycopy(barcode.getBytes(), 0, res, cmd.length - 1,
                    barcode.getBytes().length);
        }else if(type.getValue() == EmuConst.BarcodeType.CODE128.getValue()){
            //code128c
            barcode = this.doChangeCode128c(barcode);
            code128c = new byte[barcode.length() / 2];
            for(int i = 0 ;i < code128c.length ; i ++){
                code128c[i] = Byte.valueOf(barcode.substring(i * 2, (i + 1)* 2));
            }
            cmd =new byte []{0x1d, 0x6b, 73, 15, 105};
            res = new byte[cmd.length + code128c.length];
            System.arraycopy(cmd, 0, res, 0, cmd.length);
            System.arraycopy(code128c, 0, res, cmd.length,
                    code128c.length);
        }
      //end modify 2014/09/11
        return res;
    }

    /**
     * check bar code type
     *
     * @return
     */
    private boolean typeCheck(BarcodeType type, String barcode) {
        boolean result;
        switch (type) {
            case UPC_A:

                result = false;
                break;

            case UPC_E:

                result = false;
                break;

            case JAN13:

                result = false;
                break;

            case JAN8:

                result = false;
                break;

            case CODE39:

                result = false;
                break;
            //Interleaved Two of Five
            case INTERLEAVED2OF5:
                result = true;
                break;
            //NW-7
            case CODEBAR:
                result = checkCdBar(barcode);
                break;
            //start modify by mlwang 2014/09/11
            //CODE128
            case CODE128:
                result = this.checkCode128c(barcode);
                break;
            //end modify 2014/09/11
            case PDF417:

                result = false;
                break;

            default:
                result = false;
        }

        return result;
    }

    /**
     * @param barcode
     * @return
     */
    private String doChangeCode128c(String barcode){
        char[] numChar = barcode.toCharArray();
        int allSum = 0;
        for(int i = 1 ;i <= numChar.length; i ++){
            if(i % 2 == 1){
                allSum += (3 * Integer.parseInt(numChar[i - 1] + ""));
            }else{
                allSum += (1 * Integer.parseInt(numChar[i - 1] + ""));
            }
        }
        barcode = barcode + (10 - allSum % 10 == 10 ? 0 : 10 - allSum % 10);
        return barcode;
    }
    /**
     * check code128
     *  add by mlwang 2014/09/15
     * @return
     */
    private boolean checkCode128c(String barcode){
        if (barcode == null || "".equals(barcode)) {
            return false;
        }
        String regex = "^[0-9]*$";
        if(!barcode.matches(regex)){
            return false;
        }
        if(barcode.length() % 2 == 0){
            return false;
        }
        return true;
    }
    /**
     * check barcode
     *
     * @return
     */
    private boolean checkCdBar(String barcode) {

        if (barcode == null || "".equals(barcode)) {
            return false;
        }

        String body = "0123456789$+-./:";

        for (int i = 0; i < barcode.length(); i++) {
            if ((i != 0) && (i != barcode.length() - 1)
                    && (!body.contains(String.valueOf(barcode.charAt(i))))) {
                    return false;

            }
        }
        return true;
    }

    /**
     * select character height
     *
     * @param height 0-7 times height, default:0
     * @return command
     */
    protected byte[] setCharHeight(int height) {

        // normal
        byte defHeight = 0;

        if (!(height < 0 || height > 7)) {
            defHeight = Integer.valueOf(height).byteValue();
        }

        if (height < 17 && height > 10) {
            defHeight = Integer.valueOf(height).byteValue();
        }

        return new byte[] {0x1d, 0x21, defHeight};
    }

    /**
     * get the formatted price value with comma.
     *
     * @param amt - amount to format
     * @return e.g. 1000->1,000
     */
    protected String getNumberWithComma(double amt) {

        DecimalFormat df = new DecimalFormat();
        return df.format(amt);
    }

    /**
     * Get the formatted price value in yen currency.
     *
     * @param amt to format in yen currency
     *
     * @return string of cultures currency symbol e.g. \(￥ 1,000)
     */
    protected String getCurrencySymbol(double amt) {

        NumberFormat nf;
        if ("en".equals(this.rb.getLocale().getLanguage())) {
            nf = NumberFormat.getCurrencyInstance(Locale.US);
        } else {
            nf = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        }
        DecimalFormat df = (DecimalFormat) nf;
        return df.format(amt);
    }
    public String getJASymbol(double amt){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.JAPAN);
        DecimalFormat df = (DecimalFormat) nf;
        return df.format(amt);
    }
    /**
     * format reference number xxxxxxxxxxxxxxxxxxxxxxxx ->
     * xxxxxx-xxxxxxxx-xxxxx-xxxxx
     */
    protected String[] formatReferenceNo(String no) {

        StringBuilder sb = new StringBuilder();

        sb.append(this.getBlanks(24 - no.length()));
        sb.append(no);

        return new String[] {sb.toString().replace(" ", "0").substring(0, 6),
                sb.toString().replace(" ", "0").substring(6, 14),
                sb.toString().replace(" ", "0").substring(14, 19),
                sb.toString().replace(" ", "0").substring(19)};
    }

    /**
     * Add signature column
     */
    protected void addSignColumn() {

        this.receiptCmd.add(SIGN_COLUMN);
    }

    /**
     * get asterisk
     */
    protected String getAsterisk(int amount, int space, int flg) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            if (flg == 1) {
                sb.append("＊");
            } else {
                sb.append("*");
            }
            if (i < amount - 1) {
                sb.append(this.getBlanks(space));
            }
        }
        return sb.toString();
    }

    /**
     * get relative position
     *
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    protected int getRelativePosition(String... texts)
            throws UnsupportedEncodingException {

        int limit = 0;
        int length = 0;
        for (String text : texts) {
            length = text.getBytes("MS932").length;
            limit = limit < length ? length : limit;
        }

        return (this.lineMaxLimit - limit) / 2;
    }

    /**
     * returns a string with the specified number of sign. TODO
     *
     * @param length the number of sign
     * @return String with sign
     */
    protected String getSign(int length, String sign) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(sign);
        }

        return sb.toString();
    }

	public int getReceiptPaperWidth() {
		return receiptPaperWidth;
	}

	public void setReceiptPaperWidth(int receiptPaperWidth) {
		this.receiptPaperWidth = receiptPaperWidth;
	}
    
}
