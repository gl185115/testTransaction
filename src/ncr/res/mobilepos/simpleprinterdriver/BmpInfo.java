package ncr.res.mobilepos.simpleprinterdriver;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that encapsulates Bitmap Information.
 *
 */
public class BmpInfo {

    /**
     * Bitmap information header.
     */
    private BmpInfoHeader bmpInfoHeader;
    /**
     * Bitmap file header.
     */
    private BmpFileHeader bmpFileHeader;
    /**
     * Bitmap information.
     */
    private byte[] bmpData;
    /**
     * Bitmap line data length.
     */
    private int bmpLineDataLen = 0;

    /** Bitmap PrintCommand 1. */
    private static final byte BMP_PRINT_CMD1 = 0x1B;
    /** Bitmap PrintCommand 2. */
    private static final byte BMP_PRINT_CMD2 = 0x2E;
    /** Bitmap PrintCommand 3. */
    private static final byte BMP_PRINT_CMD3 = 0x00;
    /** Bitmap PrintCommand 4. */
    private static final byte BMP_PRINT_CMD4 = 0x35;
    /** Bitmap PrintCommand 5. */
    private static final byte BMP_PRINT_CMD5 = 0x01;
    /** Bitmap PrintCommand 6. */
    private static final byte BMP_PRINT_CMD6 = 0x00;
    /** Variable use to check first buffer Bitmap. */
    private static final byte BIT = 0x42;
    /** Variable use to check second buffer Bitmap. */
    private static final byte MAP = 0x4D;
    /** The key for Transfer for Port. */
    private byte TRANSFER_FOR_PORT_KEY = (byte) 0xFF;
    /** A four valued constant. */
    private static final int FOUR_INDEX = 4;
    /** An eight valued constant. */
    private static final int EIGHT_INDEX = 8;
    /** A 256Color size. */
    private static final int COLOR_SIZE_256 = 256;
    /** The max width of bitmap size that BT printer can read. */
    private static final int PRIFLEX_MAX_WIDTH = 48;
    public static final int BT_FILE_HEADER_LEN = 14;
    /**
     * Flag of 53mm receipt paper.
     */
    public static final int FLG_PAPER53 = 1;
    /**
     * Flag of 80mm receipt paper.
     */
    public static final int FLG_PAPER80 = 2;
    /**
     * The width of 53mm receipt paper.
     */
    public static final int PAPER_WIDTH_53MM = 0x35;
    /**
     * The width of 80mm receipt paper.
     */
    public static final int PAPER_WIDTH_80MM = 0x48;
    /**
     * The digits of Info Header of bitmap.
     */
    public static final int BT_INFO_HEADER_DIGIT = 0x28;
    /**
     * The start position of info header of bitmap.
     */
    public static final int BT_INFO_HEADER_POS = 0x0E;

    /** 1画素あたりのビット数 */
    public static enum BI_BIT_COUNT {
        COLOR_2(1), COLOR_16(4), COLOR_256(8), COLOR_HIGH(16), COLOR_TRUE1(24),
        COLOR_TRUE2(32);

        private Integer val;

        private BI_BIT_COUNT(Integer para) {
            this.val = para;
        }

        public int getValue() {
            return this.val;
        }
    }

    /**
     * Printer command.
     */
    private byte[] bmpPrintCmd = { BMP_PRINT_CMD1, BMP_PRINT_CMD2,
            BMP_PRINT_CMD3, BMP_PRINT_CMD4, BMP_PRINT_CMD5, BMP_PRINT_CMD6 };

    /**
     * Constructor.
     *
     * @param bmpDataInfo
     *            The binary data of bitmap
     * @param paperFlg
     *            The kind of Receipt paper
     */
    public BmpInfo(final byte[] bmpDataInfo, final int paperFlg) {
        this.bmpData = bmpDataInfo.clone();
        // get the file header of bitmap(fixed 14digits)
        this.bmpFileHeader = new BmpFileHeader(subByteArray(0,
                BT_FILE_HEADER_LEN));

        if (paperFlg == FLG_PAPER53) {
            bmpLineDataLen = PAPER_WIDTH_53MM;
        } else if (paperFlg == FLG_PAPER80) {
            bmpLineDataLen = PAPER_WIDTH_80MM;
        }
    }

    /**
     * Checks bitmap file type.
     *
     * @return Boolean result whether file is bitmap or not
     */
    public final boolean checkBitmap() {
        if (bmpData == null || bmpData.length == 0) {
            return false;
        }
        byte[] buffer = bmpFileHeader.getBfType();
        // 0x42:B 0x4d:M
        if (buffer[0] == (byte) BIT && buffer[1] == (byte) MAP) {
            return true;
        }

        return false;
    }

