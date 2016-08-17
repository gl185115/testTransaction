// Copyright(c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.offlinecredit.dao;

import java.io.Closeable;

public interface IOfflineCreditDAO extends Closeable {
    /**
     * save eventlog.
     */
    boolean save(String companyId, String retailStoreId, String workstationId, int training,
                 int sequenceNumber, String businessDayDate, byte[] iv, byte[] data) throws Exception;
}
