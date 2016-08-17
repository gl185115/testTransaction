package ncr.res.mobilepos.line.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Line for the searched Lines.
 *<br>
 * Members:<br>
 * ResultCode   : Result code of the operation.<br>
 * Line[]       : Array of {@link Line} object.<br>
 *                But this only contains a single<br>
 *                Line as search result for a specific line.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SearchedLine")
@ApiModel(value="SearchedLine")
public class SearchedLine extends ResultBase {
    /**
     * Array of Line.
     */
    @XmlElement(name = "Lines")
    private List<Line> lines;

    /**
     * Getter for array of Lines.
     * @return array of Lines
     */
    @ApiModelProperty( value="ïiéÌèÓïÒ", notes="ïiéÌèÓïÒ")
    public final List<Line> getLines(){
        return lines;
    }

    /**
     * Setter for array of Lines.
     * @param lineToSet    Array of Lines to set
     */
    public final void setLines(final List<Line> lineToSet) {
        this.lines = lineToSet;
    }

    @Override
    public final String toString() {
        int noOfLines = 0;
        if (null != this.lines) {
        	noOfLines = lines.size();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Lines count: " + noOfLines);
        return sb.toString();
    }

}
