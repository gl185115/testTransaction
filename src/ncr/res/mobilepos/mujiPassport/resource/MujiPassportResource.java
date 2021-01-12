package ncr.res.mobilepos.mujiPassport.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.mujiPassport.constants.MujiPassportConstants;
import ncr.res.mobilepos.mujiPassport.helper.HttpRequestXml;
import ncr.res.mobilepos.mujiPassport.model.AccountInfoRequest;
import ncr.res.mobilepos.mujiPassport.model.AccountInfoResponse;
import ncr.res.mobilepos.mujiPassport.model.PassportPaymentInfoRequest;
import ncr.res.mobilepos.mujiPassport.model.PassportPaymentInfoResponse;
import ncr.res.mobilepos.mujiPassport.model.PayPassportRequest;
import ncr.res.mobilepos.mujiPassport.model.PayPassportResponse;
import ncr.res.mobilepos.mujiPassport.model.RegistTradeRequest;
import ncr.res.mobilepos.mujiPassport.model.RegistTradeResponse;
import ncr.res.mobilepos.mujiPassport.model.SettlementCompleteRequest;
import ncr.res.mobilepos.mujiPassport.model.SettlementCompleteResponse;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

/**
 * CustomerSearchResource class is a web resourse which provides support for
 * search customer.
 */
@Path("/mujiPassport")
@Api(value = "/mujiPassport", description = "���API")
public class MujiPassportResource {

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** */
    private static final String PROG_NAME = "MujiPassport";

