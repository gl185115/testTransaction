package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstDptInfo;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstDptInfoDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     *
     * @param connection Connection is given from caller and caller is responsible to close connection.
     */
    public MstDptInfoDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_DPTINFO considering default companyId and storeId.
     */
    public MstDptInfo selectWithDefaultId(final String companyId, final String storeId, final String dptCode)
            throws SQLException {
        MstDptInfo selectedRecord;
        // First attempt, selects with given ids.
        selectedRecord = selectOne(companyId, storeId, dptCode);
        if (selectedRecord != null) return selectedRecord;
        // Second attempt, selects with default storeId.
        selectedRecord = selectOne(companyId, DEFAULT_STOREID, dptCode);
        if (selectedRecord != null) return selectedRecord;
        // Third attempt, selects with default storeId.
        selectedRecord = selectOne(DEFAULT_COMPANYID, storeId, dptCode);
        if (selectedRecord != null) return selectedRecord;
        // Fourth attempt with default company and store.
        selectedRecord = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, dptCode);
        return selectedRecord;
    }

    /**
     * Selects MST_DPTINFO by unique key.
     */
    public MstDptInfo selectOne(final String companyId, final String storeId, final String dptCode)
            throws SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreID", storeId)
                .println("Dpt", dptCode);

        MstDptInfo selectedRecord = null;

        try (PreparedStatement statement = createSelectOneStatement(companyId, storeId, dptCode);
             ResultSet result = statement.executeQuery();
        ) {
            if (result.next()) {
                selectedRecord = new MstDptInfo();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setDpt(result.getString("Dpt"));
                selectedRecord.setDptName(result.getString("DptName"));
                selectedRecord.setDptNameLocal(result.getString("DptNameLocal"));
                selectedRecord.setDptKanaName(result.getString("DptKanaName"));

                selectedRecord.setTaxType(result.getInt("TaxType"));
                selectedRecord.setTaxRate(result.getInt("TaxRate"));
                selectedRecord.setDiscountType(result.getInt("DiscountType"));
                selectedRecord.setExceptionFlag(result.getInt("ExceptionFlag"));

                selectedRecord.setSubNum1(result.getInt("SubNum1"));
                selectedRecord.setSubNum2(result.getInt("SubNum2"));
                selectedRecord.setSubNum3(result.getInt("SubNum3"));
                selectedRecord.setSubNum4(result.getInt("SubNum4"));

                selectedRecord.setStatus(result.getString("Status"));
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
    private PreparedStatement createSelectOneStatement(String companyId, String storeId, String dptCode)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-dptinfo"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, dptCode);
        return statement;
    }
}
