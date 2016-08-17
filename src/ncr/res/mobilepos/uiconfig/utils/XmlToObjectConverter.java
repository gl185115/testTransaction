package ncr.res.mobilepos.uiconfig.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;


public class XmlToObjectConverter {

    public static <T> Object parseXml(String xmlString, Class<T> c) {
        Object obj = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(trim(xmlString));
            obj = unmarshaller.unmarshal(reader);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return obj;

    }

    private static String trim(String xmlString) {

        if (xmlString == null || xmlString.isEmpty()) {
            return "";
        }

        return xmlString.substring(xmlString.indexOf('<'));
    }

    /**
     * Marshall XML with given file.
     *
     * @param xmlFile
     * @param c
     * @param <T>
     * @return
     */
    public static <T> Object parseXml(File xmlFile, Class<T> c) {
        Object obj = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
            obj = unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
