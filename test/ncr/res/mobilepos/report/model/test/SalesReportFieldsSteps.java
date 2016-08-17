package ncr.res.mobilepos.report.model.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ncr.res.mobilepos.report.model.Details;
import ncr.res.mobilepos.report.model.SalesReportFields;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class SalesReportFieldsSteps extends Steps{
    SalesReportFields salesreportfields;
    Details details;
    
    @Given("an uninitialized Sales Report Fields")
    public final void ASalesReportFieldsModel(){
        salesreportfields = new SalesReportFields();
    }
    
    @Given("A Details Model detail")
    public final void ADetailsModel(){
        details = new Details();
    }
    @Given("A Details Model detail with business date {$business}"
            + " and store number {$store} and deivice number {$device}")
    public final void ADetailsModel2(
            final String business, final String store, final String device){
        details = new Details();
        details.setBusinessDate(business);
        details.setDetailsDeviceNumber(device);
        details.setStoreNumber(store);
        
    }
    @When("the details property of Sales Report Fields is set to detail")
    public final void setReportDetails()
    {
        salesreportfields.setDetails(details);
    }

    @Then("the xml of Sales Report should be : {$xml}")
    public final void salesreporthasXML(final String xml){        
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context;
        
            context = JAXBContext.newInstance(SalesReportFields.class);
        
            Marshaller m = context.createMarshaller();        
            m.marshal(salesreportfields, writer);    
                        
            assertThat(writer.toString(), is(equalTo(xml)));        
                        
        } catch (JAXBException e) {
            e.printStackTrace();
        }    
    }    
        
                
    @When("I set its Sales Report Type {$salesReportType}")
    public final void ItsSalesReportType(final String salesReportType){        
        salesreportfields.setSalesReportType(salesReportType);
    }
    
    @Then("its Sales Report Type is : {$salesReportType}")
    public final void getSalesReportType(final String salesReportType){
        assertEquals(salesReportType, salesreportfields.getSalesReportType());
    }
    
    
    
}
