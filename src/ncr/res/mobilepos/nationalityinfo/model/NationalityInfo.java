package ncr.res.mobilepos.nationalityinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "NationalityInfo")
@ApiModel(value="NationalityInfo")
public class NationalityInfo {
	@XmlElement(name = "CompanyId")
	private String companyId;
	@XmlElement(name = "StoreId")
	private String storeId;
	@XmlElement(name = "NationalityId")
	private String id;
	@XmlElement(name = "NationalityName")
	private String name;
	@XmlElement(name = "NationalityKanaName")
	private String kanaName;
	@XmlElement(name = "NationalityShortName")
	private String shortName;
	@XmlElement(name = "NationalityShortKanaName")
	private String shortKanaName;

	@ApiModelProperty( value="��ЃR�[�h", notes="��ЃR�[�h")
	public final String getCompanyId() {
		return companyId;
	}

	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty( value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public final String getStoreId() {
		return storeId;
	}

	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@ApiModelProperty( value="���ЃR�[�h", notes="���ЃR�[�h")
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	@ApiModelProperty( value="���Ж���", notes="���Ж���")
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty( value="���Ж���(�J�i)", notes="���Ж���(�J�i)")
	public final String getKanaName() {
		return kanaName;
	}

	public final void setKanaName(String kanaName) {
		this.kanaName = kanaName;
	}

	@ApiModelProperty( value="���З���", notes="���З���")
	public final String getShortName() {
		return shortName;
	}

	public final void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@ApiModelProperty( value="���З���(�J�i)", notes="���З���(�J�i)")
	public final String getShortKanaName() {
		return shortKanaName;
	}

	public final void setShortKanaName(String shortKanaName) {
		this.shortKanaName = shortKanaName;
	}

}
