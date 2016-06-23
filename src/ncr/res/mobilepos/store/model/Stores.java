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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;
/**
 * Model Class containing the list of stores.
 * @author CC185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Stores")
@XmlSeeAlso(Store.class)
@ApiModel(value="Stores")
public class Stores extends ResultBase {
    /**
     * The List of stores.
     */
    @XmlElementWrapper(name = "StoreList")
    @XmlElementRef
    private List<Store> storelist;

    /**
     * Get the list of stores.
     * @return  The List of stores.
     */
    @ApiModelProperty(value="ストアリスト", notes="ストアリスト")
    public final List<Store> getStorelist() {
        return storelist;
    }

    /**
     * Set the list of stores.
     * @param newstorelist The new List of stores
     */
    public final void setStorelist(final List<Store> newstorelist) {
        this.storelist = newstorelist;
    }

    @Override
    public final String toString() {
        int storeCount = 0;
        if (null != storelist) {
            storeCount = storelist.size();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Number of Stores: ").append(storeCount);
        return sb.toString();
    }
}
