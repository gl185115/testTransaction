package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstColorInfo;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstColorInfoDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_colorinfo";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstColorInfoDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstColorInfoDAO mstColorInfoDAO = new MstColorInfoDAO(dbManager.getConnection());
        MstColorInfo selected = mstColorInfoDAO.selectWithDefaultId("1","1","11");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getColorId(), "11");
        assertEquals(selected.getColorName(), "name11");
        assertEquals(selected.getColorKanaName(), "kana11");
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstColorInfoDAO mstColorInfoDAO = new MstColorInfoDAO(connection);
        MstColorInfo selected = mstColorInfoDAO.selectWithDefaultId("1","2","10");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getColorId(), "10");
        assertEquals(selected.getColorName(), "name10");
        assertEquals(selected.getColorKanaName(), "kana10");
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstColorInfoDAO mstColorInfoDAO = new MstColorInfoDAO(connection);
        MstColorInfo selected = mstColorInfoDAO.selectWithDefaultId("2","1","01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getColorId(), "01");
        assertEquals(selected.getColorName(), "name01");
        assertEquals(selected.getColorKanaName(), "kana01");
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstColorInfoDAO mstColorInfoDAO = new MstColorInfoDAO(connection);
        MstColorInfo selected = mstColorInfoDAO.selectWithDefaultId("2","2","00");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getColorId(), "00");
        assertEquals(selected.getColorName(), "name00");
        assertEquals(selected.getColorKanaName(), "kana00");
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstColorInfoDAO mstColorInfoDAO = new MstColorInfoDAO(connection);
        MstColorInfo selected = mstColorInfoDAO.selectWithDefaultId("0","0","44");
        assertNull(selected);
    }

}