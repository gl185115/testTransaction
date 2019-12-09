package ncr.res.mobilepos.terminalInfo.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.terminalInfo.dao.ITerminalInfoDAO;
import ncr.res.mobilepos.terminalInfo.model.TerminalInfo;

/**
 * SystemSettingResource class is a web resource which provides support for
 * System Setting.
 */
@Path("/TerminalInfo")
@Api(value = "/TerminalInfo", description = "�^�[�~�i���̍ŏI����ԍ��̎擾")
public class TerminalInfoResource {

    /**
     * ServletContext.
     */
    @Context
    private ServletContext context;

    /**
     * @return the context
     */
    public final ServletContext getContext() {
        return context;
    }

    /**
     * @param contextToSet the context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

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
    private String PROG_NAME = "SysSet";

    /**
     * DAO Factory for System Setting.
     */
    public TerminalInfoResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Web Method Call used to get the Server Machine's current Date and Time.
     *
     * @return DateTime
     */
    @GET
    @Path("/getTxidInfo")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "�^�[�~�i���̍ŏI����ԍ��̎擾", response = TerminalInfo.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���["), })
    public final TerminalInfo getCurrentDateTime(
            @ApiParam(name = "companyId", value = "��ЃR�[�h") @QueryParam("companyId") final String companyId,
            @ApiParam(name = "storeId", value = "�X�ԍ�") @QueryParam("storeId") final String storeId,
            @ApiParam(name = "workstationId", value = "�^�[�~�i���ԍ�") @QueryParam("workstationId") final String workstationId,
            @ApiParam(name = "businessdaydate", value = "�Ɩ����t") @QueryParam("businessdaydate") final String businessDayDate,
            @ApiParam(name = "trainingmode", value = "�g���[�j���O���[�h�t���O") @QueryParam("trainingmode") final int trainingMode) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("storeId", storeId).println("workstationId", workstationId)
                .println("businessDayDate", businessDayDate).println("trainingMode", trainingMode);
        String strCheck = "";
        TerminalInfo terminalInfo = new TerminalInfo();
        if (StringUtility.isNullOrEmpty(companyId)) {
            strCheck += "," + "companyId";
        }
        if (StringUtility.isNullOrEmpty(storeId)) {
            strCheck += "," + "storeId";
        }
        if (StringUtility.isNullOrEmpty(workstationId)) {
            strCheck += "," + "workstationId";
        }
        // if (StringUtility.isNullOrEmpty(businessDayDate)) {
        //     strCheck += "," + "businessDayDate";
        // }
        //if (StringUtility.isNullOrEmpty(trainingMode)) {
        //    strCheck += "," + "trainingMode";
        //}
        try {
            if (!StringUtility.isNullOrEmpty(strCheck)) {
                strCheck = strCheck.substring(1);
                LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_PARSE,
                        "���N�G�X�g�p�����[�^�G���[�B \n" + "Query�p�����[�^���s���ł��B(" + strCheck + ")");
                terminalInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            } else {
                DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
                ITerminalInfoDAO terminalInfoDDAO = sqlServer.getTerminalInfoDAO();
                terminalInfo = terminalInfoDDAO.getTxidInfo(companyId, storeId, workstationId, businessDayDate,
                        trainingMode);
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the server's TxidInfo. \n" + e.getMessage());
            terminalInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(terminalInfo.toString());
        }
        return terminalInfo;
    }
}
