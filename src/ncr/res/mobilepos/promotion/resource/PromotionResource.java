package ncr.res.mobilepos.promotion.resource;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.CheckDigitValue;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.barcodeassignment.constant.BarcodeAssignmentConstant;
import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.barcodeassignment.util.BarcodeAssignmentUtility;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentName;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.CouponInfo;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PremiumInfo;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.promotion.dao.ICodeConvertDAO;
import ncr.res.mobilepos.promotion.dao.IPromotionMsgInfoDAO;
import ncr.res.mobilepos.promotion.dao.IQrCodeInfoDAO;
import ncr.res.mobilepos.promotion.factory.PromotionMsgInfoFactory;
import ncr.res.mobilepos.promotion.factory.QrCodeInfoFactory;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.helper.TerminalItemsHandler;
import ncr.res.mobilepos.promotion.model.ItemList;
import ncr.res.mobilepos.promotion.model.MixMatchDetailInfo;
import ncr.res.mobilepos.promotion.model.Promotion;
import ncr.res.mobilepos.promotion.model.PromotionMsgInfo;
import ncr.res.mobilepos.promotion.model.PromotionResponse;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.model.Transaction;
import ncr.res.mobilepos.searchapi.helper.UrlConnectionHelper;

/**
 * PromotionResource Class is a Web Resource which support MobilePOS Promotion
 * processes.
 *
 */

@Path("/promotion")
@Api(value = "/promotion", description = "プロモーションAPI")
public class PromotionResource {
	/**
	 * Non discountable type of item.
	 */
	public static final int NOT_DISCOUNTABLE = 1;
	/**
	 * Discountable type of item.
	 */
	public static final int DISCOUNTABLE = 0;

	public static final String REMOTE_UTL = "resTransaction/rest/remoteitem/item_getremoteinfo";
	public static final String ENTERPRISE_DPT_UTL = "resTransaction/rest/enterprisedpt/department_getremoteinfo";
	public static final String ENTERPRISE_MDNAME_UTL = "resTransaction/rest/remoteitem/mdName_getremoteinfo";

	/**
	 * ALL_DISCOUNTABLE for all MixMatch item.
	 */
	public static final String ALL_DISCOUNTABLE = "ALL";

	/** A private member variable used for the servlet context. */
	@Context
	private ServletContext context; // to access the web.xml
	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get
																		// the
																		// Logger
	/** The instance of the trace debug printer. */
	private Trace.Printer tp = null;

	/**
	 * The Program Name.
	 */
	private static final String PROG_NAME = "Prom";

	private final BarcodeAssignment barcodeAssignment;

	private final List<QrCodeInfo> qrCodeInfoList;

	private final List<PromotionMsgInfo> promotionMsgInfoList;

	public static final String PROMOTIONTYPE_ALL = "1";
	public static final String PROMOTIONTYPE_DPT = "2";
	public static final String PROMOTIONTYPE_DPT_CONN = "3";
	public static final String PROMOTIONTYPE_DPT_BRANDID = "4";
	public static final String PROMOTIONTYPE_LINE = "5";
	public static final String PROMOTIONTYPE_ITEMCODE = "6";
	public static final String OUTPUTTYPE_ONE = "1";
	public static final String OUTPUTTYPE_TWO = "2";
	public static final String OUTPUTTYPE_THREE = "3";
	public static final String MEMBERTARGETTYPE_ZERO = "0";
	public static final String MEMBERTARGETTYPE_ONE = "1";

	/**
	 * Default Constructor for PromotionResource.
	 *
	 * <P>
	 * Initializes the logger object.
	 */
	public PromotionResource() {
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
		barcodeAssignment = BarcodeAssignmentFactory.getInstance();
		qrCodeInfoList = QrCodeInfoFactory.getInstance();
		promotionMsgInfoList = PromotionMsgInfoFactory.getInstance();
	}

