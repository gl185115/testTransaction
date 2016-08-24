package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstBrandInfo;
import ncr.res.mobilepos.dao.model.MstNameSystem;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstNameSystemDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_namesystem";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstNameSystemDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstNameSystemDAO mstNameSystemDAO = new MstNameSystemDAO(this.connection);
        MstNameSystem selected = mstNameSystemDAO.selectWithDefaultId("1","1","3","0060");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getNameId(), "3");
        assertEquals(selected.getNameCategory(), "0060");
        assertEquals(selected.getNameIdName(), "nameid11");
        assertEquals(selected.getNameText(), "name11");
        connection.close();
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstNameSystemDAO mstNameSystemDAO = new MstNameSystemDAO(connection);
        MstNameSystem selected = mstNameSystemDAO.selectWithDefaultId("1","2","1","0060");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getNameId(), "1");
        assertEquals(selected.getNameCategory(), "0060");
        assertEquals(selected.getNameIdName(), "nameid10");
        assertEquals(selected.getNameText(), "name10");
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstNameSystemDAO mstNameSystemDAO = new MstNameSystemDAO(connection);
        MstNameSystem selected = mstNameSystemDAO.selectWithDefaultId("2","1","2","0060");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getNameId(), "2");
        assertEquals(selected.getNameCategory(), "0060");
        assertEquals(selected.getNameIdName(), "nameid01");
        assertEquals(selected.getNameText(), "name01");
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstNameSystemDAO mstNameSystemDAO = new MstNameSystemDAO(connection);
        MstNameSystem selected = mstNameSystemDAO.selectWithDefaultId("2","2","0","0060");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getNameId(), "0");
        assertEquals(selected.getNameCategory(), "0060");
        assertEquals(selected.getNameIdName(), "nameid00");
        assertEquals(selected.getNameText(), "name00");
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstNameSystemDAO mstNameSystemDAO = new MstNameSystemDAO(connection);
        MstNameSystem selected = mstNameSystemDAO.selectWithDefaultId("0","0","44","0060");
        assertNull(selected);
    }

}