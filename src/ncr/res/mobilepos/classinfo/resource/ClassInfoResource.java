/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ClassInfoResource
 *
 * Resource which provides Web Service for ClassInfo details retrieval
 *
 * Romares, Sul
 */

package ncr.res.mobilepos.classinfo.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.sql.SQLException;
import java.util.List;

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

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.classinfo.dao.IClassInfoDAO;
import ncr.res.mobilepos.classinfo.model.ClassInfo;
import ncr.res.mobilepos.classinfo.model.SearchedClassInfo;
import ncr.res.mobilepos.classinfo.model.ViewClassInfo;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.line.model.ViewLine;
import ncr.res.mobilepos.line.resource.LineResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;

/**
 * ClassInfoResource Web Resource Class
 * 
 * <P>
 * Supports MobilePOS ClassInfo processes.
 * 
 */
@Path("/class")
@Api(value="/class", description="クラス関連API")
public class ClassInfoResource {

    /**
     * ServletContext.
     */
    @Context
    private ServletContext context;

    /**
     * SecurityContext.
     */
    @Context
    private SecurityContext securityContext;

    /**
     * @return the context
     */
    public final ServletContext getContext() {
        return context;
    }

    /**
     * @param contextToSet
     *            the context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * The resource DAO factory.
     */
    private DAOFactory sqlServerDAO;
    /**
     * The program name.
     */
    private static final String PROG_NAME = "ClassInfoRes";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    private String pathName = "class";

    /**
     * Default Constructor. Instantiate ioWriter and sqlServerDAO member
     * variables.
     */
    public ClassInfoResource() {
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Sets the DaoFactory of the ClassInfoResource to use the DAO methods.
     * 
     * @param daoFactory
     *            The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daoFactory) {
        this.sqlServerDAO = daoFactory;
    }

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

       
    /**
     * Method called by the Web Service for retrieving a list of ClassInfos in a
     * particular Store. This is used by the Supervisor maintenance. *
     * 
     * @param storeId
     *            The ID of the store which the Class are located
     * @param key
     *            The key used to search the Class by itemClass
     * @param name 
     *            The name is used to search the Class by Name (En,Ja)     
     * @param limit
     *            Search Limit 0 = for SystemConfig.searchMaxResult 
     *                        -1 = search all
     *                         
     * @return The list of ClassInfos having the specified itemClass(key) and Class name or local name
     */
    @Path("/list")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="クラス一覧検索", response=SearchedClassInfo.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final SearchedClassInfo list(
            @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailstoreid,
            @ApiParam(name="departmentid", value="部門コード") @FormParam("departmentid") final String departmentid,
            @ApiParam(name="key", value="検索キー") @FormParam("key") final String key,
            @ApiParam(name="name", value="名前検索値") @FormParam("name") final String name,
            @ApiParam(name="limit", value="結果取得限度") @FormParam("limit") final int limit) {

        String functionName = "ClassInfoResource.list";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        		.println("retailstoreid", retailstoreid)
                .println("departmentid", departmentid)
                .println("key", key)
                .println("name", name)
                .println("limit", limit);

        SearchedClassInfo searchClassInfo = new SearchedClassInfo();
        List<ClassInfo> classInfoList = null;
        try {        	                      
            IClassInfoDAO classInfoDAO = sqlServerDAO.getClassInfoDAO();            
            classInfoList = classInfoDAO.listClasses(retailstoreid, departmentid, key, name, limit);            
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, "ClassInfoResource.List",
                    Logger.RES_EXCEP_DAO, "Failed to get the list of ClassInfos.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                searchClassInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	searchClassInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            searchClassInfo.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            searchClassInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
        	searchClassInfo.setClassInfos(classInfoList);
            tp.methodExit(searchClassInfo);
        }
        return searchClassInfo;
    }  
    
