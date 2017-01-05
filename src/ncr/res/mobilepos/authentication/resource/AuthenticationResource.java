package ncr.res.mobilepos.authentication.resource;

import java.util.regex.Pattern;

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

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.authentication.dao.IAuthDeviceDao;
import ncr.res.mobilepos.authentication.model.ActivationKey;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.model.SignDetails;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.ApiRestriction;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * AuthenticationResource Class is a Web Resource which support
 * MobilePOS authentication processes.
 *
 */

@Path("/authentication")
@Api(value="/authentication", description="認証関連API")
public class AuthenticationResource {
    /**
     * the class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * constant. the company id of guest accounts.
     */
    private static final String GUEST_CORP_ID = "9999";

    /**
     * the class instance of the servelet context.
     */
    @Context private ServletContext context;
    /**
     * class instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * Default constructor.
     */
    public AuthenticationResource() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Device authentication with device id represented
     *  in path via POST request.
     * example: http://localhost/ncr.res.mobilepos.authentication
     *          /rest/authentication/TERMINALNO
     *          
     * @method POST
     * 
     * @param storeid - the store id
     * @param terminalid - the terminal id
     * 
     * @return  returns Authentication Result
     * structure containing the result status code.
     */
    @Path("/{terminalid}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="デバイス認証", response=DeviceStatus.class)
    @ApiResponses(value={
	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
	    @ApiResponse(code=ResultBase.RESREG_INVALIDPARAMETER_DEVID, message="デバイスコード無効")
    })
    public final DeviceStatus authenticateDevice(
            @ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyid,
            @ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeid,
            @ApiParam(name="terminalid", value="端末コード") @PathParam("terminalid") final String terminalid,
            @ApiParam(name="udid", value="UDID") @FormParam("udid") final String udid,
            @ApiParam(name="uuid", value="UUID") @FormParam("uuid") final String uuid) {

        tp.methodEnter("authenticateDevice");
        tp.println("companyid", companyid)
            .println("storeid", storeid)
        	.println("terminalid", terminalid)
        	.println("udid", udid)
        	.println("uuid", uuid);

        DeviceStatus result = null;
        int resultCode;

        try {
            //checking for non-alphanumeric characters
            if (hasNonAlpha(terminalid)) {
                tp.println("terminalid is invalid.");
                return new DeviceStatus(
                        ResultBase.RESREG_INVALIDPARAMETER_DEVID,
                        "Device Id contains invalid characters.");
            }

            IAuthDeviceDao deviceDao = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getAuthDeviceDAO(); 
            resultCode = deviceDao.authenticateUser(storeid, terminalid);

            if (resultCode == DeviceStatus.STATUS_DEVICESTATUSNOCHANGE) {
                resultCode = ResultBase.RESAUTH_OK;
            }

            result = new DeviceStatus(resultCode, "Device authentication result");
            SignDetails signDetails = deviceDao.getSignDetails(companyid, storeid,
                    terminalid, udid, uuid);
            int signStatus = signDetails.getSignStatus();

            //fill the DeviceStatus
            if (resultCode == ResultBase.RESAUTH_OK) {
            	result.setStoreID(storeid);
                result.setTerminalID(terminalid);
                result.setSignStatus(signStatus);
            }
        } catch (DaoException e) {
            LOGGER.logAlert("AuthRes", "authenticateDevice",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result =  new DeviceStatus(ResultBase.RES_ERROR_DB,
                    e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("AuthRes", "authenticateDevice",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result =  new DeviceStatus(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(result);
        }
        return result;
    }
    
    /**
     * Check if the string has non-alpha numeric characters.
     * @param str - the string to check
     * @return true if there are present, false if not
     */
    private boolean hasNonAlpha(final String str) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        return p.matcher(str).find();
    }
    
    /**
     * sets the servelet context.
     * used for unit test
     * @param contextToSet - the context to be set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * Set the signature service activation status of the device.
     * @method POST
     * @param corpId        The Company Id
     * @param storeId        The Store Id
     * @param terminalId        The Terminal Id
     * @param udid        The unique device number of the device
     * @param uuid        The random generated key for the device
     * @param signStatus    The status for the signature
     * @param signTid       The signature tid
     * @param signActivationKey The activation key of the signature
     * @return                The Restriction Code
     */
    @Path("/setSignatureActivationStatus")
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="シグネチャーテータス設定", response=DeviceStatus.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESREG_INVALIDPARAMETER_DEVID, message="デバイスコード無効")
    })
    public final DeviceStatus setSignatureActivationStatus(
    		@ApiParam(name="corpid", value="会社コード") @FormParam("corpid") final String corpId,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeId,
    		@ApiParam(name="terminalid", value="端末コード") @FormParam("terminalid") final String terminalId,
    		@ApiParam(name="udid", value="UDID") @FormParam("udid") final String udid,
    		@ApiParam(name="uuid", value="UUID") @FormParam("uuid") final String uuid,
    		@ApiParam(name="signstatus", value="シグネチャーテータス") @FormParam("signstatus") final int signStatus,
    		@ApiParam(name="signtid", value="シグネチャーTID") @FormParam("signtid") final String signTid,
    		@ApiParam(name="signactivationkey", value="シグネチャー活性キー") @FormParam("signactivationkey") final String signActivationKey) {
        tp.methodEnter("setSignatureActivationStatus");
        tp.println("corpId", corpId)
        	.println("storeId", storeId)
            .println("terminalId", terminalId)
            .println("udid", udid)
            .println("uuid", uuid)
            .println("signStatus", signStatus)
            .println("signTid", signTid)
            .println("signActivationKey", signActivationKey);

        DeviceStatus result = new DeviceStatus(DeviceStatus.RESAUTH_OK, "");
        int resultCode = DeviceStatus.RESAUTH_OK;

        try {
            if (hasNonAlpha(terminalId)) {
                tp.println("terminalid is invalid");
                return new DeviceStatus(
                        ResultBase.RESREG_INVALIDPARAMETER_DEVID,
                        "Device Id contains invalid characters");
            }

            IAuthDeviceDao device =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                          .getAuthDeviceDAO();
            
            resultCode = device.setSignatureActivationStatus(storeId,
                    terminalId, udid, uuid, signStatus, signTid,
                    signActivationKey);
            result = new DeviceStatus(resultCode,
                    "Signature Activation status retrieval operation result");

            ActivationKey activationKey =
                new ActivationKey(signActivationKey, signTid);

            //fill the DeviceStatus
            if (resultCode == ResultBase.RESAUTH_OK) {
                result.setCorpID(corpId);
                result.setStoreID(storeId);
                result.setTerminalID(terminalId);
                result.setActivationKey(activationKey);
                result.setSignStatus(signStatus);
            }
        } catch (DaoException e) {
            LOGGER.logAlert("AuthRes", "setSignatureActivationStatus",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result =  new DeviceStatus(ResultBase.RES_ERROR_DB, e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("AuthRes", "setSignatureActivationStatus",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result =  new DeviceStatus(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(result);
        }

        return result;
    }
   
}


