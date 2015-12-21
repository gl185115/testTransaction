/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* FileParser
*
* Helper Class for file handling.
*
* jd185128
*/

package ncr.res.mobilepos.helper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.output.FileWriterWithEncoding;

/**
 * class that handles file parsing.
 *
 */
public class FileParser {

    /**
     * class instance of the file path to the file to parse.
     */
    private String filepath = "";

    /**
     * constructor.
     * @param filepathToSet - the path to the file
     */
    public FileParser(final String filepathToSet) {
        this.filepath = filepathToSet;
    }

    /**
     * Sets the filepath.
     * @param filepathToSet - the path to the file
     */
    public final void setFilePath(final String filepathToSet) {
        this.filepath = filepathToSet;
    }

    /**
     * Gets filepath.
     * @return path
     */
    public final String getFilePath() {
        return filepath;
    }

    /**
     * Write data to file.
     * @param data - the data to write to the file
     * @return boolean success is true, failed is false
     */
    public final boolean writeToFile(final String data) {
        boolean hasData = true;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriterWithEncoding(
                    filepath, Charset.forName("UTF-8")));
            out.write(data);
              out.newLine();
              out.close();
        } catch (IOException ioe) {
            hasData = false;
        }
        return hasData;
    }

}
