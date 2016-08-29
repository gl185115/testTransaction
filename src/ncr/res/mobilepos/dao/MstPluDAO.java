package ncr.res.mobilepos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.dao.model.MstPlu;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.property.SQLStatement;

public class MstPluDAO {
    private static String DEFAULT_COMPANYID = "0";
    private static String DEFAULT_STOREID = "0";

    private Trace.Printer tp;
    private Connection connection;

    public MstPluDAO(Connection connection) {
        this.connection = connection;
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Selects MST_PLU considering default companyId and storeId.
     */
    public MstPlu selectWithDefaultId(
            final String companyId, final String storeId, final String pluCode)
            throws SQLException {
        MstPlu selected;
        // First attempt, selects with given ids.
        selected = selectOne(companyId, storeId, pluCode);
        if (selected != null) return selected;
        // Second attempt, selects with default storeId.
        selected = selectOne(companyId, DEFAULT_STOREID, pluCode);
        if (selected != null) return selected;
        // Third attempt, selects with default storeId.
        selected = selectOne(DEFAULT_COMPANYID, storeId, pluCode);
        if (selected != null) return selected;
        // Fourth attempt with default company and store.
        selected = selectOne(DEFAULT_COMPANYID, DEFAULT_STOREID, pluCode);
        return selected;
    }

    /**
     * Selects MST_PLU by unique key.
     * @param companyId
     * @param storeId
     * @param pluCode
     * @return
     * @throws SQLException
     */
    public MstPlu selectOne(
            final String companyId, final String storeId, final String pluCode)
            throws SQLException {
        tp.methodEnter(
                DebugLogger.getCurrentMethodName())
                .println("CompanyId", companyId)
                .println("StoreID", storeId)
                .println("Plu", pluCode);

        MstPlu selectedRecord = null;

        try(PreparedStatement statement = createSelectOneStatement(companyId, storeId, pluCode);
            ResultSet result = statement.executeQuery();) {
            if (result.next()) {
                selectedRecord = new MstPlu();
                selectedRecord.setCompanyId(result.getString("CompanyId"));
                selectedRecord.setStoreId(result.getString("StoreId"));
                selectedRecord.setMdInternal(result.getString("MdInternal"));
                selectedRecord.setMdType(result.getString("MdType"));
                selectedRecord.setMdVender(result.getString("MdVender"));
                selectedRecord.setDpt(result.getString("Dpt"));
                selectedRecord.setLine(result.getString("Line"));
                selectedRecord.setClassCode(result.getString("Class"));
                selectedRecord.setSku(result.getString("Sku"));

                selectedRecord.setMd01(result.getString("Md01"));
                selectedRecord.setMd02(result.getString("Md02"));
                selectedRecord.setMd03(result.getString("Md03"));
                selectedRecord.setMd04(result.getString("Md04"));
                selectedRecord.setMd05(result.getString("Md05"));
                selectedRecord.setMd06(result.getString("Md06"));
                selectedRecord.setMd07(result.getString("Md07"));
                selectedRecord.setMd08(result.getString("Md08"));
                selectedRecord.setMd09(result.getString("Md09"));
                selectedRecord.setMd10(result.getString("Md10"));

                selectedRecord.setMd11(result.getInt("Md11"));
                selectedRecord.setMd12(result.getInt("Md12"));
                selectedRecord.setMd13(result.getInt("Md13"));
                selectedRecord.setMd14(result.getInt("Md14"));
                selectedRecord.setMd15(result.getInt("Md15"));
                selectedRecord.setMd16(result.getInt("Md16"));

                selectedRecord.setMdNameLocal(result.getString("MdNameLocal"));
                selectedRecord.setMdKanaName(result.getString("MdKanaName"));

                selectedRecord.setOrgSalesPrice1(result.getLong("OrgSalesPrice1"));
                selectedRecord.setSalesPrice1(result.getLong("SalesPrice1"));
                selectedRecord.setSalesPrice2(result.getLong("SalesPrice2"));

                selectedRecord.setCostPrice1(result.getDouble("CostPrice1"));
                selectedRecord.setMakerPrice(result.getLong("MakerPrice"));
                selectedRecord.setTaxType(result.getInt("TaxType"));
                selectedRecord.setTaxRate(result.getInt("TaxRate"));
                selectedRecord.setPaymentType(result.getInt("PaymentType"));

                selectedRecord.setConn1(result.getString("Conn1"));
                selectedRecord.setConn2(result.getString("Conn2"));

                selectedRecord.setSubCode1(result.getString("SubCode1"));
                selectedRecord.setSubCode2(result.getString("SubCode2"));
                selectedRecord.setSubCode3(result.getString("SubCode3"));

                selectedRecord.setSubNum1(result.getInt("SubNum1"));
                selectedRecord.setSubNum2(result.getInt("SubNum2"));

                selectedRecord.setDiscountType(result.getInt("DiscountType"));
            } else {
                tp.println("MstPlu not matched.");
            }
        }

        tp.methodExit(selectedRecord);
        return selectedRecord;
    }

    /**
     * Creates PreparedStatement.
     */
    private PreparedStatement createSelectOneStatement(
            String companyId, String storeId, String pluCode)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                SQLStatement.getInstance().getProperty("select-mst-plu"));

        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, pluCode);
        return statement;
    }
}
