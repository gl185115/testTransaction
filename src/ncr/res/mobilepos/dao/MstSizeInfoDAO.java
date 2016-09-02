package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstSizeInfo;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstSizeInfoDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     * @param connection
     *  Connection is given from caller and caller is responsible to close connection.
     */
    public MstSizeInfoDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_SIZEINFO considering default companyId and storeId.
     * @param companyId
     * @param storeId
     * @param sizeId
     * @return
     * @throws SQLStatementException
     * @throws SQLException
     */
    public MstSizeInfo selectWithDefaultId(final String companyId, final String storeId, final String sizeId)
            throws SQLStatementException, SQLException {
        MstSizeInfo selectedRecord;
        // First attempt, selects with given ids.
        selectedRecord = selectOne(companyId, storeId, sizeId);
        if (selectedRecord != null) return selectedRecord;
        // Second attempt, selects with default storeId.
        selectedRecord = selectOne(companyId, DEFAULT_STOREID, sizeId);
        if (selectedRecord != null) return selectedRecord;
        // Third attempt, selects with default storeId.
        selectedRecord = selectOne(DEFAULT_COMPANYID, storeId, sizeId);
        if (selectedRecord != null) return selectedRecord;
        // Fourth attempt with default company and store.
        selectedRecord = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, sizeId);
        return selectedRecord;
    }

    /**
     * Selects MST_SIZEINFO by unique key.
     * @param companyId
     * @param storeId
     * @param sizeId
     * @return
     * @throws SQLStatementException
     * @throws SQLException
     */
    public MstSizeInfo selectOne(final String companyId, final String storeId, final String sizeId)
            throws SQLStatementException, SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreId", storeId)
                .println("SizeId", sizeId);

        MstSizeInfo selectedRecord = null;

        try(
        PreparedStatement statement = createSelectOneStatement(companyId, storeId, sizeId);
        ResultSet result = statement.executeQuery();
        ) {
            if (result.next()) {
                selectedRecord = new MstSizeInfo();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setSizeId(result.getString("SizeId"));
                selectedRecord.setSizeName(result.getString("SizeName"));
                selectedRecord.setSizeKanaName(result.getString("SizeKanaName"));
                selectedRecord.setSizeShortName(result.getString("SizeShortName"));
                selectedRecord.setSizeShortKanaName(result.getString("SizeShortKanaName"));
            } else {
                tp.println("MstSizeInfo not matched.");
            }
        }
        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement for SelectOne method.
     */
    private PreparedStatement createSelectOneStatement(String companyId, String storeId, String sizeId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-sizeinfo"));

        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, sizeId);
        return statement;
    }
}
