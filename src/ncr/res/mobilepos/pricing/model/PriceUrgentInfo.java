package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PriceUrgentInfo")
@ApiModel(value="PriceUrgentInfo")
public class PriceUrgentInfo {

	@XmlElement(name = "dpt")
    private String dpt;

	@XmlElement(name = "line")
    private String line;

	@XmlElement(name = "class")
    private String clas;

	@XmlElement(name = "sku")
    private String sku;

	@XmlElement(name = "urgentPrice")
    private long urgentPrice;
	
	@ApiModelProperty(value="����R�[�h", notes="����R�[�h")
    public String getDpt() {
        return dpt;
    }

    public void setDpt(String dpt) {
        this.dpt = dpt;
    }

	@ApiModelProperty(value="�i��R�[�h", notes="�i��R�[�h")
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

	@ApiModelProperty(value="�N���X�R�[�h", notes="�N���X�R�[�h")
    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

	@ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @ApiModelProperty(value="�ً}����", notes="�ً}����")
	public long getUrgentPrice() {
		return urgentPrice;
	}

	public void setUrgentPrice(long urgentPrice) {
		this.urgentPrice = urgentPrice;
	}
	
   @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String clrf = "; ";
        str.append("dpt: ").append(dpt).append(clrf)
        .append("line: ").append(line).append(clrf)
        .append("clas: ").append(clas).append(clrf)
        .append("sku: ").append(sku).append(clrf)
        .append("urgentPrice: ").append(urgentPrice).append(clrf);
        return str.toString();
    }
	
	public PriceUrgentInfo() {
	}

	//copy constructor
	public PriceUrgentInfo(final PriceUrgentInfo info) {
	    this.dpt = info.getDpt();
	    this.line = info.getLine();
	    this.clas = info.getClas();
	    this.sku = info.getSku();
	    this.urgentPrice = info.getUrgentPrice();	
    }
}

