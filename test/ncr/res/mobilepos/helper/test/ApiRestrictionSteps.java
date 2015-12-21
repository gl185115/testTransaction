/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* ApiRestrictionSteps
*
* Class containing Steps for Unit Testing ApiRestriction Helper Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.ApiRestriction;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ApiRestrictionSteps extends Steps {
    
    private int result;
    private String deviceNumber;
    private String operatorNumber;
    private int restriction;
    private DBInitiator dbInit;
    
    @BeforeScenario
    public final void SetUpClass()
    throws DatabaseUnitException, SQLException, Exception
    {        
        Requirements.SetUp();
        dbInit = new DBInitiator("ApiRestrictionSteps", DATABASE.RESMaster);
        dbInit.ExecuteOperation( DatabaseOperation.CLEAN_INSERT, 
                "test/ncr/res/mobilepos/helper/test/dataset.xml");    
        GlobalConstant.setCredentialExpiry("5");
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
    
    @Given("a non-existing Device Number {$deviceNumber}")
    public final void NonExistingDeviceNum(final String deviceNumberToSet){
        setDeviceNumber(deviceNumberToSet);
    }
    
    @Given("an existing Device number {$deviceNumber} but not authenticated")
    public final void ExistingNonAuthDeviceNumber(
            final String deviceNumberToSet){
        setDeviceNumber(deviceNumberToSet);
    }
    
    @Given("an existing Device number {$deviceNumber} and is authenticated")
    public final void ExistingAuthDeviceNumber(final String deviceNumberToSet){
        setDeviceNumber(deviceNumberToSet);
    }
    
    @Given("a non-existing Operator number {$operatorNumber}")
    public final void NonExistingOpNum(final String operatornumber){
        setOperator(operatornumber);
    }
    
    @Given("an existing Operator number {$operatornumber} but not signed on")
    public final void ExistingUnSignedOpNum(final String operatornumber){
        setOperator(operatornumber);
    }

    @Given("an existing Operator number {$operatorNumber} and is signed on")
    public final void ExistingSignedOpNum(final String operatornumber){
        setOperator(operatornumber);
    }
    
    @When("the restriction for the Device Number is retrieved")
    public final void getDeviceRestriction(){
        try{
            restriction = ApiRestriction.getRestriction(deviceNumber);
            result = 0;
        }
        catch(Exception e){
            result = 1;
        }
    }
    
    @When("the restriction for the Device Number"
            + " and Operator Number is retrieved")
    public final void getDeviceOpRestriction(){
        try{
            restriction =
                ApiRestriction.getRestriction(deviceNumber, operatorNumber);
            result = 0;
        }
        catch(Exception e){
            result = 1;
        }
    }
    
    @Then("the Restriction should be {$restriction}")
    public final void theRestrictionShouldBe(final int expectedrestriction){
        try{
            assertThat(expectedrestriction, is(equalTo(this.restriction)));
            result = 0;
        }
        catch(Exception e){
            result = 1;
        }
    }

    @Then("it should succeed")
    public final void shouldSucceed(){
        assertThat(0, is(equalTo(result)));
    }
    
    @Then("it should fail")
    public final void shouldFail(){
        assertThat(0, is(not(equalTo(result))));
    }
    
    private void setOperator(final String operatorNumberToSet){
        if(!operatorNumberToSet.isEmpty() && null != operatorNumberToSet){
            this.operatorNumber = operatorNumberToSet;
            result = 0;
        }
        else{
            result = 1;
        }
    }
    
    private void setDeviceNumber(final String deviceNumberToSet){
        if(!deviceNumberToSet.isEmpty() && null != deviceNumberToSet){
            this.deviceNumber = deviceNumberToSet;
            result = 0;
        }
        else{
            result = 1;
        }
    }
}
