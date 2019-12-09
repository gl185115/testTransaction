package ncr.res.mobilepos.barcode.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import ncr.res.mobilepos.barcode.dao.IBarCodeDAO;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.webserviceif.model.JSONData;

@Path("/barcode")
@Api(value="/barcode", description="�o�[�R�[�h�֘AAPI")
public class BarCodeResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private static final String PROG_NAME = "discountInfoResource";
    @Context
    private ServletContext servletContext;

    public BarCodeResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * 
     * @param companyId
     * @param storeId
     * @param cardType
     * @param seqNo
     * @param discountType
     * @return discountInfo
     */
    @Path("/getDiscountInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�l���������擾",response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o"),
        })
    public final JSONData getDiscountInfo(@ApiParam(name="CompanyId", value="��ЃR�[�h") @QueryParam("CompanyId") final String companyId,
    		@ApiParam(name="StoreId", value="�X�ԍ�") @QueryParam("StoreId") final String storeId,
    		@ApiParam(name="CardType", value="��������R�[�h") @QueryParam("CardType") final String cardType,
    		@ApiParam(name="SeqNo", value="�V�[�P���X�ԍ�") @QueryParam("SeqNo") final String seqNo,
    		@ApiParam(name="DiscountType", value="�������") @QueryParam("DiscountType") final String discountType) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                .println("CardType", cardType).println("SeqNo", seqNo).println("DiscountType", discountType);
        JSONData discountInfo = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(companyId, storeId, cardType, seqNo)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                discountInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                discountInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                discountInfo.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return discountInfo;
            }
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IBarCodeDAO iBarCodeDAO = sqlServer.getDiscountInfo();
            discountInfo = iBarCodeDAO.getDiscountInfo(companyId, storeId, cardType, seqNo, discountType);
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get discount infomation.", e);
            discountInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            discountInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            discountInfo.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get discount infomation.", ex);
            discountInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            discountInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            discountInfo.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(discountInfo);
        }
        return discountInfo;
    }
    
    /**
     * 
     * @param companyId
     * @param storeId
     * @param cardType
     * @param seqNo
     * @param discountType
     * @return discountInfo
     */
    @Path("/isMemberCard")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="����J�[�h����",response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
        })
    public final JSONData isMemberCard(@ApiParam(name="cardCode", value="�J�[�h�R�[�h") @FormParam("cardCode") final String cardCode) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("cardCode", cardCode);
        JSONData jsondata = new JSONData();
        try {
            if (StringUtility.isNullOrEmpty(cardCode)) {
                jsondata.setJsonObject("false");
                return jsondata;
            }
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IBarCodeDAO iBarCodeDAO = sqlServer.getDiscountInfo();
            jsondata.setJsonObject(iBarCodeDAO.isMemberCard(cardCode).toString());
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get discount infomation.", e);
            jsondata.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            jsondata.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            jsondata.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get discount infomation.", ex);
            jsondata.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            jsondata.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            jsondata.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(jsondata);
        }
        return jsondata;
    }
}