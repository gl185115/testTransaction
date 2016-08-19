package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstBrandInfo;
import ncr.res.mobilepos.dao.model.MstBrandInfo;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstBrandInfoDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_brandinfo";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstBrandInfoDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstBrandInfoDAO mstBrandInfoDAO = new MstBrandInfoDAO(dbManager.getConnection());
        MstBrandInfo selected = mstBrandInfoDAO.selectWithDefaultId("1","1","11");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getBrandId(), "11");
        assertEquals(selected.getBrandName(), "name11");
        assertEquals(selected.getBrandKanaName(), "kana11");
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstBrandInfoDAO mstBrandInfoDAO = new MstBrandInfoDAO(connection);
        MstBrandInfo selected = mstBrandInfoDAO.selectWithDefaultId("1","2","10");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getBrandId(), "10");
        assertEquals(selected.getBrandName(), "name10");
        assertEquals(selected.getBrandKanaName(), "kana10");
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstBrandInfoDAO mstBrandInfoDAO = new MstBrandInfoDAO(connection);
        MstBrandInfo selected = mstBrandInfoDAO.selectWithDefaultId("2","1","01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getBrandId(), "01");
        assertEquals(selected.getBrandName(), "name01");
        assertEquals(selected.getBrandKanaName(), "kana01");
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstBrandInfoDAO mstBrandInfoDAO = new MstBrandInfoDAO(connection);
        MstBrandInfo selected = mstBrandInfoDAO.selectWithDefaultId("2","2","00");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getBrandId(), "00");
        assertEquals(selected.getBrandName(), "name00");
        assertEquals(selected.getBrandKanaName(), "kana00");
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstBrandInfoDAO mstBrandInfoDAO = new MstBrandInfoDAO(connection);
        MstBrandInfo selected = mstBrandInfoDAO.selectWithDefaultId("0","0","44");
        assertNull(selected);
    }

}