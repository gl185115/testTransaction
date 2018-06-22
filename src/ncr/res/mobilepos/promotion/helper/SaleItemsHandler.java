package ncr.res.mobilepos.promotion.helper;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2014.11.19      LIQIAN        商品情報取得
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.promotion.model.Sale;

/**
 * Handler for calculation of amounts in
 * {@link Sale} objects.
 * @author jg185106
 *
 */
public final class SaleItemsHandler {
    /**
     * Private SUBINT10_ONE.
     */
    private final static int SUBINT10_ONE = 1;
    /**
     * Private SUBINT10_TWO.
     */
    private final static int SUBINT10_TWO = 2;

    /**
     * Private default constructor.
     */
    private SaleItemsHandler() {
    }

    /**
     * The Comparator for Sale.
     */
    private static final Comparator<Sale> COMPARATOR =
        new Comparator<Sale>() {
            @Override
            public int compare(final Sale o1, final Sale o2) {
                return (int) (o2.getRegularSalesUnitPrice()
                        - o1.getRegularSalesUnitPrice());
        }
    };
    /**
     * Constant value for percent divisor.
     */
    public static final int PER_CENT = 100;
    /**
     * Calculates the discount amount
     * based on discount information.
     * @param sale the sale to calculate the discount amoutn from
     * @return double (the discount amount)
     */
    public static double calculateDiscountAmount(final Sale sale) {
        double discountAmount = 0;
        if (sale.getDiscountable()
                && sale.getDiscountFlag() == Sale.DISC_AMOUNT) {
            discountAmount = sale.getDiscountAmount();
            if (discountAmount > sale.getRegularSalesUnitPrice()
                    || discountAmount < 0) {
                discountAmount = 0;
            }
        } else if (sale.getDiscountable()
            && sale.getDiscountFlag() == Sale.DISC_RATE) {
            double discountRate = sale.getDiscountRate() / PER_CENT;
            discountAmount = Math.floor(discountRate
                    * sale.getRegularSalesUnitPrice());
        }
        return discountAmount;
    }
    /**
     * Calculates the actual sales price
     * based on discount information.
     * @param sale the sale to calculate the actual sales
     *              price from
     * @return double (the actual sales price)
     */
    public static double calculateActualSalesUnitPrice(final Sale sale) {
        double actualSalesUnitPrice = 0;
        double discountAmount = sale.getDiscountAmount();
        actualSalesUnitPrice = sale.getRegularSalesUnitPrice();
        // do not apply discount if discount is less than zero
        // or more than the total sales price.
        if (discountAmount <= actualSalesUnitPrice
                && discountAmount > 0) {
            actualSalesUnitPrice
                = actualSalesUnitPrice - discountAmount;
        }
        return actualSalesUnitPrice;
    }
    /**
     * Calculates the extended amount
     * based on discount information.
     * @param sale the sale to calculate the extended amount from
     * @return double (the extended amount)
     */
    public static double calculateExtendedAmount(final Sale sale) {
        return sale.getActualSalesUnitPrice() * sale.getQuantity();
    }

