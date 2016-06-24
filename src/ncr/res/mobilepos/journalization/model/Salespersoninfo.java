package ncr.res.mobilepos.journalization.model;
/**
 * ���藚��
 * �o�[�W����      ������t        �S���Җ�      ������e
 * 1.01     2014.11.19  GUZHEN    �̔������擾(�V�K) 
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


/**
 * Sales person info
 * Model for Sales person  information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Salespersoninfo")
@ApiModel(value="Salespersoninfo")
public class Salespersoninfo {

	/* the opernate kananame */
    @XmlElement(name = "OpeKanaName")
    private String OpeKanaName ;
    
    /* the opernate name */
    @XmlElement(name = "OpeName")
    private String OpeName ;
    
    /* the employee code */
    @XmlElement(name = "EmpCode")
    private String EmpCode ;

    /**
	 * Gets the opernate kananame.
	 *
	 * @return the opernate kananame
	 */
    
    @ApiModelProperty(value="�J�i����", notes="�J�i����")
    public String getOpeKanaName() {
        return OpeKanaName;
    }
    
    /**
	 * Sets the opernate kananame of the column.
	 *
	 * @param opernatekananameToSet
	 *            the new opernate kananame
	 */
    public void setOpeKanaName(String opeKanaName) {
        OpeKanaName = opeKanaName;
    }
    
    /**
   	 * Gets the opernate name.
   	 *
   	 * @return the opernate name
   	 */
   
    @ApiModelProperty(value="����", notes="����")
    public String getOpeName() {
        return OpeName;
    }
    
    /**
   	 * Sets the opernate name of the column.
   	 *
   	 * @param opernatenameToSet
   	 *            the new opernate name
   	 */
    public void setOpeName(String opeName) {
        OpeName = opeName;
    }
    
    /**
   	 * Gets the employee code.
   	 *
   	 * @return the employee code
   	 */
    @ApiModelProperty(value="�S���҃R�[�h", notes="�S���҃R�[�h")
    public String getEmpCode() {
        return EmpCode;
    }
    
    /**
   	 * Sets the employee code of the column.
   	 *
   	 * @param employeecodeToSet
   	 *            the new employee code
   	 */
    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		String crlf= "\r\n";
		
		if(null != this.OpeKanaName){
			sb.append("OpeKanaName: ").append(this.OpeKanaName.toString());
		}
		
		if(null != this.OpeName){
			sb.append(crlf).append("OpeName: ").append(this.OpeName.toString());
		}
		
		if(null != this.EmpCode){
			sb.append(crlf).append("EmpCode: ").append(this.EmpCode.toString());
		}
		
		return sb.toString();
	}

}