    /**
     * Deletes a ClassInfo.
     * 
     * @param retailstoreid
     *            The storeid of a Class.
     * @param departmentid
     *            The department of a Class.
     * @param lineid
     *            The line of a Class.  
     * @param class
     *            The class id of a Class.         
     * @return resulBase The ResultBase object that contains resultcode.
     */
    @Path("/delete")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="クラス情報削除", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public final ResultBase deleteClass(
            @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailstoreid,
            @ApiParam(name="departmentid", value="部門コード") @FormParam("departmentid") final String departmentid,
            @ApiParam(name="lineid", value="品種コード") @FormParam("lineid") final String lineid,
            @ApiParam(name="classid", value="クラス") @FormParam("classid") final String classid) {

        String functionName = "ClassInfoResource.deleteClass";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("retailstoreid", retailstoreid)
                .println("departmentid", departmentid)
                .println("lineid", lineid)
                .println("classid", classid);

        ResultBase resultBase = null;

        try {
            IClassInfoDAO classInfoDAO = sqlServerDAO.getClassInfoDAO(); 
            resultBase = classInfoDAO.deleteClass(retailstoreid, departmentid, lineid, classid);

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            if (e.getCause() instanceof SQLException) {
                resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
                        e.getMessage());
            } else {
                resultBase = new ResultBase(ResultBase.RES_ERROR_DAO,
                        e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }
    
    /**
     * The Web Method resource for creating an ClassInfo.
     *     
     * @param jsonClass
     *            The JSON representation of the ClassInfo.
     * @return The ResultBase object that contains the resultcode.
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="クラス情報設定", response=DeviceStatus.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_STORE_NOT_EXIST, message="店舗無しエラー"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_DPT_NOT_EXIST, message="部門無しエラー"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_LINE_NOT_EXIST, message="品種無しエラー"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_ALREADY_EXIST, message="重複エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="引数無効")
    })
    public final ResultBase createClassInfo(
            @ApiParam(name="class", value="クラス") @FormParam("class") final String jsonClass) {

        String functionname = "ClassInfoResource.createClassInfo";
        tp.methodEnter(DebugLogger.getCurrentMethodName())              
                .println("class", jsonClass);
        ResultBase resultbase = new ResultBase();        

        try {
        	
            String appId = pathName.concat(".create");
			if (StringUtility.isNullOrEmpty(jsonClass)) {
				resultbase
						.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is null or empty.");
				return resultbase;
			}            
            JsonMarshaller<ClassInfo> classJsonMarshaller = new JsonMarshaller<ClassInfo>();
            ClassInfo classInfo = classJsonMarshaller.unMarshall(jsonClass, ClassInfo.class);
            classInfo.setUpdAppId(appId);
            classInfo.setUpdOpeCode(getOpeCode());            
           
			if (classInfo == null
					|| StringUtility.isNullOrEmpty(classInfo.getLine(),
							classInfo.getRetailStoreId(),
							classInfo.getDepartment())) {
				resultbase
						.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is null or empty.");
				return resultbase;
			}

            if (!isEnterpriseStore(classInfo.getRetailStoreId())) {
                StoreResource storeRes = new StoreResource();
                ViewStore store = storeRes.viewStore(classInfo.getRetailStoreId());
                if (store.getNCRWSSResultCode() != ResultBase.RES_OK) {
                	if (store.getNCRWSSResultCode() == ResultBase.RES_STORE_NOT_EXIST) {
                		resultbase.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_STORE_NOT_EXIST);
                		tp.println("Class' RetailStoreID did not exist.");
                	}else{
                		resultbase.setNCRWSSResultCode(store.getNCRWSSResultCode());
                		tp.println("Error occured in searching for the store.");
                	} 
                    return resultbase;
                }                
            }            
            DepartmentResource deptRes = new DepartmentResource();
            ViewDepartment viewDept = deptRes.selectDepartmentDetail(classInfo.getCompanyId(),
                    classInfo.getRetailStoreId(), classInfo.getDepartment());
            if (viewDept.getNCRWSSResultCode() != ResultBase.RES_DPTMT_OK) {
                resultbase
                        .setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_DPT_NOT_EXIST);
                tp.println("Class' DepartmentID did not exist.");
                return resultbase;
            }             
            LineResource lineRes = new LineResource();
            ViewLine viewLine = lineRes.selectLineDetail(
                    classInfo.getRetailStoreId(), classInfo.getDepartment(), classInfo.getLine());
            
            if (viewLine.getNCRWSSResultCode() != ResultBase.RES_OK) {
		        if (viewLine.getNCRWSSResultCode() == ResultBase.RES_LINE_INFO_NOT_EXIST) {
		            resultbase.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_LINE_NOT_EXIST);
		            tp.println("Class' Line did not exist.");
		            
		        }else{
		        	viewLine.setNCRWSSResultCode(viewLine.getNCRWSSResultCode());
            		tp.println("Error occured in searching for the line in classinfo.");
            	}    
		        return resultbase;            
            }         
            if(StringUtility.isNullOrEmpty(classInfo.getTaxRate())){
	            classInfo.setTaxRate(GlobalConstant.getTaxRate());
            }    
            IClassInfoDAO classInfoDAO = sqlServerDAO.getClassInfoDAO();
            resultbase = classInfoDAO.createClassInfo(classInfo);
            
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            if (e.getCause() instanceof SQLException) {
                int errorcode = ((SQLException) e.getCause()).getErrorCode();
                // Is the exception caused by ROW Duplicate.
                if (errorcode == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                    errorcode = ResultBase.RES_CLASS_INFO_ALREADY_EXIST;
                    tp.println("Class is duplicated.");
                } else {
                    errorcode = ResultBase.RES_ERROR_DB;
                }
                resultbase.setNCRWSSResultCode(errorcode);
                resultbase.setMessage(e.getMessage());
            } else {
                resultbase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                resultbase.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            resultbase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            resultbase.setMessage(e.getMessage());
        } finally {
            tp.methodExit(resultbase);
        }
        return resultbase;
    }
    
    /**
     * The Web Method called to update ClassInfo.
     * 
     * @param retailStoreId
     *            The Retail Store ID.
     * @param departmentid
     *            The ClassInfo department.
     * @param lineid
     *            The line ID.
     * @param classid
     *            The ClassInfo ID.
     * @param jsonClass
     *            The JSON representation of ClassInfo.
     * @return The Json representation of the updated ClassInfo.
     */
    @Path("/maintenance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="クラス情報更新", response=DeviceStatus.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="引数無効"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_NOT_EXIST, message="クラス無効"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_ALREADY_EXIST, message="クラス重複"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_STORE_NOT_EXIST, message="店舗コード無効"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_DPT_NOT_EXIST, message="部門コード無効"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_LINE_NOT_EXIST, message="品種コード無効"),
            @ApiResponse(code=ResultBase.RES_CLASS_INFO_NOT_UPDATED, message="更新失敗")
    })
    public final ViewClassInfo updateClassInfo(
            @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailStoreId,
            @ApiParam(name="departmentid", value="部門コード") @FormParam("departmentid") final String departmentid,
            @ApiParam(name="lineid", value="品種コード") @FormParam("lineid") final String lineid,
            @ApiParam(name="classid", value="クラスコード") @FormParam("classid") final String classid,
            @ApiParam(name="class", value="クラス情報") @FormParam("class") final String jsonClass) {

        String functionname = "ClassInfoResource.updateClass";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("RetailStoreID", retailStoreId)
                .println("departmentid", departmentid)     
                .println("lineid", lineid)     
                .println("classid", classid)     
                .println("class", jsonClass);           
        
        ViewClassInfo viewClassInfo = new ViewClassInfo();   
         
        try { 
        	
        	if (StringUtility.isNullOrEmpty(jsonClass)){
        		viewClassInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);                      
                tp.println("Parameter[s] is null or empty.");               
                return viewClassInfo;
        	}
        	
            JsonMarshaller<ClassInfo> classInfoJsonMarshaller = new JsonMarshaller<ClassInfo>();
            ClassInfo classInfo = classInfoJsonMarshaller.unMarshall(jsonClass, ClassInfo.class);
            String appId = pathName.concat(".maintenance");
            classInfo.setUpdAppId(appId);
            classInfo.setUpdOpeCode(getOpeCode());            
            IClassInfoDAO classInfoDAO = sqlServerDAO.getClassInfoDAO(); 
           
			if (classInfo == null
					|| StringUtility.isNullOrEmpty(classInfo.getLine(), classInfo.getItemClass(),
							classInfo.getRetailStoreId(), classInfo.getDepartment(),
							classid, lineid, retailStoreId, departmentid)) {
				viewClassInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);                      
                tp.println("Parameter[s] is null or empty.");               
                return viewClassInfo;
            }			 
			
			ViewClassInfo viewClasInfoTemp = this.selectClassInfoDetail(retailStoreId, departmentid, lineid, classid);
            if( viewClasInfoTemp == null || viewClasInfoTemp.getNCRWSSResultCode() == ResultBase.RES_CLASS_INFO_NOT_EXIST){
            	viewClassInfo.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_NOT_EXIST);
                tp.println("ClassInfo not exist.");
                    return viewClassInfo;
            }
            if(!retailStoreId.equals(classInfo.getRetailStoreId()) || !classid.equals(classInfo.getItemClass()) || !lineid.equals(classInfo.getLine()) || !departmentid.equals(classInfo.getDepartment())){
            	viewClasInfoTemp = new ViewClassInfo();
            	viewClasInfoTemp = this.selectClassInfoDetail(classInfo.getRetailStoreId(), classInfo.getDepartment(), classInfo.getLine(), classInfo.getItemClass());
                if (viewClasInfoTemp != null && viewClasInfoTemp.getNCRWSSResultCode() == ResultBase.RES_CLASS_INFO_ALREADY_EXIST) {
                    viewClassInfo.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_ALREADY_EXIST);
                    return viewClassInfo;
                }
            } 
            if (!isEnterpriseStore(classInfo.getRetailStoreId())) {
                StoreResource storeRes = new StoreResource();
                ViewStore store = storeRes.viewStore(classInfo.getRetailStoreId());                
                if (store.getNCRWSSResultCode() != ResultBase.RES_OK) {
                	if (store.getNCRWSSResultCode() == ResultBase.RES_STORE_NOT_EXIST) {
                		viewClassInfo.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_STORE_NOT_EXIST);
                		tp.println("ClassInfo RetailStoreID did not exist.");
                	}else{
                		viewClassInfo.setNCRWSSResultCode(store.getNCRWSSResultCode());
                		tp.println("Error occured in searching for the store.");
                	} 
                    return viewClassInfo;
                }
            } 
            DepartmentResource deptRes = new DepartmentResource();
            ViewDepartment viewDept = deptRes.selectDepartmentDetail(classInfo.getCompanyId(),
                    classInfo.getRetailStoreId(), classInfo.getDepartment());
            if (viewDept.getNCRWSSResultCode() != ResultBase.RES_DPTMT_OK) {
            	viewClassInfo.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_DPT_NOT_EXIST);
                tp.println("ClassInfo DepartmentID did not exist.");
                return viewClassInfo;
            }                
            LineResource lineRes = new LineResource();
            ViewLine viewLine = lineRes.selectLineDetail(
                    classInfo.getRetailStoreId(), classInfo.getDepartment(), classInfo.getLine());            
            
            if (viewLine.getNCRWSSResultCode() != ResultBase.RES_OK) {
            	if (viewLine.getNCRWSSResultCode() == ResultBase.RES_LINE_INFO_NOT_EXIST) {
            		viewClassInfo.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_LINE_NOT_EXIST);
            		tp.println("Class' Line did not exist.");
            	}else{
            		viewClassInfo.setNCRWSSResultCode(viewLine.getNCRWSSResultCode());
            		tp.println("Error occured in searching for the line in classinfo.");
            	} 
                return viewClassInfo;
            }             
            
            if(StringUtility.isNullOrEmpty(classInfo.getTaxRate())){
	            classInfo.setTaxRate(GlobalConstant.getTaxRate());
            } 
            
            ClassInfo updatedClassInfo;
            ViewClassInfo updatedViewClassInfo = classInfoDAO.updateClassInfo(retailStoreId, departmentid, lineid, classid, classInfo);
            if (null == updatedViewClassInfo || null == updatedViewClassInfo.getClassInfo() || updatedViewClassInfo.getNCRWSSResultCode() != ResultBase.RES_OK) {
                viewClassInfo.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_NOT_UPDATED);
                updatedClassInfo = new ClassInfo();
                updatedClassInfo.setLine(lineid);
                updatedClassInfo.setItemClass(classid);
                updatedClassInfo.setRetailStoreId(retailStoreId);
                updatedClassInfo.setDepartment(departmentid);
                tp.println("ClassInfo not updated.");
            }else{ 
            	updatedClassInfo = updatedViewClassInfo.getClassInfo();
            }           
                      
            viewClassInfo.setClassInfo(updatedClassInfo);
        } catch (DaoException e) {           
            Throwable cause = e.getCause();
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_DAO,
                    cause.getMessage());
            if (cause instanceof SQLException) {
                int errorcode = ((SQLException) cause).getErrorCode();
                // Is the exception caused by ROW Duplicate.
                if (errorcode == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                    errorcode = ResultBase.RES_CLASS_INFO_ALREADY_EXIST;
                    tp.println("ClassInfo update duplicated.");
                } else {
                    errorcode = ResultBase.RES_ERROR_DB;
                }
                viewClassInfo.setMessage(cause.getMessage());
                viewClassInfo.setNCRWSSResultCode(errorcode);
            } else {
                viewClassInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                viewClassInfo.setMessage(cause.getMessage());
            }
        } catch (Exception e) {            
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            viewClassInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            viewClassInfo.setMessage(e.getMessage());
        } finally {
            tp.methodExit(viewClassInfo);
        }

        return viewClassInfo;
    }
    
    /**
     * Retrieves active department.
     *
     * @param retailStoreID
     *            - Path parameter for store identifier
     * @param departmentid
     *            - Path parameter for department identifier
     * @return a JSON string of the Department Details.
     */
    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="クラス情報取得", response=ViewClassInfo.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final ViewClassInfo selectClassInfoDetail(
            @ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String retailStoreID,
            @ApiParam(name="departmentid", value="部門コード") @QueryParam("departmentid") final String departmentid,
            @ApiParam(name="lineid", value="品種コード") @QueryParam("lineid") final String lineid,
            @ApiParam(name="classid", value="クラスコード") @QueryParam("classid") final String classid) {

        tp.methodEnter("selectClassInfoDetail");
        tp.println("retailStoreID", retailStoreID)
        		.println("departmentid", departmentid)
        		.println("lineid", lineid)
                .println("classid", classid);
                
        ViewClassInfo classInfoModel = new ViewClassInfo();
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));

        String functionName = PROG_NAME+".selectClassInfoDetail";
		try {
            IClassInfoDAO iClassInfoDAO = sqlServerDAO.getClassInfoDAO();
            classInfoModel = iClassInfoDAO.selectClassInfoDetail(retailStoreID, departmentid, lineid, classid);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to get the Classinfo Details.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                  classInfoModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                  classInfoModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
            		PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to view ClassInfo : "
                            + ex.getMessage());
            classInfoModel.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(classInfoModel);
        }
        return classInfoModel;
    }
    
    
    
    private String getOpeCode() {
        return ((securityContext != null) && (securityContext
                .getUserPrincipal()) != null) ? securityContext
                .getUserPrincipal().getName() : null;
    }
    /**
     * Checks if storeid is an enterprise store(0).
     * 
     * @param storeID
     *            to check.
     * @return true if an enterprise storeid, false if not.
     */
    private boolean isEnterpriseStore(final String storeID) {
        return null != storeID && !storeID.isEmpty() && 
        		"0".equals(storeID.trim());
    }
    
    
}
