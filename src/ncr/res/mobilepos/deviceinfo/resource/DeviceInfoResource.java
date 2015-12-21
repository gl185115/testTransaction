package ncr.res.mobilepos.deviceinfo.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.credential.dao.SQLServerCredentialDAO;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.deviceinfo.dao.ILinkDAO;
import ncr.res.mobilepos.deviceinfo.dao.SQLDeviceInfoDAO;
import ncr.res.mobilepos.deviceinfo.model.AttributeInfo;
import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.POSLinks;
import ncr.res.mobilepos.deviceinfo.model.PrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.Printers;
import ncr.res.mobilepos.deviceinfo.model.SearchedDevice;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

/**
 * DeviceInfoResource Web Resource Class.
 * WebService resource to manage PosTerminal link and
 * Printer associated with a mobile device.
 *
 */
@Path("/deviceinfo")
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
     * PeripheralDeviceControl default constructor.
     */
    public DeviceInfoResource() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
        daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
    }
    /**
     * Service resource to set the Pos Terminal
     * association for the device identified
     * by the terminal id.
     *
     *  @param corpid the current corpid
     *  @param storeid the current storeid
     *  @param terminalid the target terminal to set
     *                  the pos terminal link
     *  @param linkposterminalid the pos terminal id to link
     *
     * @return ResultBase indicates result of operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/setlinkposterminalid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase setLinkPosTerminalId(
            @FormParam("storeid") final String storeid,
            @FormParam("terminalid") final String terminalid,
            @FormParam("linkposterminalid") final String linkposterminalid) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeid", storeid)
				.println("terminalid", terminalid)
				.println("linkposterminalid", linkposterminalid);

        ResultBase result = new ResultBase();

        try {
            IDeviceInfoDAO iPerCtrlDao =
                daoFactory.getDeviceInfoDAO();
            result =
                iPerCtrlDao.setLinkPosTerminalId( storeid,
                        terminalid, linkposterminalid);
            if (ResultBase.RESDEVCTL_NOPOSTERMINALLINK
                    == result.getNCRWSSResultCode()) {
                DeviceInfo newDeviceInfo = new DeviceInfo();
                newDeviceInfo.setDeviceId(terminalid);
                newDeviceInfo.setRetailStoreId(storeid);
                newDeviceInfo.setLinkPOSTerminalId(linkposterminalid);
                newDeviceInfo.setPrinterId("0");
                result = iPerCtrlDao.createPeripheralDeviceInfo(newDeviceInfo);
            }

		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to set linkposterminalid.", ex);
			result = new ResultBase(ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set linkposterminalid.", ex);
			result = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(result);
		}

		return result;
    }
    /**
     * Service resource to set the Printer
     * association for the device identified
     * by the terminal id.
     *
     *  @param storeId the current storeid
     *  @param terminalId the target terminal to set
     *                  the pos terminal link
     *  @param printerId the printerid to link
     *
     * @return ResultBase indicates result of operation
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/setprinterid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase setPrinterId(
            @FormParam("retailstoreid") final String storeId,
            @FormParam("terminalid") final String terminalId,
            @FormParam("printerid") final String printerId) {

    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("retailstoreid", storeId)
            .println("terminalid", terminalId)
            .println("printerid", printerId);

        ResultBase result = new ResultBase();

        try {           
            IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
            // printerid = 0  local printer.
            // printerid = -1 clear the link of printer and device.
            if(!"0".equals(printerId) && !"-1".equals(printerId)  && 
            		!StringUtility.isNullOrEmpty(printerId)){
                PrinterInfo printInfo =
                        iPerCtrlDao.getPrinterInfo(storeId, printerId);
                if (printInfo == null) {
                    result.setNCRWSSResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
                    result.setMessage("printer does not exist");
                    return result;
                }
            }
            String updAppId = pathName.concat(".setprinterid");
            result = iPerCtrlDao.setPrinterId(storeId,
                    terminalId, printerId,updAppId,getOpeCode());
            if (ResultBase.RESDEVCTL_NOPOSTERMINALLINK
                    == result.getNCRWSSResultCode()) {
                DeviceInfo newDeviceInfo = new DeviceInfo();
                newDeviceInfo.setDeviceId(terminalId);
                newDeviceInfo.setRetailStoreId(storeId);
                newDeviceInfo.setLinkPOSTerminalId("");
                newDeviceInfo.setUpdAppId(pathName.concat(".setprinterid"));
                newDeviceInfo.setUpdOpeCode(getOpeCode());
                newDeviceInfo.setPrinterId(printerId);
                result = iPerCtrlDao.createPeripheralDeviceInfo(newDeviceInfo);
            }
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to set printerid.", ex);
			result = new ResultBase(ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set printerid.", ex);
			result = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(result);
		}

		return result;
    }
    /**
     * Service resource to set retrieve
     * all the registered printers
     * in the store configuration.
     *
     *  @param storeid the current storeid
     *  @param key the current search key
     *  @param name the current search for printer description
     *
     * @return Printers indicates result of operation
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getallprinters")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final Printers getAllPrinters(
            @QueryParam("storeid") final String storeid,
            @QueryParam("key") final String key,
            @QueryParam("name") final String name,
            @QueryParam("limit") final int limit) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeid", storeid)
				.println("key", key).println("name", name)
				.println("limit", limit);

        Printers result = new Printers();

        try {
            IDeviceInfoDAO iPerCtrlDao =
                daoFactory.getDeviceInfoDAO();

            List<PrinterInfo> allprinter =
                iPerCtrlDao.getAllPrinterInfo(storeid,key,name,limit);

            if (!allprinter.isEmpty()) {
                PrinterInfo[] arrayPrinters =
                    new PrinterInfo[allprinter.size()];
                allprinter.toArray(arrayPrinters);
                result.setPrinters(arrayPrinters);
                result.setNCRWSSResultCode(ResultBase.RESDEVCTL_OK);
                result.setMessage("retrieved printers");
            } 

		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get Peripheral Device Info.",
					ex);
			result = new Printers(ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get Peripheral Device Info.",
					ex);
			result = new Printers(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(result);
		}

		return result;
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
    public final ViewDeviceInfo getDeviceInfo(
		@PathParam("companyid") final String companyId,
		@PathParam("retailstoreid") final String retailStoreID,
		@PathParam("deviceid") final String deviceID,
        @PathParam("trainingmode") final int trainingMode) {
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
     * Deletes device entry in MST_DEVICEINFO.
     *
     * @param deviceID
     *            The device to delete.
     * @param retailStoreID
     *            The storeid where the device belongs.
     * @return the ResultBase that holds the resultcode. 0 for success.
     *
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase deleteDevice(
            @FormParam("deviceid") final String deviceID,
            @FormParam("retailstoreid") final String retailStoreID,
            @FormParam("training") final int training,
            @FormParam("companyid") final String companyId) {
        
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
			.println("deviceid", deviceID)
			.println("retailstoreid", retailStoreID)
			.println("training", training)
			.println("companyid", companyId);

        ResultBase resultBase = new ResultBase();

        if (StringUtility.isNullOrEmpty(deviceID, retailStoreID)) {
            resultBase.setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("RetailStoreID or DeviceID is null or empty.");
            tp.methodExit(resultBase);
            return resultBase;
        }
        
        if (!deviceExists(deviceID, retailStoreID, training, companyId)) {
            resultBase.setNCRWSSResultCode(ResultBase.RESDEVCTL_NOTFOUND);
            tp.println("Device is not found.");
            tp.methodExit(resultBase);
            return resultBase;
        }
        
        if (deviceInUse(deviceID, retailStoreID)) {
            resultBase.setNCRWSSResultCode(ResultBase.RES_DEVICE_IS_IN_USE);
            tp.println("Device is currently used.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        try {
        	IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
            String appId  = pathName.concat(".delete");
            String opeCode = getOpeCode();
            resultBase = iPerCtrlDao.deleteDevice(deviceID, retailStoreID, appId, opeCode);
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to delete device.", ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DAO, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to delete device.", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase);
		}
		return resultBase;
	}

    private boolean deviceExists(String deviceID, String retailStoreID, int training, String companyId) {
        ViewDeviceInfo viewDeviceInfo = getDeviceInfo(companyId, deviceID, retailStoreID, training);
        if (viewDeviceInfo.getNCRWSSResultCode() != ResultBase.RES_OK || 
    		"Deleted".equals(viewDeviceInfo.getDeviceInfo().getStatus())) {
            return false;
        }
        return true;
    }
    
    private boolean deviceInUse(String deviceId, String storeId)  {
        try {
            SQLServerCredentialDAO credential = new SQLServerCredentialDAO();
            List<Employee> employees = credential.listOperators(null, null, null, -1);//-1 means list All
            for(Employee emp: employees){
                if(deviceId.equals(emp.getWorkStationID()) && storeId.equals(emp.getRetailStoreID()) && "Active".equals(emp.getStatus())){
                    return true;
                }
            }
        } catch (DaoException ex) {
        	String functionName = DebugLogger.getCurrentMethodName();
        	LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to list operators.", ex);
        }
        return false;
    }
    
    /**
     * Search devices from MST_DEVICEINFO.
     *
     * @param retailstoreid
     *      the storeid to search
     * @param key
     *      the key to search. Either a device id or name.
     * @param name
     *      the device name to search
     * @param limit
     *      the limit for number of records to list
     *      0 = Use the Default System Search Limit,
     *      < 0 = Search on all stores
     *     n > 0 = limit value
     * @return
     *      the List of Devices with ResultCode. 0 for success.
     */
    @Path("/list")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final SearchedDevice listDevices(
    		@QueryParam("retailstoreid") final String storeId,
    		@QueryParam("key") final String key,
            @QueryParam("name") final String name,
            @QueryParam("limit") final int limit) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("retailstoreid", storeId)
				.println("key", key).println("name", name)
				.println("limit", limit);
		
        SearchedDevice searchedDevice = new SearchedDevice();
        searchedDevice.setDevices(new ArrayList<DeviceInfo>());
        if(!StringUtility.isNullOrEmpty(storeId) && !storeExists(storeId)){
            tp.println("New StoreId does not exist");
            searchedDevice.setNCRWSSResultCode(
                    ResultBase.RES_STORE_NOT_EXIST);
            return searchedDevice;
        }
        
        int searchLimit = (limit == 0) ? GlobalConstant.getMaxSearchResults() : limit;
        
        try {
        	IDeviceInfoDAO iPerCtrlDao =
                daoFactory.getDeviceInfoDAO();
			searchedDevice.setDevices(iPerCtrlDao.listDevices(storeId, key,
					name, searchLimit));
		} catch (DaoException daoEx) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get the list of devices.",
					daoEx);
			searchedDevice = new SearchedDevice(
					(daoEx.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, daoEx);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get the list of devices.", ex);
			searchedDevice = new SearchedDevice(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(searchedDevice);
		}

		return searchedDevice;
    }

   
    /**
     * Web Method call used to add a new Device Information in the DataBase.
     * @param deviceInfoJson    The new Device Information.
     * @return The ResultBase
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public final ResultBase createDevice(
			@FormParam("deviceinfo") final String deviceInfoJson) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("deviceinfo", deviceInfoJson);
        
        ResultBase resultBase = new ResultBase();
        
        try {
            JsonMarshaller<DeviceInfo> jsonMarshall =
                new JsonMarshaller<DeviceInfo>();
            DeviceInfo deviceInfo = jsonMarshall.unMarshall(deviceInfoJson,
                    DeviceInfo.class);
            String storeid = deviceInfo.getRetailStoreId();

            if (StringUtility.isNullOrEmpty(storeid)) {
                tp.println("Invalid Store ID.");
                resultBase.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALID_STOREID);
                return resultBase;
            }
            
            if(!storeExists(storeid)){
                tp.println("Store does not exist");
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_STORE_NOT_EXIST);
                return resultBase;
            }
            
            String deviceID = deviceInfo.getDeviceId();
            if (!StringUtility.isNullOrEmpty(deviceID)
                    && StringUtility
                        .isNumberFormatted(deviceInfo.getDeviceId())) {
                IDeviceInfoDAO deviceInfoDao =
                    daoFactory.getDeviceInfoDAO();
                deviceInfo.setUpdAppId(pathName.concat(".add"));
                deviceInfo.setUpdOpeCode(getOpeCode());
                resultBase =
                    deviceInfoDao.createPeripheralDeviceInfo(deviceInfo);
            } else {
                resultBase.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALID_DEVICEID);
                tp.println("DeviceID for the new device is invalid.");
            }
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to create device with "
							+ deviceInfoJson, ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DAO, ex);
		} catch (IOException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + ": Failed to create device with "
							+ deviceInfoJson, ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
					ResultBase.RESDEVCTL_INVALIDPARAMETER, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to create device with "
							+ deviceInfoJson, ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
        return resultBase;
    }

  
    /**
     * Deletes registered device from AUT_DEVICES table.
     *
     * @param deviceID
     *            the deviceid to delete.
     * @param storeID
     *            the store where the device belongs.
     * @return ResultBase object with resultcode that determines if request is
     *         successful. 0 for success.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/delete/registration")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public final ResultBase deleteRegisteredDevice(
			@FormParam("deviceid") final String deviceID,
			@FormParam("retailstoreid") final String storeID) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("deviceid", deviceID)
				.println("retailstoreid", storeID);

        ResultBase resultBase = new ResultBase();
        if (null == deviceID || null == storeID
                || deviceID.isEmpty() || storeID.isEmpty()) {
            resultBase
                    .setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("retailstoreid/deviceid/corpid is null or empty.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        try {
            IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
            resultBase = iPerCtrlDao.deleteRegisteredDevice(deviceID, storeID);
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to delete registered device.", ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DAO, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to delete registered device.", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase);
		}
		return resultBase;
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
    public final ViewDeviceInfo updateDevice(
		@FormParam("companyid") final String companyID,
        @FormParam("retailstoreid") final String retailStoreID,
        @FormParam("deviceid") final String deviceID,            
        @FormParam("deviceinfo") final String jsonDeviceInfo,
        @FormParam("trainingmode") final int trainingMode) {
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
     * Creates the printer.
     *
     * @param storeID
     *            the store id where the printer belongs.
     * @param printerID
     *            the printer identifier.
     * @param jsonPrinterInfo
     *            the printer information in json format.
     * @return 0 for success, otherwise fail.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/printer/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public final ResultBase createPrinter(
			@FormParam("retailstoreid") final String storeId,
			@FormParam("printerid") final String printerId,
			@FormParam("printerinfo") final String jsonPrinterInfo) {
        
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("retailstoreid", storeId)
                .println("printerid", printerId)
                .println("printerinfo", jsonPrinterInfo);

        ResultBase resultBase = new ResultBase();
        if (StringUtility.isNullOrEmpty(storeId, printerId, jsonPrinterInfo)) {
            resultBase
                    .setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("Parameter[s] is empty or null.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        
        if(!storeExists(storeId)){
            resultBase.setNCRWSSResultCode(ResultBase.RES_STORE_NOT_EXIST);
            tp.println("Store does not exist");
            tp.methodExit(resultBase);
            return resultBase;
        }
        
        try {
            IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
            JsonMarshaller<PrinterInfo> jsonMarshaller =
                new JsonMarshaller<PrinterInfo>();
            PrinterInfo printerInfo = jsonMarshaller.unMarshall(
                    jsonPrinterInfo, PrinterInfo.class);
            printerInfo.setUpdAppId(pathName.concat(".printer.create"));
            printerInfo.setUpdOpeCode(getOpeCode());
            resultBase = iPerCtrlDao.createPrinterInfo(storeId, printerId,
                    printerInfo);

        } catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": failed to create printer", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
					ResultBase.RES_ERROR_DB, ex);
		} catch (IOException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + ": failed to create printer", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
					ResultBase.RES_ERROR_IOEXCEPTION, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": failed to create printer", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase);
		}

		return resultBase;
	}

    /**
     * Updates Printer Info.
     *
     * @param storeId
     *            the store id where the poslinkid belongs.
     * @param printerId
     *            the POS Link identifier.
     * @param printerInfoJson
     *            the Printer Info data in JSON format.
     * @return the updated PrinterInfo with resultcode. 0 for success, otherwise,
     *         fail.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/printer/maintenance")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ViewPrinterInfo updatePrinterInfo(
    		@FormParam("retailstoreid") final String storeId,
            @FormParam("printerid") final String printerId,
            @FormParam("printerinfo") final String printerInfoJson) {
    	
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
                .println("retailstoreid", storeId)
                .println("printerid", printerId)
                .println("printerinfo", printerInfoJson);

        ViewPrinterInfo newPrinterInfo = new ViewPrinterInfo();

        if (StringUtility.isNullOrEmpty(storeId, printerId,
                printerInfoJson)) {
            newPrinterInfo
                    .setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("Parameter[s] is null or empty.");
            tp.methodExit(newPrinterInfo);
            return newPrinterInfo;
        }

        if (!printerExists(storeId, printerId)) {
            newPrinterInfo.setNCRWSSResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
            tp.println("Printer does not exist");
            tp.methodExit(newPrinterInfo);
            return newPrinterInfo;
        }

        
        try {

            JsonMarshaller<PrinterInfo> jsonMarshall =
                new JsonMarshaller<PrinterInfo>();
            PrinterInfo printerInfo = jsonMarshall.unMarshall(
                    printerInfoJson, PrinterInfo.class);

            // check that new storeid should be existing
            String newStoreId = printerInfo.getRetailStoreId();
            if (newStoreId != null && !storeExists(newStoreId)) {
                    newPrinterInfo.setNCRWSSResultCode(
                            ResultBase.RES_STORE_NOT_EXIST);
                    tp.println("New Store does not exist");
                    return newPrinterInfo;  
            }

            IDeviceInfoDAO deviceInfoDao = daoFactory
                    .getDeviceInfoDAO();
            printerInfo.setUpdAppId(pathName.concat(".printer.maintenance"));
            printerInfo.setUpdOpeCode(getOpeCode());
            newPrinterInfo = deviceInfoDao.updatePrinterInfo(storeId,
                    printerId, printerInfo);
    	} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to update printer info.", ex);
			newPrinterInfo = new ViewPrinterInfo(ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (IOException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + ": Failed to update printer info.", ex);
			newPrinterInfo = new ViewPrinterInfo(
					ResultBase.RES_ERROR_IOEXCEPTION,
					ResultBase.RESDEVCTL_INVALIDPARAMETER, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to update printer info.", ex);
			newPrinterInfo = new ViewPrinterInfo(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(newPrinterInfo);
		}

		return newPrinterInfo;
	}
    
    /*
    * Web Method call used to delete a printerinfo in the DataBase.
    * @param storeid    The storeid.
    * @param printerid    The printerid.
    * @return The ResultBase
    */
   @POST
   @Produces({MediaType.APPLICATION_JSON })
   @Path("/printer/delete")
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public final ResultBase deletePrinter(
           @FormParam("retailstoreid") final String storeid,
           @FormParam("printerid") final String printerid) {
       
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("retailstoreid", storeid)
				.println("printerid", printerid);

       ResultBase resultBase = new ResultBase();

       try {

           if (StringUtility.isNullOrEmpty(storeid, printerid)) {
               tp.println("RetailStoreID or PrinterID is null or empty.");
               resultBase.setNCRWSSResultCode(
                       ResultBase.RESDEVCTL_INVALIDPARAMETER);
               return resultBase;
           }
       

           if (!printerExists(storeid, printerid)) {
               tp.println("Printer does not exist");
               resultBase.setNCRWSSResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
               return resultBase;
           }
           
           IDeviceInfoDAO deviceInfoDao = daoFactory.getDeviceInfoDAO();
           String updAppId = pathName.concat(".printer.delete");
           resultBase = deviceInfoDao.deletePrinter(storeid, printerid,updAppId, getOpeCode());

		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to delete printer.", ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DAO, ex);

		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to delete printer.", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
		return resultBase;
	}
    
    
    /**
     * Web Method call used to add a new Links in the DataBase.
     * @param linktype  The type of link to add to
     * @param storeid    The storeid.
     * @param linkid    The link id.
     * @param linkinfoJson    The link info.
     * @return The ResultBase
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/link/{linktype}/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public final ResultBase createLink(
			@PathParam("linktype") final String linktype,
			@FormParam("retailstoreid") final String storeid,
			@FormParam("poslinkid") final String linkid,
			@FormParam("poslinkinfo") final String linkinfoJson) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("linktype", linktype)
				.println("retailstoreid", storeid).println("poslinkid", linkid)
				.println("poslinkinfo", linkinfoJson);

        ResultBase resultBase = new ResultBase();
        
        try {
        	if (StringUtility.isNullOrEmpty(linkid)) {
                tp.println("Invalid link id.");
                resultBase.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALIDPARAMETER);
                return resultBase;
            }

            if (StringUtility.isNullOrEmpty(linkinfoJson)) {
                tp.println("Invalid link info.");
                resultBase.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALIDPARAMETER);
                return resultBase;
            }

            if (StringUtility.isNullOrEmpty(storeid)) {
                tp.println("Invalid Store ID.");
                resultBase.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALIDPARAMETER);
                return resultBase;
            } else {
                StoreResource storeRes = new StoreResource();
                ViewStore store = storeRes.viewStore(storeid);
                if (store.getNCRWSSResultCode() != ResultBase.RES_STORE_OK) {
                    resultBase.setNCRWSSResultCode(
                            ResultBase.RES_STORE_NOT_EXIST);
                    tp.methodExit("retailStoreID does not exist in database.");
                    return resultBase;
                }
            }

            if (StringUtility.isNullOrEmpty(linkid)) {
                resultBase.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALIDPARAMETER);
                tp.println("Link id for the new link is invalid.");
                return resultBase;
            }

            JsonMarshaller<POSLinkInfo> jsonMarshall =
                new JsonMarshaller<POSLinkInfo>();
            POSLinkInfo poslinkinfo = jsonMarshall.unMarshall(linkinfoJson,
                    POSLinkInfo.class);
            poslinkinfo.setUpdAppId(pathName.concat(".link.add"));
            poslinkinfo.setUpdOpeCode(getOpeCode());

            if (QUEUEBUSTER_LINK_TYPE.equals(linktype)) {
                ILinkDAO queueBusterLinkDao = daoFactory
                        .getQueueBusterLinkDAO();
                resultBase = queueBusterLinkDao.createLink(storeid, linkid,
                        poslinkinfo);
            } else if (CREDITAUTH_LINK_TYPE.equals(linktype)) {
                ILinkDAO creditAuthLinkDao = daoFactory
                        .getCreditAuthorizationLinkDAO();
                resultBase = creditAuthLinkDao.createLink(storeid, linkid,
                        poslinkinfo);
            } else if (SIGNATURE_LINK_TYPE.equals(linktype)) {
                ILinkDAO signatureLinkDao = daoFactory
                        .getSignatureLinkDAO();
                resultBase = signatureLinkDao.createLink(storeid, linkid,
                        poslinkinfo);
            } else {
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_LINK_TYPEINVALID);
                tp.println("link type is invalid.");
            }
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to create link with "
							+ linkinfoJson, ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DAO, ex);
		} catch (IOException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + ": Failed to create link with "
							+ linkinfoJson, ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
					ResultBase.RESDEVCTL_INVALIDPARAMETER, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to create link with "
							+ linkinfoJson, ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
		return resultBase;
    }

    /**
     * Web Method call used to delete a Link in the DataBase.
     * @param linkType  The link type
     * @param storeId    The storeid.
     * @param linkId    The link id.
     * @return The ResultBase
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/link/{linktype}/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase deleteLink(
            @PathParam("linktype") final String linkType,
            @FormParam("retailstoreid") final String storeId,
            @FormParam("poslinkid") final String linkId) {
        
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("linktype", linkType)
				.println("retailstoreid", storeId).println("poslinkid", linkId);

        ResultBase resultBase = new ResultBase();

        try {

            if (StringUtility.isNullOrEmpty(storeId, linkId)) {
                tp.println("Invalid Store ID.");
                resultBase.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALIDPARAMETER);
                return resultBase;
            }
            
            ILinkDAO linkDao = null;

            if (QUEUEBUSTER_LINK_TYPE.equals(linkType)) {
                linkDao = daoFactory
                        .getQueueBusterLinkDAO();
                
            } else if (CREDITAUTH_LINK_TYPE.equals(linkType)) {
                linkDao = daoFactory
                        .getCreditAuthorizationLinkDAO();
               
            } else if (SIGNATURE_LINK_TYPE.equals(linkType)) {
                linkDao =
                    daoFactory.getSignatureLinkDAO();
                 
            } else {
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_LINK_TYPEINVALID);
                tp.println("link type is invalid.");
                tp.methodExit(resultBase);
                return resultBase;
            }
            
            
            if(!linkExists(storeId, linkType, linkId)){
                resultBase.setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                tp.println("POSLink is already deleted.");
                tp.methodExit(resultBase);
                return resultBase;
            }
            
            if(linkDao != null){
                String appId = pathName.concat(".link.delete");
                String opeCode = getOpeCode();
                resultBase = linkDao.deleteLink(storeId, linkId, appId, opeCode);
            }else{
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                tp.println("Error in retrieving DAO.");
                tp.methodExit(resultBase);
                return resultBase;
            }
            
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to delete link.", ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DAO, ex);
		} catch (IOException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + ": Failed to delete link.", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
					ResultBase.RESDEVCTL_INVALIDPARAMETER, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to delete link.", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
		return resultBase;
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
	public final ViewPosLinkInfo getLinkItem(
			@PathParam("linktype") final String linkType,
			@PathParam("retailstoreid") final String storeId,
			@PathParam("poslinkid") final String posLinkId) {
    	
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
    public final POSLinks getLinksList(
            @PathParam("linktype") final String linkType,
            @QueryParam("storeid") final String storeId,
            @QueryParam("key") final String key,
            @QueryParam("name") final String name,
            @QueryParam("limit") final int limit) {

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
     * Updates link.
     *
     * @param linkType
     *            the type of link to update.
     * @param storeId
     *            the store id where the poslinkid belongs.
     * @param posLinkId
     *            the POS Link identifier.
     * @param posLinkInfoJson
     *            the POS Link data in JSON format.
     * @return the updated POSLink with resultcode. 0 for success, otherwise,
     *         fail.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/link/{linktype}/maintenance")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ViewPosLinkInfo updateLink(
            @PathParam("linktype") final String linkType,
            @FormParam("retailstoreid") final String storeId,
            @FormParam("poslinkid") final String posLinkId,
            @FormParam("poslinkinfo") final String posLinkInfoJson) {
    	
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("linktype", linkType)
                .println("retailstoreid", storeId)
                .println("poslinkid", posLinkId)
                .println("poslinkinfo", posLinkInfoJson);

        ViewPosLinkInfo newPosLinkInfo = new ViewPosLinkInfo();

        if (StringUtility.isNullOrEmpty(linkType, storeId, posLinkId,
                posLinkInfoJson)) {
            newPosLinkInfo
                    .setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            tp.println("Parameter[s] is null or empty.");
            tp.methodExit(newPosLinkInfo);
            return newPosLinkInfo;
        }

        
        try {

            JsonMarshaller<POSLinkInfo> jsonMarshall =
                new JsonMarshaller<POSLinkInfo>();
            POSLinkInfo posLinkInfo = jsonMarshall.unMarshall(
                    posLinkInfoJson, POSLinkInfo.class);
            

            // check that new storeid should be existing
            String newStoreID = posLinkInfo.getRetailStoreId();
            String newPosLinkId = posLinkInfo.getPosLinkId(); 
            if(newPosLinkId!=null && StringUtility.isEmpty(newPosLinkId)){
                newPosLinkInfo.setNCRWSSResultCode(
                        ResultBase.RESDEVCTL_INVALID_POSLINKID);
                tp.println("Invalid PosLinkId");
                return newPosLinkInfo;    
            }
            
            if (newStoreID != null) {
                if(StringUtility.isEmpty(newStoreID)){
                    newPosLinkInfo.setNCRWSSResultCode(
                            ResultBase.RESDEVCTL_INVALID_STOREID);
                    tp.println("Invalid StoreId!");
                    return newPosLinkInfo; 
                }
                if (!storeExists(newStoreID)) {
                    newPosLinkInfo.setNCRWSSResultCode(
                            ResultBase.RES_STORE_NOT_EXIST);
                    tp.println("Store does not exist");
                    return newPosLinkInfo; 
                }
                
            }
            
            posLinkInfo.setUpdAppId(pathName.concat(".link.maintenance"));
            posLinkInfo.setUpdOpeCode(getOpeCode());

            if (QUEUEBUSTER_LINK_TYPE.equals(linkType)) {
                ILinkDAO queueBusterLinkDao = daoFactory
                        .getQueueBusterLinkDAO();
                newPosLinkInfo = queueBusterLinkDao.updateLink(storeId,
                        posLinkId, posLinkInfo, null);
            } else if (CREDITAUTH_LINK_TYPE.equals(linkType)) {
                ILinkDAO creditAuthLinkDao = daoFactory
                        .getCreditAuthorizationLinkDAO();
                newPosLinkInfo = creditAuthLinkDao.updateLink(storeId,
                        posLinkId, posLinkInfo, null);
            } else if (SIGNATURE_LINK_TYPE.equals(linkType)) {
                ILinkDAO  signatureLinkDao = daoFactory
                        .getSignatureLinkDAO();
                newPosLinkInfo = signatureLinkDao.updateLink(storeId,
                        posLinkId, posLinkInfo, null);
            } else {
                newPosLinkInfo
                        .setNCRWSSResultCode(ResultBase.RES_LINK_TYPEINVALID);
                tp.println("Link type is invalid.");
            }

		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to update link.", ex);
			newPosLinkInfo = new ViewPosLinkInfo(ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (IOException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + ": Failed to update link.", ex);
			newPosLinkInfo = new ViewPosLinkInfo(
					ResultBase.RES_ERROR_IOEXCEPTION,
					ResultBase.RES_ERROR_INVALIDPARAMETER, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to update link.", ex);
			newPosLinkInfo = new ViewPosLinkInfo(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(newPosLinkInfo);
		}

		return newPosLinkInfo;
    }

    /**
     * Web Method call used to set SignatureLink to a device.
     * @param retailstoreid     The Retail Store ID.
     * @param terminalid        The Device ID.
     * @param signaturelink     The POS Link ID for Signature.
     * @return ResultBase with result code.
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/setsignaturelink")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase setSignatureLink(
            @FormParam("retailstoreid") final String retailstoreid,
            @FormParam("terminalid") final String terminalid,
            @FormParam("signaturelink") final String signaturelink) {
       
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("retailstoreid", retailstoreid)
				.println("terminalid", terminalid)
				.println("signaturelink", signaturelink);

		ResultBase resultBase = new ResultBase();

		try {

			if (!StringUtility.isNullOrEmpty(signaturelink)) {
				ViewPosLinkInfo qbInfo = this.getLinkItem(SIGNATURE_LINK_TYPE,
						retailstoreid, signaturelink);
				if (qbInfo.getNCRWSSResultCode() != ResultBase.RES_OK) {
					tp.println("Signature Link to set does not exist");
					resultBase
							.setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
					return resultBase;
				}
			}

			IDeviceInfoDAO deviceInfoDao = daoFactory.getDeviceInfoDAO();
			String appId = pathName.concat(".setsignaturelink");
			resultBase = deviceInfoDao.setSignatureLink(retailstoreid,
					terminalid, signaturelink, appId, getOpeCode());

		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to set signaturelink.", ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set signaturelink.", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
		return resultBase;
	}

    /**
     * Web Method call used to set AuthorizationLink to a device.
     * @param retailStoreId     The Retail Store ID.
     * @param terminalId        The Device ID.
     * @param authorizationLink     The POS Link ID for Signature.
     * @return ResultBase with result code.
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/setauthorizationlink")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase setAuthorizationLink(
            @FormParam("retailstoreid") final String retailStoreId,
            @FormParam("terminalid") final String terminalId,
            @FormParam("authorizationlink") final String authorizationLink) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("retailstoreid", retailStoreId)
				.println("terminalid", terminalId)
				.println("authorizationlink", authorizationLink);

		ResultBase resultBase = new ResultBase();

		try {

			if (!StringUtility.isNullOrEmpty(authorizationLink)) {
				ViewPosLinkInfo qbInfo = this.getLinkItem(CREDITAUTH_LINK_TYPE,
						retailStoreId, authorizationLink);
				if (qbInfo.getNCRWSSResultCode() != ResultBase.RES_OK) {
					tp.println("Authorization Link to set does not exist");
					resultBase
							.setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
					return resultBase;
				}
			}

			IDeviceInfoDAO deviceInfoDao = daoFactory.getDeviceInfoDAO();
			String appId = pathName.concat(".setauthlink");
			resultBase = deviceInfoDao.setAuthorizationLink(retailStoreId,
					terminalId, authorizationLink, appId, getOpeCode());

		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to set the authorization link.",
					ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set the authorization link.",
					ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
		return resultBase;
	}

    /**
     * Web Method call used to set QueueBuster Link
     * to a device.
     * @param storeid    The storeid.
     * @param terminalid    The terminal to link to.
     * @param queuebusterlink    The queuebuster link.
     * @return The ResultBase
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/setqueuebusterlink")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase setQueueBusterLink(
            @FormParam("retailstoreid") final String storeid,
            @FormParam("terminalid") final String terminalid,
            @FormParam("queuebusterlink") final String queuebusterlink) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("retailstoreid", storeid)
				.println("terminalid", terminalid)
				.println("queuebusterlink", queuebusterlink);
        
        ResultBase resultBase = new ResultBase();
        try {

            if (!StringUtility.isNullOrEmpty(queuebusterlink)) {
                ViewPosLinkInfo qbInfo = this.getLinkItem(
                        QUEUEBUSTER_LINK_TYPE, storeid, queuebusterlink);
                if (qbInfo.getNCRWSSResultCode() != ResultBase.RES_OK) {
                    tp.println("queuebusterlink to set does not exist.");                    
                    resultBase.setNCRWSSResultCode(
                            ResultBase.RES_LINK_NOTFOUND);
                    return resultBase;
                }
            }

            IDeviceInfoDAO deviceInfoDao = daoFactory
                    .getDeviceInfoDAO();
            String appId = pathName.concat(".setqueuebusterlink");
            resultBase = deviceInfoDao.setQueueBusterLink(
                    storeid, terminalid, queuebusterlink, appId, getOpeCode());

		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to set queuebusterlink.", ex);
			resultBase = new ResultBase(
					(ex.getCause() instanceof SQLException) ? ResultBase.RES_ERROR_DB
							: ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set queuebusterlink.", ex);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
		return resultBase;
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

    /**
     * Checks if printer is existing.
     *
     * @param printerID the printer identifier.
     * @return true, if printer exists.
     */
    private boolean printerExists(final String storeID, final String printerID){
        boolean isExist = true;
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        .println("retailstoreid", storeID)
        .println("printerID", printerID);
       
      PrinterInfo printerInfo = null;
      String functionName = className + "isPrinterExisting";
        try{
        	SQLDeviceInfoDAO deviceInfoDao = new SQLDeviceInfoDAO();
        	printerInfo = deviceInfoDao.getPrinterInfo(storeID, printerID);
        	if (printerInfo == null) {
        		isExist = false;
        	}
        } catch (DaoException ex) {
        LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                "Failed to update PrinterInfo.\n" + ex.getMessage());
        }catch (Exception ex) {
        	LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                "Failed to update PrinterInfo.\n" + ex.getMessage());
        } finally {
        	tp.methodExit(printerInfo);
        }
        return isExist;
    }
    
    /* Checks if store is existing.
    *
    * @param retailStoreID the store identifier.
    * @return true, if store exist.
    */
   private boolean linkExists(final String storeId, final String linkType, final String posLinkId) {
       boolean isExist = true;
       
       ViewPosLinkInfo viewPosLinkInfo = getLinkItem(linkType, storeId, posLinkId);
       if (viewPosLinkInfo.getPosLinkInfo() == null || 
    		   "Deleted".equalsIgnoreCase(viewPosLinkInfo.getPosLinkInfo().getStatus())) {
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
    public final ViewTill getAllTills(
            @QueryParam("storeid") final String storeid,
            @QueryParam("key") final String key,
            @QueryParam("limit") final int limit) {
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
     * Service resource to set the Till/Drawer
     * association for the device identified
     * by the terminal id.
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     * @param tillId		- The till/drawer identifier.
     * @return ResultBase	- The result of this operation.
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/settillid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase setTillId(
            @FormParam("storeid") final String storeId,
            @FormParam("terminalid") final String terminalId,
            @FormParam("tillid") final String tillId) {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", storeId)
            .println("terminalid", terminalId)
            .println("tillid", tillId);

        ResultBase result = new ResultBase();
        String updAppId = pathName.concat(".settillid");
        String opeCode = getOpeCode();
        
        try {           
            IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
            
            if(StringUtility.isNullOrEmpty(storeId)) {
            	tp.println("Invalid value for StoreId.");
            	result.setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            	result.setMessage("Invalid value for StoreId.");
            	tp.methodExit(result);
            	return result;
            }
            
            if(StringUtility.isNullOrEmpty(terminalId)) {
            	tp.println("Invalid value for TerminalId.");
            	result.setNCRWSSResultCode(ResultBase.RESDEVCTL_INVALIDPARAMETER);
            	result.setMessage("Invalid value for TerminalId.");
            	tp.methodExit(result);
            	return result;
            }
                                
            if(tillId != null) {
            	if(tillId.trim().isEmpty()) {
            		tp.println("Invalid value for TillId. TillId should not be empty.");
                	result.setNCRWSSResultCode(ResultBase.RES_TILL_INVALIDPARAMS);
                	result.setMessage("Invalid value for TillId. TillId should not be empty.");
                	tp.methodExit(result);
                	return result;
            	} else {
            		ViewTill viewTill = new TillInfoResource().viewTill(storeId, tillId);
	        	 
            		if(viewTill.getNCRWSSResultCode() != ResultBase.RES_OK) {
            			tp.println("Till does not exist.");
            			result.setNCRWSSResultCode(viewTill.getNCRWSSResultCode());
            			result.setMessage("Till does not exist.");
            			tp.methodExit(result);
            			return result;
            		}
            	}	
            }           
     
            result = iPerCtrlDao.setTillId(storeId,
                    terminalId, tillId, updAppId, opeCode);
            
            if (ResultBase.RESDEVCTL_NOTFOUND
                    == result.getNCRWSSResultCode()) {
                DeviceInfo newDeviceInfo = new DeviceInfo();
                newDeviceInfo.setDeviceId(terminalId);
                newDeviceInfo.setRetailStoreId(storeId);
                newDeviceInfo.setLinkPOSTerminalId("");
                newDeviceInfo.setUpdAppId(updAppId);
                newDeviceInfo.setUpdOpeCode(opeCode);
                newDeviceInfo.setTillId(tillId);
                result = iPerCtrlDao.createPeripheralDeviceInfo(newDeviceInfo);
            }
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to set TillId.", ex);
			result = new ResultBase(ResultBase.RES_ERROR_DAO,
					ResultBase.RES_ERROR_DB, ex);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set TillId.", ex);
			result = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(result);
		}
		return result;
    }

    /**
     * í[ññëÆê´èÓïÒéÊìæ
     * @param storeId		- The store identifier.
     * @param terminalId	- The terminal/device identifier.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/getattribute")
    public final AttributeInfo getAttribute(
            @QueryParam("storeId") final String storeId,
            @QueryParam("terminalId") final String terminalId) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		
		tp.methodEnter(functionName);
		tp.println("storeid", storeId)
		  .println("terminalId", terminalId);
		
		AttributeInfo result = new AttributeInfo();
		
		try{
			if(StringUtility.isNullOrEmpty(storeId, terminalId)){
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
				
				return result;
			}
			IDeviceInfoDAO iPerCtrlDao =
		               daoFactory.getDeviceInfoDAO();
			 
			result = iPerCtrlDao.getAttributeInfo(storeId, terminalId);
		}catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get the AttributeInfo.", ex);
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
					functionName + ": Failed to get the AttributeInfo.", ex);
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
    @Path("/getdeviceattribute")
    public final DeviceAttribute getDeviceAttribute(
            @QueryParam("companyId") final String companyId,
            @QueryParam("storeId") final String storeId,
            @QueryParam("terminalId") final String terminalId) {
    	
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
    public final ViewTerminalInfo getTerminalInfo(
		@QueryParam("companyId") final String companyId,
		@QueryParam("storeId") final String storeId,
		@QueryParam("terminalId") final String terminalId) {
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
}
