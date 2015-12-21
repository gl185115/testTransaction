/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * PosLogResp
 *
 * Model Class PosLogResp
 *
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * PosLogResp Model Object.
 *
 * <P>Provides information about the web service processing status of the
 * response for Journal transaction.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "POSLogResp")
public class PosLogResp extends ResultBase {
	/**
	 * Default constructor.
	 */
	public PosLogResp(){
		super();
	}
	/**
	 * Constructor. 
	 * @param resultCode	The error response code.
	 * @param extendedResultCode	The extended error response code.
	 * @param status	The status of response.
	 * @param throwable	The exception.
	 */
	public PosLogResp(final int resultCode, final int extendedResultCode,
			final String status, final Throwable throwable){
		super(resultCode, extendedResultCode, throwable);
		setStatus(status);
	}
	
    /**
     * The Status of the POSLog Response.
     */
    @XmlElement(name = "Status")
    private String status;
    /**
     * The Transaction number from Journal.
     */
    @XmlElement(name = "TxID")
    private String txID;
     /**
     * Gets the Web Service Processing status.
     *
     * @return        Returns the processing status response.
     */
    public final String getStatus() {
        return status;
    }

    /**
     * Sets the Web Service Processing status.
     *
     * @param statusToSet   The new value for processing
     *                         status response.
     */
    public final void setStatus(final String statusToSet) {
        this.status = statusToSet;
    }
    /**
     * Gets the transaction number.
     *
     * @return        Returns the transaction number.
     */
    public final String getTxID() {
        return txID;
    }

    /**
     * Sets the transaction number.
     *
     * @param txIDToSet        The new value for transaction number.
     */
    public final void setTxID(final String txIDToSet) {
        this.txID = txIDToSet;
    }

    /**
     * Overrides the toString() Method.
     * @return The String representation of POSLog Response.
     */
    public final String toString() {
       StringBuffer str = new StringBuffer();
       String dlmtr = "; ";
       str.append(super.toString());
       str.append(" Status: ").append(this.status).append(dlmtr)
          .append("Transaction Number: ").append(this.txID);
       return str.toString();
    }
}
