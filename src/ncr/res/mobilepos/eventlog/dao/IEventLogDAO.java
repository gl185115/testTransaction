// Copyright(c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.eventlog.dao;

import java.io.Closeable;

/**
 * interface for saving and reading EventLog.
 */
public interface IEventLogDAO extends Closeable {
    /**
     * save eventlog.
     */
    boolean save(String companyId, String retailStoreId, String workstationId, int training,
                 int sequenceNumber, int child, String businessDayDate, String eventLog) throws Exception;


    /**
     * read eventlog.
     * devided log into one.
     */
    String load(String companyId, String retailStoreId, String workstationId, int training, int sequenceNumber)
        throws Exception;
}
