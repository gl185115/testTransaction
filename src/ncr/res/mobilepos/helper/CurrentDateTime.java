   /*
    * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
    *
    * CurrentDateTime
    *
    * Helper class for manipulating the system's current date and time
    *
    * Jessel G. De la Cerna
    */

package ncr.res.mobilepos.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Helper class to retrieve the
 * current date and time of the system.
 */
public final class CurrentDateTime {
    /** Default Constructor. */
    private CurrentDateTime() {    	
    }
    /**
     * the class instance of the current date.
     */
    private static String currentDate;
    /**
     * the class instance of the previous date.
     */
    private static String prevDate;
    /**
     * flag for telling whether am or pm.
     */
    private static int amPm;

    /***
     * Get the current time in h:mm format.
     * @return currenttime in h:mm format
     */
    public static String getCurrTimeByHourMins() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat timeformat = new SimpleDateFormat("h:mm");

        StringBuilder sb = new StringBuilder();
        sb.append(timeformat.format(cal.getTime()));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        setCurrentDate(dateFormat.format(date));

        cal.add(Calendar.DATE, -1); // subtract 1day to date
        setPrevDate(dateFormat.format(cal.getTime()));
        //set AM_PM, use for SQLServerPosLogDAO
        setCurrentTimeAmPm(cal.get(Calendar.AM_PM));

        return sb.toString();
    }

    /***
     * Use to set the current time's AM_PM.
     * @param amPmToSet - flag to determine if am or pm
     */
    public static void setCurrentTimeAmPm(final int amPmToSet) {
        setAmPm(amPmToSet); //use for SQLServerPosLogDAO
    }
    /**
     * Set the Previous date.
     * @param prevDateToSet The previous date to set.
     */
    public static void setPrevDate(final String prevDateToSet) {
        CurrentDateTime.prevDate = prevDateToSet;
    }
    /**
     * Getter for the Previous Date.
     * @return The previous date.
     */
    public static String getPrevDate() {
        return prevDate;
    }
    /**
     * Setter for the Current Date.
     * @param currentDateToSet The Current Date.
     */
    public static void setCurrentDate(final String currentDateToSet) {
        CurrentDateTime.currentDate = currentDateToSet;
    }

    /**
     * Get the Current Date.
     * @return  The Current Date.
     */
    public static String getCurrentDate() {
        return currentDate;
    }

    /**
     * Setter for the AM/PM timezone.
     * @param amPM The timezone flag.
     */
    public static void setAmPm(final int amPM) {
        CurrentDateTime.amPm = amPM;
    }

    /**
     * Getter for the AM/PM timezon.
     * @return The Timezone.
     */
    public static int getAmPm() {
        return CurrentDateTime.amPm;
    }

}
