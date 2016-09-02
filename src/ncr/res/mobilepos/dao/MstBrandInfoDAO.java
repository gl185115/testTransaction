package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstBrandInfo;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstBrandInfoDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     * @param connection Connection is given from caller and caller is responsible to close connection.
     */
    public MstBrandInfoDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_BRANDINFO considering default companyId and storeId.
     */
    public MstBrandInfo selectWithDefaultId(final String companyId, final String storeId, final String brandId)
            throws SQLException {
        MstBrandInfo selectedRecord;
        // First attempt, selects with given ids.
        selectedRecord = selectOne(companyId, storeId, brandId);
        if (selectedRecord != null) return selectedRecord;
        // Second attempt, selects with default storeId.
        selectedRecord = selectOne(companyId, DEFAULT_STOREID, brandId);
        if (selectedRecord != null) return selectedRecord;
        // Third attempt, selects with default storeId.
        selectedRecord = selectOne(DEFAULT_COMPANYID, storeId, brandId);
        if (selectedRecord != null) return selectedRecord;
        // Fourth attempt with default company and store.
        selectedRecord = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, brandId);
        return selectedRecord;
    }

    /**
     * Selects MST_BRANDINFO by unique key.
     */
    public MstBrandInfo selectOne(final String companyId, final String storeId, final String brandId)
            throws SQLException {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreId", storeId)
                .println("BrandId", brandId);

        MstBrandInfo selectedRecord = null;

        try (PreparedStatement statement =
                     createSelectOneStatement(companyId, storeId, brandId);
             ResultSet result = statement.executeQuery();) {
            if (result.next()) {
                selectedRecord = new MstBrandInfo();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setBrandId(result.getString("BrandId"));
                selectedRecord.setBrandName(result.getString("BrandName"));
                selectedRecord.setBrandKanaName(result.getString("BrandKanaName"));
            } else {
                tp.println("MstBrandInfo not matched.");
            }
        }
        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement.
     */
    private PreparedStatement createSelectOneStatement(
            final String companyId,
            final String storeId,
            final String brandId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-brandinfo"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, brandId);
        return statement;
    }
}
