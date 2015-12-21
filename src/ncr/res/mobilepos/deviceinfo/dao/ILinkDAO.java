package ncr.res.mobilepos.deviceinfo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;

/**
 * The Interface ILinkDAO.
 */
public interface ILinkDAO {

    /**
     * Creates the link.
     *
     * @param storeid the storeid
     * @param posLinkId the pos link id
     * @param posLinkInfo the pos link info
     * @return the result base
     * @throws Exception the exception
     */
    ResultBase createLink(String storeid, String posLinkId,
            POSLinkInfo posLinkInfo) throws Exception;

    /**
     * Delete link.
     * @param storeid the storeid
     * @param posLinkId the pos link id
     * @param appId the application ID
     * @param opeCode the operator code
     * @return the result base
     * @throws Exception the exception
     */
    ResultBase deleteLink(String storeid, String posLinkId, String appId, String opeCode) throws Exception;

    /**
     * Gets the link item.
     *
     * @param retailstoreid the retailstoreid
     * @param poslinkid the poslinkid
     * @return the link item
     * @throws SQLException the sQL exception
     * @throws DaoException the dao exception
     */
    POSLinkInfo getLinkItem(String retailstoreid, String poslinkid)
            throws Exception;

    /**
     * Update link.
     *
     * @param storeID the store id
     * @param posLinkID the pos link id
     * @param posLinkInfo the pos link info
     * @param connection the connection for sql query
     * @return the view pos link info
     * @throws DaoException the dao exception
     * @throws SQLException 
     */
    ViewPosLinkInfo updateLink(String storeID, String posLinkID,
            POSLinkInfo posLinkInfo, Connection connection) throws Exception;

    /**
     * Gets the links.
     *
     * @param retailstoreid the retailstoreid
     * @param limit 
     * @param name 
     * @param key 
     * @return the links
     * @throws SQLException the sQL exception
     * @throws DaoException the dao exception
     */
    List<POSLinkInfo> getLinks(String retailstoreid, String key, String name, int limit) throws Exception;
}
