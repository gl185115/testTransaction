package ncr.res.mobilepos.tillinfo.resource;
/**
 * 改定履歴
 * バージョン        改定日付          担当者名      改定内容
 * 1.01      2015.03.09 YUANCHI  閉設の時、営業日一致性チェック処理を削除するように変更
 */

import java.sql.SQLException;
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
     * @param storeid
     *            storeid to lookup.
     *@param tillid
     *            tillid to lookup.
     * @return JSON type of till.
     */
    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final ViewTill viewTill(
    		@QueryParam("storeid") final String storeID,
    		@QueryParam("tillid") final String tillID) {

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
     * @param storeid
     *            - store number
     * @param tillid
     *            - till number
     * @param till
     *            - till
     * @return ResultBase
     */
    @Path("/create")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final ResultBase createTill(
            @FormParam("storeid") final String storeID,
            @FormParam("tillid") final String tillID,
            @FormParam("till") final String till) {

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
    
    /**
     * Web Method called to update a store.
     * 
     * @param storeid
     *            The retail Store id
     * @param storeJson
     *            The new values for store.
     * @return The Stores JSON Object containing the list of stores.
     */
    @Path("/maintenance")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ViewTill updateTill(
            @FormParam("storeid") final String storeID,
            @FormParam("tillid") final String tillID,
            @FormParam("till") final String tillJson) {
        String functionName = "TillInfoResource.updateTill";

        tp.methodEnter("updateTill");
        tp.println("storeID", storeID).println("till", tillJson);
        tp.println("tillID", tillID);
        
        Till till = new Till();
        ViewTill viewTill = new ViewTill();
        
        if (StringUtility.isNullOrEmpty(storeID)) {
            tp.println("Invalid value for storeid");
            viewTill.setNCRWSSResultCode(ResultBase.RES_STORE_INVALIDPARAMS);
            tp.methodExit(viewTill);
            return viewTill;
        }

        if (StringUtility.isNullOrEmpty(tillID)) {
            tp.println("Invalid value for tillid");
            viewTill.setNCRWSSResultCode(ResultBase.RES_TILL_INVALIDPARAMS);
            tp.methodExit(viewTill);
            return viewTill;
        }
        
        try {
            String appID = pathName.concat(".maintenance");
            JsonMarshaller<Till> tillJsonMarshaller = new JsonMarshaller<Till>();             
            till = tillJsonMarshaller
                    .unMarshall(tillJson, Till.class);
            
            if(!DateFormatUtility.
            		isLegalFormat(till.getBusinessDayDate(), "yyyy-MM-dd")){
                tp.println("Invalid value for till info");
                viewTill.setNCRWSSResultCode(ResultBase.RES_TILL_INVALIDPARAMS);
                tp.methodExit(viewTill);
                return viewTill;
            }
            ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
            till.setUpdAppId(appID);
            till.setUpdOpeCode(getOpeCode());
            viewTill = tillInfoDAO.updateTill(storeID, tillID, till);        
        } catch (DaoException ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to update store# " + storeID + " and till#"+ tillID+":" 
                            + ex.getMessage());

            if (ex.getCause() instanceof SQLException) {
            	viewTill.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update store# " + storeID + " and till#"+ tillID+":"
                            + ex.getMessage());
            viewTill.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {			
            tp.methodExit(viewTill);
        }
        return viewTill;
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
    public final ResultBase getExecuteAuthority(
        @FormParam("companyid") final String companyId,
		@FormParam("retailstoreid") final String storeId,
		@FormParam("tillid") final String tillId,
	    @FormParam("terminalid") final String terminalId,
	    @FormParam("operatorno") final String operatorNo,
	    @FormParam("processing") final String processingType,
	    @FormParam("compulsoryflag") final String compulsoryFlag) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
		        .println("companyid", companyId)
				.println("retailstoreid", storeId)
				.println("tillid", tillId)
				.println("terminalid", terminalId)
				.println("operatorno", operatorNo)
				.println("processing", processingType)
				.println("compulsoryflag", compulsoryFlag);

		ViewTill viewTill = new ViewTill(); 	
		ResultBase resultBase = new ResultBase();
    	String todayDate = new JournalizationResource().getBussinessDate(companyId, storeId);
    	Till aTill = new Till();
    	String appId = pathName.concat(".getexecuteauthority");
    	
    	// check for required parameters
    	if (StringUtility.isNullOrEmpty(storeId, tillId, 
    			terminalId, operatorNo, processingType)) {
    		tp.println("A required parameter is null or empty.");
    		resultBase.setNCRWSSResultCode(
    				ResultBase.RES_ERROR_INVALIDPARAMETER);
    		tp.methodExit(resultBase);
    		return resultBase;
    	}
    	
    	// check if valid value for processingType
    	if (!"SOD".equalsIgnoreCase(processingType) && 
			!"EOD".equalsIgnoreCase(processingType)) {
    		tp.println("Invalid value for parameter processing.");
    		resultBase.setNCRWSSResultCode(
    				ResultBase.RES_ERROR_INVALIDPARAMETER);
    		tp.methodExit(resultBase);
    		return resultBase;
    	}
		
    	// check if till is existing
		viewTill = this.viewTill(storeId, tillId);		
		if (viewTill.getNCRWSSResultCode() != ResultBase.RES_OK) {
			resultBase.setNCRWSSResultCode(viewTill.getNCRWSSResultCode());
			tp.methodExit(resultBase);
			return resultBase;
		}
		
		// get compulsory flag value
		boolean compulsory = false;
		if ("true".equalsIgnoreCase(compulsoryFlag)) {
			compulsory = true;
		}
		
		try {			
			if("SOD".equalsIgnoreCase(processingType)) {
				ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
				aTill = viewTill.getTill();
				String sodFlagToChange = aTill.getSodFlag();
				
				aTill.setTerminalId(terminalId);		
				aTill.setSodFlag(SOD_FLAG_PROCESSING); //change current sod flag to 9 (processing)
				aTill.setUpdAppId(appId);
				aTill.setUpdOpeCode(operatorNo);
				if (!GlobalConstant.isMultiSOD()) {
					resultBase = checkSodFlagValidity(sodFlagToChange, compulsory);
					if (resultBase.getNCRWSSResultCode() == ResultBase.RES_OK) {
						resultBase = tillInfoDAO.updateSODTill(aTill,
								Integer.parseInt(sodFlagToChange));
					}
				} else {
					tp.println("Allow multiple SOD on businessdaydate.");
					resultBase = tillInfoDAO.updateSODTill(aTill, 
							Integer.parseInt(sodFlagToChange));
				}
    		 } else if ("EOD".equalsIgnoreCase(processingType)) {
    			 resultBase = doEODGetExecuteAuthority(viewTill, terminalId, 
    					 tillId, compulsory, todayDate, appId, operatorNo);
    		 }
        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get execution authority for store# " + storeId + 
                    " and till# " + tillId +": "  + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
            	resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get execution authority for store# " + storeId + 
                    " and till#"+ tillId +": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase);
        }
    	return resultBase;
    }
    
    private final ResultBase checkSodFlagValidity(String sodFlag, boolean compulsoryFlag) {
    	ResultBase resultBase = new ResultBase();
    	
    	switch (sodFlag) {
    	case SOD_FLAG_FINISHED:
    		// can't get execution authority with finished sod (sod flag = 1)
			tp.println("SOD has already been performed "
						+ "for the same till.");
			resultBase.setNCRWSSResultCode(
					ResultBase.RES_TILL_SOD_FINISHED);
    		break;
    	case SOD_FLAG_PROCESSING:
    		if (!compulsoryFlag) {
				// Other terminal is currently processing SOD for till (sod flag = 9)
				tp.println("Other terminal is currently "
							+ "processing SOD for the same till.");
				resultBase.setNCRWSSResultCode(
						ResultBase.RES_TILL_SOD_PROCESSING);
			}
    		break;
    	case SOD_FLAG_UNFINISHED:
    		//do nothing since this flag value is valid
    		break;
    	default:
    		tp.println("Invalid value for sod flag.");
			resultBase.setNCRWSSResultCode(
					ResultBase.RES_TILL_INVALID_SOD_FLAG_VAL);
    	}
    	return resultBase;
    }
    
    private final ResultBase doEODGetExecuteAuthority(ViewTill viewTill, 
    		String terminalId, String tillId, boolean compulsory, 
    		String todayDate, String appId, String operatorNo) 
    				throws DaoException {
    	ResultBase resultBase = new ResultBase();
    	
		 String eodFlag = viewTill.getTill().getEodFlag();
		 
		 if (EOD_FLAG_FINISHED.equals(eodFlag)) {
			 tp.println("EOD has already been performed for till# "
			 		+ tillId);
			 resultBase.setNCRWSSResultCode(
					 ResultBase.RES_TILL_EOD_FINISHED);
		 } else if (EOD_FLAG_UNFINISHED.equals(eodFlag) || 
				 EOD_FLAG_PROCESSING.equals(eodFlag)) {
			 if (!compulsory) {
				 if (EOD_FLAG_PROCESSING.equals(eodFlag)) {
					 tp.println("Other terminal is currently "
					 		+ "processing EOD for till# " + tillId);
					 resultBase.setNCRWSSResultCode(
							 ResultBase.RES_TILL_EOD_PROCESSING);
					 return resultBase;
				 }
			 }
			 
			 /* 1.01 2015.03.09 営業日一致性チェック処理削除 MOD START*/
//			 String businessDate = 
//					 viewTill.getTill().getBusinessDayDate();
//			 if (todayDate.equals(businessDate)) {
//				 ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
//				 
//				 viewTill.getTill().setTerminalId(terminalId);
//				 viewTill.getTill().setEodFlag(EOD_FLAG_PROCESSING);
//				 viewTill.getTill().setUpdAppId(appId);
//				 viewTill.getTill().setUpdOpeCode(operatorNo);
//				 
//				 resultBase = tillInfoDAO.updateEODTill(viewTill.getTill(), 
//						 Integer.parseInt(eodFlag));
//			 } else {
//				 resultBase.setNCRWSSResultCode(
//						 ResultBase.RES_TILL_INVALID_BIZDATE);
//			 }
			 ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
			 
			 viewTill.getTill().setTerminalId(terminalId);
			 viewTill.getTill().setEodFlag(EOD_FLAG_PROCESSING);
			 viewTill.getTill().setUpdAppId(appId);
			 viewTill.getTill().setUpdOpeCode(operatorNo);
			 
			 resultBase = tillInfoDAO.updateEODTill(viewTill.getTill(), 
					 Integer.parseInt(eodFlag));
			 /* 1.01 2015.03.09 営業日一致性チェック処理削除 MOD END*/
		 } else {
			 tp.println("Invalid value for eod flag.");
			 resultBase.setNCRWSSResultCode(
					 ResultBase.RES_TILL_INVALID_EOD_FLAG_VAL);
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
    public final ResultBase releaseExecuteAuthority(
            @FormParam("companyid") final String companyId,
    		@FormParam("retailstoreid") final String storeId,
    		@FormParam("tillid") final String tillId,
    	    @FormParam("terminalid") final String terminalId,
    	    @FormParam("operatorno") final String operatorNo,
    	    @FormParam("processing") final String processingType) {
    	
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
		        .println("companyid", companyId)
				.println("retailstoreid", storeId)
				.println("tillid", tillId)
				.println("terminalid", terminalId)
				.println("operatorno", operatorNo)
				.println("processing", processingType);

		ViewTill viewTill = new ViewTill(); 	
		ResultBase resultBase = new ResultBase();
    	String todayDate = new JournalizationResource().getBussinessDate(companyId, storeId);
    	Till aTill = new Till();

    	// check for required parameters
    	if (StringUtility.isNullOrEmpty(storeId, tillId, terminalId, 
    			operatorNo, processingType)) {
    		tp.println("A required parameter is null or empty.");
    		resultBase.setNCRWSSResultCode(
    				ResultBase.RES_ERROR_INVALIDPARAMETER);
    		tp.methodExit(resultBase);
    		return resultBase;
    	}
    	
    	// check if valid value for processingType
    	if (!"SOD".equalsIgnoreCase(processingType) && 
    			!"EOD".equalsIgnoreCase(processingType)) {
    		tp.println("Invalid value for parameter processing.");
    		resultBase.setNCRWSSResultCode(
    				ResultBase.RES_ERROR_INVALIDPARAMETER);
    		tp.methodExit(resultBase);
    		return resultBase;
    	}
		
    	// check if till is existing
		viewTill = this.viewTill(storeId, tillId);		
		if (viewTill.getNCRWSSResultCode() != ResultBase.RES_OK) {
			resultBase.setNCRWSSResultCode(viewTill
					.getNCRWSSResultCode());
			tp.methodExit(resultBase);
			return resultBase;
		}
		    	   	
    	try {
    		 ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();   		 
			 aTill = viewTill.getTill();
    		 String appId = pathName.concat(".releaseexecuteauthority");
    		 
    		 if("SOD".equalsIgnoreCase(processingType)) {
    			 if(SOD_FLAG_PROCESSING.equals(aTill.getSodFlag())) { //check if sod flag is 9 (processing)
    				 if((!StringUtility.isNullOrEmpty(aTill.getBusinessDayDate()) && 
    						 todayDate.compareTo(aTill.getBusinessDayDate()) >= 0) || 
    						 StringUtility.isNullOrEmpty(aTill.getBusinessDayDate())) {
    					 aTill.setTerminalId(terminalId);
        				 aTill.setSodFlag(SOD_FLAG_UNFINISHED); //change from 9 (processing) to 0 (unfinished) since SOD is cancelled
        				 aTill.setUpdAppId(appId);
    	    			 aTill.setUpdOpeCode(operatorNo);
    	    			//change till with 9 sodFlag (processing)
    	    			 resultBase = tillInfoDAO.updateSODTill(aTill, Integer.parseInt(SOD_FLAG_PROCESSING)); 
    				 } else {
    					 resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_INVALID_BIZDATE);
    				 }
    			 } else if(SOD_FLAG_UNFINISHED.equals(aTill.getSodFlag())) { //check if sod flag is 0 (unfinished)
    				 //can't release execution authority with unfinished sod
    				 resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_SOD_UNFINISHED);
    			 } else if(SOD_FLAG_FINISHED.equals(aTill.getSodFlag())) { //check if sod flag is 1 (finished)
    				 //can't release execution authority with finished sod
    				 resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_SOD_FINISHED);
    			 } else {
    				 //for sod flag values not 0, 1, or 9 (fail-safe checking)
    				 resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_INVALID_SOD_FLAG_VAL);
    			 }
    		 } else if ("EOD".equalsIgnoreCase(processingType)) {
    			 resultBase = doEODReleaseExecuteAuthority(aTill,  
    					 terminalId, appId, operatorNo, tillInfoDAO);
    		 }
        } catch (DaoException ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to release execution authority for store# " + storeId + 
                    " and till#"+ tillId +": " + ex.getMessage());
            if (ex.getCause() instanceof SQLException) {
            	resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to release execution authority for store# " + storeId + 
                    " and till#"+ tillId +": " + ex.getMessage());
            resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(resultBase);
        }
    	return resultBase;
    }
    
    private final ResultBase doEODReleaseExecuteAuthority(Till aTill, 
    	    String terminalId, String appId, 
    		String operatorNo, ITillInfoDAO tillInfoDAO) throws DaoException {
    	ResultBase resultBase = new ResultBase();
    	
		 if (EOD_FLAG_PROCESSING.equals(aTill.getEodFlag())) {
			aTill.setTerminalId(terminalId);
			aTill.setEodFlag(EOD_FLAG_UNFINISHED);
			aTill.setUpdAppId(appId);
			aTill.setUpdOpeCode(operatorNo);

			resultBase = tillInfoDAO.updateEODTill(aTill,
					Integer.parseInt(EOD_FLAG_PROCESSING));
		 } else if (EOD_FLAG_UNFINISHED.equals(aTill.getEodFlag())) {
			 resultBase.setNCRWSSResultCode(
					 ResultBase.RES_TILL_EOD_UNFINISHED);
		 } else if (EOD_FLAG_FINISHED.equals(aTill.getEodFlag())) {
			 resultBase.setNCRWSSResultCode(
					 ResultBase.RES_TILL_EOD_FINISHED);
		 } else {
			 resultBase.setNCRWSSResultCode(
					 ResultBase.RES_TILL_INVALID_EOD_FLAG_VAL);
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
    public final ResultBase search(
            @FormParam("companyid") final String companyid,
    		@FormParam("retailstoreid") final String storeId, 
    		@FormParam("tillid") final String tillId, 
    		@FormParam("terminalid") final String terminalId) {
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
     * @param storeid
     *            - store number
     * @return ResultBase
     */
    @Path("/getTillList")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final ViewTill getTillList(
            @FormParam("storeId") final String storeId){
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
    
    
}
