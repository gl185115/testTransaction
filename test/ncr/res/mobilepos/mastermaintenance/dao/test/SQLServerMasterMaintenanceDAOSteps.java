package ncr.res.mobilepos.mastermaintenance.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;



import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.mastermaintenance.dao.SQLServerMasterMaintenanceDAO;
import ncr.res.mobilepos.mastermaintenance.model.MaintenanceTbl;
import ncr.res.mobilepos.mastermaintenance.model.MdMMMastTbl;
import ncr.res.mobilepos.mastermaintenance.model.MdMastTbl;
import ncr.res.mobilepos.mastermaintenance.model.OpeMastTbl;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class SQLServerMasterMaintenanceDAOSteps extends Steps {
    private DBInitiator dbInit = null;
    private MaintenanceTbl spartData = null;
    private SQLServerMasterMaintenanceDAO sqlservermastermaintenanceTest;
    private ResultBase actualResultBase = null;
    @BeforeScenario
    public final void setUp() throws DaoException {
        Requirements.SetUp();
        this.dbInit = new DBInitiator("SQLServerMasterMaintenanceDAOSteps", DATABASE.RESMaster);
        this.sqlservermastermaintenanceTest = new SQLServerMasterMaintenanceDAO();        
    }

    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a database table {$dataset}")
    public final void givenADatabaseTable(final String dataset) throws DatabaseUnitException, SQLException, Exception{
        this.dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/mastermaintenance/dao/test/" + dataset);
    }

    @SuppressWarnings("deprecation")
    @Given("the following data from SPART for OPE_MAST_TBL: $opeMastTbl")
    public final void givenTheFollowingDataFromSPARTForOpeMastTbl(ExamplesTable opeMastTbl) {
        //||opeCode|password|opeType|opeName|opeKanaName|zipCode|address|telNo|faxNo|secLevel1|secLevel2|subChar1|subChar2|subChar3|insDate|updDate|updAppId|updOpeCode|
        this.spartData = new OpeMastTbl();
        Map<String, String> row = opeMastTbl.getRow(0);
        ((OpeMastTbl)spartData).setAddress((null == row.get("address") || row.get("address").equalsIgnoreCase("NULL")
                ? null : row.get("address")));
        ((OpeMastTbl)spartData).setEmpCode((null == row.get("empcode") || row.get("empcode").equalsIgnoreCase("NULL"))
                ? null : row.get("empcode"));
        ((OpeMastTbl)spartData).setFaxNo((null == row.get("faxno") || row.get("faxno").equalsIgnoreCase("NULL"))
                ? null : row.get("faxno"));
        ((OpeMastTbl)spartData).setInsDate((null == row.get("insdate") ||row.get("insdate").equalsIgnoreCase("NULL"))
                ? null : row.get("insdate"));
        ((OpeMastTbl)spartData).setOpeCode((null == row.get("opecode") || row.get("opecode").equalsIgnoreCase("NULL")) 
                ? null : row.get("opecode"));
        ((OpeMastTbl)spartData).setOpeKanaName((null == row.get("opekananame") ||  row.get("opekananame").equalsIgnoreCase("NULL"))
                ? null :  row.get("opekananame"));
        ((OpeMastTbl)spartData).setOpeName((null == row.get("opename") || row.get("opename").equalsIgnoreCase("NULL"))
                ? null : row.get("opename"));
        ((OpeMastTbl)spartData).setOpeType((null == row.get("opetype") || row.get("opetype").equalsIgnoreCase("NULL"))
                ? null : row.get("opetype"));
        ((OpeMastTbl)spartData).setPassword((null == row.get("password") || row.get("password").equalsIgnoreCase("NULL"))
                ? null : row.get("password"));
        ((OpeMastTbl)spartData).setSecLevel1((null == row.get("seclevel1") || row.get("seclevel1").equalsIgnoreCase("NULL")) ? null : row.get("seclevel1"));
        ((OpeMastTbl)spartData).setSecLevel2((null == row.get("seclevel2") || row.get("seclevel2").equalsIgnoreCase("NULL")) ? null : row.get("seclevel2"));
        ((OpeMastTbl)spartData).setSubChar1((null == row.get("subchar1") || row.get("subchar1").equalsIgnoreCase("NULL"))
                ? null : row.get("subchar1"));
        ((OpeMastTbl)spartData).setSubChar2((null == row.get("subchar2") || row.get("subchar2").equalsIgnoreCase("NULL"))
                ? null : row.get("subchar2"));
        ((OpeMastTbl)spartData).setSubChar3((null == row.get("subchar3") || row.get("subchar3").equalsIgnoreCase("NULL"))
                ? null : row.get("subchar3"));
        ((OpeMastTbl)spartData).setTelNo((null == row.get("telno") || row.get("telno").equalsIgnoreCase("NULL"))
                ? null : row.get("telno"));
        ((OpeMastTbl)spartData).setUpdAppId((null == row.get("updappid") || row.get("updappid").equalsIgnoreCase("NULL"))
                ? null : row.get("updappid"));
        ((OpeMastTbl)spartData).setUpdDate((null == row.get("upddate") || row.get("upddate").equalsIgnoreCase("NULL"))
                ? null : row.get("upddate"));
        ((OpeMastTbl)spartData).setUpdOpeCode((null == row.get("updopecode") || row.get("updopecode").equalsIgnoreCase("NULL"))
                ? null : row.get("updopecode"));
        ((OpeMastTbl)spartData).setZipCode((null == row.get("zipcode") || row.get("zipcode").equalsIgnoreCase("NULL"))
                ? null : row.get("zipcode"));
    }
    
    @Given("the following data from SPART for MST_MIXMATCH: $mdMMMastTbl")
    public void theFollowingDataFromSpartForMstMixMatch(ExamplesTable mdMMMastTbl) {
        this.spartData = new MdMMMastTbl();
         Map<String, String> row = mdMMMastTbl.getRow(0);
         ((MdMMMastTbl)this.spartData).setStoreId((null == row.get("storeid")
                                    || row.get("storeid").equalsIgnoreCase("null"))
                                    ? null : row.get("storeid"));
         ((MdMMMastTbl)this.spartData).setMmCode((null == row.get("code")
                                    || row.get("code").equalsIgnoreCase("null"))
                                    ? null : row.get("code"));
         ((MdMMMastTbl)this.spartData).setMnStartDateId(null == row.get("startdate")
                                    || row.get("startdate").equalsIgnoreCase("null")
                                    ? null :row.get("startdate"));
         ((MdMMMastTbl)this.spartData).setMmEndDateId(null == row.get("enddate")
                                    || row.get("enddate").equalsIgnoreCase("null")
                                    ? null : row.get("enddate"));
         ((MdMMMastTbl)this.spartData).setMmName(null == row.get("name")
                                    || row.get("name").equalsIgnoreCase("null")
                                    ? null : row.get("name"));
         ((MdMMMastTbl)this.spartData).setMmType(null == row.get("type")
                                    || row.get("type").equalsIgnoreCase("null")
                                    ? null : row.get("type"));
         ((MdMMMastTbl)this.spartData).setMustBuyFlag(null == row.get("mustbuyflag")
                                    || row.get("mustbuyflag").equalsIgnoreCase("null")
                                    ? null : row.get("mustbuyflag"));
         ((MdMMMastTbl)this.spartData).setPriceMulti1(null == row.get("quantity1")
                                    || row.get("quantity1").equalsIgnoreCase("null")
                                    ? null : row.get("quantity1"));
         ((MdMMMastTbl)this.spartData).setDiscountPrice1(null == row.get("discountprice1")
                                    || row.get("discountprice1").equalsIgnoreCase("null")
                                    ? null : row.get("discountprice1"));
         ((MdMMMastTbl)this.spartData).setEmpPrice11(null == row.get("empprice11")
                                    || row.get("empprice11").equalsIgnoreCase("null")
                                    ? null : row.get("empprice11"));
         ((MdMMMastTbl)this.spartData).setEmpPrice12(null == row.get("empprice12")
                                    || row.get("empprice12").equalsIgnoreCase("null")
                                    ? null : row.get("empprice12"));
         ((MdMMMastTbl)this.spartData).setEmpPrice13(null == row.get("empprice13")
                                    || row.get("empprice13").equalsIgnoreCase("null")
                                    ? null : row.get("empprice13"));
         ((MdMMMastTbl)this.spartData).setPriceMulti2(null == row.get("quantity2")
                                    || row.get("quantity2").equalsIgnoreCase("null")
                                    ? null : row.get("quantity2"));
         ((MdMMMastTbl)this.spartData).setDiscountPrice2(null == row.get("discountprice2")
                                    || row.get("discountprice2").equalsIgnoreCase("null")
                                    ? null : row.get("discountprice2"));
         ((MdMMMastTbl)this.spartData).setEmpPrice21(null == row.get("empprice21")
                                    || row.get("empprice21").equalsIgnoreCase("null")
                                    ? null : row.get("empprice21"));
         ((MdMMMastTbl)this.spartData).setEmpPrice22(null == row.get("empprice22")
                                    || row.get("empprice22").equalsIgnoreCase("null")
                                    ? null : row.get("empprice22"));
         ((MdMMMastTbl)this.spartData).setEmpPrice23(null == row.get("empprice23")
                                    || row.get("empprice23").equalsIgnoreCase("null")
                                    ? null : row.get("empprice23"));
    }
    
    @Given("the following data from SPART for MST_GROUP_MIXMATCH: $mdMMMastTbl")
    public void theFollowingDataFromSpartForMstGroupMixMatch(ExamplesTable mdMMMastTbl) {
        this.spartData = new MdMMMastTbl();
         Map<String, String> row = mdMMMastTbl.getRow(0);
         ((MdMMMastTbl)this.spartData).setStoreId((null == row.get("storeid")
                            || row.get("storeid").equalsIgnoreCase("null"))
                            ? null : row.get("storeid"));
         ((MdMMMastTbl)this.spartData).setPriceMulti2((null == row.get("code1")
                            || row.get("code1").equalsIgnoreCase("null"))
                            ? null : row.get("code1"));
         ((MdMMMastTbl)this.spartData).setDiscountPrice2((null == row.get("code2")
                            || row.get("code2").equalsIgnoreCase("null"))
                            ? null : row.get("code2"));
         ((MdMMMastTbl)this.spartData).setMnStartDateId(null == row.get("startdate")
                            || row.get("startdate").equalsIgnoreCase("null")
                            ? null : row.get("startdate"));
         ((MdMMMastTbl)this.spartData).setMmEndDateId(null == row.get("enddate")
                            || row.get("enddate").equalsIgnoreCase("null")
                            ? null : row.get("enddate"));
         ((MdMMMastTbl)this.spartData).setMmCode(null == row.get("groupcode")
                            || row.get("groupcode").equalsIgnoreCase("null")
                            ? null : row.get("groupcode"));
         ((MdMMMastTbl)this.spartData).setMmName(null == row.get("name")
                            || row.get("name").equalsIgnoreCase("null")
                            ? null : row.get("name"));
    }
    @Given("the following data from MD_MAST_TBL from SPART: $mdMastTbl")
    public final void givenTheFollowingDataFromSPARTForMstPluTbl(ExamplesTable mdMastTbl) {
    	//Plu | MdType | MdInternal | MdVender | Division | Category | Brand | Sku | Size | KeyPlu | Md1 | Md2 | Md3 | Md4 
    	//| Md5 | Md6 | Md7 | Md8 | Md9 | Md10 | MdName | MdName1 | MdName2 | MdKanaName | MdKanaName1 | MdKanaName2 | MdShortName 
    	//| OrgSalesPrice1 | SalesPrice1 | SalesPrice2 | SalesPriceChgDate1 | SalesPriceChgDate2 | EmpPrice1 | EmpPrice2 | EmpPrice3 
    	//| PuPrice1 | PuPrice2 | PuPriceChgDate1 | PuPriceChgDate2 | OrgCostPrice1 | CostPrice1 | CostPrice2 | CostPriceChgDate1 
    	//| CostPriceChgDate2 | SalesDate | MakerPrice | TaxType | DiscountType | SeasonType | PaymentType | OrderType | PosMdType 
    	//| CatType | OrderUnit | OrderPoint | BaseStockCnt | Conn | ConnType1 | Conn | ConnType2 | VenderCode | VenderType | TagType 
    	//| TagCode | TagCode | TagCode | PointRate | PictureFileName | PictureFileName | SubMoney1 | SubMoney2 | SubMoney3 | SubMoney4 
    	//| SubMoney5 | SubCode | SubCode | SubCode | SubCode | SubCode | SubCode | SubCode | SubCode | SubCode | SubCode10 | SubTinyInt1 
    	//| SubTinyInt2 | SubTinyInt3 | SubTinyInt4 | SubTinyInt5 | SubTinyInt6 | SubTinyInt7 | SubTinyInt8 | SubTinyInt9 | SubTinyInt10 
    	//| InsDate | UpdDate | UpdAppId | UpdOpeCode
        this.spartData = new MdMastTbl();
        Map<String, String> row = mdMastTbl.getRow(0);
        ((MdMastTbl)this.spartData).setStoreid((null == row.get("StoreID") || row.get("StoreID").equalsIgnoreCase("NULL"))
                ? null : row.get("StoreID"));
        ((MdMastTbl)this.spartData).setPlu((null == row.get("Plu") || row.get("Plu").equalsIgnoreCase("NULL"))
                ? null : row.get("Plu"));
        ((MdMastTbl)this.spartData).setMdType((null == row.get("MdType") || row.get("MdType").equalsIgnoreCase("NULL"))
                ? null : row.get("MdType"));
        ((MdMastTbl)this.spartData).setMdInternal((null == row.get("MdInternal") || row.get("MdInternal").equalsIgnoreCase("NULL"))
                ? null : row.get("MdInternal"));
        ((MdMastTbl)this.spartData).setMdVender((null == row.get("MdVender") || row.get("MdVender").equalsIgnoreCase("NULL"))
                ? null : row.get("MdVender"));
        ((MdMastTbl)this.spartData).setDivision((null == row.get("Division") || row.get("Division").equalsIgnoreCase("NULL"))
                ? null : row.get("Division"));
        ((MdMastTbl)this.spartData).setCategory((null == row.get("Category") || row.get("Category").equalsIgnoreCase("NULL"))
                ? null : row.get("Category"));
        ((MdMastTbl)this.spartData).setBrand((null == row.get("Brand") || row.get("Brand").equalsIgnoreCase("NULL"))
                ? null : row.get("Brand"));
        ((MdMastTbl)this.spartData).setSku((null == row.get("Sku") || row.get("Sku").equalsIgnoreCase("NULL"))
                ? null : row.get("Sku"));
        ((MdMastTbl)this.spartData).setSize((null == row.get("Size") || row.get("Size").equalsIgnoreCase("NULL"))
                ? null : row.get("Size"));
        ((MdMastTbl)this.spartData).setKeyPlu((null == row.get("KeyPlu") || row.get("KeyPlu").equalsIgnoreCase("NULL"))
                ? null : row.get("KeyPlu"));
        ((MdMastTbl)this.spartData).setMd1((null == row.get("Md1") || row.get("Md1").equalsIgnoreCase("NULL"))
                ? null : row.get("Md1"));
        ((MdMastTbl)this.spartData).setMd2((null == row.get("Md2") || row.get("Md2").equalsIgnoreCase("NULL"))
                ? null : row.get("Md2"));
        ((MdMastTbl)this.spartData).setMd3((null == row.get("Md3") || row.get("Md3").equalsIgnoreCase("NULL"))
                ? null : row.get("Md3"));
        ((MdMastTbl)this.spartData).setMd4((null == row.get("Md4") || row.get("Md4").equalsIgnoreCase("NULL"))
                ? null : row.get("Md4"));
        ((MdMastTbl)this.spartData).setMd5((null == row.get("Md5") || row.get("Md5").equalsIgnoreCase("NULL"))
                ? null : row.get("Md5"));
        ((MdMastTbl)this.spartData).setMd6((null == row.get("Md6") || row.get("Md6").equalsIgnoreCase("NULL"))
                ? null : row.get("Md6"));
        ((MdMastTbl)this.spartData).setMd7((null == row.get("Md7") || row.get("Md7").equalsIgnoreCase("NULL"))
                ? null : row.get("Md7"));
        ((MdMastTbl)this.spartData).setMd8((null == row.get("Md8") || row.get("Md8").equalsIgnoreCase("NULL"))
                ? null: row.get("Md8"));
        ((MdMastTbl)this.spartData).setMd9((null == row.get("Md9") || row.get("Md9").equalsIgnoreCase("NULL"))
                ? null : row.get("Md9"));
        ((MdMastTbl)this.spartData).setMd10((null == row.get("Md10") || row.get("Md10").equalsIgnoreCase("NULL"))
                ? null : row.get("Md10"));
        ((MdMastTbl)this.spartData).setMdName((null == row.get("MdName") || row.get("MdName").equalsIgnoreCase("NULL"))
                ? null : row.get("MdName"));
        ((MdMastTbl)this.spartData).setMdName1((null == row.get("MdName1") || row.get("MdName1").equalsIgnoreCase("NULL"))
                ? null : row.get("MdName1"));
        ((MdMastTbl)this.spartData).setMdName2((null == row.get("MdName2") || row.get("MdName2").equalsIgnoreCase("NULL"))
                ? null : row.get("MdName2"));
        ((MdMastTbl)this.spartData).setMdKanaName((null == row.get("MdKanaName") || row.get("MdKanaName").equalsIgnoreCase("NULL"))
                ? null : row.get("MdKanaName"));
        ((MdMastTbl)this.spartData).setMdKanaName1((null == row.get("MdKanaName1") || row.get("MdKanaName1").equalsIgnoreCase("NULL"))
                ? null : row.get("MdKanaName1"));
        ((MdMastTbl)this.spartData).setMdKanaName2((null == row.get("MdKanaName2") || row.get("MdKanaName2").equalsIgnoreCase("NULL"))
                ? null : row.get("MdKanaName2"));
        ((MdMastTbl)this.spartData).setMdShortName((null == row.get("MdShortName") || row.get("MdShortName").equalsIgnoreCase("NULL"))
                ? null : row.get("MdShortName"));
        ((MdMastTbl)this.spartData).setOrgSalesPrice1((null == row.get("OrgSalesPrice1") || row.get("OrgSalesPrice1").equalsIgnoreCase("NULL"))
                ? null : row.get("OrgSalesPrice1"));
        ((MdMastTbl)this.spartData).setSalesPrice1((null == row.get("SalesPrice1") || row.get("SalesPrice1").equalsIgnoreCase("NULL"))
                ? null: row.get("SalesPrice1"));
        ((MdMastTbl)this.spartData).setSalesPrice2((null == row.get("SalesPrice2") || row.get("SalesPrice2").equalsIgnoreCase("NULL"))
                ? null : row.get("SalesPrice2"));
        ((MdMastTbl)this.spartData).setSalesPriceChgDate1((null == row.get("SalesPriceChgDate1") || row.get("SalesPriceChgDate1").equalsIgnoreCase("NULL"))
                ? null : row.get("SalesPriceChgDate1"));
        ((MdMastTbl)this.spartData).setSalesPriceChgDate2((null == row.get("SalesPriceChgDate2") || row.get("SalesPriceChgDate2").equalsIgnoreCase("NULL"))
                ? null : row.get("SalesPriceChgDate2"));
        ((MdMastTbl)this.spartData).setEmpPrice1((null == row.get("EmpPrice1") || ("NULL").equalsIgnoreCase(row.get("EmpPrice1")))
                ? null : row.get("EmpPrice1"));
        ((MdMastTbl)this.spartData).setEmpPrice2((null == row.get("EmpPrice2") || row.get("EmpPrice2").equalsIgnoreCase("NULL"))
                ? null : row.get("EmpPrice2"));
        ((MdMastTbl)this.spartData).setEmpPrice3((null == row.get("EmpPrice3") || row.get("EmpPrice3").equalsIgnoreCase("NULL"))
                ? null : row.get("EmpPrice3"));
        ((MdMastTbl)this.spartData).setPuPrice1((null == row.get("PuPrice1") || row.get("PuPrice1").equalsIgnoreCase("NULL"))
                ? null : row.get("PuPrice1"));
        ((MdMastTbl)this.spartData).setPuPrice2((null == row.get("PuPrice2") || row.get("PuPrice2").equalsIgnoreCase("NULL"))
                ? null : row.get("PuPrice2"));
        ((MdMastTbl)this.spartData).setPuPriceChgDate1((null == row.get("PuPriceChgDate") || row.get("PuPriceChgDate").equalsIgnoreCase("NULL"))
                ? null : row.get("PuPriceChgDate"));
        ((MdMastTbl)this.spartData).setPuPriceChgDate2((null == row.get("PuPriceChgDate2") || row.get("PuPriceChgDate2").equalsIgnoreCase("NULL"))
                ? null : row.get("PuPriceChgDate2"));
        ((MdMastTbl)this.spartData).setOrgCostPrice1((null == row.get("OrgCostPrice1") || row.get("OrgCostPrice1").equalsIgnoreCase("NULL"))
                ? null : row.get("OrgCostPrice1"));
        ((MdMastTbl)this.spartData).setCostPrice1((null == row.get("CostPrice1") || row.get("CostPrice1").equalsIgnoreCase("NULL"))
                ? null : row.get("CostPrice1"));
        ((MdMastTbl)this.spartData).setCostPrice2((null == row.get("CostPrice2") || row.get("CostPrice2").equalsIgnoreCase("NULL"))
                ? null : row.get("CostPrice2"));
        ((MdMastTbl)this.spartData).setCostPriceChgDate1((null == row.get("CostPriceChgDate1") || row.get("CostPriceChgDate1").equalsIgnoreCase("NULL"))
                ? null : row.get("CostPriceChgDate1"));
        ((MdMastTbl)this.spartData).setCostPriceChgDate2((null == row.get("CostPriceChgDate2") || row.get("CostPriceChgDate2").equalsIgnoreCase("NULL"))
                ? null : row.get("CostPriceChgDate2"));
        ((MdMastTbl)this.spartData).setSalesDate((null == row.get("SalesDate") || row.get("SalesDate").equalsIgnoreCase("NULL"))
                ? null : row.get("SalesDate"));
        ((MdMastTbl)this.spartData).setMakerPrice((null == row.get("MakerPrice") || row.get("MakerPrice").equalsIgnoreCase("NULL"))
                ? null : row.get("MakerPrice"));
        ((MdMastTbl)this.spartData).setDiscountType((null == row.get("DiscountType") || row.get("DiscountType").equalsIgnoreCase("NULL"))
                ? null : row.get("DiscountType"));
        ((MdMastTbl)this.spartData).setTaxType((null == row.get("TaxType") || row.get("TaxType").equalsIgnoreCase("NULL"))
                ? null : row.get("TaxType"));
        ((MdMastTbl)this.spartData).setPaymentType((null == row.get("PaymentType") || row.get("PaymentType").equalsIgnoreCase("NULL"))
                ? null : row.get("PaymentType"));
        ((MdMastTbl)this.spartData).setOrderPoint((null == row.get("OrderPoint") || row.get("OrderPoint").equalsIgnoreCase("NULL"))
                ? null : row.get("OrderPoint"));
        ((MdMastTbl)this.spartData).setOrderUnit((null == row.get("OrderUnit") || row.get("OrderUnit").equalsIgnoreCase("NULL"))
                ? null : row.get("OrderUnit"));
        ((MdMastTbl)this.spartData).setOrderType((null == row.get("OrderType") || row.get("OrderType").equalsIgnoreCase("NULL"))
                ? null : row.get("OrderType"));
        ((MdMastTbl)this.spartData).setPosMdType((null == row.get("PosMdType") || row.get("PosMdType").equalsIgnoreCase("NULL"))
                ? null : row.get("PosMdType"));
        ((MdMastTbl)this.spartData).setSeasonType((null == row.get("SeasonType") || row.get("SeasonType").equalsIgnoreCase("NULL"))
                ? null : row.get("SeasonType"));
        ((MdMastTbl)this.spartData).setCatType((null == row.get("CatType") || row.get("CatType").equalsIgnoreCase("NULL"))
                ? null : row.get("CatType"));
        ((MdMastTbl)this.spartData).setBaseStockCnt((null == row.get("BaseStockCnt") || row.get("BaseStockCnt").equalsIgnoreCase("NULL"))
                ? null : row.get("BaseStockCnt"));
        ((MdMastTbl)this.spartData).setPointRate((null == row.get("PointRate") || row.get("PointRate").equalsIgnoreCase("NULL"))
                ? null : row.get("PointRate"));
        ((MdMastTbl)this.spartData).setConn1((null == row.get("Conn1") || row.get("Conn1").equalsIgnoreCase("NULL"))
                ? null : row.get("Conn1"));
        ((MdMastTbl)this.spartData).setConn2((null == row.get("Conn2") || row.get("Conn2").equalsIgnoreCase("NULL"))
                ? null : row.get("Conn2"));
        ((MdMastTbl)this.spartData).setConnType1((null == row.get("ConnType1") || row.get("ConnType1").equalsIgnoreCase("NULL"))
                ? null : row.get("ConnType1"));
        ((MdMastTbl)this.spartData).setConnType2((null == row.get("ConnType2") || row.get("ConnType2").equalsIgnoreCase("NULL"))
                ? null : row.get("ConnType2"));
        ((MdMastTbl)this.spartData).setVenderCode((null == row.get("VenderCode") || row.get("VenderCode").equalsIgnoreCase("NULL"))
                ? null : row.get("VenderCode"));
        ((MdMastTbl)this.spartData).setVenderType((null == row.get("VenderType") || row.get("VenderType").equalsIgnoreCase("NULL"))
                ? null : row.get("VenderType"));
        ((MdMastTbl)this.spartData).setTagType((null == row.get("TagType") || row.get("TagType").equalsIgnoreCase("NULL"))
                ? null : row.get("TagType"));
        ((MdMastTbl)this.spartData).setTagCode1((null == row.get("TagCode1") || row.get("TagCode1").equalsIgnoreCase("NULL"))
                ? null : row.get("TagCode1"));
        ((MdMastTbl)this.spartData).setTagCode2((null == row.get("TagCode2") || row.get("TagCode2").equalsIgnoreCase("NULL"))
                ? null : row.get("TagCode2"));
        ((MdMastTbl)this.spartData).setTagCode3((null == row.get("TagCode3") || row.get("TagCode3").equalsIgnoreCase("NULL"))
                ? null : row.get("TagCode3"));
        ((MdMastTbl)this.spartData).setPictureFileName1((null == row.get("PictureFileName1") || row.get("PictureFileName1").equalsIgnoreCase("NULL"))
                ? null : row.get("PictureFileName1"));
        ((MdMastTbl)this.spartData).setPictureFileName2((null == row.get("PictureFileName2") || row.get("PictureFileName2").equalsIgnoreCase("NULL"))
                ? null : row.get("PictureFileName2"));
        ((MdMastTbl)this.spartData).setSubMoney1((null == row.get("SubMoney1") || row.get("SubMoney1").equalsIgnoreCase("NULL"))
                ? null : row.get("SubMoney1"));
        ((MdMastTbl)this.spartData).setSubMoney2((null == row.get("SubMoney2") || row.get("SubMoney2").equalsIgnoreCase("NULL"))
                ? null : row.get("SubMoney2"));
        ((MdMastTbl)this.spartData).setSubMoney3((null == row.get("SubMoney3") || row.get("SubMoney3").equalsIgnoreCase("NULL"))
                ? null : row.get("SubMoney3"));
        ((MdMastTbl)this.spartData).setSubMoney4((null == row.get("SubMoney4") || row.get("SubMoney4").equalsIgnoreCase("NULL"))
                ? null : row.get("SubMoney4"));
        ((MdMastTbl)this.spartData).setSubMoney5((null == row.get("SubMoney5") || row.get("SubMoney5").equalsIgnoreCase("NULL"))
                ? null : row.get("SubMoney5"));
        ((MdMastTbl)this.spartData).setSubCode1((null == row.get("SubCode1") || row.get("SubCode1").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode1"));
        ((MdMastTbl)this.spartData).setSubCode2((null == row.get("SubCode2") || row.get("SubCode2").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode2"));
        ((MdMastTbl)this.spartData).setSubCode3((null == row.get("SubCode3") || row.get("SubCode3").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode3"));
        ((MdMastTbl)this.spartData).setSubCode4((null == row.get("SubCode4") || row.get("SubCode4").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode4"));
        ((MdMastTbl)this.spartData).setSubCode5((null == row.get("SubCode5") || row.get("SubCode5").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode5"));
        ((MdMastTbl)this.spartData).setSubCode6((null == row.get("SubCode6") || row.get("SubCode6").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode6"));
        ((MdMastTbl)this.spartData).setSubCode7((null == row.get("SubCode7") || row.get("SubCode7").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode7"));
        ((MdMastTbl)this.spartData).setSubCode8((null == row.get("SubCode8") || row.get("SubCode8").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode8"));
        ((MdMastTbl)this.spartData).setSubCode9((null == row.get("SubCode9") || row.get("SubCode9").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode9"));
        ((MdMastTbl)this.spartData).setSubCode10((null == row.get("SubCode10") || row.get("SubCode10").equalsIgnoreCase("NULL"))
                ? null : row.get("SubCode10"));
        ((MdMastTbl)this.spartData).setSubTinyInt1((null == row.get("SubTinyInt1") || row.get("SubTinyInt1").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt1"));
        ((MdMastTbl)this.spartData).setSubTinyInt2((null == row.get("SubTinyInt2") || row.get("SubTinyInt2").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt2"));
        ((MdMastTbl)this.spartData).setSubTinyInt3((null == row.get("SubTinyInt3") || row.get("SubTinyInt3").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt3"));
        ((MdMastTbl)this.spartData).setSubTinyInt4((null == row.get("SubTinyInt4") || row.get("SubTinyInt4").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt4"));
        ((MdMastTbl)this.spartData).setSubTinyInt5((null == row.get("SubTinyInt5") || row.get("SubTinyInt5").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt5"));
        ((MdMastTbl)this.spartData).setSubTinyInt6((null == row.get("SubTinyInt6") || row.get("SubTinyInt6").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt6"));
        ((MdMastTbl)this.spartData).setSubTinyInt7((null == row.get("SubTinyInt7") || row.get("SubTinyInt7").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt7"));
        ((MdMastTbl)this.spartData).setSubTinyInt8((null == row.get("SubTinyInt8") || row.get("SubTinyInt8").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt8"));
        ((MdMastTbl)this.spartData).setSubTinyInt9((null == row.get("SubTinyInt9") || row.get("SubTinyInt9").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt9"));
        ((MdMastTbl)this.spartData).setSubInt10((null == row.get("SubTinyInt10") || row.get("SubTinyInt10").equalsIgnoreCase("NULL"))
                ? null : row.get("SubTinyInt10"));
        ((MdMastTbl)this.spartData).setInsDate((null == row.get("InsDate") || row.get("InsDate").equalsIgnoreCase("NULL"))
                ? null : row.get("InsDate"));
        ((MdMastTbl)this.spartData).setUpdAppId((null == row.get("UpdAppId") || row.get("UpdAppId").equalsIgnoreCase("NULL"))
                ? null : row.get("UpdAppId"));
        ((MdMastTbl)this.spartData).setUpdDate((null == row.get("UpdDate") || row.get("UpdDate").equalsIgnoreCase("NULL"))
                ? null : row.get("UpdDate"));
        ((MdMastTbl)this.spartData).setUpdOpeCode((null == row.get("UpdOpeCode") || row.get("UpdOpeCode").equalsIgnoreCase("NULL"))
                ? null : row.get("UpdOpeCode"));    
    }
    @SuppressWarnings("deprecation")
    @When("a DB trigger from SPART")
    public final void whenaDBTriggerFromSpartForMdMastTbl() {
        try {
            this.actualResultBase = this.sqlservermastermaintenanceTest.importSpartData(this.spartData);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("deprecation")
    @Then("OPE_MAST_TBL should have the following rows: $opeMastTbl")
    public final void thenOpeMastTblShouldHaveTheFollwingRows(ExamplesTable opeMastTbl) throws DataSetException {
        ITable actualTable = dbInit.getTableSnapshot("OPE_MAST_TBL");
        
        assertThat("The number of rows in OPE_MAST_TBL", actualTable.getRowCount(), is(equalTo(opeMastTbl.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : opeMastTbl.getRows()) {
        	Assert.assertEquals(item.get("empcode"), (String)actualTable.getValue(i, "EmpCode"));
        	
        	assertThat("OPE_MAST_TBL (row"+i+" - EmpCode",
                    item.get("empcode"), 
                    is(equalTo((String)actualTable.getValue(i, "EmpCode"))));
        	
            assertThat("OPE_MAST_TBL (row"+i+" - OpeCode",
                    item.get("opecode"), 
                    is(equalTo((String)actualTable.getValue(i, "OpeCode"))));
            
            assertThat("OPE_MAST_TBL (row"+i+" - Password",
                    item.get("password"), 
                    is(equalTo((String)actualTable.getValue(i, "Password"))));
            
            assertThat("OPE_MAST_TBL (row"+i+" - OpeType",
                    item.get("opetype"), 
                    is(equalTo((String)actualTable.getValue(i, "OpeType"))));
            
            assertThat("OPE_MAST_TBL (row"+i+" - OpeName",
                    item.get("opename"), 
                    is(equalTo((String)actualTable.getValue(i, "OpeName"))));
            
            assertThat("OPE_MAST_TBL (row"+i+" - OpeKanaName",
                    item.get("opekananame"), 
                    is(equalTo((String)actualTable.getValue(i, "OpeKanaName"))));
            
            assertThat("OPE_MAST_TBL (row"+i+" - ZipCode",
                    item.get("zipcode"), 
                    is(equalTo((String)actualTable.getValue(i, "ZipCode"))));
            assertThat("OPE_MAST_TBL (row"+i+" - Address",
                    item.get("address"), 
                    is(equalTo((String)actualTable.getValue(i, "Address"))));
            assertThat("OPE_MAST_TBL (row"+i+" - TelNo",
                    item.get("telno"), 
                    is(equalTo((String)actualTable.getValue(i, "TelNo"))));
            assertThat("OPE_MAST_TBL (row"+i+" - FaxNo",
                    item.get("faxno"), 
                    is(equalTo((String)actualTable.getValue(i, "FaxNo"))));
            
            assertThat("OPE_MAST_TBL (row"+i+" - SecLevel1", Integer.parseInt(item.get("seclevel1")), is(equalTo((Integer)actualTable.getValue(i, "SecLevel1"))));
            assertThat("OPE_MAST_TBL (row"+i+" - SecLevel2", Integer.parseInt(item.get("seclevel2")), is(equalTo((Integer)actualTable.getValue(i, "SecLevel2"))));
            assertThat("OPE_MAST_TBL (row"+i+" - SubChar1", item.get("subchar1"), is(equalTo((String)actualTable.getValue(i, "SubChar1"))));
            assertThat("OPE_MAST_TBL (row"+i+" - SubChar2", item.get("subchar2"), is(equalTo((String)actualTable.getValue(i, "SubChar2"))));
            
            assertThat("OPE_MAST_TBL (row"+i+" - InsDate",
                    Timestamp.valueOf(item.get("insdate")), 
                    is(equalTo((Timestamp)actualTable.getValue(i, "InsDate"))));
            assertThat("OPE_MAST_TBL (row"+i+" - UpdDate",
                    Timestamp.valueOf(item.get("upddate")), 
                    is(equalTo((Timestamp)actualTable.getValue(i, "UpdDate"))));
            assertThat("OPE_MAST_TBL (row"+i+" - UpdAppId",
                    item.get("updappid"), 
                    is(equalTo((String)actualTable.getValue(i, "UpdAppId"))));
            assertThat("OPE_MAST_TBL (row"+i+" - UpdOpeCode",
                    item.get("updopecode"), 
                    is(equalTo((String)actualTable.getValue(i, "UpdOpeCode"))));
            i++;
        }
    }
    
    @Then("MST_GROUP_MIXMATCH should have the following rows:  $mstgroupmixmatch")
    public final void  mstGroupMixMatchShouldHaveTheFollowingRows(ExamplesTable expectedMixMatchTable) throws DataSetException {
        ITable actualTable = dbInit.getTableSnapshot("MST_GROUP_MIXMATCH");
         assertThat("The number of rows in MST_GROUP_MIXMATCH",
                    actualTable.getRowCount(),
                    is(equalTo(expectedMixMatchTable.getRowCount())));
         int i = 0;        
            for (Map<String, String> expectedMixMatch : expectedMixMatchTable.getRows()) {
             Assert.assertEquals("row"+i+" - storeid", expectedMixMatch.get("storeid").trim(),
                     actualTable.getValue(i, "StoreId").toString().trim());
             Assert.assertEquals("row"+i+" - code1", expectedMixMatch.get("code1"),
                     actualTable.getValue(i, "Code1"));
             Assert.assertEquals("row"+i+" - code2", expectedMixMatch.get("code2"),
                     actualTable.getValue(i, "Code2"));
             Assert.assertEquals("row"+i+" - startdate", Date.valueOf(expectedMixMatch.get("startdate")),
                     (Date) actualTable.getValue(i, "StartDate"));
             Assert.assertEquals("row"+i+" - enddate", Date.valueOf(expectedMixMatch.get("enddate")),
                     (Date) actualTable.getValue(i, "EndDate"));
             if (expectedMixMatch.get("groupcode").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "groupcode"));
             } else {
                 Assert.assertEquals("row"+i+" - groupcode", expectedMixMatch.get("groupcode"),
                         actualTable.getValue(i, "GroupCode"));
             }
             if (expectedMixMatch.get("name").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "Name"));
             } else {
                 Assert.assertEquals("row"+i+" - name", expectedMixMatch.get("name"),
                         actualTable.getValue(i, "Name"));
             }
             i++;
             }
    }

    @Then("MST_MIXMATCH should have the following rows: $mstmixmatch")
    public final void mstMixMatchShouldHaveTheFollowingRows(ExamplesTable expectedMixMatchTable) throws DataSetException {
        ITable actualTable = dbInit.getTableSnapshot("MST_MIXMATCH");
         assertThat("The number of rows in MST_MIXMATCH",
                 actualTable.getRowCount(),
                 is(equalTo(expectedMixMatchTable.getRowCount())));
         int i = 0;        
         for (Map<String, String> expectedMixMatch : expectedMixMatchTable.getRows()) {
             
       
             Assert.assertEquals("row"+i+" - StoreID", expectedMixMatch.get("storeid"),
                     actualTable.getValue(i, "StoreId"));
             Assert.assertEquals("row"+i+" - Code", expectedMixMatch.get("code"),
                     actualTable.getValue(i, "Code"));
             Assert.assertEquals("row"+i+" - Startdate", Date.valueOf(expectedMixMatch.get("startdate")),
                     (Date) actualTable.getValue(i, "StartDate"));
             Assert.assertEquals("row"+i+" - Enddate", Date.valueOf(expectedMixMatch.get("enddate")),
                     (Date) actualTable.getValue(i, "EndDate"));
             if (expectedMixMatch.get("name").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - Name should be NULL", actualTable.getValue(i, "Name"));
             } else {
                 Assert.assertEquals("row"+i+" - name", expectedMixMatch.get("name"),
                         actualTable.getValue(i, "Name"));
             }
             if (expectedMixMatch.get("type").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "Type"));
             } else {            
                 Assert.assertEquals("row"+i+" - type", expectedMixMatch.get("type"),
                     actualTable.getValue(i, "Type"));
             }
             if (expectedMixMatch.get("mustbuyflag").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "MustBuyFlag"));
             } else {            
                 Assert.assertEquals("row"+i+" - mustbuyflag", Integer.valueOf(expectedMixMatch.get("mustbuyflag")),
                         (Integer) actualTable.getValue(i, "MustBuyFlag"));
             }
             if (expectedMixMatch.get("quantity1").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "Quantity1"));
             } else {            
                 Assert.assertEquals("row"+i+" - quantity1", Integer.valueOf(expectedMixMatch.get("quantity1")),
                         (Integer) actualTable.getValue(i, "Quantity1"));
             }
             if (expectedMixMatch.get("discountprice1").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "DiscountPrice1"));
             } else {            
                 Assert.assertEquals("row"+i+" - discountprice1", BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("discountprice1"))),
                         (BigDecimal) actualTable.getValue(i, "DiscountPrice1"));
             }
             if (expectedMixMatch.get("empprice11").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "EmpPrice11"));
             } else {            
                 Assert.assertEquals("row"+i+" - empprice11",  BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("empprice11"))),
                         (BigDecimal) actualTable.getValue(i, "EmpPrice11"));
             }
             if (expectedMixMatch.get("empprice12").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "EmpPrice12"));
             } else {            
                 Assert.assertEquals("row"+i+" - empprice12", BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("empprice12"))),
                         (BigDecimal) actualTable.getValue(i, "EmpPrice12"));
             }
             if (expectedMixMatch.get("empprice13").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "EmpPrice13"));
             } else {            
                 Assert.assertEquals("row"+i+" - empprice13", BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("empprice13"))),
                         (BigDecimal) actualTable.getValue(i, "EmpPrice13"));
             }
             if (expectedMixMatch.get("quantity2").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "Quantity2"));
             } else {            
                 Assert.assertEquals("row"+i+" - quantity2", Integer.valueOf(expectedMixMatch.get("quantity2")),
                         (Integer) actualTable.getValue(i, "Quantity2"));
             }
             if (expectedMixMatch.get("discountprice2").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "DiscountPrice2"));
             } else {            
                 Assert.assertEquals("row"+i+" - discountprice2", BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("discountprice2"))),
                         (BigDecimal) actualTable.getValue(i, "DiscountPrice2"));
             }
             if (expectedMixMatch.get("empprice21").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "EmpPrice21"));
             } else {            
                 Assert.assertEquals("row"+i+" - empprice21", BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("empprice21"))),
                         (BigDecimal) actualTable.getValue(i, "EmpPrice21"));
             }
             if (expectedMixMatch.get("empprice22").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "EmpPrice22"));
             } else {            
                 Assert.assertEquals("row"+i+" - empprice22", BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("empprice22"))),
                         (BigDecimal) actualTable.getValue(i, "EmpPrice22"));
             }
             if (expectedMixMatch.get("empprice23").equalsIgnoreCase("NULL")) {
                 Assert.assertNull("row"+i+" - type should be NULL", actualTable.getValue(i, "EmpPrice23"));
             } else {            
                 Assert.assertEquals("row"+i+" - empprice23", BigDecimal.valueOf(Integer.valueOf(expectedMixMatch.get("empprice23"))),
                         (BigDecimal) actualTable.getValue(i, "EmpPrice23"));
             }
             i++;
         }
    }
    
    @Then("MST_PLU should have the following rows: $mstPlu")
    public final void thenMstPluShouldHaveTheFollwingRows(ExamplesTable mstPlu) throws DataSetException {
        ITable actualTable = dbInit.getTableSnapshot("MST_PLU");
        
        assertThat("The number of rows in MST_PLU",
                actualTable.getRowCount(),
                is(equalTo(mstPlu.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : mstPlu.getRows()) {	           
	            Assert.assertEquals("MST_PLU (row"+i+" - StoreId",
	                    item.get("StoreId"), 
	                    ((String)actualTable.getValue(i, "StoreId")).trim());
	             Assert.assertEquals("MST_PLU (row"+i+" - PLU",
	                    item.get("Plu"), 
	                    (String)actualTable.getValue(i, "Plu"));
	            Assert.assertEquals("MST_PLU (row"+i+" - MdInternal",
	                    item.get("MdInternal"), 
	                    (String)actualTable.getValue(i, "MdInternal"));
	            if (item.get("MdType").equalsIgnoreCase("NULL")) {
	            	Assert.assertNull("MDTYPE must be null", actualTable.getValue(i, "MdType"));
	            } else {
		            Assert.assertEquals("MST_PLU (row"+i+" - MdType",
		                    item.get("MdType"), 
		                    (String)actualTable.getValue(i, "MdType"));
	            }
	            if (item.get("MdVender").equalsIgnoreCase("NULL")) {
	            	Assert.assertNull("MdVender must be null", actualTable.getValue(i, "MdVender"));
	            } else {
		            Assert.assertEquals("MST_PLU (row"+i+" - MdVender",
		                    item.get("MdVender"), 
		                    (String)actualTable.getValue(i, "MdVender"));
	            }
	            if (item.get("Dpt").equalsIgnoreCase("NULL")) {
	            	Assert.assertNull("Dpt must be null", actualTable.getValue(i, "Dpt"));
	            } else {
	            	Assert.assertEquals("MST_PLU (row"+i+" - Dpt",
		                    item.get("Dpt"), 
		                    (String)actualTable.getValue(i, "Dpt"));
	            }
	            
	            if (item.get("Line").equalsIgnoreCase("NULL")) {
	            	Assert.assertNull("Line must be null", actualTable.getValue(i, "Line"));
	            } else {
		            Assert.assertEquals("MST_PLU (row"+i+" - Line",
		                    item.get("Line"), 
		                    (String)actualTable.getValue(i, "Line"));
	            }
	            Assert.assertEquals("MST_PLU (row"+i+" - Class",
	                    item.get("Class"), 
	                    (String)actualTable.getValue(i, "Class"));
	            if (item.get("Sku").equalsIgnoreCase("NULL")) {
	            	Assert.assertNull("Sku must be null", actualTable.getValue(i, "Sku"));
	            } else {
	            	Assert.assertEquals("MST_PLU (row"+i+" - Sku",
	                     item.get("Sku"), 
	                     (String)actualTable.getValue(i, "Sku"));
	            }
	            if (item.get("ItemSize").equalsIgnoreCase("null")) {
	            	Assert.assertNull("ItemSize must be null", actualTable.getValue(i, "ItemSize"));
	            } else {
		            Assert.assertEquals("MST_PLU (row"+i+" - ItemSize",
		                    item.get("ItemSize"), 
		                    (String)actualTable.getValue(i, "ItemSize"));
	            }
	            if (item.get("KeyPlu").equalsIgnoreCase("null")) {
	            	Assert.assertNull("KeyPlu must be null", actualTable.getValue(i, "KeyPlu"));
	            } else {
	            	Assert.assertEquals("MST_PLU (row"+i+" - KeyPlu",
	                        item.get("KeyPlu"), 
	                        (String)actualTable.getValue(i, "KeyPlu"));
	            }
			    if (item.get("Md1").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md1 must be null", actualTable.getValue(i, "Md01"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md1",item.get("Md1"),
			        		(String)actualTable.getValue(i, "Md01"));
	            }
			    if (item.get("Md2").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md2 must be null", actualTable.getValue(i, "Md02"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md2", item.get("Md2"),
			        		(String)actualTable.getValue(i, "Md02"));
	            }
			    if (item.get("Md3").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md3 must be null", actualTable.getValue(i, "Md03"));
		        } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - Md3", item.get("Md3"),
		        		(String)actualTable.getValue(i, "Md03"));
	            }
			    if (item.get("Md4").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md4 must be null", actualTable.getValue(i, "Md04"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md4", item.get("Md4"),
			        		(String)actualTable.getValue(i, "Md04"));
	            }
			    if (item.get("Md5").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md5 must be null", actualTable.getValue(i, "Md05"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md5", item.get("Md5"),
			        		(String)actualTable.getValue(i, "Md05"));
	            }
			    if (item.get("Md6").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md6 must be null", actualTable.getValue(i, "Md06"));
			   } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md6", item.get("Md6"),
			        		(String)actualTable.getValue(i, "Md06"));
	            }
			    if (item.get("Md7").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md7 must be null", actualTable.getValue(i, "Md07"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md7", item.get("Md7"),
			        		(String)actualTable.getValue(i, "Md07"));
	            }
			    if (item.get("Md8").equalsIgnoreCase("null")) {
			    	//If the value saved is null then the value returned is 0
			    	Assert.assertEquals("MST_PLU (row"+i+" - Md8", 0, 
		                    ((String)actualTable.getValue(i, "Md08")));
			   } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - Md8",
		        		item.get("Md8"), 
	                    (String)actualTable.getValue(i, "Md08"));
	            }
			    if (item.get("Md9").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md9 must be null", actualTable.getValue(i, "Md09"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md9", item.get("Md9"),
			        		(String)actualTable.getValue(i, "Md09"));
	            }
			    if (item.get("Md10").equalsIgnoreCase("null")) {
			        Assert.assertNull("Md10 must be null", actualTable.getValue(i, "Md10"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - Md10", item.get("Md10"),
			        		(String)actualTable.getValue(i, "Md10"));
	            }
			    if (item.get("MdName").equalsIgnoreCase("null")) {
			        Assert.assertNull("MdName must be null", actualTable.getValue(i, "MdName"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - MdName",
			                        item.get("MdName"), 
			                        (String)actualTable.getValue(i, "MdName"));
			    }        
			    if (item.get("MdName1").equalsIgnoreCase("null")) {
			        Assert.assertNull("MdName1 must be null", actualTable.getValue(i, "MdName1"));
			    } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - MdName1",
	                item.get("MdName1"), 
	                (String)actualTable.getValue(i, "MdName1"));
			    }
			    if (item.get("MdName2").equalsIgnoreCase("null")) {
			        Assert.assertNull("MdName2 must be null", actualTable.getValue(i, "MdName2"));
			   } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - MdName2",
			                        item.get("MdName2"), 
			                        (String)actualTable.getValue(i, "MdName2"));
			   }
			   if (item.get("MdKanaName").equalsIgnoreCase("null")) {
			        Assert.assertNull("MdKanaName must be null", actualTable.getValue(i, "MdKanaName"));
			   } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - MdKanaName",
	                    item.get("MdKanaName"), 
	                    (String)actualTable.getValue(i, "MdKanaName"));
			   }
			   if (item.get("MdKanaName1").equalsIgnoreCase("null")) {
			        Assert.assertNull("MdKanaName1 must be null", actualTable.getValue(i, "MdKanaName1"));
			   } else {
			        Assert.assertEquals("MST_PLU (row"+i+" - MdKanaName1",
	                    item.get("MdKanaName1"), 
	                    (String)actualTable.getValue(i, "MdKanaName1"));
	          }
			  if (item.get("MdKanaName2").equalsIgnoreCase("null")) {
			        Assert.assertNull("MdKanaName2 must be null", actualTable.getValue(i, "MdKanaName2"));
			  } else {
	        		Assert.assertEquals("MST_PLU (row"+i+" - MdKanaName2",
	                    item.get("MdKanaName2"), 
	                    (String)actualTable.getValue(i, "MdKanaName2"));
			  }
		     if (item.get("OrgSalesPrice1").equalsIgnoreCase("null")) {
		        Assert.assertNull("OrgSalesPrice1 must be null", actualTable.getValue(i, "OrgSalesPrice1"));
		     } else {
		    	 Assert.assertEquals("MST_PLU (row"+i+" - OrgSalesPrice1",
	                        BigDecimal.valueOf(Double.valueOf(item.get("OrgSalesPrice1"))).longValue(), 
	                        ((BigDecimal)actualTable.getValue(i, "OrgSalesPrice1")).longValue());
	          }
			 if (item.get("SalesPrice1").equalsIgnoreCase("null")) {
		        Assert.assertNull("SalesPrice1 must be null", actualTable.getValue(i, "SalesPrice1"));
		     } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - SalesPrice1",
	        		BigDecimal.valueOf(Double.valueOf(item.get("SalesPrice1"))).longValue(), 
	                ((BigDecimal)actualTable.getValue(i, "SalesPrice1")).longValue());
	         }
	        if (item.get("SalesPrice2").equalsIgnoreCase("null")) {
		        Assert.assertNull("SalesPrice2 must be null", actualTable.getValue(i, "SalesPrice2"));
		    } else {
		    	Assert.assertEquals("MST_PLU (row"+i+" - SalesPrice2",
	                BigDecimal.valueOf(Double.valueOf(item.get("SalesPrice2"))).longValue(), 
	                ((BigDecimal)actualTable.getValue(i, "SalesPrice2")).longValue());
	        }
		    if (item.get("EmpPrice1").equalsIgnoreCase("null")) {
		        Assert.assertNull("EmpPrice1 must be null", actualTable.getValue(i, "EmpPrice1"));
		    } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - EmpPrice1",
	        		BigDecimal.valueOf(Double.valueOf(item.get("EmpPrice1"))).longValue(), 
	                        ((BigDecimal)actualTable.getValue(i, "EmpPrice1")).longValue());
	        }
		    if (item.get("EmpPrice2").equalsIgnoreCase("null")) {
		        Assert.assertNull("EmpPrice2 must be null", actualTable.getValue(i, "EmpPrice2"));
		   } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - EmpPrice2",
		        		BigDecimal.valueOf(Double.valueOf(item.get("EmpPrice2"))).longValue(), 
	                        ((BigDecimal)actualTable.getValue(i, "EmpPrice2")).longValue());
	            }
		    if (item.get("EmpPrice3").equalsIgnoreCase("null")) {
		        Assert.assertNull("EmpPrice3 must be null", actualTable.getValue(i, "EmpPrice3"));
		    } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - EmpPrice3",
	        		BigDecimal.valueOf(Double.valueOf(item.get("EmpPrice3"))).longValue(), 
	                        ((BigDecimal)actualTable.getValue(i, "EmpPrice3")).longValue());
	        }
		    
		    if (item.get("PuPrice1").equalsIgnoreCase("null")) {
		        Assert.assertNull("PuPrice1 must be null", actualTable.getValue(i, "PuPrice1"));
		    } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - PuPrice1",
	                        BigDecimal.valueOf(Double.valueOf(item.get("PuPrice1"))).longValue(), 
	                        ((BigDecimal)actualTable.getValue(i, "PuPrice1")).longValue());
	        }
		    
		    if (item.get("PuPrice2").equalsIgnoreCase("null")) {
		        Assert.assertNull("PuPrice2 must be null", actualTable.getValue(i, "PuPrice2"));
		    } else {
		    	Assert.assertEquals("MST_PLU (row"+i+" - PuPrice2",
        		BigDecimal.valueOf(Double.valueOf(item.get("PuPrice2"))).longValue(), 
                    ((BigDecimal)actualTable.getValue(i, "PuPrice2")).longValue());
            }
		    if (item.get("PuPriceChgDate1").equalsIgnoreCase("null")) {
		        Assert.assertNull("PuPriceChgDate1 must be null", actualTable.getValue(i, "PuPriceChgDate1"));
	        } else {
	            	Assert.assertEquals("MST_PLU (row"+i+" - PuPriceChgDate1",
        		Timestamp.valueOf((item.get("PuPriceChgDate1"))), 
                (Timestamp)(actualTable.getValue(i, "PuPriceChgDate1"))); 
            }
		    if (item.get("PuPriceChgDate2").equalsIgnoreCase("null")) {
		        Assert.assertNull("PuPriceChgDate2 must be null", actualTable.getValue(i, "PuPriceChgDate2"));
		    } else {
		        Assert.assertEquals("MST_PLU (row"+i+" - PuPriceChgDate2",
	        		Timestamp.valueOf((item.get("PuPriceChgDate2"))), 
                    (Timestamp)(actualTable.getValue(i, "PuPriceChgDate2"))); 
	        }
	    
		    if (item.get("OrgCostPrice1").equalsIgnoreCase("null")) {
		        Assert.assertNull("OrgCostPrice1 must be null", actualTable.getValue(i, "OrgCostPrice1"));
		    } else {
		    	Assert.assertEquals("MST_PLU (row"+i+" - OrgCostPrice1",
	        		BigDecimal.valueOf(Double.valueOf(item.get("OrgCostPrice1"))).longValue(), 
	                    ((BigDecimal)actualTable.getValue(i, "OrgCostPrice1")).longValue());
	       }
		   if (item.get("CostPrice1").equalsIgnoreCase("null")) {
		        Assert.assertNull("CostPrice1 must be null", actualTable.getValue(i, "CostPrice1"));
		   } else {
	            	Assert.assertEquals("MST_PLU (row"+i+" - CostPrice1",
	            			BigDecimal.valueOf(Double.valueOf(item.get("CostPrice1"))).longValue(), 
	                    ((BigDecimal)actualTable.getValue(i, "CostPrice1")).longValue());
	       }
	   
		    if (item.get("CostPrice2").equalsIgnoreCase("null")) {
		        Assert.assertNull("CostPrice2 must be null", actualTable.getValue(i, "CostPrice2"));
		    } else {
		            Assert.assertEquals("MST_PLU (row"+i+" - CostPrice2",
	        		BigDecimal.valueOf(Double.valueOf(item.get("CostPrice2"))).longValue(), 
	                        ((BigDecimal)actualTable.getValue(i, "CostPrice2")).longValue());
	         }
	   
			if (item.get("CostPriceChgDate1").equalsIgnoreCase("null")) {
			        Assert.assertNull("CostPriceChgDate1 must be null", actualTable.getValue(i, "CostPriceChgDate1"));
	        } else {
	        Assert.assertEquals("MST_PLU (row"+i+" - CostPriceChgDate1",
	        		Timestamp.valueOf((item.get("CostPriceChgDate1"))), 
                    (Timestamp)(actualTable.getValue(i, "CostPriceChgDate1"))); 
	        }
			
	    	if (item.get("CostPriceChgDate2").equalsIgnoreCase("null")) {
	    		Assert.assertNull("CostPriceChgDate2 must be null", actualTable.getValue(i, "CostPriceChgDate2"));
	        } else {
	            	Assert.assertEquals("MST_PLU (row"+i+" - CostPriceChgDate2",
	        		Timestamp.valueOf((item.get("CostPriceChgDate2"))), 
                    (Timestamp)(actualTable.getValue(i, "CostPriceChgDate2"))); 
	        }
	    
	   		if (item.get("SalesDate").equalsIgnoreCase("null")) {
	   			Assert.assertNull("SalesDate must be null", actualTable.getValue(i, "SalesDate"));
	        } else {
	        	Assert.assertEquals("MST_PLU (row"+i+" - SalesDate",
	        		Timestamp.valueOf((item.get("SalesDate"))), 
                    (Timestamp)(actualTable.getValue(i, "SalesDate"))); 
	        }
	   		
		    if (item.get("MakerPrice").equalsIgnoreCase("null")) {
		    	Assert.assertNull("MakerPrice must be null", actualTable.getValue(i, "MakerPrice"));
		    } else {
		        	Assert.assertEquals("MST_PLU (row"+i+" - MakerPrice",
            			BigDecimal.valueOf(Double.valueOf(item.get("MakerPrice"))).longValue(), 
                    ((BigDecimal)actualTable.getValue(i, "MakerPrice")).longValue());
           
		        	
            }
	    	if (item.get("TaxType").equalsIgnoreCase("null")) {
	        	Assert.assertNull("TaxType must be null", actualTable.getValue(i, "TaxType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - TaxType",
            			BigDecimal.valueOf(Double.valueOf(item.get("TaxType"))).longValue(), 
                    ((BigDecimal)actualTable.getValue(i, "TaxType")).longValue());
            }
    	
            if (item.get("DiscountType").equalsIgnoreCase("null")) {
            	Assert.assertNull("DiscountType must be null", actualTable.getValue(i, "DiscountType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - DiscountType",
            			BigDecimal.valueOf(Double.valueOf(item.get("DiscountType"))).longValue(), 
                    ((BigDecimal)actualTable.getValue(i, "DiscountType")).longValue());
            }
            
            if (item.get("SeasonType").equalsIgnoreCase("null")) {
            	Assert.assertNull("SeasonType must be null", actualTable.getValue(i, "SeasonType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SeasonType",
            			BigDecimal.valueOf(Double.valueOf(item.get("SeasonType"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "SeasonType")).longValue());
            }
            
            if (item.get("PaymentType").equalsIgnoreCase("null")) {
            	Assert.assertNull("PaymentType must be null", actualTable.getValue(i, "PaymentType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - PaymentType",
            			BigDecimal.valueOf(Double.valueOf(item.get("PaymentType"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "PaymentType")).longValue());
            }
            
            if (item.get("OrderType").equalsIgnoreCase("null")) {
            	Assert.assertNull("OrderType must be null", actualTable.getValue(i, "OrderType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - OrderType",
            			BigDecimal.valueOf(Double.valueOf(item.get("OrderType"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "OrderType")).longValue());
            }
            
            if (item.get("PosMdType").equalsIgnoreCase("null")) {
            	Assert.assertNull("PosMdType must be null", actualTable.getValue(i, "PosMdType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - PosMdType",
            			BigDecimal.valueOf(Double.valueOf(item.get("PosMdType"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "PosMdType")).longValue());
            }
            
            if (item.get("CatType").equalsIgnoreCase("null")) {
            	Assert.assertNull("CatType must be null", actualTable.getValue(i, "CatType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - CatType",
            			BigDecimal.valueOf(Double.valueOf(item.get("CatType"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "CatType")).longValue());
            }
            
            if (item.get("OrderType").equalsIgnoreCase("null")) {
            	Assert.assertNull("OrderType must be null", actualTable.getValue(i, "OrderType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - OrderType",
            			BigDecimal.valueOf(Double.valueOf(item.get("OrderType"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "OrderType")).longValue());
            }
            
            if (item.get("OrderUnit").equalsIgnoreCase("NULL")) {
            	Assert.assertNull("OrderUnit must be null", actualTable.getValue(i, "OrderUnit"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - OrderUnit",
            			BigDecimal.valueOf(Double.valueOf(item.get("OrderUnit"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "OrderUnit")).longValue());
            }
            
            if (item.get("BaseStockCnt").equalsIgnoreCase("null")) {
            	Assert.assertNull("BaseStockCnt must be null", actualTable.getValue(i, "BaseStockCnt"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - BaseStockCnt",
        			BigDecimal.valueOf(Double.valueOf(item.get("BaseStockCnt"))).longValue(), 
        			((BigDecimal)actualTable.getValue(i, "BaseStockCnt")).longValue());
             }
            
            if (item.get("Conn1").equalsIgnoreCase("null")) {
            	Assert.assertNull("Conn1 must be null", actualTable.getValue(i, "Conn1"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - Conn1",
            			item.get("Conn1"), 
            			(String)actualTable.getValue(i, "Conn1"));
            }
            
            if (item.get("Conn2").equalsIgnoreCase("null")) {
            	Assert.assertNull("Conn2 must be null", actualTable.getValue(i, "Conn2"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - Conn2",
            			item.get("Conn2"), 
            			(String)actualTable.getValue(i, "Conn2"));
             }
            
            if (item.get("ConnType1").equalsIgnoreCase("null")) {
            	Assert.assertNull("ConnType1 must be null", actualTable.getValue(i, "ConnType1"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - ConnType1",
            			BigDecimal.valueOf(Integer.valueOf(item.get("ConnType1"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "ConnType1")).intValue());
            }
            
            if (item.get("ConnType2").equalsIgnoreCase("null")) {
            	Assert.assertNull("ConnType2 must be null", actualTable.getValue(i, "ConnType2"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - ConnType2",
            			BigDecimal.valueOf(Double.valueOf(item.get("ConnType2"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "ConnType2")).longValue());
            }
            
            if (item.get("VenderCode").equalsIgnoreCase("null")) {
            	Assert.assertNull("VenderCode must be null", actualTable.getValue(i, "VenderCode"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - VenderCode",
            			item.get("VenderCode"), 
            			(String)actualTable.getValue(i, "VenderCode"));
            }
            
            if (item.get("VenderType").equalsIgnoreCase("null")) {
            		Assert.assertNull("VenderType must be null", actualTable.getValue(i, "VenderType"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - VenderType",
            			BigDecimal.valueOf(Double.valueOf(item.get("VenderType"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "VenderType")).longValue());
            }
            
            if (item.get("TagCode1").equalsIgnoreCase("null")) {
            	Assert.assertNull("TagCode1 must be null", actualTable.getValue(i, "TagCode1"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - TagCode1",
					item.get("TagCode1"), 
        			(String)actualTable.getValue(i, "TagCode1"));
            }
            
            if (item.get("TagCode2").equalsIgnoreCase("null")) {
            	Assert.assertNull("TagCode2 must be null", actualTable.getValue(i, "TagCode2"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - TagCode2",
        			BigDecimal.valueOf(Double.valueOf(item.get("TagCode2"))).longValue(), 
        			((BigDecimal)actualTable.getValue(i, "TagCode2")).longValue());
            }
            
            if (item.get("TagCode3").equalsIgnoreCase("null")) {
            	Assert.assertNull("TagCode3 must be null", actualTable.getValue(i, "TagCode3"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - TagCode3",
        			BigDecimal.valueOf(Double.valueOf(item.get("TagCode3"))).longValue(), 
        			((BigDecimal)actualTable.getValue(i, "TagCode3")).longValue());
            }
            
            if (item.get("PointRate").equalsIgnoreCase("null")) {
            	Assert.assertNull("PointRate must be null", actualTable.getValue(i, "PointRate"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - PointRate",
            			BigDecimal.valueOf(Double.valueOf(item.get("PointRate"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "PointRate")).longValue());
            } 
            
            if (item.get("SubMoney1").equalsIgnoreCase("null")) {
            	Assert.assertNull("SUMONEY1 must be null", actualTable.getValue(i, "SubMoney1"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubMoney1",
            			BigDecimal.valueOf(Double.valueOf(item.get("SubMoney1"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubMoney1")).longValue());
            }
            
            if (item.get("SubMoney2").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubMoney2 must be null", actualTable.getValue(i, "SubMoney2"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubMoney2",
            			BigDecimal.valueOf(Double.valueOf(item.get("SubMoney2"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubMoney2")).longValue());
            }
            
            if (item.get("SubMoney3").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubMoney3 must be null", actualTable.getValue(i, "SubMoney3"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubMoney3",
            			BigDecimal.valueOf(Double.valueOf(item.get("SubMoney3"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubMoney3")).longValue());
            }
            
            if (item.get("SubMoney4").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubMoney4 must be null", actualTable.getValue(i, "SubMoney4"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubMoney4",
            			BigDecimal.valueOf(Double.valueOf(item.get("SubMoney4"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubMoney4")).longValue());
            }
            
            if (item.get("SubMoney5").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubMoney5 must be null", actualTable.getValue(i, "SubMoney5"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubMoney5",
            			BigDecimal.valueOf(Double.valueOf(item.get("SubMoney5"))).longValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubMoney5")).longValue());
            }
            
            if (item.get("SubCode1").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode1 must be null", actualTable.getValue(i, "SubCode1"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode1",
            			item.get("SubCode1"), 
                    (String)actualTable.getValue(i, "SubCode1"));
            }
            
            if (item.get("SubCode2").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode2 must be null", actualTable.getValue(i, "SubCode2"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode2",
                    item.get("SubCode2"), 
                    (String)actualTable.getValue(i, "SubCode2"));
            }
            
            if (item.get("SubCode3").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode3 must be null", actualTable.getValue(i, "SubCode3"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode3",
                    item.get("SubCode3"), 
                    (String)actualTable.getValue(i, "SubCode3"));
            }
            
            if (item.get("SubCode4").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode4 must be null", actualTable.getValue(i, "SubCode4"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode4",
                    item.get("SubCode4"), 
                    (String)actualTable.getValue(i, "SubCode4"));
            }
            
            if (item.get("SubCode5").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode5 must be null", actualTable.getValue(i, "SubCode5"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode5",
                    item.get("SubCode5"), 
                    (String)actualTable.getValue(i, "SubCode5"));
            }
            
            if (item.get("SubCode6").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode6 must be null", actualTable.getValue(i, "SubCode6"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode6",
                    item.get("SubCode6"), 
                    (String)actualTable.getValue(i, "SubCode6"));
           }
            
            if (item.get("SubCode7").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode7 must be null", actualTable.getValue(i, "SubCode7"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode7",
                    item.get("SubCode7"), 
                    (String)actualTable.getValue(i, "SubCode7"));
            }
            
            if (item.get("SubCode8").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode8 must be null", actualTable.getValue(i, "SubCode8"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode8",
                    item.get("SubCode8"), 
                    (String)actualTable.getValue(i, "SubCode8"));
            }
            
            if (item.get("SubCode9").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode9 must be null", actualTable.getValue(i, "SubCode9"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode9",
                    item.get("SubCode9"), 
                    (String)actualTable.getValue(i, "SubCode9"));
            }
            
            if (item.get("SubCode10").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubCode10 must be null", actualTable.getValue(i, "SubCode10"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubCode10",
                    item.get("SubCode10"), 
                    (String)actualTable.getValue(i, "SubCode10"));
            }

            if (item.get("SubInt1").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt1 must be null", actualTable.getValue(i, "SubInt1"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt1",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt1"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt1")).intValue());
            }
            if (item.get("SubInt2").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt2 must be null", actualTable.getValue(i, "SubInt2"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt2",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt2"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt2")).intValue());
            }
            if (item.get("SubInt3").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt3 must be null", actualTable.getValue(i, "SubInt3"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt3",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt3"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt3")).intValue());
            }
            if (item.get("SubInt4").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt4 must be null", actualTable.getValue(i, "SubInt4"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt4",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt4"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt4")).intValue());
            }
            if (item.get("SubInt5").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt5 must be null", actualTable.getValue(i, "SubInt5"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt5",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt5"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt5")).intValue());
            }
            if (item.get("SubInt6").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt6 must be null", actualTable.getValue(i, "SubInt6"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt6",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt6"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt6")).intValue());
            }
            if (item.get("SubInt7").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt7 must be null", actualTable.getValue(i, "SubInt7"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt7",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt7"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt7")).intValue());
            }
            if (item.get("SubInt8").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt8 must be null", actualTable.getValue(i, "SubInt8"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt8",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt8"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt8")).intValue());
            }
            if (item.get("SubInt9").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt9 must be null", actualTable.getValue(i, "SubInt9"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt9",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt9"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt9")).intValue());
            }
            if (item.get("SubInt10").equalsIgnoreCase("null")) {
            	Assert.assertNull("SubInt10 must be null", actualTable.getValue(i, "SubInt10"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - SubInt10",
            			BigDecimal.valueOf(Integer.valueOf(item.get("SubInt10"))).intValue(), 
            			((BigDecimal)actualTable.getValue(i, "SubInt10")).intValue());
            }
            if (item.get("InsDate").equalsIgnoreCase("null")) {
            	 Assert.assertNull("InsDate must be null", actualTable.getValue(i, "InsDate"));
            } else { 
            	Assert.assertEquals("MST_PLU (row"+i+" - InsDate",
                    Timestamp.valueOf((item.get("InsDate"))), 
                    (Timestamp)(actualTable.getValue(i, "InsDate"))); 
            }
            if (item.get("UpdDate").equalsIgnoreCase("null")) {
            	 Assert.assertNull("UpdDate must be null", actualTable.getValue(i, "UpdDate"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - UpdDate",
            			Timestamp.valueOf((item.get("UpdDate"))), 
	                    (Timestamp)(actualTable.getValue(i, "UpdDate"))); 
            }
	        if (item.get("UpdAppId").equalsIgnoreCase("null")) {
            	 Assert.assertNull("UpdAppId must be null", actualTable.getValue(i, "UpdAppId"));
            } else { 
            	Assert.assertEquals("MST_PLU (row"+i+" - UpdAppId",
                    item.get("UpdAppId"), 
                    (String)actualTable.getValue(i, "UpdAppId"));  }
           if (item.get("UpdOpeCode").equalsIgnoreCase("null")) {
            	 Assert.assertNull("UpdOpeCode must be null", actualTable.getValue(i, "UpdOpeCode"));
            } else {
            	Assert.assertEquals("MST_PLU (row"+i+" - UpdOpeCode",
                        item.get("UpdOpeCode"), 
                        (String)actualTable.getValue(i, "UpdOpeCode")); 
            } 
                
             i++;
            }
      }
    
    @Then("the resultbase should be {$resultbase}")
    public final void thenTheResultbaseShouldBe(int expectedresultbase) {
        Assert.assertEquals("Compare the ResultBase", expectedresultbase, this.actualResultBase.getNCRWSSResultCode());
    }
    
}
