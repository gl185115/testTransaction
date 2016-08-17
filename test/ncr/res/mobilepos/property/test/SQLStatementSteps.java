package ncr.res.mobilepos.property.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.property.SQLStatement;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;
/*
Steps:
Given an SQL Statement Listing from {$xml}
When I get an Item from Listing with key {$key}
Then I should get the value {$value}
 */
/**
 * SQLstatement steps for SQL Statements.
 */
public class SQLStatementSteps extends Steps {
   /**
    * item result.
    */
    private String result;

    /**
     * Given Step : An SQL Statement listing from xml.
     * @param xml   XML file
     */
    @Given("an SQL Statement Listing from {$xml}")
    public final void anSQLStatementListingXml(final String xml) {
        SQLStatement.setFilepath(xml);
        SQLStatement.setInputStream(SQLStatementSteps
                .class.getResourceAsStream(xml));
    }

    /**
     * Given When : I get an Item from Listing with key number.
     * @param key   Item's key
     */
    @When("I get an Item from Listing with key {$key}")
    public final void getItemByWithKey(final String key) {
        try {
            SQLStatement instance = SQLStatement.getInstance();
            result = instance.getProperty(key);
        } catch (Exception e) {
            // TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
            e.printStackTrace();
            Assert.fail("Sql Statement has no inputStream variable");
        }
    }

    /**
     * Given Then : I should get the value.
     * @param value     Item's value.
     */
    @Then("I should get the value {$value}")
    public final void theValueIs(final String value) {
        if (value.compareToIgnoreCase("null") != 0) {
            assertThat(result, is(equalTo(value)));
        } else {
            assertNull(result);
        }
    }
}
