package ncr.res.mobilepos.uiconfig.model.fileInfo;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "PictureInfoUpload")
@XmlAccessorType(XmlAccessType.NONE)
public class PictureInfoUpload extends ResultBase{
	
	@XmlElement(name = "image")
	private String image;

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

        sb.append("image: ").append(image);
		
		return sb.toString();
	}
}
