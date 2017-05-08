package ncr.res.mobilepos.promotion.qrcodeinfo.test;

import junit.framework.Assert;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.promotion.factory.QrCodeInfoFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QrCodeSteps extends Steps {
	private QrCodeInfoFactory qrCodeInfoFactory = null;
	private List<QrCodeInfo> QrCodeList = null;
	private DBInitiator dbInit = null;
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
			qrCodeInfoFactory = new QrCodeInfoFactory();
			Field contextField = qrCodeInfoFactory.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(qrCodeInfoFactory, context);
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/promotion/qrcodeinfo/test/mst_bizday.xml");
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
	
    
	@When("the tomcat startUp with systemPath $systemPath")
	public final void getQrCodeInfo(String systemPath) throws Exception {
		try{
			QrCodeList = qrCodeInfoFactory.initialize(systemPath);
		} catch(IOException e) {
			status = ERROR_IOEXCEPTION;
		} catch (DaoException e) {
			status = ERROR_DAOEXCEPTION;
		}
	}
	
	
	@Then("I should get the QrCodeInfo : $expectedJson")
	public final void qrCodeInfoShouldBe(final ExamplesTable expectedItems) {
		int i = 0;
		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the PromotionId at row ",
					"" + QrCodeList.get(i).getPromotionId(),
					is(equalTo(expectedItem.get("PromotionId"))));
			assertThat("Compare the MinimumPrice row ", 
					"" + QrCodeList.get(i).getMinimumPrice(),
					is(equalTo(expectedItem.get("MinimumPrice"))));
			assertThat("Compare the OutputTargetValue at row ",
					"" + QrCodeList.get(i).getOutputTargetValue(),
					is(equalTo(expectedItem.get("OutputTargetValue"))));
			assertThat("Compare the BMPFileName at row ",
					QrCodeList.get(i).getBmpFileName(),
					is(equalTo(expectedItem.get("BMPFileName"))));
			assertThat("Compare the BMPFileFlag at row",
					"" + String.valueOf(QrCodeList.get(i).getBmpFileFlag()),
					is(equalTo(expectedItem.get("BMPFileFlag"))));
			assertThat("Compare the BMPFileCount at row", ""
					+ QrCodeList.get(i).getBmpFileCount(),
					is(equalTo(expectedItem.get("BMPFileCount"))));
			assertThat("Compare the OutputType at row ", ""
					+ QrCodeList.get(i).getOutputType(),
					is(equalTo(expectedItem.get("OutputType"))));
			assertThat("Compare the DisplayOrder row ",
					"" + QrCodeList.get(i).getDisplayOrder(),
					is(equalTo(expectedItem.get("DisplayOrder"))));
			i++;
		}
	}
	
	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the  result code", expectedResultCode,
				status);
	}
}