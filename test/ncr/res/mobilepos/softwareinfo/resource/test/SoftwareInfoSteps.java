package ncr.res.mobilepos.softwareinfo.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResourceLoader;

import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.softwareinfo.resource.SoftwareInfo;
import ncr.res.mobilepos.softwareinfo.model.SoftwareComponents;
import ncr.res.mobilepos.softwareinfo.model.SoftwareVersion;

public class SoftwareInfoSteps extends Steps {
	@InjectMocks
    private SoftwareInfo service;
    private SoftwareVersion software;
    private SoftwareComponents softwareComponents;
    @Mock
    private ServletContext context;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        // Incorporate mock servlet context to the root directory
        Requirements.setMockUpServletContext("/WebContent", new FileSystemResourceLoader());
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("Web API Service SofwareInfo")
    public final void givenSoftwareInfoService(){
		ServletContext mockContext = Requirements.getMockServletContext();
		try {
			service = new SoftwareInfo();
			Field contextField = service.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(service, mockContext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		softwareComponents = new SoftwareComponents();
    }    
    
    @Given("the $filename file")
    public void givenWithNoProperty(String filename) throws FileNotFoundException{
     	MockitoAnnotations.initMocks(this);
    	try {
			Mockito.stub(context.getResourceAsStream("/META-INF/MANIFEST.MF")).toReturn(new FileInputStream("test/ncr/res/mobilepos/softwareinfo/resource/test/" + filename));
			Mockito.stub(context.getServerInfo()).toReturn("Actual");			
		} catch (Exception e) {}
    	System.setProperty("os.version", "Actual");
    	System.setProperty("os.name", "Windows 7");
    	System.setProperty("os.arch", "amd64");
    }
    
    @When("I get $software Information")
    public final void getSoftwareInfo(final String softwareName){
        if (softwareName.equals("Tomcat")) {
        	stubTomcat();
            software = service.getTomCatVersion();
        } else if (softwareName.equals("J2EE")) {
            software = service.getJavaVersion();
        } else if (softwareName.equals("WebStoreServer")) {
            software = service.getWebAPIVersion();
        } else if (softwareName.equals("OperatingSystem")) {
            software = service.getOperatingSystemInfo();    
        } else {
            software = new SoftwareVersion();
        }
    }    
    
    @When("I get all Software Information")
    public final void getAllSoftwareInfo(){       
    	softwareComponents = service.getAllSoftwareInfo();
    }  
    
    @Then("I should get all other Software Information")
    public final void shouldGetAllSoftwareInfo(){      	
    	Assert.assertNotNull(softwareComponents); 
    	
    	software = softwareComponents.getServiceVersionInfo();
    	this.shouldGetSoftwareInfo("WebStoreServer3", "s30wip");
    	
    	software = softwareComponents.getServerVersionInfo();
    	this.shouldGetSoftwareInfo("Tomcat", "Actual");
    	
    	software = softwareComponents.getJavaVersionInfo();
    	this.shouldGetSoftwareInfo("J2EE", "Actual");
    	
    	software = softwareComponents.getOperatingSystemVersionInfo();
    	this.shouldGetSoftwareInfo("OperatingSystem", "Windows 7(amd64)Actual");
    	
    }
    
    @When("I get the Service Information")
    public final void shouldGetServiceInfo(){      	
    	Assert.assertNotNull(softwareComponents);    	
    	software = softwareComponents.getServiceVersionInfo(); 
    }    
    
    
    
    @Then("I should get $expected software version information")
    public final void getNAWSSInfo(String expectedValue){
        Assert.assertNotNull(software);
        Assert.assertEquals(0, software.getNCRWSSResultCode());
        Assert.assertTrue(null != software.getName() && !software.getName().isEmpty());
        Assert.assertTrue(null != software.getVersion() && !software.getVersion().isEmpty());
        Assert.assertEquals(expectedValue, software.getVersion());
        Assert.assertEquals(expectedValue, "NA");
    }
    
    @Given("that the MANIFEST.MF file is not available in the META-INF")
    public void givenAFile() throws FileNotFoundException{
        MockitoAnnotations.initMocks(this);
    	try {
			Mockito.stub(context.getResourceAsStream("/META-INF/MANIFEST.MF")).toReturn(null);
			Mockito.stub(context.getServerInfo()).toReturn("Actual");
		} catch (Exception e) {}
        
    }
   
    
    
    private void stubTomcat(){
    	MockitoAnnotations.initMocks(this);
    	try {
			Mockito.stub(context.getServerInfo()).toReturn("Actual");
		} catch (Exception e) {}
    }
    
    @Then("I should get Software Information with name {$name}"
            + " and Version {$version}")
	public final void shouldGetSoftwareInfo(final String name, String version) {
    	try {
			assertThat(name, is(equalTo(software.getName())));
			if ((name.equals("J2EE")) && (version.equals("Actual"))) {
				version = System.getProperty("java.version");
			} else if ((name.equals("OperatingSystem"))
					&& (version.equals("Actual"))) {			
				version = System.getProperty("os.name");
			}
			assertThat(version, is(equalTo(software.getVersion())));
    	} catch (Exception e) {}	
	}
    
    @Then("I should get the software version information")
    public final void testWSSInformation(){
        Assert.assertNotNull(software);
        Assert.assertEquals(0, software.getNCRWSSResultCode());
        Assert.assertTrue(null != software.getName() && !software.getName().isEmpty());
        Assert.assertTrue(null != software.getVersion() && !software.getVersion().isEmpty());
    }
}
