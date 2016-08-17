package ncr.res.mobilepos.report.model.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ncr.res.mobilepos.report.model.ReportParam;
import ncr.res.mobilepos.report.model.SalesReportFields;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ReportParamSteps extends Steps{
    ReportParam reportparam;
    SalesReportFields salereportfields;
    
    @Given("an uninitialized Report Param Model")
    public final void AReportParamModel(){
        reportparam = new ReportParam();
    }
    
    @Given("A Sales Report Fields")
    public final void ASalesReportFields(){
        salereportfields = new SalesReportFields();
    }
    
    @Given("A Sale Report Fields with sales report type {$reporttype}")
    public final void ASalesReportFieldsModel(final String reporttype){    
        salereportfields.setSalesReportType(reporttype);        
    }
    
    @When("the sales report fields property of Report Param is set to fields")
    public final void setSalesReportFields(){            
        reportparam.setSalesReportFields(salereportfields);
    }
    
    @Then("the XML of Sales Report Fields should be :$xml")
    public final void IShouldHaveReportParam(final String xml){
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context;
        
            context = JAXBContext.newInstance(ReportParam.class);
        
            Marshaller m = context.createMarshaller();        
            m.marshal(reportparam, writer);    
                        
            assertThat(writer.toString(), is(equalTo(xml)));        
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("I set its Report Type to {$reportType}")
    public final void ItsReportType(final String reportType){        
        reportparam.setReportType(reportType);
    }
    
    @Then("its Report type is : {$reportType}")
    public final void getReportType(final String reportType){
        assertEquals(reportType, reportparam.getReportType());
    }
}
