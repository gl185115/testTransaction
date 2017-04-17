package ncr.res.mobilepos.giftcard.test;

import java.io.File;
import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import junit.framework.Assert;

import ncr.res.giftcard.toppan.model.Config;
import ncr.res.mobilepos.giftcard.resource.ToppanGiftcardResource;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.model.ResultBase;

@SuppressWarnings("deprecation")
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
	
	@Then("I should get the result: $expectedJson")
	public final void giftCardGetData(int expectedJson) {
		Assert.assertEquals("Expect the  result code", expectedJson,
				giftCardResultBase.getNCRWSSResultCode());
	}
}