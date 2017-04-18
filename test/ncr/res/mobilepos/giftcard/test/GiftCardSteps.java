package ncr.res.mobilepos.giftcard.test;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.Assert;
import ncr.res.giftcard.toppan.dao.CenterAccess;
import ncr.res.giftcard.toppan.model.Message;
import ncr.res.mobilepos.giftcard.resource.ToppanGiftcardResource;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ToppanGiftcardResource.class)
public class GiftCardSteps extends Steps {
	private ToppanGiftcardResource toppanGiftcardResource = null;
	private ResultBase giftCardResultBase = null;
	
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
    
	@When("I test QueryMember with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardQuery(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		if (storeId.equals("NULL")) {
			storeId = null;
		}
		if (workstationId.equals("NULL")) {
			workstationId = null;
		}
		if (transactionId.equals("NULL")) {
			transactionId = null;
		}
		if (giftcard.equals("NULL")) {
			giftcard = null;
		}
		
		giftCardResultBase = toppanGiftcardResource.QueryMember(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test sales with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardSales(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		if (storeId.equals("NULL")) {
			storeId = null;
		}
		if (workstationId.equals("NULL")) {
			workstationId = null;
		}
		if (transactionId.equals("NULL")) {
			transactionId = null;
		}
		if (giftcard.equals("NULL")) {
			giftcard = null;
		}
		
		giftCardResultBase = toppanGiftcardResource.sales(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test cancel with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void giftCardCancel(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		if (storeId.equals("NULL")) {
			storeId = null;
		}
		if (workstationId.equals("NULL")) {
			workstationId = null;
		}
		if (transactionId.equals("NULL")) {
			transactionId = null;
		}
		if (giftcard.equals("NULL")) {
			giftcard = null;
		}
		
		giftCardResultBase = toppanGiftcardResource.cancel(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test Mockito QueryMember with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void mockitoGiftCardQuery(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		if (storeId.equals("NULL")) {
			storeId = null;
		}
		if (workstationId.equals("NULL")) {
			workstationId = null;
		}
		if (transactionId.equals("NULL")) {
			transactionId = null;
		}
		if (giftcard.equals("NULL")) {
			giftcard = null;
		}
		
		Message mockRequestMessage = PowerMockito.mock(Message.class);
	    Message mockResponseMessage = PowerMockito.mock(Message.class);
		CenterAccess mockCenterAccess = PowerMockito.mock(CenterAccess.class);
		
		try {
			PowerMockito.whenNew(CenterAccess.class).withAnyArguments().thenReturn(mockCenterAccess);
			PowerMockito.when(mockCenterAccess.send(mockRequestMessage)).thenReturn(mockResponseMessage);
			
			PowerMockito.when(mockResponseMessage.getPriorAmount()).thenReturn("100");
			PowerMockito.when(mockResponseMessage.getCurrentAmount()).thenReturn("200");
			PowerMockito.when(mockResponseMessage.getErrorCode()).thenReturn("0");
			PowerMockito.when(mockResponseMessage.getSubErrorCode()).thenReturn("0");

			PowerMockito.when(mockResponseMessage.getAuthNumber()).thenReturn("01");
			PowerMockito.when(mockResponseMessage.getExpiration()).thenReturn("2017-04-18");
			PowerMockito.when(mockResponseMessage.getCardStatus()).thenReturn("0000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		giftCardResultBase = toppanGiftcardResource.QueryMember(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test Mockito sales with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void mockitoGiftCardSales(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		if (storeId.equals("NULL")) {
			storeId = null;
		}
		if (workstationId.equals("NULL")) {
			workstationId = null;
		}
		if (transactionId.equals("NULL")) {
			transactionId = null;
		}
		if (giftcard.equals("NULL")) {
			giftcard = null;
		}
		
		Message mockRequestMessage = PowerMockito.mock(Message.class);
	    Message mockResponseMessage = PowerMockito.mock(Message.class);
		CenterAccess mockCenterAccess = PowerMockito.mock(CenterAccess.class);
		
		try {
			PowerMockito.whenNew(CenterAccess.class).withAnyArguments().thenReturn(mockCenterAccess);
			PowerMockito.when(mockCenterAccess.send(mockRequestMessage)).thenReturn(mockResponseMessage);
			
			PowerMockito.when(mockResponseMessage.getPriorAmount()).thenReturn("100");
			PowerMockito.when(mockResponseMessage.getCurrentAmount()).thenReturn("200");
			PowerMockito.when(mockResponseMessage.getErrorCode()).thenReturn("0");
			PowerMockito.when(mockResponseMessage.getSubErrorCode()).thenReturn("0");

			PowerMockito.when(mockResponseMessage.getAuthNumber()).thenReturn("01");
			PowerMockito.when(mockResponseMessage.getExpiration()).thenReturn("2017-04-18");
			PowerMockito.when(mockResponseMessage.getCardStatus()).thenReturn("0000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		giftCardResultBase = toppanGiftcardResource.sales(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@When("I test Mockito cancel with storeid $storeid workstationid $workstationid transactionid $transactionid test $test giftcard $giftcard")
	public final void mockitoGiftCardCancel(
			String storeId, String workstationId, String transactionId,
			boolean test, String giftcard) {
		if (storeId.equals("NULL")) {
			storeId = null;
		}
		if (workstationId.equals("NULL")) {
			workstationId = null;
		}
		if (transactionId.equals("NULL")) {
			transactionId = null;
		}
		if (giftcard.equals("NULL")) {
			giftcard = null;
		}
		
		Message mockRequestMessage = PowerMockito.mock(Message.class);
	    Message mockResponseMessage = PowerMockito.mock(Message.class);
		CenterAccess mockCenterAccess = PowerMockito.mock(CenterAccess.class);
		
		try {
			PowerMockito.whenNew(CenterAccess.class).withAnyArguments().thenReturn(mockCenterAccess);
			PowerMockito.when(mockCenterAccess.send(mockRequestMessage)).thenReturn(mockResponseMessage);
			
			PowerMockito.when(mockResponseMessage.getPriorAmount()).thenReturn("100");
			PowerMockito.when(mockResponseMessage.getCurrentAmount()).thenReturn("200");
			PowerMockito.when(mockResponseMessage.getErrorCode()).thenReturn("0");
			PowerMockito.when(mockResponseMessage.getSubErrorCode()).thenReturn("0");

			PowerMockito.when(mockResponseMessage.getAuthNumber()).thenReturn("01");
			PowerMockito.when(mockResponseMessage.getExpiration()).thenReturn("2017-04-18");
			PowerMockito.when(mockResponseMessage.getCardStatus()).thenReturn("0000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		giftCardResultBase = toppanGiftcardResource.cancel(storeId, workstationId, transactionId, test, giftcard);
	}
	
	@Then("I should get the result: $expectedJson")
	public final void giftCardGetData(int expectedJson) {
		Assert.assertEquals("Expect the  result code", expectedJson,
				giftCardResultBase.getNCRWSSResultCode());
	}
}