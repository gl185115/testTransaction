package ncr.res.mobilepos.searchapi.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.searchapi.constant.SearchApiConstants;
import ncr.res.mobilepos.searchapi.helper.UrlConnectionHelper;
import ncr.res.mobilepos.searchapi.model.JSONData;

/**
 * SearchApiResource class is a web resourse which provides support for search
 * customer.
 */
@Path("/searchapi")
public class SearchApiResource {

	/** A private member variable used for the servlet context. */
	@Context
	private ServletContext context;

	/** The instance of the trace debug printer. */
	private Trace.Printer tp = null;

	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance();

	/** */
	private static final String PROG_NAME = "SearchApiResource";

	/**
	 * constructor.
	 */
	public SearchApiResource() {
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
				getClass());
	}

	/*
	 * ÇoÇnÇrè§ïiÉfÅ[É^API
	 */
	@Path("/productData")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public final JSONData getProductData(
			@FormParam("Body") String body) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("Body", body);

		JSONData jsonData = new JSONData();
		JSONObject result = null;
		try {
			JSONObject header = new JSONObject();
			header.put("system_id", SearchApiConstants.SYSTEM_ID);
			header.put("webapi", SearchApiConstants.WEVAPI_ID_3);

			ArrayList<JSONObject> valueList = new ArrayList<JSONObject>();
			JSONObject value = new JSONObject();
			value.put("body", body);
			value.put("header", header);
			
			JSONObject valueResult = new JSONObject();
			valueList.add(value);
			valueResult.put("Values", valueList);
			String json = valueResult.toString().replace("\\", "")
					.replace("\"", "\\\"").replace(" ", "");
			String address = context
					.getAttribute(GlobalConstant.INVENTORYORDERSEARCHURL)
					+ json;
			result = UrlConnectionHelper.connectionForGet(address);

			// Check if error is empty.
			if (StringUtility.isNullOrEmpty(result)) {
				// Error occurred, Abnormal return
				jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
			} else {
				// No error, Normal return
				jsonData.setJsonObject(result.toString());
				jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
			}

		} catch (MalformedURLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
					+ "Failed to get product data.\n", e);
			jsonData
					.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData.setMessage(e.getMessage());
		} catch (IOException e) {
 			if (e instanceof UnknownHostException) {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get product data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData.setMessage(e.getMessage());
			} else {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get product data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData.setMessage(e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ "Failed to get product data.\n", e);
			jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData.setMessage(e.getMessage());
		} finally {
			tp.methodExit(jsonData);
		}
		return jsonData;
	}
	
	/*
	 * ÇoÇnÇrêÊçséÛíçÉfÅ[É^API
	 */
	@Path("/advanceOrderData")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public final JSONData getAdvanceOrderData(
			@FormParam("Body") String body) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("Body", body);

		JSONData jsonData = new JSONData();
		JSONObject result = null;
		try {
			JSONObject header = new JSONObject();
			header.put("system_id", SearchApiConstants.SYSTEM_ID);
			header.put("webapi", SearchApiConstants.WEVAPI_ID_3);
			
			ArrayList<JSONObject> valueList = new ArrayList<JSONObject>();
			JSONObject value = new JSONObject();
			
			value.put("body", body);
			value.put("header", header);
			
			JSONObject valueResult = new JSONObject();
			valueList.add(value);
			valueResult.put("Values", valueList);

			String json = valueResult.toString().replace("\\", "")
					.replace("\"", "\\\"").replace(" ", "");
			String address = context
					.getAttribute(GlobalConstant.INVENTORYORDERSEARCHURL)
					+ json;
			result = UrlConnectionHelper.connectionForGet(address);

			// Check if error is empty.
			if (StringUtility.isNullOrEmpty(result)) {
				// Error occurred, Abnormal return
				jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
			} else {
				// No error, Normal return
				jsonData.setJsonObject(result.toString());
				jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
			}

		} catch (MalformedURLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
					+ "Failed to advance Order Data.\n", e);
			jsonData
					.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData.setMessage(e.getMessage());
		} catch (IOException e) {
			if (e instanceof UnknownHostException) {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to advance Order Data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData.setMessage(e.getMessage());
			} else {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to advance Order Data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData.setMessage(e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ "Failed to advance Order Data.\n", e);
			jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData.setMessage(e.getMessage());
		} finally {
			tp.methodExit(jsonData);
		}
		return jsonData;
	}
	
	/*
	 * ÇoÇnÇrîÑÇËì¶ÇµÉfÅ[É^API
	 */
	@Path("/sellMissData")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public final JSONData getSellMissData(
			@FormParam("Body") String body) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("Body", body);
		
		JSONData jsonData = new JSONData();
		JSONObject result = null;
		try {
			JSONObject header = new JSONObject();
			header.put("system_id", SearchApiConstants.SYSTEM_ID);
			header.put("webapi", SearchApiConstants.WEVAPI_ID_3);
			
			ArrayList<JSONObject> valueList = new ArrayList<JSONObject>();
			JSONObject value = new JSONObject();
			value.put("body", body);
			value.put("header", header);
			
			JSONObject valueResult = new JSONObject();
			valueList.add(value);
			valueResult.put("Values", valueList);

			String json = valueResult.toString().replace("\\", "")
					.replace("\"", "\\\"").replace(" ", "");
			String address = context
					.getAttribute(GlobalConstant.INVENTORYORDERSEARCHURL)
					+ json;
			result = UrlConnectionHelper.connectionForGet(address);

			// Check if error is empty.
			if (StringUtility.isNullOrEmpty(result)) {
				// Error occurred, Abnormal return
				jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
			} else {
				// No error, Normal return
				jsonData.setJsonObject(result.toString());
				jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
			}

		} catch (MalformedURLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
					+ "Failed to get sell Miss Data.\n", e);
			jsonData
					.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData.setMessage(e.getMessage());
		} catch (IOException e) {
			if (e instanceof UnknownHostException) {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get sell Miss Data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData.setMessage(e.getMessage());
			} else {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get sell Miss Data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData.setMessage(e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ "Failed to get sell Miss Data.\n", e);
			jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData.setMessage(e.getMessage());
		} finally {
			tp.methodExit(jsonData);
		}
		return jsonData;
	}
	
	/*
	 * ÇoÇnÇrî≠íçÉfÅ[É^API
	 */
	@Path("/orderData")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public final JSONData getOrderData(
			@FormParam("Body") String body) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("Body", body);
		JSONObject result = null;
		JSONData jsonData = new JSONData();

		try {

			JSONObject header = new JSONObject();
			header.put("system_id", SearchApiConstants.SYSTEM_ID);
			header.put("webapi", SearchApiConstants.WEVAPI_ID_3);
			
			ArrayList<JSONObject> valueList = new ArrayList<JSONObject>();
			JSONObject value = new JSONObject();
			value.put("body", body);
			value.put("header", header);
			
			JSONObject valueResult = new JSONObject();
			valueList.add(value);
			valueResult.put("Values", valueList);
			
			String json = valueResult.toString().replace("\\", "")
					.replace("\"", "\\\"").replace(" ", "");
			String address = context
					.getAttribute(GlobalConstant.INVENTORYORDERSEARCHURL)
					+ json;
			result = UrlConnectionHelper.connectionForGet(address);
			
			// Check if error is empty.
			if (StringUtility.isNullOrEmpty(result)) {
				// Error occurred, Abnormal return
				jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
			} else {
				// No error, Normal return
				jsonData.setJsonObject(result.toString());
				jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
			}
		} catch (MalformedURLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
					+ "Failed to get order data.\n", e);
			jsonData
					.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData.setMessage(e.getMessage());
		} catch (IOException e) {
			if (e instanceof UnknownHostException) {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get order data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData.setMessage(e.getMessage());
			} else {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get order data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData.setMessage(e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ "Failed to get order data.\n", e);
			jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData.setMessage(e.getMessage());
		} finally {
			tp.methodExit(jsonData);
		}
		return jsonData;
	}

	/*
	 * ÇoÇnÇrç›å…ÉfÅ[É^API
	 */
	@Path("/inventoryData")
	@POST
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	public final JSONData getInventoryData(
			@FormParam("Body") String body) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("Body", body);

		JSONData jsonData = new JSONData();
		JSONObject result = null;
		try {

			JSONObject header = new JSONObject();
			header.put("system_id", SearchApiConstants.SYSTEM_ID);
			header.put("webapi", SearchApiConstants.WEVAPI_ID_3);
			
			
			ArrayList<JSONObject> valueList = new ArrayList<JSONObject>();
			JSONObject value = new JSONObject();
			value.put("body", body);
			value.put("header", header);
			
			JSONObject valueResult = new JSONObject();
			valueList.add(value);
			valueResult.put("Values", valueList);

			String json = valueResult.toString().replace("\\", "")
					.replace("\"", "\\\"").replace(" ", "");
			String address = context
					.getAttribute(GlobalConstant.INVENTORYORDERSEARCHURL)
					+ json;
			result = UrlConnectionHelper.connectionForGet(address);

			// Check if error is empty.
			if (StringUtility.isNullOrEmpty(result)) {
				// Error occurred, Abnormal return
				jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
				jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
			} else {
				// No error, Normal return
				jsonData.setJsonObject(result.toString());
				jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
			}
		} catch (MalformedURLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
					+ "Failed to get inventory data.\n", e);
			jsonData
					.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
			jsonData.setMessage(e.getMessage());
		} catch (IOException e) {
			if (e instanceof UnknownHostException) {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get inventory data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
				jsonData.setMessage(e.getMessage());
			} else {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
						+ "Failed to get inventory data.\n", e);
				jsonData
						.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData
						.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				jsonData.setMessage(e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ "Failed to get inventory data.\n", e);
			jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData
					.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			jsonData.setMessage(e.getMessage());
		} finally {
			tp.methodExit(jsonData);
		}
		return jsonData;
	}

}