    /**
     * Initialize the bitmap data. Get the Info header.
     *
     * @return Boolean result if the bitmap data is initialized
     */
    public final boolean init() {
        if (bmpData == null || bmpData.length == 0) {
            return false;
        }

        byte[] buffer = subByteArray(BT_INFO_HEADER_POS, FOUR_INDEX);
        // For Info Type only
        if (convertToIntSize(buffer) == BT_INFO_HEADER_DIGIT) {
            // get info header
            bmpInfoHeader = new BmpInfoHeader(subByteArray(BT_INFO_HEADER_POS,
                    BT_INFO_HEADER_DIGIT));
            // Center alignment
            bmpPrintCmd[2] = (byte) ((bmpLineDataLen - bmpInfoHeader
                    .getBiWidthInt() / EIGHT_INDEX) / 2);
        } else {
            return false;
        }
        return true;
    }

    /**
     * Check if this bitmap is in 2 color.
     *
     * @return
     */
    public final boolean isMonoBmp() {
        // If biBitCount is not 2(2色), return false.
        if (convertToIntSize(bmpInfoHeader.getBiBitCount()) != BI_BIT_COUNT.COLOR_2
                .getValue()) {
            return false;
        }
        // If the length of color pallet is not 8, return false.
        if ((bmpFileHeader.getBfOffBitsInt() - BT_INFO_HEADER_DIGIT - BT_FILE_HEADER_LEN) != 8) {
            return false;
        }
        return true;
    }

    /**
     * Get the Monochrome type bitmap data For Receipt Printer.
     *
     * @return data for print
     */
    public final byte[] getMonoBmpdataForPrinter() {
        setTransferForPortKey();
        // get image data offset
        int bmpDataStart = bmpFileHeader.getBfOffBitsInt();
        // get the length of bitmap data
        int bmpDataLen = bmpInfoHeader.getBiSizeImageInt();
        // The value of bitmap size image maybe 0,
        // so when it is 0, use bitmap size minus bitmap file off bits
        if (bmpDataLen == 0) {
            bmpDataLen = bmpFileHeader.getBfSizeInt() - bmpDataStart;
        }
        // get bitmap data
        byte[] buffer = subByteArray(bmpDataStart, bmpDataLen);
        // get the width and height of bitmap
        int height = bmpInfoHeader.getBiHeightInt();

        int widthBit = bmpDataLen / height;
        // the windows bitmap data is reverse, so read
        // begin with the last data line
        byte[] bmpLineBf = new byte[widthBit];
        int index = buffer.length - widthBit;
        List<byte[]> bmpLineBytes = new ArrayList<byte[]>();
        for (int i = 0; i < height; i++) {
            // 6 is the length of bmpPrintCmd
            System.arraycopy(buffer, index, bmpLineBf, 0, widthBit);
            bmpLineBytes.add(transferForPrt(bmpLineBf));
            bmpLineBf = new byte[widthBit];
            index = index - widthBit;
        }

        return getBmpDataAsPaperWidth(bmpLineBytes);
    }

    private void setTransferForPortKey() {
        byte[] colorPalletBytes = subByteArray(BT_INFO_HEADER_DIGIT
                + BT_FILE_HEADER_LEN, 8);
        BmpColorPallet colorPallet = new BmpColorPallet(colorPalletBytes);
        if (colorPallet.getMonoBmpIndex1().equals(Color.BLACK)
                && colorPallet.getMonoBmpIndex2().equals(Color.WHITE)) {
            TRANSFER_FOR_PORT_KEY = (byte) 0xFF;
        } else if (colorPallet.getMonoBmpIndex1().equals(Color.WHITE)
                && colorPallet.getMonoBmpIndex2().equals(Color.BLACK)) {
            TRANSFER_FOR_PORT_KEY = (byte)0x00;
        }
    }

    /**
     * Transfer the bitmap data for printer.
     *
     * @param data
     *            Data to be transferred to the printer
     * @return Data
     */
    private byte[] transferForPrt(final byte[] data) {
        if (data == null || data.length == 0) {
            return new byte[0];
        }

        int loop = 0;
        int digit = 0;
        if (bmpInfoHeader.getBiWidthInt() > (bmpLineDataLen * EIGHT_INDEX)) {
            loop = bmpLineDataLen;
        } else {
            loop = bmpInfoHeader.getBiWidthInt() / EIGHT_INDEX;
            digit = bmpInfoHeader.getBiWidthInt() % EIGHT_INDEX;
        }
        for (int i = 0; i < loop; i++) {
            data[i] = (byte) (data[i] ^ TRANSFER_FOR_PORT_KEY);
        }
        if (digit != 0) {
            data[loop] = (byte) (data[loop] ^ (TRANSFER_FOR_PORT_KEY << (EIGHT_INDEX - digit)));
        }
        return data;
    }

