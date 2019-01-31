/*
  * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * StoreResource
 *
 * Is a Resource Class for Store maintenance services.
 *
 */

package ncr.res.mobilepos.store.resource;

import java.sql.SQLException;
import java.util.List;

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
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.journalization.model.JSONData;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.dao.IStoreDAO;
import ncr.res.mobilepos.store.model.CMPresetInfo;
import ncr.res.mobilepos.store.model.CMPresetInfos;
import ncr.res.mobilepos.store.model.PresetSroreInfo;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.model.StoreInfo;
import ncr.res.mobilepos.store.model.Stores;
import ncr.res.mobilepos.store.model.ViewStore;

/**
 * 
 * @author AP185142
 * @author cc185102 - (July 10, 2012) Added List Store Web Method - (July 30,
 *         2012) Updated List Store Web Method. Added partial Search prefix when
 *         StoreID and StoreName
 * @author RD185102
 */
@Path("/storeinfo")
@Api(value="/storeinfo", description="ストア情報API")
public class StoreResource {
    /**
     * the servelet context.
     */
    @Context
    private SecurityContext context;
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
    private String progName = "StrRsc";
    /**
     * constructor.
     */
    public StoreResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Service to view store details of given parameter storeid.
     * 
     * @param retailStoreID
     *            storeid to lookup.
     * @return JSON type of Store.
     */
    @Path("/getstoredetailinfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="店舗情報取得", response=ViewStore.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="店舗はデータベースにみつからない"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ViewStore getStoreDetailInfo(
    		@ApiParam(name="retailstoreid", value="店舗コード")@QueryParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="companyId", value="会社コード")@QueryParam("companyId") final String companyId) {

        String functionName = "StoreResource.getStoreDetailInfo";

        tp.methodEnter("getStoreDetailInfo");
        tp.println("RetailStoreID", retailStoreID).println("CompanyId",companyId);
        ViewStore store = new ViewStore();

        try {

            IStoreDAO storeDAO = daoFactory.getStoreDAO();
            store = storeDAO.getStoreDetaiInfo(retailStoreID, companyId);

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
                    "Failed to view Store# " + retailStoreID + ": "
                            + ex.getMessage());
            store.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(store.toString());
        }

        return store;
    }
    
    /**
     * Web Method called to list all stores within a company.
     * 
     * @param key
     *            The key of the Store(s) to be search.
     * @return The Stores JSON Object containing the list of stores.
     */
    @Path("/list")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="店舗情報検索", response=Stores.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final Stores searchStores(
    		@ApiParam(name="companyId", value="会社コード")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="key", value="キー")@QueryParam("key") final String key, 
    		@ApiParam(name="name", value="名")@QueryParam("name") final String name,
    		@ApiParam(name="limit", value="期限")@QueryParam("limit") final int searchLimit) {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("searchStores")
        	.println("companyId", companyId)
        	.println("key", key)
        	.println("name", name)
        	.println("limit", searchLimit);
        Stores stores = new Stores();

        try {
            IStoreDAO storeDao = daoFactory.getStoreDAO();            
            List<Store> storeList = storeDao.listStores(companyId, key, name, searchLimit);
            stores.setStorelist(storeList);
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to search Stores: " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                stores.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	stores.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to search Stores: " + ex.getMessage());
            stores.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(stores);
        }
        return stores;
    }

    /**
     * Web Method called to list all stores within a company.
     * 
     * @param key
     *            The key of the Store(s) to be search.
     * @return The Stores JSON Object containing the list of stores.
     */
    @Path("/presetcminfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="レシート広告情報取得", response=CMPresetInfos.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final CMPresetInfos getCMPresetInfoList(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="店舗コード")@QueryParam("storeid") final String storeId, 
    		@ApiParam(name="terminalid", value="ターミナル番号")@QueryParam("terminalid") final String terminalId,
    		@ApiParam(name="businessdaydate", value="営業日")@QueryParam("businessdaydate") final String businessDayDate) {
    	
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("getCMPresetInfoList")
            .println("companyid", companyId)
            .println("storeid", storeId)
            .println("terminalid", terminalId)
            .println("businessdaydate", businessDayDate);
        CMPresetInfos cmPresetInfos = new CMPresetInfos();

        try {
            IStoreDAO cmPresetDao = daoFactory.getStoreDAO();            
            List<CMPresetInfo> cmPresetInfoList = cmPresetDao.listCMPresetInfo(companyId, storeId, terminalId, businessDayDate);
            cmPresetInfos.setCMPresetInfoList(cmPresetInfoList);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to search CM Preset Info: " + ex.getMessage());
            cmPresetInfos.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(cmPresetInfos);
        }
        return cmPresetInfos;
    }
    
    /**
     * Web Method called to list all stores within a company.
     * 
     * @param key
     *            The key of the Store(s) to be search.
     * @return The Stores JSON Object containing the list of stores.
     */
    @Path("/getcmpresetstoreinfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="プリセットストア情報取得", response=PresetSroreInfo.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final PresetSroreInfo getCMPresetStoreInfo(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="店舗番号")@QueryParam("storeid") final String storeId, 
    		@ApiParam(name="terminalid", value="端末番号")@QueryParam("terminalid") final String terminalId) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("getCMPresetStoreInfo")
            .println("companyid", companyId)
            .println("storeid", storeId)
            .println("terminalid", terminalId);
        PresetSroreInfo presetsroreinfo = new PresetSroreInfo();
        String BusinessRegistrationNo = null;
        try {
            IStoreDAO cmPresetDao = daoFactory.getStoreDAO();
            BusinessRegistrationNo = cmPresetDao.getBusinessRegistrationNo(companyId, storeId, terminalId);
            presetsroreinfo= cmPresetDao.getPresetSroreInfo(companyId, storeId, terminalId);
            presetsroreinfo.setBusinessRegistrationNo(BusinessRegistrationNo);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to search CM PresetStore Info: " + ex.getMessage());
            presetsroreinfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(presetsroreinfo);
        }
        return presetsroreinfo;
    }
    
    /**
     * Web Method called to list all stores within a company.
     * 
     * @param key
     *            The key of the Store(s) to be search.
     * @return The Stores JSON Object containing the list of stores.
     */
    @Path("/getsummaryreceiptno")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="領収証No取得", response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final JSONData getSummaryReceiptNo(
    		@ApiParam(name="companyid", value="会社コード")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeid", value="店舗番号")@QueryParam("storeId") final String storeId, 
    		@ApiParam(name="terminalid", value="端末番号")@QueryParam("terminalId") final String terminalId,
    		@ApiParam(name="traning", value="トレーニング")@QueryParam("traning") final String traning) {

        String functionName = DebugLogger.getCurrentMethodName();
        JSONData json = new JSONData();
        tp.methodEnter("getSummaryReceiptNo")
            .println("companyid", companyId)
            .println("storeid", storeId)
            .println("terminalid", terminalId)
            .println("traning", traning);
        try {
            IStoreDAO cmPresetDao = daoFactory.getStoreDAO();            
            json.setJsonObject(cmPresetDao.getSummaryReceiptNo(companyId, storeId, terminalId,traning));
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to search Summary Receipt No: " + ex.getMessage());
            json.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(json);
        }
        return json;
    }
    /**
     * Web Method called to list all stores within a company.
     * 
     * @param key
     *            The key of the Store(s) to be search.
     * @return The Stores JSON Object containing the list of stores.
     */
    @Path("/updatesummaryreceiptno")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="領収証No更新", response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_QUEUEFULL, message="各商店の列がいっぱいになっている"),
        })
    public final JSONData updateSummaryReceiptNo(
    		@ApiParam(name="SubNum1", value="予約")@QueryParam("SubNum1") final int SubNum1,
    		@ApiParam(name="companyId", value="会社コード")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗コード")@QueryParam("storeId") final String storeId, 
    		@ApiParam(name="terminalId", value="POSコード")@QueryParam("terminalId") final String terminalId,
    		@ApiParam(name="traning", value="トレーニング")@QueryParam("traning") final String traning) {

        String functionName = DebugLogger.getCurrentMethodName();
        JSONData json = new JSONData();
        tp.methodEnter("updateSummaryReceiptNo")
            .println("SubNum1",SubNum1)
            .println("companyid", companyId)
            .println("storeid", storeId)
            .println("terminalid", terminalId)
            .println("traning", traning);
        try {
            IStoreDAO updateSummaryReceiptNoDao = daoFactory.getStoreDAO();
            json.setJsonObject(String.valueOf(updateSummaryReceiptNoDao.updateSummaryReceiptNo(SubNum1, companyId, storeId, terminalId,traning)));
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update Summary Receipt No: " + ex.getMessage());
            json.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(json);
        }
        return json;
    }
    
	/**
	 * The number of total settlement
	 * 
	 * @param companyId
	 * @param storeId
	 * @param terminalId
	 * @param businessdaydate
	 * @return The StoreInfo.
	 */
	@Path("/addstoretotal")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value="精算回数を集計", response=Stores.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
		})
	public final StoreInfo addStoreTotal(
			@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
			@ApiParam(name="storeid", value="店番号")@FormParam("storeid") final String storeId, 
			@ApiParam(name="terminalid", value="ターミナル番号")@FormParam("terminalid") final String terminalId,
			@ApiParam(name="businessdaydate", value="営業日付")@FormParam("businessdaydate") final String businessdaydate) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
			.println("companyid", companyId)
			.println("storeid", storeId)
			.println("terminalid", terminalId)
			.println("businessdaydate", businessdaydate);
		StoreInfo storeInfo = new StoreInfo();

		try {
			IStoreDAO storeDao = daoFactory.getStoreDAO();
			storeInfo = storeDao.addStoreTotal(companyId, storeId, terminalId, businessdaydate);
		} catch (DaoException ex) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
					"Failed to search Stores: " + ex.getMessage());
			if (ex.getCause() instanceof SQLException) {
				storeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			} else {
				storeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to search Stores: " + ex.getMessage());
			storeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(storeInfo);
		}
		return storeInfo;
	}
	
	/**
	 * get Store Total
	 * 
	 * @param companyId
	 * @param storeId
	 * @param terminalId
	 * @param businessdaydate
	 * @return The StoreInfo.
	 */
	@Path("/getstoretotal")
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value="精算回数を取得", response=Stores.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
		})
	public final StoreInfo getStoreTotal(
			@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
			@ApiParam(name="storeid", value="店番号")@FormParam("storeid") final String storeId, 
			@ApiParam(name="terminalid", value="ターミナル番号")@FormParam("terminalid") final String terminalId,
			@ApiParam(name="businessdaydate", value="営業日付")@FormParam("businessdaydate") final String businessdaydate) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
			.println("companyid", companyId)
			.println("storeid", storeId)
			.println("terminalid", terminalId)
			.println("businessdaydate", businessdaydate);
		StoreInfo storeInfo = new StoreInfo();

		try {
			IStoreDAO storeDao = daoFactory.getStoreDAO();
			storeInfo = storeDao.getStoreTotal(companyId, storeId, terminalId, businessdaydate);
		} catch (DaoException ex) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
					"Failed to search Stores: " + ex.getMessage());
			if (ex.getCause() instanceof SQLException) {
				storeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			} else {
				storeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to search Stores: " + ex.getMessage());
			storeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(storeInfo);
		}
		return storeInfo;
	}
}
