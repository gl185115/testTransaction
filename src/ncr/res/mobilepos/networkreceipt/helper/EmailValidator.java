package ncr.res.mobilepos.networkreceipt.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class for email validator.
 */
public class EmailValidator {

    /** The pattern given. */
    private Pattern pattern;

    /** The matcher to match the pattern given. */
    private Matcher matcher;

    /** The Constant EMAIL_PATTERN. */
    private static final String EMAIL_PATTERN =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Instantiates a new email validator.
     */
    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Validates an email address.
     *
     * @param emailAddress the email address to validate.
     * @return true, if successful
     */
    public final boolean validate(final String emailAddress) {
        matcher = pattern.matcher(emailAddress);
        return matcher.matches();
    }
}
