package ncr.res.mobilepos.promotion.qrcodeinfo.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.SystemFileConfig;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.promotion.factory.QrCodeInfoFactory;
import ncr.res.mobilepos.promotion.model.Promotion;
import ncr.res.mobilepos.promotion.resource.PromotionResource;

public class QrCodeListSteps extends Steps {
	private PromotionResource promotionResource = null;
	private Promotion response = null;
	private DBInitiator dbInit = null;
	private SystemFileConfig systemFileConfig = null;
	private static int status = 0;
	public static final int ERROR_IOEXCEPTION = 1;
	public static final int ERROR_DAOEXCEPTION = 2;

	@BeforeScenario
    public final void setUpClass() {
		dbInit = new DBInitiator("SQLServerItemDAOSteps", DATABASE.RESMaster);
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
			promotionResource = new PromotionResource();
			Field contextField = promotionResource.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(promotionResource, context);
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/promotion/qrcodeinfo/test/mst_bizday.xml");

			SystemFileConfig sys = SystemFileConfig.initInstance(WindowsEnvironmentVariables.getInstance().getSystemPath());
			String companyId = sys.getCompanyId();
			String storeId = sys.getStoreId();

			QrCodeInfoFactory.initialize(companyId,storeId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Cant load Mock Servlet Context.");
		}
	}

    @Given("a loaded dataset $dataset")
	public final void initdatasetsDpt(final String dataset) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, dataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Given("the systemPath: $systemPath")
	public final void changeSys(final String systemPath) {
		try {
			SystemFileConfig sys = SystemFileConfig.initInstance(systemPath);
			String companyId = sys.getCompanyId();
			String storeId = sys.getStoreId();

			QrCodeInfoFactory.initialize(companyId,storeId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("check the list with companyId $companyId retailstoreid $retailstoreid workstationid $workstationid sequencenumber $sequencenumber businessDate $businessDate transaction $transaction")
	public final void getQrCodeInfo(String companyId, String retailStoreId, String workstationid, String sequencenumber, String businessDate, String transaction) throws Exception {
		status = 0;
		response = promotionResource.getQrCodeInfoList(companyId, retailStoreId, workstationid, sequencenumber, businessDate, transaction);
	}


	@Then("I should get the QrCodeInfoList : $expectedJson")
	public final void qrCodeInfoListShouldBe(final ExamplesTable expectedItems) {
		int i = 0;
		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the BMPFileCount at row", ""
					+ response.getQrCodeInfoMap().get(i).getBmpFileCount(),
					is(equalTo(expectedItem.get("BMPFileCount"))));
			assertThat("Compare the BMPFileFlag at row",
					"" + String.valueOf(response.getQrCodeInfoMap().get(i).getBmpFileFlag()),
					is(equalTo(expectedItem.get("BMPFileFlag"))));
			assertThat("Compare the BMPFileName at row ",
					response.getQrCodeInfoMap().get(i).getBmpFileName(),
					is(equalTo(expectedItem.get("BMPFileName"))));
			assertThat("Compare the MinimumPrice row ",
					"" + response.getQrCodeInfoMap().get(i).getMinimumPrice(),
					is(equalTo(expectedItem.get("MinimumPrice"))));
			assertThat("Compare the OutputType at row ", ""
					+ response.getQrCodeInfoMap().get(i).getOutputType(),
					is(equalTo(expectedItem.get("OutputType"))));
			assertThat("Compare the PromotionId at row ",
					"" + response.getQrCodeInfoMap().get(i).getPromotionId(),
					is(equalTo(expectedItem.get("PromotionId"))));
			assertThat("Compare the PromotionId at row ",
					"" + response.getQrCodeInfoMap().get(i).getPromotionName(),
					is(equalTo(expectedItem.get("PromotionName"))));
			i++;
		}
	}

	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the result code", expectedResultCode,
				response.getNCRWSSResultCode());
	}

	@Then("the emptyResult is $expectedResult")
	public final void qrCodeInfoIsNull(String expectedResult) {
		assertThat("Expect the result ",
				"" + response,
				is(equalTo(expectedResult)));
	}
}