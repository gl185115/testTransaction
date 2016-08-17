/**
 * 
 */
package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Developer
 *
 */
public class GroupMixMatchRepetitionItem extends GroupMixMatchSubItem {

    /**
     * @param codeToSet
     */
    public GroupMixMatchRepetitionItem(final String codeToSet) {
        super(codeToSet);
    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.promotion.model.MixMatchItem#resetComputation()
     */
    @Override
    protected void resetComputation() {
        // Not used in Group Mix Match Sub item.
        // Computation is done in the containing Group Mix Match Main item.

    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.promotion.model.MixMatchItem#mixMatchComputation
     * (java.util.List)
     */
    @Override
    protected List<Discount> mixMatchComputation(
            final List<Sale> itemListToSet) {
        // Not used in Group Mix Match Sub item.
        // Computation is done in the containing Group Mix Match Main item.
        return new ArrayList<Discount>();
    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.promotion.model
     * .MixMatchItem#mustBuyMixMatchEstablishMent()
     */
    @Override
    protected List<Sale> mustBuyMixMatchEstablishMent() {
        // Not used in Group Mix Match Sub item.
        // Computation is done in the containing Group Mix Match Main item.
        return new ArrayList<Sale>();
    }

}