    /**
     * Creates a {@link Sale} object based on {@link Item}
     * information.
     * @param item the item to create the {@link Sale} from.
     * @param saleIn contains new Values for Sale.
     * @return {@link Sale}
     */
    public static Sale createSale(final Item item,
 final Sale saleIn) {
        Sale saleRes = new Sale();
        saleRes.setDepartment(item.getDepartment());
        saleRes.setDescription(item.getDescription());
        saleRes.setItemId(item.getItemId());
        saleRes.setDiscountable("0".equals(item.getDiscountType()));
        saleRes.setQuantity(saleIn.getQuantity());
        saleRes.setLine(item.getLine());
        saleRes.setItemClass(item.getItemClass());
        saleRes.setMustBuyFlag(item.getMustBuyFlag());
        saleRes.setTaxType(item.getTaxType());
        saleRes.setDiscountType(item.getDiscountType());
        saleRes.setTaxRate(item.getTaxRate());
        saleRes.setNonSales(item.getNonSales());
        saleRes.setSubCode1(item.getSubCode1());
        saleRes.setSubInt10(item.getSubInt10());
        saleRes.setMd02(item.getMd02());
        saleRes.setMd03(item.getMd03());
        saleRes.setMd04(item.getMd04());
        saleRes.setMd05(item.getMd05());
        saleRes.setMd06(item.getMd06());
        saleRes.setMd07(item.getMd07());
        saleRes.setMd08(item.getMd08());
        saleRes.setMd09(item.getMd09());
        saleRes.setMd10(item.getMd10());
        saleRes.setMd11(item.getMd11());
        saleRes.setMd12(item.getMd12());
        saleRes.setMd13(item.getMd13());
        saleRes.setMd14(item.getMd14());
        saleRes.setMd15(item.getMd15());
        saleRes.setMd16(item.getMd16());
        saleRes.setSku(item.getSku());
        saleRes.setMdType(item.getMdType());
        saleRes.setHostFlag(item.getHostFlag());
        saleRes.setPromotionId(item.getPromotionId());
        saleRes.setPromotionNo(item.getPromotionNo());
        saleRes.setPromotionType(item.getPromotionType());
        saleRes.setDptDiscountType(item.getDptDiscountType());
        saleRes.setMdNameLocal(item.getMdNameLocal());
        saleRes.setSalesNameSource(item.getSalesNameSource());
        saleRes.setMdName(item.getMdName());
        saleRes.setMdKanaName(item.getMdKanaName());
        saleRes.setSalesPrice2(item.getSalesPrice2());
        saleRes.setPaymentType(item.getPaymentType());
        saleRes.setSubNum2(item.getSubNum2());
        saleRes.setDptSubNum1(item.getDptSubNum1());
        saleRes.setDptSubNum2(item.getDptSubNum2());
        saleRes.setDptSubNum3(item.getDptSubNum3());
        saleRes.setDptSubNum4(item.getDptSubNum4());
        saleRes.setRuleQuantity1(item.getRuleQuantity1());
        saleRes.setRuleQuantity2(item.getRuleQuantity2());
        saleRes.setRuleQuantity3(item.getRuleQuantity3());
        saleRes.setConditionPrice1(item.getConditionPrice1());
        saleRes.setConditionPrice2(item.getConditionPrice2());
        saleRes.setConditionPrice3(item.getConditionPrice3());
        saleRes.setDecisionPrice1(item.getDecisionPrice1());
        saleRes.setDecisionPrice2(item.getDecisionPrice2());
        saleRes.setDecisionPrice3(item.getDecisionPrice3());
        saleRes.setAveragePrice1(item.getAveragePrice1());
        saleRes.setAveragePrice2(item.getAveragePrice2());
        saleRes.setAveragePrice3(item.getAveragePrice3());
        saleRes.setDiacountRate(item.getDiacountRate());
        saleRes.setDiscountClass(item.getDiscountClass());
        saleRes.setDiscountAmt(item.getDiscountAmt());
        saleRes.setCouponNo(item.getCouponNo());
        saleRes.setEvenetName(item.getEvenetName());
        saleRes.setReceiptName(item.getReceiptName());
        saleRes.setUnitPrice(item.getUnitPrice());
        saleRes.setIssueCount(item.getIssueCount());
        saleRes.setIssueType(item.getIssueType());
        saleRes.setPremiumItemNo(item.getPremiumItemNo());
        saleRes.setPremiumItemName(item.getPremiumItemName());
        saleRes.setTargetPrice(item.getTargetPrice());
        saleRes.setTargetCount(item.getTargetCount());
        saleRes.setOldPrice(item.getOldPrice());
        saleRes.setSalesPriceFrom(item.getSalesPriceFrom());
        saleRes.setMdVender(item.getMdVender());
        saleRes.setCostPrice1(item.getCostPrice1());
        saleRes.setMakerPrice(item.getMakerPrice());
        saleRes.setConn1(item.getConn1());
        saleRes.setConn2(item.getConn2());
        saleRes.setPublishingCode(item.getPublishingCode());
        saleRes.setPluPrice(item.getPluPrice());
        saleRes.setDptNameLocal(item.getDptNameLocal());
        saleRes.setClassNameLocal(item.getClassNameLocal());
        saleRes.setGroupName(item.getGroupName());
        saleRes.setNameText(item.getNameText());
        saleRes.setDptSubCode1(item.getDptSubCode1());
        saleRes.setGroupID(item.getGroupID());
        saleRes.setReplaceSupportdiscountAmt(item.getReplaceSupportdiscountAmt());
        saleRes.setQrCodeList(item.getQrCodeList());
        saleRes.setPremiumList(item.getPremiumList());
        saleRes.setColorkananame(item.getColorkananame());
        saleRes.setSizeKanaName(item.getSizeKanaName());
        saleRes.setBrandName(item.getBrandName());
        /* 1.01 2014.11.19 商品情報取得 ADD START */
        saleRes.setEmpPrice1(item.getEmpPrice1());
        saleRes.setMd01(item.getMd01());
        saleRes.setPSType(item.getPSType());
        saleRes.setOrgSalesPrice1(item.getOrgSalesPrice1());
        /* 1.01 2014.11.19 商品情報取得 ADD END */
        saleRes.setEventId(item.getEventId());
        saleRes.setEventName(item.getEventName());
        saleRes.setEventSalesPrice(item.getEventSalesPrice());
        double discount = 0;
//        if (saleRes.getDiscountable()) {
            discount = item.getDiscount();
            saleRes.setDiscountRate(discount);
            saleRes.setDiscountAmount(item.getDiscountAmount());
            saleRes.setDiscountFlag(item.getDiscountFlag());
            saleRes.setMixMatchCode(item.getMixMatchCode());
//        }
        saleRes.setRegularSalesUnitPrice(item.getRegularSalesUnitPrice());
        saleRes.setDiscountAmount(SaleItemsHandler.calculateDiscountAmount(saleRes));
        saleRes.setActualSalesUnitPrice(SaleItemsHandler.calculateActualSalesUnitPrice(saleRes));
        saleRes.setExtendedAmount(SaleItemsHandler.calculateExtendedAmount(saleRes));
        saleRes.setDiscount(discount);
        saleRes.setItemEntryId(saleIn.getItemEntryId());
        saleRes.setItemIdType(saleIn.getItemIdType());
        saleRes.setSubInt10(item.getSubInt10());
        if (Integer.valueOf(item.getSubInt10()) == SUBINT10_ONE
                || Integer.valueOf(item.getSubInt10()) == SUBINT10_TWO) {
            saleRes.setPriceOverride(
                    -1 < saleIn.getActualSalesUnitPrice() && (saleIn.getInputType() != 0) && saleRes.getDiscountable()
                            && saleIn.getActualSalesUnitPrice() != saleRes.getActualSalesUnitPrice());
        } else {
            saleRes.setPriceOverride(0 < saleIn.getActualSalesUnitPrice() && saleRes.getDiscountable()
                    && saleIn.getActualSalesUnitPrice() != saleRes.getActualSalesUnitPrice());
        }
        saleRes.setAgeRestrictedFlag(item.getAgeRestrictedFlag());
        saleRes.setCouponFlag(item.getCouponFlag());
        saleRes.setTaxTypeSource(item.getTaxTypeSource());
        saleRes.setDiscountTypeSource(item.getDiscountTypeSource());
        saleRes.setLabelPrice(item.getLabelPrice());
        saleRes.setBrandSaleName(item.getBrandSaleName());
        saleRes.setSaleSizeCode(item.getSaleSizeCode());
        saleRes.setSizePatternId(item.getSizePatternId());
        saleRes.setPointAddFlag(item.getPointAddFlag());
        saleRes.setPointUseFlag(item.getPointUseFlag());
        saleRes.setTaxExemptFlag(item.getTaxExemptFlag());
        saleRes.setClsDiscountType(item.getClsDiscountType());
        return saleRes;
    }

