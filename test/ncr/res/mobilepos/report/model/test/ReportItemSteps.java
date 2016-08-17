package ncr.res.mobilepos.report.model.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.report.model.ItemLabel;
import ncr.res.mobilepos.report.model.ReportItem;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ReportItemSteps extends Steps{
    ReportItem reportItem;
    String reportItemXml = "";
    XmlSerializer<ReportItem> serializer = new XmlSerializer<ReportItem>();
    
    @Given("a ReportItem Class")
    public final void givenFinancialReport()
    {
        reportItem = new ReportItem();
    }
    @Then("I should get properties' initial values")
    public final void getInitialValues(){
        assertEquals(0, reportItem.getCustomer());
        assertEquals(0, reportItem.getItems());      
        assertEquals(0, new BigDecimal(reportItem.getSalesRevenue()).longValue());
        Assert.assertNotNull(reportItem.getItemLabel());
    }
    @When("I set data to properties: {customers:$customers,itemLabel:"
            + "{code:$code,name:$dpt},items:$items,salesRevenue:$salesRev}")
    public final void setDataToProperties(
            final int customers, final String code,
            final String dpt, final int items, final int salesRev){
        reportItem.setCustomer(customers);
        ItemLabel itemLabel = new ItemLabel();
        itemLabel.setCode(code);
        itemLabel.setName(dpt);
        reportItem.setItemLabel(itemLabel);
        reportItem.setItems(items);
        reportItem.setSalesRevenue(salesRev);        
    }
    
    @Then("I should get properties' values: $customers,"
            + " $code, $name, $items, $salesRev")
    public final void shouldGetValues(final int customers, final String code,
            final String name, final int items, final int salesRev){
        assertEquals(customers, reportItem.getCustomer());
        assertEquals(code, reportItem.getItemLabel().getCode());
        assertEquals(name, reportItem.getItemLabel().getName());
        assertEquals(items, reportItem.getItems());
        assertEquals(salesRev, new BigDecimal(reportItem.getSalesRevenue()).longValue());
    }
    @Given("I have an XmlSerializer for ReportItem")
    public final void serializeFinancialReportToXml(){
        try {
            reportItemXml = serializer.marshallObj(
                    ReportItem.class, reportItem, "UTF-8");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    @Then("I should get the xml format: $xml")
    public final void shouldGetXML(final String xml){
        assertEquals(xml, reportItemXml);
    }
    @When("I unmarshall Xml")
    public final void unMarshallXML(){
        try {
            reportItem = serializer.unMarshallXml(
                    reportItemXml, ReportItem.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
