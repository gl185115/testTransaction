package ncr.res.mobilepos.systemsetting.resource;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.systemsetting.dao.ISystemSettingDAO;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.model.DateTime;
import ncr.res.mobilepos.systemsetting.model.SystemSetting;
import ncr.res.mobilepos.xebioapi.model.JSONData;

/**
 * SystemSettingResource class is a web resource which provides support
 * for System Setting.
 */
@Path("/SystemSettings")
@Api(value="/SystemSettings", description="�V�X�e���̐ݒ�API")
public class SystemSettingResource {

    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * DAO Factory for system setting.
     */
    private DAOFactory daoFactory;
    /**
     * Program name.
     */
    private String progname = "BizRsc";

    /**
     * DAO Factory for System Setting.
     */
    public SystemSettingResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * A Web Method Call used to set the System's Date Settings.
     *
     * @param today        The BusinessDate
     * @param eod        The End of Day
     * @param skips        The number of Skips
     * @return            The Resultbase containing the ResultCode
     */
    @Path("/DateSettings/change")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="���t��ݒ肷��", response=ResultBase.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RESSYS_ERROR_INVALID_DATE, message="�V�X�e���̐ݒ���t�͖���"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_INVALID_TIME, message="�V�X�e���̐ݒ莞�Ԃ͖���"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_INVALID_SKIP, message="�V�X�e���̐ݒ�X�L�b�v�͖���"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_DATESET_FAIL, message="�V�X�e���ݒ�f�[�^�̎擾�Ɏ��s���܂���"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final ResultBase setDateSetting(
    		@ApiParam(name="companyid", value="��ЃR�[�h")@FormParam("companyid") final String companyid,
    		@ApiParam(name="storeid", value="�X�ܔԍ�")@FormParam("storeid") final String storeid,
    		@ApiParam(name="today", value="����")@FormParam("today") final String today,
    		@ApiParam(name="endofday", value="����̏I���")@FormParam("endofday") final String eod,
    		@ApiParam(name="skips", value="�X�L�b�v")@FormParam("skips") final String skips) {
        String functionname = "BusinessResource.setDateSetting";

        tp.methodEnter("setDateSetting");
        tp.println("Today", today).println("EndOfDay", eod)
            .println("Skips", skips);

        ResultBase rsBase = new ResultBase();

        try {
            if (null != today && !today.isEmpty()
                    && !DateFormatUtility.isLegalFormat(today, "yyyy-MM-dd")) {
                rsBase.setNCRWSSResultCode(ResultBase
                        .RESSYS_ERROR_INVALID_DATE);
                tp.println("today is invalid");
                return rsBase;
            }

            if (null != eod && !eod.isEmpty()
                  && !DateFormatUtility.isLegalFormat(eod, "hh:mm", "HH:mm")) {
                rsBase.setNCRWSSResultCode(
                        ResultBase.RESSYS_ERROR_INVALID_TIME);
                tp.println("eod is invalid");
                return rsBase;
            }

            if (null != skips && !skips.isEmpty()
                    && Integer.parseInt(skips) < 0) {
                rsBase.setNCRWSSResultCode(
                        ResultBase.RESSYS_ERROR_INVALID_SKIP);
                tp.println("skips is invalid");
                return rsBase;
            }

            ISystemSettingDAO businessDao = daoFactory.getSystemSettingDAO();
            int result = businessDao.setDateSetting(companyid, storeid, today, eod, skips);

            if (result < SQLResultsConstants.ONE_ROW_AFFECTED) {
                rsBase.setNCRWSSResultCode(
                        ResultBase.RESSYS_ERROR_DATESET_FAIL);
                tp.println("Failed to set the date.");
            }
        } catch (DaoException e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_DAO,
                    "Failed to set the System Settings: \n" + e.getMessage());
            rsBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_GENERAL,
                    "Failed to set the System Settings: \n" + e.getMessage());
            rsBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(rsBase.toString());
        }

        return rsBase;
    }

    /**
     * A Web Method Call used to get the System's Date Settings.
     *
     * @return The JSON object represenation of the Date Settings
     */
    @POST
    @Path("/DateSettings/get")
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="���t�̐ݒ�𓾂�", response=SystemSetting.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RESSYS_ERROR_NO_SETTINGS_FOUND, message="�V�X�e���ݒ�f�[�^��������Ȃ�"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final SystemSetting getDateSetting(
    		@ApiParam(name="companyid", value="��ЃR�[�h")@FormParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="�X�ܔԍ�")@FormParam("storeid") final String storeId) {
        String functionname = "SystemSettingResource.getDateSetting";
        DateSetting dateSetting;
        SystemSetting systemSetting = new SystemSetting();

        tp.methodEnter("getDateSetting")
            .println("companyid", companyId)
            .println("storeid", storeId);

        try {
            String qCompanyId = StringUtility.isNullOrEmpty(companyId) ? "0" : companyId;
            String qStoreId = StringUtility.isNullOrEmpty(storeId) ? "0" : storeId;

            ISystemSettingDAO systemSetDao = daoFactory.getSystemSettingDAO();

            dateSetting = systemSetDao.getDateSetting(qCompanyId, qStoreId);
            if (dateSetting == null) {
                if (!"0".equals(qStoreId)) {
                    qStoreId = "0";
                    dateSetting = systemSetDao.getDateSetting(qCompanyId, qStoreId);
                }
            }
            if (dateSetting == null) {
                if (!"0".equals(qCompanyId)) {
                    qCompanyId = "0";
                    dateSetting = systemSetDao.getDateSetting(qCompanyId, qStoreId);
                }
            }

            //Are there date being reetrieved?
            //If yes, set the DateSetting.
            if (null != dateSetting) {
                systemSetting.setDateSetting(dateSetting);
            } else {
                systemSetting.setNCRWSSResultCode(
                        ResultBase.RESSYS_ERROR_NO_SETTINGS_FOUND);
                tp.println("No settings found.");
            }
        } catch (DaoException e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_DAO,
                    "Failed to get the System Settings: \n" + e.getMessage());
            systemSetting.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the System Settings: \n" + e.getMessage());
            systemSetting.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(systemSetting.toString());
        }

        return systemSetting;
    }

    /**
     * Method for Debug Log Label.
     * @param level Level
     * @return Result base object
     */
    @GET
    @Path("/DebugLogLevel/change/{level}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="���O���x���ݒ�", response=ResultBase.class)
    public final ResultBase changeDebugLevel(
            @ApiParam(name="level", value="���O���x��") @PathParam("level") final int level) {
        ResultBase result = new ResultBase();

        DebugLogger.setDebugLevel(level);
        result.setMessage("Debug Level is changed to " + level);

        return result;
    }
    /**
     * Web Method Call used to get the Server Machine's current Date
     * and Time.
     *
     * @return DateTime
     */
    @GET
    @Path("/DateSettings/getcurrentdatetime")
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="�ėp���t�𓾂�", response=DateTime.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final DateTime getCurrentDateTime() {
        String functionname = "SystemSettingResource.getCurrentDateTime";
        tp.methodEnter("getCurrentDateTime");
        DateTime dateTime = new DateTime();

        try {
            String today = DateFormatUtility.
                               getCurrentDateTimeFormatted("yyyyMMddHHmmss");

            if (null != today && !today.isEmpty()) {
                dateTime.setCurrentDateTime(today);
                dateTime.setNCRWSSResultCode(ResultBase.RES_OK);
            } else {
                dateTime.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                tp.println("Unabled to retrieve current date and time.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionname, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the server's current date and time. \n"
                               + e.getMessage());
            dateTime.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(dateTime.toString());
        }
        return dateTime;
    }
}
