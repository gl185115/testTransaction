package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstBrandInfo;
import ncr.res.mobilepos.dao.model.MstClassInfo;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

public class MstClassInfoDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_classinfo";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstClassInfoDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstClassInfoDAO classInfoDAO = new MstClassInfoDAO(connection);
        MstClassInfo mstClassInfo = classInfoDAO.selectWithDefaultId("1","1","class11","dpt11","line11");

        assertNotNull(mstClassInfo);
        assertEquals(mstClassInfo.getCompanyId(), "1");
        assertEquals(mstClassInfo.getStoreId(), "1");
        assertEquals(mstClassInfo.getClassCode(), "class11");
        assertEquals(mstClassInfo.getDptCode(), "dpt11");
        assertEquals(mstClassInfo.getLineCode(), "line11");
        assertEquals(mstClassInfo.getClassKanaName(), null);
        assertEquals(mstClassInfo.getClassName(), null);
        assertEquals(mstClassInfo.getClassNameLocal(), null);
        assertEquals(mstClassInfo.getSubCode1(), null);
        assertEquals(mstClassInfo.getSubCode2(), null);
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstClassInfoDAO classInfoDAO = new MstClassInfoDAO(connection);
        MstClassInfo mstClassInfo = classInfoDAO.selectWithDefaultId("1","2","class10","dpt10","line10");

        assertNotNull(mstClassInfo);
        assertEquals(mstClassInfo.getCompanyId(), "1");
        assertEquals(mstClassInfo.getStoreId(), "0");
        assertEquals(mstClassInfo.getClassCode(), "class10");
        assertEquals(mstClassInfo.getDptCode(), "dpt10");
        assertEquals(mstClassInfo.getLineCode(), "line10");
        assertEquals(mstClassInfo.getClassKanaName(), null);
        assertEquals(mstClassInfo.getClassName(), null);
        assertEquals(mstClassInfo.getClassNameLocal(), null);
        assertEquals(mstClassInfo.getSubCode1(), null);
        assertEquals(mstClassInfo.getSubCode2(), null);
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstClassInfoDAO classInfoDAO = new MstClassInfoDAO(connection);
        MstClassInfo mstClassInfo = classInfoDAO.selectWithDefaultId("2","1","class01","dpt01","line01");

        assertNotNull(mstClassInfo);
        assertEquals(mstClassInfo.getCompanyId(), "0");
        assertEquals(mstClassInfo.getStoreId(), "1");
        assertEquals(mstClassInfo.getClassCode(), "class01");
        assertEquals(mstClassInfo.getDptCode(), "dpt01");
        assertEquals(mstClassInfo.getLineCode(), "line01");
        assertEquals(mstClassInfo.getClassKanaName(), null);
        assertEquals(mstClassInfo.getClassName(), null);
        assertEquals(mstClassInfo.getClassNameLocal(), null);
        assertEquals(mstClassInfo.getSubCode1(), null);
        assertEquals(mstClassInfo.getSubCode2(), null);
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstClassInfoDAO classInfoDAO = new MstClassInfoDAO(connection);
        MstClassInfo mstClassInfo = classInfoDAO.selectWithDefaultId("2","2","class00","dpt00","line00");

        assertNotNull(mstClassInfo);
        assertEquals(mstClassInfo.getCompanyId(), "0");
        assertEquals(mstClassInfo.getStoreId(), "0");
        assertEquals(mstClassInfo.getClassCode(), "class00");
        assertEquals(mstClassInfo.getDptCode(), "dpt00");
        assertEquals(mstClassInfo.getLineCode(), "line00");
        assertEquals(mstClassInfo.getClassKanaName(), null);
        assertEquals(mstClassInfo.getClassName(), null);
        assertEquals(mstClassInfo.getClassNameLocal(), null);
        assertEquals(mstClassInfo.getSubCode1(), null);
        assertEquals(mstClassInfo.getSubCode2(), null);
    }

    @Test
    public void selectNotMatched() throws SQLException {
        MstClassInfoDAO classInfoDAO = new MstClassInfoDAO(connection);
        MstClassInfo mstClassInfo = classInfoDAO.selectWithDefaultId("0","0","99","99","99");
        assertNull(mstClassInfo);
    }

}