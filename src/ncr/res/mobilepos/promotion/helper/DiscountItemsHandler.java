package ncr.res.mobilepos.promotion.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ncr.res.mobilepos.promotion.model.Discount;

/**
 * Handler for {@link Discount}.
 * @author jg185106
 *
 */
public final class DiscountItemsHandler {
    /**
     * Private constructor.
     */
    private DiscountItemsHandler() {
        // TODO Auto-generated constructor stub
    }
    /**
     * Get the group Discount.
     * @param discounts The discounts.
     * @return  List of Discount
     */
    public static List<Discount> groupDiscounts(
            final List<Discount> discounts) {
        if (discounts == null) {
            return new ArrayList<Discount>();
        }
        List<Discount> ungroupdedDiscounts =
            discounts;
        List<Discount> groupedDiscounts =
            new ArrayList<Discount>();
        Iterator<Discount> itr = ungroupdedDiscounts.iterator();
        while (itr.hasNext()) {
          Discount discount = itr.next();
          discount.setQuantity(1);
          ungroupdedDiscounts.remove(discount);
          itr = ungroupdedDiscounts.iterator();
          while (itr.hasNext()) {
              Discount disc = itr.next();
              if (disc.getItemEntryID().equals(
                      discount.getItemEntryID())
                      && (disc.getRewardID()
                      == discount.getRewardID())) {
                  discount.setUnitDiscountAmount(
                          disc.getUnitDiscountAmount()
                          + discount.getUnitDiscountAmount());
                  discount.setQuantity(discount.getQuantity() + 1);
                  ungroupdedDiscounts.remove(disc);
                  itr = ungroupdedDiscounts.iterator();
              }
          }
          groupedDiscounts.add(discount);
          itr = ungroupdedDiscounts.iterator();
        }
        return groupedDiscounts;
    }

    /**
     * Get the Sum of discounts.
     * @param discounts The List of Discounts
     * @return The Sum of discounts.
     */
    public static long getSumUnitDiscountAmt(final List<Discount> discounts) {
        if (null == discounts) {
            return 0;
        }
        long totalDiscounts = 0;
        for (Discount discount : discounts) {
            totalDiscounts += discount.getUnitDiscountAmount();
        }
        return totalDiscounts;
    }
}
