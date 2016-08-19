package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstClassInfo;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstClassInfoDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     * @param connection
     *  Connection is given from caller and caller is responsible to close connection.
     */
    public MstClassInfoDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_CLASSINFO considering default companyId and storeId.
     * @param companyId
     * @param storeId
     * @param classCode
     * @param dptCode
     * @param lineCode
     * @return
     * @throws SQLException
     */
    public MstClassInfo selectWithDefaultId(
            final String companyId, final String storeId,
            final String classCode, final String dptCode, final String lineCode)
            throws SQLException {
        MstClassInfo selectedRecord;
        // First attempt, selects with given ids.
        selectedRecord = selectOne(companyId, storeId, classCode, dptCode, lineCode);
        if (selectedRecord != null) return selectedRecord;
        // Second attempt, selects with default storeId.
        selectedRecord = selectOne(companyId, DEFAULT_STOREID, classCode, dptCode, lineCode);
        if (selectedRecord != null) return selectedRecord;
        // Third attempt, selects with default storeId.
        selectedRecord = selectOne(DEFAULT_COMPANYID, storeId, classCode, dptCode, lineCode);
        if (selectedRecord != null) return selectedRecord;
        // Fourth attempt with default company and store.
        selectedRecord = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, classCode, dptCode, lineCode);
        return selectedRecord;
    }

    /**
     * Selects MST_CLASSINFO by unique key.
     * @param companyId
     * @param storeId
     * @param classCode
     * @param dptCode
     * @param lineCode
     * @return
     * @throws SQLException
     */
    public MstClassInfo selectOne(final String companyId, final String storeId,
                                  final String classCode, final String dptCode, final String lineCode)
            throws SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreID", storeId)
                .println("Class", classCode)
                .println("Dpt", dptCode)
                .println("Line", lineCode);

        MstClassInfo selectedRecord = null;
        try (PreparedStatement statement =
                     createSelectOneStatement(companyId, storeId, classCode, dptCode, lineCode);
             ResultSet result = statement.executeQuery();) {
            if (result.next()) {
                selectedRecord = new MstClassInfo();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setClassCode(result.getString("Class"));
                selectedRecord.setDptCode(result.getString("Dpt"));
                selectedRecord.setLineCode(result.getString("Line"));
            } else {
                tp.println("MstClassInfo not matched.");
            }
        }
        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement for SelectOne method.
     */
    private PreparedStatement createSelectOneStatement (
            final String companyId,
            final String storeId,
            final String classCode,
            final String dptCode,
            final String lineCode) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-classinfo"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, classCode);
        statement.setString(SQLStatement.PARAM4, dptCode);
        statement.setString(SQLStatement.PARAM5, lineCode);
        return statement;
    }
}
