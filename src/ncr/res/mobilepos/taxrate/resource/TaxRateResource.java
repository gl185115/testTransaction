package ncr.res.mobilepos.taxrate.resource;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customerSearch.constants.CustomerSearchConstants;
import ncr.res.mobilepos.customerSearch.dao.ICustomerSearthDAO;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.ChangeableTaxRate;
import ncr.res.mobilepos.pricing.model.DefaultTaxRate;
import ncr.res.mobilepos.pricing.model.TaxRateInfo;
import ncr.res.mobilepos.promotion.factory.TaxRateInfoFactory;
import ncr.res.mobilepos.taxrate.dao.ITaxRateDao;
import ncr.res.mobilepos.taxrate.dao.SQLServerTaxRateDao;
import ncr.res.mobilepos.taxrate.model.DptTaxRate;

/**
 * PromotionResource Class is a Web Resource which support MobilePOS Promotion
 * processes.
 *
 */

@Path("/taxrate")
@Api(value="/TaxRate", description="消費税率API")
public class TaxRateResource {

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get
                                                                        // the
                                                                        // Logger
    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "taxRate";

    private final List<TaxRateInfo> taxRateInfoList;
    /**
     * Default Constructor for PromotionResource.
     *
     * <P>
     * Initializes the logger object.
     */
    public TaxRateResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        taxRateInfoList = TaxRateInfoFactory.getInstance();
    }

    /**
     * get the taxRate
     * @param businessdate the date of request
     * @return taxRate
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/gettaxrate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="消費税率取得", response=Map.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
    		@ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final Map<String,String> itemMixMatchInfobySku(
    		@ApiParam(name="businessDate", value="営業日")@FormParam("businessDate") final String businessdate) {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("businessdate", businessdate);
        Map<String,String> map = new HashMap<String,String>();
        try {
            if (StringUtility.isNullOrEmpty(businessdate)) {
                tp.println("Parameter[s] is empty or null.");
                return map;
            }
            ITaxRateDao dao = new SQLServerTaxRateDao();
            map = dao.getTaxRateByDate(businessdate);

        } catch (DaoException e) {
            if (e.getCause() instanceof SQLException) {
                LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                        functionName + ": Failed to get taxRate." + e.getMessage(), e);
                map.put("errorCode",String.valueOf(ResultBase.RES_ERROR_DB));
            } else {
                LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQLSTATEMENT,
                        functionName + ": Failed to get taxRate." + e.getMessage(), e);
                map.put("errorCode", String.valueOf(ResultBase.RES_ERROR_DAO));
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get taxRate.", e);
            map.put("errorCode", String.valueOf(ResultBase.RES_ERROR_GENERAL));
        } finally {
            tp.methodExit(map);
        }
        return map;
    }

    /**
     * get the dptTaxRate
     * @param companyId, retailstoreid, departmentId
     * @return taxRate
     */
    @Path("/getDptTaxRate")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="部門に紐づく税率区分とその特性情報取得", response=DptTaxRate.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final DptTaxRate getDptTaxRate(
    		@ApiParam(name="companyid", value="会社コード") @QueryParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="店舗コード") @QueryParam("retailstoreid") final String retailstoreId,
    		@ApiParam(name="departmentid", value="部門コード") @QueryParam("departmentid") final String departmentId) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyid", companyId)
									.println("retailstoreid", retailstoreId)
									.println("departmentid", departmentId);
		DptTaxRate taxRateInfo = new DptTaxRate();
		try {
			if (StringUtility.isNullOrEmpty(companyId) || StringUtility.isNullOrEmpty(retailstoreId)
					|| StringUtility.isNullOrEmpty(departmentId)) {
				tp.println("Parameter[s] is empty or null.");
				taxRateInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				return taxRateInfo;
			}
			ITaxRateDao dao = new SQLServerTaxRateDao();
			List<TaxRateInfo> taxInfoList = new ArrayList<TaxRateInfo>();
			DefaultTaxRate defaultTaxRate = null;
			ChangeableTaxRate changeableTaxRate = null;

        	// get common url
			DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			ICustomerSearthDAO iCustomerSearthDAO = sqlServer.getCustomerSearthDAO();
			Map<String, String> mapTaxId = iCustomerSearthDAO.getPrmSystemConfigValue(CustomerSearchConstants.CATEGORY_TAX);

			String taxId_Type = dao.getTaxRateByDptId(companyId, retailstoreId, departmentId, mapTaxId);
			String taxId = null;
			String taxType = null;

			if(!StringUtility.isNullOrEmpty(taxId_Type)) {
				if(taxId_Type.contains(",")){
					taxId = taxId_Type.split(",")[0];
					taxType = taxId_Type.split(",")[1];
					taxRateInfo.setTaxId(Integer.parseInt(taxId));
				}else{
					taxRateInfo.setTaxId(null);
					taxType = taxId_Type;
				}
			}
			
			// 非課税の場合、商品の税率情報を取得する
			if(("2").equals(taxType)){
				defaultTaxRate = new DefaultTaxRate();
				defaultTaxRate.setRate(0);
				taxRateInfo.setTaxId(null);
				taxRateInfo.setDefaultTaxRate(defaultTaxRate);
				return taxRateInfo;
			}
			
			if (taxRateInfoList != null) {
				for (TaxRateInfo TaxInfo : taxRateInfoList) {
					if (TaxInfo.getTaxId().equals(taxRateInfo.getTaxId())) {
						taxInfoList.add(TaxInfo);
					}
				}
			}
			if(taxInfoList.size() > 0){
				for (TaxRateInfo TaxInfo : taxInfoList) {
					if (TaxInfo.getSubNum1() == 0 && TaxInfo.getSubNum2() == 1) {
						if (defaultTaxRate != null) {
							LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_TABLE_DATA_ERR,
									"The data in MST_TAXRATE is error.\n");
							taxRateInfo.setNCRWSSResultCode(ResultBase.RES_TABLE_DATA_ERR);
							taxRateInfo.setMessage("The data in MST_TAXRATE is error.");
							return taxRateInfo;
						} else {
							defaultTaxRate = new DefaultTaxRate();
							defaultTaxRate.setRate(TaxInfo.getTaxRate());
						}
					}
					if (TaxInfo.getSubNum1() == 0 && TaxInfo.getSubNum2() == 0) {
						changeableTaxRate = new ChangeableTaxRate();
						changeableTaxRate.setRate(TaxInfo.getTaxRate());
					}
					if (TaxInfo.getSubNum1() == 1 && TaxInfo.getSubNum2() == 1) {
						if (defaultTaxRate != null) {
							LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_TABLE_DATA_ERR,
									"The data in MST_TAXRATE is error.\n");
							taxRateInfo.setNCRWSSResultCode(ResultBase.RES_TABLE_DATA_ERR);
							taxRateInfo.setMessage("The data in MST_TAXRATE is error.");
							return taxRateInfo;
						} else {
							defaultTaxRate = new DefaultTaxRate();
							defaultTaxRate.setRate(TaxInfo.getTaxRate());
							defaultTaxRate.setReceiptMark(TaxInfo.getSubCode1());
							defaultTaxRate.setReducedTaxRate(TaxInfo.getSubNum1());
						}
					}
					if (TaxInfo.getSubNum1() == 1 && TaxInfo.getSubNum2() == 0) {
						changeableTaxRate = new ChangeableTaxRate();
						changeableTaxRate.setRate(TaxInfo.getTaxRate());
						changeableTaxRate.setReceiptMark(TaxInfo.getSubCode1());
						changeableTaxRate.setReducedTaxRate(TaxInfo.getSubNum1());
					}
				}
			}
			
			if(changeableTaxRate != null || defaultTaxRate != null){
				taxRateInfo.setDepartmentId(departmentId);
				taxRateInfo.setChangeableTaxRate(changeableTaxRate);
				taxRateInfo.setDefaultTaxRate(defaultTaxRate);
			}else{
				LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_GET_DATA_ERR,
						"税率取得エラー。\n"+"Company="+ companyId +",Store="+ retailstoreId + ",DPT=" + departmentId);
				taxRateInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				taxRateInfo.setMessage("The data is not found.");
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get taxRate.", ex);
			taxRateInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(taxRateInfo);
		}
		return taxRateInfo;
	}
}
