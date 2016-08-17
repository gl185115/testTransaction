package ncr.res.mobilepos.simpleprinterdriver;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2015.01.20      FENGSHA       80mmの紙変更を対応,字体圧縮しない
 * 1.02            2015.02.06      FENGSHA       Printer時間の設定変更を対応
 */
public interface IPrinter {
    /**************************Printer Parameters****************************/
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

    /**************************Printer Commands****************************/
    /**
     * Command for Initialize the printer.
     */
    static final byte[] INIT_CMD = {(byte) 0x10, (byte) 0x1b,
        (byte) 0x52, (byte) 0x80, (byte) 0x1b, (byte) 0x21, (byte) 0x00,
        (byte) 0x1c, (byte) 0x21, (byte) 0x00, (byte) 0x1b, (byte) 0x16,(byte) 1};
    //1.01 2015.01.20 FENGSHA ADD START
    static final byte[] INIT_CMDFOR80 = {(byte) 0x10, (byte) 0x1b,
        (byte) 0x52, (byte) 0x80, (byte) 0x1b, (byte) 0x21, (byte) 0x00,
        (byte) 0x1c, (byte) 0x21, (byte) 0x00, (byte) 0x1b, (byte) 0x16,(byte) 0};
    //1.01 2015.01.20 FENGSHA ADD END
    /**
     * Command for Feed 100 dot rows (20mm) and Cut paper.
     */
    static final byte[] CUT_CMD = {(byte) 0x15, (byte) 0xA0,
                        (byte) 0x19};
    /**
     * Command for Feed 30 times vertical motion unit performs a partial cut paper
     */
    static final byte[] CUT_CMD2 = { 0x1D, 0x56, 66,30};
    /**
     * Command for Feed 30 times cuts the paper completely
     */
    static final byte[] CUT_CMD3 = { 0x1D, 0x56, 65,10};
    /**
     * Cuts the receipt, leaving 5 mm (.20 inch) of paper
     */
    static final byte[] CUT_CMD4 = { 0x15,(byte)200,0x1B, 0x6D};
    /**
     * Command for open drawer.
     */
    static final byte[] OPENDRAWER_CMD = {(byte) 0x1B, (byte) 0x70,
        (byte) 0x00, (byte) 0x32, (byte) 0x32};

    /**
     * Command for recover and clear buffers.
     */
    static final byte[] RECOVER_CLEAR_CMD =
        {(byte) 0x1D, (byte) 0x03, (byte) 0x02};

    /**
     * Real Time Request to Printer.
     * Command for Recover and Clear Buffer
     */
    static final byte[] CLEAR_BUFFER_CMD = {0x1D, 0x03, 0x02};

    /**
     * Transmit status to TCP port to get printer status.
     */
    static final byte[] GET_STATUS_TCP_CMD = {0x1D, 0x72, 0x01};

    /**
     * Transmit RS232C Busy Status to UDP port.
     */
    static final byte[] GET_BUSY_STATUS_UDP_CMD = {0x1D, 0x04, 0x02};

    /**
     * Transmit Error status to UDP port.
     */
    static final  byte[] GET_ERROR_STATUS_UDP_CMD = {0x1D, 0x04, 0x03};

    /**
     * Transmit Receipt paper status to UDP port.
     */
    static final byte[] GET_PAPER_STATUE_UDP_CMD = {0x1D, 0x04, 0x04};

    /**
     * Check Printer Status Max times.
     */
    public static final int CON_MAX_TIMES = 10;

    /**
     * Timeout for connection to Printer TCP Port.
     */
    public static final int CON_TCP_TIMEOUT = 1000;

    /**
     * Timeout for receive message from Printer TCP Port.
     */
    //1.02 2015.02.06 FENGSHA MOD START
    //public static final int RECV_TCP_TIMEOUT = 500;
    public static final int RECV_TCP_TIMEOUT = 5000;
    //1.02 2015.02.06 FENGSHA MOD END
    //1.02 2015.02.06 FENGSHA ADD START
    public static final int CONNECT_TIME_OUT = 1000;
    //1.02 2015.02.06 FENGSHA ADD END
    /**
     * Timeout for receive message from Printer UDP Port.
     */
    public static final int RECV_UDP_TIMEOUT = 100;

    /**
     * Error number when failed connecting to the printer.
     */
    public static final int ERROR_NUM_CONNECT = 99;
    /**
     * Error message when failed connecting to the printer.
     */
    public static final String ERROR_MSG_CONNECT = "Failed to Connect"
                + "to the Printer.";

