package ncr.res.mobilepos.networkreceipt.resource;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.TransactionVariable;
import ncr.res.mobilepos.customeraccount.dao.ICustomerDAO;
import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.*;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.journalization.model.poslog.*;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.networkreceipt.dao.IReceiptDAO;
import ncr.res.mobilepos.networkreceipt.helper.CreditSlipFormatter;
import ncr.res.mobilepos.networkreceipt.helper.CreditSlipPrint;
import ncr.res.mobilepos.networkreceipt.helper.PaperReceiptPrint;
import ncr.res.mobilepos.networkreceipt.model.ItemMode;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMode;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;
import ncr.res.mobilepos.store.dao.IStoreDAO;
import ncr.res.mobilepos.store.model.CMPresetInfo;
import ncr.res.mobilepos.store.model.CMPresetInfos;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;
import ncr.res.mobilepos.xebioapi.model.JSONData;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * NetworkReceipt Class is a Web Resource which support MobilePOS Email
 * processes.
 *
 */
@Path("/networkreceipt")
@Api(value="/networkreceipt", description="領収書印刷API")
public class NetworkReceipt {

    static class NoDataFoundException extends RuntimeException {
        NoDataFoundException(String msg) {
            super(msg);
        }
    }
    /**
     * Credit slip max lines.
     */
    public static final int CREDITSLIP_MAXLINES = 40;
    /**
     * the class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * the class instance of the debug trace printer.
     */
    private Trace.Printer tp;
    /**
     * Snap Logger.
     */
    private SnapLogger snap;
    /**
     * Default IP address.
     */
    private static final String IP_ADDR = "0.0.0.0";
    /**
     * Local Printer.
     */
    private static final String LOCAL_PRINTER = "0";
    /**
     * Local Printer.
     */
    private static final String PRINTER_TAG = "Printer";
    /**
     * Local Printer.
     */
    private static final String PORTS_TAG = "Ports";
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "NtwkRcpt";
    /**
     * constructor.
     */
    public NetworkReceipt() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * utility instance.
     */
    public NetworkReceipt(ServletContext cxt) {
        this();
        servContext = cxt;
    }

    /**
     * the class instance of the servlet context.
     */
    @javax.ws.rs.core.Context
    private ServletContext servContext;

