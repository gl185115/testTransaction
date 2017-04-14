package ncr.res.mobilepos.tillinfo.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.sql.SQLException;
import java.util.Collections;
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
import javax.ws.rs.core.SecurityContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.dao.ITillInfoDAO;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
/**
 *
 * @author ES185134
 */
@Path("/till")
@Api(value="/till", description="�h�������API")
public class TillInfoResource {
    /**
     * the servelet context.
     */
    @Context
    private SecurityContext context;
    /**
     * the instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * the instance of the dao factory.
     */
    private DAOFactory daoFactory;
    /**
     * variable that holds the class codename.
     */
    private static final String PROG_NAME = "TilRsc";

    private String pathName = "till";

    public static final String SOD_FLAG_UNFINISHED = "0";

    public static final String SOD_FLAG_PROCESSING= "9";

    public static final String SOD_FLAG_FINISHED = "1";

    public static final String EOD_FLAG_UNFINISHED = "0";

    public static final String EOD_FLAG_PROCESSING= "9";

    public static final String EOD_FLAG_FINISHED = "1";

    @Context
    private ServletContext servletContext;

    /**
     * constructor.
     */
    public TillInfoResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
    /**
     * Service to view till details of given parameter storeid.
     *
     * @param storeID
     *            storeid to lookup.
     *@param tillID
     *            tillid to lookup.
     * @return JSON type of till.
     */
    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="�h�������擾", response=ViewTill.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    @ApiResponse(code=ResultBase.RES_TILL_NOT_EXIST, message="�h���������݂��Ȃ�")
    })
    public final ViewTill viewTill(
    		@ApiParam(name="storeid", value="�X�܃R�[�h")  @QueryParam("storeid") final String storeID,
    		@ApiParam(name="tillid", value="�h�����[�R�[�h")  @QueryParam("tillid") final String tillID) {

        String functionName = "TillResource.viewTill";

        tp.methodEnter("viewTill");
        tp.println("storeID", storeID);
        tp.println("tillID", tillID);
        ViewTill viewTill = new ViewTill();

        try {
            ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
            viewTill = tillInfoDAO.viewTill(storeID, tillID);
        } catch (DaoException ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to view store# " + storeID + " and till#"+ tillID+": "
                            + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
            	viewTill.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	viewTill.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to view store# " + storeID + " and till#"+ tillID+": "
                            + ex.getMessage());
            viewTill.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(viewTill.toString());
        }

        return viewTill;
    }
    /**
     * Service to create a till for till id info table.
     *
     * @param storeID
     *            - store number
     * @param tillID
     *            - till number
     * @param till
     *            - till
     * @return ResultBase
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�V�����h�������쐬����", response=ResultBase.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_TILL_INVALIDPARAMS, message="�����ȃh�����R�[�h"),
    @ApiResponse(code=ResultBase.RES_STORE_INVALIDPARAMS, message="�����ȓX�܃R�[�h"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    @ApiResponse(code=ResultBase.RES_TILL_EXISTS, message="�h���������łɑ��݂��Ă��܂�")
    })
    public final ResultBase createTill(
    		@ApiParam(name="storeid", value="�X�܃R�[�h")  @FormParam("storeid") final String storeID,
    		@ApiParam(name="tillid", value="�h�����[�R�[�h")  @FormParam("tillid") final String tillID,
    		@ApiParam(name="till", value="�h����") @FormParam("till") final String till) {

        tp.methodEnter("createTill");
        tp.println("storeID", storeID).println("Till", till);
        tp.println("tillID", tillID);

        ResultBase result = null;

        try {
            JsonMarshaller<Till> jsonMarshaller = new JsonMarshaller<Till>();
            Till cTill = jsonMarshaller.unMarshall(till, Till.class);

            if(!StringUtility.isNullOrEmpty(cTill.getBusinessDayDate()) &&
            		!DateFormatUtility.isLegalFormat(cTill.getBusinessDayDate(),
            				"yyyy-MM-dd")){
                tp.println("Invalid value for till info");
                result = new ResultBase(ResultBase.RES_TILL_INVALIDPARAMS,
                        "Invalid value for business day date");
                tp.methodExit(result);
                return result;
            }

            if (StringUtility.isNullOrEmpty(storeID)) {
                tp.println("Invalid value for storeid");
                result = new ResultBase(ResultBase.RES_STORE_INVALIDPARAMS,
                        "Invalid value for storeid");
                tp.methodExit(result);
                return result;
            }

            if (StringUtility.isNullOrEmpty(tillID)) {
                tp.println("Invalid value for tillid");
                result = new ResultBase(ResultBase.RES_TILL_INVALIDPARAMS,
                        "Invalid value for tillid");
                tp.methodExit(result);
                return result;
            }

            ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
            String appId = pathName.concat(".create");
            cTill.setUpdOpeCode(getOpeCode());
            cTill.setUpdAppId(appId);
            result = tillInfoDAO.createTill(storeID, tillID, cTill);
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, "createTill",
                    Logger.RES_EXCEP_DAO, e.getMessage());
            result = new ResultBase(ResultBase.RES_ERROR_DB, e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, "createTill",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            result = new ResultBase(ResultBase.RES_ERROR_GENERAL,
                    e.getMessage());
        } finally {
            tp.methodExit(result);
        }
        return result;
    }
    
	private String getOpeCode(){
		return ((context != null) && (context.getUserPrincipal()) != null) ? context
				.getUserPrincipal().getName() : null;
	}
	
    /**
     * Web method called to get execution authority when SOD/EOD is triggered.
     * @param companyId - the company id which the Till belongs to.
     * @param storeId - The retail store id which the Till belongs to.
     * @param tillId - The till/drawer identifier.
     * @param terminalId - The terminal ID of the POS where SOD/EOD is executed.
     * @param operatorNo - The operator performing the SOD/EOD.
     * @param processingType - The processing type: SOD or EOD.
     * @param compulsoryFlag -  The compulsory flag.
     * 				false - Get the execution authority normally.
     * 				true - Get the execution authority compulsorily/forcibly.
     * @return ResultBase
     */
    @Path("/getexecuteauthority")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="SOD/EOD���s�����擾", response=ResultBase.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_NO_BIZDATE, message="�f�[�^�x�[�X�ɂ́A�f�[�^�x�[�X�ɑΉ�������t��������"),
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
    @ApiResponse(code=ResultBase.RES_TILL_NOT_EXIST, message="�h�����͑��݂��Ȃ�"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    @ApiResponse(code=ResultBase.RES_OPTIMISTIC_LOCKING_ERROR, message="�X�V���ł��Ȃ�"),
    @ApiResponse(code=ResultBase.RES_TILL_SOD_FINISHED, message="���̒[���ɂ͂��łɊJ�X����������"),
    @ApiResponse(code=ResultBase.RES_TILL_SOD_PROCESSING, message="���̒[���ɂ͂��łɊJ�X������"),
    @ApiResponse(code=ResultBase.RES_TILL_INVALID_SOD_FLAG_VAL, message="�����ȊJ�X�t���O"),
    @ApiResponse(code=ResultBase.RES_TILL_EOD_FINISHED, message="���̒[���ɂ͂��łɕX����������"),
    @ApiResponse(code=ResultBase.RES_TILL_EOD_PROCESSING, message="���̒[���ɂ͂��łɕX������"),
    @ApiResponse(code=ResultBase.RES_TILL_INVALID_SOD_STATE, message="�����ȊJ�X���"),
    @ApiResponse(code=ResultBase.RES_TILL_INVALID_EOD_FLAG_VAL, message="�����ȕX�t���O")
    })
    public final ResultBase getExecuteAuthority(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="tillid", value="�h�����[�R�[�h") @FormParam("tillid") final String tillId,
    		@ApiParam(name="terminalid", value="�[���R�[�h") @FormParam("terminalid") final String terminalId,
    		@ApiParam(name="operatorno", value="�]�ƈ��ԍ�") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="processing", value="�����^�C�v") @FormParam("processing") final String processingType,
    		@ApiParam(name="compulsoryflag", value="�����t���O") @FormParam("compulsoryflag") final String compulsoryFlag) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
		        .println("companyid", companyId)
				.println("retailstoreid", storeId)
				.println("tillid", tillId)
				.println("terminalid", terminalId)
				.println("operatorno", operatorNo)
				.println("processing", processingType)
				.println("compulsoryflag", compulsoryFlag);

		ResultBase resultBase = new ResultBase();
		String appId = pathName.concat(".getexecuteauthority");

    	String thisBusinessDay = new JournalizationResource().getBussinessDate(companyId, storeId);
		if(StringUtility.isNullOrEmpty(thisBusinessDay)) {
			// This is unlikely to happen.
			resultBase.setMessage("No business day for the store");
			resultBase.setNCRWSSResultCode(ResultBase.RES_NO_BIZDATE);
			tp.println("No business day fot the store on MST_BIZDAY.");
			tp.methodExit(resultBase);
			return resultBase;
		}

    	// check for required parameters
    	if (StringUtility.isNullOrEmpty(companyId, storeId, tillId, terminalId, operatorNo, processingType)) {
			resultBase.setMessage("Required fields are empty");
			resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
    		tp.println("A required parameter is null or empty.");
    		tp.methodExit(resultBase);
    		return resultBase;
    	}

    	// check if valid value for processingType
    	if (!"SOD".equalsIgnoreCase(processingType) && !"EOD".equalsIgnoreCase(processingType)) {
			resultBase.setMessage("Invalid processing parameter");
			resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
    		tp.println("Invalid value for parameter processingType.");
    		tp.methodExit(resultBase);
    		return resultBase;
    	}

		// Converts compulsory flag to boolean, matching with 'true', ignoring case.
		boolean compulsory = new Boolean(compulsoryFlag);

		try {
			ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();

			// check if till is existing
			Till currentTill = tillInfoDAO.fetchOne(companyId, storeId, tillId);
			if(currentTill == null) {
				// Target till doesn't exist.
				resultBase.setMessage("Till not found");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_NOT_EXIST);
				tp.methodExit(resultBase);
				return resultBase;
			}

			// Creates copy instance of current till for updating it.
			Till updatingTill = new Till(currentTill);
			updatingTill.setTerminalId(terminalId);
			updatingTill.setUpdAppId(appId);
			updatingTill.setUpdOpeCode(operatorNo);
			updatingTill.setBusinessDayDate(thisBusinessDay);

			// SOD or EOD.
			switch(processingType.toUpperCase()) {
				case "SOD" :
					updatingTill.setSodFlag(SOD_FLAG_PROCESSING);

					// If MultiSOD from SystemConfiguration is ON, it passes any SodFlag validation.
					if (GlobalConstant.isMultiSOD()) {
						tp.println("Allow multiple SOD on businessdaydate.");
						resultBase = tillInfoDAO.updateTillDailyOperation(currentTill, updatingTill);
						break;
					}
					// Checks if sodFlag is valid for getting Sod authority.
					ResultBase sodValidity = checkSodFlagValidity(currentTill, updatingTill, compulsory);
					if (sodValidity.getNCRWSSResultCode() == ResultBase.RES_OK) {
						resultBase = tillInfoDAO.updateTillDailyOperation(currentTill, updatingTill);
					} else {
						// SodFlag is invalid with the error code.
						resultBase = sodValidity;
					}
					break;

				case "EOD" :
					updatingTill.setEodFlag(EOD_FLAG_PROCESSING);

					// Checks if eodFlag is valid for getting Eod authority.
					ResultBase eodValidity = checkEodFlagValidity(currentTill, updatingTill, compulsory);
					if (eodValidity.getNCRWSSResultCode() == ResultBase.RES_OK) {
						resultBase = tillInfoDAO.updateTillDailyOperation(currentTill, updatingTill);
					} else {
						// EodFlag is invalid with the error code.
						resultBase = eodValidity;
					}
					break;
			}
        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get execution authority for processing:"+ processingType
							+ ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TillId:" + tillId + ":" + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
            	resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get execution authority for processing:"+ processingType
							+ ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TillId:" + tillId + ":" + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase);
        }
    	return resultBase;
    }

	/**
	 * EXECUTING SOD
	 * Checks sodFlag and complusory flag and determine if it can proceed to get SOD authority.
	 * @param compulsoryFlag if true, it force to perform SOD even SodFlag is already '9'.
	 * @param updatingTill Till with new values.
	 * @param currentTill Till with current values.
     * @return resultBase 0: OK to get SOD authority. Otherwise: errors.
     */
	private final ResultBase checkSodFlagValidity(Till currentTill, Till updatingTill, boolean compulsoryFlag) {
		ResultBase resultBase = new ResultBase();

    	switch (currentTill.getSodFlag()) {
    	case SOD_FLAG_FINISHED: // sodFlag = 1
    		// can't get execution authority with finished sod.
			tp.println("SOD has already been performed for the same till.");
			resultBase.setMessage("SodFlag is already 1");
			resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_SOD_FINISHED);
    		break;
    	case SOD_FLAG_PROCESSING: // sodFlag = 9
			// compulsoryFlag is 'true'. so it forces to proceed to get SOD authority.
    		if (compulsoryFlag) {
				resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
				break;
			}
			// Once a terminal gets the authority, the terminal passes the validation in the same business day.
			if (updatingTill.getBusinessDayDate().equals(currentTill.getBusinessDayDate()) &&
					updatingTill.getTerminalId().equals(currentTill.getTerminalId())) {
				resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
				break;
			}
			// Other terminal is currently processing SOD for till.
			tp.println("Other terminal is currently processing SOD for the same till.");
			resultBase.setMessage("SodFlag is already 9");
			resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_SOD_PROCESSING);
    		break;
    	case SOD_FLAG_UNFINISHED: // sodFlag = 0
			// It is safe to get SOD authority.
			resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
    		break;
    	default://for sod flag values not 0, 1, or 9 (fail-safe checking)
    		tp.println("Invalid value for sod flag.");
			resultBase.setMessage("Invalid SodFlag db-entry");
			resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_INVALID_SOD_FLAG_VAL);
			break;
    	}
    	return resultBase;
    }

	/**
	 * EXECUTING EOD
	 * Checks eodFlag and complusory flag and determine if it can proceed to get EOD authority.
	 * @param updatingTill Till with new values.
	 * @param currentTill Till with current values.
	 * @param compulsoryFlag if true, it forces to perform EOD even SodFlag is already '9'.
     * @return
     */
	private final ResultBase checkEodFlagValidity(Till currentTill, Till updatingTill, boolean compulsoryFlag) {
		ResultBase resultBase = new ResultBase();
		switch (currentTill.getEodFlag()) {
			case EOD_FLAG_FINISHED: // eodFlag = 1
				// can't get execution authority with finished EOD.
				tp.println("EOD has already been performed for the same till.");
				resultBase.setMessage("EodFlag is already 1");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_EOD_FINISHED);
				break;
			case EOD_FLAG_PROCESSING: // sodFlag = 9
				// compulsoryFlag is 'true'. so it forces to proceed to get EOD authority.
				if (compulsoryFlag) {
					resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
					break;
				}
				// Once a terminal gets the authority, the terminal passes the validation in the same business day.
				if (updatingTill.getBusinessDayDate().equals(currentTill.getBusinessDayDate()) &&
						updatingTill.getTerminalId().equals(currentTill.getTerminalId())) {
					resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
					break;
				}
				// Other terminal is currently processing EOD for till.
				tp.println("Other terminal is currently processing EOD for the same till.");
				resultBase.setMessage("EodFlag is already 9");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_EOD_PROCESSING);
				break;
			case EOD_FLAG_UNFINISHED: // eodFlag = 0
				// To perform EOD, SOD has to be finished prior on the day.
				if(currentTill.getSodFlag().equals(SOD_FLAG_FINISHED)) {
					resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
					break;
				}
				// Invalid SOD state to get EOD authority.
				tp.println("SOD should be finished prior to EOD.");
				resultBase.setMessage("SOD should be finished prior to EOD");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_INVALID_SOD_STATE);
				break;
			default://for sod flag values not 0, 1, or 9 (fail-safe checking)
				tp.println("Invalid value for eod flag.");
				resultBase.setMessage("Invalid EodFlag db-entry");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_INVALID_EOD_FLAG_VAL);
				break;
		}
		return resultBase;
	}

    /**
     * Web method called to release execution authority when SOD/EOD is released/cancelled.
     * @param companyId - The company id which the Till belongs to.
     * @param storeId - The retail store id which the Till belongs to.
     * @param tillId - The till/drawer identifier.
     * @param terminalId - The terminal ID of the POS where SOD/EOD is executed.
     * @param operatorNo - The operator performing the SOD/EOD.
     * @param processingType - The processing type: SOD or EOD.
     * @return ResultBase
     */
	
    @Path("/releaseexecuteauthority")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="SOD/EOD���s�����j��", response=ResultBase.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_NO_BIZDATE, message="�f�[�^�x�[�X�ɂ́A�f�[�^�x�[�X�ɑΉ�������t��������"),   
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
    @ApiResponse(code=ResultBase.RES_TILL_NOT_EXIST, message="�h�����͑��݂��Ȃ�"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    @ApiResponse(code=ResultBase.RES_OPTIMISTIC_LOCKING_ERROR, message="�X�V���ł��Ȃ�"),
    @ApiResponse(code=ResultBase.RES_TILL_SOD_UNFINISHED, message="�J�X�͂܂��������Ă��Ȃ�"),
    @ApiResponse(code=ResultBase.RES_TILL_SOD_FINISHED, message="���̒[���ɂ͂��łɊJ�X����������"),
    @ApiResponse(code=ResultBase.RES_TILL_INVALID_SOD_FLAG_VAL, message="�����ȊJ�X�t���O"),
    @ApiResponse(code=ResultBase.RES_TILL_EOD_UNFINISHED, message="�X�͂܂��������Ă��Ȃ�"),
    @ApiResponse(code=ResultBase.RES_TILL_EOD_FINISHED, message="���̒[���ɂ͂��łɕX����������"),
    @ApiResponse(code=ResultBase.RES_TILL_INVALID_EOD_FLAG_VAL, message="�����ȕX�t���O")
    })
    public final ResultBase releaseExecuteAuthority(
    		@ApiParam(name="companyid", value="�h�����[�R�[�h") @FormParam("companyid") final String companyId,
    		@ApiParam(name="retailstoreid",value="�X�܃R�[�h") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="tillid",value="�h�����[�R�[�h") @FormParam("tillid") final String tillId,
    		@ApiParam(name="terminalid",value="�[���R�[�h") @FormParam("terminalid") final String terminalId,
    		@ApiParam(name="operatorno",value="�]�ƈ��ԍ�") @FormParam("operatorno") final String operatorNo,
    		@ApiParam(name="processing",value="�����^�C�v") @FormParam("processing") final String processingType) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
		        .println("companyid", companyId)
				.println("retailstoreid", storeId)
				.println("tillid", tillId)
				.println("terminalid", terminalId)
				.println("operatorno", operatorNo)
				.println("processing", processingType);

		ResultBase resultBase = new ResultBase();
		String appId = pathName.concat(".releaseexecuteauthority");

		String thisBusinessDay = new JournalizationResource().getBussinessDate(companyId, storeId);
		if(StringUtility.isNullOrEmpty(thisBusinessDay)) {
			// This is unlikely to happen.
			resultBase.setMessage("No business day for the store");
			resultBase.setNCRWSSResultCode(ResultBase.RES_NO_BIZDATE);
			tp.println("No business day fot the store on MST_BIZDAY.");
			tp.methodExit(resultBase);
			return resultBase;
		}

    	// check for required parameters
    	if (StringUtility.isNullOrEmpty(companyId, storeId, tillId, terminalId,	operatorNo, processingType)) {
    		tp.println("A required parameter is null or empty.");
			resultBase.setMessage("Required fields are empty");
    		resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
    		tp.methodExit(resultBase);
    		return resultBase;
    	}

    	// check if valid value for processingType
    	if (!"SOD".equalsIgnoreCase(processingType) && !"EOD".equalsIgnoreCase(processingType)) {
			resultBase.setMessage("Invalid processing parameter");
    		resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			tp.println("Invalid value for parameter processing.");
    		tp.methodExit(resultBase);
    		return resultBase;
    	}

		try {
			ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();

			// check if till is existing
			Till currentTill = tillInfoDAO.fetchOne(companyId, storeId, tillId);
			if(currentTill == null) {
				// Target till doesn't exist.
				resultBase.setMessage("Till not found");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_NOT_EXIST);
				tp.methodExit(resultBase);
				return resultBase;
			}

			// Creates copy instance of current till for updating it.
			Till updatingTill = new Till(currentTill);
			updatingTill.setTerminalId(terminalId);
			updatingTill.setUpdAppId(appId);
			updatingTill.setUpdOpeCode(operatorNo);
			updatingTill.setBusinessDayDate(thisBusinessDay);

			// SOD or EOD
			switch(processingType.toUpperCase()) {
				case "SOD" :
					//change from 9 (processing) to 0 (unfinished) since SOD is cancelled
					updatingTill.setSodFlag(SOD_FLAG_UNFINISHED);

					// Checks if sodFlag is valid for getting Sod authority.
					ResultBase sodFlagValidity = checkReleaseSodFlagValidity(currentTill.getSodFlag());
					if (sodFlagValidity.getNCRWSSResultCode() == ResultBase.RES_OK) {
						resultBase = tillInfoDAO.updateTillDailyOperation(currentTill, updatingTill);
					} else {
						// SodFlag is invalid with the error code.
						resultBase = sodFlagValidity;
					}
					break;
				case "EOD" :
					//change from 9 (processing) to 0 (unfinished) since EOD is cancelled
					updatingTill.setEodFlag(EOD_FLAG_UNFINISHED);

					// Checks if eodFlag is valid for releasing Eod authority.
					ResultBase eodFlagValidity = checkReleaseEodFlagValidity(currentTill.getEodFlag());
					if (eodFlagValidity.getNCRWSSResultCode() == ResultBase.RES_OK) {
						resultBase = tillInfoDAO.updateTillDailyOperation(currentTill, updatingTill);
					} else {
						// EodFlag is invalid with the error code.
						resultBase = eodFlagValidity;
					}
					break;
			}
		} catch (DaoException ex) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
					"Failed to release execution authority for processing:"+ processingType
							+ ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TillId:" + tillId + ":" + ex.getMessage());
			if (ex.getCause() instanceof SQLException) {
				resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			} else {
				resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to release execution authority for processing:"+ processingType
							+ ":CompanyId:" + companyId + ":StoreId:" + storeId + ":TillId:" + tillId + ":" + ex.getMessage());
			resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(resultBase);
		}
    	return resultBase;
    }

	/**
	 * RELEASE SOD
	 * Checks sodFlag and determine if it can release SOD authority.
	 * @param sodFlag sodFlag to check.
	 * @return
	 */
	private final ResultBase checkReleaseSodFlagValidity(String sodFlag) {
		ResultBase resultBase = new ResultBase();
		switch (sodFlag) {
			case SOD_FLAG_PROCESSING: // sodFlag = 9
				// This is the only case, it can release SOD authority.
				resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
			break;
			case SOD_FLAG_UNFINISHED: // sodFlag = 0
				// SOD has not yet started. Nothing to cancel.
				tp.println("SOD has not yet started. Nothing to cancel.");
				resultBase.setMessage("SodFlag is still 0");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_SOD_UNFINISHED);
				break;
			case SOD_FLAG_FINISHED: // sodFlag = 1
				// can't release execution authority with finished SOD.
				tp.println("SOD has already been performed for the same till.");
				resultBase.setMessage("SodFlag is already 1");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_SOD_FINISHED);
				break;
			default://for sod flag values not 0, 1, or 9 (fail-safe checking)
				tp.println("Invalid value for SOD flag.");
				resultBase.setMessage("Invalid SodFlag db-entry");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_INVALID_SOD_FLAG_VAL);
				break;
		}
		return resultBase;
	}

	/**
	 * RELEASE EOD
	 * Checks eodFlag and determine if it can release EOD authority.
	 * @param eodFlag eodFlag to check.
	 * @return
	 */
	private final ResultBase checkReleaseEodFlagValidity(String eodFlag) {
		ResultBase resultBase = new ResultBase();
		switch (eodFlag) {
			case EOD_FLAG_PROCESSING: // sodFlag = 9
				// This is the only case, it can release SOD authority.
				resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
				break;
			case EOD_FLAG_UNFINISHED: // eodFlag = 0
				// EOD has not yet started. Nothing to cancel.
				tp.println("EOD has not yet started. Nothing to cancel.");
				resultBase.setMessage("EodFlag is still 0");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_EOD_UNFINISHED);
				break;
			case EOD_FLAG_FINISHED: // eodFlag = 1
				// can't release execution authority with finished eod.
				tp.println("EOD has already been performed for the same till.");
				resultBase.setMessage("EodFlag is already 1");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_EOD_FINISHED);
				break;
			default://for sod flag values not 0, 1, or 9 (fail-safe checking)
				tp.println("Invalid value for EOD flag.");
				resultBase.setMessage("Invalid EodFlag db-entry");
				resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_INVALID_EOD_FLAG_VAL);
				break;
		}
		return resultBase;
	}

    /**
     * Web method called to check if there are still logged on users on a given till
     * @param storeId - The retail store id which the Till belongs to.
     * @param tillId - The till/drawer identifier.
     * @param terminalId - The terminal ID of the POS where SOD/EOD is executed.
     * @return ResultBase
     */
	
	
    @Path("/search")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="���[�U�[���h�����[���g�p���邩���`�F�b�N����", response=ResultBase.class)
    @ApiResponses(value={   
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
    @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
    @ApiResponse(code=ResultBase.RES_TILL_OTHER_USERS_SIGNED_ON, message="���̑��̃��[�U�[�̐ڑ��ɂ̓}�[�N���K�v")  
    })
    public final ResultBase search(
    		@ApiParam(name="companyid",value="�h�����[�R�[�h") @FormParam("companyid") final String companyid,
    		@ApiParam(name="retailstoreid",value="�X�܃R�[�h") @FormParam("retailstoreid") final String storeId,
    		@ApiParam(name="tillid",value="�h�����[�R�[�h") @FormParam("tillid") final String tillId,
    		@ApiParam(name="terminalid",value="�[���R�[�h") @FormParam("terminalid") final String terminalId) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
		        .println("companyid", companyid)
				.println("storeId", storeId)
				.println("tillId", tillId)
				.println("terminalId", terminalId);

		ResultBase resultBase = new ResultBase();

        // check for required parameters
        if (StringUtility.isNullOrEmpty(storeId, tillId, terminalId)) {
            tp.println("A required parameter is null or empty.");
            resultBase.setNCRWSSResultCode(
            		ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(resultBase);
            return resultBase;
        }

    	// check if till is existing
        ViewTill viewTill = this.viewTill(storeId, tillId);
		if (viewTill.getNCRWSSResultCode() != ResultBase.RES_OK) {
			resultBase.setNCRWSSResultCode(viewTill
					.getNCRWSSResultCode());
			tp.methodExit(resultBase);
			return resultBase;
		}

		try {
			ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
			resultBase = tillInfoDAO.searchLogonUsers(companyid, storeId, tillId,
					terminalId);
		} catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
            		"Failed to search for logon users for storeid "
            		+ storeId + " and tillid " + tillId + ": " +
            				e.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
		} catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
            		"Failed to search for logon users for storeid "
            		+ storeId + " and tillid " + tillId + ": " +
            				e.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
            tp.methodExit(resultBase);
        }

    	return resultBase;
    }

    /**
     * Service to get till information list.
     *
     * @param storeId
     *            - store number
     * @return ResultBase
     */
    @Path("/getTillList")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�擾�h�������X�g", response=ViewTill.class)
    @ApiResponses(value={
    @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����̃p�����[�^"),
    @ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="�f�[�^�����o"),
    @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
    @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
    })
    public final ViewTill getTillList(
    		@ApiParam(name="storeId", value="�X�܃R�[�h") @FormParam("storeId") final String storeId){
            String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("storeId", storeId);
        ViewTill result = new ViewTill();

        try {
			if (StringUtility.isNullOrEmpty(storeId)) {
				tp.println(ResultBase.RES_INVALIDPARAMETER_MSG);
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
				return result;
			}
        	 ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
        	 List<Till> tillList = tillInfoDAO.getTillInformation(storeId);
        	 if(tillList != null){
        		 result.setTillList(tillList);
                 result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                 result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                 result.setMessage(ResultBase.RES_SUCCESS_MSG);
        	 }else{
        		 result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                 result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                 result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                 tp.println(ResultBase.RES_NODATAFOUND_MSG);
        	 }
        }catch (DaoException ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
                    functionName
                            + ": Failed to get till infomation list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
            result.setMessage(ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName
                            + ": Failed to get till infomation list.", ex);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(result);
        }
        return result;
    }

	/**
	 * API to return all the activated tills on the business day.
	 * @param companyId
	 * @param storeId
	 * @param businessDate yyyy-MM-dd
	 * @return ViewTill
	 */
	@Path("/alltillinfo")
	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@ApiOperation(value="�h�������擾", response=ViewTill.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�s���ȃp�����[�^"),
			@ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
			@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
	})
	public final ViewTill getActivatedTillsOnBusinessDay(
			@ApiParam(name="companyId", value="��ƃR�[�h") @QueryParam("companyId") final String companyId,
			@ApiParam(name="storeId", value="�X�܃R�[�h") @QueryParam("storeId") final String storeId,
			@ApiParam(name="businessDate", value="�c�Ɠ�") @QueryParam("businessDate") final String businessDate) {
		// Trace Logging
		final String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("companyId", companyId);
		tp.println("storeId", storeId);
		tp.println("businessDate", businessDate);

		// Initializes response holder
		ViewTill result = new ViewTill();
		result.setTillList(Collections.emptyList());

		// Checks if required fields are present.
		if (StringUtility.isNullOrEmpty(storeId, companyId, businessDate)) {
			tp.println(ResultBase.RES_INVALIDPARAMETER_MSG + ":Required fields are missing.");
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
			return result;
		}
		// Checks if businessDate has valid date format
		if (!DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
			tp.println(ResultBase.RES_INVALIDPARAMETER_MSG + ":Invalid BusinessDayDate format.");
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
			return result;
		}

		try {
			// Selects from DB.
			ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
			List<Till> activatedTills = tillInfoDAO.getActivatedTillsOnBusinessDay(companyId, storeId, businessDate);
			result.setTillList(activatedTills);
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get till infomation list.", ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			result.setMessage(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get till infomation list.", ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(ex.getMessage());
		}
		return (ViewTill)tp.methodExit(result);
	}

	/**
	 * API to return unclosed tills on the business day.
	 * @param companyId
	 * @param storeId
	 * @param businessDate yyyy-MM-dd
	 * @return ViewTill
	 */
	@Path("/unfinishedeodinfo")
	@GET
	@Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
	@ApiOperation(value="�h�������擾", response=ViewTill.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�s���ȃp�����[�^"),
			@ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
			@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���[")
	})
	public final ViewTill getUnclosedTillsOnBusinessDay(
			@ApiParam(name="companyId", value="��ƃR�[�h") @QueryParam("companyId") final String companyId,
			@ApiParam(name="storeId", value="�X�܃R�[�h") @QueryParam("storeId") final String storeId,
			@ApiParam(name="businessDate", value="�c�Ɠ�") @QueryParam("businessDate") final String businessDate) {
		// Trace Logging
		final String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		tp.println("companyId", companyId);
		tp.println("storeId", storeId);
		tp.println("businessDate", businessDate);

		// Initializes response holder
		ViewTill result = new ViewTill();
		result.setTillList(Collections.emptyList());

		// Checks if required fields are present.
		if (StringUtility.isNullOrEmpty(storeId, companyId, businessDate)) {
			tp.println(ResultBase.RES_INVALIDPARAMETER_MSG + ":Required fields are missing.");
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
			return result;
		}
		// Checks if businessDate has valid date format
		if (!DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
			tp.println(ResultBase.RES_INVALIDPARAMETER_MSG + ":Invalid BusinessDayDate format.");
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
			result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
			return result;
		}

		try {
			// Selects from DB.
			ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
			List<Till> unclosedTills = tillInfoDAO.getUnclosedTillsOnBusinessDay(companyId, storeId, businessDate);
			result.setTillList(unclosedTills);
		} catch (DaoException ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to get till infomation list.", ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			result.setMessage(ex.getMessage());
		} catch (Exception ex) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get till infomation list.", ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(ex.getMessage());
		}
		return (ViewTill)tp.methodExit(result);
	}

}
