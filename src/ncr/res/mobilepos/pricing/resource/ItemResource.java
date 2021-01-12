/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ItemResource
 *
 * Resource which provides Web Service for Item details retrieval
 *
 * Meneses, Chris Niven
 * Campos, Carlos
 */

package ncr.res.mobilepos.pricing.resource;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.dao.IPricePromInfoDAO;
import ncr.res.mobilepos.pricing.dao.IUrgentChangePriceInfoDAO;
import ncr.res.mobilepos.pricing.factory.PriceMMInfoFactory;
import ncr.res.mobilepos.pricing.factory.PricePromInfoFactory;
import ncr.res.mobilepos.pricing.factory.PriceUrgentInfoFactory;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PickList;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.pricing.model.PricePromInfoList;
import ncr.res.mobilepos.pricing.model.PriceUrgentInfo;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.model.UrgentChangeItemInfo;
import ncr.res.mobilepos.promotion.model.Promotion;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.model.Transaction;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

/**
 * ItemResource Web Resource Class
 *
 * <P>
 * Supports MobilePOS Item Pricing/Search processes.
 *
 */
@Path("/pricing")
@Api(value="/pricing", description="���i���擾API")
public class ItemResource {

    /**
     * ServletContext.
     */
    @Context
    private ServletContext context;

    /**
     * SecurityContext.
     */
    @Context
    private SecurityContext securityContext;

    /**
     * @return the context
     */
    public final ServletContext getContext() {
        return context;
    }

    /**
     * @param contextToSet
     *            the context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * The resource DAO factory.
     */
    private DAOFactory sqlServerDAO;
    /**
     * The program name.
     */
    private String progname = "ItemRes";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    private String pathName = "pricing";

    private static BarcodeAssignment barcodeAssignment;

	private final List<PricePromInfo> pricePromInfoList;
	
	private final List<PriceMMInfo> priceMMInfoList;
	private final List<PriceUrgentInfo> priceUrgentInfoList;

	public static final String PROMOTIONTYPE_DPT = "1";
	public static final String PROMOTIONTYPE_LINE = "2";
	public static final String PROMOTIONTYPE_ITEMCODE = "3";
	public static final String PROMOTIONTYPE_CLASS = "4"; 
	public static final String DISCOUNTCLASS_RATE = "1";
	public static final String DISCOUNTCLASS_AMT = "2";	
	public static final String DISCOUNTCLASS_PRICE = "3";

    /**
     * Default Constructor. Instantiate ioWriter and sqlServerDAO member
     * variables.
     */
    public ItemResource() {
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        barcodeAssignment = BarcodeAssignmentFactory.getInstance();
        pricePromInfoList = PricePromInfoFactory.getInstance();
        priceMMInfoList = PriceMMInfoFactory.getInstance();
        priceUrgentInfoList = PriceUrgentInfoFactory.getInstance();
    }

