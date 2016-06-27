package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="SequenceNo")
@ApiModel(value="SequenceNo")
public class SequenceNo extends ResultBase{

    @XmlElement(name="SequenceTypeId")
    private String SequenceTypeId;

    @XmlElement(name="SequenceNo")
    private String SequenceNo;

    @XmlElement(name="SequenceTypeName")
    private String SequenceTypeName;

    /**
     * @return SequenceTypeId
     */
     @ApiModelProperty(value="シーケンスタイプコード", notes="シーケンスタイプコード")
    public String getSequenceTypeId() {
        return SequenceTypeId;
    }

    /**
     * @param sequenceTypeId
     */
    public void setSequenceTypeId(String sequenceTypeId) {
        SequenceTypeId = sequenceTypeId;
    }

    /**
     * @return SequenceNo
     */

    @ApiModelProperty(value="送信管理順序", notes="送信管理順序")
    public String getSequenceNo() {
        return SequenceNo;
    }

    /**
     * @param sequenceNo
     */
    public void setSequenceNo(String sequenceNo) {
        SequenceNo = sequenceNo;
    }

    /**
     * @return SequenceTypeName
     */
    @ApiModelProperty(value="シーケンスタイプ名", notes="シーケンスタイプ名")
    public String getSequenceTypeName() {
        return SequenceTypeName;
    }

    /**
     * @param sequenceTypeName
     */
    public void setSequenceTypeName(String sequenceTypeName) {
        SequenceTypeName = sequenceTypeName;
    }


	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());

		if(null != this.SequenceTypeId){
			sb.append(crlf).append("SequenceTypeId: ").append(this.SequenceTypeId.toString());
		}

		if(null != this.SequenceNo){
			sb.append(crlf).append("SequenceNo: ").append(this.SequenceNo.toString());
		}

		if(null != this.SequenceTypeName){
			sb.append(crlf).append("SequenceTypeName: ").append(this.SequenceTypeName.toString());
		}

		return sb.toString();
	}

}
