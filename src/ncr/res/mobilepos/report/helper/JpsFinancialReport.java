/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.report.helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.report.constants.ReportConstants;
import ncr.res.mobilepos.report.model.PrintLine;

/**
 * JpsFinancialReport is a print service.
 */
public class JpsFinancialReport implements Printable {

    /** The textalign center. */
    private static final int TEXTALIGN_CENTER = 0;

    /** The textalign left. */
    private static final int TEXTALIGN_LEFT = 1;

    /** The textalign right. */
    private static final int TEXTALIGN_RIGHT = 2;

    /** The FOR m58_ lengt h_ siz e10. */
    private static final int FORM58_LENGTH_SIZE10 = 32;

    /** The FOR m58_ lengt h_ siz e12. */
    private static final int FORM58_LENGTH_SIZE12 = 26;

    /** The FOR m58_ lengt h_ siz e14. */
    private static final int FORM58_LENGTH_SIZE14 = 21;

    /** The FOR m58_ lengt h_ siz e16. */
    private static final int FORM58_LENGTH_SIZE16 = 19;

    /** The line length map. */
    private Map<Integer, Integer> lineLengthMap;

    /** The print buf. */
    private List<PrintLine> printBuf;

    /** The default font. */
    private Font defaultFont = new Font("ÇlÇr ÉSÉVÉbÉN", Font.PLAIN,
            ReportConstants.PARAM12);

    /** The printer name. */
    private String printerName;

    /** The logger. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * Constructor.
     */
    public JpsFinancialReport() {
        printBuf = new ArrayList<PrintLine>();
        initLineLengthMap();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Constructor with setting printer name.
     *
     * @param printerNameToSet
     *            the new printer name.
     */
    public JpsFinancialReport(final String printerNameToSet) {
        this.printerName = printerNameToSet;
        printBuf = new ArrayList<PrintLine>();
        initLineLengthMap();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Initialize the map of Char size --> the max length of one line.
     */
    private void initLineLengthMap() {
        lineLengthMap = new HashMap<Integer, Integer>();
        lineLengthMap.put(ReportConstants.PARAM10, FORM58_LENGTH_SIZE10);
        lineLengthMap.put(ReportConstants.PARAM12, FORM58_LENGTH_SIZE12);
        lineLengthMap.put(ReportConstants.PARAM14, FORM58_LENGTH_SIZE14);
        lineLengthMap.put(ReportConstants.PARAM16, FORM58_LENGTH_SIZE16);
    }

    /**
     * Add Separator.
     *
     * @param sep
     *            the separator string.
     * @param font
     *            the font format.
     */
    public final void addSeparator(final String sep, final Font font) {
        PrintLine printLine = new PrintLine();

        if (" ".equals(sep)) {
            printLine.setLineText(" ");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < FORM58_LENGTH_SIZE10; i++) {
                sb.append(sep);
            }
            printLine.setLineText(sb.toString());
        }
        if (font == null) {
            printLine.setFont(defaultFont);
        } else {
            printLine.setFont(font);
        }

        printBuf.add(printLine);
    }

    /**
     * Add text.
     *
     * @param text
     *            the text to add.
     * @param type
     *            Align Center or Align Left or Align Right
     * @param font
     *            the font
     * @return true, if successful
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public final boolean addText(final String text, final int type,
            final Font font) throws UnsupportedEncodingException {
        PrintLine printLine = new PrintLine();
        int digits = 0;
        switch (type) {
        case TEXTALIGN_CENTER:          
            if(!this.addPrintLineToBuf(text, font, printLine, toCenter(text, digits))){
            	return false;
            }
            break;
        case TEXTALIGN_LEFT:
            printLine.setLineText(text);
            printLine.setFont(font);
            printBuf.add(printLine);
            break;
        case TEXTALIGN_RIGHT:          
            if(!this.addPrintLineToBuf(text, font, printLine, toRight(text, digits))){
            	return false;
            }           
            break;
        default:
            break;
        }
        return true;
    }

    /**
     * Add two texts.
     *
     * @param item
     *            the item
     * @param value
     *            the value
     * @param font
     *            the font
     * @return true, if successful
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public final boolean addItemStr(final String item, final String value,
            final Font font) throws UnsupportedEncodingException {
        int digits = lineLengthMap.get(font.getSize());

        if ((item.getBytes("MS932").length
                + value.getBytes("MS932").length) > digits) {

            // display in two lines
            PrintLine printLine = new PrintLine();
            printLine.setLineText(item);
            printLine.setFont(font);
            printBuf.add(printLine);

            if (digits < value.getBytes("MS932").length) {
                return false;
            }
            printLine.setLineText(toRight(value, digits));
            printLine.setFont(font);
            printBuf.add(printLine);
        } else {
            int num = digits - (item.getBytes("MS932").length
                                    + value.getBytes("MS932").length);
            PrintLine printLine = new PrintLine();
            printLine.setLineText(item + getSpaces(num) + value);
            printLine.setFont(font);
            printBuf.add(printLine);
        }
        return true;
    }

    /**
     * Add item and value in format TD align left.
     *
     * @param item
     *            the item
     * @param value
     *            the value
     * @param align
     *            the alignment number
     * @param font
     *            the font format
     * @return true, if successful
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    public final boolean addItemStrLeft(final String item, final String value,
            final int align, final Font font)
            throws UnsupportedEncodingException {
        PrintLine printLine = new PrintLine();
        int digits = lineLengthMap.get(font.getSize());
        if ((align - item.getBytes("MS932").length) < 0) {
            return false;
        }

        if (align > digits) {
            return false;
        }
        printLine.setLineText(item
                + getSpaces(align - item.getBytes("MS932").length) + value);
        printLine.setFont(font);
        printBuf.add(printLine);

        return true;
    }

    /**
     * Set String for align center.
     *
     * @param data
     *            the data
     * @param digits
     *            the digits
     * @return the string value of the center alignment.
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     */
    private String toCenter(final String data, final int digits)
            throws UnsupportedEncodingException {
        String center = null;
        int num = (digits - data.getBytes("MS932").length) / 2;
        center = getSpaces(num) + data;

        return center;
    }

    /**
     * Set String for align right.
     *
     * @param data
     *            the data
     * @param digits
     *            the digits
     * @return the string value of the right alignment.
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
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
     * Get number digits spaces.
     *
     * @param num
     *            the number.
     * @return the number spaces
     */
    private String getSpaces(final int num) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < num; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.print.Printable#print(java.awt.Graphics,
     * java.awt.print.PageFormat, int)
     */
    @Override
    public final int print(final Graphics g, final PageFormat pf,
            final int pageIndex) throws PrinterException {
        if (pageIndex == 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            int current = ReportConstants.PARAM10;
            g2d.setColor(Color.black);

            int height = 0;
            for (int i = 0; i < printBuf.size(); i++) {               
                Font font = printBuf.get(i).getFont();
                if (font == null) {
                    font = defaultFont;
                }
                if (font.getSize() == ReportConstants.PARAM12) {
                    g2d.setFont(font);
                    g2d.drawString(printBuf.get(i).getLineText(), 0, current);
                    height = ReportConstants.PARAM13;
                } else if (font.getSize() == ReportConstants.PARAM10) {
                    g2d.setFont(font);
                    g2d.drawString(printBuf.get(i).getLineText(), 0, current);
                    height = ReportConstants.PARAM10;
                }
                current = current + height;
            }
            return Printable.PAGE_EXISTS;
        } else {
            return Printable.NO_SUCH_PAGE;
        }
    }

