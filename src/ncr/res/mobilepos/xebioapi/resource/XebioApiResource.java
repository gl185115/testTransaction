package ncr.res.mobilepos.xebioapi.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.xebioapi.constants.XebioApiConstants;
import ncr.res.mobilepos.xebioapi.helper.UrlConnectionHelper;
import ncr.res.mobilepos.xebioapi.model.JSONData;


    @Path("/xebioapi")
    public class XebioApiResource {

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
        public XebioApiResource() {
            tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                    getClass());
        }

    @Path("/updateSalesCharge")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final JSONData salesChargeUpdateAPI(@FormParam("Data") String Data) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("Data", Data);

        JSONData salesCharge = new JSONData();
        JSONObject result = null;
        String address = "";
        try {
            if (StringUtility.isNullOrEmpty(Data)) {
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                salesCharge.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                tp.methodExit(salesCharge.toString());
                return salesCharge;
            }
            String value = Data;
            int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
            String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
            address = apiUrl + XebioApiConstants.SALESCHARGEAPI_UPDATE_URL;
            result = UrlConnectionHelper.connectionForPost(address,value, timeOut);
            if (StringUtility.isNullOrEmpty(result)) {
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                salesCharge.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
            } else {
                salesCharge.setJsonObject(result.toString());
                salesCharge.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                salesCharge.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to update SalesCharge data.\n", e);
            salesCharge.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            salesCharge.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to update SalesCharge data.\n",
                        e);
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                salesCharge.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to update SalesCharge data.\n",
                        e);
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                salesCharge.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to update SalesCharge data.\n",
                    e);
            salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            salesCharge.setMessage(e.getMessage());
        } finally {
            tp.methodExit(salesCharge);
        }
        return salesCharge;
    }
        
    @Path("/getSalesCharge")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final JSONData getSalesChargeAPI(@FormParam("Data") String Data) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("Data", Data);

        JSONData salesCharge = new JSONData();
        JSONObject result = null;
        String address = "";
        try {
            if (StringUtility.isNullOrEmpty(Data)) {
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                salesCharge.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                tp.methodExit(salesCharge.toString());
                return salesCharge;
            }
            String value = Data;
            int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
            String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
            address = apiUrl + XebioApiConstants.SALESCHARGEAPI_SEACHER_URL + value;
            result = UrlConnectionHelper.connectionForGet(address, timeOut);
            if (StringUtility.isNullOrEmpty(result)) {
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                salesCharge.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
            } else {
                salesCharge.setJsonObject(result.toString());
                salesCharge.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                salesCharge.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (MalformedURLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get SalesCharge data.\n", e);
            salesCharge.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
            salesCharge.setMessage(e.getMessage());
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get SalesCharge data.\n", e);
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                salesCharge.setMessage(e.getMessage());
            } else {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get SalesCharge data.\n", e);
                salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                salesCharge.setMessage(e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to get SalesCharge data.\n", e);
            salesCharge.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            salesCharge.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            salesCharge.setMessage(e.getMessage());
        } finally {
            tp.methodExit(salesCharge);
        }
        return salesCharge;
    }
        /**
         * Gets the transaction report info.
         *
         * @param apiData
         *            the api Data
         * @param reportType
         *            the reportType
         * @return the transaction report
         */
        @Path("/getTransactionReport")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getTransactionReport(
                @FormParam("apiData") String apiData,
                @FormParam("reportType") String reportType) {
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            tp.println("reportType", reportType);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            String apiUrl = "";
            try {
                apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                if (XebioApiConstants.TRASACTIONREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.TRASACTION_REPORT_URL + apiData;
                } else if (XebioApiConstants.CREDITREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.CREDIT_REPORT_URL + apiData;
                } else if (XebioApiConstants.OPERATORREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.OPERATOR_REPORT_URL + apiData;
                } else if (XebioApiConstants.SALESPERSONREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.SALESPERSON_REPORT_URL + apiData;
                } else if (XebioApiConstants.TIMEZONEREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.TIMEZONE_REPORT_URL + apiData;
                } else if (XebioApiConstants.TARGETREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.TARGET_REPORT_URL + apiData;
                } else if (XebioApiConstants.COMPANYNOREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.COMPANYNO_REPORT_URL + apiData;
                } else if (XebioApiConstants.SINGLEITEMREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.SINGLEITEM_REPORT_URL + apiData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get transactionReport data.\n",
                        e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get transactionReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get transactionReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                        functionName + "Failed to get transactionReport data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        /**
         * Gets the CashInOut report info.
         *
         * @param apiData
         *            the api Data
         * @return the CashInOut report
         */
        @Path("/getCashInOutReport")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getCashInOutReport(@FormParam("apiData") String apiData) {
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                address = context.getAttribute(GlobalConstant.API_SERVER_URL).toString()
                        + XebioApiConstants.CASHINOUT_REPORT_URL + apiData;
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get transactionReport data.\n",
                        e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get CashInOutReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get CashInOutReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                        functionName + "Failed to get CashInOutReport data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        /**
         * Gets the Group report info.
         *
         * @param apiData
         *            the api Data
         * @param reportType
         *            the reportType
         * @return the Group report
         */
        @Path("/getGroupReport")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getGroupReport(
                @FormParam("apiData") String apiData,
                @FormParam("reportType") String reportType) {
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            tp.println("reportType", reportType);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            String apiUrl = "";
            try {
                apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                if (XebioApiConstants.GROUPREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.GROUP_REPORT_URL + apiData;
                } else if (XebioApiConstants.DEPTREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.DEPT_REPORT_URL + apiData;
                } else if (XebioApiConstants.CLASSREPORTNAME.equals(reportType)) {
                    address = apiUrl + XebioApiConstants.CLASS_REPORT_URL + apiData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get transactionReport data.\n",
                        e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get getGroupReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get getGroupReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                        functionName + "Failed to get getGroupReport data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        /**
         * Gets the Operation report info.
         *
         * @param apiData
         *            the api Data
         * @return the Operation report
         */
        @Path("/getOperationReport")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getOperationReport(@FormParam("apiData") String apiData) {
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                address = context.getAttribute(GlobalConstant.API_SERVER_URL).toString()
                        + XebioApiConstants.OPERATION_REPORT_URL + apiData;
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get transactionReport data.\n",
                        e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get getOperationReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to get getOperationReport data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                        functionName + "Failed to get getOperationReport data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
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
        public final JSONData getTransaction(
                @FormParam("APIType") String APIType,
                @FormParam("JournalData") String JournalData) {
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("APIType", APIType);
            tp.println("JournalData", JournalData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(JournalData)) {
                    tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return jsonData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                if (XebioApiConstants.APITYPE.equals(APIType)) {
                    address = apiUrl + XebioApiConstants.JOURNALDETAILS_URL;
                    result = UrlConnectionHelper.connectionForPost(address, JournalData, timeOut);
                } else {
                    address = apiUrl + XebioApiConstants.JOURNALLIST_URL + JournalData;
                    result = UrlConnectionHelper.connectionForGet(address, timeOut);
                }
                if (StringUtility.isNullOrEmpty(result)) {
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
                        + "Failed to get transaction data.\n", e);
                jsonData
                        .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData
                        .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                            + "Failed to get transaction data.\n", e);
                    jsonData
                            .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData
                            .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                            + "Failed to get transaction data.\n", e);
                    jsonData
                            .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData
                            .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                        + "Failed to get transaction data.\n", e);
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
         * forwardItems Update.
         *
         * @param APIType
         *            the APIType
         * @param JournalData
         *            the JournalData
         * @return the journal info.
         */
        @Path("/hhtUpdate")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData hhtUpdate(
                @FormParam("Data") String Data) {
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("Data", Data);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            try {
                if (StringUtility.isNullOrEmpty(Data)) {
                    tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return jsonData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                String address = apiUrl + XebioApiConstants.PROCESSINGTRAN_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address, Data, timeOut);
                
                if (StringUtility.isNullOrEmpty(result)) {
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
                        + "Failed to update hht data.\n", e);
                jsonData
                        .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData
                        .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                            + "Failed to update hht data.\n", e);
                    jsonData
                            .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData
                            .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                            + "Failed to update hht data.\n", e);
                    jsonData
                            .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData
                            .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                        + "Failed to update hht data.\n", e);
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
         * get HTT Transaction List and Detile data
         * 
         * @param ReferenceFlag
         * @param CompanyId
         * @param SlipNo
         * @param StoreCode
         * @param NotDicideFlag
         * @param TerminalNo
         * @param BusinessDateFrom
         * @param BusinessDateTo
         * @param Training
         * @param Count
         * @return
         */
        @Path("/gethhtinfo")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getHHTInfo(@FormParam("param") String param) {

            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("param", param);

            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String companyCode = "";
            try {
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());

                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                String address = apiUrl + XebioApiConstants.PROCESSINGTRAN_URL + param;
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
                if (StringUtility.isNullOrEmpty(result)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                    jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
                } else {
                    IItemDAO itemDao = new SQLServerItemDAO();
                    // the map Store setBindInfo
                    JSONArray setJsonArray = new  JSONArray();
                    Set<String> set = new HashSet<String>();
                    
                    JSONArray removeArray = new  JSONArray();
                    // if has the detailInfo Then get the plu table Info put in the
                    // detailInfo
                    if (!result.isNull("ProcessingDetailData")) {
                        JSONArray newJsonArray = new JSONArray();
                        // Get The CompanyId from Para
                        if (!StringUtility.isNullOrEmpty(param)) {
                            for (String companyId : param.split("&")) {
                                if (companyId.startsWith("CompanyId")) {
                                    companyCode = companyId.split("=").length > 0 ? companyId.split("=")[1] : "";
                                }
                            }
                        }
                        // get the itemInfo by Api Data 
                        JSONArray jsonArray = new JSONArray(result.getString("ProcessingDetailData"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            json.put("RepictName", "–¾×Š„ˆø");
                            //When the SetType is '1' then  the data don't return to page
                            if("1".equals(json.getString("SetType"))){
                                removeArray.add(json);
                            }
                            Item item = itemDao.getItemByApiData(json.getString("MdInternal"), companyCode);
                            if (null != item) {
                                newJsonArray.add(addJson(json, item));
                            }
                            Double priceDiscountAmt = json.get("PriceDiscountAmt") != null ? json.getDouble("PriceDiscountAmt") : 0;
                            // get the same setCode make a setBind
                            if(!"null".equals(json.getString("SetNo"))){
                                if(!set.contains(json.getString("SetNo"))){
                                    JSONObject setInfo = new JSONObject();
                                    setInfo.put("count", json.getInt("ItemCnt"));
                                    setInfo.put("code", json.getString("SetNo"));
                                    setInfo.put("pattern" ,"setPrice");
                                    if("1".equals(json.getString("SetType"))){
                                        setInfo.put("net" , -(json.getDouble("ItemTotalAmt") - priceDiscountAmt));
                                        setInfo.put("amount", priceDiscountAmt);
                                        setInfo.put("preAmount", json.getDouble("ItemTotalAmt"));
                                    } 
                                    setJsonArray.add(setInfo);
                                    set.add(json.getString("SetNo"));
                                }else{
                                    for(int j = 0; j < setJsonArray.length(); j++){
                                        JSONObject setInfo = setJsonArray.getJSONObject(j);
                                        if(setInfo.getString("code").equals(json.getString("SetNo"))){
                                            setInfo.put("count", setInfo.getInt("count") + json.getInt("ItemCnt"));
                                            if("1".equals(json.getString("SetType"))){
                                                setInfo.put("net" , -(json.getDouble("ItemTotalAmt") - priceDiscountAmt));
                                                setInfo.put("amount", priceDiscountAmt);
                                                setInfo.put("preAmount", json.getDouble("ItemTotalAmt"));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        newJsonArray.removeAll(removeArray);
                        if(newJsonArray.length() > 0){
                            result.put("ProcessingDetailData", newJsonArray);
                        }
                        result.put("SetBind", setJsonArray);
                    }
                    jsonData.setJsonObject(result.toString());
                    jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                    jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
                }

            } catch (MalformedURLException e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get Hht data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get Hht data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get Hht data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to get Hht data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        
        private JSONObject addJson(JSONObject json,Item item) throws JSONException{
            json.put("SubNum1", item.getSubNum1());
            json.put("SubNum2", item.getSubNum2());
            json.put("Md01", item.getMd01());
            json.put("Md02", item.getMd02());
            json.put("Md03", item.getMd03());
            json.put("Md04", item.getMd04());
            json.put("Md05", item.getMd05());
            json.put("Md06", item.getMd06());
            json.put("Md07", item.getMd07());
            json.put("Md08", item.getMd08());
            json.put("Md09", item.getMd09());
            json.put("Md10", item.getMd10());
            json.put("Md11", item.getMd11());
            json.put("Md12", item.getMd12());
            json.put("Md13", item.getMd13());
            json.put("Md14", item.getMd14());
            json.put("Md15", item.getMd15());
            json.put("Md16", item.getMd16());
            json.put("Dpt", item.getDepartment());
            json.put("DiscountType", item.getDiscountType());
            json.put("MdType", item.getMdType());
            json.put("Sku", item.getSku());
            json.put("PaymentType", item.getPaymentType());
            json.put("SubCode1", item.getSubCode1());
            json.put("SubCode2", item.getSubCode2());
            json.put("SubCode3", item.getSubCode3());
            json.put("MdVender", item.getMdVender());
            json.put("CostPrice1", item.getCostPrice1());
            json.put("MakerPrice", item.getMakerPrice());
            json.put("Conn1", item.getConn1());
            json.put("Conn2", item.getConn2());
            json.put("Line", item.getLine());
            json.put("Class", item.getItemClass());
            return json;
        }
        /**
         * get PendingTran data
         *  
         * @param ReferenceFlag
         * @param CompanyId
         * @param SlipNo
         * @param StoreCode
         * @param GlanceSlipFlag
         * @param Training
         * @param OperatorId
         * @param Application
         * @param Count
         * @return PendingTran data
         */
        @Path("/PendingTran")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getPendingTranInfo(
            @FormParam("Data") String Data){

        	String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("Data", Data);

            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(Data)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    tp.methodExit(jsonData.toString());
                    return jsonData;
                }
                 int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                 String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                 address = apiUrl + XebioApiConstants.PENDINGTRANAPI_SEACHER_URL;
                 result = UrlConnectionHelper.connectionForPost(address, Data, timeOut);
          
                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get PendingTran data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get PendingTran data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
              } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get PendingTran data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to get PendingTran data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        /**
         * get ItemPrice data
         *  
         * @param CompanyId
         * @param StoreCode
         * @param MdInternal
         * @param Training
         * @param OperatorId
         * @param Application
         * @return ItemPrice data
         */
        @Path("/getpricingsearch")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getPricingSearch(
            @FormParam("Data") String Data) {
        	
        	String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName);
            tp.println("Data", Data);
            
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(Data)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    tp.methodExit(jsonData.toString());
                    return jsonData;
                }
                 int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                 String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                 address = apiUrl + XebioApiConstants.ITEMPRICEAPI_SEACHER_URL;
                 result = UrlConnectionHelper.connectionForPost(address, Data, timeOut);
          
                if (StringUtility.isNullOrEmpty(result)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SEARCHAPI);
                    jsonData.setMessage(ResultBase.RES_SEARCHAPIERROR_MSG);
                } else if (result.isNull("PLUReportDate")) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_ITEMIDNOTFOUND);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_ITEMIDNOTFOUND);
                    jsonData.setMessage(ResultBase.RES_SEARCHItemIdERROR_MSG);
                } else {
                    jsonData.setJsonObject(result.toString());
                    jsonData.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                    jsonData.setMessage(ResultBase.RES_SUCCESS_MSG);
                }
            } catch (MalformedURLException e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get ItemPrice data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get ItemPrice data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
              } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to get ItemPrice data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to get ItemPrice data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        
        /**
         * get the PendingTranUpdate info
         * @param apiData PendingTranUpdate
         * @return  The PendingTranUpdate Data
         */
        @Path("/getpendingtranupdate")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getPendingTranUpdate(@FormParam("apiData") String apiData){
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName).println("apiData",apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(apiData)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    tp.methodExit(jsonData.toString());
                    return jsonData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                address = apiUrl + XebioApiConstants.PENDINGTRANAPI_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);
                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getPendingUpdate data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getPendingUpdate data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getPendingUpdate data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to getPendingUpdate data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        
        
        /**
         * get the PremiumItemStore info
         * @param apiData request Data
         * @return The PremiumItemStore Data
         */
        @Path("/getpremiumitemstore")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getPremiumItemStore(
                @FormParam("apiData") String apiData){
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName)
            .println("apiData",apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(apiData)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    tp.methodExit(jsonData.toString());
                    return jsonData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                address = apiUrl + XebioApiConstants.PREMIUMITEMSTOREAPI_SEACHER_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);

                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getPremiumItemStore.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getPremiumItemStore data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getPremiumItemStore data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to getPremiumItemStore data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        
        /**
         * get the PremiumItemStoreUpdate info
         * @param apiData The request Data
         * @return  The PremiumItemStoreUpdate Data
         */
        @Path("/getpremiumitemstoreupdate")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getPremiumItemStoreUpdate(
                @FormParam("apiData") String apiData) {
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName).println("apiData", apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(apiData)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    tp.methodExit(jsonData.toString());
                    return jsonData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                address = apiUrl + XebioApiConstants.PREMIUMITEMSTOREAPI_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);

                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getPremiumItemStoreUpdate.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to getPremiumItemStoreUpdate data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO,
                            functionName + "Failed to getPremiumItemStoreUpdate data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                        functionName + "Failed to getPremiumItemStoreUpdate data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        
        
        /**
         * get the PremiumItemStoreUpdate info
         * @param apiData The request Data
         * @return  The SlipNo Data
         */
        @Path("/getslipno")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getSlipNo(
                @FormParam("apiData") String apiData){
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName)
            .println("apiData",apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(apiData)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    tp.methodExit(jsonData.toString());
                    return jsonData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                address = apiUrl + XebioApiConstants.SLIPNOAPI_SEACHER_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);

                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getSlipNo.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getSlipNo data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getSlipNo data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to getSlipNo data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
        
        /**
         * get the PremiumItemStoreUpdate info
         * @param apiData The request Data
         * @return The SlipNoUpdate Data
         */
        @Path("/getslipnoupdate")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        public final JSONData getSlipNoUpdate(
                @FormParam("apiData") String apiData){
            String functionName = DebugLogger.getCurrentMethodName();
            tp.methodEnter(functionName)
            .println("apiData",apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(apiData)) {
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    tp.methodExit(jsonData.toString());
                    return jsonData;
                }
                int timeOut = Integer.valueOf(context.getAttribute(GlobalConstant.API_SERVER_TIMEOUT).toString());
                String apiUrl = context.getAttribute(GlobalConstant.API_SERVER_URL).toString();
                address = apiUrl + XebioApiConstants.SLIPNOAPI_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);

                if (StringUtility.isNullOrEmpty(result)) {
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
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getSlipNoUpdate.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getSlipNoUpdate data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName + "Failed to getSlipNoUpdate data.\n", e);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + "Failed to getSlipNoUpdate data.\n", e);
                jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
                jsonData.setMessage(e.getMessage());
            } finally {
                tp.methodExit(jsonData);
            }
            return jsonData;
        }
}
