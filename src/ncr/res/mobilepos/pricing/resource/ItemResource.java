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
import java.util.ArrayList;
import java.util.List;

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
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.IItemDAO;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.PickList;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.model.Transaction;
import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;

/**
 * ItemResource Web Resource Class
 * 
 * <P>
 * Supports MobilePOS Item Pricing/Search processes.
 * 
 */
@Path("/pricing")
@Api(value="/pricing", description="価格設定API")
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

    /**
     * Default Constructor. Instantiate ioWriter and sqlServerDAO member
     * variables.
     */
    public ItemResource() {
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        barcodeAssignment = BarcodeAssignmentFactory.getInstance();
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
    @ApiOperation(value="商品情報取得", response=SearchedProduct.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
            @ApiResponse(code=ResultBase.RES_ITEM_NOT_EXIST, message="プロジェクトは存在しない")
        })
    public final SearchedProduct getItemByPLUcode(
    		@ApiParam(name="storeid", value="店舗コード") @PathParam("storeid") final String storeID,
    		@ApiParam(name="plucode", value="商品の値段が上がる") @PathParam("plucode") final String pluCode,
    		@ApiParam(name="companyId", value="会社コード") @FormParam("companyId") final String companyId,
    		@ApiParam(name="bussinessDate", value="営業日") @FormParam("bussinessDate") final String bussinessDate) {

        String functionName = "ItemResource.getItemByPLUcode";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("storeid", storeID)
                .println("plucode", pluCode)
                .println("companyId", companyId);
        SearchedProduct searchedProduct = new SearchedProduct();

        // Check if pricingtype is normal(0).
        // If 1, only PLU code is returned without searching database (MST_PLU).
        // If 0, continue searching.
        if (!isNormalPricing()) {
            Item item = new Item();
            item.setItemId(pluCode);
            item.setAgeRestrictedFlag(0);
            searchedProduct.setItem(item);
            tp.println("Pricing is not normal.");
            tp.methodExit(searchedProduct);
            return searchedProduct;
        }

        Item returnItem = null;
        try {
            IItemDAO itemDAO = sqlServerDAO.getItemDAO();
            String priceIncludeTax = GlobalConstant.getPriceIncludeTaxKey();
            returnItem = itemDAO.getItemByPLU(storeID, pluCode,companyId,Integer.parseInt(priceIncludeTax),bussinessDate);
            
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
                returnItem.setItemId(pluCode);
                returnItem.setAgeRestrictedFlag(0);
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
    @ApiOperation(value="プロジェクトの価格表を獲得し", response=Item.class)
    public final List<Item> getItemsPrice(
    		@ApiParam(name="transaction", value="業務") @FormParam("transaction") String transaction,
    		@ApiParam(name="StoreId", value="店舗コード") @FormParam("StoreId") String storeId, 
    		@ApiParam(name="CompanyId", value="会社コード") @FormParam("CompanyId") String companyId ,
    		@ApiParam(name="businessDate", value="営業日") @FormParam("businessDate") String businessDate) {
        String functionName = DebugLogger.getCurrentMethodName();
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
            LOGGER.logSnapException(progname, Logger.RES_EXCEP_DAO, functionName + ": Failed to get Group Line　Info.",
                    daoEx);
            return null;
        } catch (Exception e) {
            LOGGER.logSnapException(progname, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get Group Line　Info.", e);
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
    @ApiOperation(value="ピックリスト商品取得", response=PickList.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final PickList getPickList(
    		@ApiParam(name="companyId", value="会社コード") @QueryParam("companyId") final String companyId,
    		@ApiParam(name="storeId", value="店舗コード") @QueryParam("storeId") final String storeId,
    		@ApiParam(name="itemType", value="アイテムタイプ") @QueryParam("itemType") final String itemType) {
        String functionName = DebugLogger.getCurrentMethodName();
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
    @ApiOperation(value="バーコード情報取得", response=BarcodeAssignment.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
	public final BarcodeAssignment getBarcodeInfo() {
		String functionName = DebugLogger.getCurrentMethodName();
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

}
