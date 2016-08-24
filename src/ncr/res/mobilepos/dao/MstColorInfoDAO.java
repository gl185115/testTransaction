package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstColorInfo;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstColorInfoDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     *
     * @param connection Connection is given from caller and caller is responsible to close connection.
     */
    public MstColorInfoDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_COLORINFO considering default companyId and storeId.
     */
    public MstColorInfo selectWithDefaultId(final String companyId, final String storeId, final String colorId)
            throws SQLException {
        MstColorInfo selectedRecord;
        // First attempt, selects with given ids.
        selectedRecord = selectOne(companyId, storeId, colorId);
        if (selectedRecord != null) return selectedRecord;
        // Second attempt, selects with default storeId.
        selectedRecord = selectOne(companyId, DEFAULT_STOREID, colorId);
        if (selectedRecord != null) return selectedRecord;
        // Third attempt, selects with default storeId.
        selectedRecord = selectOne(DEFAULT_COMPANYID, storeId, colorId);
        if (selectedRecord != null) return selectedRecord;
        // Fourth attempt with default company and store.
        selectedRecord = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, colorId);
        return selectedRecord;
    }

    /**
     * Selects MST_COLORINFO by unique key
     */
    public MstColorInfo selectOne(final String companyId, final String storeId, final String colorId)
            throws SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreId", storeId)
                .println("ColorId", colorId);

        MstColorInfo selectedRecord = null;

        try (PreparedStatement statement =
                     createSelectOneStatement(companyId, storeId, colorId);
             ResultSet result = statement.executeQuery();) {
            if (result.next()) {
                selectedRecord = new MstColorInfo();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setColorId(result.getString("ColorId"));
                selectedRecord.setColorName(result.getString("ColorName"));
                selectedRecord.setColorKanaName(result.getString("ColorKanaName"));
                selectedRecord.setColorShortName(result.getString("ColorShortName"));
                selectedRecord.setColorShortKanaName(result.getString("ColorShortKanaName"));
            } else {
                tp.println("MstColorInfo not matched.");
            }
        }

        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement.
     */
    private PreparedStatement createSelectOneStatement(final String companyId, final String storeId,
                                                       final String colorId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-colorinfo"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, colorId);
        return statement;
    }
}
