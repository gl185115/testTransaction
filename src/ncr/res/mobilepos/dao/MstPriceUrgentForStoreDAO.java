package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstPriceUrgentForStore;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstPriceUrgentForStoreDAO {
    private Trace.Printer tp;
    private Connection connection;

    /**
     * Constructor.
     * @param connection
     *  Connection is given from caller and caller is responsible to close connection.
     */
    public MstPriceUrgentForStoreDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects urgent price change information
     * from joined tables of MST_PRICE_URGENT_INFO_FORSTORE and MST_PRICE_URGENT_FORSTORE.
     * @param companyId
     * @param storeId
     * @param sku
     * @param colorId
     * @param businessDate
     * @return
     * @throws SQLStatementException
     * @throws SQLException
     */
    public MstPriceUrgentForStore selectOne(final String companyId, final String storeId,
                                 final String sku, final String colorId, final String businessDate)
            throws SQLStatementException, SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreId", storeId)
                .println("Sku", sku)
                .println("ColorId", colorId)
                .println("BusinessDate", businessDate);

        MstPriceUrgentForStore selectedRecord = null;

        try(
        PreparedStatement statement = createSelectOneStatement(companyId, storeId, sku, colorId, businessDate);
        ResultSet result = statement.executeQuery();
        ) {
            if (result.next()) {
                selectedRecord = new MstPriceUrgentForStore();
                selectedRecord.setSequenceNo(result.getLong("SequenceNo"));
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setRecordId(result.getString("RecordId"));
                selectedRecord.setSku(result.getString("Sku"));
                selectedRecord.setColorId(result.getString("ColorId"));
                selectedRecord.setNewPrice(result.getLong("NewPrice"));
                selectedRecord.setOldPrice(result.getLong("OldPrice"));
                selectedRecord.setTargetStoreType(result.getString("TargetStoreType"));
                selectedRecord.setStoreId(result.getString("StoreId"));
            } else {
                tp.println("MstPriceUrgentForStore not matched.");
            }
        }
        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement for SelectOne method.
     */
    private PreparedStatement createSelectOneStatement(final String companyId, final String storeId,
                                                       final String sku, final String colorId,
                                                       final String businessDate)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-price-urgent-forstore"));

        statement.setString(SQLStatement.PARAM1, sku);
        statement.setString(SQLStatement.PARAM2, colorId);
        statement.setString(SQLStatement.PARAM3, businessDate);
        statement.setString(SQLStatement.PARAM4, storeId);
        statement.setString(SQLStatement.PARAM5, companyId);
        return statement;
    }
}
