package ncr.res.mobilepos.deviceinfo.resource;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
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
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.deviceinfo.dao.ILinkDAO;
import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.Indicators;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.POSLinks;
import ncr.res.mobilepos.deviceinfo.model.PrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.TerminalStatus;
import ncr.res.mobilepos.deviceinfo.model.TerminalTillGroup;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.deviceinfo.model.WorkingDevices;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;

/**
 * DeviceInfoResource Web Resource Class.
 * WebService resource to manage PosTerminal link and
 * Printer associated with a mobile device.
 *
 */
@Path("/deviceinfo")
@Api(value="/deviceinfo", description="端末関連API")
public class DeviceInfoResource {
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * Progname assignment for PeripheralDeviceControl.
     */
    private static final String PROG_NAME = "DvInfo";
    /**
     * PeripheralDeviceControl classname.
     */
    private String className = "DeviceInfoResource.";
    /**
     * instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * The Dao Factory for accessing the database.
     */
    private DAOFactory daoFactory;
    /**
     * Servlet context holder.
     */
    @Context private ServletContext context;
     /**
        *Security context holder.
        */
    @Context private SecurityContext securityContext;
    /**
     * Setter for the context necessary
     * for unit tests operation.
     *
     *  @param contextToSet servlet context to set to the resource
     *
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * constant for queuebuster link type.
     */
    private static final String QUEUEBUSTER_LINK_TYPE = "queuebuster";
    /**
     * Constant for Signature Link Type.
     */
    private static final String SIGNATURE_LINK_TYPE = "signature";

    /**
     * constant for creditauthorization link type.
     */
    private static final String CREDITAUTH_LINK_TYPE = "creditauthorization";

    private String pathName = "deviceinfo";

    /**
     * PeripheralDeviceControl resource name.
     *
     * @return String name of resource
     */
    public final String getName() {
        return "peripheraldevicecontrol";
    }

    /**
     * TXU_POS_CTRL OpenCloseStat.
     */
    private static int POSCTRL_OPEN_CLOSE_STAT_OPENED = 1;
    private static int POSCTRL_OPEN_CLOSE_STAT_CLOSED = 4;

