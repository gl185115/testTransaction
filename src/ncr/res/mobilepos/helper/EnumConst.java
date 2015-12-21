package ncr.res.mobilepos.helper;

public class EnumConst {

    public enum BarcodeType {
        UPC_A(0),
        UPC_E(1),
        JAN13(2),
        JAN8(3),
        CODE39(4),
        INTERLEAVED2OF5(5),
        CODEBAR(6),
        PDF417(10);

        private Integer val;

        private BarcodeType(Integer para) {

            this.val = para;
        }

        public Integer getValue() {

            return val;
        }
    }

    public enum XmlTag {
     // 2014/07/09 bmp‚Ì“Y‰Á‚ğ‘Î‰‚·‚éBSTART
        LineItems,
        Line,
        Item,
        Lable,
        Price,
        Sign,
        Condition,
        Date,
        Barcode,
        eq,
        ne,
        gt,
        lt,
        ge,
        le,
        LineImageBmp,
        Underline;
     // 2014/07/09 bmp‚Ì“Y‰Á‚ğ‘Î‰‚·‚éBEND
    }

    public enum XmlAlign {
        left(0),
        center(1),
        right(2);

        private Integer val;

        private XmlAlign(Integer para) {

            this.val = para;
        }

        public Integer getValue() {

            return val;
        }
    }
}
