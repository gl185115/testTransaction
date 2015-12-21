/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * Store
 *
 * Is a Class for Store information.
 *
 */

package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author EA185055
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Store")
public class CMPresetInfo {    
	@XmlElement(name = "CompanyId")
	private String companyId;

	@XmlElement(name = "CMId")
    private int cmId;

    @XmlElement(name = "CMName")
    private String cmName;
    
    @XmlElement(name = "CMType")
    private String cmType;
    
    @XmlElement(name = "BizCatId")
    private String bizCatId;

    @XmlElement(name = "StoreId")
    private String storeId;

    @XmlElement(name = "TerminalId")
    private String terminalId;
    
    @XmlElement(name = "StartDate")
    private String startDate;
    
    @XmlElement(name = "EndDate")
    private String endDate;
    
    @XmlElement(name = "Top1Message")
    private String top1Message;
    
    @XmlElement(name = "Top2Message")
    private String top2Message;
    
    @XmlElement(name = "Top3Message")
    private String top3Message;
    
    @XmlElement(name = "Top4Message")
    private String top4Message;
    
    @XmlElement(name = "Top5Message")
    private String top5Message;
    
    @XmlElement(name = "Bottom1Message")
    private String bottom1Message;

    @XmlElement(name = "Bottom2Message")
    private String bottom2Message;
    
    @XmlElement(name = "Bottom3Message")
    private String bottom3Message;
    
    @XmlElement(name = "Bottom4Message")
    private String bottom4Message;
    
    @XmlElement(name = "Bottom5Message")
    private String bottom5Message;

    public final String getCompanyId() {
    	return companyId;
    }

    public final void setCompanyId(String companyId) {
    	this.companyId = companyId;
    }
    
    public final int getCMId() {
        return cmId;
    }

    public final void setCMId(int cmID) {
        this.cmId = cmID;
    }

    public final String getCMName() {
        return cmName;
    }

    public final void setCMName(final String cmName) {
        this.cmName = cmName;
    }

    public final String getCMType() {
        return cmType;
    }

    public final void setCMType(final String cmType) {
        this.cmType = cmType;
    }

    public final String getBizCatId() {
        return bizCatId;
    }

    public final void setBizCatId(final String bizCatID) {
        this.bizCatId = bizCatID;
    }

    public final String getStoreId() {
        return storeId;
    }

    public final void setStoreId(final String storeID) {
        this.storeId = storeID;
    }
    
    public final String getTerminalId() {
        return terminalId;
    }

    public final void setTerminalId(final String terminalID) {
        this.terminalId = terminalID;
    }
    
    public final String getStartDate() {
        return startDate;
    }

    public final void setStartDate(final String startDate) {
        this.startDate = startDate;
    }
    
    public final String getEndDate() {
        return endDate;
    }

    public final void setEndDate(final String endDate) {
        this.endDate = endDate;
    }
    
    public final String getTop1Message() {
        return top1Message;
    }

    public final void setTop1Message(final String top1Message) {
        this.top1Message = top1Message;
    }
    
    public final String getTop2Message() {
        return top2Message;
    }

    public final void setTop2Message(final String top2Message) {
        this.top2Message = top2Message;
    }
    
    public final String getTop3Message() {
        return top3Message;
    }

    public final void setTop3Message(final String top3Message) {
        this.top3Message = top3Message;
    }
    
    public final String getTop4Message() {
        return top4Message;
    }

    public final void setTop4Message(final String top4Message) {
        this.top4Message = top4Message;
    }
    
    public final String getTop5Message() {
        return top5Message;
    }

    public final void setTop5Message(final String top5Message) {
        this.top5Message = top5Message;
    }
    
    public final String getBottom1Message() {
        return bottom1Message;
    }

    public final void setBottom1Message(final String bottom1Message) {
        this.bottom1Message = bottom1Message;
    }
    
    public final String getBottom2Message() {
        return bottom2Message;
    }

    public final void setBottom2Message(final String bottom2Message) {
        this.bottom2Message = bottom2Message;
    }
    
    public final String getBottom3Message() {
        return bottom3Message;
    }

    public final void setBottom3Message(final String bottom3Message) {
        this.bottom3Message = bottom3Message;
    }
    
    public final String getBottom4Message() {
        return bottom4Message;
    }

    public final void setBottom4Message(final String bottom4Message) {
        this.bottom4Message = bottom4Message;
    }
    
    public final String getBottom5Message() {
        return bottom5Message;
    }

    public final void setBottom5Message(final String bottom5Message) {
        this.bottom5Message = bottom5Message;
    }
}
