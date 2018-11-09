package ncr.res.mobilepos.promotion.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customerSearch.constants.CustomerSearchConstants;
import ncr.res.mobilepos.customerSearch.dao.ICustomerSearthDAO;
import ncr.res.mobilepos.daofactory.DAOFactory;
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
@Api(value="/remoteitem", description="è§ïièÓïÒAPI")
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
    @ApiOperation(value="è§ïièÓïÒéÊìæ", response=Void.class)
    public final PromotionResponse itemEntry(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        String functionName = DebugLogger.getCurrentMethodName();
        String retailStoreId = request.getParameter("storeId");
        String pluCode = request.getParameter("pluCode");
        String companyId = request.getParameter("companyId");
        String businessDate = request.getParameter("businessDate");
        PromotionResponse promotionResponse = null;
        Item item = null;
            if (StringUtility.isNullOrEmpty(retailStoreId, pluCode, companyId, businessDate)) {
                tp.println("Parameter[s] is empty or null.");
            }
            try {
            	DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            	ICustomerSearthDAO iCustomerSearthDAO = sqlServer.getCustomerSearthDAO();
            	Map<String, String> mapTaxId = iCustomerSearthDAO.getPrmSystemConfigValue(CustomerSearchConstants.CATEGORY_TAX);

                SQLServerItemDAO sqlDao = new SQLServerItemDAO();
                item = sqlDao.getItemByPLU(retailStoreId, pluCode, companyId, 0, businessDate, mapTaxId);
                Sale saleIn = new Sale();
                Transaction transactionOut = new Transaction();
                promotionResponse = new PromotionResponse();
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
            return promotionResponse;
    }
    
    /**
     * get Item DetailInfo
     * 
     * @param companyId
     *            The companyId
     * @param retailStoreId
     *            The retailStoreId
     * @param ItemCode
     *            The code of The Item
     * @return MdName
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/mdName_getremoteinfo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="è§ïiñºéÊìæ", response=Void.class)
    public final PromotionResponse getMdName(@Context HttpServletRequest request,@Context HttpServletResponse response) {
        String functionName = DebugLogger.getCurrentMethodName();
        String companyId = request.getParameter("companyId");
        String retailStoreId = request.getParameter("retailStoreId");
        String ItemCode = request.getParameter("ItemCode");
        PromotionResponse promotionResponse = null;
            if (StringUtility.isNullOrEmpty(companyId, retailStoreId, ItemCode)) {
                tp.println("Parameter[s] is empty or null.");
            }
            try {
                SQLServerItemDAO sqlDao = new SQLServerItemDAO();
                Sale saleMdName = sqlDao.getItemNameFromPluName(companyId, retailStoreId, ItemCode);
                Transaction transactionOut = new Transaction();
                promotionResponse = new PromotionResponse();
                try {
                    OutputStream out = response.getOutputStream();
                    if(saleMdName != null){
                    	transactionOut.setSale(saleMdName);
                        promotionResponse.setTransaction(transactionOut);
                        out.write(promotionResponse.toString().getBytes());
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (DaoException e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get MdName Info.", e); 
            }
            return promotionResponse;
    }
}
