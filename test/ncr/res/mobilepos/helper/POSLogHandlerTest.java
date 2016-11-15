package ncr.res.mobilepos.helper;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.journalization.model.poslog.WorkstationID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class POSLogHandlerTest {

    private PosLog inputPosLog;

    @Before
    public void init() {
        inputPosLog = new PosLog();
        Transaction transaction = new Transaction();
        transaction.setRetailStoreID("0");
        WorkstationID workstationID = new WorkstationID();
        transaction.setWorkStationID(workstationID);
        transaction.setSequenceNo("0");
        transaction.setBusinessDayDate("0000-00-00");
        transaction.setBeginDateTime("0000-00-00T00:00:00");
        inputPosLog.setTransaction(transaction);
    }

    @Test
    public void validateRequiredFields() throws ParseException {
        assertTrue(POSLogHandler.isValid(inputPosLog));

        Transaction transaction = inputPosLog.getTransaction();

        // Asserts null transaction
        inputPosLog.setTransaction(null);
        assertFalse(POSLogHandler.isValid(inputPosLog));

        // Allows empty String.
        inputPosLog.setTransaction(transaction);
        transaction.setRetailStoreID("");
        assertTrue(POSLogHandler.isValid(inputPosLog));
        transaction.setSequenceNo("");
        assertTrue(POSLogHandler.isValid(inputPosLog));
    }

    @Test
    public void validateNullPosLog() throws ParseException {
        assertTrue(POSLogHandler.isValid(inputPosLog));

        assertFalse(POSLogHandler.isValid(null));

        Transaction transaction = inputPosLog.getTransaction();
        // Asserts null String.
        transaction.setRetailStoreID(null);
        assertFalse(POSLogHandler.isValid(inputPosLog));

        transaction.setRetailStoreID("");
        assertTrue(POSLogHandler.isValid(inputPosLog));

        transaction.setSequenceNo(null);
        assertFalse(POSLogHandler.isValid(inputPosLog));
    }

    @Test
    public void validateNullWorkStationID() throws ParseException {
        assertTrue(POSLogHandler.isValid(inputPosLog));
        Transaction transaction = inputPosLog.getTransaction();
        // Asserts null workstation id.
        transaction.setWorkStationID(null);
        assertFalse(POSLogHandler.isValid(inputPosLog));
    }

    @Test
    public void validateNullDates() throws ParseException {
        assertTrue(POSLogHandler.isValid(inputPosLog));
        Transaction transaction = inputPosLog.getTransaction();
        // Asserts null business day.
        String businessDay = transaction.getBusinessDayDate();
        transaction.setBusinessDayDate(null);
        assertFalse(POSLogHandler.isValid(inputPosLog));
        transaction.setBusinessDayDate(businessDay);

        assertTrue(POSLogHandler.isValid(inputPosLog));
        // Asserts null begin date time.
        transaction.setBeginDateTime(null);
        assertFalse(POSLogHandler.isValid(inputPosLog));
    }


    @Test(expected = ParseException.class)
    public void isValidBusinessDayParseException() throws ParseException {
        assertTrue(POSLogHandler.isValid(inputPosLog));
        Transaction transaction = inputPosLog.getTransaction();
        transaction.setBusinessDayDate("aaaa");
        POSLogHandler.isValid(inputPosLog);
    }

    @Test(expected = ParseException.class)
    public void isValidBeginDateTimeException() throws ParseException {
        assertTrue(POSLogHandler.isValid(inputPosLog));
        Transaction transaction = inputPosLog.getTransaction();
        transaction.setBeginDateTime("aaaa");
        POSLogHandler.isValid(inputPosLog);
    }

}