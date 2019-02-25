package ncr.res.mobilepos.ej.resource;

import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
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

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.ej.Factory.NameCategoryFactory;
import ncr.res.mobilepos.ej.dao.INameSystemInfoDAO;
import ncr.res.mobilepos.ej.dao.SQLServerNameSystemInfoDAO;
import ncr.res.mobilepos.ej.model.EjInfo;
import ncr.res.mobilepos.ej.model.EjInfos;
import ncr.res.mobilepos.ej.model.PosLogInfo;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.ej.helper.UrlConnectionHelper;

/**
 * EjResource Class is a Web Resource which support MobilePOS
 *
 */

@Path("/ej")
@Api(value="/ej", description="E/Jのリスト取得API")
public class EjResource {

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "ej";

    public static final String ENTERPRISE_EJ_UTL = "resTransaction/rest/enterpriseej/getremoteejinfo";
    public static final String ENTERPRISE_POSLOG_UTL = "resTransaction/rest/enterpriseej/getremoteposloginfo";

    private List<EjInfo> listNameSystemInfo = null;

    private static final String Category = "JournalSearch";
    private static final String KeyId = "TxTypeNameCategory";

    /**
     * Default Constructor for EjResource.
     *
     * <P>
     * Initializes the logger object.
     */
    public EjResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        listNameSystemInfo = new ArrayList<EjInfo>();
        listNameSystemInfo = NameCategoryFactory.getInstance();
    }

    /**
     * get the JournalList
     *
     * @param CompanyId
     * @param RetailstoreId
     * @param WorkstationId
     * @param TxType
     * @param SequencenumberFrom
     * @param SequencenumberTo
     * @param BusinessDateTimeFrom
     * @param BusinessDateTimeTo
     * @param OperatorId
     * @param SalesPersonId
     * @param TrainingFlag
     * @param MaxNumber
     *
     * @return List<EjInfo>
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/getJournalList")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="E/Jのリストを取得する", response=List.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final EjInfos getJournalList(
    		@ApiParam(name = "companyid", value = "会社コード") @FormParam("companyid") final String CompanyId,
			@ApiParam(name = "retailstoreid", value = "小売店コード") @FormParam("retailstoreid") final String RetailstoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String WorkstationId,
			@ApiParam(name = "txtype", value = "取引種別") @FormParam("txtype") final String TxType,
			@ApiParam(name = "SequencenumberFrom", value = "取引番号（下限）") @FormParam("SequencenumberFrom") final String SequencenumberFrom,
			@ApiParam(name = "SequencenumberTo", value = "取引番号（上限）") @FormParam("SequencenumberTo") final String SequencenumberTo,
			@ApiParam(name = "BusinessDateTimeFrom", value = "業務日付と時刻の下限") @FormParam("BusinessDateTimeFrom") final String BusinessDateTimeFrom,
			@ApiParam(name = "BusinessDateTimeTo", value = "業務日付と時刻の上限") @FormParam("BusinessDateTimeTo") final String BusinessDateTimeTo,
			@ApiParam(name = "OperatorId", value = "担当者コード") @FormParam("OperatorId") final String OperatorId,
			@ApiParam(name = "SalesPersonId", value = "販売員") @FormParam("SalesPersonId") final String SalesPersonId,
			@ApiParam(name = "TrainingFlag", value = "トレーニングフラグ ") @FormParam("TrainingFlag") final String TrainingFlag,
			@ApiParam(name = "MaxNumber", value = "最大表示件数") @FormParam("MaxNumber") final String MaxNumber) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyid", CompanyId).println("retailstoreid", RetailstoreId)
				.println("workstationid", WorkstationId).println("txtype", TxType)
				.println("SequencenumberFrom", SequencenumberFrom).println("SequencenumberTo", SequencenumberTo)
				.println("BusinessDateTimeFrom", BusinessDateTimeFrom).println("BusinessDateTimeTo", BusinessDateTimeTo)
				.println("OperatorId", OperatorId).println("SalesPersonId", SalesPersonId)
				.println("TrainingFlag", TrainingFlag).println("MaxNumber", MaxNumber);
		EjInfos ejInfos = new EjInfos();
		WindowsEnvironmentVariables windowsEnvironmentVariables = WindowsEnvironmentVariables.getInstance();
		try {
			if (windowsEnvironmentVariables.isServerTypeEnterprise()) {
				ejInfos = getEjInfoByTaxType(CompanyId, RetailstoreId, WorkstationId, TxType,
						SequencenumberFrom, SequencenumberTo, BusinessDateTimeFrom, BusinessDateTimeTo, OperatorId,
						SalesPersonId, TrainingFlag, MaxNumber);
			} else {
				ejInfos = getSubEjInfo(CompanyId, RetailstoreId, WorkstationId, TxType, SequencenumberFrom,
						SequencenumberTo, BusinessDateTimeFrom, BusinessDateTimeTo, OperatorId, SalesPersonId,
						TrainingFlag, MaxNumber);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get list E/J info.", ex);
			ejInfos.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			ejInfos.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(ejInfos);
		}
		return ejInfos;
	}

    /**
     * get the JournalList
     *
     * @param CompanyId
     * @param RetailstoreId
     * @param WorkstationId
     * @param TxType
     * @param Sequencenumber
     * @param BusinessDate
     * @param BusinessDateTimeFrom
     * @param TrainingFlag
     *
     * @return PosLogInfo
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/getPOSLog")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="E/Jのリストを取得する", response=List.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final PosLogInfo getPOSLog(
    		@ApiParam(name = "companyid", value = "会社コード") @FormParam("companyid") final String CompanyId,
			@ApiParam(name = "retailstoreid", value = "小売店コード") @FormParam("retailstoreid") final String RetailstoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String WorkstationId,
			@ApiParam(name = "sequencenumber", value = "取引番号") @FormParam("sequencenumber") final String Sequencenumber,
			@ApiParam(name = "businessDate", value = "業務日付") @FormParam("businessDate") final String BusinessDate,
			@ApiParam(name = "trainingFlag", value = "トレーニングフラグ") @FormParam("trainingFlag") final String TrainingFlag){
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyid", CompanyId).println("retailstoreid", RetailstoreId)
				.println("workstationid", WorkstationId).println("sequencenumber", Sequencenumber)
				.println("businessDate", BusinessDate).println("trainingFlag", TrainingFlag);
    	PosLogInfo posLogInfo = new PosLogInfo();
    	WindowsEnvironmentVariables windowsEnvironmentVariables = WindowsEnvironmentVariables.getInstance();
    	INameSystemInfoDAO dao = null;
    	try {
			if (windowsEnvironmentVariables.isServerTypeEnterprise()) {
				dao = new SQLServerNameSystemInfoDAO();
				posLogInfo = dao.getPosLogInfo(CompanyId, RetailstoreId, WorkstationId, Sequencenumber, BusinessDate, TrainingFlag);
			} else {
				posLogInfo = getSubPOSLogInfo(CompanyId, RetailstoreId, WorkstationId, Sequencenumber, BusinessDate, TrainingFlag);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get posLog info.", ex);
			posLogInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			posLogInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(posLogInfo);
		}
		return posLogInfo;
    }

	/**
	 * get server E/J Info
	 * @param CompanyId
     * @param RetailstoreId
     * @param WorkstationId
     * @param TxType
     * @param SequencenumberFrom
     * @param SequencenumberTo
     * @param BusinessDateTimeFrom
     * @param BusinessDateTimeTo
     * @param OperatorId
     * @param SalesPersonId
     * @param TrainingFlag
     * @param MaxNumber
	 * @return EjInfos
	 *
	 * @throws Exception
	 */
    private final EjInfos getSubEjInfo(String CompanyId,String RetailstoreId,String WorkstationId,String TxType,String SequencenumberFrom,String SequencenumberTo,
    		String BusinessDateTimeFrom,String BusinessDateTimeTo,String OperatorId,String SalesPersonId,String TrainingFlag,String MaxNumber) throws Exception{
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyid", CompanyId).println("retailstoreid", RetailstoreId)
				.println("workstationid", WorkstationId).println("txtype", TxType)
				.println("SequencenumberFrom", SequencenumberFrom).println("SequencenumberTo", SequencenumberTo)
				.println("BusinessDateTimeFrom", BusinessDateTimeFrom).println("BusinessDateTimeTo", BusinessDateTimeTo)
				.println("OperatorId", OperatorId).println("SalesPersonId", SalesPersonId)
				.println("TrainingFlag", TrainingFlag).println("MaxNumber", MaxNumber);
		JSONObject result = null;
		EjInfos ejInfos = new EjInfos();
		try {
			JSONObject valueResult = new JSONObject();
			valueResult.put("companyId", CompanyId);
			valueResult.put("retailStoreId", RetailstoreId);
			valueResult.put("workstationId", WorkstationId);
			valueResult.put("txType", TxType);
			valueResult.put("sequencenumberFrom", SequencenumberFrom);
			valueResult.put("sequencenumberTo", SequencenumberTo);
			valueResult.put("businessDateTimeFrom", BusinessDateTimeFrom);
			valueResult.put("businessDateTimeTo", BusinessDateTimeTo);
			valueResult.put("operatorId", OperatorId);
			valueResult.put("salesPersonId", SalesPersonId);
			valueResult.put("trainingFlag", TrainingFlag);
			valueResult.put("maxNumber", MaxNumber);
			int timeOut = 5;
			String enterpriseServerTimeout = GlobalConstant.getEnterpriseServerTimeout();
			if (StringUtility.isNullOrEmpty(enterpriseServerTimeout)) {
				timeOut = Integer.valueOf(enterpriseServerTimeout.toString());
			}
			String endStr = GlobalConstant.getEnterpriseServerUri().substring(GlobalConstant.getEnterpriseServerUri().length() - 1);
			String url = "";
			if ("/".equals(endStr)) {
				url = GlobalConstant.getEnterpriseServerUri() + ENTERPRISE_EJ_UTL;
			} else {
				url = GlobalConstant.getEnterpriseServerUri() + '/' + ENTERPRISE_EJ_UTL;
			}
			if (StringUtility.isNullOrEmpty(GlobalConstant.getAuthenticationUid()) || StringUtility.isNullOrEmpty(GlobalConstant.getAuthenticationPassword())) {
				LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_AUTHENTICATION_FAILED, "BASIC Authentication failed。\n");
				ejInfos.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				ejInfos.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				ejInfos.setMessage("BASIC Authentication failed");
				return ejInfos;
			}
			result = UrlConnectionHelper.connectionHttpsForGet(getUrl(url, valueResult), timeOut);
			// Check if error is empty.
			if (result != null) {
				if (result.has("ncrwssresultCode")) {
					if (result.getInt("ncrwssresultCode") == ResultBase.RES_OK) {
						ejInfos = (EjInfos)jsonToEjInfos(result.getJSONArray("ejList"));
					} else {
						ejInfos.setNCRWSSExtendedResultCode(ResultBase.RES_EJINFOS_NOTFOUND);
						ejInfos.setNCRWSSResultCode(ResultBase.RES_EJINFOS_NOTFOUND);
						ejInfos.setMessage("E/J list info search error");
					}
				} else if(result.has("ConnectionStatus") && result.getInt("ConnectionStatus") == HttpURLConnection.HTTP_UNAUTHORIZED) {
					LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_AUTHENTICATION_FAILED, "BASIC Authentication failed。\n");
					ejInfos.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
					ejInfos.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
					ejInfos.setMessage("BASIC Authentication failed");
					return ejInfos;
				}
			}
		} catch (Exception e) {
			if (e instanceof SocketException || e instanceof SocketTimeoutException) {
				//MeX Enterpriseがオフラインの場合
				ejInfos = getEjInfoByTaxType(CompanyId, RetailstoreId, WorkstationId, TxType,
						SequencenumberFrom, SequencenumberTo, BusinessDateTimeFrom, BusinessDateTimeTo, OperatorId,
						SalesPersonId, TrainingFlag, MaxNumber);
				return ejInfos;
			}
			
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send remote E/J info.",
					e);
			if (e instanceof JSONException) {
				throw new JSONException(e);
			} else {
				throw new Exception();
			}
		} finally {
			tp.methodExit(ejInfos);
		}
		return ejInfos;
    }

    /**
     * get the server PosLog Info
     *
     * @param CompanyId
     * @param RetailstoreId
     * @param WorkstationId
     * @param Sequencenumber
     * @param BusinessDate
     * @param TrainingFlag
     *
     * @return PosLogInfo
     */
    public final PosLogInfo getSubPOSLogInfo(String companyId, String retailstoreId, String workstationId, String sequencenumber, String businessDate,
    		String trainingFlag ) throws Exception{
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyid", companyId).println("retailstoreid", retailstoreId)
				.println("sequencenumber", sequencenumber).println("businessDate", businessDate)
				.println("trainingFlag", trainingFlag);
		JSONObject result = null;
		PosLogInfo posLogInfo = new PosLogInfo();
		try {
			JSONObject valueResult = new JSONObject();
			valueResult.put("companyId", companyId);
			valueResult.put("retailStoreId", retailstoreId);
			valueResult.put("workstationId", workstationId);
			valueResult.put("sequencenumber", sequencenumber);
			valueResult.put("businessDate", businessDate);
			valueResult.put("trainingFlag", trainingFlag);
			int timeOut = 5;
			String enterpriseServerTimeout = GlobalConstant.getEnterpriseServerTimeout();
			if (!StringUtility.isNullOrEmpty(enterpriseServerTimeout)) {
				timeOut = Integer.valueOf(enterpriseServerTimeout.toString());
			}
			String endStr = GlobalConstant.getEnterpriseServerUri().substring(GlobalConstant.getEnterpriseServerUri().length() - 1);
			String url = "";
			if ("/".equals(endStr)) {
				url = GlobalConstant.getEnterpriseServerUri() + ENTERPRISE_POSLOG_UTL;
			} else {
				url = GlobalConstant.getEnterpriseServerUri() + '/' + ENTERPRISE_POSLOG_UTL;
			}

			if (StringUtility.isNullOrEmpty(GlobalConstant.getAuthenticationUid()) || StringUtility.isNullOrEmpty(GlobalConstant.getAuthenticationPassword())) {
				LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_AUTHENTICATION_FAILED, "BASIC Authentication failed。\n");
				posLogInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				posLogInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				posLogInfo.setMessage("BASIC Authentication failed");
				return posLogInfo;
			}
			result = UrlConnectionHelper.connectionHttpsForGet(getUrl(url, valueResult), timeOut);
			
			// Check if error is empty.
			if (result != null) {
				if (result.has("ncrwssresultCode")) {
					if (result.getInt("ncrwssresultCode") == ResultBase.RES_OK) {
						posLogInfo = jsonToPosLogInfo(result);
					} else {
						posLogInfo.setNCRWSSResultCode(ResultBase.RES_POSLOG_NOTFOUND);
						posLogInfo.setNCRWSSExtendedResultCode(ResultBase.RES_POSLOG_NOTFOUND);
						posLogInfo.setMessage("PosLog info search failed");
					}
				} else if(result.has("ConnectionStatus") && result.getInt("ConnectionStatus") == HttpURLConnection.HTTP_UNAUTHORIZED) {
					LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_AUTHENTICATION_FAILED, "BASIC Authentication failed。\n");
					posLogInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
					posLogInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
					posLogInfo.setMessage("BASIC Authentication failed");
					return posLogInfo;
				}
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send remote POSLog.",e);
			if (e instanceof JSONException) {
				throw new JSONException(e);
			} else {
				throw new Exception();
			}
		} finally {
			tp.methodExit(posLogInfo);
		}
		return posLogInfo;
	}


	/***
	 * Add ParaMeters to The URL
	 *
	 * @param url
	 *            The remote url
	 * @param json
	 *            The ParaMeters
	 * @return The url
	 * @throws JSONException
	 *             The Exception of json
	 */
	private String getUrl(String url, JSONObject json) throws JSONException {
		Iterator<?> it = json.keys();
		int count = 0;
		url = url + "?";
		while (it.hasNext()) {

			String key = (String) it.next();
			if (count == 0) {
				url = url + key + "=" + json.get(key);
			} else {
				url = url + "&" + key + "=" + json.get(key);
			}
			count++;
		}
		return url;
	}

	/**
	 * JSONObject to Get PosLogInfo
	 *
	 * @param JSONObject
	 *            The data of the remote return
	 * @return PosLogInfo
	 * @throws NumberFormatException
	 *             The Number of The Format Exception
	 * @throws JSONException
	 *             The Json Exception
	 */
	private PosLogInfo jsonToPosLogInfo(JSONObject json) throws NumberFormatException, JSONException {
		PosLogInfo posLogInfo = new PosLogInfo();
		posLogInfo = new PosLogInfo();
		posLogInfo.setCompanyId(StringUtility.convNullStringToNull(json.getString("companyId")));
		posLogInfo.setRetailStoreId(StringUtility.convNullStringToNull(json.getString("retailStoreId")));
		posLogInfo.setBusinessDate(StringUtility.convNullStringToNull(json.getString("workstationId")));
		posLogInfo.setPOSLog(StringUtility.convNullStringToNull(json.getString("poslog")));
		posLogInfo.setTrainingFlag(StringUtility.convNullStringToNull(json.getString("trainingFlag")));
		posLogInfo.setWorkstationId(StringUtility.convNullStringToNull(json.getString("workstationId")));
		posLogInfo.setSequenceNumber(StringUtility.convNullStringToNull(json.getString("sequenceNumber")));
		return posLogInfo;
	}

	/**
	 * JSONObject to Get EjInfos
	 *
	 * @param jsonArray
	 *            The data of the remote return
	 * @return EjInfos
	 * @throws NumberFormatException
	 *             The Number of The Format Exception
	 * @throws JSONException
	 *             The Json Exception
	 */
	private EjInfos jsonToEjInfos(JSONArray jsonArray) throws NumberFormatException, JSONException {
		EjInfos ejInfos = new EjInfos();
		List<EjInfo> ejList = new ArrayList<EjInfo>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject itemJson = (JSONObject) jsonArray.get(i);
			EjInfo ejInfo = new EjInfo();
			ejInfo.setTxType(StringUtility.convNullStringToNull(itemJson.getString("txType")));
			ejInfo.setTxTypeName(StringUtility.convNullStringToNull(itemJson.getString("txTypeName")));
			ejInfo.setWorkstationId(StringUtility.convNullStringToNull(itemJson.getString("workstationId")));
			ejInfo.setSequenceNumber(StringUtility.convNullStringToNull(itemJson.getString("sequenceNumber")));
			ejInfo.setBusinessDateTimeStart(StringUtility.convNullStringToNull(itemJson.getString("businessDateTimeStart")));
			ejInfo.setBillingAmt(StringUtility.convNullStringToNull(itemJson.getString("billingAmt")));
			ejList.add(ejInfo);
		}
		ejInfos.setEjList(ejList);
		return ejInfos;
	}

	/**
	 * get Ej Info By TaxType
	 *
	 * @param companyId
     * @param retailstoreId
     * @param workstationId
     * @param txType
     * @param sequencenumberFrom
     * @param sequencenumberTo
     * @param businessDateTimeFrom
     * @param businessDateTimeTo
     * @param operatorId
     * @param salesPersonId
     * @param trainingFlag
     * @param maxNumber
     *
	 * @return EjInfos
	 *
	 * The exception thrown when searching failed.
	 */
	public EjInfos getEjInfoByTaxType(String companyId, String retailstoreId, String workstationId, String txType, String sequencenumberFrom,
			String sequencenumberTo, String businessDateTimeFrom, String businessDateTimeTo, String operatorId, String salesPersonId, String trainingFlag,
			String maxNumber) throws Exception{
		String functionName = DebugLogger.getCurrentMethodName();
		String NameCategory = null;
		EjInfos ejInfos = new EjInfos();
		List<EjInfo> listEjInfo = new ArrayList<EjInfo>();
		List<EjInfo> listNameSystemInfos = new ArrayList<EjInfo>();
		String commonStoreId = retailstoreId;
		if (listNameSystemInfo.isEmpty()) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_GET_DATA_ERR, "E/Jのリスト情報取得エラー。\n");
			ejInfos.setNCRWSSResultCode(ResultBase.RES_SYSTEM_NAME_NOTFOUND);
			ejInfos.setNCRWSSExtendedResultCode(ResultBase.RES_SYSTEM_NAME_NOTFOUND);
			ejInfos.setMessage("System Name List info is empty");
			return ejInfos;
		}

		INameSystemInfoDAO dao = new SQLServerNameSystemInfoDAO();
		Iterator<EjInfo> iterator = null;

		// get NameCategory
		NameCategory = dao.getNameCategory(Category, KeyId);
		if (StringUtility.isNullOrEmpty(NameCategory)) {
			NameCategory = "0061";
		}
		for(int count = 0; count < 2; count++){
			iterator = listNameSystemInfo.iterator();
			while (iterator.hasNext()) {
				EjInfo nameSystemInfo = iterator.next();
				if(nameSystemInfo.getCompanyId().equals(companyId) && nameSystemInfo.getStoreId().equals(commonStoreId)
						&& nameSystemInfo.getNameCategory().equals(NameCategory)){
					listNameSystemInfos.add(nameSystemInfo);
				}
			}
			if(!listNameSystemInfos.isEmpty()){
				break;
			}else{
				commonStoreId = "0";
			}
		}
		if (listNameSystemInfos.isEmpty()) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_GET_DATA_ERR, "E/Jのリスト情報取得エラー。\n");
			ejInfos.setNCRWSSResultCode(ResultBase.RES_SYSTEM_NAME_NOTFOUND);
			ejInfos.setNCRWSSExtendedResultCode(ResultBase.RES_SYSTEM_NAME_NOTFOUND);
			ejInfos.setMessage("Matched E/J list info search error");
			return ejInfos;
		} else {
			EjInfo ejInfo1 =null;
			listEjInfo = dao.getEjInfo(companyId, retailstoreId, workstationId, txType, sequencenumberFrom,
					sequencenumberTo, businessDateTimeFrom, businessDateTimeTo, operatorId, salesPersonId,
					trainingFlag, maxNumber);
			iterator = listEjInfo.iterator();
			while (iterator.hasNext()){
				ejInfo1 = iterator.next();
				for (EjInfo ejInfo2 : listNameSystemInfos) {
					if (ejInfo1.getTxType().equals(ejInfo2.getTxType())) {
						ejInfo1.setTxTypeName(ejInfo2.getTxTypeName());
					}
				}
			}
			iterator = listEjInfo.iterator();
			while (iterator.hasNext()){
				ejInfo1 = iterator.next();
				if(ejInfo1.getTxTypeName() == null){
					iterator.remove();
				}
			}
			if (!listEjInfo.isEmpty()) {
				ejInfos.setEjList(listEjInfo);
			} else {
				ejInfos.setNCRWSSExtendedResultCode(ResultBase.RES_EJINFOS_NOTFOUND);
				ejInfos.setNCRWSSResultCode(ResultBase.RES_EJINFOS_NOTFOUND);
				ejInfos.setMessage("E/J list info search error");
			}
		}
		return ejInfos;
	}
}
