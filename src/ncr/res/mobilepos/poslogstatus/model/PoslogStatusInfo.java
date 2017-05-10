package ncr.res.mobilepos.poslogstatus.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * PoslogStatus Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PoslogStatusInfo")
@ApiModel(value="PoslogStatusInfo")
public class PoslogStatusInfo extends ResultBase{

	/**
     * Count of consolidation result.
     */
    @XmlElement(name = "consolidationResult")
	private int consolidationResult;
    /**
     * Count of transfer result.
     */
    @XmlElement(name = "transferResult")
	private int transferResult;
	
    /**
     * Gets the count of consolidation result.
     *
     * @return consolidation result count.
     */
    @ApiModelProperty( value="consolidationŒ‹‰Ê", notes="consolidationŒ‹‰Ê")
    public final int getConsolidationResult() {
		return consolidationResult;
	}
    
    /**
     * Sets the count of consolidation result.
     *
     * @param consolidationResultToSet consolidationResult.
     */
	public final void setConsolidationResult(int consolidationResult) {
		this.consolidationResult = consolidationResult;
	}
	
	/**
     * Gets the count of transfer result.
     *
     * @return transfer result count.
     */
	@ApiModelProperty( value="transferŒ‹‰Ê", notes="transferŒ‹‰Ê")
	public final int getTransferResult() {
		return transferResult;
	}
	
	/**
     * Sets the count of transfer result.
     *
     * @param transferResultToSet transferResult.
     */
	public final void setTransferResult(int transferResult) {
		this.transferResult = transferResult;
	}
    
	@Override
    public final String toString() {
      StringBuilder str = new StringBuilder();
      String clrf = "; ";
      str.append("ConsolidationResult: ").append(consolidationResult).append(clrf)
            .append("TransferResult: ").append(transferResult).append(clrf);
      return str.toString();
    }
}
