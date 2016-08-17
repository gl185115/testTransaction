package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The General Promotion Item.
 * @author cc185102
 *
 */
public class GeneralPromotionItem extends MixMatchItem {
    /** The default constructor. */
    public GeneralPromotionItem() {
        setCode(MixMatchItem.GENERAL_PROMOTION);
    }

    @Override
    protected final void resetComputation() {
        //do nothing.
    }
    @Override
    protected final List<Discount> mixMatchComputation(
            final List<Sale> itemList) {
        return new ArrayList<Discount>();
    }
    @Override
    protected final List<Sale> mustBuyMixMatchEstablishMent() {
        // TODO Auto-generated method stub
        return new ArrayList<Sale>();
    }
}
