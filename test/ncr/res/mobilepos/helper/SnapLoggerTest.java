package ncr.res.mobilepos.helper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SnapLoggerTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void getInstance() throws Exception {

        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SnapLogger.initInstance(tempDir.getAbsolutePath());
        } catch(IllegalStateException ise) {
            fail();
        }
        SnapLogger logger = SnapLogger.getInstance();
        assertNotNull(logger);
        SnapLogger.closeInstance();
    }

    @Test
    public void initInstanceFailed() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        tempFile.createNewFile();
        try {
            // Sets file to first arg, expects failure.
            SnapLogger.initInstance(tempFile.getAbsolutePath());
            // Throws Exception
            fail();
        } catch(IllegalStateException ise) {
        }
        // When initialization fails, then getInstance returns null.
        assertNull(SnapLogger.getInstance());
    }

    @Test
    public void initInstanceDirectoryMissing() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        try {
            // Sets file to first arg, expects failure.
            SnapLogger.initInstance(tempFile.getAbsolutePath());
        } catch(IllegalStateException ise) {
            fail();
        }
        // When initialization fails, then getInstance returns null.
        assertNotNull(SnapLogger.getInstance());
        SnapLogger.closeInstance();
    }

    @Test
    public void directCloseFails() throws IOException {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SnapLogger.initInstance(tempDir.getAbsolutePath());
        } catch(IllegalStateException ise) {
            fail();
        }
        SnapLogger logger = SnapLogger.getInstance();
        assertNotNull(logger);

        // Can't allow to close SnapLogger directly. Throws Exception.
        try {
            logger.close();
            fail();
        } catch (UnsupportedOperationException e) {

        }
        SnapLogger.closeInstance();
    }

    @Test
    public void closeInstance() throws IOException {
        // Closes instance before initialization does nothing.
        SnapLogger.closeInstance();

        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SnapLogger.initInstance(tempDir.getAbsolutePath());
        } catch(IllegalStateException ise) {
            fail();
        }
        SnapLogger logger = SnapLogger.getInstance();
        assertNotNull(logger);

        // Closes initialized SnapLogger.
        SnapLogger.closeInstance();
    }

    @Test
    public void testWriteString() throws Exception {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SnapLogger.initInstance(tempDir.getAbsolutePath());
        } catch(IllegalStateException ise) {
            fail();
        }
        SnapLogger logger = SnapLogger.getInstance();
        String loggerPath = logger.getPathName();
        assertNotNull(logger);
        logger.write("TestComment", "TestComment2");
        SnapLogger.closeInstance();

        try (BufferedReader reader =  new BufferedReader(
                new FileReader(loggerPath))) {
            String line1 = reader.readLine();
            assertTrue(line1.indexOf("TestComment") > 0);
            assertTrue(line1.indexOf("TestComment2") > 0);
        }
    }

    @Test
    public void testWriteException() throws Exception {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SnapLogger.initInstance(tempDir.getAbsolutePath());
        } catch(IllegalStateException ise) {
            fail();
        }
        SnapLogger logger = SnapLogger.getInstance();
        String loggerPath = logger.getPathName();
        assertNotNull(logger);
        logger.write("ExceptionTest", new NullPointerException());
        logger.write("ExceptionTest2", new IllegalStateException());
        SnapLogger.closeInstance();

        try (BufferedReader reader =  new BufferedReader(
                new FileReader(loggerPath))) {
            String line;
            boolean includeEx1 = false;
            boolean includeEx2 = false;
            while((line = reader.readLine()) != null) {
                includeEx1 |= line.indexOf("NullPointerException") > 0;
                includeEx2 |= line.indexOf("IllegalStateException") > 0;
            }
            assertTrue(includeEx1);
            assertTrue(includeEx2);
        }
    }

    @Test
    public void testWriteByte() throws Exception {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SnapLogger.initInstance(tempDir.getAbsolutePath());
        } catch (IllegalStateException ise) {
            fail();
        }
        SnapLogger logger = SnapLogger.getInstance();
        String loggerPath = logger.getPathName();
        assertNotNull(logger);

        byte[] bytearg = "ByteWriteTestData".getBytes();
        logger.write("ByteWriteTest", bytearg);
        SnapLogger.closeInstance();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(loggerPath))) {
            String line;
            boolean includeEx1 = false;
            while ((line = reader.readLine()) != null) {
                includeEx1 |= line.indexOf("ByteWriteTestData") > 0;
            }
            assertTrue(includeEx1);
        }
    }


}