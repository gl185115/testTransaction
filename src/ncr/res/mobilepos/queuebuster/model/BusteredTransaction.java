/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* BusteredTransaction
*
* Model Class representing a transaction in the bustered list
*
* Campos, Carlos
*/
package ncr.res.mobilepos.queuebuster.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.credential.model.Employee;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * The Model Class representation of a Transaction for QueueBuster.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Transaction")
@ApiModel(value="BusteredTransaction")
public class BusteredTransaction {
    /** The Workstation ID. */
    @XmlElement(name = "WorkstationID")
    private String workstationid;
    /** The Sequence Number. */
    @XmlElement(name = "SequenceNumber")
    private String sequencenumber;
    /** The Total. */
    @XmlElement(name = "Total")
    private Double total;
    @XmlElement(name = "ReceiptDateTime")
    private String receiptDateTime;
    
    @ApiModelProperty(value="�̎������t����", notes="�̎������t����")
    public String getReceiptDateTime() {
		return receiptDateTime;
	}

	public void setReceiptDateTime(String receiptDateTime) {
		this.receiptDateTime = receiptDateTime;
	}

    @ApiModelProperty(value="�c�Ɠ�", notes="�c�Ɠ�")
	public String getBusinessDayDate() {
		return businessDayDate;
	}

	public void setBusinessDayDate(String businessDayDate) {
		this.businessDayDate = businessDayDate;
	}

    @ApiModelProperty(value="�]�ƈ�", notes="�]�ƈ�")
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@XmlElement(name = "BusinessDayDate")
    private String businessDayDate;
    @XmlElement(name = "Employee")
    private Employee employee;
    @XmlElement(name = "salesPersonName")
    private String setsalesPersonName;

    /**
     * Get the workstation ID.
     * @return  The Workstation ID.
     */
    @ApiModelProperty(value="��Ƒ�R�[�h", notes="��Ƒ�R�[�h")
    public final String getWorkstationid() {
        return workstationid;
    }

    /**
     * Set the Workstation ID.
     * @param workstationidToSet The Workstation ID to set.
     */
    public final void setWorkstationid(final String workstationidToSet) {
        this.workstationid = workstationidToSet;
    }

    /**
     * Get the Sequence Number.
     * @return The Sequence Number.
     */
    @ApiModelProperty(value="�V���A���i���o�[", notes="�V���A���i���o�[")
    public final String getSequencenumber() {
        return sequencenumber;
    }

    /**
     * Set the Sequence Number.
     * @param sequencenumberToSet The Sequence Number to set.
     */
    public final void setSequencenumber(final String sequencenumberToSet) {
        this.sequencenumber = sequencenumberToSet;
    }

    /**
     * Get the Total.
     * @return  The Total.
     */
    @ApiModelProperty(value="����", notes="����")
    public final Double getTotal() {
        return total;
    }

    /**
     * Set the Total.
     * @param totalToSet The Total to set.
     */
    public final void setTotal(final Double totalToSet) {
        this.total = totalToSet;
    }

    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
    public final void setsalesPerson(String salesPersonName) {
        this.setsalesPersonName = salesPersonName;
    }
}
