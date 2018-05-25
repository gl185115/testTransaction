/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ForwardItemListResource
 *
 * Resource for Transfer transactions between smart phone and POS
 *
 */
package ncr.res.mobilepos.forwarditemlist.resource;

import java.util.List;

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

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.IForwardItemListDAO;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.forwarditemlist.model.ForwardItemCount;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.dao.ICommonDAO;
import ncr.res.mobilepos.journalization.dao.IPosLogDAO;
import ncr.res.mobilepos.journalization.model.ForwardList;
import ncr.res.mobilepos.journalization.model.ForwardListInfo;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.SearchForwardPosLog;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.webserviceif.model.JSONData;

/**
 * Transfer transactions between smart phone and POS.
 */
@Path("/ItemForward")
@Api(value="/ItemForward", description="�O�J��������OAPI")
public class ForwardItemListResource {
    /**
     * The IOWriter for the Log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Instance of SnapLogger for output error transaction data to snap file.
     */
    private SnapLogger snap;
    /**
     * The program name.
     */
    private static final String PROG_NAME = "ForwdItm";

    @Context
    private ServletContext context; //to access the web.xml

    /**
     * Constructor.
     */
    public ForwardItemListResource() {
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        this.snap =  (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * Sets the DaoFactory of the ForwardItemListResource to
     * use the DAO methods.
     *
     * @param daofactory
     *            - The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daofactory) {
    	//no implementation
    }

    /**
     * The method called by POS to get Forward data.
     * @param storeid      The Store ID
     * @param terminalid   The Terminal ID
     * @param txdate       The BusinessDate
     * @return Forward Item data
     * @deprecated
     */
    @Deprecated
//    @Path("/request")
//    @GET
//    @Produces({ MediaType.APPLICATION_XML + ";charset=SHIFT-JIS" })
    public final String requestForwardData(
            @QueryParam("storeid") final String storeid,
            @QueryParam("terminalid") final String terminalid,
            @QueryParam("txdate") final String txdate) {

        tp.methodEnter("requestForwardData");
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        String posLogXml = null;

        //store id is 4 digits in POS, so anyway set to 6 digits with 0.
        String actualstoreid = String.format("%6s", storeid).replace(" ", "0");

        try {
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = sqlServer
                    .getForwardItemListDAO();
            posLogXml = forwardItemListDAO.getShoppingCartData(actualstoreid,
                    terminalid, txdate);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.requestForwardData",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process requestForwardData.\n"
                    + e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.requestForwardData",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process requestForwardData.\n"
                    + e.getMessage());
        } finally {
            tp.methodExit(posLogXml);
        }
        return posLogXml;
    }

    /**
     * The method called by POS to get Forward data count.
     * @param storeid          The Store ID
     * @param terminalid       The Terminal ID
     * @param txdate           The BusinessDate
     * @return the count of Forward data
     * @deprecated
     */
    @Deprecated
    @Path("/getCount")
    @GET
    @Produces({ MediaType.APPLICATION_XML + ";charset=SHIFT-JIS" })
    @ApiOperation(value="�O�J���f�[�^���J�E���g", response=String.class)
    @ApiResponses(value={
    })
    public final String getCount(
            @ApiParam(name="storeid", value="�X�܃R�[�h") @QueryParam("storeid") final String storeid,
            @ApiParam(name="terminalid", value="�[���R�[�h") @QueryParam("terminalid") final String terminalid,
            @ApiParam(name="txdate", value="�c�Ɠ�") @QueryParam("txdate") final String txdate) {

        tp.methodEnter("getCount");
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        //store id is 4 digits in POS, so anyway set to 6 digits with 0.
        String actualstoreid = String.format("%6s", storeid).replace(" ", "0");

        ForwardCountData forwardCountData = new ForwardCountData();
        String countXml = null;
        try {
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = sqlServer
                    .getForwardItemListDAO();
            forwardCountData = forwardItemListDAO.getForwardCountData(
                    actualstoreid, terminalid, txdate);

            XmlSerializer<ForwardCountData> xmlSerializer =
                new XmlSerializer<ForwardCountData>();
            countXml = xmlSerializer.marshallObj(ForwardCountData.class,
                    forwardCountData, "shift_jis");
            countXml = countXml.replace(" standalone=\"yes\"", "");
        } catch (JAXBException e) {
            LOGGER.logAlert(PROG_NAME, "ForwardItemListResource.getCount",
                    Logger.RES_EXCEP_JAXB,
                    "Failed to process getCount.\n " + e.getMessage());
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, "ForwardItemListResource.getCount",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process getCount.\n " + e.getMessage());
        } finally {
            tp.methodExit(countXml);
        }
        return countXml;
    }

