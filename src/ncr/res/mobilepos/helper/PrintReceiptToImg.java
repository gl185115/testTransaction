package ncr.res.mobilepos.helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.lowagie.text.pdf.BarcodeCodabar;

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.networkreceipt.model.ReceiptLine;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;

public class PrintReceiptToImg {
	private final static int POSITION_EIGHT = 8;
	private final static int POSITION_FOUR = 4;
	private final static int POSITION_ONE = 1;
	private final static int POSITION_TWO = 2;
	
    public final static String FILE_PATH = "d:" + File.separator + "ncr"
            + File.separator + "res" + File.separator + "dbg" + File.separator
            + "receipt";
    public final static String LOGO_PATH = "d:" + File.separator + "ncr"
            + File.separator + "res" + File.separator + "para" + File.separator
            + "images";
    
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "PrintReceiptToImg";
    

    /** Default Constructor. */
    private PrintReceiptToImg() { 
    	
    }
    
    /**
     * Helper method of graphics2D
     * @param current
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static int imageYAxis(int current)
    		throws FileNotFoundException, IOException {
    	int cur = current;
        File fileCheck = new File(LOGO_PATH + File.separator + "ncrlogo.bmp");
        if (fileCheck.exists()) {
            BufferedImage logo = ImageIO.read(new FileInputStream(fileCheck));
            if (logo != null) {
            	cur = current + 60 + 12;        
            }
        }
        
        return cur;
    }
    
    /**
     * Helper method of graphics2D that will draw the logo.
     * 
     * @param g2d
     * @param current
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static Graphics2D drawLogo(Graphics2D g2d, int current)
    		throws FileNotFoundException, IOException {
        File fileCheck = new File(LOGO_PATH + File.separator + "ncrlogo.bmp");
        if (fileCheck.exists()) {
            BufferedImage logo = ImageIO.read(new FileInputStream(fileCheck));
            g2d.setColor(Color.WHITE);
            if (logo != null) {
                g2d.drawImage(logo, 35, current, 205, 60, null);               
            }
        }
        
        return g2d;
    }
    
    /**
     * Helper method of graphics2D that will draw the CodeBar.
     * 
     * @param g2d
     * @param codebarPosition
     * @param codeBarImage
     * @param codebarStr
     * @param current
     * @param font
     * @return
     */
    private static Graphics2D drawCodeBar(Graphics2D g2d, int codebarPosition,
    		java.awt.Image codeBarImage, String codebarStr, int current, Font font) {
        AttributedString ats = new AttributedString(codebarStr);
        ats.addAttribute(TextAttribute.FONT, font, 0,
                codebarStr.length());
        AttributedCharacterIterator iter = ats.getIterator();
        g2d.setColor(Color.white);
        // codeBarのpositionが０の場合、codeBarのストリングを出力しない。
        if (codebarPosition == 0) {
            g2d.drawImage(codeBarImage, 15, current, 235, 45,
                    Color.white, null);
        }else if (codebarPosition == 1) {
        	// codeBarのpositionが１の場合、codeBarの下の上にcodeBarのストリングを出力する。
            g2d.drawString(iter, 15 + 80 - codebarStr.length(), current);
            g2d.drawImage(codeBarImage, 15, current, 235, 45,
                    Color.white, null);
        }else if (codebarPosition == 2) {
        	// codeBarのpositionが2の場合、codeBarの下の方にcodeBarのストリングを出力する。
            g2d.drawImage(codeBarImage, 15, current, 235, 45,
                    Color.white, null);
            g2d.drawString(iter, 15 + 80 - codebarStr.length(),
                    current + 45 + 10);
        }else if (codebarPosition == 3) {
        	// codeBarのpositionが3の場合、codeBarの上と下にcodeBarのストリングを出力する。
            g2d.drawString(iter, 15 + 80 - codebarStr.length(), current);
            g2d.drawImage(codeBarImage, 15, current, 235, 45,
                    Color.white, null);
            g2d.drawString(iter, 15 + 80 - codebarStr.length(),
                    current + 45 + 10);
        } 
        
        return g2d;
    }
    
    /**
     * Helper method of graphics2D that will determine the CodeBar height.
     * 
     * @param codebarPosition
     * @param height
     * @return
     */
    private static int heightCodeBar(int codebarPosition, int height) {

    	int nheight = height;
        // codeBarのpositionが０の場合、codeBarのストリングを出力しない。
        if (codebarPosition == 0) {
        	nheight = 45;
        }else if (codebarPosition == 1) {
        	// codeBarのpositionが１の場合、codeBarの下の上にcodeBarのストリングを出力する。        
        	nheight = 10 + 45;
        } else if (codebarPosition == 2) {
        	// codeBarのpositionが2の場合、codeBarの下の方にcodeBarのストリングを出力する。       
        	nheight = 45 + 20;
        }else if (codebarPosition == 3) {
        	// codeBarのpositionが3の場合、codeBarの上と下にcodeBarのストリングを出力する。        
        	nheight = 10 + 45 + 10;
        }  
        
        return nheight;
    }
    
