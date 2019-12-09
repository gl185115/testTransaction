/**
 * Copyright (c) 2015 NCR Japan Ltd.
 */
package ncr.res.mobilepos.pointserver.resource;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.pointserver.dao.SQLServerPointRequest;
import ncr.res.pointserver.dao.ServerAccess;
import ncr.res.pointserver.model.Config;
import ncr.res.pointserver.model.message.CardChangeRequest;
import ncr.res.pointserver.model.message.CardMergeRequest;
import ncr.res.pointserver.model.message.CardStopRequest;
import ncr.res.pointserver.model.message.HistoryItemRequest;
import ncr.res.pointserver.model.message.HistoryItemResponse;
import ncr.res.pointserver.model.message.HistoryRequest;
import ncr.res.pointserver.model.message.HistoryResponse;
import ncr.res.pointserver.model.message.IssueTicket;
import ncr.res.pointserver.model.message.MemberRequest;
import ncr.res.pointserver.model.message.MemberResponse;
import ncr.res.pointserver.model.message.MemberSearchRequest;
import ncr.res.pointserver.model.message.MemberSearchResponse;
import ncr.res.pointserver.model.message.Message;
import ncr.res.pointserver.model.message.MessageHelper;
import ncr.res.pointserver.model.message.NewMemberRequest;
import ncr.res.pointserver.model.message.PosSales;
import ncr.res.pointserver.model.message.PostPoints;

/**
 * Back Office PointServer interface.
 */
@Path("/POINTAPI")
@Api(value="/POINTAPI", description="ポイントAPI")
public class BoInterface {
    static final String PROGNAME = "BOPTINTF";
    static final String DEFAULT_PATH = "d:\\software\\ncr\\res\\para";
    static final String PARA = "PARA";

    static Config config;

