/*
 * Copyright (c) 2011-2012,2015 NCR/JAPAN Corporation SW-R&D
 *
 * CredentialResource
 *
 * CredentialResource Class is a Web Resource which support
 * MobilePOS Credential processes.
 */
package ncr.res.mobilepos.credential.resource;

import java.sql.SQLException;
import java.util.List;
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
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.authentication.dao.IAuthDeviceDao;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.credential.dao.ICredentialDAO;
import ncr.res.mobilepos.credential.dao.IGroupDAO;
import ncr.res.mobilepos.credential.model.Authorization;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.Employees;
import ncr.res.mobilepos.credential.model.NameMasterInfo;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.model.SystemNameMasterList;
import ncr.res.mobilepos.credential.model.UserGroup;
import ncr.res.mobilepos.credential.model.UserGroupLabel;
import ncr.res.mobilepos.credential.model.UserGroupList;
import ncr.res.mobilepos.credential.model.ViewEmployee;
import ncr.res.mobilepos.credential.model.ViewUserGroup;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.TodHelper;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;

/**
 * CredentialResource Class is a Web Resource which support MobilePOS Credential
 * processes.
 *
 */
@Path("/credential")
@Api(value="/credential", description="ユーザ認証関連API")
public class CredentialResource {
    /**
     * ServletContext instance.
     */
    @Context
    private ServletContext context;

    @Context
    private SecurityContext securityContext;
    /**
     * Logger instance use for logging.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Program name use in logging.
     */
    private String progName = "Credntl";
    /**
     * Class name use in logging.
     */
    private String className = "CredentialResource";
    /** Path classname */
    private String pathName = "credential";
    /**
     * DAOFactory instance use to access CredentialDAO.
     */
    private DAOFactory daoFactory = null;

    /**
     * Gets the dao factory.
     *
     * @return the dao factory
     */
    public final DAOFactory getDaoFactory() {
        return daoFactory;
    }

