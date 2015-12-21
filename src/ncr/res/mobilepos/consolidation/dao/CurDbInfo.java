/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * CurDbInfo
 *
 * CurDbInfo is a helper class that holds the current sequence
 * number being processed
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.dao;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * CurDbInfo is a helper class that holds the current sequence number being
 * processed within the consolidation.
 */
public class CurDbInfo {
    /**
     * Recovery Time in 30 Minutes.
     */
    private static final int RECOVERY_TIME = 30;
    /**
     * Timestamp instance use for recovery time.
     */
    private Timestamp recoveryTime;
    /**
     * Sequence number.
     */
    private String seqNum;

    /**
     * isRecoveryTime is a method that tells if the Sequence Number needs to be
     * assigned back to Zero. Doing the assigning back to zero of the sequence
     * number, makes unprocessed(due to deadlock, if there is a chance) PosLOG
     * XML will be re-selected .
     *
     * @return true if it is time for recovery, else, false.
     */
    public final boolean isRecoveryTime() {
        java.util.Calendar currentDate = java.util.Calendar.getInstance();
        Timestamp currenttime = new Timestamp(currentDate.getTime().getTime());

        // Is this the first consolidation? OR
        // Is it Okay to do the Recovery?
        // If yes, set the recovery time. And, return true.
        if (null == recoveryTime || recoveryTime.before(currenttime)) {
            currentDate.add(Calendar.MINUTE, RECOVERY_TIME);
            recoveryTime = new Timestamp(currentDate.getTime().getTime());
            return true;
        }

        return false;
    }

    /**
     * Sets sequence number.
     *
     * @param seqNumToSet
     *            The sequence number to set.
     */
    public final void setSeqNum(final String seqNumToSet) {
        this.seqNum = seqNumToSet;
    }

    /**
     * Gets sequence number.
     *
     * @return sequence number.
     */
    public final String getSeqNum() {
        return seqNum;
    }
}
