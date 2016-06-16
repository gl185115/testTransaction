package ncr.res.mobilepos.journalization.model;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01     2014.11.19  GUZHEN    販売員情報取得(新規) 
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
    
    @ApiModelProperty(value="カナ氏名", notes="カナ氏名")
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
   
    @ApiModelProperty(value="氏名", notes="氏名")
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
    @ApiModelProperty(value="担当者コード", notes="担当者コード")
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