    /**
     * The method is called by Mobile to upload the
     * information of Shopping Cart as Forward Item Data.
     * @param poslogXml     The POSLog xml
     * @param terminalNo    The Terminal No
     * @param deviceNo      The Device Number
     * @return The POSLog Response {@see PosLogResp}
     * @deprecated
     */
    @Deprecated
    @Path("/uploadforward")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    @ApiOperation(value="�O�J���f�[�^���A�b�v���[�h����", response=PosLogResp.class)
    @ApiResponses(value={
    		 @ApiResponse(code=ResultBase.RES_FORWARD_ITEM_NO_INSERT, message="�f�[�^�}�����s"),
    })
    public final PosLogResp uploadForwardData(
    		@ApiParam(name="poslogxml", value="PoslogXml���") @FormParam("poslogxml") final String poslogXml,
    		@ApiParam(name="deviceid", value="���u�ԍ�") @FormParam("deviceid") final String deviceNo,
    		@ApiParam(name="terminalid", value="�[���ԍ�") @FormParam("terminalid") final String terminalNo) {

        tp.methodEnter("uploadForwardData");
        tp.println("POSLogXML", poslogXml).println("DeviceNo", deviceNo).
            println("TerminalNo", terminalNo);

        PosLogResp posLogResponse = null;
        try {
            DAOFactory sqlServer = DAOFactory
                    .getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = sqlServer
                    .getForwardItemListDAO();
            posLogResponse = forwardItemListDAO.uploadItemForwardData(deviceNo,
                    terminalNo, poslogXml);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    Logger.RES_EXCEP_DAO,
                    "Failed to process uploadForwardData.\n" + e.getMessage());
            Snap.SnapInfo info = snap.write("poslog xml data", poslogXml);
            LOGGER.logSnap(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    "Output error transaction data to snap file.", info);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process uploadForwardData.\n" + e.getMessage());
            Snap.SnapInfo info = snap.write("poslog xml data", poslogXml);
            LOGGER.logSnap(PROG_NAME,
                    "ForwardItemListResource.uploadForwardData",
                    "Output error transaction data to snap file.", info);
        } finally {
            tp.methodExit(posLogResponse);
        }
        return posLogResponse;
    }

    /**
     * �O�J���f�[�^�o�^
     * @param poslogxml
     * @param queue
     * @param workstationid
     * @param trainingmode
     * @param total
     * @return ResultBase
     */
    @POST
    @Path("/suspend")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�O�J���f�[�^�o�^", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXB�G���["),
        @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_DATEINVALID, message="�����ȃL���[�ԍ�"),
    })
    public final ResultBase saveForwardPosLog(
    		@ApiParam(name="poslogxml", value="PoslogXml���") @FormParam("poslogxml") final String poslogxml,
    		@ApiParam(name="queue", value="�L���[�ԍ�") @FormParam("queue") final String queue,
    		@ApiParam(name="workstationid", value="POS�R�[�h") @FormParam("workstationid") final String workstationid,
    		@ApiParam(name="trainingmode", value="�g���[�j���O���[�h") @FormParam("trainingmode") final String trainingmode,
    		@ApiParam(name="total", value="�g�[�^��") @FormParam("total") final String total) {

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
     * �O�J���f�[�^�擾
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
    @Path("/resume")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�O�J���f�[�^�擾", response=SearchForwardPosLog.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="����f�[�^�����o"),
    })
    public final SearchForwardPosLog getForwardItems(@ApiParam(name="CompanyId", value="��ЃR�[�h") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="�X�܃R�[�h") @FormParam("RetailStoreId") String RetailStoreId, 
    		@ApiParam(name="WorkstationId", value="POS�R�[�h") @FormParam("WorkstationId") String WorkstationId,
    		@ApiParam(name="SequenceNumber", value="����ԍ�") @FormParam("SequenceNumber") String SequenceNumber, 
    		@ApiParam(name="Queue", value="�L���[�ԍ�") @FormParam("Queue") String Queue,
    		@ApiParam(name="BusinessDayDate", value="POS�Ɩ����t") @FormParam("BusinessDayDate") String BusinessDayDate, 
    		@ApiParam(name="TrainingFlag", value="�g���[�j���O�t���O") @FormParam("TrainingFlag") String TrainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
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
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get poslog xml", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        }
        return (SearchForwardPosLog) tp.methodExit(poslog);
    }

    /**
     * �O�J���f�[�^�ꗗ�擾
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param TrainingFlag
     * @param LayawayFlag
     * @param Queue
     * @return ResultBase
     */
    @POST
    @Path("/list")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�O�J���f�[�^�ꗗ�擾", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
    })
    public final ResultBase getForwardList(
    		@ApiParam(name="CompanyId", value="��ЃR�[�h") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="�X�܃R�[�h") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="TrainingFlag", value="�g���[�j���O�t���O") @FormParam("TrainingFlag") String TrainingFlag,
    		@ApiParam(name="LayawayFlag", value="�\��t���O") @FormParam("LayawayFlag") String LayawayFlag,
    		@ApiParam(name="Queue", value="�L���[�ԍ�") @FormParam("Queue") String Queue,
            @ApiParam(name="TxType", value="����^�C�v") @FormParam("TxType") String TxType) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.println("CompanyId", CompanyId);
        tp.println("StoreCode", RetailStoreId);
        tp.println("Training", TrainingFlag);
        tp.println("LayawayFlag", LayawayFlag);
        tp.println("Queue", Queue);
        tp.println("TxType", TxType);

        ForwardList result = new ForwardList();
        try {
            if (StringUtility.isNullOrEmpty(RetailStoreId)) {
                tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
                result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
                return result;
            }

            DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            ICommonDAO iCommonDAO = sqlServer.getCommonDAO();
            List<ForwardListInfo> forwardList = iCommonDAO.getForwardList(CompanyId, RetailStoreId,
                    TrainingFlag, LayawayFlag, Queue, TxType);
            result.setForwardListInfo(forwardList);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get forward list.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(e.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        }
        return (ForwardList) tp.methodExit(result);
    }

    /**
     * �O�J���f�[�^���J�E���g
     * @param companyId
     * @param storeId
     * @param businessDayDate
     * @param workstationId
     * @param queue
     * @param trainingFlag
     * @return Forward Item Count
     */
    @GET
    @Path("/getforwarditemcount")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�O�J���f�[�^���J�E���g", response=ForwardItemCount.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    })
    public final ForwardItemCount getForwardItemCount(
    	@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
    	@ApiParam(name="storeId", value="�X�܃R�[�h") @QueryParam("storeId") final String storeId,
    	@ApiParam(name="businessDayDate", value="POS�Ɩ����t") @QueryParam("businessDayDate") final String businessDayDate,
    	@ApiParam(name="workstationId", value="POS�R�[�h") @QueryParam("workstationId") final String workstationId,
    	@ApiParam(name="queue", value="�L���[�ԍ�") @QueryParam("queue") final String queue,
    	@ApiParam(name="trainingFlag", value="�g���[�j���O�t���O") @QueryParam("trainingFlag") final String trainingFlag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
            .println("storeId", storeId)
            .println("businessDayDate", businessDayDate)
            .println("workstationId", workstationId)
            .println("queue", queue)
            .println("trainingFlag", trainingFlag);

        String count = null;
        ForwardItemCount result = new ForwardItemCount();
        try {
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IForwardItemListDAO forwardItemListDAO = dao.getForwardItemListDAO();
            count = forwardItemListDAO.selectForwardItemCount(companyId, storeId,
                    businessDayDate, workstationId, queue, trainingFlag);
            result.setCount(count);
            result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
                    + ": Failed to Select Forward Item Count of"
                    + " CompanyId=" + companyId
                    + ";StoreId=" + storeId
                    + ";Businessdaydate=" + businessDayDate
                    + ";WorkstationId=" + workstationId
                    + ";Queue=" + queue
                    + ";trainingFlag=" + trainingFlag
                    , daoEx);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(daoEx.getMessage());
        } catch(Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,functionName
                    + ": Failed to Select Forward Item Count of"
                    + " CompanyId=" + companyId
                    + ";StoreId=" + storeId
                    + ";Businessdaydate=" + businessDayDate
                    + ";WorkstationId=" + workstationId
                    + ";Queue=" + queue
                    + ";trainingFlag=" + trainingFlag
                    , ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        } finally {
            tp.methodExit();
        }
        return result;
    }

    /**
     * �O�J���f�[�^�X�e�[�^�X�X�V
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
    @Path("/request")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�O�J���f�[�^�X�e�[�^�X�X�V", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    })
    public final ResultBase updateForwardStatus(
    		@ApiParam(name="CompanyId", value="��ЃR�[�h") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="�X�܃R�[�h") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="WorkstationId", value="POS�R�[�h") @FormParam("WorkstationId") String WorkstationId,
    		@ApiParam(name="SequenceNumber", value="����ԍ�") @FormParam("SequenceNumber") String SequenceNumber,
    		@ApiParam(name="Queue", value="�L���[�ԍ�") @FormParam("Queue") String Queue,
    		@ApiParam(name="BusinessDayDate", value="POS�Ɩ����t") @FormParam("BusinessDayDate") String BusinessDayDate,
    		@ApiParam(name="TrainingFlag", value="�g���[�j���O�t���O") @FormParam("TrainingFlag") String TrainingFlag,
    		@ApiParam(name="Status", value="������") @FormParam("Status") int Status) {
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
            ICommonDAO iCommonDAO = sqlServer.getCommonDAO();
            int result = iCommonDAO.updateForwardStatus(CompanyId, RetailStoreId, WorkstationId, SequenceNumber,
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
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update Forward status.\n" + ex.getMessage());
            resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL, ex);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }
    
    /**
     * �O�J�� ���[�U�[�̌������擾
     *
     * @param CompanyId
     * @param RetailStoreId
     * @param WorkstationId
     * @param OperatorId
     */
    @POST
    @Path("/userPermission")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�O�J�� ���[�U�[�̌������擾", response=Operator.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
    })
    public final Operator UserPermission(
    		@ApiParam(name="CompanyId", value="��ЃR�[�h") @FormParam("CompanyId") String CompanyId,
    		@ApiParam(name="RetailStoreId", value="�X�܃R�[�h") @FormParam("RetailStoreId") String RetailStoreId,
    		@ApiParam(name="WorkstationId", value="���W�ԍ�") @FormParam("WorkstationId") String WorkstationId,
    		@ApiParam(name="OperatorId", value="���[�U�[ID") @FormParam("OperatorId") String OperatorId) {
    	
    	CredentialResource CredenRou = new CredentialResource();
    	Operator operator = CredenRou.getStatusOfOperator(CompanyId,OperatorId);
    	operator.setOperatorNo(OperatorId);
    	
        return operator;
    }

    /**
     * �^�O�ԍ��őO�J���f�[�^�o�^
     * @param companyid
     * @param retailstoreid
     * @param queue
     * @param workstationid
     * @param trainingmode
     * @param tag
     * @param total
     * @param poslogxml
     * @return ResultBase
     */
    @POST
    @Path("/SuspendWithTag")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�^�O�ԍ��őO�J���f�[�^�o�^", response=ResultBase.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXB�G���["),
        @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_DATEINVALID, message="�����ȃL���[�ԍ�"),
    })
    public final ResultBase saveForwardPosLogIncludeTag(
    		@ApiParam(name="companyid", value="��ЃR�[�h") @FormParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="�����X�R�[�h") @FormParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="queue", value="�L���[�ԍ�") @FormParam("queue") final String queue,
    		@ApiParam(name="workstationid", value="POS�R�[�h") @FormParam("workstationid") final String workstationId,
    		@ApiParam(name="trainingmode", value="�g���[�j���O���[�h") @FormParam("trainingmode") final String trainingMode,
    		@ApiParam(name="tag", value="�^�O�ԍ�") @FormParam("tag") final String tag,
    		@ApiParam(name="total", value="���v���z") @FormParam("total") final String total,
    		@ApiParam(name="poslogxml", value="Poslog Xml") @FormParam("poslogxml") final String posLogXml) {

    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
          .println("companyid", companyId)
          .println("retailstoreid", retailStoreId)
          .println("queue", queue)
          .println("workstationid", workstationId)
          .println("trainingmode", trainingMode)
          .println("tag", tag)
          .println("total", total)
          .println("poslogxml", posLogXml);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        ResultBase resultBase = new ResultBase();

        try {
            // unmarshall poslog xml
            XmlSerializer<PosLog> poslogSerializer = new XmlSerializer<PosLog>();
            PosLog posLog = poslogSerializer.unMarshallXml(posLogXml, PosLog.class);

            // check if valid poslog
            if (!POSLogHandler.isValid(posLog)) {
                tp.println("Required POSLog elements are missing.");
                Snap.SnapInfo info = snap.write("Required POSLog elements are missing.", posLogXml);
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
            int result = quebusterDAO.saveForwardPosLogIncludeTag(posLog, posLogXml, queue, tag, total);

            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                result = 0;
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Duplicate suspended transaction. It writes out to a snap file.");
                Snap.SnapInfo duplicatePOSLog = snap.write("Duplicate POSLog Transaction", posLogXml);
                LOGGER.logSnap(PROG_NAME, functionName, "Duplicate POSLog Transaction to snap file", duplicatePOSLog);
            }

            resultBase.setNCRWSSResultCode(result);

        } catch (DaoException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (JAXBException ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
            LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
                    "Failed to suspend transaction. Output error transaction data to snap file.",
                    infos);
            resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB, ResultBase.RES_ERROR_JAXB, ex);
        } catch (Exception ex) {
            Snap.SnapInfo[] infos = new Snap.SnapInfo[] { snap.write("poslog xml data", posLogXml), snap.write("Exception", ex) };
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
     * 	�^�O�ԍ��őO�J���f�[�^�擾
     *
     * @param companyId     	The Company ID
     * @param retailStoreId		The Retail Store ID
     * @param queue     		The Queue from POS
     * @param businessDayDate	The BusinessDay Date
     * @param tag				The Ext1
     * @return SearchForwardPosLog
     */
    @GET
    @Path("/ResumeWithTag")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�^�O�ԍ��őO�J���f�[�^�擾", response=SearchForwardPosLog.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_TXNOTFOUND, message="����f�[�^�����o")
        })
    public final SearchForwardPosLog getForwardItemsWithTag(
    		@ApiParam(name="CompanyId", value="��ЃR�[�h") @QueryParam("CompanyId") final String companyId,
    		@ApiParam(name="RetailStoreId", value="�����X�R�[�h") @QueryParam("RetailStoreId") final String retailStoreId,
    		@ApiParam(name="Queue", value="�L���[�ԍ�") @QueryParam("Queue") final String queue,
    		@ApiParam(name="Businessdaydate", value="�Ɩ����t") @QueryParam("Businessdaydate") final String businessDayDate,
    		@ApiParam(name="tag", value="�^�O�ԍ�") @QueryParam("tag") final String tag) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.println("CompanyId", companyId);
        tp.println("RetailStoreId", retailStoreId);
        tp.println("Queue", queue);
        tp.println("Businessdaydate", businessDayDate);
        tp.println("tag", tag);

        SearchForwardPosLog poslog = new SearchForwardPosLog();
        try {
               DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
               IPosLogDAO posLogDAO = sqlServer.getPOSLogDAO();
               poslog = posLogDAO.getForwardItemsPosLogWithTag(companyId, retailStoreId,
                		queue, businessDayDate, tag);

               if (StringUtility.isNullOrEmpty(poslog.getPosLogXml())) {
                    poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
                    tp.println("Forward poslog with tag not found.");
               } else {
                    XmlSerializer<PosLog> poslogSerializer = new XmlSerializer<PosLog>();
                    poslog.setPoslog(poslogSerializer.unMarshallXml(poslog.getPosLogXml(), PosLog.class));
               }
		} catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to get poslog xml with tag", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get poslog xml with tag", ex);
            poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        }
        return (SearchForwardPosLog) tp.methodExit(poslog);
    }

}
