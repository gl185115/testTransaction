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
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(this.connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("1","1","dpt11");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getDpt(), "dpt11");
        assertEquals(selected.getDptName(), "dptn11");
        assertEquals(selected.getDptNameLocal(), "dptnl11");
        assertEquals(selected.getDptKanaName(), "dptkn11");
        assertEquals(selected.getSubCode1(), "grp11");
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("1","2","dpt10");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getDpt(), "dpt10");
        assertEquals(selected.getDptName(), "dptn10");
        assertEquals(selected.getDptNameLocal(), "dptnl10");
        assertEquals(selected.getDptKanaName(), "dptkn10");
        assertEquals(selected.getSubCode1(), "grp10");
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("2","1","dpt01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getDpt(), "dpt01");
        assertEquals(selected.getDptName(), "dptn01");
        assertEquals(selected.getDptNameLocal(), "dptnl01");
        assertEquals(selected.getDptKanaName(), "dptkn01");
        assertEquals(selected.getSubCode1(), "grp01");
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("2","2","dpt00");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getDpt(), "dpt00");
        assertEquals(selected.getDptName(), "dptn00");
        assertEquals(selected.getDptNameLocal(), "dptnl00");
        assertEquals(selected.getDptKanaName(), "dptkn00");
        assertEquals(selected.getSubCode1(), "grp00");
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstDptInfoDAO mstDptInfoDAO = new MstDptInfoDAO(connection);
        MstDptInfo selected = mstDptInfoDAO.selectWithDefaultId("0","0","dpt44");
        assertNull(selected);
    }

}