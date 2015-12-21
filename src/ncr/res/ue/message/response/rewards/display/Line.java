package ncr.res.ue.message.response.rewards.display;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Line model for Display.
 * @author jg185106
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DisplayLine")
public class Line {
    /**
     * LineLength message length.
     */
    public static final int LINE_LENGTH_LENGTH = 2;
    /**
     * Language ID message length.
     */
    public static final int LANGUAGE_ID_LENGTH = 2;
    /**
     * Length of the following DisplayLine field
     * in the CashierNotification reward.
     */
    @XmlElement(name = "LineLength")
    private int lineLength = 0;
    /**
     * Message for cashier.
     */
    @XmlElement(name = "DisplayMessage")
    private String displayLine;
    /**
     * Language identifier for preceding DisplayLine.
     */
    @XmlElement(name = "LanguageID")
    private String languageId;
    /**
     * Getter for lineLength.
     * @return int
     */
    public final int getLineLength() {
        return this.lineLength;
    }
    /**
     * Setter for lineLength.
     * @param lineLen - the length of the display line.
     */
    public final void setLineLength(
            final int lineLen) {
        this.lineLength = lineLen;
    }
    /**
     * Getter for the displayLine.
     * @return String
     */
    public final String getDisplayLine() {
        return this.displayLine;
    }
    /**
     * Setter for displayLine.
     * @param line - the the display line.
     */
    public final void setDisplayLine(
            final String line) {
        this.displayLine = line;
    }
    /**
     * Getter for the languageId.
     * @return String
     */
    public final String getLanguageId() {
        return this.languageId;
    }
    /**
     * Setter for languageId.
     * @param langId - the the display line.
     */
    public final void setLanguageId(
            final String langId) {
        this.languageId = langId;
    }
}
