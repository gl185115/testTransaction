package ncr.res.mobilepos.intaPay.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.intaPay.constants.IntaPayConstants;
import ncr.res.mobilepos.intaPay.dao.IIntaPayDAO;
import ncr.res.mobilepos.intaPay.factory.IntaPayFactory;
import ncr.res.mobilepos.intaPay.helper.HTTPClientCertAuthorization;
import ncr.res.mobilepos.intaPay.helper.Util;
import ncr.res.mobilepos.intaPay.model.BaseModel;
import ncr.res.mobilepos.intaPay.model.IntaPayReturnBean;
import ncr.res.mobilepos.intaPay.model.IntaPayStoreConfig;
import ncr.res.mobilepos.intaPay.model.IntaPaySystemConfig;
import ncr.res.mobilepos.intaPay.model.PaymentInputModel;
import ncr.res.mobilepos.intaPay.model.PaymentQueryModel;
import ncr.res.mobilepos.intaPay.model.RefundInputModel;
import ncr.res.mobilepos.intaPay.model.RefundQueryModel;
import ncr.res.mobilepos.model.ResultBase;

/**
 * IntaPayResource class is a web resourse which provides support for search
 * intaPay.
 */
@Path("/IntaPay")
@Api(value = "/IntaPay", description = "intaPayAPI")
public class IntaPayResource {

    /** A private intaPay variable used for logging the class implementations. */
    private static final Logger LOGGER = Logger.getInstance();

    /** */
    private static final String PROG_NAME = "intaPay";

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    private static Map<String, IntaPayStoreConfig> STROE_PARAM_MAP = new HashMap<String, IntaPayStoreConfig>();