    /**
     * Get bitmap data to print with paper width when the width of bitmap is
     * longer than paper and center alignment when the width of bitmap is
     * shorter than paper.
     *
     * @param bmpLineBytes
     *            Bitmap line in bytes
     * @return Bitmap data to print
     */
    private byte[] getBmpDataAsPaperWidth(final List<byte[]> bmpLineBytes) {
        int bmpLineDataLength = bmpPrintCmd[3];
        try (ByteArrayOutputStream ba = new ByteArrayOutputStream()) {
            for (byte[] bmpLinePrtBf : bmpLineBytes) {
                ba.write(bmpPrintCmd, 0, 3);
                ba.write(bmpLinePrtBf.length & 0xff);
                ba.write(bmpPrintCmd, 4, bmpPrintCmd.length - 4);
                ba.write(bmpLinePrtBf, 0, bmpLinePrtBf.length);
            }
            return ba.toByteArray();
        } catch (IOException e) {
            // ByteArrayOutputStream never throw an exception
        }
        return new byte[0]; // dummy
    }

    /**
     * Get the 16color type bitmap data For Receipt Printer.
     *
     * @return null
     */
    public final byte[] get16ClrBmpdataForPrinter() {
        return new byte[0];
    }

    /**
     * Get the 256color type bitmap data For Receipt Printer.
     *
     * @return null
     */
    public final byte[] get256ClrBmpdataForPrinter() {
        return new byte[0];
    }

    /**
     * Get the 24bits color type bitmap data For Receipt Printer.
     *
     * @return null
     */
    public final byte[] get24bitsClrBmpdataForPrinter() {
        return new byte[0];
    }

    /**
     * Convert original bitmap to NV bit image format for PriFlexPro BT printer.
     *
     * @return NV bit image data.
     */
    public final byte[] getNVImagePriFlexPro() {
        setTransferForPortKey();
        // Get original bitmap image file size.
        int bmpFileSize = bmpFileHeader.getBfSizeInt();
        // Get original bitmap image offset.
        int bmpFileOffset = bmpFileHeader.getBfOffBitsInt();
        // Get the length of original raw data.
        int bmpDataLen = bmpFileSize - bmpFileOffset;
        byte[] rawData = new byte[bmpDataLen];
        rawData = subByteArray(bmpFileOffset, bmpDataLen);

        boolean indexFlg = true;
        // The dot count in the vertical direction of original bitmap.
        int height = bmpInfoHeader.getBiHeightInt();
        // The byte count in horizontal.
        int width = bmpDataLen / height;
        // The dot count in horizontal direction of original bitmap.
        int widthInfo = bmpInfoHeader.getBiWidthInt();
        // The byte count in vertical direction of NV bit image.
        int lenH = height / EIGHT_INDEX + 1;
        if (height % EIGHT_INDEX == 0) {
            lenH = height / EIGHT_INDEX;
            indexFlg = false;
        }
        // The byte count in horizontal direction of NV bit image.
        int loop = widthInfo / EIGHT_INDEX + 1;
        if (widthInfo % EIGHT_INDEX == 0) {
            loop = (widthInfo / EIGHT_INDEX);
        }
        // If the dot count in horizontal is bigger than 48*8,
        // NV bit image define command will be useless.
        if (loop > PRIFLEX_MAX_WIDTH) {
            loop = PRIFLEX_MAX_WIDTH;
        }
        int index = 0;
        // If the width of bitmap is smaller than the width of paper.
        // Set bitmap to center align.
        int centerAlignDigits = 0;
        if (loop < PRIFLEX_MAX_WIDTH) {
            centerAlignDigits = (PRIFLEX_MAX_WIDTH - loop) / 2;
            index += centerAlignDigits * EIGHT_INDEX * lenH;
        }
        byte[] buffer = new byte[lenH * (loop + centerAlignDigits)
                * EIGHT_INDEX];
        byte[] nvImgData = new byte[buffer.length + FOUR_INDEX];
        // Get xL xH yL yH
        nvImgData[0] = (byte) ((loop + centerAlignDigits) % COLOR_SIZE_256);
        nvImgData[1] = (byte) ((loop + centerAlignDigits) / COLOR_SIZE_256);
        nvImgData[2] = (byte) (lenH % COLOR_SIZE_256);
        nvImgData[3] = (byte) (lenH / COLOR_SIZE_256);

        for (int i = 0; i < loop; i++) {
            for (int k = 0; k < EIGHT_INDEX; k++) {
                if (i * EIGHT_INDEX + k >= widthInfo) {
                    break;
                }
                int nvIdx = 0;
                for (int j = height - 1; j >= 0; j--) {
                    // Reverse the byte of original bitmap
                    // Printer: 1->black 0->white
                    // Original:0->black 1->white
                    byte tmp1 = (byte) (rawData[i + j * width] ^ TRANSFER_FOR_PORT_KEY);
                    // Get the k digit.
                    byte tmp2 = (byte) (tmp1 & (0x01 << (EIGHT_INDEX - 1 - k)));
                    byte tmp3 = (byte) (((tmp2 << k) >> nvIdx) & (0x01 << (EIGHT_INDEX - 1 - nvIdx)));
                    buffer[index] = (byte) (buffer[index] ^ tmp3);
                    nvIdx++;
                    if (nvIdx == EIGHT_INDEX) {
                        index++;
                        nvIdx = 0;
                    }
                }
                if (indexFlg) {
                    index++;
                }
            }
        }
        System.arraycopy(buffer, 0, nvImgData, FOUR_INDEX, buffer.length);
        return nvImgData;
    }

