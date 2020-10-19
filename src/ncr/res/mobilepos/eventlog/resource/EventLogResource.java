// Copyright (c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.eventlog.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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
@Api(value="/eventlog", description="�C�x���g���OAPI")
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

    /**
     *
     * @param companyId
     * @param retailStoreId
     * @param workstationId
     * @param training
     * @param sequenceNumber
     * @param businessDayDate
     * @param eventLog
     * @return
     */
    @Path("/log")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    @ApiOperation(value="����̃C�x���g���O�̕ۑ�", response=ResultBase.class)
    @ApiResponses(value={
    })
    public ResultBase log(
                          @ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") String companyId,
                          @ApiParam(name="retailStoreId", value="�X�ԍ�") @FormParam("retailStoreId") String retailStoreId,
                          @ApiParam(name="workstationId", value="�^�[�~�i���ԍ�") @FormParam("workstationId") String workstationId,
                          @ApiParam(name="training", value="�g���[�j���O���[�h�t���O") @FormParam("training") int training,
                          @ApiParam(name="sequenceNumber", value="����ԍ�") @FormParam("sequenceNumber") int sequenceNumber,
                          @ApiParam(name="businessDayDate", value="�Ɩ����t") @FormParam("businessDayDate") String businessDayDate,
                          @ApiParam(name="eventlog", value="�C�x���g���O") @FormParam("eventlog") String eventLog) {
        return log(companyId, retailStoreId, workstationId, training, sequenceNumber, 0,
                   businessDayDate, eventLog);
    }

    /**
     *
     * @param companyId
     * @param retailStoreId
     * @param workstationId
     * @param training
     * @param sequenceNumber
     * @param childNumber
     * @param businessDayDate
     * @param eventLog
     * @return
     */
    @Path("/log/{childNumber}")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    @ApiOperation(value="����̃C�x���g���O(����̎q�ԍ�)�̕ۑ�", response=ResultBase.class)
    @ApiResponses(value={
    })
    public ResultBase log(
            @ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") String companyId,
            @ApiParam(name="retailStoreId", value="�X�ԍ�") @FormParam("retailStoreId") String retailStoreId,
            @ApiParam(name="workstationId", value="�^�[�~�i���ԍ�") @FormParam("workstationId") String workstationId,
            @ApiParam(name="training", value="�g���[�j���O���[�h�t���O") @FormParam("training") int training,
            @ApiParam(name="sequenceNumber", value="����ԍ�") @FormParam("sequenceNumber") int sequenceNumber,
            @ApiParam(name="childNumber", value="������q�ԍ�") @PathParam("childNumber") int childNumber,
            @ApiParam(name="businessDayDate", value="�Ɩ����t") @FormParam("businessDayDate") String businessDayDate,
            @ApiParam(name="eventlog", value="�C�x���g���O") @FormParam("eventlog") String eventLog) {
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

    /**
     *
     * @param companyId
     * @param retailStoreId
     * @param workstationId
     * @param training
     * @param sequenceNumber
     * @return
     */
    @Path("/{companyId}/{retailStoreId}/{workstationId}/{training}/{sequenceNumber}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @ApiOperation(value="�C�x���g���O�̌���", response=String.class)
    @ApiResponses(value={
    })
    public String read(
                       @ApiParam(name="companyId", value="��ЃR�[�h") @PathParam("companyId") String companyId,
                       @ApiParam(name="retailStoreId", value="�X�ԍ�") @PathParam("retailStoreId") String retailStoreId,
                       @ApiParam(name="workstationId", value="�^�[�~�i���ԍ�") @PathParam("workstationId") String workstationId,
                       @ApiParam(name="training", value="�g���[�j���O���[�h�t���O") @PathParam("training") int training,
                       @ApiParam(name="sequenceNumber", value="����ԍ�") @PathParam("sequenceNumber") int sequenceNumber) {
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
