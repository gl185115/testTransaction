package ncr.res.mobilepos.authentication.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.authentication.dao.IAuthAdminDao;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.constant.ResultCodeConstants;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

/**
 * AdminResource handles administration tasks
 * for the RES Authentication service.
 *
 */
@Path("/authentication/admin")
@Api(value="/admin", description="管理者API")
public class AdminResource {
    /**
     * the class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * the class instance of the servelet context.
     */
    @Context private ServletContext context;
    /**
     * instance of the trace debug printer.
     */
    private Trace.Printer tp = null;

    /**
     * constructor.
     */
    public AdminResource() {
    	
    }
    /**
     * setPasscode
     * Set passcode for registetring iOS devices to RES Authentication service.
     * Registration fails if adminKey is invalid.
     *
     * @param passcode - passcode to set
     * @param expiry - length of time (in number of hours) it
     * takes for the passcode to become invalid.
     * @param key - the admin key for validation
     * @return ResultBase structure containing the information of the execution.
     */
    @Path("/setpasscode")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="パスコード更新", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESREG_INVALIDPARAMETER_NEWPASSCODE, message="パスコード無効")
    })
    public final ResultBase setPasscode(
            @ApiParam(name="passcode", value="パスコード") @FormParam("passcode") final String passcode,
            @ApiParam(name="expiry", value="有効期限(hour)") @FormParam("expiry") final String expiry,
            @ApiParam(name="key", value="管理者キー") @FormParam("key") final String key) {
        ResultBase result = new ResultBase(ResultBase.RESREG_OK, "");


        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
        tp.methodEnter("setPasscode");
        tp.println("passcode", passcode).println("expiry", expiry)
            .println("key", key);

        try {
            if (hasNonAlpha(passcode)) {
                return new ResultBase(
                        ResultBase.RESREG_INVALIDPARAMETER_NEWPASSCODE,
                        "New passcode has invalid characters");
            }

            DAOFactory daoFactory =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IAuthAdminDao admin = daoFactory.getAuthAdminDAO();

            int ret = ResultBase.RESREG_OK;

            ret = admin.setPasscode(passcode, key,
                    Integer.parseInt(expiry));

            if (ret == ResultBase.RESREG_OK) {
                result = new ResultBase(ret,
                        "Setpasscode successfully completed.");
            } else {
                result = new ResultBase(ret, "Setpasscode failed.");
            }
        } catch (DaoException daoEx) {
            LOGGER.logAlert("AdminRes", "setPasscode",
                    Logger.RES_EXCEP_DAO, daoEx.getMessage());
            result =  new ResultBase(ResultBase.RES_ERROR_DB,
                    daoEx.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("AdminRes", "setPasscode",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result =  new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit();
        }
        return result;
    }

    /**
     * setAdminKey
     * Set adminKey credential to restrict access
     * to setting the registration passcode.
     * Modifying the adminKey will fail if the
     * currentkey parameter does not match to
     * currently set in the Authentication service administration data.
     *
     * @param currentkey - current admin key that is set to the database.
     * @param newkey - new key to replace the currently set admin key.
     * @return ResultBase structure containing the information of the execution.
     */
    @Path("/setadminkey")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="管理者キー更新", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESREG_INVALIDPARAMETER_NEWADMINKEY, message="管理者キー無効")
    })
    public final ResultBase setAdminKey(
            @ApiParam(name="currentkey", value="現管理者キー") @FormParam("currentkey") final String currentkey,
            @ApiParam(name="newkey", value="新管理者キー") @FormParam("newkey") final String newkey) {

        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
        tp.methodEnter("setAdminKey");
        tp.println("currentkey", currentkey).println("newkey", newkey);

        ResultBase result = new ResultBase(ResultBase.RESREG_OK, "");
        int ret = ResultBase.RESREG_OK;

        try {
            if (hasNonAlpha(newkey)) {
                return new ResultBase(
                        ResultBase.RESREG_INVALIDPARAMETER_NEWADMINKEY,
                        "Newkey has invalid characters");
            }

            DAOFactory daoFactory =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IAuthAdminDao admin = daoFactory.getAuthAdminDAO();

            //Get the Result Code upon setting the AdminKey.
            ret = admin.setAdminKey(currentkey, newkey);
            if (ret == ResultBase.RESREG_OK) {
                result = new ResultBase(ret,
                        "Set admin key successfully completed.");
            } else {
                result = new ResultBase(ret, "Set admin key failed.");
            }
        } catch (DaoException daoEx) {
            LOGGER.logAlert("AdminRes", "setAdminKey",
                    Logger.RES_EXCEP_DAO, daoEx.getMessage());
            result =  new ResultBase(ResultBase.RES_ERROR_DB,
                    ResultCodeConstants.DBERR);
        } catch (Exception e) {
            LOGGER.logAlert("AdminRes", "setAdminKey",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result =  new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit();
        }

        return result;
    }

    /**
     * removeDevice
     * Administration interface to removing
     * device registration data from database
     * using admin credential.
     * removeDevice will fail if adminKey does not match
     * to currently set adminKey in database.
     *
     * @param deviceID - ID of device to be removed from database.
     * @param adminKey - admin credential identifier to allow deletion
     * of device using device ID only.
     * @return ResultBase structure containing the information of the execution.
     */
    @Path("/removedevice")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="デバイス登録削除(管理者権限)", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public final ResultBase removeDevice(
            @ApiParam(name="deviceid", value="デバイスID") @FormParam("deviceid") final String deviceID,
            @ApiParam(name="adminkey", value="管理者キー") @FormParam("adminkey") final String adminKey) {

        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
        tp.methodEnter("removeDevice");
        tp.println("deviceID", deviceID).println("adminKey", adminKey);

        ResultBase result = new ResultBase(ResultBase.RESREG_OK, "");
        try {
            DAOFactory daoFactory =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IAuthAdminDao admin = daoFactory.getAuthAdminDAO();
            int ret = ResultBase.RESREG_OK;
            ret = admin.removeDevice(deviceID, adminKey);
            if (ret == ResultBase.RESREG_OK) {
                result = new ResultBase(ret, "Device removed successfuly.");
            } else {
                result = new ResultBase(ret, "Remove device failed.");
            }
        } catch (DaoException daoEx) {
            LOGGER.logAlert("AdminRes", "removeDevice",
                    Logger.RES_EXCEP_DAO, daoEx.getMessage());
            result = new ResultBase(ResultBase.RES_ERROR_DB,
                    ResultCodeConstants.DBERR);
        } catch (Exception e) {
            LOGGER.logAlert("AdminRes", "removeDevice",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result =  new ResultBase(
                    ResultBase.RES_ERROR_GENERAL, e.getMessage());
        } finally {
            tp.methodExit();
        }
        return result;
    }

    /**
     * check if the string has non-alpha numeric characters.
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
}