    /**
     * Error number when data is wrong.
     */
    public static final int ERROR_NUM_DATA = 98;
    /**
     * Error message when data is wrong.
     */
    public static final String ERROR_MSG_DATA = "Data is wrong.";

    /**
     * Error number when receipt paper is exhausted.
     */
    public static final int ERROR_NUM_PAPER = 1;
    /**
     * Error message when receipt paper is exhausted.
     */
    public static final String ERROR_MSG_PAPER = "Receipt Paper Exhausted.";

    /**
     * Error number when the printer cover is opened.
     */
    public static final int ERROR_NUM_COVER = 2;
    /**
     * Error message when the printer cover is opened.
     */
    public static final String ERROR_MSG_COVER = "Cover Opened.";

    /**
     * Error number when printing is stopped due to paper condition.
     */
    public static final int ERROR_NUM_STOP_PRINT = 3;
    /**
     * Error message when printing is stopped due to paper condition.
     */
    public static final String ERROR_MSG_STOP_PRINT =
        "Printing stopped due to paper condition.";
    /**
     * Error number when error condition exists in the printer.
     */
    public static final int ERROR_NUM_ERR_COMDITION = 4;
    /**
     * Error message when error condition exists in the printer.
     */
    public static final String ERROR_MSG_ERR_COMDITION =
        "Error condition exists in the printer.";
    /**
     * Error number when knife error occurred.
     */
    public static final int ERROR_NUM_KNIFE = 5;
    /**
     * Error message when knife error occurred.
     */
    public static final String ERROR_MSG_KNIFE = "Knife error occurred.";
    /**
     * Error number when unrecoverable error occurred.
     */
    public static final int ERROR_NUM_UNRECOVERABLE = 6;
    /**
     * Error message when unrecoverable error occurred.
     */
    public static final String ERROR_MSG_UNRECOVERABLE =
        "Unrecoverable error occurred.";
    /**
     * Error number when thermal print head or printer power supply
     * voltage is out of range.
     */
    public static final int ERROR_NUM_POWER = 7;
    /**
     * Error message when thermal print head or printer power supply
     * voltage is out of range.
     */
    public static final String ERROR_MSG_POWER
        = "Thermal print head temp./power supply voltage are out of range.";
    /**
     * When printer is ok.
     */
    public static final boolean PRINTER_OK = true;
    /**
     * When printer error occurs.
     */
    public static final boolean PRINTER_ERROR = false;

    /**************************Bitmap Information****************************/
    /**
     * The byte number of bitmap file header.
     */
    public static final int BT_FILE_HEADER_LEN = 14;

    /**
     * The start position of info header of bitmap.
     */
    public static final int BT_INFO_HEADER_POS = 0x0E;

    /**
     * The digits of Info Header of bitmap.
     */
    public static final int BT_INFO_HEADER_DIGIT = 0x28;

    /**
     * Connect to printer
     * @return
     */
    public boolean connectPrinter();

    /**
     * Initialize printer.
     *
     * @return Boolean result if printer is initialized.
     */
    public boolean initPrinter();

    /**
     * Send Real Time Command to UDP. Recover and Clear Buffers.
     *
     * @return Results to true if buffer is cleared, otherwise false.
     */
    public boolean clearBuffer();

    /**
     * Add bitmap.
     *
     * @param data binary data of bitmap file
     * @param flag the type of bitmap 1:monochrome 2:16color 3:256color 4:24bits
     *            color
     * @param paperFlg flag of paper 1:53mm 2:80mm
     * @return Boolean result if bitmap is added.
     */
    public boolean writeBmp(final byte[] data, final int flag,
            final int paperFlg);

    /**
     * Add text.
     *
     * @param data Data to be added in the text.
     */
    public void writeText(final byte[] data);

    /**
     * Add text.
     *
     * @param data Data to be added in the text.
     * @param index
     */
    public void writeText(final byte[] data, int index);

    /**
     * Send print data to printer.
     *
     * @return Result after printer check result
     */
    public int print();

    /**
     * Cut paper.
     *
     * @return Returns true or false if the paper is cut
     */
    public boolean cutPaper();

    /**
     * Open Drawer.
     *
     * @return Returns true or false if the drawer is opened
     */
    public boolean openDrawer();

    /**
     * Disconnect to the printer.
     *
     * @return Returns true or false if the printer is disconnected
     */
    public boolean close();
}
