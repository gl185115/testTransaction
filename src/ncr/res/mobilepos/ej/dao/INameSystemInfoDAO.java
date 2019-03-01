package ncr.res.mobilepos.ej.dao;

import java.util.List;

import ncr.res.mobilepos.ej.model.EjInfo;
import ncr.res.mobilepos.ej.model.PosLogInfo;
import ncr.res.mobilepos.exception.DaoException;

public interface INameSystemInfoDAO {
	/**
	 * Get System Name Info from MST_NAME_SYSTEM
	 * @param void
	 * @return List<EjInfo>
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	List<EjInfo> getNameSystemInfo() throws DaoException;

	/**
	 * Get NameCategory from PRM_SYSTEM_CONFIG
	 * @param Category
	 * @param KeyId
	 * @return NameCategory
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	String getNameCategory(String Category, String KeyId) throws DaoException;

	/**
	 * Get List EJ Info
	 *
	 * @param CompanyId
	 * @param RetailStoreId
	 * @param WorkstationId
	 * @param TxType
	 * @param SequencenumberFrom
	 * @param SequencenumberTo
	 * @param BusinessDateTimeFrom
	 * @param BusinessDateTimeTo
	 * @param OperatorId
	 * @param SalesPersonId
	 * @param TrainingFlag
	 *
	 * @return List<EjInfo>
	 *
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	List<EjInfo> getEjInfo(String CompanyId, String RetailStoreId, String WorkstationId, String TxType, String SequencenumberFrom, String SequencenumberTo,
			String BusinessDateTimeFrom, String BusinessDateTimeTo, String OperatorId, String SalesPersonId, String TrainingFlag) throws DaoException;

	/**
	 * Get PosLog Info
	 *
	 * @param CompanyId
	 * @param RetailStoreId
	 * @param WorkstationId
	 * @param Sequencenumber
	 * @param BusinessDateTime
	 * @param TrainingFlag
	 *
	 * @return PosLogInfo
	 *
	 * @throws DaoException
	 *             The exception thrown when searching failed.
	 */
	PosLogInfo getPosLogInfo(String CompanyId, String RetailStoreId, String WorkstationId, String Sequencenumber, String BusinessDateTime, String TrainingFlag) throws DaoException;
}
