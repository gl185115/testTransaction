/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * PosLog
 *
 * Model Class for PosLog
 *
 * Carlos G. Campos
 */
package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;

/**
 * SearchedPosLog Model Object.
 *
 * <P>
 * A POSLog object representing a PosLog XML.
 *
 * <P>
 * SearchedPOSLog is a model class that represents a POSLog retrieved from
 * transaction search. Specially for Last Normal Transaction search.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SearchForwardPosLog")
public class SearchForwardPosLog extends ResultBase {

	/**
	 * The private member variable that will hold the Transaction information.
	 */
	@XmlElement(name = "PosLog")
	private PosLog poslog;

	/**
	 * The private member variable that will hold the forward status.
	 */
	@XmlElement(name = "Status")
	private int Status;

	@XmlElement(name = "posLogXml")
	private String posLogXml;

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	/**
	 * Default Constructor for PosLog.
	 *
	 * <P>
	 * Initializes Transaction object.
	 */
	public SearchForwardPosLog() {
	    poslog = new PosLog();
	}

	/**
	 * Sets the PosLog.
	 *
	 * @param transactionToSet
	 *            The new value for the Transaction under POSLog.
	 */
	public SearchForwardPosLog(final PosLog posLogToSet) {
	    setPoslog(posLogToSet);
	}

	public String getPosLogXml() {
		return posLogXml;
	}

	public void setPosLogXml(String posLogXml) {
		this.posLogXml = posLogXml;
	}

	public PosLog getPoslog() {
		return poslog;
	}

	public void setPoslog(PosLog poslog) {
		this.poslog = poslog;
	}

	/**
	 * Overrides the toString() method.
	 * 
	 * @return The String representration of POSLog.
	 */
	public final String toString() {
		StringBuffer str = new StringBuffer();
		str.append(super.toString()).append("\r\n");

		if (null != poslog) {
			str.append("Transaction: ").append(poslog.toString()).append("Status: ").append(Status);
		}

		return str.toString();
	}
}
