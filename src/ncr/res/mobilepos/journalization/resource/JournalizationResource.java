/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * JournalizationResource
 *
 * Resource which provides Web Service for journalizing transaction
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.journalization.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.JournalizationException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.exception.TillException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JrnSpm;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.POSLogUtility;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.SpmFileWriter;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.constants.JournalizationConstants;
import ncr.res.mobilepos.journalization.constants.PosLogRespConstants;
import ncr.res.mobilepos.journalization.dao.ICommonDAO;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.journalization.helper.PosLogLogger;
import ncr.res.mobilepos.journalization.helper.UrlConnectionHelper;
import ncr.res.mobilepos.journalization.model.JSONData;
import ncr.res.mobilepos.journalization.model.PointPosted;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.SearchGuestOrder;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.journalization.model.poslog.AdditionalInformation;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.MemberInfo;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Journalization Web Resource Class.
 *
 * <P>Journalize transaction.
 *
 */
@Path("/transaction")
@Api(value="/transaction", description="������API")
public class JournalizationResource {
    /** A private member variable used for the servlet context. */
    @Context
    private ServletContext context; //to access the web.xml
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /** The Trace Printer. */
    private Trace.Printer tp = null;
    /** Snap Logger. */
    private SnapLogger snap;

    /**
     * The program name.
     */
    private static final String PROG_NAME = "Jrnalztn";
    /**
     * Default Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     */
    public JournalizationResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        snap = SnapLogger.getInstance();
    }

    /**
     * Custom Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     *
     * @param contextToSet The context of the servlet.
     */
    public JournalizationResource(final ServletContext contextToSet) {
        this();
        this.context = contextToSet;
    }

    /**
     * <P>Init method of journalization servlet.<br/>
     * This invokes after the constructor was called.<br/>
     * This initializes Spm File used in journalize method.
     */
    @PostConstruct
    public void init(){
    }

    /**
     * The method called by the Web Service to journalize a normal transaction.
     *
     * @param posLogXml     The PosLog XML necessary for Normal Transaction
     *
     * @return PosLogResp   The POSLog Response
     */
    @Path("/saveposlogxml")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    @ApiOperation(value="POSLog�̃A�b�v���[�h", response=PosLogResp.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXB�G���[")
    })
    public final PosLogResp journalize(//poslogxml�H
    		@ApiParam(name="poslogxml", value="POSLog (xml�f�[�^)")  @FormParam("poslogxml") final String posLogXml,
    		@ApiParam(name="trainingmode", value="�g���[�j���O���[�h�t���O")  @FormParam("trainingmode") final int trainingMode) {
        SpmFileWriter spmFw = SpmFileWriter.getInstance();
        JrnSpm jrnlSpm = JrnSpm.startSpm(spmFw);

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("POSLog xml", posLogXml);

        PosLogResp posLogResponse = null;
        PosLogLogger posLogger = new PosLogLogger();
        PosLog posLog = null;

        try {
        	XmlSerializer<PosLog> xmlTmpl = new XmlSerializer<PosLog>();
	        posLog = (PosLog) xmlTmpl.unMarshallXml(posLogXml, PosLog.class);

	        if (!POSLogHandler.isValid(posLog)) {
	        	tp.println("Required POSLog elements are missing.");
	            Snap.SnapInfo info =
	            		snap.write("Required POSLog elements are missing.", posLogXml);
				LOGGER.logSnap(PROG_NAME, functionName,
						"Invalid POSLog Transaction to snap file", info);
	            posLogResponse = new PosLogResp();
                posLogResponse.setMessage("Required POSLog elements are missing.");
                posLogResponse.setStatus(PosLogRespConstants.ERROR_END_1);
                posLogResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                return posLogResponse;
            }

            posLogResponse = posLogger.log(posLog, posLogXml, trainingMode);

        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Pos log xml data in journalize", posLogXml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Output error transaction data to snap file", infos);
            posLogResponse = new PosLogResp(ResultBase.RES_ERROR_JAXB,
                    ResultBase.RES_ERROR_JAXB, PosLogRespConstants.ERROR_END_1, e);
        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[]{
                    snap.write("Pos log xml data in journalize", posLogXml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Output error transaction data to snap file", infos);
            posLogResponse = new PosLogResp(ResultBase.RES_ERROR_DB,
                    ResultBase.RES_ERROR_DB, PosLogRespConstants.ERROR_END_2, e);
        } catch (JournalizationException e) {
        	Snap.SnapInfo[] infos = {
					snap.write("Pos log xml data in journalize", posLogXml),
					snap.write("Exception", e) };
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
					"Output error transaction data to snap file", infos);
			posLogResponse = new PosLogResp(e.getErrorCode(), e.getErrorCode(),
					PosLogRespConstants.ERROR_END_1, e);
        } catch (TillException e) {
        	 Snap.SnapInfo[] infos = {
 					snap.write("Pos log xml data in journalize", posLogXml),
 					snap.write("Exception", e) };
 			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_TILL, functionName,
 					"Output error transaction data to snap file", infos);
 			posLogResponse = new PosLogResp(e.getErrorCode(), e.getErrorCode(),
 					PosLogRespConstants.ERROR_END_1, e);
        } catch (ParseException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Pos log xml data in journalize", posLogXml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName,
                    "Output error transaction data to snap file", infos);
            posLogResponse = new PosLogResp(ResultBase.RES_ERROR_PARSE, ResultBase.RES_ERROR_PARSE,
                    PosLogRespConstants.ERROR_END_1, e);
        } catch (NamingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Pos log xml data in journalize", posLogXml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_NAMINGEXC, functionName,
                    "Failed to lookup 'ServerID' in Context", infos);
            posLogResponse = new PosLogResp(
                    ResultBase.RES_ERROR_NAMINGEXCEPTION, ResultBase.RES_ERROR_NAMINGEXCEPTION,
                    PosLogRespConstants.ERROR_END_1, e);
        } catch (SQLStatementException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Pos log xml data in journalize", posLogXml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName,
                    "Failed to read 'sql_statements.xml'", infos);
            posLogResponse = new PosLogResp(
                    ResultBase.RES_ERROR_SQLSTATEMENT, ResultBase.RES_ERROR_SQLSTATEMENT,
                    PosLogRespConstants.ERROR_END_1, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = {
					snap.write("Pos log xml data in journalize", posLogXml),
					snap.write("Exception", e) };
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
					"Output error transaction data to snap file", infos);
			posLogResponse = new PosLogResp(
			        ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL,
					PosLogRespConstants.ERROR_END_1, e);
        }

        if(spmFw != null){
			jrnlSpm.endSpm(POSLogUtility.toPosLog(posLogXml),
					Integer.parseInt(posLogResponse.getStatus()));
        }

        return (PosLogResp)tp.methodExit(posLogResponse);
    }

    /**
     * The method called by the Web Service to retrieve the
     * POSLog of a transaction in JSON format.
     * @param companyId
     * @param storeId        The StoreID
     * @param workstationId    The Terminal ID
     * @param businessDate
     * @param txId            The Transaction Number
     * @param trainingFlag
     * @param txtype
     * @return                The POSLog Object of a given transaction
     */
    @Path("/gettransactionposlog")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="����̎����POSLog�擾", response=SearchedPosLog.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="����̃f�[�^��������Ȃ�"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
    })
    public final SearchedPosLog getPOSLogTransactionByNumber(
    		@ApiParam(name="companyid", value="��ЃR�[�h") @QueryParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="�X�ԍ�") @QueryParam("storeid") final String storeId,
    		@ApiParam(name="deviceid", value="�^�[�~�i���ԍ�") @QueryParam("deviceid") final String workstationId,
    		@ApiParam(name="businessdate", value="�Ɩ����t") @QueryParam("businessdate") final String businessDate,
    		@ApiParam(name="txid", value="����ԍ�") @QueryParam("txid") final String txId,
    		@ApiParam(name="trainingmode", value="�g���[�j���O���[�h�t���O") @QueryParam("trainingmode") final int trainingFlag,
    		@ApiParam(name="txtype", value="������") @QueryParam("txtype") final String txtype) {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("CompanyId", companyId)
          .println("StoreID", storeId)
          .println("WorkstationId", workstationId)
          .println("BusinessDate", businessDate)
          .println("Transaction Number", txId)
          .println("TrainingMode", trainingFlag)
          .println("Txtype", txtype);
        SearchedPosLog poslog = new SearchedPosLog();
        String poslogXML = "";
        AdditionalInformation info = null;
        PointPosted pointPosted = null;
        int lockStatus = 0;
        int receiptCount = 0;
        try {
            DAOFactory sqlServer =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
            poslogXML =
                posLogDAO.getPOSLogTransaction(companyId, storeId, workstationId, businessDate, txId, trainingFlag, txtype);

            if (poslogXML.isEmpty()) {
                poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                tp.println("Transaction not found.");
            } else {
                XmlSerializer<SearchedPosLog> poslogSerializer =
                    new XmlSerializer<SearchedPosLog>();
                poslog =
                    poslogSerializer.unMarshallXml(poslogXML,
                            SearchedPosLog.class);

                info = posLogDAO.getVoidedAndReturned(companyId, storeId, workstationId, businessDate, txId, trainingFlag, txtype);
                lockStatus = posLogDAO.getOrUpdLockStatus(companyId, storeId, workstationId, businessDate, Integer.parseInt(txId), trainingFlag, "", "", "", "getLockStatus");
                receiptCount = posLogDAO.getSummaryReceiptCount(companyId,storeId, workstationId, txId, businessDate, trainingFlag);
                info.setSummaryReceipt(String.valueOf(receiptCount));
                info.setGiftReceipt(String.valueOf(posLogDAO.getGiftReceiptCount(companyId, storeId, workstationId, txId, businessDate, trainingFlag)));
                pointPosted = posLogDAO.isPointPosted(companyId, storeId, workstationId, businessDate, txId, trainingFlag);
                if(pointPosted.isPostPointed()){
                   String pointXML = posLogDAO.getPOSLogTransaction(pointPosted.getCompanyId(), pointPosted.getRetailStoreId(), pointPosted.getWorkstationId(),
                           pointPosted.getBusinessDayDate(), pointPosted.getSequenceNumber(), pointPosted.getTrainingFlag(), txtype);
                   if (!StringUtility.isNullOrEmpty(pointXML)) {
                       SearchedPosLog pointPoslog = new SearchedPosLog();
                       pointPoslog = poslogSerializer.unMarshallXml(pointXML,
                                       SearchedPosLog.class);
                       setMemberInfo(poslog,pointPoslog);
                   }
                }
                info.setPostPointed(pointPosted);
                info.setLocked(String.valueOf(lockStatus));
                info.setPoslogXML(poslogXML);
            }
        } catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME,
					Logger.RES_EXCEP_DAO,
                                    "Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME,
                    Logger.RES_EXCEP_GENERAL,
                                    "Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            poslog.setAdditionalInformation(info);
        }
        return (SearchedPosLog)tp.methodExit(poslog);
    }
    private void setMemberInfo(SearchedPosLog poslog, SearchedPosLog pointPoslog){
        List<LineItem> pointPoslogLineItems = pointPoslog.getTransaction().getRetailTransaction().getLineItems();
        MemberInfo memberInfo = null;
        for(int j = 0; j < pointPoslogLineItems.size(); j++){
            if(null !=pointPoslogLineItems.get(j).getPoints()){
                memberInfo = pointPoslogLineItems.get(j).getPoints();
                poslog.setMemberInfo(memberInfo);
                break;
            }
        }

    }
    /**
     * Gets the Business day date set by the Administrator.
     * @param companyId the company ID
     * @param storeId the store ID
     * @return    BusinessDate used for the transaction.
     */
    @GET
    @Path("/businessdate")
    @Produces({ MediaType.TEXT_PLAIN + ";charset=UTF-8" })
    @ApiOperation(value="�Ɩ����t�擾", response=String.class)
    @ApiResponses(value={})
    public final String getBussinessDate(
    	@ApiParam(name="companyid", value="��ЃR�[�h") @QueryParam("companyid") final String companyId,
        @ApiParam(name="storeid", value="�X�ԍ�") @QueryParam("storeid") final String storeId) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyid", companyId)
            .println("storeid", storeId);
        String businessDate = "";
        try {
            DAOFactory sqlServer =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
            businessDate = posLogDAO.getBussinessDate(companyId, storeId, null);
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get businessdaydate.", ex);
        }
        return tp.methodExit(businessDate);
    }

    /**
     * Gets the journal info.
     *
     * @param APIType
     *            the APIType
     * @param JournalData
     *            the JournalData
     * @return the journal info.
     */
    @Path("/list")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="������X�g�擾API", response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RESRPT_OK, message="OK"),
            @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="API�����G���["),
            @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="APIURL���s��"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="�z�X�g����"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O��O����"),
    })
    public final JSONData getTransactionReport(
            @ApiParam(name="APIType", value="API�^�C�v") @FormParam("APIType") String APIType,
            @ApiParam(name="JournalData", value="�W���[�i���f�[�^") @FormParam("JournalData") String JournalData) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("APIType", APIType);
        tp.println("JournalData", JournalData);

        JSONData jsonData = new JSONData();
        JSONObject result = null;
        String address = "";
        try {
            int timeOut = GlobalConstant.getApiServerTimeout();
            String apiUrl = GlobalConstant.getApiServerUrl();
            if ("JournalReceipt".equals(APIType)) {
                address = apiUrl + JournalizationConstants.JOURNALDETAILS_URL;
                result = UrlConnectionHelper.connectionForPost(address, JournalData, timeOut);
            } else {
            	String value = JournalData;
                address = apiUrl + JournalizationConstants.JOURNALLIST_URL + value;
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
            }

            if (result == null) {
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
            } else {
                jsonData.setJsonObject(result.toString());
                jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                    + "Failed to get transactionReport data.\n", e);
            jsonData
                    .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            jsonData
                    .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            jsonData.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + "Failed to get transactionReport data.\n", e);
                jsonData
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                jsonData
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                jsonData.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + "Failed to get transactionReport data.\n", e);
                jsonData
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                jsonData
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                jsonData.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + "Failed to get transactionReport data.\n", e);
            jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            jsonData
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            jsonData.setMessage(e.getMessage());
        } finally {
            tp.methodExit(jsonData);
        }
        return jsonData;
    }

    /**
     * @param storeId
     *            the store Id
     *
     * @param deviceId
     *            the device Id
     *
     * @param sequenceNo
     *            the sequence No
     *
     * @param businessDate
     *            the business Date
     *
     * @return the list of josn with resultcode. 0 for success.
     */
    @Path("/getAdvancedInfoBySequenceNo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�O����ԍ��擾", response=SearchGuestOrder.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="���N�G�X�g�p�����[�^���s��"),
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����G���[ (���t����Ȃ�)"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")

    })
    public final SearchGuestOrder getAdvancedInfoBySequenceNo(
    		@ApiParam(name="storeId", value="�X�ԍ�") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="deviceId", value="�^�[�~�i���ԍ�") @QueryParam("deviceId") final String deviceId,
    		@ApiParam(name="sequenceNo", value="����ԍ�") @QueryParam("sequenceNo") final String sequenceNo,
    		@ApiParam(name="businessDate", value="�Ɩ����t") @QueryParam("businessDate") final String businessDate) {

    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeId", storeId)
                .println("deviceId", deviceId)
                .println("sequenceNo", sequenceNo)
                .println("businessDate", businessDate);

        SearchGuestOrder searchGuestOrder = new SearchGuestOrder();

        try {
        	if (StringUtility.isNullOrEmpty(storeId)
                    || StringUtility.isNullOrEmpty(deviceId)
                    || StringUtility.isNullOrEmpty(sequenceNo)
                    || StringUtility.isNullOrEmpty(businessDate)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                searchGuestOrder
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                searchGuestOrder
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                searchGuestOrder.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return searchGuestOrder;
            }

            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            ICommonDAO iCommonDAO = sqlServer
                    .getCommonDAO();
            searchGuestOrder = iCommonDAO
                    .searchGuestOrderInfoBySequenceNo(storeId, deviceId,
                            sequenceNo, businessDate);
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to get advanced info by sequenceNo.", e);
            searchGuestOrder
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            searchGuestOrder
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            searchGuestOrder.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get advanced info by sequenceNo.", ex);
            searchGuestOrder
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            searchGuestOrder
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            searchGuestOrder.setMessage(ex.getMessage());
        }
        return (SearchGuestOrder)tp.methodExit(searchGuestOrder);
    }

     @GET
	 @Path("/getlastpaytransactionposlog")
	 @Produces({ MediaType.APPLICATION_JSON })
	 @ApiOperation(value="�ŐV�̓��o��POSLog�̎擾", response=SearchedPosLog.class)
	    @ApiResponses(value={
	    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="���N�G�X�g�p�����[�^���s��"),
	    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����G���[ (���t����Ȃ�)"),
	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
	    })
	 public final SearchedPosLog getLastPayTxPoslog(
			 @ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") String companyId,
			 @ApiParam(name="storeId", value="�X�ԍ�") @QueryParam("storeId") String storeId,
			 @ApiParam(name="terminalId", value="�^�[�~�i���ԍ�") @QueryParam("terminalId") String terminalId,
			 @ApiParam(name="businessDate", value="�Ɩ����t") @QueryParam("businessDate") String businessDate,
			 @ApiParam(name="trainingFlag", value="�g���[�j���O���[�h�t���O") @QueryParam("trainingFlag") int trainingFlag) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
     		.println("companyid", companyId)
         	.println("storeId", storeId)
         	.println("terminalId", terminalId)
         	.println("businessDate", businessDate)
         	.println("trainingFlag", trainingFlag);

		SearchedPosLog poslog = new SearchedPosLog();
		String poslogString = null;

		if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId, businessDate)) {
			tp.println("A required parameter is null or empty.");
			poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			poslog.setMessage("A required parameter is null or empty.");
			tp.methodExit(poslog.toString());
			return poslog;
        }

		try {
			DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			IPosLogDAO poslogDao = sqlServer.getPOSLogDAO();
			poslogString = poslogDao.getLastPayTxPoslog(companyId, storeId,
					terminalId, businessDate, trainingFlag);
			if (poslogString == null) {
				poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				poslog.setMessage("Last pay tx poslog not found.");
			} else {
				XmlSerializer<SearchedPosLog>
						poslogSerializer = new XmlSerializer<SearchedPosLog>();
				poslog = poslogSerializer.unMarshallXml(
						poslogString, SearchedPosLog.class);
			 }
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
            poslog.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get last payIn/payOut transaction poslog for "
                    + "companyId#" + companyId + ", storeId#" + storeId + ", "
                    + "terminalId#" + terminalId + ", businessDate" + businessDate + ", "
                    + "and trainingFlag" + trainingFlag + ": " + e.getMessage());
		 } finally {
			 tp.methodExit(poslog.toString());
		 }
		 return poslog;
	 }
     @GET
	 @Path("/getlastbalancingtransactionposlog")
	 @Produces({ MediaType.APPLICATION_JSON })
	 @ApiOperation(value="�ŐV�̍ݍ�POSLog�̎擾", response=SearchedPosLog.class)
	    @ApiResponses(value={
	    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="���N�G�X�g�p�����[�^���s��"),
	    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����G���[ (���t����Ȃ�)"),
	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
	    })
	 public final SearchedPosLog getLastBalancingTxPoslog(
			 @ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") String companyId,
			 @ApiParam(name="storeId", value="�X�ԍ�") @QueryParam("storeId") String storeId,
			 @ApiParam(name="terminalId", value="�^�[�~�i���ԍ�") @QueryParam("terminalId") String terminalId,
			 @ApiParam(name="businessDate", value="�Ɩ����t") @QueryParam("businessDate") String businessDate,
			 @ApiParam(name="trainingFlag", value="�g���[�j���O���[�h�t���O") @QueryParam("trainingFlag") int trainingFlag) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
     		.println("companyid", companyId)
         	.println("storeId", storeId)
         	.println("terminalId", terminalId)
         	.println("businessDate", businessDate)
         	.println("trainingFlag", trainingFlag);

		SearchedPosLog poslog = new SearchedPosLog(null);
		String poslogString = null;

		if (StringUtility.isNullOrEmpty(companyId, storeId, terminalId, businessDate)) {
			tp.println("A required parameter is null or empty.");
			poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			poslog.setMessage("A required parameter is null or empty.");
			tp.methodExit(poslog.toString());

            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "���N�G�X�g �p�����[�^�G���[\r\n" +
					"�N�G���[�p�����[�^���s�����Ă��܂��B");

			return poslog;
        }

		try {
			DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			IPosLogDAO poslogDao = sqlServer.getPOSLogDAO();
			poslogString = poslogDao.getLastBalancingTxPoslog(companyId, storeId,
					terminalId, businessDate, trainingFlag);
			if (poslogString != null) {
				XmlSerializer<SearchedPosLog>
						poslogSerializer = new XmlSerializer<SearchedPosLog>();
				poslog = poslogSerializer.unMarshallXml(
						poslogString, SearchedPosLog.class);
			}
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
            poslog.setNCRWSSResultCode(resultBaseErrorCode);
            LOGGER.logAlert(PROG_NAME, functionName, loggerErrorCode,
                    "Failed to get last balancing transaction poslog for "
                    + "companyId#" + companyId + ", storeId#" + storeId + ", "
                    + "terminalId#" + terminalId + ", businessDate" + businessDate + ", "
                    + "and trainingFlag" + trainingFlag + ": " + e.getMessage());
		 } finally {
			 tp.methodExit(poslog.toString());
		 }
		 return poslog;
	 }

     /**
     * ������b�N�X�e�[�^�X�̍X�V�ƃ`�F�b�N
     *
     * @param Type
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param SequenceNumber
     * @param BusinessDayDate
     * @param TrainingFlag
     * @return ResultBase
     */
     @Path("/updatelockstatus")
     @POST
     @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
     @ApiOperation(value="���b�N��ԍX�V", response=ResultBase.class)
     @ApiResponses(value={
     @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
     @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
     })
     public final ResultBase UpdateLockStatus(
    		 @ApiParam(name="Type", value="���") @FormParam("Type") String Type,
    		 @ApiParam(name="CompanyId", value="��ЃR�[�h") @FormParam("CompanyId") String CompanyId,
    		 @ApiParam(name="RetailStoreId", value="�X�ԍ�") @FormParam("RetailStoreId") String RetailStoreId,
    		 @ApiParam(name="WorkstationId", value="�^�[�~�i���ԍ�") @FormParam("WorkstationId") String WorkstationId,
    		 @ApiParam(name="SequenceNumber", value="����ԍ�") @FormParam("SequenceNumber") int SequenceNumber,
    		 @ApiParam(name="BusinessDayDate", value="�Ɩ����t") @FormParam("BusinessDayDate") String BusinessDayDate,
    		 @ApiParam(name="TrainingFlag", value="�g���[�j���O�t���O") @FormParam("TrainingFlag") int TrainingFlag,
    		 @ApiParam(name="CallType", value="�ďo�����") @FormParam("CallType") String CallType,
    		 @ApiParam(name="AppId", value="�v���O����ID") @FormParam("AppId") String AppId,
    		 @ApiParam(name="UserId", value="���[�U�[ID") @FormParam("UserId") String UserId) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.println("Type", Type);
        tp.println("CompanyId", CompanyId);
        tp.println("RetailStoreId", RetailStoreId);
        tp.println("WorkstationId", WorkstationId);
        tp.println("SequenceNumber", SequenceNumber);
        tp.println("BusinessDayDate", BusinessDayDate);
        tp.println("TrainingFlag", TrainingFlag);
        tp.println("CallType", CallType);
        tp.println("AppId", AppId);
        tp.println("UserId", UserId);

        ResultBase resultBase = new ResultBase();
        int result = 0;
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO iPosLogDAO = sqlServer.getPOSLogDAO();
            result = iPosLogDAO.getOrUpdLockStatus(CompanyId, RetailStoreId, WorkstationId, BusinessDayDate, SequenceNumber, TrainingFlag, CallType, AppId, UserId, Type);
            if (result == -1) {
                resultBase.setNCRWSSResultCode(result);
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get or update lock status.\n" + ex.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get or update lock status.\n" + ex.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, ex);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }
}