	/**
	 * The Begin Transaction for Promotion.
	 *
	 * @param retailStoreId
	 *	        The Retail Store ID.
	 * @param workStationId
	 *            The Workstation ID.
	 * @param sequenceNo
	 *            The Sequence No.
	 * @param transactionJson
	 *            The Transaction in JSON.
	 * @return The {@link ResultBase}
	 */
	@SuppressWarnings("unchecked")
	@Path("/begin_transaction")
	@POST
	@Produces("application/json;charset=UTF-8")
	@ApiOperation(value = "プロモーション取引開始", response = ResultBase.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
			@ApiResponse(code = ResultBase.RES_PROMOTION_DATE_INVALID, message = "プロモーション日付無効") })
	public final ResultBase beginTransaction(
			@ApiParam(name = "retailstoreid", value = "小売店コード") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "シリアルナンバー") @FormParam("sequencenumber") final String sequenceNo,
			@ApiParam(name = "companyid", value = "会社コード") @FormParam("companyid") final String companyid,
			@ApiParam(name = "transaction", value = "取引情報") @FormParam("transaction") final String transactionJson) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreId", retailStoreId).println("WorkstationId", workStationId)
				.println("SequenceNumber", sequenceNo).println("companyid", companyid)
				.println("Transaction", transactionJson);
		ResultBase rsBase = new ResultBase();
		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, workStationId, sequenceNo, transactionJson)) {
				rsBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return rsBase;
			}
			JsonMarshaller<Transaction> transactionMarshaller = new JsonMarshaller<Transaction>();
			Transaction tx = transactionMarshaller.unMarshall(transactionJson, Transaction.class);
			// Is of Legal Format? If yes, Set to Legal Format.
			if (!DateFormatUtility.isLegalFormat(tx.getBeginDateTime(), "yyyy-MM-dd'T'HH:mm:ss.SS")) {
				rsBase.setNCRWSSResultCode(ResultBase.RES_PROMOTION_DATE_INVALID);
				tp.println("BeginDateTime Format is invalid. It must be in " + "yyyy-MM-ddTHH:mm:ss.SS");
				return rsBase;
			}

			synchronized ((Map<String, TerminalItem>) GlobalConstant.getTerminalItemsMap()) {
				Map<String, TerminalItem> terminalItemsHashMap = (Map<String, TerminalItem>) GlobalConstant
						.getTerminalItemsMap();
				TerminalItem termItem = new TerminalItem(retailStoreId, workStationId, sequenceNo);
				termItem.setBeginDateTime(tx.getBeginDateTime());
				termItem.setOperatorid(tx.getOperatorID());
				termItem.setTransactionMode(tx.getTransactionMode());
				TerminalItemsHandler.add(termItem, terminalItemsHashMap);
			}

		} catch (IOException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
					functionName + ": Failed to begin transaction for promotion.", e);
			rsBase = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION, ResultBase.RES_OK, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to begin transaction for promotion.", e);
			rsBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_OK, e);
		} finally {
			tp.methodExit(rsBase);
		}
		return rsBase;
	}

	/**
	 * @param retailStoreId
	 *            Store number where the transaction is coming from.
	 * @param workStationId
	 *            Device number where the transaction is coming from.
	 * @param sequenceNumber
	 *            Transaction number.
	 * @param jsonTransaction
	 *            Transaction in JSON.
	 * @return The {@link ResultBase}
	 */
	@SuppressWarnings("unchecked")
	@Path("/end_transaction")
	@POST
	@Produces("application/json;charset=UTF-8")
	@ApiOperation(value = "プロモーション取引終了", response = ResultBase.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
			@ApiResponse(code = ResultBase.RES_PROMOTION_ENDTRANSACTION_FAILED, message = "取引終了失敗"),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ"),
			@ApiResponse(code = ResultBase.RES_PROMOTION_DATE_INVALID, message = "プロモーション日付無効") })
	public final ResultBase endTransaction(
			@ApiParam(name = "retailstoreid", value = "小売店コード") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "シリアルナンバー") @FormParam("sequencenumber") final String sequenceNumber,
			@ApiParam(name = "transaction", value = "取引情報") @FormParam("transaction") final String jsonTransaction) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreID", retailStoreId).println("WorkStationID", workStationId)
				.println("SequenceNumber", sequenceNumber).println("Transaction", jsonTransaction);
		ResultBase result = new ResultBase();
		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, workStationId, sequenceNumber, jsonTransaction)) {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return result;
			}
			JsonMarshaller<Transaction> jsonMarshaller = new JsonMarshaller<Transaction>();
			Transaction transaction = jsonMarshaller.unMarshall(jsonTransaction, Transaction.class);
			if (transaction.getStatus() == null || transaction.getStatus().isEmpty()) {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Status is invalid!");
				return result;
			}
			if (transaction.getEndDateTime() == null || transaction.getEndDateTime().isEmpty()
					|| !DateFormatUtility.isLegalFormat(transaction.getEndDateTime(), "yyyy-MM-dd'T'HH:mm:ss.SS")) {
				result.setNCRWSSResultCode(ResultBase.RES_PROMOTION_DATE_INVALID);
				tp.println("EndDateTime Format is invalid. It must be in " + "yyyy-MM-ddTHH:mm:ss.SS");
				return result;
			}

			synchronized ((Map<String, TerminalItem>) GlobalConstant.getTerminalItemsMap()) {
				Map<String, TerminalItem> terminalItemsHashMap = (Map<String, TerminalItem>) GlobalConstant
						.getTerminalItemsMap();
				TerminalItem terminalItem = TerminalItemsHandler.get(retailStoreId, workStationId, sequenceNumber,
						terminalItemsHashMap);
				if (terminalItem == null) {
					tp.println("No matching transaction!");
					LOGGER.logAlert(PROG_NAME, functionName, Logger.LOG_MSGID, "No matching transaction!\n");
					return result;
				}

				if (terminalItem.getSequenceNumber().equals(sequenceNumber)) {
					boolean isdeleted = TerminalItemsHandler.delete(retailStoreId, workStationId, terminalItemsHashMap);

					if (!isdeleted) {
						result.setNCRWSSResultCode(ResultBase.RES_PROMOTION_ENDTRANSACTION_FAILED);
						tp.println("Transaction is not deleted.");
						return result;
					}
				}
			}
		} catch (IOException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to end transaction for promotion.",
					e);
			result = new ResultBase(ResultBase.RES_ERROR_IOEXCEPTION, ResultBase.RES_OK, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to end transaction for promotion.", e);
			result = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_OK, e);
		} finally {
			tp.methodExit(result);
		}
		return result;
	}

	/**
	 * Item entry for requesting item and promotion information.
	 *
	 * @param retailStoreId
	 *            Store Number where the transaction is coming from
	 * @param workStationId
	 *            Device Number where the transaction is coming from
	 * @param sequenceNumber
	 *            The current transaction sequence number
	 * @param companyId
	 *            The companyId
	 * @param businessDate
	 *            The businessDate
	 * @return {@link Transaction}
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Produces("application/json;charset=UTF-8")
	@Path("/item_entry")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "プロモーション商品読取", response = PromotionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
			@ApiResponse(code = ResultBase.PROMOTION.NO_MATCHING_TRANSACTION, message = "一致する取引無し"),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ") })
	public final PromotionResponse itemEntry(
			@ApiParam(name = "retailstoreid", value = "小売店コード") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "シリアルナンバー") @FormParam("sequencenumber") final String sequenceNumber,
			@ApiParam(name = "transaction", value = "取引情報") @FormParam("transaction") final String transaction,
			@ApiParam(name = "companyId", value = "会社コード") @FormParam("companyId") final String companyId,
			@ApiParam(name = "businessDate", value = "営業日") @FormParam("businessDate") final String businessDate) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreId", retailStoreId).println("WorkstationId", workStationId)
				.println("SequenceNumber", sequenceNumber).println("Transaction", transaction)
				.println("businessDate", businessDate).println("companyId", companyId);
		Transaction transactionOut = new Transaction();
		Sale saleOut = new Sale();
		PromotionResponse response = new PromotionResponse();
		String discounttype = "0";
		boolean twoStep = false;
		String codeTemp = null;
		String codeTempConn = null;
		String dptCode = null;
		String barcode_fst = null;
		String barcode_sec = null;
		String cCode = null;

		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, workStationId, sequenceNumber, transaction, companyId,
					businessDate)) {
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return response;
			}

			synchronized ((Map<String, TerminalItem>) GlobalConstant.getTerminalItemsMap()) {
				Map<String, TerminalItem> terminalItemsHashMap = (Map<String, TerminalItem>) GlobalConstant
						.getTerminalItemsMap();
				TerminalItem terminalItem = TerminalItemsHandler.get(retailStoreId, workStationId, sequenceNumber,
						terminalItemsHashMap);

				if (terminalItem == null) {
					tp.println("Item entry has no matching transaction!");
					LOGGER.logAlert(PROG_NAME, functionName, Logger.LOG_MSGID,
							"Item entry has no matching transaction.\n");
					response.setNCRWSSResultCode(ResultBase.PROMOTION.NO_MATCHING_TRANSACTION);
					return response;
				}

				MixMatchDetailInfo info = new MixMatchDetailInfo();

				// terminalItem.getMixMatch
				JsonMarshaller<Transaction> jsonMarshall = new JsonMarshaller<Transaction>();
				Transaction transactionIn = jsonMarshall.unMarshall(transaction, Transaction.class);
				Sale saleIn = transactionIn.getSale();

				ResultBase rs = SaleItemsHandler.validateSale(saleIn);
				if (ResultBase.RES_OK != rs.getNCRWSSResultCode()) {
					tp.println(rs.getMessage());
					response.setNCRWSSResultCode(rs.getNCRWSSResultCode());
					return response;
				}

				String itemId = saleIn.getItemId();

				DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
				ICodeConvertDAO codeCvtDAO = daoFactory.getCodeConvertDAO();
				IDepartmentDAO idepartmentDAO = daoFactory.getDepartmentDAO();
				IItemDAO dao = new SQLServerItemDAO();
				ViewDepartment departmentInfo = new ViewDepartment();

				// 二段バーコード判断
				if (itemId.contains(" ")) {
					twoStep = Boolean.parseBoolean(ResultBase.TRUE);
					barcode_fst = itemId.split(" ")[0];
					barcode_sec = itemId.split(" ")[1];
				} else {
					twoStep = Boolean.parseBoolean(ResultBase.FALSE);
				}

				// 品目名を取得する
				String varietiesName = BarcodeAssignmentUtility.getBarcodeAssignmentItemId(itemId, barcodeAssignment);

				info.setQuantity(saleIn.getQuantity());
				info.setEntryId(saleIn.getItemEntryId());

				ItemResource itemResource = new ItemResource();
				itemResource.setContext(context);

				// バーコード桁数変換
				String itemIdTemp = null;
				if (twoStep){
					itemIdTemp = barcode_fst;
				} else if (BarcodeAssignmentConstant.VARIETIES_JANBOOKOLD.equals(varietiesName) ||
						BarcodeAssignmentConstant.VARIETIES_FOREIGNBOOKOLD.equals(varietiesName)){
					itemIdTemp = CDCalculation(itemId);
				} else if (BarcodeAssignmentConstant.VARIETIES_JANMAGAZINE.equals(varietiesName)){
					itemIdTemp = itemId.substring(0, 13);
				} else {
					itemIdTemp = itemId;
				}

				SearchedProduct searchedProd = itemResource.getItemByPLUcode(retailStoreId, itemIdTemp, companyId,
						businessDate); // 各種割引情報を含めた商品情報
				Item item = null;
				if (searchedProd.getNCRWSSResultCode() != ResultBase.RES_OK) {
					tp.println("Item was not found!");
					try {
						item = getdetailInfoData(retailStoreId, itemIdTemp, companyId, businessDate);
					} catch (Exception e) {
						LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
								functionName + ": Failed to send item entry.", e);
					}
				} else {
					item = searchedProd.getItem();
				}

				// 部門コードを取得する
				codeTempConn = getDptCode(codeCvtDAO,itemId,varietiesName,companyId,retailStoreId,response,item);

				// 商品マスタから取得失敗の場合
				if (ResultBase.RES_OK != response.getNCRWSSResultCode() || ResultBase.RES_OK != response.getNCRWSSExtendedResultCode()){
					// 各種特売情報を取得する
					item = getRelatedInformation(itemIdTemp, item, retailStoreId, companyId, businessDate, dao);
					if (item != null) {
						item.setItemId(itemIdTemp);
						Sale saleItem = SaleItemsHandler.createSale(item, saleIn);
						if (!StringUtility.isNullOrEmpty(item.getMixMatchCode())) {
							terminalItem.addBmRuleMap(item.getMixMatchCode(), item, saleIn.getItemEntryId());
						}
						transactionOut.setSale(saleItem);
						response.setTransaction(transactionOut);
					}
					return response;
				} else {
					if (codeTempConn.contains(" ")) {
						codeTemp = codeTempConn.split(" ")[0];
						cCode = codeTempConn.split(" ")[1];
					} else {
						codeTemp = codeTempConn;
					}
				}
				String mdName = null;
				Sale saleMdName = null;
				if (searchedProd.getNCRWSSResultCode() != ResultBase.RES_OK && item != null) {
					// サーバーから部門情報を取得する
					departmentInfo = getDptInfoData(companyId, retailStoreId, codeTemp, retailStoreId);
					saleMdName = getMdName(companyId, retailStoreId, itemIdTemp);
					mdName = saleMdName.getMdNameLocal();
				} else {
					// ローカルから部門情報を取得する
					departmentInfo = idepartmentDAO.selectDepartmentDetail(companyId, retailStoreId, codeTemp, retailStoreId);
					saleMdName = dao.getItemNameFromPluName(companyId, retailStoreId, itemIdTemp);
					mdName = saleMdName.getMdNameLocal();
					if (departmentInfo.getNCRWSSResultCode() != ResultBase.RES_OK && item == null) {
						tp.println("departmentInfo was not found!");
						try {
							// サーバーから部門情報を取得する
							departmentInfo = getDptInfoData(companyId, retailStoreId, codeTemp, retailStoreId);
							saleMdName = getMdName(companyId, retailStoreId, itemIdTemp);
							mdName = saleMdName.getMdNameLocal();
						} catch (Exception e) {
							LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
									functionName + ": Failed to send item entry.", e);
						}
					}
				}

				// 部門コードを部門マスタテーブルに存在チェック
				if (departmentInfo == null) {
					dptCode = null;
				} else {
					dptCode = (departmentInfo.getDepartment() == null) ? null : departmentInfo.getDepartment().getDepartmentID();
				}

				if (StringUtility.isNullOrEmpty(dptCode)) {
					response.setNCRWSSResultCode(ResultBase.RES_ERROR_DPTNOTFOUND);
					response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DPTNOTFOUND);
					response.setMessage("The dpt code doesn't exist in the MST_DPTINFO.");
					tp.println("The dpt code doesn't exist in the MST_DPTINFO.");
					return response;
				} else {
					if (item == null) {
						// 部門情報を戻る
						String dptName = departmentInfo.getDepartment().getDepartmentName().getJa();
						String taxType = departmentInfo.getDepartment().getTaxType();
						if (StringUtility.isNullOrEmpty(mdName)) {
							saleOut.setMdNameLocal(dptName);
						} else {
							saleOut.setMdNameLocal(mdName);
						}
						saleOut.setTaxType(Integer.parseInt(taxType));
						saleOut.setTaxTypeSource("2");
						saleOut.setMd11(departmentInfo.getDepartment().getSubNum1());
						saleOut.setMd12(departmentInfo.getDepartment().getSubNum2());
						saleOut.setMd13(departmentInfo.getDepartment().getSubNum3());
						saleOut.setDptSubCode1(departmentInfo.getDepartment().getSubCode1());
						saleOut.setDptSubNum1(departmentInfo.getDepartment().getSubNum1());
						saleOut.setDptSubNum2(departmentInfo.getDepartment().getSubNum2());
						saleOut.setDptSubNum3(departmentInfo.getDepartment().getSubNum3());
						saleOut.setDptSubNum4(departmentInfo.getDepartment().getSubNum4());
						
						// 各種特売情報を取得する
						item = getRelatedInformation(itemIdTemp, item, retailStoreId, companyId, businessDate, dao);
						
						if (item != null) {
							if (item.getDiscountClass() != 0) {
								saleOut.setDiscountClass(item.getDiscountClass());
							} else {
								if (!StringUtility.isNullOrEmpty(departmentInfo.getDiscountClass())) {
									saleOut.setDiscountClass(Integer.parseInt(departmentInfo.getDiscountClass()));
								}
							}
							if (item.getDiscountAmt() != 0) {
								saleOut.setDiscountAmt(item.getDiscountAmt());
							} else {
								if (departmentInfo.getDiscountAmt() != null) {
									saleOut.setDiscountAmt(departmentInfo.getDiscountAmt().intValue());
								}
							}
							if (item.getDiacountRate() != 0) {
								saleOut.setDiacountRate(item.getDiacountRate());
							} else {
								if (departmentInfo.getDiscountRate() != null) {
									saleOut.setDiacountRate(departmentInfo.getDiscountRate());
								}
							}
							if (item.getPromotionNo() != null) {
								saleOut.setPromotionNo(item.getPromotionNo());
							} else {
								saleOut.setPromotionNo(departmentInfo.getPromotionNo());
							}
							if (item.getPromotionType() != null) {
								saleOut.setPromotionType(item.getPromotionType());
							} else {
								saleOut.setPromotionType(departmentInfo.getPromotionType());
							}
							
							// バンドルミックス(PRICE_MM_INFO mixmatch)
							saleOut.setMixMatchCode(item.getMixMatchCode());
							saleOut.setRuleQuantity1(item.getRuleQuantity1());
							saleOut.setRuleQuantity2(item.getRuleQuantity2());
							saleOut.setRuleQuantity3(item.getRuleQuantity3());
							saleOut.setConditionPrice3(item.getConditionPrice3());
							saleOut.setConditionPrice2(item.getConditionPrice2());
							saleOut.setConditionPrice1(item.getConditionPrice1());
							saleOut.setDecisionPrice1(item.getDecisionPrice1());
							saleOut.setDecisionPrice2(item.getDecisionPrice2());
							saleOut.setDecisionPrice3(item.getDecisionPrice3());
							saleOut.setAveragePrice1(item.getAveragePrice1());
							saleOut.setAveragePrice2(item.getAveragePrice2());
							saleOut.setAveragePrice3(item.getAveragePrice3());
							saleOut.setNote(item.getNote());
							saleOut.setSku(item.getSku());
							if (!StringUtility.isNullOrEmpty(item.getMixMatchCode())) {
								item.setItemId(itemIdTemp);
								terminalItem.addBmRuleMap(item.getMixMatchCode(), item, saleIn.getItemEntryId());
							}
						} else {
							if (!StringUtility.isNullOrEmpty(departmentInfo.getDiscountClass())) {
								saleOut.setDiscountClass(Integer.parseInt(departmentInfo.getDiscountClass()));
							}
							if (departmentInfo.getDiscountAmt() != null) {
								saleOut.setDiscountAmt(departmentInfo.getDiscountAmt().intValue());
							} 
							if (departmentInfo.getDiscountRate() != null) {
								saleOut.setDiacountRate(departmentInfo.getDiscountRate());
							}
							saleOut.setPromotionNo(departmentInfo.getPromotionNo());
							saleOut.setPromotionType(departmentInfo.getPromotionType());
						}
						
						String taxRate = departmentInfo.getDepartment().getTaxRate();
						saleOut.setTaxRate(taxRate == "null" || taxRate == null ? 0 : Integer.parseInt(taxRate));

						saleOut.setNonSales(departmentInfo.getDepartment().getNonSales());

						saleOut.setDiscountType(departmentInfo.getDepartment().getDiscountType());
						saleOut.setDiscountTypeSource("2");
						saleOut.setItemId(itemIdTemp);
						saleOut.setDepartment(dptCode);

						if (!StringUtility.isNullOrEmpty(cCode)) {
							if (cCode.length() == 4) {
								saleOut.setCategoryCode(cCode);
							} else {
								saleOut.setMagazineCode(cCode);
							}
						}

						Double barCodePrice = null;
						barCodePrice = barCodePriceCalculation(varietiesName, itemId);
						if (twoStep && barcode_sec.length() == 4) {
							saleOut.setLabelPrice(barCodePrice);
							response.setNCRWSSExtendedResultCode(ResultBase.PRICE_INPUT_REQUEST);
						} else {
							// バーコード価格を使用
							if (barCodePrice != null) {
								if ("1".equals(taxType)) {
									barCodePrice = (double) Math.round(barCodePrice * 1.08);
								}
								saleOut.setLabelPrice(barCodePrice);
								saleOut.setRegularSalesUnitPrice(barCodePrice);
								saleOut.setActualSalesUnitPrice(barCodePrice);
							}
						}

						transactionOut.setSale(saleOut);
						response.setDepartmentName(dptName);
						response.setTransaction(transactionOut);
						return response;
					}
				}

				// 各種特売情報を取得する
				item = getRelatedInformation(itemIdTemp, item, retailStoreId, companyId, businessDate, dao);
				
				info.setTruePrice(item.getRegularSalesUnitPrice());
				Promotion promotion = new Promotion();
				promotion.setCouponInfoList(makeCouponInfoList(terminalItem.getCouponInfoMap(item)));
//				promotion.setQrCodeInfoList(terminalItem.getQrCodeInfoList(item));
				if (!StringUtility.isNullOrEmpty(item.getMixMatchCode())) {
					terminalItem.addBmRuleMap(item.getMixMatchCode(), item, saleIn.getItemEntryId());
					if (!"false".equals(transactionIn.getEntryFlag())) {
						terminalItem.setBmDetailMap(item.getMixMatchCode(), info, false);
						Map<String, Map<String, Object>> map = terminalItem.getMixMatchMap(item.getMixMatchCode(), "");
						// promotion.setMap(map);
						if(terminalItem.isDeleteBm(map)){
							Map<String, Map<String, Object>> newMap = new HashMap<String, Map<String, Object>>();
							Map<String, Object> childMap = new HashMap<String, Object>();
							childMap.put("hasMixMatch", "false");
							newMap.put(item.getMixMatchCode(), childMap);
							promotion.setMap(newMap);
						} else {
							promotion.setMap(map);
						}
					}
				}

				discounttype = item.getDiscountType();
				Sale saleItem = SaleItemsHandler.createSale(item, saleIn);

				if (saleItem.isPriceOverride()) {
					saleItem.setActualSalesUnitPrice(saleIn.getActualSalesUnitPrice());
					double price = saleIn.getActualSalesUnitPrice() * saleIn.getQuantity();
					saleItem.setExtendedAmount(price);
					saleItem.setDiscount(0);
					saleItem.setDiscountAmount(0);
				}

				Double barCodePrice = null;
				barCodePrice = barCodePriceCalculation(varietiesName, itemId);
				if (barCodePrice != null) {
					saleItem.setLabelPrice(barCodePrice);
				}

				saleItem.setTaxTypeSource("1");
				if (saleItem.getDiscountClass() == 0 && !StringUtility.isNullOrEmpty(departmentInfo.getDiscountClass())) {
					saleOut.setDiscountClass(Integer.parseInt(departmentInfo.getDiscountClass()));
				}
				if (saleItem.getDiscountAmt() == 0 && departmentInfo.getDiscountAmt() != null) {
					saleOut.setDiscountAmt(departmentInfo.getDiscountAmt().intValue());
				} 
				if (saleItem.getDiacountRate() == 0 && departmentInfo.getDiscountRate() != null) {
					saleOut.setDiacountRate(departmentInfo.getDiscountRate());
				}
				if (saleItem.getPromotionNo() == null) {
					saleOut.setPromotionNo(departmentInfo.getPromotionNo());
				}
				if (saleItem.getPromotionType() == null) {
					saleOut.setPromotionType(departmentInfo.getPromotionType());
				}
				
				// バーコード価格を使用
				if (saleItem.getRegularSalesUnitPrice() == 0.0) {
					String taxType = departmentInfo.getDepartment().getTaxType();
					saleItem.setTaxType(Integer.parseInt(taxType));
					saleItem.setTaxTypeSource("2");
					if (barCodePrice != null) {
						if ("1".equals(taxType)) {
							barCodePrice = (double) Math.round(barCodePrice * 1.08);
						}
						saleItem.setRegularSalesUnitPrice(barCodePrice);
						saleItem.setActualSalesUnitPrice(barCodePrice);
					}
				}

				if (!StringUtility.isNullOrEmpty(cCode)) {
					if (cCode.length() == 4) {
						saleItem.setCategoryCode(cCode);
					} else {
						saleItem.setMagazineCode(cCode);
					}
				}

				saleItem.setDiscountTypeSource("1");
				if (discounttype == null) {
					discounttype = departmentInfo.getDepartment().getDiscountType();
					saleItem.setDiscountTypeSource("2");
				}
				saleItem.setDiscountType(discounttype);
				boolean flag = ("0".equals(discounttype));
				saleItem.setDiscountable(flag);
				response.setPromotion(promotion);
				transactionOut.setSale(saleItem);
				response.setTransaction(transactionOut);
			}
		} catch (JsonParseException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to send item entry.", e);
			response = new PromotionResponse(ResultBase.RES_ERROR_INVALIDPARAMETER,
					ResultBase.RES_ERROR_INVALIDPARAMETER, e);
		} catch (IOException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to send item entry.", e);
			response = new PromotionResponse(ResultBase.RES_ERROR_IOEXCEPTION, ResultBase.RES_ERROR_IOEXCEPTION, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send item entry.", e);
			if ("0".equals(e.getMessage())) {
				response = new PromotionResponse(1111, 1111, e);
			} else {
				response = new PromotionResponse(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, e);
			}
		} finally {
			tp.methodExit(response);
		}
		return response;
	}

	private List<CouponInfo> makeCouponInfoList(Map<String, CouponInfo> map) {
		List<CouponInfo> list = new ArrayList<CouponInfo>();
		for (Map.Entry<String, CouponInfo> map1 : map.entrySet()) {
			list.add(map1.getValue());
		}
		return list;
	}

	/**
	 * get Related Information
	 *
	 * @param item
	 * @param retailStoreId
	 * @param companyId
	 * @param businessDate
	 * @param dao
	 * @throws DaoException 
	 */
	private Item getRelatedInformation(String itemId, Item item , String retailStoreId, String companyId, String businessDate, IItemDAO dao) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("itemId", itemId).println("item", item).println("retailStoreId", retailStoreId)
			.println("companyId", companyId).println("businessDate", businessDate).println("dao", dao);

		ItemResource itemResource = new ItemResource();
		PricePromInfo pricePromInfo;
		PriceMMInfo priceMMInfo;
		if (item == null) {
			pricePromInfo = itemResource.getPricePromInfo(itemId, null, null);
			priceMMInfo = itemResource.getPriceMMInfo(itemId);
			if (pricePromInfo != null) {
				item = new Item();
			}
		} else {
			priceMMInfo = itemResource.getPriceMMInfo(item.getSku());
			pricePromInfo = itemResource.getPricePromInfo(item.getSku(), item.getDepartment(), item.getLine());
		}
		
		// 特売管理(PROM_INFO 自動割引)
		if (!dao.isHasPromDetailInfoList(pricePromInfo, item)) {
			if (item == null && priceMMInfo != null) {
				item = new Item();
			}
			// バンドルミックス(PRICE_MM_INFO mixmatch)
			if (!dao.isHasPriceMMInfoList(priceMMInfo, item)) {
				if (item != null) {
					// 買替サポート（REPLACESUPPORT_INFO）
					dao.isHasReplaceSupportDetailInfo(retailStoreId, item, companyId, businessDate);
				}
			}
		}
		if (item != null) {
			// 割引券発行管理（COUPON_INFO）
			dao.getCouponInfo(retailStoreId, item, companyId, businessDate);
			// プレミアム商品（PREMIUMITEM_INFO）
			dao.getPremiumitemInfo(retailStoreId, item, companyId, businessDate);
		}
		
		return item;
	}
	
	/**
	 * 部門コードを取得する
	 *
	 * @param codeCvtDAO
	 * @param dao
	 * @param itemId
	 * @param varietiesName
	 * @param companyId
	 * @param operatorType
	 * @param response
	 * @return codeTemp
	 * @throws DaoException
	 */
	private String getDptCode(ICodeConvertDAO codeCvtDAO,String itemId,String varietiesName,
			String companyId,String StoreId,PromotionResponse response,Item item) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("codeCvtDAO", codeCvtDAO).println("itemId", itemId)
			.println("varietiesName", varietiesName).println("companyId", companyId).println("StoreId", StoreId)
			.println("response", response).println("item", item);

		String codeTemp = null;
		String dpt = null;
		String barcode_sec = null;
		String cCode = "";

		// 二段バーコード判断
		if (itemId.contains(" ")) {
			barcode_sec = itemId.split(" ")[1];
		}

		dpt = (item == null) ? null : item.getDepartment();

		// 部門コードを取得する
		switch (varietiesName) {
		case BarcodeAssignmentConstant.VARIETIES_JANBOOK:
			if (barcode_sec.length() > 7) {
				cCode = barcode_sec.substring(3, 7);
			} else {
				cCode = barcode_sec;
			}
			if(dpt == null){
				codeTemp = codeCvtDAO.convertCCodeToDpt(companyId, cCode, StoreId);
				codeTemp = codeTemp + " " + cCode;
			} else if(StringUtility.isEmpty(dpt)){
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Dpt is Empty in the PLU.");
				tp.println("Dpt is Empty in the PLU.");
			} else {
				codeTemp = dpt + " " + cCode;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_JANBOOKOLD:
			cCode = itemId.substring(10, 14);
			if(dpt == null){
				codeTemp = codeCvtDAO.convertCCodeToDpt(companyId, cCode, StoreId);
				codeTemp = codeTemp + " " + cCode;
			} else if(StringUtility.isEmpty(dpt)){
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Dpt is Empty in the PLU.");
				tp.println("Dpt is Empty in the PLU.");
			} else {
				codeTemp = dpt + " " + cCode;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_JANMAGAZINE:
			String magCode = itemId.substring(0, 5);
			if(dpt == null){
				codeTemp = codeCvtDAO.convertMagCodeToDpt(companyId, magCode, StoreId);
				codeTemp = codeTemp + " " + magCode;
			} else if(StringUtility.isEmpty(dpt)){
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Dpt is Empty in the PLU.");
				tp.println("Dpt is Empty in the PLU.");
			} else {
				codeTemp = dpt + " " + magCode;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_FOREIGNBOOK:
			if (barcode_sec.length() > 7) {
				cCode = barcode_sec.substring(3, 7);
			} else {
				cCode = barcode_sec;
			}
			if(dpt == null){
				codeTemp = JaCodeCvt(cCode);
				codeTemp = codeTemp + " " + cCode;
			} else if(StringUtility.isEmpty(dpt)){
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Dpt is Empty in the PLU.");
				tp.println("Dpt is Empty in the PLU.");
			} else {
				codeTemp = dpt + " " + cCode;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_FOREIGNBOOKOLD:
			cCode = itemId.substring(10, 14);
			if(dpt == null){
				codeTemp = JaCodeCvt(cCode);
				codeTemp = codeTemp + " " + cCode;
			} else if(StringUtility.isEmpty(dpt)){
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Dpt is Empty in the PLU.");
				tp.println("Dpt is Empty in the PLU.");
			} else {
				codeTemp = dpt + " " + cCode;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_FOREIGNJANBOOK:
			if (StringUtility.isNullOrEmpty(dpt)) {
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Not found in the PLU.");
				tp.println("Not found in the PLU.");
			} else {
				codeTemp = dpt;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_FOREIGNMAGAZINE:
			if(dpt == null){
				codeTemp = "0161";
			} else if(StringUtility.isEmpty(dpt)){
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Dpt is Empty in the PLU.");
				tp.println("Dpt is Empty in the PLU.");
			} else {
				codeTemp = dpt;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_KINOKUNIYA:
			if (StringUtility.isNullOrEmpty(dpt)) {
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Not found in the PLU.");
				tp.println("Not found in the PLU.");
			} else {
				codeTemp = dpt;
			}
			break;
		case BarcodeAssignmentConstant.VARIETIES_JANSALES:
		case BarcodeAssignmentConstant.VARIETIES_JANMAGAZINEOLD1:
		case BarcodeAssignmentConstant.VARIETIES_JANMAGAZINEOLD2:
			if (StringUtility.isNullOrEmpty(dpt) || !(item.getRegularSalesUnitPrice() > 0.0)) {
				response.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
				response.setMessage("Not found in the PLU.");
				tp.println("Not found in the PLU.");
			} else {
				codeTemp = dpt;
			}
			break;
		default:
			break;
		}

		tp.methodExit(codeTemp);
		return codeTemp;
	}

	/**
	 * バーコード価格を使用
	 *
	 * @param varietiesName
	 * @param itemId
	 * @return commodityPrice
	 */
	private Double barCodePriceCalculation(String varietiesName, String itemId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("varietiesName", varietiesName).println("itemId", itemId);

		Double commodityPrice = null;
		switch (varietiesName) {
		case BarcodeAssignmentConstant.VARIETIES_JANBOOK:
			commodityPrice = janBook(itemId);
			break;
		case BarcodeAssignmentConstant.VARIETIES_FOREIGNBOOK:
			commodityPrice = foreignBook(itemId);
			break;
		case BarcodeAssignmentConstant.VARIETIES_JANMAGAZINE:
			commodityPrice = janMagazine(itemId);
			break;
		case BarcodeAssignmentConstant.VARIETIES_FOREIGNMAGAZINE:
			commodityPrice = foreignMagazine(itemId);
			break;
		case BarcodeAssignmentConstant.VARIETIES_JANBOOKOLD:
		case BarcodeAssignmentConstant.VARIETIES_FOREIGNBOOKOLD:
			commodityPrice = Double.parseDouble("0");
			break;
		default:
			break;
		}

		tp.methodExit(commodityPrice);
		return commodityPrice;
	}

	/**
	 * C/D を算出する
	 *
	 * @param itemId
	 * @return itemId
	 */
	private String CDCalculation(String itemId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("itemId", itemId);

		char cd = CheckDigitValue.generate("978" + itemId.substring(0, 9), 12, "131313131313", 10,
				CheckDigitValue.SKIP_0_OK, true);
		itemId = "978" + itemId.substring(0, 9) + cd;

		tp.methodExit(itemId);
		return itemId;
	}

	/**
	 * 部門変換する
	 *
	 * @param code
	 * @return code
	 */
	private String JaCodeCvt(String code) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("code", code);

		if ("0000".equals(code)){
			code = "0183";
		} else if (!"040".equals(code.substring(0, 3))){
			code = "016" + code.substring(3, 4);
		}

		tp.methodExit(code);
		return code;
	}

	/**
	 * 洋雑誌価格計算
	 *
	 * @param itemId
	 * @return price
	 */
	private Double foreignMagazine(String itemId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("itemId", itemId);

		Double price = Double.parseDouble(itemId.substring(9, 12)) * 10;

		tp.methodExit(price);
		return price;
	}

	/**
	 * 和雑誌価格計算
	 *
	 * @param itemId
	 * @return price
	 */
	private Double janMagazine(String itemId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("itemId", itemId);
		if (itemId.length() < 18) {
			return Double.parseDouble("0");
		}
		String endFour = itemId.substring(itemId.length() - 4);
		Double price = Double.parseDouble(endFour);

		tp.methodExit(price);
		return price;
	}

	/**
	 * 洋書価格計算
	 *
	 * @param itemId
	 * @return price
	 */
	private Double foreignBook(String itemId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("itemId", itemId);

		String[] item = itemId.split(" ");
		Double price = null;
		if (item[1].length() == 13) {
			price = Double.parseDouble(item[1].substring(7, 12));
		}else{
			price = Double.parseDouble("0");
		}

		tp.methodExit(price);
		return price;
	}

	/**
	 * 和書価格計算
	 *
	 * @param itemId
	 * @return price
	 */
	private Double janBook(String itemId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("itemId", itemId);

		String[] item = itemId.split(" ");
		String topThree = item[1].substring(0, 3);
		Double price = null;
		if ("191".equals(topThree) && item[1].length() == 13) {
			price = (double) Math.round(Double.parseDouble(item[1].substring(7, 12)) * 100 / 103);
		} else if ("192".equals(topThree) && item[1].length() == 13) {
			price = Double.parseDouble(item[1].substring(7, 12));
		}else{
			price = Double.parseDouble("0");
		}

		tp.methodExit(price);
		return price;
	}

	/**
	 * Get The MixMatch by Sku
	 *
	 * @param retailStoreId
	 *            Store Number where the transaction is coming from
	 * @param workStationId
	 *            Device Number where the transaction is coming from
	 * @param sequenceNumber
	 *            The current transaction sequence number
	 * @param companyId
	 *            The companyId
	 * @param businessDate
	 *            The businessDate
	 * @return {@link Transaction}
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Produces("application/json;charset=UTF-8")
	@Path("/item_mixMatchInfo")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "ミックスマッチ商品情報取得", response = PromotionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
			@ApiResponse(code = ResultBase.RES_ERROR_DAO, message = "DAOエラー"),
			@ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
			@ApiResponse(code = ResultBase.PROMOTION.NO_MATCHING_TRANSACTION, message = "一致する取引無し"),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ") })
	public final PromotionResponse itemMixMatchInfobySku(
			@ApiParam(name = "retailstoreid", value = "小売店コード") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "シリアルナンバー") @FormParam("sequencenumber") final String sequenceNumber,
			@ApiParam(name = "transaction", value = "業務") @FormParam("transaction") final String transaction,
			@ApiParam(name = "companyId", value = "会社コード") @FormParam("companyId") final String companyId,
			@ApiParam(name = "businessDate", value = "営業日") @FormParam("businessDate") final String businessDate) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreId", retailStoreId).println("WorkstationId", workStationId)
				.println("SequenceNumber", sequenceNumber).println("Transaction", transaction)
				.println("businessDate", businessDate).println("companyId", companyId).println("requestFrom");
		PromotionResponse response = new PromotionResponse();
		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, workStationId, sequenceNumber, transaction, companyId,
					businessDate)) {
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return response;
			}

			synchronized ((Map<String, TerminalItem>) GlobalConstant.getTerminalItemsMap()) {
				Map<String, TerminalItem> terminalItemsHashMap = (Map<String, TerminalItem>) GlobalConstant
						.getTerminalItemsMap();
				TerminalItem terminalItem = TerminalItemsHandler.get(retailStoreId, workStationId, sequenceNumber,
						terminalItemsHashMap);

				if (terminalItem == null) {
					tp.println("Item entry has no matching transaction!");
					LOGGER.logAlert(PROG_NAME, functionName, Logger.LOG_MSGID,
							"Item entry has no matching transaction.\n");
					response.setNCRWSSResultCode(ResultBase.PROMOTION.NO_MATCHING_TRANSACTION);
					return response;
				}

				JsonMarshaller<Transaction> jsonMarshall = new JsonMarshaller<Transaction>();
				Transaction transactionIn = jsonMarshall.unMarshall(transaction, Transaction.class);
				List<Sale> sales = transactionIn.getSales();
				for (Sale saleIn : sales) {
					MixMatchDetailInfo info = new MixMatchDetailInfo();
					if (null != saleIn) {
						if (saleIn.getQuantity() < 0 && StringUtility.isNullOrEmpty(saleIn.getItemEntryId())) {
							tp.println(" The quantity or The ItemEntryId is invalid!");
							response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
							return response;
						}
					} else {
						tp.println("Transaction has no sale data!");
						response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
						return response;
					}
					info.setQuantity(saleIn.getQuantity());
					info.setEntryId(saleIn.getItemEntryId());
					info.setTruePrice(saleIn.getActualSalesUnitPrice());

					IItemDAO dao = new SQLServerItemDAO();
					Item item = dao.getMixMatchInfo(retailStoreId, saleIn.getSku(), companyId, businessDate);
					if (null != item && !StringUtility.isNullOrEmpty(item.getMixMatchCode())) {
						info.setMmNo(item.getMixMatchCode());
						terminalItem.addBmRuleMap(item.getMixMatchCode(), item, saleIn.getItemEntryId());
						terminalItem.setBmDetailMap(item.getMixMatchCode(), info, true);
					}
				}
				Map<String, Map<String, Object>> map = terminalItem.getMixMatchMap();
				Promotion promotion = new Promotion();
				promotion.setMap(terminalItem.getTheNewMap(map));
				// promotion.setMap(map);
				response.setPromotion(promotion);
			}
		} catch (JsonParseException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to get mixMatch.", e);
			response = new PromotionResponse(ResultBase.RES_ERROR_INVALIDPARAMETER,
					ResultBase.RES_ERROR_INVALIDPARAMETER, e);
		} catch (IOException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to get mixMatch.", e);
			response = new PromotionResponse(ResultBase.RES_ERROR_IOEXCEPTION, ResultBase.RES_ERROR_IOEXCEPTION, e);
		} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get mixMatch.", e);
			if (e.getCause() instanceof SQLException) {
				response = new PromotionResponse(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, e);
			} else {
				response = new PromotionResponse(ResultBase.RES_ERROR_DAO, ResultBase.RES_ERROR_DAO, e);
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get mixMatch.", e);
			response = new PromotionResponse(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, e);
		} finally {
			tp.methodExit(response);
		}
		return response;
	}

    /**
     * get remote serverInfo
     *
     * @param companyId
     * @param retailStoreId
     * @param itemCode
     * @return
     * @throws Exception
     */
    private final Sale getMdName(String companyId, String retailStoreId, String itemCode)
            throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("RetailStoreId", retailStoreId)
                .println("ItemCode", itemCode);
        JSONObject result = null;
        Sale saleMdName = new Sale();
        try {
            JSONObject valueResult = new JSONObject();
            valueResult.put("companyId", companyId);
            valueResult.put("retailStoreId", retailStoreId);
            valueResult.put("ItemCode", itemCode);
            int timeOut = 5;
            String enterpriseServerTimeout = GlobalConstant.getEnterpriseServerTimeout();
            if (!StringUtility.isNullOrEmpty(enterpriseServerTimeout)) {
                timeOut = Integer.valueOf(enterpriseServerTimeout.toString());
            }
            String endStr = GlobalConstant.getEnterpriseServerUri().substring(GlobalConstant.getEnterpriseServerUri().length() - 1);
            String url = "";
            if ("/".equals(endStr)) {
                url = GlobalConstant.getEnterpriseServerUri() + ENTERPRISE_MDNAME_UTL;
            } else {
                url = GlobalConstant.getEnterpriseServerUri() + '/' + ENTERPRISE_MDNAME_UTL;
            }

            result = UrlConnectionHelper.connectionHttpsForGet(getUrl(url, valueResult), timeOut);
            // Check if error is empty.
            if (result != null) {
            	saleMdName = (Sale) jsonToMdName(result.getJSONObject("transaction").getJSONObject("sale"));
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send remote getMdName.",
                    e);
            throw new Exception();
        } finally {
            tp.methodExit(saleMdName);
        }
        return saleMdName;
    }

	/**
	 * get remote serverInfo
	 *
	 * @param companyId
	 * @param retailStoreId
	 * @param codeTemp
	 * @param retailStoreId
	 * @return
	 * @throws Exception
	 */
	private final ViewDepartment getDptInfoData(String companyId, String retailStoreId, String codeTemp, String searchRetailStoreID)
			throws Exception {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("CompanyId", companyId).println("RetailStoreId", retailStoreId)
				.println("CodeTemp", codeTemp).println("SearchRetailStoreID", searchRetailStoreID);
		JSONObject result = null;
		ViewDepartment departmentInfo = null;
		try {
			JSONObject valueResult = new JSONObject();
			valueResult.put("companyId", companyId);
			valueResult.put("retailStoreId", retailStoreId);
			valueResult.put("codeTemp", codeTemp);
			valueResult.put("searchRetailStoreID", searchRetailStoreID);
			int timeOut = 5;
			String enterpriseServerTimeout = GlobalConstant.getEnterpriseServerTimeout();
			if (!StringUtility.isNullOrEmpty(enterpriseServerTimeout)) {
				timeOut = Integer.valueOf(enterpriseServerTimeout.toString());
			}
			String endStr = GlobalConstant.getEnterpriseServerUri().substring(GlobalConstant.getEnterpriseServerUri().length() - 1);
			String url = "";
			if ("/".equals(endStr)) {
				url = GlobalConstant.getEnterpriseServerUri() + ENTERPRISE_DPT_UTL;
			} else {
				url = GlobalConstant.getEnterpriseServerUri() + '/' + ENTERPRISE_DPT_UTL;
			}

			result = UrlConnectionHelper.connectionHttpsForGet(getUrl(url, valueResult), timeOut);
			// Check if error is empty.
			if (result != null && result.getInt("ncrwssresultCode") == ResultBase.RES_OK) {
				departmentInfo = (ViewDepartment) jsonToDeparment(result.getJSONObject("department"));
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send remote item entry.",
					e);
			throw new Exception();
		} finally {
			tp.methodExit(departmentInfo);
		}
		return departmentInfo;
	}

	/**
	 * get remote serverInfo
	 *
	 * @param retailStoreId
	 * @param pluCode
	 * @param companyId
	 * @param businessDate
	 * @return
	 * @throws Exception
	 */
	private final Item getdetailInfoData(String retailStoreId, String pluCode, String companyId, String businessDate)
			throws Exception {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreId", retailStoreId).println("pluCode", pluCode)
				.println("businessDate", businessDate).println("companyId", companyId);
		JSONObject result = null;
		Sale sale = null;
		try {
			JSONObject valueResult = new JSONObject();
			valueResult.put("storeId", retailStoreId);
			valueResult.put("pluCode", pluCode);
			valueResult.put("companyId", companyId);
			valueResult.put("businessDate", businessDate);
			int timeOut = 5;
			String enterpriseServerTimeout = GlobalConstant.getEnterpriseServerTimeout();
			if (!StringUtility.isNullOrEmpty(enterpriseServerTimeout)) {
				timeOut = Integer.valueOf(enterpriseServerTimeout.toString());
			}
			String endStr = GlobalConstant.getEnterpriseServerUri().substring(GlobalConstant.getEnterpriseServerUri().length() - 1);
			String url = "";
			if ("/".equals(endStr)) {
				url = GlobalConstant.getEnterpriseServerUri() + REMOTE_UTL;
			} else {
				url = GlobalConstant.getEnterpriseServerUri() + '/' + REMOTE_UTL;
			}

			result = UrlConnectionHelper.connectionHttpsForGet(getUrl(url, valueResult), timeOut);
			// Check if error is empty.
			if (result != null) {
				sale = (Sale) jsonToItem(result.getJSONObject("transaction").getJSONObject("sale"));
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send remote item entry.",
					e);
			throw new Exception();
		} finally {
			tp.methodExit(sale);
		}
		return sale;
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
	 * JSONObject to Sale
	 *
	 * @param json
	 *            The data of the remote return
	 * @return Sale
	 * @throws NumberFormatException
	 *             The Number of The Format Exception
	 * @throws JSONException
	 *             The Json Exception
	 */
	private Sale jsonToMdName(JSONObject json) throws NumberFormatException, JSONException {
		Sale mdName = new Sale();
		mdName.setMdNameLocal(StringUtility.convNullStringToNull(json.getString("mdNameLocal")));
		return mdName;
	}

	/**
	 * JSONObject to Deparment
	 *
	 * @param json
	 *            The data of the remote return
	 * @return item
	 * @throws NumberFormatException
	 *             The Number of The Format Exception
	 * @throws JSONException
	 *             The Json Exception
	 */
	private ViewDepartment jsonToDeparment(JSONObject json) throws NumberFormatException, JSONException {
		ViewDepartment departmentInfo = new ViewDepartment();
		Department dpt = new Department();
		dpt.setDepartmentID(StringUtility.convNullStringToNull(json.getString("departmentID")));

		// department name
		DepartmentName departmentName = new DepartmentName();
		departmentName.setEn(StringUtility.convNullStringToNull(json.getJSONObject("departmentName").getString("en")));
		departmentName.setJa(StringUtility.convNullStringToNull(json.getJSONObject("departmentName").getString("ja")));
		dpt.setDepartmentName(departmentName);
		dpt.setTaxRate(StringUtility.convNullStringToNull(json.getString("taxRate")));
		dpt.setTaxType(StringUtility.convNullStringToNull(json.getString("taxType")));
		dpt.setDiscountType(StringUtility.convNullStringToNull(json.getString("discountType")));
		dpt.setNonSales(json.getInt("nonSales"));
		dpt.setSubCode1(StringUtility.convNullStringToNull(json.getString("subCode1")));
		dpt.setSubNum1(StringUtility.convNullStringToNull(json.getString("subNum1")));
		dpt.setSubNum2(StringUtility.convNullStringToNull(json.getString("subNum2")));
		dpt.setSubNum3(StringUtility.convNullStringToNull(json.getString("subNum3")));
		dpt.setSubNum4(StringUtility.convNullStringToNull(json.getString("subNum4")));
		departmentInfo.setDepartment(dpt);
		departmentInfo.setRetailStoreID(StringUtility.convNullStringToNull(json.getString("retailStoreID")));
		return departmentInfo;
	}

	/**
	 * JSONObject to item
	 *
	 * @param json
	 *            The data of the remote return
	 * @return item
	 * @throws NumberFormatException
	 *             The Number of The Format Exception
	 * @throws JSONException
	 *             The Json Exception
	 */
	private Item jsonToItem(JSONObject json) throws NumberFormatException, JSONException {
		Sale item = new Sale();
		item.setActualSalesUnitPrice(json.getDouble("actualSalesUnitPrice"));
		item.setItemId(StringUtility.convNullStringToNull(json.getString("itemId")));
		Description description = new Description();
		description.setEn(StringUtility.convNullStringToNull(json.getJSONObject("description").getString("en")));
		description.setJa(StringUtility.convNullStringToNull(json.getJSONObject("description").getString("ja")));
		item.setDescription(description);
		item.setRegularSalesUnitPrice(Double.parseDouble(StringUtility.convNullStringToNull(json.getString("regularSalesUnitPrice"))));
		item.setDiscount(Double.parseDouble(StringUtility.convNullStringToNull(json.getString("discount"))));
		item.setDiscountFlag(Integer.parseInt(StringUtility.convNullStringToNull(json.getString("discountFlag"))));
		item.setDiscountAmount(Double.parseDouble(StringUtility.convNullStringToNull(json.getString("discountAmount"))));
		item.setDepartment(StringUtility.convNullStringToNull(json.getString("department")));
		item.setDiscountable(json.getBoolean("discountable"));
		item.setTaxRate(json.getInt("taxRate"));
		item.setTaxType(json.getInt("taxType"));
		item.setDiscountType(StringUtility.convNullStringToNull(json.getString("discountType")));
		item.setSubNum1(json.getInt("subNum1"));
		item.setDptDiscountType(json.getInt("dptDiscountType"));
		item.setNonSales(json.getInt("nonSales"));
		item.setSubInt10(json.getInt("subInt10"));
		item.setLine(StringUtility.convNullStringToNull(json.getString("line")));
		item.setCompanyId(StringUtility.convNullStringToNull(json.getString("companyId")));
		item.setPromotionNo(StringUtility.convNullStringToNull(json.getString("promotionNo")));
		item.setMdType(StringUtility.convNullStringToNull(json.getString("mdType")));
		item.setSku(StringUtility.convNullStringToNull(json.getString("sku")));
		item.setMd01(StringUtility.convNullStringToNull(json.getString("md01")));
		item.setMd02(StringUtility.convNullStringToNull(json.getString("md02")));
		item.setMd03(StringUtility.convNullStringToNull(json.getString("md03")));
		item.setMd04(StringUtility.convNullStringToNull(json.getString("md04")));
		item.setMd05(StringUtility.convNullStringToNull(json.getString("md05")));
		item.setMd06(StringUtility.convNullStringToNull(json.getString("md06")));
		item.setMd07(StringUtility.convNullStringToNull(json.getString("md07")));
		item.setMd08(StringUtility.convNullStringToNull(json.getString("md08")));
		item.setMd09(StringUtility.convNullStringToNull(json.getString("md09")));
		item.setMd10(StringUtility.convNullStringToNull(json.getString("md10")));
		item.setMd11(StringUtility.convNullStringToNull(json.getString("md11")));
		item.setMd12(StringUtility.convNullStringToNull(json.getString("md12")));
		item.setMd13(StringUtility.convNullStringToNull(json.getString("md13")));
		item.setMd14(StringUtility.convNullStringToNull(json.getString("md14")));
		item.setMd15(StringUtility.convNullStringToNull(json.getString("md15")));
		item.setMd16(StringUtility.convNullStringToNull(json.getString("md16")));
		item.setMdNameLocal(StringUtility.convNullStringToNull(json.getString("mdNameLocal")));
		item.setMdKanaName(StringUtility.convNullStringToNull(json.getString("mdKanaName")));
		item.setSalesPrice2(json.getDouble("salesPrice2"));
		item.setPaymentType(json.getInt("paymentType"));
		item.setNonSales(json.getInt("nonSales"));
		item.setSubCode1(StringUtility.convNullStringToNull(json.getString("subCode1")));
		item.setSubCode2(StringUtility.convNullStringToNull(json.getString("subCode2")));
		item.setSubCode3(StringUtility.convNullStringToNull(json.getString("subCode3")));
		item.setPromotionId(StringUtility.convNullStringToNull(json.getString("promotionId")));
		item.setDptDiscountType(json.getInt("dptDiscountType"));
		item.setDiacountRate(json.getDouble("diacountRate"));
		item.setDiscountAmt(json.getInt("discountAmt"));
		item.setDiscountClass(json.getInt("discountClass"));
		item.setDptSubNum1(StringUtility.convNullStringToNull(json.getString("dptSubNum1")));
		item.setDptSubNum2(StringUtility.convNullStringToNull(json.getString("dptSubNum2")));
		item.setDptSubNum3(StringUtility.convNullStringToNull(json.getString("dptSubNum3")));
		item.setDptSubNum4(StringUtility.convNullStringToNull(json.getString("dptSubNum4")));


		List<PremiumInfo> premiumList = new ArrayList<PremiumInfo>();
		if (json.get("premiumList") != null && !"null".equals(json.getString("premiumList"))) {
			JSONArray jsonArray = new JSONArray(json.getString("premiumList"));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemJson = (JSONObject) jsonArray.get(i);
				PremiumInfo premiumInfo = new PremiumInfo();
				premiumInfo.setPremiumItemName(StringUtility.convNullStringToNull(itemJson.getString("premiumItemName")));
				premiumInfo.setPremiumItemNo(StringUtility.convNullStringToNull(itemJson.getString("premiumItemNo")));
				premiumInfo.setTargetCount(itemJson.getInt("targetCount"));
				premiumInfo.setTargetPrice(StringUtility.convNullStringToNull(itemJson.getString("targetPrice")));
				premiumList.add(premiumInfo);
			}
		}
		item.setPremiumList(premiumList);

		item.setItemClass(StringUtility.convNullStringToNull(json.getString("itemClass")));
		item.setAgeRestrictedFlag(json.getInt("ageRestrictedFlag"));
		item.setCouponFlag(StringUtility.convNullStringToNull(json.getString("couponFlag")));
		item.setRetailStoreId(StringUtility.convNullStringToNull(json.getString("retailStoreId")));
		item.setEventId(StringUtility.convNullStringToNull(json.getString("eventId")));
		item.setEventName(StringUtility.convNullStringToNull(json.getString("eventName")));
		item.setEventSalesPrice(json.getDouble("eventSalesPrice"));
		item.setEmpPrice1(json.getDouble("empPrice1"));
		item.setPSType(StringUtility.convNullStringToNull(json.getString("pstype")));
		item.setOrgSalesPrice1(json.getDouble("orgSalesPrice1"));
		if (!"null".equals(json.getString("mixMatchCode"))) {
			item.setMixMatchCode(json.getString("mixMatchCode"));
			item.setInheritFlag(StringUtility.convNullStringToNull(json.getString("inheritFlag")));
			item.setRuleQuantity1(json.getInt("ruleQuantity1"));
			item.setRuleQuantity2(json.getInt("ruleQuantity2"));
			item.setRuleQuantity3(json.getInt("ruleQuantity3"));
			item.setConditionPrice1(json.getDouble("conditionPrice1"));
			item.setConditionPrice2(json.getDouble("conditionPrice2"));
			item.setConditionPrice3(json.getDouble("conditionPrice3"));
			item.setDecisionPrice1(
					null == json.get("decisionPrice1") || "null".equals(json.get("decisionPrice1").toString()) ? null
							: json.getDouble("decisionPrice1"));
			item.setDecisionPrice2(
					null == json.get("decisionPrice2") || "null".equals(json.get("decisionPrice2").toString()) ? null
							: json.getDouble("decisionPrice2"));
			item.setDecisionPrice3(
					null == json.get("decisionPrice3") || "null".equals(json.get("decisionPrice3").toString()) ? null
							: json.getDouble("decisionPrice3"));
			item.setAveragePrice1(json.getDouble("averagePrice1"));
			item.setAveragePrice2(json.getDouble("averagePrice2"));
			item.setAveragePrice3(json.getDouble("averagePrice3"));
			item.setNote(StringUtility.convNullStringToNull(json.getString("note")));
		}
		item.setMdVender(StringUtility.convNullStringToNull(json.getString("mdVender")));
		item.setReplaceSupportdiscountAmt(json.getInt("replaceSupportdiscountAmt"));
		item.setSubNum2(json.getInt("subNum2"));
		item.setSalesPriceFrom(StringUtility.convNullStringToNull(json.getString("salesPriceFrom")));
		item.setOldPrice(json.getDouble("oldPrice"));
		item.setCostPrice1(json.getDouble("costPrice1"));
		item.setMakerPrice(json.getDouble("makerPrice"));
		item.setConn1(StringUtility.convNullStringToNull(json.getString("conn1")));
		item.setConn2(StringUtility.convNullStringToNull(json.getString("conn2")));
		item.setPublishingCode(StringUtility.convNullStringToNull(json.getString("publishingCode")));
		item.setPluPrice(json.getDouble("pluPrice"));
		item.setDptNameLocal(StringUtility.convNullStringToNull(json.getString("dptNameLocal")));
		item.setClassNameLocal(StringUtility.convNullStringToNull(json.getString("classNameLocal")));
		item.setGroupName(StringUtility.convNullStringToNull(json.getString("groupName")));
		item.setGroupID(StringUtility.convNullStringToNull(json.getString("groupID")));
		item.setNameText(StringUtility.convNullStringToNull(json.getString("nameText")));
		item.setDptSubCode1(StringUtility.convNullStringToNull(json.getString("dptSubCode1")));
		item.setOldSku(StringUtility.convNullStringToNull(json.getString("oldSku")));
		item.setSizeCode(StringUtility.convNullStringToNull(json.getString("sizeCode")));
		item.setColorCode(StringUtility.convNullStringToNull(json.getString("colorCode")));
		item.setDiscountRate(json.getDouble("discountRate"));
		item.setDiscountCount(json.getInt("discountCount"));

		if (!"null".equals(json.getString("qrPromotionId"))) {
			item.setQrBmpFileCount(StringUtility.convNullStringToNull(json.getString("qrBmpFileCount")));
			item.setQrBmpFileFlag(StringUtility.convNullStringToNull(json.getString("qrBmpFileFlag")));
			item.setQrBmpFileName(StringUtility.convNullStringToNull(json.getString("qrBmpFileName")));
			item.setQrMinimumPrice(
					null == json.get("qrMinimumPrice") || "null".equals(json.get("qrMinimumPrice").toString()) ? null
							: json.getDouble("qrMinimumPrice"));
			item.setQrOutputTargetValue(StringUtility.convNullStringToNull(json.getString("qrOutputTargetValue")));
			item.setQrPromotionId(StringUtility.convNullStringToNull(json.getString("qrPromotionId")));
			item.setQrPromotionName(StringUtility.convNullStringToNull(json.getString("qrPromotionName")));
		}

		List<QrCodeInfo> list = new ArrayList<QrCodeInfo>();
		if (!"null".equals(json.getString("qrCodeList"))) {
			JSONArray jsonArray = new JSONArray(json.getString("qrCodeList"));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemJson = (JSONObject) jsonArray.get(i);
				QrCodeInfo qr = new QrCodeInfo();
				qr.setBmpFileCount(StringUtility.convNullStringToNull(itemJson.getString("bmpFileCount")));
				qr.setBmpFileFlag(StringUtility.convNullStringToNull(itemJson.getString("bmpFileFlag")));
				qr.setBmpFileName(StringUtility.convNullStringToNull(itemJson.getString("bmpFileName")));
				qr.setMinimumPrice(itemJson.getDouble("minimumPrice"));
				qr.setOutputTargetValue(StringUtility.convNullStringToNull(itemJson.getString("outputTargetValue")));
				qr.setPromotionId(StringUtility.convNullStringToNull(itemJson.getString("promotionId")));
				qr.setPromotionName(StringUtility.convNullStringToNull(itemJson.getString("promotionName")));
				qr.setOutputType(StringUtility.convNullStringToNull(itemJson.getString("outputType")));
				list.add(qr);
			}
			item.setQrCodeList(list);
		}
		
		if (!"null".equals(json.getString("couponNo"))) {
			item.setCouponNo(json.getString("couponNo"));
			item.setEvenetName(json.getString("evenetName"));
			item.setIssueCount(json.getInt("issueCount"));
			item.setIssueType(json.getString("issueType"));
			item.setReceiptName(json.getString("receiptName"));
			item.setUnitPrice(json.getDouble("unitPrice"));
		}

		return item;
	}

	/**
	 * Item update to an existing item in the transaction a.) requesting of
	 * removing or adding quantity b.) Change of Item's Price.
	 *
	 * @param retailStoreId
	 *            Store number where the transaction is coming from.
	 * @param workStationId
	 *            Device number where the transaction is coming from.
	 * @param sequenceNumber
	 *            Transaction number.
	 * @param transactionJson
	 *            Transaction in JSON.
	 * @return {@link PromotionResponse}
	 */
	@SuppressWarnings("unchecked")
	@Path("/item_update")
	@POST
	@Produces("application/json;charset=UTF-8")
	@ApiOperation(value = "取引商品更新", response = PromotionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_PARSE, message = "解析エラー"),
			@ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
			@ApiResponse(code = ResultBase.PROMOTION.NO_MATCHING_TRANSACTION, message = "マッチング取引ができない"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ") })
	public final PromotionResponse itemUpdate(
			@ApiParam(name = "retailstoreid", value = "小売店コード") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "シリアルナンバー") @FormParam("sequencenumber") final String sequenceNumber,
			@ApiParam(name = "transaction", value = "取引情報") @FormParam("transaction") final String transactionJson) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreID", retailStoreId).println("WorkStationID", workStationId)
				.println("SequenceNumber", sequenceNumber).println("Transaction", transactionJson);
		PromotionResponse promotionResponse = new PromotionResponse();
		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, workStationId, sequenceNumber, transactionJson)) {
				promotionResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is null or empty.");
				return promotionResponse;
			}
			JsonMarshaller<Transaction> jsonMarshaller = new JsonMarshaller<Transaction>();
			Transaction transactionIn = jsonMarshaller.unMarshall(transactionJson, Transaction.class);
			Sale saleIn = transactionIn.getSale();
			if (saleIn == null || saleIn.getItemEntryId() == null || saleIn.getItemEntryId().isEmpty()) {
				tp.println("Transaction has no Sale data!");
				promotionResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.methodExit(promotionResponse);
				return promotionResponse;
			}

			synchronized ((Map<String, TerminalItem>) GlobalConstant.getTerminalItemsMap()) {
				Map<String, TerminalItem> terminalItemsHashMap = (Map<String, TerminalItem>) GlobalConstant
						.getTerminalItemsMap();
				TerminalItem terminalItem = TerminalItemsHandler.get(retailStoreId, workStationId, sequenceNumber,
						terminalItemsHashMap);

				if (terminalItem == null) {
					tp.println("No matching transaction!");
					LOGGER.logAlert(PROG_NAME, functionName, Logger.LOG_MSGID, "No matching transaction!\n");
					promotionResponse.setNCRWSSResultCode(ResultBase.PROMOTION.NO_MATCHING_TRANSACTION);
					return promotionResponse;
				}

				String mixMatchCode = terminalItem.getItemMixMatchCode(saleIn.getItemEntryId());
				if (StringUtility.isNullOrEmpty(mixMatchCode)) {
					mixMatchCode = terminalItem.getItemMixMatchCodeByItemId(saleIn.getItemId());
				}
				MixMatchDetailInfo info = new MixMatchDetailInfo();
				info.setEntryId(saleIn.getItemEntryId());
				info.setQuantity(saleIn.getQuantity());
				info.setTruePrice(saleIn.getActualSalesUnitPrice());

				Promotion promotion = new Promotion();
				if (!StringUtility.isNullOrEmpty(mixMatchCode)) {
					terminalItem.setBmDetailMap(mixMatchCode, info, true);
					Map<String, Map<String, Object>> map = terminalItem.getMixMatchMap(mixMatchCode, "true");
					if(terminalItem.isDeleteBm(map)){
					    Map<String, Map<String, Object>> newMap = new HashMap<String, Map<String, Object>>();
					    Map<String, Object> childMap = new HashMap<String, Object>();
					    childMap.put("hasMixMatch", "false");
					    newMap.put(mixMatchCode, childMap);
					    promotion.setMap(newMap);
					} else {
					    promotion.setMap(map);
					}
				} else {
					promotion.setMap(new HashMap<String, Map<String, Object>>());
				}
				promotionResponse.setPromotion(promotion);
			}

		} catch (JsonParseException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to update quantity.", e);
			promotionResponse = new PromotionResponse(ResultBase.RES_ERROR_PARSE, ResultBase.RES_ERROR_PARSE, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to update quantity.", e);
			promotionResponse = new PromotionResponse(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, e);
		} finally {
			tp.methodExit(promotionResponse);
		}
		return promotionResponse;
	}

	/**
	 * getQrCodeInfoList for requesting promotion information.
	 *
	 * @param companyId
	 *            The companyId
	 * @param retailStoreId
	 *            Store Number where the transaction is coming from
	 * @param workStationId
	 *            Device Number where the transaction is coming from
	 * @param sequenceNumber
	 *            The current transaction sequence number
	 * @param businessDate
	 *            The businessDate
	 * @param transaction
	 *            The item Info and Operator Info XML
	 * @return {@link Transaction}
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Produces("application/json;charset=UTF-8")
	@Path("/getQrCodeInfoList")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "QRCODE情報を取得", response = Promotion.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
			@ApiResponse(code = ResultBase.PROMOTION.NO_MATCHING_TRANSACTION, message = "一致する取引無し"),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ") })
	public final Promotion getQrCodeInfoList(
			@ApiParam(name = "companyId", value = "企業コード") @FormParam("companyId") final String companyId,
			@ApiParam(name = "retailstoreid", value = "店番号") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "取引番号") @FormParam("sequencenumber") final String sequenceNumber,
			@ApiParam(name = "businessDate", value = "業務日付") @FormParam("businessDate") final String businessDate,
			@ApiParam(name = "transaction", value = "商品情報と会員情報") @FormParam("transaction") final String transaction) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyId", companyId).println("RetailStoreId", retailStoreId)
				.println("WorkstationId", workStationId).println("SequenceNumber", sequenceNumber)
				.println("businessDate", businessDate).println("Transaction", transaction);
		Promotion response = new Promotion();
		int maxPrintNum = 4;
		List<QrCodeInfo> qrCodeInfoListTemp = new ArrayList<QrCodeInfo>();;
		List<QrCodeInfo> qrCodeInfoListOut = null;

		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, workStationId, sequenceNumber, transaction, companyId,
					businessDate)) {
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return response;
			}

			if (qrCodeInfoList == null) {
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
				response.setMessage("Not found Initial startup data.");
				tp.println("Not found Initial startup data.");
			}

			JsonMarshaller<Transaction> jsonMarshall = new JsonMarshaller<Transaction>();
			Transaction transactionIn = jsonMarshall.unMarshall(transaction, Transaction.class);
			transactionIn.setCompanyId(companyId);

			// get valid data
			qrCodeInfoListTemp = getQrCodeInfo(transactionIn);

			int count = 0;
			if (qrCodeInfoListTemp == null || qrCodeInfoListTemp.size() < 1) {
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
				response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
				response.setMessage("Not found valid data.");
				tp.println("Not found valid data.");
				return response;
			} else {
				Collections.sort(qrCodeInfoListTemp, new Comparator<QrCodeInfo>() {

					@Override
					public int compare(QrCodeInfo o1, QrCodeInfo o2) {
						if(Integer.parseInt(o1.getDisplayOrder()) > Integer.parseInt(o2.getDisplayOrder())){
							return 1;
						}
						if(Integer.parseInt(o1.getDisplayOrder()) == Integer.parseInt(o2.getDisplayOrder())){
							return 0;
						}
						return -1;
					}
				});
				// 一番小さい値を持つ企画コードが複数の場合
				String displayOrder = qrCodeInfoListTemp.get(0).getDisplayOrder();
				for (int i = 0; i < qrCodeInfoListTemp.size(); i++) {
					if (displayOrder.equals(qrCodeInfoListTemp.get(i).getDisplayOrder())){
						count++;
					}
				}
			}

			// 最大印字枚数をパラメータに取得する
			String getMaxPrintNum = GlobalConstant.getMaxQRCodePrintNum();
			if (!StringUtility.isNullOrEmpty(getMaxPrintNum)){
				maxPrintNum = Integer.parseInt(getMaxPrintNum);
			}

			// 有効な企画コードを取得する
			boolean flag = true;
			for (int i = 0; i < count; i++) {
				if (flag) {
					if (qrCodeInfoListOut == null) {
						qrCodeInfoListOut = new ArrayList<QrCodeInfo>();
					}
					if (!(qrCodeInfoListTemp.get(i).getQuantity() < maxPrintNum)) {
						qrCodeInfoListTemp.get(i).setQuantity(maxPrintNum);
						flag = false;
					} else {
						maxPrintNum = maxPrintNum - qrCodeInfoListTemp.get(i).getQuantity();
					}
					qrCodeInfoListOut.add(qrCodeInfoListTemp.get(i));
				}
			}

			getQrCodeFileExist(qrCodeInfoListOut, companyId, retailStoreId, workStationId);

			response.setQrCodeInfoList(qrCodeInfoListOut);
		} catch (JsonParseException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to send QrCodeInfoList.", e);
			response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
		} catch (IOException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to send QrCodeInfoList.", e);
			response.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
			response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send QrCodeInfoList.", e);
			response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(response);
		}
		return response;
	}

	/**
	 * set valid data into map
	 *
	 * @param transactionIn
	 * @return List<QrCodeInfo>
	 * @throws DaoException
	 */
	private List<QrCodeInfo> getQrCodeInfo(Transaction transactionIn) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("Transaction", transactionIn);

		String SexTypeIn = transactionIn.getCustomerClass().getSexType();
		String rank = null;
		String birthMonth = null;
		String customerId = null;
		boolean CustomerExistFlag = false;
		String CustomerSexTypeIn = null;
		if (transactionIn.getCustomer() != null) {
			rank = transactionIn.getCustomer().getRank();
			birthMonth = transactionIn.getCustomer().getBirthMonth();
			customerId = transactionIn.getCustomer().getId();
			CustomerSexTypeIn = transactionIn.getCustomer().getSexType();
			CustomerExistFlag = true;
		}

		List<ItemList> itemListIns = transactionIn.getItemList();
		List<ItemList> itemListOut = null;
		List<QrCodeInfo> qrCodeInfoListTemp = null;
		Map<String, List<ItemList>> qrItemMap = new TreeMap<String, List<ItemList>>();

		for (QrCodeInfo qrCodeInfo : qrCodeInfoList) {
			if (!StringUtility.isNullOrEmpty(qrCodeInfo.getSexType())) {
				if (CustomerExistFlag == true) {
					if (!(("0".equals(CustomerSexTypeIn) || "0".equals(SexTypeIn) || "0".equals(qrCodeInfo.getSexType())
							|| SexTypeIn.equals(qrCodeInfo.getSexType()) || CustomerSexTypeIn.equals(qrCodeInfo.getSexType()))
							&& ("0".equals(rank) || "0".equals(qrCodeInfo.getMemberRank()) || rank.equals(qrCodeInfo.getMemberRank()))
							&& ("00".equals(birthMonth) || "00".equals(qrCodeInfo.getBirthMonth()) || birthMonth.equals(qrCodeInfo.getBirthMonth())))) {
						continue;
					}
					// MemberTargetType = 1の場合
					if (MEMBERTARGETTYPE_ONE.equals(qrCodeInfo.getMemberTargetType())){
						qrCodeInfo.setCustomerId(checkCustomerID(qrCodeInfo, transactionIn));
						if(!(customerId.equals(qrCodeInfo.getCustomerId()))) {
							continue;
						}
					}
				} else {
					continue;
				}
			}

			itemListOut = new ArrayList<ItemList>();
			switch (qrCodeInfo.getPromotionType()) {
			case PROMOTIONTYPE_ALL:
				itemListOut.addAll(itemListIns);
				break;
			case PROMOTIONTYPE_DPT:
				for (ItemList itemListIn : itemListIns) {
					if (itemListIn.getDpt() == null ? false : itemListIn.getDpt().equals(qrCodeInfo.getDpt())) {
						itemListOut.add(itemListIn);
					}
				}
				break;
			case PROMOTIONTYPE_DPT_CONN:
				for (ItemList itemListIn : itemListIns) {
					if (StringUtility.isNullOrEmpty(itemListIn.getDpt())
							|| StringUtility.isNullOrEmpty(itemListIn.getConnCode())) {
						break;
					}
					if (itemListIn.getDpt().equals(qrCodeInfo.getDpt())
							&& itemListIn.getConnCode().equals(qrCodeInfo.getConnCode())) {
						itemListOut.add(itemListIn);
					}
				}
				break;
			case PROMOTIONTYPE_DPT_BRANDID:
				for (ItemList itemListIn : itemListIns) {
					if (StringUtility.isNullOrEmpty(itemListIn.getDpt())
							|| StringUtility.isNullOrEmpty(itemListIn.getBrandId())) {
						break;
					}
					if (itemListIn.getDpt().equals(qrCodeInfo.getDpt())
							&& itemListIn.getBrandId().equals(qrCodeInfo.getBrandId())) {
						itemListOut.add(itemListIn);
					}
				}
				break;
			case PROMOTIONTYPE_LINE:
				for (ItemList itemListIn : itemListIns) {
					if (itemListIn.getLine() == null ? false : itemListIn.getLine().equals(qrCodeInfo.getLine())) {
						itemListOut.add(itemListIn);
					}
				}
				break;
			case PROMOTIONTYPE_ITEMCODE:
				for (ItemList itemListIn : itemListIns) {
					String qrCodeSku = qrCodeInfo.getSku();
					String listInItemCode = itemListIn.getItemcode();
					if (!StringUtility.isNullOrEmpty(qrCodeSku)) {
						if (qrCodeSku.contains("*")) {
							if (listInItemCode.startsWith(qrCodeSku.replace("*", ""))) {
								itemListOut.add(itemListIn);
							}
						} else {
							if (listInItemCode.equals(qrCodeSku)) {
								itemListOut.add(itemListIn);
							}
						}
					}
				}
				break;
			}

			if (itemListOut.size() > 0 ) {
				if(qrItemMap != null && qrItemMap.size() > 0) {
					for (String promotionId : qrItemMap.keySet()) {
						if (qrCodeInfo.getPromotionId().equals(promotionId)) {
							itemListOut.addAll(qrItemMap.get(promotionId));
						}
					}
				}
				qrItemMap.put(qrCodeInfo.getPromotionId(), itemListOut);
			}
		}

		// get valid data from map
		if (qrItemMap.size() > 0) {
			qrCodeInfoListTemp = getQrCodeInfoListTemp(qrItemMap);
		}

		tp.methodExit(qrCodeInfoListTemp);
		return qrCodeInfoListTemp;
	}

	/**
	 * get valid data from map
	 *
	 * @param qrItemMap
	 * @return List<QrCodeInfo>
	 */
	private List<QrCodeInfo> getQrCodeInfoListTemp(Map<String, List<ItemList>> qrItemMap) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("qrItemMap", qrItemMap);

		List<QrCodeInfo> qrCodeInfoListTemp = new ArrayList<QrCodeInfo>();

		for (String promotionId : qrItemMap.keySet()) {
			for (QrCodeInfo qrCodeInfo : qrCodeInfoList) {
				if (promotionId.equals(qrCodeInfo.getPromotionId())) {
					int quanlitySum = 0;
					int priceSum = 0;

					for (ItemList item : qrItemMap.get(promotionId)) {
						quanlitySum = quanlitySum + item.getQty();
						priceSum = priceSum + item.getAmount();
					}

					switch (qrCodeInfo.getOutputType()) {
					case OUTPUTTYPE_ONE:
						qrCodeInfo.setQuantity(1);
						break;
					case OUTPUTTYPE_TWO:
						qrCodeInfo.setQuantity(quanlitySum);
						break;
					case OUTPUTTYPE_THREE:
						if (!StringUtility.isNullOrEmpty(qrCodeInfo.getOutputTargetValue())) {
							int outputTargetValue = Integer.parseInt(qrCodeInfo.getOutputTargetValue());
							quanlitySum = outputTargetValue == 0 ? 0 : priceSum / outputTargetValue;
						} else {
							quanlitySum = 0;
						}

						qrCodeInfo.setQuantity(quanlitySum);
						break;
					}

					qrCodeInfo.setTargetValue(qrCodeInfo.getOutputTargetValue());
					int minimumPrice = (int) Math.round(qrCodeInfo.getMinimumPrice());
					if (priceSum >= minimumPrice && qrCodeInfo.getQuantity() > 0) {
						qrCodeInfoListTemp.add(qrCodeInfo);
					}
					break;
				}
			}
		}

		tp.methodExit(qrCodeInfoListTemp);
		return qrCodeInfoListTemp;
	}

	/**
	 * check customerid in MST_QRCODE_MEMBERID
	 *
	 * @param qrCodeInfo
	 * @param transactionIn
	 * @return rightCustomerId
	 */
	private String checkCustomerID(QrCodeInfo qrCodeInfo, Transaction transactionIn) throws DaoException {
		String rightCustomerId = null;
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		IQrCodeInfoDAO codeInfDAO = daoFactory.getQrCodeInfoDAO();
		String companyId = transactionIn.getCompanyId();
		String promotionId = qrCodeInfo.getPromotionId();
		String customerId = transactionIn.getCustomer().getId();
		rightCustomerId = codeInfDAO.getCustomerQrCodeInfoList(companyId, promotionId, customerId);
		return rightCustomerId;
	}

	/**
	 * get qrcode file exist
	 *
	 * @param qrCodeInfoListOut
	 * @param companyId
	 * @param storeId
	 * @param terminalId
	 *
	 * @return List<QrCodeInfo>
	 * @throws Exception
	 */
	private void getQrCodeFileExist(List<QrCodeInfo> qrCodeInfoListOut, String companyId, String storeId, String terminalId) throws Exception {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("qrCodeInfoListOut", qrCodeInfoListOut).println("companyId", companyId)
			.println("storeId", storeId).println("terminalId", terminalId);

		ViewTerminalInfo terminalInfoResult = new ViewTerminalInfo();
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		String fileName = null;

		try {
			IDeviceInfoDAO iPerCtrlDao = daoFactory.getDeviceInfoDAO();
			terminalInfoResult = iPerCtrlDao.getTerminalInfo(companyId, storeId, terminalId);

			if (terminalInfoResult.getNCRWSSResultCode() == ResultBase.RES_OK) {
				String systemPath = terminalInfoResult.getTerminalInfo().getSubCode2();

				for (QrCodeInfo qrCodeInfo : qrCodeInfoListOut) {
					int bmpFileCount = Integer.parseInt(qrCodeInfo.getBmpFileCount());
					fileName = qrCodeInfo.getBmpFileName();
					if (StringUtility.isNullOrEmpty(fileName)) {
						qrCodeInfo.setFileExist(0);
					} else {
						for (int i = 0; i < bmpFileCount; i++) {
							String strTemp = "_" +String.format("%02d", i+1) + ".";
							String fileNameTemp = fileName.replaceAll("\\.", strTemp);
							File file = new File(systemPath + File.separator + fileNameTemp);
							if (file.isFile() || file.exists()) {
								qrCodeInfo.setFileExist(1);
							} else {
								qrCodeInfo.setFileExist(0);
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			String loggerErrorCode = null;
			if (e.getCause() instanceof SQLException) {
				loggerErrorCode = Logger.RES_EXCEP_DAO;
			} else if (e.getCause() instanceof SQLStatementException) {
				loggerErrorCode = Logger.RES_EXCEP_DAO;
			} else if (e.getCause() instanceof IOException) {
				loggerErrorCode = Logger.RES_EXCEP_IO;
			} else {
				loggerErrorCode = Logger.RES_EXCEP_GENERAL;
			}
			LOGGER.logAlert(PROG_NAME, loggerErrorCode, functionName,
					"Failed to read File for fileName#" + fileName + ", " + "companyId#" + companyId + ", "
					+ "storeId#" + storeId + " and terminalId#" + terminalId + ": " + e.getMessage());
			throw new Exception();

		} finally {
			tp.methodExit(qrCodeInfoListOut);
		}
	}

	/**
	 * getPromotionMessageList for requesting promotion msg information.
	 *
	 * @param companyId
	 *            The companyId
	 * @param retailStoreId
	 *            Store Number where the transaction is coming from
	 * @param workStationId
	 *            Device Number where the transaction is coming from
	 * @param sequenceNumber
	 *            The current transaction sequence number
	 * @param businessDate
	 *            The businessDate
	 * @param transaction
	 *            The item Info XML
	 * @return {@link Transaction}
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Produces("application/json;charset=UTF-8")
	@Path("/getPromotionMessageList")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "販促メッセージ情報を取得", response = Promotion.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー"),
			@ApiResponse(code = ResultBase.PROMOTION.NO_MATCHING_TRANSACTION, message = "一致する取引無し"),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO異常"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効のパラメータ") })
	public final Promotion getPromotionMessageList(
			@ApiParam(name = "companyId", value = "企業コード") @FormParam("companyId") final String companyId,
			@ApiParam(name = "retailstoreid", value = "店番号") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "workstationid", value = "ターミナル番号") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "取引番号") @FormParam("sequencenumber") final String sequenceNumber,
			@ApiParam(name = "businessdate", value = "業務日付") @FormParam("businessdate") final String businessDate,
			@ApiParam(name = "transaction", value = "商品情報") @FormParam("transaction") final String transaction) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyId", companyId).println("RetailStoreId", retailStoreId)
				.println("WorkstationId", workStationId).println("sequencenumber", sequenceNumber)
				.println("businessDate", businessDate).println("Transaction", transaction);
		Promotion response = new Promotion();

		List<PromotionMsgInfo> promMsgInfoListOut = null;

		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, workStationId, sequenceNumber, transaction, companyId,
					businessDate)) {
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return response;
			}

			if (promotionMsgInfoList == null) {
				tp.println("Not found Initial startup data.");
				return response;
			}

			JsonMarshaller<Transaction> jsonMarshall = new JsonMarshaller<Transaction>();
			Transaction transactionIn = jsonMarshall.unMarshall(transaction, Transaction.class);
			transactionIn.setCompanyId(companyId);

			// get valid data
			promMsgInfoListOut = getPromotionMsgInfo(transactionIn);
			if (promMsgInfoListOut == null || promMsgInfoListOut.size() < 1) {
				tp.println("Not found valid data.");
				return response;
			}

			response.setPromotionMsgInfoList(promMsgInfoListOut);

		} catch (JsonParseException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to send promotionMsgInfoList.", e);
			response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
		} catch (IOException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + ": Failed to send promotionMsgInfoList.", e);
			response.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
			response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to send promotionMsgInfoList.", e);
			response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(response);
		}
		return response;
	}

	/**
	 * set valid data into map
	 *
	 * @param transactionIn
	 * @return List<QrCodeInfo>
	 * @throws DaoException
	 */
	private List<PromotionMsgInfo> getPromotionMsgInfo(Transaction transactionIn) throws DaoException {
		String companyId = transactionIn.getCompanyId();
		List<ItemList> itemListIns = transactionIn.getItemList();
		List<ItemList> itemListOut = null;
		List<PromotionMsgInfo> promMsgInfoListTemp = null;
		Map<Integer, List<ItemList>> promMsgItemMap = new TreeMap<Integer, List<ItemList>>();

		for (PromotionMsgInfo promotionMsgInfo : promotionMsgInfoList) {
			itemListOut = new ArrayList<ItemList>();

			for (ItemList itemListIn : itemListIns) {
				String promMsgMdInternal = promotionMsgInfo.getItemCode();
				String listInItemCode = itemListIn.getItemcode();

				if (!StringUtility.isNullOrEmpty(promMsgMdInternal)) {
					if (promMsgMdInternal.contains("*")) {
						if (listInItemCode.startsWith(promMsgMdInternal.replace("*", ""))) {
							itemListOut.add(itemListIn);
						}
					} else {
						if (listInItemCode.equals(promMsgMdInternal)) {
							itemListOut.add(itemListIn);
						}
					}
				}
			}

			if (itemListOut.size() > 0 ) {
				if(promMsgItemMap != null && promMsgItemMap.size() > 0) {
					for (int recordId : promMsgItemMap.keySet()) {
						if (promotionMsgInfo.getRecordId() == recordId) {
							itemListOut.addAll(promMsgItemMap.get(recordId));
						}
					}
				}
				promMsgItemMap.put(promotionMsgInfo.getRecordId(), itemListOut);
			}
		}

		// get valid data from map
		if (promMsgItemMap.size() > 0) {
			promMsgInfoListTemp = getPromotionMsgInfoListTemp(companyId, promMsgItemMap);
		}
		return promMsgInfoListTemp;
	}

	/**
	 * get valid data from map
	 *
	 * @param companyId
	 * @param promMsgItemMap
	 * @return List<PromotionMsgInfo>
	 * @throws DaoException
	 */
	private List<PromotionMsgInfo> getPromotionMsgInfoListTemp(String companyId,Map<Integer, List<ItemList>> promMsgItemMap) throws DaoException {

		List<PromotionMsgInfo> promMsgInfoListTemp = new ArrayList<PromotionMsgInfo>();
		List<PromotionMsgInfo> promMsgInfoListOut = new ArrayList<PromotionMsgInfo>();

		List<String> dptList = new ArrayList<String>();
		List<String> itemCodeList = new ArrayList<String>();
		String itemCode;
		int priceSum;
		PromotionMsgInfo promMsgInfo = null;

		for (Integer recordId : promMsgItemMap.keySet()) {

			for (PromotionMsgInfo promotionMsgInfo : promotionMsgInfoList) {

				if (recordId.equals(promotionMsgInfo.getRecordId())) {
					priceSum = 0;
					itemCodeList.clear();
					promMsgInfoListTemp.clear();

					dptList = getPromotionMsgDpt(companyId, recordId);

					for (ItemList item : promMsgItemMap.get(recordId)) {
						if (dptList == null || dptList.size() == 0 || dptList.contains(item.getDpt())){
							itemCode = item.getItemcode();
							if (!itemCodeList.contains(itemCode)){
								promMsgInfo = new PromotionMsgInfo();
								promMsgInfo.setItemCode(itemCode);
								promMsgInfo.setMdName(item.getMdName());
								promMsgInfo.setMessageBody(promotionMsgInfo.getMessageBody());

								promMsgInfoListTemp.add(promMsgInfo);
								itemCodeList.add(itemCode);
							}
							priceSum = priceSum + item.getAmount();
						}
					}
					int minimumPrice = (int) Math.round(promotionMsgInfo.getMinimunPrice());

					if (priceSum >= minimumPrice) {
						promMsgInfoListOut.addAll(promMsgInfoListTemp.stream().distinct().collect(Collectors.toList()));
					}
					break;
				}
			}
		}

		return promMsgInfoListOut;
	}

	/**
	 * get dpt from MST_PROMOTIONMSG_DPT
	 *
	 * @param companyId
	 * @param recordId
	 * @return List<String>
	 */
	private List<String> getPromotionMsgDpt(String companyId, int recordId) throws DaoException {
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		IPromotionMsgInfoDAO promMsgInfDAO = daoFactory.getPromotionMsgInfoDAO();

		return promMsgInfDAO.getPromotionMsgDptList(companyId, recordId);
	}
}