/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* Stores
*
* Is a Class for containing a list of store information.
*
*/
package ncr.res.mobilepos.store.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
/**
 * Model Class containing the list of CM Preset Info.
 * @author EA185055
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CMPresetInfos")
@XmlSeeAlso(CMPresetInfo.class)
@ApiModel(value="CMPresetInfos")
public class CMPresetInfos extends ResultBase {
    @XmlElementWrapper(name = "CMPresetInfoList")
    @XmlElementRef
    private List<CMPresetInfo> cmPresetInfoList;

    @ApiModelProperty(value="cmプリセット情報リスト", notes="cmプリセット情報リスト")
    public final List<CMPresetInfo> getCMPresetInfoList() {
        return cmPresetInfoList;
    }

    public final void setCMPresetInfoList(final List<CMPresetInfo> newCMPresetInfoList) {
        this.cmPresetInfoList = newCMPresetInfoList;
    }

    @Override
    public final String toString() {
        int cmPresetInfoCount = 0;
        if (null != cmPresetInfoList) {
            cmPresetInfoCount = cmPresetInfoList.size();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Number of CM Preset Info: ").append(cmPresetInfoCount);
        return sb.toString();
    }
}
