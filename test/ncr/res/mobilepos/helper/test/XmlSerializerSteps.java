/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* XMLSerializerSteps
*
* Class containing Steps for Unit Testing XMLSerializer Helper Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;

import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.xml.sax.SAXParseException;

public class XmlSerializerSteps extends Steps{
    XmlSerializer<SampleModel> xmlSerializer;
    SampleModel sample;
    String xml;
    
    
    @Given("an XmlSerializer for SampleModel")
    public final void createLogger(){
        xmlSerializer = new XmlSerializer<SampleModel>();
    }
    

    @When("asked to marshall object to XML: $values")
    public final void marshallSampleModel(final ExamplesTable values){
        sample = new SampleModel();
        sample.setStringElem(values.getRow(0).get("stringElem"));
        sample.setIntElem(Integer.parseInt(values.getRow(0).get("intElem")));
        try {
            xml = xmlSerializer.marshallObj(SampleModel.class, sample, "utf-8");
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("asked to marshall null SampleModel")
    public final void marshallNullSampleModel(){
        sample = new SampleModel();
        try {
            xml = xmlSerializer.marshallObj(SampleModel.class, sample, "utf-8");
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("asked to unmarshall XML to object: $xml")
    public final void unmarshallSampleModel(final String xmlinput){
        try {
            sample = xmlSerializer.unMarshallXml(xmlinput, SampleModel.class);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @When("asked to unmarshall empty XML")
    public final void unmarshallSampleModelFromEmptyXML(){
        try {
            sample = xmlSerializer.unMarshallXml("", SampleModel.class);
        } catch (Exception ex) {
            assertThat("unMarshalling empty xmL should throw",
                    (Class<SAXParseException>)ex.getCause().getClass(),
                    is(equalTo((SAXParseException.class))));
        }
    }
    
    @Then("xml should be: $value")
    public final void checkXML(String value){
        value = value.replaceAll("(\\r|\\n|\\t)", "");
        assertThat(value, is(equalTo(this.xml)));
    }
    
    @Then("object should be: $values")
    public final void checkModel(final ExamplesTable values){
        assertThat(sample.getStringElem(),
                is(equalTo(values.getRow(0).get("stringElem"))));
        assertThat(sample.getIntElem(),
                is(equalTo(Integer.parseInt(values.getRow(0).get("intElem")))));
    }
    @Then("object should be null")
    public final void checkEmptyModel(){
        Assert.assertEquals(null, sample);;
      
    }
}
