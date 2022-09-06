/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* JsonSerializer
*
* Helper Class for Json Marshaller.
*
* cc185102
*/
package ncr.res.mobilepos.helper;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;


/**
 * JsonMarshaller is a helper class for converting JSON string
 * to Object and vice versa.
 *
 * @author CC185102
 * @param <T> The Class of the object.
 */
public class JsonMarshaller<T> {
    /**
     * Member variable that act as the Object Mapper needed for
     * converting JSON String to Object and vice versa.
     */
    private ObjectMapper mapper;
    
    /**
     * Member variable that act as the Json Factory needed for
     * converting JSON String to Object and vice versa.
     */
    private JsonFactory factory;
    
    /**
     * Member variable that act as the Object Reader needed for
     * converting JSON String to Object and vice versa.
     */
    private ObjectReader reader;
    
    
    /**
     * The Default Constructor.
     */
    @SuppressWarnings("deprecation")
    public JsonMarshaller() {
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
        mapper = new ObjectMapper();
        //Setting the Deserialization Configuration allows
        //annotated Object to be Serialized
        mapper.getDeserializationConfig()
            .setAnnotationIntrospector(introspector);
        mapper.getSerializationConfig()
            .setAnnotationIntrospector(introspector);
        /**
         * Make the JSON Serializer loose with rule against JSON string
         */
        mapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        mapper.configure(Feature.ALLOW_COMMENTS, true);
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        mapper.configure(Feature.CANONICALIZE_FIELD_NAMES, true);
        mapper.configure(Feature.INTERN_FIELD_NAMES, true);
        
        factory = mapper.getJsonFactory();
    }
    
    
    /**
     * The Extend Constructor.
     */
    public JsonMarshaller(final Class<T> clas) {
        this();
        assert mapper != null;
        reader = mapper.reader(clas);
    }
    
    /**
     * Static method used to convert json String into specified object.
     * @param json      The JSON String
     * @param clas      The Class for the Object
     * @return The Object representation of JSON String.
     * @throws IOException The exception thrown when error occur.
     */
    public final T unMarshall(final String json, final Class<T> clas)
    throws IOException {
        assert mapper != null && factory != null;
        
        JsonParser jp = factory.createJsonParser(json);
        T returnObject = mapper.readValue(jp, clas);
        return returnObject;
    }
    
    /**
     * Static method used to convert json String into specified object.
     * To use this method, need to use the extended constructor
     * @param json      The JSON String
     * @return The Object representation of JSON String.
     * @throws IOException The exception thrown when error occur.
     */
    public final T unMarshall(final String json)
    throws IOException {
        assert reader != null && factory != null;
        
        JsonParser jp = factory.createJsonParser(json);
        T returnObject = reader.readValue(jp);
        return returnObject;
    }
    

    /**
     * Method used to Object to its own JSON format.
     * @param object        The object to be represented into its JSON Format.
     * @return              The JSON String representation.
     * @throws IOException  The exception thrown when error occurs.
     */
    public final String marshall(final T object)
    throws IOException {
        return this.mapper.writeValueAsString(object);
    }
}
