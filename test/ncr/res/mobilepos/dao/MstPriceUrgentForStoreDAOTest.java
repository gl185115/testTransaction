package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstPriceUrgentForStore;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MstPriceUrgentForStoreDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_price_urgent_forstore";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstPriceUrgentForStoreDAOTest", DBInitiator.DATABASE.RESMaster);
        initDataSets(DDL_FILENAME);
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        connection = dbManager.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        Requirements.TearDown();
        connection.close();
    }

    public final void initDataSets(final String dataset) throws Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/dao/" + dataset + ".xml");
    }

    @Test
    public void priceChangeSpecificStoreType() throws SQLException {
        MstPriceUrgentForStoreDAO mstPriceUrgentForStoreDAO =
                new MstPriceUrgentForStoreDAO(this.connection);
        MstPriceUrgentForStore selected =
                mstPriceUrgentForStoreDAO.selectOne("1", "1", "sku11", "color11", "2016-09-01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getSku(), "sku11");
        assertEquals(selected.getColorId(), "color11");
        assertEquals(selected.getRecordId(), "AAA1234567890");
        assertEquals(selected.getNewPrice(), 980);
        assertEquals(selected.getOldPrice(), 1000);
        assertEquals(selected.getTargetStoreType(), "1");
    }

    @Test
    public void priceChangeAllStoreType() throws SQLException {
        MstPriceUrgentForStoreDAO mstPriceUrgentForStoreDAO =
                new MstPriceUrgentForStoreDAO(this.connection);
        MstPriceUrgentForStore selected =
                mstPriceUrgentForStoreDAO.selectOne("1", "2", "sku11", "color11", "2016-09-01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "2");
        assertEquals(selected.getSku(), "sku11");
        assertEquals(selected.getColorId(), "color11");
        assertEquals(selected.getRecordId(), "AAA12345678901");
        assertEquals(selected.getNewPrice(), 1980);
        assertEquals(selected.getOldPrice(), 2000);
        assertEquals(selected.getTargetStoreType(), "2");
    }

    @Test
    public void priceChangeOutDated() throws SQLException {
        MstPriceUrgentForStoreDAO mstPriceUrgentForStoreDAO =
                new MstPriceUrgentForStoreDAO(this.connection);

        MstPriceUrgentForStore selected =
                mstPriceUrgentForStoreDAO.selectOne("1", "1", "sku11", "color11", "2016-03-01");
        assertNotNull(selected);

        selected =
                mstPriceUrgentForStoreDAO.selectOne("1", "1", "sku11", "color11", "2017-03-01");
        assertNull(selected);
    }

    @Test
    public void priceChangeForMultipleResult() throws SQLException {
        MstPriceUrgentForStoreDAO mstPriceUrgentForStoreDAO =
                new MstPriceUrgentForStoreDAO(this.connection);
        MstPriceUrgentForStore selected =
                mstPriceUrgentForStoreDAO.selectOne("3", "1", "sku11", "color11", "2016-09-01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "3");
        assertEquals(selected.getStoreId(), "4");
        assertEquals(selected.getSku(), "sku11");
        assertEquals(selected.getColorId(), "color11");
        assertEquals(selected.getRecordId(), "AAA12345678904");
        assertEquals(selected.getNewPrice(), 2000);
        assertEquals(selected.getOldPrice(), 3000);
        assertEquals(selected.getTargetStoreType(), "2");
    }

}