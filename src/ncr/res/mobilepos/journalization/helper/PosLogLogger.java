/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * PosLogger
 *
 * Helper Class for logging the PosLog transaction.
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.journalization.helper;

import javax.xml.bind.JAXBException;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.JournalizationException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.constants.PosLogRespConstants;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;


/**
 * A helper class used to log the PosLog transaction.
 *
 * @author cc185102
 *
 */
public class PosLogLogger {
    /** The Trace Printer. */
    private Trace.Printer tp;
    
    /** The Default Constructor. */
    public PosLogLogger() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Logs transaction into the database using its xml format.
     * @param posLogXml         The PosLog XML necessary to any type of
     *                                transaction
     * @return                  PoSLogResp that tells if a transaction
     *                               has been successful.
     * @throws Exception        Thrown when logging transaction
     *                               exception occurred
     */
    public final PosLogResp log(final PosLog posLog, final String posLogXml, final int trainingMode) 
    		throws Exception {
        tp.methodEnter(DebugLogger.getCurrentMethodName());
        
        PosLogResp posLogresp = null;
        
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        IPosLogDAO posLogDAO = daoFactory.getPOSLogDAO();

        //Ask the IPosLogDAO to save the POSLog information
        posLogDAO.savePOSLog(posLog, posLogXml, trainingMode);
        
        posLogresp = new PosLogResp();
        posLogresp.setStatus(PosLogRespConstants.NORMAL_END);
        posLogresp.setTxID(posLog.getTransaction().getSequenceNo());
        tp.methodExit(posLogresp.toString());
        
        return posLogresp;
    }
}
