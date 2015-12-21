package ncr.res.mobilepos.creditauthorization.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;

/**
 * The Class Sha256KeyGenerator.
 */
public class Sha256KeyGenerator {

    /** The master key. */
    private String masterKey = "";

    /** The calendar instance. */
    private static Calendar calendar = null;

    /**
     * Instantiates a new sha256 key generator.
     */
    public Sha256KeyGenerator() {
    }

    /**
     * Instantiates a new sha256 key generator.
     *
     * @param key
     *            the key
     */
    public Sha256KeyGenerator(final String key) {
        this.masterKey = key;
    }

    /**
     * Gets the master key.
     *
     * @return the master key
     */
    public final String getMasterKey() {
        return masterKey;
    }

    /**
     * Sets the calendar.
     *
     * @param cal
     *            the new calendar
     */
    public final void setCalendar(final Calendar cal) {
        Sha256KeyGenerator.calendar = cal;
    }

    /**
     * Sets the master key.
     *
     * @param mastrKey
     *            the new master key
     */
    public final void setMasterKey(final String mastrKey) {
        this.masterKey = mastrKey;
    }

    /**
     * Get the current Machine Date and Time.
     *
     * @return The Machine Date and Time in yyyyMMddHHmmss format
     */
    public String now() {
        if (null == Sha256KeyGenerator.calendar) {
            Sha256KeyGenerator.calendar = Calendar.getInstance();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(Sha256KeyGenerator.calendar.getTimeInMillis());

        return now;
    }

    /**
     * Generate a Sha256 Key by specifying a Date and time.
     *
     * @param datetime
     *            The Date and Time in yyyyMMddHHmmss format
     * @return Sha256 Key generated
     * @throws NoSuchAlgorithmException
     *             The exception thrown when the function failed
     */
    public final String getKeyHex(final String datetime) throws NoSuchAlgorithmException {
        String inputkey = this.masterKey + datetime;
        String resultKey = "";

        try {
            byte[] hash = this.getKey(inputkey);

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            resultKey = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("Generating the Sha256 Key failed with key = "
                    + inputkey, e);
        }

        return resultKey;
    }

    /**
     * Generate a Base64 Sha256 Key by specifying a Date and time.
     *
     * @param datetime
     *            The Date and Time in yyyyMMddHHmmss format
     * @return A Base64 Sha256 Key generated
     * @throws NoSuchAlgorithmException
     *             The exception thrown when the function failed
     */
    public final String getKeyBase64(final String datetime) throws NoSuchAlgorithmException {
        String inputkey = this.masterKey + datetime;
        String resultkey = "";

        try {
            byte[] hash = this.getKey(inputkey);
            resultkey = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("Generating a Base64"
                    + " Sha256 key failed with key = " + this.masterKey
                    + datetime, e);
        }

        return resultkey;
    }

    /**
     * Generate a Sha256 Key with the current Date and time.
     *
     * @return Sha256 Key generated
     * @throws NoSuchAlgorithmException
     *             The exception thrown when the function failed
     */
    public final String getKeyHex() throws NoSuchAlgorithmException  {
        return getKeyHex(this.now());
    }

    /**
     * Generate a Base64 Sha256 Key with the current Date and time.
     *
     * @return A Base64 Sha256 Key generated
     * @throws NoSuchAlgorithmException 
     *             The exception thrown when the function failed
     */
    public final String getKeyBase64() throws NoSuchAlgorithmException  {
        return getKeyBase64(this.now());
    }

    /**
     * Get the Sha256 Key in array of bytes.
     *
     * @param inputkey
     *            The key to be hashed
     * @return the key
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     */
    private byte[] getKey(final String inputkey)
            throws NoSuchAlgorithmException {
        byte[] hashedByte = null;

        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");

        hashedByte = md.digest(inputkey.getBytes());

        return hashedByte;
    }
}
