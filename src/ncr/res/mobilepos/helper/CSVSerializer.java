package ncr.res.mobilepos.helper;

import java.io.IOException;
import java.io.StringReader;

import org.jsefa.csv.CsvDeserializer;
import org.jsefa.csv.CsvIOFactory;
import org.jsefa.csv.config.CsvConfiguration;
import org.jsefa.csv.lowlevel.config.CsvLowLevelConfiguration;
import org.jsefa.csv.lowlevel.config.EscapeMode;

/**
 * CSVSerializer is a helper class for converting a csv string to object.
 *
 * @param <T>   The Class of the object.
 */
public class CSVSerializer<T> {
    /**
     * Parses a csv string into an object.
     *
     * @param clas The type of the bean
     * @param csv The csv string to parse
     * @return Object
     * @throws IOException  Exception thrown when
     *                 I/O exception has occurred.
     */
    public final T deserializeCsv(final String csv,
                              final Class<T> clas) throws IOException {
        CsvDeserializer deserializer = null;
        T object = null;
        try {
            EscapeMode mode = EscapeMode.ESCAPE_CHARACTER;
            CsvLowLevelConfiguration conf = new CsvLowLevelConfiguration();
            conf.setQuoteCharacterEscapeMode(mode);
            conf.setQuoteCharacter('"');
            conf.setFieldDelimiter(',');
            CsvConfiguration config = new CsvConfiguration();
            config.setLowLevelConfiguration(conf);

            deserializer =
                CsvIOFactory.createFactory(config, clas).createDeserializer();
            StringReader reader = new StringReader(csv);
            deserializer.open(reader);
            object = null;
            while (deserializer.hasNext()) {
                object = deserializer.next();
            }
        } finally {
            if (null != deserializer) {
                deserializer.close(true);
            }
        }
        return object;
    }
}
