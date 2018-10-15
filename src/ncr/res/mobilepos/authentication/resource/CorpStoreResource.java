/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * CorpStoreResource
 *
 * Is a Resource Class for CorpStore maintenance services.
 *
 */

package ncr.res.mobilepos.authentication.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.io.IOException;
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

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.authentication.dao.ICorpStoreDAO;
import ncr.res.mobilepos.authentication.model.CorpStore;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * CorpStore resource.
 * @author AP185142
 */
@Path("/registration/corpstore")
@Api(value="/registration/corpstore", description="端末/設備の登録のサービスAPI")
public class CorpStoreResource {
	/**
     * the servelet context.
     */
    @Context
    private ServletContext context;
    /**
     * the instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * the instance of the dao factory.
     */
    private DAOFactory daoFactory;
    /**
     * variable that holds the class codename.
     */
    private String progName = "CStRsc";

    /**
     * constructor.
     */
    public CorpStoreResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }


    /**
     * Service to create a CorpStore for CorpStore Table Maintenance.
     * @param companyID company id
     * @param retailStoreID - Store number
     * @param store - Store
     * @return ResultBase
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="店舗パスコード登録", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_CORPSTORE_INVALID_PARAM, message="無効なパラメータ"),
    })
    public final ResultBase createCorpStore(
            @ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyID,
            @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailStoreID,
            @ApiParam(name="store", value="店舗情報") @FormParam("store") final String store) {

        tp.methodEnter("createCorpStore");
        tp.println("CompanyID", companyID).
        println("RetailStoreID", retailStoreID).println("Store", store);

        ResultBase result = null;

        if (StringUtility.isNullOrEmpty(companyID, retailStoreID, store)) {
            tp.println("a parameter is invalid");
            result = new DeviceStatus(
                    ResultBase.RES_CORPSTORE_INVALID_PARAM,
                    "Invalid parameter");
            return result;
        }

         try {
             JsonMarshaller<CorpStore> jsonMarshaller =
                   new JsonMarshaller<CorpStore>();
             CorpStore cStore = jsonMarshaller.unMarshall(store,
                             CorpStore.class);
             if (StringUtility.isNullOrEmpty(
                     cStore.getPasscode(), cStore.getStorename())) {
                 tp.println("a parameter is invalid");
                 result = new DeviceStatus(
                         ResultBase.RES_CORPSTORE_INVALID_PARAM,
                         "Invalid parameter");
                 return result;
             }
             if (cStore.getPermission() > 1 || cStore.getPermission() < 0) {
                 tp.println("permission cannot have value aside from 1 and 0");
                 result = new DeviceStatus(
                         ResultBase.RES_CORPSTORE_INVALID_PARAM,
                         "Invalid parameter");
                 return result;
             }

             ICorpStoreDAO corpstoredao = daoFactory.getCorpStoreDAO();
             result = corpstoredao.createCorpStore(
                         companyID, retailStoreID, cStore);

         } catch (DaoException e) {
             LOGGER.logAlert("CorpStoreResource",
                     "createCorpStore", Logger.RES_EXCEP_DAO, e.getMessage());
             result = new ResultBase(
                     ResultBase.RES_ERROR_DB, e.getMessage());
         } catch (Exception e) {
             LOGGER.logAlert("CorpStoreResource",
                     "createCorpStore", Logger.RES_EXCEP_GENERAL,
                     e.getMessage());
             result = new ResultBase(
                     ResultBase.RES_ERROR_GENERAL, e.getMessage());
         } finally {
              tp.methodExit(result);
         }
       return result;
    }

    /**
     * Service to view store details of given parameter storeid.
     *
     * @param companyID the company id
     * @param retailStoreID
     *            storeid to lookup.
     * @return JSON type of Store.
     */
    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="店舗パスコード詳細取得", response=ViewCorpStore.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final ViewCorpStore viewCorpStore(
            @ApiParam(name="companyid", value="会社コード") @QueryParam("companyid") final String companyID,
            @ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String retailStoreID) {

        String functionName = "StoreResource.viewStore";

        tp.methodEnter("viewStore");
        tp.println("RetailStoreID", retailStoreID);
        ViewCorpStore store = new ViewCorpStore();

        try {

            ICorpStoreDAO storeDAO = daoFactory.getCorpStoreDAO();
            store = storeDAO.viewCorpStore(companyID, retailStoreID);

        } catch (DaoException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to view Store# " + retailStoreID + ": "
                            + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                store.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                store.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to view CorpStore# " + retailStoreID + ": "
                            + ex.getMessage());
            store.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(store.toString());
        }

        return store;
    }

    /**
     * Service to delete corpstore of given parameter
     * companyid and storeid from database.
     *
     * @param companyID
     *            companyid to lookup
     * @param retailStoreID
     *            storeid to lookup.
     * @return JSON type of Store.
     */
    @Path("/delete")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="店舗パスコード削除", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final ResultBase deleteCorpStore(
            @ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyID,
            @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailStoreID) {

        String functionName = "CorpStoreResource.deleteCorpStore";

        tp.methodEnter("deleteCorpStore");
        tp.println("RetailStoreID", retailStoreID)
        .println("CompanyID", companyID);

        ResultBase result = new ResultBase();

        try {

            ICorpStoreDAO corpstoreDAO = daoFactory.getCorpStoreDAO();
            result = corpstoreDAO.deleteCorpStore(companyID, retailStoreID);

        } catch (DaoException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to delete CorpStore# " + retailStoreID + ": "
                            + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to delete CorpStore# " + retailStoreID + ": "
                            + ex.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Web Method called to update a corpstore.
     * @param companyid The company id
     * @param storeid The retail Store id
     * @param storeJson The new values for store.
     * @return the updated corpstore data.
     */
    @Path("/maintenance")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="店舗パスコード登録", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_CORPSTORE_INVALID_PARAM, message="無効なパラメータ"),
    })
    public final ViewCorpStore updateCorpStore(
            @ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyid,
            @ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String storeid,
            @ApiParam(name="store", value="店舗情報") @FormParam("store") final String storeJson) {
        String functionName = "StoreResource.updateStores";

        tp.methodEnter("updateCorpStore");
        tp.println("RetailStoreID", storeid).println("Store", storeJson)
            .println("CompanyID", companyid);

        ViewCorpStore viewStore = new ViewCorpStore();

        if (StringUtility.isNullOrEmpty(storeJson)) {
            viewStore.setCorpstore(new CorpStore());
            viewStore.setNCRWSSResultCode(
                    ResultBase.RES_CORPSTORE_INVALID_PARAM);
            tp.println("json is either null or empty");
            return viewStore;
        }

        try {
            JsonMarshaller<CorpStore> storeJsonMarshaller =
                new JsonMarshaller<CorpStore>();

            CorpStore store =
                storeJsonMarshaller.unMarshall(storeJson, CorpStore.class);
            ICorpStoreDAO storeDao = daoFactory.getCorpStoreDAO();
            viewStore = storeDao.updateCorpStore(companyid, storeid, store);
        } catch (DaoException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to update CorpStore with Store# "
                            + storeid + ":"
                            + ex.getMessage());

            if (ex.getCause() instanceof SQLException) {
                viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (IOException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update CorpStore with Store# "
                        + storeid + ":"
                        + ex.getMessage());
            viewStore.setNCRWSSResultCode(
                    ResultBase.RES_CORPSTORE_INVALID_PARAM);
            viewStore.setCorpstore(new CorpStore());
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update CorpStore with Store# "
                        + storeid + ":"
                        + ex.getMessage());
            viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewStore);
        }
        return viewStore;
    }
}
