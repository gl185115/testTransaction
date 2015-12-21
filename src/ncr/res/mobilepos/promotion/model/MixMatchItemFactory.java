package ncr.res.mobilepos.promotion.model;
/**
 * The MixMatchItem Factory.
 * @author cc185102
 *
 */
public final class MixMatchItemFactory {
    /**
     * Unknown type of MixMatch.
     */
    public static final int UNKNOWN = -1;
    /**The Default Constructor. */
    private MixMatchItemFactory() {    	
    }
    
    /**
     * Get the Mix Match Item.
     * @param mixmatchType  The type of mix match.
     * @param code 			The Mix and Match item Code.
     * @return  The {@link MixMatchItem}
     */
    public static MixMatchItem createMixMatchItem(
            final int mixmatchType,
            final String code) {
        MixMatchItem mixMatchItem;
        switch (mixmatchType) {
            case MixMatchItem.FIRST_STEP_REPETITION_PRICE:
            case MixMatchItem.FIRST_STEP_REPETITION_RATE :
            case MixMatchItem.FIRST_STEP_REPETITION_AMOUNT:
                mixMatchItem =
                    new FirstStepRepetitionMixMatchItem(code);
                break;
            case MixMatchItem.SECOND_STEP_REPETITION_PRICE:
            case MixMatchItem.SECOND_STEP_REPETITION_RATE:
            case MixMatchItem.SECOND_STEP_REPETITION_AMOUNT:
                mixMatchItem =
                    new SecondStepRepetitionMixMatchItem(code);
                break;
            case MixMatchItem.FIRST_STEP_UNIFORMITY_PRICE:
            case MixMatchItem.FIRST_STEP_UNIFORMITY_AMOUNT:
                mixMatchItem =
                    new FirstStepUniformityMixMatchItem(code);
                break;
            case MixMatchItem.FIRST_STEP_UNIFORMITY_RATE:
                mixMatchItem =
                    new FirstStepUniformityMixMatchItemByRate(code);
                break;
            case MixMatchItem.SECOND_STEP_UNIFORMITY_RATE:
            	mixMatchItem =
                    new SecondStepUniformityMixMatchItemDiscountRate(code);
                break;
            case MixMatchItem.SECOND_STEP_UNIFORMITY_PRICE:
            case MixMatchItem.SECOND_STEP_UNIFORMITY_AMOUNT:
                mixMatchItem =
                    new SecondStepUniformityMixMatchItem(code);
                break;
            case MixMatchItem.GROUP_REPETITION_PRICE:
            case MixMatchItem.GROUP_REPETITION_RATE:
            case MixMatchItem.GROUP_REPETITION_AMOUNT:
            case MixMatchItem.GROUP_UNIFORMITY_PRICE:
            case MixMatchItem.GROUP_UNIFORMITY_RATE:
            case MixMatchItem.GROUP_UNIFORMITY_AMOUNT:
            	mixMatchItem =
                    new GroupMixMatchItem(code);
                break;
            default:
                    mixMatchItem = new GeneralPromotionItem();
        }
        return mixMatchItem;
    }
    
    /**
     * Get the Mix Match Item.
     * @param mixmatchType  The type of mix match.
     * @param code 			The Mix and Match item Code.
     * @return  The {@link MixMatchItem}
     */
    public static GroupMixMatchSubItem createSubMixMatchItem(
    		 final int mixmatchType,
             final String code) {
    	GroupMixMatchSubItem mixMatchItem;
        switch (mixmatchType) {
            case MixMatchItem.GROUP_REPETITION_PRICE:
            case MixMatchItem.GROUP_REPETITION_RATE:
            case MixMatchItem.GROUP_REPETITION_AMOUNT:
            	mixMatchItem =
                    new GroupMixMatchRepetitionItem(code);
            	break;
            case MixMatchItem.GROUP_UNIFORMITY_PRICE:
            case MixMatchItem.GROUP_UNIFORMITY_RATE:
            case MixMatchItem.GROUP_UNIFORMITY_AMOUNT:
            	mixMatchItem =
                    new GroupMixMatchUniformityItem(code);
                break;
            default:
                    mixMatchItem = null;
        }
        return mixMatchItem;
    }
}
