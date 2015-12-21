package ncr.res.pastelport.platform;

import atg.taglib.json.util.JSONException;

/**
 * to hold the XML document sent from the
 * NCR Payment Client
 *
 * @version $Revision: 1.3 $ $Date: 2008/05/28 02:49:48 $
 */
/**
 * @author zbliuc
 *
 */

public class CommonTx extends CommonBase {
    /** Member Variable to Duplicate flag. */
    private boolean duplicate;
    /** Member Variable of Other Sys If. */
    private boolean othersysif;
    /** Member variable for need manage flag. */
    private boolean needmanage;

    /**
     * To obtain a manage flag, that is need to manage the communication with
     * pastelport.
     * @return True, if it needs manage, else, False.
     */
    public final boolean isNeedmanage() {
        return needmanage;
    }

    /**
     * To set the manage flag, that is need to manage the process of
     * communication with pastalport.
     * @param isneedmanage The Manage Flag.
     */
    public final void setIsneedmanage(final boolean isneedmanage) {
        needmanage = isneedmanage;
    }

    /**
     * CommonTx to generate a new object.
     */
    public CommonTx() {
        super();
        setTopElement("topelement");
        duplicate = false;
        othersysif = false;
        needmanage = false;
    }

    /**
     * Set all the settings of Common TX to default.
     */
    @Override
    public final void clear() {
        super.clear();
        setTopElement("topelement");
        duplicate = false;
        othersysif = false;
    }

    /**
     * Returns the value of the brand tag of the document XML.
     * @return The Brand.
     * @throws JSONException    The exception thrown when metahod fail.
     */
    public final String getBrand() throws JSONException {
        return getFieldValue("brand");
    }

    /**
     * Returns the value of servicecode tags of a document XML.
     * @return The Field Value.
     * @throws JSONException The exception thrown when error occur.
     */
    public final String getSc() throws JSONException {
        return getFieldValue("sc");
    }

    /**
     * Returns the value of the service tag of the document XML.
     * @return The Service.
     * @throws JSONException The Exception thrown when error occur.
     */
    public final String getService() throws JSONException {
        return getFieldValue("service");
    }

    /**
     * Get Store ID.
     * @return  The Store ID.
     * @throws JSONException The exception thrown when error occur.
     */
    public final int getStoreid() throws JSONException {
        try {
            return Integer.parseInt(getFieldValue("storeid"));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * To return (int) value of txtype tags of a document XML.
     * @return The Transaction Type.
     * @throws JSONException The exception thrown when error occur.
     */
    public final int getTxType() throws JSONException {
        try {
            return Integer.parseInt(getFieldValue("txtype"));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * To obtain a duplicate Transaction flag.
     * @return True, if duplicate, else, false.
     */
    public final boolean getDuplicate() {
        return duplicate;
    }

    /**
     * Transaction flag to set the overlap.
     * @param dup The Duplicate flag.
     */
    public final void setDuplicate(final boolean dup) {
        duplicate = dup;
    }

    /**
     * Get the flags associated with other system integration.
     * @return Other System Information.
     */
    public final boolean getOtherSysIF() {
        return othersysif;
    }

    /**
     * Conjunction with other systems to set the flag.
     * @param osysif The Other System Flag.
     */
    public final void setOtherSysIF(final boolean osysif) {
        othersysif = osysif;
    }
    @Override
    public final String toString() {
        StringBuilder strBuilder = new StringBuilder();
        
        strBuilder.append("Duplicate : ").append(this.duplicate)
        .append("OtherSysIf : ").append(this.othersysif)
        .append("NeedManage : ").append(this.needmanage);

        return strBuilder.toString();
    }
}
