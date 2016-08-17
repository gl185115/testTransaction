/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* LoggerSteps
*
* Class containing Steps for Unit Testing Logger Helper Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class LoggerSteps extends Steps{
    private Logger iowriter;
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
        deleteLogFile();        
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
    
    @Given("a logger")
    public final void createLogger(){
        iowriter = (Logger) Logger.getInstance();
    }
    
    @When("asked to log an alert with progName {$progName},"
            + " functionName {$functionName}, code {$code},"
            + " and logMessage {$logMessage}")
    public final void logAlert(final String progName, final String functionName,
            final String code, final String logMessage){
        iowriter.logAlert(progName, functionName, code, logMessage);
    }
    
    @When("asked to log an error with progName {$progName},"
            + " functionName {$functionName}, code {$code}, and "
            + "logMessage {$logMessage}")
    public final void logError(final String progName, final String functionName,
            final String code, final String logMessage){
        iowriter.logError(progName, functionName, code, logMessage);
    }
    
    @When("asked to log a warning with progName {$progName}, "
            + "functionName {$functionName}, code {$code}, and logMessage"
            + " {$logMessage}")
    public final void logWarning(final String progName,
            final String functionName,
            final String code, final String logMessage){
        iowriter.logWarning(progName, functionName, code, logMessage);
    }
    
    @When("asked to log a function entry with progName"
            + " {$progName} and functionName {$functionName}")
    public final void logFunctionEntry(final String progName,
            final String functionName){
        iowriter.logFunctionEntry(progName, functionName);
    }
    
    @When("asked to log a function entry with progName {$progName},"
            + " functionName {$functionName}, and params"
            + " {$param1} {$param2} {$param3}")
    public final void logFunctionEntryParams(final String progName,
            final String functionName,
            final String param1, final String param2, final String param3){
        iowriter.logFunctionEntry(progName, functionName,
                param1, param2, param3);
    }
    
    @When("asked to log a function exit with progName {$progName},"
            + " functionName {$functionName}, code {$code},"
            + " and logMessage {$logMessage}")
    public final void logFunctionExit(final String progName,
            final String functionName,
            final String code, final String logMessage){
        iowriter.logFunctionExit(progName, functionName, code, logMessage);
    }

    @When("asked to log a function exit with progName {$progName},"
            + " functionName {$functionName}, and logMessage {$logMessage}")
    public final void logFunctionExit2(final String progName,
            final String functionName,
            final String logMessage){
        iowriter.logFunctionExit(progName, functionName, logMessage);
    }
    
    @When("asked to log normal with progName {$progName}, functionName"
            + " {$functionName}, and logMessage {$logMessage}")
    public final void logNormal(final String progName,
            final String functionName,
            final String logMessage){
        iowriter.logNormal(progName, functionName, logMessage);
    }
        
    @When("asked to log a snap line with progName {$progName}, "
            + "functionName {$functionName}, code {$code},"
            + " and logMessage {$logMessage}")
    public final void logSnapLine(final String progName,
            final String functionName,
            final String code, final String logMessage){
        try {
            iowriter.logSnapLine(progName, functionName, code, logMessage);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("directory is deleted")
    public final void deleteDirectory(){
        File file = new File("c:/testtemp");
        try {
            deleteDir(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Then("output in log file should be {$output}")
    public final void checkOutputLog(String output){
        FileInputStream fis;
        
        File file = new File("C://testtemp//IOWLOG");
        BufferedReader br = null;
        try{
            fis = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fis));
            String log ="";
            String tmp = "";
            while ((tmp = br.readLine()) != null)
            {
               log = tmp;
            }
            
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            
            output = output.replace("?",dateFormat.format(date));
            
            assertThat(output, is(equalTo(log)));
            
        } catch (FileNotFoundException e) {
        }
        /*if(file.canRead()){
            fis = new FileInputStream(file);
            String fileContent = new Scanner(fis).useDelimiter("\\Z").next();
            StringBuilder outputText = new StringBuilder(output);
            assertThat(output, is(equalTo(fileContent)));
        }*/ catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
        	if (br != null) {
        		try {
        			br.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
    }
    
    private void deleteLogFile(){
        File file = new File("C://testtemp//IOWLOG");
        if(file.exists()){
            file.delete();
        }
    }
    
    private void deleteDir(final File file)
    throws IOException{

    if(file.isDirectory()){

        //directory is empty, then delete it
        if(file.list().length==0){

           file.delete();
           System.out.println("Directory is deleted : " 
                                             + file.getAbsolutePath());

        }else{

           //list all the directory contents
           String[] files = file.list();

           for (String temp : files) {
              //construct the file structure
              File fileDelete = new File(file, temp);

              //recursive delete
             deleteDir(fileDelete);
           }

           //check the directory again, if empty then delete it
           if(file.list().length==0){
                file.delete();
             System.out.println("Directory is deleted : " 
                                              + file.getAbsolutePath());
           }
        }

    }else{
        //if file, then delete it
        file.delete();
        System.out.println("File is deleted : " + file.getAbsolutePath());
    }
}
}
