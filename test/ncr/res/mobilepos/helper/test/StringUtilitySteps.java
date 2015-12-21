/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* StringUtilitySteps
*
* Class containing Steps for Unit Testing StringUtility Helper Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import ncr.res.mobilepos.helper.ReceiptFormatter;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;

import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class StringUtilitySteps extends Steps{
    String result;
    
    @When("asked to add commas to numeric value {$value}")
    public final void getCurrencySymbol(final String value){
        result = StringUtility.addCommaToNumString(value);
    }
    
    @When("asked to add commas and currency symbol to numeric value {$value}")
    public final void getCurrencySymbolAndYen(final String value){
        result = StringUtility.addCommaYenToNumString(value);
    }
    
    @When("asked to mask numeric value {$value} up to {$index} with {$symbol}")
    public final void getReplaceValueWith(
            final String value, final int index, final String symbol){
        char toChar = symbol.charAt(0);
        result = StringUtility.replaceStringIndexWith(value, index, toChar);
    }
    
    @When("asked to format the DataBase Date {$date} to format {$format}"
            + " in Locale {$locale}")
    public final void getFormatDBDate(
            final String date, final String format, final String lan){
        Locale locale = null;
        if(lan.equals("en")){
            locale = Locale.ENGLISH;
        }else if(lan.equals("ja")){
            locale = Locale.JAPAN;
        }
        result = StringUtility.formatDBDate(date, format, locale);
    }
    
    @When("asked to get currency symbol value {$amt}")
    public final void getGetCurrencySymbol(final long amt){
        result = StringUtility.getCurrencySymbol(amt);
    }
    
    @When("asked to get number with comma value {$amt}")
    public final void getGetNumberWithComma(final long amt){
        result = StringUtility.getNumberWithComma(amt);
    }
    
    @Then("value should be {$value}")
    public final void checkAmount(final String value){
        assertThat(value, is(equalTo(this.result)));
    }
}
