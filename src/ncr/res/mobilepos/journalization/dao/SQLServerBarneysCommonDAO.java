package ncr.res.mobilepos.journalization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.journalization.model.DepartmentName;
import ncr.res.mobilepos.journalization.model.EventDetail;
import ncr.res.mobilepos.journalization.model.ForwardListInfo;
import ncr.res.mobilepos.journalization.model.GuestZoneInfo;
import ncr.res.mobilepos.journalization.model.GoldCertificate;
import ncr.res.mobilepos.journalization.model.GoldCertificateInfo;
import ncr.res.mobilepos.journalization.model.Salespersoninfo;
import ncr.res.mobilepos.journalization.model.SearchGuestOrder;
import ncr.res.mobilepos.journalization.model.SearchGuestOrderInfo;
import ncr.res.mobilepos.journalization.model.Reservation;
import ncr.res.mobilepos.journalization.model.ReservationInfo;
import ncr.res.mobilepos.journalization.model.SequenceNo;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerBarneysCommonDAO extends AbstractDao implements
        IBarneysCommonDAO {
    private final String PROG_NAME = "BnsCommomDao";
    /** The database manager. */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * The Constructor of the Class.
     *
     * @throws DaoException
     *             thrown when process fails.
     */
    public SQLServerBarneysCommonDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    // 1.01 2014.11.19 客層情報取得 ADD START
    /**
     * @return return the guest zone information
     *
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
    public ArrayList<GuestZoneInfo> getGuestZoneList() throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        ArrayList<GuestZoneInfo> allguestzonelist = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-all-guestzone"));
            result = select.executeQuery();
            while (result.next()) {
                if(allguestzonelist == null){
                   allguestzonelist = new ArrayList<GuestZoneInfo>();
                }
                GuestZoneInfo guestzoneInfo = new GuestZoneInfo();
                guestzoneInfo.setGuestcode(result.getString("GuestCode"));
                guestzoneInfo.setGuestzonename(result
                        .getString("GuestZoneName"));
                guestzoneInfo.setGuestzonekananame(result
                        .getString("GuestZoneKanaName"));
                guestzoneInfo.setGuestgen(result.getString("GuestGen"));
                guestzoneInfo.setGuestsex(result.getString("GuestSex"));
                guestzoneInfo.setGuesttype(result.getString("GuestType"));
                guestzoneInfo.setSubcode(result.getString("SubCode"));
                allguestzonelist.add(guestzoneInfo);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get guestzone list.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.getGuestZoneList", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get guestzone list.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.getGuestZoneList", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get guestzone list.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.getGuestZoneList", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(allguestzonelist);
        }
        return allguestzonelist;
    }

    // 1.01 2014.11.19 客層情報取得 ADD END
    // 1.02 2014.11.19 前受金情報取得 ADD START
    /**
     * @param guestNo
     *
     * @return return the guest order information
     *
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
    public SearchGuestOrder searchGuestOrderInfo(String guestNo)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("guestNo", guestNo);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        SearchGuestOrder searchGuestOrder = new SearchGuestOrder();
        List<SearchGuestOrderInfo> guestOrderlist = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-guest-order-info"));
            selectStmnt.setString(SQLStatement.PARAM1, guestNo);
            // selectStmnt.setString(SQLStatement.PARAM2, Line);

            resultSet = selectStmnt.executeQuery();
            while (resultSet.next()) {
                if(guestOrderlist == null){
                    guestOrderlist = new ArrayList<SearchGuestOrderInfo>();
                }
                SearchGuestOrderInfo searchGuestOrderInfo = new SearchGuestOrderInfo();
                searchGuestOrderInfo.setGuestNO(resultSet.getString("GuestNO"));
                searchGuestOrderInfo.setLine(resultSet.getString("Line"));
                searchGuestOrderInfo.setRetailStoreId(resultSet
                        .getString("RetailStoreId"));
                searchGuestOrderInfo.setBusinessDate(resultSet
                        .getString("BusinessDate"));
                searchGuestOrderInfo.setBusinessDateTime(resultSet
                        .getString("BusinessDateTime"));
                searchGuestOrderInfo.setTranNo(resultSet.getString("TranNo"));
                searchGuestOrderInfo.setTerminalNo(resultSet
                        .getString("TerminalNo"));
                searchGuestOrderInfo.setOpeCode(resultSet.getString("OpeCode"));
                searchGuestOrderInfo.setOpeName(resultSet.getString("OpeName"));
                searchGuestOrderInfo.setKaiinNo(resultSet.getString("KaiinNo"));
                searchGuestOrderInfo.setKaiinNoSeiSan(resultSet
                        .getString("KaiinNoSeiSan"));
                searchGuestOrderInfo.setOrderKbn(resultSet
                        .getString("OrderKbn"));
                searchGuestOrderInfo.setMdInternal(resultSet
                        .getString("MdInternal"));
                searchGuestOrderInfo.setMd1(resultSet.getString("Md1"));
                searchGuestOrderInfo.setMd2(resultSet.getString("Md2"));
                searchGuestOrderInfo.setMd3(resultSet.getString("Md3"));
                searchGuestOrderInfo.setMd4(resultSet.getString("Md4"));
                searchGuestOrderInfo.setMd5(resultSet.getString("Md5"));
                searchGuestOrderInfo.setMd6(resultSet.getString("Md6"));
                searchGuestOrderInfo.setMd7(resultSet.getString("Md7"));
                searchGuestOrderInfo.setMd8(resultSet.getString("Md8"));
                searchGuestOrderInfo.setMd9(resultSet.getString("Md9"));
                searchGuestOrderInfo.setMd10(resultSet.getString("Md10"));
                searchGuestOrderInfo.setMdName(resultSet.getString("MdName"));
                searchGuestOrderInfo.setSalesCnt(resultSet
                        .getString("SalesCnt"));
                searchGuestOrderInfo.setSalesPrice(resultSet
                        .getString("SalesPrice"));
                searchGuestOrderInfo.setSalesAmt(resultSet
                        .getString("SalesAmt"));
                searchGuestOrderInfo.setDepositAmt(resultSet
                        .getString("DepositAmt"));
                searchGuestOrderInfo.setTelKakuninFlag(resultSet
                        .getString("TelKakuninFlag"));
                searchGuestOrderInfo.setSeisanDate(resultSet
                        .getString("SeisanDate"));
                searchGuestOrderInfo.setSeisanDateTime(resultSet
                        .getString("SeisanDateTime"));
                searchGuestOrderInfo.setCommitSalesAmt(resultSet
                        .getString("CommitSalesAmt"));
                searchGuestOrderInfo.setRepayAmt(resultSet
                        .getString("RepayAmt"));
                searchGuestOrderInfo.setBalanceAmt(resultSet
                        .getString("BalanceAmt"));
                searchGuestOrderInfo.setStatusCode(resultSet
                        .getString("StatusCode"));
                searchGuestOrderInfo.setPOSFlag(resultSet.getString("POSFlag"));
                searchGuestOrderInfo.setPluFlag(resultSet.getString("PluFlag"));
                searchGuestOrderInfo.setMemo(resultSet.getString("Memo"));
                searchGuestOrderInfo.setMerchandiseHierarchy1(resultSet
                        .getString("MerchandiseHierarchy1"));
                searchGuestOrderInfo.setMerchandiseHierarchy2(resultSet
                        .getString("MerchandiseHierarchy2"));
                searchGuestOrderInfo.setMerchandiseHierarchy3(resultSet
                        .getString("MerchandiseHierarchy3"));
                searchGuestOrderInfo.setMerchandiseHierarchy4(resultSet
                        .getString("MerchandiseHierarchy4"));
                searchGuestOrderInfo.setMerchandiseHierarchy5(resultSet
                        .getString("MerchandiseHierarchy5"));
                searchGuestOrderInfo.setTaxType(resultSet.getString("TaxType"));
                searchGuestOrderInfo.setPosMdType(resultSet
                        .getString("PosMdType"));
                searchGuestOrderInfo.setTagType(resultSet.getString("TagType"));
                searchGuestOrderInfo.setKeyPlu(resultSet.getString("KeyPlu"));
                searchGuestOrderInfo.setMdType(resultSet.getString("MdType"));
                searchGuestOrderInfo.setPlu(resultSet.getString("Plu"));
                searchGuestOrderInfo.setFlag1(resultSet.getString("Flag1"));
                searchGuestOrderInfo.setFlag2(resultSet.getString("Flag2"));
                searchGuestOrderInfo.setType1(resultSet.getString("Type1"));
                searchGuestOrderInfo.setType2(resultSet.getString("Type2"));
                searchGuestOrderInfo.setInsDate(resultSet.getString("InsDate"));
                searchGuestOrderInfo.setUpdDate(resultSet.getString("UpdDate"));
                searchGuestOrderInfo.setUpdAppId(resultSet
                        .getString("UpdAppId"));
                searchGuestOrderInfo.setUpdOpeCode(resultSet
                        .getString("UpdOpeCode"));
                //set Department name
                DepartmentName dptName = new DepartmentName();
                dptName.setEn(resultSet.getString("DptName"));
                dptName.setJa(resultSet.getString("DptNameLocal"));
                searchGuestOrderInfo.setDepartmentName(dptName);
                
                guestOrderlist.add(searchGuestOrderInfo);
            }

            if (guestOrderlist == null) {
                searchGuestOrder
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchGuestOrder
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchGuestOrder.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return searchGuestOrder;
            } else {
                searchGuestOrder.setSearchGuestOrderInfo(guestOrderlist);
                searchGuestOrder.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                searchGuestOrder
                        .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                searchGuestOrder.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to search guest order.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.searchGuestOrderInfo",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to search guest order.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.searchGuestOrderInfo", sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to search guest order info.", e);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.searchGuestOrderInfo", e);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(searchGuestOrder);
        }
        return searchGuestOrder;
    }
    // 1.02 2014.11.19 前受金情報取得 ADD START
    // 1.03 2014.11.19 販売員情報取得 ADD START
    /**
     * @param OpeKanaName
     * @return the SalesPerson information
     * @throws DaoException
     *             Thrown when process fails.
     */
    public ArrayList<Salespersoninfo> SearchSalesPerson(final String OpeKanaName)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("OpeKanaName", OpeKanaName);

        ArrayList<Salespersoninfo> allSalesPersoninfo = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-all-salesPerson"));
            select.setString(SQLStatement.PARAM1, OpeKanaName);
            result = select.executeQuery();
            while (result.next()) {
                if(allSalesPersoninfo == null){
                    allSalesPersoninfo = new ArrayList<Salespersoninfo>();
                }
                Salespersoninfo personSalesinfo = new Salespersoninfo();
                personSalesinfo.setOpeKanaName(result.getString("OpeKanaName"));
                personSalesinfo.setEmpCode(result.getString("EmpCode"));
                personSalesinfo.setOpeName(result.getString("OpeName"));

                allSalesPersoninfo.add(personSalesinfo);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get all sales person.",
                    sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.SearchSalesPerson",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get all sales person.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.SearchSalesPerson", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get all sales person.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.SearchSalesPerson", ex);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(allSalesPersoninfo);
        }

        return allSalesPersoninfo;
    }
    // 1.03 2014.11.19 販売員情報取得 ADD END
    // 1.04 2014.11.20 前受金番号取得 ADD START
    /**
     * Gets the current sequence info contains a given key.(SequenceTypeId)
     *
     * @param SequenceTypeId
     *            The primary key of sequence
     * @return SequenceNo
     * @throws DaoException
     */
    @Override
    public SequenceNo getNextSequenceNo(String SequenceTypeId)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("SequenceTypeId", SequenceTypeId);

        SequenceNo sqNo = new SequenceNo();
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement selectAdvancePay = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectAdvancePay = connection.prepareStatement(sqlStatement
                    .getProperty("get-advance-payment"));
            selectAdvancePay.setString(SQLStatement.PARAM1, SequenceTypeId);
            result = selectAdvancePay.executeQuery();
            if (result.next()) {
                sqNo.setSequenceTypeId(result.getString("SequenceTypeId"));
                sqNo.setSequenceNo(result.getString("SequenceNo"));
                sqNo.setSequenceTypeName(result.getString("SequenceTypeName"));
            } else {
                tp.println("sequence number not found.");
            }
            connection.commit();
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName+ ": Failed to get the sequence number",
                    sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.getNextSequenceNo",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    Logger.RES_EXCEP_SQL,
                    functionName+ ": Failed to get the sequence number",
                    sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.getNextSequenceNo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    Logger.RES_EXCEP_GENERAL,
                    functionName+ ": Failed to get the sequence number",
                    ex);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.getNextSequenceNo", ex);
        } finally {
            closeConnectionObjects(connection,selectAdvancePay, result);

            tp.methodExit(sqNo);
        }
        return sqNo;
    }
    // 1.04 2014.11.20 前受金番号取得 ADD END
    // 1.05 2014.11.20 取引番号により前受金番号取得  ADD START
    /**
     * @param storeId
     *            the store id
     * @param deviceId
     *            the device id
     * @param sequenceNo
     *            the sequence No
     * @param businessDate
     *            the business Date
     *
     * @return return the guest order information
     *
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
    public SearchGuestOrder searchGuestOrderInfoBySequenceNo(String storeId,
            String deviceId, String sequenceNo, String businessDate)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeId", storeId)
                .println("deviceId", deviceId)
                .println("sequenceNo", sequenceNo)
                .println("businessDate", businessDate);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        SearchGuestOrder searchGuestOrder = new SearchGuestOrder();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-guest-order-info-by-sequenceNo"));
            selectStmnt.setString(SQLStatement.PARAM1, storeId);
            selectStmnt.setString(SQLStatement.PARAM2, deviceId);
            selectStmnt.setString(SQLStatement.PARAM3, sequenceNo);
            selectStmnt.setString(SQLStatement.PARAM4, businessDate);

            resultSet = selectStmnt.executeQuery();
            if (!resultSet.next()) {
                searchGuestOrder
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchGuestOrder
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchGuestOrder.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return searchGuestOrder;
            } else {
                searchGuestOrder.setGuestNo(resultSet.getString("GuestNO"));
            }
            searchGuestOrder.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            searchGuestOrder.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            searchGuestOrder.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to search guest order By SequenceNo.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.searchGuestOrderInfoBySequenceNo",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to search guest order By SequenceNo.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.searchGuestOrderInfoBySequenceNo", sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to search guest order info By SequenceNo.", e);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.searchGuestOrderInfoBySequenceNo", e);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(searchGuestOrder);
        }
        return searchGuestOrder;
    }
    // 1.05 2014.11.20 取引番号により前受金番号取得 ADD END
    // 1.06 2014.12.22 金種情報取得 ADD Start
    /**
     * @param compCat
     *
     * @return GoldCertificate
     *
     * @throws DaoException
     */
    @Override
    public GoldCertificate getGoldCertificateType(String compCat) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CrCompCat", compCat);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        GoldCertificate goldCertificate = new GoldCertificate();
        List<GoldCertificateInfo> goldCertificateList = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-goldcertificate-type"));
            selectStmnt.setString(SQLStatement.PARAM1, compCat);
            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                if(goldCertificateList == null){
                    goldCertificateList = new ArrayList<GoldCertificateInfo>();
                }
                GoldCertificateInfo goldCertificateInfo = new GoldCertificateInfo();
                goldCertificateInfo.setCrCompCode(resultSet.getString("CrCompCode"));
                goldCertificateInfo.setCrCompName(resultSet.getString("CrCompName"));
                goldCertificateInfo.setCrCompKanaName(resultSet
                        .getString("CrCompKanaName"));
                goldCertificateInfo.setSubcode1(resultSet.getString("Subcode1"));

                goldCertificateList.add(goldCertificateInfo);
            }
            if (goldCertificateList == null) {
                goldCertificate.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                goldCertificate
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                goldCertificate.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return goldCertificate;
            } else {
                goldCertificate.setGoldCertificateInfo(goldCertificateList);
                goldCertificate.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                goldCertificate.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                goldCertificate.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get gold certificate type.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.getGoldCertificateType", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get gold certificate type.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.getGoldCertificateType", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get gold certificate type.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.getGoldCertificateType", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(goldCertificate);
        }

        return goldCertificate;
    }

    // 1.06 2014.12.22 金種情報取得 ADD End
    /**
     * @param eventId
     * @param eventKbn
     * @param businessDateId
     * @param storeId
     * @param pluCode
     * @return return the event information
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
    public List<EventDetail> getEventList(String eventId,int eventKbn,
    		int businessDateId,String storeId,String pluCode) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("eventId",eventId)
        .println("eventKbn",eventKbn)
        .println("businessDateId",businessDateId)
        .println("storeId",storeId)
        .println("pluCode",pluCode);

        ArrayList<EventDetail> eventList = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-event-list"));
            select.setString(SQLStatement.PARAM1,eventId);
            select.setInt(SQLStatement.PARAM2,eventKbn);
            select.setInt(SQLStatement.PARAM3,businessDateId);
            select.setString(SQLStatement.PARAM4,storeId);
            select.setString(SQLStatement.PARAM5,pluCode);
            result = select.executeQuery();
            while (result.next()) {
                if(eventList == null){
                	eventList = new ArrayList<EventDetail>();
                }
                EventDetail eventDetail = new EventDetail();
                eventDetail.setEventId(result.getString("EventId"));
                eventDetail.setEventName(result.getString("EventName"));
                eventDetail.setStartDateId(result.getInt("StartDateId"));
                eventDetail.setEndDateId(result.getInt("EndDateId"));
                eventDetail.setMdInternal(result.getString("MdInternal"));
                eventDetail.setSalesPrice(result.getInt("SalesPrice"));
                eventList.add(eventDetail);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get event list.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.getEventList", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get event list.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.getEventList", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get event list.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.getEventList", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(eventList);
        }
        return eventList;
    }
    /**
     * @param eventId
     * @param storeId
     * @param businessDateId
     * @param eventKbn
     * @return event detail for event login check use.
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
    public EventDetail getEventLoginResultSet(String eventId,String storeId) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("eventId",eventId);
        tp.println("storeId",storeId);
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement select = null;
        EventDetail eventDetail = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-event-for-eventcheck"));
            select.setString(SQLStatement.PARAM1,eventId);
            select.setString(SQLStatement.PARAM2,storeId);
            resultSet = select.executeQuery();
            if (resultSet.next()){
            	eventDetail = new EventDetail();
                eventDetail.setStartDateId(resultSet.getInt("StartDateId"));
                eventDetail.setEndDateId(resultSet.getInt("EndDateId"));
                eventDetail.setEventKbn(resultSet.getInt("EventKbn"));
                eventDetail.setEventId(resultSet.getString("EventId"));
                eventDetail.setEventName(resultSet.getString("EventName"));
                eventDetail.setMdInternal(resultSet.getString("MdInternal"));
                eventDetail.setSalesPrice(resultSet.getInt("SalesPrice"));
            }
            
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get event login result set", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.getEventLoginResultSet", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get event login result set.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.getEventLoginResultSet", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get event login result set.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.getEventLoginResultSet", ex);
        } finally {
            closeConnectionObjects(connection, select, resultSet);
            tp.methodExit(resultSet);
        }
        return eventDetail;
    }
    
    /**
     * @param reservationId
     *
     * @return return the reservation information
     *
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
    public Reservation searchReservationInfo(String reservationId)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("reservationId", reservationId);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        Reservation reservation = new Reservation();
        List<ReservationInfo> reservationInfolist = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-reservation-info"));
            selectStmnt.setString(SQLStatement.PARAM1, reservationId);

            resultSet = selectStmnt.executeQuery();
            
            while (resultSet.next()) {
                if(reservationInfolist == null){
                	reservationInfolist = new ArrayList<ReservationInfo>();
                }
                ReservationInfo reservationInfo = new ReservationInfo();
                reservationInfo.setGuestNO(resultSet.getString("GuestNO"));
                reservationInfo.setLine(resultSet.getString("Line"));
                reservationInfo.setRetailStoreId(resultSet
                        .getString("RetailStoreId"));
                reservationInfo.setBusinessDate(resultSet
                        .getString("BusinessDate"));
                reservationInfo.setBusinessDateTime(resultSet
                        .getString("BusinessDateTime"));
                reservationInfo.setTranNo(resultSet.getString("TranNo"));
                reservationInfo.setTerminalNo(resultSet
                        .getString("TerminalNo"));
                reservationInfo.setOpeCode(resultSet.getString("OpeCode"));
                reservationInfo.setOpeName(resultSet.getString("OpeName"));
                reservationInfo.setKaiinNo(resultSet.getString("KaiinNo"));
                reservationInfo.setKaiinNoSeiSan(resultSet
                        .getString("KaiinNoSeiSan"));
                reservationInfo.setOrderKbn(resultSet
                        .getString("OrderKbn"));
                reservationInfo.setMdInternal(resultSet
                        .getString("MdInternal"));
                reservationInfo.setMd1(resultSet.getString("Md1"));
                reservationInfo.setMd2(resultSet.getString("Md2"));
                reservationInfo.setMd3(resultSet.getString("Md3"));
                reservationInfo.setMd4(resultSet.getString("Md4"));
                reservationInfo.setMd5(resultSet.getString("Md5"));
                reservationInfo.setMd6(resultSet.getString("Md6"));
                reservationInfo.setMd7(resultSet.getString("Md7"));
                reservationInfo.setMd8(resultSet.getString("Md8"));
                reservationInfo.setMd9(resultSet.getString("Md9"));
                reservationInfo.setMd10(resultSet.getString("Md10"));
                reservationInfo.setMdName(resultSet.getString("MdName"));
                reservationInfo.setSalesCnt(resultSet
                        .getString("SalesCnt"));
                reservationInfo.setSalesPrice(resultSet
                        .getString("SalesPrice"));
                reservationInfo.setSalesAmt(resultSet
                        .getString("SalesAmt"));
                reservationInfo.setDepositAmt(resultSet
                        .getString("DepositAmt"));
                reservationInfo.setTelKakuninFlag(resultSet
                        .getString("TelKakuninFlag"));
                reservationInfo.setSeisanDate(resultSet
                        .getString("SeisanDate"));
                reservationInfo.setSeisanDateTime(resultSet
                        .getString("SeisanDateTime"));
                reservationInfo.setCommitSalesAmt(resultSet
                        .getString("CommitSalesAmt"));
                reservationInfo.setRepayAmt(resultSet
                        .getString("RepayAmt"));
                reservationInfo.setBalanceAmt(resultSet
                        .getString("BalanceAmt"));
                reservationInfo.setStatusCode(resultSet
                        .getString("StatusCode"));
                reservationInfo.setPOSFlag(resultSet.getString("POSFlag"));
                reservationInfo.setPluFlag(resultSet.getString("PluFlag"));
                reservationInfo.setMemo(resultSet.getString("Memo"));
                reservationInfo.setMerchandiseHierarchy1(resultSet
                        .getString("MerchandiseHierarchy1"));
                reservationInfo.setMerchandiseHierarchy2(resultSet
                        .getString("MerchandiseHierarchy2"));
                reservationInfo.setMerchandiseHierarchy3(resultSet
                        .getString("MerchandiseHierarchy3"));
                reservationInfo.setMerchandiseHierarchy4(resultSet
                        .getString("MerchandiseHierarchy4"));
                reservationInfo.setMerchandiseHierarchy5(resultSet
                        .getString("MerchandiseHierarchy5"));
                reservationInfo.setTaxType(resultSet.getString("TaxType"));
                reservationInfo.setPosMdType(resultSet
                        .getString("PosMdType"));
                reservationInfo.setTagType(resultSet.getString("TagType"));
                reservationInfo.setKeyPlu(resultSet.getString("KeyPlu"));
                reservationInfo.setMdType(resultSet.getString("MdType"));
                reservationInfo.setPlu(resultSet.getString("Plu"));
                reservationInfo.setFlag1(resultSet.getString("Flag1"));
                reservationInfo.setFlag2(resultSet.getString("Flag2"));
                reservationInfo.setFlag3(resultSet.getString("Flag3"));
                reservationInfo.setFlag4(resultSet.getString("Flag4"));
                reservationInfo.setFlag5(resultSet.getString("Flag5"));
                reservationInfo.setType1(resultSet.getString("Type1"));
                reservationInfo.setType2(resultSet.getString("Type2"));
                reservationInfo.setInsDate(resultSet.getString("InsDate"));
                reservationInfo.setUpdDate(resultSet.getString("UpdDate"));
                reservationInfo.setUpdAppId(resultSet
                        .getString("UpdAppId"));
                reservationInfo.setUpdOpeCode(resultSet
                        .getString("UpdOpeCode"));
                //set Department name
                DepartmentName dptName = new DepartmentName();
                dptName.setEn(resultSet.getString("DptName"));
                dptName.setJa(resultSet.getString("DptNameLocal"));
                reservationInfo.setDepartmentName(dptName);
                
                reservationInfolist.add(reservationInfo);
            }
            
            if (reservationInfolist == null) {
            	reservation
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
            	reservation
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
            	reservation.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return reservation;
            } else {
            	reservation.setReservationInfo(reservationInfolist);
             	closeConnectionObjects(null, selectStmnt, resultSet);
                selectStmnt = connection.prepareStatement(sqlStatement
                        .getProperty("get-reservation-max-line"));
                selectStmnt.setString(SQLStatement.PARAM1, reservationId);
                
                resultSet = selectStmnt.executeQuery();
                if(resultSet.next()){
                	reservation.setMaxLine(resultSet.getString("maxLine"));
                }
            	reservation.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            	reservation
                        .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            	reservation.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to search reservation information.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerBarneysCommenDAO.searchReservationInfo",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to search reservation information.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerBarneysCommenDAO.searchReservationInfo", sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to search reservation information.", e);
            throw new DaoException("Exception:"
                    + " @SQLServerBarneysCommenDAO.searchReservationInfo", e);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(reservation);
        }
        return reservation;
    }

    /**
     * 前捌 リストの取得
     */
    @Override
    public List<ForwardListInfo> getForwardList(String CompanyId, String RetailStoreId, 
            String TrainingFlag, String LayawayFlag) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("CompanyId", CompanyId).println("RetailStoreId", RetailStoreId)
                .println("TrainingFlag", TrainingFlag)
                .println("LayawayFlag", LayawayFlag);

        ArrayList<ForwardListInfo> forwardList = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-forward-list"));
            select.setString(SQLStatement.PARAM1, CompanyId);
            select.setString(SQLStatement.PARAM2, RetailStoreId);
