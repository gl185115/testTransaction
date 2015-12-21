/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * ICredentialDAO
 *
 * Is a DAO interface for Operator Sign ON/OFF.
 *
 * Menesses, Chris Niven
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.credential.dao;

import java.util.List;
import ncr.res.mobilepos.credential.model.UserGroup;
import ncr.res.mobilepos.credential.model.UserGroupLabel;
import ncr.res.mobilepos.credential.model.ViewUserGroup;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;

/**
 * ICreadentialDAO
 *
 * A Data Access Object implementation for Operator Sign ON/OFF.
 */
public interface IGroupDAO {

    /**
     * Creates a new group.
     * @param groupCode
     *            The code attributed to the group.
     * @param group
     *            The UserGroup Model that holds details of the new group.
     * @return Error code if error occurred otherwise 0
     * @throws DaoException
     *             Thrown when error occurs.
     */
    ResultBase createGroup(int groupCode, UserGroup group) throws DaoException;

    /**
     * List Groups.
     *
     * @param key
     *            The key to search. If empty, it will list all groups. If
     *            numeric, it will look up for the common base of group
     *            name.
     * @return The List of groups
     * @throws DaoException
     *             The DaoException when the method fails
     */
    List<UserGroupLabel> listGroups(String key)
            throws DaoException;

    /**
     * Gets group detail.
     *
     * @param groupCode
     *            The code attributed to the group.
     * @return UserGroup The Object containing Result Code and User Group
     *         details.
     * @throws DaoException
     *             Thrown when DaoException occurs.
     */
    ViewUserGroup viewGroupDetail(int groupCode) throws DaoException;

    /**
     * Updates group details.
     *
     * @param groupCode
     *            The code attributed to the group.
     * @param groupDetails
     *            a UserGroup class that contains the new values.
     * @return UserGroup The group details with resultcode.
     * @throws DaoException
     *             Thrown when error occurs.
     */
    ViewUserGroup updateGroup(int groupCode, UserGroup groupDetails
            ) throws DaoException;

    /**
     * Deletes group detail.
     *
     * @param groupCode
     *            The code attributed to the group.
     * @return ResultBase The Object that contains result code.
     * @throws DaoException
     *             Thrown when exception occurs.
     */
    ResultBase deleteGroup(int groupCode)
            throws DaoException;

}
