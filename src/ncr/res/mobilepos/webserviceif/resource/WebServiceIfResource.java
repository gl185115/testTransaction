package ncr.res.mobilepos.webserviceif.resource;

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
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.webserviceif.constants.WebServiceIfConstants;
import ncr.res.mobilepos.webserviceif.helper.UrlConnectionHelper;
import ncr.res.mobilepos.webserviceif.model.JSONData;


    @Path("/WebServiceIf")
    @Api(value="/WebServiceIf", description="WebServiceIf")
    public class WebServiceIfResource {

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
        public WebServiceIfResource() {
            tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                    getClass());
        }

    @Path("/getSalesCharge")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="???|??????????", response=JSONData.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
    @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
    @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
    @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

    })
    public final JSONData getSalesChargeAPI(@ApiParam(name="Data", value="?f?[?^") @FormParam("Data") String Data) {

        String functionName = "getSalesChargeAPI";
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
            int timeOut = GlobalConstant.getApiServerTimeout();
            String apiUrl = GlobalConstant.getApiServerUrl();
            address = apiUrl + WebServiceIfConstants.SALESCHARGEAPI_SEACHER_URL + value;
            result = UrlConnectionHelper.connectionForGet(address, timeOut);
            if (result == null) {
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
        @ApiOperation(value="???????_?????|?[?g??????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getTransactionReport(
        		@ApiParam(name="apiData", value="API?f?[?^") @FormParam("apiData") String apiData,
        		@ApiParam(name="reportType", value="???|?[?g?^?C?v") @FormParam("reportType") String reportType) {
            String functionName = "getTransactionReport";
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            tp.println("reportType", reportType);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            String apiUrl = "";
            try {
                apiUrl = GlobalConstant.getApiServerUrl();
                if (WebServiceIfConstants.TRASACTIONREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.TRASACTION_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.CREDITREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.CREDIT_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.OPERATORREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.OPERATOR_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.SALESPERSONREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.SALESPERSON_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.TIMEZONEREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.TIMEZONE_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.TARGETREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.TARGET_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.COMPANYNOREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.COMPANYNO_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.SINGLEITEMREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.SINGLEITEM_REPORT_URL + apiData;
                }
                int timeOut = GlobalConstant.getApiServerTimeout();
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
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
        @ApiOperation(value="???o???_?????|?[?g??????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getCashInOutReport(@ApiParam(name="apiData", value="API?f?[?^")  @FormParam("apiData") String apiData) {
            String functionName = "getCashInOutReport";
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                address = GlobalConstant.getApiServerUrl() + WebServiceIfConstants.CASHINOUT_REPORT_URL + apiData;
                int timeOut = GlobalConstant.getApiServerTimeout();
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
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
        @ApiOperation(value="?O???[?v?_?????|?[?g??????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getGroupReport(
        		@ApiParam(name="apiData", value="API?f?[?^") @FormParam("apiData") String apiData,
        		@ApiParam(name="reportType", value="???|?[?g???^?C?v") @FormParam("reportType") String reportType) {
            String functionName = "getGroupReport";
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            tp.println("reportType", reportType);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            String apiUrl = "";
            try {
                apiUrl = GlobalConstant.getApiServerUrl();
                if (WebServiceIfConstants.GROUPREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.GROUP_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.DEPTREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.DEPT_REPORT_URL + apiData;
                } else if (WebServiceIfConstants.CLASSREPORTNAME.equals(reportType)) {
                    address = apiUrl + WebServiceIfConstants.CLASS_REPORT_URL + apiData;
                }
                int timeOut = GlobalConstant.getApiServerTimeout();
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
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
        @ApiOperation(value="?I?y???[?^?[?????|?[?g??????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getOperationReport(@ApiParam(name="apiData", value="API?f?[?^") @FormParam("apiData") String apiData) {
            String functionName = "getOperationReport";
            tp.methodEnter(functionName);
            tp.println("apiData", apiData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                address = GlobalConstant.getApiServerUrl() + WebServiceIfConstants.OPERATION_REPORT_URL + apiData;
                int timeOut = GlobalConstant.getApiServerTimeout();
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
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
        @ApiOperation(value="?W???[?i????????????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getTransaction(
        		@ApiParam(name="APIType", value="API?^?C?v") @FormParam("APIType") String APIType,
        		@ApiParam(name="JournalData", value="?W???[?i???f?[?^") @FormParam("JournalData") String JournalData) {
            String functionName = "getTransaction";
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
                int timeOut = GlobalConstant.getApiServerTimeout();
                String apiUrl = GlobalConstant.getApiServerUrl();
                if (WebServiceIfConstants.APITYPE.equals(APIType)) {
                    address = apiUrl + WebServiceIfConstants.JOURNALDETAILS_URL;
                    result = UrlConnectionHelper.connectionForPost(address, JournalData, timeOut);
                } else {
                    address = apiUrl + WebServiceIfConstants.JOURNALLIST_URL + JournalData;
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
         * @param Data
         *            the JournalData
         * @return the journal info.
         */
        @Path("/hhtUpdate")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        @ApiOperation(value="HHT???????????X?V", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData hhtUpdate(
        		@ApiParam(name="Data", value="?f?[?^") @FormParam("Data") String Data) {
            String functionName = "hhtUpdate";
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
                int timeOut = GlobalConstant.getApiServerTimeout();
                String apiUrl = GlobalConstant.getApiServerUrl();
                String address = apiUrl + WebServiceIfConstants.PROCESSINGTRAN_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address, Data, timeOut);

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
         * get HHT Transaction List and Detile data
         * @param param
         * @return
         */
        @Path("/gethhtinfo")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        @ApiOperation(value="HHT???????X?g????????????????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getHHTInfo(@ApiParam(name="param", value="?p?????[?^") @FormParam("param") String param) {

            String functionName = "getHHTInfo";
            tp.methodEnter(functionName);
            tp.println("param", param);

            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String companyCode = "";
            try {
                int timeOut = GlobalConstant.getApiServerTimeout();

                String apiUrl = GlobalConstant.getApiServerUrl();
                String address = apiUrl + WebServiceIfConstants.PROCESSINGTRAN_URL + param;
                result = UrlConnectionHelper.connectionForGet(address, timeOut);
                if (result == null) {
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
                            json.put("RepictName", "????????");
                            //When the SetType is '1' then  the data don't return to page
                            if("1".equals(json.getString("SetType"))){
                                removeArray.add(json);
                            }
                            Item item = itemDao.getItemByApiData(json.getString("MdInternal"), companyCode);
                            if (null != item) {
                                newJsonArray.add(addJson(json, item));
                            }else{
                                newJsonArray.add(json);
                            }
                            Double priceDiscountAmt = !"null".equals(json.getString("PriceDiscountAmt")) ? json.getDouble("PriceDiscountAmt") : 0;
                            // get the same setCode make a setBind
                            if(!"null".equals(json.getString("SetNo")) && !"0".equals(json.getString("SetNo"))){
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
            json.put("Colorkananame", item.getColorkananame());
            json.put("SizeKanaName", item.getSizeKanaName());
            return json;
        }
        /**
         * get PendingTran data
         * @param Data
         * @return
         */
        @Path("/PendingTran")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        @ApiOperation(value="??????????????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???["),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????")
        })
        public final JSONData getPendingTranInfo(
        		@ApiParam(name="Data", value="?f?[?^") @FormParam("Data") String Data){

        	String functionName = "getPendingTranInfo";
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
                 int timeOut = GlobalConstant.getApiServerTimeout();
                 String apiUrl = GlobalConstant.getApiServerUrl();
                 address = apiUrl + WebServiceIfConstants.PENDINGTRANAPI_SEACHER_URL;
                 result = UrlConnectionHelper.connectionForPost(address, Data, timeOut);

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
         * @param Data
         * @return
         */
        @Path("/getpricingsearch")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        @ApiOperation(value="???????i????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_ERROR_ITEMIDNOTFOUND, message="?w???????iID?????G???[ (???t????????)"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s?B"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getPricingSearch(
        		@ApiParam(name="Data", value="?f?[?^") @FormParam("Data") String Data) {

        	String functionName = "getPricingSearch";
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
                 int timeOut = GlobalConstant.getApiServerTimeout();
                 String apiUrl = GlobalConstant.getApiServerUrl();
                 address = apiUrl + WebServiceIfConstants.ITEMPRICEAPI_SEACHER_URL;
                 result = UrlConnectionHelper.connectionForPost(address, Data, timeOut);

                if (result == null) {
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
        @ApiOperation(value="?????????X?V", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getPendingTranUpdate(@ApiParam(name="apiData", value="API?f?[?^") @FormParam("apiData") String apiData){
            String functionName = "getPendingTranUpdate";
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
                int timeOut = GlobalConstant.getApiServerTimeout();
                String apiUrl = GlobalConstant.getApiServerUrl();
                address = apiUrl + WebServiceIfConstants.PENDINGTRANAPI_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);
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
        @ApiOperation(value="?v???~?A???E?A?C?e???????X??????????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getPremiumItemStore(
        		@ApiParam(name="apiData", value="API?f?[?^") @FormParam("apiData") String apiData){
            String functionName = "getPremiumItemStore";
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
                int timeOut = GlobalConstant.getApiServerTimeout();
                String apiUrl = GlobalConstant.getApiServerUrl();
                address = apiUrl + WebServiceIfConstants.PREMIUMITEMSTOREAPI_SEACHER_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);

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
        @ApiOperation(value="?v???~?A???E?A?C?e???????X?????????X?V????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getPremiumItemStoreUpdate(
        		@ApiParam(name="apiData", value="API?f?[?^") @FormParam("apiData") String apiData) {
            String functionName = "getPremiumItemStoreUpdate";
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
                int timeOut = GlobalConstant.getApiServerTimeout();
                String apiUrl = GlobalConstant.getApiServerUrl();
                address = apiUrl + WebServiceIfConstants.PREMIUMITEMSTOREAPI_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);

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
         * @return The SlipNoUpdate Data
         */
        @Path("/getslipnoupdate")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        @ApiOperation(value="?????????X?V", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getSlipNoUpdate(
        		@ApiParam(name="apiData", value="API?f?[?^") @FormParam("apiData") String apiData){
            String functionName = "getSlipNoUpdate";
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
                int timeOut = GlobalConstant.getApiServerTimeout();
                String apiUrl = GlobalConstant.getApiServerUrl();
                address = apiUrl + WebServiceIfConstants.SLIPNOAPI_UPDATE_URL;
                result = UrlConnectionHelper.connectionForPost(address,apiData,timeOut);

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

        /**
         * Gets the pastel point info.
         *
         * @param requestData: The request data
         *
         * @return the point info.
         */
        @Path("/getPointInfo")
        @POST
        @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
        @ApiOperation(value="?|?C???g??????????????", response=JSONData.class)
        @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_SEARCHAPI, message="WOApi ?????G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="???N?G?X?g?p?????[?^???s??"),
        @ApiResponse(code=ResultBase.RES_MALFORMED_URL_EXCEPTION, message="URL????"),
        @ApiResponse(code=ResultBase.RES_ERROR_UNKNOWNHOST, message="?????[?g?z?X?g?????????????s"),
        @ApiResponse(code=ResultBase.RES_ERROR_IOEXCEPTION, message="I/O?G???["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="???p?G???[")

        })
        public final JSONData getPastelPointInfo(
                @ApiParam(name="requestData", value="?f?[?^") @FormParam("requestData") String requestData) {
            String functionName = "getPastelPointInfo";
            tp.methodEnter(functionName);
            tp.println("requestData", requestData);
            JSONData jsonData = new JSONData();
            JSONObject result = null;
            String address = "";
            try {
                if (StringUtility.isNullOrEmpty(requestData)) {
                    tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                    jsonData.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                    jsonData.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                    return jsonData;
                }
                int timeOut = GlobalConstant.getApiServerTimeout();
                address = WebServiceIfConstants.PASTELPOINT_APIURI;
                result = UrlConnectionHelper.connectionForPost(address, requestData, timeOut);
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
                        + "Failed to get point info.\n", e);
                jsonData
                        .setNCRWSSResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData
                        .setNCRWSSExtendedResultCode(ResultBase.RES_MALFORMED_URL_EXCEPTION);
                jsonData.setMessage(e.getMessage());
            } catch (IOException e) {
                if (e instanceof UnknownHostException) {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                            + "Failed to get point info.\n", e);
                    jsonData
                            .setNCRWSSResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData
                            .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_UNKNOWNHOST);
                    jsonData.setMessage(e.getMessage());
                } else {
                    LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_IO, functionName
                            + "Failed to get point info.\n", e);
                    jsonData
                            .setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData
                            .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
                    jsonData.setMessage(e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                        + "Failed to get point info.\n", e);
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