    /**
     * Sort the Sales in higher Order.
     * @param sales   The List of sales to be sorted.
     * @return The The sorted List of {@link Sale} in Descending order.
     */
    public static List<Sale> sortSalesDescending(
            final List<Sale> sales) {
        if (sales == null) {
            return new ArrayList<Sale>();
        }
        Collections.sort(sales, COMPARATOR);
        return sales;
    }

    /**
     * Make  a Copy of List of Sales.
     * @param actualSales The List of sales to be copy
     * @return The Copy of List of Sales
     */
    public static List<Sale> copySales(final List<Sale> actualSales) {
        if (actualSales == null) {
            return new ArrayList<Sale>();
        }

        if (actualSales.isEmpty()) {
            return new ArrayList<Sale>();
        }

        List<Sale> copySales = new ArrayList<Sale>();

        //Perform a shallow Copy first.
        for (Sale sale : actualSales) {
            copySales.add((Sale) sale.clone());
        }

        return copySales;
    }
    /**
     * Reverse the List of Sales.
     * @param actualSales The List of Sales to be reverted.
     * @return The Reverted List of Sales.
     */
    public static List<Sale> reverseSales(
            final List<Sale> actualSales) {
        if (actualSales == null) {
            return new ArrayList<Sale>();
        }
        Collections.reverse(actualSales);
        return actualSales;
    }

    /**
     * Return the sales having Must Buy flag.
     * @param actualSales   The List of sales.
     * @return The List of Sales with Mustbuy flag.
     */
    public static List<Sale> extractAllSalesWithMustBuy(
            final List<Sale> actualSales) {
        if (actualSales == null) {
         return new ArrayList<Sale>();
        }

        List<Sale>  salesWithMustBuy = new ArrayList<Sale>();

        for (Sale sale : actualSales) {
            if (sale.getMustBuyFlag() == Sale.MUST_BUY) {
                salesWithMustBuy.add(sale);
            }
        }
        return salesWithMustBuy;
    }

