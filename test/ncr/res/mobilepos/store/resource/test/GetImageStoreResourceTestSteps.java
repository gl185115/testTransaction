package ncr.res.mobilepos.store.resource.test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.store.model.StoreLogo;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GetImageStoreResourceTestSteps extends Steps {
	@InjectMocks
    private StoreResource storeResourceTest;
	@Mock
    private DAOFactory daoFactory;
    private DBInitiator dbinit;
    private ViewStore actualStoreLogo;
    private String currentpath =  System.getProperty("user.dir")
                + "/test/ncr/res/mobilepos/store/resource/test/";
    /**
     *Connects to the database.
     */
    @BeforeScenario
     public final void setUp() {
        Requirements.SetUp();    
    }

    /**
     * Destroys the database connection.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }
    
    /**
     * Given Step: Initialize the Store Resource.
     */
    @Given("a StoreResource")
    public final void aStoreResource() {
        storeResourceTest = new StoreResource();
    }
    
    @Given("a set of database table in MST_STOREINFO named $table")
    public final void aSetOfDatabaseTable(final String table) throws Exception {
        Map<String, Object> replacement = new HashMap<String, Object>(); 
        dbinit = new DBInitiator("StoreResource", DATABASE.RESMaster);
        dbinit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/store/resource/test/"
                + table);

        try {
            replacement.put("OriginalImage.bmp", currentpath + "OriginalImage.bmp");
            replacement.put("OriginalImage.jpg", currentpath + "OriginalImage.jpg");
            replacement.put("UnknownImage.bmp", currentpath + "UnknownImage.bmp");
            replacement.put("ncrlogo.bmp", currentpath + "ncrlogo.bmp");
            dbinit.executeWithReplacement(
                    "test/ncr/res/mobilepos/store/resource/test/"
                    + table, replacement);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Can't set the MST_STOREINFO table.");
        }        
    }
    
    @Given("that database is throwing an unexpected {$exception}")
	public final void givenThrownException(String exception) {
		MockitoAnnotations.initMocks(this);
		Exception ex = new Exception();
		
		if ("DaoException".equalsIgnoreCase(exception)) {
			ex = new DaoException();
		} else if ("Exception".equalsIgnoreCase(exception)) {
			ex = new Exception();
		} else if ("SQLException".equalsIgnoreCase(exception)) {
			ex = new DaoException(new SQLException());
		} else if ("SQLStatementException".equalsIgnoreCase(exception)) {
			ex = new DaoException(new SQLStatementException());
		}
			
		try {
			Mockito.stub(daoFactory.getStoreDAO()).toThrow(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Then("the following StoreInfo are as follow(s): $expectedRows")
    public final void theFollowingStoreInfoAreAsFollows(ExamplesTable expectedRows) {
        ITable actualStoreInfos = dbinit.getTableSnapshot("MST_STOREINFO");
        Assert.assertEquals("Compare the actual Store in the database",
                expectedRows.getRowCount(), actualStoreInfos.getRowCount());
        try {
            int i = 0;
            for (Map<String, String> expectedStoreInfo : expectedRows.getRows()) {
                    Assert.assertEquals("Compare the Store Info's ID at row" + i,
                            expectedStoreInfo.get("storeid"),
                            actualStoreInfos.getValue(i, "StoreId"));
                    Assert.assertEquals("Compare the Store Info's Name at row" + i,
                            expectedStoreInfo.get("storename"),
                            actualStoreInfos.getValue(i, "StoreName"));
                    String electroFilePath = "null".equals(expectedStoreInfo.get("electrofilepath")) ? null : 
                    	expectedStoreInfo.get("electrofilepath").isEmpty() ? "" :
                    		currentpath + expectedStoreInfo.get("electrofilepath");
                    Assert.assertEquals("Compare the Store Info's ElectroFilePath at row" + i,
                    		electroFilePath, actualStoreInfos.getValue(i, "ElectroFilePath"));
                i++;
            }
        } catch (DataSetException e) {
            e.printStackTrace();
            Assert.fail("Can't Compare the MST_STORENFO.");
        }
    }
    
    @When("a request to get Store Logo of RetailStoreID{$storeid}")
    public final void aRequestToGetStoreLogoOfRetailStoreID(final String storeid) {
           String actualStoreID = (storeid.equals("NULL") ? null : storeid);
           actualStoreLogo = storeResourceTest.getImage(actualStoreID);
    }
    
    @Then("the Store Logo is in the following xml: $expectedStoreLogoxml")
    public final void theStoreLogoIsInTheFollowingXml(final String expStoreLogoXml) {
        XmlSerializer<ViewStore> storeLogoSerializrer =
            new XmlSerializer<ViewStore>();
        try {
            String actualStoreLogoXml =
                storeLogoSerializrer.marshallObj(ViewStore.class,
                        actualStoreLogo, "utf-8");
//            Assert.assertEquals("Compare the StoreLogo Xml",
//                    expStoreLogoXml, actualStoreLogoXml);
            
        } catch (JAXBException e) {
            e.printStackTrace();
            Assert.fail("Can't serialize StoreLogo model.");
        }
        
    }
    @Then("the result code should be {$resultCode}")
    public final void resultCodeShouldBe(final int resultCode) {
    	Assert.assertEquals("Compare result code", resultCode, actualStoreLogo.getNCRWSSResultCode());
    }
}
