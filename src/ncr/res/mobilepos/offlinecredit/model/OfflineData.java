//
// Copyright(c) 2015 NCR Japan Ltd.
//
package ncr.res.mobilepos.offlinecredit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Offline credit data holder.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OfflineData")
public class OfflineData {
    /**
     * Create empty instance.
     */
    public OfflineData() {
    }

    @XmlElement(name = "companyId")
    String companyId;
    /** ��Дԍ���ݒ肷��B*/
    public void setCompanyId(String val) {
        companyId = val;
    }
    /** ��Дԍ����擾����B*/
    public String getCompanyId() {
        return companyId;
    }
    @XmlElement(name = "retailStoreId")
    String retailStoreId;
    /** �X�ܔԍ���ݒ肷��B*/
    public void setRetailStoreId(String val) {
        retailStoreId = val;
    }
    /** �X�ܔԍ����擾����B*/
    public String getRetailStoreId() {
        return retailStoreId;
    }
    @XmlElement(name = "workstationId")
    String workstationId;
    /** �[��ID��ݒ肷��B*/
    public void setWorkstationId(String id) {
        workstationId = id;
    }
    /** �[��ID���擾����B*/
    public String getWorkstationId() {
        return workstationId;
    }
    @XmlElement(name = "sequenceNumber")
    int sequenceNumber;
    /** ����ԍ���ݒ肷��B*/
    public void setSequenceNumber(int val) {
        sequenceNumber = val;
    }
    /** ����ԍ����擾����B*/
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    @XmlElement(name = "businessDayDate")
    String businessDayDate;
    /** �c�Ɠ��t��ݒ肷��B */
    public void setBusinessDayDate(String val) {
        businessDayDate = val;
    }
    /** �c�Ɠ��t���擾����B */
    public String getBusinessDayDate() {
        return businessDayDate;
    }
    @XmlElement(name = "IV")
    String iv;
    /** IV��ݒ肷��B*/
    public void setIV(String val) {
        iv = val;
    }
    /** IV���擾����B*/
    public String getIV() {
        return iv;
    }
    @XmlElement(name = "personalData")
    String personalData;
    /** �f�[�^�{�̂�ݒ肷��B*/
    public void setPersonalData(String val) {
        personalData = val;
    }
    /** �f�[�^�{�̂��擾����B*/
    public String getPersonalData() {
        return personalData;
    }
}