    /**
     * Iterates the Sales into Linear List. Items with quantity n is iterated
     * n times in the List.
     * @param actualSales The List of Sales.
     * @return   The Iterated List of Sales.
     */
    public static List<Sale> iterateSales(final List<Sale> actualSales) {
        List<Sale> iteratedSale = new ArrayList<Sale>();

        int origQty = 0;
        for (Sale sale : actualSales) {
            origQty = sale.getQuantity();

            sale.setQuantity(1);
            iteratedSale.add(sale);

            if (origQty > 1) {
                for (int i = 0; i < origQty - 1; i++) {
                    //iterate all sales into each copy.
                    iteratedSale.add((Sale) sale.clone());
                }
            }
        }
        return iteratedSale;
    }

    /**
     * Get the Sum of Sales Quantities.
     * @param actualSales   The Actual Sales in the List.
     * @return  The Sum of Sale's quantities
     */
    public static int getSumSalesQuantity(final List<Sale> actualSales)  {
        if (null == actualSales || actualSales.isEmpty()) {
            return 0;
        }
        int sumSalesQty = 0;
        for (Sale sale : actualSales) {
            sumSalesQty += sale.getQuantity();
        }
        return sumSalesQty;
    }

    /**
     * Get the sum of Regular sales unit price.
     * @param actualSales
     *      The actual Sales to compute
     * @param toComputeByQuantity
     *      Tells if Sales Quantity is a factor for computation
     * @return The Sum of Sales Regular Price.
     */
    public static long getSumSalesRegularPrice(final List<Sale> actualSales,
            final boolean toComputeByQuantity) {
        long sumRegularSalesPrice = 0;
        for (Sale sale : actualSales) {
           long regularprice = (long) sale.getRegularSalesUnitPrice();
           if (toComputeByQuantity) {
               regularprice = regularprice * sale.getQuantity();
           }
           sumRegularSalesPrice += regularprice;
        }
        return sumRegularSalesPrice;
    }

    /**
     * Tell the list of Sales contains Mustbuy Item.
     * @param actualSales The Actual List of Sales
     * @return true if Contains must buy Item, else false.
     */
    public static boolean hasMustBuyItem(final List<Sale> actualSales) {
        for (Sale sale : actualSales) {
            if (sale.getMustBuyFlag() == Sale.MUST_BUY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the sale by Item Entry ID from the list.
     * @param actualSales   The actual List of Sales
     * @param entryID   The Item Entry ID of the target sale
     * @return  The target sale.
     */
    public static Sale getSale(final List<Sale> actualSales,
            final String entryID) {
        for (Sale sale : actualSales) {
            if (sale.getItemEntryId().equals(entryID)) {
                return sale;
            }
        }
        return new Sale();
    }

    /**
     * Validate the Sale model if correct.
     * @param sale The target Sale to be validated
     * @return The ResultBase.
     */
    public static ResultBase validateSale(final Sale sale) {
        ResultBase rs = new ResultBase();
        if (sale == null) {
            rs.setMessage("Transaction has no sale data!");
            rs.setNCRWSSResultCode(
                    ResultBase.RES_ERROR_INVALIDPARAMETER);
            return rs;
        }
        if (sale.getQuantity() <= 0) {
            rs.setMessage("Quantity is invalid!");
            rs.setNCRWSSResultCode(
                    ResultBase.RES_ERROR_INVALIDPARAMETER);
            return rs;
        }
        if (sale.getItemEntryId() == null
                || sale.getItemEntryId().isEmpty()) {
            rs.setMessage("ItemEntryId is invalid!");
            rs.setNCRWSSResultCode(
                    ResultBase.RES_ERROR_INVALIDPARAMETER);
            return rs;
        }
        return rs;
    }

    /**
     * Return the sales having price not override.
     * @param actualSales   The List of sales.
     * @return The List of Sales with price not override.
     */
    public static List<Sale> extractAllSalesWithOutPriceOverride(
            final List<Sale> actualSales) {
        if (actualSales == null) {
         return new ArrayList<Sale>();
        }

        List<Sale>  salesWithOutPriceOverride = new ArrayList<Sale>();

        for (Sale sale : actualSales) {
            if (!sale.isPriceOverride()) {
                salesWithOutPriceOverride.add(sale);
            }
        }
        return salesWithOutPriceOverride;
    }
}
