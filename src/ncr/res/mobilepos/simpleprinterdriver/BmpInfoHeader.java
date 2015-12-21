package ncr.res.mobilepos.simpleprinterdriver;

/**
 * BmpInfoHeader class specifies the dimensions, compression type, and color
 * format for the bitmap.
 *
 */
public class BmpInfoHeader {
    /**
     * Bitmap size.
     */
    private byte[] biSize = new byte[4];
    /**
     * Bitmap width.
     */
    private byte[] biWidth = new byte[4];
    /**
     * Bitmap height.
     */
    private byte[] biHeight = new byte[4];
    /**
     * Planes.
     */
    private byte[] biPlanes = new byte[2];
    /**
     * Count.
     */
    private byte[] biBitCount = new byte[2];
    /**
     * Bitmap compression.
     */
    private byte[] biCompression = new byte[4];
    /**
     * Bitmap image size.
     */
    private byte[] biSizeImage = new byte[4];
    /**
     * biXPelsPerMeter.
     */
    private byte[] biXPelsPerMeter = new byte[4];
    /**
     * biYPelsPerMeter.
     */
    private byte[] biYPelsPerMeter = new byte[4];
    /**
     * Number of bits and number of colors used in the bitmap.
     */
    private byte[] biClrUsed = new byte[4];
    /**
     * Number of bits and number of colors that are important in the bitmap.
     */
    private byte[] biClrImportant = new byte[4];
    /**
     * Bitmap Information Data.
     */
    private byte[] biInfoData;

    /**
     * Constructor.
     * @param infoHeader        Information Header
     */
    public BmpInfoHeader(final byte[] infoHeader) {
        biInfoData = infoHeader.clone();
        biSize = subByteArray(0, 4);
        biWidth = subByteArray(4, 4);
        biHeight = subByteArray(8, 4);
        biPlanes = subByteArray(12, 2);
        biBitCount = subByteArray(14, 2);
        biCompression = subByteArray(16, 4);
        biSizeImage = subByteArray(20, 4);
        biXPelsPerMeter = subByteArray(24, 4);
        biYPelsPerMeter = subByteArray(28, 4);
        biClrUsed = subByteArray(32, 4);
        biClrImportant = subByteArray(36, 4);
    }
    /**
     * Getter for Bitmap size.
     * @return  bitmap size
     */
    public final byte[] getBiSize() {
        return biSize.clone();
    }
    /**
     * Getter for BiSizeInt.
     * @return  BiSize converted to size.
     */
    public final int getBiSizeInt() {
        return convertToIntSize(biSize);
    }
    /**
     * Getter for bitmap width.
     * @return  bitmap width
     */
    public final byte[] getBiWidth() {
        return biWidth.clone();
    }
    /**
     * Getter for converted bitmap width.
     * @return  converted bitmap width
     */
    public final int getBiWidthInt() {
        return convertToIntSize(biWidth);
    }
    /**
     * Getter for bitmap height.
     * @return bitmap height.
     */
    public final byte[] getBiHeight() {
        return biHeight.clone();
    }
    /**
     * Getter for converted bitmap height.
     * @return  converted bitmap height
     */
    public final int getBiHeightInt() {
        return convertToIntSize(biHeight);
    }
    /**
     * Bitmap planes.
     * @return  bitmap planes
     */
    public final byte[] getBiPlanes() {
        return biPlanes.clone();
    }
    /**
     * Getter for count.
     * @return  count
     */
    public final byte[] getBiBitCount() {
        return biBitCount.clone();
    }
    /**
     * Bitmap compression.
     * @return  bitmap compression
     */
    public final byte[] getBiCompression() {
        return biCompression.clone();
    }
    /**
     * Bitmap image size.
     * @return bitmap image size
     */
    public final byte[] getBiSizeImage() {
        return biSizeImage.clone();
    }
    /**
     * Getter for converted bitmap image size.
     * @return  converted bitmap image size
     */
    public final int getBiSizeImageInt() {
        return convertToIntSize(biSizeImage);
    }
    /**
     * Getter for biXPelsPerMeter.
     * @return  biXPelsPerMeter
     */
    public final byte[] getBiXPelsPerMeter() {
        return biXPelsPerMeter.clone();
    }
    /**
     * Getter for biYPelsPerMeter.
     * @return  biYPelsPerMeter
     */
    public final byte[] getBiYPelsPerMeter() {
        return biYPelsPerMeter.clone();
    }
    /**
     * Getter for biClrUsed.
     * @return  biClrUsed
     */
    public final byte[] getBiClrUsed() {
        return biClrUsed.clone();
    }
    /**
     * Getter for biClrImportant.
     * @return  biClrImportant
     */
    public final byte[] getBiClrImportant() {
        return biClrImportant.clone();
    }
    /**
     * Copies the Bitmap Information Data.
     * @param index     Bitmap Information data index position
     * @param digits    Length
     * @return          Copy of array of bitmap information data
     */
    private byte[] subByteArray(final int index, final int digits) {
        byte[] buffer = new byte[digits];

        System.arraycopy(biInfoData, index, buffer, 0, digits);

        return buffer;
    }
    /**
     * Converts to bitmap size.
     * @param data  Data to be converted
     * @return      converted size
     */
    private int convertToIntSize(final byte[] data) {
        int size = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            size = size + (data[i] & 0xFF)
            * (int) Math.pow(256, i);
        }
        return size;
    }
}
