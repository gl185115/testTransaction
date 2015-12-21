/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

import java.awt.Font;

/**
 * PaperReceiptFooter Class is a Model representation of the
 * information which identifies the line data of paper receipt.
 */
public class ReceiptLine {
    /**
     * font of the line.
     */
    private Font font;
    /**
     * data that makes up the line.
     */
    private String linedata;

    /**
     * default constructor.
     */
    public ReceiptLine() {

    }
    /**
     * constructor.
     * @param fontToSet font
     * @param linedataToSet linedata
     */
    public ReceiptLine(final Font fontToSet, final String linedataToSet) {
        this.font = fontToSet;
        this.linedata = linedataToSet;
    }
    /**
     * @return font
     */
    public final Font getFont() {
        return font;
    }

    /**
     * @param fontToSet  font
     */
    public final void setFont(final Font fontToSet) {
        this.font = fontToSet;
    }

    /**
     * @return linedata
     */
    public final String getLinedata() {
        return linedata;
    }

    /**
     * @param linedataToSet  linedata
     */
    public final void setLinedata(final String linedataToSet) {
        this.linedata = linedataToSet;
    }

}
