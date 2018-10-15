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
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2014.11.21       LIQIAN       操作員状態取得
 * 1.02            2014.11.26       FENGSHA      名称情報取得
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
    /* 1.01 2014.11.21 操作員状態取得 ADD START*/
    /**
    *
    * @param empCode    Employee Code
    * @param companyId 
    *
    * @return status of an operator
    */
    Operator getStatusOfOperator(String companyId, String empCode) throws DaoException;
    /* 1.01 2014.11.21 操作員状態取得 ADD END*/

    /*1.02 2014.11.26  FENGSHA   名称情報取得 START*/
    /**
     * FOR Get System Name Master.
     * @return  System Name Master list
     * @throws DaoException
     *             Thrown when error occurs.
     */
    List<NameMasterInfo> getSystemNameMaster(String companyId, String StoreId, String nameCategory)
              throws DaoException;
    /*1.02 2014.11.26 FENGSHA 名称情報取得 END*/

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
    
    /** CHG BGN 担当者権限の検証 **/
    //public Authorization getOperatorAuthorization(String companyId, String opeCode) throws DaoException;
    public Authorization getOperatorAuthorization(String companyId, String opeCode, String opePass) throws DaoException;
    /** CHG END 担当者権限の検証 **/
}
