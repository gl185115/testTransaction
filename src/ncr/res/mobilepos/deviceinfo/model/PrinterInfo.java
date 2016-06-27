package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * PrinterInfo
 * Model for representing printer information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PrinterInfo")
@ApiModel(value="PrinterInfo")
public class PrinterInfo {
	 /**
     * The store identifier is set here.
     */
    @XmlElement(name = "RetailStoreID")
    private String storeid;
    /**
     * The printer identifier is set here.
     */
    @XmlElement(name = "PrinterID")
    private String printerid;
    /**
     * The printer name is set here.
     */
    @XmlElement(name = "PrinterName")
    private String printername;
    /**
     * The printer description is set here.
     */
    @XmlElement(name = "PrinterDescription")
    private String printerdescription;

    /** The ip address. */
    @XmlElement(name = "IpAddress")
    private String ipAddress;

    /** The port num tcp. */
    @XmlElement(name = "PortNumTcp")
    private String portNumTcp;

    /** The port num udp. */
    @XmlElement(name = "PortNumUdp")
    private String portNumUdp;

    /** The status. */
    @XmlElement(name = "Status")
    private String status;

    private String updAppId;

    private String updOpeCode;

    @ApiModelProperty(value="最終更新プログラムID", notes="最終更新プログラムID")
    public String getUpdAppId() {
        return updAppId;
    }
    public void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }
    @ApiModelProperty(value="最終更新ユーザーID", notes="最終更新ユーザーID")
    public String getUpdOpeCode() {
        return updOpeCode;
    }
    public void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }
    /**
     * Setter for the store id.
     * @param printerId the store identifier
     */
    public final void setRetailStoreId(final String storeId) {
        storeid = storeId;
    }
    /**
     * Getter for the store id.
     * @return String
     */
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
    public final String getRetailStoreId() {
        return storeid;
    }
    /**
     * Setter for the printer id.
     * @param printerId the printer identifier
     */
    public final void setPrinterId(final String printerId) {
        printerid = printerId;
    }
    /**
     * Getter for the printer id.
     * @return int
     */
    @ApiModelProperty(value="プリンターコード", notes="プリンターコード")
    public final String getPrinterId() {
        return printerid;
    }
    /**
     * Setter for the printer name.
     * @param printerName the printer name
     */
    public final void setPrinterName(final String printerName) {
        printername = printerName;
    }
    /**
     * Getter for the printer name.
     * @return String
     */
    @ApiModelProperty(value="プリンター名称", notes="プリンター名称")
    public final String getPrinterName() {
        return printername;
    }
    /**
     * Setter for the printer description.
     * @param printerDescription the printer description
     */
    public final void setPrinterDescription(final String printerDescription) {
        printerdescription = printerDescription;
    }
    /**
     * Getter for the printer description.
     * @return String
     */
    @ApiModelProperty(value="備考", notes="備考")
    public final String getPrinterDescription() {
        return printerdescription;
    }
    /**
     * Convert to string.
     * @return String
     */
    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }

    /**
     * Sets the IP address.
     *
     * @param newIpAddress the new IP Address
     */
    public final void setIpAddress(final String newIpAddress) {
        this.ipAddress = newIpAddress;
    }

    /**
     * Gets the IP Address.
     *
     * @return the IP Address
     */
    @ApiModelProperty(value="IPアドレス", notes="IPアドレス")
    public final String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the port num tcp.
     *
     * @param newPortNumTcp the new TCP port number.
     */
    public final void setPortNumTcp(final String newPortNumTcp) {
        this.portNumTcp = newPortNumTcp;
    }

    /**
     * Gets the port number tcp.
     *
     * @return the TCP port number.
     */
    @ApiModelProperty(value="TCPポート番号", notes="TCPポート番号")
    public final String getPortNumTcp() {
        return portNumTcp;
    }

    /**
     * Sets the port num udp.
     *
     * @param newPortNumUdp the new UDP port number.
     */
    public final void setPortNumUdp(final String newPortNumUdp) {
        this.portNumUdp = newPortNumUdp;
    }

    /**
     * Gets the port number udp.
     *
     * @return the UDP port number
     */
    @ApiModelProperty(value="UDPポート番号", notes="UDPポート番号")
    public final String getPortNumUdp() {
        return portNumUdp;
    }

    /**
     * Setter for the status.
     * @param status of printer
     */
	public final void setStatus(final String status) {
		this.status = status;
	}
	/**
     * Gets the status.
     *
     * @return the status of printer
     */
	@ApiModelProperty(value="レコードステータス", notes="レコードステータス")
    public final String getStatus() {
        return portNumUdp;
    }
}
