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
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;

import ncr.res.giftcard.toppan.dao.CenterAccess;
import ncr.res.giftcard.toppan.dao.ITxlCardFailureDAO;
import ncr.res.giftcard.toppan.model.Config;
import ncr.res.giftcard.toppan.model.Message;
import ncr.res.giftcard.toppan.model.MessageBuilder;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.giftcard.model.GiftCard;
import ncr.res.mobilepos.giftcard.model.GiftResult;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.giftcard.dao.SQLServerTxlCardFailureDAO;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Toppan Giftcard center interface class.
 */
@Path("/giftcard")
@Api(value="/giftcard", description="ギフトカード情報API")
public class ToppanGiftcardResource {
    static final String PROGNAME = "TpnGiftR";
    static final String DEFAULT_PATH = "d:\\software\\ncr\\res\\custom";
    static final String CUSTPARA = "CUSTPARA";
    static final int DEFAULT_WEBSERVICE_CONNTIMEOUT = 3000;
    static final int DEFAULT_WEBSERVICE_CONNINTERVAL = 1000;
    static final int DEFAULT_WEBSERVICE_CONNRETRY = 3;

    static Config config;

    public ToppanGiftcardResource() throws JAXBException {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        tp.methodEnter("ctor");
        snap = (SnapLogger) SnapLogger.getInstance();
        synchronized (PROGNAME) {
            if (config == null) {
                String path = System.getenv(CUSTPARA);
                if (path == null) {
                    path = DEFAULT_PATH;
                }
                File configFile = new File(path + File.separator + Config.FILENAME);
                try {
                    XmlSerializer<Config> serializer = new XmlSerializer<Config>();
                    config = serializer.unMarshallXml(configFile, Config.class);
                    if (config.getConnectionRetryOver() == 0) {
                        config.setConnectionRetryOver(DEFAULT_WEBSERVICE_CONNRETRY);
                    }
                    if (config.getConnectionTimeout() > DEFAULT_WEBSERVICE_CONNTIMEOUT) {
                        config.setConnectionTimeout(DEFAULT_WEBSERVICE_CONNTIMEOUT);
                    }
                    if (config.getConnectionRetryInterval() > DEFAULT_WEBSERVICE_CONNINTERVAL) {
                        config.setConnectionRetryInterval(DEFAULT_WEBSERVICE_CONNINTERVAL);
                    }
                    tp.println("config.connectionTimeout", config.getConnectionTimeout());
                    tp.println("config.connectionRetryOver", config.getConnectionRetryOver());
                    tp.println("config.connectionRetryInterval", config.getConnectionRetryInterval());
                    tp.println("config.nonActivityTimeout", config.getNonActivityTimeout());
                    tp.println("config.responseTimeout", config.getResponseTimeout());
                    tp.println("config.jdbc", config.getJdbc());
                } catch (JAXBException e) {
                    LOGGER.logSnapException(PROGNAME, Logger.RES_EXCEP_JAXB,
                                            "can't read:" + configFile.getAbsolutePath(), e);
                    throw e;
                }
            }
        }
        tp.methodExit();
    }

