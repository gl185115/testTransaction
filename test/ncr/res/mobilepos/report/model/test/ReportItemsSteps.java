package ncr.res.mobilepos.report.model.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.report.model.ItemLabel;
import ncr.res.mobilepos.report.model.ReportItem;
import ncr.res.mobilepos.report.model.ReportItems;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ReportItemsSteps extends Steps{
    ReportItems reportItems;
    String reportItemsXml = "";
    XmlSerializer<ReportItems> serializer = new XmlSerializer<ReportItems>();
    
    @Given("a ReportItems Class")
    public final void givenFinancialReport()
    {
        reportItems = new ReportItems();
    }
    @SuppressWarnings("deprecation")
    @Then("I should get properties' initial values")
    public final void getInitialValues(){
        assertEquals("", reportItems.getBusinessDayDate());        
        if(reportItems != null && reportItems.getReportItems() != null && reportItems.getReportItems().length == 0){
        	Assert.assertNull(null);
        }else{        
        	assertEquals(null, reportItems.getReportItems());
        }	        
    }
    @When("I set data to properties: {customers:$customers,itemLabel:"
            + "{code:$code,name:$dpt},items:$items,salesRevenue:$salesRev}]"
            + ",businessDayDate:$businessDate}")
    public final void setDataToProperties(final int customers,
            final String code, final String dpt,
            final int items, final int salesRev, final String businessDate){
        ReportItem reportItem = new ReportItem();        
        reportItem.setCustomer(customers);
        ItemLabel itemLabel = new ItemLabel();
        itemLabel.setCode(code);
        itemLabel.setName(dpt);
        reportItem.setItemLabel(itemLabel);
        reportItem.setItems(items);
        reportItem.setSalesRevenue(salesRev);
        ReportItem[] reportItemsArr = new ReportItem[1];
        reportItemsArr[0] = reportItem;
        reportItems.setReportItems(reportItemsArr);
        reportItems.setBusinessDayDate(businessDate);
    }
    
    @Then("I should get properties' values: $customers, $code, $name,"
            + " $items, $salesRev, $bussDate")
    public final void shouldGetValues(final int customers,
            final String code, final String name,
            final int items, final double salesRev, final String bussDate){
        assertEquals("2012-01-01", reportItems.getBusinessDayDate());
        assertEquals(1, reportItems.getReportItems().length);
        assertEquals(customers, reportItems.getReportItems()[0].getCustomer());
        assertEquals(code, reportItems
                .getReportItems()[0].getItemLabel().getCode());
        assertEquals(name, reportItems
                .getReportItems()[0].getItemLabel().getName());
        assertEquals(items, reportItems
                .getReportItems()[0].getItems());
        assertEquals(new BigDecimal(salesRev), new BigDecimal(reportItems
                .getReportItems()[0].getSalesRevenue()));
        assertEquals(bussDate, reportItems.getBusinessDayDate());
    }
    @Given("I have an XmlSerializer for ReportItems")
    public final void serializeFinancialReportToXml(){
        try {
            reportItemsXml = serializer.marshallObj(ReportItems.class,
                    reportItems, "UTF-8");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    @Then("I should get the xml format: $xml")
    public final void shouldGetXML(final String xml){
        assertEquals(xml, reportItemsXml);
    }
    @When("I unmarshall Xml")
    public final void unMarshallXML(){
        try {
            reportItems =
                serializer.unMarshallXml(reportItemsXml, ReportItems.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
