package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstPluStore;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstPluStoreDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_plu_store";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstPluStoreDAOTest", DBInitiator.DATABASE.RESMaster);
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
    public void selectWithGivenId() throws DaoException, SQLException {
        MstPluStoreDAO mstPluStoreDAO = new MstPluStoreDAO(dbManager.getConnection());
        MstPluStore selected = mstPluStoreDAO.selectWithDefaultId("1","1","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getSalesPrice(), 1011.0, 0.0);
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstPluStoreDAO mstPluStoreDAO = new MstPluStoreDAO(connection);
        MstPluStore selected = mstPluStoreDAO.selectWithDefaultId("1","2","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getSalesPrice(), 1010.0, 0.0);
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstPluStoreDAO mstPluStoreDAO = new MstPluStoreDAO(connection);
        MstPluStore selected = mstPluStoreDAO.selectWithDefaultId("2","1","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getSalesPrice(), 1001.0, 0.0);
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstPluStoreDAO mstPluStoreDAO = new MstPluStoreDAO(connection);
        MstPluStore selected = mstPluStoreDAO.selectWithDefaultId("2","2","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getSalesPrice(), 1000.0, 0.0);
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstPluStoreDAO mstPluStoreDAO = new MstPluStoreDAO(connection);
        MstPluStore selected = mstPluStoreDAO.selectWithDefaultId("2","2","876542310987654321");
        assertNull(selected);
    }

}