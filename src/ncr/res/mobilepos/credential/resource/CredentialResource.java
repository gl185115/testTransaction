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
import ncr.res.mobilepos.credential.dao.ICredentialDAO;
import ncr.res.mobilepos.credential.model.Authorization;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.Employees;
import ncr.res.mobilepos.credential.model.NameMasterInfo;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.model.SystemNameMasterList;
import ncr.res.mobilepos.credential.model.ViewEmployee;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * CredentialResource Class is a Web Resource which support MobilePOS Credential
 * processes.
 *
 */
@Path("/credential")
@Api(value="/credential", description="���[�U�F�؊֘AAPI")
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
    @ApiOperation(value="���[�U�[(�]�ƈ�)���O�C���F��", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_GROUP_NOTFOUND, message="���[�U�O���[�v�����o"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="���[�U�����o"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_PASSCODE_INVALID, message="�p�X���[�h����")
    })
    public final Operator requestSignOn(@ApiParam(name="operatorno", value="�]�ƈ��ԍ�") @PathParam("operatorno") final String operatorNumber,
            @ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") final String companyId,
            @ApiParam(name="passcode", value="�p�X���[�h") @FormParam("passcode") final String passCode,
            @ApiParam(name="terminalid", value="�^�[�~�i���ԍ�") @FormParam("terminalid") final String terminalID,
            @ApiParam(name="demo", value="�f�����[�h�t���O") @FormParam("demo") final boolean demo) {

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
     * Signs off an operator.
     *
     * @param operatorNumber
     *            Unique number identifier of operator
     * @return "OK" if operator sign off success otherwise "NG".
     */
    @Path("/{operatorno}/signoff")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="���[�U�[(�]�ƈ�)���O�I�t", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="���[�U�����o"),
            @ApiResponse(code=ResultBase.RESCREDL_ERROR_NG, message="�p�����[�^����")
        })
    public final ResultBase requestSignOff(@ApiParam(name="operatorno", value="�]�ƈ��ԍ�") @PathParam("operatorno") final String operatorNumber) {

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
    @ApiOperation(value="���[�U�[(�]�ƈ�)��Ԏ擾", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o"),
    })
    public final Operator getStatusOfOperator(@ApiParam(name="companyid", value="��ЃR�[�h") @PathParam("companyid") final String companyid,
    		@ApiParam(name="operatorno", value="�]�ƈ��ԍ�") @PathParam("operatorno") final String empCode) {

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
            /** CHG BGN �S���Ҍ����̌��� **/
            //Authorization authorization = credentialDAO.getOperatorAuthorization(companyid, empCode);
            Authorization authorization = credentialDAO.getOperatorAuthorization(companyid, empCode, null);
            /** CHG END �S���Ҍ����̌��� **/
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
    /** ADD BGN �S���Ҍ����̌��� **/
    /**
     * Check Operator and get its Authorization
     *
     * @param companyid
     * 			��ƃR�[�h�A�K�{
     * @param retailstoreid
     * 			�Y�t�R�[�h�A�s�K�{
     * @param terminalid
     * 			�Y�t�R�[�h�A�s�K�{
     * @param operatorid
     * 			�I�y���[�^�R�[�h�A�K�{
     * @param operatorpass
     *			�I�y���[�^�p�X���[�h�A�K�{
     * @return status of an operator
     */
    @Path("/checkoperator")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="�I�y���[�^�X�e�[�^�X�ƌ���", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RESAUTH_DEVICENOTFOUND, message="�F�ؒ��Ƀf�o�C�X��������܂���ł����B"),
        @ApiResponse(code=ResultBase.RESAUTH_INVALIDPARAMETER, message="�F�؃p�����[�^�������ł��B"),
        @ApiResponse(code=ResultBase.RESAUTH_STOREID_NOTEXIST, message="�w�肳�ꂽ��ƃR�[�h�܂��͓X�܃R�[�h�̓f�[�^�x�[�X�ɑ��݂��܂���"),
        @ApiResponse(code=ResultBase.RESREG_INVALIDPASSCODE, message="�w�肳�ꂽ�p�X�R�[�h�������ł�"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    })
    public final Operator getAttribute(
    		@ApiParam(name="companyid", value="��ЃR�[�h") @QueryParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="�X�ԍ�") @QueryParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="terminalid", value="�^�[�~�i���ԍ�") @QueryParam("terminalid") final String terminalId,
    		@ApiParam(name="operatorid", value="�I�y���[�^�R�[�h") @QueryParam("operatorid") final String operatorId,
    		@ApiParam(name="operatorpass", value="�I�y���[�^�p�X���[�h") @QueryParam("operatorpass") final String operatorPass) {

    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("CompanyId", companyId)
	        .println("RetailStoreId", retailStoreId)
	        .println("WorkStationId", terminalId)
	        .println("OperatorId", operatorId)
        	.println("OperatorPass", operatorPass);

        Operator operator = new Operator();
        
        try {
            if (StringUtility.isNullOrEmpty(companyId, retailStoreId, terminalId, operatorId, operatorPass)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                operator.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                operator.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                operator.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return operator;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICredentialDAO credentialDAO = sqlServer.getCredentialDAO();
            operator = credentialDAO.signOnOperator(companyId, operatorId, operatorPass, terminalId);
            if(operator.getNCRWSSResultCode() == 0){
                Authorization authorization = credentialDAO.getOperatorAuthorization(companyId, operatorId, operatorPass);
                operator.setAuthorization(authorization);
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_DAO, functionName + ": Failed to get status and authorization of operator", ex);
            operator.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            operator.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            operator.setMessage(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get status and authorization of operator",
                    ex);
            operator.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            operator.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            operator.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(operator);
        }
    	return operator;
    }
    /** ADD END �S���Ҍ����̌��� **/
    /**
     * Get system name list
     *
     * @param name
     *            type id (NameTypeId���P���NameTypeId��ݒ�)
     * @param system
     *            name
     * @param except
     *            name type id (ExceptTypeId���P��܂��͕�����NameTypeId��ݒ�)
     * @return system name list
     */
    @Path("/getSystemNameMasterList")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�V�X�e�����̈ꗗ�擾", response=SystemNameMasterList.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o")
    })
    public final SystemNameMasterList getSystemNameMasterList(@ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�ԍ�") @FormParam("storeId") final String storeId,
    		@ApiParam(name="nameCategory", value="���̋敪") @FormParam("nameCategory") final String nameCategory) {

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
    @ApiOperation(value="�S���ҏ�Ԏ擾", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RESCREDL_OPERATOR_OFFLINE, message="�S���҃I�t���C��"),
            @ApiResponse(code=ResultBase.RESCREDL_OPERATOR_ONLINE, message="�S����"),
            @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="�S���Җ���"),
            @ApiResponse(code=ResultBase.RESCREDL_ERROR_NG, message="�G���[")
    })
    public final ResultBase getOperatorStatus(
            @ApiParam(name="operatorno", value="�S���҃R�[�h") @PathParam("operatorno") final String operatorNo) {

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
    @ApiOperation(value="���[�U�[(�]�ƈ�)���X�g�擾", response=Employees.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
        })
    public final Employees listOperators(@ApiParam(name="retailstoreid", value="�X�ԍ�") @QueryParam("retailstoreid") final String retailStoreID,
            @ApiParam(name="key", value="�]�ƈ��ԍ������L�[���[�h") @QueryParam("key") final String key,
            @ApiParam(name="name", value="�]�ƈ��������L�[���[�h") @QueryParam("name") final String name,
            @ApiParam(name="limit", value="�ő�擾����") @QueryParam("limit") final int limit) {

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
    @ApiOperation(value="���[�U�[(�]�ƈ�)�폜", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_ONLINE, message="���[�U�[(�]�ƈ�)�I�����C��"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="���[�U�[(�]�ƈ�)�����o")
    })
    public final ResultBase deleteEmployee(@ApiParam(name="retailstoreid", value="�X�ԍ�") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="operatorid", value="�]�ƈ��ԍ�") @FormParam("operatorid") final String operatorId) {

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
    @ApiOperation(value="���[�U�[(�]�ƈ�)�����e�i���X", response=ViewEmployee.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_NO_UPDATE, message="���[�U�[(�]�ƈ�)�X�V���s"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND, message="���[�U�[�����o"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_OPERATOR_ONLINE, message="���[�U�[(�]�ƈ�)�I�����C��"),
        @ApiResponse(code=ResultBase.RESCREDL_ERROR_EXISTS, message="���[�U�[(�]�ƈ�)��������")
    })
    public final ViewEmployee updateEmployee(@ApiParam(name="retailstoreid", value="�X�ԍ�") @FormParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="currentoperatorid", value="���݂̏]�ƈ��ԍ�") @FormParam("currentoperatorid") final String currentOperatorID,
    		@ApiParam(name="operatorid", value="�]�ƈ��ԍ�") @FormParam("operatorid") final String operatorID,
    		@ApiParam(name="employee", value="�]�ƈ����") @FormParam("employee") final String employeeJSON) {

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
    
    private String getOpeCode() {
        return ((securityContext != null) && (securityContext.getUserPrincipal()) != null)
                ? securityContext.getUserPrincipal().getName() : null;
    }
}
