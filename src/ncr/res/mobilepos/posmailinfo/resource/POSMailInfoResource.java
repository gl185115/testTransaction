/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 * 
 * POSMailInfoResource
 * 
 * Resource which provides a list of POS Mail notification messages.
 * 
 */
package ncr.res.mobilepos.posmailinfo.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.posmailinfo.dao.IPOSMailInfoDAO;
import ncr.res.mobilepos.webserviceif.model.JSONData;

@Path("/posmailinfo")
@Api(value = "/posmailinfo", description = "���[�����擾API")
public class POSMailInfoResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private String PROG_NAME = "POSMailInfo";
    private Trace.Printer tp;
    @Context
    private ServletContext context;
    public POSMailInfoResource() {
    	this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "JSONData", response = JSONData.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_POSMAIL_INFO_OK, message="Success"),
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    		@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
    })
    public final JSONData getPOSMailInfo(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="�X�ԍ�") @QueryParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="workstationid", value="�^�[�~�i���ԍ�") @QueryParam("workstationid") final String workstationId,
    		@ApiParam(name="businessdate", value="�Ɩ����t") @QueryParam("businessdate") final String businessDate) {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter(functionName)
    		.println("companyId", companyId)
    		.println("retailStoreId", retailStoreId)
    		.println("workstationId", workstationId)
    		.println("businessDate", businessDate);

		JSONData posMailInfo = new JSONData();
		try {
			DAOFactory sqlServer = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IPOSMailInfoDAO iPOSMailInfoDAO = sqlServer.getPOSMailInfoDAO();
			posMailInfo = iPOSMailInfoDAO.getPOSMailInfo(companyId,
					retailStoreId, workstationId, businessDate);
		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
					+ ": Failed to get list of messages.", e);
			posMailInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			posMailInfo
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			posMailInfo.setMessage(e.getMessage());
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get list of messages.", e);
			posMailInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			posMailInfo
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			posMailInfo.setMessage(e.getMessage());
		} finally {
			tp.methodExit(posMailInfo);
		}
		return posMailInfo;
    }
}