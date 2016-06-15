package ncr.res.mobilepos.customerSearch.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@ApiModel(value="CustomerSearchReturnBean")
public class CustomerSearchReturnBean extends ResultBase {

    /** */
    private String strResultXml;

    /**
     *
     * @return
     */
    @ApiModelProperty(value="Žæ“¾Xml", notes="Žæ“¾Xml")
    public String getStrResultXml() {
        return strResultXml;
    }

    /**
     *
     * @param strResultXml
     */
    public void setStrResultXml(String strResultXml) {
        this.strResultXml = strResultXml;
    }

}
