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
    @ApiModelProperty(value="�擾Xml", notes="�擾Xml")
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
