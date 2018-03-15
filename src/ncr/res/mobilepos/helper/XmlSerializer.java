/*
    * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
    *
    * CreditAuthXmlTemplater
    *
    * Class Handler used for Credit Authorization XML
    *
    * Campos, Carlos
    */

package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * XmlSerializer is a helper class for converting an xml to object.
 *
 * @param <T>    The Class of the object.
 */
public class XmlSerializer<T> {

    /**
     * Trims the extra characters outside the Xml if
     * in any case a given string contains an xml
     * but with extra unnecessary characters.
     *
     * @param strXml    The String to be trimmed
     * @return            Returns the exact/correct xml format.
     */
    public final String toTrim(final String strXml) {
        if (null == strXml || strXml.isEmpty()) {
            return "";
        }

        return strXml.substring(strXml.indexOf('<'));
    }

    /**
     * Deserialize Xml into Object.
     *
     * @param xml                The xml to be serialize.
     * @param clas                The class of the object that
     *                              will represent the xml.
     * @return                    The instance of an object
     *                              that represents the xml.
     * @throws JAXBException    Exception thrown when method fails.
     */
    @SuppressWarnings("unchecked")
    public final T unMarshallXml(final String xml, final Class<T> clas)
    throws JAXBException {
        if (xml.isEmpty()) {
            return null;
        }

        T object;

        JAXBContext jaxbContext = JAXBContext.newInstance(clas);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        //Trim the CreditAuthorizationXML to get the exact xml string
        String trimedXml = toTrim(xml);

        StringReader reader = new StringReader(trimedXml);

        object =  (T) unmarshaller.unmarshal(reader);

        return object;
    }  
    
    
    /**
     * Deserialize Xml into Object.
     *
     * @param xml
     * @param clas                The class of the object that
     *                              will represent the xml.
     * @return                    The instance of an object
     *                              that represents the xml.
     * @throws JAXBException    Exception thrown when method fails.
     */
    @SuppressWarnings("unchecked")
    public final T unMarshallXml(File fileXml, final Class<T> clas)
    throws JAXBException {

        if (!fileXml.exists()) {
            return null;
        }

        T object;

        JAXBContext jaxbContext = JAXBContext.newInstance(clas);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        object =  (T) unmarshaller.unmarshal(fileXml);

        return object;
    }
    
    /**
     * * Serialize Object to XML String.
     * @param clas -  the class to serialize to xml
     * @param object - the object to serialize
     * @param encoding - encoding to use
     * @return String - the xml equivalent of the object
     * @throws JAXBException - exception that is thrown
     */
    public final String marshallObj(
            final Class<T> clas, final T object, final String encoding)
    throws JAXBException {
        StringWriter sw = new StringWriter();

        JAXBContext context = JAXBContext.newInstance(clas);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.marshal(object, sw);

        return sw.getBuffer().toString();
    }
}
