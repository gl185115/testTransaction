package ncr.res.mobilepos.dao;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.dao.model.MstPlu;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MstPluDAOTest {
    private DBInitiator dbInit;
    private Connection connection;
    private DBManager dbManager;

    private static final String DDL_FILENAME = "mst_plu";

    @Before
    public void setUp() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("MstPluDAOTest", DBInitiator.DATABASE.RESMaster);
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
        MstPluDAO mstPluDAO = new MstPluDAO(this.connection);
        MstPlu selected = mstPluDAO.selectWithDefaultId("1","1","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getMdType(), "0");
        assertEquals(selected.getMdVender(), "vender11");
        assertEquals(selected.getDpt(), "dpt11");
        assertEquals(selected.getLine(), "line11");
        assertEquals(selected.getClassCode(), "class11");
        assertEquals(selected.getSku(), "sku11");

        assertEquals(selected.getMd01(), "color11");
        assertEquals(selected.getMd02(), "md0211");
        assertEquals(selected.getMd03(), "md0311");
        assertEquals(selected.getMd04(), "md0411");
        assertEquals(selected.getMd05(), "size11");
        assertEquals(selected.getMd06(), "md0611");
        assertEquals(selected.getMd07(), "brand11");
        assertEquals(selected.getMd08(), "md0811");
        assertEquals(selected.getMd09(), "md0911");
        assertEquals(selected.getMd10(), "md1011");

        assertEquals(selected.getMd11(), 0);
        assertEquals(selected.getMd12(), 0);
        assertEquals(selected.getMd13(), 0);
        assertEquals(selected.getMd14(), 0);
        assertEquals(selected.getMd15(), 0);
        assertEquals(selected.getMd16(), 0);

        assertEquals(selected.getMdNameLocal(), "name11");
        assertEquals(selected.getMdKanaName(), "kana11");

        assertEquals(selected.getOrgSalesPrice1(), 1000);
        assertEquals(selected.getSalesPrice1(), 990);
        assertEquals(selected.getSalesPrice2(), 980);

        assertEquals(selected.getCostPrice1(), 800, 0);
        assertEquals(selected.getMakerPrice(), 1500);
        assertEquals(selected.getTaxType(), 3);
        assertEquals(selected.getTaxRate(), 8);
        assertEquals(selected.getPaymentType(), 0);

        assertEquals(selected.getConn1(), "con111");
        assertEquals(selected.getConn2(), "con211");

        assertEquals(selected.getSubCode1(), "sub111");
        assertEquals(selected.getSubCode2(), "sub211");
        assertEquals(selected.getSubCode3(), "sub311");

        assertEquals(selected.getSubNum1(), 0);
        assertEquals(selected.getSubNum2(), 0);

        assertEquals(selected.getDiscountType(), 0);
    }

    @Test
    public void selectWithDefaultStoreId() throws Exception {
        MstPluDAO mstPluDAO = new MstPluDAO(this.connection);
        MstPlu selected = mstPluDAO.selectWithDefaultId("1","2","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "1");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getMdType(), "0");
        assertEquals(selected.getMdVender(), "vender10");
        assertEquals(selected.getDpt(), "dpt10");
        assertEquals(selected.getLine(), "line10");
        assertEquals(selected.getClassCode(), "class10");
        assertEquals(selected.getSku(), "sku10");

        assertEquals(selected.getMd01(), "color10");
        assertEquals(selected.getMd02(), "md0210");
        assertEquals(selected.getMd03(), "md0310");
        assertEquals(selected.getMd04(), "md0410");
        assertEquals(selected.getMd05(), "size10");
        assertEquals(selected.getMd06(), "md0610");
        assertEquals(selected.getMd07(), "brand10");
        assertEquals(selected.getMd08(), "md0810");
        assertEquals(selected.getMd09(), "md0910");
        assertEquals(selected.getMd10(), "md1010");

        assertEquals(selected.getMd11(), 0);
        assertEquals(selected.getMd12(), 0);
        assertEquals(selected.getMd13(), 0);
        assertEquals(selected.getMd14(), 0);
        assertEquals(selected.getMd15(), 0);
        assertEquals(selected.getMd16(), 0);

        assertEquals(selected.getMdNameLocal(), "name10");
        assertEquals(selected.getMdKanaName(), "kana10");

        assertEquals(selected.getOrgSalesPrice1(), 1000);
        assertEquals(selected.getSalesPrice1(), 990);
        assertEquals(selected.getSalesPrice2(), 980);

        assertEquals(selected.getCostPrice1(), 800, 0);
        assertEquals(selected.getMakerPrice(), 1500);
        assertEquals(selected.getTaxType(), 2);
        assertEquals(selected.getTaxRate(), 8);
        assertEquals(selected.getPaymentType(), 0);

        assertEquals(selected.getConn1(), "con110");
        assertEquals(selected.getConn2(), "con210");

        assertEquals(selected.getSubCode1(), "sub110");
        assertEquals(selected.getSubCode2(), "sub210");
        assertEquals(selected.getSubCode3(), "sub310");

        assertEquals(selected.getSubNum1(), 0);
        assertEquals(selected.getSubNum2(), 0);

        assertEquals(selected.getDiscountType(), 0);
    }

    @Test
    public void selectWithDefaultCompanyId() throws DaoException, SQLException {
        MstPluDAO mstPluDAO = new MstPluDAO(this.connection);
        MstPlu selected = mstPluDAO.selectWithDefaultId("2","1","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "1");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getMdType(), "0");
        assertEquals(selected.getMdVender(), "vender01");
        assertEquals(selected.getDpt(), "dpt01");
        assertEquals(selected.getLine(), "line01");
        assertEquals(selected.getClassCode(), "class01");
        assertEquals(selected.getSku(), "sku01");

        assertEquals(selected.getMd01(), "color01");
        assertEquals(selected.getMd02(), "md0201");
        assertEquals(selected.getMd03(), "md0301");
        assertEquals(selected.getMd04(), "md0401");
        assertEquals(selected.getMd05(), "size01");
        assertEquals(selected.getMd06(), "md0601");
        assertEquals(selected.getMd07(), "brand01");
        assertEquals(selected.getMd08(), "md0801");
        assertEquals(selected.getMd09(), "md0901");
        assertEquals(selected.getMd10(), "md1001");

        assertEquals(selected.getMd11(), 0);
        assertEquals(selected.getMd12(), 0);
        assertEquals(selected.getMd13(), 0);
        assertEquals(selected.getMd14(), 0);
        assertEquals(selected.getMd15(), 0);
        assertEquals(selected.getMd16(), 0);

        assertEquals(selected.getMdNameLocal(), "name01");
        assertEquals(selected.getMdKanaName(), "kana01");

        assertEquals(selected.getOrgSalesPrice1(), 1000);
        assertEquals(selected.getSalesPrice1(), 990);
        assertEquals(selected.getSalesPrice2(), 980);

        assertEquals(selected.getCostPrice1(), 800, 0);
        assertEquals(selected.getMakerPrice(), 1500);
        assertEquals(selected.getTaxType(), 1);
        assertEquals(selected.getTaxRate(), 8);
        assertEquals(selected.getPaymentType(), 0);

        assertEquals(selected.getConn1(), "con101");
        assertEquals(selected.getConn2(), "con201");

        assertEquals(selected.getSubCode1(), "sub101");
        assertEquals(selected.getSubCode2(), "sub201");
        assertEquals(selected.getSubCode3(), "sub301");

        assertEquals(selected.getSubNum1(), 0);
        assertEquals(selected.getSubNum2(), 0);

        assertEquals(selected.getDiscountType(), 0);
    }

    @Test
    public void selectWithDefaultIds() throws DaoException, SQLException {
        MstPluDAO mstPluDAO = new MstPluDAO(this.connection);
        MstPlu selected = mstPluDAO.selectWithDefaultId("2","2","123456789012345678");

        assertNotNull(selected);
        assertEquals(selected.getCompanyId(), "0");
        assertEquals(selected.getStoreId(), "0");
        assertEquals(selected.getMdInternal(),"123456789012345678");
        assertEquals(selected.getMdType(), "0");
        assertEquals(selected.getMdVender(), "vender00");
        assertEquals(selected.getDpt(), "dpt00");
        assertEquals(selected.getLine(), "line00");
        assertEquals(selected.getClassCode(), "class00");
        assertEquals(selected.getSku(), "sku00");

        assertEquals(selected.getMd01(), "color00");
        assertEquals(selected.getMd02(), "md0200");
        assertEquals(selected.getMd03(), "md0300");
        assertEquals(selected.getMd04(), "md0400");
        assertEquals(selected.getMd05(), "size00");
        assertEquals(selected.getMd06(), "md0600");
        assertEquals(selected.getMd07(), "brand00");
        assertEquals(selected.getMd08(), "md0800");
        assertEquals(selected.getMd09(), "md0900");
        assertEquals(selected.getMd10(), "md1000");

        assertEquals(selected.getMd11(), 0);
        assertEquals(selected.getMd12(), 0);
        assertEquals(selected.getMd13(), 0);
        assertEquals(selected.getMd14(), 0);
        assertEquals(selected.getMd15(), 0);
        assertEquals(selected.getMd16(), 0);

        assertEquals(selected.getMdNameLocal(), "name00");
        assertEquals(selected.getMdKanaName(), "kana00");

        assertEquals(selected.getOrgSalesPrice1(), 1000);
        assertEquals(selected.getSalesPrice1(), 990);
        assertEquals(selected.getSalesPrice2(), 980);

        assertEquals(selected.getCostPrice1(), 800, 0);
        assertEquals(selected.getMakerPrice(), 1500);
        assertEquals(selected.getTaxType(), 0);
        assertEquals(selected.getTaxRate(), 8);
        assertEquals(selected.getPaymentType(), 0);

        assertEquals(selected.getConn1(), "con100");
        assertEquals(selected.getConn2(), "con200");

        assertEquals(selected.getSubCode1(), "sub100");
        assertEquals(selected.getSubCode2(), "sub200");
        assertEquals(selected.getSubCode3(), "sub300");

        assertEquals(selected.getSubNum1(), 0);
        assertEquals(selected.getSubNum2(), 0);

        assertEquals(selected.getDiscountType(), 0);
    }

    @Test
    public void selectNotMatched() throws DaoException, SQLException {
        MstPluDAO mstPluDAO = new MstPluDAO(connection);
        MstPlu selected = mstPluDAO.selectWithDefaultId("2","2","876542310987654321");
        assertNull(selected);
    }

}