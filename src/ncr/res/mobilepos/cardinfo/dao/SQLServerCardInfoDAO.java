package ncr.res.mobilepos.cardinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.cardinfo.model.CardClassInfo;
import ncr.res.mobilepos.cardinfo.model.CardTypeInfo;
import ncr.res.mobilepos.cardinfo.model.MemberInfo;
import ncr.res.mobilepos.cardinfo.model.StatusInfo;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerCardInfoDAO extends AbstractDao implements ICardInfoDAO {
    /**
     * DBManager instance, provides database connection object.
     */
    private DBManager dbManager;
    /**
     * Logger instance, logs error and information.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * program name of the class.
     */
    private static final String PROG_NAME = "CardInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerCardInfoDAO() throws Exception {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Retrieves DBManager.
     * 
     * @return dbManager instance of DBManager.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

    @Override
    public List<CardClassInfo> getCardClassInfo(String companyId, String storeId, String cardClassId,
            String membershipId) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId)
                .println("cardClassId", cardClassId).println("membershipId", membershipId);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        List<CardClassInfo> cardClassInfoList = new ArrayList<CardClassInfo>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            if (membershipId == null) {
                statement = connection.prepareStatement(sqlStatement.getProperty("get-card-class-info-all"));
                statement.setString(SQLStatement.PARAM3, cardClassId);
            } else {
                statement = connection.prepareStatement(sqlStatement.getProperty("get-card-class-info-member"));
                statement.setString(SQLStatement.PARAM3, membershipId);
            }
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);

            result = statement.executeQuery();

            while (result.next()) {
                CardClassInfo cardClassInfo = new CardClassInfo();
                cardClassInfo.setCompanyId(result.getString("CompanyId"));
                cardClassInfo.setStoreId(result.getString("StoreId"));
                cardClassInfo.setId(result.getString("CardClassId"));
                cardClassInfo.setName(result.getString("CardClassName"));
                cardClassInfo.setKanaName(result.getString("CardClassKanaName"));
                cardClassInfo.setShortName(result.getString("CardClassShortName"));
                cardClassInfo.setShortKanaName(result.getString("CardClassShortKanaName"));
                cardClassInfo.setCreditType(result.getString("CreditType"));
                cardClassInfo.setMainCardDigitType(result.getString("MaincardDigitType"));
                cardClassInfo.setRankType(result.getString("RankType"));

                if (membershipId != null) {
                    cardClassInfo.setCardStatusType(result.getString("CardStatusType"));
                }
                cardClassInfoList.add(cardClassInfo);
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get card class info.", e);
            throw new Exception("SQLException: @SQLServerCardInfoDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(cardClassInfoList);
        }
        return cardClassInfoList;
    }

    @Override
    public List<CardTypeInfo> getCardTypeInfoByCardType(String companyId, String storeId, String cardTypeNo)
            throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("cardClassId",
                cardTypeNo);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        CardTypeInfo cardTypeInfo = null;
        List<CardTypeInfo> cardTypeInfos = new ArrayList<CardTypeInfo>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            if (StringUtility.isNullOrEmpty(cardTypeNo)) {
                statement = connection.prepareStatement(sqlStatement.getProperty("get-all-card-type-info"));
                statement.setString(SQLStatement.PARAM1, companyId);
                statement.setString(SQLStatement.PARAM2, storeId);
            } else {
                statement = connection.prepareStatement(sqlStatement.getProperty("get-card-type-info"));
                statement.setString(SQLStatement.PARAM1, companyId);
                statement.setString(SQLStatement.PARAM2, storeId);
                statement.setString(SQLStatement.PARAM3, cardTypeNo);
            }
            result = statement.executeQuery();
            while (result.next()) {
                cardTypeInfo = new CardTypeInfo();
                cardTypeInfo.setCompanyId(result.getString("CompanyId"));
                cardTypeInfo.setStoreId(result.getString("StoreId"));
                cardTypeInfo.setCardTypeId(result.getString("CardTypeId"));
                cardTypeInfo.setCardTypeName(result.getString("CardTypeName"));
                cardTypeInfo.setCardTypeKanaName(result.getString("CardTypeKanaName"));
                cardTypeInfo.setCardTypeShortName(result.getString("CardTypeShortName"));
                cardTypeInfo.setCardTypeShortKanaName(result.getString("CardTypeShortKanaName"));
                cardTypeInfo.setTerminalCompanyId(result.getString("TerminalCompanyId"));
                cardTypeInfo.setCardClassId(result.getString("CardClassId"));
                cardTypeInfo.setMemberType(result.getString("MemberType"));
                cardTypeInfo.setMemberRank(result.getString("MemberRank"));
                cardTypeInfo.setNewRegistFlag(result.getInt("NewRegistFlag"));
                cardTypeInfo.setCardMergeFlag(result.getInt("CardMergeFlag"));
                cardTypeInfo.setPointExpdatePrintFlag(result.getInt("PointExpdatePrintFlag"));
                cardTypeInfo.setDisplayDigitType(result.getString("DisplayDigitType"));
                cardTypeInfo.setPrintDigitType(result.getString("PrintDigitType"));
                cardTypeInfo.setCardCategory(result.getString("CardCategory"));
                cardTypeInfo.setPointAllowanceFlag(result.getString("PointAllowanceFlag"));
                cardTypeInfo.setPointExpdateAddMonth(result.getInt("PointExpDateAddMonth"));
                cardTypeInfo.setuCUseArea(result.getString("UCUseArea"));
                cardTypeInfo.setPrefixCode16From(result.getString("PrefixCode16From"));
                cardTypeInfo.setPrefixCode16To(result.getString("PrefixCode16To"));
                cardTypeInfo.setPrefixCode13(result.getString("PrefixCode13"));
                cardTypeInfo.setCardStatusType(result.getString("CardStatusType"));
                cardTypeInfo.setMagneticDataType(result.getString("MagneticDataType"));
                cardTypeInfo.setCardTypeShortName(result.getString("CardTypeShortName"));
                cardTypeInfo.setSubCode1(result.getString("SubCode1"));
                cardTypeInfo.setOwnCreditFlag(result.getString("OwnCreditFlag"));
                cardTypeInfos.add(cardTypeInfo);
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get card type info.", e);
            throw new Exception("SQLException: @SQLServerCardInfoDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
        }
        tp.methodExit(cardTypeInfos);
        return cardTypeInfos;
    }

    @Override
    public MemberInfo getMemberInfoById(String companyId, String storeId, String membershipId) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("membershipId",
                membershipId);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        MemberInfo memberInfo = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-member-info"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, membershipId);
            result = statement.executeQuery();
            if (result.next()) {
                memberInfo = new MemberInfo();
                memberInfo.setCompanyId(result.getString("CompanyId"));
                memberInfo.setStoreId(result.getString("StoreId"));
                memberInfo.setMemberId(result.getString("MemberId"));
                memberInfo.setCardCompanyId(result.getString("CardCompanyId"));
                memberInfo.setMemberId1(result.getString("MemberId1"));
                memberInfo.setMemberId2(result.getString("MemberId2"));
                memberInfo.setCardType(result.getString("CardType"));
                memberInfo.setMemberRank(result.getString("MemberRank"));
                memberInfo.setMemberStatus(result.getString("MemberStatus"));
                memberInfo.setMemberType(result.getString("MemberType"));
                memberInfo.setMemberTypeNext(result.getString("MemberTypeNext"));
                memberInfo.setMemberTypeUpdDate(result.getString("MemberTypeUpdDate"));
                memberInfo.setSubCode1(result.getString("SubCode1"));
                memberInfo.setSubCode2(result.getString("SubCode2"));
                memberInfo.setSubCode3(result.getString("SubCode3"));
                memberInfo.setSubCode4(result.getString("SubCode4"));
                memberInfo.setSubCode5(result.getString("SubCode5"));
                memberInfo.setSubNum1(result.getString("SubNum1"));
                memberInfo.setSubNum2(result.getString("SubNum2"));
                memberInfo.setSubNum3(result.getString("SubNum3"));
                memberInfo.setSubNum4(result.getString("SubNum4"));
                memberInfo.setSubNum5(result.getString("SubNum5"));
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get membershipId info.", e);
            throw new DaoException("SQLException: @SQLServerCardInfoDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
        }
        tp.methodExit(memberInfo);
        return memberInfo;
    }
    @Override
    public StatusInfo getStatusInfoByCode(String companyId, String storeId, String statusCode) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("statusCode",
                statusCode);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        StatusInfo statusInfo = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-status-info"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, statusCode);
            result = statement.executeQuery();
            if (result.next()) {
                statusInfo = new StatusInfo();
                statusInfo.setCompanyId(result.getString("CompanyId"));
                statusInfo.setStoreId(result.getString("StoreId"));
                statusInfo.setMemberStatus(result.getString("MemberStatus"));
                statusInfo.setMemberStatusName(result.getString("MemberStatusName"));
                statusInfo.setRegisterFlag(result.getString("RegisterFlag"));
                statusInfo.setPointAddFlag(result.getString("PointAddFlag"));
                statusInfo.setPointUseFlag(result.getString("PointUseFlag"));
                statusInfo.setSubCode1(result.getString("SubCode1"));
                statusInfo.setSubCode2(result.getString("SubCode2"));
                statusInfo.setSubCode3(result.getString("SubCode3"));
                statusInfo.setSubCode4(result.getString("SubCode4"));
                statusInfo.setSubCode5(result.getString("SubCode5"));
                statusInfo.setSubNum1(result.getString("SubNum1"));
                statusInfo.setSubNum2(result.getString("SubNum2"));
                statusInfo.setSubNum3(result.getString("SubNum3"));
                statusInfo.setSubNum4(result.getString("SubNum4"));
                statusInfo.setSubNum5(result.getString("SubNum5"));
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get statusCode info.", e);
            throw new DaoException("SQLException: @SQLServerCardInfoDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
        }
        tp.methodExit(statusInfo);
        return statusInfo;
    }

}
