package ncr.res.mobilepos.initialization.resource;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Field;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import ncr.res.giftcard.toppan.model.Config;
import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.giftcard.factory.ToppanGiftCardConfigFactory;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.SpmFileWriter;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.promotion.factory.QrCodeInfoFactory;
import ncr.res.mobilepos.property.SQLStatement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InitializationStatusResourceSteps extends Steps {
    /**
     * ResultBase object.
     */
    private ResultBase resultBase;
    /**
     * StatusCheck API
     */
    InitializationStatusResource testInitStatusResource;

    private DBInitiator dbInit;

    @BeforeScenario
    public final void SetUpClass() throws Exception {
        Requirements.SetUp();
        testInitStatusResource = new InitializationStatusResource();
    }

    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("Web application was initialized")
    public void initInstance() throws NamingException {
        InitialContext initContext = new InitialContext();
        ServletContext servletContext = Requirements.getMockServletContext();
        EnvironmentEntries env = EnvironmentEntries.getInstance();
    }

    @Given("Business logic factories were initialized")
    public void initBusinessLogicFactories() throws Exception {
        // Mocks BarcodeAssignment.
        BarcodeAssignment baMock = mock(BarcodeAssignment.class);
        Field barcodeAssignmentInstance = BarcodeAssignmentFactory.class.getDeclaredField("instance");
        barcodeAssignmentInstance.setAccessible(true);
        barcodeAssignmentInstance.set(null, baMock);

        // Mocks Config for GiftCard.
        Config giftCardConfigMock = mock(Config.class);
        Field giftCardConfigInstance = ToppanGiftCardConfigFactory.class.getDeclaredField("instance");
        giftCardConfigInstance.setAccessible(true);
        giftCardConfigInstance.set(null, giftCardConfigMock);

        // Mocks BarcodeAssignment.
        List<QrCodeInfo> qrCodeInfosMock = mock(List.class);
        Field qrCodeInfosInstance = QrCodeInfoFactory.class.getDeclaredField("instance");
        qrCodeInfosInstance.setAccessible(true);
        qrCodeInfosInstance.set(null, qrCodeInfosMock);
    }


    @Given("$className failed to instantiate")
    public void initFailureEnvironmentalVariable(final String className) throws IllegalAccessException {
        Class targetClass = null;
        Field targetField = null;
        switch(className) {
            case "EnvironmentEntries":
                targetClass = EnvironmentEntries.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;

            case "Logger":
                targetClass = Logger.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;

            case "SnapLogger":
                targetClass = SnapLogger.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;

            case "DebugLogger":
                targetClass = DebugLogger.class;
                targetField = PowerMockito.field(targetClass, "isInitialized");
                targetField.set(null, false);
                break;

            case "SpmFileWriter":
                targetClass = SpmFileWriter.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;

            case "SQLStatement":
                targetClass = SQLStatement.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;

            case "JndiDBManagerMSSqlServer":
                targetClass = JndiDBManagerMSSqlServer.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;

            case "ToppanGiftCardConfigFactory":
                targetClass = ToppanGiftCardConfigFactory.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;
            case "QrCodeInfoFactory":
                targetClass = QrCodeInfoFactory.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;
            case "BarcodeAssignmentFactory":
                targetClass = BarcodeAssignmentFactory.class;
                targetField = PowerMockito.field(targetClass, "instance");
                targetField.set(null, null);
                break;
        }
    }

    @When("the status check API are called")
    public void statusCheck() {
        resultBase  =testInitStatusResource.getInitializationStatus();
    }

    /**
     * Method to test the value of resultCode and extendedResultCode.
     * @param resultCode - the result code
     * @param extendedResultCode - the extended result code
     */
    @Then("the resultcode should be $resultCode and "
            + "extendedResultCode should be $extendedResultCode and "
            + "message should be $message")
    public final void resultCodeShouldBe(final String resultCode,
                                         final String extendedResultCode,
                                        final String message) {
        assertEquals(ResultBaseHelper.getErrorCode(resultCode), resultBase.getNCRWSSResultCode());
        assertEquals(ResultBaseHelper.getErrorCode(extendedResultCode), resultBase.getNCRWSSExtendedResultCode());
        assertEquals(ResultBaseHelper.getErrorMessage(message), resultBase.getMessage());
    }

}