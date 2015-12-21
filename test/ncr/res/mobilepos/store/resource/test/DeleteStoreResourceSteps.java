package ncr.res.mobilepos.store.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DeleteStoreResourceSteps extends Steps {
	@InjectMocks
    private StoreResource strResource;
	@Mock
	private DAOFactory daoFactory;
    private ResultBase resultBase;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("I have a Store Resource")
    public final void IHaveAStoreResource(){
        strResource = new StoreResource();    
    }

    @Then("the result should be {$Result}")
    public final void checkResult(final int Result){
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(Result)));
    }
    
    @Given ("a Store Table")
    public final void emptyTable(){
        DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/store/resource/test/"
                + "MST_STOREINFO_forDelete.xml", DATABASE.RESMaster);
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
    
    @When ("I delete a store ($storeid)")
    public final void delStore(final String storeid) {
        resultBase = strResource.deleteStore(storeid);
    }
    
}
