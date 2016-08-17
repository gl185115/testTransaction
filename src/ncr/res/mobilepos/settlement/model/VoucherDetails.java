package ncr.res.mobilepos.settlement.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "VoucherDetails")
@ApiModel(value="VoucherDetails")
public class VoucherDetails {

        /**
         * private variable that will store the value of company id
         */
        @XmlElement(name = "CompanyId")
        private String companyId;
        
        /**
         * private variable that will store the value of store id
         */
        @XmlElement(name = "StoreId")
        private String storeId;
        
        /**
         * private variable that will store the value of data type
         */
        @XmlElement(name = "DataType")
        private String dataType;
        
        /**
         * private variable that will store the value of item level 1
         */
        @XmlElement(name = "ItemLevel1")
        private String itemLevel1;
        
        /**
         * private variable that will store the value of item level 2
         */
        @XmlElement(name = "ItemLevel2")
        private String itemLevel2;
        
        /**
         * private variable that will store the value of item level 3
         */
        @XmlElement(name = "ItemLevel3")
        private String itemLevel3;
        
        /**
         * private variable that will store the value of item level 4
         */
        @XmlElement(name = "ItemLevel4")
        private String itemLevel4;
        
           
        /**
         * private variable that will store the value of workstation id
         */
        @XmlElement(name = "WorkStationId")
        private String workStationId;
        
        
        /**
         * private variable that will store the value of Business day date
         */
        @XmlElement(name = "BusinessDayDate")
        private String businessDayDate;
        
        /**
         * private variable that will store the value of item count
         */
        @XmlElement(name = "ItemCount")
        private int itemCount;
        
        /**
         * private variable that will store the value of item amount
         */
        @XmlElement(name = "ItemAmt")
        private String itemAmt;

        /**
         * @return the companyId
         */
        @ApiModelProperty(value="会社コード", notes="会社コード")
        public String getCompanyId() {
            return companyId;
        }

        /**
         * @param companyId the companyId to set
         */
        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        /**
         * @return the storeId
         */
        @ApiModelProperty(value="店舗番号", notes="店舗番号")
        public String getStoreId() {
            return storeId;
        }

        /**
         * @param storeId the storeId to set
         */
        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        /**
         * @return the dataType
         */
        @ApiModelProperty(value="データ種別", notes="データ種別")
        public String getDataType() {
            return dataType;
        }

        /**
         * @param dataType the dataType to set
         */
        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        /**
         * @return the itemLevel1
         */
        @ApiModelProperty(value="項目レベル１", notes="項目レベル１")
        public String getItemLevel1() {
            return itemLevel1;
        }

        /**
         * @param itemLevel1 the itemLevel1 to set
         */
        public void setItemLevel1(String itemLevel1) {
            this.itemLevel1 = itemLevel1;
        }

        /**
         * @return the itemLevel2
         */
        @ApiModelProperty(value="項目レベル２", notes="項目レベル２")
        public String getItemLevel2() {
            return itemLevel2;
        }

        /**
         * @param itemLevel2 the itemLevel2 to set
         */
        public void setItemLevel2(String itemLevel2) {
            this.itemLevel2 = itemLevel2;
        }

        /**
         * @return the itemLevel3
         */
        @ApiModelProperty(value="項目レベル３", notes="項目レベル３")
        public String getItemLevel3() {
            return itemLevel3;
        }

        /**
         * @param itemLevel3 the itemLevel3 to set
         */
        public void setItemLevel3(String itemLevel3) {
            this.itemLevel3 = itemLevel3;
        }

        /**
         * @return the itemLevel4
         */
        @ApiModelProperty(value="項目レベル４", notes="項目レベル４")
        public String getItemLevel4() {
            return itemLevel4;
        }

        /**
         * @param itemLevel4 the itemLevel4 to set
         */
        public void setItemLevel4(String itemLevel4) {
            this.itemLevel4 = itemLevel4;
        }

        /**
         * @return the workStationId
         */
        @ApiModelProperty(value="POSコード", notes="POSコード")
        public String getWorkStationId() {
            return workStationId;
        }

        /**
         * @param workStationId the workStationId to set
         */
        public void setWorkStationId(String workStationId) {
            this.workStationId = workStationId;
        }

        /**
         * @return the businessDayDate
         */
        @ApiModelProperty(value="営業日", notes="営業日")
        public String getBusinessDayDate() {
            return businessDayDate;
        }

        /**
         * @param businessDayDate the businessDayDate to set
         */
        public void setBusinessDayDate(String businessDayDate) {
            this.businessDayDate = businessDayDate;
        }

        /**
         * @return the itemCount
         */
        @ApiModelProperty(value="商品数", notes="商品数")
        public int getItemCount() {
            return itemCount;
        }

        /**
         * @param itemCount the itemCount to set
         */
        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        /**
         * @return the itemAmt
         */
        @ApiModelProperty(value="商品金額", notes="商品金額")
        public String getItemAmt() {
            return itemAmt;
        }

        /**
         * @param itemAmt the itemAmt to set
         */
        public void setItemAmt(String itemAmt) {
            this.itemAmt = itemAmt;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "VoucherDetails [companyId=" + companyId + ", storeId=" + storeId + ", dataType=" + dataType
                    + ", itemLevel1=" + itemLevel1 + ", itemLevel2=" + itemLevel2 + ", itemLevel3=" + itemLevel3
                    + ", itemLevel4=" + itemLevel4 + ", workStationId=" + workStationId + ", businessDayDate="
                    + businessDayDate + ", itemCount=" + itemCount + ", itemAmt=" + itemAmt + "]";
        }
        
        

}
