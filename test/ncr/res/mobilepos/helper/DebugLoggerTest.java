package ncr.res.mobilepos.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;

import ncr.realgate.servlet.IowLogger;
import ncr.realgate.util.Trace;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DebugLoggerTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void getInstance() throws Exception {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            DebugLogger.initInstance(tempDir.getAbsolutePath(), 0);
        } catch(IllegalStateException ise) {
            fail();
        }
        Trace.Printer logger = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        assertNotNull(logger);

        DebugLogger.closeAllDbgPrinter();
    }


    @Test
    public void initInstanceFailed() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        tempFile.createNewFile();
        try {
            // Sets file to first arg, expects failure.
            DebugLogger.initInstance(tempFile.getAbsolutePath(), 0);
            // Throws Exception
            fail();
        } catch(IllegalStateException ise) {
        }
        // When initialization fails, then getInstance returns null.
        assertNull(DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass()));
    }

    @Test
    public void initInstanceDirectoryMissing() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        try {
            // Sets file to first arg, expects failure.
            DebugLogger.initInstance(tempFile.getAbsolutePath(), 0);
        } catch(IllegalStateException ise) {
            fail();
        }
        // When initialization fails, then getInstance returns null.
        assertNotNull(DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass()));
        DebugLogger.closeAllDbgPrinter();
    }

}