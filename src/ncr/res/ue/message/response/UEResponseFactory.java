package ncr.res.ue.message.response;

import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates the proper response object.
 *
 */
public final class UEResponseFactory {
    /**
     * Private default constructor
     * as this is a utility class.
     */
    private UEResponseFactory() {
    }
    /**
     * class instance of the trace printer.
     */
    private static Trace.Printer tp = null;
    /**
     * generates the proper response class.
     * @param responseStrings - List of Strings - response from UE
     * @return ResponseBase - the base form of the response class
     * @throws MessageException - thrown when parsing error occurs
     * for easy handling
     */
    public static List<UEResponseBase> generateResponse(
            final List<String> responseStrings) throws MessageException {

        if (tp == null) {
            tp = DebugLogger.getDbgPrinter(
                    Thread.currentThread().getId(), UEResponseFactory.class);
        }

        List<UEResponseBase> responses = new ArrayList<UEResponseBase>();
        for (String reply : responseStrings) {

            int messageType = Integer.parseInt(reply.substring(
                    TransportHeader.TRANSPORT_HEADER_LENGTH,
                        TransportHeader.TRANSPORT_HEADER_LENGTH
                        + TransportHeader.DATA_MESSAGE_HEADER_LENGTH));
            try {
                switch(messageType) {
                //terminating messages
                case MessageTypes.IM_CONNECTION_INITIALIZE_RESPONSE :
                    responses.add(new ConnectionInitializationResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_BEGIN_TRANSACTION_RESPONSE :
                    responses.add(new BeginTransactionResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_MEMBER_ID_RESPONSE :
                    responses.add(new MemberIdResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_ITEM_ENTRY_RESPONSE :
                    responses.add(new ItemEntryResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_ITEM_QUANTITY_RESPONSE :
                    responses.add(new ItemQuantityResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_ADJUSTMENT_RESPONSE :
                    responses.add(new AdjustmentResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_TOTAL_RESPONSE :
                    responses.add(new TotalResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_TENDER_ENTRY_RESPONSE:
                    responses.add(new TenderEntryResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_RESUME_TRANSACTION_RESPONSE:
                    responses.add(new ResumeResponse(messageType, reply));
                    return responses;
                case MessageTypes.IM_END_TRANSACTION_RESPONSE:
                    responses.add(new EndTransactionResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_ITEM_POINTS_RESPONSE:
                    responses.add(new ItemPointsResponse(
                            messageType, reply));
                    return responses;
                case MessageTypes.IM_TRIGGER_CODE_RESPONSE:
                    responses.add(new TriggerCodeResponse(
                            messageType, reply));
                    return responses;
                //non-terminating
                case MessageTypes.IM_DISCOUNT_REWARD:
                    responses.add(new DiscountResponse(
                            messageType, reply));
                    break;
                case MessageTypes.IM_SUSPEND_TRANSACTION_RESPONSE:
                    responses.add(new SuspendTransactionResponse(
                            messageType, reply));
                    break;
                case MessageTypes.IM_CASHIER_NOTIFICATION_REWARD:
                case MessageTypes.IM_CASHIER_NOTIFICATION_IMMEDIATE_REWARD:
                case MessageTypes.IM_STORED_VALUE_REWARD:
                    responses.add(new RewardsResponse(
                            messageType, reply));
                    break;
                case MessageTypes.IM_POINTS_REWARD:
                    responses.add(new PointsResponse(messageType, reply));
                    break;
                default :
                    break;
                }
            } catch (MessageException e) {
                tp.write("");
            }
        }
        return responses;
    }
}
