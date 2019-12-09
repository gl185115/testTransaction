package ncr.res.mobilepos.customerSearch.resource;

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

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.customerSearch.constants.CustomerSearchConstants;
import ncr.res.mobilepos.customerSearch.helper.HTTPBasicAuthorization;
import ncr.res.mobilepos.customerSearch.model.CustomerSearchReturnBean;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

/**
 * CustomerSearchResource class is a web resourse which provides support for
 * search customer.
 */
@Path("/customerSearch")
@Api(value="/customerSearch", description="会員検索API")
public class CustomerSearchResource {

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** */
    private static final String PROG_NAME = "CSResource";

    /**
     * constructor.
     */
    public CustomerSearchResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Member Search API
     * @param cardNo : Card number
     * @param memberSeiKana : Member surname
     * @param memberMeiKana : Member name
     * @param birthday : birthday
     * @param phone : phone
     * @param maxResult : Get maximum number (max: 1 ~ 50)
     * @return the Xml of member information
     */
    @Path("/getMemberSearch")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="会員の検索", response=CustomerSearchReturnBean.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データが見つからない"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL異常"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="リモートホストへの接続に失敗"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/Oエラー")
    })
    public final CustomerSearchReturnBean getMemberSearch(
    		@ApiParam(name="cardNo", value="カード番号") @FormParam("cardNo") final String cardNo,
    		@ApiParam(name="memberSeiKana", value="会員の姓") @FormParam("memberSeiKana") final String memberSeiKana,
    		@ApiParam(name="memberMeiKana", value="会員の名") @FormParam("memberMeiKana") final String memberMeiKana,
    		@ApiParam(name="birthday", value="誕生日") @FormParam("birthday") final String birthday,
    		@ApiParam(name="phone", value="電話番号") @FormParam("phone") final String phone,
    		@ApiParam(name="maxResult", value="最大カウント") @FormParam("maxResult") final String maxResult) {

        String functionName = DebugLogger.getCurrentMethodName();
        CustomerSearchReturnBean customerSearchReturnBean = new CustomerSearchReturnBean();

        try {
            // param check
            if (StringUtility.isNullOrEmpty(maxResult)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return customerSearchReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

			if (mapReturn == null
					|| StringUtility
							.isNullOrEmpty(mapReturn
									.get(CustomerSearchConstants.KEYID_MEMBERSERVERURI))
					|| StringUtility
							.isNullOrEmpty(mapReturn
									.get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER))
					|| StringUtility
							.isNullOrEmpty(mapReturn
									.get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS))) {
				tp.println(ResultBase.RES_NODATAFOUND_MSG);
				customerSearchReturnBean
						.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				customerSearchReturnBean
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				customerSearchReturnBean
						.setMessage(ResultBase.RES_NODATAFOUND_MSG);
				return customerSearchReturnBean;
			}

            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(CustomerSearchConstants.API_B0110);
            
            StringBuilder strbParams = new StringBuilder();
            // add maxResult
            strbParams.append("maxResult=");
            strbParams.append(maxResult);
            // add cardNo
            if (!StringUtility.isNullOrEmpty(cardNo)) {
            	strbParams.append("&");
            	strbParams.append("cardNo=");
            	strbParams.append(cardNo);
            }
            // add memberSeiKana
            if (!StringUtility.isNullOrEmpty(memberSeiKana)) {
            	strbParams.append("&");
            	strbParams.append("memberSeiKana=");
            	strbParams.append(memberSeiKana);
            }
            // add memberMeiKana
            if (!StringUtility.isNullOrEmpty(memberMeiKana)) {
            	strbParams.append("&");
            	strbParams.append("memberMeiKana=");
            	strbParams.append(memberMeiKana);
            }
            // add birthday
            if (!StringUtility.isNullOrEmpty(birthday)) {
            	strbParams.append("&");
            	strbParams.append("birthday=");
            	strbParams.append(birthday);
            }
            // add phone
            if (!StringUtility.isNullOrEmpty(phone)) {
            	strbParams.append("&");
            	strbParams.append("phone=");
            	strbParams.append(phone);
            }
            // basic authenticate
            // send url
			List<String> lstReturn = HTTPBasicAuthorization.connection(
					strbUrl.toString(), strbParams.toString(),
					mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER),
					mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS),
					mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERTIMEOUT));

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            if (intReturnStatus != 200) {
                customerSearchReturnBean.setNCRWSSResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            } else {
                String strReturn = lstReturn.get(1);
                customerSearchReturnBean.setStrResultXml(strReturn);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to member information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                    + ": Failed to Member Trade List information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
        	if (e instanceof UnknownHostException) {
        		LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to member information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean.setMessage(e.getMessage());
        	}else{
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to member information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean.setMessage(e.getMessage());        		
        	}
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to member information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean.setMessage(e.getMessage());
        }

        return customerSearchReturnBean;
    }

    /**
     * Member Trade List API
     * @param cardNo : Card number
     * @param tradeDateFrom : Search Start Date
     * @param tradeDateTo : Search period (1: 30, 2:180, 3:365, 4:ALL)
     * @param firstResult : Search start position
     * @param maxResult : Get maximum number (max: 1 ~ 20)
     * @param forcedBflag : Force B history acquisition flag (0 or 1)
     * @return the Xml of member information
     */
    @Path("/getMemberTradeList")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="会員取引情報を取得する", response=CustomerSearchReturnBean.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データが見つからない"),
            @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL異常"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="リモートホストへの接続に失敗"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/Oエラー")
        })
    public final CustomerSearchReturnBean getMemberTradeList(
    		@ApiParam(name="cardNo", value="カード番号") @FormParam("cardNo") final String cardNo,
    		@ApiParam(name="tradeDateFrom", value="取引開始日") @FormParam("tradeDateFrom") final String tradeDateFrom,
    		@ApiParam(name="tradeDateTo", value="取引終了日") @FormParam("tradeDateTo") final String tradeDateTo,
    		@ApiParam(name="firstResult", value="取引開始位置") @FormParam("firstResult") final String firstResult,
    		@ApiParam(name="maxResult", value="リストの返送最大数") @FormParam("maxResult") final String maxResult,
    		@ApiParam(name="forcedBflag", value="検索フラグ") @FormParam("forcedBflag") final String forcedBflag) {

        String functionName = DebugLogger.getCurrentMethodName();
        CustomerSearchReturnBean customerSearchReturnBean = new CustomerSearchReturnBean();

        try {
            // param check
            if (StringUtility.isNullOrEmpty(cardNo)
                    || StringUtility.isNullOrEmpty(tradeDateFrom)
                    || StringUtility.isNullOrEmpty(tradeDateTo)
                    || StringUtility.isNullOrEmpty(firstResult)
                    || StringUtility.isNullOrEmpty(maxResult)
                    || StringUtility.isNullOrEmpty(forcedBflag)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return customerSearchReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

			if (mapReturn == null
					|| StringUtility
							.isNullOrEmpty(mapReturn
									.get(CustomerSearchConstants.KEYID_MEMBERSERVERURI))
					|| StringUtility
							.isNullOrEmpty(mapReturn
									.get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER))
					|| StringUtility
							.isNullOrEmpty(mapReturn
									.get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS))) {
				tp.println(ResultBase.RES_NODATAFOUND_MSG);
				customerSearchReturnBean
						.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				customerSearchReturnBean
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				customerSearchReturnBean
						.setMessage(ResultBase.RES_NODATAFOUND_MSG);
				return customerSearchReturnBean;
			}

            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(CustomerSearchConstants.API_B0407);
            
            StringBuilder strbParams = new StringBuilder();
            // add cardNo
            strbParams.append("cardNo=");
            strbParams.append(cardNo);
            // add tradeDateFrom
            strbParams.append("&");
            strbParams.append("tradeDateFrom=");
            strbParams.append(tradeDateFrom);
            // add tradeDateTo
            strbParams.append("&");
            strbParams.append("tradeDateTo=");
            strbParams.append(tradeDateTo);
            // add firstResult
            strbParams.append("&");
            strbParams.append("firstResult=");
            strbParams.append(firstResult);
            // add maxResult
            strbParams.append("&");
            strbParams.append("maxResult=");
            strbParams.append(maxResult);
            // add forcedBflag
            strbParams.append("&");
            strbParams.append("forcedBflag=");
            strbParams.append(forcedBflag);

            // basic authenticate
            // send url
			List<String> lstReturn = HTTPBasicAuthorization.connection(
					strbUrl.toString(), strbParams.toString(),
					mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER),
					mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS),
					mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERTIMEOUT));

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            if (intReturnStatus != 200) {
                customerSearchReturnBean.setNCRWSSResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            } else {
                String strReturn = lstReturn.get(1);
                customerSearchReturnBean.setStrResultXml(strReturn);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to Member Trade List information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                    + ": Failed to Member Trade List information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
        	if (e instanceof UnknownHostException) {
        		LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to member information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean.setMessage(e.getMessage());
        	}else{
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to Member Trade List information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean.setMessage(e.getMessage());
        	}
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to Member Trade List information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean.setMessage(e.getMessage());
        }

        return customerSearchReturnBean;
    }
    
    /**
     * Login Key Change API
     * @param loginKey : Login Key
     * @return the Xml of Changed Login Key
     */
    @Path("/getChangedLoginKey")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="変換ログインキーの取得", response=CustomerSearchReturnBean.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データが見つからない"),
            @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL異常"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="リモートホストへの接続に失敗"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/Oエラー")
        })
    public final CustomerSearchReturnBean getChangedLoginKey(
            @ApiParam(name="loginKey", value="ログインキー") @FormParam("loginKey") final String loginKey) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("loginKey", loginKey);
        CustomerSearchReturnBean customerSearchReturnBean = new CustomerSearchReturnBean();

        try {
            // param check
            if (StringUtility.isNullOrEmpty(loginKey)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return customerSearchReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER))
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return customerSearchReturnBean;
            }

            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(CustomerSearchConstants.API_B0001);
            
            StringBuilder strbParams = new StringBuilder();
            // add loginKey
            strbParams.append("loginKey=");
            strbParams.append(loginKey);

            // basic authenticate
            // send url
            List<String> lstReturn = null;
            
            for (int retryTimes = 0; retryTimes < CustomerSearchConstants.RETRYTOTAL; retryTimes++){
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()){
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + CustomerSearchConstants.LOGINKEYXML);
                    InputStream in = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder strbReturn = new StringBuilder();
                    String strReadLine = "";
                    lstReturn.add("200");
                    while((strReadLine = br.readLine()) != null){
                        strbReturn.append(strReadLine.trim());
                    }
                    br.close();
                    lstReturn.add(strbReturn.toString());
                } else {
                    lstReturn = HTTPBasicAuthorization.connection(
                            strbUrl.toString(), strbParams.toString(),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }
                
                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)) && Integer.parseInt(lstReturn.get(0)) == 200) break;
            }

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            if (intReturnStatus != 200) {
                customerSearchReturnBean.setNCRWSSResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            } else {
                String strReturn = lstReturn.get(1);
                customerSearchReturnBean.setStrResultXml(strReturn);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to get LoginKey.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                    + ": Failed to get LoginKey.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to get LoginKey.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean.setMessage(e.getMessage());
            }else{
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to get LoginKey.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get LoginKey.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean.setMessage(e.getMessage());
        } finally {
            tp.methodExit(customerSearchReturnBean);
        }

        return customerSearchReturnBean;
    }
    
    /**
     * Get Rank Info API
     * @param memberCode : Member Code
     * @return the Xml of rank information
     */
    @Path("/getRankInfo")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="ランク情報の取得", response=CustomerSearchReturnBean.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データが見つからない"),
            @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL異常"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="リモートホストへの接続に失敗"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/Oエラー")
        })
    public final CustomerSearchReturnBean getRankInfo(
            @ApiParam(name="memberCode", value="会員番号") @FormParam("memberCode") final String memberCode) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("memberCode", memberCode);
        CustomerSearchReturnBean customerSearchReturnBean = new CustomerSearchReturnBean();

        try {
            // param check
            if (StringUtility.isNullOrEmpty(memberCode)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return customerSearchReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER))
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return customerSearchReturnBean;
            }

            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(CustomerSearchConstants.API_B2001);
            
            StringBuilder strbParams = new StringBuilder();
            // add memberCode
            strbParams.append("memberCode=");
            strbParams.append(memberCode);

            // basic authenticate
            // send url
            List<String> lstReturn = null;
            
            for (int retryTimes = 0; retryTimes < CustomerSearchConstants.RETRYTOTAL; retryTimes++){
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()){
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + CustomerSearchConstants.RANKINFOXML);
                    InputStream in = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder strbReturn = new StringBuilder();
                    String strReadLine = "";
                    lstReturn.add("200");
                    while((strReadLine = br.readLine()) != null){
                        strbReturn.append(strReadLine.trim());
                    }
                    br.close();
                    lstReturn.add(strbReturn.toString());
                } else {
                    lstReturn = HTTPBasicAuthorization.connection(
                            strbUrl.toString(), strbParams.toString(),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }
                
                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)) && Integer.parseInt(lstReturn.get(0)) == 200) break;
            }
            

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            if (intReturnStatus != 200) {
                customerSearchReturnBean.setNCRWSSResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            } else {
                String strReturn = lstReturn.get(1);
                customerSearchReturnBean.setStrResultXml(strReturn);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to get rank information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                    + ": Failed to get rank information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to get rank information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean.setMessage(e.getMessage());
            }else{
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to get rank information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get rank information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean.setMessage(e.getMessage());
        } finally {
            tp.methodExit(customerSearchReturnBean);
        }

        return customerSearchReturnBean;
    }
    
    /**
     * Get Member Info API
     * @param memberCode : Member Code
     * @return the Xml of member information
     */
    @Path("/getMemberInfo")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="会員情報の取得", response=CustomerSearchReturnBean.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データが見つからない"),
            @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL異常"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="リモートホストへの接続の失敗"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/Oエラー")
        })
    public final CustomerSearchReturnBean getMemberInfo(
            @ApiParam(name="memberCode", value="会員番号") @FormParam("memberCode") final String memberCode) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("memberCode", memberCode);
        CustomerSearchReturnBean customerSearchReturnBean = new CustomerSearchReturnBean();

        try {
            // param check
            if (StringUtility.isNullOrEmpty(memberCode)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return customerSearchReturnBean;
            }

            // get common url
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
            Map<String, String> mapReturn = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_MEMBER_SERVER);

            if (mapReturn == null
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERURI))
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER))
                    || StringUtility
                            .isNullOrEmpty(mapReturn
                                    .get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS))) {
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return customerSearchReturnBean;
            }

            StringBuilder strbUrl = new StringBuilder();
            strbUrl.append(mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERURI));
            strbUrl.append(CustomerSearchConstants.API_B0102);
            
            StringBuilder strbParams = new StringBuilder();
            // add memberCode
            strbParams.append("memberCode=");
            strbParams.append(memberCode);

            // basic authenticate
            // send url
            List<String> lstReturn = null;
            for (int retryTimes = 0; retryTimes < CustomerSearchConstants.RETRYTOTAL; retryTimes++){
                lstReturn = new ArrayList<String>();
                if (GlobalConstant.getMemberServerDebug()){
                    File file = new File(EnvironmentEntries.getInstance().getParaBasePath() + CustomerSearchConstants.MEMBERINFOXML);
                    InputStream in = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder strbReturn = new StringBuilder();
                    String strReadLine = "";
                    lstReturn.add("200");
                    while((strReadLine = br.readLine()) != null){
                        strbReturn.append(strReadLine.trim());
                    }
                    br.close();
                    lstReturn.add(strbReturn.toString());
                } else {
                    lstReturn = HTTPBasicAuthorization.connection(
                            strbUrl.toString(), strbParams.toString(),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERUSER),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERPASS),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERTIMEOUT),
                            mapReturn.get(CustomerSearchConstants.KEYID_MEMBERSERVERCONNECTTIMEOUT));
                }
                
                if (!StringUtility.isNullOrEmpty(lstReturn.get(0)) && Integer.parseInt(lstReturn.get(0)) == 200) break;
            }
            

            // sorting the returned data
            if (lstReturn == null || lstReturn.size() == 0) {
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            }
            int intReturnStatus = Integer.parseInt(lstReturn.get(0));
            if (intReturnStatus != 200) {
                customerSearchReturnBean.setNCRWSSResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(intReturnStatus);
                customerSearchReturnBean
                        .setMessage(ResultBase.RES_HTTPCONNECTIONFAILED_MSG);
                return customerSearchReturnBean;
            } else {
                String strReturn = lstReturn.get(1);
                customerSearchReturnBean.setStrResultXml(strReturn);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                customerSearchReturnBean.setMessage(ResultBase.RES_SUCCESS_MSG);
            }

        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to Member information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                    + ": Failed to Member information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            customerSearchReturnBean.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to member information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                customerSearchReturnBean.setMessage(e.getMessage());
            }else{
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to Member information.", e);
                customerSearchReturnBean
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                customerSearchReturnBean.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to Member information.", e);
            customerSearchReturnBean
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            customerSearchReturnBean.setMessage(e.getMessage());
        } finally {
            tp.methodExit(customerSearchReturnBean);
        }

        return customerSearchReturnBean;
    }
}
