package ncr.res.ue.message.response.rewards;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.message.response.rewards.display.Line;

/**
 * CashierNotification response parsing model.
 * @author jg185106
 *
 */
@XmlRootElement(name = "CashierNotification")
public class CashierNotification extends RewardBase {
    /**
     * Command length constant.
     */
    public static final int COMMAND_LENGTH = 1;
    /**
     * Beep length constant.
     */
    public static final int BEEP_LENGTH = 1;
    /**
     * Duration length constant.
     */
    public static final int DURATION_LENGTH = 2;
    /**
     * Action to take with the display message.
     * 0 = Display Message as normal
     */
    @XmlElement(name = "Command")
    private int command = 0;
    /**
     * Action to take regarding audible alert.
     * 0 = Do Not Beep
     * 1 = Beep
     * 2 = Beep until cleared
     * 3 = Beep for duration XX seconds
     */
    @XmlElement(name = "Beep")
    private int beep = 0;
    /**
     * Beep duration in seconds.
     */
    @XmlElement(name = "Duration")
    private int duration = 0;
    /**
     * Display lines.
     */
    @XmlElement(name = "DisplayLine")
    private List<Line> displayLines;
    /**
     * Getter for the Command.
     * @return int
     */
    public final int getCommand() {
        return this.command;
    }
    /**
     * Getter for the beep.
     * @return int
     */
    public final int getBeep() {
        return this.beep;
    }
    /**
     * Getter for the duration.
     * @return int
     */
    public final int getDuration() {
        return this.duration;
    }
    /**
     * Getter for the display lines.
     * @return List<{@link Line}>
     */
    public final List<Line> getDisplayLines() {
        return this.displayLines;
    }
    /**
     * Setter for the display lines.
     * @param lines - the display lines array to set.
     */
    protected final void setDisplayLines(
            final List<Line> lines) {
        this.displayLines = lines;
    }
    /**
     * Default constructor.
     */
    public CashierNotification() {
    }
    /**
     * Constructor that receives the message
     * string.
     * @param message - the reward message to parse.
     */
    public CashierNotification(final String message) {
        String cashierMsg = parseRewardIds(message);
        String displayLineMessage =
            this.parseNotificationParameters(cashierMsg);
        List<Line> dispLines = new ArrayList<Line>();
        while (!displayLineMessage.isEmpty()) {
            Line line = new Line();
            int startIndex = 0;
            int endIndex = startIndex + Line.LINE_LENGTH_LENGTH;
            line.setLineLength(Integer.parseInt(
                    displayLineMessage.substring(startIndex, endIndex)));
            startIndex = endIndex;
            endIndex = startIndex + line.getLineLength();
            line.setDisplayLine(
                    displayLineMessage.substring(startIndex, endIndex));
            startIndex = endIndex;
            endIndex = startIndex + Line.LANGUAGE_ID_LENGTH;
            line.setLanguageId(
                    displayLineMessage.substring(startIndex, endIndex));
            startIndex = endIndex;
            dispLines.add(line);
            displayLineMessage = displayLineMessage.substring(startIndex);
        }
        this.setDisplayLines(dispLines);
    }
    /**
     * Parses the cashier notification parameters.
     * @param message - the message to parse the notification
     *                  parameters from.
     * @return String - returns the unparsed string
     */
    protected final String parseNotificationParameters(final String message) {
        int startIndex = 0;
        int endIndex = COMMAND_LENGTH;
        this.command = Integer.parseInt(
                message.substring(startIndex, endIndex));
        startIndex = endIndex;
        endIndex = startIndex + BEEP_LENGTH;
        this.beep = Integer.parseInt(
                message.substring(startIndex, endIndex));
        startIndex = endIndex;
        endIndex = startIndex + DURATION_LENGTH;
        this.duration = Integer.parseInt(
                message.substring(startIndex, endIndex));
        startIndex = endIndex;
        return message.substring(startIndex);
    }
}
