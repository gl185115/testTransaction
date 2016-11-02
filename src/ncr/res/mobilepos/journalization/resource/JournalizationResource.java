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

import atg.taglib.json.util.JSONObject;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.JournalizationException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.exception.TillException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.FileHandler;
import ncr.res.mobilepos.helper.FileParser;
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
import ncr.res.mobilepos.journalization.dao.IBarneysCommonDAO;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.journalization.dao.SQLServerBarneysCommonDAO;
import ncr.res.mobilepos.journalization.helper.PosLogLogger;
import ncr.res.mobilepos.journalization.helper.UrlConnectionHelper;
import ncr.res.mobilepos.journalization.model.EventDetail;
import ncr.res.mobilepos.journalization.model.EventInformation;
import ncr.res.mobilepos.journalization.model.EventList;
import ncr.res.mobilepos.journalization.model.GoldCertificate;
import ncr.res.mobilepos.journalization.model.GuestZone;
import ncr.res.mobilepos.journalization.model.GuestZoneInfo;
import ncr.res.mobilepos.journalization.model.JSONData;
import ncr.res.mobilepos.journalization.model.PointPosted;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.Reservation;
import ncr.res.mobilepos.journalization.model.Salesperson;
import ncr.res.mobilepos.journalization.model.Salespersoninfo;
import ncr.res.mobilepos.journalization.model.SearchForwardPosLog;
import ncr.res.mobilepos.journalization.model.SearchGuestOrder;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.journalization.model.SequenceNo;
import ncr.res.mobilepos.journalization.model.poslog.AdditionalInformation;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.ViewStore;

/**
 * Journalization Web Resource Class.
 *
 * <P>Journalize transaction.
 *
 */
