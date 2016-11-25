package ncr.res.mobilepos.constant;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.env.Environment;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnvironmentEntriesTest {

    private static final String KEY_ROOT = "java:comp/env";
    private static final String KEY_SERVER_ID = "serverID";
    private static final String KEY_IOW_PATH = "iowPath";
    private static final String KEY_TRACE_PATH = "tracePath";
    private static final String KEY_DEBUG_LEVEL = "debugLevel";
    private static final String KEY_SNAP_PATH = "snapPath";
    private static final String KEY_SPM_PATH = "Journalization/spmPath";

    @Test
    public void getInstance() throws Exception {
        Context mockInitialContext = mock(Context.class);
        Context mockEnvContext = mock(Context.class);
        when(mockInitialContext.lookup(anyString())).thenReturn(mockEnvContext);

        when(mockEnvContext.lookup(KEY_SERVER_ID)).thenReturn("testServerID");
        when(mockEnvContext.lookup(KEY_IOW_PATH)).thenReturn("testIowPath");
        when(mockEnvContext.lookup(KEY_TRACE_PATH)).thenReturn("testTracePath");
        when(mockEnvContext.lookup(KEY_DEBUG_LEVEL)).thenReturn(0);
        when(mockEnvContext.lookup(KEY_SNAP_PATH)).thenReturn("testSnapPath");
        when(mockEnvContext.lookup(KEY_SPM_PATH)).thenReturn("testSpmPath");

        EnvironmentEntries envInit = EnvironmentEntries.initInstance(mockInitialContext);
        assertNotNull(envInit);
        EnvironmentEntries env = EnvironmentEntries.getInstance();
        assertEquals(envInit, env);

        assertEquals(env.getServerId(), "testServerID");
        assertEquals(env.getIowPath(), "testIowPath");
        assertEquals(env.getTracePath(), "testTracePath");
        assertEquals(env.getDebugLevel(), 0);
        assertEquals(env.getSnapPath(), "testSnapPath");
        assertEquals(env.getSpmPath(), "testSpmPath");
    }

    @Test
    public void initInstanceFailed() {
        // InitInstance gets failed by config error.
        // Internally when it throws NamingExceptionin, Constructor fails and instance gets null.
        Context mockInitialContext = mock(Context.class);
        Context mockEnvContext = mock(Context.class);
        try {
            when(mockInitialContext.lookup(anyString())).thenReturn(mockEnvContext);

            when(mockEnvContext.lookup(KEY_SERVER_ID)).thenReturn("testServerID");
            when(mockEnvContext.lookup(KEY_IOW_PATH)).thenReturn("testIowPath");
            when(mockEnvContext.lookup(KEY_TRACE_PATH)).thenReturn("testTracePath");
            when(mockEnvContext.lookup(KEY_DEBUG_LEVEL)).thenReturn(0);
            when(mockEnvContext.lookup(KEY_SNAP_PATH)).thenReturn("testSnapPath");
            when(mockEnvContext.lookup(KEY_SPM_PATH)).thenReturn("testSpmPath");

            // Throws NamingException while lookup.
            when(mockEnvContext.lookup(KEY_SERVER_ID)).thenThrow(new NamingException());
            EnvironmentEntries envInit = EnvironmentEntries.initInstance(mockInitialContext);
            fail();
        } catch (NamingException e) {

        }
        // Initialization fails then getInstance returns null.
        assertNull(EnvironmentEntries.getInstance());
    }

}