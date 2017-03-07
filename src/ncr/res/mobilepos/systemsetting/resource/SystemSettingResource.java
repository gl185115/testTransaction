package ncr.res.mobilepos.systemsetting.resource;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemsetting.dao.ISystemSettingDAO;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.model.DateTime;
import ncr.res.mobilepos.systemsetting.model.SystemSetting;

/**
 * SystemSettingResource class is a web resource which provides support
 * for System Setting.
 */
@Path("/SystemSettings")
@Api(value="/SystemSettings", description="システムの設定API")
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
     * A Web Method Call used to get the System's Date Settings.
     *
     * @return The JSON object represenation of the Date Settings
     */
    @POST
    @Path("/DateSettings/get")
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="日付設定取得", response=SystemSetting.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RESSYS_ERROR_NO_SETTINGS_FOUND, message="システム設定データが見つからない"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final SystemSetting getDateSetting(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeId) {
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
     * Web Method Call used to get the Server Machine's current Date
     * and Time.
     *
     * @return DateTime
     */
    @GET
    @Path("/DateSettings/getcurrentdatetime")
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="現在日時取得", response=DateTime.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
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
