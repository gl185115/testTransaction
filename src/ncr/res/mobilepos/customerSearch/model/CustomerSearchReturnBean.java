package ncr.res.mobilepos.customerSearch.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
