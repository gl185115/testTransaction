package ncr.res.mobilepos.mastersync.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "pickListImage")
@ApiModel("PickListImage")
public class PickListImage {
    @XmlElement(name = "fileName")
    private String fileName;

    @XmlElement(name = "contents")
    private String contents;

    /**
     * PickListImageを初期化する。
     */
    public PickListImage() {
        fileName = "";
        contents = "";
    }

    /**
     * ファイル名を取得する。
     * @return ファイル名
     */
    @ApiModelProperty(value = "ファイル名", notes = "ファイル名")
    public String getFileName() {
        return fileName;
    }

    /**
     * ファイル名を設定する。
     * @param fileName ファイル名
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * ファイル内容を取得する。
     * @return ファイル内容
     */
    @ApiModelProperty(value = "ファイル内容", notes = "ファイル内容")
    public String getContents() {
        return contents;
    }

    /**
     * ファイル内容を設定する。
     * @param contents ファイル内容
     */
    public void setContents(final String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("fileName: ").append(getFileName());
        builder.append("; contents: ").append(getContents());

        return builder.toString();
    }
}
