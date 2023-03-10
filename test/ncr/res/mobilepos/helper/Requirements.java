package ncr.res.mobilepos.helper;

import junit.framework.Assert;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;

import java.lang.reflect.Field;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.SystemFileConfig;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.systemconfiguration.property.WebContextListener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("deprecation")
public class Requirements {

    /** The mock servlet context. */
    private static MockServletContext servletContext = new MockServletContext("");    
    private static WebContextListener listener = null;
    private static ServletContextEvent servletContextEvent = null;
    private static InitialContext initContext = null;
    
    /**
     * Gets the mock servlet context.
     * This doesn't initialize business logic factories.
     * @return the mock servlet context
     */
    public static ServletContext getMockServletContext() {
        servletContext.addInitParameter("AESKeyStorePath",
                "test/ncr/res/mobilepos/creditauthorization/helper/"
                        + "test/AES_MasterKey.keystore");
        servletContext.addInitParameter("AESKeyStorePass", "changeit");
        servletContext.addInitParameter("AESKeyStoreKeyAlias", "AESKey");
        servletContext.addInitParameter("AESKeyStoreGenDateAlias",
                "AESKeyStoreGenDateAlias");
        servletContextEvent = new ServletContextEvent(servletContext);
        listener = new WebContextListener();
        initializeWebServiceForTest();
        return servletContext;
    }

