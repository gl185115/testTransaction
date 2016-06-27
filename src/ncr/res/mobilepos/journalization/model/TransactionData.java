/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * TransactionData
 *
 * Model Class for the TransactionData.
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * TransactionData Model Object.
 *
 * <P>Encapsulates Transaction's Receipt and POSLog XML.
 */
@XmlRootElement(name="TransactionData")
@ApiModel(value="TransactionData")
public class TransactionData extends ResultBase {
    /**
     * The Transaction POSLOg XML.
     */
	@XmlElement(name = "xmlData")
    private String xmlData;
    /**
     * The Transaction Receipt.
     */
	@XmlElement(name = "receiptData")
    private String receiptData;
    /**
     * The transaction Type.
     */
	@XmlElement(name = "txType")
    private String txType;

    /**
     * Default Constructor.
     */

    public TransactionData() {
    }

    /**
     * Custom Constructor.
     *
     * Sets the POSLog XML and Receipt for the class.
     *
     * @param xmlDataToSet      PosLog's xml format
     * @param receiptDataToSet  data to display in receipt
     */
    public TransactionData(
            final String xmlDataToSet, final String receiptDataToSet) {
        super();
        this.xmlData = xmlDataToSet;
        this.receiptData = receiptDataToSet;
    }
    /**
     * Gets the POSLog XML.
     * @return The POSLog XML.
     */
    @ApiModelProperty(value="xmlデータ", notes="xmlデータ")
    public final String getXmlData() {
        return xmlData;
    }
    /**
     * Sets the POSLog XML.
     * @param xmlDataToSet    The new POSLog XML value.
     */
    public final void setXmlData(final String xmlDataToSet) {
        this.xmlData = xmlDataToSet;
    }
    /**
     * Gets the Receipt.
     * @return The Receipt.
     */
    @ApiModelProperty(value="領収書のデータ", notes="領収書のデータ")
    public final String getReceiptData() {
        return receiptData;
    }
    /**
     * Sets the Receipt.
     * @param receiptDataToSet    The new Receipt value.
     */
    public final void setReceiptData(final String receiptDataToSet) {
        this.receiptData = receiptDataToSet;
    }
    /**
     * Gets the Transaction Type.
     * @return Return the Transaction Type.
     */
    @ApiModelProperty(value="電文種別", notes="電文種別")
    public final String getTxType() {
        return txType;
    }
    /**
     * Sets the Transaction Type.
     * @param txTypeToSet    The new Value for the Transaction Type.
     */
    public final void setTxType(final String txTypeToSet) {
        this.txType = txTypeToSet;
    }
}
