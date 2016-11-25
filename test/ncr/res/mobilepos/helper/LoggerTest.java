package ncr.res.mobilepos.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import ncr.realgate.util.Snap;

import static org.junit.Assert.*;

public class LoggerTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void getInstance() throws Exception {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            Logger.initInstance(tempDir.getAbsolutePath(), "0000");
        } catch(IllegalStateException ise) {
            fail();
        }
        Logger logger = Logger.getInstance();
        assertNotNull(logger);
    }

    @Test
    public void initInstanceFailed() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        tempFile.createNewFile();
        try {
            // Sets file to first arg, expects failure.
            Logger.initInstance(tempFile.getAbsolutePath(), "testServerID");
            // Throws Exception
            fail();
        } catch(IllegalStateException ise) {
        }
        // When initialization fails, then getInstance returns null.
        assertNull(Logger.getInstance());
    }

    @Test
    public void initInstanceDirectoryMissing() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        try {
            // Sets file to first arg, expects failure.
            Logger.initInstance(tempFile.getAbsolutePath(), "1234");
        } catch(IllegalStateException ise) {
            fail();
        }
        // When initialization fails, then getInstance returns null.
        assertNotNull(Logger.getInstance());
    }

    @Test
    public void testWrite() throws Exception {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            Logger.initInstance(tempDir.getAbsolutePath(), "0000");
        } catch(IllegalStateException ise) {
            fail();
        }
        Logger logger = Logger.getInstance();
        assertNotNull(logger);

        String iowFilePath = tempDir.getAbsolutePath() + File.separator + "IOWLOG";
        logger.logAlert("progA", "functionNameA","AA","logMessageA");
        logger.logWarning("progW", "functionNameW","WW","logMessageW");
        logger.write(Logger.ERROR, "progE", "EE", "functionNameE: logMessageE");
        logger.write(Logger.ERROR, "prog1", 1, "functionName1: logMessage1");

        Snap.SnapInfo snapInfo = Snap.getEmptyInfo();
        logger.write(Logger.LOG, "emppr", "em","emptySnap", snapInfo);

        try (BufferedReader reader =  new BufferedReader(
                new FileReader(iowFilePath))) {
            boolean includeEx1 = false;
            String line = reader.readLine();
            assertEquals(Logger.ALERT, line.charAt(0));
            assertTrue(line.indexOf("progA") > 0);
            assertTrue(line.indexOf("AA:functionNameA: logMessageA") > 0);

            line = reader.readLine();
            assertEquals(Logger.WARNING, line.charAt(0));
            assertTrue(line.indexOf("progW") > 0);
            assertTrue(line.indexOf("WW:functionNameW: logMessageW") > 0);

            line = reader.readLine();
            assertEquals(Logger.ERROR, line.charAt(0));
            assertTrue(line.indexOf("progE") > 0);
            assertTrue(line.indexOf("EE:functionNameE: logMessageE") > 0);

            line = reader.readLine();
            assertEquals(Logger.ERROR, line.charAt(0));
            assertTrue(line.indexOf("prog1") > 0);
            assertTrue(line.indexOf("1:functionName1: logMessage1") > 0);

            line = reader.readLine();
            assertEquals(Logger.LOG, line.charAt(0));
            assertTrue(line.indexOf("emppr") > 0);
            assertTrue(line.indexOf("EM:emptySnap") > 0);
        }
    }



}