    /**
     * PeripheralDeviceControl default constructor.
     */
    public DeviceInfoResource() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
    }

    /**
     * Service resource to set retrieve
     * the device info.
     *
     *  @param deviceID - the id of the device
     *                      to retrieve
     *  @param retailStoreID  - the current store id
     *
     * @return DeviceInfo - the information on the given
     *                      device id
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/{companyid}/{retailstoreid}/{deviceid}/{trainingmode}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="デバイス情報取得", response=ViewDeviceInfo.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="引数無効"),
            @ApiResponse(code=ResultBase.RESDEVCTL_NOTFOUND, message="デバイス情報無し")
    })
    public final ViewDeviceInfo getDeviceInfo(
            @ApiParam(name="companyid", value="企業コード") @PathParam("companyid") final String companyId,
            @ApiParam(name="retailstoreid", value="店舗コード") @PathParam("retailstoreid") final String retailStoreID,
            @ApiParam(name="deviceid", value="デバイスコード") @PathParam("deviceid") final String deviceID,
            @ApiParam(name="trainingmode", value="トレーニングモード") @PathParam("trainingmode") final int trainingMode) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
			.println("companyid", companyId)
			.println("retailstoreid", retailStoreID)
			.println("deviceid", deviceID)
			.println("trainingmode", trainingMode);

        ViewDeviceInfo viewDevInfo = new ViewDeviceInfo();
        viewDevInfo.setDeviceInfo(new DeviceInfo());
        viewDevInfo.getDeviceInfo().setPrinterInfo(new PrinterInfo());

        boolean hasNullEmptyCompanyId = StringUtility.isNullOrEmpty(companyId);
        boolean hasNullEmptyRetailStoreID = StringUtility.isNullOrEmpty(retailStoreID);
        boolean hasNullEmptyDeviceID = StringUtility.isNullOrEmpty(deviceID);

        if (hasNullEmptyCompanyId || hasNullEmptyDeviceID || hasNullEmptyRetailStoreID) {
            viewDevInfo.setDeviceInfo(new DeviceInfo());
            if (!hasNullEmptyCompanyId) {
            	viewDevInfo.getDeviceInfo().setCompanyId(companyId);
            }
            if (!hasNullEmptyRetailStoreID) {
                viewDevInfo.getDeviceInfo().setRetailStoreId(retailStoreID);
            }
            if (!hasNullEmptyDeviceID) {
                viewDevInfo.getDeviceInfo().setDeviceId(deviceID);
            }
            viewDevInfo.getDeviceInfo().setLinkPOSTerminalId("");
            viewDevInfo.setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("RetailStoreID or DeviceID is invalid.");
            tp.methodExit(viewDevInfo);
            return viewDevInfo;
        }

        try {
            IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
            DeviceInfo devInfo = iPerCtrlDao.getDeviceInfo(
            		companyId, retailStoreID, deviceID, trainingMode);
            String uniqueDeviceID = devInfo.getDeviceId();
            if (StringUtility.isNullOrEmpty(uniqueDeviceID)) {
                devInfo.setDeviceId(deviceID);
                devInfo.setRetailStoreId(retailStoreID);
                viewDevInfo.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_NOTFOUND);
                tp.println("The info with given storeid,"
                        + " and terminalid does not exist");
            }
            viewDevInfo.setDeviceInfo(devInfo);
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get Device Info.", ex);
			viewDevInfo = new ViewDeviceInfo(ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get Device Info.", ex);
			viewDevInfo = new ViewDeviceInfo(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(viewDevInfo);
		}
        return viewDevInfo;
    }

    /**
     * Updates device entry in MST_DEVICEINFO.
     *
     * @param deviceID
     *            The device to delete.
     * @param retailStoreID
     *            The storeid where the device belongs.
     * @param jsonDeviceInfo
     *            The device info model containing the new values
     * @return ViewDeviceInfo - contains the DeviceInfo and result codes
     *
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/maintenance")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="更新端末情報", response=ViewDeviceInfo.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="店舗はデータベースにみつからない"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RESDEVCTL_ALREADY_EXIST, message="設備データはすでにデータベースに存在している"),
        @ApiResponse(code=ResultBase.RESDEVCTL_NOTFOUND, message="設備データを発見していない")
    })
    public final ViewDeviceInfo updateDevice(
    	@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyID,
    	@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailStoreID,
    	@ApiParam(name="deviceid", value="端末番号") @FormParam("deviceid") final String deviceID,
    	@ApiParam(name="deviceinfo", value="端末情報") @FormParam("deviceinfo") final String jsonDeviceInfo,
    	@ApiParam(name="trainingmode", value="トレーニングフラグ") @FormParam("trainingmode") final int trainingMode) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
	        .println("companyid", companyID)
	        .println("retailstoreid", retailStoreID)
	        .println("deviceid", deviceID)
			.println("deviceinfo", jsonDeviceInfo)
			.println("trainingmode", trainingMode);

        ViewDeviceInfo viewInfo = new ViewDeviceInfo();
		if (StringUtility.isNullOrEmpty(deviceID, companyID, retailStoreID, jsonDeviceInfo)) {
			viewInfo.setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
			tp.println("Parameter is null or empty.");
			tp.methodExit(viewInfo);
			return viewInfo;
		}
        try {
			JsonMarshaller<DeviceInfo> jsonMarshaller = new JsonMarshaller<DeviceInfo>();
			DeviceInfo newDeviceInfo = jsonMarshaller.unMarshall(
					jsonDeviceInfo, DeviceInfo.class);
			if (!StringUtility.isNullOrEmpty(newDeviceInfo.getRetailStoreId()) &&
				!storeExists(newDeviceInfo.getRetailStoreId())) {
				tp.println("New StoreId does not exist");
				viewInfo.setNCRWSSResultCode(ResultBase.RES_STORE_NOT_EXIST);
				return viewInfo;
			}
			IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
			newDeviceInfo.setUpdAppId(pathName.concat(".maintenance"));
			newDeviceInfo.setUpdOpeCode(getOpeCode());
			viewInfo = iPerCtrlDao.updateDevice(companyID, retailStoreID, deviceID,
					newDeviceInfo, trainingMode, null);
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to update device.", ex);
			viewInfo = new ViewDeviceInfo(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to update device.", ex);
			viewInfo = new ViewDeviceInfo(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(viewInfo);
		}
		return viewInfo;
	}

    /**
     * Service resource to set retrieve
     * the Link Information.
     *
     *  @param linkType         - The type of link to retrieve information.
     *  @param storeId    - Store number of the Link ID to look up.
     *  @param posLinkId        - The POS Link ID.
     *  @return POSLinkInfo     - The POS Link Information.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/link/{linktype}/{retailstoreid}/{poslinkid}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="検索リンク情報", response=ViewPosLinkInfo.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RES_LINK_NOTFOUND, message="リンクは見つからない"),
        @ApiResponse(code=ResultBase.RES_LINK_TYPEINVALID, message="無効なリンクタイプ"),
    })
	public final ViewPosLinkInfo getLinkItem(
			@ApiParam(name="linktype", value="リンクタイプ") @PathParam("linktype") final String linkType,
			@ApiParam(name="retailstoreid", value="店舗コード") @PathParam("retailstoreid") final String storeId,
			@ApiParam(name="poslinkid", value="posリンクコード") @PathParam("poslinkid") final String posLinkId) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("linktype", linkType)
				.println("retailstoreid", storeId)
				.println("poslinkid", posLinkId);

        ViewPosLinkInfo viewPosLinkInfo = new ViewPosLinkInfo();
        viewPosLinkInfo.setPosLinkInfo(new POSLinkInfo());

        if (StringUtility.isNullOrEmpty(linkType,storeId,posLinkId)) {
            viewPosLinkInfo.setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("RetailStoreID or POSLink ID is invalid.");
            tp.methodExit(viewPosLinkInfo);
            return viewPosLinkInfo;
        }



        try {
            if (QUEUEBUSTER_LINK_TYPE.equals(linkType)) {
                ILinkDAO queueBusterLinkDao = daoFactory
                        .getQueueBusterLinkDAO();
                POSLinkInfo poslinkinfo = queueBusterLinkDao.getLinkItem(
                        storeId, posLinkId);
                if (poslinkinfo == null) {
                    viewPosLinkInfo
                            .setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                    tp.println("The QueueBuster Link does not exist.");
                }
                viewPosLinkInfo.setPosLinkInfo(poslinkinfo);
            } else if (CREDITAUTH_LINK_TYPE.equals(linkType)) {
                ILinkDAO creditAuthorizationDao =
                    daoFactory.getCreditAuthorizationLinkDAO();
                POSLinkInfo poslinkinfo = creditAuthorizationDao.getLinkItem(
                        storeId, posLinkId);
                if (poslinkinfo == null) {
                    viewPosLinkInfo
                            .setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                    tp.println("The Authorization Link does not exist.");
                }
                viewPosLinkInfo.setPosLinkInfo(poslinkinfo);
            } else if (SIGNATURE_LINK_TYPE.equals(linkType)) {
                ILinkDAO signatureLinkDao = daoFactory.getSignatureLinkDAO();
                POSLinkInfo poslinkinfo = signatureLinkDao.getLinkItem(
                        storeId, posLinkId);
                if (poslinkinfo == null) {
                    viewPosLinkInfo
                            .setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                    tp.println("The Signature Link does not exist.");
                }
                viewPosLinkInfo.setPosLinkInfo(poslinkinfo);
            } else {
                viewPosLinkInfo
                        .setNCRWSSResultCode(ResultBase.RES_LINK_TYPEINVALID);
                tp.println("Link type is invalid.");
            }

		} catch (DaoException daoEx) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get the pos link.", daoEx);
			viewPosLinkInfo = new ViewPosLinkInfo(
					(daoEx.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DAO, daoEx);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get the pos link.", ex);
			viewPosLinkInfo = new ViewPosLinkInfo(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(viewPosLinkInfo);
		}
		return viewPosLinkInfo;
	}

    /**
     * Service resource to retrieve list of Link Information.
     *
     *  @param linkType         - the type of links to retrieve
     *  @param storeId    - Store number of the POS Link ID to look up.
     *
     * @return POSLinks - the List of POS Link Information.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/link/{linktype}/list")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="リンクリストを取得", response=POSLinks.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RES_LINK_TYPEINVALID, message="無効なリンクタイプ"),
        @ApiResponse(code=ResultBase.RES_LINK_LISTEMPTY, message="リンクリストは空き")
    })
    public final POSLinks getLinksList(
    		@ApiParam(name="linktype", value="リンクタイプ") @PathParam("linktype") final String linkType,
    		@ApiParam(name="storeid", value="店舗コード") @QueryParam("storeid") final String storeId,
    		@ApiParam(name="key", value="検索キー") @QueryParam("key") final String key,
    		@ApiParam(name="name", value="検索名") @QueryParam("name") final String name,
    		@ApiParam(name="limit", value="制限数") @QueryParam("limit") final int limit) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("linktype", linkType)
				.println("storeid", storeId).println("key", key)
				.println("name", name).println("limit", limit);

        POSLinks poslinks = new POSLinks();

        if(storeId == null || linkType == null){
            poslinks.setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("RetailStoreID or Link Type is invalid.");
            tp.methodExit(poslinks);
            return poslinks;
        }

        int searchLimit = (limit == 0) ? GlobalConstant.getMaxSearchResults() : limit;

        try {
            if (QUEUEBUSTER_LINK_TYPE.equals(linkType)) {
                ILinkDAO queueBusterLinkDao = daoFactory
                        .getQueueBusterLinkDAO();
                List<POSLinkInfo> linksArray = queueBusterLinkDao
                        .getLinks(storeId, key, name, searchLimit);

                if (linksArray.isEmpty()) {
                    poslinks.setNCRWSSResultCode(ResultBase.RES_LINK_LISTEMPTY);
                    tp.println("QueueBuster Link List is empty.");
                } else {
                    poslinks.setPOSLinkInfos(linksArray);
                }
            } else if (CREDITAUTH_LINK_TYPE.equals(linkType)) {
                ILinkDAO creditAuthLinkDao = daoFactory
                        .getCreditAuthorizationLinkDAO();
                List<POSLinkInfo> linksArray = creditAuthLinkDao
                		.getLinks(storeId, key, name, searchLimit);

                if (linksArray.isEmpty()) {
                    poslinks.setNCRWSSResultCode(ResultBase.RES_LINK_LISTEMPTY);
                    tp.println("AuthorizationLink List is empty.");
                } else {
                    poslinks.setPOSLinkInfos(linksArray);
                }
            } else if (SIGNATURE_LINK_TYPE.equals(linkType)) {
                ILinkDAO signatureLinkDao = daoFactory
                        .getSignatureLinkDAO();
                List<POSLinkInfo> linksArray = signatureLinkDao
                		.getLinks(storeId, key, name, searchLimit);

                if (linksArray.isEmpty()) {
                    poslinks.setNCRWSSResultCode(
                            ResultBase.RES_LINK_LISTEMPTY);
                    tp.println("Signature Link List is empty.");
                } else {
                    poslinks.setPOSLinkInfos(linksArray);
                }
            } else {
                poslinks.setNCRWSSResultCode(ResultBase.RES_LINK_TYPEINVALID);
                tp.println("link type is invalid.");
            }

		} catch (DaoException daoEx) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get the link list.", daoEx);
			poslinks = new POSLinks(
					(daoEx.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, daoEx);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get the link list.", ex);
			poslinks = new POSLinks(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(poslinks);
		}
		return poslinks;
	}

    /**
     * Checks if store is existing.
     *
     * @param retailStoreID the store identifier.
     * @return true, if store exist.
     */
    private boolean storeExists(final String retailStoreID) {
        boolean isExist = true;
        StoreResource storeResource = new StoreResource();
        ViewStore viewStore = storeResource.viewStore(retailStoreID);
        if (viewStore.getNCRWSSResultCode() != ResultBase.RES_OK) {
            isExist = false;
        }
        return isExist;
    }

    private String getOpeCode(){
        return ((securityContext != null) && (securityContext.getUserPrincipal()) != null) ? securityContext
                .getUserPrincipal().getName() : null;
    }

    /**
     * Service resource to retrieve
     * all the tills/drawers
     * in the store configuration.
     *
     *  @param storeid the current storeid
     *  @param key the current search key
     *  @param limit the limit to the result set to be returned
     *
     * @return ViewTill indicates result of operation
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getalltills")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="すべてのドゥローアを取得する", response=ViewTill.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public final ViewTill getAllTills(
    		@ApiParam(name="storeid", value="店舗コード") @QueryParam("storeid") final String storeid,
    		@ApiParam(name="key", value="検索キー") @QueryParam("key") final String key,
    		@ApiParam(name="limit", value="制限数") @QueryParam("limit") final int limit) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeid", storeid)
				.println("key", key).println("limit", limit);

        ViewTill result = new ViewTill();

        try {
            IDeviceInfoDAO iPerCtrlDao =
                daoFactory.getDeviceInfoDAO();

            List<Till> tillList =
                iPerCtrlDao.getAllTills(storeid, key, limit);

            if (!tillList.isEmpty()) {
                result.setTillList(tillList);
                result.setMessage("Successfully retrieved tills.");
            }
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to retrieve tills.", ex);
			if (ex.getCause() instanceof SQLException) {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			} else {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
			}
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to retrieve tills.", ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(result);
		}
		return result;
	}

    /**
     * GetAttribute.
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     * @param companyId - Company ID.
     * @param training - training flag.
     * @return AttributeInfo
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getattribute")
    @ApiOperation(value="属性情報を得る", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データベース情報は見つからない")

    })
    public final ResultBase getAttribute(
    		@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="terminalId", value="端末番号") @QueryParam("terminalId") final String terminalId,
    		@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="training", value="トレーニングフラグ") @QueryParam("training") final String training) {
		final String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("storeid", storeId)
                .println("terminalId", terminalId)
                .println("companyId", companyId)
                .println("Training", training);

        // Prepare Invalid param return.
        ResultBase invalidParamResult = new ResultBase();
        invalidParamResult.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
        invalidParamResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
        invalidParamResult.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);

        // Validates if all arguments are not empty.
        if(StringUtility.isNullOrEmpty(storeId, terminalId, companyId, training)){
            tp.methodExit("Validation failed: Empty params");
            LOGGER.logAlert(PROG_NAME, functionName, LOGGER.RES_EXCEP_GENERAL,
                    "Validation failed: Empty params");
            return invalidParamResult;
        }

        // Validates if training is a parsable integer.
        int parsedTraining = -1;
        try {
            parsedTraining = Integer.parseInt(training);
        } catch (NumberFormatException nfe) {
            tp.methodExit("Validation failed: training is not a number");
            LOGGER.logAlert(PROG_NAME, functionName, LOGGER.RES_EXCEP_GENERAL,
                    "Validation failed: training is not a number", nfe);
            return invalidParamResult;
        }

        ResultBase result;
        // DB lookup.
        try{
            // Normal case.
            IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
			result = iPerCtrlDao.getAttributeInfo(storeId, terminalId, companyId, parsedTraining);
		}catch (DaoException ex) {
            // When DB related exception is thrown.
            int resultCode;
            String loggerExcepionCode;
			if (ex.getCause() instanceof SQLException) {
                resultCode = ResultBase.RES_ERROR_DB;
                loggerExcepionCode = LOGGER.RES_EXCEP_SQL;
			} else {
                resultCode = ResultBase.RES_ERROR_DAO;
                loggerExcepionCode = LOGGER.RES_EXCEP_DAO;
			}
            LOGGER.logAlert(PROG_NAME, functionName, loggerExcepionCode,
                    "DaoException thrown: failed to get AttributeInfo", ex);
            result = new ResultBase();
            result.setNCRWSSResultCode(resultCode);
            result.setNCRWSSExtendedResultCode(resultCode);
            result.setMessage(ex.getMessage());
		} catch (Exception ex) {
            // When Exception is thrown.
            LOGGER.logAlert(PROG_NAME, functionName, LOGGER.RES_EXCEP_GENERAL,
                    "Exception thrown: failed to get AttributeInfo", ex);
            result = new ResultBase();
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
		}
    	tp.methodExit(result);
		return result;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getdeviceattribute")
    @ApiOperation(value="端末属性情報を得る", response=DeviceAttribute.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データベース情報は見つからない")

    })
    public final DeviceAttribute getDeviceAttribute(
    		@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="terminalId", value="端末番号") @QueryParam("terminalId") final String terminalId) {

		String functionName = DebugLogger.getCurrentMethodName();

		tp.methodEnter(functionName);
		tp.println("companyid", companyId)
		    .println("storeid", storeId)
		    .println("terminalId", terminalId);

		DeviceAttribute result = new DeviceAttribute();

		try{
			if(StringUtility.isNullOrEmpty(storeId, terminalId)){
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);

				return result;
			}
			IDeviceInfoDAO iPerCtrlDao =
		               daoFactory.getDeviceInfoDAO();

			result = iPerCtrlDao.getDeviceAttributeInfo(companyId, storeId, terminalId);
		}catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get the Device attributeInfo.", ex);
			if (ex.getCause() instanceof SQLException) {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
				result.setMessage(ex.getMessage());
			} else {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DAO);
				result.setMessage(ex.getMessage());
			}
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get the Device attributeInfo.", ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(ex.getMessage());
		} finally {
			tp.methodExit(result);
		}

		return result;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getterminalinfo")
    @ApiOperation(value="取得端末情報", response=ViewTerminalInfo.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データベース情報は見つからない")
    })
    public final ViewTerminalInfo getTerminalInfo(
    	@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
    	@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId,
    	@ApiParam(name="terminalId", value="端末コード") @QueryParam("terminalId") final String terminalId) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("companyId", companyId)
			.println("storeId", storeId)
			.println("terminalId", terminalId);

    	ViewTerminalInfo terminalInfoResult = new ViewTerminalInfo();

    	if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId)) {
    		terminalInfoResult.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
        	tp.methodExit(terminalInfoResult.toString());
        	return terminalInfoResult;
        }

    	try {
    		IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
    		terminalInfoResult = iPerCtrlDao.getTerminalInfo(companyId, storeId, terminalId);
    	} catch (Exception e) {
    		String loggerErrorCode = null;
        	int resultBaseErrorCode = 0;
        	if (e.getCause() instanceof SQLException) {
        		loggerErrorCode = Logger.RES_EXCEP_DAO;
        		resultBaseErrorCode = ResultBase.RES_ERROR_DB;
        	} else if (e.getCause() instanceof SQLStatementException) {
        		loggerErrorCode = Logger.RES_EXCEP_DAO;
        		resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
        	} else {
        		loggerErrorCode = Logger.RES_EXCEP_GENERAL;
        		resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
        	}
        	terminalInfoResult.setNCRWSSResultCode(resultBaseErrorCode);
        	LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get terminal info for companyId#" + companyId + ", "
                    + "storeId#" + storeId + " and terminalId#" + terminalId + ": "
                    + e.getMessage());
    	} finally {
    		tp.methodExit(terminalInfoResult.toString());
    	}
    	return terminalInfoResult;
    }

    /**
     * Gets Terminal Open-Close status from TXU_POS_CTRL.OpenCloseStatus.
     * @param storeId - Store ID
     * @param terminalId - Terminal ID
     * @param companyId - Company ID
     * @return PosControlOpenCloseStatus for successful return, ResultBase for failure.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getdevicestatus")
    @ApiOperation(value="取得端末状態", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RES_NO_BIZDATE, message="無効な営業日"),
        @ApiResponse(code=ResultBase.RES_TERMINAL_NOT_WORKING, message="端末が仕事をしない")
    })
    public final ResultBase getDeviceStatus(
    		@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="terminalId", value="端末コード") @QueryParam("terminalId") final String terminalId) {
        final String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
                .println("companyId", companyId)
                .println("storeid", storeId)
                .println("terminalId", terminalId);

        ResultBase resultBase = new ResultBase();

        // Obtains this business day.
        String thisBusinessDay = new JournalizationResource().getBussinessDate(companyId, storeId);
        if(StringUtility.isNullOrEmpty(thisBusinessDay)) {
            // This is unlikely to happen.
            resultBase.setMessage("No business day for the store");
            resultBase.setNCRWSSResultCode(ResultBase.RES_NO_BIZDATE);
            tp.println("No business day fot the store on MST_BIZDAY.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        // check for required parameters
        if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId)) {
            resultBase.setMessage("Required fields are empty");
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.println("A required parameter is null or empty.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        // DB lookup.
        try{
            IDeviceInfoDAO deviceInfoDao = daoFactory.getDeviceInfoDAO();
            resultBase = deviceInfoDao.getPosCtrlOpenCloseStatus(companyId, storeId, terminalId, thisBusinessDay);
        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to select TXU_POS_CTRL:"
                            + ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TerminalId:" + terminalId + ":"
                            + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to select TXU_POS_CTRL:"
                            + ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TerminalId:" + terminalId + ":"
                            + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase);
        }
        return resultBase;
    }

    /**
     * Gets all the active terminals on this business day under the Application Server.
     * @param storeId - Store ID
     * @param terminalId - Terminal ID
     * @param companyId - Company ID
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getworkingdevices")
    @ApiOperation(value="使用中の端末情報を得る", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_NO_BIZDATE, message="無効な営業日"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ")
    })
    public final ResultBase getWorkingDevices(
    		@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="terminalId", value="端末NO") @QueryParam("terminalId") final String terminalId) {
        final String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
                .println("companyId", companyId)
                .println("storeid", storeId)
                .println("terminalId", terminalId);

        ResultBase resultBase = new ResultBase();

        // 1, check for required parameters
        if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId)) {
            resultBase.setMessage("Required fields are empty");
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.println("A required parameter is null or empty.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        // 2, Obtains this business day.
        String thisBusinessDay = new JournalizationResource().getBussinessDate(companyId, storeId);
        if(StringUtility.isNullOrEmpty(thisBusinessDay)) {
            // This is unlikely to happen.
            resultBase.setMessage("No business day for the store");
            resultBase.setNCRWSSResultCode(ResultBase.RES_NO_BIZDATE);
            tp.println("No business day fot the store on MST_BIZDAY.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        try{
            // 3, Gets all the devices by selecting RESMaster.AUT_DEVICES
            IDeviceInfoDAO deviceInfoDao = daoFactory.getDeviceInfoDAO();
            List<TerminalStatus> allDeviceList = deviceInfoDao.getWorkingDeviceStatus();

            // 4, Makes terminal groups by till id.
            resultBase = groupByTillId(allDeviceList, thisBusinessDay, companyId, storeId, terminalId);

        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "DaoException thrown:"
                            + ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TerminalId:" + terminalId + ":"
                            + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Exception thrown:"
                            + ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TerminalId:" + terminalId + ":"
                            + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase);
        }
        return resultBase;
    }
    
    /**
     * Gets all the Indicator from RESMaster.dbo.PRM_DEVICE_INDICATOR
     * @param attributeid - attribute ID
     * @return Indicators
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getdeviceindicators")
    @ApiOperation(value="インジケーター情報を得る", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_NO_BIZDATE, message="無効な営業日"),
        @ApiResponse(code=ResultBase.RESDEVCTL_INVALIDPARAMETER, message="無効のパラメータ")
    })
    public final Indicators getDeviceIndicators(
            @ApiParam(name="attributeid", value="ターミナル") @QueryParam("attributeid") final String attributeid) {
        final String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("attributeid", attributeid);

        Indicators indicatorList = new Indicators();

        // 1, check for required parameters
        if (StringUtility.isNullOrEmpty(attributeid)) {
            indicatorList.setMessage("Required fields are empty");
            indicatorList.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.println("A required parameter is null or empty.");
            tp.methodExit(indicatorList);
            return indicatorList;
        }

        try{
            // 2, Gets all the deviceIndicators by selecting RESMaster.PRM_DEVICE_INDICATOR
            IDeviceInfoDAO deviceInfoDao = daoFactory.getDeviceInfoDAO();
            indicatorList = deviceInfoDao.getDeviceIndicators(attributeid);

        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "DaoException thrown:"
                            + ":attributeid:" + attributeid
                            + ex.getMessage());
            indicatorList.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Exception thrown:"
                            + ":attributeid:" + attributeid
                            + ex.getMessage());
            indicatorList.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(indicatorList);
        }
        return indicatorList;
    }

    /**
     * Makes terminal groups by till id.
     * @param allTerminals Terminals on AUT_DEVICES.
     * @param thisBusinessDay this Business day.
     * @param companyId Company Id.
     * @param storeId Store Id.
     * @param terminalId Terminal Id.
     * @return WorkingDevices
     * @throws ParseException
     */
    private WorkingDevices groupByTillId(List<TerminalStatus> allTerminals,
                                         String thisBusinessDay,
                                         String companyId,
                                         String storeId,
                                         String terminalId) throws ParseException {
        // Parses business day to Timestamp.
        Timestamp thisBusinessDatetime = new Timestamp(DateFormatUtility.parseDate(thisBusinessDay,"yyyy-MM-dd").getTime());

        // Stores all the active terminals.
        List<TerminalStatus> activeTerminals = new ArrayList<>();
        // Stores all the active terminals by till groups.
        Map<String, TerminalTillGroup> activeGroups = new HashMap<>();
        // Keeps a reference to a group which API caller belongs to.
        TerminalTillGroup ownGroup = null;
        String tillId = null;
        for(TerminalStatus terminal : allTerminals) {
            // Checks if the terminal is active on this business day.
            if(isTerminalWorking(terminal, thisBusinessDatetime)) {
                activeTerminals.add(terminal);

                // Adds an entry to till id group.
                TerminalTillGroup tillGroup = activeGroups.get(terminal.getTillId());
                if(tillGroup == null) {
                    // New Till Id group.
                    tillGroup = new TerminalTillGroup(terminal.getTillId());
                    activeGroups.put(terminal.getTillId(), tillGroup);
                }
                tillGroup.add(terminal);
                if(companyId.equals(terminal.getCompanyId())  &&
                        storeId.equals(terminal.getStoreId()) &&
                        terminalId.equals(terminal.getTerminalId())) {
                    tillId = terminal.getTillId();
                }
            }
        }
        
        // This terminal belongs to the group, so keeps the reference as own group.
        ownGroup = activeGroups.get(tillId);
        // Creates WorkingDevices for JSON return.
        WorkingDevices normalReturn = new WorkingDevices();
        normalReturn.setNCRWSSResultCode(ResultBase.RES_OK);
        normalReturn.setActiveTerminals(activeTerminals);
        normalReturn.setTillGroups(new ArrayList<TerminalTillGroup>(activeGroups.values()));
        normalReturn.setOwnTillGroup(ownGroup);
        return normalReturn;
    }

    /**
     * Checks if the given terminal is considered as active and working this business day.
     * @param device
     * @param thisBusinessDatetime
     * @return true : terminal is opened and active.
     *          false : terminal is closed or inactive on this business day.
     */
    private boolean isTerminalWorking(TerminalStatus device, Timestamp thisBusinessDatetime) {
        // 0-1, Validates sodTime, If SodTime is null, this means the terminal never gets available.
        if(device.getSodTime() == null) {
            // This terminal has been inactive.
            return false;
        }
        // 0-2, Validates eodTIme, if OpenCloseStat == 4,Eod time must be given.
        if(device.getOpenCloseStat() == POSCTRL_OPEN_CLOSE_STAT_CLOSED && device.getEodTime() == null) {
            // This unlikely happens.
            return false;
        }
        // 1, Terminal which already finishes EOD and SOD on the business day has to be inactive as closed.
        if(device.getOpenCloseStat() == POSCTRL_OPEN_CLOSE_STAT_CLOSED &&
                device.getSodTime().after(thisBusinessDatetime) &&
                device.getEodTime().after(thisBusinessDatetime )) {
            return false;
        }
        // 2, Terminal whose SodTIme is past means inactive as not opened on the day.
        if(device.getSodTime().before(thisBusinessDatetime)) {
            return false;
        }
        // Otherwise, active.
        return true;
    }

}