    /**
     * constructor.
     */
    public IntaPayResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    private String getResponse(BaseModel model, String url, String dubugType, int training, String companyId,
            String storeId) throws Exception {
        IntaPaySystemConfig intaPaySystemConfig = IntaPayFactory.getIntaPaySystemConfig();
        IntaPayStoreConfig intaPayStoreConfig = getIntaPayStoreConfig(companyId, storeId);
        String res = null;
        String apiKey = intaPayStoreConfig != null ? intaPayStoreConfig.getAPIKey() : intaPaySystemConfig.getApiKey();
        String mchCode = intaPayStoreConfig != null ? intaPayStoreConfig.getMchCode()
                : intaPaySystemConfig.getMchCode();
        if (intaPaySystemConfig.isDebug() || 1 == training) {
            File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + IntaPayConstants.INTAPAYINFO_TEST
                    + IntaPayConstants.INTAPAYINFO_TESTDATATYPE);
            InputStream in = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strbReturn = new StringBuilder();
            String strReadLine = "";
            while ((strReadLine = br.readLine()) != null) {
                strbReturn.append(strReadLine.trim());
            }
            br.close();
            JSONObject test = new JSONObject(strbReturn.toString());
            JSONObject responseStr = new JSONObject();
            responseStr.put("request", HTTPClientCertAuthorization.createRequestStr(model, apiKey, mchCode));
            responseStr.put("response", test.get(dubugType).toString());
            responseStr.put("debug", true);
            res = responseStr.toString();
        } else {
            if (intaPaySystemConfig.isCertuse()) {
                res = HTTPClientCertAuthorization.getResponseByModelWithClientCert(model,
                        intaPaySystemConfig.getGateway_server_url() + url, intaPaySystemConfig.getCertpw(),
                        intaPaySystemConfig.getCertfilepath(), apiKey, mchCode);
            } else {
//            res = getResponseByModel(model, url);
            }
        }
        return res;
    }

    private IntaPayStoreConfig getIntaPayStoreConfig(String companyId, String storeId) throws DaoException {
        String key = companyId.trim() + '_' + storeId.trim();
        if (STROE_PARAM_MAP.get(key) != null) {
            return STROE_PARAM_MAP.get(key);
        } else {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IIntaPayDAO iIntaPayDAO = sqlServer.getIntaPayDAO();
            IntaPayStoreConfig intaPayStoreConfig = iIntaPayDAO.getStoreParam(companyId, storeId);
            if (intaPayStoreConfig != null) {
                STROE_PARAM_MAP.put(key, intaPayStoreConfig);
                return intaPayStoreConfig;
            }
            return null;
        }
    }

    /**
     * @param authCode
     * @param totalFee
     * @return
     */
    @Path("/pay")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "intaPay　支払", response = IntaPayReturnBean.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常") })
    public final IntaPayReturnBean pay(
            @ApiParam(name = "companyId", value = "companyId") @QueryParam("companyId") final String companyId,
            @ApiParam(name = "storeId", value = "storeId") @QueryParam("storeId") final String storeId,
            @ApiParam(name = "training", value = "storeId") @QueryParam("training") final int training,
            @ApiParam(name = "authCode", value = "バーコード") @QueryParam("authCode") final String authCode,
            @ApiParam(name = "totalFee", value = "金額") @QueryParam("totalFee") final String totalFee,
            @ApiParam(name = "body", value = "Body message") @QueryParam("body") final String body,
            @ApiParam(name = "mchTransactionId", value = "加盟店支払取引ID") @QueryParam("mchTransactionId") final String mchTransactionId) {

        String functionName = "pay";
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("storeId", storeId).println("training", training)
                .println("authCode", authCode).println("totalFee", totalFee).println("body", body)
                .println("mchTransactionId", mchTransactionId);
        IntaPayReturnBean intaPayReturnBean = new IntaPayReturnBean();
        IntaPaySystemConfig intaPaySystemConfig = IntaPayFactory.getIntaPaySystemConfig();
        try {
            if (StringUtility.isNullOrEmpty(companyId) || StringUtility.isNullOrEmpty(storeId)
                    || StringUtility.isNullOrEmpty(authCode) || StringUtility.isNullOrEmpty(totalFee)
                    || StringUtility.isNullOrEmpty(mchTransactionId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return intaPayReturnBean;
            }

            String bodyMessage = null;
            if (StringUtility.isNullOrEmpty(body)) {
                bodyMessage = intaPaySystemConfig.getBodyMessage();
            } else {
                bodyMessage = body;
            }
            PaymentInputModel model = new PaymentInputModel();
            model.setBody(bodyMessage);
            model.setMch_transaction_id(mchTransactionId);
            model.setDevice_info(intaPaySystemConfig.getDevice_info());
            model.setTotal_fee(totalFee);
            model.setAuth_code(authCode);
            model.setUnique_distinction_id(Util.getUUID());
            String res = null;
            res = getResponse(model, IntaPayConstants.PAY_URL, IntaPayConstants.DEBUG_TYPE_PAY, training, companyId,
                    storeId);
            
            // sorting the returned data
            if (res == null) {
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return intaPayReturnBean;
            }
            intaPayReturnBean.setResponseJSON(res);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setMessage(e.getMessage());
            }
        } catch (JSONException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } finally {
            tp.methodExit(intaPayReturnBean);
        }

        return intaPayReturnBean;
    }

    /**
     * @param isctransactionid
     * @param mchtransactionid
     * @return
     */
    @Path("/query.pay")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "intaPay　支払検索", response = IntaPayReturnBean.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常") })
    public final IntaPayReturnBean payQuery(
            @ApiParam(name = "companyId", value = "companyId") @QueryParam("companyId") final String companyId,
            @ApiParam(name = "storeId", value = "storeId") @QueryParam("storeId") final String storeId,
            @ApiParam(name = "training", value = "storeId") @QueryParam("training") final int training,
            @ApiParam(name = "iscTransactionId", value = "") @QueryParam("iscTransactionId") final String iscTransactionId,
            @ApiParam(name = "mchTransactionId", value = "加盟店支払取引ID") @QueryParam("mchTransactionId") final String mchTransactionId) {

        String functionName = "payQuery";
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("storeId", storeId).println("training", training);
        tp.println("iscTransactionId", iscTransactionId);
        tp.println("mchtransactionid", mchTransactionId);
        IntaPayReturnBean intaPayReturnBean = new IntaPayReturnBean();

        try {
            if (StringUtility.isNullOrEmpty(companyId) || StringUtility.isNullOrEmpty(storeId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return intaPayReturnBean;
            }
            PaymentQueryModel model = new PaymentQueryModel();
            if (!Util.isEmptyString(iscTransactionId)) {
                model.setISC_transaction_id(iscTransactionId);
            }
            if (!Util.isEmptyString(mchTransactionId)) {
                model.setMCH_transaction_id(mchTransactionId);
            }
            String res = null;
            for (int retryTimes = 0; retryTimes < IntaPayConstants.RETRYTOTAL; retryTimes++) {
                res = getResponse(model, IntaPayConstants.PAYQUERY_URL, IntaPayConstants.DEBUG_TYPE_PAYQUERY, training,
                        companyId, storeId);
                if (!StringUtility.isNullOrEmpty(res))
                    break;
            }
            // sorting the returned data
            if (res == null) {
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return intaPayReturnBean;
            }
            intaPayReturnBean.setResponseJSON(res);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setMessage(e.getMessage());
            }
        } catch (JSONException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } finally {
            tp.methodExit(intaPayReturnBean);
        }

        return intaPayReturnBean;
    }

    /**
     * @param isctransactionid
     * @param mchtransactionid
     * @param refundFee
     * @return
     */
    @Path("/refund")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "intaPay　返金", response = IntaPayReturnBean.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常") })
    public final IntaPayReturnBean refund(
            @ApiParam(name = "companyId", value = "companyId") @QueryParam("companyId") final String companyId,
            @ApiParam(name = "storeId", value = "storeId") @QueryParam("storeId") final String storeId,
            @ApiParam(name = "training", value = "storeId") @QueryParam("training") final int training,
            @ApiParam(name = "iscTransactionId", value = "") @QueryParam("iscTransactionId") final String iscTransactionId,
            @ApiParam(name = "mchTransactionId", value = "加盟店支払取引ID") @QueryParam("mchTransactionId") final String mchTransactionId,
            @ApiParam(name = "refundFee", value = "") @QueryParam("refundFee") final String refundFee) {

        String functionName = "refund";
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("storeId", storeId).println("training", training)
                .println("iscTransactionId", iscTransactionId).println("mchtransactionid", mchTransactionId)
                .println("refundFee", refundFee);
        IntaPayReturnBean intaPayReturnBean = new IntaPayReturnBean();

        try {
            if (StringUtility.isNullOrEmpty(companyId) || StringUtility.isNullOrEmpty(storeId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return intaPayReturnBean;
            }
            RefundInputModel model = new RefundInputModel();
            if (!Util.isEmptyString(iscTransactionId)) {
                model.setIsc_transaction_id(iscTransactionId);
            }
            if (!Util.isEmptyString(mchTransactionId)) {
                model.setMch_transaction_id(mchTransactionId);
            }
            model.setMch_refund_id(Util.getUUIDWithAllNumber(20));
            model.setRefund_fee(refundFee);
            model.setUnique_distinction_id(Util.getUUID());
            String res = null;
            res = getResponse(model, IntaPayConstants.REFUND_URL, IntaPayConstants.DEBUG_TYPE_REFUND, training,
                    companyId, storeId);
            
            // sorting the returned data
            if (res == null) {
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return intaPayReturnBean;
            }
            intaPayReturnBean.setResponseJSON(res);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setMessage(e.getMessage());
            }
        } catch (JSONException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } finally {
            tp.methodExit(intaPayReturnBean);
        }

        return intaPayReturnBean;
    }

    /**
     * @param isctransactionid
     * @param mchtransactionid
     * @return
     */
    @Path("/query.refund")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "intaPay　返金検索", response = IntaPayReturnBean.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常") })
    public final IntaPayReturnBean refundQuery(
            @ApiParam(name = "companyId", value = "companyId") @QueryParam("companyId") final String companyId,
            @ApiParam(name = "storeId", value = "storeId") @QueryParam("storeId") final String storeId,
            @ApiParam(name = "training", value = "storeId") @QueryParam("training") final int training,
            @ApiParam(name = "iscRefundId", value = "") @QueryParam("iscRefundId") final String iscRefundId,
            @ApiParam(name = "mchRefundId", value = "加盟店支払取引ID") @QueryParam("mchRefundId") final String mchRefundId) {

        String functionName = "refundQuery";
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("storeId", storeId).println("training", training)
                .println("iscRefundId", iscRefundId).println("mchRefundId", mchRefundId);
        IntaPayReturnBean intaPayReturnBean = new IntaPayReturnBean();

        try {
            if (StringUtility.isNullOrEmpty(companyId) || StringUtility.isNullOrEmpty(storeId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                intaPayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return intaPayReturnBean;
            }
            RefundQueryModel model = new RefundQueryModel();
            if (!Util.isEmptyString(iscRefundId)) {
                model.setIsc_refund_id(iscRefundId);
            }
            if (!Util.isEmptyString(mchRefundId)) {
                model.setMch_refund_id(mchRefundId);
            }
            String res = null;
            for (int retryTimes = 0; retryTimes < IntaPayConstants.RETRYTOTAL; retryTimes++) {
                res = getResponse(model, IntaPayConstants.REFUNDQUERY_URL, IntaPayConstants.DEBUG_TYPE_REFUNDQUERY,
                        training, companyId, storeId);
                if (!StringUtility.isNullOrEmpty(res))
                    break;
            }
            // sorting the returned data
            if (res == null) {
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                intaPayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return intaPayReturnBean;
            }
            intaPayReturnBean.setResponseJSON(res);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            intaPayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                intaPayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to intaPay information.", e);
                intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                intaPayReturnBean.setMessage(e.getMessage());
            }
        } catch (JSONException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to intaPay information.", e);
            intaPayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            intaPayReturnBean.setMessage(e.getMessage());
        } finally {
            tp.methodExit(intaPayReturnBean);
        }

        return intaPayReturnBean;
    }
}
