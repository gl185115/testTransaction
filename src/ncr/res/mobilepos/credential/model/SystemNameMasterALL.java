package ncr.res.mobilepos.credential.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SystemNameMasterALL")

public class SystemNameMasterALL extends ResultBase{
    /**the all system name master
     */
    @XmlElement(name = "systemNameMasterALL")
    private List<SystemNameMasterList> systemnamemasterall = new ArrayList<SystemNameMasterList>();

    /**
     * Gets all system name master.
     *
     * @return  the all system name master
     */
    public List<SystemNameMasterList> getSystemnamemasterall() {
        return systemnamemasterall;
    }

    /**
     * Sets the all system name master.
     *
     * @param sysrtem name master all
     *            systemnamemasterall
     */


    public void setSystermnamemasterall(
            List<SystemNameMasterList> systemnamemasterall) {
        this.systemnamemasterall = systemnamemasterall;
    }
    
    @Override
   	public String toString() {
   		
   		StringBuilder sb = new StringBuilder();
   		String crlf = "\r\n";
   		sb.append(super.toString());
   		
   		if(null != this.systemnamemasterall){
   			sb.append(crlf).append("Systemnamemasterall: ").append(this.systemnamemasterall.toString());
   		}
   		
   		return sb.toString();
       }

}
