package ncr.res.mobilepos.constant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class EnvironmentEntriesTest {

    private static final String KEY_ROOT = "java:comp/env";
    
    private static int status = 0;
    public static final int ERROR_EXCEPTION = 1;

    // 1
    private static final String KEY_SERVER_ID = "serverID";
    // 2
    private static final String KEY_DEBUG_LEVEL = "debugLevel";
    // 3
    private static final String KEY_PRINT_PAPER_LENGTH = "Printpaperlength";
    // 4
    private static final String KEY_CUSTOM_MAINTENANCE_BASE_PATH ="customMaintenanceBasePath";
    // 5
    private static final String KEY_CUSTOM_RESOURCE_BASE_PATH = "customResourceBasePath";
    // 6
    private static final String KEY_IOW_PATH = "iowPath";
    // 7
    private static final String KEY_UI_CONSOLE_LOG_PATH = "UiConsoleLogPath";
    // 8
    private static final String KEY_DEVICE_LOG_PATH = "deviceLogPath";
    // 9
    private static final String KEY_TRACE_PATH = "tracePath";
    // 10
    private static final String KEY_SPM_PATH = "Journalization/spmPath";
    // 11
    private static final String KEY_SNAP_PATH = "snapPath";
    // 12
    private static final String KEY_REPORT_FORMAT_NEW_PATH = "ReportFormatNewPath";
    // 13
    private static final String KEY_NR_RCPT_FORMAT_PATH = "nrRcptFormatPath";
    // 14
    private static final String KEY_LOCAL_PRINTER = "localPrinter";
    // 15
    private static final String KEY_SCHEDULE_FILE_PATH = "scheduleFilePath";
    // 16
    private static final String KEY_CUSTOM_PARAM_BASE_PATH = "customParamBasePath";
    // 17
    private static final String KEY_PARA_BASE_PATH = "paraBasePath";
    // 18
    private static final String KEY_POSLOG_TRANSFER_STATUS_COLUMN = "POSLogTransferStatusColumn";
    // 19
    private static final String KEY_SYSTEM_PATH = "SYS";

    @Before
    public void setUp() throws NamingException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        InitialContext initContext = new InitialContext();

        initContext.createSubcontext("java:comp");
        initContext.createSubcontext("java:comp/env");
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
    }

    @After
    public void tearDown() throws NamingException {
        InitialContext initContext = new InitialContext();
        initContext.destroySubcontext("java:comp/env/Journalization");
        initContext.destroySubcontext("java:comp/env");
        initContext.destroySubcontext("java:comp");
    }

    @Test
    public void getInstance() throws NamingException {
        try {
            EnvironmentEntries envInit = EnvironmentEntries.initInstance(new InitialContext());
            assertNotNull(envInit);

            EnvironmentEntries env = EnvironmentEntries.getInstance();
            assertEquals(envInit, env);

            assertEquals("A010", env.getServerId());
            assertEquals(2, env.getDebugLevel());
            assertEquals("2", env.getPrintPaperLength());
            assertEquals("c:/ncr/res/custom/", env.getCustomMaintenanceBasePath());
            assertEquals("c:/ncr/res/custom/", env.getCustomResourceBasePath());

            assertEquals("c:/ncr/res/log", env.getIowPath());
            assertEquals("c:/ncr/res/log/UiConsoleLog.txt", env.getUiConsoleLogPath());
            assertEquals("c:/ncr/res/log", env.getDeviceLogPath());
            assertEquals("c:/ncr/res/dbg", env.getTracePath());
            assertEquals("c:/ncr/res/log/SPM_DIR", env.getSpmPath());

            assertEquals("c:/ncr/res/log/snap_dir", env.getSnapPath());
            assertEquals("c:/ncr/res/custom/ReportFormatNew.xml", env.getReportFormatNewPath());
            assertEquals("c:/ncr/res/custom/NormalReceiptFormat.xml", env.getNrRcptFormatPath());
            assertEquals("c:/ncr/res/link/systemconfig/interface.xml", env.getLocalPrinter());
            assertEquals("/schedule.xml", env.getScheduleFilePath());

            assertEquals("test/resources/cust/para", env.getCustomParamBasePath());
            assertEquals("test/resources/para", env.getParaBasePath());
            assertEquals("SendStatus1", env.getPoslogTransferStatusColumn());

        } catch (Exception e) {
            fail("No Exception expected.");
        }
    }
    
    private void poslogBindEmpty() throws NamingException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        InitialContext initContext = new InitialContext();

        initContext.createSubcontext("java:comp");
        initContext.createSubcontext("java:comp/env");
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
        initContext.bind("java:comp/env/POSLogTransferStatusColumn", "");
    }
    
    @SuppressWarnings("unused")
    @Test
    public void poslogBindEmptyTest() throws NamingException {
        tearDown();
        poslogBindEmpty();
        try {
            EnvironmentEntries envInit = EnvironmentEntries.initInstance(new InitialContext());
        } catch (Exception e) {
            status = ERROR_EXCEPTION;
            try {
                assertEquals(1, status);
            } catch (Exception e1) {
                fail("No Exception exptected.");
            }
        }
    }
    
    private void poslogBindNull() throws NamingException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        InitialContext initContext = new InitialContext();

        initContext.createSubcontext("java:comp");
        initContext.createSubcontext("java:comp/env");
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
    }
    
    @Test
    public void poslogBindNullTest() throws NamingException {
        tearDown();
        poslogBindNull();
        try {
            EnvironmentEntries envInit = EnvironmentEntries.initInstance(new InitialContext());
            assertNotNull(envInit);

            EnvironmentEntries env = EnvironmentEntries.getInstance();
            assertEquals(envInit, env);

            assertEquals("A010", env.getServerId());
            assertEquals(2, env.getDebugLevel());
            assertEquals("2", env.getPrintPaperLength());
            assertEquals("c:/ncr/res/custom/", env.getCustomMaintenanceBasePath());
            assertEquals("c:/ncr/res/custom/", env.getCustomResourceBasePath());

            assertEquals("c:/ncr/res/log", env.getIowPath());
            assertEquals("c:/ncr/res/log/UiConsoleLog.txt", env.getUiConsoleLogPath());
            assertEquals("c:/ncr/res/log", env.getDeviceLogPath());
            assertEquals("c:/ncr/res/dbg", env.getTracePath());
            assertEquals("c:/ncr/res/log/SPM_DIR", env.getSpmPath());

            assertEquals("c:/ncr/res/log/snap_dir", env.getSnapPath());
            assertEquals("c:/ncr/res/custom/ReportFormatNew.xml", env.getReportFormatNewPath());
            assertEquals("c:/ncr/res/custom/NormalReceiptFormat.xml", env.getNrRcptFormatPath());
            assertEquals("c:/ncr/res/link/systemconfig/interface.xml", env.getLocalPrinter());
            assertEquals("/schedule.xml", env.getScheduleFilePath());

            assertEquals("test/resources/cust/para", env.getCustomParamBasePath());
            assertEquals("test/resources/para", env.getParaBasePath());
            assertEquals("SendStatus1", env.getPoslogTransferStatusColumn());

        } catch (Exception e1) {
            fail("No Exception expected.");
        }
    }

    @Test
    public void initInstanceFailed() {
        try {
            InitialContext init = new InitialContext();
            init.unbind("java:comp/env/serverID");
            EnvironmentEntries.initInstance(new InitialContext());
            // NamingException has to be thrown.
            fail();
        } catch (NamingException e) {
        } catch (Exception e) {
        }
        // Initialization fails then getInstance returns null.
        assertNull(EnvironmentEntries.getInstance());
    }

}