    /**
     * Helper method of positionDrawLogo
     * 
     * @param i
     * @param position
     * @param k1
     * @param count
     * @return
     */
    private static boolean isPositionEight(int i, int position, int k1, int count) {
    	return  (position & 0x08) == POSITION_EIGHT && i == 1 && count == k1;
    }
    
    /**
     * Helper method of positionDrawLogo
     * 
     * @param i
     * @param position
     * @param k1
     * @param count
     * @return
     */
    private static boolean isPositionFour(int i, int position, int k1, int count) {
    	return (position & 0x04) == POSITION_FOUR && i == 3 && count == k1;
    }
    
    /**
     * Helper method of positionDrawLogo
     * 
     * @param i
     * @param position
     * @param k2
     * @param count
     * @return
     */
    private static boolean isPositionOne(int i, int position, int k2, int count) {
    	return (position & 0x01) == POSITION_ONE && i == 0 && count == k2;
    }
    
    /**
     * Helper method of positionDrawLogo
     * 
     * @param i
     * @param position
     * @param k3
     * @param count
     * @return
     */
    private static boolean isPositionTwo(int i, int position, int k3, int count) {
    	return (position & 0x02) == POSITION_TWO && i == 1 && count == k3;
    }
    
    /**
     * Helper method of graphics2D that will determine position of the logo.
     * 
     * @param i
     * @param position
     * @param k1
     * @param k2
     * @param k3
     * @param count
     * @return result
     */
    private static int positionDrawLogo(int i, int position, int k1, int k2, int k3, int count) {

    	int result = 0;
   	
        if (isPositionEight(i, position, k1, count)) {
        	result = POSITION_EIGHT;
        } else if (isPositionFour(i, position, k1, count)) {
        	result = POSITION_FOUR;
        } else if (isPositionOne(i, position, k2, count)) {
        	result = POSITION_ONE;
        } else if (isPositionTwo(i, position, k3, count)) {
        	result = POSITION_TWO;
        }
        
        return result;
    }

