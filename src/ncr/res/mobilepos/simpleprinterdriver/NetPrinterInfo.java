package ncr.res.mobilepos.simpleprinterdriver;

/**
 * Net Printer Information.
 *
 */
public class NetPrinterInfo {
    /**
     * IP address of printer.
     */
    private String url;
    /**
     * TCP protocol port.
     */
    private int portTCP;
    /**
     * UDP Protocol port.
     */
    private int portUDP;

    /**
     * Getter for IP address of printer.
     * @return  IP address of printer
     */
    public final String getUrl() {
        return url;
    }
    /**
     * Setter for the IP address of printer.
     * @param urlToSet      IP Address to set
     */
    public final void setUrl(final String urlToSet) {
        this.url = urlToSet;
    }
    /**
     * Getter of TCP protocol port.
     * @return  TCP protocol port
     */
    public final int getPortTCP() {
        return portTCP;
    }
    /**
     * Setter for TCP protocol port.
     * @param portTcp   TCP protocol to set
     */
    public final void setPortTCP(final int portTcp) {
        portTCP = portTcp;
    }
    /**
     * Getter for UDP Protocol port.
     * @return      UDP protocol port
     */
    public final int getPortUDP() {
        return portUDP;
    }
    /**
     * Setter for UDP Protocolport.
     * @param portUdp   UDP Protocol port to set
     */
    public final void setPortUDP(final int portUdp) {
        portUDP = portUdp;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("url=" + url);
        sb.append("\nportTcp=" + portTCP);
        sb.append("\nportUdp=" + portUDP);
        return sb.toString();
    }
}
