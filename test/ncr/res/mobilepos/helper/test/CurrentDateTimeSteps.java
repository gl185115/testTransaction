/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* CurrentDateTimeSteps
*
* Class containing Steps for Unit Testing CurrentDateTime Helper Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ncr.res.mobilepos.helper.CurrentDateTime;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class CurrentDateTimeSteps extends Steps{
    private String dateTime;
    
    @When("Current Date Time is retrieved")
    public final void currentDateTime(){
        dateTime = CurrentDateTime.getCurrTimeByHourMins();
    }
    
    @Then("Current hh:mm of computer must be same with what is retrieved")
    public final void checkHHMM(){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat timeformat = new SimpleDateFormat("h:mm");
        
        StringBuilder sb = new StringBuilder();        
        sb.append(timeformat.format(cal.getTime()));
        
        assertThat(dateTime, is(equalTo(sb.toString())));
    }
    
    @Then ("Current date of computer is same with"
            + " what is stored in CurrentDateTime")
    public final void checkCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        
        assertThat(dateFormat.format(date),
                is(equalTo(CurrentDateTime.getCurrentDate())));
    }
    
    @Then ("Previous date of computer is same"
            + " with what is stored in CurrentDateTime")
    public final void checkPreviousDate(){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1); 
        
        assertThat(dateFormat.format(cal.getTime()),
                is(equalTo(CurrentDateTime.getPrevDate())));
    }
    
    @Then ("Am_Pm of computer is same with what is stored in CurrentDateTime")
    public final void checkAMPM(){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.add(Calendar.DATE, -1); 
        
        assertThat(cal.get(Calendar.AM_PM),
                is(equalTo(CurrentDateTime.getAmPm())));
    }
}
