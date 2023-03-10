package ncr.res.mobilepos.barcodeassignment.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "item")
@ApiModel(value = "item")
public class ItemSale {

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "type")
    private String type;
    
    @XmlElement(name = "maxCount")
    private int maxCount;
    
    @XmlElement(name = "symbol")
    private String symbol;
    
	@XmlElement(name = "emergency")
    private boolean emergency = true;

    @XmlElement(name = "format")
    private List<String> format;

    @XmlElement(name = "nextformat")
    private List<String> nextFormat;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
	
    public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
    
    public boolean getEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    /**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List<String> getNextFormat() {
        return nextFormat;
    }

    public void setNextFormat(List<String> nextFormat) {
        this.nextFormat = nextFormat;
    }

    public List<String> getFormat() {
        return format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

	@Override
	public String toString() {
		return "ItemSale [description=" + description + ", id=" + id + ", type=" + type + ", maxCount=" + maxCount
				+ ", symbol=" + symbol + ", emergency=" + emergency + ", format=" + format + ", nextFormat="
				+ nextFormat + "]";
	}

}
