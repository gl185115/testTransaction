package ncr.res.ue.exception;
/**
 * MessageException.
 * @author jg185106
 */
public class MessageException extends Exception {
    /**
     * Default constructor.
     */
    public MessageException() {
    }
    /**
     * Constructor to set message.
     * @param message the message
     */
    public MessageException(final String message) {
        super(message);
    }
}
