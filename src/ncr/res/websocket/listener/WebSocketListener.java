package ncr.res.websocket.listener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.websocket.model.RESMobileParam;

public class WebSocketListener implements ServletContextListener{
    
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    
    private Trace.Printer tp;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	//no implementation
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        tp.methodEnter("WebSocketListener.contextInitialized");
        tp.println(" - The WebSocket Server called. ");
        tp.println("Preparing to retrieve the Parameters");
        ServletContext servletContext = event.getServletContext();
        try {
            RESMobileParam resMobileParam = new RESMobileParam();
            InputStream is = servletContext.getResourceAsStream("/parameters/RESMobileParam.xml");
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(RESMobileParam.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader strReader = new StringReader(sb.toString());

            resMobileParam = (RESMobileParam) unmarshaller.unmarshal(strReader);

            reader.close();
            is.close();
            
            servletContext.setAttribute("RESMobileParam", resMobileParam);
        } catch (Exception e) {
            LOGGER.logAlert("WSktSvr", "WebSocketListener.contextInitialized", "99",
                    "Failed when contexntInitialized.\n" + e.getMessage());
        } finally {
            tp.methodExit();
        }
    }
}
