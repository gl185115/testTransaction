/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthorizationResp
 *
 * Model Class for the lis of Credit Auythorization Responses
 *
 * Campos, Carlos
 */
package ncr.res.mobilepos.creditauthorization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class Model that contains the list of Credit Authorization Responses.
 *
 * @author cc185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CreditAuthorizationResps")
public class CreditAuthorizationResps extends ResultBase {

    /** The list credit authorization response. */
    @XmlElement(name = "CreditAuthorizationResp")
    private List<CreditAuthorizationResp> creditAuthorizationResp;

    /**
     * Gets the credit authorization response.
     *
     * @return the credit authorization response.
     */
    public final List<CreditAuthorizationResp> getCreditAutrhorizationResp() {
        return creditAuthorizationResp;
    }

    /**
     * Sets the credit authorization response.
     *
     * @param caResp
     *            the new credit authorization response
     */
    public final void setCreditAutrhorizationResp(
            final List<CreditAuthorizationResp> caResp) {
        this.creditAuthorizationResp = caResp;
    }
}
