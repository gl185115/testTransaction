/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * LineResource
 *
 * Resource which provides Web Service for Line details retrieval
 *
 * Romares, Sul
 */

package ncr.res.mobilepos.line.resource;

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

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
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
import ncr.res.mobilepos.line.dao.ILineDAO;
import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.line.model.SearchedLine;
import ncr.res.mobilepos.line.model.ViewLine;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;
import ncr.res.mobilepos.xebioapi.model.JSONData;

/**
 * LineResource Web Resource Line
 * 
 * <P>
 * Supports MobilePOS Line processes.
 * 
 */
@Path("/line")
@Api(value="/line", description="品種情報API")
public class LineResource {

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
    private static final String PROG_NAME = "LineRes";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    private String pathName = "line";

    /**
     * Default Constructor. Instantiate ioWriter and sqlServerDAO member
     * variables.
     */
    public LineResource() {
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Sets the DaoFactory of the LineResource to use the DAO methods.
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
     * Method called by the Web Service for retrieving a list of Lines in a
     * particular Store. This is used by the Supervisor maintenance. *
     * 
     * @param retailstoreid
     *            The ID of the store which the Line are located
     * @param key
     *            The key used to search the Line by line
     * @param name 
     *            The name is used to search the Line by Name (En,Jp)     
     * @param limit
     *            Search Limit 0 = for SystemConfig.searchMaxResult 
     *                        -1 = search all
     *                         
     * @return The list of Lines having the specified Line(key) and Line name or local name
     */
    @Path("/list")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="品種情報一覧取得", response=SearchedLine.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
    })
    public final SearchedLine list(
    		@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailstoreid,
    		@ApiParam(name="departmentid", value=" 部門コード") @FormParam("departmentid") final String departmentid,
    		@ApiParam(name="key", value="品種コード") @FormParam("key") final String key,
    		@ApiParam(name="name", value="品種名称") @FormParam("name") final String name,
    		@ApiParam(name="limit", value="制限条目") @FormParam("limit") final int limit) {

        String functionName = "LineResource.list";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("retailstoreid", retailstoreid)
                .println("departmentid", departmentid)
                .println("key", key)
                .println("name", name)
                .println("limit", limit);

        SearchedLine searchLine = new SearchedLine();
        List<Line> lineList = null;
        try {        	                      
            ILineDAO lineDAO = sqlServerDAO.getLineDAO();            
            lineList = lineDAO.listLines(retailstoreid, departmentid, key, name, limit);            
        } catch (DaoException daoEx) {           
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_DAO, "Failed to get the list of Lines.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                searchLine.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	searchLine.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            searchLine.setMessage(daoEx.getMessage());
        } catch (Exception ex) {          
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            searchLine.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
        	searchLine.setLines(lineList);
            tp.methodExit(searchLine);
        }
        return searchLine;
    } 
    
    
    /**
     * Deletes a Line.
     * 
     * @param retailstoreid
     *            The storeid of a line.
     * @param lineid
     *            The line id of a line.
     * @param departmentid
     *            The department of a line.
     * @return resulBase The ResultBase object that contains resultcode.
     */
    @Path("/delete")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase deleteLine(
            @FormParam("retailstoreid") final String retailstoreid,
            @FormParam("departmentid") final String departmentid,
            @FormParam("lineid") final String lineid) {

        String functionName = "LineResource.deleteLine";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("retailstoreid", retailstoreid)
                .println("departmentid", departmentid)
                .println("lineid", lineid);

        ResultBase resultBase = null;

        try {
            ILineDAO lineDAO = sqlServerDAO.getLineDAO(); 
            resultBase = lineDAO.deleteLine(retailstoreid, departmentid,lineid);
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
     * The Web Method resource for creating an Line.
     *     
     * @param jsonLine
     *            The JSON representation of the line.
     * @return The ResultBase object that contains the resultcode.
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase createLine(
    		@FormParam("line") final String jsonLine) {

        String functionname = "LineResource.createLine";
        tp.methodEnter(DebugLogger.getCurrentMethodName())              
                .println("line", jsonLine);
        ResultBase resultbase = new ResultBase();        

        try {
            String appId = pathName.concat(".create");
			if (StringUtility.isNullOrEmpty(jsonLine)) {
				resultbase
						.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is null or empty.");				
				return resultbase;
			}
            
            JsonMarshaller<Line> lineJsonMarshaller = new JsonMarshaller<Line>();
            Line line = lineJsonMarshaller.unMarshall(jsonLine, Line.class);
            line.setUpdAppId(appId);
            line.setUpdOpeCode(getOpeCode());
			if (line == null
					|| StringUtility.isNullOrEmpty(line.getLine(),
							line.getRetailStoreId(), line.getDepartment())) {
				resultbase
						.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is null or empty.");				
				return resultbase;
			}
            if (!isEnterpriseStore(line.getRetailStoreId())) {
                StoreResource storeRes = new StoreResource();
                ViewStore store = storeRes.viewStore(line.getRetailStoreId());
                if (store.getNCRWSSResultCode() != ResultBase.RES_OK) {                	
	                if (store.getNCRWSSResultCode() == ResultBase.RES_STORE_NOT_EXIST) {
	                    resultbase.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_STORE_NOT_EXIST);
	                    tp.println("Line's RetailStoreID did not exist.");
	                }else{    
	                	resultbase.setNCRWSSResultCode(store.getNCRWSSResultCode());
                		tp.println("Error occured in searching for the store.");
	                }	              
                    return resultbase;
                }
            }            
            DepartmentResource deptRes = new DepartmentResource();
            ViewDepartment viewDept = deptRes.selectDepartmentDetail(line.getCompanyId(),
                    line.getRetailStoreId(), line.getDepartment());
            if (viewDept.getNCRWSSResultCode() != ResultBase.RES_DPTMT_OK) {
                resultbase
                        .setNCRWSSResultCode(ResultBase.RES_LINE_INFO_DPT_NOT_EXIST);
                tp.println("Line's DepartmentID did not exist.");
                return resultbase;
            }          
            if(StringUtility.isNullOrEmpty(line.getTaxRate())){
                line.setTaxRate(GlobalConstant.getTaxRate());
            }
            ILineDAO lineDAO = sqlServerDAO.getLineDAO();
            resultbase = lineDAO.createLine(line);
            
        } catch (DaoException e) {            
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            if (e.getCause() instanceof SQLException) {
                int errorcode = ((SQLException) e.getCause()).getErrorCode();
                // Is the exception caused by ROW Duplicate.
                if (errorcode == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                    errorcode = ResultBase.RES_LINE_INFO_ALREADY_EXIST;
                    tp.println("Line is duplicated.");
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
    public final ViewLine selectLineDetail(
            @QueryParam("retailstoreid") final String retailStoreID,
            @QueryParam("departmentid") final String departmentid,
            @QueryParam("lineid") final String lineid) {

    	String functionName = "LineResource.selectLineDetail";
        tp.methodEnter(functionName);
        tp.println("retailstoreid", retailStoreID)
        		.println("departmentid", departmentid)
                .println("lineid", lineid);
                
        ViewLine lineModel = new ViewLine();
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));

		try {
            ILineDAO iLineDAO = sqlServerDAO.getLineDAO();
            lineModel = iLineDAO.selectLineDetail(retailStoreID, departmentid, lineid);
        } catch (DaoException daoEx) {         
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to get the Line Details.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                  lineModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                  lineModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {          
            LOGGER.logAlert(
            		PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to view Line " + lineid + ": "
                            + ex.getMessage());
            lineModel.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(lineModel);
        }
        return lineModel;
    }
    
    /**
     * The Web Method called to update Line.
     * 
     * @param retailStoreId
     *            The Retail Store ID.
     * @param departmentid
     *            The line department.
     * @param lineid
     *            The line ID.
     * @param jsonLine
     *            The JSON representation of Line.
     * @return The Json representation of the updated line.
     */
    @Path("/maintenance")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public final ViewLine updateLine(
            @FormParam("retailstoreid") final String retailStoreId,
            @FormParam("departmentid") final String departmentid,
            @FormParam("lineid") final String lineid,
            @FormParam("line") final String jsonLine) {

        String functionname = "LineResource.updateLine";
        tp.methodEnter(functionname)
                .println("retailstoreid", retailStoreId)
                .println("departmentid", departmentid)     
                .println("lineid", lineid)     
                .println("line", jsonLine);           
        
        ViewLine viewLine = new ViewLine();   
         
        try { 
        	if(StringUtility.isNullOrEmpty(jsonLine)){
        		viewLine.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);                      
                tp.println("Parameter[s] is null or empty.");               
                return viewLine;
        	}
        	
            JsonMarshaller<Line> lineJsonMarshaller = new JsonMarshaller<Line>();
            Line line = lineJsonMarshaller.unMarshall(jsonLine, Line.class);
            String appId = pathName.concat(".maintenance");
            line.setUpdAppId(appId);
            line.setUpdOpeCode(getOpeCode());
            ILineDAO lineDAO = sqlServerDAO.getLineDAO(); 
           
			if (line == null
					|| StringUtility.isNullOrEmpty(line.getLine(),
							line.getRetailStoreId(), line.getDepartment(),
							lineid, retailStoreId, departmentid)) {
				viewLine.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);                      
                tp.println("Parameter[s] is null or empty.");                
                return viewLine;
            }			
			
			ViewLine viewLineTemp = this.selectLineDetail(retailStoreId, departmentid, lineid);           
			if( viewLineTemp == null || viewLineTemp.getNCRWSSResultCode() == ResultBase.RES_LINE_INFO_NOT_EXIST){
            	viewLine.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_NOT_EXIST);
                tp.println("Line not exist.");
                    return viewLine;
            }
            if(!retailStoreId.equals(line.getRetailStoreId()) || !lineid.equals(line.getLine()) || !departmentid.equals(line.getDepartment())){
            	viewLineTemp = new ViewLine();
            	viewLineTemp = this.selectLineDetail(line.getRetailStoreId(), line.getDepartment(), line.getLine());
                if (viewLineTemp != null && viewLineTemp.getNCRWSSResultCode() == ResultBase.RES_LINE_INFO_ALREADY_EXIST) {
                    viewLine.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_ALREADY_EXIST);
                    return viewLine;
                }
            } 
            if (!isEnterpriseStore(line.getRetailStoreId())) {
                StoreResource storeRes = new StoreResource();
                ViewStore store = storeRes.viewStore(line.getRetailStoreId());                 
                if (store.getNCRWSSResultCode() != ResultBase.RES_OK) {                	
	                if (store.getNCRWSSResultCode() == ResultBase.RES_STORE_NOT_EXIST) {
	                    viewLine.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_STORE_NOT_EXIST);
	                    tp.println("Line's RetailStoreID did not exist.");
	                }else{    
	                	viewLine.setNCRWSSResultCode(store.getNCRWSSResultCode());
                		tp.println("Error occured in searching for the store.");
	                }	              
                    return viewLine;
                }               
            } 
            DepartmentResource deptRes = new DepartmentResource();
            ViewDepartment viewDept = deptRes.selectDepartmentDetail(line.getCompanyId(),
                    line.getRetailStoreId(), line.getDepartment());
            if (viewDept.getNCRWSSResultCode() != ResultBase.RES_DPTMT_OK) {
            	viewLine.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_DPT_NOT_EXIST);
                tp.println("Line's DepartmentID did not exist.");
                return viewLine;
            }            
            
            if(StringUtility.isNullOrEmpty(line.getTaxRate())){
                line.setTaxRate(GlobalConstant.getTaxRate());
            }
            
            Line updatedLine;
            ViewLine updatedViewLine = lineDAO.updateLine(retailStoreId, departmentid, lineid, line);
            if (null == updatedViewLine || null == updatedViewLine.getLine() || updatedViewLine.getNCRWSSResultCode() != ResultBase.RES_OK) {
                viewLine.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_NOT_UPDATED);
                updatedLine = new Line();
                updatedLine.setLine(lineid);
                updatedLine.setRetailStoreId(retailStoreId);
                updatedLine.setDepartment(departmentid);
                tp.println("Line not updated.");
            }else{
            	updatedLine = updatedViewLine.getLine();
            }
            viewLine.setLine(updatedLine);
        } catch (DaoException e) {           
            Throwable cause = e.getCause();
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_DAO,
                    cause.getMessage());
            if (cause instanceof SQLException) {
                int errorcode = ((SQLException) cause).getErrorCode();
                // Is the exception caused by ROW Duplicate.
                if (errorcode == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                    errorcode = ResultBase.RES_LINE_INFO_ALREADY_EXIST;
                    tp.println("Line update duplicated.");
                } else {
                    errorcode = ResultBase.RES_ERROR_DB;
                }
                viewLine.setMessage(cause.getMessage());
                viewLine.setNCRWSSResultCode(errorcode);
            } else {
                viewLine.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                viewLine.setMessage(cause.getMessage());
            }
        } catch (Exception e) {            
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            viewLine.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            viewLine.setMessage(e.getMessage());
        } finally {
            tp.methodExit(viewLine);
        }

        return viewLine;
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