    /**
     * Sets the DaoFactory of the ItemResource to use the DAO methods.
     *
     * @param daoFactory
     *            The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daoFactory) {
        this.sqlServerDAO = daoFactory;
    }

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * Method called by the Web Service for retrieving the information of an
     * Item by specifying its Store ID and the Item's price Look Up.<br>
     * <br>
     *
     * Interface Name: Price Lookup<br>
     * Request Type: POST<br>
     * URL: {Base URI}/pricing/{storeid}/{plucode}<br>
     * Produces: {@link SearchedProduct} JSON Object<br>
     *
     * @param storeID
     *            The Store ID of the store which the item is located
     * @param pluCode
     *            The Item's Price Look Up
     * @param companyId
     * @param bussinessDate
     * @return The details of an Item
     */
    @Path("/{storeid}/{plucode}")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value="���i���擾", response=SearchedProduct.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ITEM_NOT_EXIST, message="���i�����G���[ (���t����Ȃ�)")
        })
    public final SearchedProduct getItemByPLUcode(
    		@ApiParam(name="storeid", value="�X�ԍ�") @PathParam("storeid") final String storeID,
    		@ApiParam(name="plucode", value="PLU�R�[�h") @PathParam("plucode") final String pluCode,
    		@ApiParam(name="companyId", value="��ЃR�[�h") @FormParam("companyId") final String companyId,
    		@ApiParam(name="bussinessDate", value="�Ɩ����t") @FormParam("bussinessDate") final String bussinessDate) {

        String functionName = "ItemResource.getItemByPLUcode";
        tp.methodEnter("getItemByPLUcode")
                .println("storeid", storeID)
                .println("plucode", pluCode)
                .println("companyId", companyId);
        SearchedProduct searchedProduct = new SearchedProduct();

        // Check if pricingtype is normal(0).
        // If 1, only PLU code is returned without searching database (MST_PLU).
        // If 0, continue searching.
        if (!isNormalPricing()) {
            Item item = new Item();
            item.setItemId(pluCode.contains(" ")?pluCode.split(" ")[0]:pluCode);
            item.setAgeRestrictedFlag("0");
            searchedProduct.setItem(item);
            tp.println("Pricing is not normal.");
            tp.methodExit(searchedProduct);
            return searchedProduct;
        }

        Item returnItem = null;
        try {

        	// get common url
			DAOFactory sqlServer = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
			SQLServerSystemConfigDAO systemDao = sqlServer.getSystemConfigDAO();
			Map<String, String> mapTaxId = systemDao.getPrmSystemConfigValue(GlobalConstant.CATE_TAX_ID);
			//�^�Ԏ擾����
			String comstdName = systemDao.getParameterString(GlobalConstant.KEY_COLUMN_PLU, GlobalConstant.CATE_COMSTD_NAME);

            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();
            returnItem = itemDAO.getItemByPLU(storeID, pluCode,companyId,Integer.parseInt(priceIncludeTax),bussinessDate,mapTaxId,comstdName);

        } catch (DaoException daoEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get the item details.\n" + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                searchedProduct.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                searchedProduct.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            searchedProduct.setMessage(daoEx.getMessage());
        } catch (Exception e) {
            tp.println(context.toString());
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            searchedProduct.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            searchedProduct.setMessage(e.getMessage());
        } finally {
            if (null == returnItem) {
                returnItem = new Item();
                returnItem.setItemId(pluCode.contains(" ")?pluCode.split(" ")[0]:pluCode);
                returnItem.setAgeRestrictedFlag("0");
                searchedProduct.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
            }
            searchedProduct.setItem(returnItem);
            tp.methodExit(searchedProduct);
        }

        return searchedProduct;
    }

    /**
     * Get The Item Price List.
     * @param transaction transaction
     * @param storeId  The ID of The Store
     * @param companyId The ID of The Company
     * @return itemList The List of The Item Price
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    @Path("/getItemPrice")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ApiOperation(value="���i�̉��i��񃊃X�g�̎擾", response=Item.class)
    public final List<Item> getItemsPrice(
    		@ApiParam(name="transaction", value="������") @FormParam("transaction") String transaction,
    		@ApiParam(name="StoreId", value="�X�ԍ�") @FormParam("StoreId") String storeId,
    		@ApiParam(name="CompanyId", value="��ЃR�[�h") @FormParam("CompanyId") String companyId ,
    		@ApiParam(name="businessDate", value="�Ɩ����t") @FormParam("businessDate") String businessDate) {
        String functionName = "getItemsPrice";
        tp.methodEnter(functionName).println("transaction", transaction).println("storeId", storeId)
                .println("companyId", companyId);

        List<Item> itemList = new ArrayList<Item>();
        if (StringUtility.isNullOrEmpty(transaction, companyId, storeId,businessDate)) {
            return itemList;
        }
        try {
            JsonMarshaller<Transaction> jsonMarshall = new JsonMarshaller<Transaction>();
            Transaction transactionIn = jsonMarshall.unMarshall(transaction, Transaction.class);
            List<Sale> sales = transactionIn.getSales();
            DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IItemDAO iItemDAO = daoFactory.getItemDAO();
            for (Sale saleIn : sales) {
                Item item = new Item();
                item = iItemDAO.getItemBypluCode(storeId, saleIn.getItemId(), companyId,businessDate);
                if (null != item) {
                    itemList.add(item);
                }
            }

        } catch (IOException e1) {
            LOGGER.logAlert(progname, Logger.RES_EXCEP_IO, functionName + ": Failed to get item price.", e1);
            return null;
        } catch (DaoException daoEx) {
            LOGGER.logSnapException(progname, Logger.RES_EXCEP_DAO, functionName + ": Failed to get Group Line�@Info.",
                    daoEx);
            return null;
        } catch (Exception e) {
            LOGGER.logSnapException(progname, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get Group Line�@Info.", e);
            return null;
        } finally {
            tp.methodExit(itemList);
        }
        return itemList;
    }

    /**
     *
     * @param companyId
     * @param storeId
     * @param itemType
     * @return
     */
    @Path("/getpicklistitems")
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="�s�b�N���X�g���i���擾", response=PickList.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
    public final PickList getPickList(
    		@ApiParam(name="companyId", value="��ЃR�[�h") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="�X�ԍ�") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="itemType", value="�A�C�e���^�C�v") @QueryParam("itemType") final String itemType) {
        String functionName = "getPickList";
        tp.methodEnter(functionName)
          .println("companyId", companyId)
          .println("storeId", storeId)
          .println("itemType", itemType);
        PickList pickList = null;
        try {
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            pickList = new PickList();
            pickList.setItems(itemDAO.getPickListItems(companyId, storeId, itemType));
        } catch (Exception ex) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            pickList.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(pickList);
        }
        return pickList;
    }

    /**
     *
     * @return towStep and deptEntry is true items
     */
    @Path("/getBarcodeInfo")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�o�[�R�[�h���擾", response=BarcodeAssignment.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        })
	public final BarcodeAssignment getBarcodeInfo() {
		String functionName = "getBarcodeInfo";
		tp.methodEnter(functionName);

		try {
			if(barcodeAssignment == null){
			    barcodeAssignment = new BarcodeAssignment();
			    barcodeAssignment.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOINFORMATION);
			    barcodeAssignment.setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOINFORMATION);
			    barcodeAssignment.setMessage("xml file information get failed");
				tp.println("xml file no information");

				return barcodeAssignment;
			}

			barcodeAssignment.setNCRWSSResultCode(ResultBase.RES_OK);
			barcodeAssignment.setNCRWSSExtendedResultCode(ResultBase.RES_OK);
			barcodeAssignment.setMessage("xml file information get success");
			tp.println("xml file information get success");
		} catch (Exception ex) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL, ex.getMessage());
		} finally {
			tp.methodExit(barcodeAssignment);
		}
		return barcodeAssignment;
	}

    /**
     * Method called by the Web Service for the urgent change Pirce Client's Request. <br>
     * <br>
     *
     * Interface Name: getUrgentChangeItemInfo<br>
     * Request Type: POST<br>
     * URL: {Base URI}/pricing/getUrgentChangeItemInfo<br>
     * Produces: {@link UrgentChangeItemInfo} JSON Object<br>
     *
     * @param storecd
     *            The Store ID of the store which the item is located
     * @param mdinternal
     *            The Item's Price Look Up
     * @param companyId
     *            The Company ID of the store which the item is located
     * @param bizdate
     * @param bizdatetime
     * @return The UrgentChangeItemInfo
     */
	@Path("/getUrgentChangeItemInfo")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�ً}�������i���擾", response=UrgentChangeItemInfo.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
            @ApiResponse(code=ResultBase.RES_ITEM_NOT_EXIST, message="�v���W�F�N�g�͑��݂��Ȃ�")
        })
    public final UrgentChangeItemInfo getUrgentChangeItemInfo(
    		@ApiParam(name="storecd", value="�X��") @FormParam("storecd") final String storeID,
    		@ApiParam(name="bizdate", value="����") @FormParam("bizdate") final String bizDate,
    		@ApiParam(name="bizdatetime", value="���t") @FormParam("bizdatetime") final String bizDatetime,
    		@ApiParam(name="mdinternal", value="���i�R�[�h") @FormParam("mdinternal") final String pluCode) {

        String functionName = "ItemResource.getUrgentChangeItemInfo";
        tp.methodEnter("getUrgentChangeItemInfo")
                .println("storecd", storeID)
                .println("mdinternal", pluCode)
                .println("bizdate", bizDate)
                .println("bizdatetime", bizDatetime);
                
        
        UrgentChangeItemInfo urgentChangeItemInfo = new UrgentChangeItemInfo();
        
        try {       	
        	// Check parameters
			if (StringUtility.isNullOrEmpty(storeID, pluCode, bizDate, bizDatetime)) {
				urgentChangeItemInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return urgentChangeItemInfo;
			}
        	
        	// get UrgentMst by pluCode
			IUrgentChangePriceInfoDAO urgentChangePriceInfoDao = sqlServerDAO.getUrgentChangePriceInfoDAO();
			urgentChangeItemInfo = urgentChangePriceInfoDao.getUrgentChangePriceInfo("01", storeID, pluCode, bizDate, bizDatetime);

        } catch (DaoException daoEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get the item details.\n" + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
            	urgentChangeItemInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
            	urgentChangeItemInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
            urgentChangeItemInfo.setMessage(daoEx.getMessage());
        } catch (Exception e) {
            tp.println(context.toString());
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            urgentChangeItemInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            urgentChangeItemInfo.setMessage(e.getMessage());
        } finally {
            if (null == urgentChangeItemInfo) {
            	urgentChangeItemInfo = new UrgentChangeItemInfo();
                urgentChangeItemInfo.setNCRWSSResultCode(ResultBase.RES_ITEM_NOT_EXIST);
            }
            tp.methodExit(urgentChangeItemInfo);
        }

        return urgentChangeItemInfo;
    }
	@POST
	@Produces("application/json;charset=UTF-8")
	@Path("/getPricePromInfoDptList")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "�ꊇ���������擾", response = Promotion.class)
	@ApiResponses(value = { @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���["),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "IO�ُ�"),
			@ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����̃p�����[�^") })
	public final PricePromInfoList getPricePromInfoDptList(
			@ApiParam(name = "companyId", value = "��ƃR�[�h") @FormParam("companyId") final String companyId,
			@ApiParam(name = "retailstoreid", value = "�X�ԍ�") @FormParam("retailstoreid") final String retailStoreId,
			@ApiParam(name = "businessdate", value = "�Ɩ����t") @FormParam("businessdate") final String businessDate) {
		String functionName = "getPricePromInfoDptList";
		tp.methodEnter(functionName).println("companyId", companyId).println("RetailStoreId", retailStoreId)
				.println("businessDate", businessDate);
		PricePromInfoList response = new PricePromInfoList();
		try {
			if (StringUtility.isNullOrEmpty(retailStoreId, companyId, businessDate)) {
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				tp.println("Parameter[s] is empty or null.");
				return response;
			}
			// get valid data
			IPricePromInfoDAO pricePromInfoDAODao = sqlServerDAO.getPricePromInfoDAO();
			List<PricePromInfo> pricePromInfoList = pricePromInfoDAODao.getPricePromInfoDpt(companyId, retailStoreId,
					businessDate);
			if (pricePromInfoList == null || pricePromInfoList.size() < 1) {
				tp.println("Not found valid data.");
				response.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				return response;
			}

			response.setPricePromInfoList(pricePromInfoList);

		} catch (Exception e) {
			LOGGER.logAlert(progname, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to send getPricePromInfoDptList.", e);
			response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			response.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(response);
		}
		return response;
	}
    /**
     * Checks if storeid is an enterprise store(0).
     *
     * @param storeID
     *            to check.
     * @return true if an enterprise storeid, false if not.
     */
    private boolean isEnterpriseStore(final String storeID) {
        return null != storeID && !storeID.isEmpty() &&
        		"0".equals(storeID.trim());
    }

    /** The constant for normal pricing. */
    private static final String NORMAL_PRICING = "0";

    /**
     * Checks if is normal pricing.
     *
     * @return true, if is normal pricing
     */
    private boolean isNormalPricing() {
        String pricingType = GlobalConstant.getPricingType();
        return pricingType == null || pricingType.equals(NORMAL_PRICING);
    }

    private String getOpeCode() {
        return ((securityContext != null) && (securityContext
                .getUserPrincipal()) != null) ? securityContext
                .getUserPrincipal().getName() : null;
    }

	 /**
     * Get The Price Prom Info.
     * @param sku The ID of The Sku
     * @param dpt  The ID of The Department
     * @param line The ID of The Line
     * @return PricePromInfo The Price Prom Info Object
     */
    public final PricePromInfo getPricePromInfo(
			final String sku, final String dpt, final String line) {

		if (pricePromInfoList == null){
			return null;
		}
		for (PricePromInfo pricePromInfo : pricePromInfoList) {
			switch (pricePromInfo.getPromotionType()) {
			case PROMOTIONTYPE_ITEMCODE:
				String pricePromSku = pricePromInfo.getSku();
				String itemSku = sku;
				if (!StringUtility.isNullOrEmpty(pricePromSku)) {
					if (pricePromSku.contains("*")) {
						if (itemSku.startsWith(pricePromSku.replace("*", ""))) {
							return pricePromInfo;
						}
					} else {
						if (itemSku.equals(pricePromSku)) {
							return pricePromInfo;
						}
					}
				}
				break;
			case PROMOTIONTYPE_LINE:
				if (line != null && line.equals(pricePromInfo.getLine())){
					return pricePromInfo;
				}
				break;
			case PROMOTIONTYPE_DPT:
				if (dpt != null && dpt.equals(pricePromInfo.getDpt())){
					return pricePromInfo;
				}
				break;
			}
		}
		return null;
	}
    
	 /**
     * Get The Price Prom Info.
     * @param sku The ID of The Sku
     * @param dpt  The ID of The Department
     * @param line The ID of The Line
     * @param clas The ID of Class
     * @return PricePromInfo The Price Prom Info Object
     */
    public final List<PricePromInfo> getPricePromList(
			final String sku, final String dpt, final String line, final String clas) {
    	List<PricePromInfo> retLst = new ArrayList<PricePromInfo>();
//    	List<PricePromInfo> rateLst = new ArrayList<PricePromInfo>(); 	// lilx 20191125
//    	List<PricePromInfo> amtLst = new ArrayList<PricePromInfo>(); 	// lilx 20191125
//    	List<PricePromInfo> priceLst = new ArrayList<PricePromInfo>(); 	// lilx 20191125
    	
		if (pricePromInfoList == null){
			return null;
		}
		for (PricePromInfo pricePromInfo : pricePromInfoList) {
			switch (pricePromInfo.getPromotionType()) {
			case PROMOTIONTYPE_CLASS:
				if (clas != null && line != null && dpt != null && clas.equals(pricePromInfo.getClas()) 
				&& line.equals(pricePromInfo.getLine()) && dpt.equals(pricePromInfo.getDpt())){
//					if (isPricePromValid(pricePromInfo) && (isDrug = isDrug(pricePromInfo, retLst))) {
//						selectPromInfo(rateLst, amtLst, priceLst, pricePromInfo);
//					}
					if (isPricePromValid(pricePromInfo)) {
						retLst.add(pricePromInfo);
					}
				}
				break;
			case PROMOTIONTYPE_ITEMCODE:
				String pricePromSku = pricePromInfo.getSku();
				String itemSku = sku;
				if (!StringUtility.isNullOrEmpty(pricePromSku)) {
					if (pricePromSku.equals(itemSku)) {
//						if (isPricePromValid(pricePromInfo) && (isDrug = isDrug(pricePromInfo, retLst))) {
//							selectPromInfo(rateLst, amtLst, priceLst, pricePromInfo);
//						}
						if (isPricePromValid(pricePromInfo)) {
							retLst.add(pricePromInfo);
						}
					}
				}
				break;
			case PROMOTIONTYPE_LINE:
				if (line != null && dpt != null && line.equals(pricePromInfo.getLine()) && dpt.equals(pricePromInfo.getDpt())){
//					if (isPricePromValid(pricePromInfo) && (isDrug = isDrug(pricePromInfo, retLst))) {
//						selectPromInfo(rateLst, amtLst, priceLst, pricePromInfo);
//					}
					if (isPricePromValid(pricePromInfo)) {
						retLst.add(pricePromInfo);
					}
				}
				break;
			case PROMOTIONTYPE_DPT:
				if (dpt != null && dpt.equals(pricePromInfo.getDpt())){
					if (isPricePromValid(pricePromInfo)) {
//						selectPromInfo(rateLst, amtLst, priceLst, pricePromInfo);
						retLst.add(pricePromInfo);
					}
					break;
				}
			}
		}
//		if(isDrug) {
//			if (rateLst != null && rateLst.size() > 0) retLst.add(rateLst.get(0));
//			if (amtLst != null && amtLst.size() > 0) retLst.add(amtLst.get(0));
//			if (priceLst != null && priceLst.size() > 0) retLst.add(priceLst.get(0));
//		}

		return retLst;
	}
    
    public final void selectPromInfo(List<PricePromInfo> rateLst,List<PricePromInfo> amtLst,
    		List<PricePromInfo> priceLst,PricePromInfo pricePromInfo) {
    	if (DISCOUNTCLASS_RATE.equals(pricePromInfo.getDiscountClass())) {
    		if (rateLst.size() == 0)
    			rateLst.add(pricePromInfo);
    		else if( pricePromInfo.getDiscountRate() > rateLst.get(0).getDiscountRate()) {
    			rateLst.set(0, pricePromInfo);
    		}
		} else if (DISCOUNTCLASS_AMT.equals(pricePromInfo.getDiscountClass())) {
			if (amtLst.size() == 0)
				amtLst.add(pricePromInfo);
    		else if( pricePromInfo.getDiscountAmt() > amtLst.get(0).getDiscountAmt()) {
    			amtLst.set(0, pricePromInfo);
    		}
		} else if (DISCOUNTCLASS_PRICE.equals(pricePromInfo.getDiscountClass())) {
			if (priceLst.size() == 0)
				priceLst.add(pricePromInfo);
    		else if( pricePromInfo.getSalesPrice() < priceLst.get(0).getSalesPrice()) {
    			priceLst.set(0, pricePromInfo);
    		}
		} 
    }
    
	 /**
     * Get The Price Urgent Info.     
     * @param dpt  The ID of The Department
     * @param line The ID of The Line
     * @param sku The ID of The clas
     * @return PriceUrgentInfo The Price Urgent Info Object
     */
    public final PriceUrgentInfo getPriceUrgentInfo(final String dpt, final String line, final String clas) {

		if (priceUrgentInfoList == null){
			return null;
		}
		for (PriceUrgentInfo priceUrgentInfo : priceUrgentInfoList) {
			if (clas != null && line != null && dpt != null && clas.equals(priceUrgentInfo.getClas()) 
					&& line.equals(priceUrgentInfo.getLine()) && dpt.equals(priceUrgentInfo.getDpt())){
				return priceUrgentInfo;
			}
		}
		return null;
	}
    
    public final boolean isPricePromValid(PricePromInfo pricePromInfo) {
    	boolean ret = false;
		// �^�C���Z�[���J�n�����ƃ^�C���Z�[���I�������̔��f
		if (!StringUtility.isNullOrEmpty(pricePromInfo.getSaleStartTime()) && !StringUtility.isNullOrEmpty(pricePromInfo.getSaleEndTime())) {
			DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
			String saleStartTime = df1.format(new Date()) + pricePromInfo.getSaleStartTime() + "00";
			String saleEndTime = df1.format(new Date()) + pricePromInfo.getSaleEndTime() + "59";
			
			SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");			
    		Date sysTime = new Date();
    		try {
				if (sysTime.before(df2.parse(saleEndTime)) && sysTime.after(df2.parse(saleStartTime))) {
					ret = true;
				}
			} catch (ParseException e) {
				tp.println(context.toString());
	            LOGGER.logAlert(progname, "isPricePromValid", Logger.RES_EXCEP_GENERAL,
	                    e.getMessage());
			}
		} else {
			ret = true;
		}
    	return ret;
    }
    
    /**
     * Get The Price MM Info.
     * @param sku The ID of The Sku
     * @return PriceMMInfo The Price MM Info Object
     */
    public final PriceMMInfo getPriceMMInfo(final String sku) {

		if (priceMMInfoList == null || StringUtility.isNullOrEmpty(sku)){
			return null;
		}

		List<PriceMMInfo> priceMMInfoListTemp = new ArrayList<PriceMMInfo>();
		for (PriceMMInfo priceMMInfo : priceMMInfoList) {
			String priceMMSku = priceMMInfo.getSku();
			if (!StringUtility.isNullOrEmpty(priceMMSku)) {
				if (priceMMSku.contains("*")) {
					if (sku.startsWith(priceMMSku.replace("*", ""))) {
						priceMMInfoListTemp.add(priceMMInfo);
					}
				} else {
					if (sku.equals(priceMMSku)) {
						priceMMInfoListTemp.add(priceMMInfo);
					}
				}
			}
		}

		if (!(priceMMInfoListTemp.size() > 0)){
			return null;
		}

		for(PriceMMInfo priceMMInfoTemp : priceMMInfoListTemp) {
			if ("1".equals(priceMMInfoTemp.getTargetStoreType())) {
				return priceMMInfoTemp;
			}
		}
		return priceMMInfoListTemp.get(0);
	}
    
    /**
     * Get The Price MM Info List.
     * @param sku The ID of The Sku
     * @return List<PriceMMInfo> The Price MM Info List
     */
    public final List<PriceMMInfo> getPriceMMList(final String sku) {

		if (priceMMInfoList == null || StringUtility.isNullOrEmpty(sku)){
			return null;
		}
		
		List<PriceMMInfo> retPriceMMInfoList = new ArrayList<PriceMMInfo>();
		for (PriceMMInfo priceMMInfo : priceMMInfoList) {
			String priceMMSku = priceMMInfo.getSku();
			if (!StringUtility.isNullOrEmpty(priceMMSku)) {
				if (sku.equals(priceMMSku) && isPriceMMValid(priceMMInfo)) {
					retPriceMMInfoList.add(priceMMInfo);
				}
			}
		}
		
		if (retPriceMMInfoList.isEmpty()) return null;
			
		return retPriceMMInfoList;
	}
    
    public final boolean isPriceMMValid(PriceMMInfo priceMMInfo) {
    	boolean ret = false;
		// �^�C���Z�[���J�n�����ƃ^�C���Z�[���I�������̔��f
		if (!StringUtility.isNullOrEmpty(priceMMInfo.getSaleStartTime()) && !StringUtility.isNullOrEmpty(priceMMInfo.getSaleEndTime())) {
			DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
			String saleStartTime = df1.format(new Date()) + priceMMInfo.getSaleStartTime() + "00";
			String saleEndTime = df1.format(new Date()) + priceMMInfo.getSaleEndTime() + "59";
			
			SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");			
    		Date sysTime = new Date();
    		try {
				if (sysTime.before(df2.parse(saleEndTime)) && sysTime.after(df2.parse(saleStartTime))) {
					ret = true;
				}
			} catch (Exception e) {
				tp.println(context.toString());
	            LOGGER.logAlert(progname, "isPriceMMValid", Logger.RES_EXCEP_GENERAL,
	                    e.getMessage());
			}
		} else {
			ret = true;
		}
    	return ret;
    }
}
