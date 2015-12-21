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

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class DetailsSteps extends Steps{
    Details details;
    
    @Given("an uninitialized Details Model")
    public final void ADetailsModel(){
        details = new Details();        
    }
    
    @Then("its XML should be :$xml")
    public final void IShouldHaveDetailsModel(final String xml){
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context;
        
            context = JAXBContext.newInstance(Details.class);
        
            Marshaller m = context.createMarshaller();
            m.marshal(details, writer);    
                        
            assertThat(writer.toString(), is(equalTo(xml)));
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("I set its Business Date to {$businessDate}")
    public final void ItsBusinessDate(final String businessDate){
        details.setBusinessDate(businessDate);
    }
    
    @Then("its Business Date is : {$businessDate}")
    public final void getBusinessDate(final String businessDate){
        assertEquals(businessDate, details.getBusinessDate());
    }
    
    @When("I set its Store Number to {$storeNumber}")
    public final void ItsStoreNumber(final String storeNumber){
        details.setStoreNumber(storeNumber);
    }
    
    @Then("its Store Number is : {$storeNumber}")
    public final void getStoreNumber(final String storeNumber){
        assertEquals(storeNumber, details.getStoreNumber());
    }
    
    @When("I set its Department {$department}")
    public final void ItsDepartment(final String department){
        details.setDepartment(department);
    }
    
    @Then("its Department is : {$department}")
    public final void getDepartment(final String department){
        assertEquals(department, details.getDepartment());
    }
    
    @When("I set its Time Zone {$timeZone}")
    public final void ItsTimeZone(final String timeZone){
        details.setTimeZone(timeZone);
    }
    
    @Then("its Time Zone is : {$timeZone}")
    public final void getTimeZone(final String timeZone){
        assertEquals(timeZone, details.getTimeZone());
    }
        
    @When("I set its Details Operator Number {$detailsOperatorNumber}")
    public final void ItsDetailsOprNo(final String detailsOperatorNumber){
        details.setDetailsOperatorNumber(detailsOperatorNumber);
    }
    
    @Then("its OperatorNumber is : {$detailsOperatorNumber}")
    public final void getDetailsOprNo(final String detailsOperatorNumber){
        assertEquals(detailsOperatorNumber, details.getDetailsOperatorNumber());
    }
        
    @When("I set its Details Device Number {$detailsDeviceNumber}")
    public final void ItsDetailsDeviceNo(final String detailsDeviceNumber){
        details.setDetailsOperatorNumber(detailsDeviceNumber);
    }
    
    @Then("its Details Device Number is : {$detailsDeviceNumber}")
    public final void getDetailsDeviceNumber(final String detailsDeviceNumber){
        assertEquals(detailsDeviceNumber, details.getDetailsOperatorNumber());
    }
}
