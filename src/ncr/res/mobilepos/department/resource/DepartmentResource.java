package ncr.res.mobilepos.department.resource;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.dao.IDepartmentDAO;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
/**
 * ���藚��
 * �o�[�W����         ������t       �S���Җ�           ������e
 * 1.01               2014.12.11     LiQian             DIV���݃`�F�b�N��Ή�
 */

/**
 *
 * @author AP185142
 * @author RD185102
 */
@Path("/departmentinfo")
@Api(value="/departmentinfo", description="������API")
public class DepartmentResource {
	/**
     * context.
     */
    @Context
    private ServletContext context;
    /**
     * context.
     */
    @Context
    private SecurityContext securityContext;
    /**
     * logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * dao factory.
     */
    private DAOFactory daoFactory;
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    
    /**
     * constructor.
     */
    public DepartmentResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Sets the DaoFactory of the DepartmentResource to use the DAO methods.
     *
     * @param daofactory
     *            - The new value for the DAO Factory
     */
    public final void setDaoFactory(final DAOFactory daofactory) {
        this.daoFactory = daofactory;
    }
    
    /**
     * @param contextToSet
     *            the context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * Retrieves active department.
     *
     * @param retailStoreID
     *            - Path parameter for store identifier
     * @param departmentID
     *            - Path parameter for department identifier
     * @return a JSON string of the Department Details.
     */

    @Path("/detail")
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @ApiOperation(value="�L���ȕ��������������", response=ViewDepartment.class)
    @ApiResponses(value={
        @ApiResponse(code=ResultBase.RES_ERROR_DPTNOTFOUND, message="���傪�݂���Ȃ�"),
        @ApiResponse(code=ResultBase.RES_ERROR_DB, message="�f�[�^�x�[�X�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="�ėp�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAO�G���["),
        @ApiResponse(code=ResultBase.RES_ERROR_INVALIDPARAMETER, message="�����ȃp�����[�^"),
    })
    public final ViewDepartment selectDepartmentDetail(
    		@ApiParam(name="companyid", value="��ЃR�[�h") @QueryParam("companyid") final String companyID,
    		@ApiParam(name="retailstoreid", value="�X�܃R�[�h") @QueryParam("retailstoreid") final String retailStoreID,
    		@ApiParam(name="departmentid", value="����R�[�h") @QueryParam("departmentid") final String departmentID) {

        tp.methodEnter("selectDepartmentDetail");
        tp.println("CompanyID", companyID)
          .println("RetailStoreID", retailStoreID)
          .println("DepartmentID", departmentID);

        ViewDepartment dptModel = new ViewDepartment();
        if (StringUtility.isNullOrEmpty(companyID, retailStoreID, departmentID)) {
            dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            tp.methodExit(dptModel.toString());
            return dptModel;
        }
        
        try {
            IDepartmentDAO iDptDao = daoFactory.getDepartmentDAO();
            dptModel = iDptDao
                    .selectDepartmentDetail(companyID, retailStoreID, departmentID);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    "DepartmentRes",
                    "DepartmentResource.selectDepartmentDetail",
                    Logger.RES_EXCEP_DAO,
                    "Failed to get the Department Details.\n"
                            + daoEx.getMessage());
            if (daoEx.getCause() instanceof SQLException) {
                  dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            } else {
                  dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(
                    "DepartmentRes",
                    "DepartmentResource.selectDepartmentDetail",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to view Department " + departmentID + ": "
                            + ex.getMessage());
            dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(dptModel);
        }
        return dptModel;
    }
}
