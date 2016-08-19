package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstDptInfo;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstDptInfoDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_dptinfo";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstDptInfoDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(dbManager.getConnection());
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("1","1","11");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getDpt(), "11");
        assertEquals(selected.getDptName(), "dpt11");
        assertEquals(selected.getDptNameLocal(), "dptl11");
        assertEquals(selected.getDptKanaName(), "dptkn11");
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("1","2","10");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getDpt(), "10");
        assertEquals(selected.getDptName(), "dpt10");
        assertEquals(selected.getDptNameLocal(), "dptl10");
        assertEquals(selected.getDptKanaName(), "dptkn10");
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("2","1","01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getDpt(), "01");
        assertEquals(selected.getDptName(), "dpt01");
        assertEquals(selected.getDptNameLocal(), "dptl01");
        assertEquals(selected.getDptKanaName(), "dptkn01");
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("2","2","00");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getDpt(), "00");
        assertEquals(selected.getDptName(), "dpt00");
        assertEquals(selected.getDptNameLocal(), "dptl00");
        assertEquals(selected.getDptKanaName(), "dptkn00");
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("0","0","44");
        assertNull(selected);
    }

}