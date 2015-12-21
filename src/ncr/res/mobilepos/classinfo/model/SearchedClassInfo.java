package ncr.res.mobilepos.classinfo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Class for the searched Classes.
 *<br>
 * Members:<br>
 * ResultCode   : Result code of the operation.<br>
 * ClassInfo[]       : Array of {@link ClassInfo} object.<br>
 *                But this only contains a single<br>
 *                ClassInfo as search result for a specific itemClass.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SearchedClassInfo")
public class SearchedClassInfo extends ResultBase {
    /**
     * Array of ClassInfo.
     */
    @XmlElement(name = "ClassInfos")
    private List<ClassInfo> classInfos;

    /**
     * Getter for array of ClassInfos.
     * @return array of ClassInfos
     */
    public final List<ClassInfo> getClassInfos(){
        return classInfos;
    }

    /**
     * Setter for array of ClassInfos.
     * @param classInfoToSet    Array of ClassInfos to set
     */
    public final void setClassInfos(final List<ClassInfo> classInfoToSet) {
        this.classInfos = classInfoToSet;
    }

    @Override
    public final String toString() {
        int noOfClassInfos = 0;
        if (null != this.classInfos) {
            noOfClassInfos = classInfos.size();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("ClassInfos count: " + noOfClassInfos);
        return sb.toString();
    }

}
