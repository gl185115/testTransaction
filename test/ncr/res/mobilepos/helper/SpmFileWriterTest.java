package ncr.res.mobilepos.helper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class SpmFileWriterTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void getInstance() throws Exception {

        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SpmFileWriter.initInstance(tempDir.getAbsolutePath(), "0000", true);
        } catch(IllegalStateException ise) {
            fail();
        }
        SpmFileWriter logger = SpmFileWriter.getInstance();
        assertNotNull(logger);
        SpmFileWriter.closeInstance();
    }

    @Test
    public void initInstanceFailed() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        tempFile.createNewFile();
        try {
            // Sets file to first arg, expects failure.
            SpmFileWriter.initInstance(tempFile.getAbsolutePath(), "0000", true);
            // Throws Exception
            fail();
        } catch(IllegalStateException ise) {
        }
        // When initialization fails, then getInstance returns null.
        assertNull(SpmFileWriter.getInstance());
        SpmFileWriter.closeInstance();
    }

    @Test
    public void initInstanceDirectoryMissing() throws Exception {
        File tempDir = tempFolder.getRoot();
        File tempFile = new File(tempDir + File.separator + "testPath");
        try {
            // Sets file to first arg, expects failure.
            SpmFileWriter.initInstance(tempFile.getAbsolutePath(), "0000", true);
        } catch(IllegalStateException ise) {
            fail();
        }
        // When initialization fails, then getInstance returns null.
        assertNotNull(SpmFileWriter.getInstance());

        SpmFileWriter.closeInstance();
    }

    @Test
    public void directCloseFails() throws IOException {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SpmFileWriter.initInstance(tempDir.getAbsolutePath(), "0000", true);
        } catch(IllegalStateException | IOException ise) {
            fail();
        }
        SpmFileWriter logger = SpmFileWriter.getInstance();
        assertNotNull(logger);

        // Can't allow to close SpmFileWriter directly. Throws Exception.
        try {
            logger.close();
            fail();
        } catch(UnsupportedOperationException e) {

        }
        SpmFileWriter.closeInstance();
    }

    @Test
    public void closeInstance() throws IOException {
        // Closes instance before initialization does nothing.
        SpmFileWriter.closeInstance();

        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SpmFileWriter.initInstance(tempDir.getAbsolutePath(), "0000", true);
        } catch(IllegalStateException ise) {
            fail();
        }
        SpmFileWriter logger = SpmFileWriter.getInstance();
        assertNotNull(logger);

        // Closes initialized SpmFileWriter.
        SpmFileWriter.closeInstance();
    }

    @Test
    public void testWrite() throws Exception {
        File tempDir = tempFolder.getRoot();
        try {
            // Sets tempdir path.
            SpmFileWriter.initInstance(tempDir.getAbsolutePath(), "0000", true);
        } catch(IllegalStateException ise) {
            fail();
        }
        SpmFileWriter logger = SpmFileWriter.getInstance();
        assertNotNull(logger);
        logger.write(JrnSpm.HEADER);
        // Writes HEADER twice. Second header should be ignored.
        logger.write(JrnSpm.HEADER);
        logger.write("aaa");
        SpmFileWriter.closeInstance();

        String spmFileName = SpmFileWriter.SPM_FILENAME + "_0000";
        try (BufferedReader reader =  new BufferedReader(
                new FileReader(tempFolder.getRoot() + File.separator + spmFileName))) {
            String header = reader.readLine();
            assertEquals(JrnSpm.HEADER.trim(), header);

            String line1 = reader.readLine();
            assertEquals("aaa", line1);
        }
    }


}