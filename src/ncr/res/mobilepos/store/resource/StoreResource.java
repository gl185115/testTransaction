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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.model.JSONData;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.simpleprinterdriver.priflexpro.PriFlexProBTPrinter;
import ncr.res.mobilepos.store.dao.IStoreDAO;
import ncr.res.mobilepos.store.model.CMPresetInfo;
import ncr.res.mobilepos.store.model.CMPresetInfos;
import ncr.res.mobilepos.store.model.PresetSroreInfo;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.model.StoreInternSys;
import ncr.res.mobilepos.store.model.StoreLogo;
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
@Api(value="/storeinfo", description="�X�g�A���API")
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

    private String pathName = "storeinfo";
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
    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�r���[�X", response=ViewStore.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="�X�܂̓f�[�^�x�[�X�ɂ݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final ViewStore viewStore(
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h")@QueryParam("retailstoreid") final String retailStoreID) {

        String functionName = "StoreResource.viewStore";

        tp.methodEnter("viewStore");
        tp.println("RetailStoreID", retailStoreID);
        ViewStore store = new ViewStore();

        try {

            IStoreDAO storeDAO = daoFactory.getStoreDAO();
            store = storeDAO.viewStore(retailStoreID);

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
     * Service to view store details of given parameter storeid.
     * 
     * @param retailStoreID
     *            storeid to lookup.
     * @return JSON type of Store.
     */
    @Path("/getstoredetailinfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�X�̏ڍ׏����擾����", response=ViewStore.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="�X�܂̓f�[�^�x�[�X�ɂ݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final ViewStore getStoreDetailInfo(
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h")@QueryParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="companyId", value="��ЃR�[�h")@QueryParam("companyId") final String companyId) {

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
     * Service to create a Store for Store Table Maintenance.
     * 
     * @param retailStoreID
     *            - Store number
     * @param store
     *            - Store
     * @return ResultBase
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�n������X�g�A", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_STORE_EXISTS, message="�X�͂��łɑ���"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final ResultBase createStore(
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h")@FormParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="store", value="�X��")@FormParam("store") final String store) {

        tp.methodEnter("createStore");
        tp.println("RetailStoreID", retailStoreID).println("Store", store);

        ResultBase result = null;

        try {
            JsonMarshaller<Store> jsonMarshaller = new JsonMarshaller<Store>();
            Store cStore = jsonMarshaller.unMarshall(store, Store.class);

            if (retailStoreID == null || retailStoreID.trim().isEmpty()) {
                tp.println("Invalid value for retailstoreid");
                result = new ResultBase(ResultBase.RES_STORE_INVALIDPARAMS,
                        "Invalid value for retailstoreid");
                return result;
            }

            IStoreDAO storedao = daoFactory.getStoreDAO();
            String appId = pathName.concat(".create");
            cStore.setUpdOpeCode(getOpeCode());
            cStore.setUpdAppId(appId);
            result = storedao.createStore(retailStoreID, cStore);

        } catch (DaoException e) {
            LOGGER.logAlert("StoreResource", "createStore",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result = new ResultBase(ResultBase.RES_ERROR_DB, e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("StoreResource", "createStore",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(result);
        }
        return result;
    }

    /**
     * Service to delete store of given parameter storeid from database.
     * 
     * @param retailStoreID
     *            storeid to lookup.
     * @return JSON type of Store.
     */
    @Path("/delete")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�폜����X�g�A", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="�X�܂̓f�[�^�x�[�X�ɂ݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final ResultBase deleteStore(
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h")@FormParam("retailstoreid") final String retailStoreID) {

        String functionName = "StoreResource.deleteStore";

        tp.methodEnter("deleteStore");
        tp.println("RetailStoreID", retailStoreID);
        ResultBase result = new ResultBase();

        try {

            IStoreDAO storeDAO = daoFactory.getStoreDAO();
            String updAppId = pathName.concat(".delete");
            result = storeDAO.deleteStore(retailStoreID,updAppId, getOpeCode());

        } catch (DaoException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to delete Store# " + retailStoreID + ": "
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
                    "Failed to delete Store# " + retailStoreID + ": "
                            + ex.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(result);
        }

        return result;
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
    @ApiOperation(value="��������X�g�A", response=Stores.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final Stores searchStores(
    		@ApiParam(name="companyId", value="��ЃR�[�h")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="key", value="�L�[")@QueryParam("key") final String key, 
    		@ApiParam(name="name", value="��")@QueryParam("name") final String name,
    		@ApiParam(name="limit", value="����")@QueryParam("limit") final int searchLimit) {
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
     * Web Method called to update a store.
     * 
     * @param storeid
     *            The retail Store id
     * @param storeJson
     *            The new values for store.
     * @return The Stores JSON Object containing the list of stores.
     */
    @Path("/maintenance")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�X�V����X�g�A", response=ViewStore.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_STORE_EXISTS, message="�X�͂��łɑ���"),
            @ApiResponse(code=ResultBase.RES_STORE_NO_UPDATE, message="�X�V����X�g�A�͎��s"),
        })
    public final ViewStore updateStores(
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h")@FormParam("retailstoreid") final String storeid,
    		@ApiParam(name="store", value="�X��")@FormParam("store") final String storeJson) {
        String functionName = "StoreResource.updateStores";

        tp.methodEnter("updateStores");
        tp.println("RetailStoreID", storeid).println("Store", storeJson);

        ViewStore viewStore = new ViewStore();

        try {
            String appID = pathName.concat(".maintenance");
            JsonMarshaller<Store> storeJsonMarshaller = new JsonMarshaller<Store>();             
            Store store = storeJsonMarshaller
                    .unMarshall(storeJson, Store.class);
            IStoreDAO storeDao = daoFactory.getStoreDAO();
            store.setUpdAppId(appID);
            store.setUpdOpeCode(getOpeCode());
            viewStore = storeDao.updateStore(storeid, store);
        } catch (DaoException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to update Store with Store# " + storeid + ":"
                            + ex.getMessage());

            if (ex.getCause() instanceof SQLException) {
                viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update Store with Store# " + storeid + ":"
                            + ex.getMessage());
            viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewStore);
        }
        return viewStore;
    }

    /**
     * Web Method Called to retrieve Store Logo Information.
     * 
     * @param retailStoreID
     *            The Retail Store ID.
     * @return The {@link ViewStore} containing the StoreLogo.
     */
    @Path("/{retailstoreid}/image")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�摜�𓾂�", response=ViewStore.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
            @ApiResponse(code=ResultBase.RES_STORE_LOGO_INVALID, message="�X���S������"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="�X�܂̓f�[�^�x�[�X�ɂ݂���Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final ViewStore getImage(
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h")@PathParam("retailstoreid") final String retailStoreID) {
        tp.methodEnter("getImage").println("RetailStoreID", retailStoreID);
        ViewStore viewStore = new ViewStore();
        IStoreDAO storeDao;
        if (StringUtility.isNullOrEmpty(retailStoreID)) {
            viewStore
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.println("Parameter[s] is empty or null.");
            tp.methodExit(viewStore);
            return viewStore;
        }

        try {
            storeDao = daoFactory.getStoreDAO();
            ViewStore store = storeDao.viewStore(retailStoreID);
            // Is the Store NOT Existing? If yes, then fail to get StoreLogo.
            if (ResultBase.RES_STORE_OK < store.getNCRWSSResultCode()) {
                viewStore.setNCRWSSResultCode(store.getNCRWSSResultCode());
                return viewStore;
            }
            List<String> paths = new ArrayList<String>();
            paths.add(store.getStore().getElectroFilePath());
            paths.add(store.getStore().getStampTaxFilePath());

            PriFlexProBTPrinter priFlexProBTPrinter = new PriFlexProBTPrinter(
                    paths);
            final byte[] definedNVImgData = priFlexProBTPrinter
                    .getDefinedNVImgData();

            // Are there NO bytes of Store Logo Retrieved?
            // If yes, then Store Logo is invalid.
            if (definedNVImgData == null || (definedNVImgData != null && 
            		definedNVImgData.length == 0)) {
                viewStore
                        .setNCRWSSResultCode(ResultBase.RES_STORE_LOGO_INVALID);
                tp.println("Store Logo is invalid.");
                return viewStore;
            }
            StoreLogo storeLogo = new StoreLogo();
            String base64Image = Base64.getEncoder().encodeToString(definedNVImgData);
            storeLogo.setImage(base64Image);
            viewStore.setLogo(storeLogo);
        } catch (DaoException e) {
            LOGGER.logAlert(
                    progName,
                    "StoreResource.getImage",
                    Logger.RES_EXCEP_DAO,
                    "Failed to get Customer Logo Information: \n"
                            + e.getMessage());
            viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception e) {
            LOGGER.logAlert(
                    progName,
                    "StoreResource.getImage",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get Customer Logo Information: \n"
                            + e.getMessage());
            viewStore.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewStore);
        }
        return viewStore;
    }

    /**
     * RES-5500 Web Method Called to retrieve value.
     * 
     * @param storecode
     *            The Store_Intern_Sys
     * @param usage
     *            The Store_Intern_Sys
     * @return The storeInSys object.
     */
    @Path("/spartinternalsystem")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final StoreInternSys spartStore(
            @FormParam("storecode") final String storecode,
            @FormParam("usage") final Integer usage) {
        tp.methodEnter("spartStore").println("storecode", storecode)
                .println("usage", usage);

        StoreInternSys storeInSys = new StoreInternSys();

        IStoreDAO storeDao;
        if (StringUtility.isNullOrEmpty(storecode)
                || usage == null) {
            storeInSys
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.println("Parameter[s] is empty or null.");
            tp.methodExit(storeInSys);
            return storeInSys;
        }

        try {
            storeDao = daoFactory.getStoreDAO();
            storeInSys = storeDao.getStoreInterSys(storecode, usage);
        } catch (DaoException e) {
            LOGGER.logAlert(progName, "StoreResource.StoreInternSys",
                    Logger.RES_EXCEP_DAO,
                    "Failed to get value for " + storecode + " and " + usage
                            + " \n" + e.getMessage());
            storeInSys.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception e) {
            LOGGER.logAlert(progName, "StoreResource.StoreInternSys",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get value for " + storecode + " and " + usage
                            + " \n" + e.getMessage());
            storeInSys.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(storeInSys);
        }
        return storeInSys;
    }

    private String getOpeCode(){
        return ((context != null) && (context.getUserPrincipal()) != null) ? context
                .getUserPrincipal().getName() : null;
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
    @ApiOperation(value="CM�v���Z�b�g����X�g�A���X�g���𓾂�", response=CMPresetInfos.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final CMPresetInfos getCMPresetInfoList(
    		@ApiParam(name="companyid", value="��ЃR�[�h")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="�X�ܔԍ�")@QueryParam("storeid") final String storeId, 
    		@ApiParam(name="terminalid", value="�[���ԍ�")@QueryParam("terminalid") final String terminalId,
    		@ApiParam(name="businessdaydate", value="�c�Ɠ�")@QueryParam("businessdaydate") final String businessDayDate) {
    	
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
    @ApiOperation(value="CM�v���Z�b�g����X�g�A���𓾂�", response=PresetSroreInfo.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final PresetSroreInfo getCMPresetStoreInfo(
    		@ApiParam(name="companyid", value="��ЃR�[�h")@QueryParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="�X�ܔԍ�")@QueryParam("storeid") final String storeId, 
    		@ApiParam(name="terminalid", value="�[���ԍ�")@QueryParam("terminalid") final String terminalId) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("getCMPresetStoreInfo")
            .println("companyid", companyId)
            .println("storeid", storeId)
            .println("terminalid", terminalId);
        PresetSroreInfo presetsroreinfo = new PresetSroreInfo();
        try {
            IStoreDAO cmPresetDao = daoFactory.getStoreDAO();            
            presetsroreinfo= cmPresetDao.getPresetSroreInfo(companyId, storeId, terminalId);
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
    @ApiOperation(value="�E�v���V�[�gNo�𓾂�", response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final JSONData getSummaryReceiptNo(
    		@ApiParam(name="companyid", value="��ЃR�[�h")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeid", value="�X�ܔԍ�")@QueryParam("storeId") final String storeId, 
    		@ApiParam(name="terminalid", value="�[���ԍ�")@QueryParam("terminalId") final String terminalId,
    		@ApiParam(name="traning", value="�g���[�j���O")@QueryParam("traning") final String traning) {

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
    @ApiOperation(value="�E�v���V�[�gNo���X�V����", response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_QUEUEFULL, message="�e���X�̗񂪂����ς��ɂȂ��Ă���"),
        })
    public final JSONData updateSummaryReceiptNo(
    		@ApiParam(name="SubNum1", value="�\��")@QueryParam("SubNum1") final int SubNum1,
    		@ApiParam(name="companyId", value="��ЃR�[�h")@QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�܃R�[�h")@QueryParam("storeId") final String storeId, 
    		@ApiParam(name="terminalId", value="POS�R�[�h")@QueryParam("terminalId") final String terminalId,
    		@ApiParam(name="traning", value="�g���[�j���O")@QueryParam("traning") final String traning) {

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
}
