package ncr.res.mobilepos.queuebuster.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.journalization.model.poslog.PosLog;

/**
 * SuspendData Model Object.
 *
 * <P>The SuspendData is a model class for representing
 * the Queue buster response with relation to Disney Store specification.
 *
 * @author cc185102 - Added initial implementation of SuspendData object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SuspendData")
@ApiModel(value="SuspendData")
public class SuspendData {
    /**
     * The Initial Status of the Suspended Data.
     */
    public static final int INITIAL_STATUS = 0;
    /**
     * The Status when the Suspend Data was successfully forwarded.
     */
    public static final int FORWARD_COMPLETE = 1;
    /**
     * The Status when the Suspend Data was canceled.
     */
    public static final int CANCEL_FROM_POS = 2;
    /**
     * The Status when a Suspend Data was completely processed.
     */
    public static final int PROCESS_COMPLETE = 3;
    /**
     * The Normal End Status.
     */
    public static final int NORMAL_END = 0;
    /**
     * The Abnormal End Status.
     */
    public static final int ABNORMAL_END = -1;
    /**
     * The response status for the request.
     */
    @XmlElement(name = "status")
    private int status;
    /**
     * The suspend status of the POSLog being requested.
     */
    @XmlElement(name = "SuspendStatus")
    private String suspendStatus;
    /**
     * The possible error message when a request fail.
     */
    @XmlElement(name = "errormessage")
    private String errormessage;
    /**
     * The target PosLog transaction.
     */
    @XmlElement(name = "POSLog")
    private PosLog poslog;

    /**
     * The Getter method for Status.
     * @return The status
     */
    @ApiModelProperty(value="取引状態", notes="取引状態")
    public final int getStatus() {
        return status;
    }
    /**
     * The Setter method for Status.
     * @param statusToSet    The Status to set.
     */
    public final void setStatus(final int statusToSet) {
        this.status = statusToSet;
    }
    /**
     * The Getter method for the suspended Status.
     * @return  The Suspend status.
     */
    @ApiModelProperty(value="一時停止取引", notes="一時停止取引")
    public final String getSuspendStatus() {
        return suspendStatus;
    }
    /**
     * The Setter for Suspend Status.
     * @param suspendStatusToSet The new suspend status.
     */
    public final void setSuspendStatus(final String suspendStatusToSet) {
        this.suspendStatus = suspendStatusToSet;
    }
    /**
     * The getter of the POSLog transaction.
     * @return  The POSLog object.
     */
    @ApiModelProperty(value="業務のposlog", notes="業務のposlog")
    public final PosLog getPoslog() {
        return poslog;
    }
    /**
     * The setter of the POSLog transaction.
     * @param poslogToSet The new POSLog object.
     */
    public final void setPoslog(final PosLog poslogToSet) {
        this.poslog = poslogToSet;
    }
    /**
     * The getter of the error message.
     * @return  The Error Message.
     */
    @ApiModelProperty(value="APIエラー内容", notes="APIエラー内容")
    public final String getErrormessage() {
        return errormessage;
    }
    /**
     * The setter of the error message.
     * @param errormessageToSet The error message to set.
     */
    public final void setErrormessage(final String errormessageToSet) {
        this.errormessage = errormessageToSet;
    }
    /**
     * An override to toString() implementation.
     * @return the String representation of the object.
     */
    @Override
    public final String toString() {
        StringBuilder strBuilder = new StringBuilder();
        String dlmtr = "; ";
        strBuilder.append("Status: ").append(status).append(dlmtr)
                  .append("SuspendStatus: ").append(suspendStatus).append(dlmtr)
                  .append("ErrorMessage: ").append(errormessage);
        return strBuilder.toString();
    }
}
