package ncr.res.mobilepos.tillinfo.resource.test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

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
import org.powermock.api.support.membermodification.MemberModifier;

import junit.framework.Assert;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.tillinfo.dao.SQLServerTillInfoDAO;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

@SuppressWarnings("deprecation")
public class GetUnclosedTillSteps extends Steps {
	private TillInfoResource tillInfoResource;
	private ViewTill resultList;
	private DBInitiator dbInitMaster;

	@BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }

    @Given("a TillInfoResource Resource")
    public final void createResource() {
        ServletContext context = Requirements.getMockServletContext();
        tillInfoResource = new TillInfoResource();
        try {
            Field tillContext = tillInfoResource.getClass().getDeclaredField("servletContext");
            tillContext.setAccessible(true);
            tillContext.set(tillInfoResource, context);
        } catch (Exception e) {
            Assert.fail("Cant load Mock Servlet Context.");
        }
    }

    @Given("a RESMaster DBInitiator")
    public final void createDBInitiator() {
        dbInitMaster = new DBInitiator("GetTillListSteps", DATABASE.RESMaster);
    }

    @Given("a $dataset dataset")
    public final void insertDatabase(final String dataset) {
    	try {
	    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
	    				"test/ncr/res/mobilepos/tillinfo/resource/test/"
	    				+ dataset + ".xml");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    @When("I get activated tills with companyId:$companyId storeId:$storeId businessDate:$businessDate trainingFlag:$trainingFlag")
    public final void execGetActivatedTills(String companyId, String storeId, String businessDate, String trainingFlag) {
    	companyId = StringUtility.convNullOrEmptryString(companyId);
		storeId = StringUtility.convNullOrEmptryString(storeId);
		businessDate = StringUtility.convNullOrEmptryString(businessDate);
		trainingFlag = StringUtility.convNullOrEmptryString(trainingFlag);
    	resultList = tillInfoResource.getActivatedTillsOnBusinessDay(companyId, storeId, businessDate, trainingFlag);
    }

	@When("I get unclosed tills with companyId:$companyId storeId:$storeId businessDate:$businessDate trainingFlag:$trainingFlag")
	public final void execGetUnclosedTills(String companyId, String storeId, String businessDate, String trainingFlag) {
		companyId = StringUtility.convNullOrEmptryString(companyId);
		storeId = StringUtility.convNullOrEmptryString(storeId);
		businessDate = StringUtility.convNullOrEmptryString(businessDate);
		trainingFlag = StringUtility.convNullOrEmptryString(trainingFlag);
		resultList = tillInfoResource.getUnclosedTillsOnBusinessDay(companyId, storeId, businessDate, trainingFlag);
	}

	@When("database has trouble")
	public final void makeBrokenDatasource() throws DaoException, IllegalAccessException {
		DAOFactory daoFactoryMock = mock(DAOFactory.class);
		SQLServerTillInfoDAO daoMock = mock(SQLServerTillInfoDAO.class);
		when(daoFactoryMock.getTillInfoDAO()).thenReturn(daoMock);
		when(daoMock.getActivatedTillsOnBusinessDay(anyString(),anyString(),anyString(),anyInt())).thenThrow(new DaoException());
		when(daoMock.getUnclosedTillsOnBusinessDay(anyString(),anyString(),anyString(),anyInt())).thenThrow(new DaoException());
		Field daoFactoryField = MemberModifier.field(TillInfoResource.class, "daoFactory");
		daoFactoryField.set(tillInfoResource, daoFactoryMock);
	}

	@When("runtime exception is thrown")
	public final void throwRuntimeException() throws DaoException, IllegalAccessException {
		DAOFactory daoFactoryMock = mock(DAOFactory.class);
		SQLServerTillInfoDAO daoMock = mock(SQLServerTillInfoDAO.class);
		when(daoFactoryMock.getTillInfoDAO()).thenReturn(daoMock);
		when(daoMock.getActivatedTillsOnBusinessDay(anyString(),anyString(),anyString(),anyInt())).thenThrow(new NullPointerException());
		when(daoMock.getUnclosedTillsOnBusinessDay(anyString(),anyString(),anyString(),anyInt())).thenThrow(new NullPointerException());
		Field daoFactoryField = MemberModifier.field(TillInfoResource.class, "daoFactory");
		daoFactoryField.set(tillInfoResource, daoFactoryMock);
	}

	@Then("the result message should be $message")
	public final void resultCodeShouldBe(final String message) {
		assertEquals(ResultBaseHelper.getErrorMessage(message), resultList.getMessage());
	}

	@Then("the result should be ResultCode:$resultCode and ExtendedResultCode:$extendedResultCode")
	public final void resultCodeShouldBe(final String resultCode,
										 final String extendedResultCode) {
		assertEquals(ResultBaseHelper.getErrorCode(resultCode), resultList.getNCRWSSResultCode());
		assertEquals(ResultBaseHelper.getErrorCode(extendedResultCode), resultList.getNCRWSSExtendedResultCode());
	}

    @Then("I should get the following tills: $rows")
    public final void shouldGetTheTills(ExamplesTable rows) {
        assertEquals("Compare the number of result", rows.getRowCount(), resultList.getTillList().size());
        try {
        	int i = 0;
	        for (Map<String, String> row : rows.getRows()) {
				assertEquals("Compare CompanyId at row " + i,
						row.get("CompanyId"), resultList.getTillList().get(i).getCompanyId());
	            assertEquals("Compare StoreId at row " + i,
		            		row.get("StoreId"), resultList.getTillList().get(i).getStoreId());
	            assertEquals("Compare TillId at row " + i,
	            		row.get("TillId"), resultList.getTillList().get(i).getTillId());
	            assertEquals("Compare TerminalId at row " + i,
	            		row.get("TerminalId"), resultList.getTillList().get(i).getTerminalId());
	            assertEquals("Compare BusinessDayDate at row " + i,
	            		row.get("BusinessDayDate"), resultList.getTillList().get(i).getBusinessDayDate());
	            assertEquals("Compare SodFlag at row " + i,
	            		row.get("SodFlag"), resultList.getTillList().get(i).getSodFlag());
	            assertEquals("Compare EodFlag at row " + i,
	            		row.get("EodFlag"), resultList.getTillList().get(i).getEodFlag());
	            i++;
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

}
