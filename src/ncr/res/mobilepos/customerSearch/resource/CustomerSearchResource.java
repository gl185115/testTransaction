package ncr.res.mobilepos.customerSearch.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customerSearch.constants.CustomerSearchConstants;
import ncr.res.mobilepos.customerSearch.dao.ICustomerSearthDAO;
import ncr.res.mobilepos.customerSearch.helper.HTTPBasicAuthorization;
import ncr.res.mobilepos.customerSearch.model.CustomerSearchReturnBean;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

/**
 * CustomerSearchResource class is a web resourse which provides support for
 * search customer.
 */
@Path("/customerSearch")
@Api(value="/customerSearch", description="検索会員API")
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
    @ApiOperation(value="会員を検索する", response=CustomerSearchReturnBean.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
        @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データは見つからない"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL異常"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="失敗したリモートホストへの接続を作成します"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常")
    })
    public final CustomerSearchReturnBean getMemberSearch(
    		@ApiParam(name="cardNo", value="カードコード") @FormParam("cardNo") final String cardNo,
    		@ApiParam(name="memberSeiKana", value="会員の姓") @FormParam("memberSeiKana") final String memberSeiKana,
    		@ApiParam(name="memberMeiKana", value="会員の名") @FormParam("memberMeiKana") final String memberMeiKana,
    		@ApiParam(name="birthday", value="誕生日") @FormParam("birthday") final String birthday,
    		@ApiParam(name="phone", value="電話") @FormParam("phone") final String phone,
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
            ICustomerSearthDAO iCustomerSearthDAO = sqlServer
                    .getCustomerSearthDAO();
            Map<String, String> mapReturn = iCustomerSearthDAO.getPrmSystemConfigValue(
                    CustomerSearchConstants.CATEGORY);

			if (StringUtility.isNullOrEmpty(mapReturn)
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
            @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データは見つからない"),
            @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL異常"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="失敗したリモートホストへの接続を作成します"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常")
        })
    public final CustomerSearchReturnBean getMemberTradeList(
    		@ApiParam(name="cardNo", value="カードコード") @FormParam("cardNo") final String cardNo,
    		@ApiParam(name="tradeDateFrom", value="取引開始日") @FormParam("tradeDateFrom") final String tradeDateFrom,
    		@ApiParam(name="tradeDateTo", value="取引終了日") @FormParam("tradeDateTo") final String tradeDateTo,
    		@ApiParam(name="firstResult", value="取引開始位置") @FormParam("firstResult") final String firstResult,
    		@ApiParam(name="maxResult", value="最大カウント") @FormParam("maxResult") final String maxResult,
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
            ICustomerSearthDAO iCustomerSearthDAO = sqlServer
                    .getCustomerSearthDAO();
            Map<String, String> mapReturn = iCustomerSearthDAO.getPrmSystemConfigValue(
                    CustomerSearchConstants.CATEGORY);

			if (StringUtility.isNullOrEmpty(mapReturn)
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
}
