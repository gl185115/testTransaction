// Copyright (c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException ;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncr.res.mobilepos.model.WebServerGlobals;
import ncr.realgate.util.IoWriter;

/**
 * Time of day service.
 * This object get current tod from enterprise erver for adjust date time of POS.
 */
public class TodHelper {
    static final int REALGATE_BATCH_OK = 200;
    static final long PROCESS_TIMEOUT = 1000;
    static final String ENV_JCL = "JCL";
    static final String ENV_LOG = "LOG";
    static final String JOBLOG = "joblog";
    static final String TOD_BATCH = "rejstdat.bat";

    static final String PROGNAME = "TOD_HELP";

    static final Pattern TOD_PATTERN = Pattern.compile("Timestamp\"\\s*:\\s*\"(\\d{4}-\\d\\d-\\d\\d)T(\\d\\d:\\d\\d:\\d\\d)");
    /**
     * create instance.
     * @param glob setting for picking up URI of TOD server.
     */
    public TodHelper(WebServerGlobals glob) {
        batchFile = new File(System.getenv(ENV_JCL), TOD_BATCH);
        joblog = new File(System.getenv(ENV_LOG), JOBLOG);
        globals = glob;
    }

    /**
     * Adjust TOD.
     * query to Enterprise Server for TOD.
     * call batch (rejstdat) for adjusting date/time.
     * wait 200 exit.
     *
     * @return true: if all the above conditions were satisfied.
     */
    public boolean adjust() {
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            URL url = new URL(globals.getTodUri());
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(globals.getTodConnectionTimeout());
            conn.setReadTimeout(globals.getTodReadTimeout());
            conn.setDoOutput(false);
            conn.connect();
            try (InputStream is = conn.getInputStream()) {
                byte[] buff = new byte[4096];
                for (;;) {
                    int len = is.read(buff);
                    if (len < 0) break;
                    if (len > 0) {
                        bao.write(buff, 0, len);
                    }
                }
            }
            Matcher m = TOD_PATTERN.matcher(new String(bao.toByteArray(), "UTF-8"));
            if (m.find()) {
                if (execBatch(m.group(1), m.group(2))) {
                    logger.write(IoWriter.LOG, PROGNAME, 80, "TOD reset by EnterpriseServer, " 
                                 + m.group(1) + " " + m.group(2));
                    return true;
                }
            }    
        } catch (Exception e) {
            logger.logSnapException(PROGNAME, "61", "Fetch TOD from EnterpriseServer failed." + e.getMessage(), e);
        }
        return false;
    }

    boolean execBatch(String date, String time) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(batchFile.getAbsolutePath(), date, time);
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(joblog));
        Process p = pb.start();
        try {
            if (p.waitFor(PROCESS_TIMEOUT, TimeUnit.MILLISECONDS)) {
                return p.exitValue() == REALGATE_BATCH_OK;
            }
            p.destroy();
        } catch (InterruptedException e) {
            // force abort
            e.printStackTrace();
        }
        return false;
    }

    /**
     * set tod adjust batch.
     * (default: rejsttod)
     */
    public void setBatchFile(File file) {
        batchFile = file;
    }

    /**
     * get tod adjust batch.
     */
    public File getBatchFile() {
        return batchFile;
    }

    File batchFile;
    File joblog;
    WebServerGlobals globals;
    Logger logger = Logger.getInstance();
}
