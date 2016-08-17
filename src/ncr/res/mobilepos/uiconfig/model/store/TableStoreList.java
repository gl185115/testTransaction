package ncr.res.mobilepos.uiconfig.model.store;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TableStoreList")
public class TableStoreList extends ResultBase{
    
	@XmlElement(name = "tableStore")
	private List<TableStore> tableStore = null;
	
	
	

	/**
	 * @return tableStore
	 */
	public List<TableStore> getTableStore() {
        return tableStore;
	}
	
	public void setTableStore(List<TableStore> tableStore) {
		this.tableStore = tableStore;
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
