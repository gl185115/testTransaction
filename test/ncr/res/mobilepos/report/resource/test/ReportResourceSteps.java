/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * ReportResourceSteps
 *
 * Class containing Steps for Unit Testing ReportResource Class
 *
 * jd185128
 */
package ncr.res.mobilepos.report.resource.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.consolidation.resource.ConsolidationResource;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.report.model.DrawerFinancialReport;
import ncr.res.mobilepos.report.model.FinancialReport;
import ncr.res.mobilepos.report.model.ReportItems;
import ncr.res.mobilepos.report.resource.ReportResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ReportResourceSteps extends Steps {
    private ServletContext servletContext = null;
    JournalizationResource journalizationRes = null;
    String businessDate = "";
    ReportResource reportResource = null;
    FinancialReport financialReport;
    DrawerFinancialReport drawerFinancialReport;
    DBInitiator dbInitiatorRESTransaction = null;
    DBInitiator dbInitiatorRESMaster = null;
    ReportItems reportItems = null;
    int reportItemsIndex = 0;
    
    @BeforeScenario
    public final void SetUpClass() {
		try {
			Requirements.SetUp();
			dbInitiatorRESTransaction = new DBInitiator(
					"ReportResourceTest",
					"test/ncr/res/mobilepos/report/resource/test/nodata_dataset.xml",
					DATABASE.RESTransaction);
			dbInitiatorRESMaster = new DBInitiator(
					"ReportResourceTest",
					"test/ncr/res/mobilepos/report/resource/test/departments.xml",
					DATABASE.RESMaster);
			dbInitiatorRESMaster.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/report/resource/test/"
							+ "MST_BIZDAY_2012-06-09.xml");
			dbInitiatorRESMaster.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/report/resource/test/"
							+ "MST_USER_CREDENTIALS.xml");
			journalizationRes = new JournalizationResource(
					Requirements.getMockServletContext());
			String companyid = "01";
			String storeid = "0031";
			businessDate = journalizationRes.getBussinessDate(companyid, storeid);
			GlobalConstant.setStoreOpenTime("10");
			GlobalConstant.setCredentialExpiry("5");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
    }
    private void reSetUpClass(final String filepath){
        String contents = "";
        Requirements.SetUp();

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(filepath));
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size());
            contents = Charset.forName("utf-8").decode(bb).toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail("dataset.xml not found!");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Error occurred when retrieving dataset.xml!");
            return;
        } finally {
        	if (stream != null) {
        		try {
        			stream.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
        
        JournalizationResource journ = new JournalizationResource();
        
        String companyid = "01";
        String storeid = "0031";
        String date = journ.getBussinessDate(companyid, storeid);

        contents = contents.replace(".DATE.", "<![CDATA[" + date
                + "]]>");

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(
                            "test/ncr/res/mobilepos/report/resource/"
                            + "test/dataset.xml"), "UTF8"));            
            out.write(contents);
            out.close();
        } catch (IOException e) {
            Assert.fail("Error occurred while writing to tempdataset.xml!");
            return;
        }
        dbInitiatorRESTransaction = new DBInitiator("ReportResourceTest",
                "test/ncr/res/mobilepos/report/resource/test/dataset.xml", DATABASE.RESTransaction);
    }
    @Given("a ReportResource")
    public final void createSQLServerReportDAO() {
        reportResource = new ReportResource();
        try {
            servletContext = Requirements.getMockServletContext();
            Field context = reportResource.getClass().getDeclaredField(
                    "context");
            context.setAccessible(true);
            context.set(reportResource, servletContext);
        } catch (/* RuntimeError */Exception ex) {
            // startUp = false;
            Assert.fail("Cannot Start the WebAPI");
        }
    }
    @When("I request for Today's Financial Report of terminalid{$deviceNo},"
            + " operatorno{$operatorNo}, storeno{$storeNo}")
    public final void requestFinancialReport(final String companyId, final String deviceNo,
            final String operatorNo, final String storeNo) {
        financialReport = new FinancialReport();        
        financialReport =
            reportResource.getFinancialReport(companyId, deviceNo, storeNo);
        
    }
    @When("I request for Today's Drawer Financial Report"
            + " of printerID{$printerID}, storeno{$storeNo}")
    public final void requestDrawerFinancialReport(
            final String companyId,
            final String printerID, final String storeNo) {
        drawerFinancialReport = new DrawerFinancialReport();        
        drawerFinancialReport =
            reportResource.getDrawerFinancialReport(companyId, printerID, storeNo);
        
    }
    @When("I request for Today's $reportType Sales Report"
            + " of terminalid{$deviceNo}, operatorno{$operatorNo},"
            + " storeno{$storeNo}")
    public final void requestSalesReport(final String companyId, final String reportType,
            final String deviceNo, final String operatorNo,
            final String storeNo) {
        String deviceNoTemp = deviceNo.equalsIgnoreCase("null") ? null
                : deviceNo;
        reportItemsIndex = 0;        
        reportItems = reportResource.getReportNew(companyId, reportType, operatorNo, storeNo);
    }
    @Then("I should get: grossSalesRevenue{$grossSales},"
            + " discountSale{$discountSale}, returnSale{$returnSale}, "
            + "voidSale{$voidSale}, cash{$cash}, creditCard{$creditCard}")
    public final void shouldGetFinancialReportData(final BigDecimal grossSales,
            final BigDecimal discountSale, final BigDecimal returnSale,
            final BigDecimal voidSale, final BigDecimal cash,
            final BigDecimal creditCard){
    	System.out.println(financialReport);
        assertEquals("grossSales", grossSales,
                financialReport.getGrossSalesRevenue());
        assertEquals("discountSale",
                discountSale, financialReport.getDiscountSale());
        assertEquals("returnSale", returnSale,
                financialReport.getReturnSale());
        assertEquals("voidSale", voidSale, financialReport.getVoidSale());
        assertEquals("cash", cash, financialReport.getCash());
        assertEquals("creditCard", creditCard, financialReport.getCreditCard());
        assertEquals("businessDate", businessDate,
                financialReport.getBusinessDate());
    }    
    @Then("I should get drawer financial details:"
            + " grossSalesRevenue{$grossSales}, discountSale{$discountSale}, "
            + "returnSale{$returnSale}, voidSale{$voidSale}, cash{$cash}, "
            + "creditCard{$creditCard}")
    public final void shouldGetDrawerFinancialReportData(
            final BigDecimal grossSales,
            final BigDecimal discountSale, final BigDecimal returnSale,
            final BigDecimal voidSale, final BigDecimal cash,
            final BigDecimal creditCard){
        assertEquals(grossSales, drawerFinancialReport.getGrossSalesRevenue());
        assertEquals(discountSale, drawerFinancialReport.getDiscountSale());
        assertEquals(returnSale, drawerFinancialReport.getReturnSale());
        assertEquals(voidSale, drawerFinancialReport.getVoidSale());
        assertEquals(cash, drawerFinancialReport.getCash());
        assertEquals(creditCard, drawerFinancialReport.getCreditCard());
        assertEquals(businessDate, drawerFinancialReport.getBusinessDate());
    }    
    @SuppressWarnings("deprecation")
	@Then("I should get {$items} reportItemList with data:{customers:{$cntr},"
            + "itemLabel:{code:{$code},name:{$name}},items:{$itemsCnt},"
            + "salesRevenue:{$salesRev}}")
    public final void shouldGetSalesReportData(final long items,
            final int cntr, final String code,
            final String name, final int itemsCnt, final int salesRev){
        Assert.assertNotNull(reportItems.getReportItems());
        assertEquals(items, reportItems.getReportItems().length);
        assertEquals(businessDate, reportItems.getBusinessDayDate());
        if(items > 1){
            assertEquals(cntr, reportItems
                    .getReportItems()[reportItemsIndex].getCustomer());
            assertEquals(code, reportItems
                    .getReportItems()[reportItemsIndex]
                                      .getItemLabel().getCode());
            assertEquals(name, reportItems
                    .getReportItems()[reportItemsIndex]
                                      .getItemLabel()
                                      .getName().replaceAll(" ", ""));
            assertEquals(itemsCnt, reportItems
                    .getReportItems()[reportItemsIndex].getItems());
            assertEquals(salesRev,
                    reportItems.getReportItems()[reportItemsIndex]
                                                 .getSalesRevenue());
            reportItemsIndex++;
        }else{
            assertEquals(cntr, reportItems.getReportItems()
                    [reportItemsIndex].getCustomer());
            assertEquals(code, 
                    reportItems.getReportItems()
                    [reportItemsIndex].getItemLabel().getCode());
            assertEquals(name, reportItems
                    .getReportItems()[reportItemsIndex].getItemLabel()
                    .getName().replaceAll(" ", ""));
            assertEquals(itemsCnt, reportItems
                    .getReportItems()[reportItemsIndex].getItems());
            assertEquals(salesRev, reportItems
                    .getReportItems()[reportItemsIndex].getSalesRevenue());
            reportItemsIndex = 0;
        }
    }
    @Given("a consolidation of cash only transaction")
    public final void consolidationCashOnly(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/"
                + "test/cash-only_dataset.xml");          
    }    
    @Given("a consolidation of cash and change transaction")
    public final void consolidationCashAndChange(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/"
                + "test/cash-change_dataset.xml");          
    }
    @Given("a consolidation of credit only transaction")
    public final void consolidationCreditCardOnly(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/"
                + "test/credit-only_dataset.xml");          
    }
    @Given("a consolidation of cash and credit transaction")
    public final void consolidationCashAndCredit(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/"
                + "resource/test/cash-credit_dataset.xml");          
    }
    @Given("a consolidation of item discount transaction")
    public final void consolidationItemDiscount(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/"
                + "resource/test/item-discount_dataset.xml");          
    }
    @Given("a consolidation of transaction discount")
    public final void consolidationTransactionDiscount(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/"
                + "test/transaction-discount_dataset.xml");
    }
    @Given("a consolidation of void cash transaction")
    public final void consolidationVoidCashTransction(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/test/"
                + "void-cash-transaction_dataset.xml");
    }
    @Given("a consolidation of void credit transaction")
    public final void consolidationVoidCreditTransction(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/test/"
                + "void-credit-transaction_dataset.xml");
    }
    @Given("a consolidation of different dpt item transaction")
    public final void consolidationDifferentDPTItemTransaction(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/test/"
                + "different-dpt-item-transaction_dataset.xml");
    }
    @Given("a consolidation of different terminal transaction")
    public final void consolidationDifferentTerminalTransaction(){
        reportItemsIndex = 0;
        this.reSetUpClass("test/ncr/res/mobilepos/report/resource/test/"
                + "different-terminal-transaction_dataset.xml");
    }
    
    @Given("a poslogxml{$posLogXml} and operator{$operatorNo}")
    public final void givenATransaction(String posLogXml, String operatorNo, int trainingMode){
        journalizationRes.journalize(posLogXml, trainingMode);
    }
    
    @Then("a consolidation runs")
    public final void consolidate(){
        ConsolidationResource consolidationService = new ConsolidationResource();
        try 
        {
            servletContext = Requirements.getMockServletContext();
            Field context = consolidationService
                                .getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(consolidationService, servletContext);
        } catch (/*RuntimeError*/Exception ex) { 
            //startUp = false;
            Assert.fail("Cannot Start the WebAPI");
        }
        
        consolidationService.consolidate();
    }
    
    @Given("a corporate id{$corpid}")
    public final void givenCorporateID(final String corpID){
        GlobalConstant.setCorpid(corpID);
    }
}
