// Copyright (c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.offlinecredit.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.sql.SQLException;
import java.util.Base64;
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
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.offlinecredit.dao.IOfflineCreditDAO;
import ncr.res.mobilepos.offlinecredit.model.OfflineData;

/**
 * Web service for storing offline credit transaction
 */
@Path("/offlinecredit")
@Api(value="/offlinecredit", description="オフラインクレジットAPI")
public class OfflineCreditResource {
    static final String PROGNAME = "OFFCREDIT";

    @Context
    ServletContext context; //to access the web.xml
    /** A private member variable used for logging the class implementations. */
    static final Logger LOGGER = (Logger) Logger.getInstance();
    /** The Trace Printer. */
    Trace.Printer tp;
    
    /**
     * create an instance.
     */
    public OfflineCreditResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Custom Constructor for an instance.
     *
     * <P>Initializes the logger object.
     *
     * @param contextToSet The context of the servlet.
     */
    public OfflineCreditResource(ServletContext contextToSet) {
        this();
        context = contextToSet;
    }

    @Path("/log")
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="オフラインクレジット取引情報保存", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    })
    public ResultBase log(OfflineData data) {
        tp.methodEnter("log")
            .println("companyId", data.getCompanyId())
            .println("storeId", data.getRetailStoreId())
            .println("workstationId", data.getWorkstationId())
            .println("sequenceNumber", data.getSequenceNumber());
        ResultBase result = new ResultBase(ResultBase.RES_OK);

        try {
            Base64.Decoder dec = Base64.getDecoder();
            byte[] iv = dec.decode(data.getIV());
            tp.println("iv", iv);
            byte[] body = dec.decode(data.getPersonalData());
            IOfflineCreditDAO dao = getDAO();
            dao.save(data.getCompanyId(), data.getRetailStoreId(), data.getWorkstationId(), 0,
                     data.getSequenceNumber(), data.getBusinessDayDate(), iv, body);
        } catch (Exception e) {
            LOGGER.logSnapException(PROGNAME, LOGGER.RES_EXCEP_DAO, "save offlinecredit failed", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        }
        return (ResultBase)tp.methodExit(result);
    }

    /**
     * create dao.
     */
    protected IOfflineCreditDAO getDAO() throws DaoException {
        return DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getOfflineCreditDAO();
    }
}

