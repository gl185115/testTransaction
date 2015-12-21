package ncr.res.pastelport.platform;

/**
 * The Interface for Pastel Port Head Base.
 *
 */
public interface PastelPortHeadBase {
    /**
     * @return the length of the message
     */
    String getLegth();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory2();

    /**
     * @return Item identification
     */
    String getItemdiscrimination1();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory4();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory5();

    /**
     * @return Scrutiny flag counter
     */
    String getCountcheckflag();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory7();

    /**
     * @return the INQ telegram
     */
    String getInqmessage();

    /**
     * @return Flag originated
     */
    String getSourceflag();

    /**
     * @return Application error flag
     */
    String getApplicationerrorflag();

    /**
     * @return Final result flag
     */
    String getResultflag();

    /**
     * @return Preliminary fixed part
     */
    String getReservefield();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory13();

    /**
     * @return Automatic revocation flag (notice of revocation)
     */
    String getCancelnoticeflag();

    /**
     * @return the shop code
     */
    String getStoreid();

    /**
     * @return the POSNoÅD
     */
    String getPosno();

    /**
     * @return the Transaction sequence number
     */
    String getTxid();

    /**
     * @return posoperatetime
     */
    String getPosoperatetime();

    /**
     * @return Systemreservationterritory14
     */
    String getSystemreservationterritory14();

    /**
     * @return End determination
     */
    String getFinishflag();

    /**
     * @param legth the message's length
     */
    void setLegth(String legth);

    /**
     * @param systemReservationTerritory2
     *            the length of the message
     */
    void setSystemreservationterritory2(
            String systemReservationTerritory2);

    /**
     * @param itemDiscrimination1
     *             The Item's Discrimination 1.
     */
    void setItemdiscrimination1(String itemDiscrimination1);

    /**
     * @param systemReservationTerritory4
     *          The System Reservation 4.
     */
    void setSystemreservationterritory4(
            String systemReservationTerritory4);

    /**
     * @param systemReservationTerritory5
     *          The System reservation 5.
     */
    void setSystemreservationterritory5(
            String systemReservationTerritory5);

    /**
     * @param countCheckFlag The count check flag.
     */
    void setCountcheckflag(String countCheckFlag);

    /**
     * @param systemReservationTerritory7
     *              The System reservation 7.
     */
    void setSystemreservationterritory7(
            String systemReservationTerritory7);

    /**
     * @param inqMessage The Inquiry Message.
     */
    void setInqmessage(String inqMessage);

    /**
     * @param sourceFlag The source flag.
     */
    void setSourceflag(String sourceFlag);

    /**
     * @param applicationErrorFlag The application error Flag.
     */
    void setApplicationerrorflag(String applicationErrorFlag);

    /**
     * @param resultFlag The Result flag.
     */
    void setResultflag(String resultFlag);

    /**
     * @param reserveField The Reserver Field.
     */
    void setReservefield(String reserveField);

    /**
     * @param systemReservationTerritory13
     *          The System reservation 13.
     */
    void setSystemreservationterritory13(
            String systemReservationTerritory13);

    /**
     * @param cancelNoticeFlag
     *          The Cancel Notice Flag.
     */
    void setCancelnoticeflag(String cancelNoticeFlag);

    /**
     * @param storeid   The Store ID.
     */
    void setStoreid(String storeid);

    /**
     * @param posNo The POS Number.
     */
    void setPosno(String posNo);

    /**
     * @param txid The Transaction Number.
     */
    void setTxid(String txid);

    /**
     * @param posOperateTime The POS Operation time.
     */
    void setPosoperatetime(String posOperateTime);

    /**
     * @param systemReservationTerritory14
     *          The System reservation Territory 14.
     */
    void setSystemreservationterritory14(
            String systemReservationTerritory14);

    /**
     * @param finishFlag The Finish flag.
     */
    void setFinishflag(String finishFlag);

}
