package ncr.res.mobilepos.daofactory;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ncr.res.mobilepos.exception.DaoException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class JndiDBManagerMSSqlServerSteps extends Steps {

    @BeforeScenario
    public void setUpInitialContext() throws NamingException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

        InitialContext initContext = new InitialContext();
        initContext.createSubcontext("java:");
        initContext.createSubcontext("java:comp");
        initContext.createSubcontext("java:comp/env");
        initContext.createSubcontext("java:comp/env/jdbc");
    }

    @AfterScenario
    public void cleanUpInitialContext() throws NamingException {
        InitialContext initContext = new InitialContext();
        initContext.destroySubcontext("java:comp/env/jdbc");
        initContext.destroySubcontext("java:comp/env");
        initContext.destroySubcontext("java:comp");
        initContext.destroySubcontext("java:");
    }

    @When("DataSource is properly bound to InitialContext")
    public void ifDataSourceRegisteredProperly() throws NamingException {
        DataSource dataSource = mock(DataSource.class);
        InitialContext initContext = new InitialContext();
        initContext.bind("java:comp/env/jdbc/MSSQLSERVER", dataSource);
    }

    @When("DataSource is not bound")
    public void ifDataSourceIsNotRegisteredProperly() throws NamingException {
    }

    @Then("DBManager doesn't throw DaoException")
    public void initializedSuccessfully() {
            DBManager dbManager = null;
            try {
                dbManager = JndiDBManagerMSSqlServer.initialize();
            } catch(DaoException dao) {
                fail("No exception expected here.");
            }
            assertSame(dbManager, JndiDBManagerMSSqlServer.getInstance());
        }

    @Then("DBManager throws DaoException caused by NamingException")
    public void initializedFailure() throws Exception {
        try {
            DBManager dbManager = JndiDBManagerMSSqlServer.initialize();
            fail("Dao exception is expected.");
        } catch(DaoException dao) {
            assertEquals("NamingException: Unable to find datasource", dao.getMessage());
            assertTrue(dao.getCause() instanceof NamingException);
        }
        assertNull(JndiDBManagerMSSqlServer.getInstance());
    }

}