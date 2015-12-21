package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlRootElement;
import ncr.res.mobilepos.model.ResultBase;

/**
 * The Class CARequestResult.
 */
@XmlRootElement(name = "CARequestResult")
public class CARequestResult {

    /** The credit authorization response. */
    private CreditAuthorizationResp creditAuthorizationResp;

    /** The result base. */
    private ResultBase result;

    /**
     * Instantiates a new cA request result.
     */
    public CARequestResult() {
    }

    /**
     * Instantiates a new cA request result.
     *
     * @param caResp
     *            the credit authorization response
     * @param resultBase
     *            the result
     */
    public CARequestResult(
            final CreditAuthorizationResp caResp,
            final ResultBase resultBase) {
        this.creditAuthorizationResp = caResp;
        this.result = resultBase;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public final ResultBase getResult() {
        return result;
    }

    /**
     * Sets the result.
     *
     * @param value
     *            the new result
     */
    public final void setResult(final ResultBase value) {
        this.result = value;
    }

    /**
     * Gets the credit authorization response.
     *
     * @return the credit authorization response
     */
    public final CreditAuthorizationResp getCreditAuthorizationResp() {
        return creditAuthorizationResp;
    }

    /**
     * Sets the credit authorization response.
     *
     * @param value
     *            the new credit authorization response
     */
    public final void setCreditAuthorizationResp(
            final CreditAuthorizationResp value) {
        this.creditAuthorizationResp = value;
    }
}
