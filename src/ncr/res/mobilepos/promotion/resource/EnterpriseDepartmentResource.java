package ncr.res.mobilepos.promotion.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

/**
 * EnterpriseDepartmentResource Class is a Web Resource which support MobilePOS Promotion
 * processes.
 *
 */

@Path("/enterprisedpt")
@Api(value="/enterprisedpt", description="ïîñÂèÓïÒAPI")
public class EnterpriseDepartmentResource {
	/**
	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get
	/** The instance of the trace debug printer. */
	private Trace.Printer tp = null;

	/**
	 * The Program Name.
	 */
	private static final String PROG_NAME = "RemoteProm";

	/**
	 * Default Constructor for PromotionResource.
	 *
	 * <P>
	 * Initializes the logger object.
	 */
	public EnterpriseDepartmentResource() {
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * get Department DetailInfo
	 * 
	 * @param companyId
	 *			The companyId
	 * @param retailStoreId
	 *			Store Number where the transaction is coming from
	 * @param codeTemp
	 *			The codeTemp
	 * @param searchRetailStoreID
	 *			The searchRetailStoreID
	 * @return departmentInfo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/department_getremoteinfo")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value="ïîñÂèÓïÒéÊìæ", response=Void.class)
	public final ViewDepartment departmentInfo(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		String functionName = DebugLogger.getCurrentMethodName();
		String companyId = request.getParameter("companyId");
		String retailStoreId = request.getParameter("retailStoreId");
		String codeTemp = request.getParameter("codeTemp");
		String searchRetailStoreID = request.getParameter("searchRetailStoreID");
		ViewDepartment departmentInfo = null;
		if (StringUtility.isNullOrEmpty(companyId, retailStoreId, codeTemp, searchRetailStoreID)) {
			tp.println("Parameter[s] is empty or null.");
		}
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);

			SQLServerSystemConfigDAO systemDao = daoFactory.getSystemConfigDAO();
        	Map<String, String> mapTaxId = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_TAX_ID);

			IDepartmentDAO idepartmentDAO = daoFactory.getDepartmentDAO();
			departmentInfo = idepartmentDAO.selectDepartmentDetail(companyId, retailStoreId, codeTemp, searchRetailStoreID, mapTaxId);

			try {
				OutputStream out = response.getOutputStream();
				if(departmentInfo != null){
					out.write(departmentInfo.toString().getBytes());
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get department Info.", e); 
		}
		return departmentInfo;
	}
}
