/**
 * 
 */
/**
 * @author xxj
 *
 */
package ncr.res.mobilepos.cashAbstract.resource;


import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.cashAbstract.dao.ICashAbstractDAO;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.xebioapi.model.JSONData;

@Path("/cashAbstract")
@Api(value="/cashAbstract", description="入出金摘要情報API")
public class CashAbstractResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private static final String PROG_NAME = "CashAbstractResource";
    @Context
    private ServletContext servletContext;

    public CashAbstractResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * 
     * @param companyId,
     *            storeId, cashFlowDirection
     * @return Tender
     */
    @Path("/getcashAbstract")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="入出金摘要情報取得", response=JSONData.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ")
    })
    public final JSONData getcashAbstract(
            @ApiParam(name="CompanyId", value="会社コード") @QueryParam("CompanyId") final String companyId,
            @ApiParam(name="StoreId", value="店舗コード") @QueryParam("StoreId") final String storeId, 
            @ApiParam(name="CashFlowDirection", value="入出金区分") @QueryParam("CashFlowDirection") final String cashFlowDirection,
            @ApiParam(name="TenderId", value="種別コード") @QueryParam("TenderId") final String tenderId,
            @ApiParam(name="TenderType", value="支払種別") @QueryParam("TenderType") final String tenderType) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        .println("CompanyId", companyId)
        .println("StoreId", storeId)
        .println("CashFlowDirection", cashFlowDirection)
        .println("TenderId", tenderId)
        .println("TenderType", tenderType);

        JSONData tender = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(companyId, storeId, cashFlowDirection,tenderId,tenderType)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                tender.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                tender.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return tender;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICashAbstractDAO iCashAbstarctDAO = sqlServer.getCashAbstractDAO();

            tender = iCashAbstarctDAO.getcashAbstract(companyId, storeId, cashFlowDirection, tenderId,tenderType);
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get cashAbstract infomation.", e);
            tender.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            tender.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get cashAbstract infomation.", ex);
            tender.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            tender.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(tender);
        }
        return tender;
    }
}