    /**
     * Sets the daoFactory.
     *
     * @param newDaoFactory
     *            the new daoFactory
     */
    public final void setDaoFactory(final DAOFactory newDaoFactory) {
        this.daoFactory = newDaoFactory;
    }

    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Default constructor. Instantiate daoFactory and ioWriter.
     */
    public CredentialResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Handles the sign-on process with SPART credentials.
     *
     * @param empCode
     *            Employee Code.
     * @param passcode
     *            Passcode of the operator.
     * @param terminalId
     *            Device identifier used in log in operation.
     * @return The Operator Information.
     */
    @Path("/spart/{operatorno}/signon")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="SPARTユーザー(従業員)ログイン認証", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_INVALID_PARAM, message="無効なパラメータ"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザー未検出"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_INACTIVE, message="ユーザー無効"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_DELETED, message="ユーザー削除された"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_PASSCODE_INVALID, message="パスワード無効"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_EXPIRED, message="パスワード満期"),
    })
    public final Operator credentialSpartLogin(@ApiParam(name="operatorno", value="従業員番号") @PathParam("operatorno") final String empCode,
    		@ApiParam(name="passcode", value="パスワード") @FormParam("passcode") final String passcode,
    		@ApiParam(name="terminalid", value="端末コード") @FormParam("terminalid") final String terminalId) {

        tp.methodEnter("CcredentialSpartLogIn");
        tp.println("EmpCode", empCode).println("Passcode", passcode).println("TerminalID", terminalId);

        Operator spartOperator = new Operator();

        if (StringUtility.isNullOrEmpty(empCode, passcode, terminalId)) {
            spartOperator.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_INVALID_PARAM);
            tp.methodExit("Parameter is null or empty.");
            return spartOperator;
        }

        try {
            ICredentialDAO credentialDAO = daoFactory.getCredentialDAO();
            spartOperator = credentialDAO.credentialSpartLogin(empCode, passcode, terminalId);

            updateTod();
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "credentialSpartLogin", Logger.RES_EXCEP_DAO,
                    "Failed to Sign On Operator# " + empCode + ": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                spartOperator.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                spartOperator.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }

        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "credentialSpartLogin", Logger.RES_EXCEP_GENERAL,
                    "Failed to Sign On Operator# " + empCode + ": " + ex.getMessage());
            spartOperator.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);

        } finally {
            tp.methodExit(spartOperator.toString());
        }
        return spartOperator;
    }

    /**
     * Signs on an operator.
     *
     * @param operatorNumber
     *            Unique number identifier of operator
     * @param passCode
     *            Operator passcode
     * @param terminalID
     *            ID of the device where the operator account is signed on
     * @param demo
     *            True for demo mode. False if not demo mode.
     * @return "OK" if operator sign on success otherwise "NG".
     */
    @Path("/{operatorno}/signon")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ユーザー(従業員)ログイン認証", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_GROUP_NOTFOUND, message="ユーザグループ未検出"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザ未検出"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_PASSCODE_INVALID, message="パスワード無効")
    })
    public final Operator requestSignOn(@ApiParam(name="operatorno", value="従業員番号") @PathParam("operatorno") final String operatorNumber,
            @ApiParam(name="companyId", value="会社コード") @FormParam("companyId") final String companyId,
            @ApiParam(name="passcode", value="パスワード") @FormParam("passcode") final String passCode,
            @ApiParam(name="terminalid", value="端末コード") @FormParam("terminalid") final String terminalID,
            @ApiParam(name="demo", value="") @FormParam("demo") final boolean demo) {

        tp.methodEnter("requestSignOn");
        tp.println("companyId", companyId).println("operatorNumber", operatorNumber).println("passCode", passCode)
                .println("terminalID", terminalID).println("demo", demo);

        Operator operator = new Operator();

        try {
            ICredentialDAO credentialDAO = this.daoFactory.getCredentialDAO();

            if (!demo) {
                operator = credentialDAO.signOnOperator(companyId, operatorNumber, passCode, terminalID);
            } else {
                operator = credentialDAO.guestSignOnOperator(operatorNumber, terminalID);
            }

            updateTod();
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "requestSignOn", Logger.RES_EXCEP_DAO,
                    "Failed to Sign On Operator# " + operatorNumber + ": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                operator.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                operator.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }

        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "requestSignOn", Logger.RES_EXCEP_GENERAL,
                    "Failed to Sign On Operator# " + operatorNumber + ": " + ex.getMessage());
            operator.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);

        } finally {
            tp.methodExit(operator.toString());
        }
        return operator;
    }

    /**
     * @deprecated Creates a new operator.
     *
     * @param operatorNumber
     *            The Operator identifier string.
     * @param passCode
     *            The Operator's passcode.
     * @return "OK" if operator creation Successful, otherwise "NG"
     */
    @Deprecated
    @Path("/createoperator/{operatorno}")
    @POST
    @Produces({ MediaType.TEXT_PLAIN })
    public final ResultBase createOperator(@PathParam("operatorno") final String operatorNumber,
            @FormParam("passcode") final String passCode) {

        tp.methodEnter("createOperator");
        tp.println("operatorNumber", operatorNumber).println("passCode", passCode);

        int resultCode = 0;
        ResultBase resultBase = new ResultBase();
        DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);

        if (Operator.hasAlpha(operatorNumber) || "".equals(operatorNumber)) {
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
            tp.methodExit("operatorNumber is invalid");
            return resultBase;
        }

        try {
            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();
            resultCode = credentialDAO.createOperator(operatorNumber, passCode);
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_OK);

            if (0 < resultCode) {
                resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESCREDL_ZERO_RESULTSET);
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "requestSignOn", Logger.RES_EXCEP_DAO,
                    "Failed to Create Operator# " + operatorNumber + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "requestSignOn", Logger.RES_EXCEP_GENERAL,
                    "Failed to Create Operator# " + operatorNumber + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * @deprecated Creates a new guest operator.
     *
     * @param operatorNumber
     *            The operator identifier string.
     * @param corpid
     *            The corporate identifier string.
     * @param storeid
     *            The store identifier string.
     * @param terminalid
     *            The device or terminal identifier string.
     * @param udid
     *            The unique device identifier string.
     * @param uuid
     *            The universal unique identifier string.
     * @return "OK" if operator creation Successful, otherwise "NG"
     */
    @Deprecated
    @Path("/createoperatorguest/{operatorno}/{deviceid}")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final Operator createOperatorGuest(@PathParam("operatorno") final String operatorNumber,
            @PathParam("corpid") final String corpid, @PathParam("storeid") final String storeid,
            @PathParam("terminalid") final String terminalid, @FormParam("udid") final String udid,
            @FormParam("uuid") final String uuid) {
        Operator result = new Operator();

        tp.methodEnter("createOperatorGuest");
        tp.println("operatorNumber", operatorNumber).println("corpid", corpid).println("storeid", storeid)
                .println("terminalid", terminalid).println("udid", udid).println("uuid", uuid);

        int resultCode = 0;
        DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        try {
            IAuthDeviceDao authDevDao = sqlServer.getAuthDeviceDAO();
            if (authDevDao.isDeviceExisting(corpid, storeid, terminalid, true)) {
                int devauthres = authDevDao.authenticateUser(storeid, terminalid);
                if (ResultBase.RESAUTH_OK != devauthres && DeviceStatus.STATUS_DEVICESTATUSNOCHANGE != devauthres) {
                    result.setMessage("authenticating device error");
                    result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                    result.setResponse("NG");
                    return result;
                }
            } else {
                if (authDevDao.isDeviceExisting(corpid, storeid, terminalid, false)) {
                    result.setMessage("Either Devid or UUID" + " or UDID is already in use.");
                    result.setNCRWSSResultCode(ResultBase.RESAUTH_INVALIDPARAMETER);
                    result.setResponse("NG");
                    return result;
                }
                if (ResultBase.RESREG_OK != authDevDao.registerTerminal(corpid, storeid, terminalid, "", udid, uuid, 1,
                        "", "")) {
                    result.setMessage("registering device error");
                    result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                    result.setResponse("NG");
                    return result;
                }
            }

            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();
            credentialDAO.guestSignOnOperator(operatorNumber, terminalid);

            resultCode = 1;

            if (1 != resultCode) {
                result.setMessage("operator sign-on error");
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                result.setResponse("NG");
                return result;
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "createOperatorGuest", Logger.RES_EXCEP_DAO,
                    "Failed to Create Operator# " + operatorNumber + ": " + ex.getMessage());
            result.setMessage("operator sign-on error");
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            result.setResponse("NG");
            return result;
        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "createOperatorGuest", Logger.RES_EXCEP_GENERAL,
                    "Failed to Create Operator# " + operatorNumber + ": " + ex.getMessage());
            result.setMessage("operator sign-on error");
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setResponse("NG");
            return result;
        } finally {
            tp.methodExit(result.toString());
        }

        result.setOperatorNo(operatorNumber);
        result.setName("Anonymous");
        result.setNCRWSSResultCode(ResultBase.RESAUTH_OK);
        result.setResponse("OK");
        return result;
    }

    /**
     * Signs off an operator.
     *
     * @param operatorNumber
     *            Unique number identifier of operator
     * @return "OK" if operator sign off success otherwise "NG".
     */
    @Path("/{operatorno}/signoff")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ユーザー(従業員)ログオフ", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザ未検出"),
            @ApiResponse(code=ResultBase.RESCREDL_ERROR_NG, message="パラメータ無効")
        })
    public final ResultBase requestSignOff(@ApiParam(name="operatorno", value="従業員番号") @PathParam("operatorno") final String operatorNumber) {

        tp.methodEnter("requestSignOff");
        tp.println("operatorNumber", operatorNumber);

        ResultBase resultBase = new ResultBase();
        DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);

        try {
            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();
            int result = credentialDAO.signOffOperator(operatorNumber);
            if (result == 0) {
                result = ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND;
            } else {
                result = ResultBase.RESCREDL_OK;
            }
            resultBase.setNCRWSSResultCode(result);
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "requestSignOff", Logger.RES_EXCEP_DAO,
                    "Failed to Sign Off Operator# " + operatorNumber + ": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "requestSignOff", Logger.RES_EXCEP_GENERAL,
                    "Failed to Sign Off Operator# " + operatorNumber + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * Get status of Operator
     *
     * @param empCode
     *            Employee Code
     *
     * @return status of an operator
     */
    @Path("/getstatusofoperator/{companyid}/{operatorno}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="ユーザー(従業員)状態取得", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
    })
    public final Operator getStatusOfOperator(@ApiParam(name="companyid", value="会社コード") @PathParam("companyid") final String companyid,
    		@ApiParam(name="operatorno", value="従業員番号") @PathParam("operatorno") final String empCode) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("CompanyId", companyid);
        tp.println("EmpCode", empCode);

        Operator operator = new Operator();

        try {
            if (StringUtility.isNullOrEmpty(empCode)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                operator.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                operator.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                operator.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return operator;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();
            operator = credentialDAO.getStatusOfOperator(companyid, empCode);
            Authorization authorization = credentialDAO.getOperatorAuthorization(companyid, empCode);
            operator.setAuthorization(authorization);
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_DAO, functionName + ": Failed to Get status of Operator.", ex);
            operator.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            operator.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            operator.setMessage(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to Get status of Operator.",
                    ex);
            operator.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            operator.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            operator.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(operator);
        }
        return operator;
    }

    /**
     * Get system name list
     *
     * @param name
     *            type id (NameTypeIdが単一のNameTypeIdを設定)
     * @param system
     *            name
     * @param except
     *            name type id (ExceptTypeIdが単一または複数のNameTypeIdを設定)
     * @return system name list
     */
    @Path("/getSystemNameMasterList")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="システム名称一覧取得", response=SystemNameMasterList.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出")
    })
    public final SystemNameMasterList getSystemNameMasterList(@ApiParam(name="companyId", value="会社コード") @FormParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗コード") @FormParam("storeId") final String storeId,
    		@ApiParam(name="nameCategory", value="名称区分") @FormParam("nameCategory") final String nameCategory) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("storeId", storeId).println("nameCategory", nameCategory);

        SystemNameMasterList result = new SystemNameMasterList();
        if (StringUtility.isNullOrEmpty(companyId, storeId, nameCategory)) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(result.toString());
            return result;
        }

        try {
            ICredentialDAO credentialDAO = daoFactory.getCredentialDAO();
            List<NameMasterInfo> nmMstInfoLst = credentialDAO.getSystemNameMaster(companyId, storeId, nameCategory);
            if (nmMstInfoLst.size() != 0) {
                result.setNamemasterinfo(nmMstInfoLst);
                result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                result.setMessage(ResultBase.RES_SUCCESS_MSG);
            } else {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_DAO, functionName + ": Failed to get system name list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get system name list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(result);
        }

        return result;

    }

    /**
     * Gets status of an operator.
     *
     * @param operatorNo
     *            Unique number identifier of operator
     * @return 1 if online and 0 if offline. 2 if an error occurs
     */
    @Path("/getoperatorstatus/{operatorno}")
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    public final ResultBase getOperatorStatus(@PathParam("operatorno") final String operatorNo) {

        tp.methodEnter("getOperatorStatus");
        tp.println("operatorNo", operatorNo);

        int resultCode = 0;
        ResultBase resultBase = new ResultBase();
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();
            resultCode = credentialDAO.checkOperatorStatus(operatorNo);

            switch (resultCode) {
            case Operator.STATUS_OFFLINE:
                resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_OPERATOR_OFFLINE);
                break;
            case Operator.STATUS_ONLINE:
                resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_OPERATOR_ONLINE);
                break;
            default:
                resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND);
                tp.println("Operator not found.");
                break;
            }

        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "getOperatorStatus", Logger.RES_EXCEP_DAO,
                    "Failed to get the Credential Status of Operator# " + operatorNo + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "getOperatorStatus", Logger.RES_EXCEP_GENERAL,
                    "Failed to get the Credential Status of Operator# " + operatorNo + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
        } finally {
            tp.methodExit(resultBase.toString());
        }

        return resultBase;
    }

    /**
     * Gets the list of operator of a given retailStoreID.
     *
     * @param retailStoreID
     *            The Retail Store ID where the operator belong.
     * @param key
     *            The key to search or look-up in the database.
     * @param name
     *            The operator name to search or look-up in the database.
     * @return The Employee List The list of employee or operator.
     */
    @Path("/list")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ユーザー(従業員)リスト取得", response=Employees.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
        })
    public final Employees listOperators(@ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String retailStoreID,
            @ApiParam(name="key", value="従業員番号検索キーワード") @QueryParam("key") final String key,
            @ApiParam(name="name", value="従業員名検索キーワード") @QueryParam("name") final String name,
            @ApiParam(name="limit", value="最大取得件数") @QueryParam("limit") final int limit) {

        String functioname = className + ".listOperators";
        tp.methodEnter("listOperators").println("RetailStoreID", retailStoreID).println("Key", key)
                .println("name", name).println("limit", limit);

        Employees employees = new Employees();
        ResultBase resultBase = new ResultBase();
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();

            List<Employee> employeeList = credentialDAO.listOperators(retailStoreID, key, name, limit);

            employees.setEmployeeList(employeeList);
            employees.setRetailtStoreID(retailStoreID);

        } catch (DaoException ex) {
            LOGGER.logAlert(progName, functioname, Logger.RES_EXCEP_DAO, "Failed to list Employees with key " + key
                    + " and storeid " + retailStoreID + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functioname, Logger.RES_EXCEP_GENERAL, "Failed to list Employees with key " + key
                    + " and storeid " + retailStoreID + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(employees.getEmployeeList().size());
        }

        return employees;
    }

    /**
     * Resets a passcode of an operator.
     *
     * @param retailstoreid
     *            The retail store id where the operator belong.
     * @param operatorno
     *            The operator's number.
     * @return ResulBase containing a result code.
     */
    @Path("/reset")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="パスワードリセット", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザー未検出"),
        @ApiResponse(code=ResultBase.RESCREDL_RESET_FAIL, message="パスワードリセット失敗")
    })
    public final ResultBase resetOperator(@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailstoreid,
    		@ApiParam(name="operatorid", value="従業員番号") @FormParam("operatorid") final String operatorno) {
        ResultBase rsBase = new ResultBase();
        String functioname = className + "resetOperator";

        tp.methodEnter("resetOperator");
        tp.println("retailstoreid", retailstoreid).println("operatorno", operatorno);

        ResultBase resultBase = new ResultBase();
        try {
            rsBase = getOperatorStatus(operatorno);

            if (rsBase.getNCRWSSResultCode() == ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND) {
                tp.println("Operator not found.");
                return rsBase;
            }
            /* Reset the passcode disregarding if Operator is Online/Offline */

            // Refresh ResultBase
            rsBase = new ResultBase();

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();

            int result = credentialDAO.resetOperatorPasscode(retailstoreid, operatorno);

            if (result < SQLResultsConstants.ONE_ROW_AFFECTED) {
                rsBase.setNCRWSSResultCode(ResultBase.RESCREDL_RESET_FAIL);
                tp.println("Failed to reset Operator");
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, functioname, Logger.RES_EXCEP_DAO,
                    "Failed to Reset the Operator Passcode With RetailStoreID " + retailstoreid + " and OperatorNo "
                            + operatorno + " : " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functioname, Logger.RES_EXCEP_GENERAL,
                    "Failed to Reset the Operator Passcode With RetailStoreID " + retailstoreid + " and OperatorNo "
                            + operatorno + " : " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(rsBase.toString());
        }

        return rsBase;
    }

    /**
     * Gets employee detail.
     *
     * @param retailStoreID
     *            The store number which the employee belong to.
     * @param operatorID
     *            The employee number.
     * @return viewEmployee The object containing resultcode and Employee
     *         details.
     */
    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ユーザー(従業員)詳細情報取得", response=ViewEmployee.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザー(従業員)未検出")
    })
    public final ViewEmployee viewEmployee(@ApiParam(name="retailstoreid", value="店舗コード") @QueryParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="operatorid", value="従業員番号") @QueryParam("operatorid") final String operatorID) {

        tp.methodEnter("viewEmployee");
        tp.println("retailStoreID", retailStoreID).println("operatorID", operatorID);

        ViewEmployee viewEmployee = new ViewEmployee();

        try {
            ICredentialDAO credtlDAO = daoFactory.getCredentialDAO();
            viewEmployee = credtlDAO.viewEmployee(retailStoreID, operatorID, true);

        } catch (DaoException ex) {
            LOGGER.logAlert(progName, "CredentialResource.viewEmployee", Logger.RES_EXCEP_DAO,
                    "Failed to view employee# " + operatorID + ": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                viewEmployee.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                viewEmployee.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, "CredentialResource.viewEmployee", Logger.RES_EXCEP_GENERAL,
                    "Failed to view employee# " + operatorID + ": " + ex.getMessage());
            viewEmployee.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewEmployee.toString());
        }
        return viewEmployee;
    }

    /**
     * Creates a new employee.
     *
     * @param retailStoreID
     *            - id of the store
     * @param operatorID
     *            - id of the operator to create
     * @param jsonEmployee
     *            - json string form of an employee model that contains the
     *            details of the employee to create
     * @return ResultBase - class that contains the result of the request
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ユーザー(従業員)新規作成", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_INVALID_PARAM, message="無効なパラメータ"),
        @ApiResponse(code=ResultBase.RES_STORE_NOT_EXIST, message="店舗未検出"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_EXISTS, message="ユーザー(従業員)もう存在")
    })
    public final ResultBase createEmployee(@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="operatorid", value="従業員番号") @FormParam("operatorid") final String operatorID,
    		@ApiParam(name="employee", value="従業員情報") @FormParam("employee") final String jsonEmployee) {

        tp.methodEnter("createEmployee");
        tp.println("retailStoreID", retailStoreID).println("operatorID", operatorID).println("jsonEmployee",
                jsonEmployee);

        ResultBase resultBase = new ResultBase();

        if (StringUtility.isNullOrEmpty(retailStoreID, operatorID, jsonEmployee) || hasNonAlpha(operatorID)) {
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_INVALID_PARAM);
            resultBase.setMessage(
                    "storeid:" + retailStoreID + " operatorid:" + operatorID + " employee string:" + jsonEmployee);
            tp.methodExit("One of the parameters is invalid.");
            return resultBase;
        }

        StoreResource storeRes = new StoreResource();
        ViewStore store = storeRes.viewStore(retailStoreID);
        if (store.getNCRWSSResultCode() != ResultBase.RES_STORE_OK) {
            resultBase.setNCRWSSResultCode(ResultBase.RES_STORE_NOT_EXIST);
            tp.methodExit("retailStoreID does not exist in database.");
            return resultBase;
        }

        try {
            JsonMarshaller<Employee> jsonMarshall = new JsonMarshaller<Employee>();
            Employee employee = jsonMarshall.unMarshall(jsonEmployee, Employee.class);
            ICredentialDAO credentialDAO = daoFactory.getCredentialDAO();
            employee.setUpdAppId(pathName.concat(".create"));
            employee.setUpdOpeCode(getOpeCode());
            resultBase = credentialDAO.createEmployee(retailStoreID, operatorID, employee);

        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "createEmployee", Logger.RES_EXCEP_DAO,
                    "Failed to Create Employee# " + operatorID + ": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "createEmployee", Logger.RES_EXCEP_GENERAL,
                    "Failed to Create Employee# " + operatorID + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * deletes an employee.
     *
     * @param storeId
     *            - id of the store
     * @param operatorId
     *            - id of the operator to delete
     * @return ResultBase containing the result code.
     */
    @Path("/delete")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="ユーザー(従業員)削除", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_ONLINE, message="ユーザー(従業員)オンライン"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザー(従業員)未検出")
    })
    public final ResultBase deleteEmployee(@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="operatorid", value="従業員番号") @FormParam("operatorid") final String operatorId) {

        tp.methodEnter("deleteEmployee");
        tp.println("storeid", storeId).println("operatorid", operatorId);

        ResultBase result = new ResultBase();

        try {
            ICredentialDAO cred = daoFactory.getCredentialDAO();
            String updOpeCode = getOpeCode();
            String updAppId = pathName.concat(".delete");
            result = cred.deleteEmployee(storeId, operatorId, updOpeCode, updAppId);
        } catch (DaoException e) {
            LOGGER.logAlert(this.progName, "deleteEmployee", Logger.RES_EXCEP_DAO, e.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception e) {
            LOGGER.logAlert(this.progName, "deleteEmployee", Logger.RES_EXCEP_GENERAL, e.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(result.toString());
        }

        return result;
    }

    /**
     * Updates employee information.
     *
     * @param retailStoreID
     *            The retail store number where the employee belong.
     * @param currentOperatorID
     *            The id of the operator requesting the update.
     * @param employeeJSON
     *            The JSON object of to be updated employee.
     * @param operatorID
     *            The operator number.
     * @return viewEmployee The ViewEmployee object containing updated employee
     *         and result code.
     */
    @Path("/maintenance")
    @POST
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ユーザー(従業員)メンテナンス", response=ViewEmployee.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_NO_UPDATE, message="ユーザー(従業員)更新失敗"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザー未検出"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_ONLINE, message="ユーザー(従業員)オンライン"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_EXISTS, message="ユーザー(従業員)もう存在")
    })
    public final ViewEmployee updateEmployee(@ApiParam(name="retailstoreid", value="店舗コード") @FormParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="currentoperatorid", value="現在の従業員番号") @FormParam("currentoperatorid") final String currentOperatorID,
    		@ApiParam(name="operatorid", value="従業員番号") @FormParam("operatorid") final String operatorID,
    		@ApiParam(name="employee", value="従業員情報") @FormParam("employee") final String employeeJSON) {

        tp.methodEnter("updateEmployee");
        tp.println("retailStoreID", retailStoreID).println("currentOperatorID", currentOperatorID)
                .println("operatorID", operatorID).println("employeeJSON", employeeJSON);

        ViewEmployee viewEmployee = new ViewEmployee();

        try {

            if (StringUtility.isNullOrEmpty(retailStoreID, employeeJSON, operatorID)) {
                viewEmployee.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NO_UPDATE);
                tp.println("One of the parameters is invalid.");

            } else {
                JsonMarshaller<Employee> jsonMarshaller = new JsonMarshaller<Employee>();
                Employee employee = jsonMarshaller.unMarshall(employeeJSON, Employee.class);
                employee.setUpdAppId(pathName.concat(".maintenance"));
                employee.setUpdOpeCode(getOpeCode());
                ICredentialDAO iCredtlDao = daoFactory.getCredentialDAO();
                viewEmployee = iCredtlDao.updateEmployee(retailStoreID, currentOperatorID, operatorID, employee);
            }

        } catch (DaoException daoEx) {
            LOGGER.logAlert(progName, "CredentialResource.updateEmployee", Logger.RES_EXCEP_DAO,
                    "Failed to update the employee Details.\n" + daoEx.getMessage());

            if (daoEx.getCause() instanceof SQLException) {
                viewEmployee.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                viewEmployee.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }

        } catch (Exception e) {
            LOGGER.logAlert(progName, "CredentialResource.updateEmployee", Logger.RES_EXCEP_GENERAL, e.getMessage());
            viewEmployee.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewEmployee.toString());
        }

        return viewEmployee;
    }

    /**
     * Change store operator's old passcode to a new passcode.
     *
     * @param operatorID
     *            the operatorid which passcode should be change.
     * @param oldPasscode
     *            the operator's old passcode.
     * @param newPasscode
     *            the opertor's new passcode.
     * @return the ResultBase. If resultcode is zero(0), it is success.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/changepasscode/{operatorid}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="パスワード変更", response=ResultBase.class)
    @ApiResponses(value={
    	@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_INVALID_PARAM, message="無効なパラメータ"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="ユーザー(従業員)未検出"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_PASSCODE_INVALID, message="パスワード無効"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_ONLINE, message="ユーザー(従業員)オンライン")
    })
    public final ResultBase changePasscode(@ApiParam(name="operatorid", value="従業員番号") @PathParam("operatorid") final String operatorID,
    		@ApiParam(name="old", value="旧いパスワード") @FormParam("old") final String oldPasscode,
    		@ApiParam(name="new", value="新しいパスワード") @FormParam("new") final String newPasscode) {

        String functionName = "changePasscode";
        tp.methodEnter(functionName).println("operatorid", operatorID).println("old", oldPasscode).println("new",
                newPasscode);

        ResultBase resultBase = new ResultBase();

        if (StringUtility.isNullOrEmpty(operatorID, oldPasscode, newPasscode)) {
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_INVALID_PARAM);
            tp.println("Parameter[s] is empty or null.");
            tp.methodExit(resultBase);
            return resultBase;
        }

        try {
            ICredentialDAO iCredDao = daoFactory.getCredentialDAO();
            String updAppId = pathName.concat(".changepasscode");
            resultBase = iCredDao.changePasscode(operatorID, oldPasscode, newPasscode, updAppId, getOpeCode());
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO, "\n" + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL, "\n" + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    /**
     * Creates a new group.
     *
     * @param groupCode
     *            - the code attributed to the group.
     * @param jsonGroup
     *            - json string form of a group model that contains the details
     *            of the group to create
     * @return ResultBase - class that contains the result of the request
     */
    @Path("/groups/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase createGroup(@FormParam("groupcode") final int groupCode,
            @FormParam("group") final String jsonGroup) {

        tp.methodEnter("createGroup");
        tp.println("groupCode", groupCode).println("jsonGroup", jsonGroup);

        ResultBase resultBase = new ResultBase();

        if (StringUtility.isNullOrEmpty(jsonGroup, groupCode)) {
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_INVALID_PARAM);
            resultBase.setMessage("groupcode:" + groupCode + " group string:" + jsonGroup);
            tp.methodExit("Parameter is null or empty.");
            return resultBase;
        }

        try {
            JsonMarshaller<UserGroup> jsonMarshall = new JsonMarshaller<UserGroup>();
            UserGroup group = jsonMarshall.unMarshall(jsonGroup, UserGroup.class);
            IGroupDAO groupDAO = daoFactory.getGroupDAO();
            resultBase = groupDAO.createGroup(groupCode, group);

        } catch (DaoException ex) {
            LOGGER.logAlert(progName, className + "createGroup", Logger.RES_EXCEP_DAO,
                    "Failed to Create Group# " + groupCode + ": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, className + "createGroup", Logger.RES_EXCEP_GENERAL,
                    "Failed to Create Group# " + groupCode + ": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_NG);
            resultBase.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * Web Method call for Deleting User Group.
     *
     * @param groupCode
     *            the code of the User Group to be deleted.
     * @return the {@link ResultBase}. If result code is zero(0), it is success.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/groups/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ResultBase deleteGroup(@FormParam("groupcode") final int groupCode) {
        tp.methodEnter("deleteGroup");
        tp.println("GroupCode", groupCode);

        ResultBase result = new ResultBase();

        try {
            IGroupDAO groupDAO = daoFactory.getGroupDAO();
            result = groupDAO.deleteGroup(groupCode);
        } catch (DaoException e) {
            LOGGER.logAlert(this.progName, "deleteGroup", Logger.RES_EXCEP_DAO, e.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception e) {
            LOGGER.logAlert(this.progName, "deleteGroup", Logger.RES_EXCEP_GENERAL, e.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(result.toString());
        }
        return result;
    }

    /**
     * View Group Detail.
     *
     * @param groupCode
     *            Group Code (Identifier).
     * @return UserGroup The object containing result code and UserGroup
     *         details.
     */
    @Path("groups/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ViewUserGroup viewGroupDetail(@QueryParam("code") final int groupCode) {

        tp.methodEnter("viewGroupDetail");
        tp.println("GroupCode", groupCode);

        ViewUserGroup viewUserGrp = new ViewUserGroup();

        try {
            IGroupDAO groupDao = daoFactory.getGroupDAO();
            viewUserGrp = groupDao.viewGroupDetail(groupCode);

        } catch (DaoException e) {
            LOGGER.logAlert(progName, "CredentialResource.viewGroupDetail", Logger.RES_EXCEP_DAO,
                    "Failed to view Group Detail. " + e.getMessage());
            if (e.getCause() instanceof SQLException) {
                viewUserGrp.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                viewUserGrp.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, "CredentialResource.viewGroupDetail", Logger.RES_EXCEP_GENERAL,
                    "Failed to view Group Detail. " + ex.getMessage());
            viewUserGrp.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewUserGrp.toString());
        }
        return viewUserGrp;
    }

    /**
     * Gets the list of groups correspondin to a given key.
     *
     * @param key
     *            The key to search or look-up in the database. returns all
     *            groups if key is empty or null.
     * @return The Group List The list of groups.
     */
    @Path("/groups/list")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final UserGroupList listGroups(@QueryParam("key") final String key) {

        String functioname = className + ".listGroups";
        tp.methodEnter("listGroups").println("key", key);

        UserGroupList groups = new UserGroupList();
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IGroupDAO groupDAO = sqlServer.getGroupDAO();

            List<UserGroupLabel> groupsList = groupDAO.listGroups(key);

            groups.setGroupList(groupsList);

            if (groupsList.isEmpty()) {
                groups.setNCRWSSResultCode(ResultBase.RES_GROUP_NOTFOUND);
                tp.println("ResultSet is Empty.");
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, functioname, Logger.RES_EXCEP_DAO,
                    "Failed to list all Groups with key of " + key + ": " + ex.getMessage());
            groups.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functioname, Logger.RES_EXCEP_GENERAL,
                    "Failed to list all Groups with key of " + key + ": " + ex.getMessage());
            groups.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            if (groups.getGroupList() != null) {
                tp.methodExit(groups.getGroupList().size());
            } else {
                tp.methodExit("null");
            }
        }

        return groups;
    }

    /**
     * Updates Group in PRM_GROUP_FUNCTION.
     *
     * @param groupCode
     *            Group code of the group to be updated.
     * @param jsonGroup
     *            Group data to update.
     * @return ViewUserGroup Contains the Group details and Result codes.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/groups/maintenance")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final ViewUserGroup updateGroup(@FormParam("groupcode") final int groupCode,
            @FormParam("group") final String jsonGroup) {

        String functionName = "updateGroup";
        tp.methodEnter(functionName);
        tp.println("GroupCode", groupCode).println("Group", jsonGroup);

        ViewUserGroup viewUserGroup = new ViewUserGroup();

        try {
            if (jsonGroup == null || jsonGroup.isEmpty()) {
                viewUserGroup.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tp.println("group string:" + jsonGroup);
                tp.methodExit("Json Group is invalid.");
                return viewUserGroup;
            } else {
                JsonMarshaller<UserGroup> jsonMarshaller = new JsonMarshaller<UserGroup>();
                UserGroup newUserGroup = jsonMarshaller.unMarshall(jsonGroup, UserGroup.class);

                IGroupDAO groupDao = daoFactory.getGroupDAO();
                viewUserGroup = groupDao.updateGroup(groupCode, newUserGroup);
            }

        } catch (DaoException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to update Group. \n" + e.getMessage());
            if (e.getCause() instanceof SQLException) {
                viewUserGroup.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                viewUserGroup.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update Group. \n" + e.getMessage());
            viewUserGroup.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewUserGroup);
        }

        return viewUserGroup;
    }

    /**
     * check if the string has non-alpha numeric characters.
     *
     * @param str
     *            - the string to check
     * @return true if there are present, false if not
     */
    private boolean hasNonAlpha(final String str) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        return p.matcher(str).find();
    }

    private String getOpeCode() {
        return ((securityContext != null) && (securityContext.getUserPrincipal()) != null)
                ? securityContext.getUserPrincipal().getName() : null;
    }

    // environmental variable name for the server type.
    static final String SERVERTYPE = "SERVERTYPE";
    // environmental variable SERVERTYPE value for the enterprise server.
    static final String ENTERPRISE = "ENTERPRISE";

    void updateTod() {
        // Enterprise server is TOD source. Therefore, it does not need to get
        // TOD.
        if (ENTERPRISE.equals(System.getenv(SERVERTYPE)))
            return;

        TodHelper helper = new TodHelper();
        helper.adjust();
    }
}
