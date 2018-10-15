package ncr.res.mobilepos.constant;
/**
 * Server Types enum for SERVERTYPE on Windows Environment Variables.
 */
public enum ServerTypes {
	ENTERPRISE("ENTERPRISE"), STORE("STORE");

	/**
	 * Server type.
	 */
	private final String serverType;

	/**
	 * ServerType constructor.
	 * @param serverType
	 */
	ServerTypes(String serverType) {
		this.serverType = serverType;
	}

	/**
	 *
	 * @param serverType
	 * @return
	 */
	public boolean equalsIgnoreCase(String serverType) {
		return this.serverType.equalsIgnoreCase(serverType);
	}
}