    /*
     * query current amount.
     */
    @Path("/query")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ギフト照会", response=GiftResult.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_PARSE, message="データ転換エラー")
    })
    public GiftResult QueryMember(
            @FormParam("storeid") String storeId,
            @FormParam("workstationid") String workstationId,
            @FormParam("transactionid") String transactionId,
            @FormParam("test") boolean test,
            @FormParam("giftcard") String jsonItem) {
        tp.methodEnter("QueryMember");
        tp.println("storeid", storeId);
        tp.println("workstationid", workstationId);
        tp.println("transactionid", transactionId);
        tp.println("test", test);
        tp.println("giftcard", jsonItem);
        JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
        try {
            Message msg = MessageBuilder.buildQuery(config, test,
                                    storeId, workstationId, transactionId,
                createParameter(jsonMarshaller.unMarshall(jsonItem, GiftCard.class)));
            tp.println("request", msg);
            return (GiftResult)tp.methodExit(centerAccess(msg));
        } catch (IOException e) {
            LOGGER.logSnapException(PROGNAME, Logger.RES_EXCEP_IO,
                                    "decode request param:" + jsonItem, e);
        }
        return (GiftResult)tp.methodExit(new GiftResult(ResultBase.RES_ERROR_PARSE));
    }

    /*
     * sell itmes by the card.
     */
    @Path("/sales")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ギフト売上", response=GiftResult.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_PARSE, message="データ転換エラー")
    })
    public GiftResult sales(
            @FormParam("storeid") String storeId,
            @FormParam("workstationid") String workstationId,
            @FormParam("transactionid") String transactionId,
            @FormParam("test") boolean test,
            @FormParam("giftcard") String jsonItem) {
        tp.methodEnter("sales");
        JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
        try {
            Message msg = MessageBuilder.buildSales(config, test,
                                    storeId, workstationId, transactionId,
                createParameter(jsonMarshaller.unMarshall(jsonItem, GiftCard.class)));
            return (GiftResult)tp.methodExit(centerAccess(msg));
        } catch (IOException e) {
            LOGGER.logSnapException(PROGNAME, Logger.RES_EXCEP_IO,
                                    "decode request param:" + jsonItem, e);
        }
        return (GiftResult)tp.methodExit(new GiftResult(ResultBase.RES_ERROR_PARSE));
    }

    /*
     * cancel passed sales.
     */
    @Path("/cancel")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ギフト売上取消", response=GiftResult.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_PARSE, message="データ転換エラー")
    })
    public GiftResult cancel(
            @FormParam("storeid") String storeId,
            @FormParam("workstationid") String workstationId,
            @FormParam("transactionid") String transactionId,
            @FormParam("test") boolean test,
            @FormParam("giftcard") String jsonItem) {
        tp.methodEnter("Cancel");
        JsonMarshaller<GiftCard> jsonMarshaller = new JsonMarshaller<GiftCard>();
        try {
            Message msg = MessageBuilder.buildCancel(config, test,
                                    storeId, workstationId, transactionId,
                createParameter(jsonMarshaller.unMarshall(jsonItem, GiftCard.class)));
            return (GiftResult)tp.methodExit(centerAccess(msg));
        } catch (IOException e) {
            LOGGER.logSnapException(PROGNAME, Logger.RES_EXCEP_IO,
                                    "decode request param:" + jsonItem, e);
        }
        return (GiftResult)tp.methodExit(new GiftResult(ResultBase.RES_ERROR_PARSE));
    }

    /**
     * factory method for creating dao.
     */
    protected ITxlCardFailureDAO getDaoInstance() throws DaoException {
        return new SQLServerTxlCardFailureDAO();
    }

    MessageBuilder.GiftCard createParameter(GiftCard gc) {
        String amt = gc.getAmount();
        return new MessageBuilder.GiftCard(gc.getJis1(), gc.getJis2(), gc.getPin(),
                   (amt == null || amt.length() == 0) ? 0 : Long.parseLong(amt),
                   gc.getTransactionId());
    }

    GiftResult centerAccess(Message msg) {
        tp.methodEnter("centerAccess");
        GiftResult result = new GiftResult();
        try (CenterAccess ca = new CenterAccess(config, getDaoInstance())) {
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
            LOGGER.logSnapException(PROGNAME, Logger.RES_EXCEP_IO,
                                    "can't connect to the giftcard center", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
        } catch (Exception e) {
            LOGGER.logSnapException(PROGNAME, Logger.RES_EXCEP_GENERAL,
                                    "CenterAccess exception", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        }
        return (GiftResult)tp.methodExit(result);
    }


    @Context
    ServletContext context; //to access the web.xml
    static final Logger LOGGER = (Logger) Logger.getInstance();
    Trace.Printer tp = null;
    SnapLogger snap;
}

