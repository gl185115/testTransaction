package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstNameSystem;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstNameSystemDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     * @param connection
     *  Connection is given from caller and caller is responsible to close connection.
     */
    public MstNameSystemDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_BRANDINFO considering default companyId and storeId.
     * @param companyId
     * @param storeId
     * @param nameId
     * @param nameCategory
     * @return
     * @throws SQLException
     */
    public MstNameSystem selectWithDefaultId(
            final String companyId, final String storeId,
            final String nameId, final String nameCategory)
            throws SQLException {
        MstNameSystem selectedRecord;
        // First attempt, selects with given ids.
        selectedRecord = selectOne(companyId, storeId, nameId, nameCategory);
        if (selectedRecord != null) return selectedRecord;
        // Second attempt, selects with default storeId.
        selectedRecord = selectOne(companyId, DEFAULT_STOREID, nameId, nameCategory);
        if (selectedRecord != null) return selectedRecord;
        // Third attempt, selects with default storeId.
        selectedRecord = selectOne(DEFAULT_COMPANYID, storeId, nameId, nameCategory);
        if (selectedRecord != null) return selectedRecord;
        // Fourth attempt with default company and store.
        selectedRecord = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, nameId, nameCategory);
        return selectedRecord;
    }

    /**
     * Selects MST_CLASSINFO by unique key.
     * @param companyId
     * @param storeId
     * @param nameId
     * @param nameCategory
     * @return
     * @throws SQLException
     */
    public MstNameSystem selectOne(final String companyId, final String storeId,
                                             final String nameId, final String nameCategory)
            throws SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreId", storeId)
                .println("NameId", nameId)
                .println("NameCategory", nameCategory);

        MstNameSystem selectedRecord = null;

        PreparedStatement statement = createSelectOneStatement(companyId, storeId, nameId, nameCategory);

        ResultSet result = statement.executeQuery();
        if (result.next()) {
            selectedRecord = new MstNameSystem();
            selectedRecord.setCompanyId(result.getString("CompanyId"));
            selectedRecord.setStoreId(result.getString("StoreId"));
            selectedRecord.setNameId(result.getString("NameId"));
            selectedRecord.setNameCategory(result.getString("NameCategory"));
            selectedRecord.setNameText(result.getString("NameText"));
            selectedRecord.setDisplayOrder(result.getInt("DisplayOrder"));
            selectedRecord.setNameIdName(result.getString("NameIdName"));
        } else {
            tp.println("MstNameSystem not matched.");
        }

        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement.
     */
    private PreparedStatement createSelectOneStatement(
            String companyId, String storeId, String nameId, String nameCategory) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-name-system"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, nameId);
        statement.setString(SQLStatement.PARAM4, nameCategory);
        return statement;
    }
}
