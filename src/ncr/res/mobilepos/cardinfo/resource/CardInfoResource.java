package ncr.res.mobilepos.cardinfo.resource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.cardinfo.dao.ICardInfoDAO;
import ncr.res.mobilepos.cardinfo.model.CardInfo;
import ncr.res.mobilepos.cardinfo.model.CardTypeInfo;
import ncr.res.mobilepos.cardinfo.model.CardTypeInfoResponse;
import ncr.res.mobilepos.cardinfo.model.MemberInfo;
import ncr.res.mobilepos.cardinfo.model.MemberInfoResponse;
import ncr.res.mobilepos.cardinfo.model.StatusInfo;
import ncr.res.mobilepos.cardinfo.model.StatusInfoResponse;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

@Path("/cardinfo")
public class CardInfoResource {
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    private Trace.Printer tp;
    private DAOFactory daoFactory;
    private static final String PROG_NAME = "CardInfoResource";
    private static final String PATH_NAME = "cardinfo";
    @Context
    private ServletContext servletContext;

    public CardInfoResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    @Path("/getcardclassinfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final CardInfo getCardClassInfo(@QueryParam("companyId") final String companyId,
            @QueryParam("storeId") final String storeId, @QueryParam("cardClassId") final String cardClassId,
            @QueryParam("membershipId") final String membershipId) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId)
                .println("cardClassId", cardClassId).println("membershipId", membershipId);
        CardInfo cardInfo = new CardInfo();

        if (StringUtility.isNullOrEmpty(companyId, storeId)) {
            cardInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(cardInfo.toString());
            return cardInfo;
        }

        try {
            ICardInfoDAO cardInfoDao = daoFactory.getCardInfoDAO();
            cardInfo.setCardClassInfoList(cardInfoDao.getCardClassInfo(companyId, storeId, cardClassId, membershipId));
        } catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
                loggerErrorCode = Logger.RES_EXCEP_GENERAL;
                resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            cardInfo.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get card class info for companyId#" + companyId + ", " + "storeId#" + storeId
                            + " and cardClassId#" + cardClassId + ": " + e.getMessage());
        } finally {
            tp.methodExit(cardInfo.toString());
        }
        return cardInfo;
    }

    @Path("/getcardtypeinfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final CardTypeInfoResponse getCardTypeInfo(@QueryParam("companyId") final String companyId,
            @QueryParam("storeId") final String storeId, @QueryParam("cardTypeNo") final String cardTypeNo) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("cardTypeNo",
                cardTypeNo);

        CardTypeInfoResponse response = new CardTypeInfoResponse();
        List<CardTypeInfo> cardInfos = new ArrayList<CardTypeInfo>();

        if (StringUtility.isNullOrEmpty(companyId, storeId)) {
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(response.toString());
            return response;
        }

        try {
            ICardInfoDAO cardInfoDao = daoFactory.getCardInfoDAO();
            cardInfos = cardInfoDao.getCardTypeInfoByCardType(companyId, storeId, cardTypeNo);
            response.setCardTypeInfos(cardInfos);
        } catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
                loggerErrorCode = Logger.RES_EXCEP_GENERAL;
                resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            response.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode, "Failed to get card class type for companyId#"
                    + companyId + ", " + "storeId#" + storeId + " and cardType#" + cardTypeNo + ": " + e.getMessage());
        } finally {
            tp.methodExit(response.toString());
        }
        return response;
    }

    @Path("/getMemberInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final MemberInfoResponse getMemberInfo(@QueryParam("companyId") final String companyId,
            @QueryParam("storeId") final String storeId, @QueryParam("membershipId") final String membershipId) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("membershipId",
                membershipId);
        MemberInfoResponse response = new MemberInfoResponse();
        MemberInfo memberInfo = new MemberInfo();

        if (StringUtility.isNullOrEmpty(companyId, storeId, membershipId)) {
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(response.toString());
            return response;
        }

        try {
            ICardInfoDAO memberInfoDao = daoFactory.getMemberInfo();
            memberInfo = memberInfoDao.getMemberInfoById(companyId, storeId, membershipId);
            response.setMemberInfo(memberInfo);
        } catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
                loggerErrorCode = Logger.RES_EXCEP_GENERAL;
                resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            response.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get membership info for companyId#" + companyId + ", " + "storeId#" + storeId
                            + " and membershipId#" + membershipId + ": " + e.getMessage());
        } finally {
            tp.methodExit(response.toString());
        }
        return response;
    }
    @Path("/getStatusInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final StatusInfoResponse getStatusInfo(@QueryParam("companyId") final String companyId,
            @QueryParam("storeId") final String storeId, @QueryParam("statusCode") final String statusCode) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId).println("statusCode",
                statusCode);
        StatusInfoResponse response = new StatusInfoResponse();
        StatusInfo statusInfo = new StatusInfo();

        if (StringUtility.isNullOrEmpty(companyId, storeId, statusCode)) {
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(response.toString());
            return response;
        }

        try {
            ICardInfoDAO statusInfoDao = daoFactory.getStatusInfo();
            statusInfo = statusInfoDao.getStatusInfoByCode(companyId, storeId, statusCode);
            response.setStatusInfo(statusInfo);
        } catch (Exception e) {
            String loggerErrorCode = null;
            int resultBaseErrorCode = 0;
            if (e.getCause() instanceof SQLException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DB;
            } else if (e.getCause() instanceof SQLStatementException) {
                loggerErrorCode = Logger.RES_EXCEP_DAO;
                resultBaseErrorCode = ResultBase.RES_ERROR_DAO;
            } else {
                loggerErrorCode = Logger.RES_EXCEP_GENERAL;
                resultBaseErrorCode = ResultBase.RES_ERROR_GENERAL;
            }
            response.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get membership info for companyId#" + companyId + ", " + "storeId#" + storeId
                            + " and statusCode#" + statusCode + ": " + e.getMessage());
        } finally {
            tp.methodExit(response.toString());
        }
        return response;
    }
}