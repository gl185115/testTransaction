/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* SQLServerReportDAOSteps
*
* Class containing Steps for Unit Testing SQLServerReportDAO Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.report.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.report.dao.IReportDAO;
import ncr.res.mobilepos.report.model.Details;
import ncr.res.mobilepos.report.model.DrawerFinancialReport;
import ncr.res.mobilepos.report.model.FinancialReport;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class SQLServerReportDAOSteps extends Steps{

    DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
    IReportDAO reportDAO;
    String report;
    int result;
    
    FinancialReport financialReport;
    DrawerFinancialReport drawerFinancialReport;
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
        new DBInitiator("ApiRestrictionTest",
                "test/ncr/res/mobilepos/report/dao/test/dataset.xml", DATABASE.RESTransaction);
        GlobalConstant.setStoreOpenTime("10");
        GlobalConstant.setCorpid("0002");
    }
    
    @Given("an SQLServerReportDAO")
    public final void createSQLServerReportDAO(){
        try {
            reportDAO = daoFactory.getReportDAO();
            result = 1;
        } catch (DaoException e) {
            result = 0;
        }
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
        
    @When("asked to create a Department report with device number {$deviceNo},"
            + " operator number {$operatorNo}, business day date"
            + " {$businessdaydate}, and store number {$storeNo}")
    public final void generateDepartmentReport(
            final String deviceNo, final String operatorNo,
            final String businessdaydate, final String storeNo){
        try {
            report = reportDAO.generateDepartmentReport(deviceNo, operatorNo,
                    businessdaydate, storeNo);
            result = 0;
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @When("asked to create a Detail report with details: $details")
    public final void generateDetailReport(final String details){
        XmlSerializer<Details> xmlSerializer = new XmlSerializer<Details>();
        Details detailsModel = new Details();
        try {
            detailsModel = xmlSerializer.unMarshallXml(details,
                    Details.class);
            report = reportDAO.generateDetailReport(detailsModel);
            result = 0;
        } catch (DaoException e) {
            result = 1;
        } catch (JAXBException e) {
            result = 1;
        }
    }
    
    /*@When("asked to create a Financial report with device number {$deviceNo},"
            + " business day date {$businessdaydate},"
            + " and store number {$storeNo}")
    public final void generateFinancialReport(final String deviceNo,
            final String businessdaydate, final String storeNo){
        try {
            report = reportDAO.generateFinancialReport(deviceNo,
                    businessdaydate, storeNo);
            result = 0;
            return;
        } catch (SQLException e) {
        } catch (DaoException e) {
        }
        result = 1;
    }*/
    
        
    @When("asked to create an Hourly report with device number {$deviceNo},"
            + " operator number {$operatorNo}, business day date"
            + " {$businessdaydate}, store number {$storeNo}, "
            + "and start time {$startTime}")
    public final void generateHourlyReport(
            final String deviceNo, final String operatorNo, 
            final String businessdaydate, final String storeNo,
            final String startTime){
        try {
            report = reportDAO.generateHourlyReport(deviceNo, operatorNo,
                    businessdaydate, storeNo, startTime);
            result = 0;
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @When("asked to create a SalesClerk report with device number {$deviceNo},"
            + " operator number {$operatorNo}, business day date"
            + " {$businessdaydate}, and store number {$storeNo}")
    public final void generateSalesClerkReport(final String deviceNo,
            final String operatorNo, final String businessdaydate,
            final String storeNo){
        try {
            report = reportDAO.generateSalesClerkReport(deviceNo,
                    operatorNo, businessdaydate, storeNo);
            result = 0;
        } catch (DaoException e) {
            result = 1;
        }
    }
    
    @When("asked to create a Financial report Object with device number"
            + " {$deviceNo}, business day date {$businessdaydate},"
            + " and store number {$storeNo}")
    public final void getFinancialReportObject(final String deviceNo,
            final String businessdadate, final String storeNo){
        try {
            financialReport = reportDAO.getFinancialReportObj(deviceNo,
                    businessdadate, storeNo);
        } catch (DaoException e) {
            result = 1;
        } catch (SQLException e) {
            result = 1;
        }catch (Exception e) {
        	result = 1;
        }
    }
    
    @Then("report should be: $report")
    public final void checkReport(String expectedreport){
        boolean checker = false;
        expectedreport = expectedreport.replaceAll("(\\r|\\t|\\n)", "");
        checker = expectedreport.equals(this.report);
        if(!checker){
            System.out.println("Expected Report: "
                    + expectedreport + "\nCurrent Report: "
                    + this.report + "\n");
            return;
        }
        assertThat(true, is(equalTo(checker)));
    }

    @Then("financial report should be: $report")
    public final void checkFinancialReport(String expectedreport){
        expectedreport = expectedreport.replaceAll("(\\r)", "");
        assertThat(true, is(equalTo(expectedreport.equals(this.report))));
    }
    
    @Then("financial report object should be retrieved.")
    public final void successFinancialReportObj(){
        assertThat("000031", is(equalTo(financialReport.getStoreid())));
        assertThat("NCR Plaza", is(equalTo(financialReport.getStoreName())));
        assertThat("2011-08-08", is(equalTo(
                financialReport.getBusinessDate())));
        assertThat((long)0, is(equalTo(
                financialReport.getGrossSalesRevenue().longValue())));
        assertThat((long)0, is(equalTo(
                financialReport.getReturnSale().longValue())));
        assertThat((long)0, is(equalTo(
                financialReport.getVoidSale().longValue())));
        assertThat((long)0, is(equalTo(
                financialReport.getDiscountSale().longValue())));
        assertThat((long)0, is(equalTo(
                financialReport.getNetRevenue().longValue())));
        assertThat((long)223, is(equalTo(
                financialReport.getCash().longValue())));
        assertThat((long)0, is(equalTo(
                financialReport.getCreditCard().longValue())));
        assertThat((long)223, is(equalTo(
                financialReport.getTotalSale().longValue())));
    }
    
    @Then("it should fail")
    public final void checkReportFail(){
        assertThat(1, is(equalTo(result)));
    }
    
    @When("asked to create a Drawer Financial report with printerID"
            + " {$printerID}, business day date {$businessdaydate},"
            + " and store number {$storeNo}")
    public final void generateDrawerFinancialReport(final String printerID,
            final String businessdaydate, final String storeNo){
        try {
            drawerFinancialReport = reportDAO.generateDrawerFinancialReport(
                    printerID, businessdaydate, storeNo);
            result = 0;
            return;
        } catch (SQLException e) {
        } catch (DaoException e) {
        }catch (Exception e) {
        }
        result = 1;
    }
    
    @Then("drawer financial report object should be retrieved.")
    public final void successDrawerFinancialReportObj(){
        assertThat("000031",
                is(equalTo(drawerFinancialReport.getStoreid())));
        assertThat("NCR Plaza",
                is(equalTo(drawerFinancialReport.getStoreName())));
        assertThat("2011-08-08",
                is(equalTo(drawerFinancialReport.getBusinessDate())));
        assertThat((long)0,
                is(equalTo(drawerFinancialReport.getGrossSalesRevenue()
                                                .longValue())));
        assertThat((long)0,
                is(equalTo(drawerFinancialReport.getReturnSale()
                                                .longValue())));
        assertThat((long)0, is(equalTo(drawerFinancialReport
                                                .getVoidSale().longValue())));
        assertThat((long)0, is(equalTo(drawerFinancialReport
                                                .getDiscountSale()
                                                .longValue())));
        assertThat((long)0, is(equalTo(
                drawerFinancialReport.getNetRevenue().longValue())));
        assertThat((long)223, is(equalTo(
                drawerFinancialReport.getCash().longValue())));
        assertThat((long)0, is(equalTo(
                drawerFinancialReport.getCreditCard().longValue())));
        assertThat((long)223, is(equalTo(
                drawerFinancialReport.getTotalSale().longValue())));
    }
}