//            select.setString(SQLStatement.PARAM3, WorkstationId);
            select.setString(SQLStatement.PARAM3, TrainingFlag);
            select.setString(SQLStatement.PARAM4, LayawayFlag);
            result = select.executeQuery();
            while (result.next()) {
                if (forwardList == null) {
                    forwardList = new ArrayList<ForwardListInfo>();
                }
                ForwardListInfo forwardListInfo = new ForwardListInfo();
                forwardListInfo.setCompanyId(result.getString("CompanyId"));
                forwardListInfo.setRetailStoreId(result.getString("RetailStoreId"));
                forwardListInfo.setWorkstationId(result.getString("WorkstationId"));
                forwardListInfo.setQueue(result.getString("Queue"));
                forwardListInfo.setSequenceNumber(String.format("%04d", result.getInt("SequenceNumber")));
                forwardListInfo.setTrainingFlag(result.getString("TrainingFlag"));
                forwardListInfo.setBusinessDayDate(result.getString("BusinessDayDate"));
                forwardListInfo.setBusinessDateTime(result.getString("BusinessDateTime"));
                forwardListInfo.setOperatorId(result.getString("OperatorId"));
                forwardListInfo.setOperatorName(result.getString("OperatorName"));
                forwardListInfo.setStatus(result.getString("Status"));
                forwardListInfo.setSalesTotalAmt(result.getString("SalesTotalAmt"));
                forwardList.add(forwardListInfo);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName + ": Failed to get forward list.",
                    sqlStmtEx);
            throw new DaoException("SQLStatementException:" + functionName, sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get forward list.", sqlEx);
            throw new DaoException("SQLException:" + functionName, sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward list.", ex);
            throw new DaoException("Exception:" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(forwardList);
        }
        return forwardList;
    }

    /**
     * 前捌レコード ステータスの更新
     * 
     * @throws SQLException
     */
    @Override
    public int updateForwardStatus(String CompanyId, String RetailStoreId, String WorkstationId, String SequenceNumber,
            String Queue, String BusinessDayDate, String TrainingFlag, int Status) throws DaoException, SQLException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", CompanyId).println("RetailStoreId", RetailStoreId)
                .println("WorkstationId", WorkstationId).println("SequenceNumber", SequenceNumber)
                .println("Queue", Queue).println("BusinessDayDate", BusinessDayDate)
                .println("TrainingFlag", TrainingFlag);

        int result = 0;
        Connection connection = null;
        PreparedStatement updateForwardPrepStmnt = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateForwardPrepStmnt = connection.prepareStatement(sqlStatement.getProperty("update-forward-status"));
            updateForwardPrepStmnt.setInt(SQLStatement.PARAM1, Status);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM2, CompanyId);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM3, RetailStoreId);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM4, WorkstationId);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM5, SequenceNumber.replaceFirst("^0*", ""));
            updateForwardPrepStmnt.setString(SQLStatement.PARAM6, Queue);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM7, BusinessDayDate);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM8, TrainingFlag);

            if (updateForwardPrepStmnt.executeUpdate() != 1) {
                result = ResultBase.RESSYS_ERROR_QB_QUEUEFULL;
            }
            connection.commit();
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to update forward status.", sqlStmtEx);
            connection.rollback();
            throw new DaoException("SQLStatementException:" + functionName, sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to update forward status.",
                    sqlEx);
            connection.rollback();
            throw new DaoException("SQLException:" + functionName, sqlEx);
        } catch (Exception ex) {
            connection.rollback();
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to update forward status.",
                    ex);
            throw new DaoException("Exception:" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, updateForwardPrepStmnt);
            tp.methodExit(result);
        }
        return result;
    }
}
