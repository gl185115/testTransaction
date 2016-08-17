/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.report.model;

import java.awt.Font;

/**
 * PrintLine Class is a Model representation of the information which identifies
 * the line data of printing.
 */
public class PrintLine {

    /** The line text. */
    private String lineText;

    /** The font. */
    private Font font;

    /**
     * Gets the line text.
     *
     * @return lineText
     */
    public final String getLineText() {
        return lineText;
    }

    /**
     * Sets the line text.
     *
     * @param lineTextToSet
     *            the new line text
     */
    public final void setLineText(final String lineTextToSet) {
        this.lineText = lineTextToSet;
    }

    /**
     * Gets the font.
     *
     * @return font
     */
    public final Font getFont() {
        return font;
    }

    /**
     * Sets the font.
     *
     * @param fontToSet
     *            the new font
     */
    public final void setFont(final Font fontToSet) {
        this.font = fontToSet;
    }
}
