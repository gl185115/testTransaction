package ncr.res.mobilepos.promotion.resource;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;
import ncr.res.mobilepos.promotion.model.PromotionResponse;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.model.Transaction;

/**
 * RemoteItemResource Class is a Web Resource which support MobilePOS Promotion
 * processes.
 *
 */

@Path("/remoteitem")
public class RemoteItemResource {
    /**
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get
    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "RemoteProm";

    /**
     * Default Constructor for PromotionResource.
     *
     * <P>
     * Initializes the logger object.
     */
    public RemoteItemResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * get Item DetailInfo
     * 
     * @param retailStoreId
     *            Store Number where the transaction is coming from
     * @param pluCode
     *            The code of The Item
     * @param companyId
     *            The companyId
     * @param businessDate
     *            The businessDate
     * @return Item
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/item_getremoteinfo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final void itemEntry(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        String functionName = DebugLogger.getCurrentMethodName();
        String retailStoreId = request.getParameter("storeId");
        String pluCode = request.getParameter("pluCode");
        String companyId = request.getParameter("companyId");
        String businessDate = request.getParameter("businessDate");
        Item item = null;
            if (StringUtility.isNullOrEmpty(retailStoreId, pluCode, companyId, businessDate)) {
                tp.println("Parameter[s] is empty or null.");
            }
            try {
                SQLServerItemDAO sqlDao = new SQLServerItemDAO();
                item = sqlDao.getItemByPLU(retailStoreId, pluCode, companyId, businessDate);
                Sale saleIn = new Sale();
                Transaction transactionOut = new Transaction();
                PromotionResponse promotionResponse = new PromotionResponse();
                try {
                    OutputStream out = response.getOutputStream();
                    if(item != null){
                        Sale saleItem = SaleItemsHandler.createSale(item, saleIn);
                        transactionOut.setSale(saleItem);
                        promotionResponse.setTransaction(transactionOut);
                        out.write(promotionResponse.toString().getBytes());
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (DaoException e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get item Info.", e); 
            }
    }
}
