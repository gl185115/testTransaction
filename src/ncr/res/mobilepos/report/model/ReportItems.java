package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
/**
 * ���藚��
 * �o�[�W����         ������t               �S���Җ�           ������e
 * 1.01               2014.11.27             FENGSHA           ����\��Ή�
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

    //1.01 FENGSHA 2014.11.27 ����\��Ή� ADD START
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
    //1.01 FENGSHA 2014.11.27 ����\��Ή� ADD END

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
    @ApiModelProperty(value="���z����v���W�F�N�g", notes="���z����v���W�F�N�g")
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
    @ApiModelProperty(value="�������t", notes="�������t")
    public final String getBusinessDayDate() {
        return this.businessDayDate;
    }
    //1.01 FENGSHA 2014.11.27 ����\��Ή� ADD START
    /**
     * Gets the store name.
     *
     * @return the store name
     */
    @ApiModelProperty(value="�X�ܖ�", notes="�X�ܖ�")
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
    @ApiModelProperty(value="�S���Җ�", notes="�S���Җ��g")
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
    @ApiModelProperty(value="������", notes="������")
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
    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
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
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
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
	@ApiModelProperty(value="�S���҃R�[�h", notes="�S���҃R�[�h")
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
	@ApiModelProperty(value="�����R�[�h", notes="�����R�[�h")
	public final String getDepartmentId() {
		return DepartmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public final void setDepartmentId(String departmentId) {
		DepartmentId = departmentId;
	}

  //1.01 FENGSHA 2014.11.27 ����\��Ή� ADD END
	@ApiModelProperty(value="�h�����[�R�[�h", notes="�h�����[�R�[�h")
	public String getTillid() {
		return tillid;
	}

	public void setTillid(String tillid) {
		this.tillid = tillid;
	}
}
