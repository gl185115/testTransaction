/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.giftcard.toppan.dao.CenterAccess;
import ncr.res.giftcard.toppan.dao.ITxlCardFailureDAO;
import ncr.res.giftcard.toppan.model.Config;
import ncr.res.giftcard.toppan.model.Message;
import ncr.res.giftcard.toppan.model.MessageBuilder;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.giftcard.factory.ToppanGiftCardConfigFactory;
import ncr.res.mobilepos.giftcard.model.GiftCard;
import ncr.res.mobilepos.giftcard.model.GiftResult;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Toppan Giftcard center interface class.
 */
@Path("/giftcard")
@Api(value="/giftcard", description="ギフトカード情報API")
public class ToppanGiftcardResource {
	
	/** 
	 * A private member variable used for the servlet context. 
	 */
    @Context
    private ServletContext context = null;;
    
    /** 
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    
    /** 
     * The instance of the trace debug printer. 
     */
    private Trace.Printer tp = null;
    
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "TpnGiftR";

    private final Config toppanGiftcardConfig;

    /**
     * Default Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     */
    public ToppanGiftcardResource() {
    	tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    	toppanGiftcardConfig = ToppanGiftCardConfigFactory.getInstance();
    }

    /**
     * The method called by the Web Service to query giftcard current amount.
     * @param storeId The StoreID
     * @param workstationId The Terminal ID
     * @param transactionId The Transaction Number
     * @param test The Test Flag
     * @param jsonItem The GiftCard Information Model
     * @return GiftResult    The GiftResult Object
     */
    @Path("/query")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ギフト照会", response=GiftResult.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_PARSE, message="データ転換エラー")
    })
    public GiftResult QueryMember(
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeId,
    		@ApiParam(name="workstationid", value="ターミナル番号") @FormParam("workstationid") final String workstationId,
    		@ApiParam(name="transactionid", value="取引番号") @FormParam("transactionid") final String transactionId,
    		@ApiParam(name="test", value="テストモード") @FormParam("test") final boolean test,
    		@ApiParam(name="giftcard", value="ギフトカード情報") @FormParam("giftcard") final String jsonItem) {
    	
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("storeid", storeId);
        tp.println("workstationid", workstationId);
        tp.println("transactionid", transactionId);
        tp.println("test", test);
        tp.println("giftcard", jsonItem);
        
        GiftResult giftResult = new GiftResult();
        try {
            JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
            GiftCard giftCard = jsonMarshaller.unMarshall(jsonItem, GiftCard.class);
            MessageBuilder.GiftCard messageBuilderGiftCard= createParameter(giftCard);
            Message msg = MessageBuilder.buildQuery(toppanGiftcardConfig, test,
                                    storeId, workstationId, transactionId,
                                    messageBuilderGiftCard);
            tp.println("request", msg);
            giftResult = centerAccess(msg, toppanGiftcardConfig);
        } catch (IOException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
		} catch (Exception e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
		} finally {
			tp.methodExit(giftResult);
        }
        
        return giftResult;
    }

    /**
     * The method called by the Web Service to sell itmes by the card.
     * @param storeId The StoreID
     * @param workstationId The Terminal ID
     * @param transactionId The Transaction Number
     * @param test The Test Flag
     * @param jsonItem The GiftCard Information
     * @return The GiftResult Object
     */
    @Path("/sales")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ギフト売上", response=GiftResult.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_PARSE, message="データ転換エラー")
    })
    public GiftResult sales(
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeId,
    		@ApiParam(name="workstationid", value="ターミナル番号") @FormParam("workstationid") final String workstationId,
    		@ApiParam(name="transactionid", value="取引番号") @FormParam("transactionid") final String transactionId,
    		@ApiParam(name="test", value="テストモード") @FormParam("test") final boolean test,
    		@ApiParam(name="giftcard", value="ギフトカード情報") @FormParam("giftcard") final String jsonItem) {
    	
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("storeid", storeId);
        tp.println("workstationid", workstationId);
        tp.println("transactionid", transactionId);
        tp.println("test", test);
        tp.println("giftcard", jsonItem);
        
        GiftResult giftResult = new GiftResult();
        try {
            JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
            GiftCard giftCard = jsonMarshaller.unMarshall(jsonItem, GiftCard.class);
            MessageBuilder.GiftCard messageBuilderGiftCard= createParameter(giftCard);
            Message msg = MessageBuilder.buildSales(toppanGiftcardConfig, test,
                                    storeId, workstationId, transactionId,
                                    messageBuilderGiftCard);
            tp.println("request", msg);
            giftResult = centerAccess(msg, toppanGiftcardConfig);
        } catch (IOException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
		} catch (Exception e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
		} finally {
			tp.methodExit(giftResult);
        }
        
        return giftResult;
    }

    /**
     * The method called by the Web Service to cancel passed sales.
     * @param storeId The StoreID
     * @param workstationId The Terminal ID
     * @param transactionId The Transaction Number
     * @param test The Test Flag
     * @param jsonItem The GiftCard Information
     * @return The GiftResult Object
     */
    @Path("/cancel")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ギフト売上取消", response=GiftResult.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_PARSE, message="データ転換エラー")
    })
    public GiftResult cancel(
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeId,
    		@ApiParam(name="workstationid", value="ターミナル番号") @FormParam("workstationid") final String workstationId,
    		@ApiParam(name="transactionid", value="取引番号") @FormParam("transactionid") final String transactionId,
    		@ApiParam(name="test", value="テストモード") @FormParam("test") final boolean test,
    		@ApiParam(name="giftcard", value="ギフトカード情報") @FormParam("giftcard") final String jsonItem) {
    	
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("storeid", storeId);
        tp.println("workstationid", workstationId);
        tp.println("transactionid", transactionId);
        tp.println("test", test);
        tp.println("giftcard", jsonItem);
        
        GiftResult giftResult = new GiftResult();
        try {
            JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
            GiftCard giftCard = jsonMarshaller.unMarshall(jsonItem, GiftCard.class);
            MessageBuilder.GiftCard messageBuilderGiftCard= createParameter(giftCard);
            Message msg = MessageBuilder.buildCancel(toppanGiftcardConfig, test,
                                    storeId, workstationId, transactionId,
                                    messageBuilderGiftCard);
            tp.println("request", msg);
            giftResult = centerAccess(msg, toppanGiftcardConfig);
        } catch (IOException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
		} catch (Exception e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
		} finally {
			tp.methodExit(giftResult);
        }
        
        return giftResult;
    }

    /**
     * SendMessage to giftCard Center.
     * @param gc The GiftResult Information Model
     * @return GiftCard The MessageBuilder.GiftCard Object
     */
    MessageBuilder.GiftCard createParameter(GiftCard gc) {
        String amt = gc.getAmount();
        return new MessageBuilder.GiftCard(gc.getJis1(), gc.getJis2(), gc.getPin(),
                   (amt == null || amt.length() == 0) ? 0 : Long.parseLong(amt),
                   gc.getTransactionId());
    }

    /**
     * SendMessage to giftCard Center.
     * @param msg The Message Information
     * @param config The GiftCard Information Model
     * @return GiftResult The GiftResult Object
     */
	private GiftResult centerAccess(Message msg, Config config) {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        GiftResult result = new GiftResult();
        try {
        	DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        	ITxlCardFailureDAO txlCardFailureDAO = daoFactory.getTxlCardFailureDAO();
        	CenterAccess ca = new CenterAccess(config, txlCardFailureDAO);
            Message response = ca.send(msg);
            if (response == null) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
            } else {
                tp.println("response", response);
                result.transfer(response);
            }
        } catch (DaoException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (CenterAccess.ConnectionException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO,
                                    "can't connect to the giftcard center", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
        } catch (Exception e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                                    "CenterAccess exception", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(result);
        }
        
        return result;
    }
    
}

