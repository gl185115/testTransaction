package ncr.res.mobilepos.networkreceipt.model;

public class EmuConst {

    public enum BarcodeType {
        UPC_A(0),
        UPC_E(1),
        JAN13(2),
        JAN8(3),
        CODE39(4),
        //(Interleaved Two of Five)
        INTERLEAVED2OF5(5),
        CODEBAR(6),
        //start modify by mlwang 2014/09/11
        CODE128(73),
        //end modify 2014/09/11
        PDF417(10);

        private Integer val;

        private BarcodeType(Integer para) {

            this.val = para;
        }

        public Integer getValue() {

            return this.val;
        }
    }

    public enum ReceiptType {
        NORMAL_CASH(0),
        NORMAL_CREDIT(1);

        private Integer val;

        private ReceiptType(Integer para) {
            this.val = para;
        }

        public Integer getValue() {
            return this.val;
        }
    }
}