@Path("/transaction")
@Api(value="/transaction", description="取引情報API")
public class JournalizationResource {
    /** A private member variable used for the servlet context. */
    @Context
    private ServletContext context; //to access the web.xml
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /** A private member variable used for accessing the transaction file. */
    private FileParser fileParser = null;
    /** The Trace Printer. */
    private Trace.Printer tp = null;
    /** Snap Logger. */
    private SnapLogger snap;
    /** SPM Logger. */
    private SpmFileWriter spmFw;
    /** SPM filename. */
    public final static String SPM_FILENAME = "SPM_JOURNALIZATION";
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
        snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * Custom Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     *
     * @param contextToSet The context of the servlet.
     */
    public JournalizationResource(final ServletContext contextToSet) {
        this.context = contextToSet;
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * <P>Init method of journalization servlet.<br/>
     * This invokes after the constructor was called.<br/>
     * This initializes Spm File used in journalize method.
     */
    @PostConstruct
    public void init(){
    	try {
			javax.naming.Context env = (javax.naming.Context) new InitialContext().lookup("java:comp/env");
			String spmPath = (String) (env).lookup("Journalization/spmPath");
			String serverID = (String) (env).lookup("serverID");

			FileHandler.createDirectory(spmPath);

			if(spmPath!=null){
				File file = new File(spmPath + "/" + SPM_FILENAME + "_" + serverID);
				if(spmFw == null){
					try {
						spmFw = ncr.res.mobilepos.helper.SpmFileWriter.getInstance(file, true);
						spmFw.write(JrnSpm.HEADER);
					} catch (IOException e) {
						LOGGER.logWarning("Jrnalztn", "Init",
								Logger.RES_EXCEP_IO, e.getMessage());
					} finally {
						if (context.getAttribute(GlobalConstant.SPM_FW) == null) {
							context.setAttribute(GlobalConstant.SPM_FW, spmFw);
						}
					}
				}
			}
		} catch (NamingException e) {
			LOGGER.logWarning(PROG_NAME, "init", Logger.RES_EXCEP_PARSE, e);
		}
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
    @ApiOperation(value="通常の取引を記録する", response=PosLogResp.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー")
    })
    public final PosLogResp journalize(//poslogxml？
    		@ApiParam(name="poslogxml", value="poslogのxml")  @FormParam("poslogxml") final String posLogXml,
    		@ApiParam(name="trainingmode", value="トレーニングモード")  @FormParam("trainingmode") final int trainingMode) {
    	JrnSpm jrnlSpm = null;
		if (spmFw != null) {
			jrnlSpm = JrnSpm.startSpm(spmFw);
		}

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("POSLog xml", posLogXml);
        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

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
                    ResultBase.RES_ERROR_JAXB, PosLogRespConstants.ERROR_END_1,
                    e);
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
            posLogResponse = new PosLogResp(
                    ResultBase.RES_ERROR_PARSE,
                    ResultBase.RES_ERROR_PARSE,
                    PosLogRespConstants.ERROR_END_1, e);
        } catch (NamingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Pos log xml data in journalize", posLogXml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_NAMINGEXC, functionName,
                    "Failed to lookup 'ServerID' in Context", infos);
            posLogResponse = new PosLogResp(
                    ResultBase.RES_ERROR_NAMINGEXCEPTION,
                    ResultBase.RES_ERROR_NAMINGEXCEPTION,
                    PosLogRespConstants.ERROR_END_1, e);
        } catch (SQLStatementException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Pos log xml data in journalize", posLogXml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName,
                    "Failed to read 'sql_statements.xml'", infos);
            posLogResponse = new PosLogResp(
                    ResultBase.RES_ERROR_SQLSTATEMENT,
                    ResultBase.RES_ERROR_SQLSTATEMENT,
                    PosLogRespConstants.ERROR_END_1, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = {
					snap.write("Pos log xml data in journalize", posLogXml),
					snap.write("Exception", e) };
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
					"Output error transaction data to snap file", infos);
			posLogResponse = new PosLogResp(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL,
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
     * @param terminalid    The Terminal ID
     * @param storeid        The StoreID
     * @param txid            The Transaction Number
     * @return                The POSLog Object of a given transaction
     */
    @Path("/gettransactionposlog")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="取引のposlogを得る", response=SearchedPosLog.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="取引のデータが見つからない"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="無効のパラメータ"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public final SearchedPosLog getPOSLogTransactionByNumber(
    		@ApiParam(name="companyId", value="会社コード") @QueryParam("companyid") final String companyid,
    		@ApiParam(name="storeid", value="店舗コード") @QueryParam("storeid") final String storeid,
    		@ApiParam(name="deviceid", value="設備コード") @QueryParam("deviceid") final String workstationid,
    		@ApiParam(name="businessdate", value="営業日") @QueryParam("businessdate") final String businessdate,
    		@ApiParam(name="txid", value="取引コード") @QueryParam("txid") final String txid,
    		@ApiParam(name="trainingmode", value="トレーニングモード") @QueryParam("trainingmode") final int trainingflag,
    		@ApiParam(name="txtype", value="取引種別") @QueryParam("txtype") final String txtype) {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("CompanyId", companyid)
          .println("StoreID", storeid)
          .println("WorkstationId", workstationid)
          .println("BusinessDate", businessdate)
          .println("Transaction Number", txid)
          .println("TrainingMode", trainingflag)
          .println("txtype", txtype);
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
                posLogDAO.getPOSLogTransaction(companyid, storeid, workstationid, businessdate, txid, trainingflag, txtype);

            if (poslogXML.isEmpty()) {
                poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                tp.println("Transaction not found.");
            } else {
                XmlSerializer<SearchedPosLog> poslogSerializer =
                    new XmlSerializer<SearchedPosLog>();
                poslog =
                    poslogSerializer.unMarshallXml(poslogXML,
                            SearchedPosLog.class);

                info = posLogDAO.getVoidedAndReturned(companyid, storeid, workstationid, businessdate, txid, trainingflag, txtype);
                lockStatus = posLogDAO.getOrUpdLockStatus(companyid, storeid, workstationid, businessdate, Integer.parseInt(txid), trainingflag, "", "", "", "getLockStatus");
                receiptCount = posLogDAO.getSummaryReceiptCount(companyid,storeid, workstationid, txid, businessdate);
                info.setSummaryReceipt(String.valueOf(receiptCount));
                pointPosted = posLogDAO.isPointPosted(companyid, storeid, workstationid, businessdate, txid, trainingflag);
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

    /**
     * The method called by the Web Service to retrieve
     * the POSLog of a Last Normal Transaction in JSON format.
     * @param terminalid    The Terminal ID
     * @param storeid       The StoreID
     * @return              The POSLog Object of a given transaction
     */
    @Path("/getlasttransactionposlog")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="最後の通常の取引を得てください", response=SearchedPosLog.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="取引データは見つからない"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public final SearchedPosLog getLastNormalTransaction(
    		@ApiParam(name="deviceid", value="設備コード") @QueryParam("deviceid") final String terminalid,
    		@ApiParam(name="storeid", value="店舗コード") @QueryParam("storeid") final String storeid) {
        tp.methodEnter(DebugLogger.getCurrentMethodName())
          .println("TerminalID", terminalid)
          .println("StoreID", storeid);

        SearchedPosLog poslog = new SearchedPosLog();
        String poslogXML = "";
        try {
            DAOFactory sqlServer =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
            poslogXML = posLogDAO.getLastNormalTransaction(terminalid, storeid);

            if (poslogXML.isEmpty()) {
                poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                tp.println("Transaction not found.");
            } else {
                XmlSerializer<SearchedPosLog> poslogSerializer =
                    new XmlSerializer<SearchedPosLog>();
                poslog =
                    poslogSerializer.unMarshallXml(poslogXML,
                            SearchedPosLog.class);
            }
        } catch (DaoException ex) {
            LOGGER.logSnapException(PROG_NAME,
                    Logger.RES_EXCEP_DAO,
                                    "Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logSnapException("Jrnalztn",
                    Logger.RES_EXCEP_GENERAL, "Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        }
        return (SearchedPosLog)tp.methodExit(poslog);
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
    @ApiOperation(value="営業日を得る", response=String.class)
    @ApiResponses(value={})
    public final String getBussinessDate(
    	@ApiParam(name="companyid", value="会社コード") @QueryParam("companyid") final String companyId,
        @ApiParam(name="storeid", value="店舗コード") @QueryParam("storeid") final String storeId) {
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
     * Gets new Transaction number from transaction.xml file.
     * @return transaction number
     */
    @GET
    @Path("/gettxnumber")
    @Produces({ MediaType.TEXT_PLAIN + ";charset=UTF-8" })
    @ApiOperation(value="取引番号更新", response=String.class)
    public final String getTransactionNumber() {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        if (fileParser == null) {
            fileParser = new FileParser("c:\\temp\\transaction.xml");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        String filepath = fileParser.getFilePath();
        String txId = "";
        boolean isFileExists = true;

        File file = new File(filepath);
        if (!file.exists()) {
            isFileExists = false;
            if (!fileParser.writeToFile(getInitXmlData())) {
                return txId;
            }
        }

        synchronized (file) {
            try {
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(filepath);
                Element rootElement = doc.getDocumentElement();
                NodeList nodeTx =
                    rootElement.getElementsByTagName("TransactionNumber");
                long txNum = Integer.parseInt(nodeTx.item(0).getTextContent());
                txNum++;
                txId = String.valueOf(txNum);
                nodeTx.item(0).setTextContent(txId);

                if (isFileExists) {
                    NodeList nodeDate =
                        rootElement.getElementsByTagName("TransactionDateTime");
                    String businessDate = getDateTimeNow();
                    nodeDate.item(0).setTextContent(businessDate);
                }

                Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StreamResult result = new StreamResult(new File(filepath));
                DOMSource source = new DOMSource(doc);
                transformer.transform(source, result);
            } catch (ParserConfigurationException e) {
                LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_PARSE,
                        functionName + ": Failed to get txid from " + filepath,
                        e);
            } catch (SAXException e) {
                LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_PARSE,
                        functionName + ": Failed to get txid from " + filepath,
                        e);
            } catch (IOException e) {
                LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                        + ": Failed to get txid from " + filepath, e);
            } catch (TransformerException e) {
                LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_PARSE,
                        functionName + ": Failed to get txid from " + filepath,
                        e);
            }
        }
        return tp.methodExit(txId);
    }

    /**
     * Gets the initial data of an transaction.xml file.
     * @return String xml data
     */
    private String getInitXmlData() {
        StringBuilder sb = new StringBuilder();
        String businesdate = getDateTimeNow();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<Transaction>");
        sb.append("<TransactionNumber>0</TransactionNumber>");
        sb.append("<TransactionDateTime>"
                + businesdate + "</TransactionDateTime>");
        sb.append("</Transaction>");
        return sb.toString();
    }

    /**
     * Gets the date and time of the current date.
     * @return The Current Date and Time
     */
    private String getDateTimeNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return formatter.format(date);
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
    @ApiOperation(value="ジャーナルAPI呼出", response=JSONData.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESRPT_OK, message="OK"),
            @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="API検索エラー"),
            @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="APIURL無効"),
            @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="ホスト無効"),
            @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO例外発生"),
    })
    public final JSONData getTransactionReport(
            @ApiParam(name="APIType", value="APIタイプ") @FormParam("APIType") String APIType,
            @ApiParam(name="JournalData", value="ジャーナルデータ") @FormParam("JournalData") String JournalData) {

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
     * Search guestzone.
     *
     * @param null
     *
     * @return the list of josn with resultcode. 0 for success.
     *
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/getguestzoneList")
    @ApiOperation(value="客層のリストを得る", response=GuestZone.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="ドロワがすでに存在しています")
    })
    public final GuestZone getGuestZoneList() {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter(functionName);

        GuestZone result = new GuestZone();
        try {
            IBarneysCommonDAO iPerCtrlDao = new SQLServerBarneysCommonDAO();
            List<GuestZoneInfo> allguestzone = iPerCtrlDao.getGuestZoneList();

            if (allguestzone != null) {
                result.setGuestZoneInfos(allguestzone);
                result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                result.setMessage(ResultBase.RES_SUCCESS_MSG);
            } else {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
            }

        } catch (DaoException ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName
                            + ": Failed to get guest zone information.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName
                            + ": Failed to get guest zone information.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (GuestZone)tp.methodExit(result);
    }
    /*
    * @param the guestNO
    * @return  the list of josn with resultcode. 0 for success.
    */
    @Path("/getGuestOrderList")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="ゲストのための検索", response=SearchGuestOrder.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="パラメーターが無効です。"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出")
    })
    public final SearchGuestOrder getGuestOrderList(
    		@ApiParam(name="guestNo", value="お客様番号") @QueryParam("guestNo") final String guestNo) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("guestNo", guestNo);

        SearchGuestOrder searchGuestOrder = new SearchGuestOrder();

        try {
        	if (StringUtility.isNullOrEmpty(guestNo)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                searchGuestOrder.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                searchGuestOrder.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                searchGuestOrder.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return searchGuestOrder;
            }

            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IBarneysCommonDAO iBarneysCommenDAO = sqlServer
                    .getBarneysCommonDAO();
            searchGuestOrder = iBarneysCommenDAO.searchGuestOrderInfo(guestNo);
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to search Guest Order.", e);
            searchGuestOrder
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            searchGuestOrder
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            searchGuestOrder.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to search Guest Order.", ex);
            searchGuestOrder
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            searchGuestOrder
                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            searchGuestOrder.setMessage(ex.getMessage());
        }
        return (SearchGuestOrder)tp.methodExit(searchGuestOrder);
    }
    /**
     * @param OpeKanaName
     *            The OpeKanaName to get.
     * @return result Returns the SalesPerson
     */
    @Path("/getSalesperson")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="販売員情報", response=Salesperson.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public final Salesperson getSalesPerson(
    		@ApiParam(name="OpeKanaName", value="カナ氏名") @QueryParam("OpeKanaName") final String OpeKanaName) {

    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
          .println("OpeKanaName", OpeKanaName);

        Salesperson result = new Salesperson();
        try {
            DAOFactory daoFactory = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IBarneysCommonDAO ibarneysCommenDAO = daoFactory
                    .getBarneysCommonDAO();

            ArrayList<Salespersoninfo> allPerson = ibarneysCommenDAO
                    .SearchSalesPerson(OpeKanaName);

            if (allPerson != null) {
                Salespersoninfo[] arrayPerson = new Salespersoninfo[allPerson
                        .size()];
                allPerson.toArray(arrayPerson);
                result.setSalesperson(arrayPerson);
                result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                result.setMessage(ResultBase.RES_SUCCESS_MSG);
            } else {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
            }
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName
                            + ": Failed to SearchedSalesperson.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName
                            + ": Failed to SearchedSalesperson.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (Salesperson)tp.methodExit(result);
    }
    /**
     * @param SequenceTypeId
     * @return SequenceNo
     */
    @Path("/getSequenceNo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="順序番号を得る", response=SequenceNo.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データは見つからない"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public SequenceNo getSequenceNo(
    		@ApiParam(name="SequenceTypeId", value="シーケンスタイプコード") @QueryParam("SequenceTypeId") final String SequenceTypeId) {

    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter(functionName);
    	tp.println("SequenceTypeId", SequenceTypeId);

        SequenceNo sqNo = new SequenceNo();

        try {
        	if (StringUtility.isNullOrEmpty(SequenceTypeId)
                    || SequenceTypeId.length() > 4) {
                LOGGER.logError(PROG_NAME, "getSequenceNo", Logger.LOG_MSGID,
                        "SequenceTypeId from UI is not valid:" + SequenceTypeId);
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                sqNo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                sqNo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                sqNo.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return sqNo;
            }

            IBarneysCommonDAO dao = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getBarneysCommonDAO();

            sqNo = dao.getNextSequenceNo(SequenceTypeId);

            if (!StringUtility.isNullOrEmpty(sqNo.getSequenceNo())) {
                sqNo.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                sqNo.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                sqNo.setMessage(ResultBase.RES_SUCCESS_MSG);
            } else {
                sqNo.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                sqNo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                sqNo.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
            }

        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to get sequence number.", e);
            sqNo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            sqNo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            sqNo.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get sequence number.", ex);
            sqNo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            sqNo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            sqNo.setMessage(ex.getMessage());
        }
        return (SequenceNo)tp.methodExit(sqNo);
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
    @ApiOperation(value="序列号によって情報を得る", response=SearchGuestOrder.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")

    })
    public final SearchGuestOrder getAdvancedInfoBySequenceNo(
    		@ApiParam(name="storeId", value="会社コード") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="deviceId", value="端末コード") @QueryParam("deviceId") final String deviceId,
    		@ApiParam(name="sequenceNo", value="送信管理順序") @QueryParam("sequenceNo") final String sequenceNo,
    		@ApiParam(name="businessDate", value="営業日") @QueryParam("businessDate") final String businessDate) {

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
            IBarneysCommonDAO iBarneysCommenDAO = sqlServer
                    .getBarneysCommonDAO();
            searchGuestOrder = iBarneysCommenDAO
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
	/**
	 *
	 * @param compCat
	 * @return GoldCertificate
	 */
	@Path("/getGoldCertificateType")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="金券のタイプを取得する", response=GoldCertificate.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")

    })
	public final GoldCertificate getGoldCertificateType(//CrCompCat ？
			@ApiParam(name="CrCompCat", value="認証会社の種類") @QueryParam("CrCompCat") final String compCat) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("CrCompCat", compCat);

		GoldCertificate goldCertificate = new GoldCertificate();

		try {
			if (StringUtility.isNullOrEmpty(compCat)) {
				tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
				goldCertificate
						.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				goldCertificate
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				goldCertificate.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
				return goldCertificate;
			}

			DAOFactory sqlServer = DAOFactory
					.getDAOFactory(DAOFactory.SQLSERVER);
			IBarneysCommonDAO iBarneysCommenDAO = sqlServer
					.getBarneysCommonDAO();

			goldCertificate = iBarneysCommenDAO.getGoldCertificateType(compCat);
		} catch (DaoException e) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
					+ ": Failed to get gold certificate type.", e);
			goldCertificate.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			goldCertificate.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			goldCertificate.setMessage(e.getMessage());
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get gold certificate type.", ex);
			goldCertificate.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			goldCertificate.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			goldCertificate.setMessage(ex.getMessage());
		}
		return (GoldCertificate)tp.methodExit(goldCertificate);
	}
	 /**
     * @param EventId
     * @param EventKbn
     * @param BusinessDateId
     * @param StoreId
     * @param PluCode
     * @return the list of event information.
     */
	 @Path("/getEventList")
	 @GET
	 @Produces({ MediaType.APPLICATION_JSON })
	 @ApiOperation(value="イベントリストを得る", response=EventList.class)
	    @ApiResponses(value={
	    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
	    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
	    })
	 public final EventList getEventList(
			 @ApiParam(name="EventId", value="イベントコード") @QueryParam("EventId") final String eventId,
			 @ApiParam(name="EventKbn", value="イベント区分") @QueryParam("EventKbn") final int eventKbn,
			 @ApiParam(name="BusinessDateId", value="営業日コード") @QueryParam("BusinessDateId") final int businessDateId,
			 @ApiParam(name="StoreId", value="店舗番号") @QueryParam("StoreId") final String storeId,
			 @ApiParam(name="PluCode", value="付加コード") @QueryParam("PluCode") final String pluCode) {
	        String functionName = DebugLogger.getCurrentMethodName();
	        EventList result = new EventList();
	        tp.methodEnter(functionName)
	        .println("eventId", eventId)
	        .println("eventKbn", eventKbn)
	        .println("businessDateId", businessDateId)
	        .println("storeId", storeId)
	        .println("pluCode", pluCode);
	        try {
	        	if (StringUtility.isNullOrEmpty(storeId) ||
	        				!this.checkDate(businessDateId) || (eventKbn != 1
	        				&& eventKbn != 2)){
	                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
	                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
	                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
	                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
	                return result;
	            }

	            DAOFactory sqlServer = DAOFactory
	                    .getDAOFactory(DAOFactory.SQLSERVER);
	            IBarneysCommonDAO iBarneysCommenDAO = sqlServer
	                    .getBarneysCommonDAO();
	            List<EventDetail> eventList = iBarneysCommenDAO.getEventList(eventId,eventKbn,
	            		businessDateId,storeId,pluCode);
	            if (eventList != null) {
	                result.setEventDetails(eventList);
	                result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
	                result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
	                result.setMessage(ResultBase.RES_SUCCESS_MSG);
	            } else {
	                result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
	                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
	                result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
	                tp.println(ResultBase.RES_NODATAFOUND_MSG);
	            }
	        } catch (DaoException e) {
	            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
	                    + ": Failed to get event information.", e);
	            result
	                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
	            result
	                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
	            result.setMessage(e.getMessage());
	        } catch (Exception ex) {
	            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
	                    + ": Failed to get event information.", ex);
	            result
	                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
	            result
	                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
	            result.setMessage(ex.getMessage());
	        }
	        return (EventList)tp.methodExit(result);
	    }

	 /**
	     * @param EventId
	     * @param StoreId
	     * @param BusinessDateId
	     * @param EventKbn
	     * @return the event login check code.
	     */
		 @Path("/getEventLoginCheckCode")
		 @GET
		 @Produces({ MediaType.APPLICATION_JSON })
		 @ApiOperation(value="イベントのログインチェックコードを取得する", response=EventInformation.class)
		    @ApiResponses(value={
		    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
		    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
		    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
		    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
		    @ApiResponse(code=ResultBase.RES_ERROR_BUSINESSDATEIDCHECKFAILED, message="事件検査の失敗は、営業日と終わり、期日と終わり"),
		    @ApiResponse(code=ResultBase.RES_ERROR_EVENTKBNCHECKFAILED, message="事件の検査失敗はイベント区分が結果のイベント区分に等しい")

		    })
         public final EventInformation getEventLoginCheckCode(
        		 @ApiParam(name="EventId", value="イベントコード")  @QueryParam("EventId") final String eventId,
        		 @ApiParam(name="StoreId", value="店番")   @QueryParam("StoreId") final String storeId,
        		 @ApiParam(name="BusinessDateId", value="営業日コード")    @QueryParam("BusinessDateId") final int businessDateId,
        		 @ApiParam(name="EventKbn", value="イベント区分")    @QueryParam("EventKbn") final int eventKbn) {
		        String functionName = DebugLogger.getCurrentMethodName();
                EventInformation result = new EventInformation();
		        tp.methodEnter(functionName)
		        .println("eventId", eventId)
		        .println("storeId", storeId)
		        .println("businessDateId", businessDateId)
		        .println("eventKbn", eventKbn);
		        try {
		        	if (StringUtility.isNullOrEmpty(eventId,storeId) ||
		        				!this.checkDate(businessDateId) || (eventKbn != 1
		        				&& eventKbn != 2)){
		                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
		                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
		                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
		                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
		                return result;
		            }

		            DAOFactory sqlServer = DAOFactory
		                    .getDAOFactory(DAOFactory.SQLSERVER);
		            IBarneysCommonDAO iBarneysCommenDAO = sqlServer
		                    .getBarneysCommonDAO();
		            EventDetail eventDetail = iBarneysCommenDAO.getEventLoginResultSet(eventId,storeId);
		            if(eventDetail != null){
		            	if(businessDateId >= eventDetail.getStartDateId()
		            			&& businessDateId <= eventDetail.getEndDateId()){
		            		if(eventKbn == eventDetail.getEventKbn()){
                                result.setEventDetial(eventDetail);
		            			result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
		            			result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
		            			result.setMessage(ResultBase.RES_SUCCESS_MSG);
		            			return result;
		            		}else{
		            			result.setNCRWSSResultCode(ResultBase.RES_ERROR_EVENTKBNCHECKFAILED);
		            			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_EVENTKBNCHECKFAILED);
		            			result.setMessage(ResultBase.RES_VENTKBNCHECKEFAILED_MSG);
		            			return result;
		            		}
		            	}else{
		            		result.setNCRWSSResultCode(ResultBase.RES_ERROR_BUSINESSDATEIDCHECKFAILED);
	            			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_BUSINESSDATEIDCHECKFAILED);
	            			result.setMessage(ResultBase.RES_BUSINESSDATEIDCHECKFAILED_MSG);
	            			return result;
		            	}
		            }else{
		            	    result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
			                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
			                result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
			                return result;
		            }
		        } catch (DaoException e) {
		            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
		                    + ": Failed to get event login check code.", e);
		            result
		                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
		            result
		                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
		            result.setMessage(e.getMessage());
		        } catch (Exception ex) {
		            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
		                    + ": Failed to get event login check code.", ex);
		            result
		                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		            result
		                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
		            result.setMessage(ex.getMessage());
		        }
                return (EventInformation)tp.methodExit(result);
		    }

		  /*
		    * @param the reservationId
		    * @return  the list of reservation information.
		    */
		    @Path("/searchReservationList")
		    @GET
		    @Produces({ MediaType.APPLICATION_JSON })
		    @ApiOperation(value="検索予約リスト", response=Reservation.class)
		    @ApiResponses(value={
		    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
		    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
		    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
		    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
		    })
		    public final Reservation searchReservationList(
		    		@ApiParam(name="reservationId", value="予約コード") @QueryParam("reservationId") final String reservationId) {
		        String functionName = DebugLogger.getCurrentMethodName();
		        tp.methodEnter(functionName).println("reservationId", reservationId);

		        Reservation reservation = new Reservation();

		        try {
		        	if (StringUtility.isNullOrEmpty(reservationId)) {
		                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
		                reservation.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
		                reservation.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
		                reservation.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
		                return reservation;
		            }

		            DAOFactory sqlServer = DAOFactory
		                    .getDAOFactory(DAOFactory.SQLSERVER);
		            IBarneysCommonDAO iBarneysCommenDAO = sqlServer
		                    .getBarneysCommonDAO();
		            reservation = iBarneysCommenDAO.searchReservationInfo(reservationId);
		        } catch (DaoException e) {
		            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
		                    + ": Failed to search reservation list.", e);
		            reservation
		                    .setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
		            reservation
		                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
		            reservation.setMessage(e.getMessage());
		        } catch (Exception ex) {
		            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
		                    + ": Failed to search reservation list.", ex);
		            reservation
		                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		            reservation
		                    .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
		            reservation.setMessage(ex.getMessage());
		        }
		        return (Reservation)tp.methodExit(reservation);
		    }

    /**
     *  Forward PosLog save
     * @param poslogxml
     * @param queue
     * @param workstationid
     * @param trainingmode
     * @param total
     * @return int
     */
    @POST
    @Path("/saveforwardposlog")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="取引保存", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="DBエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXB例外"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_DATEINVALID, message="営業日無効"),
    })
    public final ResultBase saveForwardPosLog(
            @ApiParam(name="poslogxml", value="POSLOG XML") @FormParam("poslogxml") final String poslogxml,
            @ApiParam(name="queue", value="キューコード") @FormParam("queue") final String queue,
            @ApiParam(name="workstationid", value="端末コード") @FormParam("workstationid") final String workstationid,
            @ApiParam(name="trainingmode", value="トレーニングモード") @FormParam("trainingmode") final String trainingmode,
            @ApiParam(name="total", value="トータル") @FormParam("total") final String total) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("poslogxml", poslogxml);
        tp.methodEnter(functionName).println("queue", queue);
        tp.methodEnter(functionName).println("workstationid", workstationid);
        tp.methodEnter(functionName).println("trainingmode", trainingmode);
        tp.methodEnter(functionName).println("total", total);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        ResultBase resultBase = new ResultBase();

        try {
            // unmarshall poslog xml
            XmlSerializer<PosLog> poslogSerializer = new XmlSerializer<PosLog>();
            PosLog posLog = poslogSerializer.unMarshallXml(poslogxml, PosLog.class);

            // check if valid poslog
            if (!POSLogHandler.isValid(posLog)) {
                tp.println("Required POSLog elements are missing.");
                Snap.SnapInfo info = snap.write("Required POSLog elements are missing.", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, "Invalid POSLog Transaction to snap file", info);
                resultBase.setMessage("Required POSLog elements are missing.");
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                return resultBase;

            }

            // check if valid business date format
            String businessDate = posLog.getTransaction().getBusinessDayDate();
            if (!DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
                tp.println("BusinessDayDate should be in yyyy-MM-dd format.");
                resultBase.setNCRWSSResultCode(ResultBase.RESSYS_ERROR_QB_DATEINVALID);
                resultBase.setMessage("BusinessDayDate should be in yyyy-MM-dd format.");
                return resultBase;
            }

            // save poslog
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO quebusterDAO = dao.getPOSLogDAO();
            int result = quebusterDAO.saveForwardPosLog(posLog, poslogxml, queue, total);

            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                result = 0;
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Duplicate suspended transaction. It writes out to a snap file.");
                Snap.SnapInfo duplicatePOSLog = snap.write("Duplicate POSLog Transaction", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, "Duplicate POSLog Transaction to snap file", duplicatePOSLog);
            }

            resultBase.setNCRWSSResultCode(result);

        } catch (DaoException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", poslogxml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (JAXBException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", poslogxml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB, ResultBase.RES_ERROR_JAXB, ex);
        } catch (Exception ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", poslogxml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, ex);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * 前捌商品明細 PosLog 検索
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param SequenceNumber
     * @param Queue
     * @param BusinessDayDate
     * @param TrainingFlag
     * @return
     */
    @Path("/getforwarditems")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌き取引情報取得", response=SearchForwardPosLog.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="POSLOG無効"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
    })
    public final SearchForwardPosLog getForwardItems(
            @ApiParam(name="CompanyId", value="企業コード") @FormParam("CompanyId") String CompanyId,
            @ApiParam(name="RetailStoreId", value="店舗コード") @FormParam("RetailStoreId") String RetailStoreId,
            @ApiParam(name="WorkstationId", value="端末コード") @FormParam("WorkstationId") String WorkstationId,
            @ApiParam(name="SequenceNumber", value="取引番号") @FormParam("SequenceNumber") String SequenceNumber,
            @ApiParam(name="Queue", value="キュー番号") @FormParam("Queue") String Queue,
            @ApiParam(name="BusinessDayDate", value="営業日") @FormParam("BusinessDayDate") String BusinessDayDate,
            @ApiParam(name="TrainingFlag", value="トレーニングフラグ") @FormParam("TrainingFlag") String TrainingFlag) {
        tp.println("CompanyId", CompanyId);
        tp.println("RetailStoreId", RetailStoreId);
        tp.println("WorkstationId", WorkstationId);
        tp.println("SequenceNumber", SequenceNumber);
        tp.println("Queue", Queue);
        tp.println("BusinessDayDate", BusinessDayDate);
        tp.println("TrainingFlag", TrainingFlag);

        SearchForwardPosLog poslog = new SearchForwardPosLog();
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
            poslog = posLogDAO.getForwardItemsPosLog(CompanyId, RetailStoreId, WorkstationId, SequenceNumber, Queue,
                    BusinessDayDate, TrainingFlag);

            if (StringUtility.isNullOrEmpty(poslog.getPosLogXml())) {
                poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                tp.println("Forward poslog not found.");
            } else {
                XmlSerializer<PosLog> poslogSerializer = new XmlSerializer<PosLog>();
                poslog.setPoslog(poslogSerializer.unMarshallXml(poslog.getPosLogXml(), PosLog.class));
            }
        } catch (DaoException ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO, "Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logSnapException("Jrnalztn", Logger.RES_EXCEP_GENERAL, "Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        }
        return (SearchForwardPosLog) tp.methodExit(poslog);
    }

    /**
     * 前捌レコード ステータスの更新
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param SequenceNumber
     * @param Queue
     * @param BusinessDayDate
     * @param TrainingFlag
     * @param Status
     * @return ResultBase
     */
    @Path("/forwardupdate")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="前捌き取引情報更新", response=ViewStore.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
    })
    public final ResultBase updateForwardStatus(
            @ApiParam(name="CompanyId", value="企業コード") @FormParam("CompanyId") String CompanyId,
            @ApiParam(name="RetailStoreId", value="店舗コード") @FormParam("RetailStoreId") String RetailStoreId,
            @ApiParam(name="WorkstationId", value="端末コード") @FormParam("WorkstationId") String WorkstationId,
            @ApiParam(name="SequenceNumber", value="取引番号") @FormParam("SequenceNumber") String SequenceNumber,
            @ApiParam(name="Queue", value="キュー番号") @FormParam("Queue") String Queue,
            @ApiParam(name="BusinessDayDate", value="営業日") @FormParam("BusinessDayDate") String BusinessDayDate,
            @ApiParam(name="TrainingFlag", value="トレーニングフラグ") @FormParam("TrainingFlag") String TrainingFlag,
            @ApiParam(name="Status", value="ステータス") @FormParam("Status") int Status) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.println("CompanyId", CompanyId);
        tp.println("RetailStoreId", RetailStoreId);
        tp.println("WorkstationId", WorkstationId);
        tp.println("SequenceNumber", SequenceNumber);
        tp.println("Queue", Queue);
        tp.println("BusinessDayDate", BusinessDayDate);
        tp.println("TrainingFlag", TrainingFlag);
        tp.println("Status", Status);

        ResultBase resultBase = new ResultBase();
        try {
            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IBarneysCommonDAO iBarneysCommenDAO = sqlServer.getBarneysCommonDAO();
            int result = iBarneysCommenDAO.updateForwardStatus(CompanyId, RetailStoreId, WorkstationId, SequenceNumber,
                    Queue, BusinessDayDate, TrainingFlag, Status);

            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                result = 0;
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Failed to update Forward status.\n");
            }
            resultBase.setNCRWSSResultCode(result);

        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to update Forward status.\n" + ex.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to update Forward status.\n" + ex.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, ex);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

	 private boolean checkDate(int date){
		 Pattern p = Pattern.compile("\\d{4}+\\d{2}+\\d{2}+");
		 String dateString = String.valueOf(date);
	     Matcher m = p.matcher(dateString);
	     if (!m.matches()) {
	            return false;
	        }
	     int year = Integer.valueOf(dateString.substring(0, 4));
	     int month = Integer.valueOf(dateString.substring(4, 6));
	     int day = Integer.valueOf(dateString.substring(6, 8));
	     if (month < 1 || month > 12) {
	            return false;
	        }
	        int[] monthLengths = new int[] { 0, 31, -1, 31, 30, 31, 30, 31, 31, 30,
	                31, 30, 31 };
	        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
	            monthLengths[2] = 29;
	        } else {
	            monthLengths[2] = 28;
	        }
	        int monthLength = monthLengths[month];
	        if (day < 1 || day > monthLength) {
	            return false;
	        }
		 return true;
	 }

	 @GET
	 @Path("/getlastpaytransactionposlog")
	 @Produces({ MediaType.APPLICATION_JSON })
	 @ApiOperation(value="検索最後賃金取引のposLog情報", response=SearchedPosLog.class)
	    @ApiResponses(value={
	    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効のパラメータ"),
	    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
	    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
	    })
	 public final SearchedPosLog getLastPayTxPoslog(
			 @ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") String companyId,
			 @ApiParam(name="storeId", value="店番コード") @QueryParam("storeId") String storeId,
			 @ApiParam(name="terminalId", value="端末コード") @QueryParam("terminalId") String terminalId,
			 @ApiParam(name="businessDate", value="営業日") @QueryParam("businessDate") String businessDate,
			 @ApiParam(name="trainingFlag", value="トレーニングフラグ") @QueryParam("trainingFlag") int trainingFlag) {
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
	 /**
     * 取引ロックステータスの更新とチエック
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
     @ApiOperation(value="ロックの状態を更新", response=ResultBase.class)
     @ApiResponses(value={
     @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
     @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー")
     })
     public final ResultBase UpdateLockStatus(
    		 @ApiParam(name="Type", value="種別") @FormParam("Type") String Type,
    		 @ApiParam(name="CompanyId", value="会社コード") @FormParam("CompanyId") String CompanyId,
    		 @ApiParam(name="RetailStoreId", value="店舗コード") @FormParam("RetailStoreId") String RetailStoreId,
    		 @ApiParam(name="WorkstationId", value="ターミナルID") @FormParam("WorkstationId") String WorkstationId,
    		 @ApiParam(name="SequenceNumber", value="取引番号") @FormParam("SequenceNumber") int SequenceNumber,
    		 @ApiParam(name="BusinessDayDate", value="POS業務日付") @FormParam("BusinessDayDate") String BusinessDayDate,
    		 @ApiParam(name="TrainingFlag", value="トレーニングフラグ") @FormParam("TrainingFlag") int TrainingFlag,
    		 @ApiParam(name="CallType", value="呼出し種別") @FormParam("CallType") String CallType,
    		 @ApiParam(name="AppId", value="プログラムID") @FormParam("AppId") String AppId,
    		 @ApiParam(name="UserId", value="ユーザID") @FormParam("UserId") String UserId) {
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

