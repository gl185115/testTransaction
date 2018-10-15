package ncr.res.mobilepos.helper;

import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.model.ResultBase;

/**
 * class that handles checking of restrictions.
 *
 */
public final class ApiRestriction {
    /** Default Constructor. */
    private ApiRestriction() {    	
    }
    /**
     * constant for clear of restrictions.
     */
    public static final int OK = 5000;
    /**
     * constant for authentication restriction.
     */
    public static final int FAIL_AUTHENTICATION = 5001;
    /**
     * constant for credential restriction.
     */
    public static final int FAIL_SIGNON = 5002;
    /**
     * constant for failure due to abnormal operation.
     */
    public static final int FAIL_ABNORMALOPERATION = 5003;
    /**
     * constant for failure due to invalid parameters.
     */
    public static final int FAIL_INVALIDPARAMETERS = 5004;
    /**
     * constant for failure due to device not existing.
     */
    public static final int FAIL_DEVICENOTFOUND = 5005;
    /**
     * constant for failure due to credential not existing.
     */
    public static final int FAIL_OPERATORNOTFOUND = 5006;

    /**
     * checks the status of the operator.
     * @param operatorNo - the operator id to check
     * @return int - the appropriate result code that
     * describes the operator status
     */
    private static int checkSignon(final String operatorNo) {
        CredentialResource cr = new CredentialResource();
        ResultBase result = cr.getOperatorStatus(operatorNo);

        if (ResultBase.RESCREDL_OPERATOR_ONLINE
                == result.getNCRWSSResultCode()) {
            return OK;
        } else if (ResultBase.RESCREDL_OPERATOR_OFFLINE
                == result.getNCRWSSResultCode()) {
          //fail to sign on
            return FAIL_SIGNON;
        } else if (ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND
                == result.getNCRWSSResultCode()) {
          //operator cannot be found
            return FAIL_OPERATORNOTFOUND;
        }

        return FAIL_ABNORMALOPERATION; //unexpected error occurred

    }

    /**
     * gets the restriction code of the given terminal.
     * @param deviceNo - the terminal id to check
     * @return OK
     */
    public static int getRestriction(final String deviceNo) {
        return OK;
    }

    /**
     * gets the overall restriction code of the given terminal
     * and operator id. terminal restrictions are checked first then
     * operator restrictions.
     * @param deviceNo - the terminal id to check
     * @param operatorNo - the operator id to check
     * @return int - the appropriate restriction code
     */
    public static int getRestriction(
            final String deviceNo, final String operatorNo) {
        int result = OK;
        if (OK != result) {
            return result;
        }

        result = checkSignon(operatorNo);
        return result;
    }


}
