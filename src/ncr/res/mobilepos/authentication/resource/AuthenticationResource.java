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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value="/authentication", description="�F�؊֘AAPI")
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
    @ApiOperation(value="�f�o�C�X�F��", response=DeviceStatus.class)
    @ApiResponses(value={
	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
	    @ApiResponse(code=ResultBase.RESREG_INVALIDPARAMETER_DEVID, message="�f�o�C�X�R�[�h����")
    })
    public final DeviceStatus authenticateDevice(
            @ApiParam(name="companyid", value="��ЃR�[�h") @FormParam("companyid") final String companyid,
            @ApiParam(name="storeid", value="�X�܃R�[�h") @FormParam("storeid") final String storeid,
            @ApiParam(name="terminalid", value="�[���R�[�h") @PathParam("terminalid") final String terminalid,
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

    // Terminal/device deauthentication with terminal id represented in path
    // example: http://localhost/ncr.res.mobilepos.resauthentication
    //                /rest/authentication/TERMINALNO/deauth
    /**
     * Deauthenticates the device.
     * @method POST
     * @param corpid - the company id
     * @param storeid - the store id
     * @param terminalid - the terminal id
     * @param udid - the unique device number of the device
     * @param uuid - the random generated key for the device
     * @return  returns ResultBase
     * structure containing the result status code.
     */
    @Path("{terminalid}/deauth")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase deauthenticateDevice(
            @FormParam("corpid") final String corpid,
            @FormParam("storeid") final String storeid,
            @PathParam("terminalid") final String terminalid,
            @FormParam("udid") final String udid,
            @FormParam("uuid") final String uuid) {

        tp.methodEnter("deauthenticateDevice");
        tp.println("corpid", corpid).println("storeid", storeid)
            .println("terminalid", terminalid).println("udid", udid)
            .println("uuid", uuid);

        ResultBase result = new ResultBase(ResultBase.RESAUTH_OK, "");
        int resultCode = ResultBase.RESAUTH_OK;

        try {

            if (hasNonAlpha(terminalid)) {
                tp.println("terminalid is invalid");
                return new ResultBase(ResultBase.RESREG_INVALIDPARAMETER_DEVID,
                        "Device Id contains invalid characters");
            }

            IAuthDeviceDao device =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                          .getAuthDeviceDAO();
            resultCode =
                device.deauthenticateUser(storeid,
                        terminalid, uuid, udid);
            result = new ResultBase(resultCode,
                    "Device deauthentication result");

        } catch (DaoException e) {
            LOGGER.logAlert("AuthRes", "deauthenticateDevice",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result =
                new ResultBase(ResultBase.RES_ERROR_DB, e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("AuthRes", "deauthenticateDevice",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result =  new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    // Terminal/device status with terminal id represented
    // in path via GET request
    // example: http://localhost
    //          /ncr.res.mobilepos.resauthentication/rest
    //          /authentication/TERMINALNO
    /**
     * Gets the status of the device.
     * @method GET
     * @param corpid - the company id
     * @param storeid - the store id
     * @param terminalid - the terminal id
     * @return  returns DeviceStatus
     * structure containing the result status code.
     */
    @Path("{terminalid}/statusnew")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final DeviceStatus getDeviceStatus(
            @FormParam("corpid") final String corpid,
            @FormParam("storeid") final String storeid,
            @PathParam("terminalid") final String terminalid) {

        tp.methodEnter("getDeviceStatus");
        tp.println("corpid", corpid).println("storeid", storeid)
            .println("terminalid", terminalid);

        DeviceStatus result = new DeviceStatus(DeviceStatus.RESAUTH_OK, "");
        int resultCode = DeviceStatus.RESAUTH_OK;

        try {
            if (hasNonAlpha(terminalid)) {
                tp.println("terminalid is invalid");
                return new DeviceStatus(
                        ResultBase.RESREG_INVALIDPARAMETER_DEVID,
                        "Device Id contains invalid characters");
            }

            IAuthDeviceDao device =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                          .getAuthDeviceDAO();

            resultCode = device.getUserStatus(storeid, terminalid);
            result = new DeviceStatus(resultCode,
                    "Device status retrieval operation result");
        } catch (DaoException e) {
            LOGGER.logAlert("AuthRes", "getDeviceStatus",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result =  new DeviceStatus(ResultBase.RES_ERROR_DB, e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("AuthRes", "getDeviceStatus",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result =  new DeviceStatus(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(result);
        }

        return result;
    }
    /**
     * Gets the device status.
     * @method GET
     * @param terminalid - the terminal id
     * @return  returns DeviceStatus
     * structure containing the result status code.
     */
    @Path("{terminalid}/status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final DeviceStatus getDeviceStatus(
            @PathParam("terminalid") final String terminalid) {

        tp.methodEnter("getDeviceStatus");
        tp.println("terminalid", terminalid);

        DeviceStatus result = new DeviceStatus(DeviceStatus.RESAUTH_OK, "");
        int resultCode = DeviceStatus.RESAUTH_OK;

        try {
            if (StringUtility.isNullOrEmpty(terminalid)
                    || hasNonAlpha(terminalid)) {
                tp.println("terminalid is invalid");
                return new DeviceStatus(
                        ResultBase.RESREG_INVALIDPARAMETER_DEVID,
                        "Device Id contains invalid characters");
            }

            IAuthDeviceDao device =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                          .getAuthDeviceDAO();

            resultCode = device.getUserStatus(terminalid);
            result = new DeviceStatus(resultCode,
                    "Device status retrieval operation result");
        } catch (DaoException e) {
            LOGGER.logAlert("AuthRes", "getDeviceStatus",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result =  new DeviceStatus(ResultBase.RES_ERROR_DB, e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("AuthRes", "getDeviceStatus",
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
     * Get the current restriction code of the
     * Web Service based on a given deviceno and operatorno.
     * @method POST
     * @param deviceNo        The Device Number
     * @param operatorNo    The Operator Number
     * @return                The Restriction Code
     */
    @Path("/info")
    @POST
    @Produces({MediaType.TEXT_PLAIN })
    public final String getRestrictionCode(
            @FormParam("deviceno") final String deviceNo,
            @FormParam("operatorno") final String operatorNo) {
        return String.valueOf(ApiRestriction
                    .getRestriction(deviceNo, operatorNo));
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
    @ApiOperation(value="�V�O�l�`���[�e�[�^�X�ݒ�", response=DeviceStatus.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RESREG_INVALIDPARAMETER_DEVID, message="�f�o�C�X�R�[�h����")
    })
    public final DeviceStatus setSignatureActivationStatus(
    		@ApiParam(name="corpid", value="��ЃR�[�h") @FormParam("corpid") final String corpId,
    		@ApiParam(name="storeid", value="�X�܃R�[�h") @FormParam("storeid") final String storeId,
    		@ApiParam(name="terminalid", value="�[���R�[�h") @FormParam("terminalid") final String terminalId,
    		@ApiParam(name="udid", value="UDID") @FormParam("udid") final String udid,
    		@ApiParam(name="uuid", value="UUID") @FormParam("uuid") final String uuid,
    		@ApiParam(name="signstatus", value="�V�O�l�`���[�e�[�^�X") @FormParam("signstatus") final int signStatus,
    		@ApiParam(name="signtid", value="�V�O�l�`���[TID") @FormParam("signtid") final String signTid,
    		@ApiParam(name="signactivationkey", value="�V�O�l�`���[�����L�[") @FormParam("signactivationkey") final String signActivationKey) {
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


