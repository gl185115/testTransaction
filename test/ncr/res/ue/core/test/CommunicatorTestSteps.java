package ncr.res.ue.core.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.core.Communicator;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.UEResponseBase;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class CommunicatorTestSteps extends Steps{
    private Communicator comm = null;
    private Socket testServerSocket = null;
    private String sent = "";
    private Thread testThread = null;
    private List<UEResponseBase> res = null;
    private String [] success = {"004600000031000000009200Store000031      1.0  20"};
    private String [] noterminating = {"004600000021000000009300Store000031      1.0  20",
                                        "004600000021000000009300Store000031      1.0  20"};
    private String [] countfail = {"0200000000000000000000",
                                    "004600000031000000009200Store000031      1.0  20"};
    private String [] multiple = {"004600000031000000009300Store000031      1.0  20",
                                        "004600000031000000009200Store000031      1.0  20"};
    private String [] toUse = null;
    private int socketnum;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a Test Thread using string array: $arraytouse using socket $socket")
    public final void theWebStoreServer(String arraytouse, int socket) throws Exception{
        socketnum = socket;
        if (arraytouse.equals("success")) {
            toUse = success; 
         } else if (arraytouse.equals("noterminating")) {
            toUse = noterminating; 
         } else if (arraytouse.equals("countfail")) {
            toUse = countfail; 
         } else if (arraytouse.equals("multiple")) {
            toUse = multiple; 
         } else {
             throw new Exception("no array found");
         }
        testThread = new Thread() { 
            public void run() { 
                    System.out.println("@Before myThread run()");
                    ServerSocket myServer = null;
                    try { 
                            myServer = new ServerSocket(socketnum); 
                            System.out.println("@Before myThread run() - server socket created."); 
                            testServerSocket = myServer.accept();
                            BufferedReader buff = new BufferedReader(new InputStreamReader(testServerSocket.getInputStream()));
                            PrintStream stream = new PrintStream(testServerSocket.getOutputStream(), true, "UTF-8");
                            
                            sent = buff.readLine();
                            
                            for(int i = 0; i < toUse.length; i++){
                                stream.println(toUse[i]);
                            }

                            System.out.println("@Before myThread run() - accepted connection");
                    } catch (IOException e) { 
                            e.printStackTrace(); 
                    } finally {
                    	if (myServer != null) {
                    		try {
                    			myServer.close();
                    		} catch (IOException e) {
                    			e.printStackTrace();
                    		}
                    	}
                    }
                    
            } 
        }; 
        testThread.start();
    }
        @Given("a Communicator with $ip, $port, $timeout expect ($result)")
    public final void createComm(String ip, int port, int timeout, String result) throws MessageException{
        try {
            comm = new Communicator(ip, port);
            comm.setSocketTimeout(timeout);
            if(!result.equals("success")){
                throw new MessageException("test fail - expected fail");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(result.equals("success")){
                throw new MessageException("test fail - expected success");
            } else {
                String errorMessage = e.getMessage();
                assertThat(errorMessage, is(equalTo(result)));
            }
        }
    }
    
    @When("I send a message ($message) expect ($result)")
    public final void sendReceiveMsg(String message, String result) throws MessageException{
        try {
            res = comm.sendReceiveMessage(message);
            if(!result.equals("success")){
                throw new MessageException("test fail - expected fail");
            }
        } catch (MessageException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            if(result.equals("success")){
                throw new MessageException("test fail - expected success");
            } else {
                String errorMessage = e.getMessage();
                assertThat(errorMessage, is(equalTo(result)));
            }
        }       
    }
    
    @Then("the sent message should be {$message}")
    public final void validateSentMessage(String message){
    	assertThat(sent, is(equalTo(message)));
    }
    
   /* @Then("I should get response {$response}")
    public final void validateResponseMessage(String response){ 
        assertThat(res.getMessage(), is(equalTo(response)));
    }
    
    @Then("I should get resultcode {$resultcode}")
    public final void validateResultCode(int resultcode){
    	assertThat(res.getNCRWSSResultCode(), is(equalTo(0)));
    }*/

    @Then("I stop the socket")
    public final void stop(){
        comm.stopConnection();
    }
    
    @Then("I sleep")
    public final void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
