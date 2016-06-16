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

import ncr.res.mobilepos.model.ResultBase;
/**
 * @author 
 * 
 * Sales person Class is a Model representation of the Sales person List.
 */
@XmlRootElement(name = "Salesperson")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="Salesperson")
public class Salesperson extends ResultBase{
	
	/*This list is used to save sales person information*/
    @XmlElement(name = "Salespersoninfo")
    private Salespersoninfo[] Salesperson;
    
    /**
	 * Gets the sales person list.
	 *
	 * @return the sales person list
	 */
    @ApiModelProperty(value="�̔����R�[�h", notes="�̔����R�[�h")
    public final Salespersoninfo[] getSalesperson() {
        return Salesperson;
    }
    
    /**
	 * Sets the sales person information of the list.
	 *
	 * @param sales person information ToSet
	 *            the new sales person
	 */
    public final void setSalesperson(final Salespersoninfo[] salesperson) {
        Salesperson = salesperson;
    }

    @Override
    public final String toString() {
		
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());
		
		if( null != this.Salesperson){
			sb.append(crlf).append("Salesperson: ").append(this.Salesperson.toString());
		}
		
		return sb.toString();
    }
}
