/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* QueuebusterResource
*
* Web Service that provides Reports for the Mobile POS
*
* Campos, Carlos
*/
package ncr.res.mobilepos.queuebuster.resource;

import java.util.List;

import javax.servlet.ServletContext;
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
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuebuster.dao.IQueueBusterDAO;
import ncr.res.mobilepos.queuebuster.model.BusteredTransaction;
import ncr.res.mobilepos.queuebuster.model.BusteredTransactionList;
import ncr.res.mobilepos.queuebuster.model.CashDrawer;
import ncr.res.mobilepos.queuebuster.model.ResumedTransaction;
import ncr.res.mobilepos.queuebuster.model.SuspendData;
import ncr.res.mobilepos.queuesignature.model.SignatureRequestBill;

/**
 * QueueBusterResource is a Resource Class for QueueBustering.
 *
 */
@Path("/QueueSuspend")
@Api(value="/QueueSuspend", description="�ۗ�API")
public class QueueBusterResource {
    /**
     * The IOWriter for Log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Program name for IOWriter.
     */
    private static final String PROG_NAME = "QueueBus";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Instance of SnapLogger for output error transaction data to snap file.
     */
    private SnapLogger snap;
    /**
     * The method key which tells that a Suspend Data must be forwarded as
     * told from the specs for Disney Store.
     */
    private static final String METHOD_GET = "get";
    /**
     * The method key which tells that a Suspend Data must be canceled as told
     * from the specs for Disney Store.
     */
    private static final String METHOD_CANCEL = "cancel";
    /**
     * The method key which tells that a Suspend Data needs to be complete a
     * told from the specs for Disney Store.
     */
    private static final String METHOD_COMPLETE = "complete";

    @Context
    private ServletContext context;

