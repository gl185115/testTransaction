package ncr.res.mobilepos.report.model.test;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.report.model.FinancialReport;
import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import org.jbehave.scenario.annotations.*;
import org.jbehave.scenario.steps.Steps;
import static org.junit.Assert.*;

public class FinancialReportSteps extends Steps{
    FinancialReport report;
    String financialRptXml = "";
    XmlSerializer<FinancialReport> serializer =
        new XmlSerializer<FinancialReport>();
    
    @Given("a Financial Report Class")
    public final void givenFinancialReport()
    {
        report = new FinancialReport();
    }
    @Then("I should get properties' initial values")
    public final void getInitialValues(){
        BigDecimal zero = new BigDecimal(0);
        assertEquals(null, report.getDeviceNo());
        assertEquals(null, report.getBusinessDate());
        assertEquals(zero, report.getGrossSalesRevenue());
        assertEquals(zero, report.getDiscountSale());
        assertEquals(zero, report.getReturnSale());
        assertEquals(zero, report.getVoidSale());
        assertEquals(zero, report.getCash());
        assertEquals(zero, report.getCreditCard());
        assertEquals(zero, report.getMiscellaneous());
        assertEquals(null, report.getStoreid());
        assertEquals(null, report.getStoreName());
    }
    @When("I set data to properties: $devNo, $businessDate, $grossSales,"
            + " $discount, $returnSales, $voidSales, $cash, $credit,"
            + " $misc, $storeid, $storeName")
    public final void setDataToProperties(
            final String devNo, final String businessDate,
            final BigDecimal grossSales, final BigDecimal discount,
            final BigDecimal returnSale, final BigDecimal voidSale,
            final BigDecimal cash, final BigDecimal credit,
            final BigDecimal misc, final String storeid,
            final String storeName){
        report.setDeviceNo(devNo);
        report.setBusinessDate(businessDate);
        report.setGrossSalesRevenue(grossSales);
        report.setDiscountSale(discount);
        report.setReturnSale(returnSale);
        report.setVoidSale(voidSale);
        report.setCash(cash);
        report.setCreditCard(credit);
        report.setMiscellaneous(misc);
        report.setStoreid(storeid);
        report.setStoreName(storeName);
    }
    @Then("I should get properties' values: $devNo, $businessDate,"
            + " $grossSales, $discount, $returnSales, $voidSales, $cash, "
            + "$credit, $misc, $storeid, $storeName")
    public final void shouldGetValues(final String devNo,
            final String businessDate, final BigDecimal grossSales,
            final BigDecimal discount, final BigDecimal returnSale, 
            final BigDecimal voidSale, final BigDecimal cash,
            final BigDecimal credit, final BigDecimal misc,
            final String storeid,
            final String storeName){
        assertEquals(devNo, report.getDeviceNo());
        assertEquals(businessDate, report.getBusinessDate());
        assertEquals(grossSales, report.getGrossSalesRevenue());
        assertEquals(discount, report.getDiscountSale());
        assertEquals(returnSale, report.getReturnSale());
        assertEquals(voidSale, report.getVoidSale());
        assertEquals(cash, report.getCash());
        assertEquals(credit, report.getCreditCard());
        assertEquals(misc, report.getMiscellaneous());
        assertEquals(storeid, report.getStoreid());
        assertEquals(storeName, report.getStoreName());
    }
    @Given("I have an XmlSerializer for Financial Report")
    public final void serializeFinancialReportToXml(){
        try {
            financialRptXml = serializer.marshallObj(
                    FinancialReport.class, report, "UTF-8");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    @Then("I should get the xml format: $xml")
    public final void shouldGetXML(final String xml){
        assertEquals(xml, financialRptXml);
    }
    @When("I unMarshall Xml")
    public final void unMarshallXml(){
        try {
            report = serializer.unMarshallXml(financialRptXml,
                    FinancialReport.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
