package ncr.res.mobilepos.employee.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.employee.dao.IEmployeeDao;
import ncr.res.mobilepos.employee.model.EmployeeInfoResponse;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * EmployeeResource Class is a Web Resource which support MobilePOS Employee
 * information processes.
 *
 */
@Path("/employee")
@Api(value = "/employee", description = "担当者情報関連取得API")
public class EmployeeResource {
	/**
	 * ServletContext instance.
	 */
	@Context
	private ServletContext context;

	@Context
	private SecurityContext securityContext;
	/**
	 * Logger instance use for logging.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/**
	 * Program name use in logging.
	 */
	private String progName = "Employee";

	/**
	 * DAOFactory instance use to access EmployeeDAO.
	 */
	private DAOFactory daoFactory = null;

	/**
	 * Gets the dao factory.
	 *
	 * @return the dao factory
	 */
	public final DAOFactory getDaoFactory() {
		return daoFactory;
	}

	/**
	 * Sets the daoFactory.
	 *
	 * @param newDaoFactory
	 *            the new daoFactory
	 */
	public final void setDaoFactory(final DAOFactory newDaoFactory) {
		this.daoFactory = newDaoFactory;
	}

	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;

	/**
	 * Default constructor. Instantiate daoFactory and ioWriter.
	 */
	public EmployeeResource() {
		this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * 担当者の情報をUIに返送する
	 *
	 * @param companyid
	 *            企業コード、必須
	 * @return EmployeeInfoResponse
	 */
	@Path("/getallempinfo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "全担当者情報の取得", response = EmployeeInfoResponse.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "不正なリクエストパラメータ。"), })
	public final EmployeeInfoResponse getallempinfo(
			@ApiParam(name = "CompanyId", value = "会社コード") @QueryParam("CompanyId") final String companyId) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("CompanyId", companyId);

		EmployeeInfoResponse employeeInfoResponse = new EmployeeInfoResponse();

		try {
			if (StringUtility.isNullOrEmpty(companyId)) {
				tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
				employeeInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				employeeInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				employeeInfoResponse.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
				return employeeInfoResponse;
			}

			DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			IEmployeeDao employeeDao = sqlServer.getEmployeeDao();
			employeeInfoResponse = employeeDao.EmpList(companyId);
		} catch (DaoException ex) {
			LOGGER.logAlert(progName, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get status and authorization of operator", ex);
			employeeInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			employeeInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			employeeInfoResponse.setMessage(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get status and authorization of operator", ex);
			employeeInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			employeeInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			employeeInfoResponse.setMessage(ex.getMessage());
		} finally {
			tp.methodExit(employeeInfoResponse);
		}
		return employeeInfoResponse;
	}
}
