/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ForwardItemListResource
 *
 * Resource for Transfer transactions between smart phone and POS
 *
 */
package ncr.res.mobilepos.forwarditemlist.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.IForwardItemListDAO;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;

/**
 * Transfer transactions between smart phone and POS.
 */
@Path("/ItemForward")
public class ForwardItemListResource {
    /**
     * The IOWriter for the Log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Instance of SnapLogger for output error transaction data to snap file.
     */
    private SnapLogger snap;
    
    @Context
    private ServletContext context; //to access the web.xml
    
    /**
     * Constructor.
     */
    public ForwardItemListResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.snap =  (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * Sets the DaoFactory of the ForwardItemListResource to
     * use the DAO methods.
     *
     * @param daofactory
     *            - The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daofactory) {
    	//no implementation
    }

    /**
     * The method called by POS to get Forward data.
     * @param storeid      The Store ID
     * @param terminalid   The Terminal ID
     * @param txdate       The BusinessDate
     * @return Forward Item data
     */
    @Path("/request")
    @GET
    @Produces({ MediaType.APPLICATION_XML + ";charset=SHIFT-JIS" })
    public final String requestForwardData(
            @QueryParam("storeid") final String storeid,
            @QueryParam("terminalid") final String terminalid,
            @QueryParam("txdate") final String txdate) {

        tp.methodEnter("requestForwardData");
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        String posLogXml = null;

        //store id is 4 digits in POS, so anyway set to 6 digits with 0.
        String actualstoreid = String.format("%6s", storeid).replace(" ", "0");

        try {
			DAOFactory sqlServer = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IForwardItemListDAO forwardItemListDAO = sqlServer
					.getForwardItemListDAO();
			posLogXml = forwardItemListDAO.getShoppingCartData(actualstoreid,
					terminalid, txdate);
        } catch (DaoException e) {
            LOGGER.logAlert("ForwdItm",
                    "ForwardItemListResource.requestForwardData",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process requestForwardData.\n"
                    + e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert("ForwdItm",
                    "ForwardItemListResource.requestForwardData",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process requestForwardData.\n"
                    + e.getMessage());
        } finally {
            tp.methodExit(posLogXml);
        }

        return posLogXml;
    }

    /**
     * The method called by POS to get Forward data count.
     * @param storeid          The Store ID
     * @param terminalid       The Terminal ID
     * @param txdate           The BusinessDate
     * @return the count of Forward data
     */
    @Path("/getCount")
    @GET
    @Produces({ MediaType.APPLICATION_XML + ";charset=SHIFT-JIS" })
    public final String getCount(@QueryParam("storeid") final String storeid,
            @QueryParam("terminalid") final String terminalid,
            @QueryParam("txdate") final String txdate) {

        tp.methodEnter("getCount");
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        //store id is 4 digits in POS, so anyway set to 6 digits with 0.
		String actualstoreid = String.format("%6s", storeid).replace(" ", "0");
        
        ForwardCountData forwardCountData = new ForwardCountData();
        String countXml = null;
        try {
			DAOFactory sqlServer = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IForwardItemListDAO forwardItemListDAO = sqlServer
					.getForwardItemListDAO();
			forwardCountData = forwardItemListDAO.getForwardCountData(
					actualstoreid, terminalid, txdate);

            XmlSerializer<ForwardCountData> xmlSerializer =
                new XmlSerializer<ForwardCountData>();
            countXml = xmlSerializer.marshallObj(ForwardCountData.class,
                    forwardCountData, "shift_jis");
            countXml = countXml.replace(" standalone=\"yes\"", "");
        } catch (JAXBException e) {
            LOGGER.logAlert("ForwdItm", "ForwardItemListResource.getCount",
                    Logger.RES_EXCEP_JAXB,
                    "Failed to process getCount.\n " + e.getMessage());
        } catch (DaoException e) {
            LOGGER.logAlert("ForwdItm", "ForwardItemListResource.getCount",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process getCount.\n " + e.getMessage());
        } finally {
            tp.methodExit(countXml);
        }
        return countXml;
    }

    /**
     * The method is called by Mobile to upload the
     * information of Shopping Cart as Forward Item Data.
     * @param poslogXml     The POSLog xml
     * @param terminalNo    The Terminal No
     * @param deviceNo      The Device Number
     * @return The POSLog Response {@see PosLogResp}
     */
    @Path("/uploadforward")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public final PosLogResp uploadForwardData(
            @FormParam("poslogxml") final String poslogXml,
            @FormParam("deviceid") final String deviceNo,
            @FormParam("terminalid") final String terminalNo) {

        tp.methodEnter("uploadForwardData");
        tp.println("POSLogXML", poslogXml).println("DeviceNo", deviceNo).
            println("TerminalNo", terminalNo);

        PosLogResp posLogResponse = null;        
        try {
			DAOFactory sqlServer = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IForwardItemListDAO forwardItemListDAO = sqlServer
					.getForwardItemListDAO();
			posLogResponse = forwardItemListDAO.uploadItemForwardData(deviceNo,
					terminalNo, poslogXml);
        } catch (DaoException e) {
            LOGGER.logAlert("ForwdItm",
                    "ForwardItemListResource.uploadForwardData",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process uploadForwardData.\n" + e.getMessage());
            Snap.SnapInfo info = snap.write("poslog xml data", poslogXml);
            LOGGER.logSnap("ForwdItm",
                    "ForwardItemListResource.uploadForwardData",
                    "Output error transaction data to snap file.", info);
        } catch (Exception e) {
            LOGGER.logAlert("ForwdItm",
                    "ForwardItemListResource.uploadForwardData",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process uploadForwardData.\n" + e.getMessage());
            Snap.SnapInfo info = snap.write("poslog xml data", poslogXml);
            LOGGER.logSnap("ForwdItm",
                    "ForwardItemListResource.uploadForwardData",
                    "Output error transaction data to snap file.", info);
        } finally {
            tp.methodExit(posLogResponse);
        }

        return posLogResponse;
    }
}