    /**
     * WebService for print Credit Card Slip.
     *
     * @param txid The Transaction ID
     * @param deviceNo The Device Number
     * @param settlementtermid Terminal id for settlement.
     * @param storeNo The Store Number
     * @param operatorNo The Operator Number
     * @param language The Language for the Credit Slip
     * @param businessDate The Business Date
     * @return result of the operation
     */
    @Path("/printcreditcardslip/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="クレジットカード印刷", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RESNETRECPT_CREDSLIP_INVALID_DATE, message="無効な取引データ"),
        @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NG, message="領収書印刷エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="データ符号化支を持されていない"),
        @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンターポート未検出"),
    })
    public final ResultBase printCreditCardSlip(
    		@ApiParam(name="txid", value="取引番号") @PathParam("txid") final String txid,
    		@ApiParam(name="companyid", value="会社コード") @FormParam("companyid") final String companyId,
    		@ApiParam(name="deviceid", value="装置コード") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="settlementtermid", value="端末番号") @FormParam("settlementtermid") final String settlementtermid,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeNo,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") final int trainingFlag,
    		@ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="language", value="言語") @FormParam("language") final String language,
    		@ApiParam(name="businessdate", value="営業日") @FormParam("businessdate") final String businessDate) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("deviceNo", deviceNo)
                .println("settlementtermid", settlementtermid)
                .println("storeNo", storeNo).println("operatorNo", operatorNo)
                .println("language", language)
                .println("businessdate", businessDate);

        ResultBase resultBase = new ResultBase();
        try {
            String slipDatetime = businessDate;
            if (StringUtility.isNullOrEmpty(businessDate)) {
                PosLog poslog = getPosLogFromTxid(companyId, txid, deviceNo, storeNo, trainingFlag);
                if (!POSLogHandler.isValid(poslog)) {
                    String errorMessage = "Poslog is not valid.";
                    tp.println(errorMessage);
                    resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                            ResultBase.RES_ERROR_DB,
                            new Exception(errorMessage));
                    LOGGER.logAlert(PROG_NAME, functionName,
                            Logger.RES_EXCEP_GENERAL, errorMessage);
                    return resultBase;
                }
                slipDatetime = poslog.getTransaction().getBusinessDayDate();
            }

            if (!DateFormatUtility.isLegalFormat(slipDatetime, "yyyy-MM-dd")) {
                tp.println("BusinessDate is not in yyyy-MM-dd format.");
                resultBase
                        .setNCRWSSResultCode(ResultBase.RESNETRECPT_CREDSLIP_INVALID_DATE);
                return resultBase;
            }

            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            // get net printer information
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              null,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }

            // Get credit card slip data
            PaperReceiptPayment payment = new PaperReceiptPayment();
            PaperReceiptFooter footer = new PaperReceiptFooter();

            payment = iReceiptDAO.getPaperReceiptPayment(
                    GlobalConstant.getCorpid(), storeNo, settlementtermid,
                    txid, slipDatetime);

            footer = iReceiptDAO.getPaperReceiptFooter(storeNo, operatorNo, deviceNo,
                    txid);

            // Get Credit slip format data
            CreditSlipFormatter formatter = new CreditSlipFormatter(
                    CREDITSLIP_MAXLINES);
            List<String> lines = formatter.getCreditCardSlipFormat(payment,
                    footer, language);
            if (lines == null) {
                String errorMessage = "Failed to format Credit Slip.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                        ResultBase.RES_OK, new Exception(errorMessage));
                return resultBase;
            } else {
                byte[] text = formatter.getByteArrayForPrinter(lines);
                CreditSlipPrint slipPrint = new CreditSlipPrint(netPrinterInfo);
                int printResult = slipPrint.printCreditSlip(text);
                if (printResult != 0) {
                    resultBase.setNCRWSSResultCode(printResult);
                    tp.println("Failed to Print Credit Card Slip.").println(
                            "result", printResult);
                } else {
                    resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                    tp.println("Printed Credit Card Slip.");
                }
            }
        } catch (DaoException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName + ": Failed to print credit card slip.", e);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_DB, e);
        } catch (JAXBException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB,
                    functionName + ": Failed to print credit card slip.", e);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_ENCODING,
                    functionName + ": Failed to print credit card slip.", e);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (NamingException e) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_ENCODING,
                    functionName + ": Failed to get printer info.", e);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } finally {
            tp.methodExit(resultBase.toString());
        }

        return resultBase;
    }

    /**
     * Get the POSLog information by specifying the transaction number.
     *
     * @param txid The transaction number
     * @param deviceID The terminal number
     * @param storeno The store number
     * @return The POSLog information
     * @throws DaoException The Exception thrown when getting the POSLog xml in
     *             the database fails.
     * @throws JAXBException The exception thrown when the POSLog xml can not be
     *             deserialize as object.
     */
    private PosLog getPosLogFromTxid(final String companyId, final String txid, final String deviceID,
            final String storeno, final int trainingFlag) throws DaoException, JAXBException {

        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("txid", txid).println("deviceID", deviceID)
                .println("storeno", storeno);

        // Open IPosLogDAO Database and get Transaction
        IPosLogDAO ipldao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                .getPOSLogDAO();
        String transactionData = ipldao.getPOSLogTransaction(companyId, storeno, deviceID, null, txid,
        		trainingFlag, null);

        // Serialize from xml to Transaction
        XmlSerializer<PosLog> transSerializer = new XmlSerializer<PosLog>();
        PosLog poslog = transSerializer.unMarshallXml(transactionData,
                PosLog.class);

        tp.methodExit(poslog);
        return poslog;
    }

    /**
     * Gets the Email address of a Particular Customer.
     *
     * @param customerid The Customer ID
     * @return The Email Address of the Customer.
     * @throws DaoException Exception when getting the Customer information in
     *             the Database fails.
     */
    private String getEmailFromCustID(final String customerid)
            throws DaoException {

        tp.methodEnter(DebugLogger.getCurrentMethodName());
        // Open ICustomerDAO Database and get Transaction
        ICustomerDAO cdao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                .getCustomerDAO();
        Customer customer = cdao.getCustomerByID(customerid, false);

        tp.methodExit();

        if (customer == null) {
            return "";
        }

        return customer.getMailaddress();
    }

    /**
     * WebService for print summary receipt.
     *
     * @param txid          The Transaction ID
     * @param deviceNo      The Device Number
     * @param storeNo       The Store Number
     * @param operatorNo    The Operator Number
     * @param businessDate  The Business Date
     * @param txDate        The system date of transaction made
     * @param retryflag     The print retry flag
     * @param poslogxml     The poslog
     * @param trainingFlag  The training flag
     * @return result of the operation
     */
    @Path("/printsummaryreceipt/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="概要領収書印刷", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメーター"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="データ符号化支を持されていない"),
        @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンターポート未検出"),
    })
    public final ResultBase printSummaryReceipt(
    		@ApiParam(name="txid", value="取引番号") @PathParam("txid") final String txid,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeNo,
    		@ApiParam(name="deviceid", value="装置コード") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="businessdate", value="営業日") @FormParam("businessdate") final String businessDate,
    		@ApiParam(name="txdate", value="取引日付") @FormParam("txdate") final String txDate,
            @ApiParam(name="retryflag", value="リプリントフラグ") @FormParam("retryflag") final String retryflag,
            @ApiParam(name="poslog", value="poslog情報") @FormParam("poslog") final String poslogxml,
            @ApiParam(name="printerid", value="プリンターID") @FormParam("printerid") final String printerid,
            @ApiParam(name="printerid", value="トレーニングフラグ") @FormParam("trainingflag") final int trainingFlag) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("storeid", storeNo).println("deviceid", deviceNo)
                .println("operatorno", operatorNo)
                .println("businessdate", businessDate)
                .println("retryflag", retryflag).println("poslog", poslogxml)
                .println("txdate", txDate)
                .println("printerid", printerid)
                .println("trainingflag", trainingFlag);

        ResultBase resultBase = new ResultBase();

        try {
            PosLog poslog = getPosLog(txid, storeNo, deviceNo, trainingFlag, businessDate, poslogxml);

            if (!POSLogHandler.isValid(poslog)) {
                String errorMessage = "Poslog is not valid.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }

            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            // get net printer information
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              printerid,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }

            ReceiptMode receiptMode = this.resolvePoslog(poslog);
            if(StringUtility.isNullOrEmpty(receiptMode)){
                String errorMessage = "poslog resolve by resolvePoslog method error.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_INVALIDPARAMETER,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }
            receiptMode.setPublishedSequenceNo(txid);
            receiptMode.setPublishedWorkStationID(deviceNo);
            receiptMode.setPublishedBeginDateTime(txDate);
            setLanguage(null, receiptMode);
            receiptMode
                    .setRetryflag(StringUtility.isNullOrEmpty(retryflag) ? retryflag
                            : retryflag.toLowerCase());

            List<List<byte[]>> receiptsList = summaryReceipt(iReceiptDAO, receiptMode);
            String docTaxStampPath = null;
            boolean haveDocTax = false;
            if("true".equals(receiptMode.getHaveDocTax())){
                haveDocTax = true;
                docTaxStampPath = iReceiptDAO.getDocTaxStampPath(storeNo);
            }
            String logopath = iReceiptDAO.getLogoFilePath(storeNo);
            Transaction tran = poslog.getTransaction();
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, docTaxStampPath, haveDocTax,
                    null,
                    new FormatSummaryReceiptByXML().getPrintDirection(), 
                    tran.getRetailStoreID(),
                    tran.getWorkStationID().getValue(), tran.getSequenceNo());
            int printResult = receiptPrint.printAllReceipt(receiptsList);

            if (printResult != ResultBase.RESNETRECPT_OK) {
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setMessage("Failed to Print Summary Receipt");
                tp.println("Failed to Print Summary Receipt.").println(
                        "result", printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setMessage("Print Summary Receipt");
                tp.println("Print Summary Receipt.");
            }

        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_DB, e);
        } catch (UnsupportedEncodingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_ENCODING, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (IOException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_IO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_IOEXCEPTION, e);
        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }
    
	public List<List<byte[]>> summaryReceipt(IReceiptDAO iReceiptDAO,
			ReceiptMode receiptMode) throws NamingException,
			UnsupportedEncodingException, ParseException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<List<byte[]>> receiptsList = new ArrayList<List<byte[]>>();
		Context env = (Context) new InitialContext().lookup("java:comp/env");
		FormatSummaryReceiptByXML frbx = null;
		
			int[] types = new int[4];
			
				types[0] = 1;
				types[1] = 2;

			String vdRcptFormatPath = "";
			for (int i : types) {
				if (i != 0) {

					switch (i) {
					case 1:
						vdRcptFormatPath = (String) env
								.lookup("rnRcptForSummaryFormatPath");
						break;
					case 2:
						vdRcptFormatPath = (String) env
								.lookup("rnRcptForSummaryForShopFormatPath");
						break;
					default:
						break;
					}

					if (!StringUtility.isNullOrEmpty(vdRcptFormatPath)
							&& (new File(vdRcptFormatPath)).exists()) {

						frbx = new FormatSummaryReceiptByXML(receiptMode,
								vdRcptFormatPath);
						receiptsList.add(frbx.getReceipt());
					} 
				}
			}
		return receiptsList;
	}

	/**
     * WebService for print point ticket receipt.
     *
     * @param txid          The Transaction ID
     * @param deviceNo      The Device Number
     * @param storeNo       The Store Number
     * @param operatorNo    The Operator Number
     * @param businessDate  The Business Date
     * @param retryflag	    The retry print flag
     * @param poslogxml     The poslog
     * @param printerid     The printer ID
     * @param trainingFlag  The training flag
     * @return result of the operation
     */
    @Path("/printpointticketreceipt/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="ポイントチケット領収書印刷", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメータ"),
        @ApiResponse(code=ResultBase.RESNETRECPT_ERROR_NG, message="領収書印刷エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="データ符号化支を持されていない"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常"),
        @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンターポート未検出"),
    })
    public final ResultBase printPointTicketReceipt(
    		@ApiParam(name="txid", value="取引番号") @PathParam("txid") final String txid,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeNo,
    		@ApiParam(name="deviceid", value="装置コード") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="businessdate", value="営業日") @FormParam("businessdate") final String businessDate,
    		@ApiParam(name="retryflag", value="リプリントフラグ") @FormParam("retryflag") final String retryflag,
    		@ApiParam(name="poslog", value="poslog情報") @FormParam("poslog") final String poslogxml,
    		@ApiParam(name="printerid", value="プリンターID") @FormParam("printerid") final String printerid,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") final int trainingFlag) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("storeid", storeNo).println("deviceid", deviceNo)
                .println("operatorno", operatorNo)
                .println("businessdate", businessDate)
                .println("retryflag", retryflag).println("poslog", poslogxml)
                .println("printerid", printerid)
                .println("trainingflag", trainingFlag);

        ResultBase resultBase = new ResultBase();

        try {
            PosLog poslog = getPosLog(txid, storeNo, deviceNo, trainingFlag, businessDate, poslogxml);

            if (!POSLogHandler.isValid(poslog)) {
                String errorMessage = "Poslog is not valid.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }

            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            // get net printer information
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              printerid,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }

            ReceiptMode receiptMode = this.resolvePoslog(poslog);
            if(StringUtility.isNullOrEmpty(receiptMode)){
                String errorMessage = "poslog resolve by resolvePoslog method error.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_INVALIDPARAMETER,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }
            IStoreDAO iStoreDAO = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getStoreDAO();
            ViewStore viewStore = null;
            viewStore = iStoreDAO.viewStore(storeNo);
            receiptMode.setDirection("v");
            setLanguage(null, receiptMode);
            receiptMode.setTransactionType(TransactionVariable.POINTTICKET);
            receiptMode.setPtTicketDate(PointsConstantVars.pointTicketDate);
            receiptMode.setPtTicketHdr1(PointsConstantVars.pointTicketHdr1);
            receiptMode.setPtTicketHdr2(PointsConstantVars.pointTicketHdr2);
            receiptMode.setPtTicketMsg1(PointsConstantVars.pointTicketMsg1);
            receiptMode.setTelNo(viewStore.getStore().getTel());
            receiptMode.setReceiptType("v");
            receiptMode
                    .setRetryflag(StringUtility.isNullOrEmpty(retryflag) ? retryflag
                            : retryflag.toLowerCase());
            FormatSummaryReceiptByXML frbx = null;
            List<List<byte[]>> receiptsList = new ArrayList<List<byte[]>>();
            
            String docTaxStampPath = null;
            String vdRcptFormatPath = "";
            Boolean haveDocTax = false;
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            
            vdRcptFormatPath = (String) env
					.lookup("rnRcptForPointTicketFormatPath");
            
            String logopath = iReceiptDAO.getLogoFilePath(storeNo);
            Transaction tran = poslog.getTransaction();
            
            frbx = new FormatSummaryReceiptByXML(receiptMode,
					vdRcptFormatPath);
            receiptsList.add(frbx.getReceipt());
            
            String company, dt, ticketval;
            receiptMode = new ReceiptMode();
            company = poslog.getTransaction().getOrganizationHierarchy().getLevel();
            ticketval = poslog.getTransaction().getRetailTransaction().getLineItems().get(0).getPointTicket().getPointsRedeemed();
            if (company.equalsIgnoreCase("Xebio")) {
            	receiptMode.setPtTicketBarcodeIdentifier("13");
        	} else {
        		receiptMode.setPtTicketBarcodeIdentifier("23");
        	}
            ticketval = String.format("%07d", Integer.parseInt(ticketval));
            dt = poslog.getTransaction().getBusinessDayDate();
            dt = addMonths(dt, 1);
            receiptMode.setPtTicketValueBarcode(ticketval);
            receiptMode.setSequenceNo(poslog.getTransaction().getSequenceNo());
            receiptMode.setPtTicketExpDate(dt);
            receiptMode.setPtTicketFixedValue("001");
            receiptMode.setReceiptType("Normal");
            vdRcptFormatPath = (String) env
					.lookup("rnRcptForPointTicketBarcodeFormatPath");
            FormatReceiptByXML frbxx = new FormatReceiptByXML(receiptMode, 
            		vdRcptFormatPath);
            receiptsList.add(frbxx.getReceipt());
            
            PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, docTaxStampPath, haveDocTax,
                    null,
                    new FormatSummaryReceiptByXML().getPrintDirection(), 
                    tran.getRetailStoreID(),
                    tran.getWorkStationID().getValue(), tran.getSequenceNo());
            int printResult = receiptPrint.printAllReceipt(receiptsList, false);

            if (printResult != ResultBase.RESNETRECPT_OK) {
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setMessage("Failed to Print Summary Receipt");
                tp.println("Failed to Print Summary Receipt.").println(
                        "result", printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setMessage("Print Summary Receipt");
                tp.println("Print Summary Receipt.");
            }

        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_DB, e);
        } catch (UnsupportedEncodingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_ENCODING, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (IOException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_IO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_IOEXCEPTION, e);
        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }
    
    /**
     * Prints sales transaction receipt.
     * @param txid                          The transaction number.
     * @param storeNo                       The store number.
     * @param deviceNo                      The terminal number.
     * @param operatorNo                    The operator number.
     * @param businessDate                  The business day date.
     * @param language		                The label's display language.
     *                                      "en" for English text. "ja" for Japanese text.
     * @param retryflag                     The identifier for reprinting of receipt.
     *                                      "true" if reprint receipt. "false" if first print of receipt.
     * @param poslogxml                     The POSLog xml of sales transaction to be printed.
     * @param receiptForCust                The receipt for customer
     * @param receiptForShop                The receipt for shop
     * @param receipttwoforshop             The second receipt for shop
     * @param receiptForCardCompany         The receipt for card company
     * @param receiptForCommodityAttached   The receipt for commodity attached
     * @param printerid                     The Printer ID (output destination of receipt)
     * @param trainingFlag                  The Training Flag
     * @return                              If printing is successful, NRWSSResultCode=0.
     */
    @Path("/printpaperreceipt/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="売上領収書印刷", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメーター"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="データ符号化支を持されていない"),
        @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンターポート未検出"),
    })
    public final ResultBase printNormalReceipt(
    		@ApiParam(name="txid", value="取引番号") @PathParam("txid") final String txid,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeNo,
    		@ApiParam(name="deviceid", value="装置コード") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="businessdate", value="営業日") @FormParam("businessdate") final String businessDate,
    		@ApiParam(name="language", value="言語") @FormParam("language") final String language,
    		@ApiParam(name="retryflag", value="リプリントフラグ") @FormParam("retryflag") final String retryflag,
    		@ApiParam(name="poslog", value="poslog情報") @FormParam("poslog") final String poslogxml,
    		@ApiParam(name="receiptforcust", value="顧客領収書") @FormParam("receiptforcust") final String receiptForCust,
    		@ApiParam(name="receiptforshop", value="店舗領収書") @FormParam("receiptforshop") final String receiptForShop,
    		@ApiParam(name="receipttwoforshop", value="セコンド店舗領収書") @FormParam("receipttwoforshop") final String receipttwoforshop,
    		@ApiParam(name="receiptforcardcompany", value="カード発行会社領収書") @FormParam("receiptforcardcompany") final String receiptForCardCompany,
    		@ApiParam(name="receiptforcommodityattached", value="アタッチ商品領収書") @FormParam("receiptforcommodityattached") final String receiptForCommodityAttached,
    		@ApiParam(name="printerid", value="プリンターID") @FormParam("printerid") final String printerid,
    		@ApiParam(name="StoreId", value="トレーニングフラグ") @FormParam("trainingflag") final int trainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("storeNo", storeNo).println("deviceNo", deviceNo)
                .println("operatorNo", operatorNo)
                .println("businessdate", businessDate)
                .println("language", language).println("retryflag", retryflag)
                .println("poslog", poslogxml)
                .println("receiptforcust", receiptForCust)
                .println("receiptforshop", receiptForShop)
                .println("receiptforcardcompany", receiptForCardCompany)
                .println("receiptForCommodityAttached",receiptForCommodityAttached)
                .println("printerid", printerid)
                .println("trainingflag", trainingFlag);

        ResultBase resultBase = new ResultBase();
        try {
            PosLog poslog = getPosLog(txid, storeNo, deviceNo, trainingFlag, businessDate, poslogxml);

            if (!POSLogHandler.isValid(poslog)) {
                String errorMessage = "Poslog is not valid.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_INVALIDPARAMETER,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }

            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              printerid,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }
            ReceiptMode receiptMode = this.resolvePoslog(poslog);
            if(receiptMode == null){
                String errorMessage = "poslog resolve by resolvePoslog method error.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_INVALIDPARAMETER,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }
            setLanguage(language, receiptMode);
            receiptMode.setRetryflag(StringUtility.isNullOrEmpty(retryflag) ? retryflag
                            : retryflag.toLowerCase());

            List<List<byte[]>> receiptsList = normalReceipt(
                                                            iReceiptDAO,
                                                            receiptMode,
                                                            receiptForCust,
                                                            receiptForCardCompany,
                                                            receiptForShop,
                                                            receipttwoforshop,
                                                            receiptForCommodityAttached
                                                            );

            // call print service
            int printResult;
            Transaction tran = poslog.getTransaction();
            String urlCompare = netPrinterInfo.getUrl();
            if(IP_ADDR.equals(urlCompare)){
                int position = 8;
                if (!StringUtility.isNullOrEmpty(retryflag)
                        && "true".equalsIgnoreCase(retryflag)) {
                    position = 4;
                }
                printResult =  PrintReceiptToImg.printImage(receiptsList,receiptMode,deviceNo, position);
            }else{
                String docTaxStampPath = null;
                boolean haveDocTax = false;
                if ("true".equalsIgnoreCase(receiptMode.getHaveDocTax())) {
                    haveDocTax = true;
                    docTaxStampPath = iReceiptDAO.getDocTaxStampPath(storeNo);
                }
                String logopath = iReceiptDAO.getLogoFilePath(storeNo);
                PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                        netPrinterInfo, logopath, docTaxStampPath, haveDocTax,
                        tran.getRetailStoreID(), tran.getWorkStationID().getValue(),
                        tran.getSequenceNo());
                printResult = receiptPrint.printAllReceipt(receiptsList);
            }
            if (printResult != ResultBase.RESNETRECPT_OK) {
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setMessage("Failed to Print Normal Receipt.");
                tp.println("Failed to Print Normal Receipt.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setMessage("Print Normal Receipt");
                tp.println("Print Normal Receipt.");
            }

        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[]{
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName, "Output error data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
                    ResultBase.RES_ERROR_DB, e);
        } catch (UnsupportedEncodingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_ENCODING,
                    functionName, "Output error data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_UNSUPPORTEDENCODING,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (IOException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_IO,
                    functionName, "Output error data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
                    ResultBase.RES_ERROR_IOEXCEPTION, e);
        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB,
                    functionName, "Output error data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName, "Output error data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    public List<List<byte[]>> normalReceipt(
                                            IReceiptDAO iReceiptDAO,
                                            ReceiptMode receiptMode,
                                            String receiptForCust,
                                            String receiptForCardCompany,
                                            String receiptForShop,
                                            String receipttwoforshop,
                                            String receiptForCommodityAttached
                                            ) throws NamingException,
                                                     UnsupportedEncodingException,
                                                     ParseException,
                                                     IllegalAccessException,
                                                     InvocationTargetException,
                                                     NoSuchMethodException {
        List<List<byte[]>> receiptsList = new ArrayList<List<byte[]>>();
        // Format receiptmode by XML or java source code.
        Context env = (Context) new InitialContext().lookup("java:comp/env");

        FormatReceiptByXML frbx = null;
        ReceiptNormalFormatter rf = null;
        if(receiptMode.getCreditPayment() > 0 || receiptMode.getExemptTaxAmount() > 0 
                || Boolean.TRUE.toString().equalsIgnoreCase(receiptMode.getIsAdvanceFlag())
                || Boolean.TRUE.toString().equalsIgnoreCase(receiptMode.getIsRainCheckFlag())){
            int[] types = new int[5];
            // types[0] guest
            // types[1] card company
            // types[2] shop 
            // types[3] commodity attached
            // types[4] credit and tax free other receipt for shop
            if (!StringUtility.isNullOrEmpty(receiptForCust)
                && ResultBase.TRUE.equalsIgnoreCase(receiptForCust)) {
                types[0] = 1;
            }
            if (!StringUtility.isNullOrEmpty(receiptForCardCompany)
                && ResultBase.TRUE.equalsIgnoreCase(receiptForCardCompany)
                && receiptMode.getCreditPayment() > 0) {
                types[1] = 2;
            }
            if (!StringUtility.isNullOrEmpty(receiptForShop) 
            	&& ResultBase.TRUE.equalsIgnoreCase(receiptForShop)) {
                types[2] = 3;
            }
            if (!StringUtility.isNullOrEmpty(receiptForCommodityAttached)
                && ResultBase.TRUE.equalsIgnoreCase(receiptForCommodityAttached)
                && (Boolean.TRUE.toString().equalsIgnoreCase(receiptMode.getIsAdvanceFlag()) ||
                        Boolean.TRUE.toString().equalsIgnoreCase(receiptMode.getIsRainCheckFlag()))){
                types[3] = 4;
            }
            if(!StringUtility.isNullOrEmpty(receipttwoforshop) 
                && ResultBase.TRUE.equalsIgnoreCase(receipttwoforshop)
                && receiptMode.getCreditPayment() > 0
                && (receiptMode.getExemptTaxAmount() > 0 || receiptMode.getAdvanceAmount() > 0)){
            	types[4] = 5;
            }
            String vdRcptFormatPath = "";
            for (int i : types) {
                if (i != 0) {

                    switch (i) {
                    case 1:
                        vdRcptFormatPath = (String) env
                            .lookup("nrRcptFormatPath");
                        break;
                    case 2:
                        vdRcptFormatPath = (String) env
                            .lookup("creditSlipRcptCardCompFormatPath");
                        break;
                    case 3:
                        vdRcptFormatPath = (String) env
                            .lookup("creditSlipRcptShopFormatPath");
                        break;
                    case 4:
                        vdRcptFormatPath = (String) env
                            .lookup("advRcptCommodityFormatPath");
                        break;
                    case 5:
                    	vdRcptFormatPath = (String)env
                                .lookup("creditSlipRcptShopFormatPath");
                    default:
                        break;
                    }

                    if (!StringUtility.isNullOrEmpty(vdRcptFormatPath)
                        && (new File(vdRcptFormatPath)).exists()) {

                        frbx = new FormatReceiptByXML(receiptMode,
                                                      vdRcptFormatPath);
                        receiptsList.add(frbx.getReceipt());
                    } else {
                        rf = new ReceiptNormalFormatter(receiptMode,
                                                        CREDITSLIP_MAXLINES, 0);
                        receiptsList.add(rf.getReceipt());
                    }
                }
            }
        }else{
            String nrRcptFormatPath = (String) env.lookup("nrRcptFormatPath");
            if (!StringUtility.isNullOrEmpty(nrRcptFormatPath)
                && (new File(nrRcptFormatPath)).exists()) {
                frbx = new FormatReceiptByXML(receiptMode,
                                              nrRcptFormatPath);
                receiptsList.add(frbx.getReceipt());
            } else {
                rf = new ReceiptNormalFormatter(receiptMode,
                                                CREDITSLIP_MAXLINES, 0);
                receiptsList.add(rf.getReceipt());
            }
        }
        return receiptsList;
    }

    /**
     * WebService for print Credit Slip.
     *
     * @param txid          The Transaction ID
     * @param deviceNo      The Device Number
     * @param storeNo       The Store Number
     * @param operatorNo    The Operator Number
     * @param language      The Language for the Credit Slip
     * @param businessDate  The Business Date
     * @param poslogxml     The poslog
     * @param trainingFlag  The Training Flag
     * @return result of the operation
     */
    @Path("/printcreditslip/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public final ResultBase printCreditSlip(
            @PathParam("txid") final String txid,
            @FormParam("storeid") final String storeNo,
            @FormParam("deviceid") final String deviceNo,
            @FormParam("operatorno") final String operatorNo,
            @FormParam("language") final String language,
            @FormParam("businessdate") final String businessDate,
            @FormParam("poslog") final String poslogxml,
            @FormParam("trainingflag") final int trainingFlag) {

        String functionName = DebugLogger.getCurrentMethodName();
        boolean isPoslogPara = false;
        String infoMsg = "";
        if (!StringUtility.isNullOrEmpty(poslogxml)) {
            isPoslogPara = true;
        }

        // Output the parameters debug log.
        if (isPoslogPara) {
            tp.methodEnter(functionName)
                .println("Get poslog xml from form parameter.")
                .println("poslog", poslogxml);
        } else {
            tp.methodEnter(functionName).println("txid", txid)
                .println("deviceNo", deviceNo).println("storeNo", storeNo)
                .println("operatorNo", operatorNo)
                .println("language", language)
                .println("businessdate", businessDate)
                .println("trainingflag", trainingFlag);

            infoMsg = "txid         : " + txid + "\n"
                    + "storeid      : " + storeNo + "\n"
                    + "deviceid     : " + deviceNo + "\n"
                    + "operatorno   : " + operatorNo + "\n"
                    + "language     : " + language + "\n"
                    + "businessdate : " + businessDate + "\n"
                    + "trainingflag : " + trainingFlag + "\n";
        }

        ResultBase resultBase = new ResultBase();

        try {
            PosLog poslog = getPosLog(txid, storeNo, deviceNo, trainingFlag, businessDate, poslogxml);

            if (!POSLogHandler.isValid(poslog)) {
                tp.println("Poslog is not valid.");
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                if (isPoslogPara) {
                    Snap.SnapInfo info = snap.write(
                            "Poslog from paramter is not valid.", poslogxml);
                    LOGGER.logSnap(PROG_NAME, functionName,
                            "Poslog from paramter is not valid.", info);
                } else {
                    LOGGER.logAlert(PROG_NAME,
                            functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "Poslog from DB is not valid.\n" + infoMsg);
                }
                return resultBase;
            }
            Transaction tran = poslog.getTransaction();
            // get the data of paper receipt
            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            // get net printer information
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              null,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }

            ReceiptMode receiptMode = this.resolvePoslog(poslog);
            setLanguage(language, receiptMode);

            List<List<byte[]>> receiptsList = new ArrayList<List<byte[]>>();
            // Format receiptmode by XML or java source code.
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            String vdRcptFormatPath = (String) env.lookup("creditSlipRcptFormatPath");
            if (!StringUtility.isNullOrEmpty(vdRcptFormatPath)
                    && (new File(vdRcptFormatPath)).exists()) {
                FormatReceiptByXML frbx = new FormatReceiptByXML(receiptMode,
                        vdRcptFormatPath);
                receiptsList.add(frbx.getReceipt());
            } else {
                ReceiptNormalFormatter rf = new ReceiptNormalFormatter(receiptMode,
                                                   CREDITSLIP_MAXLINES, 1);
                receiptsList.add(rf.getReceipt());
            }

            // call print service
            int printResult;
            String urlCompare = netPrinterInfo.getUrl();
            if(IP_ADDR.equals(urlCompare)){
                printResult =  PrintReceiptToImg.printImage(receiptsList,receiptMode,deviceNo,1);
            }else{
                // get documentary tax stamp path
                String docTaxRange1 = GlobalConstant.getRange1();
                int docTaxRange1Begin = Integer.valueOf(docTaxRange1.split(",")[0]);
                double cashTotal = receiptMode.getCashPament();
                double cashChange = receiptMode.getTenderChange();
                double tax = receiptMode.getTaxAmount();
                String docTaxStampPath = null;
                boolean haveDocTax = false;
                if (cashTotal - cashChange - tax >= docTaxRange1Begin) {
                    docTaxStampPath = iReceiptDAO.getDocTaxStampPath(tran.getRetailStoreID());
                    haveDocTax = true;
                }
                String logopath = iReceiptDAO.getLogoFilePath(storeNo);

                PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                    netPrinterInfo, logopath, docTaxStampPath, haveDocTax,
                    tran.getRetailStoreID(), tran.getWorkStationID().getValue(),
                    tran.getSequenceNo());
                printResult = receiptPrint.printAllReceipt(receiptsList);
            }
            if (printResult != ResultBase.RESNETRECPT_OK) {
                resultBase.setNCRWSSResultCode(printResult);
                tp.println("Failed to Print Credit Slip.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                tp.println("Print credit slip.");
            }
        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[]{
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO,
                    "printCreditSlip", "Output error data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_DB, e);
        } catch (UnsupportedEncodingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[]{
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_ENCODING,
                    "printCreditSlip", "Output error data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (IOException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_IO, "printCreditSlip",
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_IOEXCEPTION, e);
        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, "printCreditSlip",
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    "printCreditSlip", "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RESNETRECPT_ERROR_NG,
                    ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    /**
     * WebService for print return paper receipt.
     *
     * @param txid                  The Transaction ID
     * @param deviceNo              The Device Number
     * @param storeNo               The Store Number
     * @param operatorNo            The Operator Number
     * @param language              The Language for the Credit Slip
     * @param businessDate          The Business Date
     * @param retryflag             The retry print flag
     * @param poslog                The poslog
     * @param receiptForCust        The receipt for customer
     * @param receiptForShop        The receipt for shop
     * @param receipttwoforshop     The second receipt for shop
     * @param receiptForCardCompany The receipt for card company
     * @param printerid             The printer id
     * @param trainingFlag          The Training flag
     * @return result of the operation
     */
    @Path("/printreturnpaperreceipt/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="返品印刷", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメーター"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="データ符号化支を持されていない"),
    })
    public final ResultBase printReturnPaperReceipt(
    		@ApiParam(name="txid", value="取引番号") @PathParam("txid") final String txid,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeNo,
    		@ApiParam(name="deviceid", value="装置コード") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="language", value="言語") @FormParam("language") final String language,
    		@ApiParam(name="businessdate", value="営業日") @FormParam("businessdate") final String businessDate,
    		@ApiParam(name="retryflag", value="リプリントフラグ") @FormParam("retryflag") final String retryflag,
    		@ApiParam(name="poslog", value="poslog情報") @FormParam("poslog") final String poslogxml,
    		@ApiParam(name="receiptforcust", value="顧客用領収書") @FormParam("receiptforcust") final String receiptForCust,
    		@ApiParam(name="receiptforshop", value="ショップ用領収書") @FormParam("receiptforshop") final String receiptForShop,
    		@ApiParam(name="receipttwoforshop", value="セコンド店舗領収書") @FormParam("receipttwoforshop") final String receipttwoforshop,
    		@ApiParam(name="receiptforcardcompany", value="カード発行会社領収書") @FormParam("receiptforcardcompany") final String receiptForCardCompany,
    		@ApiParam(name="printerid", value="プリンターID") @FormParam("printerid") final String printerid,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ")@FormParam("trainingflag") final int trainingFlag) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("storeid", storeNo).println("deviceid", deviceNo)
                .println("operatorno", operatorNo)
                .println("language", language)
                .println("businessdate", businessDate)
                .println("retryflag", retryflag).println("poslog", poslogxml)
                .println("receiptforcust", receiptForCust)
                .println("receiptforshop", receiptForShop)
                .println("receiptforcardcompany", receiptForCardCompany)
                .println("printerid", printerid)
                .println("trainingflag", trainingFlag);

        ResultBase resultBase = new ResultBase();

        try {
            PosLog poslog = getPosLog(txid, storeNo, deviceNo, trainingFlag, businessDate, poslogxml);

            if (!POSLogHandler.isValid(poslog)) {
                String errorMessage = "Poslog is not valid.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_INVALIDPARAMETER,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }

            // get the data of paper receipt
            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              printerid,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }

            ReceiptMode receiptMode = this.resolvePoslog(poslog);
            setLanguage(language, receiptMode);
            receiptMode.setRetryflag(StringUtility.isNullOrEmpty(retryflag) ?
                    retryflag :retryflag.toLowerCase());

            ReturnReceiptResult ret = returnReceipt(poslog,
                                                    iReceiptDAO,
                                                    receiptMode,
                                                    receiptForCust,
                                                    receiptForCardCompany,
                                                    receiptForShop,
                                                    receipttwoforshop,
                                                    trainingFlag);
            List<List<byte[]>> receiptsList = ret.list;

            // call print service
            int printResult;
            String urlCompare = netPrinterInfo.getUrl();
            Transaction tran = poslog.getTransaction();
            if(IP_ADDR.equals(urlCompare)){
                printResult =  PrintReceiptToImg.printImage(receiptsList,receiptMode,deviceNo, ret.position);
            } else {
                // No Documentary Tax for Return Transaction
                String docTaxStampPath = null;
                boolean haveDocTax = false;

                // get documentary tax stamp path
                String logopath = iReceiptDAO.getLogoFilePath(storeNo);

                PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                        netPrinterInfo, logopath, docTaxStampPath, haveDocTax,
                        tran.getRetailStoreID(), tran.getWorkStationID().getValue(),
                        tran.getSequenceNo());
                printResult = receiptPrint.printAllReceipt(receiptsList);
            }
            if (printResult != ResultBase.RESNETRECPT_OK) {
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setMessage("Failed to Print Return Paper Receipt.");
                tp.println("Failed to Print Return Paper Receipt.").println(
                        "result", printResult);
                // logger
                return resultBase;
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setMessage("Print Return Paper Receipt.");
                tp.println("Print Return Paper Receipt.");
            }

        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
                    ResultBase.RES_ERROR_DB, e);
        } catch (UnsupportedEncodingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_ENCODING, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_UNSUPPORTEDENCODING,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (IOException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_IO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
                    ResultBase.RES_ERROR_IOEXCEPTION, e);
        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    public static class ReturnReceiptResult {
        ReturnReceiptResult(int pos, List<List<byte[]>> lis) {
            position = pos;
            list = lis;
        }
        public final List<List<byte[]>> list;
        public final int position;
    }

    public ReturnReceiptResult returnReceipt(PosLog poslog,
                                             IReceiptDAO iReceiptDAO,
                                             ReceiptMode receiptMode,
                                             String receiptForCust,
                                             String receiptForCardCompany,
                                             String receiptForShop,
                                             String receipttwoforshop,
                                             int trainingFlag
                                             ) throws NamingException,
                                                      JAXBException,
                                                      DaoException,
                                                      UnsupportedEncodingException,
                                                      ParseException,
                                                      IllegalAccessException,
                                                      InvocationTargetException,
                                                      NoSuchMethodException {
        // credit payment judgement
        TransactionLink origTran =
            poslog.getTransaction().getRetailTransaction().getTransactionLink();

        if(origTran != null){
            receiptMode.setReturnedType(TransactionVariable.TRANSACTION_STATUS_RETURNEDLINK);
            if(receiptMode.getCreditPayment() > 0){
                PosLog origPosLog = getPosLog(origTran.getSequenceNo(),
                                              origTran.getRetailStoreID(),
                                              origTran.getWorkStationID().getValue(),
                                              trainingFlag,
                                              origTran.getBusinessDayDate(), "");
                if(origPosLog == null){
                    throw new NoDataFoundException(
                               createErrorMessage("original PosLog not find in the database.", origTran));
                }
                List<LineItem> lineItem = origPosLog.getTransaction()
                    .getRetailTransaction()
                    .getLineItems();
                String refno = "";
                for (LineItem lineitem : lineItem) {
                    if (lineitem.getTender() != null) {
                        if( null !=lineitem.getTender().getAuthorization().getReferenceNumber()){
                            refno =lineitem.getTender()
                                .getAuthorization().getReferenceNumber();
                        }
                        if (null != lineitem.getTender().getCreditDebit().getPaymentMethod()) {
                            receiptMode.setPaymentMethodCode(
                                                             lineitem.getTender().getCreditDebit()
                                                             .getPaymentMethod().getPaymentMethodCode());
                        }
                        if( null != lineitem.getTender().getVoucher()){
                            receiptMode.setOrislipNo((lineitem.getTender().getVoucher()).get(0).getVoucherID());
                        }
                    }
                }
                if (!refno.isEmpty()) {
                    receiptMode.setOriginalReferenceNo(refno);
                }
            }
        }else{
            receiptMode.setReturnedType(TransactionVariable.TRANSACTION_STATUS_RETURNED);
        }

        ReceiptReturnFormatter rf;
        FormatReceiptByXML frbx;

        int[] types = new int[4];
        if (!StringUtility.isNullOrEmpty(receiptForCust)
            && ResultBase.TRUE.equalsIgnoreCase(receiptForCust)) {
            types[0] = 1;
        }
        if (!StringUtility.isNullOrEmpty(receiptForCardCompany)
            && ResultBase.TRUE.equalsIgnoreCase(receiptForCardCompany)
            	&& receiptMode.getCreditPayment() > 0) {
            types[1] = 2;
        }
        if (receiptMode.getExemptTaxAmount() > 0 || 
        	(!StringUtility.isNullOrEmpty(receiptForShop)
                    && ResultBase.TRUE.equalsIgnoreCase(receiptForShop))) {
            types[2] = 3;
        }
        if (receiptMode.getExemptTaxAmount() > 0 && 
        		receiptMode.getCreditPayment() > 0 && 
        		!StringUtility.isNullOrEmpty(receipttwoforshop) && 
				ResultBase.TRUE.equalsIgnoreCase(receipttwoforshop)){
        	types[3] = 4;
        }
        
        List<List<byte[]>> receiptsList = new ArrayList<List<byte[]>>();

        // Format receiptmode by XML or java source code.
        Context env = (Context) new InitialContext().lookup("java:comp/env");
        String vdRcptFormatPath = "";
        int position = 0;
        for (int i : types) {
            if (i != 0) {

                switch (i) {
                case 1:
                    vdRcptFormatPath = (String) env
                        .lookup("rnRcptForCustFormatPath");
                    break;
                case 2:
                    vdRcptFormatPath = (String) env
                        .lookup("rnRcptForCardCompanyFormatPath");
                    break;
                case 3:
                    vdRcptFormatPath = (String) env
                        .lookup("rnRcptForShopFormatPath");
                    break;
                case 4:
                	vdRcptFormatPath = (String)env
                        .lookup("rnRcptForShopFormatPath2");
                default:
                    break;
                }

                if (!StringUtility.isNullOrEmpty(vdRcptFormatPath)
                    && (new File(vdRcptFormatPath)).exists()) {

                    frbx = new FormatReceiptByXML(receiptMode,
                                                  vdRcptFormatPath);
                    receiptsList.add(frbx.getReceipt());
                } else {
                    rf = new ReceiptReturnFormatter(receiptMode,
                                                    CREDITSLIP_MAXLINES, i);
                    receiptsList.add(rf.getReceipt());
                }
                if  ( i == 1)  {
                    if (!StringUtility.isNullOrEmpty(receiptMode.getRetryflag())
                        && ("true".equalsIgnoreCase(receiptMode.getRetryflag()))) {
                        position |= 4;
                    } else {
                        position |= 8;
                    }
                } else if (i == 3) {
                    position |= 2;
                } else if (i == 2){
                    position |= 1;
                }
            }
        }
        return new ReturnReceiptResult(position, receiptsList);
    }

    /**
     * WebService for print void receipt.
     *
     * @param txid          The Transaction ID
     * @param deviceNo      The Device Number
     * @param storeNo       The Store Number
     * @param operatorNo    The Operator Number
     * @param language      The Language for the Credit Slip
     * @param businessDate  The Business Date
     * @param poslogxml     The poslog
     * @param retryflag     The retry print flag
     * @param printerid     The printer ID
     * @param trainingFlag  The Training Flag
     * @return result of the operation
     */
    @Path("/printvoidpaperreceipt/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="取消領収書印刷", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメーター"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="データ符号化支を持されていない"),
        @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンターポート未検出"),
    })
    public final ResultBase printVoidReceipt(
    		@ApiParam(name="txid", value="取引番号") @PathParam("txid") final String txid,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeNo,
    		@ApiParam(name="deviceid", value="装置コード") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="language", value="言語") @FormParam("language") final String language,
    		@ApiParam(name="businessdate", value="営業日") @FormParam("businessdate") final String businessDate,
    		@ApiParam(name="poslog", value="poslog情報") @FormParam("poslog") final String poslogxml,
    		@ApiParam(name="retryflag", value="リプリントフラグ") @FormParam("retryflag") final String retryflag,
    		@ApiParam(name="printerid", value="プリンターID") @FormParam("printerid") final String printerid,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") final int trainingFlag) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("storeid", storeNo).println("deviceid", deviceNo)
                .println("operatorno", operatorNo)
                .println("language", language)
                .println("businessdate", businessDate)
                .println("poslog", poslogxml)
                .println("retryflag",retryflag)
                .println("printerid", printerid)
                .println("trainingflag", trainingFlag);

        ResultBase resultBase = new ResultBase();

        try {
            PosLog poslog = getPosLog(txid, storeNo, deviceNo, trainingFlag, businessDate, poslogxml);

            if (!POSLogHandler.isValid(poslog)) {
                String errorMessage = "Poslog is not valid.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_INVALIDPARAMETER,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }

            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              printerid,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }

            ReceiptMode receiptMode = this.resolvePoslog(poslog);
            setLanguage(language, receiptMode);
            receiptMode.setRetryflag(StringUtility.isNullOrEmpty(retryflag) ? retryflag
                    : retryflag.toLowerCase());
            List<List<byte[]>> receiptsList = voidReceipt(poslog,
                                                          iReceiptDAO,
                                                          receiptMode,
                                                          trainingFlag);

            // call print service
            int printResult;
            String urlCompare = netPrinterInfo.getUrl();
            Transaction tran = poslog.getTransaction();
            if(IP_ADDR.equals(urlCompare)){
                printResult =  PrintReceiptToImg.printImage(receiptsList,receiptMode,deviceNo,1);
            }else{
                // get documentary tax stamp path
                String docTaxRange1 = GlobalConstant.getRange1();
                int docTaxRange1Begin = Integer.valueOf(docTaxRange1.split(",")[0]);
                double cashTotal = receiptMode.getCashPament();
                double cashChange = receiptMode.getTenderChange();
                double tax = receiptMode.getTaxAmount();

                String docTaxStampPath = null;
                boolean haveDocTax = false;
                if (cashTotal - cashChange - tax >= docTaxRange1Begin) {
                    docTaxStampPath = iReceiptDAO.getDocTaxStampPath(tran.getRetailStoreID());
                    haveDocTax = true;
                }
                String logopath = iReceiptDAO.getLogoFilePath(storeNo);

                PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                        netPrinterInfo, logopath, docTaxStampPath, haveDocTax,
                        tran.getRetailStoreID(), tran.getWorkStationID().getValue(),
                        tran.getSequenceNo());
                printResult = receiptPrint.printAllReceipt(receiptsList);
            }
            if (printResult != ResultBase.RESNETRECPT_OK) {
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setMessage("Failed to Print Void Receipt.");
                tp.println("Failed to Print Void Receipt.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setMessage("Print Void Receipt.");
                tp.println("Print Void Receipt.");
            }
        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
                    ResultBase.RES_ERROR_DB, e);
        } catch (UnsupportedEncodingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_ENCODING, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_UNSUPPORTEDENCODING,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (IOException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_IO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
                    ResultBase.RES_ERROR_IOEXCEPTION, e);
        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    public List<List<byte[]>> voidReceipt(PosLog poslog,
                                          IReceiptDAO iReceiptDAO,
                                          ReceiptMode receiptMode,
                                          int trainingFlag
                                          ) throws
                                              NamingException,
                                              DaoException,
                                              JAXBException,
                                              UnsupportedEncodingException,
                                              ParseException,
                                              IllegalAccessException,
                                              InvocationTargetException,
                                              NoSuchMethodException {
        if("Cancel".equals(receiptMode.getReceiptType())){
            throw new NoDataFoundException("Poslog from paramter is cancel");
        }

        if(receiptMode.getVoidreturnFlag().equals(ResultBase.TRUE)){
            TransactionLink origTran =
                poslog.getTransaction().getRetailTransaction().getTransactionLink();
            PosLog retrnPosLog = getPosLog(origTran.getSequenceNo(),
                                           origTran.getRetailStoreID(),
                                           origTran.getWorkStationID().getValue(),
                                           trainingFlag,
                                           origTran.getBusinessDayDate(), "");
            if(retrnPosLog == null){
                throw new NoDataFoundException(createErrorMessage("void original PosLog not find in the database.", origTran));
            }
            origTran = retrnPosLog.getTransaction().
                getRetailTransaction().getTransactionLink();
            if(origTran != null){
                receiptMode.setReturnedType(TransactionVariable.TRANSACTION_STATUS_RETURNEDLINK);
                if(receiptMode.getCreditPayment() > 0){
                    PosLog origPosLog = getPosLog(origTran.getSequenceNo(),
                                                  origTran.getRetailStoreID(),
                                                  origTran.getWorkStationID().getValue(),
                                                  trainingFlag,
                                                  origTran.getBusinessDayDate(), "");
                    if(origPosLog == null){
                        throw new NoDataFoundException(createErrorMessage("original PosLog not find in the database.", origTran));
                    }
                    List<LineItem> lineItem = origPosLog.getTransaction()
                        .getRetailTransaction()
                        .getLineItems();
                    for (LineItem lineitem : lineItem) {
                        if (lineitem.getTender() != null) {
                            if( null != lineitem.getTender().getVoucher()){
                                receiptMode.setOrislipNo((lineitem.getTender().getVoucher()).get(0).getVoucherID());
                            }
                        }
                    }
                }
            }else{
                receiptMode.setReturnedType(TransactionVariable.TRANSACTION_STATUS_RETURNED);
            }
        }
        if(!StringUtility.isNullOrEmpty(receiptMode.getOriginalOperatorID())){
            String originalClerkName = iReceiptDAO.getOperatorName(
                          receiptMode.getOriginalOperatorID());
            receiptMode.setOriginalClerkName(originalClerkName);
        }

        List<List<byte[]>> receiptsList = new ArrayList<List<byte[]>>();
        Context env = (Context) new InitialContext().lookup("java:comp/env");
        String vdRcptFormatPath = "";
        if(TransactionVariable.ADVANCEVOIDTYPE
           .equalsIgnoreCase(receiptMode.getAdvanceVoidType()) ||
           TransactionVariable.HOLDVOID
           .equalsIgnoreCase(receiptMode.getHoldVoidType()) ||
           TransactionVariable.CUSTOMERORDERVOID
           .equalsIgnoreCase(receiptMode.getCustomrOrderVoidType())){
            vdRcptFormatPath = (String) env.lookup("advanceVoidRcptFormatPath");
        }else{
            vdRcptFormatPath = (String) env.lookup("vdRcptFormatPath");
        }
        if (!StringUtility.isNullOrEmpty(vdRcptFormatPath)
            && (new File(vdRcptFormatPath)).exists()) {
            FormatReceiptByXML frbx = new FormatReceiptByXML(receiptMode,
                                                             vdRcptFormatPath);
            receiptsList.add(frbx.getReceipt());
        } else {
            ReceiptVoidFormatter rf = new ReceiptVoidFormatter(receiptMode,
                                                               CREDITSLIP_MAXLINES);
            receiptsList.add(rf.getReceipt());
        }
        return receiptsList;
    }

    /**
     * WebService for print tender control receipt.
     *
     * @param txid The Transaction ID
     * @param storeNo The Store Number
     * @param deviceNo The Device Number
     * @param operatorNo The Operator Number
     * @param language The Language for the Credit Slip
     * @param businessDate The Business Date
     * @param poslogxml     The POSLog xml of sales transaction to be printed.
     * @param printerid     The Printer ID (output destination of receipt)
     * @param poslogtype    The poslog type for printing non-cash for Balancing and EOD 
     *                      and store receipt for EOD
     * @param trainingFlag  The Training flag
     * 
     * @return result of the operation
     */
    @Path("/printtendercontrolreceipt/{txid}")
    @POST
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    @ApiOperation(value="テンダーコントロール領収書印刷", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="無効なパラメーター"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="IO異常"),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXBエラー"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNSUPPORTEDENCODING, message="データ符号化支を持されていない"),
        @ApiResponse(code=ResultBase.RES_PRINTER_PORT_NOT_FOUND, message="プリンターポート未検出"),
        @ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="指定されたファイル未検出"),
    })
    public final ResultBase printTenderControlReceipt(
    		@ApiParam(name="txid", value="取引番号") @PathParam("txid") final String txid,
    		@ApiParam(name="storeid", value="店舗コード") @FormParam("storeid") final String storeNo,
    		@ApiParam(name="deviceid", value="装置コード") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="operatorno", value="従業員番号") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="language", value="言語") @FormParam("language") final String language,
    		@ApiParam(name="businessdate", value="営業日") @FormParam("businessdate") final String businessDate,
    		@ApiParam(name="poslog", value="poslog情報") @FormParam("poslog") final String poslogxml,
    		@ApiParam(name="printerid", value="プリンターID") @FormParam("printerid") final String printerid,
    		@ApiParam(name="txprinttype", value="取引印刷種類") @FormParam("txprinttype") final String txprinttype,
    		@ApiParam(name="poslogtype", value="poslog情報種類") @FormParam("poslogtype") final String poslogtype,
    		@ApiParam(name="trainingflag", value="トレーニングフラグ") @FormParam("trainingflag") final int trainingFlag){

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid)
                .println("storeid", storeNo).println("deviceid", deviceNo)
                .println("operatorno", operatorNo)
                .println("language", language)
                .println("businessdate", businessDate)
                .println("poslog", poslogxml)
                .println("printerid", printerid)
                .println("txprinttype", txprinttype)
                .println("poslogtype", poslogtype)
                .println("trainingflag", trainingFlag);

        ResultBase resultBase = new ResultBase();

        try {
        	TxPrintTypes txPrintTypes = null;
        	switch(poslogtype) {
        	case "EOD":
        	case "Balancing":
            	if (!StringUtility.isNullOrEmpty(txprinttype)) {
                    JsonMarshaller<TxPrintTypes> txPrnTypesJsonMarshaller = new JsonMarshaller<TxPrintTypes>();
                    txPrintTypes = txPrnTypesJsonMarshaller.unMarshall(txprinttype, TxPrintTypes.class);
                }
        	    break;
        	}

            PosLog poslog = getPosLog(txid, storeNo, deviceNo, trainingFlag, businessDate, poslogxml);

            if (!POSLogHandler.isValid(poslog)) {
                String errorMessage = "Poslog is not valid.";
                tp.println(errorMessage);
                resultBase = new ResultBase(ResultBase.RES_ERROR_INVALIDPARAMETER,
                        ResultBase.RES_ERROR_INVALIDPARAMETER, new Exception(
                                errorMessage));
                Snap.SnapInfo info = snap.write("Poslog", poslogxml);
                LOGGER.logSnap(PROG_NAME, functionName, errorMessage, info);
                return resultBase;
            }

            // get the data of paper receipt
            IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                    DAOFactory.SQLSERVER).getReceiptDAO();
            //check if printer default setting
            NetPrinterInfo netPrinterInfo = createPrinterInfo(iReceiptDAO,
                                                              storeNo,
                                                              deviceNo,
                                                              printerid,
                                                              resultBase);
            if (netPrinterInfo == null) {
                return resultBase;
            }
            
            IStoreDAO iStoreDAO = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getStoreDAO();
            ViewStore viewStore = null;

            String logopath = iReceiptDAO.getLogoFilePath(storeNo),
            	   opeName, opeId, dayPart, storeName, storeTelNo;
            
            viewStore = iStoreDAO.viewStore(storeNo);
            
            Transaction trans = poslog.getTransaction();
            TenderControlTransaction tenderCtrlTrans = trans.getTenderControlTransaction();
            dayPart = tenderCtrlTrans.getDayPart();
            ReceiptMode receiptMode = null;
            
            List<List<byte[]>> receiptsList = new ArrayList<List<byte[]>>();
            // Format receiptmode by XML or java source code.
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            String vdRcptFormatPath = (String) env.lookup("tcRcptFormatPath");
            
            if (StringUtility.isNullOrEmpty(dayPart)) {
            	dayPart = StringUtility.convNullToEmpty(dayPart);
            }
            
            if (dayPart.equals("Balancing") || dayPart.equals("EOD")) {
            	List<ReceiptMode> receiptModeList = this.resolveTenderControlArrayPoslog(poslog, txPrintTypes);
            	FormatReceiptByXML frbx;
            	int i = 0;
            	
            	if (!StringUtility.isNullOrEmpty(vdRcptFormatPath)
                        && (new File(vdRcptFormatPath)).exists()) {
            		
            		for(Iterator<ReceiptMode> iterReceiptMode = receiptModeList.iterator(); iterReceiptMode.hasNext();) {
	            		receiptMode = iterReceiptMode.next();
	            		
	            		setLanguage(language, receiptMode);
	            		
	            		if (!StringUtility.isNullOrEmpty(viewStore)) {
	            			receiptMode.setStoreName(viewStore.getStore().getStoreName());
	            			receiptMode.setTelNo(viewStore.getStore().getTel());
	            		}
	            		receiptModeList.set(i, receiptMode);
	            		
	            		frbx = new FormatReceiptByXML(receiptMode,
	                            vdRcptFormatPath);
	            		receiptsList.add(frbx.getReceipt());
	            		
	            		i++;
	            	}
            	} else {
                    tp.println(" TenderControlReceiptFormat.xml not found.");
                    String errorMessage = "TenderControlReceiptFormat.xml not found.";
                    LOGGER.logError(PROG_NAME, functionName,
                            Logger.RES_EXCEP_FILENOTFOUND, errorMessage);
                    resultBase = new ResultBase(
                            ResultBase.RES_ERROR_FILENOTFOUND,
                            ResultBase.RES_ERROR_FILENOTFOUND, new Exception(
                                    errorMessage));
                }
	            	
            	
            } else {
            	receiptMode = this.resolveTenderControlPoslog(poslog);
                setLanguage(language, receiptMode);
                opeId = trans.getOperatorID().getValue();
                opeName = iReceiptDAO.getOperatorName(opeId);
                receiptMode.setOperatorName(opeName);
                if (!StringUtility.isNullOrEmpty(viewStore)) {
        			receiptMode.setStoreName(viewStore.getStore().getStoreName());
        			receiptMode.setTelNo(viewStore.getStore().getTel());
        		}
                
                if (!StringUtility.isNullOrEmpty(vdRcptFormatPath)
                        && (new File(vdRcptFormatPath)).exists()) {
                    FormatReceiptByXML frbx = new FormatReceiptByXML(receiptMode,
                            vdRcptFormatPath);
                    receiptsList.add(frbx.getReceipt());

                } else {
                    tp.println(" TenderControlReceiptFormat.xml not found.");
                    String errorMessage = "TenderControlReceiptFormat.xml not found.";
                    LOGGER.logError(PROG_NAME, functionName,
                            Logger.RES_EXCEP_FILENOTFOUND, errorMessage);
                    resultBase = new ResultBase(
                            ResultBase.RES_ERROR_FILENOTFOUND,
                            ResultBase.RES_ERROR_FILENOTFOUND, new Exception(
                                    errorMessage));
                }
            }
            
            // call print service
            int printResult;
            Transaction tran = poslog.getTransaction();
            String urlCompare = netPrinterInfo.getUrl();
            if(IP_ADDR.equals(urlCompare)){
                printResult =  PrintReceiptToImg.printImage(receiptsList,receiptMode,deviceNo,1);
            }else{
                PaperReceiptPrint receiptPrint = new PaperReceiptPrint(null,
                        netPrinterInfo, logopath, null, false,
                        tran.getRetailStoreID(), tran.getWorkStationID().getValue(),
                        tran.getSequenceNo());
                printResult = receiptPrint.printAllReceipt(receiptsList);
            }
            if (printResult != ResultBase.RESNETRECPT_OK) {
                resultBase.setNCRWSSResultCode(printResult);
                resultBase.setNCRWSSExtendedResultCode(printResult);
                resultBase.setMessage("Failed to tender control receipt.");
                tp.println("Failed to tender control receipt.").println("result",
                        printResult);
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setNCRWSSExtendedResultCode(ResultBase.RESNETRECPT_OK);
                resultBase.setMessage("Print tender control receipt.");
                tp.println("Print Receipt.");
            }
        } catch (DaoException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
                    ResultBase.RES_ERROR_DB, e);
        } catch (UnsupportedEncodingException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_ENCODING, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_UNSUPPORTEDENCODING,
                    ResultBase.RES_ERROR_UNSUPPORTEDENCODING, e);
        } catch (IOException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_IO, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION,
                    ResultBase.RES_ERROR_IOEXCEPTION, e);
        } catch (JAXBException e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB,
                    ResultBase.RES_ERROR_JAXB, e);
        } catch (Exception e) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
                    snap.write("Poslog xml to print", poslogxml),
                    snap.write("Exception", e) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
                    "Output error data to snap file.", infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    /**
     * Get the POSLog.
     *
     * @param txid          The transaction number
     * @param storeID       The store number
     * @param deviceID      The terminal number
     * @param trainingFlag  The training flag
     * @param summarydateid The summary date
     * @param poslogxml     The poslog
     *
     * @return The POSLog information
     * @throws DaoException The Exception thrown when getting the POSLog xml in
     *             the database fails.
     * @throws JAXBException The exception thrown when the POSLog xml can not be
     *             deserialize as object.
     */
    private PosLog getPosLog(final String txid, final String storeID, final String deviceID,
            final int trainingFlag ,final String summarydateid,
            final String poslogxml) throws DaoException, JAXBException {

        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("txid", txid)
                .println("storeID", storeID)
                .println("deviceID", deviceID)
                .println("trainingFlag", trainingFlag)
                .println("poslog",
                        (poslogxml == null) ? "poslog xml is null"
                                : "poslog xml is not null");

        String transactionData = "";
        if(!StringUtility.isNullOrEmpty(poslogxml)) {
            transactionData = poslogxml;
        } else {
         // Open IPosLogDAO Database and get Transaction
            IReceiptDAO ipldao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                    .getReceiptDAO();
            transactionData = ipldao.getPOSLogTransaction(deviceID, storeID,
                    txid, summarydateid, trainingFlag);
        }

        // Serialize from xml to Transaction
        XmlSerializer<PosLog> transSerializer = new XmlSerializer<PosLog>();
        PosLog poslog = transSerializer.unMarshallXml(transactionData,
                PosLog.class);

        tp.methodExit(poslog);
        return poslog;
    }

    /**
     * resolve PosLog
     * @throws DaoException
     */
    public ReceiptMode resolvePoslog(PosLog poslog) throws DaoException{
    	
    	// init debug
        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                NetworkReceipt.class);
        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("poslog",
                poslog.toString());
        
        // get the store information
        ReceiptMode receipt = null;
        if(StringUtility.isNullOrEmpty((receipt = this.setStoreInfo(poslog)))){
        	return receipt;
        }
        
        // get transaction status
        this.setReceiptTypeInfo(poslog, receipt);
        if(ReceiptMode.RECEIPTTYPE_CANCEL.equalsIgnoreCase(receipt.getReceiptType())){
        	return null;
        }
        
        tp.println("Receipt type: " + receipt.getReceiptType());
        // set transaction information
        if(!StringUtility.isNullOrEmpty(poslog.getTransaction())){
        	this.setTranasctionInfo(poslog.getTransaction(), receipt);
        }
        RetailTransaction cot = poslog.getTransaction()
                .getRetailTransaction();
        // set customer information
        if(!StringUtility.isNullOrEmpty(cot.getCustomer())){
        	this.setCustomerInfo(cot.getCustomer(), receipt);
        }
        if(!StringUtility.isNullOrEmpty(cot.getBarEmployee())){
        	receipt.setBarEmployeeFlag(ResultBase.TRUE);
        }
        // set promotion information
        if(!StringUtility.isNullOrEmpty(cot.getBarLoyaltyReward())){
        	this.setPromotionInfo(cot.getBarLoyaltyReward(), receipt);
        }
        // set transactionLink information
        if(!StringUtility.isNullOrEmpty(cot.getTransactionLink())){
        	this.setTransactinLinkInfo(cot.getTransactionLink(), receipt);
        }
        // set total information
        if(!StringUtility.isNullOrEmpty(cot.getTotal())){
        	this.setTotalInfo(cot.getTotal(), receipt);
        }
        
        List<PriceDerivationResult> pdrList = cot.getPriceDerivationResult();
        if (StringUtility.isNullOrEmpty(pdrList)) {
            pdrList = new ArrayList<PriceDerivationResult>();
        }

        Map<String, ItemMode> itemMap = new HashMap<String, ItemMode>();
        Map<String, List<ItemMode>> mmListMap = new HashMap<String, List<ItemMode>>();
        ItemMode item;
        List<ItemMode> mmItemList;
        for (PriceDerivationResult pdr : pdrList) {

            item = new ItemMode();
            // mix match rule id
            String mmId = pdr.getPriceDerivationRule()
                    .getPriceDerivationRuleID();

            item.setMmID(mmId);

            item.setMmName(pdr.getPriceDerivationRule().getDescription());
            item.setMmAmount(pdr.getAmount().getAmount());
            item.setMmPreviousPrice(pdr.getPreviousPrice());
            item.setMmSellingPrice(pdr.getPreviousPrice()
                    - pdr.getAmount().getAmount());

            itemMap.put(mmId, item);
            mmItemList = new ArrayList<ItemMode>();
            mmListMap.put(mmId, mmItemList);
        }

        List<ItemMode> itemList = new ArrayList<ItemMode>();
        String mmId = "";
        int mmSequence = 0;
        String advanceJudge = "";
        for (LineItem line : poslog.getTransaction()
                .getRetailTransaction().getLineItems()) {
            Sale sale = line.getSale();
            Layaway  layaway = line.getLayaway();
            PreviousLayaway previousLayaway = line.getPreviousLayaway();
            // 前受金利用モードセットする
            PaymentOnAccount payment = line.getPaymentOnAccount();
            RainCheck rainCheck = line.getRainCheck();
            BarPoints barpoint = line.getBarPoints();
            Return  retrn = line.getRetrn();
            Discount discount = line.getDiscount();
            Tender tender = line.getTender();
            List<Tax> tax = line.getTax();
            PostPoint postPt = line.getPostPoint();
            PointTicketIssue ptTicketIssue = line.getPointTicket();
            // for fantamiliar point system
            if (line.getPoints() != null) {
                receipt.setMemberInfo(line.getPoints());
            }
            item = new ItemMode();
            ItemMode mmItem = new ItemMode();
            if (!StringUtility.isNullOrEmpty(sale)) {
            	item.setInventoryReservationID(sale.getInventoryReservationID());
                if("true".equalsIgnoreCase(Integer.toString(sale.getTaxType()))){
                    item.setShowTaxKanji("nai");
                }else{
                    item.setShowTaxKanji("hi");
                }
                //B対象
                if(!StringUtility.isNullOrEmpty(sale.getPoints())){
                    item.setItemPoints(StringUtility.convNullToDoubleZero(
                            sale.getPoints().getCorrectionPoints()));
                }
                //品番
                item.setItemNote(sale.getNote());
                item.setItemID(sale.getItemID().getPluCode());
                item.setItemName(sale.getDescription());
                item.setQuantity(sale.getQuantity());
                item.setRegularSalesUnitPrice(sale.getRegularsalesunitprice());
                item.setActualSalesUnitPrice(sale.getActualsalesunitprice());
                item.setExtendedAmount(sale.getExtendedAmt());
                item.setExtendedDiscountAmount(sale.getExtendedDiscountAmount());

                // S mark
                item.setItemMark(ResultBase.toString(
                    TransactionVariable.SALEMARK.equalsIgnoreCase(sale.getBarPSType())
                    && receipt.getStoreType() != 3));

                if (!StringUtility.isNullOrEmpty(sale.getItemSellingRule())) {
                    // get non sales flag
                    item.setNonSalesFlag(sale.getItemSellingRule()
                            .getNonSalesFlag());
                    // get item selling rule
                    item.setDiscountableFlag(sale.getItemSellingRule()
                            .getDiscountableFlag());

                    // count the amount for discount
                    if (!StringUtility
                            .isNullOrEmpty(item.getDiscountableFlag())
                            && ResultBase.FALSE.equals(item.getDiscountableFlag())) {
                        receipt.setSubtotal(receipt.getSubtotal()
                                - (item.getExtendedAmount() - item
                                        .getExtendedDiscountAmount()));
                    }
                }

                if(null != sale.getTax() && sale.getTax().size() > 0){
                    for(Tax taxTag : sale.getTax()){
                        if(!StringUtility.isNullOrEmpty(taxTag.getTaxType())
                                        && "VAT".equals(taxTag.getTaxType())){
                            if(!StringUtility.isNullOrEmpty(taxTag.getTaxExempt())){
                                item.setExemptTaxAmount(Double.parseDouble(taxTag.getTaxExempt().getAmount()));
                            }
                        }
                    }
                }

                if (StringUtility.isNullOrEmpty(sale.getRetailPriceModifier())) {
                    itemList.add(item);
                    continue;
                }

                for (RetailPriceModifier modifier : sale
                        .getRetailPriceModifier()) {
//                    if(!StringUtility.isNullOrEmpty(modifier.getPriceDerivationRule())){
//                        if("9".equals(modifier.getPriceDerivationRule()
//                                .getPriceDerivationRuleID().trim())){
//                            item.setEmployeeFlag(ResultBase.TRUE);
//                        }
//                    	item.setPriceDerivationRuleID(modifier.getPriceDerivationRule()
//                    			.getPriceDerivationRuleID());
//                    }
                    if ("PriceOverride".equals(modifier.getMethodCode())) {
                        double price = item.getActualSalesUnitPrice();
                        double overrideAmount ;
                        if(item.getQuantity() == 0){
                            overrideAmount = 0;
                        }else{
                            overrideAmount =
                                modifier.getAmount().getAmount() /
                                item.getQuantity();}
                        if ("Add".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price + overrideAmount);
                            item.setExtendedAmount(
                                   (price + overrideAmount) * item.getQuantity());
                        } else if ("Subtract".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price - overrideAmount);
                            item.setExtendedAmount(
                                 (price - overrideAmount) * item.getQuantity());
                        }

                    } else if ("Item".equals(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        // get amount
                        item.setDiscountAmount(modifier.getAmount().getAmount());
                        // get percent
                        if (!StringUtility.isNullOrEmpty(modifier.getPercent())) {
                            item.setPercent(Double.valueOf(modifier.getPercent().getPercent()));
                        }
                        // get reason code
                        item.setReasonCode(modifier.getPriceDerivationRule()
                                .getReasonCode());
                        item.setPriceItemDerivationRuleID(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID());

                    } else if ("mixmatch".equalsIgnoreCase(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        if (!mmId.equals(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID())) {
                            mmSequence = 0;
                        }

                        mmId = modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID();

                        mmItem = itemMap.get(mmId);

                        // set mix match sequence number
                        item.setMmSequence(++mmSequence);
                        // set mix match quantity
                        item.setMmQuantity(StringUtility.convNullToZero(modifier
                                        .getPriceDerivationRule()
                                        .getMmQuantity()));
                        item.setMmID(mmId);
                        item.setMmName(mmItem.getMmName());
                        item.setMmAmount(mmItem.getMmAmount());
                        item.setMmPreviousPrice(mmItem.getMmPreviousPrice());
                        item.setMmSellingPrice(mmItem.getMmSellingPrice());

                        mmListMap.get(mmId).add(item);

                    }
                }

                if (StringUtility.isNullOrEmpty(item.getMmID())) {
                    itemList.add(item);
                }

            }else if(!StringUtility.isNullOrEmpty(layaway)){
                advanceJudge = TransactionVariable.LAYAWAY;
                item.setInventoryReservationID(layaway.getInventoryReservationID());
                item.setIsAdvancedFlag("True");
                if("true".equalsIgnoreCase(layaway.getTaxableFlag())){
                    item.setShowTaxKanji("nai");
                }else{
                    item.setShowTaxKanji("hi");
                }
                //B対象
                if(!StringUtility.isNullOrEmpty(layaway.getBarPoints())){
                    item.setItemPoints(StringUtility.convNullToDoubleZero(
                            layaway.getBarPoints().getBarCorrectionPoints()));
                }
                //品番
                item.setItemNote(layaway.getNote());
                item.setItemID(layaway.getItemID().getPluCode());
                item.setItemName(layaway.getDescription());
                item.setQuantity(layaway.getQuantity());
                item.setRegularSalesUnitPrice(layaway.getRegularsalesunitprice());
                item.setActualSalesUnitPrice(layaway.getActualsalesunitprice());
                item.setExtendedAmount(layaway.getExtendedAmt());
                item.setExtendedDiscountAmount(layaway.getExtendedDiscountAmount());

                // S mark
                item.setItemMark(ResultBase.toString(
                    TransactionVariable.SALEMARK.equalsIgnoreCase(layaway.getBarPSType())
                    && receipt.getStoreType() != 3));

                if (!StringUtility.isNullOrEmpty(layaway.getItemSellingRule())) {
                    item.setNonSalesFlag(layaway.getItemSellingRule()
                            .getNonSalesFlag());
                    item.setDiscountableFlag(layaway.getItemSellingRule()
                            .getDiscountableFlag());

                    if (!StringUtility
                            .isNullOrEmpty(item.getDiscountableFlag())
                            && ResultBase.FALSE.equals(item.getDiscountableFlag())) {
                        receipt.setSubtotal(receipt.getSubtotal()
                                - (item.getExtendedAmount() - item
                                        .getExtendedDiscountAmount()));
                    }
                }

                if (StringUtility.isNullOrEmpty(layaway.getRetailPriceModifier())) {
                    itemList.add(item);
                    continue;
                }

                for (RetailPriceModifier modifier : layaway
                        .getRetailPriceModifier()) {
//                    if(!StringUtility.isNullOrEmpty(modifier.getPriceDerivationRule())){
//                        if("9".equals(modifier.getPriceDerivationRule()
//                                .getPriceDerivationRuleID().trim())){
//                            item.setEmployeeFlag(ResultBase.TRUE);
//                        }
//                    	item.setPriceDerivationRuleID(modifier.getPriceDerivationRule()
//                    			.getPriceDerivationRuleID());
//                    }
                    if ("PriceOverride".equals(modifier.getMethodCode())) {
                        double price = item.getActualSalesUnitPrice();
                        double overrideAmount ;
                        if(item.getQuantity() == 0){
                            overrideAmount = 0;
                        }else{
                            overrideAmount =
                                modifier.getAmount().getAmount() /
                                item.getQuantity();}
                        if ("Add".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price + overrideAmount);
                            item.setExtendedAmount(
                                   (price + overrideAmount) * item.getQuantity());
                        } else if ("Subtract".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price - overrideAmount);
                            item.setExtendedAmount(
                                 (price - overrideAmount) * item.getQuantity());
                        }

                    } else if ("Item".equals(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        item.setDiscountAmount(modifier.getAmount().getAmount());
                        if (!StringUtility.isNullOrEmpty(modifier.getPercent())) {
                            item.setPercent(Double.valueOf(modifier.getPercent().getPercent()));
                        }
                        item.setReasonCode(modifier.getPriceDerivationRule()
                                .getReasonCode());
                        item.setPriceItemDerivationRuleID(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID());

                    } else if ("mixmatch".equalsIgnoreCase(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        if (!mmId.equals(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID())) {
                            mmSequence = 0;
                        }

                        mmId = modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID();

                        mmItem = itemMap.get(mmId);

                        item.setMmSequence(++mmSequence);
                        item.setMmQuantity(StringUtility.convNullToZero(modifier
                                        .getPriceDerivationRule()
                                        .getMmQuantity()));
                        item.setMmID(mmId);
                        item.setMmName(mmItem.getMmName());
                        item.setMmAmount(mmItem.getMmAmount());
                        item.setMmPreviousPrice(mmItem.getMmPreviousPrice());
                        item.setMmSellingPrice(mmItem.getMmSellingPrice());

                        mmListMap.get(mmId).add(item);

                    }
                }

                if (StringUtility.isNullOrEmpty(item.getMmID())) {
                    itemList.add(item);
                }
            }else if(!StringUtility.isNullOrEmpty(previousLayaway)){
                advanceJudge = TransactionVariable.PREVIOUSLAYAWAY;
                item.setInventoryReservationID(previousLayaway.getInventoryReservationID());
                if("true".equalsIgnoreCase(previousLayaway.getTaxableFlag())){
                    item.setShowTaxKanji("nai");
                }else{
                    item.setShowTaxKanji("hi");
                }
                //B対象
                if(!StringUtility.isNullOrEmpty(previousLayaway.getBarPoints())){
                    item.setItemPoints(StringUtility.convNullToDoubleZero(
                            previousLayaway.getBarPoints().getBarCorrectionPoints()));
                }
                //品番
                item.setItemNote(previousLayaway.getNote());
                item.setItemID(previousLayaway.getItemID().getPluCode());
                item.setItemName(previousLayaway.getDescription());
                item.setQuantity(previousLayaway.getQuantity());
                item.setRegularSalesUnitPrice(previousLayaway.getRegularsalesunitprice());
                item.setActualSalesUnitPrice(previousLayaway.getActualsalesunitprice());
                item.setExtendedAmount(previousLayaway.getExtendedAmt());
                item.setExtendedDiscountAmount(previousLayaway.getExtendedDiscountAmount());

                // S mark
                item.setItemMark(ResultBase.toString(
                         TransactionVariable.SALEMARK.equalsIgnoreCase(
                                           previousLayaway.getBarPSType())
                         && receipt.getStoreType() != 3));

                if (!StringUtility.isNullOrEmpty(previousLayaway.getItemSellingRule())) {
                    // get non sales flag
                    item.setNonSalesFlag(previousLayaway.getItemSellingRule()
                            .getNonSalesFlag());
                    item.setDiscountableFlag(previousLayaway.getItemSellingRule()
                            .getDiscountableFlag());

                    if (!StringUtility
                            .isNullOrEmpty(item.getDiscountableFlag())
                            && ResultBase.FALSE.equals(item.getDiscountableFlag())) {
                        receipt.setSubtotal(receipt.getSubtotal()
                                - (item.getExtendedAmount() - item
                                        .getExtendedDiscountAmount()));
                    }
                }

                if(null != previousLayaway.getTax() && previousLayaway.getTax().size() > 0){
                    for(Tax taxTag: previousLayaway.getTax()){
                        if(!StringUtility.isNullOrEmpty(taxTag.getTaxType())
                                        && "VAT".equals(taxTag.getTaxType())){
                            if(!StringUtility.isNullOrEmpty(taxTag.getTaxExempt())){
                                item.setExemptTaxAmount(Double.parseDouble(taxTag.getTaxExempt().getAmount()));
                            }
                        }
                    }
                }

                if (StringUtility.isNullOrEmpty(previousLayaway.getRetailPriceModifier())) {
                    itemList.add(item);
                    continue;
                }

                for (RetailPriceModifier modifier : previousLayaway
                        .getRetailPriceModifier()) {
//                    if(!StringUtility.isNullOrEmpty(modifier.getPriceDerivationRule())){
//                        if("9".equals(modifier.getPriceDerivationRule()
//                                .getPriceDerivationRuleID().trim())){
//                            item.setEmployeeFlag(ResultBase.TRUE);
//                        }
//                    	item.setPriceDerivationRuleID(modifier.getPriceDerivationRule()
//                    			.getPriceDerivationRuleID());
//                    }
                    if ("PriceOverride".equals(modifier.getMethodCode())) {
                        double price = item.getActualSalesUnitPrice();
                        double overrideAmount ;
                        if(item.getQuantity() == 0){
                            overrideAmount = 0;
                        }else{
                            overrideAmount =
                                modifier.getAmount().getAmount() /
                                item.getQuantity();}
                        if ("Add".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price + overrideAmount);
                            item.setExtendedAmount(
                                   (price + overrideAmount) * item.getQuantity());
                        } else if ("Subtract".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price - overrideAmount);
                            item.setExtendedAmount(
                                 (price - overrideAmount) * item.getQuantity());
                        }

                    } else if ("Item".equals(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        item.setDiscountAmount(modifier.getAmount().getAmount());
                        if (!StringUtility.isNullOrEmpty(modifier.getPercent())) {
                            item.setPercent(Double.valueOf(modifier.getPercent().getPercent()));
                        }
                        item.setReasonCode(modifier.getPriceDerivationRule()
                                .getReasonCode());
                        item.setPriceItemDerivationRuleID(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID());

                    } else if ("mixmatch".equalsIgnoreCase(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        if (!mmId.equals(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID())) {
                            mmSequence = 0;
                        }

                        mmId = modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID();

                        mmItem = itemMap.get(mmId);

                        item.setMmSequence(++mmSequence);
                        item.setMmQuantity(StringUtility.convNullToZero(modifier
                                        .getPriceDerivationRule()
                                        .getMmQuantity()));
                        item.setMmID(mmId);
                        item.setMmName(mmItem.getMmName());
                        item.setMmAmount(mmItem.getMmAmount());
                        item.setMmPreviousPrice(mmItem.getMmPreviousPrice());
                        item.setMmSellingPrice(mmItem.getMmSellingPrice());

                        mmListMap.get(mmId).add(item);

                    }
                }

                if (StringUtility.isNullOrEmpty(item.getMmID())) {
                    itemList.add(item);
                }
            }else if(!StringUtility.isNullOrEmpty(retrn)){
                if("Voided".equals(cot.getTransactionStatus())){
                    item.setVoidreturnFlag(ResultBase.TRUE);
                }
                if("true".equalsIgnoreCase(retrn.getTaxableFlag())){
                    item.setShowTaxKanji("nai");
                }else{
                    item.setShowTaxKanji("hi");
                }
                if(!StringUtility.isNullOrEmpty(retrn.getBarPoints())){
                    item.setItemPoints(StringUtility.convNullToDoubleZero(
                            retrn.getBarPoints().getBarCorrectionPoints()));
                }
                item.setItemNote(retrn.getNote());
                item.setItemID(retrn.getItemID().getPluCode());
                item.setItemName(retrn.getDescription());
                item.setQuantity(retrn.getQuantity());
                item.setRegularSalesUnitPrice(retrn.getRegularsalesunitprice());
                item.setActualSalesUnitPrice(retrn.getActualsalesunitprice());
                item.setExtendedAmount(retrn.getExtendedAmt());
                item.setExtendedDiscountAmount(retrn.getExtendedDiscountAmount());

                // S mark
                item.setItemMark(ResultBase.toString(
                     TransactionVariable.SALEMARK.equalsIgnoreCase(
                                               retrn.getBarPSType())
                     && receipt.getStoreType() != 3));

                if (!StringUtility.isNullOrEmpty(retrn.getItemSellingRule())) {
                    item.setNonSalesFlag(retrn.getItemSellingRule()
                            .getNonSalesFlag());
                    item.setDiscountableFlag(retrn.getItemSellingRule()
                            .getDiscountableFlag());

                    if (!StringUtility
                            .isNullOrEmpty(item.getDiscountableFlag())
                            && ResultBase.FALSE.equals(item.getDiscountableFlag())) {
                        receipt.setSubtotal(receipt.getSubtotal()
                                - (item.getExtendedAmount() - item
                                        .getExtendedDiscountAmount()));
                    }
                }

                if(null != retrn.getTax() && retrn.getTax().size() > 0){
                    for(Tax taxTag:retrn.getTax()){
                        if(!StringUtility.isNullOrEmpty(taxTag.getTaxType())
                                        && "VAT".equals(taxTag.getTaxType())){
                            if(!StringUtility.isNullOrEmpty(taxTag.getTaxExempt())){
                                item.setExemptTaxAmount(Double.parseDouble(taxTag.getTaxExempt().getAmount()));
                            }
                        }
                    }
                }

                if (StringUtility.isNullOrEmpty(retrn.getRetailPriceModifier())) {
                    itemList.add(item);
                    continue;
                }

                for (RetailPriceModifier modifier : retrn
                        .getRetailPriceModifier()) {
//                    if(!StringUtility.isNullOrEmpty(modifier.getPriceDerivationRule())){
//                        if("9".equals(modifier.getPriceDerivationRule()
//                                .getPriceDerivationRuleID().trim())){
//                            item.setEmployeeFlag(ResultBase.TRUE);
//                        }
//                    	item.setPriceDerivationRuleID(modifier.getPriceDerivationRule()
//                    			.getPriceDerivationRuleID());
//                    }
                    if ("PriceOverride".equalsIgnoreCase(modifier.getMethodCode())) {
                        double price = item.getActualSalesUnitPrice();
                        double overrideAmount ;
                        if(item.getQuantity() == 0){
                            overrideAmount = 0;
                        }else{
                            overrideAmount =
                                modifier.getAmount().getAmount() /
                                item.getQuantity();}
                        if ("Add".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price + overrideAmount);
                            item.setExtendedAmount(
                                   (price + overrideAmount) * item.getQuantity());
                        } else if ("Subtract".equals(modifier.getAmount()
                                .getAction())) {
                            item.setActualSalesUnitPrice(
                                    price - overrideAmount);
                            item.setExtendedAmount(
                                 (price - overrideAmount) * item.getQuantity());
                        }

                    } else if ("Item".equals(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        item.setDiscountAmount(modifier.getAmount().getAmount());
                        item.setItemMark(ResultBase.toString(
                             "S".equalsIgnoreCase(retrn.getBarPSType()) &&
                                 receipt.getStoreType() != 3));
                        if (!StringUtility.isNullOrEmpty(modifier.getPercent())) {
                            item.setPercent(Double.valueOf(modifier.getPercent().getPercent()));
                        }
                        item.setReasonCode(modifier.getPriceDerivationRule()
                                .getReasonCode());
                        item.setPriceItemDerivationRuleID(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID());

                    } else if ("mixmatch".equalsIgnoreCase(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        if (!mmId.equals(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID())) {
                            mmSequence = 0;
                        }

                        mmId = modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID();

                        mmItem = itemMap.get(mmId);

                        item.setMmSequence(++mmSequence);
                        item.setMmQuantity(StringUtility.convNullToZero(modifier
                                        .getPriceDerivationRule()
                                        .getMmQuantity()));
                        item.setMmID(mmId);
                        item.setMmName(mmItem.getMmName());
                        item.setMmAmount(mmItem.getMmAmount());
                        item.setMmPreviousPrice(mmItem.getMmPreviousPrice());
                        item.setMmSellingPrice(mmItem.getMmSellingPrice());

                        mmListMap.get(mmId).add(item);

                    }
                }

                if (StringUtility.isNullOrEmpty(item.getMmID())) {
                    itemList.add(item);
                }
            }else if(!StringUtility.isNullOrEmpty(rainCheck)){
            	advanceJudge = TransactionVariable.RAINCHECK;
            	item.setInventoryReservationID(rainCheck.getInventoryReservationID());
            	if(!StringUtility.isNullOrEmpty(rainCheck.getBarReservationType())){
            		item.setBarReservationStatus(rainCheck.getBarReservationType().getBarReservationStatus());
            		item.setBarReservationTypeReasonCode(rainCheck.getBarReservationType().getReasonCode());
            	}
            	// raincheck tag exist
            	if(StringUtility.isNullOrEmpty(receipt.getIsRainCheckFlag())){
            	    receipt.setIsRainCheckFlag(Boolean.TRUE.toString());
            	}
            	// hold Reservation CustomerOrder
            	item.setRainCheckReasonCode(rainCheck.getReasonCode());
                if("true".equalsIgnoreCase(rainCheck.getTaxableFlag())){
                    item.setShowTaxKanji("nai");
                }else{
                    item.setShowTaxKanji("hi");
                }
                //B対象
                if(!StringUtility.isNullOrEmpty(rainCheck.getBarPoints())){
                    item.setItemPoints(StringUtility.convNullToDoubleZero(
                    		rainCheck.getBarPoints().getBarCorrectionPoints()));
                }
                //品番
                item.setItemNote(rainCheck.getNote());
                item.setItemID(rainCheck.getItemID().getPluCode());
                item.setItemName(rainCheck.getDescription());
                item.setQuantity(rainCheck.getQuantity());
                item.setRegularSalesUnitPrice(rainCheck.getRegularsalesunitprice());
                item.setActualSalesUnitPrice(rainCheck.getActualsalesunitprice());
                item.setExtendedAmount(rainCheck.getExtendedAmt());
                item.setExtendedDiscountAmount(rainCheck.getExtendedDiscountAmount());

                // S mark
                item.setItemMark(ResultBase.toString(
                    TransactionVariable.SALEMARK.equalsIgnoreCase(rainCheck.getBarPSType())
                    && receipt.getStoreType() != 3));

                if (!StringUtility.isNullOrEmpty(rainCheck.getItemSellingRule())) {
                    // get non sales flag
                    item.setNonSalesFlag(rainCheck.getItemSellingRule()
                            .getNonSalesFlag());
                    // get item selling rule
                    item.setDiscountableFlag(rainCheck.getItemSellingRule()
                            .getDiscountableFlag());

                    // count the amount for discount
                    if (!StringUtility
                            .isNullOrEmpty(item.getDiscountableFlag())
                            && ResultBase.FALSE.equals(item.getDiscountableFlag())) {
                        receipt.setSubtotal(receipt.getSubtotal()
                                - (item.getExtendedAmount() - item
                                        .getExtendedDiscountAmount()));
                    }
                }

                if(null != rainCheck.getTax() && rainCheck.getTax().size() > 0){
                    for(Tax taxTag : rainCheck.getTax()){
                        if(!StringUtility.isNullOrEmpty(taxTag.getTaxType())
                                        && "VAT".equals(taxTag.getTaxType())){
                            if(!StringUtility.isNullOrEmpty(taxTag.getTaxExempt())){
                                item.setExemptTaxAmount(Double.parseDouble(taxTag.getTaxExempt().getAmount()));
                            }
                        }
                    }
                }

                if (StringUtility.isNullOrEmpty(rainCheck.getRetailPriceModifier())) {
                    itemList.add(item);
                    continue;
                }

                for (RetailPriceModifier modifier : rainCheck
                        .getRetailPriceModifier()) {
//                    if(!StringUtility.isNullOrEmpty(modifier.getPriceDerivationRule())){
//                        if("9".equals(modifier.getPriceDerivationRule()
//                                .getPriceDerivationRuleID().trim())){
//                            item.setEmployeeFlag(ResultBase.TRUE);
//                        }
//                    	item.setPriceDerivationRuleID(modifier.getPriceDerivationRule()
//                    			.getPriceDerivationRuleID());
//                    }
                    if ("PriceOverride".equals(modifier.getMethodCode())) {
                        double price = item.getActualSalesUnitPrice();
                        double overrideAmount ;
                        if(item.getQuantity() == 0){
                            overrideAmount = 0;
                        }else{
                            overrideAmount =
                                modifier.getAmount().getAmount() /
                                item.getQuantity();}
                        if ("Add".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price + overrideAmount);
                            item.setExtendedAmount(
                                   (price + overrideAmount) * item.getQuantity());
                        } else if ("Subtract".equals(modifier.getAmount().getAction())) {
                            item.setActualSalesUnitPrice(
                                    price - overrideAmount);
                            item.setExtendedAmount(
                                 (price - overrideAmount) * item.getQuantity());
                        }

                    } else if ("Item".equals(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        // get amount
                        item.setDiscountAmount(modifier.getAmount().getAmount());
                        // get percent
                        if (!StringUtility.isNullOrEmpty(modifier.getPercent())) {
                            item.setPercent(Double.valueOf(modifier.getPercent().getPercent()));
                        }
                        // get reason code
                        item.setReasonCode(modifier.getPriceDerivationRule()
                                .getReasonCode());
                        item.setPriceItemDerivationRuleID(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID());

                    } else if ("mixmatch".equalsIgnoreCase(modifier.getPriceDerivationRule()
                            .getApplicationType())) {

                        if (!mmId.equals(modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID())) {
                            mmSequence = 0;
                        }

                        mmId = modifier.getPriceDerivationRule()
                                .getPriceDerivationRuleID();

                        mmItem = itemMap.get(mmId);

                        // set mix match sequence number
                        item.setMmSequence(++mmSequence);
                        // set mix match quantity
                        item.setMmQuantity(StringUtility.convNullToZero(modifier
                                        .getPriceDerivationRule()
                                        .getMmQuantity()));
                        item.setMmID(mmId);
                        item.setMmName(mmItem.getMmName());
                        item.setMmAmount(mmItem.getMmAmount());
                        item.setMmPreviousPrice(mmItem.getMmPreviousPrice());
                        item.setMmSellingPrice(mmItem.getMmSellingPrice());

                        mmListMap.get(mmId).add(item);

                    }
                }

                if (StringUtility.isNullOrEmpty(item.getMmID())) {
                    itemList.add(item);
                }
            }else if (!StringUtility.isNullOrEmpty(discount)) {
                receipt.setTotalPercent(StringUtility.convNullToZero(Integer.toString(discount.getPercentage())));
                receipt.setTotalDiscount(StringUtility.convNullToDoubleZero(discount
                        .getAmount()));
                if(!StringUtility.isNullOrEmpty(discount.getPriceDerivationRule())){
                    receipt.setTotalReasonCode(discount.getPriceDerivationRule()
                            .getReasonCode());
                }
            } else if (!StringUtility.isNullOrEmpty(tender)) {
                if (TransactionVariable.CREDITDEBIT.equalsIgnoreCase(
                        tender.getTenderType())) {
                    receipt.setCreditPament(StringUtility.convNullToDoubleZero(tender
                            .getAmount()));
                    receipt.setCardCompany(tender.getCreditDebit()
                            .getIssuerName());
                    receipt.setCreditNo(tender.getCreditDebit()
                            .getAccountNumber());
                    receipt.setSlipNo((tender.getVoucher()).get(0).getVoucherID());
                    receipt.setPaymentMethod(tender.getCreditDebit()
                            .getPaymentMethod().getPaymentMethodName());
                    receipt.setNDivided(tender.getCreditDebit()
                            .getPaymentMethod().getNdivided());
                    receipt.setPaymentMethodCode(tender.getCreditDebit()
                            .getPaymentMethod().getPaymentMethodCode());
                    receipt.setIssueSequence(tender.getCreditDebit()
                            .getIssueSequence());
                    if(!StringUtility.isNullOrEmpty(tender.getAuthorization())){
                        receipt.setApprovalNo(tender.getAuthorization()
                                .getAuthorizationCode());
                        receipt.setCreditTerminalID(tender.getAuthorization()
                                .getRequestingTerminalID());
                        receipt.setReferenceNo(tender.getAuthorization()
                                .getReferenceNumber());
                        receipt.setCreditIssueDateTime(tender.getAuthorization()
                                .getAuthorizationDateTime());
                        receipt.setIcAID(tender.getAuthorization()
                                .getBarApplicationIdentifier());
                        receipt.setIcATC(tender.getAuthorization()
                                .getBarApplicationTransactionCounter());
                        receipt.setUnionPayNo(tender.getAuthorization()
                                .getChinaUnionPayNumber());
                    }
                    receipt.setSignatureFlag("false");
                    if (!StringUtility.isNullOrEmpty(tender.getAuthorization())
                            && !StringUtility.isNullOrEmpty(tender
                                    .getAuthorization()
                                    .getSignatureRequiredFlag())) {

                        receipt.setSignatureFlag(tender.getAuthorization()
                                .getSignatureRequiredFlag());
                    }
                    if(TransactionVariable.ICCREDIT.equalsIgnoreCase(
                            tender.getCreditDebit().getCardType())){
                        receipt.setCreditType(TransactionVariable.ICCREDIT);
                    }else if(TransactionVariable.UNIONPAY.equalsIgnoreCase(
                            tender.getCreditDebit().getCardType())){
                        receipt.setCreditType(TransactionVariable.UNIONPAY);
                    }else{
                        receipt.setCreditType(TransactionVariable.CREDIT);
                    }
                } else if (TransactionVariable.CASH.equalsIgnoreCase(
                        tender.getTenderType())) {
                    receipt.setCashPament(StringUtility.convNullToDoubleZero(tender
                            .getAmount()));
                    if (!StringUtility.isNullOrEmpty(tender.getTenderChange())) {
                        receipt.setTenderChange(receipt.getTenderChange() + 
                                tender.getTenderChange().getAmount());
                    }

                } else if (TransactionVariable.VOUCHER.equalsIgnoreCase(
                        tender.getTenderType())) {
                    if(!StringUtility.isNullOrEmpty(tender.getVoucher())){
                        item = new ItemMode();
                        if("True".equalsIgnoreCase(receipt.getVoidreturnFlag())){
                        	item.setVoidreturnFlag("True");
                        }
                        item.setVoucherAmount(Double.parseDouble(
                                tender.getAmount()));
                        item.setVoucherTypeCode((tender.getVoucher()).get(0)
                                .getTypeCode().toLowerCase());
                        item.setVoucherExpirationDate((tender.getVoucher()).get(0)
                                .getExpirationDate());
                        item.setVoucherFaceValueAmount((tender.getVoucher()).get(0)
                                    .getFaceValueAmount());
                        item.setVoucherSerialNumber((tender.getVoucher()).get(0)
                                .getSerialNumber());
                        item.setVoucherUnspentAmount((tender.getVoucher()).get(0)
                                .getFaceValueAmount() - Double.parseDouble(tender
                                        .getAmount()));
                        if("GiftCard".equalsIgnoreCase((tender.getVoucher()).get(0).getTypeCode())){
                            receipt.setGiftCardsAmount(receipt.getGiftCardsAmount()
                                    + item.getVoucherAmount());
                            receipt.setGiftCardCount(receipt.getGiftCardCount() + 1);
                        }else{
                            receipt.setVouchersAmount(receipt.getVouchersAmount()
                                    + item.getVoucherFaceValueAmount());
                            UnspentAmount unchange = (tender.getVoucher()).get(0).getUnspentAmount();
                            if(null != unchange && "NoChange".equalsIgnoreCase(
                                    unchange.getDisposition())){
                                double unchangerAmount = unchange.getAmount();
                                receipt.setVoucherWithoutChange(receipt.getVoucherWithoutChange()
                                        + unchangerAmount);
                            }
                        }
                        if(tender.getTenderChange() != null && "cash".equalsIgnoreCase(
                                tender.getTenderChange().getTenderType())){
                            receipt.setTenderChange(tender.getTenderChange().getAmount()
                                    + receipt.getTenderChange());
                        }
                        itemList.add(item);
                    }
                    receipt.setMiscPament(StringUtility.convNullToDoubleZero(tender
                            .getAmount()));
                } else if(TransactionVariable.STOREVALUE.equalsIgnoreCase(
                        tender.getTenderType())){
                    receipt.setSuicaAmount(StringUtility.convNullToDoubleZero(tender
                            .getAmount()));
                    StoredValueInstrument storedValueInstrument = null;
                    storedValueInstrument = tender.getStoredValueInstrument();
                    if(tender.getStoredValueInstrument() != null &&
                            "SmartCard".equals(storedValueInstrument.
                                    getTypeCode())){
                        receipt.setSuicaFaceValueAmount(storedValueInstrument.
                                getFaceValueAmount());
                        receipt.setSuicaSerialNumber(storedValueInstrument.
                                getSerialNumber());
                        receipt.setSuicaReferenceNumber(storedValueInstrument.
                                getReferenceNumber());
                        if(storedValueInstrument.getUnspentAmount() != null){
                            receipt.setSuicaUnspentAmount(storedValueInstrument.
                                    getUnspentAmount().getAmount());
                        }else{
                            receipt.setSuicaUnspentAmount(0);
                        }
                    }
                }else if(TransactionVariable.BANKTRANSFER.equalsIgnoreCase(
                        tender.getTenderType())){
                    receipt.setBankTransferAmount(StringUtility.convNullToDoubleZero(
                            tender.getAmount()));
                    if (!StringUtility.isNullOrEmpty(tender.getTenderChange())) {
                        receipt.setTenderChange(receipt.getTenderChange() + 
                                tender.getTenderChange().getAmount());
                    }
                }
            }else if(!StringUtility.isNullOrEmpty(payment)){
            	switch (advanceJudge){
            	case TransactionVariable.LAYAWAY:
            		List<LineItem> layawayList = poslog.getTransaction().
                    getRetailTransaction().getLineItems();
            		if(!StringUtility.isNullOrEmpty(layawayList) && layawayList.size() > 0){
            			receipt.setInventoryReservationID(layawayList.get(0).
                        getLayaway().getInventoryReservationID());
            		}
            		receipt.setPreviousAmount(payment.getPreviousAmount());

            		if(payment.getPreviousAmount() + payment.getAmount() > 0){
            			receipt.setReceiptTitle("normal");
            		}else{
            			receipt.setReceiptTitle("advance");
            		}
            		receipt.setIsAdvanceFlag("true");
            		break;
            	case TransactionVariable.PREVIOUSLAYAWAY:
            		item = new ItemMode();
                    item.setPaymentReservationID(payment.getInventoryReservationID());
                    item.setPaymentCode(payment.getAccountCode());
                    item.setPaymentAmount(payment.getAmount());
                    if(!StringUtility.isNullOrEmpty(payment.getTenderChange())){
                        item.setPaymentChange(payment.getTenderChange().getAmount());
                    }
                    itemList.add(item);
                    break;
            	}
            }else if (!StringUtility.isNullOrEmpty(tax) && null != tax.get(0) 
                    && null !=tax.get(0).getTaxType() 
                    && "VAT".equals(tax.get(0).getTaxType())) {
                receipt.setTaxAmount(StringUtility.convNullToDoubleZero(tax.get(0).getAmount()));
                receipt.setTaxPercent(tax.get(0).getPercent());
                if(!StringUtility.isNullOrEmpty(tax.get(0).getTaxExempt())){
                    receipt.setExemptTaxAmount(Double.parseDouble(tax.get(0).getTaxExempt().getAmount()));
                }
                if(!StringUtility.isNullOrEmpty(tax.get(0).getNote()) && "1".equals(tax.get(0).getNote().trim())){
                	receipt.setOldTaxRate("True");
                }else{
                	receipt.setOldTaxRate("False");
                }
            }else if(!StringUtility.isNullOrEmpty(barpoint)){
                receipt.setCustomerPoints(barpoint.getBarCorrectionPoints());
            }else if (!StringUtility.isNullOrEmpty(tax) && null != tax.get(0)
                    && null != tax.get(0).getTaxType() 
                    && "Documentary".equals(tax.get(0).getTaxType())) {
                receipt.setHaveDocTax("true");
            } else if (!StringUtility.isNullOrEmpty(postPt)) {
            	String txType, txId, basicpts, ptsgentot, ptthistime, company, companyId;
            	String ptcarddiv, ptcardno, prepttotal, cumulativepttotal;
            	String opeId, opeName;
            	int salesamt, amtforpts;
            	
            	IReceiptDAO iReceiptDao = DAOFactory.getDAOFactory(
                        DAOFactory.SQLSERVER).getReceiptDAO();
                opeId = poslog.getTransaction().getOperatorID().getValue();
                opeName = iReceiptDao.getOperatorName(opeId);
                
                receipt.setOperatorName(opeName);
    	        ProcessedTransaction processedTrans = postPt.getProcessedTransacton();
    	        ptcardno = "";
    	        company = poslog.getTransaction().getOrganizationHierarchy().getLevel();
    	        companyId = poslog.getTransaction().getOrganizationHierarchy().getId();
    	        txType = poslog.getTransaction().getTransactionType();
    	        txId = postPt.getTransactionId();
    	        salesamt = processedTrans.getSalesAmount();
    	        amtforpts = processedTrans.getAmountForPoints();
    	        basicpts = "";
    	        ptcarddiv = postPt.getCardClass();
    	        if (!StringUtility.isNullOrEmpty(company) && company.equalsIgnoreCase("Xebio")) {
    	        	basicpts = Integer.toString(processedTrans.getBasicPoints());
    	        	ptsgentot = Integer.toString(processedTrans.getPointsGeneratedTotal());
    	        	if (StringUtility.isNullOrEmpty(ptsgentot)) {
    	        		ptsgentot = " ";
    	        	}
    	        	ptcardno = postPt.getMembershipId16();
    	        } else {
    	        	ptthistime = Integer.toString(processedTrans.getPoints());
    	        	ptsgentot = "";
    	        	ptcardno = postPt.getMembershipId13();
    	        }
    	        prepttotal = postPt.getBeforePointsTotal();
    	        cumulativepttotal = postPt.getCumulativePointsTotal();
    	        
    	        if (txType.contains("Void")) {
    	        	receipt.setReceiptType("Voided");
    	        } else {
    	        	receipt.setReceiptType("Normal");
    	        }
    	        receipt.setCompanyId(companyId);
            	receipt.setTransactionType(txType);
            	receipt.setTransactionId(txId);
            	receipt.setSalesAmount(salesamt);
            	receipt.setAmountForPts(amtforpts);
            	receipt.setBasicPts(basicpts);
            	receipt.setPtsGeneratedTotal(ptsgentot);
            	
            	receipt.setPtCardDiv(ptcarddiv);
            	receipt.setPtCardNo(ptcardno);
            	receipt.setPrePtTotal(prepttotal);
            	receipt.setCumulativePtTotal(cumulativepttotal);
            } else if (!StringUtility.isNullOrEmpty(ptTicketIssue)) {
            	IStoreDAO iStoreDAO = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getStoreDAO();
                ViewStore viewStore = null;
                IReceiptDAO iReceiptDAO = DAOFactory.getDAOFactory(
                        DAOFactory.SQLSERVER).getReceiptDAO();
                String ticketValue, storeId, wkStnId, ticketCntr, ticketIssueNum, storeName, temp,
                       ticketValBC, company;
                
            	ticketValue = ptTicketIssue.getPointsRedeemed();
            	storeId = poslog.getTransaction().getRetailStoreID();
            	wkStnId = poslog.getTransaction().getWorkStationID().getValue();
            	ticketCntr = ptTicketIssue.getTicketCounter();
            	viewStore = iStoreDAO.viewStore(storeId);
            	if (StringUtility.isNullOrEmpty(ticketCntr)) {
            		ticketCntr = "0000";
            	} else {
            		ticketCntr = String.format("%04d", Integer.parseInt(ticketCntr));
            	}
            	
            	if (wkStnId.length()>2) {
            		wkStnId = wkStnId.substring(2, wkStnId.length());
            	}
            	
            	ticketValue = StringUtility.addCommaYenToNumString(ticketValue);
            	temp = PointsConstantVars.pointTicketFtr1a + ticketValue +
            			 PointsConstantVars.pointTicketFtr1b;
            	receipt.setPtTicketFtr1(temp);
            	ticketValue = "P " + ticketValue;
            	ticketIssueNum = storeId + wkStnId + ticketCntr;
            	receipt.setPtTicketFtr2(viewStore.getStore().getStoreName());
            	receipt.setPtTicketValue(ticketValue);
            	receipt.setPtTicketIssueNumber(ticketIssueNum);

            }
        }

        if (!poslog.getTransaction().getTransactionType().equals("PostPoint")) {
	        for (List<ItemMode> list : mmListMap.values()) {
	            list.get(list.size() - 1).setMmSize(list.size());
	            itemList.addAll(list);
	        }
        }
        receipt.setItemList(itemList);

        tp.methodExit(receipt);

        return receipt;
    }
    private List<ReceiptMode> resolveTenderControlArrayPoslog(PosLog poslog, TxPrintTypes txPrintTypes) throws DaoException{

        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("poslog",
                poslog.toString());

        IReceiptDAO iReceiptDao = DAOFactory.getDAOFactory(
                DAOFactory.SQLSERVER).getReceiptDAO();
        ReceiptMode receipt = iReceiptDao.getReceiptInfo(poslog,
                poslog.getTransaction().getRetailStoreID());
        TenderControlTransaction tenderControl = poslog.getTransaction()
                .getTenderControlTransaction();
        List<ReceiptMode> receiptResult = new ArrayList<ReceiptMode>();

        if(StringUtility.isNullOrEmpty(receipt)){
            return null;
        }else if(StringUtility.isNullOrEmpty(tenderControl)){
            return null;
        }
        
        String opeId, opeName;
        opeId = poslog.getTransaction().getOperatorID().getValue();
        opeName = iReceiptDao.getOperatorName(opeId);
        
        receipt.setOperatorID(opeId);
        receipt.setOperatorName(opeName);
        receipt.setTrainModeFlag(poslog.getTransaction().getTrainingModeFlag());
        receipt.setBeginDateTime(poslog.getTransaction().getBeginDateTime());
        receipt.setBusinessDayDate(poslog.getTransaction().getBusinessDayDate());
        receipt.setSequenceNo(poslog.getTransaction().getSequenceNo());
        receipt.setWorkStationID(poslog.getTransaction().getWorkStationID().getValue());
        receipt.setOperatorID(poslog.getTransaction().getOperatorID().getValue());
        receipt.setStoreID(poslog.getTransaction().getRetailStoreID());

        receipt.setReceiptType("TenderControl");
        TillSettle tillSettle = tenderControl.getTillSettle();
        String dayPart = tenderControl.getDayPart();
        
        if (!StringUtility.isNullOrEmpty(dayPart) && dayPart.toLowerCase().equals("balancing")) {
        	CreditDebit creditDebit = tillSettle.getCreditDebit();
        	List<Voucher> voucherList = tillSettle.getVoucher();
        	Tender tender = tillSettle.getTender();
        	CashChanger cashChanger = tender.getDevices().getCashChanger();
        	CashDrawer cashDrawer = tender.getDevices().getCashDrawer();
        	Long totalAmt;
        	String regCnt, calcAmt, calcAmt2, gap, regAmt;
        	
        	List<ItemMode> nonCashAmountList = new ArrayList<ItemMode>();
        	ItemMode item = null;
        	receipt.setTenderControlType(TransactionVariable.BALANCING);
        	receipt.setTenderCtrlBalanceType("NonCash");
        	
        	regCnt = creditDebit.getRegisteredCount();
        	calcAmt = creditDebit.getCurrentCount();
        	calcAmt2 = creditDebit.getCurrentAmount();
        	gap = creditDebit.getDifference();
        	regCnt = FillSpace(regCnt, 9, "right");
        	calcAmt = FillSpace(calcAmt, 9, "right");
        	calcAmt2 = FillSpace(calcAmt2, 13, "right");
        	gap = FillSpace(gap, 9, "right");
        	
        	item = new ItemMode();
        	item.setTenderCtrlType(dayPart);
        	item.setTenderCtrlBalType("NonCash");
        	item.setGroupLabel(CashNonCashConstants.NCCREDIT_LABEL);
        	item.setRealAmount(regCnt);
        	item.setCalculatedAmount(calcAmt);
        	item.setCalculatedAmt2(calcAmt2);
        	item.setGapAmount(gap);
        	nonCashAmountList.add(item);
        	
        	String tenderType;
        	Voucher voucher;
        	Long voucherRegAmt, voucherCurAmt, voucherDiff;
        	Long vouOtherRegAmt, vouOtherCurAmt, vouOtherDiff;
        	voucherRegAmt = Long.parseLong("0");
        	voucherCurAmt = Long.parseLong("0");
        	voucherDiff = Long.parseLong("0");
        	vouOtherRegAmt = Long.parseLong("0");
        	vouOtherCurAmt = Long.parseLong("0");
        	vouOtherDiff = Long.parseLong("0");
        	for(Iterator<Voucher> iterVoucherList=voucherList.iterator(); iterVoucherList.hasNext();) {
        		voucher = iterVoucherList.next();
        		tenderType = voucher.getTenderType();
        		
        		if (tenderType.equals(CashNonCashConstants.VOUCHERCOMMON)) {
        			voucherRegAmt = voucherRegAmt + Long.parseLong(voucher.getRegisteredAmount());
        			voucherCurAmt = voucherCurAmt + Long.parseLong(voucher.getCurrentAmount());
        			voucherDiff = voucherDiff + Long.parseLong(voucher.getDifference());
        		} else if (tenderType.equals(CashNonCashConstants.VOUCHEROTHERS)) {
        			vouOtherRegAmt = vouOtherRegAmt + Long.parseLong(voucher.getRegisteredAmount());
        			vouOtherCurAmt = vouOtherCurAmt + Long.parseLong(voucher.getCurrentAmount());
        			vouOtherDiff = vouOtherDiff + Long.parseLong(voucher.getDifference());
        		} else {
        			
        		}
        	}
        	
        	regAmt = Long.toString(voucherRegAmt);
        	calcAmt = Long.toString(voucherCurAmt);
        	gap = Long.toString(voucherDiff);
        	regAmt = FillSpace(regAmt, 23, "right");
        	regCnt = "0";
        	regCnt = FillSpace(regCnt, 9, "right");
        	calcAmt = FillSpace(calcAmt, 13, "right");
        	gap = FillSpace(gap, 23, "right");
        	
        	item = new ItemMode();
        	item.setTenderCtrlType(dayPart);
        	item.setTenderCtrlBalType("NonCash");
        	item.setGroupTicketLabel(CashNonCashConstants.NCGROUPTICKET_LABEL);
        	item.setGroupLabel(CashNonCashConstants.NCCOMMODITYTICKET_LABEL);
        	item.setRealAmount(regAmt);
        	item.setCalculatedAmount(regCnt);
        	item.setCalculatedAmt2(calcAmt);
        	item.setGapAmount(gap);
        	nonCashAmountList.add(item);
        	
        	regAmt = Long.toString(vouOtherRegAmt);
        	calcAmt = Long.toString(vouOtherCurAmt);
        	gap = Long.toString(vouOtherDiff);
        	regAmt = FillSpace(regAmt, 23, "right");
        	regCnt = "0";
        	regCnt = FillSpace(regCnt, 9, "right");
        	calcAmt = FillSpace(calcAmt, 13, "right");
        	gap = FillSpace(gap, 23, "right");
        	
        	item = new ItemMode();
        	item.setTenderCtrlType(dayPart);
        	item.setTenderCtrlBalType("NonCash");
        	item.setGroupTicketLabel("");
        	item.setGroupLabel(CashNonCashConstants.NCOTHERS_LABEL);
        	item.setRealAmount(regAmt);
        	item.setCalculatedAmount(regCnt);
        	item.setCalculatedAmt2(calcAmt);
        	item.setGapAmount(gap);
        	nonCashAmountList.add(item);
        	
        	receipt.setItemList(nonCashAmountList);
        	receiptResult.add(receipt);
        	
        	receipt = new ReceiptMode();
        	receipt = iReceiptDao.getReceiptInfo(poslog,
                    poslog.getTransaction().getRetailStoreID());
        	receipt.setOperatorID(opeId);
            receipt.setOperatorName(opeName);
            receipt.setTrainModeFlag(poslog.getTransaction().getTrainingModeFlag());
            receipt.setBeginDateTime(poslog.getTransaction().getBeginDateTime());
            receipt.setBusinessDayDate(poslog.getTransaction().getBusinessDayDate());
            receipt.setSequenceNo(poslog.getTransaction().getSequenceNo());
            receipt.setWorkStationID(poslog.getTransaction().getWorkStationID().getValue());
            receipt.setOperatorID(poslog.getTransaction().getOperatorID().getValue());
            receipt.setStoreID(poslog.getTransaction().getRetailStoreID());

            receipt.setReceiptType("TenderControl");
        	
        	List<ItemMode> cashMachineList = new ArrayList<ItemMode>();
        	receipt.setTenderControlType(TransactionVariable.BALANCING);
        	receipt.setTenderCtrlBalanceType("Cash");
        	cashMachineList = SetChangerDrawerValues(cashMachineList, tender.getDevices(), TransactionVariable.BALANCING);
        	receipt.setTenderBalCashNetTotalAmt(tender.getNetTotal());
        	receipt.setTenderBalCashGrossTotalAmt(tender.getGrossTotal());
        	receipt.setTenderBalCashDiffAmt(tender.getDifference());
        	receipt.setTenderBalCashChangeReserveAmt(tender.getChangeReserve());
        	totalAmt = (long)cashChanger.getAmount().getAmount() + 
        			            (long)cashDrawer.getAmount().getAmount();
        	receipt.setTenderBalCashCMCDTotalAmt(totalAmt.toString());
        	receipt.setTenderBalNetTotal(tender.getNetTotal());
        	receipt.setTenderBalGrossTotal(tender.getGrossTotal());
        	receipt.setTenderBalDiff(tender.getDifference());
        	receipt.setTenderBalChangeReserve(tender.getChangeReserve());
        	receipt.setItemList(cashMachineList);
        	
        	receiptResult.add(receipt);
        } else if (!StringUtility.isNullOrEmpty(dayPart) && dayPart.equals("EOD")) {
        	CreditDebit creditDebit = tillSettle.getCreditDebit();
        	List<Voucher> voucherList = tillSettle.getVoucher();
        	Tender tender = tillSettle.getTender();
        	CashChanger cashChanger = tender.getDevices().getCashChanger();
        	CashDrawer cashDrawer = tender.getDevices().getCashDrawer();
        	TransactionLink transactionLink = poslog.getTransaction().getTransactionLink();
        	List<ItemMode> listItemMode = new ArrayList<ItemMode>();
        	String testNum1, testNum2;
        	String compId = "0",
        		    storeId = "0",
        		    nameCategory = "0004",
        		    weather, weatherName = "", customers, guests;
        	        	
        	if (!StringUtility.isNullOrEmpty(txPrintTypes) && txPrintTypes.getPrintNonCashReceipt().equals("true")) {
        		String regCnt, calcAmt, calcAmt2, gap, regAmt;
            	List<ItemMode> nonCashAmountList = new ArrayList<ItemMode>();
            	ItemMode item = null;
            	
            	receipt = new ReceiptMode();
            	receipt = iReceiptDao.getReceiptInfo(poslog,
                        poslog.getTransaction().getRetailStoreID());
            	
            	receipt.setOperatorID(opeId);
                receipt.setOperatorName(opeName);
                receipt.setTrainModeFlag(poslog.getTransaction().getTrainingModeFlag());
                receipt.setBeginDateTime(poslog.getTransaction().getBeginDateTime());
                receipt.setBusinessDayDate(poslog.getTransaction().getBusinessDayDate());
                receipt.setSequenceNo(poslog.getTransaction().getSequenceNo());
                receipt.setWorkStationID(poslog.getTransaction().getWorkStationID().getValue());
                receipt.setOperatorID(poslog.getTransaction().getOperatorID().getValue());
                receipt.setStoreID(poslog.getTransaction().getRetailStoreID());

                receipt.setReceiptType("TenderControl");
            	
            	receipt.setTenderControlType(TransactionVariable.BALANCING);
            	receipt.setTenderCtrlBalanceType("NonCash");
            	
            	regCnt = creditDebit.getRegisteredCount();
            	calcAmt = creditDebit.getCurrentCount();
            	calcAmt2 = creditDebit.getCurrentAmount();
            	gap = creditDebit.getDifference();
            	regCnt = FillSpace(regCnt, 9, "right");
            	calcAmt = FillSpace(calcAmt, 9, "right");
            	calcAmt2 = FillSpace(calcAmt2, 13, "right");
            	gap = FillSpace(gap, 9, "right");
            	
            	item = new ItemMode();
            	item.setTenderCtrlType(TransactionVariable.BALANCING);
            	item.setTenderCtrlBalType("NonCash");
            	item.setGroupLabel(CashNonCashConstants.NCCREDIT_LABEL);
            	item.setRealAmount(regCnt);
            	item.setCalculatedAmount(calcAmt);
            	item.setCalculatedAmt2(calcAmt2);
            	item.setGapAmount(gap);
            	nonCashAmountList.add(item);
            	
            	String tenderType;
            	Voucher voucher;
            	Long voucherRegAmt, voucherCurAmt, voucherDiff;
            	Long vouOtherRegAmt, vouOtherCurAmt, vouOtherDiff;
            	voucherRegAmt = Long.parseLong("0");
            	voucherCurAmt = Long.parseLong("0");
            	voucherDiff = Long.parseLong("0");
            	vouOtherRegAmt = Long.parseLong("0");
            	vouOtherCurAmt = Long.parseLong("0");
            	vouOtherDiff = Long.parseLong("0");
            	for(Iterator<Voucher> iterVoucherList=voucherList.iterator(); iterVoucherList.hasNext();) {
            		voucher = iterVoucherList.next();
            		tenderType = voucher.getTenderType();
            		
            		if (tenderType.equals(CashNonCashConstants.VOUCHERCOMMON)) {
            			voucherRegAmt = voucherRegAmt + Long.parseLong(voucher.getRegisteredAmount());
            			voucherCurAmt = voucherCurAmt + Long.parseLong(voucher.getCurrentAmount());
            			voucherDiff = voucherDiff + Long.parseLong(voucher.getDifference());
            		} else if (tenderType.equals(CashNonCashConstants.VOUCHEROTHERS)) {
            			vouOtherRegAmt = vouOtherRegAmt + Long.parseLong(voucher.getRegisteredAmount());
            			vouOtherCurAmt = vouOtherCurAmt + Long.parseLong(voucher.getCurrentAmount());
            			vouOtherDiff = vouOtherDiff + Long.parseLong(voucher.getDifference());
            		} else {
            			
            		}
            	}
            	
            	regAmt = Long.toString(voucherRegAmt);
            	calcAmt = Long.toString(voucherCurAmt);
            	gap = Long.toString(voucherDiff);
            	regAmt = FillSpace(regAmt, 23, "right");
            	regCnt = "0";
            	regCnt = FillSpace(regCnt, 9, "right");
            	calcAmt = FillSpace(calcAmt, 13, "right");
            	gap = FillSpace(gap, 23, "right");
            	
            	item = new ItemMode();
            	item.setTenderCtrlType(TransactionVariable.BALANCING);
            	item.setTenderCtrlBalType("NonCash");
            	item.setGroupLabel(CashNonCashConstants.NCCOMMODITYTICKET_LABEL);
            	item.setRealAmount(regAmt);
            	item.setCalculatedAmount(regCnt);
            	item.setCalculatedAmt2(calcAmt);
            	item.setGapAmount(gap);
            	nonCashAmountList.add(item);
            	
            	regAmt = Long.toString(vouOtherRegAmt);
            	calcAmt = Long.toString(vouOtherCurAmt);
            	gap = Long.toString(vouOtherDiff);
            	regAmt = FillSpace(regAmt, 23, "right");
            	regCnt = "0";
            	regCnt = FillSpace(regCnt, 9, "right");
            	calcAmt = FillSpace(calcAmt, 13, "right");
            	gap = FillSpace(gap, 23, "right");
            	
            	item = new ItemMode();
            	item.setTenderCtrlType(TransactionVariable.BALANCING);
            	item.setTenderCtrlBalType("NonCash");
            	item.setGroupLabel(CashNonCashConstants.NCOTHERS_LABEL);
            	item.setRealAmount(regAmt);
            	item.setCalculatedAmount(regCnt);
            	item.setCalculatedAmt2(calcAmt);
            	item.setGapAmount(gap);
            	nonCashAmountList.add(item);
            	
            	receipt.setItemList(nonCashAmountList);
            	receiptResult.add(receipt);
        	}
        	
        	if (!StringUtility.isNullOrEmpty(txPrintTypes) && txPrintTypes.getPrintCashReceipt().equals("true")) {
        		Long totalAmt;
        		
        		receipt = new ReceiptMode();
            	receipt = iReceiptDao.getReceiptInfo(poslog,
                        poslog.getTransaction().getRetailStoreID());
            	receipt.setOperatorID(opeId);
                receipt.setOperatorName(opeName);
                receipt.setTrainModeFlag(poslog.getTransaction().getTrainingModeFlag());
                receipt.setBeginDateTime(poslog.getTransaction().getBeginDateTime());
                receipt.setBusinessDayDate(poslog.getTransaction().getBusinessDayDate());
                receipt.setSequenceNo(poslog.getTransaction().getSequenceNo());
                receipt.setWorkStationID(poslog.getTransaction().getWorkStationID().getValue());
                receipt.setOperatorID(poslog.getTransaction().getOperatorID().getValue());
                receipt.setStoreID(poslog.getTransaction().getRetailStoreID());

                receipt.setReceiptType("TenderControl");
            	
            	List<ItemMode> cashMachineList = new ArrayList<ItemMode>();
            	receipt.setTenderControlType(TransactionVariable.BALANCING);
            	receipt.setTenderCtrlBalanceType("Cash");
            	cashMachineList = SetChangerDrawerValues(cashMachineList, tender.getDevices(), TransactionVariable.BALANCING);
            	receipt.setTenderBalCashNetTotalAmt(tender.getNetTotal());
            	receipt.setTenderBalCashGrossTotalAmt(tender.getGrossTotal());
            	receipt.setTenderBalCashDiffAmt(tender.getDifference());
            	receipt.setTenderBalCashChangeReserveAmt(tender.getChangeReserve());
            	totalAmt = (long)cashChanger.getAmount().getAmount() + 
            			            (long)cashDrawer.getAmount().getAmount();
            	receipt.setTenderBalCashCMCDTotalAmt(totalAmt.toString());
            	receipt.setItemList(cashMachineList);
            	
            	receiptResult.add(receipt);
        	}
        	
        	if (!StringUtility.isNullOrEmpty(txPrintTypes) && txPrintTypes.getPrintStoreReceipt().equals("true")) {
	        	guests = ": ";
	        	weather = transactionLink.getWeather().getCode();
	        	weatherName = transactionLink.getWeather().getElementValue();
	        	customers = transactionLink.getCustomer();
	        	customers = ": " + customers;

	        	weatherName = ": " + weather + " " + weatherName;
	        	weatherName = FillSpace(weatherName, 29, "left");
	        	customers = FillSpace(customers, 31, "left");
	        	guests = FillSpace(guests, 31, "left");
	        	
	        	receipt.setWeatherNameId(weatherName);
	        	receipt.setEODNumCustomers(customers);
	        	receipt.setEODNumGuests(guests);
	        	receipt.setTenderControlType(TransactionVariable.EOD);
	        	receipt.setItemList(listItemMode);
	        	
	        	testNum1 = "999";
	        	testNum2 = "999999";
	        	
	        	testNum2 = FillSpace(testNum2, 15, "right");
	        	
	        	receipt.setTenderControlPayIn(testNum1 + testNum2);
	        	receiptResult.add(receipt);
        	}
        }
        
        return receiptResult;
    }
    private ReceiptMode resolveTenderControlPoslog(PosLog poslog) throws DaoException{

        tp.methodEnter(DebugLogger.getCurrentMethodName()).println("poslog",
                poslog.toString());

        IReceiptDAO iReceiptDao = DAOFactory.getDAOFactory(
                DAOFactory.SQLSERVER).getReceiptDAO();
        ReceiptMode receipt = iReceiptDao.getReceiptInfo(poslog,
                poslog.getTransaction().getRetailStoreID());
        TenderControlTransaction tenderControl = poslog.getTransaction()
                .getTenderControlTransaction();

        if(StringUtility.isNullOrEmpty(receipt)){
            return null;
        }else if(StringUtility.isNullOrEmpty(tenderControl)){
            return null;
        }
        receipt.setTrainModeFlag(poslog.getTransaction().getTrainingModeFlag());
        receipt.setBeginDateTime(poslog.getTransaction().getBeginDateTime());
        receipt.setBusinessDayDate(poslog.getTransaction().getBusinessDayDate());
        receipt.setSequenceNo(poslog.getTransaction().getSequenceNo());
        receipt.setWorkStationID(poslog.getTransaction().getWorkStationID().getValue());
        receipt.setOperatorID(poslog.getTransaction().getOperatorID().getValue());
        receipt.setStoreID(poslog.getTransaction().getRetailStoreID());

        receipt.setReceiptType("TenderControl");
        TillSettle tillSettle = tenderControl.getTillSettle();
        TenderLoan loan = tenderControl.getTenderLoan();
        TenderPickup pickup= tenderControl.getTenderPickup();
        TenderExchange exchange = tenderControl.getTenderExchange();
        Guarantee guarantee = tenderControl.getGuarantee();
        PayOut payout = null;
        PayIn payin = null;
        PayInPlan payinplan = null;
        if (!StringUtility.isNullOrEmpty(tillSettle)) {
        	payout = tillSettle.getPayOut();
            payin = tillSettle.getPayIn();
            payinplan = tillSettle.getPayInPlan();
        }
        
        Beginning beginning = null;
        TenderSummary tenderSummary = null;
        Tender tender = null;
        String dayPart = tenderControl.getDayPart();
        
        if (!StringUtility.isNullOrEmpty(loan)){
            receipt.setTenderControlType(TransactionVariable.LOAN);
            receipt.setTenderControlLoan(StringUtility.convNullToDoubleZero(loan.getAmount()));
        } else if (!StringUtility.isNullOrEmpty(pickup)){
            receipt.setTenderControlType(TransactionVariable.PICKUP);
            receipt.setTenderControlPickup(StringUtility.convNullToDoubleZero(pickup.getAmount()));
        } else if (!StringUtility.isNullOrEmpty(exchange)){
            receipt.setTenderControlType(TransactionVariable.EXCHANGE);
            receipt.setTenderControlExchange(StringUtility.convNullToDoubleZero(
                    exchange.getExchangeDetail().getTenderTotal().getTotal()));
        } else if (!StringUtility.isNullOrEmpty(payout)){
        	String total;
        	receipt.setTenderCtrlTypePayInOut("true");
        	receipt.setTenderControlType(TransactionVariable.PAYOUT);
        	total = StringUtility.convNullToDoubleZero(payout.getAmount().getAmount() + "") + "";
        	receipt.setTenderControlPayOut(total);
        	total = Long.toString((long)payinplan.getAmount().getAmount());
        	total = StringUtility.addCommaToNumString(total);
        	receipt.setTenderCtrlPayInPlanDrawerAmt(total);
        	
        	List<ItemMode> cashMachineList = new ArrayList<ItemMode>();
        	cashMachineList = SetChangerDrawerValues(cashMachineList, payout.getDevices(), tenderControl.getDayPart());
        	cashMachineList = SetDrawerValues(cashMachineList, payinplan.getDevices().getCashDrawer(), "PayInPlan");
        	receipt.setItemList(cashMachineList);
        	
        	
        } else if (!StringUtility.isNullOrEmpty(payin)){
        	receipt.setTenderCtrlTypePayInOut("true");
        	receipt.setTenderControlType(TransactionVariable.PAYIN);
        	
        	Double changeMachineAmt, cashDrawerAmt, payinAmt;
        	changeMachineAmt = payin.getDevices().getCashChanger().getAmount().getAmount();
        	cashDrawerAmt = payin.getDevices().getCashDrawer().getAmount().getAmount();
        	payinAmt = changeMachineAmt + cashDrawerAmt;
        	receipt.setTenderControlPayIn(StringUtility.convNullToDoubleZero(payinAmt + "") + "");
        	
        	List<ItemMode> cashMachineList = new ArrayList<ItemMode>();
        	cashMachineList = SetChangerDrawerValues(cashMachineList, payin.getDevices(), tenderControl.getDayPart());
        	receipt.setItemList(cashMachineList);
        } else if (!StringUtility.isNullOrEmpty(guarantee)) {
        	receipt.setTenderControlType(TransactionVariable.GUARANTEE);
        } else if (dayPart.equals("SOD")) {
        	String beginAmount = "0",
        			storeId, compId, terminalId, businessDayDate;
        	
            if (!StringUtility.isNullOrEmpty(tillSettle)) {
            	tender = tillSettle.getTender();
            	if (!StringUtility.isNullOrEmpty(tender)) {
            		beginAmount = tender.getAmount();
            	}
            }
            
            StoreResource storeRes = new StoreResource();
            CMPresetInfos cmpresetInfos = new CMPresetInfos();
            Transaction tran = poslog.getTransaction();
            String topmsg1, topmsg2, topmsg3, topmsg4, topmsg5,
                   botmsg1, botmsg2, botmsg3, botmsg4, botmsg5;
            int cmType=0;
            
            storeId = tran.getRetailStoreID();
            compId = tran.getOrganizationHierarchy().getId();
            terminalId = tran.getWorkStationID().getValue();
            businessDayDate = tran.getBusinessDayDate();
            cmpresetInfos = storeRes.getCMPresetInfoList(compId, storeId, terminalId, businessDayDate);
            CMPresetInfo cmpHighest = null;
            
            for(CMPresetInfo cmpi:  cmpresetInfos.getCMPresetInfoList()) {
            	if (cmpHighest == null) {
            		cmpHighest = cmpi;
            	}
            	for(CMPresetInfo cmpii : cmpresetInfos.getCMPresetInfoList()) {
            		if (Integer.parseInt(cmpii.getCMType()) > Integer.parseInt(cmpHighest.getCMType())) {
            			cmpHighest = cmpii;
            		}
            	}
            }

            topmsg1 = cmpHighest != null ? cmpHighest.getTop1Message() : "";
            topmsg2 = cmpHighest != null ? cmpHighest.getTop2Message() : "";
            topmsg3 = cmpHighest != null ? cmpHighest.getTop3Message() : "";
            topmsg4 = cmpHighest != null ? cmpHighest.getTop4Message() : "";
            topmsg5 = cmpHighest != null ? cmpHighest.getTop5Message() : "";
            botmsg1 = cmpHighest != null ? cmpHighest.getBottom1Message() : "";
            botmsg2 = cmpHighest != null ? cmpHighest.getBottom2Message() : "";
            botmsg3 = cmpHighest != null ? cmpHighest.getBottom3Message() : "";
            botmsg4 = cmpHighest != null ? cmpHighest.getBottom4Message() : "";
            botmsg5 = cmpHighest != null ? cmpHighest.getBottom5Message() : "";

            receipt.setTenderCtrlSODTopMsg1(topmsg1);
            receipt.setTenderCtrlSODTopMsg2(topmsg2);
            receipt.setTenderCtrlSODTopMsg3(topmsg3);
            receipt.setTenderCtrlSODTopMsg4(topmsg4);
            receipt.setTenderCtrlSODTopMsg5(topmsg5);
            receipt.setTenderCtrlSODBottomMsg1(botmsg1);
            receipt.setTenderCtrlSODBottomMsg2(botmsg2);
            receipt.setTenderCtrlSODBottomMsg3(botmsg3);
            receipt.setTenderCtrlSODBottomMsg4(botmsg4);
            receipt.setTenderCtrlSODBottomMsg5(botmsg5);

        	receipt.setTenderControlType(TransactionVariable.SOD);
        	receipt.setTenderCtrlBeginningAmount(beginAmount);
        }
        return receipt;
    }
    private String FillSpace(String strToFill, int fixedLength, String direction) {
    	String str;
    	str = strToFill;
    	
    	if(direction.toLowerCase()=="left") {
    		str = String.format("%1$-" + fixedLength + "s", str);
    	} else {
    		str = String.format("%1$," + fixedLength + "d", Long.parseLong(str));
    	}
    	
    	return str;
    }
    private List<ItemMode> SetDrawerValues(List<ItemMode> cashDrawerList, CashDrawer cashDrawer, String dayPart) {
    	List<MonetaryKind> lmonetaryKind;
    	CashNonCashConstants cncc = new CashNonCashConstants();
    	ItemMode item;
    	MonetaryKind mk;
    	int kind, col1Len, col2Len, col3Len, i;
    	Long qty, rolls, amount;
    	String sKind, sqty, samount;
    	lmonetaryKind = cashDrawer.getMonetaryKind();
    	col1Len = 0;
    	col2Len = 0;
    	col3Len = 0;
    	i = 0;
    	qty = Long.parseLong("0");
    	rolls = Long.parseLong("0");
    	amount = Long.parseLong("0");
    	
    	col1Len = cncc.GetCorrectLen(dayPart, "col1");
    	col2Len = cncc.GetCorrectLen(dayPart, "col2");
    	col3Len = cncc.GetCorrectLen(dayPart, "col3");
    	for(Iterator<MonetaryKind> iterMonetaryKind = lmonetaryKind.iterator(); iterMonetaryKind.hasNext();) {
    		if (cashDrawerList.isEmpty()) {
    			item = new ItemMode();
    		} else {
    			item = cashDrawerList.get(i);
    		}
    		
    		mk = iterMonetaryKind.next();
    		
    		kind = Integer.parseInt(mk.getKind());
    		qty = Long.parseLong(mk.getQuantity());
    		if (!StringUtility.isNullOrEmpty(mk.getRolls())) {
    			rolls = Long.parseLong(mk.getRolls());
    		}
    		qty = qty + (rolls * cncc.DetermineRolls(kind));
    		amount = qty * kind;
    		
    		sKind = StringUtility.addCommaYenToNumString(Integer.toString(kind));
    		sKind = FillSpace(sKind, col1Len, "left");
    		sqty = Long.toString(qty);
    		sqty = FillSpace(sqty, col2Len, "right");
    		samount = Long.toString(amount);
    		samount = FillSpace(samount, col3Len, "right");
    		
    		item.setPayInPlanDenomination(sKind);
    		item.setPayInPlanDenomQty(sqty);
    		item.setPayInPlanDrawerAmount(samount);
    		item.setTenderCtrlType(dayPart);
    		
    		if (cashDrawerList.isEmpty()) {
    			cashDrawerList.add(item);
    		} else {
    			cashDrawerList.set(i, item);
    			i++;
    		}
    	}
    	
    	return cashDrawerList;
    }
    private List<ItemMode> SetChangerDrawerValues(List<ItemMode> cashMachineList, Devices devices, String dayPart) {
    	List<MonetaryKind> lmonetaryKind;
    	ItemMode item;
    	int kind, col1Len, col2Len, col3Len, col4Len;
    	Long cmQty, cdQty, cdRolls, cmcdTotal;
    	MonetaryKind mk;
    	col1Len = 0;
    	col2Len = 0;
    	col3Len = 0;
    	col4Len = 0;
   		lmonetaryKind = devices.getCashChanger().getMonetaryKind();

    	for(Iterator<MonetaryKind> itermonetaryKind = lmonetaryKind.iterator(); itermonetaryKind.hasNext();) {
    		item = new ItemMode();
    		mk = itermonetaryKind.next();
    		
    		kind = Integer.parseInt(mk.getKind());
    		cmQty = Long.parseLong(mk.getQuantity());
    		item.setDenomination(mk.getKind() + "");
    		item.setChangeMachineAmount(Long.toString(kind * cmQty));
    		item.setChangeMachineDenomQty(cmQty.toString());
    		item.setDenominationQty(cmQty.toString());
    		cashMachineList.add(item);
    		
    	}
    	
    	int i;
    	Long qty;
    	lmonetaryKind = devices.getCashDrawer().getMonetaryKind();
    	
    	item = new ItemMode();
    	i=0;
    	CashNonCashConstants crc = new CashNonCashConstants();
        for(Iterator<MonetaryKind> itermonetaryKind = lmonetaryKind.iterator(); itermonetaryKind.hasNext();) {
    		mk = itermonetaryKind.next();
    		item = cashMachineList.get(i);
    		
    		kind = Integer.parseInt(mk.getKind());
    		
    		if (Integer.parseInt(item.getDenomination()) == kind) {
    			cdQty = Long.parseLong(mk.getQuantity());
    			cdRolls = Long.parseLong("0");
    			if (!StringUtility.isNullOrEmpty(mk.getRolls())) {
    				cdRolls = Long.parseLong(mk.getRolls());
    			}
    			qty = (cdQty + (cdRolls * crc.DetermineRolls(kind)));
    			cmcdTotal = (kind * qty) + Long.parseLong(item.getChangeMachineAmount());
    			item.setCashDrawerAmount(Long.toString(kind * qty));
    			item.setChangeMachineDrawerTotal(Long.toString(cmcdTotal));
    			item.setCashDrawerDenomQty(qty.toString());
    			
    			qty = qty + Long.parseLong(item.getChangeMachineDenomQty());
    			item.setDenominationQty(qty.toString());
    			cashMachineList.set(i, item);
    		}
    		i++;
    	}
        
        i=0;
        String strDenom, strcmAmount, strcdAmount, strcmdTotalAmount, strDenomQty;
        
        col1Len = crc.GetCorrectLen(dayPart, "col1");
    	col2Len = crc.GetCorrectLen(dayPart, "col2");
    	col3Len = crc.GetCorrectLen(dayPart, "col3");
    	col4Len = crc.GetCorrectLen(dayPart, "col4");
        for(Iterator<ItemMode> itercmList = cashMachineList.iterator(); itercmList.hasNext();) {
        	item = itercmList.next();
        	
        	strDenom = StringUtility.addCommaYenToNumString(item.getDenomination());
        	strcmAmount = item.getChangeMachineAmount();
        	strcdAmount = item.getCashDrawerAmount();
        	strcmdTotalAmount = item.getChangeMachineDrawerTotal();
        	strDenomQty = item.getDenominationQty();
        	
        	strDenom = FillSpace(strDenom, col1Len, "left");
        	strcmAmount = FillSpace(strcmAmount, col2Len, "right");
        	strcdAmount = FillSpace(strcdAmount, col3Len, "right");
        	strcmdTotalAmount = FillSpace(strcmdTotalAmount, col4Len, "right");
        	strDenomQty = FillSpace(strDenomQty, col2Len, "right");
        	
        	item.setDenomination(strDenom);
        	item.setChangeMachineAmount(strcmAmount);
        	item.setCashDrawerAmount(strcdAmount);
        	item.setChangeMachineDrawerTotal(strcmdTotalAmount);
        	item.setDenominationQty(strDenomQty);
        	
        	if (dayPart.equals("PayIn") || dayPart.equals("PayOut")) {
        		item.setTenderCtrlTypePayInOutList("true");
        	} else if (dayPart.equals("Balancing")) {
        		item.setTenderCtrlType(dayPart);
        		item.setTenderCtrlBalType("Cash");
        		item.setTenderCtrlTypePayInOutList("false");
        	}
        	
        	cashMachineList.set(i, item);
        	i++;
        }
        return cashMachineList;
    }
    /**
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     * @throws ParseException
     * @throws NamingException
     *
     */
    public static String getLocalPrinterPort(String portType) throws NamingException {

        String printerPort="";
        SAXReader sr = new SAXReader();
        try {

            Context env = (Context) new InitialContext()
            .lookup("java:comp/env");
            String xmlPath = (String) env
            .lookup("localPrinter");

            Document doc = sr.read(new File(xmlPath));
            Element root = doc.getRootElement();  // Root Element "Interface"
            Element ports = root.element(PORTS_TAG);  // Point to Ports element

            for (Iterator<?> it = ports.elementIterator(); it.hasNext();) {
                Element elem = (Element) it.next();

                if(elem.getName().equalsIgnoreCase(portType)){
                    printerPort= elem.getData().toString();
                    break;
                }

            }
        } catch (DocumentException e) {
            LOGGER.logError(PROG_NAME, PROG_NAME+".getLocalPrinterPort()",
                    Logger.RES_EXCEP_GENERAL,
                    "DocumentException:" + e.getMessage());
        }

        return printerPort;
    }

    NetPrinterInfo createPrinterInfo(IReceiptDAO iReceiptDAO,
                                     String storeNo, String deviceNo,
                                     String printerid,
                                     ResultBase resultBase) throws DaoException,
                                                                   NamingException {
        NetPrinterInfo netPrinterInfo = null;
        //check if printer default setting
        if(printerid != null && LOCAL_PRINTER.equalsIgnoreCase(printerid)) {
            netPrinterInfo = new NetPrinterInfo();

            //read from xml
            String printerPort = getLocalPrinterPort(PRINTER_TAG);

            //check if no port is retrieved from interface.xml
            if (StringUtility.isNullOrEmpty(printerPort)){
                String errorMessage = "No default printer port found";
                resultBase.setNCRWSSResultCode(ResultBase.RES_PRINTER_PORT_NOT_FOUND);
                resultBase.setNCRWSSExtendedResultCode(
                       ResultBase.RES_PRINTER_PORT_NOT_FOUND);
                resultBase.setMessage(errorMessage);
                return null;
            }
            //set retrieved printer port to url
            netPrinterInfo.setUrl(printerPort);
        } else{
            //for network printing
            netPrinterInfo = iReceiptDAO.getPrinterInfo(storeNo, deviceNo);
            if (netPrinterInfo == null) {
                tp.println("PrinterInfo is null.");
                String errorMessage = "No PrinterInfo found for store="
                    + storeNo + ";device=" + deviceNo;
                LOGGER.logError(PROG_NAME, "createPrinterInfo",
                                Logger.RES_EXCEP_FILENOTFOUND, errorMessage);
                resultBase.setNCRWSSResultCode(ResultBase.RESDEVCTL_NOPRINTERFOUND);
                resultBase.setNCRWSSExtendedResultCode(
                       ResultBase.RESDEVCTL_NOPRINTERFOUND);
                resultBase.setMessage(errorMessage);
                return null;
            }
        }
        tp.println("IpAddress", netPrinterInfo.getUrl())
            .println("TCP PORT", netPrinterInfo.getPortTCP())
            .println("UDP PORT", netPrinterInfo.getPortUDP());

        return netPrinterInfo;
    }

    public void setLanguage(String language, ReceiptMode receiptMode) {
        receiptMode.setLanguage(
                                (StringUtility.isNullOrEmpty(language))
                                ? GlobalConstant.getDefaultLanguage()
                                : language);
    }

    String createErrorMessage(String msg, TransactionLink link) {
        return "S:" + link.getRetailStoreID() + " T:" + link.getWorkStationID()
            + " X:" + link.getSequenceNo() + msg;
    }
    /**
     * @param poslog
     * @return
     * set the store information to receipt mode
     */
    private ReceiptMode setStoreInfo(PosLog poslog){
    	IReceiptDAO irDao;
    	ReceiptMode receipt = null;
		try {
			irDao = DAOFactory.getDAOFactory(
			        DAOFactory.SQLSERVER).getReceiptDAO();
			receipt = irDao.getReceiptInfo(poslog,
	                poslog.getTransaction().getRetailStoreID());
		} catch (DaoException e) {
			//TODO
		}
        return receipt;
    }
    /**
     * @param poslog
     * @param receipt
     * set the receipt information to receipt mode
     */
    private void setReceiptTypeInfo(PosLog poslog, ReceiptMode receipt){
    	String tStatus = poslog.getTransaction().getRetailTransaction()
                .getTransactionStatus();
        List<LineItem> lineItemList= poslog.getTransaction().getRetailTransaction().getLineItems();
        if(!StringUtility.isNullOrEmpty(poslog.getTransaction().getCancelFlag())){
            receipt.setReceiptType(ReceiptMode.RECEIPTTYPE_CANCEL);
        }else if(ReceiptMode.RECEIPTTYPE_VOID.equalsIgnoreCase(tStatus)){
            receipt.setReceiptType(ReceiptMode.RECEIPTTYPE_VOID);
            receipt.setVoidedType(ReceiptMode.RECEIPTTYPE_VOID);
        }else if(lineItemList != null && (!lineItemList.isEmpty()) &&
                lineItemList.get(0).getRetrn() != null){
            receipt.setReceiptType(ReceiptMode.RECEIPTTYPE_RETURN);
        }else{
           receipt.setReceiptType(ReceiptMode.RECEIPTTYPE_NORMAL);
        }
        receipt.setVoidreturnFlag("False");
        Iterator<LineItem> itemIter = lineItemList.iterator();
        if(ReceiptMode.RECEIPTTYPE_VOID.equalsIgnoreCase(receipt.getReceiptType())){
        	while(itemIter.hasNext()){
        		LineItem judge = itemIter.next();
        		if(judge != null && judge.getRetrn() != null){
        			receipt.setVoidreturnFlag("True");
        			break;
        		}
            }
        }
        receipt.setVoidedType(receipt.getVoidedType() + ReceiptMode.RECEIPTTYPE_RETURN);
    }
    /**
     * @param tran
     * @param receipt
     * set transaction information to receipt mode
     */
    private void setTranasctionInfo(Transaction tran, ReceiptMode receipt){
    	receipt.setTrainModeFlag(tran.getTrainingModeFlag());
        receipt.setBeginDateTime(tran.getBeginDateTime());
        receipt.setBusinessDayDate(tran.getBusinessDayDate());
        receipt.setSequenceNo(tran.getSequenceNo());
        receipt.setWorkStationID(tran.getWorkStationID().getValue());
        if(!StringUtility.isNullOrEmpty(tran.getOperatorID())){
        	receipt.setOperatorID(tran.getOperatorID().getValue());
        }
        receipt.setStoreID(tran.getRetailStoreID());
    }
    /**
     * @param Promotion
     * @param receipt
     * set promotion information to receipt mode
     */
    private void setPromotionInfo(BarLoyaltyReward Promotion, ReceiptMode receipt){
    		receipt.setBarPromotionID(Promotion.getBarPromotionID());
    		receipt.setBarPromotionName(Promotion.getBarPromotionName());
    }
    /**
     * @param tLink
     * @param receipt
     */
    private void setTransactinLinkInfo(TransactionLink tLink, ReceiptMode receipt){

        receipt.setOriginalSequenceNo(tLink.getSequenceNo());
        receipt.setOriginalWorkStationID(tLink.getWorkStationID().getValue());
        receipt.setOriginalOperatorID(tLink.getOperatorID());
        receipt.setOriginalRetailStoreID(tLink.getRetailStoreID());
        receipt.setOriginalBusinessDayDate(tLink.getBusinessDayDate());
        receipt.setOriginalBeginDateTime(tLink.getBeginDateTime());

        receipt.setVoiderBeginDateTime(tLink.getBeginDateTime());
        receipt.setVoiderBusinessDayDate(tLink.getBusinessDayDate());
        receipt.setVoiderOperatorID(tLink.getOperatorID());
        receipt.setVoiderRetailStoreID(tLink.getRetailStoreID());
        receipt.setVoiderSequenceNumber(tLink.getSequenceNo());
        receipt.setVoiderWorkstationID(tLink.getWorkStationID().getValue());
        if("Voided".equals(receipt.getReceiptType()) && "Layaway"
                .equalsIgnoreCase(tLink.getReasonCode())){
            //前受金一括取消タイプ
            receipt.setAdvanceVoidType(TransactionVariable.ADVANCEVOIDTYPE);
            receipt.setInventoryReservationID(tLink.getInventoryReservationID());
        }else if("Voided".equals(receipt.getReceiptType()) 
                && "Hold".equalsIgnoreCase(tLink.getReasonCode())){
            receipt.setHoldVoidType(TransactionVariable.HOLDVOID);
            receipt.setInventoryReservationID(tLink.getInventoryReservationID());
        }else if("Voided".equals(receipt.getReceiptType()) 
                && "CustomerOrder".equalsIgnoreCase(tLink.getReasonCode())){
            receipt.setCustomrOrderVoidType(TransactionVariable.CUSTOMERORDERVOID);
            receipt.setInventoryReservationID(tLink.getInventoryReservationID());
        }
    
    }
    /**
     * @param totalList
     * @param receipt
     * set total information to receipt mode
     */
    private void setTotalInfo(List<Total> totalList, ReceiptMode receipt){
    	for (Total total : totalList) {
            if ("TransactionSubtotal".equals(total.getTotalType())) {
                receipt.setSubtotal(total.getAmount());
                receipt.setSubtotalWithoutDiscount(total.getAmount());
            } else if ("TransactionGrandAmount".equals(total.getTotalType())) {
                receipt.setGrandAmount(total.getAmount());
            }
        }
    }
    /**
     * @param customer
     * @param receipt
     * set customer information to receipt mode
     */
    private void setCustomerInfo(ncr.res.mobilepos.journalization.model.poslog.Customer customer, ReceiptMode receipt){
    	// get customer demographic
        receipt.setCustGraphicId(customer.getCustomerDemographic());
        String customerIdSubStr = "";
        if(!StringUtility.isNullOrEmpty(customer.getCustomerId())){
             customerIdSubStr = customer
                        .getCustomerId().substring(0 , (customer
                        .getCustomerId().length() > 12) ? 12 : customer
                                .getCustomerId().length());
        }
        receipt.setCustomerId(customerIdSubStr);
    }
    private void setItemLineTagInfo(){}
    
    private String addMonths(String date, int MM) {
    	String res = "",
    		   ares[];
        int mm;
    	
    	ares = date.split("-");
    	
    	if (ares.length > 2) {
    		mm = Integer.parseInt(ares[1]);
    		mm = mm + MM;
    		if (mm > 12) {
    			mm = mm - 12;
    		}
    		
    		ares[1] = String.format("%02d", mm);
    		res = ares[0] + "-" + ares[1] + "-" + ares[2];
    	}
    	
    	return res;
    }

}