    public BoInterface() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        tp.methodEnter("ctor");
        snap = (SnapLogger) SnapLogger.getInstance();
        synchronized (PROGNAME) {
            if (config == null) {
                String path = System.getenv(PARA);
                if (path == null) {
                    path = DEFAULT_PATH;
                }
                try {
                    config = Config.getConfig(new File(path + File.separator + Config.FILENAME));
                } catch (Exception e) {
                    logger.logSnapException(PROGNAME, "I0", path + File.separator + Config.FILENAME + " error", e);
                }
            }
        }
        assert(config != null);
        tp.methodExit();
    }

    /**
     * Member Search by a card.
     */
    @Path("/TP010A01.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="カード会員検索",response=MemberResponse.class)
    public MemberResponse memberRequest(MemberRequest request) {
        tp.methodEnter("memberRequest");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, false);
        return getResponse(request, Config.SERVICE_MEMBER_INFO, MemberResponse.class);
    }

    /**
     * Members Search by attributes
     */
    @Path("/TP010A02.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="会員属性検索",response=MemberSearchResponse.class)
    public MemberSearchResponse memberSearchRequest(MemberSearchRequest request) {
        tp.methodEnter("memberSearchRequest");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, false);
        return getResponse(request, Config.SERVICE_MEMBER_SEARCH, MemberSearchResponse.class);
    }

    /**
     * POS sales.
     */
    @Path("/TP010A03.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="POS売上", response=MemberResponse.class)
    public Message posSalesRequest(PosSales request) {
        tp.methodEnter("posSales");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, true);
        Message resp = getResponse(request, Config.SERVICE_POS_SALES, Message.class);
        if (resp.getNCRWSSResultCode() != ResultBase.RES_OK) {
            resp = fromStore(request);
        } else if (resp.getError() != null && resp.getError().getErrorcd() != 0) {
            logger.write(Logger.ERROR, PROGNAME, "DC", 
                         "PointSales error from server:" + resp.getError().getErrorcd(),
                         snap.write("response", Message.marshall(resp)));
        }
        return resp;
    }

    /**
     * Post points add.
     */
    @Path("/TP010A04.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="後日ポイント", response=MemberResponse.class)
    public Message postPointsRequest(PostPoints request) {
        tp.methodEnter("postPoints");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, true);
        return getResponse(request, Config.SERVICE_POST_POINT, Message.class);
    }

    /**
     * Card change.
     */
    @Path("/TP010A05.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="カード切替", response=MemberResponse.class)
    public Message cardChangeRequest(CardChangeRequest request) {
        tp.methodEnter("cardChange");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, true);
        return getResponse(request, Config.SERVICE_CARD_CHANGE, Message.class);
    }

    /**
     * Card merge.
     */
    @Path("/TP010A06.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="カード合算", response=MemberResponse.class)
    public Message cardMergeRequest(CardMergeRequest request) {
        tp.methodEnter("cardMerge");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, true);
        return getResponse(request, Config.SERVICE_CARD_MERGE, Message.class);
    }

    /**
     * Card stop.
     */
    @Path("/TP010A07.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="カード停止", response=MemberResponse.class)
    public Message cardStopRequest(CardStopRequest request) {
        tp.methodEnter("cardStop");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, true);
        return getResponse(request, Config.SERVICE_CARD_CHANGE, Message.class);
    }

    /**
     * Card issueing.
     */
    @Path("/TP010A08.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="新規会員登録", response=MemberResponse.class)
    public Message cardIssueRequest(NewMemberRequest request) {
        tp.methodEnter("cardIssue");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, true);
        return getResponse(request, Config.SERVICE_CARD_ISSUE, Message.class);
    }

    /**
     * History search by the member.
     */
    @Path("/TP010A09.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="購買履歴一覧照会", response=MemberResponse.class)
    public HistoryResponse historySearchRequest(HistoryRequest request) {
        tp.methodEnter("historySearchRequest");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, false);
        return getResponse(request, Config.SERVICE_HISTORY, HistoryResponse.class);
    }

    /**
     * History items search by the hitsory.
     */
    @Path("/TP010A10.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="購買履歴明細照会", response=MemberResponse.class)
    public HistoryItemResponse historyItemSearchRequest(HistoryItemRequest request) {
        tp.methodEnter("historyItemSearchRequest");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, false);
        return getResponse(request, Config.SERVICE_HISTORY_ITEM, HistoryItemResponse.class);
    }

    /**
     * Issue a ticket.
     */
    @Path("/TP010A11.aspx")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ポイント券発行", response=MemberResponse.class)
    public Message ticketRequest(IssueTicket request) {
        tp.methodEnter("ticketRequest");
        tp.println("request", request);
        assert(config != null);
        setupCommon(request, config, true);
        return getResponse(request, Config.SERVICE_TICKET, Message.class);
    }

    /**
     * Batch interface.
     */
    @Path("/store")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ポイントシステムオフラインデータ登録", response=Message.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データーベースエラー"),
    })
    public Message fromStore(PosSales request) {
        assert(config != null);
        SQLServerPointRequest spr = new SQLServerPointRequest(config);
        Message resp = new Message();
        try {
            if (request.getResponse() != null) {
                Message orgResponse = request.getResponse();
                request.setResponse(null);
                spr.create(Config.SERVICE_POS_SALES, SQLServerPointRequest.SENT, request, orgResponse);
            } else {
                spr.create(Config.SERVICE_POS_SALES, SQLServerPointRequest.NOT_SENT, request, null);
            }
            resp.setError(new ncr.res.pointserver.model.message.Error());
            resp.setNCRWSSResultCode(ResultBase.RES_OK);
        } catch (SQLException e) {
            logger.logSnapException(PROGNAME, "DD", "failed to save unsent message", e);
            resp.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        }
        return resp;
    }

    void setupCommon(Message msg, Config config, boolean update) {
        MessageHelper mh = new MessageHelper(msg);
        mh.setupCommonFields(config, update);
    }

    <T extends Message, R extends Message> R getResponse(T request, String svcname, Class<R> r) {
        // don't call tp.methodEnter, because it has already done.
        tp.println("request(filled)", request);
        Config.Service svc = config.getService(svcname);
        return r.cast(tp.methodExit(request(request, svc, r)));
    }

    <T extends Message, R extends Message> R request(T message, Config.Service svc, Class<R> r) {
        try {
            ServerAccess access = new ServerAccess(svc.getUris().get(0), config, tp);
            return access.request(message, r);
        } catch (IOException e) {
            logger.logSnapException(PROGNAME, "A0", "failed to recv memberInfo from " + svc.getUris().get(0), e);
        } catch (Exception e) {
            logger.logSnapException(PROGNAME, "A1", "exception at memberInfo for " + svc.getUris().get(0), e);
        }
        try {
            R resp = r.newInstance();
            resp.setNCRWSSResultCode(ResultBase.RES_SERVER_ERROR_5XX);
            resp.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
            return resp;
        } catch (ReflectiveOperationException e) {
            logger.logSnapException(PROGNAME, "A2", "can't create " + r.getName(), e);
            throw new IllegalArgumentException("wrong argument for 'request': " + r.getName(), e);
        }
    }

    @Context
    ServletContext context; //to access the web.xml
    static final Logger logger = (Logger) Logger.getInstance();
    Trace.Printer tp = null;
    SnapLogger snap;
}
