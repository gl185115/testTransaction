package ncr.res.mobilepos.softwareinfo.resource.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResourceLoader;

import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.softwareinfo.resource.SoftwareInfo;
import ncr.res.mobilepos.softwareinfo.model.SoftwareComponents;

public class SoftwareInfoSteps extends Steps {
	@InjectMocks
    private SoftwareInfo service;
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
    	System.setProperty("java.version", "Actual");
    }
    
    @Given("that the MANIFEST.MF file is not available in the META-INF")
    public void givenAFile() throws FileNotFoundException{
        MockitoAnnotations.initMocks(this);
    	try {
			Mockito.stub(context.getResourceAsStream("/META-INF/MANIFEST.MF")).toReturn(null);
			Mockito.stub(context.getServerInfo()).toReturn("Actual");
		} catch (Exception e) {}
    	System.setProperty("os.version", "Actual");
    	System.setProperty("os.name", "Windows 7");
    	System.setProperty("os.arch", "amd64");
    	System.setProperty("java.version", "Actual");
    }
   
    @When("I get all Software Information")
    public final void getAllSoftwareInfo(){       
    	softwareComponents = service.getAllSoftwareInfo();
    }  
    
    @Then("I should get the following software version: $expected")
    public final void shouldGetAllSoftwareInfo(ExamplesTable expected){      	
    	Assert.assertNotNull(softwareComponents); 
		for (Map<String, String> data : expected.getRows()) {
			if (data.get("software").equalsIgnoreCase("java")) {
				Assert.assertEquals("Compare java name", data.get("name"),
						softwareComponents.getJavaVersionInfo().getName());
				Assert.assertEquals("Compare java version",
						data.get("version"), softwareComponents
								.getJavaVersionInfo().getVersion());
			} else if (data.get("software").equalsIgnoreCase("service")) {
				Assert.assertEquals("Compare service name", data.get("name"),
						softwareComponents.getServiceVersionInfo().getName());
				Assert.assertEquals("Compare service version", data
						.get("version"), softwareComponents
						.getServiceVersionInfo().getVersion());
			} else if (data.get("software").equalsIgnoreCase("server")) {
				Assert.assertEquals("Compare server name", data.get("name"),
						softwareComponents.getServerVersionInfo().getName());
				Assert.assertEquals("Compare server version", data
						.get("version"), softwareComponents
						.getServerVersionInfo().getVersion());
			} else if (data.get("software").equalsIgnoreCase("os")) {
				Assert.assertEquals("Compare os name", data.get("name"),
						softwareComponents.getOperatingSystemVersionInfo()
								.getName());
				Assert.assertEquals("Compare os version", data.get("version"),
						softwareComponents.getOperatingSystemVersionInfo()
								.getVersion());
			}
		}
    }
}
