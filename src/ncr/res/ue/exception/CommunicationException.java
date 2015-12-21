package ncr.res.ue.exception;
/**
 * CommunicationException.
 * @author jg185106
 */
public class CommunicationException extends Exception {
    /**
     * Default constructor.
     */
    public CommunicationException() {
    }
    /**
     * Constructor to set message.
     * @param message the message
     */
    public CommunicationException(final String message) {
        super(message);
    }
}
