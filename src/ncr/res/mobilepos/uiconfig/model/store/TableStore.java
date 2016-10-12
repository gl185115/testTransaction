package ncr.res.mobilepos.uiconfig.model.store;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TableStore")
public class TableStore {
    
	@XmlElement(name = "storeEntries")
	private List<StoreEntry> storeEntries = null;
	
	@XmlElement(name = "levelKey")
	private String levelKey = null;
	
	@XmlElement(name = "category")
	private String category = null;

	/**
	 * @return tableStore
	 */
	public List<StoreEntry> getStoreEntries() {
        return storeEntries;
	}
	/**
	 * @param tableStore ƒZƒbƒg‚·‚é tableStore
	 */
	public void setStoreEntries(List<StoreEntry> storeEntries) {
		this.storeEntries = storeEntries;
	}
	
	

	public String getLevelKey() {
		return levelKey;
	}



	public void setLevelKey(String levelKey) {
		this.levelKey = levelKey;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	
	public final String toString() {
        ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
