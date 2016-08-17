package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

public class GroupMixMatchData extends MixMatchData {
	
	/** MixMatch sub codes. */
	private List <String> subCodes;
	
	/**
	 * 
	 */
	public GroupMixMatchData() {
		super();
		this.subCodes = new ArrayList<String>();
	}
	
	/**
	 * @return the subCodes
	 */
	public final List<String> getSubCodes() {
		return subCodes;
	}

	/**
	 * @param subCodes the subCodes to set
	 */
	public final void setSubCodes(List<String> subCodes) {
		this.subCodes = subCodes;
	}

}
