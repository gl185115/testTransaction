/**
 *
 */
/**
 * @author wml
 *
 */
package ncr.res.mobilepos.creditcard.resource;

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
import ncr.res.mobilepos.creditcard.dao.ICreditCardAbstractDAO;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.webserviceif.model.JSONData;

@Path("/creditcard")
@Api(value="/creditcard", description="クレジットカード情報")
public class CreditCardResource {

    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private static final String PROG_NAME = "CreditCardResource";
    @Context
    private ServletContext servletContext;

    public CreditCardResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     *
     * @param companyId,
     * @return creditcardcompanyinfo
     */
    @Path("/getcreditcompanyinfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="クレジット会社情報", response=JSONData.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ")
    })
    public final JSONData getcashAbstract(
            @ApiParam(name="CompanyId", value="会社コード") @QueryParam("CompanyId") final String companyId) {
        String functionName = "getcashAbstract";
        tp.methodEnter(functionName)
        .println("CompanyId", companyId);

        JSONData creditcardcompanyinfo = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(companyId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                creditcardcompanyinfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                creditcardcompanyinfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                creditcardcompanyinfo.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return creditcardcompanyinfo;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICreditCardAbstractDAO iCashAbstarctDAO = sqlServer.getCreditCardInfo();
            creditcardcompanyinfo = iCashAbstarctDAO.getCreditCardCompayInfo(companyId);
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get cashAbstract infomation.", e);
            creditcardcompanyinfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            creditcardcompanyinfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            creditcardcompanyinfo.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get cashAbstract infomation.", ex);
            creditcardcompanyinfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            creditcardcompanyinfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            creditcardcompanyinfo.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(creditcardcompanyinfo);
        }
        return creditcardcompanyinfo;
    }
}