package ncr.res.mobilepos.cardinfo.resource.test;

import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.cardinfo.model.CardClassInfo;
import ncr.res.mobilepos.cardinfo.model.CardInfo;
import ncr.res.mobilepos.cardinfo.resource.CardInfoResource;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.ExceptionHelper;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class GetCardClassInfoSteps extends Steps {
    private DBInitiator dbInitiator;
    @InjectMocks
    private CardInfoResource cardInfoResource;
    @Mock
    private DAOFactory daoFactory;
	private CardInfo cardInfo;
    /**
     *Connects to the database.
     */
    @BeforeScenario
    public final void setUp() {
    	Requirements.SetUp();
        dbInitiator =  new DBInitiator("CardInfoResourceTest", DATABASE.RESMaster);    
        cardInfo = new CardInfo();
    }
    /**
     * Destroys the database connection.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }
    
    private String convNullStringToNull(String value) {
    	if ("null".equals(value)) {
    		return null;
    	}
    	return value;
    }
    
    @Given("I have a CardInfo resource and other resources")
    public final void givenCardInfoResource() throws Exception {   	
        cardInfoResource = new CardInfoResource();
    }
    
    @Given("a MST_CARD_CLASSINFO with data")
    public final void cardClassInfoWithData() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
    			"test/ncr/res/mobilepos/cardinfo/resource/datasets/MST_CARD_CLASSINFO.xml");
    }
    
    @Given("that database is throwing an unexpected {$exception}")
   	public final void givenThrownException(String exception) {
    	MockitoAnnotations.initMocks(this);
    	Exception ex = ExceptionHelper.getException(exception);  	
        try {
        	Mockito.stub(daoFactory.getCardInfoDAO()).toThrow(ex);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @When("I request to get card class info with companyId{$companyId}, storeId{$storeId} and cardClassId{$cardClassId}")
    public final void getCardClassInfo(String companyId, String storeId, String cardClassId, String membershipId) {
    	companyId = convNullStringToNull(companyId);
    	storeId = convNullStringToNull(storeId);
    	cardClassId = convNullStringToNull(cardClassId);
    	membershipId = convNullStringToNull(membershipId);
    	cardInfo = cardInfoResource.getCardClassInfo(companyId, storeId, cardClassId, membershipId);
    }
    
    @Then("I should have the following data: $examplesTable")
    public final void shouldHaveTheData(ExamplesTable examplesTable) throws Exception {
    	List<CardClassInfo> cardClassInfoList = cardInfo.getCardClassInfoList();
        int i = 0;
        
        assertEquals("Compare the expected count of card class info items",
            	examplesTable.getRowCount(), cardClassInfoList.size());
    	
    	for(Map<String, String> data : examplesTable.getRows()) {
            assertEquals("Compare companyId: ", data.get("companyId"),
            		cardClassInfoList.get(i).getCompanyId());
            assertEquals("Compare storeId: ", data.get("storeId"),
            		cardClassInfoList.get(i).getStoreId());
            assertEquals("Compare cardClassId: ", data.get("cardClassId"),
            		cardClassInfoList.get(i).getId());
            assertEquals("Compare cardClassName: ", data.get("cardClassName"),
            		cardClassInfoList.get(i).getName());
            assertEquals("Compare cardClassShortName: ", data.get("cardClassShortName"),
            		cardClassInfoList.get(i).getShortName());
            assertEquals("Compare creditType: ", data.get("creditType"),
            		cardClassInfoList.get(i).getCreditType());
            assertEquals("Compare mainCardDigitType: ", data.get("mainCardDigitType"),
            		cardClassInfoList.get(i).getMainCardDigitType());
            assertEquals("Compare rankType: ", data.get("rankType"),
            		cardClassInfoList.get(i).getRankType());
            i++;
    	}
    }
    
    @Then("I should have an empty result list")
    public final void haveEmptyResultList() {
    	assertTrue(cardInfo.getCardClassInfoList().isEmpty());
    }
    
    @Then("the result should be {$result}")
    public final void testResultCode(String result) {
    	 assertEquals(ResultBaseHelper.getErrorCode(result), cardInfo.getNCRWSSResultCode());
    }
}
