package ncr.res.mobilepos.simpleprinterdriver;
/**
 * BmpFileHeader class contains information about the type, size, and layout
 * of a bitmap file.
 *
 */
public class BmpFileHeader {

    /**
     * Bitmap file type.
     */
    private byte[] bfType = new byte[2];
    /**
     * Bitmap file size.
     */
    private byte[] bfSize = new byte[4];
    /**
     * Reserved bitmap file 1.
     */
    private byte[] bfReserved1 = new byte[2];
    /**
     * Reserved bitmap file 2.
     */
    private byte[] bfReserved2 = new byte[2];
    /**
     * Bitmap filee off bits.
     */
    private byte[] bfOffBits = new byte[4];

    /**
     * Bitmap file header data.
     */
    private byte[] bfHeaderData;

    /**
     * Constructor.
     * @param header    Header data.
     */
    public BmpFileHeader(final byte[] header) {
        bfHeaderData = header.clone();
        bfType = subByteArray(0x00, 2);
        bfSize = subByteArray(0x02, 4);
        bfReserved1 = subByteArray(0x06, 2);
        bfReserved2 = subByteArray(0x08, 2);
        bfOffBits = subByteArray(0x0A, 4);
    }
    /**
     * Getter for bitmap file type.
     * @return  bitmap file type
     */
    public final byte[] getBfType() {
        return bfType.clone();
    }

    /**
     * Getter for bitmap file size.
     * @return  bitmap file size
     */
    public final byte[] getBfSize() {
        return bfSize.clone();
    }

    /**
     * Getter for reserved bitmap file 1.
     * @return  reserved bitmap file 1
     */
    public final byte[] getBfReserved1() {
        return bfReserved1.clone();
    }
    /**
     * Getter for reserved bitmap file 2.
     * @return  reserved bitmap file 2
     */
    public final byte[] getBfReserved2() {
        return bfReserved2.clone();
    }
    /**
     * Getter for bitmap file offbits.
     * @return  bitmap file offbits
     */
    public final byte[] getBfOffBits() {
        return bfOffBits.clone();
    }

    /**
     * Getter for bitmap file size.
     * @return  bitmap file size
     */
    public final int getBfSizeInt() {
        return convertToIntSize(bfSize);
    }

    /**
     * Getter for bitmap file offbits converted to size.
     * @return      bitmap file offbits converted to size
     */
    public final int getBfOffBitsInt() {
        return convertToIntSize(bfOffBits);
    }
    /**
     * Copies data from file header data.
     * @param index     Array index
     * @param digits    Length
     * @return          Result copied from object source
     */
    private byte[] subByteArray(final int index, final int digits) {
        byte[] buffer = new byte[digits];

        System.arraycopy(bfHeaderData, index, buffer, 0, digits);

        return buffer;
    }

    /**
     * Method that converts data to size.
     * @param data      Data to be converted
     * @return          Size
     */
    private int convertToIntSize(final byte[] data) {
        int size = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            size = size + (data[i] & 0xFF) * (int) Math.pow(256, i);
        }
        return size;
    }
}
