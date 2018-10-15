package ncr.res.mobilepos.department.resource;

import java.sql.SQLException;

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
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
/**
 * 改定履歴
 * バージョン         改定日付       担当者名           改定内容
 * 1.01               2014.12.11     LiQian             DIV存在チェックを対応
 */

/**
 *
 * @author AP185142
 * @author RD185102
 */
@Path("/departmentinfo")
@Api(value="/departmentinfo", description="部門情報API")
public class DepartmentResource {
	/**
     * context.
     */
    @Context
    private ServletContext context;
    /**
     * context.
     */
    @Context
    private SecurityContext securityContext;
    /**
     * logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * dao factory.
     */
    private DAOFactory daoFactory;
    /**
     * constant for progname.
     */
    private String progname = "DptRsc";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * constructor.
     */
    public DepartmentResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Sets the DaoFactory of the DepartmentResource to use the DAO methods.
     *
     * @param daofactory
     *            - The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daofactory) {
        this.daoFactory = daofactory;
    }

    /**
     * @param contextToSet
     *            the context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * Retrieves active department.
     *
     * @param retailStoreID
     *            - Path parameter for store identifier
     * @param departmentID
     *            - Path parameter for department identifier
     * @return a JSON string of the Department Details.
     */

    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="部門詳細取得", response=ViewDepartment.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DPTNOTFOUND, message="部門未検出エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
    })
    public final ViewDepartment selectDepartmentDetail(
    		@ApiParam(name="companyid", value="会社コード") @QueryParam("companyid") final String companyID,
    		@ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="departmentid", value="部門コード") @QueryParam("departmentid") final String departmentID) {

        tp.methodEnter("selectDepartmentDetail");
        tp.println("CompanyID", companyID)
          .println("RetailStoreID", retailStoreID)
          .println("DepartmentID", departmentID);

        ViewDepartment dptModel = new ViewDepartment();
        if (StringUtility.isNullOrEmpty(companyID, retailStoreID, departmentID)) {
            dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(dptModel.toString());
            return dptModel;
        }

        try {
            IDepartmentDAO iDptDao = daoFactory.getDepartmentDAO();
            dptModel = iDptDao
                    .selectDepartmentDetail(companyID, retailStoreID, departmentID, retailStoreID);

        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    "DepartmentRes",
                    "DepartmentResource.selectDepartmentDetail",
                    Logger.RES_EXCEP_DAO,
                    "Failed to get the Department Details.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                  dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                  dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    "DepartmentRes",
                    "DepartmentResource.selectDepartmentDetail",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to view Department " + departmentID + ": "
                            + ex.getMessage());
            dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(dptModel);
        }
        return dptModel;
    }

    /**
     * Gets the list of active departments.
     *
     * @param retailStoreID The RetailStore ID.
     * @param key  The ID of the Department to be search.
     * @param name The name of the Department to be search
     * @param limit 0 = Use the Default System Search Limit,
     *             -1 = Search on all stores
     *             n > 0 = limit value
     * @return DepartmentList object with the list of departments.
     */
    @Path("/list")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="部門一覧取得", response=DepartmentList.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DPTNOTFOUND, message="部門未検出エラー"),
    })
    public final DepartmentList listDepartments(
    		@ApiParam(name="companyid", value="会社コード") @QueryParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String storeId,
    		@ApiParam(name="key", value="部門コード") @QueryParam("key") final String key,
    		@ApiParam(name="name", value="部門名称") @QueryParam("name") final String name,
    		@ApiParam(name="limit", value="最大取得件数") @QueryParam("limit") final int limit) {

        String functionName = "DepartmentResource.listDepartments";

        tp.methodEnter(DebugLogger.getCurrentMethodName())
             .println("companyid", companyId)
             .println("retailstoreid", storeId)
             .println("key", key)
             .println("limit", limit)
             .println("name",name);

        DepartmentList dptList = new DepartmentList();
        try {
            IDepartmentDAO deptDao = daoFactory.getDepartmentDAO();
            dptList = deptDao.listDepartments(companyId, storeId, key, name, limit);

        } catch (DaoException ex) {
            LOGGER.logAlert(this.progname, functionName,
                    Logger.RES_EXCEP_DAO, "Failed to list departments "
                            + storeId + ": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                dptList.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                dptList.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(this.progname, functionName,
                    Logger.RES_EXCEP_GENERAL, "Failed to list departments "
                            + storeId + ": " + ex.getMessage());
            dptList.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(dptList.toString());
        }
        return dptList;
    }
}
