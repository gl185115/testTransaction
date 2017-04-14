package ncr.res.mobilepos.promotion.resource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
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
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.promotion.dao.ICodeConvertDAO;
import ncr.res.mobilepos.promotion.helper.FormatByXml;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.helper.TerminalItemsHandler;
import ncr.res.mobilepos.promotion.model.MixMatchDetailInfo;
import ncr.res.mobilepos.promotion.model.Promotion;
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

    private static final String ITEM_FORM = "picklist";

	/**
	 * Default Constructor for PromotionResource.
	 *
	 * <P>
	 * Initializes the logger object.
	 */
	public PromotionResource() {
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * The Begin Transaction for Promotion.
	 * 
	 * @param retailStoreId
	 *            The Retail Store ID.
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
			@ApiParam(name = "workstationid", value = "作業台コード") @FormParam("workstationid") final String workStationId,
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
			@ApiParam(name = "workstationid", value = "作業台コード") @FormParam("workstationid") final String workStationId,
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
			@ApiParam(name = "workstationid", value = "作業台コード") @FormParam("workstationid") final String workStationId,
			@ApiParam(name = "sequencenumber", value = "シリアルナンバー") @FormParam("sequencenumber") final String sequenceNumber,
			@ApiParam(name = "transaction", value = "取引情報") @FormParam("transaction") final String transaction,
			@ApiParam(name = "companyId", value = "会社コード") @FormParam("companyId") final String companyId,
			@ApiParam(name = "businessDate", value = "営業日") @FormParam("businessDate") final String businessDate) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreId", retailStoreId).println("WorkstationId", workStationId)
				.println("SequenceNumber", sequenceNumber).println("Transaction", transaction)
				.println("businessDate", businessDate).println("companyId", companyId);
		Transaction transactionOut = new Transaction();
		PromotionResponse response = new PromotionResponse();
		int discounttype = 0;
		boolean twoStep = false;
		String codeTemp = null;
		String dpt = null;
		String dptCode = null;
		String barcode_fst = null;
		String barcode_sec = null;
		
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

				String itemId = saleIn.getItemId();
				//String itemForm = !StringUtility.isNullOrEmpty(saleIn.getItemForm()) ? saleIn.getItemForm() : "";
				Double regularSalesUnitPrice = saleIn.getRegularSalesUnitPrice();
				
				DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
				ICodeConvertDAO codeCvtDAO = daoFactory.getCodeConvertDAO();
				IDepartmentDAO idepartmentDAO = daoFactory.getDepartmentDAO();
				IItemDAO dao = daoFactory.getItemDAO();
				Item itemInfoTemp = new Item();
				ViewDepartment departmentInfo = new ViewDepartment();
				
				if (itemId.contains(" ")) {
					twoStep = Boolean.parseBoolean(ResultBase.TRUE);
					barcode_fst = itemId.split(" ")[0];
					barcode_sec = itemId.split(" ")[1];
				} else {
					twoStep = Boolean.parseBoolean(ResultBase.FALSE);
				}
				
				FormatByXml formatByXml= new FormatByXml();
				boolean varietiesJudge = formatByXml.varietiesJudge(itemId);
				if (varietiesJudge) {
					Transaction transation = new Transaction();
					transation.setItemId(itemId);
					response.setTransaction(transation);
					response.setNCRWSSExtendedResultCode(ResultBase.TWOSTEPITEM_VARIETIES);
					response.setNCRWSSResultCode(ResultBase.RES_OK);
					response.setMessage("This commodity is The two section commodity.");
					tp.println("This commodity is The two section commodity.");

					return response;
				}

				// 2段目に入力した桁数が4桁以下
				if (twoStep && barcode_sec.length() == 4) {
					if (regularSalesUnitPrice == 0) {
						String ccodeTemp = codeCvtDAO.convertCCodeToDpt(companyId, barcode_sec);
						departmentInfo = idepartmentDAO.selectDepartmentDetail(companyId, retailStoreId, ccodeTemp);
						dptCode = (departmentInfo.getDepartment() == null) ? null : departmentInfo.getDepartment().getDepartmentID();
						if (!StringUtility.isNullOrEmpty(dptCode)) {
							response.setNCRWSSResultCode(ResultBase.RES_OK);
							response.setNCRWSSExtendedResultCode(ResultBase.PRICE_INPUT_REQUEST);
							Transaction transation = new Transaction();
							transation.setItemId(itemId);
							response.setDepartment(ccodeTemp);
							response.setTransaction(transation);
							response.setMessage("The second stage of barcode belongs to departmentId.");
							tp.println("The second stage of barcode belongs to departmentId.");
							return response;
						} else {
							response.setNCRWSSResultCode(ResultBase.DPTCODE_NOTFOUND);
							response.setNCRWSSExtendedResultCode(ResultBase.DPTCODE_NOTFOUND);
							response.setMessage("The second stage of barcode is not departmentId.");
							tp.println("The second stage of barcode is not departmentId.");
							return response;
						}
					}
				}

				String varietiesName = formatByXml.getVarietiesName(itemId);
				switch (varietiesName) {
				case GlobalConstant.VARIETIES_JANBOOK:
					itemInfoTemp = dao.getItemByApiData(barcode_fst, companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					codeTemp = (dpt == null) ? codeCvtDAO.convertCCodeToDpt(companyId, barcode_sec.substring(3, 7)) : dpt;
					break;
				case GlobalConstant.VARIETIES_JANBOOKOLD:
					itemInfoTemp = dao.getItemByApiData(CDCalculation(itemId), companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					codeTemp = (dpt == null) ? codeCvtDAO.convertCCodeToDpt(companyId, itemId.substring(10, 14)) : dpt;
					break;
				case GlobalConstant.VARIETIES_JANMAGAZINE:
					itemInfoTemp = dao.getItemByApiData(itemId.substring(0, 13), companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					codeTemp = (dpt == null) ? codeCvtDAO.convertMagCodeToDpt(companyId, itemId.substring(5, 10)) : dpt;
					break;
				case GlobalConstant.VARIETIES_FOREIGNBOOK:
					itemInfoTemp = dao.getItemByApiData(barcode_fst, companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					codeTemp = (dpt == null) ? JaCodeCvt(barcode_sec.substring(3, 7)) : dpt;
					break;
				case GlobalConstant.VARIETIES_FOREIGNBOOKOLD:
					itemInfoTemp = dao.getItemByApiData(CDCalculation(itemId), companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					codeTemp = (dpt == null) ? JaCodeCvt(itemId.substring(10, 14)) : dpt;
					break;
				case GlobalConstant.VARIETIES_FOREIGNJANBOOK:
					itemInfoTemp = dao.getItemByApiData(barcode_fst, companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					if (dpt == null) {
						response.setNCRWSSResultCode(ResultBase.RES_OK);
						response.setNCRWSSExtendedResultCode(ResultBase.RES_ITEM_NOT_EXIST);
						response.setMessage("Not found in the PLU.");
						tp.println("Not found in the PLU.");
						return response;
					}
					codeTemp = dpt;
					break;
				case GlobalConstant.VARIETIES_FOREIGNMAGAZINE:
					itemInfoTemp = dao.getItemByApiData(itemId, companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					codeTemp = (dpt == null) ? "0161" : dpt;
					break;
				case GlobalConstant.VARIETIES_KINOKUNIYA:
				case GlobalConstant.VARIETIES_JANSALES:
					itemInfoTemp = dao.getItemByApiData(itemId, companyId);
					dpt = (itemInfoTemp == null) ? null : itemInfoTemp.getDepartment();
					if (dpt == null) {
						response.setNCRWSSResultCode(ResultBase.RES_OK);
						response.setNCRWSSExtendedResultCode(ResultBase.EMERGENCY_REGISTRATION);
						response.setMessage("The Emergency registration.");
						tp.println("The Emergency registration.");
						return response;
					}
					codeTemp = dpt;
					break;
				default:
					break;
				}

				departmentInfo = idepartmentDAO.selectDepartmentDetail(companyId, retailStoreId, codeTemp);
				dptCode = (departmentInfo.getDepartment() == null) ? null : departmentInfo.getDepartment().getDepartmentID();
				if (StringUtility.isNullOrEmpty(dptCode)) {
					response.setNCRWSSResultCode(ResultBase.DPTCODE_NOTFOUND);
					response.setNCRWSSExtendedResultCode(ResultBase.DPTCODE_NOTFOUND);
					response.setMessage("The dpt code doesn't exist in the MST_DPTINFO.");
					tp.println("The dpt code doesn't exist in the MST_DPTINFO.");
					return response;
				}

				ResultBase rs = SaleItemsHandler.validateSale(saleIn);
				if (ResultBase.RES_OK != rs.getNCRWSSResultCode()) {
					tp.println(rs.getMessage());
					response.setNCRWSSResultCode(rs.getNCRWSSResultCode());
					return response;
				}
				info.setQuantity(saleIn.getQuantity());
				info.setEntryId(saleIn.getItemEntryId());

				ItemResource itemResource = new ItemResource();
				itemResource.setContext(context);

				String itemIdTemp = null;
				if (twoStep){
					itemIdTemp = barcode_fst;
				} else if (GlobalConstant.VARIETIES_JANBOOKOLD.equals(varietiesName) || 
						GlobalConstant.VARIETIES_FOREIGNBOOKOLD.equals(varietiesName)){
					itemIdTemp = CDCalculation(itemId);
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
						response.setNCRWSSResultCode(0);
						throw new Exception("0");
					}
					if (item == null) {
						response.setNCRWSSResultCode(searchedProd.getNCRWSSResultCode());
						return response;
					}
				} else {
					item = searchedProd.getItem();
				}

				info.setTruePrice(item.getRegularSalesUnitPrice());
				Promotion promotion = new Promotion();
				promotion.setCouponInfoList(makeCouponInfoList(terminalItem.getCouponInfoMap(item)));
				promotion.setQrCodeInfoList(terminalItem.getQrCodeInfoList(item));
				if (!StringUtility.isNullOrEmpty(item.getMixMatchCode())) {
					terminalItem.addBmRuleMap(item.getMixMatchCode(), item, saleIn.getItemEntryId());
					if (!"false".equals(transactionIn.getEntryFlag())) {
						terminalItem.setBmDetailMap(item.getMixMatchCode(), info, false);
						Map<String, Map<String, Object>> map = terminalItem.getMixMatchMap(item.getMixMatchCode(), "");
						promotion.setMap(map);
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

				// バーコード価格を使用
				if (saleItem.getRegularSalesUnitPrice() == 0.0) {
					double commodityPrice = 0.0;
					switch (varietiesName) {
					case GlobalConstant.VARIETIES_JANBOOK:
						commodityPrice = janBook(itemId);
						break;
					case GlobalConstant.VARIETIES_FOREIGNBOOK:
						commodityPrice = foreignBook(itemId);
						break;
					case GlobalConstant.VARIETIES_JANMAGAZINE:
						commodityPrice = janMagazine(itemId);
						break;
					case GlobalConstant.VARIETIES_FOREIGNMAGAZINE:
						commodityPrice = foreignMagazine(itemId);
						break;
					default:
						break;
					}
					saleItem.setRegularSalesUnitPrice(commodityPrice);
				}

				saleItem.setDiscountType(discounttype);
				boolean flag = discounttype == 0;
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

	public String CDCalculation(String itemId) {
		char cd = CheckDigitValue.generate("978" + itemId.substring(0, 9), 12, "131313131313", 10,
				CheckDigitValue.SKIP_0_OK, true);
		itemId = "978" + itemId.substring(0, 9) + cd;
		return itemId;
	}

	public String JaCodeCvt(String code) {
		if ("0000".equals(code)) {
			code = "0182";
		} else {
			code = "040".equals(code.substring(0, 3)) ? "040" + code.substring(3, 4) : "016" + code.substring(3, 4);
		}
		return code;
	}

	/**
	 * 洋雑誌価格計算
	 * 
	 * @param itemId
	 * @return
	 */
	public int foreignMagazine(String itemId) {

		int price = Integer.parseInt(itemId.substring(9, 12)) * 10;

		return price;
	}

	/**
	 * 和雑誌価格計算
	 * 
	 * @param itemId
	 * @return
	 */
	public int janMagazine(String itemId) {

		String endFour = itemId.substring(itemId.length() - 4);
		int price = Integer.parseInt(endFour);

		return price;
	}

	/**
	 * 洋書価格計算
	 * 
	 * @param itemId
	 * @return
	 */
	public int foreignBook(String itemId) {

		String[] item = itemId.split(" ");
		int price = Integer.parseInt(item[1].substring(7, 12));

		return price;
	}

	/**
	 * 和書価格計算
	 * 
	 * @param itemId
	 * @return
	 */
	public int janBook(String itemId) {

		String[] item = itemId.split(" ");
		String topThree = item[1].substring(0, 3);
		int price = 0;
		if ("191".equals(topThree)) {
			price = (int) Math.round(Double.parseDouble(item[1].substring(7, 12)) * 100 / 103);
		} else if ("192".equals(topThree)) {
			price = Integer.parseInt(item[1].substring(7, 12));
		}

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
			@ApiParam(name = "workstationid", value = "作業台コード") @FormParam("workstationid") final String workStationId,
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
				promotion.setMap(map);
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
			String url = GlobalConstant.getEnterpriseServerUri() + REMOTE_UTL;
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
		item.setItemId(json.getString("itemId"));
		Description description = new Description();
		description.setEn(json.getJSONObject("description").getString("en"));
		description.setJa(json.getJSONObject("description").getString("ja"));
		item.setDescription(description);
		item.setRegularSalesUnitPrice(Double.parseDouble(json.getString("regularSalesUnitPrice")));
		item.setDiscount(Double.parseDouble(json.getString("discount")));
		item.setDiscountFlag(Integer.parseInt(json.getString("discountFlag")));
		item.setDiscountAmount(Double.parseDouble(json.getString("discountAmount")));
		item.setDepartment(json.getString("department"));
		item.setDiscountable(json.getBoolean("discountable"));
		item.setTaxRate(json.getInt("taxRate"));
		item.setTaxType(json.getInt("taxType"));
		item.setDiscountType(json.getInt("discountType"));
		item.setSubNum1(json.getInt("subNum1"));
		item.setDptDiscountType(json.getInt("dptDiscountType"));
		item.setNonSales(json.getInt("nonSales"));
		item.setSubInt10(json.getInt("subInt10"));
		item.setLine(json.getString("line"));
		item.setCompanyId(json.getString("companyId"));
		item.setPromotionNo(json.getString("promotionNo"));
		item.setMdType(json.getString("mdType"));
		item.setSku(json.getString("sku"));
		item.setMd01(json.getString("md01"));
		item.setMd02(json.getString("md02"));
		item.setMd03(json.getString("md03"));
		item.setMd04(json.getString("md04"));
		item.setMd05(json.getString("md05"));
		item.setMd06(json.getString("md06"));
		item.setMd07(json.getString("md07"));
		item.setMd08(json.getString("md08"));
		item.setMd09(json.getString("md09"));
		item.setMd10(json.getString("md10"));
		item.setMd11(json.getString("md11"));
		item.setMd12(json.getString("md12"));
		item.setMd13(json.getString("md13"));
		item.setMd14(json.getString("md14"));
		item.setMd15(json.getString("md15"));
		item.setMd16(json.getString("md16"));
		item.setMdNameLocal(json.getString("mdNameLocal"));
		item.setMdKanaName(json.getString("mdKanaName"));
		item.setSalesPrice2(json.getDouble("salesPrice2"));
		item.setPaymentType(json.getInt("paymentType"));
		item.setSubCode1(json.getString("subCode1"));
		item.setSubCode2(json.getString("subCode2"));
		item.setSubCode3(json.getString("subCode3"));
		item.setPromotionId(json.getString("promotionId"));
		item.setDptDiscountType(json.getInt("dptDiscountType"));
		item.setDiacountRate(json.getDouble("diacountRate"));
		item.setDiscountAmt(json.getInt("discountAmt"));
		item.setDiscountClass(json.getInt("discountClass"));

		List<PremiumInfo> premiumList = new ArrayList<PremiumInfo>();
		if (json.get("premiumList") != null) {
			JSONArray jsonArray = new JSONArray(json.getString("premiumList"));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemJson = (JSONObject) jsonArray.get(i);
				PremiumInfo premiumInfo = new PremiumInfo();
				premiumInfo.setPremiumItemName(itemJson.getString("premiumItemName"));
				premiumInfo.setPremiumItemNo(itemJson.getString("premiumItemNo"));
				premiumInfo.setTargetCount(itemJson.getInt("targetCount"));
				premiumInfo.setTargetPrice(itemJson.getString("targetPrice"));
				premiumList.add(premiumInfo);
			}
		}
		item.setPremiumList(premiumList);

		item.setItemClass(json.getString("itemClass"));
		item.setAgeRestrictedFlag(json.getInt("ageRestrictedFlag"));
		item.setCouponFlag(json.getString("couponFlag"));
		item.setRetailStoreId(json.getString("retailStoreId"));
		item.setEventId(json.getString("eventId"));
		item.setEventName(json.getString("eventName"));
		item.setEventSalesPrice(json.getDouble("eventSalesPrice"));
		item.setEmpPrice1(json.getDouble("empPrice1"));
		item.setPSType(json.getString("pstype"));
		item.setOrgSalesPrice1(json.getDouble("orgSalesPrice1"));
		if (!"null".equals(json.getString("mixMatchCode"))) {
			item.setMixMatchCode(json.getString("mixMatchCode"));
			item.setInheritFlag(json.getString("inheritFlag"));
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
			item.setNote(json.getString("note"));
		}
		item.setMdVender(json.getString("mdVender"));
		item.setReplaceSupportdiscountAmt(json.getInt("replaceSupportdiscountAmt"));
		item.setSubNum2(json.getInt("subNum2"));
		item.setSalesPriceFrom(json.getString("salesPriceFrom"));
		item.setOldPrice(json.getDouble("oldPrice"));
		item.setCostPrice1(json.getDouble("costPrice1"));
		item.setMakerPrice(json.getDouble("makerPrice"));
		item.setConn1(json.getString("conn1"));
		item.setConn2(json.getString("conn2"));
		item.setPluPrice(json.getDouble("pluPrice"));
		item.setDptNameLocal(json.getString("dptNameLocal"));
		item.setClassNameLocal(json.getString("classNameLocal"));
		item.setGroupName(json.getString("groupName"));
		item.setGroupID(json.getString("groupID"));
		item.setNameText(json.getString("nameText"));
		item.setDptSubCode1(json.getString("dptSubCode1"));
		item.setOldSku(json.getString("oldSku"));
		item.setSizeCode(json.getString("sizeCode"));
		item.setColorCode(json.getString("colorCode"));
		item.setDiscountRate(json.getDouble("discountRate"));
		item.setDiscountCount(json.getInt("discountCount"));

		if (!"null".equals(json.getString("qrPromotionId"))) {
			item.setQrBmpFileCount(json.getString("qrBmpFileCount"));
			item.setQrBmpFileFlag(json.getString("qrBmpFileFlag"));
			item.setQrBmpFileName(json.getString("qrBmpFileName"));
			item.setQrMinimumPrice(
					null == json.get("qrMinimumPrice") || "null".equals(json.get("qrMinimumPrice").toString()) ? null
							: json.getDouble("qrMinimumPrice"));
			item.setQrOutputTargetValue(json.getString("qrOutputTargetValue"));
			item.setQrPromotionId(json.getString("qrPromotionId"));
			item.setQrPromotionName(json.getString("qrPromotionName"));
		}

		List<QrCodeInfo> list = new ArrayList<QrCodeInfo>();
		if (json.get("qrCodeList") != null) {
			JSONArray jsonArray = new JSONArray(json.getString("qrCodeList"));
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemJson = (JSONObject) jsonArray.get(i);
				QrCodeInfo qr = new QrCodeInfo();
				qr.setBmpFileCount(itemJson.getString("bmpFileCount"));
				qr.setBmpFileFlag(itemJson.getString("bmpFileFlag"));
				qr.setBmpFileName(itemJson.getString("bmpFileName"));
				qr.setMinimumPrice(itemJson.getDouble("minimumPrice"));
				qr.setOutputTargetValue(itemJson.getString("outputTargetValue"));
				qr.setPromotionId(itemJson.getString("promotionId"));
				qr.setPromotionName(itemJson.getString("promotionName"));
				qr.setOutputType(itemJson.getString("outputType"));
				list.add(qr);
			}
		}
		item.setQrCodeList(list);

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
			@ApiParam(name = "workstationid", value = "作業台コード") @FormParam("workstationid") final String workStationId,
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
					promotion.setMap(map);
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

}
