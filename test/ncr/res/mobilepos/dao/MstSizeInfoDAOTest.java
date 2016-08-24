package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstSizeInfo;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstSizeInfoDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_sizeinfo";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstSizeInfoDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstSizeInfoDAO mstSizeInfoDAO = new MstSizeInfoDAO(this.connection);
        MstSizeInfo selected = mstSizeInfoDAO.selectWithDefaultId("1","1","11");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getSizeId(), "11");
        assertEquals(selected.getSizeName(), "name11");
        assertEquals(selected.getSizeKanaName(), "kana11");
        assertEquals(selected.getSizeShortName(), "sname11");
        assertEquals(selected.getSizeShortKanaName(), "skana11");
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstSizeInfoDAO mstSizeInfoDAO = new MstSizeInfoDAO(connection);
        MstSizeInfo selected = mstSizeInfoDAO.selectWithDefaultId("1","2","10");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getSizeId(), "10");
        assertEquals(selected.getSizeName(), "name10");
        assertEquals(selected.getSizeKanaName(), "kana10");
        assertEquals(selected.getSizeShortName(), "sname10");
        assertEquals(selected.getSizeShortKanaName(), "skana10");
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstSizeInfoDAO mstSizeInfoDAO = new MstSizeInfoDAO(connection);
        MstSizeInfo selected = mstSizeInfoDAO.selectWithDefaultId("2","1","01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getSizeId(), "01");
        assertEquals(selected.getSizeName(), "name01");
        assertEquals(selected.getSizeKanaName(), "kana01");
        assertEquals(selected.getSizeShortName(), "sname01");
        assertEquals(selected.getSizeShortKanaName(), "skana01");
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstSizeInfoDAO mstSizeInfoDAO = new MstSizeInfoDAO(connection);
        MstSizeInfo selected = mstSizeInfoDAO.selectWithDefaultId("2","1","01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getSizeId(), "01");
        assertEquals(selected.getSizeName(), "name01");
        assertEquals(selected.getSizeKanaName(), "kana01");
        assertEquals(selected.getSizeShortName(), "sname01");
        assertEquals(selected.getSizeShortKanaName(), "skana01");
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstSizeInfoDAO mstSizeInfoDAO = new MstSizeInfoDAO(connection);
        MstSizeInfo selected = mstSizeInfoDAO.selectWithDefaultId("4","4","44");
        assertNull(selected);
    }

}