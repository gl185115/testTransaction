package ncr.res.mobilepos.helper;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class that formats the date.
 *
 */
public final class DateFormatUtility {
	/**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "DateFormatUtility";
	
    /** Default Constructor. */
    private DateFormatUtility() { 
    	
    }

    /**
     * returns yyyy-MM-dd HH:mm:ss format of the passed date.
     * @param strDate - the date string to format
     * @return String - date string in yyyy-MM-dd HH:mm:ss format
     */
    public static String localeTime(final String strDate) {
        String strBld = strDate;
        try {

            SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMddhhmmss");
            SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                strBld = f2.format(f1.parse(strDate));
            } catch (Exception e) {
            	LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, "Failed to parse date. " + e.getMessage(), e);
            }

        } catch (Exception ex) {
            strBld = strDate;
        }
        return strBld;
    }

    /**
     * Check if a given date is in Legal format.
     * @param s             The string to check
     * @param params         The available formats
     * @return true if acceptable, false if not
     */
    public static boolean isLegalFormat(final String s,
            final String ... params) {
        boolean result = false;
        for (String format : params) {
              SimpleDateFormat sdf = new SimpleDateFormat(format);
              sdf.setLenient(false);
              result = result || (sdf.parse(s, new ParsePosition(0)) != null);
        }

        return result;
    }
    /**
     * Get the current date and/or time with specific format.
     * @param dateTimeFormat        The Date and time Format.
     *                              date and time.
     * @return  The Date and Time depending on the given format.
     */
    public static String getCurrentDateTimeFormatted(
            final String dateTimeFormat) {
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        return  dateFormat.format(Calendar.getInstance().getTime());
    }
    /**
     * Parse a date string to {@link Date} from
     * a given array of of date formats.
     * @param dateString - the date string to be parsed
     * @param formatStrings - the date formats
     * @return {@link Date}
     */
    public static Date parse(final String dateString,
            final String ... formatStrings) {
        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            } catch (java.text.ParseException e) {
            	LOGGER.logAlert(PROG_NAME, PROG_NAME + ".parse", 
            			Logger.RES_EXCEP_PARSE, "Failed to parse date. "
            			+ e.getMessage());
            }
        }
        return null;
    }
    
    /**
     * The helper that will set/increment a date to a specific number of days
     * @param dateToChange The date to be changed/incremented
     * @param daysToAdd The number of days to be added to the date
     * @return The new date
     */
    public static String addDays(String dateToChange, int daysToAdd) {   
    	String datePattern = "yyy-MM-dd";
    	SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
    	Calendar myCalendar = Calendar.getInstance();
    	if(StringUtility.isNullOrEmpty(dateToChange) ||
    			(!StringUtility.isNullOrEmpty(dateToChange) && 
        		!isLegalFormat(dateToChange,"yyyy-MM-dd"))) {	
    		LOGGER.logAlert(PROG_NAME, PROG_NAME + ".addDays", 
    				Logger.RES_EXCEP_PARSE, "Failed to parse date. "
        			+ "Invalid value for date.");   		
    		return null;
        } else {
    		myCalendar.setTime(parse(dateToChange, datePattern));
        	myCalendar.add(Calendar.DATE, daysToAdd);     	
        	return dateFormatter.format(myCalendar.getTime());
        } 	
    }
}
