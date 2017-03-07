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

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *
 * @author EA185055
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Store")
@ApiModel(value="CMPresetInfo")
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
    
    @XmlElement(name = "Top6Message")
    private String top6Message;
    
    @XmlElement(name = "Top7Message")
    private String top7Message;
    
    @XmlElement(name = "Top8Message")
    private String top8Message;
    
    @XmlElement(name = "Top9Message")
    private String top9Message;
    
    @XmlElement(name = "Top10Message")
    private String top10Message;
    
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
    
    @XmlElement(name = "Bottom6Message")
    private String bottom6Message;
    
    @XmlElement(name = "Bottom7Message")
    private String bottom7Message;
    
    @XmlElement(name = "Bottom8Message")
    private String bottom8Message;
    
    @XmlElement(name = "Bottom9Message")
    private String bottom9Message;
    
    @XmlElement(name = "Bottom10Message")
    private String bottom10Message;

    @ApiModelProperty(value="会社コード", notes="会社コード")
    public final String getCompanyId() {
    	return companyId;
    }

    public final void setCompanyId(String companyId) {
    	this.companyId = companyId;
    }
    
    @ApiModelProperty(value="CMNO", notes="CMNO")
    public final int getCMId() {
        return cmId;
    }

    public final void setCMId(int cmID) {
        this.cmId = cmID;
    }

    @ApiModelProperty(value="CM名称", notes="CM名称")
    public final String getCMName() {
        return cmName;
    }

    public final void setCMName(final String cmName) {
        this.cmName = cmName;
    }

    @ApiModelProperty(value="登録区分", notes="登録区分")
    public final String getCMType() {
        return cmType;
    }

    public final void setCMType(final String cmType) {
        this.cmType = cmType;
    }

    @ApiModelProperty(value="業態コード", notes="業態コード")
    public final String getBizCatId() {
        return bizCatId;
    }

    public final void setBizCatId(final String bizCatID) {
        this.bizCatId = bizCatID;
    }

    @ApiModelProperty(value="店舗コード", notes="店舗コード")
    public final String getStoreId() {
        return storeId;
    }

    public final void setStoreId(final String storeID) {
        this.storeId = storeID;
    }
    
    @ApiModelProperty(value="POSコード", notes="POSコード")
    public final String getTerminalId() {
        return terminalId;
    }

    public final void setTerminalId(final String terminalID) {
        this.terminalId = terminalID;
    }
    
    @ApiModelProperty(value="CM期間FROM", notes="CM期間FROM")
    public final String getStartDate() {
        return startDate;
    }

    public final void setStartDate(final String startDate) {
        this.startDate = startDate;
    }
    
    @ApiModelProperty(value="CM期間TO", notes="CM期間TO")
    public final String getEndDate() {
        return endDate;
    }

    public final void setEndDate(final String endDate) {
        this.endDate = endDate;
    }
    
    @ApiModelProperty(value="上段1行目", notes="上段1行目")
    public final String getTop1Message() {
        return top1Message;
    }

    public final void setTop1Message(final String top1Message) {
        this.top1Message = top1Message;
    }
    
    @ApiModelProperty(value="上段2行目", notes="上段2行目")
    public final String getTop2Message() {
        return top2Message;
    }

    public final void setTop2Message(final String top2Message) {
        this.top2Message = top2Message;
    }
    
    @ApiModelProperty(value="上段3行目", notes="上段3行目")
    public final String getTop3Message() {	
        return top3Message;
    }

    public final void setTop3Message(final String top3Message) {
        this.top3Message = top3Message;
    }
    
    @ApiModelProperty(value="上段4行目", notes="上段4行目")
    public final String getTop4Message() {
        return top4Message;
    }

    public final void setTop4Message(final String top4Message) {
        this.top4Message = top4Message;
    }
    
    @ApiModelProperty(value="上段5行目", notes="上段5行目")
    public final String getTop5Message() {
        return top5Message;
    }

    public final void setTop5Message(final String top5Message) {
        this.top5Message = top5Message;
    }
    
    @ApiModelProperty(value="上段6行目", notes="上段6行目")
    public final String getTop6Message() {
		return top6Message;
	}

    public final void setTop6Message(final String top6Message) {
        this.top6Message = top6Message;
    }
    
    @ApiModelProperty(value="上段7行目", notes="上段7行目")
    public final String getTop7Message() {
        return top7Message;
    }

    public final void setTop7Message(final String top7Message) {
        this.top7Message = top7Message;
    }
    
    @ApiModelProperty(value="上段8行目", notes="上段8行目")
    public final String getTop8Message() {  
        return top8Message;
    }

    public final void setTop8Message(final String top8Message) {
        this.top8Message = top8Message;
    }
    
    @ApiModelProperty(value="上段9行目", notes="上段9行目")
    public final String getTop9Message() {
        return top9Message;
    }

    public final void setTop9Message(final String top9Message) {
        this.top9Message = top9Message;
    }
    
    @ApiModelProperty(value="上段10行目", notes="上段10行目")
    public final String getTop10Message() {
        return top10Message;
    }

    public final void setTop10Message(final String top10Message) {
        this.top10Message = top10Message;
    }
    
    @ApiModelProperty(value="下段1行目", notes="下段1行目")
    public final String getBottom1Message() {
        return bottom1Message;
    }

    public final void setBottom1Message(final String bottom1Message) {
        this.bottom1Message = bottom1Message;
    }
    
    @ApiModelProperty(value="下段2行目", notes="下段2行目")
    public final String getBottom2Message() {
        return bottom2Message;
    }

    public final void setBottom2Message(final String bottom2Message) {
        this.bottom2Message = bottom2Message;
    }
    
    @ApiModelProperty(value="下段3行目", notes="下段3行目")
    public final String getBottom3Message() {
        return bottom3Message;
    }

    public final void setBottom3Message(final String bottom3Message) {
        this.bottom3Message = bottom3Message;
    }
    
    @ApiModelProperty(value="下段4行目", notes="下段4行目")
    public final String getBottom4Message() {
        return bottom4Message;
    }

    public final void setBottom4Message(final String bottom4Message) {
        this.bottom4Message = bottom4Message;
    }
    
    @ApiModelProperty(value="下段5行目", notes="下段5行目")
    public final String getBottom5Message() {
        return bottom5Message;
    }

    public final void setBottom5Message(final String bottom5Message) {
        this.bottom5Message = bottom5Message;
    }

    @ApiModelProperty(value="下段6行目", notes="下段6行目")
	public final String getBottom6Message() {
		return bottom6Message;
	}

    public final void setBottom6Message(final String bottom6Message) {
        this.bottom6Message = bottom6Message;
    }
    
    @ApiModelProperty(value="下段7行目", notes="下段7行目")
    public final String getBottom7Message() {
        return bottom7Message;
    }

    public final void setBottom7Message(final String bottom7Message) {
        this.bottom7Message = bottom7Message;
    }
    
    @ApiModelProperty(value="下段8行目", notes="下段8行目")
    public final String getBottom8Message() {
        return bottom8Message;
    }

    public final void setBottom8Message(final String bottom8Message) {
        this.bottom8Message = bottom8Message;
    }
    
    @ApiModelProperty(value="下段9行目", notes="下段9行目")
    public final String getBottom9Message() {
        return bottom9Message;
    }

    public final void setBottom9Message(final String bottom9Message) {
        this.bottom9Message = bottom9Message;
    }
    
    @ApiModelProperty(value="下段10行目", notes="下段10行目")
    public final String getBottom10Message() {
        return bottom10Message;
    }

    public final void setBottom10Message(final String bottom10Message) {
        this.bottom10Message = bottom10Message;
    }
}