    /**
     * Helper method of printImage that draw the image.
     * 
     * @param g2d
     * @param receiptLines
     * @param position
     * @param k1
     * @param k2
     * @param k3
     * @param count
     * @return g2d
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static Graphics2D graphics2D(Graphics2D g2d, List<ReceiptLine> receiptLines,
    		               int position, int k1, int k2, int k3, int count)
    		            		   throws FileNotFoundException, IOException {
        int current = 30;
        g2d.setColor(Color.WHITE);
        int result = 0;
        
        int height = 0;
        for (int i = 0; i < receiptLines.size(); i++) {
        	result = positionDrawLogo(i, position, k1, k2, k3, count);
            if (result == POSITION_EIGHT) {
            	g2d = drawLogo(g2d, current);
            	current = imageYAxis(current);
            } else if (result == POSITION_FOUR) {
            	g2d = drawLogo(g2d, current);
            	current = imageYAxis(current);
            } else if (result == POSITION_ONE) {
            	g2d = drawLogo(g2d, current);
            	current = imageYAxis(current);
            } else if (result == POSITION_TWO) {
            	g2d = drawLogo(g2d, current);
            	current = imageYAxis(current);
            }
            
            String strCompare = receiptLines.get(i).getLinedata();
            Font font = receiptLines.get(i).getFont();
            if(strCompare.startsWith("_H2_")){
                strCompare = strCompare.substring(4);
                g2d.setFont(font);
                g2d.scale(1, 2);
                g2d.drawString(strCompare, 15,current/2 + 6);
                g2d.scale(1, 0.5);
                height = 36 - 6;
              } else if (strCompare.startsWith("codeBar")) {
                // codeBarのデータのストリング
                String codebarStr = strCompare.substring(
                        strCompare.lastIndexOf("_") + 1, strCompare.length())
                        .trim();
                // codeBarの属性のストリング
                String codebarProperty = strCompare.substring(7,
                        strCompare.lastIndexOf("_"));
                // align
                codebarProperty = codebarProperty.substring(codebarProperty
                        .indexOf("_") + 1);
                // width
                codebarProperty = codebarProperty.substring(codebarProperty
                        .indexOf("_") + 1);
                // height
                codebarProperty = codebarProperty.substring(codebarProperty
                        .indexOf("_") + 1);
                // pitch
                codebarProperty = codebarProperty.substring(codebarProperty
                        .indexOf("_") + 1);
                // position
                int codebarPosition = Integer.parseInt(codebarProperty);
                BarcodeCodabar codebar = new BarcodeCodabar();
                codebar.setCode(codebarStr);
                // codeBarImageを作る
                java.awt.Image codeBarImage = codebar.createAwtImage(
                        Color.black, Color.white);
                g2d = drawCodeBar(g2d, codebarPosition,
                		       codeBarImage, codebarStr, current, font);
                height = heightCodeBar(codebarPosition, height);

            } else {
                g2d.setFont(font);
                g2d.drawString(receiptLines.get(i).getLinedata(), 15, current);
                height = 12;
            }
            current = current + height;
        }
        
        return g2d;
    }
    
    public static int printImage(List<List<byte[]>> receiptsList,
            ReceiptMode receiptMode, String deviceNo, int position) throws Exception {
        // TODO For printImage
        int k1 = 0, k2 = 0, k3 = 0;
        if ((position & 0x08) > 0) {
            k1 = 1;
        }
        if ((position & 0x04) > 0) {
            k1 = 1;
        }
        if ((position & 0x01) > 0) {
            k2 = k1 + 1;
        }
        if ((position & 0x02) > 0) {
            k3 = ((k2 == 0) ? k1 : k2) + 1;
        }
        // （PrintToTextFile.printToText）関数で全部のreceiptLinesを取る
        receiptsList = PrintReceiptToImg.rebuildListReceiptsList(receiptsList);
        Iterator<List<byte[]>> listIter = receiptsList.iterator();
        int count =0;
        while(listIter.hasNext()) {
	        List<ReceiptLine> receiptLines = PrintReceiptToImg.printToText(
	                listIter.next(), receiptMode, deviceNo);
	        count += 1;
	        // 画面ファイルの名前：storeId + _deviceNo + _sequenceNo + _bussinessdate
	        // 例えば：0001_0001_0015_20131213
	        StringBuilder fileImageNameBuffer = new StringBuilder();
	        // 日付（bussinessdate）フォーマット
	        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	        java.util.Date date = format.parse(receiptMode.getBusinessDayDate());
	        // storeId
	        fileImageNameBuffer.append(receiptMode.getStoreID());
	        fileImageNameBuffer.append("_");
	        // deviceNo
	        fileImageNameBuffer.append(deviceNo);
	        fileImageNameBuffer.append("_");
	        // sequenceNo
	        fileImageNameBuffer.append(receiptMode.getSequenceNo());
	        fileImageNameBuffer.append("_");
	        // bussinessdate(YYYYMMDD)
	        fileImageNameBuffer.append(format.format(date));
	        // receiptModeのtype
	        fileImageNameBuffer.append("_");
	        fileImageNameBuffer.append(receiptMode.getReceiptType());
	        fileImageNameBuffer.append("_" + count);
	        BufferedImage readImage = new BufferedImage(270, receiptLines.size() * 16,
	                BufferedImage.TYPE_INT_BGR);
	        Graphics2D g2d = readImage.createGraphics();
	        g2d = graphics2D(g2d, receiptLines, position, k1, k2, k3, count);
	        try {
	            ImageIO.write(readImage, "jpeg", new File(FILE_PATH + File.separator
	                    + fileImageNameBuffer.toString() + ".jpeg"));
	        } catch (Exception e) {
	        	LOGGER.logAlert(PROG_NAME, PROG_NAME+".printImage", Logger.RES_EXCEP_GENERAL, "Failed to print image. "+e.getMessage());
	        }
        }
        return ResultBase.RESNETRECPT_OK;
    }

    public static List<ReceiptLine> printToText(
            List<byte[]> receiptsList, ReceiptMode receiptMode,
            String deviceNo) throws Exception {
        List<ReceiptLine> receiptLineList = new ArrayList<ReceiptLine>();
        File file = new File(FILE_PATH);
        // 出力ファイルを作る
        if (!file.exists()) {
            file.mkdirs();
        }
            Iterator<byte[]> iteratorByte = receiptsList.iterator();
            while (iteratorByte.hasNext()) {
                byte[] byteStr = iteratorByte.next();
                if (byteStr == null) {
                    continue;
                }
                if (byteStr.length <= 7) {
                    ReceiptLine line = new ReceiptLine();
                    line.setFont(new Font("ＭＳ ゴシック", 0, 12));
                    line.setLinedata("");
                    receiptLineList.add(line);
                } else {
                    String outStr = "";
                    byte[] temp = null;
                    // codeBar のデータを解析する
                    if (byteStr.length > 17 && byteStr[15] == 29
                            && byteStr[16] == 107) {
                        outStr = "codeBar";
                        temp = new byte[byteStr.length - 18];
                        // codeBarのストリング："codeBar" +_align + _width + _height +
                        // _pitch + _position +_"codeのデータ"
                        // 例えば：codeBar1_2_65_0_2_A00154614546A
                        // codeBarの属性：align_width_height_pitch_position_
                        for (int i = 0; i < 5; i++) {
                            temp[i] = byteStr[i * 3 + 2];
                            outStr = outStr + temp[i] + "_";
                        }
                        // codeBarのデータ
                        for (int i = 0; i < temp.length; i++) {
                            temp[i] = byteStr[i + 18];
                        }
                    } else if (byteStr[0] == 28 && byteStr[1] == 45) {
                        temp = new byte[byteStr.length - 9];
                        System.arraycopy(byteStr, 9, temp, 0, temp.length);
                    } else if (byteStr[0] == 27 && byteStr[1] == 46) {
                        temp = new byte[] { 108, 111, 103, 111 };
                    } else if(byteStr[0] == 29 && byteStr[1] == 33){
                        temp = new byte[byteStr.length - 6];
                        for (int j = 0; j < temp.length; j++) {
                            temp[j] = byteStr[j + 6];
                        }
                    } else {
                        // その他Line
                        temp = byteStr;
                    }
                    // lineのストリングを作る
                    outStr = outStr + new String(temp);
                    // 中央に組むline前に空欄を書く
                    if (byteStr[0] == 29 && byteStr[1] == 33 &&
                            byteStr[3] == 27 && byteStr[4] == 97 && byteStr[5] == 1) {
                        StringBuilder buffer = new StringBuilder();
                        int length = outStr.getBytes().length - 1;
                        for (int k = 0; k < 20 - length / 2; k++) {
                            buffer.append(' ');
                        }
                        // 中央に組むlineのストリングを作る
                        outStr = buffer.toString() + outStr;
                    }
                    if (byteStr[0] == 29 && byteStr[1] == 33 &&
                            byteStr[3] == 27 && byteStr[4] == 97 && byteStr[5] == 2) {
                        StringBuilder buffer = new StringBuilder();
                        int length = outStr.getBytes().length;
                        for (int k = 0; k <= 42 - length ; k++) {
                            buffer.append(' ');
                        }
                        // 右に組むlineのストリングを作る
                        outStr = buffer.toString() + outStr;
                    }
                 // font
                    if (byteStr[0] == 29 && byteStr[1] == 33 &&
                            byteStr[2] == 1 ) {
                        outStr = "_H2_" + outStr;
                    }
                    ReceiptLine line = new ReceiptLine();
                    line.setFont(new Font("ＭＳ ゴシック", 0, 12));
                    line.setLinedata(outStr);
                    receiptLineList.add(line);
                }

            }
        return receiptLineList;
    }
    
    public static List<List<byte[]>> rebuildListReceiptsList(List<List<byte[]>> lists){
        List<List<byte[]>> listReceiptsList = new ArrayList<List<byte[]>>();
        Iterator<List<byte[]>> iterList = lists.iterator();
        while(iterList.hasNext()){
            List<byte[]> temp = PrintReceiptToImg.rebuildReceiptsList(iterList.next());
            listReceiptsList.add(temp);
        }
        return listReceiptsList;
    }
    
    public static List<byte[]> rebuildReceiptsList(List<byte[]> receiptsList){
        List<byte[]> returnList = new ArrayList<byte[]>();
        Iterator<byte[]> iter = receiptsList.iterator();
        boolean flag = false;
        boolean result = false;
        while (iter.hasNext()) {
            int count = 0;
            byte[] byteTemp = iter.next();
            if(byteTemp == null){
                continue;
            }
            result = byteTemp[0] == 28 && byteTemp[1] == 45;
            if(result){
            	flag = !(byteTemp[2] == 0);
            }
            if(flag == true){
                byte[] t = iter.next();
                result = t[0] == 28 && t[1] == 45 && t[2] == 0;
                if (result) {
                    flag = false;
                    continue;
                }
                byte[] lineByte = new byte[byteTemp.length + t.length];
                System.arraycopy(byteTemp,0,lineByte,0,byteTemp.length);
                System.arraycopy(t,0,lineByte,byteTemp.length,t.length);
                for(int ii=0;ii<lineByte.length;ii++){
                    if(lineByte[ii] == 32){
                        lineByte[ii] = 95;
                    }
                }
                returnList.add(lineByte);
                continue;
            }
            for (int i = 0; i < byteTemp.length; i++) {
                    if (i == byteTemp.length -1) {
                        byte[] add = new byte[i - count + 1];
                        System.arraycopy(byteTemp, count, add, 0, i - count + 1);
                        returnList.add(add);
                        count = 0;
                    } else if (byteTemp[i] == 10) {
                        byte[] add = new byte[i - count + 1];
                        System.arraycopy(byteTemp, count, add, 0, i - count + 1);
                        returnList.add(add);
                        count += add.length;
                    }
            }
        }
        return returnList;
    }

}