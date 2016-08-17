package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
/**
 * 改定履歴
 * バージョン         改定日付               担当者名           改定内容
 * 1.01               2014.11.27             FENGSHA           売上表を対応
 */

/**
 * The Class ReportItems.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReportItems")
@ApiModel(value="ReportItems")
public class ReportItems extends ResultBase {

    /** The report item list. */
    @XmlElement(name = "ReportItemList")
    private ReportItem[] reportItemList = null;

    
    @XmlElement(name = "tillid")
    private String tillid;
    
	/** The business day date. */
    @XmlElement(name = "BusinessDate")
    private String businessDayDate = "";

    //1.01 FENGSHA 2014.11.27 売上表を対応 ADD START
    /** The store name. */
    @XmlElement(name = "StoreName")
    private String storeName = "";

    /** The operator name**/
    @XmlElement(name = "OperatorName")
    private String operatorName = "";

    /** The department name. */
    @XmlElement(name = "DepartmentName")
    private String departmentName = "";
    
    /** The company id. */
    @XmlElement(name = "CompanyId")
    private String CompanyId = "";

    /** The store id. */
    @XmlElement(name = "StoreId")
    private String StoreId = "";
    
    /** The operator id**/
    @XmlElement(name = "OperatorId")
    private String OperatorId = "";
    
    /** The department id. */
    @XmlElement(name = "DepartmentId")
    private String DepartmentId = "";
    //1.01 FENGSHA 2014.11.27 売上表を対応 ADD END

    /**
     * Sets the report items.
     *
     * @param itemarray
     *            the new report items
     */
    public final void setReportItems(final ReportItem[] itemarray) {
        this.reportItemList = (ReportItem[]) itemarray.clone();
    }

    /**
     * Gets the report items.
     *
     * @return the report items
     */
    @ApiModelProperty(value="公布するプロジェクト", notes="公布するプロジェクト")
    public final ReportItem[] getReportItems() {
        if (reportItemList == null) {
            return new ReportItem[0];
        }
        return (ReportItem[]) this.reportItemList.clone();
    }

    /**
     * Sets the business day date.
     *
     * @param businessdaydate
     *            the new business day date
     */
    public final void setBusinessDayDate(final String businessdaydate) {
        this.businessDayDate = businessdaydate;
    }

    /**
     * Gets the business day date.
     *
     * @return the business day date
     */
    @ApiModelProperty(value="処理日付", notes="処理日付")
    public final String getBusinessDayDate() {
        return this.businessDayDate;
    }
    //1.01 FENGSHA 2014.11.27 売上表を対応 ADD START
    /**
     * Gets the store name.
     *
     * @return the store name
     */
    @ApiModelProperty(value="店舗名", notes="店舗名")
    public String getStoreName() {
        return storeName;
    }
    /**
     * Sets the store name.
     *
     * @param storeName
     *            the new store name
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * Gets the operator name.
     *
     * @return the operator name
     */
    @ApiModelProperty(value="担当者名", notes="担当者名ト")
    public String getOperatorName() {
        return operatorName;
    }
    /**
     * Sets the operator name.
     *
     * @param operator
     *            the new operator name
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    /**
     * Gets the department name.
     *
     * @return the department name
     */
    @ApiModelProperty(value="部署名", notes="部署名")
    public final String getDepartmentName() {
        return departmentName;
    }
    /**
     * Sets the department name.
     *
     *@param department departmentName
     *                  the new department name
     */
    public final void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return the companyId
     */
    @ApiModelProperty(value="会社コード", notes="会社コード")
    public final String getCompanyId() {
        return CompanyId;
    }

    /**
     * @param storeId the storeId to set
     */
    public final void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

	/**
	 * @return the storeId
	 */
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
	public final String getStoreId() {
		return StoreId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public final void setStoreId(String storeId) {
		StoreId = storeId;
	}

	/**
	 * @return the operatorId
	 */
	@ApiModelProperty(value="担当者コード", notes="担当者コード")
	public final String getOperatorId() {
		return OperatorId;
	}

	/**
	 * @param operatorId the operatorId to set
	 */
	public final void setOperatorId(String operatorId) {
		OperatorId = operatorId;
	}

	/**
	 * @return the departmentId
	 */
	@ApiModelProperty(value="部署コード", notes="部署コード")
	public final String getDepartmentId() {
		return DepartmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public final void setDepartmentId(String departmentId) {
		DepartmentId = departmentId;
	}

  //1.01 FENGSHA 2014.11.27 売上表を対応 ADD END
	@ApiModelProperty(value="ドロワーコード", notes="ドロワーコード")
	public String getTillid() {
		return tillid;
	}

	public void setTillid(String tillid) {
		this.tillid = tillid;
	}
}
