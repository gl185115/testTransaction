package ncr.res.mobilepos.ej.resource;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.ej.dao.INameSystemInfoDAO;
import ncr.res.mobilepos.ej.dao.SQLServerNameSystemInfoDAO;
import ncr.res.mobilepos.ej.model.EjInfos;
import ncr.res.mobilepos.ej.model.PosLogInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;

/**
 * EnterpriseEjInfoResource Class is a Web Resource which support MobilePOS Promotion
 * processes.
 *
 */

@Path("/enterpriseej")
@Api(value="/enterpriseej", description="E/JèÓïÒAPI")
public class EnterpriseEjInfoResource {
	/** The instance of the trace debug printer. */
	private Trace.Printer tp = null;

	/** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger

    /**
	 * The Program Name.
	 */
    private static final String PROG_NAME = "EnterpriseEjInfoResource";

	/**
	 * Default Constructor for EnterpriseEjInfoResource.
	 *
	 * <P>
	 * Initializes the logger object.
	 */
	public EnterpriseEjInfoResource() {
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * get E/J DetailInfo
	 *
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 *
	 * @return EjInfos
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getremoteejinfo")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value="E/JèÓïÒéÊìæ", response=Void.class)
	public final EjInfos ejInfo(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String retailstoreId = request.getParameter("retailStoreId");
		String workstationId = request.getParameter("workstationId");
		String txType = request.getParameter("txType");
		String sequencenumberFrom = request.getParameter("sequencenumberFrom");
		String sequencenumberTo = request.getParameter("sequencenumberTo");
		String businessDateTimeFrom = request.getParameter("businessDateTimeFrom");
		String businessDateTimeTo = request.getParameter("businessDateTimeTo");
		String countFrom = request.getParameter("countFrom");
		String countTo = request.getParameter("countTo");
		String operatorId = request.getParameter("operatorId");
		String salesPersonId = request.getParameter("salesPersonId");
		String trainingFlag = request.getParameter("trainingFlag");

		EjInfos ejInfos = new EjInfos();
		if (StringUtility.isNullOrEmpty(companyId, retailstoreId, workstationId, txType, sequencenumberFrom, sequencenumberTo,
				businessDateTimeFrom, businessDateTimeTo, countFrom, countTo, operatorId, salesPersonId, trainingFlag)) {
			tp.println("Parameter[s] is empty or null.");
		}
		try {
			EjResource ejResource = new EjResource();
			ejInfos = ejResource.getEjInfoByTaxType(companyId, retailstoreId, workstationId, txType, sequencenumberFrom, sequencenumberTo,
					businessDateTimeFrom, businessDateTimeTo, countFrom, countTo, operatorId, salesPersonId, trainingFlag);
		
			OutputStream out = response.getOutputStream();
			if(ejInfos != null){
				out.write(ejInfos.toString().getBytes());
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ejInfos;
	}

	/**
	 * get PosLog DetailInfo
	 *
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 *
	 * @return EjInfos
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getremoteposloginfo")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value="POSLOGèÓïÒéÊìæ", response=Void.class)
	public final PosLogInfo getSubPosLogInfo(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String functionName = DebugLogger.getCurrentMethodName();
		String companyId = request.getParameter("companyId");
		String retailstoreId = request.getParameter("retailStoreId");
		String workstationId = request.getParameter("workstationId");
		String sequencenumber = request.getParameter("sequencenumber");
		String businessDate = request.getParameter("businessDate");
		String trainingFlag = request.getParameter("trainingFlag");
		PosLogInfo posLogInfo = null;

		if (StringUtility.isNullOrEmpty(companyId, retailstoreId, workstationId, sequencenumber, businessDate, trainingFlag)) {
			tp.println("Parameter[s] is empty or null.");
		}
		try {
			INameSystemInfoDAO dao = new SQLServerNameSystemInfoDAO();
			posLogInfo = dao.getPosLogInfo(companyId, retailstoreId, workstationId, sequencenumber, businessDate, trainingFlag);

			OutputStream out = response.getOutputStream();
			if (posLogInfo != null) {
				out.write(posLogInfo.toString().getBytes());
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
					"Failed to get the PosLog Info.\n" + e.getMessage());
		}
		return posLogInfo;
	}
}
