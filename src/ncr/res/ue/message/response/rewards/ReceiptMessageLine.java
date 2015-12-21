package ncr.res.ue.message.response.rewards;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Points model.
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReceiptLine")
public class ReceiptMessageLine extends RewardBase {
    /**
     * Minimum Length of the Line.
     */
    private static final int MINIMUM_LINE_LENGTH = 11;
    /**
     * the length of the type.
     */
    private static final int TYPE_LENGTH = 2;

    /**
     * the type.
     */
    @XmlElement(name = "Type")
    private String type;

    /**
     * gets the type.
     * @return String - the type
     */
    public final String getType() {
        return type;
    }

    /**
     * the length of the alignment.
     */
    private static final int ALIGNMENT_LENGTH = 1;

    /**
     * the alignment.
     */
    @XmlElement(name = "Alignment")
    private String alignment;

    /**
     * gets the alignment.
     * @return String - the alignment
     */
    public final String getAlignment() {
        return alignment;
    }

    /**
     * the length of the format.
     */
    private static final int FORMAT_LENGTH = 3;

    /**
     * the format.
     */
    @XmlElement(name = "Format")
    private String format;

    /**
     * gets the format.
     * @return String - the format
     */
    public final String getFormat() {
        return format;
    }

    /**
     * the length of line break.
     */
    private static final int LINE_BREAK_LENGTH = 1;

    /**
     * the line break.
     */
    @XmlElement(name = "LineBreak")
    private String lineBreak;

    /**
     * gets the line break.
     * @return String - the line break
     */
    public final String getLineBreak() {
        return lineBreak;
    }

    /**
     * the length of the print length.
     */
    private static final int PRINT_LENGTH_LENGTH = 2;

    /**
     * the print length.
     */
    @XmlElement(name = "PrintLength")
    private String printLength;

    /**
     * gets the print length.
     * @return String - the print length
     */
    public final String getPrintLength() {
        return printLength;
    }

    /**
     * the quantity.
     */
    @XmlElement(name = "PrintLine")
    private String printLine;

    /**
     * gets the print line.
     * @return String - the print line
     */
    public final String getPrintLine() {
        return printLine;
    }

    /**
     * the length of the language id.
     */
    private static final int LANGUAGE_ID_LENGTH = 2;

    /**
     * the language id.
     */
    @XmlElement(name = "LanguageId")
    private String languageId;

    /**
     * gets the language id.
     * @return String - the language id
     */
    public final String getLanguageId() {
        return languageId;
    }

    /**
     * Constructor.
     * @param receiptLineMessage - the message corresponding to a certain
     * reward sent by UE.
     */
    public ReceiptMessageLine(final String receiptLineMessage) {

        if (receiptLineMessage.length()
                < MINIMUM_LINE_LENGTH) {
            return;
        }
        int startIndex = 0;
        int endIndex = TYPE_LENGTH;

        type = receiptLineMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + ALIGNMENT_LENGTH;

        alignment = receiptLineMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + FORMAT_LENGTH;

        format = receiptLineMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + LINE_BREAK_LENGTH;

        lineBreak = receiptLineMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + PRINT_LENGTH_LENGTH;

        printLength = receiptLineMessage.substring(startIndex, endIndex);

        int printLineLength = Integer.parseInt(printLength);

        if (printLineLength + endIndex + LANGUAGE_ID_LENGTH
                > receiptLineMessage.length()) {
            return;
        }

        startIndex = endIndex;
        endIndex = startIndex + printLineLength;

        printLine = receiptLineMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + LANGUAGE_ID_LENGTH;

        languageId = receiptLineMessage.substring(startIndex, endIndex);

    }
}
