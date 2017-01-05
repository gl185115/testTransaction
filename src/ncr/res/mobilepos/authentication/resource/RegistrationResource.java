package ncr.res.mobilepos.authentication.resource;

import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.authentication.dao.IAuthDeviceDao;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * the service for registration of terminals/devices.
 *
 */
@Path("/registration")
@Api(value="/registration", description="�[��/�ݔ��̓o�^�̃T�[�r�XAPI")
public class RegistrationResource {
    /**
     * the class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * constant. the company id for the guest accounts.
     */
    private static final String GUEST_CORP_ID = "9999";
    /**
     * the class instance of the servelet context.
     */
    @Context private ServletContext context;
    /**
     * the instance of the trace debug printer.
     */
    private Trace.Printer tp = null;

    /**
     * constructor.
     */
    public RegistrationResource() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
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
	 * Registers new terminal.
	 *
	 * @method POST
	 * @param storeid
	 *            store id of the device.
	 * @param terminalid
	 *            terminal/device id.
	 * @param devicename
	 *            device name.
	 * @param passcode
	 *            store passcode.
	 * @return DeviceStatus details of new terminal.
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="�V�K�[���o�^", response=DeviceStatus.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
        @ApiResponse(code=ResultBase.RESREG_DEVICEEXIST, message="���ɑ��݂���"),
        @ApiResponse(code=ResultBase.RESREG_INVALIDPARAMETER_DEVID, message="�f�o�C�XID�s�������G���["),
        @ApiResponse(code=ResultBase.RESAUTH_PASSCODE_INVALID, message="�p�X�R�[�h��CORP�ؖ�������v���Ȃ�"),
        @ApiResponse(code=ResultBase.RESAUTH_STOREID_NOTEXIST, message="�f�[�^�x�[�X���Ŋ�ƃR�[�h�ƓX�܃R�[�h���Ȃ�"),
    })
	public final DeviceStatus registerDevice(
			@ApiParam(name="companyid", value="��ЃR�[�h") @FormParam("companyid") final String companyId,
			@ApiParam(name="storeid", value="�X�܃R�[�h") @FormParam("storeid") final String storeId,
			@ApiParam(name="terminalid", value="�f�o�C�X���ʎq") @FormParam("terminalid") final String terminalId,
			@ApiParam(name="devicename", value="�[������") @FormParam("devicename") final String deviceName,
			@ApiParam(name="passcode", value="�p�X�R�[�h") @FormParam("passcode") final String passCode,
			@ApiParam(name="udid", value="UDID") @FormParam("udid") final String udid,
			@ApiParam(name="uuid", value="UUID") @FormParam("uuid") final String uuid,
			@ApiParam(name="signstatus", value="�g���}�[�N") @FormParam("signstatus") final int signStatus,
			@ApiParam(name="signtid", value="�g���R�[�h") @FormParam("signtid") String signTId,
			@ApiParam(name="signactivationkey", value="�W���̋N���L�[") @FormParam("signactivationkey") String signActivationKey) {

		tp.methodEnter("registerDevice")
		        .println("companyid", companyId)
		        .println("storeid", storeId)
		        .println("terminalid", terminalId)
				.println("devicename", deviceName)
				.println("passcode", passCode)
				.println("udid", udid)
				.println("uuid", uuid)
				.println("signstatus", signStatus)
                .println("signtid", signTId)
                .println("signactivationkey", signActivationKey);

		DeviceStatus result = new DeviceStatus(ResultBase.RES_ERROR_GENERAL,
				"Register new Device");
		try {
			if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId)) {
				tp.println("CompanyId/StoreId/TerminalId is null or empty.");
				result = new DeviceStatus(
						ResultBase.RES_ERROR_INVALIDPARAMETER,
						"CompanyId/StoreId/TerminalId is null or empty.");
				return result;
			}

			if (hasNonAlpha(terminalId)) {
				tp.println("TerminalId contains non-alphanumeric characters.");
				result = new DeviceStatus(
						ResultBase.RESREG_INVALIDPARAMETER_DEVID,
						"TerminalId contains non-alphanumeric characters.");
				return result;
			}

			DAOFactory daoFactory = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IAuthDeviceDao authAdmin = daoFactory.getAuthDeviceDAO();

			ViewCorpStore authPassCode = authAdmin.validateCorpStore(companyId, storeId,
					passCode);
			if (ResultBase.RESAUTH_OK != authPassCode.getNCRWSSResultCode()) {
				tp.println("CORPSTORE validation failed");
				result = new DeviceStatus(authPassCode.getNCRWSSResultCode(),
						"CORPSTORE validation failed");
				return result;
			}


			// TODO: DeviceName missing in AUT_DEVICES.	TBD.
			ResultBase rb = addDevice(companyId, storeId, terminalId, deviceName, udid, uuid,
			        signStatus, signTId, signActivationKey);
			if (ResultBase.RESREG_OK == rb.getNCRWSSResultCode()) {
				result = new DeviceStatus(ResultBase.RESREG_OK,
						"Registration Success");
				result.setCorpID(companyId);
				result.setStoreID(storeId);
				result.setTerminalID(terminalId);
				result.setStoreName(authPassCode.getCorpstore().getStorename());
				result.setCorpName(authPassCode.getCorpstore().getCompanyName());
				result.setDeviceName(deviceName);
			} else {
				result = new DeviceStatus(rb.getNCRWSSResultCode(), "Error");
			}
		} catch (DaoException e) {
			LOGGER.logAlert("RegRes", "registerDevice", Logger.RES_EXCEP_DAO,
					e.getMessage());
			result = new DeviceStatus(ResultBase.RES_ERROR_DB, e.getMessage());
		} catch (Exception e) {
			LOGGER.logAlert("RegRes", "registerDevice",
					Logger.RES_EXCEP_GENERAL, e.getMessage());
			result = new DeviceStatus(ResultBase.RES_ERROR_GENERAL,
					e.getMessage());
		} finally {
			tp.methodExit(result);
		}
		return result;
	}
    
	/**
	 * Adds new device.
	 *
	 * @param terminalId
	 *            the terminal id
	 * @param storeId
	 *            the store id
	 * @param deviceName
	 *            the name of the device
	 * @return DeviceStatus contains the details of the new terminal
	 * @throws DaoException
	 *             error thrown if exception occurs
	 */
	private ResultBase addDevice(final String companyId, final String storeId, final String terminalId,
			final String deviceName, final String udid, final String uuid,
			final int signStatus, final String signTId, final String signActivationKey) throws DaoException {

        tp.methodEnter("addDevice")
            .println("companyid", companyId)
            .println("storeid", storeId)
            .println("terminalid", terminalId)
            .println("devicename", deviceName)
            .println("udid", udid)
            .println("uuid", uuid)
            .println("signstatus", signStatus)
            .println("signtid", signTId)
            .println("signactivationkey", signActivationKey);

        ResultBase result = null;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        IAuthDeviceDao deviceDao = daoFactory.getAuthDeviceDAO();

		if (deviceDao.isDeviceExisting(companyId, storeId, terminalId, true)) {
			tp.println("Device already exists for storeid & terminalid.");
			result = new ResultBase(ResultBase.RESREG_DEVICEEXIST,
					"Device trying to register is already existing!");
		} else {
			int ret = deviceDao.registerTerminal(companyId, storeId, terminalId, deviceName,
			        udid, uuid, signStatus, signTId, signActivationKey);
            result = new ResultBase(ret, "Registration result: " + ret);
        }

        tp.methodExit(result);
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
    
}