    public static void initializeWebServiceForTest() {
        try {
            mockWindowsEnvironmentVariables();
            EnvironmentEntries.initInstance(new InitialContext());
            SystemFileConfig.initInstance(WindowsEnvironmentVariables.getInstance().getSystemPath());
            BarcodeAssignmentFactory.initialize("test\\resources\\para");
            listener.initializeLoggers();
            listener.initializeDBInstances();
            listener.preloadDBRecord();

        } catch (Exception e) {
            // In case Logger is failed to initialize.
            Assert.fail("resTransaction failed to initialize caused by:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Mocks WindowsEnvironmentVariables.
     * Since System.getenv is hard to mock, then mock the container instead.
     */
    public static void mockWindowsEnvironmentVariables() {
        try {
            // WindowsEnvironmentalVariables.
            WindowsEnvironmentVariables environmentVariable = mock(WindowsEnvironmentVariables.class);
            Field fieldContext = WindowsEnvironmentVariables.class.getDeclaredField("instance");
            fieldContext.setAccessible(true);
            fieldContext.set(null, environmentVariable);

            // Mock the variables.
            when(environmentVariable.getSystemPath()).thenReturn("test/resources/sys/normal");
        } catch(NoSuchFieldException | IllegalAccessException ex) {
            Assert.fail("Mocking failed for WindowsEnvironmentVariables.");
        }
    }

    /**
     * Stops the server.
     */
    public static void ContextDestroyed(){
    	listener.contextDestroyed(servletContextEvent);
    }

    /**
     * Sets the mock up servlet context.
     *
     * @param resourceBasePath the resource base path
     * @param resourceLoader the resource loader
     */
    public static void setMockUpServletContext(String resourceBasePath,
            ResourceLoader resourceLoader) {
        servletContext = new MockServletContext(resourceBasePath,
                resourceLoader);
    }

    /**
     * Setting up initial context.
     */
    public static void SetUp() {
        try {
            initContext = new InitialContext();
            initContext.lookup("java:comp/env/jdbc");
        } catch (NamingException e1) {
            try {
                System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                        "org.apache.naming.java.javaURLContextFactory");
                System.setProperty(Context.URL_PKG_PREFIXES,
                        "org.apache.naming");

                initContext = new InitialContext();
                initContext.createSubcontext("java:");
                initContext.createSubcontext("java:comp");
                initContext.createSubcontext("java:comp/env");
                initContext.createSubcontext("java:comp/env/jdbc");
                initContext.createSubcontext("java:comp/env/Journalization");

                initContext.bind("java:comp/env/serverID", "A010");
                initContext.bind("java:comp/env/debugLevel", 2);
                initContext.bind("java:comp/env/Printpaperlength", "2");
                initContext.bind("java:comp/env/customMaintenanceBasePath", "c:/ncr/res/custom/");
                initContext.bind("java:comp/env/customResourceBasePath", "c:/ncr/res/custom/");

                initContext.bind("java:comp/env/iowPath", "c:/ncr/res/log");
                initContext.bind("java:comp/env/UiConsoleLogPath", "c:/ncr/res/log/UiConsoleLog.txt");
                initContext.bind("java:comp/env/deviceLogPath", "c:/ncr/res/log");
                initContext.bind("java:comp/env/tracePath", "c:/ncr/res/dbg");
                initContext.bind("java:comp/env/Journalization/spmPath", "c:/ncr/res/log/SPM_DIR");

                initContext.bind("java:comp/env/snapPath","c:/ncr/res/log/snap_dir");
                initContext.bind("java:comp/env/ReportFormatNewPath","c:/ncr/res/custom/ReportFormatNew.xml");
                initContext.bind("java:comp/env/nrRcptFormatPath","c:/ncr/res/custom/NormalReceiptFormat.xml");
                initContext.bind("java:comp/env/localPrinter","c:/ncr/res/link/systemconfig/interface.xml");
                initContext.bind("java:comp/env/scheduleFilePath","/schedule.xml");

                initContext.bind("java:comp/env/customParamBasePath", "test/resources/cust/para");
                initContext.bind("java:comp/env/paraBasePath", "test/resources/para");
                initContext.bind("java:comp/env/POSLogTransferStatusColumn", "SendStatus1");

                BasicDataSource dsMsSqlServer = new BasicDataSource();
                dsMsSqlServer.setUrl("jdbc:sqlserver://localhost:1433;selectMethod=cursor;sendStringParametersAsUnicode=false");
//                dsMsSqlServer.setUrl("jdbc:sqlserver://153.59.128.97:1433;selectMethod=cursor;sendStringParametersAsUnicode=false");
                dsMsSqlServer.setUsername("entsvr");
                dsMsSqlServer.setPassword("ncrsa_ora");
                initContext.bind("java:comp/env/jdbc/MSSQLSERVER", dsMsSqlServer);
                JndiDBManager.setAutoCommitFalse();
            } catch (Exception e) {
                // e.printStackTrace();
                Assert.fail("Cannot Start the WebAPI");
            }
        }
    }

    /**
     * Setting up initial context.
     */
    public static void SetUpLocalUiConfig() {
        try {
            initContext.unbind("java:comp/env/customMaintenanceBasePath");
            initContext.unbind("java:comp/env/customResourceBasePath");
            initContext.unbind("java:comp/env/scheduleFilePath");
            initContext.bind("java:comp/env/customMaintenanceBasePath", "test/resources/custom/");
            initContext.bind("java:comp/env/customResourceBasePath", "test/resources/custom/");
            initContext.bind("java:comp/env/scheduleFilePath","/schedule.xml");
        } catch (NamingException e) {
            Assert.fail("Cannot set local uiconfig paths");
            e.printStackTrace();
        }
    }

    /**
     * Tear down initial context and mock servlet context.
     */
    public static void TearDown() {
        servletContext = new MockServletContext("");
        try {
            InitialContext initContext = new InitialContext();
            initContext.destroySubcontext("java:");
            initContext.destroySubcontext("java:comp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Adds another context attribute.
     * @param name	name of the attribute.
     * @param value	value of the attribute.
     */
    public static void addContextAttribute(String name, String value){
        servletContext.setAttribute(name, value);
    }
    
    /**
     * Updates the environment entry.
     * @param name
     * @param value
     */
    public static void updateEnvironmentEntry(String name, String value){
    	try {
    		initContext.unbind(name);
			initContext.bind(name, value);
		} catch (NamingException e) {
			e.printStackTrace();
		}
    }
}