    /**
     * Prints the report.
     *
     * @return true, if successful
     * @throws PrinterException
     *             the printer exception
     */
    public final boolean printReport() throws PrinterException {

        tp.methodEnter("printReport");

        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();

        paper.setImageableArea(ReportConstants.X_COORDINATE,
                ReportConstants.Y_COORDINATE, ReportConstants.DEFAULT_WIDTH,
                    ReportConstants.DEFAULT_HEIGHT);

        pageFormat.setPaper(paper);

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        printerJob.setPrintable(this, pageFormat);

        PrintService[] services = PrinterJob.lookupPrintServices();
        PrintService service = null;

        if (printerName == null || "".equals(printerName)) {
            service = PrintServiceLookup.lookupDefaultPrintService();
        } else {
            for (int i = 0; i < services.length; i++) {
                if (services[i].getName().equals(printerName)) {
                    service = services[i];
                    break;
                }
            }
        }

        if (service == null) {
            LOGGER.logNormal("JpsRport", "JpsFinancialReport.printReport",
                    "There is no printer");
            tp.methodExit("false");
            return false;
        } else {
            LOGGER.logNormal("JpsRport", "JpsFinancialReport.printReport",
                    "Selected Printer is " + service.getName());
            printerJob.setPrintService(service);
            printerJob.print(aset);
        }

        tp.methodExit("true");

        return true;
    }
    
    /**
     * Handles condition for final boolean addText
     * 
     * @param text
     * @param digits
     * @param font
     * @param printLine
     * @param textToSet
     * @return
     * @throws UnsupportedEncodingException
     */
    private boolean addPrintLineToBuf(final String text, final Font font, PrintLine printLine, String textToSet) throws UnsupportedEncodingException{
    	int digits = lineLengthMap.get(font.getSize());
    	if (text.getBytes("MS932").length > digits) {
            return false;
        }
        printLine.setLineText(toCenter(text, digits));
        printLine.setFont(font);
        printBuf.add(printLine);
        return true;
    }

}
