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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.line.dao.ILineDAO;
import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.line.model.SearchedLine;
import ncr.res.mobilepos.model.ResultBase;

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
    
}
