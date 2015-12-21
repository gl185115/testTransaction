/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * ICredentialDAO
 *
 * Is a DAO interface for Operator Sign ON/OFF.
 *
 * Menesses, Chris Niven
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.credential.dao;
/**
 * ���藚��
 * �o�[�W����      ������t        �S���Җ�      ������e
 * 1.01            2014.11.21       LIQIAN       �������Ԏ擾
 * 1.02            2014.11.26       FENGSHA      ���̏��擾
 */
import java.util.List;

import ncr.res.mobilepos.credential.model.Authorization;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.NameMasterInfo;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.model.ViewEmployee;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;

/**
 * ICreadentialDAO
 *
 * A Data Access Object implementation for Operator Sign ON/OFF.
 */
public interface ICredentialDAO {

    /**
     * Signs on an operator.
     *
     * @param operatorNumber
     *            Operator's number
     * @param passCode
     *            Pass code of the operator
     * @param terminalID
     *            ID of the device where the operator account is signed on
     * @return Error code if error occurred otherwise 0
     * @throws Exception
     *             Thrown when error occurs.
     */
    Operator signOnOperator(String companyId, String operatorNumber, String passCode,
            String terminalID) throws DaoException;
    /**
     * Creates a new operator.
     *
     * @param operatorNumber
     *            The operator number.
     * @param passCode
     *            The operator passcode.
     * @return Error code if error occurred otherwise 0
     * @throws DaoException
     *             Thrown when error occurs.
     */
    int createOperator(String operatorNumber, String passCode) throws DaoException;

    /**
     * Signs Off an operator by specifying the operator number.
     *
     * @param operatorNumber
     *            The Operator Number
     * @return The number of rows affected in the database during the Signing
     *         Off
     * @throws DaoException
     *             DaoException thrown when Signing off of an Operator fails
     */
    int signOffOperator(String operatorNumber) throws DaoException;

    /**
     * Checks for operator status (Automatically updates status if validity has
     * expired).
     *
     * @param operatorNumber
     *            The Operator's number.
     * @return Current status of Operator. 0: offline || 1: online
     * @throws DaoException
     *             Thrown when error occurs.
     */
    int checkOperatorStatus(String operatorNumber) throws DaoException;
    /* 1.01 2014.11.21 �������Ԏ擾 ADD START*/
    /**
    *
    * @param empCode    Employee Code
    * @param companyId 
    *
    * @return status of an operator
    */
    Operator getStatusOfOperator(String companyId, String empCode) throws DaoException;
    /* 1.01 2014.11.21 �������Ԏ擾 ADD END*/

    /*1.02 2014.11.26  FENGSHA   ���̏��擾 START*/
    /**
     * FOR Get System Name Master.
     * @return  System Name Master list
     * @throws DaoException
     *             Thrown when error occurs.
     */
    List<NameMasterInfo> getSystemNameMaster(String companyId, String StoreId, String nameCategory)
              throws DaoException;
    /*1.02 2014.11.26 FENGSHA ���̏��擾 END*/

    /**
     * FOR GUEST ACCOUNT.
     *
     * @param terminalId
     *            The terminal id.
     * @param operatorNumber
     *            The operator number.
     * @return 1 for success
     * @throws DaoException
     *             Thrown when error occurs.
     */
    Operator guestSignOnOperator(String operatorNumber, String terminalId)
            throws DaoException;

    /**
     * List Employees.
     *
     * @param retailstoreid
     *            The retail store id.
     * @param key
     *            The key to search in operator No
     * @param name
     * 			  Then name to search in operator name
     *  @param limit
     * 			  if 0, use the systemConfig defined limit
     *            if -1, no limit
     *            if any int value, search limit
     *
     * @return The List of operator
     * @throws Exception
     *             The Exception when the method fails
     */
    List<Employee> listOperators(String retailstoreid, String key, String name, int limit)
            throws Exception;

    /**
     * Reset the operators Passcode.
     *
     * @param retailstoreid
     *            The retail store id.
     * @param operatoreno
     *            The operator id.
     * @return int The number of rows affected.
     * @throws Exception
     *             Thrown when error occurs.
     */
    int resetOperatorPasscode(String retailstoreid, String operatoreno)
            throws Exception;

    /**
     * Gets employee detail.
     *
     * @param retailStoreID
     *            The storeid where the employee belongs to.
     * @param operatorID
     *            The employee number.
     * @param isPasscodeHidden
     *            true if not to include passcode in return. false, if to
     *            include.
     * @return viewEmployee The Object containing resultcode and Employee
     *         details.
     * @throws DaoException
     *             Thrown when exception occurs.
     */
    ViewEmployee viewEmployee(String retailStoreID, String operatorID,
            boolean isPasscodeHidden) throws DaoException;

    /**
     * Creates employee detail.
     *
     * @param retailStoreID
     *            The storeid where the employee belongs to.
     * @param operatorID
     *            The employee number.
     * @param employee
     *            employee object containing details of the employee to add
     * @return ResultBase The Object contains resultcodes.
     * @throws DaoException
     *             Thrown when exception occurs.
     */
    ResultBase createEmployee(String retailStoreID, String operatorID,
            Employee employee) throws DaoException;

    /**
     * Updates employee details.
     *
     * @param retailStoreID
     *            The storeid where the operator belongs.
     * @param currentOperatorID
     *            The operator ID of the operator requesting
     *            the update.
     * @param operatorID
     *            The operator number.
     * @param employee
     *            The employee object containing employee details.
     * @return viewEmployee The employee details with resultcode.
     * @throws DaoException
     *             Thrown when error occurs.
     */
    ViewEmployee updateEmployee(String retailStoreID, String currentOperatorID,
            String operatorID, Employee employee) throws DaoException;

    /**
     * Deletes employee detail.
     *
     * @param retailStoreID
     *            The storeid where the employee belongs to.
     * @param operatorID
     *            The employee number.
     * @return ResultBase The Object that contains result code.
     * @throws DaoException
     *             Thrown when exception occurs.
     */
    ResultBase deleteEmployee(String retailStoreID, String operatorID, String updOpeCode, String updAppId)
            throws DaoException;

    /**
     * Updates operator's old passcode to a new passcode.
     *
     * @param operatorID
     *            the operatorid which passcode should be change.
     * @param oldPasscode
     *            the operators's old passcode.
     * @param newPasscode
     *            the operator's new passcode.
     * @return
     *            the ResultBase. If resultcode is zero(0), it is success.
     * @throws DaoException
     *             the exception thrown when process fail.
     */
    ResultBase changePasscode(String operatorID,
            String oldPasscode, String newPasscode, String updAppId, String updOpeCode) throws DaoException;

    /**
     * Signs on an operator from SPART.
     * @param empCode Employee Id used to log in.
     * @param password Operator's password.
     * @param terminalId of the device where the operator account is signed on.
     * @return Errorcode if error occurred otherwise 0
     * @throws DaoException the exception thrown when process fail.
     */
    Operator credentialSpartLogin(String empCode, String password,
        String terminalId) throws DaoException;
    
    public Authorization getOperatorAuthorization(String companyId, String opeCode) throws DaoException;
}
