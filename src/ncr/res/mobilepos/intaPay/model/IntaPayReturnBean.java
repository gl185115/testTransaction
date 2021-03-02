package ncr.res.mobilepos.intaPay.model;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

@ApiModel(value = "IntaPayReturnBean")
public class IntaPayReturnBean extends ResultBase {

    /** */
    private String responseJSON;

//    private String requestJSON;
    /**
     * @return the responseJSON
     */
    public String getResponseJSON() {
        return responseJSON;
    }
    /**
     * @param responseJSON the responseJSON to set
     */
    public void setResponseJSON(String responseJSON) {
        this.responseJSON = responseJSON;
    }
//    /**
//     * @return the requestJSON
//     */
//    public String getRequestJSON() {
//        return requestJSON;
//    }
//    /**
//     * @param requestJSON the requestJSON to set
//     */
//    public void setRequestJSON(String requestJSON) {
//        this.requestJSON = requestJSON;
//    }
}
