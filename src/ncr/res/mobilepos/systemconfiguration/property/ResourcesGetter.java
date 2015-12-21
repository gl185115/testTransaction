/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.systemconfiguration.property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import ncr.res.mobilepos.helper.XmlSerializer;

/**
 * Get the Resources File.
 * @author Administrator
 */
public class ResourcesGetter {

    /**
     * Servlet Context.
     */
    private ServletContext servletContext;

    /**
     * Constructor.
     * @param context   Servlet Context.
     */
    public ResourcesGetter(final ServletContext context) {
        this.servletContext = context;
    }

    /**
     * Get Jis1Param Model.
     * @param <T>               Type of class
     * @param path              File path
     * @param clas              clas
     * @return                  Object
     * @throws IOException      Exception when input and output error occurs.
     * @throws JAXBException    Exception with specified detail message
     *                          when error occurs
     */
    public final <T> T getXmlParam(final String path, final Class<T> clas)
    throws IOException, JAXBException {
        T object;

        InputStream is = servletContext.getResourceAsStream(path);
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        XmlSerializer<T> jis1ParamSerializer = new XmlSerializer<T>();
        object = jis1ParamSerializer.unMarshallXml(sb.toString(), clas);

        reader.close();
        is.close();

        return object;
    }
}
