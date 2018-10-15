package ncr.res.mobilepos.mastermaintenance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.mastermaintenance.model.MaintenanceTbl;
import ncr.res.mobilepos.mastermaintenance.model.MdMMMastTbl;
import ncr.res.mobilepos.mastermaintenance.model.MdMastTbl;
import ncr.res.mobilepos.mastermaintenance.model.OpeMastTbl;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * The DAO implementation for Master Maintenance.
 * @author CC185102
 *
 */
public class SQLServerMasterMaintenanceDAO extends AbstractDao
implements IMasterMaintenanceDAO  {
	/** The Start code for MixMatch Group */
    private static final int START_GROUP_MIXMATCH_CODE = 9601;
    /**
     * The Database manager.
     */
    private DBManager dbManager;
    /**
     * The IOWriter to log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * The Snap Logger.
     */
    private SnapLogger snapLogger;
    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "MstMntDAO";

    /**
     * The Default Constructor.
     * @throws DaoException the Exception thrown when an error occur.
     */
    public SQLServerMasterMaintenanceDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
        this.snapLogger = (SnapLogger) SnapLogger.getInstance();
    }
    
    /**
     * @deprecated
     * @throws DaoException the Exception thrown when an error occur.
     */
    @Deprecated
    @Override
    public final ResultBase importSpartData(final MaintenanceTbl spartdata)
    throws DaoException {
       tp.methodEnter("importSpartData")
       .println("Maintenance Table", spartdata.toString());
       Connection connection = null;
       ResultBase rs = new ResultBase();
       try {
           connection = this.dbManager.getConnection();
           if (spartdata instanceof OpeMastTbl) {
               rs = this.addUpdateOperator((OpeMastTbl) spartdata, connection);
				if (rs.getNCRWSSResultCode() == ResultBase.RES_OK) {
					rs = this.addUpdateCredential((OpeMastTbl) spartdata,
							connection);
				} else {
					throw new DaoException("Fail to update OPE_MAST_TBL");
				}
           } else if (spartdata instanceof MdMMMastTbl) {
             if (((MdMMMastTbl) spartdata).getMmCode() != null && START_GROUP_MIXMATCH_CODE <= 
                     Integer.valueOf(((MdMMMastTbl) spartdata).getMmCode())) {
               rs = this.addUpdateGroupMixmatch(
                      (MdMMMastTbl) spartdata, connection);
             } else {
                   rs = this.addUpdateMixMatch(
                           (MdMMMastTbl) spartdata, connection);
        	   }
           } else if (spartdata instanceof MdMastTbl) {
        	   rs = this.addUpdateItem(
        			   (MdMastTbl)spartdata, connection);
           }
           connection.commit();
       } catch (DaoException e) {
           rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);
           rollBack(connection, "importSpartData", e);
       } catch (SQLException e) {
           rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);
           rollBack(connection, "importSpartData", e);
       } catch (Exception e) {
    	   rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);
           rollBack(connection, "importSpartData", e);
	   
	} finally {
           closeObject(connection);
           tp.methodExit(rs);
       }
        return rs;
    }

    /**
     * The private method for adding/updating a row in OPE_MAST_TBL of
     * WebStoreServer.
     * @param opemasttbl The data from SPART.
     * @param connection The Open Connection.
     * @return The Result Base.
     * @throws DaoException The exception thrown when an error occur.
     */
    private ResultBase addUpdateOperator(final OpeMastTbl opemasttbl,
            final Connection connection) throws DaoException {
        tp.methodEnter("addUpdateOperator");
        ResultBase rs = new ResultBase();
        rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);

        PreparedStatement addupdateOperatorStmnt = null;
        String functionName = "SQLServerMasterMaintenance.addUpdateOperator";
		try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            addupdateOperatorStmnt = connection.prepareStatement(
                    sqlStatement.getProperty(
                            "insert-update-operator-from-spart"));
            addupdateOperatorStmnt.setString(SQLStatement.PARAM1,
                    opemasttbl.getEmpCode());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM2,
                    opemasttbl.getOpeCode());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM3,
                    opemasttbl.getPassword());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM4,
                    opemasttbl.getOpeType());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM5,
                    opemasttbl.getOpeName());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM6,
                    opemasttbl.getOpeKanaName());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM7,
                    opemasttbl.getZipCode());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM8,
                    opemasttbl.getAddress());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM9,
                    opemasttbl.getTelNo());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM10,
                    opemasttbl.getFaxNo());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM11,
                    opemasttbl.getSecLevel1());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM12,
                    opemasttbl.getSecLevel2());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM13,
                    opemasttbl.getSubChar1());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM14,
                    opemasttbl.getSubChar2());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM15,
                    opemasttbl.getSubChar3());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM16,
                    opemasttbl.getInsDate());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM17,
                    opemasttbl.getUpdDate());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM18,
                    opemasttbl.getUpdAppId());
            addupdateOperatorStmnt.setString(SQLStatement.PARAM19,
                    opemasttbl.getUpdOpeCode());
            int rowaffected = addupdateOperatorStmnt.executeUpdate();

            if (SQLResultsConstants.NO_ROW_AFFECTED < rowaffected) {
                rs.setNCRWSSResultCode(ResultBase.RES_OK);
            }
        } catch (SQLException e) {
            tp.println("SQL Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "SQL Exception error occured. " + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "Error occured when SQLStatementException",
                        opemasttbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateOperator",
                "Output error OPE_MAST_TABLE in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } finally {
            closeObject(addupdateOperatorStmnt);
            tp.methodExit(rs);
        }

        return rs;
    }

    private ResultBase addUpdateCredential(final OpeMastTbl opemasttbl,
            final Connection connection) throws DaoException {
    	String functionName = "SQLServerMasterMaintenance.addUpdateCredential";
        tp.methodEnter(functionName);
        ResultBase rs = new ResultBase();
        rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);

        PreparedStatement addupdateCredentialStmnt = null;
        
		try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            addupdateCredentialStmnt = connection.prepareStatement(
                    sqlStatement.getProperty(
                            "insert-update-credential-from-spart"));
            addupdateCredentialStmnt.setString(SQLStatement.PARAM1,
                    opemasttbl.getEmpCode());
            addupdateCredentialStmnt.setString(SQLStatement.PARAM2,
                    opemasttbl.getPassword());
            addupdateCredentialStmnt.setString(SQLStatement.PARAM3,
                    opemasttbl.getOpeName());
            
            int rowaffected = addupdateCredentialStmnt.executeUpdate();

            if (SQLResultsConstants.NO_ROW_AFFECTED < rowaffected) {
                rs.setNCRWSSResultCode(ResultBase.RES_OK);
            }
        } catch (SQLException e) {
            tp.println("SQL Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "SQL Exception error occured. " + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "Error occured when SQLStatementException",
                        opemasttbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateOperator",
              "Output error MST_USER_CREDENTIALS in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } finally {
            closeObject(addupdateCredentialStmnt);
            tp.methodExit(rs);
        }

        return rs;
    }
    
    /**
     * The private method for adding/updating a row in MST_MIXMATCH of
     * WebStoreServer.
     * @param mdmmmasttbl The data from SPART.
     * @param connection The Open Connection.
     * @return The Result Base.
     * @throws DaoException The exception thrown when an error occur.
     */
    private ResultBase addUpdateMixMatch(final MdMMMastTbl mdmmmasttbl,
            final Connection connection) throws DaoException {
        tp.methodEnter("addUpdateMixMatch");
        ResultBase rs = new ResultBase();
        rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);

        PreparedStatement addupdateMixMatchStmnt = null;
        String functionName = "SQLServerMasterMaintenance.addUpdateMixMatch";
		try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            addupdateMixMatchStmnt = connection.prepareStatement(
                    sqlStatement.getProperty(
                            "insert-update-mixmatch-from-spart"));        
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM1,
            mdmmmasttbl.getStoreId());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM2,
            mdmmmasttbl.getMmCode());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM3,
            mdmmmasttbl.getMmStartDateId());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM4,
            mdmmmasttbl.getMmEndDateId());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM5,
            mdmmmasttbl.getMmType());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM6,
            mdmmmasttbl.getPriceMulti1());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM7,
            mdmmmasttbl.getDiscountPrice1());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM8,
            mdmmmasttbl.getEmpPrice11());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM9,
            mdmmmasttbl.getEmpPrice12());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM10,
            mdmmmasttbl.getEmpPrice13());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM11,
            mdmmmasttbl.getPriceMulti2());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM12,
            mdmmmasttbl.getDiscountPrice2());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM13,
            mdmmmasttbl.getEmpPrice21());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM14,
            mdmmmasttbl.getEmpPrice22());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM15,
            mdmmmasttbl.getEmpPrice23());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM16,
            mdmmmasttbl.getMmName());
            addupdateMixMatchStmnt.setString(SQLStatement.PARAM17,
            mdmmmasttbl.getMustBuyFlag());
            int rowaffected = addupdateMixMatchStmnt.executeUpdate();

            if (SQLResultsConstants.NO_ROW_AFFECTED < rowaffected) {
                rs.setNCRWSSResultCode(ResultBase.RES_OK);
            }
        } catch (SQLException e) {
            tp.println("SQL Statement Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Exception error occured."
                    + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "Error occured when SQLStatementException",
                        mdmmmasttbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateMixMatch",
                "Output error MST_MIXMATCH in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } catch (Exception e) {
            tp.println("General Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "General Exception error occured."
                    + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "General Exception error occured.",
                        mdmmmasttbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateMixMatch",
                "Output error MST_MIXMATCH in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } finally {
            closeObject(addupdateMixMatchStmnt);
            tp.methodExit(rs);
        }

        return rs;
      }

    /**
     * The private method for adding/updating a row in MST_GROUP_MIXMATCH of
     * WebStoreServer.
     * @param mdmmmasttbl The data from SPART.
     * @param connection The Open Connection.
     * @throws DaoException The exception thrown when an error occur.
     * @return The Result Base.
     */    
    private ResultBase addUpdateGroupMixmatch(final MdMMMastTbl mdmmmasttbl,
            final Connection connection) throws DaoException {
    	
    	String functionName = "SQLServerMasterMaintenance.addUpdateGroupMixmatch";
		tp.methodEnter(functionName);
        ResultBase rs = new ResultBase();
        rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);

        PreparedStatement addUpdateGroupMixmatchStmnt = null;
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            addUpdateGroupMixmatchStmnt = connection.prepareStatement(
                    sqlStatement.getProperty(
                            "insert-update-groupmixmatch-from-spart"));            
            addUpdateGroupMixmatchStmnt.setString(SQLStatement.PARAM1,
            mdmmmasttbl.getStoreId());
            addUpdateGroupMixmatchStmnt.setString(SQLStatement.PARAM2,
            mdmmmasttbl.getPriceMulti2());
            String discountPrice2 = mdmmmasttbl.getDiscountPrice2();
            int pos = discountPrice2.indexOf(".");
            if (pos >-1 ) {
            	discountPrice2 = discountPrice2.substring(0, discountPrice2.indexOf("."));
            }
            addUpdateGroupMixmatchStmnt.setString(SQLStatement.PARAM3,
            discountPrice2);
            addUpdateGroupMixmatchStmnt.setString(SQLStatement.PARAM4,
            mdmmmasttbl.getMmStartDateId());
            addUpdateGroupMixmatchStmnt.setString(SQLStatement.PARAM5,
            mdmmmasttbl.getMmEndDateId());
            addUpdateGroupMixmatchStmnt.setString(SQLStatement.PARAM6,
            mdmmmasttbl.getMmCode());
            addUpdateGroupMixmatchStmnt.setString(SQLStatement.PARAM7,
            mdmmmasttbl.getMmName());
            int rowaffected = addUpdateGroupMixmatchStmnt.executeUpdate();

            if (SQLResultsConstants.NO_ROW_AFFECTED < rowaffected) {
                rs.setNCRWSSResultCode(ResultBase.RES_OK);
            }
        } catch (SQLException e) {
            tp.println("SQL Statement Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Exception error occured."
                    + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "Error occured when SQLStatementException",
                        mdmmmasttbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateGroupMixmatch",
                "Output error MST_MIXMATCH in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } catch (Exception e) {
            tp.println("General Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "General Exception error occured."
                    + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "General Exception error occured.",
                        mdmmmasttbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateGroupMixmatch",
                "Output error MST_MIXMATCH in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } finally {
            closeObject(addUpdateGroupMixmatchStmnt);
            tp.methodExit(rs);
        }
        return rs;
      }

    /**
     * The private method for adding/updating a row in MST_PLU of
     * WebStoreServer.
     * @param mdmastbl The data from SPART.
     * @param connection The Open Connection.
     * @return The Result Base.
     * @throws DaoException The exception thrown when an error occur.
     */
    private ResultBase addUpdateItem(final MdMastTbl mdmastbl,
    		final Connection connection)throws DaoException {
    	
    	String functionName = "SQLServerMasterMaintenanceDAO.addUpdateItem";
        tp.methodEnter(functionName);
        ResultBase rs = new ResultBase();
        rs.setNCRWSSResultCode(ResultBase.RES_MAINTENACE_IMPORT_ERROR);

        PreparedStatement addupdateItemStmnt = null;
        
		try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            addupdateItemStmnt = connection.prepareStatement(
            sqlStatement.getProperty("insert-update-item-from-spart"));
            addupdateItemStmnt.setString(SQLStatement.PARAM1,mdmastbl.getStoreid());
            addupdateItemStmnt.setString(SQLStatement.PARAM2,mdmastbl.getPlu());
            addupdateItemStmnt.setString(SQLStatement.PARAM3,mdmastbl.getMdInternal());
            addupdateItemStmnt.setString(SQLStatement.PARAM4,mdmastbl.getMdType());
            addupdateItemStmnt.setString(SQLStatement.PARAM5,mdmastbl.getMdVender());
            addupdateItemStmnt.setString(SQLStatement.PARAM6,mdmastbl.getDivision());
            addupdateItemStmnt.setString(SQLStatement.PARAM7,mdmastbl.getCategory());
            addupdateItemStmnt.setString(SQLStatement.PARAM8,mdmastbl.getBrand());
            addupdateItemStmnt.setString(SQLStatement.PARAM9,mdmastbl.getSku());
            addupdateItemStmnt.setString(SQLStatement.PARAM10,mdmastbl.getSize());
            addupdateItemStmnt.setString(SQLStatement.PARAM11,mdmastbl.getKeyPlu());
            addupdateItemStmnt.setString(SQLStatement.PARAM12,mdmastbl.getMd1());
            addupdateItemStmnt.setString(SQLStatement.PARAM13,mdmastbl.getMd2());
            addupdateItemStmnt.setString(SQLStatement.PARAM14,mdmastbl.getMd3());
            addupdateItemStmnt.setString(SQLStatement.PARAM15,mdmastbl.getMd4());
            addupdateItemStmnt.setString(SQLStatement.PARAM16,mdmastbl.getMd5());
            addupdateItemStmnt.setString(SQLStatement.PARAM17,mdmastbl.getMd6());
            addupdateItemStmnt.setString(SQLStatement.PARAM18,mdmastbl.getMd7());
            addupdateItemStmnt.setString(SQLStatement.PARAM19,mdmastbl.getMd8());
            addupdateItemStmnt.setString(SQLStatement.PARAM20,mdmastbl.getMd9());
            addupdateItemStmnt.setString(SQLStatement.PARAM21,mdmastbl.getMd10());
            addupdateItemStmnt.setString(SQLStatement.PARAM22,mdmastbl.getMdName());
            addupdateItemStmnt.setString(SQLStatement.PARAM23,mdmastbl.getMdName1());
            addupdateItemStmnt.setString(SQLStatement.PARAM24,mdmastbl.getMdName2());
            addupdateItemStmnt.setString(SQLStatement.PARAM25,mdmastbl.getMdKanaName());
            addupdateItemStmnt.setString(SQLStatement.PARAM26,mdmastbl.getMdKanaName1());
            addupdateItemStmnt.setString(SQLStatement.PARAM27,mdmastbl.getMdKanaName2());
            addupdateItemStmnt.setString(SQLStatement.PARAM28,mdmastbl.getOrgSalesPrice1());
            addupdateItemStmnt.setString(SQLStatement.PARAM29,mdmastbl.getSalesPrice2());
            addupdateItemStmnt.setString(SQLStatement.PARAM30,mdmastbl.getSalesPrice1());
            addupdateItemStmnt.setString(SQLStatement.PARAM31,mdmastbl.getEmpPrice1());
            addupdateItemStmnt.setString(SQLStatement.PARAM32,mdmastbl.getEmpPrice2());
            addupdateItemStmnt.setString(SQLStatement.PARAM33,mdmastbl.getEmpPrice3());
            addupdateItemStmnt.setString(SQLStatement.PARAM34,mdmastbl.getPuPrice1());
            addupdateItemStmnt.setString(SQLStatement.PARAM35,mdmastbl.getPuPrice2());
            addupdateItemStmnt.setString(SQLStatement.PARAM36,mdmastbl.getPuPriceChgDate1());
            addupdateItemStmnt.setString(SQLStatement.PARAM37,mdmastbl.getPuPriceChgDate2());
            addupdateItemStmnt.setString(SQLStatement.PARAM38,mdmastbl.getOrgCostPrice1());
            addupdateItemStmnt.setString(SQLStatement.PARAM39,mdmastbl.getCostPrice1());
            addupdateItemStmnt.setString(SQLStatement.PARAM40,mdmastbl.getCostPrice2());
            addupdateItemStmnt.setString(SQLStatement.PARAM41,mdmastbl.getCostPriceChgDate1());
            addupdateItemStmnt.setString(SQLStatement.PARAM42,mdmastbl.getCostPriceChgDate2());
            addupdateItemStmnt.setString(SQLStatement.PARAM43,mdmastbl.getSalesDate());
            addupdateItemStmnt.setString(SQLStatement.PARAM44,mdmastbl.getMakerPrice());
            addupdateItemStmnt.setString(SQLStatement.PARAM45,mdmastbl.getTaxType());
            addupdateItemStmnt.setString(SQLStatement.PARAM46,mdmastbl.getDiscountType());
            addupdateItemStmnt.setString(SQLStatement.PARAM47,mdmastbl.getSeasonType());
            addupdateItemStmnt.setString(SQLStatement.PARAM48,mdmastbl.getPaymentType());
            addupdateItemStmnt.setString(SQLStatement.PARAM49,mdmastbl.getOrderType());
            addupdateItemStmnt.setString(SQLStatement.PARAM50,mdmastbl.getPosMdType());
            addupdateItemStmnt.setString(SQLStatement.PARAM51,mdmastbl.getCatType());
            addupdateItemStmnt.setString(SQLStatement.PARAM52,mdmastbl.getOrderUnit());
            addupdateItemStmnt.setString(SQLStatement.PARAM53,mdmastbl.getOrderPoint());
            addupdateItemStmnt.setString(SQLStatement.PARAM54,mdmastbl.getBaseStockCnt());
            addupdateItemStmnt.setString(SQLStatement.PARAM55,mdmastbl.getConn1());
            addupdateItemStmnt.setString(SQLStatement.PARAM56,mdmastbl.getConnType1());
            addupdateItemStmnt.setString(SQLStatement.PARAM57,mdmastbl.getConn2());
            addupdateItemStmnt.setString(SQLStatement.PARAM58,mdmastbl.getConnType2());
            addupdateItemStmnt.setString(SQLStatement.PARAM59,mdmastbl.getVenderCode());
            addupdateItemStmnt.setString(SQLStatement.PARAM60,mdmastbl.getVenderType());
            addupdateItemStmnt.setString(SQLStatement.PARAM61,mdmastbl.getTagCode1());
            addupdateItemStmnt.setString(SQLStatement.PARAM62,mdmastbl.getTagCode2());
            addupdateItemStmnt.setString(SQLStatement.PARAM63,mdmastbl.getTagCode3());
            addupdateItemStmnt.setString(SQLStatement.PARAM64,mdmastbl.getPointRate());
            addupdateItemStmnt.setString(SQLStatement.PARAM65,mdmastbl.getSubMoney1());
            addupdateItemStmnt.setString(SQLStatement.PARAM66,mdmastbl.getSubMoney2());
            addupdateItemStmnt.setString(SQLStatement.PARAM67,mdmastbl.getSubMoney3());
            addupdateItemStmnt.setString(SQLStatement.PARAM68,mdmastbl.getSubMoney4());
            addupdateItemStmnt.setString(SQLStatement.PARAM69,mdmastbl.getSubMoney5());
            addupdateItemStmnt.setString(SQLStatement.PARAM70,mdmastbl.getSubCode1());
            addupdateItemStmnt.setString(SQLStatement.PARAM71,mdmastbl.getSubCode2());
            addupdateItemStmnt.setString(SQLStatement.PARAM72,mdmastbl.getSubCode3());
            addupdateItemStmnt.setString(SQLStatement.PARAM73,mdmastbl.getSubCode4());
            addupdateItemStmnt.setString(SQLStatement.PARAM74,mdmastbl.getSubCode5());
            addupdateItemStmnt.setString(SQLStatement.PARAM75,mdmastbl.getSubCode6());
            addupdateItemStmnt.setString(SQLStatement.PARAM76,mdmastbl.getSubCode7());
            addupdateItemStmnt.setString(SQLStatement.PARAM77,mdmastbl.getSubCode8());
            addupdateItemStmnt.setString(SQLStatement.PARAM78,mdmastbl.getSubCode9());
            addupdateItemStmnt.setString(SQLStatement.PARAM79,mdmastbl.getSubCode10());
            addupdateItemStmnt.setString(SQLStatement.PARAM80,mdmastbl.getSubTinyInt1());
            addupdateItemStmnt.setString(SQLStatement.PARAM81,mdmastbl.getSubTinyInt2());
            addupdateItemStmnt.setString(SQLStatement.PARAM82,mdmastbl.getSubTinyInt3());
            addupdateItemStmnt.setString(SQLStatement.PARAM83,mdmastbl.getSubTinyInt4());
            addupdateItemStmnt.setString(SQLStatement.PARAM84,mdmastbl.getSubTinyInt5());
            addupdateItemStmnt.setString(SQLStatement.PARAM85,mdmastbl.getSubTinyInt6());
            addupdateItemStmnt.setString(SQLStatement.PARAM86,mdmastbl.getSubTinyInt7());
            addupdateItemStmnt.setString(SQLStatement.PARAM87,mdmastbl.getSubTinyInt8());
            addupdateItemStmnt.setString(SQLStatement.PARAM88,mdmastbl.getSubTinyInt9());
            addupdateItemStmnt.setString(SQLStatement.PARAM89,mdmastbl.getSubInt10());
            addupdateItemStmnt.setString(SQLStatement.PARAM90,mdmastbl.getInsDate());
            addupdateItemStmnt.setString(SQLStatement.PARAM91,mdmastbl.getUpdDate());
            addupdateItemStmnt.setString(SQLStatement.PARAM92,mdmastbl.getUpdAppId());
            addupdateItemStmnt.setString(SQLStatement.PARAM93,mdmastbl.getUpdOpeCode());
            addupdateItemStmnt.setString(SQLStatement.PARAM94,mdmastbl.getPlu());           
            addupdateItemStmnt.setString(SQLStatement.PARAM95,mdmastbl.getTagType());
            int rowaffected = addupdateItemStmnt.executeUpdate();

            if (SQLResultsConstants.NO_ROW_AFFECTED < rowaffected) {
                rs.setNCRWSSResultCode(ResultBase.RES_OK);
            }
        } catch (SQLException e) {
            tp.println("SQL Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "SQL Exception error occured. " + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "Error occured when SQLException",
                        mdmastbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateItem",
                "Output error MD_MAST_TBL in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } catch (Exception e) {
            tp.println("SQL Exception error occured.");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "General Exception error occured. " + e.getMessage());
            Snap.SnapInfo info =
                this.snapLogger.write(
                        "General exception Occured",
                        mdmastbl.toString());
            LOGGER.logSnap(PROG_NAME, "addUpdateItem",
                "Output error MD_MAST_TBL in JSON object representation"
                    + " data to snap file.", info);
            throw new DaoException(e);
        } finally {
            closeObject(addupdateItemStmnt);
            tp.methodExit(rs);
        }

        return rs;
    }
}
