package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;

import ncr.res.mobilepos.helper.CSVSerializer;
import ncr.res.mobilepos.mastermaintenance.model.MdMMMastTbl;
import ncr.res.mobilepos.mastermaintenance.model.MdMastTbl;
import ncr.res.mobilepos.mastermaintenance.model.OpeMastTbl;

import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class CSVSerializerTestSteps extends Steps {
    private String givenCSV = null;
    private  OpeMastTbl actualOpeMastTbl = null;
    private MdMastTbl actualMdMastTbl = null;
    private MdMMMastTbl actualMdMMMastTbl = null;
    private CSVSerializer<OpeMastTbl> opeMastTblCSVSer = null;
    private CSVSerializer<MdMastTbl> mdMastTblCSVSer = null;
    private CSVSerializer<MdMMMastTbl> mdMMMastTblCSVSer = null;
    
    @BeforeScenario
    public final void setUp() {
        this.givenCSV = null;
        this.actualOpeMastTbl = null;
        this.actualMdMastTbl = null;
        this.actualMdMMMastTbl = null;
        this.opeMastTblCSVSer = new CSVSerializer<OpeMastTbl>();
        this.mdMastTblCSVSer = new CSVSerializer<MdMastTbl>();
        this.mdMMMastTblCSVSer = new CSVSerializer<MdMMMastTbl>();
    }
    
    @Given("the following CSV string $csv")
    public final void givenTheFollowingCSVString(final String csv) {
        this.givenCSV = csv;
    }
    
    @SuppressWarnings("unchecked")
	@When("the CSV String is converted to OpeMastTbl") 
    public final void whenTheCSVStringIsConvertedToOpeMastTbl()
    {
        try {
            this.actualOpeMastTbl = this.opeMastTblCSVSer.deserializeCsv(this.givenCSV, OpeMastTbl.class);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed To convert CSV string to Bean for OpeMastTbl");
        } 
    }
    
    @Then("the OpeMastTbl bean should have the following values $opeMastTbl")
    public final void thenTheOpeMastTblBeanShouldHaveTheFollowingValues(ExamplesTable expectedOpeMastTbls) {
        OpeMastTbl actualOpeMastTbl = this.actualOpeMastTbl;
        Map<String, String> expectedOpeMastTbl = expectedOpeMastTbls.getRow(0);
        	assertThat("Compare the expected EmpCode for OpeMastTbl",
        			expectedOpeMastTbl.get("EmpCode").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("EmpCode"),
        					is(equalTo(actualOpeMastTbl.getEmpCode())));
        	assertThat("Compare the expected OpeCode for OpeMastTbl",
        			expectedOpeMastTbl.get("OpeCode").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("OpeCode"),
        					is(equalTo(actualOpeMastTbl.getOpeCode())));
        	assertThat("Compare the expected OpeType for OpeMastTbl",
        			expectedOpeMastTbl.get("OpeType").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("OpeType"),
        					is(equalTo(actualOpeMastTbl.getOpeType())));
        	assertThat("Compare the expected Password for OpeMastTbl",
        			expectedOpeMastTbl.get("Password").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("Password"),
        					is(equalTo(actualOpeMastTbl.getPassword())));
        	assertThat("Compare the expected OpeName for OpeMastTbl",
        			expectedOpeMastTbl.get("OpeName").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("OpeName"),
        					is(equalTo(actualOpeMastTbl.getOpeName())));
        	assertThat("Compare the expected OpeKanaName for OpeMastTbl",
        			expectedOpeMastTbl.get("OpeKanaName").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("OpeKanaName"),
        					is(equalTo(actualOpeMastTbl.getOpeKanaName())));
        	assertThat("Compare the expected ZipCode for OpeMastTbl",
        			expectedOpeMastTbl.get("ZipCode").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("ZipCode"),
        					is(equalTo(actualOpeMastTbl.getZipCode())));
        	assertThat("Compare the expected Address for OpeMastTbl",
        			expectedOpeMastTbl.get("Address").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("Address"),
        					is(equalTo(actualOpeMastTbl.getAddress())));
        	assertThat("Compare the expected TelNo for OpeMastTbl",
        			expectedOpeMastTbl.get("TelNo").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("TelNo"),
        					is(equalTo(actualOpeMastTbl.getTelNo())));
        	assertThat("Compare the expected FaxNo for OpeMastTbl",
        			expectedOpeMastTbl.get("FaxNo").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("FaxNo"),
        					is(equalTo(actualOpeMastTbl.getFaxNo())));
        	assertThat("Compare the expected SecLevel1 for OpeMastTbl",
        			expectedOpeMastTbl.get("SecLevel1").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("SecLevel1"),
        					is(equalTo(String.valueOf(actualOpeMastTbl.getSecLevel1()))));
        	assertThat("Compare the expected SecLevel2 for OpeMastTbl",
        			expectedOpeMastTbl.get("SecLevel2").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("SecLevel2"),
        					is(equalTo(String.valueOf(actualOpeMastTbl.getSecLevel2()))));
        	assertThat("Compare the expected SubChar1 for OpeMastTbl",
        			expectedOpeMastTbl.get("SubChar1").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("SubChar1"),
        					is(equalTo(actualOpeMastTbl.getSubChar1())));
        	assertThat("Compare the expected SubChar2 for OpeMastTbl",
        			expectedOpeMastTbl.get("SubChar2").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("SubChar2"),
        					is(equalTo(actualOpeMastTbl.getSubChar2())));
        	assertThat("Compare the expected SubChar3 for OpeMastTbl",
        			expectedOpeMastTbl.get("SubChar3").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("SubChar3"),
        					is(equalTo(actualOpeMastTbl.getSubChar3())));
        	assertThat("Compare the expected InsDate for OpeMastTbl",
        			expectedOpeMastTbl.get("InsDate").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("InsDate"),
        					is(equalTo(actualOpeMastTbl.getInsDate())));
        	assertThat("Compare the expected UpdDate for OpeMastTbl",
        			expectedOpeMastTbl.get("UpdDate").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("UpdDate"),
        					is(equalTo(actualOpeMastTbl.getUpdDate())));
        	assertThat("Compare the expected UpdAppId for OpeMastTbl",
        			expectedOpeMastTbl.get("UpdAppId").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("UpdAppId"),
        					is(equalTo(actualOpeMastTbl.getUpdAppId())));
        	assertThat("Compare the expected UpdOpeCode for OpeMastTbl",
        			expectedOpeMastTbl.get("UpdOpeCode").equalsIgnoreCase("NULL") ? null : expectedOpeMastTbl.get("UpdOpeCode"),
        					is(equalTo(actualOpeMastTbl.getUpdOpeCode())));
        }
    
      @When("the CSV string is converted to MdMastTbl")
      public final void whenTheCsvIsConvertedToMdMasTbl() {
    	  try {
			this.actualMdMastTbl = this.mdMastTblCSVSer.deserializeCsv(this.givenCSV, MdMastTbl.class);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Failed to convert CSV string to Bean for MdMastTbl");
		}  
      }
      
      @Then("the MdMastTbl bean should have the following values $mdMastTbl")
      public final void thenTheMdMastTblBeanShouldHaveTheFollowing(ExamplesTable expectedMdMastTbls) {
    	  MdMastTbl actualMdMastTbl = this.actualMdMastTbl;
    	  Map<String, String> expectedMdMastTbl = expectedMdMastTbls.getRow(0);
    	  		// storeid
    	  	assertThat("Compare the expected storeId for MdMastTbl",
				expectedMdMastTbl.get("storeId").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("storeId"),
    					is(equalTo(actualMdMastTbl.getStoreid())));
//    	  		private String plu;
    	  	assertThat("Compare the expected plu for MdMastTbl",
    				expectedMdMastTbl.get("plu").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("plu"),
        					is(equalTo(actualMdMastTbl.getPlu())));
//				private String mdType;
    	  	assertThat("Compare the expected mdType for MdMastTbl",
    				expectedMdMastTbl.get("mdType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdType"),
        					is(equalTo(actualMdMastTbl.getMdType())));
//				private String mdInternal;
    	  	assertThat("Compare the expected mdInternal for MdMastTbl",
    				expectedMdMastTbl.get("mdInternal").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdInternal"),
        					is(equalTo(actualMdMastTbl.getMdInternal())));
//				private String mdVender;
    	  	assertThat("Compare the expected mdVender for MdMastTbl",
    				expectedMdMastTbl.get("mdVender").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdVender"),
        					is(equalTo(actualMdMastTbl.getMdVender())));
//				private String division;
    	  	assertThat("Compare the expected division for MdMastTbl",
    				expectedMdMastTbl.get("division").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("division"),
        					is(equalTo(actualMdMastTbl.getDivision())));
//				private String category;
    	  	assertThat("Compare the expected category for MdMastTbl",
    				expectedMdMastTbl.get("category").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("category"),
        					is(equalTo(actualMdMastTbl.getCategory())));
//				private String brand;
    	  	assertThat("Compare the expected brand for MdMastTbl",
    				expectedMdMastTbl.get("brand").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("brand"),
        					is(equalTo(actualMdMastTbl.getBrand())));
//				private String sku;
    	  	assertThat("Compare the expected sku for MdMastTbl",
    				expectedMdMastTbl.get("sku").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("sku"),
        					is(equalTo(actualMdMastTbl.getSku())));
//				private String size;
    	  	assertThat("Compare the expected size for MdMastTbl",
    				expectedMdMastTbl.get("size").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("size"),
        					is(equalTo(actualMdMastTbl.getSize())));
//				private String keyPlu;
    	  	assertThat("Compare the expected keyPlu for MdMastTbl",
    				expectedMdMastTbl.get("keyPlu").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("keyPlu"),
        					is(equalTo(actualMdMastTbl.getKeyPlu())));
//				private String md1;
    	  	assertThat("Compare the expected md1 for MdMastTbl",
    				expectedMdMastTbl.get("md1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md1"),
        					is(equalTo(actualMdMastTbl.getMd1())));
//				private String Md2;
    	  	assertThat("Compare the expected Md2 for MdMastTbl",
    				expectedMdMastTbl.get("Md2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("Md2"),
    						is(equalTo(actualMdMastTbl.getMd2())));
//				private String md3;
    	  	assertThat("Compare the expected md3 for MdMastTbl",
    				expectedMdMastTbl.get("md3").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md3"),
    						is(equalTo(actualMdMastTbl.getMd3())));
//				private String md4;
    	  	assertThat("Compare the expected md4 for MdMastTbl",
    				expectedMdMastTbl.get("md4").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md4"),
        					is(equalTo(actualMdMastTbl.getMd4())));
//				private String md5;
    	  	assertThat("Compare the expected md5 for MdMastTbl",
    				expectedMdMastTbl.get("md5").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md5"),
        					is(equalTo(actualMdMastTbl.getMd5())));
//				private String md6;
    	  	assertThat("Compare the expected md6 for MdMastTbl",
    				expectedMdMastTbl.get("md6").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md6"),
        					is(equalTo(actualMdMastTbl.getMd6())));
//				private String md7;
    	  	assertThat("Compare the expected md7 for MdMastTbl",
    				expectedMdMastTbl.get("md7").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md7"),
        					is(equalTo(actualMdMastTbl.getMd7())));
//				private String md8;
    	  	assertThat("Compare the expected md8 for MdMastTbl",
    				expectedMdMastTbl.get("md8").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md8"),
        					is(equalTo(actualMdMastTbl.getMd8())));
//				private String md9;
    	  	assertThat("Compare the expected md9 for MdMastTbl",
    				expectedMdMastTbl.get("md9").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md9"),
        					is(equalTo(actualMdMastTbl.getMd9())));
//				private String md10;
    	  	assertThat("Compare the expected md10 for MdMastTbl",
    				expectedMdMastTbl.get("md10").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("md10"),
        					is(equalTo(actualMdMastTbl.getMd10())));
//				private String mdName;
    	  	assertThat("Compare the expected mdName for MdMastTbl",
    				expectedMdMastTbl.get("mdName").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdName"),
        					is(equalTo((actualMdMastTbl.getMdName()).trim())));
//				private String mdName1;
    	  	assertThat("Compare the expected mdName1 for MdMastTbl",
    				expectedMdMastTbl.get("mdName1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdName1"),
    						is(equalTo((actualMdMastTbl.getMdName1()).trim())));
//				private String mdName2;
    	  	assertThat("Compare the expected mdName2 for MdMastTbl",
    				expectedMdMastTbl.get("mdName2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdName2"),
    						is(equalTo(actualMdMastTbl.getMdName2())));
//				private String mdKanaName;
    	  	assertThat("Compare the expected mdKanaName for MdMastTbl",
    				expectedMdMastTbl.get("mdKanaName").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdKanaName"),
        					is(equalTo(actualMdMastTbl.getMdKanaName())));
//				private String mdKanaName1;
    	  	assertThat("Compare the expected mdKanaName1 for MdMastTbl",
    				expectedMdMastTbl.get("mdKanaName1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdKanaName1"),
        					is(equalTo(actualMdMastTbl.getMdKanaName1())));
//				private String mdKanaName2;
    	  	assertThat("Compare the expected mdKanaName2 for MdMastTbl",
    				expectedMdMastTbl.get("mdKanaName2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdKanaName2"),
        					is(equalTo(actualMdMastTbl.getMdKanaName2())));
//				private String mdShortName;
    	  	assertThat("Compare the expected mdShortName for MdMastTbl",
    				expectedMdMastTbl.get("mdShortName").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("mdShortName"),
        					is(equalTo(actualMdMastTbl.getMdShortName())));
//				private String orgSalesPrice1;
    	  	assertThat("Compare the expected orgSalesPrice1 for MdMastTbl",
    				expectedMdMastTbl.get("orgSalesPrice1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("orgSalesPrice1"),
        					is(equalTo(actualMdMastTbl.getOrgSalesPrice1())));
//				private String salesPrice2;
    	  	assertThat("Compare the expected salesPrice2 for MdMastTbl",
    				expectedMdMastTbl.get("salesPrice2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("salesPrice2"),
        					is(equalTo(actualMdMastTbl.getSalesPrice2())));
//				private String salesPriceChgDate1;
    	  	assertThat("Compare the expected salesPriceChgDate1 for MdMastTbl",
    				expectedMdMastTbl.get("salesPriceChgDate1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("salesPriceChgDate1"),
        					is(equalTo(actualMdMastTbl.getSalesPriceChgDate1())));
//				private String salesPriceChgDate2;
    	  	assertThat("Compare the expected salesPriceChgDate2 for MdMastTbl",
    				expectedMdMastTbl.get("salesPriceChgDate2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("salesPriceChgDate2"),
        					is(equalTo(actualMdMastTbl.getSalesPriceChgDate2())));
//				private String empPrice1;
    	  	assertThat("Compare the expected empPrice1 for MdMastTbl",
    				expectedMdMastTbl.get("empPrice1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("empPrice1"),
        					is(equalTo(actualMdMastTbl.getEmpPrice1())));
//				private String empPrice2;
    	  	assertThat("Compare the expected empPrice2 for MdMastTbl",
    				expectedMdMastTbl.get("empPrice2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("empPrice2"),
        					is(equalTo(actualMdMastTbl.getEmpPrice2())));
//				private String empPrice3;
    	  	assertThat("Compare the expected empPrice3 for MdMastTbl",
    				expectedMdMastTbl.get("empPrice3").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("empPrice3"),
        					is(equalTo(actualMdMastTbl.getEmpPrice3())));
//				private String puPrice1;
    	  	assertThat("Compare the expected puPrice1 for MdMastTbl",
    				expectedMdMastTbl.get("puPrice1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("puPrice1"),
        					is(equalTo(actualMdMastTbl.getPuPrice1())));
//				private String puPrice2;
    	  	assertThat("Compare the expected puPrice2 for MdMastTbl",
    				expectedMdMastTbl.get("puPrice2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("puPrice2"),
        					is(equalTo(actualMdMastTbl.getPuPrice2())));
//				private String puPriceChgDate1;
    	  	assertThat("Compare the expected puPriceChgDate1 for MdMastTbl",
    				expectedMdMastTbl.get("puPriceChgDate1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("puPriceChgDate1"),
        					is(equalTo(actualMdMastTbl.getPuPriceChgDate1())));
//				private String puPriceChgDate2;
    	  	assertThat("Compare the expected puPriceChgDate2 for MdMastTbl",
    				expectedMdMastTbl.get("puPriceChgDate2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("puPriceChgDate2"),
        					is(equalTo(actualMdMastTbl.getPuPriceChgDate2())));
//				private String orgCostPrice1;
    	  	assertThat("Compare the expected orgCostPrice1 for MdMastTbl",
    				expectedMdMastTbl.get("orgCostPrice1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("orgCostPrice1"),
        					is(equalTo(actualMdMastTbl.getOrgCostPrice1())));
//				private String costPrice1;
    	  	assertThat("Compare the expected costPrice1 for MdMastTbl",
    				expectedMdMastTbl.get("costPrice1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("costPrice1"),
        					is(equalTo(actualMdMastTbl.getCostPrice1())));
//				private String costPrice2;
    	  	assertThat("Compare the expected costPrice2 for MdMastTbl",
    				expectedMdMastTbl.get("costPrice2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("costPrice2"),
        					is(equalTo(actualMdMastTbl.getCostPrice2())));
//				private String costPriceChgDate1;
    	  	assertThat("Compare the expected costPriceChgDate1 for MdMastTbl",
    				expectedMdMastTbl.get("costPriceChgDate1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("costPriceChgDate1"),
        					is(equalTo(actualMdMastTbl.getCostPriceChgDate1())));
//				private String costPriceChgDate2;
    	  	assertThat("Compare the expected costPriceChgDate2 for MdMastTbl",
    				expectedMdMastTbl.get("costPriceChgDate2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("costPriceChgDate2"),
        					is(equalTo(actualMdMastTbl.getCostPriceChgDate2())));
//				private String salesDate;
    	  	assertThat("Compare the expected salesDate for MdMastTbl",
    				expectedMdMastTbl.get("salesDate").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("salesDate"),
        					is(equalTo(actualMdMastTbl.getSalesDate())));
//				private String makerPrice;
    	  	assertThat("Compare the expected makerPrice for MdMastTbl",
    				expectedMdMastTbl.get("makerPrice").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("makerPrice"),
        					is(equalTo(actualMdMastTbl.getMakerPrice())));
//				private String taxType;
    	  	assertThat("Compare the expected taxType for MdMastTbl",
    				expectedMdMastTbl.get("taxType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("taxType"),
        					is(equalTo(actualMdMastTbl.getTaxType())));
//				private String discountType;
    	  	assertThat("Compare the expected discountType for MdMastTbl",
    				expectedMdMastTbl.get("discountType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("discountType"),
        					is(equalTo(actualMdMastTbl.getDiscountType())));
//				private String seasonType;
    	  	assertThat("Compare the expected seasonType for MdMastTbl",
    				expectedMdMastTbl.get("seasonType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("seasonType"),
        					is(equalTo(actualMdMastTbl.getSeasonType())));
//				private String paymentType;
    	  	assertThat("Compare the expected paymentType for MdMastTbl",
    				expectedMdMastTbl.get("paymentType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("paymentType"),
        					is(equalTo(actualMdMastTbl.getPaymentType())));
//				private String orderType;
    	  	assertThat("Compare the expected orderType for MdMastTbl",
    				expectedMdMastTbl.get("orderType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("orderType"),
        					is(equalTo(actualMdMastTbl.getOrderType())));
//				private String posMdType;
    	  	assertThat("Compare the expected posMdType for MdMastTbl",
    				expectedMdMastTbl.get("posMdType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("posMdType"),
        					is(equalTo(actualMdMastTbl.getPosMdType())));
//				private String catType;
    	  	assertThat("Compare the expected catType for MdMastTbl",
    				expectedMdMastTbl.get("catType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("catType"),
        					is(equalTo(actualMdMastTbl.getCatType())));
//				private String orderUnit;
    	  	assertThat("Compare the expected orderUnit for MdMastTbl",
    				expectedMdMastTbl.get("orderUnit").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("orderUnit"),
        					is(equalTo(actualMdMastTbl.getOrderUnit())));
//				private String orderPoint;
    	  	assertThat("Compare the expected orderPoint for MdMastTbl",
    				expectedMdMastTbl.get("orderPoint").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("orderPoint"),
        					is(equalTo(actualMdMastTbl.getOrderPoint())));
//				private String baseStockCnt;
    	  	assertThat("Compare the expected baseStockCnt for MdMastTbl",
    				expectedMdMastTbl.get("baseStockCnt").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("baseStockCnt"),
        					is(equalTo(actualMdMastTbl.getBaseStockCnt())));
//				private String conn1;
    	  	assertThat("Compare the expected conn1 for MdMastTbl",
    				expectedMdMastTbl.get("conn1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("conn1"),
        					is(equalTo(actualMdMastTbl.getConn1())));
//				private String connType1;
    	  	assertThat("Compare the expected connType1 for MdMastTbl",
    				expectedMdMastTbl.get("connType1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("connType1"),
        					is(equalTo(actualMdMastTbl.getConnType1())));
//				private String conn2;
    	  	assertThat("Compare the expected conn2 for MdMastTbl",
    				expectedMdMastTbl.get("conn2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("conn2"),
        					is(equalTo(actualMdMastTbl.getConn2())));
//				private String connType2;
    	  	assertThat("Compare the expected connType2 for MdMastTbl",
    				expectedMdMastTbl.get("connType2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("connType2"),
        					is(equalTo(actualMdMastTbl.getConnType2())));
//				private String venderCode;
    	  	assertThat("Compare the expected venderCode for MdMastTbl",
    				expectedMdMastTbl.get("venderCode").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("venderCode"),
        					is(equalTo(actualMdMastTbl.getVenderCode())));
//				private String venderType;
    	  	assertThat("Compare the expected venderType for MdMastTbl",
    				expectedMdMastTbl.get("venderType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("venderType"),
        					is(equalTo(actualMdMastTbl.getVenderType())));
//				private String tagType;
    	  	assertThat("Compare the expected tagType for MdMastTbl",
    				expectedMdMastTbl.get("tagType").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("tagType"),
        					is(equalTo(actualMdMastTbl.getTagType())));
//				private String tagCode1;
    	  	assertThat("Compare the expected tagCode1 for MdMastTbl",
    				expectedMdMastTbl.get("tagCode1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("tagCode1"),
        					is(equalTo(actualMdMastTbl.getTagCode1())));
//				private String tagCode2;
    	  	assertThat("Compare the expected tagCode2 for MdMastTbl",
    				expectedMdMastTbl.get("tagCode2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("tagCode2"),
        					is(equalTo(actualMdMastTbl.getTagCode2())));
//				private String tagCode3;
    	  	assertThat("Compare the expected tagCode3 for MdMastTbl",
    				expectedMdMastTbl.get("tagCode3").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("tagCode3"),
        					is(equalTo(actualMdMastTbl.getTagCode3())));
//				private String PointRate;
    	  	assertThat("Compare the expected PointRate for MdMastTbl",
    				expectedMdMastTbl.get("pointRate").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("pointRate"),
        					is(equalTo(actualMdMastTbl.getPointRate())));
//				private String pictureFileName1;
    	  	assertThat("Compare the expected pictureFileName1 for MdMastTbl",
    				expectedMdMastTbl.get("pictureFileName1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("pictureFileName1"),
        					is(equalTo(actualMdMastTbl.getPictureFileName1())));
//				private String pictureFileName2;
    	  	assertThat("Compare the expected pictureFileName2 for MdMastTbl",
    				expectedMdMastTbl.get("pictureFileName2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("pictureFileName2"),
        					is(equalTo(actualMdMastTbl.getPictureFileName2())));
//				private String subMoney1;
    	  	assertThat("Compare the expected subMoney1 for MdMastTbl",
    				expectedMdMastTbl.get("subMoney1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subMoney1"),
        					is(equalTo(actualMdMastTbl.getSubMoney1())));
//				private String subMoney2;
    	  	assertThat("Compare the expected subMoney2 for MdMastTbl",
    				expectedMdMastTbl.get("subMoney2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subMoney2"),
        					is(equalTo(actualMdMastTbl.getSubMoney2())));
//				private String subMoney3;
    	  	assertThat("Compare the expected subMoney3 for MdMastTbl",
    				expectedMdMastTbl.get("subMoney3").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subMoney3"),
        					is(equalTo(actualMdMastTbl.getSubMoney3())));
//				private String subMoney4;
    	  	assertThat("Compare the expected subMoney4 for MdMastTbl",
    				expectedMdMastTbl.get("subMoney4").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subMoney4"),
        					is(equalTo(actualMdMastTbl.getSubMoney4())));
//				private String subMoney5;
    	  	assertThat("Compare the expected subMoney5 for MdMastTbl",
    				expectedMdMastTbl.get("subMoney5").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subMoney5"),
        					is(equalTo(actualMdMastTbl.getSubMoney5())));
//				private String subCode1;
    	  	assertThat("Compare the expected subCode1 for MdMastTbl",
    				expectedMdMastTbl.get("subCode1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode1"),
        					is(equalTo(actualMdMastTbl.getSubCode1())));
//				private String subCode2;
    	  	assertThat("Compare the expected subCode2 for MdMastTbl",
    				expectedMdMastTbl.get("subCode2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode2"),
        					is(equalTo(actualMdMastTbl.getSubCode2())));
//				private String subCode3;
    	  	assertThat("Compare the expected subCode3 for MdMastTbl",
    				expectedMdMastTbl.get("subCode3").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode3"),
        					is(equalTo(actualMdMastTbl.getSubCode3())));
//				private String subCode4;
    	  	assertThat("Compare the expected subCode4 for MdMastTbl",
    				expectedMdMastTbl.get("subCode4").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode4"),
        					is(equalTo(actualMdMastTbl.getSubCode4())));
//				private String subCode5;
    	  	assertThat("Compare the expected subCode5 for MdMastTbl",
    				expectedMdMastTbl.get("subCode5").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode5"),
        					is(equalTo(actualMdMastTbl.getSubCode5())));
//				private String subCode6;
    	  	assertThat("Compare the expected subCode6 for MdMastTbl",
    				expectedMdMastTbl.get("subCode6").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode6"),
        					is(equalTo(actualMdMastTbl.getSubCode6())));
//				private String subCode7;
    	  	assertThat("Compare the expected subCode7 for MdMastTbl",
    				expectedMdMastTbl.get("subCode7").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode7"),
        					is(equalTo(actualMdMastTbl.getSubCode7())));
//				private String subCode8;
    	  	assertThat("Compare the expected subCode8 for MdMastTbl",
    				expectedMdMastTbl.get("subCode8").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode8"),
        					is(equalTo(actualMdMastTbl.getSubCode8())));
//				private String subCode9;
    	  	assertThat("Compare the expected subCode9 for MdMastTbl",
    				expectedMdMastTbl.get("subCode9").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode9"),
        					is(equalTo(actualMdMastTbl.getSubCode9())));
//				private String subCode10;
    	  	assertThat("Compare the expected subCode10 for MdMastTbl",
    				expectedMdMastTbl.get("subCode10").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subCode10"),
        					is(equalTo(actualMdMastTbl.getSubCode10())));
//				private String subTinyInt1;
    	  	assertThat("Compare the expected subTinyInt1 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt1").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt1"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt1())));
//				private String SubTinyInt2;
    	  	assertThat("Compare the expected subTinyInt2 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt2").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt2"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt2())));
//				private String SubTinyInt3;
    	  	assertThat("Compare the expected subTinyInt3 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt3").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt3"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt3())));
//				private String SubTinyInt4;
    	  	assertThat("Compare the expected subTinyInt4 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt4").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt4"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt4())));
//				private String SubTinyInt5;
    	  	assertThat("Compare the expected subTinyInt5 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt5").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt5"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt5())));
//				private String SubTinyInt6;
    	  	assertThat("Compare the expected subTinyInt6 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt6").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt6"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt6())));
//				private String SubTinyInt7;
    	  	assertThat("Compare the expected subTinyInt7 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt7").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt7"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt7())));
//				private String SubTinyInt8;
    	  	assertThat("Compare the expected subTinyInt8 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt8").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt8"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt8())));
//				private String SubTinyInt9;
    	  	assertThat("Compare the expected subTinyInt9 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt9").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt9"),
        					is(equalTo(actualMdMastTbl.getSubTinyInt9())));
//				private String SubTinyInt10;
    	  	assertThat("Compare the expected subTinyInt10 for MdMastTbl",
    				expectedMdMastTbl.get("subTinyInt10").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("subTinyInt10"),
        					is(equalTo(actualMdMastTbl.getSubInt10())));
//				private String insDate;
    	  	assertThat("Compare the expected insDate for MdMastTbl",
    				expectedMdMastTbl.get("insDate").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("insDate"),
        					is(equalTo(actualMdMastTbl.getInsDate())));
//				private String updDate;
    	  	assertThat("Compare the expected updDate for MdMastTbl",
    				expectedMdMastTbl.get("updDate").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("updDate"),
        					is(equalTo(actualMdMastTbl.getUpdDate())));
//				private String updAppId;
    	  	assertThat("Compare the expected updAppId for MdMastTbl",
    				expectedMdMastTbl.get("updAppId").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("updAppId"),
        					is(equalTo(actualMdMastTbl.getUpdAppId())));
//				private String updOpeCode;
    	  	assertThat("Compare the expected updOpeCode for MdMastTbl",
    				expectedMdMastTbl.get("updOpeCode").equalsIgnoreCase("NULL") ? null : expectedMdMastTbl.get("updOpeCode"),
        					is(equalTo(actualMdMastTbl.getUpdOpeCode())));
      }
      
	      @When("the CSV string is converted to MdMMMastTbl")
		  	public final void whenTheCsvStringIsConvertedToMdMMMastTbl() {
		  		try {
					this.actualMdMMMastTbl = this.mdMMMastTblCSVSer.deserializeCsv(this.givenCSV, MdMMMastTbl.class);
				} catch (IOException e) {
					e.printStackTrace();
					Assert.fail("Failed to convert CSV string to Bean for MdMMMastTbl");
				}
		  	}
	      
	      @Then("the MdMMMastTbl bean should have the following values $mdMMMastTbl")
	      public final void thenTheMdMMMastTblBeanShouldHaveTheFollowing(ExamplesTable expectedMdMMMastTbls) {
	    	  MdMMMastTbl actualMdMMMastTbl = this.actualMdMMMastTbl;
	    	  Map<String, String> expectedMdMMMastTbl = expectedMdMMMastTbls.getRow(0);
	    	  System.out.println("MDMMMASTTBL: " + expectedMdMMMastTbl.toString());
	//  	  	|storeId
	    	  	assertThat("Compare the expected storeId for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("storeId").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("storeId"),
	    	  					is(equalTo(actualMdMMMastTbl.getStoreId())));    
	//    	  	|mmCode
	    	  	assertThat("Compare the expected mmCode for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("mmCode").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("mmCode"),
	    	  					is(equalTo(actualMdMMMastTbl.getMmCode())));
	//    	  	|mmStartDateId
	    	  	assertThat("Compare the expected mmStartDateId for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("mmStartDateId").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("mmStartDateId"),
	    	  					is(equalTo(actualMdMMMastTbl.getMmStartDateId())));
	//    	  	|mmEndDateId
	    	  	assertThat("Compare the expected mmEndDateId for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("mmEndDateId").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("mmEndDateId"),
	    	  					is(equalTo(actualMdMMMastTbl.getMmEndDateId())));
	//    	  	|mmType
	    	  	assertThat("Compare the expected mmType for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("mmType").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("mmType"),
	    	  					is(equalTo(actualMdMMMastTbl.getMmType())));
	//    	  	|priceMulti1
	    	  	assertThat("Compare the expected priceMulti1 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("priceMulti1").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("priceMulti1"),
	    	  					is(equalTo(actualMdMMMastTbl.getPriceMulti1())));
	//    	  	 |discountPrice1
	    	  	assertThat("Compare the expected discountPrice1 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("discountPrice1").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("discountPrice1"),
	    	  					is(equalTo(actualMdMMMastTbl.getDiscountPrice1())));
	//    	  	  |empPrice11
	    	  	assertThat("Compare the expected empPrice11 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("empPrice11").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("empPrice11"),
	    	  					is(equalTo(actualMdMMMastTbl.getEmpPrice11())));
	//    	  	  |empPrice12
	    	  	assertThat("Compare the expected empPrice12 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("empPrice12").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("empPrice12"),
	    	  					is(equalTo(actualMdMMMastTbl.getEmpPrice12())));
	//    	  	  |empPrice13
	    	  	assertThat("Compare the expected empPrice13 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("empPrice13").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("empPrice13"),
	    	  					is(equalTo(actualMdMMMastTbl.getEmpPrice13())));
	//    	  	  |priceMulti2    
	    	  	assertThat("Compare the expected priceMulti2 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("priceMulti2").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("priceMulti2"),
	    	  					is(equalTo(actualMdMMMastTbl.getPriceMulti2())));
	//    	  	  |discountPrice2
	    	  	assertThat("Compare the expected discountPrice2 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("discountPrice2").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("discountPrice2"),
	    	  					is(equalTo(actualMdMMMastTbl.getDiscountPrice2())));
	//    	  	  |empPrice21
	    	  	assertThat("Compare the expected empPrice21 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("empPrice21").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("empPrice21"),
	    	  					is(equalTo(actualMdMMMastTbl.getEmpPrice21())));
	//    	  	  |empPrice22    
	    	  	assertThat("Compare the expected empPrice22 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("empPrice22").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("empPrice22"),
	    	  					is(equalTo(actualMdMMMastTbl.getEmpPrice22())));
	//    	  	  |empPrice23
	    	  	assertThat("Compare the expected empPrice23 for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("empPrice23").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("empPrice23"),
	    	  					is(equalTo(actualMdMMMastTbl.getEmpPrice23())));
	//    	  	   |mmName 
	    	  	assertThat("Compare the expected mmName for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("mmName").equalsIgnoreCase("NULL") ? null : expectedMdMMMastTbl.get("mmName"),
	    	  					is(equalTo(actualMdMMMastTbl.getMmName())));
	//    	  	   |mustbuyflag
	    	  	assertThat("Compare the expected mustbuyflag for MdMMMastTbl",
	    	  			expectedMdMMMastTbl.get("mustbuyflag"),
	    	  					is(equalTo(actualMdMMMastTbl.getMustBuyFlag())));
	      }
}