    /** The Default Constructor. */
    public QueueBusterResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * The Web Method call to suspend a Transaction in QueueBuster Process.
     *
     * @param retailStoreId        The Retail Store ID
     * @param queue                The Queue
     * @param workstationId        The Workstation ID
     * @param sequenceNumber    The Sequence Number
     * @param posLogXml            The POSLog XML
     * @return    The JSON object that holds the result
     *          code for the Web Method {@see ResultBase}
     */
    @POST
    @Path("/suspend")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="TXL_INPROGRESS_ITEM", response=ResultBase.class)
    @ApiResponses(value={
    	    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_DATEINVALID, message="���t����"),
            @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXB�G���[")
        })
    public final ResultBase suspend(
    		@ApiParam(name="retailstoreid", value="�����X�R�[�h") @QueryParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="queue", value="�L���[�ԍ�") @QueryParam("queue") final String queue,
    		@ApiParam(name="workstationid", value="POS�R�[�h") @QueryParam("workstationid") final String workstationId,
    		@ApiParam(name="sequencenumber", value="����ԍ�") @QueryParam("sequencenumber") final String sequenceNumber,
    		@ApiParam(name="poslogxml", value="Poslog Xml") @FormParam("poslogxml") final String posLogXml) {

    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
          .println("retailstoreid", retailStoreId)
          .println("queue", queue)
          .println("workstationid", workstationId)
          .println("sequencenumber", sequenceNumber)
          .println("poslogxml", posLogXml);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        ResultBase resultBase = new ResultBase();

        try {
        	// unmarshall poslog xml
            XmlSerializer<PosLog> poslogSerializer =
            		new XmlSerializer<PosLog>();
            PosLog posLog = poslogSerializer.unMarshallXml(posLogXml,
            		PosLog.class);

        	// check if valid poslog
            if (!POSLogHandler.isValid(posLog)) {
            	tp.println("Required POSLog elements are missing.");
	            Snap.SnapInfo info = snap.write(
	            		"Required POSLog elements are missing.", posLogXml);
				LOGGER.logSnap(PROG_NAME, functionName,
						"Invalid POSLog Transaction to snap file", info);
            	resultBase.setMessage("Required POSLog elements are missing.");
            	resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            	return resultBase;

            }

            // check if valid business date format
            String businessDate = posLog.getTransaction().getBusinessDayDate();
            if (!DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
            	tp.println("BusinessDayDate should be in yyyy-MM-dd format.");
            	resultBase.setNCRWSSResultCode(
            			ResultBase.RESSYS_ERROR_QB_DATEINVALID);
            	resultBase.setMessage(
            			"BusinessDayDate should be in yyyy-MM-dd format.");
            	return resultBase;
            }

        	// suspend poslog
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueBusterDAO quebusterDAO = dao.getQueBusterDAO();
            int result = quebusterDAO.saveTransactionToQueue(posLog, posLogXml, queue);

			if (result == SQLResultsConstants.ROW_DUPLICATE) {
				result = 0;
				LOGGER.logWarning(PROG_NAME, functionName,
						Logger.RES_ERROR_RESTRICTION,
						"Duplicate suspended transaction for Store:"
								+ retailStoreId + ";Queue:" + queue
								+ ";TermId:" + workstationId + ";TxId:"
								+ sequenceNumber + ";TxDate:" + businessDate
								+ ". It writes out to a snap file.");
				Snap.SnapInfo duplicatePOSLog = snap.write(
						"Duplicate POSLog Transaction", posLogXml);
				LOGGER.logSnap(PROG_NAME, functionName,
						"Duplicate POSLog Transaction to snap file",
						duplicatePOSLog);
			}

            resultBase.setNCRWSSResultCode(result);

		} catch (DaoException ex) {
			Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
					snap.write("poslog xml data", posLogXml),
					snap.write("Exception", ex) };
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
					"Failed to suspend transaction for Store:" + retailStoreId
							+ ";Queue:" + queue + ";TermId:" + workstationId
							+ ";TxId:" + sequenceNumber
							+ ". Output error transaction data to snap file.",
					infos);
			resultBase = new ResultBase(ResultBase.RES_ERROR_DB,
					ResultBase.RES_ERROR_DB, ex);
		} catch (JAXBException ex) {
			Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
					snap.write("poslog xml data", posLogXml),
					snap.write("Exception", ex) };
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
					"Failed to suspend transaction for Store:" + retailStoreId
							+ ";Queue:" + queue + ";TermId:" + workstationId
							+ ";TxId:" + sequenceNumber
							+ ". Output error transaction data to snap file.",
					infos);
			resultBase = new ResultBase(ResultBase.RES_ERROR_JAXB,
					ResultBase.RES_ERROR_JAXB, ex);
		} catch (Exception ex) {
			Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
					snap.write("poslog xml data", posLogXml),
					snap.write("Exception", ex) };
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
					"Failed to suspend transaction for Store:" + retailStoreId
							+ ";Queue:" + queue + ";TermId:" + workstationId
							+ ";TxId:" + sequenceNumber
							+ ". Output error transaction data to snap file.",
					infos);
			resultBase = new ResultBase(ResultBase.RES_ERROR_GENERAL,
					ResultBase.RES_ERROR_GENERAL, ex);
		} finally {
			tp.methodExit(resultBase.toString());
		}
        return resultBase;
    }

    /**
     * The Web Method call to resume a Transaction in QueueBuster Process.
     *
     * @param retailStoreId     The Retail StoreID
     * @param queue             The Queue
     * @param workstationId     The Workstation ID
     * @param sequenceNumber    The Sequence Number
     * @param businessDayDate	The BusinessDayDate.
     * @return    The JSON object that holds the Resume
     *                  Transaction {@see ResumedTransaction}
     */
    @GET
    @Path("/resume")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�ۗ�����̍ĊJ", response=SearchedPosLog.class)
    @ApiResponses(value={
        	@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_TXINVALID, message="�����Ȏ��"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_TXALREADYRESUMED, message="�ĊJ�ςݎ��"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_TXNOTFOUND, message="�g�����U�N�V���������o"),
            @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_INVLDPRM, message="�����ȗv��"),
            @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXB�G���[")
        })
    public final SearchedPosLog resume(
    		@ApiParam(name="companyid", value="��ЃR�[�h") @QueryParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="�����X�R�[�h") @QueryParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="queue", value="�L���[�ԍ�") @QueryParam("queue") final String queue,
    		@ApiParam(name="workstationid", value="POS�R�[�h") @QueryParam("workstationid") final String workstationId,
    		@ApiParam(name="sequencenumber", value="����ԍ�") @QueryParam("sequencenumber") final String sequenceNumber,
    		@ApiParam(name="businessdaydate", value="POS�Ɩ����t") @QueryParam("businessdaydate") final String businessDayDate,
    		@ApiParam(name="trainingflag", value="�g���[�j���O�t���O") @QueryParam("trainingflag") final Integer trainingFlag) {
        SearchedPosLog poslog = new SearchedPosLog();
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
            .println("companyId", companyId)
            .println("retailstoreid", retailStoreId)
            .println("queue", queue)
            .println("workstationid", workstationId)
            .println("sequencenumber", sequenceNumber)
            .println("businessdaydate", businessDayDate)
            .println("trainingFlag", trainingFlag);

        ResumedTransaction resumedTransaction = new ResumedTransaction();
        resumedTransaction.setRetailStoreID(retailStoreId);
        resumedTransaction.setQueue(queue);
        resumedTransaction.setWorkstationID(workstationId);
        resumedTransaction.setSequenceNumber(sequenceNumber);

        try {
        	if (StringUtility.isNullOrEmpty(companyId, retailStoreId, queue,
        	        workstationId, sequenceNumber, businessDayDate) || trainingFlag == null  ) {
        		tp.println("Some of the parameters are null or empty.");
        		poslog.setNCRWSSResultCode(
        				ResultBase.RESSYS_ERROR_QB_INVLDPRM);
        	} else {
                DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
                IQueueBusterDAO quebusterDAO = dao.getQueBusterDAO();

                resumedTransaction = quebusterDAO.selectTransactionFromQueue(
                        companyId, retailStoreId, queue,
                        workstationId, sequenceNumber, businessDayDate, trainingFlag);

                XmlSerializer<SearchedPosLog> poslogSerializer =
                        new XmlSerializer<SearchedPosLog>();
                poslog =
                        poslogSerializer.unMarshallXml(resumedTransaction.getPoslog(),
                                SearchedPosLog.class);
                poslog.setNCRWSSResultCode(resumedTransaction.getNCRWSSResultCode());
                poslog.setNCRWSSExtendedResultCode(resumedTransaction.getNCRWSSExtendedResultCode());
			}
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to resume transaction for Store:"
							+ retailStoreId + ";Queue:" + queue + ";TermId:"
							+ workstationId + ";TxId:" + sequenceNumber
							+ ";TxDate=" + businessDayDate, ex);
			poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
		} catch (JAXBException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_JAXB,
					functionName + ": Failed to resume transaction for Store:"
							+ retailStoreId + ";Queue:" + queue + ";TermId:"
							+ workstationId + ";TxId:" + sequenceNumber
							+ ";TxDate=" + businessDayDate, ex);
			poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_JAXB);
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to resume transaction for Store:"
							+ retailStoreId + ";Queue:" + queue + ";TermId:"
							+ workstationId + ";TxId:" + sequenceNumber
							+ ";TxDate=" + businessDayDate, ex);
			poslog.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		}

        return (SearchedPosLog)tp.methodExit(poslog);
    }

    /**
     * The Web Method call used for retrieving
     *  the list of Transaction to be busted.
     *
     * @param retailStoreId        The Retail StoreID
     * @param queue                The Queue
     * @param workstationId        The Retail StationID
     * @param trainingFlag         The Training Flag
     * @return   The JSON Object that holds the list of transaction to be busted
     */
    @GET
    @Path("/list")
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�@������X�g�𓾂�", response=BusteredTransactionList.class)
    @ApiResponses(value={
    	@ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_LISTEMPTY, message="�L���[�̃o�X�^�[�E�g�����U�N�V�����̃��X�g����")
    })
    public final BusteredTransactionList list(
    		@ApiParam(name="companyid", value="��ЃR�[�h") @QueryParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h") @QueryParam("retailstoreid") final String retailStoreId,
    		@ApiParam(name="workstationid", value="��Ƒ�R�[�h") @QueryParam("workstationid") final String workstationId,
    		@ApiParam(name="queue", value="�����v���̗�W��") @QueryParam("queue") final String queue,
    		@ApiParam(name="trainingflag", value="�g���[�j���O�t���O") @QueryParam("trainingflag") final int trainingFlag) {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("retailstoreid", retailStoreId)
                .println("workstationid", workstationId)
                .println("queue", queue)
                .println("trainingflag", trainingFlag);

        BusteredTransactionList busteredTransactionList =
            new BusteredTransactionList();

        try {
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueBusterDAO quebuster = dao.getQueBusterDAO();

            List<BusteredTransaction> bustList =
                quebuster.listTransactionFromQueue(queue, companyId, retailStoreId, workstationId, trainingFlag);
            if (bustList.isEmpty()) {
                busteredTransactionList.setNCRWSSResultCode(
                        ResultBase.RESSYS_ERROR_QB_LISTEMPTY);
                tp.println("There are no List of Transaction for"
                        + " QueueBustering.");
            }

            busteredTransactionList.setBusteredTransactionList(bustList);

        } catch (DaoException ex) {
            LOGGER.logSnapException(
                    PROG_NAME,
                    Logger.RES_EXCEP_DAO,
                    functionName
                            + ": Failed to list suspended transactions of Store="
                            + retailStoreId + ";Station=" + workstationId +";Queue=" + queue + "Flag=" + trainingFlag, ex);
            busteredTransactionList = new BusteredTransactionList(
                    ResultBase.RES_ERROR_DB, ResultBase.RES_ERROR_DB, ex);
        } catch (Exception ex) {
            LOGGER.logSnapException(
                    PROG_NAME,
                    Logger.RES_EXCEP_GENERAL,
                    functionName
                            + ": Failed to list suspended transactions of Store="
                            + retailStoreId + ";Station=" + workstationId + ";Queue=" + queue + "Flag=" + trainingFlag, ex);
            busteredTransactionList = new BusteredTransactionList(
                    ResultBase.RES_ERROR_GENERAL, ResultBase.RES_ERROR_GENERAL,
                    ex);
        } finally {
            tp.methodExit(busteredTransactionList.toString());
        }
        return busteredTransactionList;
    }

    //FOR DISNEY STORE QUEUE BUSTER ===========================================
    /**
     * @deprecated
     * The Web Method call to forward a Transaction in QueueBuster Process.
     *
     * @param poslogxml            The POSLog XML
     * @return    The JSON object that holds the result
     *          code for the Web Method {@see ResultBase}
     */
    @Deprecated
    @POST
    @Path("/uploadforward")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�ۗ�����A�b�v���[�h", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_TXALREADY, message="����d���G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_JAXB, message="JAXB�G���[")
    })
    public final PosLogResp suspendTransactionToQueue(
            @ApiParam(name="poslogxml", value="POSLOG���") @FormParam("poslogxml") final String poslogxml) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("poslogxml", poslogxml);

		PosLogResp posLogResp = new PosLogResp();
        posLogResp.setStatus("0");

        try {
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueBusterDAO quebusterDAO = dao.getQueBusterDAO();

            int result = quebusterDAO.forwardTransactionToQueue(poslogxml);

            if (result == SQLResultsConstants.ROW_DUPLICATE) {
                posLogResp.setNCRWSSResultCode(ResultBase.RES_ERROR_TXALREADY);
                tp.println("Entry is duplicated.");
                posLogResp.setStatus("1");
				Snap.SnapInfo info = snap.write("posLog xml data", poslogxml);
				LOGGER.logSnap(PROG_NAME, functionName,
						"Output error transaction data to snap file.", info);
            }
        } catch (DaoException ex) {
        	Snap.SnapInfo[] infos = new Snap.SnapInfo[]{
        			snap.write("Poslog xml to upload.", poslogxml),
        			snap.write("Exception", ex)
        	};
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_DAO, functionName,
					"Output error transaction data to snap file.", infos);
			posLogResp.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            posLogResp.setStatus("1");
        } catch (JAXBException ex) {
        	Snap.SnapInfo[] infos = new Snap.SnapInfo[]{
        			snap.write("Poslog xml to upload.", poslogxml),
        			snap.write("Exception", ex)
        	};
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_JAXB, functionName,
					"Output error transaction data to snap file.", infos);
            posLogResp.setNCRWSSResultCode(ResultBase.RES_ERROR_JAXB);
            posLogResp.setStatus("1");
        } catch (Exception ex) {
			Snap.SnapInfo[] infos = new Snap.SnapInfo[] {
					snap.write("Poslog xml to upload.", poslogxml),
					snap.write("Exception", ex) };
			LOGGER.logSnap(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName,
					"Output error transaction data to snap file.", infos);
			posLogResp.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            posLogResp.setStatus("1");
        } finally {
            tp.methodExit(posLogResp.toString());
        }
        return posLogResp;
    }

    /**
     * The Web Method call to request get, cancel, complete a
     *  Transaction in QueueBuster Process.
     *
     * @param method               The request type
     * @param storeid              The storeid
     * @param terminalid           The terminalid where
     *                                  the transaction was made
     * @param txid                 The transaction id
     * @param txdate               The date of the transaction
     * @return    The JSON object that holds the result
     *          code for the Web Method {@see ResultBase}
     */
    @GET
    @Path("/request")
    @Produces({ MediaType.APPLICATION_XML })
    @ApiOperation(value="����̎擾�E����E�����v��", response=SuspendData.class)
    @ApiResponses(value={
    	@ApiResponse(code=ResultBase.RESSYS_ERROR_QB_TXNOTFOUND, message="��������o"),
        @ApiResponse(code=ResultBase.RESSYS_ERROR_QB_REQINVALID, message="�����ȗv��")
    })
    public final SuspendData requestToQueue(
    		@ApiParam(name="method", value="���@") @QueryParam("method") final String method,
    		@ApiParam(name="storeid", value="�X�܃R�[�h") @QueryParam("storeid") final String storeid,
    		@ApiParam(name="terminalid", value="�[���ԍ�") @QueryParam("terminalid") final String terminalid,
    		@ApiParam(name="txid", value="����ԍ�") @QueryParam("txid") final String txid,
    		@ApiParam(name="txdate", value="�������") @QueryParam("txdate") final String txdate) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("method", method)
				.println("storeid", storeid).println("terminalid", terminalid)
				.println("txid", txid).println("txdate", txdate);

        SuspendData suspendData = new SuspendData();

        try {
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueBusterDAO quebusterDAO = dao.getQueBusterDAO();

            int status = 0;
            if (method.equals(METHOD_GET)) {
                suspendData = quebusterDAO
                    .selectSuspendTransactionFromQueue(storeid, txdate,
                        terminalid, txid);
                return suspendData;
            } else if (method.equals(METHOD_CANCEL)) {
                status = SuspendData.CANCEL_FROM_POS;
            } else if (method.equals(METHOD_COMPLETE)) {
                status = SuspendData.PROCESS_COMPLETE;
            } else {
                suspendData.setStatus(SuspendData.ABNORMAL_END);
                suspendData.setErrormessage("Request is invalid");
                tp.println("An Abnormal End Occur. Request is invalid");
                return suspendData;
            }

            int result = quebusterDAO.updateQueuedTransactionStatus(
                                storeid, txdate, terminalid, txid, status);
            if (result != ResultBase.RES_OK) {
                String message = "";

                if (result == ResultBase.RESSYS_ERROR_QB_TXNOTFOUND) {
                    message = "Queued Transaction was not found";
                } else if (result == ResultBase.RESSYS_ERROR_QB_REQINVALID) {
                    message = "Request is invalid";
                }

                suspendData.setStatus(SuspendData.ABNORMAL_END);
                suspendData.setErrormessage(message);
                tp.println("An Abnormal End Occur. " + message);
                return suspendData;
            }
        } catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to request for Method:"
							+ method + ";Store=" + storeid + ";TermId="
							+ terminalid + ";TxId=" + txid + ";TxDate="
							+ txdate, ex);
			suspendData.setErrormessage("Request to " + method
					+ " transaction failed in the Queue.\n" + ex.getMessage());
			suspendData.setStatus(-1);
        } catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to request for Method:" + method
							+ ";Store=" + storeid + ";TermId=" + terminalid
							+ ";TxId=" + txid + ";TxDate=" + txdate, ex);
			suspendData.setErrormessage("Request to " + method
					+ " transaction failed in the Queue.\n" + ex.getMessage());
			suspendData.setStatus(-1);
        } finally {
            tp.methodExit(suspendData.toString());
        }
        return suspendData;
    }

    /**
     * Web Method call for Deleting ForWard Item.
     * @param storeId
     * @param businessDayDate
     * @return The JSON object that holds the
     *         ForWard Item Count for the Web Method.
     */
    @GET
    @Path("/deleteforwarditem")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�O�J�����i�폜", response=ResultBase.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o"),
        })
    public final ResultBase deleteForwardItem(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�܃R�[�h") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="businessDayDate", value="�c�Ɠ�") @QueryParam("businessDayDate") final String businessDayDate) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("companyid", companyId)
		  .println("storeid", storeId)
		  .println("businessDayDate", businessDayDate);

		ResultBase result = null;
		try {
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IQueueBusterDAO quebusterDao = dao.getQueBusterDAO();
            result = quebusterDao.deleteForwardItem(companyId, storeId, businessDayDate);
		} catch(DaoException daoEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName
					+ ": Failed to Select Forward Item Count of StoreId="
					+ storeId + ";Businessdaydate=" + businessDayDate, daoEx);
		} catch(Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to Select Forward Item Count of StoreId="
					+ storeId + ";Businessdaydate=" + businessDayDate, ex);
		} finally {
			tp.methodExit();
		}
		return result;
    }
    /**
     * Retrieve the previous amount of sod
     *
     * @param tillId			drawer id
     * @param storeId			store id
     *
     * @return GetCashBalance   Cash Balance response
     */
    @Path("/getpreviousamount")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="SOD�O���z�擾", response=CashDrawer.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
        })
    public final CashDrawer getPreviousAmount(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeid", value="�X�܃R�[�h") @QueryParam("storeid") final String storeId) {

    	tp.methodEnter("getPreviousAmount");
    	tp.println("Company Id", companyId)
    		.println("Store Id", storeId);

    	CashDrawer response = new CashDrawer();

    	try {
    		DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
    		IQueueBusterDAO queueBusterDao = sqlServer.getQueBusterDAO();

    		response = queueBusterDao.getPreviousAmount(companyId, storeId);
    	} catch (DaoException e) {
			LOGGER.logAlert(PROG_NAME, "getPreviousAmount", Logger.RES_EXCEP_DAO,
					"Failed to get Previous Amount.\n" + e.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
    	} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, "getPreviousAmount",
					Logger.RES_EXCEP_GENERAL, "Failed to getPrevious Amount.\n"
							+ e.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
    	} finally {
    		tp.methodExit(response.toString());
    	}

    	return response;
    }

    String getContextAttribute(String key, String defVal) {
        Object o = context.getAttribute(key);
        return (o == null || "".equals(o)) ? defVal : (String)o;
    }
}
