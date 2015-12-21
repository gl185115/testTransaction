package ncr.res.mobilepos.discountplaninfo.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.discountplaninfo.dao.IPromotionInfoDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.xebioapi.model.JSONData;

@Path("/discountplaninfo")
public class PromotionInfoResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private static final String PROG_NAME = "PromotionInfoResource";
    @Context
    private ServletContext servletContext;

    public PromotionInfoResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * @param companyId
     * @param storeId
     * @param discountReason
     * @param discountBarcodeType
     * @param partialFlag
     * @param priceDiscountFlag
     * @param rateDiscountFlag
     * @return Promotion
     * @throws DaoException
     */
    @Path("/getPromotionInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final JSONData getPromotionInfo(@QueryParam("CompanyId") final String companyId,
            @QueryParam("StoreId") final String storeId, @QueryParam("DiscountReason") final String discountReason,
            @QueryParam("DiscountBarcodeType") final String discountBarcodeType,
            @QueryParam("PartialFlag") final String partialFlag,
            @QueryParam("PriceDiscountFlag") final String priceDiscountFlag,
            @QueryParam("RateDiscountFlag") final String rateDiscountFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                .println("DiscountReason", discountReason).println("DiscountBarcodeType", discountBarcodeType)
                .println("PartialFlag", partialFlag).println("PriceDiscountFlag", priceDiscountFlag)
                .println("RateDiscountFlag", rateDiscountFlag);

        JSONData promotion = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(companyId, storeId, discountReason, discountBarcodeType, partialFlag,
                    priceDiscountFlag, rateDiscountFlag)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                promotion.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                promotion.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                promotion.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return promotion;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPromotionInfoDAO iPromotionInfoDAO = sqlServer.getPromotionInfoDAO();

            promotion = iPromotionInfoDAO.getPromotionInfo(companyId, storeId, discountReason, discountBarcodeType,
                    partialFlag, priceDiscountFlag, rateDiscountFlag);
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get promotion infomation.", e);
            promotion.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            promotion.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            promotion.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get promotion infomation.", ex);
            promotion.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            promotion.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            promotion.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(promotion);
        }
        return promotion;
    }
}