    /**
     * Copies the bitmap data.
     *
     * @param index
     *            Source index position
     * @param digits
     *            Length
     * @return Copied bitmap data
     */
    private byte[] subByteArray(final int index, final int digits) {
        byte[] buffer = new byte[digits];

        System.arraycopy(bmpData, index, buffer, 0, digits);

        return buffer;
    }

    /**
     * Converts data to size in int.
     *
     * @param data
     *            Data to be converted
     * @return Converted data
     */
    private int convertToIntSize(final byte[] data) {
        int size = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            size = size + (data[i] & TRANSFER_FOR_PORT_KEY)
                    * (int) Math.pow(COLOR_SIZE_256, i);
        }
        return size;
    }

    /**
     * Get the Monochrome type bitmap data For Receipt Printer.
     *
     * @return data for print
     */
    public final byte[] get90DegreeBmpdataForPrinter() {

        // Center alignment
        bmpPrintCmd[2] = (byte) ((bmpLineDataLen - bmpInfoHeader
                .getBiHeightInt() / EIGHT_INDEX) / 2);

        // 有効バイト数．
        if (bmpInfoHeader.getBiHeightInt() % EIGHT_INDEX == 0) {
            bmpPrintCmd[3] = (byte) (bmpInfoHeader.getBiHeightInt() / EIGHT_INDEX);
        } else {
            bmpPrintCmd[3] = (byte) (bmpInfoHeader.getBiHeightInt()
                    / EIGHT_INDEX + 1);
        }

        setTransferForPortKey();
        // get image data offset
        int bmpDataStart = bmpFileHeader.getBfOffBitsInt();
        // get the length of bitmap data
        int bmpDataLen = bmpInfoHeader.getBiSizeImageInt();
        // The value of bitmap size image maybe 0,
        // so when it is 0, use bitmap size minus bitmap file off bits
        if (bmpDataLen == 0) {
            bmpDataLen = bmpFileHeader.getBfSizeInt() - bmpDataStart;
        }
        // get bitmap data
        byte[] buffer = subByteArray(bmpDataStart, bmpDataLen);
        // get the width and height of bitmap
        int height = bmpInfoHeader.getBiHeightInt();

        int widthBit = bmpDataLen / height;
        // the windows bitmap data is reverse, so read
        // begin with the last data line
        byte[] bmpLineBf = new byte[widthBit];
        int index = buffer.length - widthBit;
        List<byte[]> bmpLineBytes = new ArrayList<byte[]>();
        byte[][] bmp2Bytes = new byte[height][widthBit * 8];
        for (int i = 0; i < height; i++) {
            int j = 0;
            // 6 is the length of bmpPrintCmd
            System.arraycopy(buffer, index, bmpLineBf, 0, widthBit);
            for (byte b : transferForPrt(bmpLineBf)) {
                int k = 0;
                for (; k < 8; k++) {
                    bmp2Bytes[i][j + k] = (byte) ((b & (int) Math.pow(2,
                            8 - k - 1)) >> (8 - k - 1));
                }
                j += k;
            }
            bmpLineBf = new byte[widthBit];
            index = index - widthBit;
        }

        byte[][] bmpTran = transpose(bmp2Bytes, height, widthBit * 8);

        for (int i = 0; i < widthBit * 8; i++) {
            bmpLineBf = new byte[widthBit];
            for (int j = 0; j < height;) {
                for (int k = 0; k < 8; k++, j++) {
                    if (j < height) {
                        bmpLineBf[j / 8] = (byte) ((bmpLineBf[j / 8] << 1) + bmpTran[i][j]);
                    } else {
                        bmpLineBf[j / 8] = (byte) (bmpLineBf[j / 8] << 1);
                    }
                }
            }
            bmpLineBytes.add(bmpLineBf);
        }

        return getBmpDataAsPaperWidth(bmpLineBytes);
    }

    private byte[][] transpose(byte[][] array, int rows, int cols) {
        byte[][] result = new byte[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                result[i][rows - 1 - j] = array[j][i];
            }
        }
        return result;
    }
}
