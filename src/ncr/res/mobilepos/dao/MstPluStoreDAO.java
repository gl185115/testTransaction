package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstBrandInfo;
import ncr.res.mobilepos.dao.model.MstPluStore;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstPluStoreDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    public MstPluStoreDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_PLU_STORE considering default companyId and storeId.
     */
    public MstPluStore selectWithDefaultId(final String companyId, final String storeId, final String mdInternal)
            throws SQLException {
        MstPluStore selected;
        // First attempt, selects with given ids.
        selected = selectOne(companyId, storeId, mdInternal);
        if (selected != null) return selected;
        // Second attempt, selects with default storeId.
        selected = selectOne(companyId, DEFAULT_STOREID, mdInternal);
        if (selected != null) return selected;
        // Third attempt, selects with default storeId.
        selected = selectOne(DEFAULT_COMPANYID, storeId, mdInternal);
        if (selected != null) return selected;
        // Fourth attempt with default company and store.
        selected = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, mdInternal);
        return selected;
    }

    /**
     * Selects MST_PLU_STORE by unique key.
     * @param companyId
     * @param storeId
     * @param mdInternal
     * @return
     * @throws SQLException
     */
    public MstPluStore selectOne(final String companyId, final String storeId, final String mdInternal)
            throws SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreID", storeId)
                .println("MdInternal", mdInternal);

        MstPluStore selectedRecord = null;

        try(PreparedStatement statement = createSelectOneStatement(companyId, storeId, mdInternal);
            ResultSet result = statement.executeQuery();) {
            if (result.next()) {
                selectedRecord = new MstPluStore();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setMdInternal(result.getString("MdInternal"));
                selectedRecord.setSalesPrice(result.getLong("SalesPrice"));
            } else {
                tp.println("MstPluStore not matched.");
            }
        }

        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement.
     */
    private PreparedStatement createSelectOneStatement(String companyId, String storeId, String mdInternal)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-plu-store"));

        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, mdInternal);
        return statement;
    }
}
