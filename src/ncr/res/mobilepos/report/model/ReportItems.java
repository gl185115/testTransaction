package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
    public final String getBusinessDayDate() {
        return this.businessDayDate;
    }
    //1.01 FENGSHA 2014.11.27 売上表を対応 ADD START
    /**
     * Gets the store name.
     *
     * @return the store name
     */
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

	public String getTillid() {
		return tillid;
	}

	public void setTillid(String tillid) {
		this.tillid = tillid;
	}
}
