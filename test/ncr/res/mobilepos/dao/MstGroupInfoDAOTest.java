package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstGroupInfo;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstGroupInfoDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_groupinfo";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstGroupInfoDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstGroupInfoDAO mstGroupInfoDAO = new MstGroupInfoDAO(this.connection);
        MstGroupInfo selected = mstGroupInfoDAO.selectWithDefaultId("1","1","grp11");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getGroupId(), "grp11");
        assertEquals(selected.getGroupName(), "name11");
        assertEquals(selected.getGroupKanaName(), "kana11");
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstGroupInfoDAO mstGroupInfoDAO = new MstGroupInfoDAO(connection);
        MstGroupInfo selected = mstGroupInfoDAO.selectWithDefaultId("1","2","grp10");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getGroupId(), "grp10");
        assertEquals(selected.getGroupName(), "name10");
        assertEquals(selected.getGroupKanaName(), "kana10");
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstGroupInfoDAO mstGroupInfoDAO = new MstGroupInfoDAO(connection);
        MstGroupInfo selected = mstGroupInfoDAO.selectWithDefaultId("2","1","grp01");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getGroupId(), "grp01");
        assertEquals(selected.getGroupName(), "name01");
        assertEquals(selected.getGroupKanaName(), "kana01");
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstGroupInfoDAO mstGroupInfoDAO = new MstGroupInfoDAO(connection);
        MstGroupInfo selected = mstGroupInfoDAO.selectWithDefaultId("2","2","grp00");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getGroupId(), "grp00");
        assertEquals(selected.getGroupName(), "name00");
        assertEquals(selected.getGroupKanaName(), "kana00");
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstGroupInfoDAO mstGroupInfoDAO = new MstGroupInfoDAO(connection);
        MstGroupInfo selected = mstGroupInfoDAO.selectWithDefaultId("0","0","grp44");
        assertNull(selected);
    }

}