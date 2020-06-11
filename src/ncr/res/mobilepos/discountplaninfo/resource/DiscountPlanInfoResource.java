package ncr.res.mobilepos.discountplaninfo.resource;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.discountplaninfo.dao.DiscountPlanInfoCommomDAO;
import ncr.res.mobilepos.discountplaninfo.model.SubtotalDiscount;
import ncr.res.mobilepos.discountplaninfo.model.SubtotalDiscountInfo;
import ncr.res.mobilepos.model.ResultBase;

/**
 * DiscountPlanInfo Web Resource Class.
 *
 * <P>DiscountPlan transaction.
 *
 */
@Path("/discountplan")
@Api(value="/discountplan", description="小計割引獲得API")
public class DiscountPlanInfoResource {
	/** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The program name.
     */
    private static final String PROG_NAME = "DiscountPlan";
    /** The Trace Printer. */
    private Trace.Printer tp = null;
    /**
     * constructor.
     */
    public DiscountPlanInfoResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
	/**
     * @param SubtotalDiscount
     *            The SubtotalDiscount to get.
     * @return result Returns the SubtotalDiscount
     */
    @Path("/getSubtotalDiscount")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="小計割引理由のリストの取得", response=SubtotalDiscount.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
    })
    public final SubtotalDiscount getSubtotalDiscount() {

    	String functionName = "getSubtotalDiscount";

    	SubtotalDiscount result = new SubtotalDiscount();
        try {
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            DiscountPlanInfoCommomDAO discountPlanCommenDAO = daoFactory
                    .getDiscountPlanInfoCommomDAO();

            ArrayList<SubtotalDiscountInfo> subtotalDiscount = discountPlanCommenDAO.getSubtotalDiscount();

            if (subtotalDiscount != null) {
            	SubtotalDiscountInfo[] arraySubtotalDiscount = new SubtotalDiscountInfo[subtotalDiscount
                        .size()];
            	subtotalDiscount.toArray(arraySubtotalDiscount);
                result.setSubtotalDiscount(arraySubtotalDiscount);
                result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                result.setMessage(ResultBase.RES_SUCCESS_MSG);
            } else {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
            }
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName
                            + ": Failed to getSubtotalDiscount.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName
                            + ": Failed to getSubtotalDiscount.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (SubtotalDiscount)tp.methodExit(result);
    }
}
