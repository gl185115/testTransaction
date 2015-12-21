package ncr.res.mobilepos.report.model.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ncr.res.mobilepos.report.model.ReportResult;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ReportResultSteps extends Steps{
    ReportResult reportresult;
    
    @Given("an uninitialized Report result")
    public final void AReportResult(){
        reportresult = new ReportResult();
    }
    
    @Then("I should have a xml report result {$xml}")
    public final void IShouldHaveAReportResult(final String xml){
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context;
        
            context = JAXBContext.newInstance(ReportResult.class);
        
            Marshaller m = context.createMarshaller();        
            m.marshal(reportresult, writer);    
                        
            assertThat(writer.toString(), is(equalTo(xml)));        
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
        
    @When("I set its Message {$message}")
    public final void ItsMessage(final String message){        
        reportresult.setMessage(message);
    }
    
    @Then("its Message is : {$message}")
    public final void getMessage(final String message){
        assertEquals(message, reportresult.getMessage());
    }
    
    @When("I set its Report {$report}")
    public final void ItsReport(final String report){        
        reportresult.setReport(report);
    }
    
    @Then("its Report is : {$report}")
    public final void getReport(final String report){
        assertEquals(report, reportresult.getReport());
    }
}
