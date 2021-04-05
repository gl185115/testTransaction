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
     * PickListImage������������B
     */
    public PickListImage() {
        fileName = "";
        contents = "";
    }

    /**
     * �t�@�C�������擾����B
     * @return �t�@�C����
     */
    @ApiModelProperty(value = "�t�@�C����", notes = "�t�@�C����")
    public String getFileName() {
        return fileName;
    }

    /**
     * �t�@�C������ݒ肷��B
     * @param fileName �t�@�C����
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * �t�@�C�����e���擾����B
     * @return �t�@�C�����e
     */
    @ApiModelProperty(value = "�t�@�C�����e", notes = "�t�@�C�����e")
    public String getContents() {
        return contents;
    }

    /**
     * �t�@�C�����e��ݒ肷��B
     * @param contents �t�@�C�����e
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
