package ncr.res.mobilepos.futurePay.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.futurePay.constants.FuturePayConstants;
import ncr.res.mobilepos.futurePay.dao.IFuturePayDAO;
import ncr.res.mobilepos.futurePay.helper.HTTPBasicAuthorization;
import ncr.res.mobilepos.futurePay.model.FuturePayReturnBean;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * CustomerSearchResource class is a web resourse which provides support for
 * search customer.
 */
@Path("/futurePay")
@Api(value = "/futurePay", description = "会員API")
public class FuturePayResource {

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** */
    private static final String PROG_NAME = "FuturePay";

    private static final String INQUIRY_CARD_INFO = "inquiry_card_info";
    private static final String WITHDRAW_POINT = "withdraw_point";
    private static final String SALES_REWARD = "sales_reward";
    private static final String DEPOSIT_POINT = "deposit_point";
    private static final String INQUIRY_HISTORY = "inquiry_history";
    private static final String COUPON_DELETE = "coupondelete";

    /**
     * constructor.
     */
    public FuturePayResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Get Member Info API
     * 
     * @param terminal_code
     *            : terminal_code
     * @param card_no
     *            : card_no
     * @param extend_expiration
     *            : extend_expiration
     * @return the JSON of member information
     */
    @Path("/get")
    @GET
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ApiOperation(value = "会員情報取得する", response = FuturePayReturnBean.class)
    @ApiResponses(value = {@ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常")})
    public final FuturePayReturnBean get(
            @ApiParam(name = "terminal_code", value = "端末コード") @QueryParam("terminal_code") final String terminal_code,
            @ApiParam(name = "card_no", value = "カード番号") @QueryParam("card_no") final String card_no,
            @ApiParam(name = "extend_expiration", value = "有効期限延長") @QueryParam("extend_expiration") final int extend_expiration) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("terminal_code", terminal_code).println("card_no", card_no).println("extend_expiration",
                extend_expiration);
        FuturePayReturnBean futurePayReturnBean = new FuturePayReturnBean();
        JSONObject request = new JSONObject();
        try {
            // param check
            if (StringUtility.isNullOrEmpty(card_no)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return futurePayReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IFuturePayDAO iFuturePayDAO = sqlServer.getFuturePayDAO();
            Map<String, String> mapReturn = iFuturePayDAO.getPrmSystemConfigValue(FuturePayConstants.MEMBER_CATEGORY);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERUSER))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERPASS))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return futurePayReturnBean;
            }

            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(FuturePayConstants.INQUIRY_CARD_INFO);

            JSONObject attributes = new JSONObject();
            request.put("terminal_code", terminal_code);
            request.put("card_no", card_no);
            attributes.put("extend_expiration", extend_expiration);
            request.put("attributes", attributes);
            // basic authenticate
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes < FuturePayConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()) {
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + card_no
                            + FuturePayConstants.MEMBERINFO_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + FuturePayConstants.MEMBERINFO_TEST);
                    }
                    InputStream in = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    StringBuilder strbReturn = new StringBuilder();
                    String strReadLine = "";
                    lstReturn.add("200");
                    while ((strReadLine = br.readLine()) != null) {
                        strbReturn.append(strReadLine.trim());
                    }
                    br.close();
                    JSONObject response = new JSONObject(strbReturn.toString());
                    String responseCurrent = response.getString(INQUIRY_CARD_INFO);
                    if (null != responseCurrent) {
                        lstReturn.add(responseCurrent.toString());
                    }
                } else {
                    lstReturn = HTTPBasicAuthorization.connection(strbUrl.toString(), request.toString(),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERUSER),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERPASS),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }

                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return futurePayReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            String strReturn = lstReturn.get(1);
            futurePayReturnBean.setResponseJSON(strReturn);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            futurePayReturnBean.setNCRWSSExtendedResultCode(intReturnStatus);
            futurePayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to Member information.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to Member information.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to member information.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to Member information.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to Member information.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setMessage(e.getMessage());
        } finally {
            futurePayReturnBean.setRequestJSON(request.toString());
            tp.methodExit(futurePayReturnBean);
        }

        return futurePayReturnBean;
    }
    /**
     * Get Member Info API
     * 
     * @param api
     *            : api
     * @param terminal_code
     *            : terminal_code
     * @param card_no
     *            : card_no
     * @param businessDate
     *            : businessDate
     * @param sequenceNumber
     *            : sequenceNumber
     * @param transactionType
     *            : transactionType
     * @param slip_no
     *            : slip_no
     * @param pin_code
     *            : pin_code
     * @param operation_type
     *            : operation_type
     * @param amount
     *            : amount
     * @param terminal_deal_time
     *            : terminal_deal_time
     * @param limit
     *            : limit
     * @param attributes
     *            : attributes
     * @return the JSON of member information
     */
    @Path("/update")
    @GET
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ApiOperation(value = "会員情報更新する", response = FuturePayReturnBean.class)
    @ApiResponses(value = {@ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常")})
    public final FuturePayReturnBean update(@ApiParam(name = "api", value = "API") @QueryParam("api") final String api,
            @ApiParam(name = "terminal_code", value = "端末コード") @QueryParam("terminal_code") final String terminal_code,
            @ApiParam(name = "card_no", value = "カード番号") @QueryParam("card_no") final String card_no,
            @ApiParam(name = "businessDate", value = "日付") @QueryParam("businessDate") final String businessDate,
            @ApiParam(name = "sequenceNumber", value = "取引No") @QueryParam("sequenceNumber") final String sequenceNumber,
            @ApiParam(name = "transactionType", value = "取引種別") @QueryParam("transactionType") final String transactionType,
            @ApiParam(name = "slip_no", value = "伝票番号") @QueryParam("slip_no") final String slip_no,
            @ApiParam(name = "pin_code", value = "PINコード") @QueryParam("pin_code") final String pin_code,
            @ApiParam(name = "operation_type", value = "操作種別") @QueryParam("operation_type") final int operation_type,
            @ApiParam(name = "retryTime", value = "リトライ回数") @QueryParam("retryTime") final int retryTime,
            @ApiParam(name = "amount", value = "ポイント") @QueryParam("amount") final int amount,
            @ApiParam(name = "terminal_deal_time", value = "端末取引日時") @QueryParam("terminal_deal_time") final String terminal_deal_time,
            @ApiParam(name = "limit", value = "最大件数") @QueryParam("limit") final String limit,
            @ApiParam(name = "attributes", value = "付加属性") @QueryParam("attributes") final String attributes) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("terminal_code", terminal_code).println("card_no", card_no);
        FuturePayReturnBean futurePayReturnBean = new FuturePayReturnBean();
        JSONObject request = new JSONObject();
        Boolean dealSerialNoFlag = false;
        try {
            // param check
            if (StringUtility.isNullOrEmpty(card_no) || StringUtility.isNullOrEmpty(api)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return futurePayReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IFuturePayDAO iFuturePayDAO = sqlServer.getFuturePayDAO();
            Map<String, String> mapReturn = iFuturePayDAO.getPrmSystemConfigValue(FuturePayConstants.MEMBER_CATEGORY);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERUSER))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERPASS))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return futurePayReturnBean;
            }

            StringBuilder strbUrl = new StringBuilder();
            StringBuilder deal_serial_no = new StringBuilder();
            strbUrl.append(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERURI));
            request.put("terminal_code", terminal_code);
            request.put("card_no", card_no);
            request.put("slip_no", slip_no);
            request.put("pin_code", pin_code);
            request.put("operation_type", operation_type);
            request.put("attributes", attributes);
            switch (api) {
                case WITHDRAW_POINT :
                    dealSerialNoFlag = true;
                    strbUrl.append(FuturePayConstants.WITHDRAW_POINT);
                    request.put("amount", amount);
                    break;
                case SALES_REWARD :
                    String terminalDealTime = null;
                    dealSerialNoFlag = true;
                    strbUrl.append(FuturePayConstants.SALES_REWARD);
                    if (null == terminal_deal_time) {
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        terminalDealTime = formatter.format(new Date()).toString();
                    } else {
                        terminalDealTime = terminal_deal_time;
                    }
                    request.put("terminal_deal_time", terminalDealTime);
                    request.put("amount", amount);
                    break;
                case DEPOSIT_POINT :
                    dealSerialNoFlag = true;
                    strbUrl.append(FuturePayConstants.DEPOSIT_POINT);
                    request.put("amount", amount);
                    break;
                case INQUIRY_HISTORY :
                    strbUrl.append(FuturePayConstants.INQUIRY_HISTORY);
                    request.put("limit", limit);
                    break;
            }

            // basic authenticate
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes <= FuturePayConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (dealSerialNoFlag) {
                    int times = retryTime + retryTimes + 1;
                    if (times > 999) {
                        times = 1;
                    }
                    deal_serial_no.append(businessDate);
                    deal_serial_no.append(sequenceNumber);
                    deal_serial_no.append(String.format("%03d", times));
                    deal_serial_no.append(transactionType);
                    request.put("deal_serial_no", deal_serial_no);
                }
                if (GlobalConstant.getMemberServerDebug()) {
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + card_no
                            + FuturePayConstants.MEMBERINFO_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + FuturePayConstants.MEMBERINFO_TEST);
                    }
                    InputStream in = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    StringBuilder strbReturn = new StringBuilder();
                    String strReadLine = "";
                    lstReturn.add("200");
                    while ((strReadLine = br.readLine()) != null) {
                        strbReturn.append(strReadLine.trim());
                    }
                    br.close();
                    JSONObject response = new JSONObject(strbReturn.toString());
                    String responseCurrent = response.getString(api);
                    if (null != responseCurrent) {
                        if (0 != operation_type) {
                            response = new JSONObject(responseCurrent);
                            responseCurrent = response.getString(String.valueOf(operation_type));
                        }
                        lstReturn.add(responseCurrent.toString());
                    }
                } else {
                    lstReturn = HTTPBasicAuthorization.connection(strbUrl.toString(), request.toString(),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERUSER),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERPASS),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }

                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return futurePayReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            String strReturn = lstReturn.get(1);
            futurePayReturnBean.setResponseJSON(strReturn);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            futurePayReturnBean.setNCRWSSExtendedResultCode(intReturnStatus);
            futurePayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to update Member.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to update Member.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to update member.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to update Member.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to update Member.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setMessage(e.getMessage());
        } finally {
            futurePayReturnBean.setRequestJSON(request.toString());
            tp.methodExit(futurePayReturnBean);
        }
        return futurePayReturnBean;
    }

    /**
     * Get Member Info API
     * 
     * @param terminal_code
     *            : terminal_code
     * @param card_no
     *            : card_no
     * @param businessDate
     *            : businessDate
     * @param sequenceNumber
     *            : sequenceNumber
     * @param transactionType
     *            : transactionType
     * @param slip_no
     *            : slip_no
     * @param pin_code
     *            : pin_code
     * @param terminal_deal_time
     *            : terminal_deal_time
     * @param limit
     *            : limit
     * @param attributes
     *            : attributes
     * @return the JSON of member information
     */
    @Path("/history")
    @GET
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ApiOperation(value = "会員履歴情報取得する", response = FuturePayReturnBean.class)
    @ApiResponses(value = {@ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常")})
    public final FuturePayReturnBean update(
            @ApiParam(name = "terminal_code", value = "端末コード") @QueryParam("terminal_code") final String terminal_code,
            @ApiParam(name = "card_no", value = "カード番号") @QueryParam("card_no") final String card_no,
            @ApiParam(name = "businessDate", value = "日付") @QueryParam("businessDate") final String businessDate,
            @ApiParam(name = "sequenceNumber", value = "取引No") @QueryParam("sequenceNumber") final String sequenceNumber,
            @ApiParam(name = "transactionType", value = "取引種別") @QueryParam("transactionType") final String transactionType,
            @ApiParam(name = "retryTime", value = "リトライ回数") @QueryParam("retryTime") final int retryTime,
            @ApiParam(name = "slip_no", value = "伝票番号") @QueryParam("slip_no") final String slip_no,
            @ApiParam(name = "pin_code", value = "PINコード") @QueryParam("pin_code") final String pin_code,
            @ApiParam(name = "limit", value = "最大件数") @QueryParam("limit") final int limit,
            @ApiParam(name = "attributes", value = "付加属性") @QueryParam("attributes") final String attributes) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("terminal_code", terminal_code).println("card_no", card_no);
        FuturePayReturnBean futurePayReturnBean = new FuturePayReturnBean();
        JSONObject request = new JSONObject();
        Boolean dealSerialNoFlag = true;
        try {
            // param check
            if (StringUtility.isNullOrEmpty(card_no)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return futurePayReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IFuturePayDAO iFuturePayDAO = sqlServer.getFuturePayDAO();
            Map<String, String> mapReturn = iFuturePayDAO.getPrmSystemConfigValue(FuturePayConstants.MEMBER_CATEGORY);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERUSER))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERPASS))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return futurePayReturnBean;
            }

            StringBuilder strbUrl = new StringBuilder();
            StringBuilder deal_serial_no = new StringBuilder();
            strbUrl.append(mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(FuturePayConstants.INQUIRY_HISTORY);
            request.put("terminal_code", terminal_code);
            request.put("card_no", card_no);
            request.put("slip_no", slip_no);
            request.put("pin_code", pin_code);
            request.put("attributes", attributes);
            request.put("limit", limit);

            // basic authenticate
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes <= FuturePayConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (dealSerialNoFlag) {
                    int times = retryTime + retryTimes + 1;
                    if (times > 999) {
                        times = 1;
                    }
                    deal_serial_no.append(businessDate);
                    deal_serial_no.append(sequenceNumber);
                    deal_serial_no.append(String.format("%03d", times));
                    deal_serial_no.append(transactionType);
                    request.put("deal_serial_no", deal_serial_no);
                }
                if (GlobalConstant.getMemberServerDebug()) {
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + card_no
                            + FuturePayConstants.MEMBERINFO_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + FuturePayConstants.MEMBERINFO_TEST);
                    }
                    InputStream in = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    StringBuilder strbReturn = new StringBuilder();
                    String strReadLine = "";
                    lstReturn.add("200");
                    while ((strReadLine = br.readLine()) != null) {
                        strbReturn.append(strReadLine.trim());
                    }
                    br.close();
                    JSONObject response = new JSONObject(strbReturn.toString());
                    String responseCurrent = response.getString(INQUIRY_HISTORY);
                    if (null != responseCurrent) {
                        lstReturn.add(responseCurrent.toString());
                    }
                } else {
                    lstReturn = HTTPBasicAuthorization.connection(strbUrl.toString(), request.toString(),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERUSER),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERPASS),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(FuturePayConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }

                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return futurePayReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            String strReturn = lstReturn.get(1);
            futurePayReturnBean.setResponseJSON(strReturn);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            futurePayReturnBean.setNCRWSSExtendedResultCode(intReturnStatus);
            futurePayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get Member history.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to get Member history.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to get member history.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to get Member history.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get Member history.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setMessage(e.getMessage());
        } finally {
            futurePayReturnBean.setRequestJSON(request.toString());
            tp.methodExit(futurePayReturnBean);
        }
        return futurePayReturnBean;
    }

    /**
     * Clearing of Coupon used by member.
     * 
     * @param companyId
     *            : companyId
     * @param storeId
     *            : storeId
     * @param terminalId
     *            : terminalId
     * @param couponId:
     *            : couponId
     * @param memberId:
     *            : memberId
     * @return the result of the clearing of the coupon
     */
    @Path("/coupondelete")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
    @ApiOperation(value = "クーポン消し込み処理", response = FuturePayReturnBean.class)
    @ApiResponses(value = {@ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "データは見つからない"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL異常"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常")})
    public final FuturePayReturnBean couponDelete(
    		@ApiParam(name = "companyId", value = "企業コード") @FormParam("companyId") final String companyId,
    		@ApiParam(name = "storeId", value = "店舗コード") @FormParam("storeId") final String storeId,
            @ApiParam(name = "terminalId", value = "端末コード") @FormParam("terminalId") final String terminalId,
            @ApiParam(name = "couponId", value = "クーポン番号") @FormParam("couponId") final String couponId,
            @ApiParam(name = "memberId", value = "会員番号") @FormParam("memberId") final String memberId) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
        	.println("storeId", storeId)
        	.println("terminalId", terminalId)
        	.println("couponId", couponId)
        	.println("memberId", memberId);

        FuturePayReturnBean futurePayReturnBean = new FuturePayReturnBean();
        JSONObject request = new JSONObject();
        try {
            // param check
        	if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId, couponId, memberId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                futurePayReturnBean.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return futurePayReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IFuturePayDAO iFuturePayDAO = sqlServer.getFuturePayDAO();
            Map<String, String> mapReturn = iFuturePayDAO.getPrmSystemConfigValue(FuturePayConstants.COUPONSERVER_CATEGORY);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_DEBUG))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_USER))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_PASS))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_CONNECT_TIMEOUT))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_TIMEOUT))
                    || StringUtility.isNullOrEmpty(mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_URI))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                futurePayReturnBean.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return futurePayReturnBean;
            }
            
            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_URI));

            request.put("futurepay_id", memberId);
            request.put("coupon_code", couponId);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            request.put("used_dt", sdfDate.format(new Date()));
            // basic authenticate
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes < FuturePayConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getCouponServerDebug()) {
                	File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + couponId
                            + FuturePayConstants.COUPON_DELETE_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + FuturePayConstants.COUPON_DELETE_TEST);
                    }
                    InputStream in = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    StringBuilder strbReturn = new StringBuilder();
                    String strReadLine = "";
                    lstReturn.add("200");
                    while ((strReadLine = br.readLine()) != null) {
                        strbReturn.append(strReadLine.trim());
                    }
                    br.close();
                    JSONObject response = new JSONObject(strbReturn.toString());
                    String responseCurrent = response.getString(COUPON_DELETE);
                    if (null != responseCurrent) {
                        lstReturn.add(responseCurrent.toString());
                    }
                } else {
                	lstReturn = HTTPBasicAuthorization.connection(
            			strbUrl.toString(), 
            			request.toString(),
                        mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_USER),
                        mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_PASS),
                        mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_TIMEOUT),
                        mapReturn.get(FuturePayConstants.KEYID_COUPONSERVER_CONNECT_TIMEOUT)
                    ); 
                }

                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                futurePayReturnBean.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return futurePayReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            String strReturn = lstReturn.get(1);
            futurePayReturnBean.setResponseJSON(strReturn);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            futurePayReturnBean.setNCRWSSExtendedResultCode(intReturnStatus);
            futurePayReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to perform coupon delete.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to perform coupon delete.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            futurePayReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to perform coupon delete.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                futurePayReturnBean.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to perform coupon delete.", e);
                futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                futurePayReturnBean.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to perform coupon delete.", e);
            futurePayReturnBean.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            futurePayReturnBean.setMessage(e.getMessage());
        } finally {
            futurePayReturnBean.setRequestJSON(request.toString());
            tp.methodExit(futurePayReturnBean);
        }

        return futurePayReturnBean;
    }
}
