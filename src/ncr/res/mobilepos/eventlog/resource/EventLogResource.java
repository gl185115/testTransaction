// Copyright (c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.eventlog.resource;

import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.eventlog.dao.IEventLogDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Web service for storing and reading POS Event Log.
 */
@Path("/eventlog")
public class EventLogResource {
    static final String PROGNAME = "EVLOGRES";

    @Context
    ServletContext context; //to access the web.xml
    /** A private member variable used for logging the class implementations. */
    static final Logger LOGGER = (Logger) Logger.getInstance();
    /** The Trace Printer. */
    Trace.Printer tp;

    /**
     * create an instance.
     */
    public EventLogResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), EventLogResource.class);
    }

    /**
     * Custom Constructor for an instance.
     *
     * <P>Initializes the logger object.
     *
     * @param contextToSet The context of the servlet.
     */
    public EventLogResource(ServletContext contextToSet) {
        this();
        context = contextToSet;
    }

    @Path("/log")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public ResultBase log(@FormParam("companyId") String companyId,
                          @FormParam("retailStoreId") String retailStoreId,
                          @FormParam("workstationId") String workstationId,
                          @FormParam("training") int training,
                          @FormParam("sequenceNumber") int sequenceNumber,
                          @FormParam("businessDayDate") String businessDayDate,
                          @FormParam("eventlog") String eventLog) {
        return log(companyId, retailStoreId, workstationId, training, sequenceNumber, 0,
                   businessDayDate, eventLog);
    }

    @Path("/log/{childNumber}")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public ResultBase log(@FormParam("companyId") String companyId,
                          @FormParam("retailStoreId") String retailStoreId,
                          @FormParam("workstationId") String workstationId,
                          @FormParam("training") int training,
                          @FormParam("sequenceNumber") int sequenceNumber,
                          @PathParam("childNumber") int childNumber,
                          @FormParam("businessDayDate") String businessDayDate,
                          @FormParam("eventlog") String eventLog) {
        tp.methodEnter("log");
        tp.println("companyId", companyId);
        tp.println("retailStoreId", retailStoreId);
        tp.println("workstationId", workstationId);
        tp.println("training", training);
        tp.println("sequenceNumber", sequenceNumber);
        tp.println("childNumber", childNumber);
        tp.println("businessDayDate", businessDayDate);
        tp.println("eventlog", eventLog.substring(0, (eventLog.length() > 16) ? 16 : eventLog.length()));

        ResultBase result = new ResultBase(ResultBase.RES_ERROR_DB);
        try {
            if (getDAO().save(companyId, retailStoreId, workstationId, training, 
                              sequenceNumber, childNumber, businessDayDate, eventLog)) {
                result.setNCRWSSResultCode(ResultBase.RES_OK);
            } else {
                result.setMessage("save error");
            }
        } catch (SQLException e) {
            LOGGER.logSnapException(PROGNAME, LOGGER.RES_EXCEP_DAO, "save exception:" + e.getErrorCode() + 
                                    "-" + e.getSQLState(), e);
        } catch (Exception e) {
            LOGGER.logSnapException(PROGNAME, LOGGER.RES_EXCEP_DAO, "save eventlog failed", e);
        }
        return (ResultBase)tp.methodExit(result);
    }

    @Path("/{companyId}/{retailStoreId}/{workstationId}/{training}/{sequenceNumber}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public String read(@PathParam("companyId") String companyId,
                       @PathParam("retailStoreId") String retailStoreId,
                       @PathParam("workstationId") String workstationId,
                       @PathParam("training") int training,
                       @PathParam("sequenceNumber") int sequenceNumber) {
        tp.methodEnter("read");
        try {
            return tp.methodExit(getDAO().load(companyId, retailStoreId, workstationId, training, sequenceNumber));
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to get event log", e);
        }
    }

    /**
     * create dao.
     */
    protected IEventLogDAO getDAO() throws DaoException {
        return DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getEventLogDAO();
    }
}
