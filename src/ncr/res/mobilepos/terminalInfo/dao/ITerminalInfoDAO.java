package ncr.res.mobilepos.terminalInfo.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.terminalInfo.model.TerminalInfo;

/**
 * ITerminalInfoDAO.
 *
 * A Data Access Object implementation for Web Store Server's Business Date.
 */
public interface ITerminalInfoDAO {

    /**
     * @param companyid
     * @param storeid
     * @param workstationid
     * @param businessDayDate
     * @param trainingmode
     * @return
     * @throws DaoException
     */
    TerminalInfo getTxidInfo(String companyid, String storeid, String workstationid, String businessDayDate,
            int trainingmode) throws DaoException;
}
