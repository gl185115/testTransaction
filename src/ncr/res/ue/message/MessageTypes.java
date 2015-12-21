package ncr.res.ue.message;
/**
 * UE Message types.
 * @author jg185106
 */
public final class MessageTypes {
    /**
     * Default constructor.
     */
    private MessageTypes() {

    }
    /**
     * ConnectionInitialization message.
     */
    public static final int CONNECTION_INITIALIZE = 91;
    /**
     * BeginTransaction message.
     */
    public static final int BEGIN_TRANSACTION = 1;
    /**
     * MemberId message.
     */
    public static final int MEMBER_ID = 2;
    /**
     * ItemEntry message.
     */
    public static final int ITEM_ENTRY = 3;
    /**
     * Adjustment message.
     */
    public static final int ADJUSTMENT = 4;
    /**
     * Total message.
     */
    public static final int TOTAL = 5;
    /**
     * Tender Entry message.
     */
    public static final int TENDER_ENTRY = 6;
    /**
     * End Transaction message.
     */
    public static final int END_TRANSACTION = 7;
    /**
     * Suspend Transaction message.
     */
    public static final int SUSPEND_TRANSACTION = 8;
    /**
     * Resume Transaction message.
     */
    public static final int RESUME = 9;
    /**
     * Item Quantity message.
     */
    public static final int ITEM_QUANTITY = 10;
    /**
     * Trigger Code.
     */
    public static final int TRIGGER_CODE = 11;
    /**
     * Item Points message.
     */
    public static final int ITEM_POINTS = 12;
    /**
     * Item SV message.
     */
    public static final int ITEM_SV = 13;
    /**
     * GS1_COUPON message.
     */
    public static final int GS1_COUPON = 14;
    /* ------------------ INCOMING MESSAGES ---------------------- */
    /**
     * constant Message Id for Begin Transaction Response.
     */
    public static final int IM_BEGIN_TRANSACTION_RESPONSE = 31;
    /**
     * constant Message Id for Member Id Response.
     */
    public static final int IM_MEMBER_ID_RESPONSE = 32;
    /**
     * constant Message Id for Item Entry Response.
     */
    public static final int IM_ITEM_ENTRY_RESPONSE = 33;
    /**
     * constant Message Id for Adjustment Response.
     */
    public static final int IM_ADJUSTMENT_RESPONSE = 34;
    /**
     * constant Message Id for Total Response.
     */
    public static final int IM_TOTAL_RESPONSE = 35;
    /**
     * constant Message Id for Tender Entry Response.
     */
    public static final int IM_TENDER_ENTRY_RESPONSE = 36;
    /**
     * constant Message Id for End Transaction Response.
     */
    public static final int IM_END_TRANSACTION_RESPONSE = 37;
    /**
     * constant Message Id for Suspend Transaction Response.
     */
    public static final int IM_SUSPEND_TRANSACTION_RESPONSE = 38;
    /**
     * constant Message Id for Resume Transaction Response.
     */
    public static final int IM_RESUME_TRANSACTION_RESPONSE = 39;
    /**
     * constant Message Id for Item Quantity Response.
     */
    public static final int IM_ITEM_QUANTITY_RESPONSE = 40;
    /**
     * constant Message Id for Trigger Code Response.
     */
    public static final int IM_TRIGGER_CODE_RESPONSE = 41;
    /**
     * constant Message Id for Item Points Response.
     */
    public static final int IM_ITEM_POINTS_RESPONSE = 42;
    /**
     * constant Message Id for GS1 Coupon Response.
     */
    public static final int IM_GS1_COUPON_RESPONSE = 44;
    /**
     * constant Message Id for Connection Initialize Response.
     */
    public static final int IM_CONNECTION_INITIALIZE_RESPONSE = 92;
    /**
     * constant Message Id for Status Response.
     */
    public static final int IM_STATUS_RESPONSE = 94;

    //Non-Terminating messages
    /**
     * constant Message Id for Discount Reward.
     */
    public static final int IM_DISCOUNT_REWARD = 61;
    /**
     * constant Message Id for Receipt Message Reward.
     */
    public static final int IM_RCPTMSG_REWARD = 63;
    /**
     * Constant Message ID for Cashier Notification Reward.
     */
    public static final int IM_CASHIER_NOTIFICATION_REWARD = 65;
    /**
     * Constant Message ID for Cashier Notification Reward.
     */
    public static final int IM_CASHIER_NOTIFICATION_IMMEDIATE_REWARD = 66;
    /**
     * constant Message Id for Points Reward.
     */
    public static final int IM_POINTS_REWARD = 70;
    /**
     * Constant Message ID for Stored Value Reward.
     */
    public static final int IM_STORED_VALUE_REWARD = 71;
}
