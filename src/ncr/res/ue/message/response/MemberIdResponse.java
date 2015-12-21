package ncr.res.ue.message.response;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;

/**
 * class that holds the response for Member Id Message.
 *
 */
@XmlRootElement(name = "MemberIdResponse")
public class MemberIdResponse extends UEResponseBase {

    /**
     * the expected message length of the response.
     */
    private static final int EXPECTED_MESSAGE_LENGTH = 107; //20(TH) + 2(M)

    /**
     * the length of the responseFlag.
     */
    private static final int RESPONSE_FLAG_LENGTH = 2;
    /**
     * the length of the member id.
     */
    private static final int MEMBER_ID_LENGTH = 20;
    /**
     * the customer identifier.
     */
    @XmlElement(name = "MemberID")
    private String memberId = "";
    /**
     * gets the member id.
     * @return String
     */
    public final String getMemberId() {
        return memberId;
    }
    /**
     * the length of the secondary id.
     */
    private static final int SECONDARY_ID_LENGTH = 20;
    /**
     * the identifier of the secondary id type.
     */
    @XmlElement(name = "SecondaryID")
    private String secondaryId = "";
    /**
     * gets the secondary id.
     * @return String
     */
    public final String getSecondaryId() {
        return secondaryId;
    }
    /**
     * the length of the type of the secondary id.
     */
    private static final int SECONDARY_ID_TYPE_LENGTH = 2;
    /**
     * the secondary id type.
     */
    @XmlElement(name = "SecondaryIDType")
    private String secondaryIdType = "";
    /**
     * gets the secondary id type.
     * @return String
     */
    public final String getSecondaryIdType() {
        return secondaryIdType;
    }
    /**
     * the length of the first name.
     */
    private static final int FIRST_NAME_LENGTH = 15;
    /**
     * the first name of the customer.
     */
    @XmlElement(name = "FirstName")
    private String firstName = "";
    /**
     * gets the first name.
     * @return String
     */
    public final String getFirstName() {
        return firstName;
    }
    /**
     * the length of the last name.
     */
    private static final int LAST_NAME_LENGTH = 25;
    /**
     * the last name of the customer.
     */
    @XmlElement(name = "LastName")
    private String lastName = "";
    /**
     * gets the last name.
     * @return String
     */
    public final String getLastName() {
        return lastName;
    }
    /**
     * the length of the member flag.
     */
    private static final int MEMBER_FLAG_LENGTH = 3;
    /**
     * the member flag.
     */
    @XmlElement(name = "MemberFlag")
    private String memberFlag = "";
    /**
     * gets the member flag.
     * @return String
     */
    public final String getMemberFlag() {
        return memberFlag;
    }
    /**
     * The custom constructor.
     * @param responseFlag The Response flag.
     */
    public MemberIdResponse(final int responseFlag) {
        setResponseFlag(responseFlag);
    }

    @Override
    protected final void parseResponse(final String responseString)
            throws MessageException {

        setResponseFlag(checkResponse(
                responseString, EXPECTED_MESSAGE_LENGTH));

        int startIndex = UEResponseBase.IX_MESSAGE_START + RESPONSE_FLAG_LENGTH;
        int endIndex = startIndex + MEMBER_ID_LENGTH;
        memberId = responseString.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + SECONDARY_ID_LENGTH;
        secondaryId = responseString.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + SECONDARY_ID_TYPE_LENGTH;
        secondaryIdType = responseString.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + FIRST_NAME_LENGTH;
        firstName = responseString.substring(startIndex, endIndex).trim();

        startIndex = endIndex;
        endIndex = startIndex + LAST_NAME_LENGTH;
        lastName = responseString.substring(startIndex, endIndex).trim();

        startIndex = endIndex;
        endIndex = startIndex + MEMBER_FLAG_LENGTH;
        memberFlag = responseString.substring(startIndex, endIndex);
    }

    /**
     * constructor.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public MemberIdResponse(final int msgType,
            final String responseString) throws MessageException {
        super(msgType);

        try {
            parseResponse(responseString);
        } catch (IndexOutOfBoundsException ie) {
            throw new MessageException("parse fail - index out of bounds: "
                    + ie.getMessage());
        } catch (Exception e) {
            throw new MessageException("parse fail - exception: "
                    + e.getMessage());
        }
    }


}
