/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SearchedPosLogs
 *
 * Model Class for SearchedPosLogs
 *
 */
package ncr.res.mobilepos.journalization.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.TransactionSearch;
import ncr.res.mobilepos.model.ResultBase;

/**
 * SearchedPosLogs Model Object.
 *
 * <P>
 * A List of POSLog transaction.
 *
 * <P>
 * SearchedPOSLogs is a model class that represents a list of POSLog retrieved
 * from transaction search.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "POSLogs")
@XmlSeeAlso({ PosLog.class })
@ApiModel(value="SearchedPosLogs")
public class SearchedPosLogs extends ResultBase {

	/**
	 * Constructor.
	 * @param resultCode	The error result code.
	 * @param extendedResultCode	The extended error code.
	 * @param throwable	The exception thrown.
	 */
	public SearchedPosLogs(final int resultCode, final int extendedResultCode,
			final Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
	}
	
    /**
     * The private member variable that will hold the List of PosLog.
     */
    @XmlElementWrapper(name = "POSLogList")
    @XmlElementRef()
   
    private List<TransactionSearch> posLogs;

    /**
     * Gets the pos logs.
     *
     * @return the pos logs
     */
    @ApiModelProperty(value="posLogs", notes="posLogs")
    public final List<TransactionSearch> getPosLogs() {
        return posLogs;
    }

    /**
     * Sets the pos logs.
     *
     * @param newPosLogs the new list of poslog
     */
    public final void setPosLogs(final List<TransactionSearch> newPosLogs) {
        this.posLogs = newPosLogs;
    }

    /**
     * Instantiates a new searched pos logs.
     */
    public SearchedPosLogs() {
        super();
        posLogs = new ArrayList<TransactionSearch>();
    }

    /**
     * Instantiates a new searched pos logs.
     *
     * @param newPosLogs the list of poslog
     */
    public SearchedPosLogs(final List<TransactionSearch> newPosLogs) {
        super();
        this.posLogs = newPosLogs;
    }


}
