package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstGroupInfo;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstGroupInfoDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     * @param connection
     *  Connection is given from caller and caller is responsible to close connection.
     */
    public MstGroupInfoDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_GROUPINFO considering default companyId and storeId.
     * @param companyId
     * @param storeId
     * @param groupId
     * @return
     * @throws SQLException
     */
    public MstGroupInfo selectWithDefaultId(final String companyId, final String storeId, final String groupId)
            throws SQLException {
        MstGroupInfo selectedRecord;
        // First attempt, selects with given ids.
        selectedRecord = selectOne(companyId, storeId, groupId);
        if (selectedRecord != null) return selectedRecord;
        // Second attempt, selects with default storeId.
        selectedRecord = selectOne(companyId, DEFAULT_STOREID, groupId);
        if (selectedRecord != null) return selectedRecord;
        // Third attempt, selects with default storeId.
        selectedRecord = selectOne(DEFAULT_COMPANYID, storeId, groupId);
        if (selectedRecord != null) return selectedRecord;
        // Fourth attempt with default company and store.
        selectedRecord = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, groupId);
        return selectedRecord;
    }

    /**
     * Selects MST_GROUPINFO by unique key.
     * @param companyId
     * @param storeId
     * @param groupId
     * @return
     * @throws SQLException
     */
    public MstGroupInfo selectOne(final String companyId, final String storeId, final String groupId)
            throws SQLException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreId", storeId)
                .println("GroupId", groupId);
        MstGroupInfo selectedRecord = null;
        try(PreparedStatement statement = createSelectOneStatement(companyId, storeId, groupId);
            ResultSet result = statement.executeQuery();) {

            if (result.next()) {
                selectedRecord = new MstGroupInfo();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setGroupId(result.getString("GroupId"));
                selectedRecord.setGroupName(result.getString("GroupName"));
                selectedRecord.setGroupKanaName(result.getString("GroupKanaName"));
            } else {
                tp.println("MstDptInfo not matched.");
            }
        }
        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement.
     */
    private PreparedStatement createSelectOneStatement(String companyId, String storeId, String groupId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-groupinfo"));

        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, groupId);
        return statement;
    }

}