    /**
     * constructor.
     */
    public MujiPassportResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * @param authKey
     * @param memberID
     * @return
     */
    @Path("/getAccountInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "����m�FAPI", response = AccountInfoResponse.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "�f�[�^�x�[�X�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����̃p�����[�^"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "�f�[�^�͌�����Ȃ�"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL�ُ�"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "���s���������[�g�z�X�g�ւ̐ڑ����쐬���܂�"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO�ُ�") })
    public final AccountInfoResponse getAccountInfo(
            @ApiParam(name = "authKey", value = "API�F�؃L�[") @QueryParam("authKey") final String authKey,
            @ApiParam(name = "memberID", value = "���ID") @QueryParam("memberID") final String memberID) {

        String functionName = "getAccountInfo";
        tp.methodEnter(functionName);
        tp.println("authKey", authKey).println("memberID", memberID);
        AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
        AccountInfoRequest request = new AccountInfoRequest();
        try {
            // param check
            if (StringUtility.isNullOrEmpty(memberID)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                accountInfoResponse.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return accountInfoResponse;
            }
            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                accountInfoResponse.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return accountInfoResponse;
            }

            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(MujiPassportConstants.GETACCOUNTINFO);

            if(StringUtility.isNullOrEmpty(authKey)){
                request.setAuthKey(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER));
            } else {                
                request.setAuthKey(authKey);
            }
            request.setMemberID(memberID);
            // basic authenticate
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes < MujiPassportConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()) {
                    lstReturn.add("200");
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + memberID
                            + MujiPassportConstants.ACCOUNTINFO_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + MujiPassportConstants.ACCOUNTINFO_TEST);
                    }
                    InputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        StringBuilder strbReturn = new StringBuilder();
                        String strReadLine = "";
                        while ((strReadLine = br.readLine()) != null) {
                            strbReturn.append(strReadLine.trim());
                        }
                        br.close();
                        lstReturn.add(strbReturn.toString());
                    } finally {
                        in.close();
                    }
                } else {
                    XmlSerializer<AccountInfoRequest> conSerializer = new XmlSerializer<AccountInfoRequest>();
                    lstReturn = HttpRequestXml.connection(strbUrl.toString(),
                            conSerializer.marshallObj(AccountInfoRequest.class, request, "utf-8"),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }

                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                accountInfoResponse.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return accountInfoResponse;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            String resultXml = lstReturn.get(1);
            XmlSerializer<AccountInfoResponse> conSerializer = new XmlSerializer<AccountInfoResponse>();
            accountInfoResponse = conSerializer.unMarshallXml(resultXml, AccountInfoResponse.class);

            accountInfoResponse.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            accountInfoResponse.setNCRWSSExtendedResultCode(intReturnStatus);
            accountInfoResponse.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get Member information.", e);
            accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            accountInfoResponse.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to get Member information.", e);
            accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            accountInfoResponse.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to get Member information.",
                        e);
                accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                accountInfoResponse.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to get Member information.",
                        e);
                accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                accountInfoResponse.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get Member information.",
                    e);
            accountInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            accountInfoResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            accountInfoResponse.setMessage(e.getMessage());
        } finally {
            tp.methodExit(accountInfoResponse);
        }

        return accountInfoResponse;
    }

    /**
     * @param authKey
     * @param memberID
     * @param strCd
     * @param registarNo
     * @param tradeDate
     * @param tradeNo
     * @param tradeType
     * @param beforeTradeDate
     * @param beforeTradeNo
     * @param couponAmount
     * @param additionalMile
     * @param useCouponList
     * @param mybag
     * @return
     */
    @Path("/registTrade")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "����A��API", response = RegistTradeResponse.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "�f�[�^�x�[�X�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����̃p�����[�^"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "�f�[�^�͌�����Ȃ�"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL�ُ�"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "���s���������[�g�z�X�g�ւ̐ڑ����쐬���܂�"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO�ُ�") })
    public final RegistTradeResponse registTrade(
            @ApiParam(name = "authKey", value = "API�F�؃L�[") @QueryParam("authKey") final String authKey,
            @ApiParam(name = "memberID", value = "���ID") @QueryParam("memberID") final String memberID,
            @ApiParam(name = "strCd", value = "�X��") @QueryParam("strCd") final String strCd,
            @ApiParam(name = "registarNo", value = "���W�ԍ�") @QueryParam("registarNo") final String registarNo,
            @ApiParam(name = "tradeDate", value = "�������") @QueryParam("tradeDate") final String tradeDate,
            @ApiParam(name = "tradeNo", value = "����ԍ�") @QueryParam("tradeNo") final String tradeNo,
            @ApiParam(name = "tradeType", value = "������") @QueryParam("tradeType") final int tradeType,
            @ApiParam(name = "beforeTradeDate", value = "���������") @QueryParam("beforeTradeDate") final String beforeTradeDate,
            @ApiParam(name = "beforeTradeNo", value = "������ԍ�") @QueryParam("beforeTradeNo") final String beforeTradeNo,
            @ApiParam(name = "couponAmount", value = "�|�C���g���p(�Ԋ�)�z") @QueryParam("couponAmount") final int couponAmount,
            @ApiParam(name = "additionalMile", value = "���Z(���Z)�}�C����") @QueryParam("additionalMile") final int additionalMile,
            @ApiParam(name = "useCouponList", value = "�g�p�N�[�|���ꗗ") @QueryParam("useCouponList") final String useCouponList,
            @ApiParam(name = "mybag", value = "�}�C�o�b�O���Q�L��") @QueryParam("mybag") final int mybag) {

        String functionName = "registTrade";
        tp.methodEnter(functionName);
        tp.println("authKey", authKey).println("memberID", memberID).println("strCd", strCd)
                .println("registarNo", registarNo).println("tradeDate", tradeDate).println("tradeNo", tradeNo)
                .println("tradeType", tradeType).println("beforeTradeDate", beforeTradeDate)
                .println("beforeTradeNo", beforeTradeNo).println("couponAmount", couponAmount)
                .println("additionalMile", additionalMile).println("useCouponList", useCouponList)
                .println("mybag", mybag);
        RegistTradeRequest request = new RegistTradeRequest();
        RegistTradeResponse registTradeResponse = new RegistTradeResponse();
        try {
            // param check
            if (StringUtility.isNullOrEmpty(memberID)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                registTradeResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                registTradeResponse.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return registTradeResponse;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                registTradeResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                registTradeResponse.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return registTradeResponse;
            }

            StringBuilder strbUrl = new StringBuilder();

            strbUrl.append(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(MujiPassportConstants.REGISTTRADE);

            if(StringUtility.isNullOrEmpty(authKey)){
                request.setAuthKey(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER));
            } else {                
                request.setAuthKey(authKey);
            }
            request.setMemberID(memberID);
            request.setStrCd(strCd);
            request.setRegistarNo(registarNo);
            request.setTradeDate(tradeDate);
            request.setTradeNo(tradeNo);
            request.setTradeType(tradeType);
            request.setBeforeTradeDate(beforeTradeDate);
            request.setBeforeTradeNo(beforeTradeNo);
            request.setCouponAmount(couponAmount);
            request.setAdditionalMile(additionalMile);
            request.setUseCouponList(useCouponList);
            request.setMybag(mybag);

            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes <= MujiPassportConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()) {
                    lstReturn.add("200");
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + memberID
                            + MujiPassportConstants.REGISTTRADE_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + MujiPassportConstants.REGISTTRADE_TEST);
                    }
                    InputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        StringBuilder strbReturn = new StringBuilder();
                        String strReadLine = "";
                        while ((strReadLine = br.readLine()) != null) {
                            strbReturn.append(strReadLine.trim());
                        }
                        br.close();
                        lstReturn.add(strbReturn.toString());
                    } finally {
                        in.close();
                    }
                } else {
                    XmlSerializer<RegistTradeRequest> conSerializer = new XmlSerializer<RegistTradeRequest>();
                    lstReturn = HttpRequestXml.connection(strbUrl.toString(),
                            conSerializer.marshallObj(RegistTradeRequest.class, request, "utf-8"),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }
                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                registTradeResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                registTradeResponse.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return registTradeResponse;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));

            String resultXml = lstReturn.get(1);
            XmlSerializer<RegistTradeResponse> conSerializer = new XmlSerializer<RegistTradeResponse>();
            registTradeResponse = conSerializer.unMarshallXml(resultXml, RegistTradeResponse.class);

            registTradeResponse.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            registTradeResponse.setNCRWSSExtendedResultCode(intReturnStatus);
            registTradeResponse.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to update Member.", e);
            registTradeResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            registTradeResponse.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to update Member.", e);
            registTradeResponse.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            registTradeResponse.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to update member.", e);
                registTradeResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                registTradeResponse.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to update Member.", e);
                registTradeResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                registTradeResponse.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to update Member.", e);
            registTradeResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            registTradeResponse.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            registTradeResponse.setMessage(e.getMessage());
        } finally {
            tp.methodExit(registTradeResponse);
        }
        return registTradeResponse;
    }

    /**
     * @param authKey
     * @param memberID
     * @param strCd
     * @param settlementId
     * @return
     */
    @Path("/getPassportPaymentInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "����A��API", response = PassportPaymentInfoResponse.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "�f�[�^�x�[�X�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����̃p�����[�^"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "�f�[�^�͌�����Ȃ�"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL�ُ�"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "���s���������[�g�z�X�g�ւ̐ڑ����쐬���܂�"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO�ُ�") })
    public final PassportPaymentInfoResponse getPassportPaymentInfo(
            @ApiParam(name = "authKey", value = "API�F�؃L�[") @QueryParam("authKey") final String authKey,
            @ApiParam(name = "memberID", value = "���ID") @QueryParam("memberID") final String memberID,
            @ApiParam(name = "strCd", value = "�X��") @QueryParam("strCd") final String strCd,
            @ApiParam(name = "settlementId", value = "���Ϗ��擾TOKEN") @QueryParam("settlementId") final String settlementId) {

        String functionName = "getPassportPaymentInfo";
        tp.methodEnter(functionName);
        tp.println("authKey", authKey).println("memberID", memberID).println("strCd", strCd).println("settlementId",
                settlementId);
        PassportPaymentInfoRequest request = new PassportPaymentInfoRequest();
        PassportPaymentInfoResponse response = new PassportPaymentInfoResponse();
        try {
            // param check
            if (StringUtility.isNullOrEmpty(memberID)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return response;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                response.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return response;
            }

            StringBuilder strbUrl = new StringBuilder();

            strbUrl.append(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(MujiPassportConstants.GETPASSPORTPAYMENTINFO);

            if(StringUtility.isNullOrEmpty(authKey)){
                request.setAuthKey(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER));
            } else {                
                request.setAuthKey(authKey);
            }
            request.setMemberID(memberID);
            request.setStrCd(strCd);
            request.setSettlementId(settlementId);

            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes <= MujiPassportConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()) {
                    lstReturn.add("200");
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + memberID
                            + MujiPassportConstants.PASSPORTPAYMENTINFO_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + MujiPassportConstants.PASSPORTPAYMENTINFO_TEST);
                    }
                    InputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        StringBuilder strbReturn = new StringBuilder();
                        String strReadLine = "";
                        while ((strReadLine = br.readLine()) != null) {
                            strbReturn.append(strReadLine.trim());
                        }
                        br.close();
                        lstReturn.add(strbReturn.toString());
                    } finally {
                        in.close();
                    }
                } else {
                    XmlSerializer<PassportPaymentInfoRequest> conSerializer = new XmlSerializer<PassportPaymentInfoRequest>();
                    lstReturn = HttpRequestXml.connection(strbUrl.toString(),
                            conSerializer.marshallObj(PassportPaymentInfoRequest.class, request, "utf-8"),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }
                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return response;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));

            String resultXml = lstReturn.get(1);
            XmlSerializer<PassportPaymentInfoResponse> conSerializer = new XmlSerializer<PassportPaymentInfoResponse>();
            response = conSerializer.unMarshallXml(resultXml, PassportPaymentInfoResponse.class);

            response.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            response.setNCRWSSExtendedResultCode(intReturnStatus);
            response.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to get Passport Payment information.", e);
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            response.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                    functionName + ": Failed to get Passport Payment information.", e);
            response.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            response.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                        functionName + ": Failed to get Passport Payment information.", e);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                response.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                        functionName + ": Failed to get Passport Payment information.", e);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                response.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get Passport Payment information.", e);
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setMessage(e.getMessage());
        } finally {
            tp.methodExit(response);
        }
        return response;
    }

    /**
     * @param authKey
     * @param memberID
     * @param paymentType
     * @param paymentRequestCode
     * @param strCd
     * @param paymentInfoCode
     * @return
     */
    @Path("/payPassport")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "����A��API", response = PassportPaymentInfoResponse.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "�f�[�^�x�[�X�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����̃p�����[�^"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "�f�[�^�͌�����Ȃ�"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL�ُ�"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "���s���������[�g�z�X�g�ւ̐ڑ����쐬���܂�"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO�ُ�") })
    public final PayPassportResponse payPassport(
            @ApiParam(name = "authKey", value = "API�F�؃L�[") @QueryParam("authKey") final String authKey,
            @ApiParam(name = "memberID", value = "���ID") @QueryParam("memberID") final String memberID,
            @ApiParam(name = "paymentType", value = "���ώ��") @QueryParam("paymentType") final String paymentType,
            @ApiParam(name = "paymentRequestCode", value = "����TOKEN") @QueryParam("paymentRequestCode") final String paymentRequestCode,
            @ApiParam(name = "strCd", value = "�X�ܔԍ�") @QueryParam("strCd") final String strCd,
            @ApiParam(name = "paymentInfoCode", value = "���ψ˗�ID") @QueryParam("paymentInfoCode") final String paymentInfoCode) {

        String functionName = "payPassport";
        tp.methodEnter(functionName);
        tp.println("authKey", authKey).println("memberID", memberID).println("paymentType", paymentType)
                .println("paymentRequestCode", paymentRequestCode).println("strCd", strCd)
                .println("paymentInfoCode", paymentInfoCode);
        PayPassportRequest request = new PayPassportRequest();
        PayPassportResponse response = new PayPassportResponse();
        try {
            // param check
            if (StringUtility.isNullOrEmpty(memberID)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return response;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                response.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return response;
            }

            StringBuilder strbUrl = new StringBuilder();

            strbUrl.append(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(MujiPassportConstants.PAYPASSPORT);

            if(StringUtility.isNullOrEmpty(authKey)){
                request.setAuthKey(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER));
            } else {                
                request.setAuthKey(authKey);
            }
            request.setMemberID(memberID);
            request.setPaymentRequestCode(paymentRequestCode);
            request.setPaymentInfoCode(paymentInfoCode);
            request.setPaymentType(paymentType);
            request.setStrCd(strCd);
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes <= MujiPassportConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()) {
                    lstReturn.add("200");
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + memberID
                            + MujiPassportConstants.PAYPASSPORT_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + MujiPassportConstants.PAYPASSPORT_TEST);
                    }
                    InputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        StringBuilder strbReturn = new StringBuilder();
                        String strReadLine = "";
                        while ((strReadLine = br.readLine()) != null) {
                            strbReturn.append(strReadLine.trim());
                        }
                        br.close();
                        lstReturn.add(strbReturn.toString());
                    } finally {
                        in.close();
                    }
                } else {
                    XmlSerializer<PayPassportRequest> conSerializer = new XmlSerializer<PayPassportRequest>();
                    lstReturn = HttpRequestXml.connection(strbUrl.toString(),
                            conSerializer.marshallObj(PayPassportRequest.class, request, "utf-8"),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }
                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return response;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));

            String resultXml = lstReturn.get(1);
            XmlSerializer<PayPassportResponse> conSerializer = new XmlSerializer<PayPassportResponse>();
            response = conSerializer.unMarshallXml(resultXml, PayPassportResponse.class);

            response.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            response.setNCRWSSExtendedResultCode(intReturnStatus);
            response.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to pay passport.", e);
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            response.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to pay passport.", e);
            response.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            response.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to pay passport.", e);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                response.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to pay passport.", e);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                response.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to pay passport.", e);
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setMessage(e.getMessage());
        } finally {
            tp.methodExit(response);
        }
        return response;
    }

    /**
     * @param authKey
     * @param memberID
     * @param settlementId
     * @param journalId
     * @param tradeDate
     * @param strCd
     * @param amount
     * @param paymentType
     * @param paymentRequestCode
     * @param paymentInfoCode
     * @param prepaidBalance
     * @param prepaidExpirationDate
     * @return
     */
    @Path("/settlementComplete")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value = "����A��API", response = PassportPaymentInfoResponse.class)
    @ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "�f�[�^�x�[�X�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����̃p�����[�^"),
            @ApiResponse(code = ResultBase.RES_ERROR_NODATAFOUND, message = "�f�[�^�͌�����Ȃ�"),
            @ApiResponse(code = ResultBase.RES_MALFORMED_URL_EXCEPTION, message = "URL�ُ�"),
            @ApiResponse(code = ResultBase.RES_ERROR_UNKNOWNHOST, message = "���s���������[�g�z�X�g�ւ̐ڑ����쐬���܂�"),
            @ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO�ُ�") })
    public final SettlementCompleteResponse settlementComplete(
            @ApiParam(name = "authKey", value = "API�F�؃L�[") @QueryParam("authKey") final String authKey,
            @ApiParam(name = "memberID", value = "���ID") @QueryParam("memberID") final String memberID,
            @ApiParam(name = "settlementId", value = "���Ϗ��擾TOKEN") @QueryParam("settlementId") final String settlementId,
            @ApiParam(name = "journalId", value = "�W���[�i��ID") @QueryParam("journalId") final String journalId,
            @ApiParam(name = "tradeDate", value = "���ϓ���") @QueryParam("tradeDate") final String tradeDate,
            @ApiParam(name = "strCd", value = "�X�ܔԍ�") @QueryParam("strCd") final String strCd,
            @ApiParam(name = "amount", value = "�X�ܔԍ�") @QueryParam("amount") final int amount,
            @ApiParam(name = "paymentType", value = "���ώ��") @QueryParam("paymentType") final String paymentType,
            @ApiParam(name = "paymentRequestCode", value = "����TOKEN") @QueryParam("paymentRequestCode") final String paymentRequestCode,
            @ApiParam(name = "paymentInfoCode", value = "���ψ˗�ID") @QueryParam("paymentInfoCode") final String paymentInfoCode,
            @ApiParam(name = "prepaidBalance", value = "���ό�v���y�C�h�c��") @QueryParam("prepaidBalance") final int prepaidBalance,
            @ApiParam(name = "prepaidExpirationDate", value = "�v���y�C�h�L����������") @QueryParam("prepaidExpirationDate") final String prepaidExpirationDate) {

        String functionName = "settlementComplete";
        tp.methodEnter(functionName);
        tp.println("authKey", authKey).println("memberID", memberID).println("settlementId", settlementId)
                .println("journalId", journalId).println("tradeDate", tradeDate).println("strCd", strCd)
                .println("amount", amount).println("paymentType", paymentType)
                .println("paymentRequestCode", paymentRequestCode).println("paymentInfoCode", paymentInfoCode)
                .println("prepaidBalance", prepaidBalance).println("prepaidExpirationDate", prepaidExpirationDate);
        SettlementCompleteRequest request = new SettlementCompleteRequest();
        SettlementCompleteResponse response = new SettlementCompleteResponse();
        try {
            // param check
            if (StringUtility.isNullOrEmpty(memberID)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                response.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return response;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility.isNullOrEmpty(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                response.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return response;
            }

            StringBuilder strbUrl = new StringBuilder();

            strbUrl.append(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(MujiPassportConstants.SETTLEMENTCOMPLETE);

            if(StringUtility.isNullOrEmpty(authKey)){
                request.setAuthKey(mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERUSER));
            } else {                
                request.setAuthKey(authKey);
            }
            request.setMemberID(memberID);
            request.setSettlementId(settlementId);
            request.setJournalId(journalId);
            request.setTradeDate(tradeDate);
            request.setStrCd(strCd);
            request.setAmount(amount);
            request.setPaymentType(paymentType);
            request.setPaymentRequestCode(paymentRequestCode);
            request.setPaymentInfoCode(paymentInfoCode);
            request.setPrepaidBalance(prepaidBalance);
            request.setPrepaidExpirationDate(prepaidExpirationDate);
            
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes <= MujiPassportConstants.RETRYTOTAL; retryTimes++) {
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()) {
                    lstReturn.add("200");
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + memberID
                            + MujiPassportConstants.SETTLEMENTCOMPLETE_TEST);
                    if (!file.isDirectory()) {
                        file = new File(EnvironmentEntries.getInstance().getParaBasePath()
                                + MujiPassportConstants.SETTLEMENTCOMPLETE_TEST);
                    }
                    InputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        StringBuilder strbReturn = new StringBuilder();
                        String strReadLine = "";
                        while ((strReadLine = br.readLine()) != null) {
                            strbReturn.append(strReadLine.trim());
                        }
                        br.close();
                        lstReturn.add(strbReturn.toString());
                    } finally {
                        in.close();
                    }
                } else {
                    XmlSerializer<SettlementCompleteRequest> conSerializer = new XmlSerializer<SettlementCompleteRequest>();
                    lstReturn = HttpRequestXml.connection(strbUrl.toString(),
                            conSerializer.marshallObj(SettlementCompleteRequest.class, request, "utf-8"),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(MujiPassportConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }
                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)))
                    break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return response;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));

            String resultXml = lstReturn.get(1);
            XmlSerializer<SettlementCompleteResponse> conSerializer = new XmlSerializer<SettlementCompleteResponse>();
            response = conSerializer.unMarshallXml(resultXml, SettlementCompleteResponse.class);

            response.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            response.setNCRWSSExtendedResultCode(intReturnStatus);
            response.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to settlement complete.", e);
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            response.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to settlement complete.", e);
            response.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            response.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to settlement complete.", e);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                response.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to settlement complete.", e);
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                response.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to settlement complete.", e);
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setMessage(e.getMessage());
        } finally {
            tp.methodExit(response);
        }
        return response;
    }
}
