package ncr.res.mobilepos.department.resource;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;
/**
 * 改定履歴
 * バージョン         改定日付       担当者名           改定内容
 * 1.01               2014.12.11     LiQian             DIV存在チェックを対応
 */
import ncr.res.mobilepos.xebioapi.model.JSONData;

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
    
    private String pathName = "departmentinfo";

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
     * Deletes a department.
     *
     * @param storeid
     *            - id of the store
     * @param dptid
     *            - id of the department
     * @return ResultBase
     */
    @Path("/delete")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="部門を削除する", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_DPTMT_NOT_EXIST, message="部門が存在しない"),
    })
    public final ResultBase deleteDepartment(
    		@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String storeid,
    		@ApiParam(name="departmentid", value="部門コード") @FormParam("departmentid") final String dptid) {

        tp.methodEnter("deleteDepartment");
        tp.println("StoreID", storeid).println("DepartmentID", dptid);

        ResultBase result = null;

        try {
        	String appId = pathName.concat(".delete");
        	Department department = new Department();
        	department.setDepartmentID(dptid);
        	department.setUpdAppId(appId);
        	department.setUpdOpeCode(getOpeCode());
        	
            IDepartmentDAO dept = daoFactory.getDepartmentDAO();
            result = dept.deleteDepartment(storeid, department);
        } catch (DaoException e) {
            LOGGER.logAlert(this.progname, "deleteDepartment",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result = new ResultBase(ResultBase.RES_ERROR_DB, e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(this.progname, "deleteDepartment",
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            result = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(result);
        }

        return result;
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
    @ApiOperation(value="有効な部門情報を検索する", response=ViewDepartment.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DPTNOTFOUND, message="部門がみつからない"),
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
                    .selectDepartmentDetail(companyID, retailStoreID, departmentID);
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
    @ApiOperation(value="有効な部門情報取得", response=DepartmentList.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
    })
    public final DepartmentList listDepartments(
    		@ApiParam(name="StoreId", value="店舗コード") @QueryParam("retailstoreid") final String storeId,
    		@ApiParam(name="key", value="部門コード") @QueryParam("key") final String key,
    		@ApiParam(name="name", value="部門名称") @QueryParam("name") final String name,
    		@ApiParam(name="limit", value="制限条数") @QueryParam("limit") final int limit) {

        String functionName = "DepartmentResource.listDepartments";

        tp.methodEnter(DebugLogger.getCurrentMethodName())
             .println("retailstoreid", storeId)
             .println("key", key)
             .println("limit", limit)
             .println("name",name);


        DepartmentList dptList = new DepartmentList();
        try {
            IDepartmentDAO deptDao = daoFactory.getDepartmentDAO();
            dptList = deptDao.listDepartments(storeId, key, name, limit);
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

    /**
     * Creates new active department.
     *
     * @param storeid
     *            - store identifier to which the department belongs.
     * @param dptid
     *            - department id of new department.
     * @param dpt
     *            - contains new department name.
     * @return ResultBase
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final ResultBase createDepartment(
            @FormParam("retailstoreid") final String storeid,
            @FormParam("departmentid") final String dptid,
            @FormParam("department") final String dpt) {

        tp.methodEnter("createDepartment");
        tp.println("RetailStoreID", storeid).println("DepartmentID", dptid).
            println("Department", dpt);

        ResultBase result = new ResultBase();
        if (StringUtility.isNullOrEmpty(storeid)) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(result);
            return result;
        } else if (!storeid.equals(GlobalConstant.ENTERPRISE_STOREID)) {
            StoreResource storeRes = new StoreResource();
            ViewStore store = storeRes.viewStore(storeid);
            if (store.getNCRWSSResultCode() == ResultBase.RES_STORE_NOT_EXIST) {
                result.setNCRWSSResultCode(
                        ResultBase.RES_DPTMT_STORE_NOT_EXIST);
                tp.methodExit(result);
                return result;
            }
        }

        try {
            JsonMarshaller<Department> jsonMarshaller =
                            new JsonMarshaller<Department>();
            Department department = jsonMarshaller.unMarshall(dpt,
                    Department.class);
            String appId = pathName.concat(".create");
            department.setUpdAppId(appId);
            department.setUpdOpeCode(getOpeCode());   
            
            if(StringUtility.isNullOrEmpty(department.getTaxRate())){
	            department.setTaxRate(GlobalConstant.getTaxRate());
            }
            IDepartmentDAO dept = daoFactory.getDepartmentDAO();
            result = dept.createDepartment(storeid, dptid, department);
        } catch (DaoException e) {
            LOGGER.logAlert(this.progname, "createDepartment",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            if (e.getCause() instanceof SQLException) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
             } else {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
             }
        } catch (Exception e) {        	
            LOGGER.logAlert(this.progname, "createDepartment",
                    Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Updates active department.
     *
     * @param retailStoreID - Path parameter for store identifier
     * @param departmentID - Path parameter for department identifier
     * @param jsonDepartment - json string representation of a
     *                          department class
     * @return a JSON string of the Department Details.
     */

        @Path("/maintenance")
        @POST
        @Produces({MediaType.APPLICATION_JSON })
        @ApiOperation(value="部門情報更新", response=ViewDepartment.class)
        @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_DPTMT_NOT_EXIST, message="部門が存在しない"),
            @ApiResponse(code=ResultBase.RES_DPTMT_NOTACTIVE, message="無効な部門"),
        })
        public final ViewDepartment updateDepartment(
          @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailStoreID,
          @ApiParam(name="departmentid", value="部門コード") @FormParam("departmentid") final String departmentID,
          @ApiParam(name="department", value="部門情報(json)") @FormParam("department") final String jsonDepartment) {

            tp.methodEnter("updateDepartment");
            tp.println("RetailStoreID", retailStoreID).println("DepartmentID",
                    departmentID).println("Department", jsonDepartment);

            ViewDepartment resultDept = new ViewDepartment();
            resultDept.setRetailStoreID(retailStoreID);

            try {
                JsonMarshaller<Department> jsonMarshaller = new JsonMarshaller<Department>();
                Department department = jsonMarshaller.unMarshall(jsonDepartment, Department.class);
                String appId = pathName.concat(".maintenance");
                department.setUpdAppId(appId);
                department.setUpdOpeCode(getOpeCode());

                if(StringUtility.isNullOrEmpty(department.getTaxRate())){
                    department.setTaxRate(GlobalConstant.getTaxRate());
                }
                
                IDepartmentDAO iDptDao = daoFactory.getDepartmentDAO();
                resultDept = iDptDao.updateDepartment(retailStoreID, departmentID, department);
            } catch (DaoException daoEx) {
                LOGGER.logAlert("DepartmentRes",
                   "DepartmentResource.updateDepartment",
                   Logger.RES_EXCEP_DAO,
                   "Failed to update the Department Details.\n"
                   + daoEx.getMessage());
               resultDept.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
           } catch (Exception e) {
               LOGGER.logAlert(this.progname,
                       "updateDepartment", Logger.RES_EXCEP_GENERAL,
                       e.getMessage());
               resultDept.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
           } finally {
               tp.methodExit(resultDept);
           }
          return resultDept;
         }
     // 1.01  2014.12.11 LiQian DIV存在チェックを対応   ADD START
        /**
         * Gets the Department info.
         *
         * @param retailStoreID
         *            - Path parameter for store identifier
         * @param departmentID
         *            - Path parameter for department identifier
         * @return a JSON string of the Department Info.
         *
         */

        @Path("/getDepartmentInfo")
        @GET
        @Produces({ MediaType.APPLICATION_JSON })
        @ApiOperation(value="部門情報取得", response=ViewDepartment.class)
        @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
        })
        public final ViewDepartment getDepartmentInfo(
        		@ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String retailStoreID,
        		@ApiParam(name="departmentid", value="部門コード") @QueryParam("departmentid") final String departmentID) {

      	  String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("RetailStoreID", retailStoreID)
              .println("DepartmentID", departmentID);

            ViewDepartment dptInfo = null;

        try {
      	  dptInfo = new ViewDepartment();
      	  if (StringUtility.isNullOrEmpty(retailStoreID, departmentID)){
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                dptInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                dptInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                dptInfo.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return dptInfo;
            }
      	  
            IDepartmentDAO iDptDao = daoFactory.getDepartmentDAO();
            dptInfo = iDptDao.getDepartmentInfo(retailStoreID, departmentID);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    progname,
                    Logger.RES_EXCEP_DAO,
                    functionName+ ": Failed to get the Department Info.",
                    daoEx);
            dptInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            dptInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            dptInfo.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.logAlert(
          		  progname,
                    Logger.RES_EXCEP_GENERAL,
                    functionName+ ": Failed to get the Department Info.",
                    ex);
            dptInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            dptInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            dptInfo.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(dptInfo);
        }
        return dptInfo;
        }
  // 1.01  2014.12.11 LiQian DIV存在チェックを対応   ADD END

        private String getOpeCode(){
            return ((securityContext != null) && (securityContext.getUserPrincipal()) != null) ? securityContext
                    .getUserPrincipal().getName() : null;
        }
}
