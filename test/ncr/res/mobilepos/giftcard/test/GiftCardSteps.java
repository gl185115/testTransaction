package ncr.res.mobilepos.giftcard.test;

import junit.framework.Assert;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberModifier;

import java.io.File;
import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import ncr.res.giftcard.toppan.dao.CenterAccess;
import ncr.res.giftcard.toppan.model.Config;
import ncr.res.giftcard.toppan.model.Message;
import ncr.res.mobilepos.giftcard.model.GiftResult;
import ncr.res.mobilepos.giftcard.resource.ToppanGiftcardResource;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;

import static org.mockito.Matchers.anyObject;

public class GiftCardSteps extends Steps {
	private ToppanGiftcardResource toppanGiftcardResource = null;
	private GiftResult giftResult = null;
	
	@BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
        initResources();
    }
	
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }
    
    private void initResources() {
		ServletContext context = Requirements.getMockServletContext();
		try {
			toppanGiftcardResource = new ToppanGiftcardResource();
			Field contextField = toppanGiftcardResource.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(toppanGiftcardResource, context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Cant load Mock Servlet Context.");
		}
	}
	
    @Given("toppan-gift.xml file does not exist.")
    public void giftXmlNotFound() {
    	Field toppanGiftcardConfigField;
		try {
			toppanGiftcardConfigField = toppanGiftcardResource.getClass().getDeclaredField("toppanGiftcardConfig");
	    	toppanGiftcardConfigField.setAccessible(true);
	    	toppanGiftcardConfigField.set(toppanGiftcardResource, null);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Given("toppan-gift.xml file exists.")
    public void giftXmlFound() {
    	Field toppanGiftcardConfigField;
		try {
			File configFile = new File("test\\ncr\\res\\mobilepos\\giftcard\\test" + File.separator + Config.FILENAME);
			XmlSerializer<Config> serializer = new XmlSerializer<Config>();
			Config config = serializer.unMarshallXml(configFile, Config.class);
			
			toppanGiftcardConfigField = toppanGiftcardResource.getClass().getDeclaredField("toppanGiftcardConfig");
	    	toppanGiftcardConfigField.setAccessible(true);
	    	toppanGiftcardConfigField.set(toppanGiftcardResource, config);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	@When("I test queryMember with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardQuery(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		storeId = StringUtility.convNullOrEmptryString(storeId);
		workstationId = StringUtility.convNullOrEmptryString(workstationId);
		transactionId = StringUtility.convNullOrEmptryString(transactionId);
		giftcard = StringUtility.convNullOrEmptryString(giftcard);
		
		giftResult = toppanGiftcardResource.queryMember(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test sales with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardSales(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		storeId = StringUtility.convNullOrEmptryString(storeId);
		workstationId = StringUtility.convNullOrEmptryString(workstationId);
		transactionId = StringUtility.convNullOrEmptryString(transactionId);
		giftcard = StringUtility.convNullOrEmptryString(giftcard);
		
		giftResult = toppanGiftcardResource.sales(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test cancel with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardCancel(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		storeId = StringUtility.convNullOrEmptryString(storeId);
		workstationId = StringUtility.convNullOrEmptryString(workstationId);
		transactionId = StringUtility.convNullOrEmptryString(transactionId);
		giftcard = StringUtility.convNullOrEmptryString(giftcard);
		
		giftResult = toppanGiftcardResource.cancel(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test activate with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardActivate(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		storeId = StringUtility.convNullOrEmptryString(storeId);
		workstationId = StringUtility.convNullOrEmptryString(workstationId);
		transactionId = StringUtility.convNullOrEmptryString(transactionId);
		giftcard = StringUtility.convNullOrEmptryString(giftcard);
		
		giftResult = toppanGiftcardResource.activate(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test charge with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardCharge(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		storeId = StringUtility.convNullOrEmptryString(storeId);
		workstationId = StringUtility.convNullOrEmptryString(workstationId);
		transactionId = StringUtility.convNullOrEmptryString(transactionId);
		giftcard = StringUtility.convNullOrEmptryString(giftcard);
		
		giftResult = toppanGiftcardResource.charge(storeId, workstationId, transactionId, test, giftcard);
	}

	@Given("I assume external library, CenterAccess returns normal result.")
	public final void mockCenterAccess() throws IllegalAccessException {
		try {
			Message mockResponseMessage = Mockito.mock(Message.class);
			CenterAccess mockCenterAccess = Mockito.mock(CenterAccess.class);

			Mockito.when(mockCenterAccess.send(anyObject())).thenReturn(mockResponseMessage);

			Mockito.when(mockResponseMessage.getPriorAmount()).thenReturn("100");
			Mockito.when(mockResponseMessage.getCurrentAmount()).thenReturn("200");
			Mockito.when(mockResponseMessage.getErrorCode()).thenReturn("0");
			Mockito.when(mockResponseMessage.getSubErrorCode()).thenReturn("0");

			Mockito.when(mockResponseMessage.getAuthNumber()).thenReturn("01");
			Mockito.when(mockResponseMessage.getExpiration()).thenReturn("2017-04-18");
			Mockito.when(mockResponseMessage.getCardStatus()).thenReturn("0000");
			
			Field centerAccessField = toppanGiftcardResource.getClass().getDeclaredField("centerAccess");
			centerAccessField.setAccessible(true);
			centerAccessField.set(toppanGiftcardResource, mockCenterAccess);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Then("I should get the result: $expectedJson")
	public final void giftCardGetData(final int expectedJson) {
		Assert.assertEquals("Expect the  result code", expectedJson,
				giftResult.getNCRWSSResultCode());
	}
	
	@Then("I should get the GiftResult : $examplesResult")
	public final void giftCardGetData(final ExamplesTable examplesResult) {
		Assert.assertEquals("Expect the  result code", 0, giftResult.getNCRWSSResultCode());
		Assert.assertEquals("Expect the PriorAmount", examplesResult.getRow(0).get("PriorAmount"),
				String.valueOf(giftResult.getPriorAmount()));
		Assert.assertEquals("Expect the CurrentAmount", examplesResult.getRow(0).get("CurrentAmount"),
				String.valueOf(giftResult.getCurrentAmount()));
		Assert.assertEquals("Expect the ErrorCode", examplesResult.getRow(0).get("ErrorCode"),
				giftResult.getErrorCode());
		Assert.assertEquals("Expect the SubErrorCode", examplesResult.getRow(0).get("SubErrorCode"),
				giftResult.getSubErrorCode());
		Assert.assertEquals("Expect the AuthorizationNumber", examplesResult.getRow(0).get("AuthorizationNumber"),
				giftResult.getAuthorizationNumber());
		Assert.assertEquals("Expect the ExpirationDate", examplesResult.getRow(0).get("ExpirationDate"),
				giftResult.getExpirationDate());
		Assert.assertEquals("Expect the ActivationStatus", examplesResult.getRow(0).get("ActivationStatus"),
				String.valueOf(giftResult.getActivationStatus()));
		Assert.assertEquals("Expect the ExpirationStatus", examplesResult.getRow(0).get("ExpirationStatus"),
				String.valueOf(giftResult.getExpirationStatus()));
		Assert.assertEquals("Expect the LostStatus", examplesResult.getRow(0).get("LostStatus"),
				String.valueOf(giftResult.getLostStatus()));
	}
}