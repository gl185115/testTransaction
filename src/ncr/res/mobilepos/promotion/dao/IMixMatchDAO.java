package ncr.res.mobilepos.promotion.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.promotion.model.GroupMixMatchData;
import ncr.res.mobilepos.promotion.model.NormalMixMatchData;

/**
 * DAO Interface for MixMatch.
 * @author RD185102
 *
 */
public interface IMixMatchDAO {

	/**
	 * Retrieves Mix Match information identified by storeid and code from
	 * MST_MIXMATCH.
	 * 
	 * @param companyid
	 *            The Company ID
	 * @param storeid
	 *            The Store ID.
	 * @param code
	 *            The MixMatch Code for each item.
	 * @return The MixMatch Data.
	 * @throws DaoException
	 *             The exception thrown when getting the MixMatch Data failed.
	 */
	NormalMixMatchData getNormalMixMatchData(String companyid, String storeid, String code)
			throws DaoException;

	GroupMixMatchData getGroupMixMatchData(String companyid, String storeid, String code1OrCode2)
			throws DaoException;
}
