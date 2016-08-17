/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ReportFile
 *
 * Handles reading file of the report
 *
 * jd185128
 */
package ncr.res.mobilepos.report.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import ncr.res.mobilepos.helper.Logger;

/**
 * ReportFile Class is a Utility class for handling file of the report.
 */
public final class ReportFile {/**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    private static final String PROG_NAME = "ReportFile";
	
    /** Default Constructor. */
    private ReportFile() { 
    	
    }
    /**
     * Read FinancialRpt html file.
     *
     * @return String FinancialRpt data on file
     * @throws Exception
     *             Exception when file is not found.
     */
    public static String readFinancialRpt() throws Exception {
        StringBuilder data = new StringBuilder();
        BufferedReader reader = null;
        InputStreamReader isr = null;
        try {
            InputStream file = FinancialReport.class
                    .getResourceAsStream("financialRpt.html");
            isr = new InputStreamReader(file, Charset.forName("UTF-8"));
            reader = new BufferedReader(isr);
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } finally {
                reader.close();
            }
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (final IOException e) {
                	LOGGER.logAlert(PROG_NAME, PROG_NAME+".readFinancialRpt", Logger.RES_EXCEP_IO, "Failed to close stream. "+e.getMessage());
                }
            }
        }

        return data.toString();
    }
}
