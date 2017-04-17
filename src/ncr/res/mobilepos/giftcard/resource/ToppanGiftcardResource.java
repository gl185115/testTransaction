/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.resource;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;

import ncr.res.giftcard.toppan.dao.CenterAccess;
import ncr.res.giftcard.toppan.dao.ITxlCardFailureDAO;
import ncr.res.giftcard.toppan.model.Config;
import ncr.res.giftcard.toppan.model.Message;
import ncr.res.giftcard.toppan.model.MessageBuilder;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.giftcard.model.GiftCard;
import ncr.res.mobilepos.giftcard.model.GiftResult;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.giftcard.dao.SQLServerTxlCardFailureDAO;
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
    
    private static final String CUSTOM = "CUSTOM";
    private static final String CUSTPARA = "CUSTPARA";
    private static final int DEFAULT_WEBSERVICE_CONNTIMEOUT = 3000;
    private static final int DEFAULT_WEBSERVICE_CONNINTERVAL = 1000;
    private static final int DEFAULT_WEBSERVICE_CONNRETRY = 3;

    /**
     * Default Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     */
    public ToppanGiftcardResource() {
    	tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * The method called by the Web Service to query giftcard current amount.
     * @param storeid        The StoreID
     * @param workstationid  The Terminal ID
     * @param transactionid  The Transaction Number
     * @param test           The Test Flag
     * @param giftcard       The GiftCard Information Model
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
        	Config giftCardConfig = this.getGiftCardConfig();
        	if(giftCardConfig == null){
        		tp.println("No giftCardConfig File found.");
                String errorMessage = "No giftCardConfig File found." + "(" + Config.FILENAME + ")";
                giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                giftResult.setMessage(errorMessage);
        	} else {
        		JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
            	GiftCard giftCard = jsonMarshaller.unMarshall(jsonItem, GiftCard.class);
            	MessageBuilder.GiftCard messageBuilderGiftCard= createParameter(giftCard);
                Message msg = MessageBuilder.buildQuery(giftCardConfig, test,
                                        storeId, workstationId, transactionId,
                                        messageBuilderGiftCard);
                tp.println("request", msg);
                giftResult = centerAccess(msg, giftCardConfig);
        	}
        } catch (IOException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
        } catch (JAXBException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB, logMessage, e);
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
     * @param storeid        The StoreID
     * @param workstationid  The Terminal ID
     * @param transactionid  The Transaction Number
     * @param test           The Test Flag
     * @param giftcard       The GiftCard Information
     * @return GiftResult    The GiftResult Object
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
        	Config giftCardConfig = this.getGiftCardConfig();
        	if(giftCardConfig == null){
        		tp.println("No giftCardConfig File found.");
                String errorMessage = "No giftCardConfig File found." + "(" + Config.FILENAME + ")";
                giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                giftResult.setMessage(errorMessage);
        	} else {
        		JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
            	GiftCard giftCard = jsonMarshaller.unMarshall(jsonItem, GiftCard.class);
            	MessageBuilder.GiftCard messageBuilderGiftCard= createParameter(giftCard);
                Message msg = MessageBuilder.buildSales(giftCardConfig, test,
                                        storeId, workstationId, transactionId,
                                        messageBuilderGiftCard);
                tp.println("request", msg);
                giftResult = centerAccess(msg, giftCardConfig);
        	}
        } catch (IOException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
        } catch (JAXBException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB, logMessage, e);
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
     * @param storeid        The StoreID
     * @param workstationid  The Terminal ID
     * @param transactionid  The Transaction Number
     * @param test           The Test Flag
     * @param giftcard       The GiftCard Information
     * @return GiftResult    The GiftResult Object
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
        	Config giftCardConfig = this.getGiftCardConfig();
        	if(giftCardConfig == null){
        		tp.println("No giftCardConfig File found.");
                String errorMessage = "No giftCardConfig File found." + "(" + Config.FILENAME + ")";
                giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
                giftResult.setMessage(errorMessage);
        	} else {
        		JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
            	GiftCard giftCard = jsonMarshaller.unMarshall(jsonItem, GiftCard.class);
            	MessageBuilder.GiftCard messageBuilderGiftCard= createParameter(giftCard);
                Message msg = MessageBuilder.buildCancel(giftCardConfig, test,
                                        storeId, workstationId, transactionId,
                                        messageBuilderGiftCard);
                tp.println("request", msg);
                giftResult = centerAccess(msg, giftCardConfig);
        	}
        } catch (IOException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO, logMessage, e);
            giftResult.setNCRWSSResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_PARSE);
            giftResult.setMessage(logMessage);
        } catch (JAXBException e) {
        	String logMessage = "decode request param:" + jsonItem;
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB, logMessage, e);
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
     * @param GiftCard  The GiftResult Information Model
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
     * @param Message     The Message Information
     * @param Config      The GiftCard Information Model
     * @return GiftResult The GiftResult Object
     */
    @SuppressWarnings("resource")
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
    
    /**
     * Get GiftCard Configuration Information.
     * @return Config    The Config Object
     * @throws JAXBException 
     */
    private Config getGiftCardConfig() throws JAXBException {

    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        Config giftcardConfig = null;
        String path = System.getenv(CUSTPARA);
        if (path == null) {
            path = System.getenv(CUSTOM);;
        }
        File configFile = new File(path + File.separator + Config.FILENAME);
        try {
            XmlSerializer<Config> serializer = new XmlSerializer<Config>();
            giftcardConfig = serializer.unMarshallXml(configFile, Config.class);
            if(giftcardConfig != null){
                if (giftcardConfig.getConnectionRetryOver() == 0) {
                	giftcardConfig.setConnectionRetryOver(DEFAULT_WEBSERVICE_CONNRETRY);
                }
                if (giftcardConfig.getConnectionTimeout() > DEFAULT_WEBSERVICE_CONNTIMEOUT) {
                	giftcardConfig.setConnectionTimeout(DEFAULT_WEBSERVICE_CONNTIMEOUT);
                }
                if (giftcardConfig.getConnectionRetryInterval() > DEFAULT_WEBSERVICE_CONNINTERVAL) {
                	giftcardConfig.setConnectionRetryInterval(DEFAULT_WEBSERVICE_CONNINTERVAL);
                }
                tp.println("config.connectionTimeout", giftcardConfig.getConnectionTimeout());
                tp.println("config.connectionRetryOver", giftcardConfig.getConnectionRetryOver());
                tp.println("config.connectionRetryInterval", giftcardConfig.getConnectionRetryInterval());
                tp.println("config.nonActivityTimeout", giftcardConfig.getNonActivityTimeout());
                tp.println("config.responseTimeout", giftcardConfig.getResponseTimeout());
                tp.println("config.jdbc", giftcardConfig.getJdbc());
            }
        } catch (JAXBException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB,
                                    "can't read:" + configFile.getAbsolutePath(), e);
            throw e;
        } finally {
            tp.methodExit(giftcardConfig);
        }
        
        return giftcardConfig;
    }
}

