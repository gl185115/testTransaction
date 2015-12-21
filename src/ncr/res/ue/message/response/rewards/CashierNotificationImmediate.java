package ncr.res.ue.message.response.rewards;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.rewards.display.Line;

/**
 * CashierNotification response parsing model.
 * @author jg185106
 *
 */
@XmlRootElement(name = "CashierNotificationImmediate")
public class CashierNotificationImmediate
       extends CashierNotification {
    /**
     * Lines length constant.
     */
    public static final int LINES_LENGTH = 2;
    /**
     * Number of display lines.
     */
    @XmlElement(name = "Lines")
    private int lines = 0;
    /**
     * Getter for the number of display lines.
     * @return The Lines.
     */
    public final int getLines() {
        return this.lines;
    }
    /**
     * Default constructor.
     */
    public CashierNotificationImmediate() {
    }
    /**
     * Constructor that receives the message
     * string.
     * @param message - the reward message to parse.
     * @throws MessageException The Exception thrown when error occurs.
     */
    public CashierNotificationImmediate(final String message)
            throws MessageException {
        String cashierMsg = parseRewardIds(message);
        cashierMsg =
            this.parseNotificationParameters(cashierMsg);
        int startIndex = 0;
        int endIndex = LINES_LENGTH;
        this.lines = Integer.parseInt(
                cashierMsg.substring(startIndex, endIndex));
        startIndex += endIndex;
        String displayLineMessage = cashierMsg.substring(startIndex);
        List<Line> displayLines = new ArrayList<Line>();
        while (!displayLineMessage.isEmpty()) {
            Line line = new Line();
            startIndex = 0;
            endIndex = startIndex + Line.LINE_LENGTH_LENGTH;
            line.setLineLength(Integer.parseInt(
                    displayLineMessage.substring(startIndex, endIndex)));
            startIndex = endIndex;
            endIndex = startIndex + line.getLineLength();
            line.setDisplayLine(
                    displayLineMessage.substring(startIndex, endIndex));
            startIndex = endIndex;
            displayLines.add(line);
            displayLineMessage = displayLineMessage.substring(startIndex);
        }
        if (lines != displayLines.size()) {
            throw new MessageException("Parsed display lines size does"
                    + " not match to the expected number of lines.");
        }
        this.setDisplayLines(displayLines);
    }
}
