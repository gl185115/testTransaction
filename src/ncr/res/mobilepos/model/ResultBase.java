package ncr.res.mobilepos.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.helper.StringUtility;
/**
 * 改定履歴
 * バージョン         改定日付            担当者名          改定内容
 * 1.01               2014.12.08          FENGSHA           レポート出力を対応
 */

/**
 * Base model for representing operational results.
 */
@XmlRootElement(name = "Result")
public class ResultBase {
    /**
     *Message holder.
     */
    private String message;
    /**
     * ResultCode holder.
     */
    private int ncrwssResultCode;
    /**
     *Extended ResultCode holder.
     */
    private int ncrwssExtendedResultCode;
    // Result Codes

    /**************** Start: Generic Server Error Codes****************/
    /**
     * Generic Server Error. <p/>
     * 
     * Preferably "Internal Server Error".
     * An error code for server failure of valid request.
     * Check the NCRWSSExtendedResultCode for the error cause.
     */
    public static final int RES_SERVER_ERROR_5XX = 5000;
    /**
     * Server error for missing library.
     */
    public static final int RES_SERVER_LIBRARYNOTFOUND = 5001;
    /**************** End: Generic Server Error Codes ****************/
    
    /**************** Start: Generic Client Error Codes ****************/
    /**
     * Generic Client Error.<p/>
     * 
     * An error code for client failure of invalid request.
     * Check the NCRWSSExtendedResultCode for the status response
     * like 404 - Not Found, 400 - Bad Request, etc.
     */
    public static final int RES_CLIENT_ERROR_4XX = 4000;   
    /**************** End: Generic Client Error Codes ****************/

    /**
     * Value : {@value}<br>
     *General OK.
     */
    public static final int RES_OK = 0x00;
    /**
     * Value : {@value}<br>
     *Authentication success result code.
     */
    public static final int RESAUTH_OK = 0x00;
    /**
     * Value : {@value}<br>
     *Device was not found during authentication.
     */
    public static final int RESAUTH_DEVICENOTFOUND = 6;
    /**
     * Value : {@value}<br>
     *Authentication parameters are invalid.
     */
    public static final int RESAUTH_INVALIDPARAMETER = 7;
    /**
     * Value : {@value}<br>
     *Authentication token does not match to device credentials.
     */
    public static final int RESAUTH_TOKENNOTMATCH = 8;
    /**
     * Value : {@value}<br>
     *Authentication UDID does not match to device credentials.
     */
    public static final int RESAUTH_UDIDNOTMATCH = 9;
    /**
     * Value : {@value}<br>
     * The provided CORPID or STOREID does not exist in the database.
     */
    public static final int RESAUTH_STOREID_NOTEXIST
                            = 11;
    /**
     * Value : {@value}<br>
     *The provided CORPID does not exist in the database.
     */
    public static final int RESAUTH_CORPID_NOTEXIST = 12;
    /**
     * Value : {@value}<br>
     *The provided Passcode does not match to CORP credentials.
     */
    public static final int RESAUTH_PASSCODE_INVALID = 13;
    /**
     * Value : {@value}<br>
     *The Registration is denied in the Store.
     */
    public static final int RESAUTH_REG_DENIED = 14;
    /**
     * Value : {@value}<br>
     *Registration success result code.
     */
    public static final int RESREG_OK = 0x00;
    /**
     * Value : {@value}<br>
     *One of device's credentials is already existing in the database.
     */
    public static final int RESREG_DEVICEEXIST = 101;
    /**
     * Value : {@value}<br>
     *The device is not existing in the database.
     */
    public static final int RESREG_DEVICENOTEXIST = 102;
    /**
     * Value : {@value}<br>
     *The AdminKey provided is not valid.
     */
    public static final int RESREG_INVALIDADMINKEY = 103;
    /**
     * Value : {@value}<br>
     *The passcode provided is already expired.
     */
    public static final int RESREG_PASSCODEEXPIRED = 104;
    /**
     * Value : {@value}<br>
     *The passcode provided is invalid.
     */
    public static final int RESREG_INVALIDPASSCODE = 105;
    /**
     * Value : {@value}<br>
     *The UDID provided is invalid.
     */
    public static final int RESREG_INVALIDPARAMETER_UDID = 106;
    /**
     * Value : {@value}<br>
     *The Authentication Token (UUID) provided is invalid.
     */
    public static final int RESREG_INVALIDPARAMETER_UUID = 107;
    /**
     * Value : {@value}<br>
     *The Device ID provided is invalid.
     */
    public static final int RESREG_INVALIDPARAMETER_DEVID = 108;
    /**
     * Value : {@value}<br>
     *The Admin Key provided is invalid.
     */
    public static final int RESREG_INVALIDPARAMETER_NEWADMINKEY = 109;
    /**
     * Value : {@value}<br>
     *The Passcode provided is invalid.
     */
    public static final int RESREG_INVALIDPARAMETER_NEWPASSCODE = 110;
    /**
     * Value : {@value}<br>
     *Manual registration process is not allowed.
     *Deprecated.
     */
    public static final int RESREG_MANUALREG_NOTALLOWED = 111;
    /**
     * Value : {@value}<br>
     *The Device Name provided is invalid.
     */
    public static final int RESREG_INVALIDPARAMETER_DEVICENAME = 112;
    //guest
    /**
     * Value : {@value}<br>
     *The Guest Device ID is invalid.
     */
    public static final int
        RESREG_INVALIDPARAMETER_MANUAL_GUEST_DEVID = 164;
    /**
     * Value : {@value}<br>
     *Reports success result code.
     */
    public static final int RESRPT_OK = 0;
    /**
     * Value : {@value}<br>
     *The ReportType provided is invalid.
     */
    public static final int RESRPT_INVALIDREPORTTYPE = 117;
    /**
     * Value : {@value}<br>
     *The SalesType provided is invalid.
     */
    public static final int RESRPT_INVALIDSALESTYPE = 118;
    /**
     * Value : {@value}<br>
     *One of the parameters is invalid.
     */
    public static final int RESRPT_INVALIDPARAMETER = 119;
    /**
     * Value : {@value}<br>
     *Report data is empty.
     */
    public static final int RESRPT_EMPTY = 120;
    /**
     * Value : {@value}<br>
     *BusinessDate is invalid.
     */
    public static final int RESRPT_INVBIZDATE = 121;
    // Application General Error IDs
    /**
     * Value : {@value}<br>
     *General error.
     */
    public static final int RES_ERROR_GENERAL = 133;
    /**
     * Value : {@value}<br>
     *Database error.
     */
    public static final int RES_ERROR_DB = 134;
    /**
     * Value : {@value}<br>
     *Restriction error.
     *Deprecated.
     */
    public static final int RES_ERROR_RESTRICTION = 135;
    /**
     * Value : {@value}<br>
     *General SQL error.
     */
    public static final int RES_ERROR_SQL = 136;
    /**
     * Value : {@value}<br>
     *SQL statements or queries error.
     */
    public static final int RES_ERROR_SQLSTATEMENT = 137;
    /**
     * Value : {@value}<br>
     *Parsing error.
     */
    public static final int RES_ERROR_PARSE = 138;
    /**
     * Value : {@value}<br>
     *DAO error.
     */
    public static final int RES_ERROR_DAO = 139;
    /**
     * Value : {@value}<br>
     *JAXB error happening during XML binding/parsing.
     */
    public static final int RES_ERROR_JAXB = 140;
    /**
     * Value : {@value}<br>
     *Transaction data was not found.
     */
    public static final int RES_ERROR_TXNOTFOUND = 141;
    /**
     * Value : {@value}<br>
     *Invalid Transaction type.
     */
    public static final int RES_ERROR_TXINVALID = 142;
    /**
     * Value : {@value}<br>
     *Transaction is already voided.
     */
    public static final int RES_ERROR_TXALREADYVOIDED = 143;
    /**
     * Value : {@value}<br>
     *Transaction is already voided.
     */
    public static final int RES_ERROR_TXALREADYRETURNED = 144;
    /**
     * Value : {@value}<br>
     *Transaction was already processed.
     */
    public static final int RES_ERROR_TXALREADY = 148;
    /**
     * Value : {@value}<br>
     *Data encoding was unsupported.
     */
    public static final int RES_ERROR_UNSUPPORTEDENCODING = 149;
    /**
     * Value : {@value}<br>
     *The file specified was not found.
     */
    public static final int RES_ERROR_FILENOTFOUND = 150;
    /**
     * Value : {@value}<br>
     *Stream IO exception occured.
     */
    public static final int RES_ERROR_IOEXCEPTION = 151;
    /**
     * Value : {@value}<br>
     *Printer exception occured.
     */
    public static final int RES_ERROR_PRINTEREXCEPTION = 152;
    /**
     * Value : {@value}<br>
     *Setting of store CORPID failed.
     */
    public static final int RES_ERROR_SETCORPIDFAIL = 153;
    /**
     * Value : {@value}<br>
     * Invalid parameter.
     */
    public static final int RES_ERROR_INVALIDPARAMETER = 154;
    /**
     * Value : {@value}<br>
     *Setting of Search result's limit failed.
     */
    public static final int RES_ERROR_SETSYSTEMCONFIGFAIL = 155;
    // 1.01  2014.12.08  FENGSHA  レポート出力を対応  ADD START
    /**
     * Value : {@value}<br>
     * naming exception occured.
     */
    public static final int RES_ERROR_NAMINGEXCEPTION = 156;
    /**
     * Value : {@value}<br>
     * Database data was not found.
     */
    public static final int RES_ERROR_NODATAFOUND = 157;
    /**
     * Value : {@value}<br>
     * Creating a connection to the remote host failed. 
     */
    public static final int RES_ERROR_UNKNOWNHOST = 158;
    /**
     * Value : {@value}<br>
     * Search API failed. 
     */
    public static final int RES_ERROR_SEARCHAPI = 159;
   
    /**
     * Value : {@value}<br>
     * Event Check failed because businessDateId not between at resultset's StartDateId and EndDateId. 
     */
    public static final int RES_ERROR_BUSINESSDATEIDCHECKFAILED = 160;
    /**
     * Value : {@value}<br>
     * Event Check failed because EventKbn not equals to resultset's EventKbn. 
     */
    public static final int RES_ERROR_EVENTKBNCHECKFAILED = 161;
    /**************** Start: Generic Client Error Messages ****************/
    /**
     * Value : {@value}<br>
     * When the success of the set message value
     */
    public static final String RES_SUCCESS_MSG = "Success";
    /**
     * Value : {@value}<br>
     * Database data was not found.
     */
    public static final String RES_NODATAFOUND_MSG = "No Rows Available";
    /**
     * Value : {@value}<br>
     * Invalid parameter.
     */
    public static final String RES_INVALIDPARAMETER_MSG = "Invalid parameter Error";
    /**
     * Value : {@value}<br>
     * Search API failed. .
     */
    public static final String RES_SEARCHAPIERROR_MSG = "Search API Error";
    /**
     * Value : {@value}<br>
     * Search ItemId NOT FOUND. .
     */
    public static final String RES_SEARCHItemIdERROR_MSG = "ItemId NOT FOUND";
    /**
     * Value : {@value}<br>
     * get event login failed because EventKbn not equals to resultSet's EventKbn.
     */
    public static final String RES_VENTKBNCHECKEFAILED_MSG = "The parameter EventKbn is not to match the event result's EventKbn";
    /**
     * Value : {@value}<br>
     * get event login failed because businessDateId not between at resultSet's StartDateIda and EndDateId.
     */
    public static final String RES_BUSINESSDATEIDCHECKFAILED_MSG = "The parameter BusinessDateId is not applicable for the event period.";
    /**************** End: Generic Client Error Messages ****************/
    // 1.01  2014.12.08  FENGSHA  レポート出力を対応  ADD END
    /**
     * Value : {@value}<br>
     * Http/Https connection error.
     */
    public static final String RES_HTTPCONNECTIONFAILED_MSG = "Http connection failed";
    //Pricing
    /**
     * Value : {@value}<br>
     *The department data given the department id was not found.
     */
    public static final int RES_ERROR_DPTNOTFOUND = 180;
    /**
     * Value : {@value}<br>
     *The price change operation was not successful due to an error.
     */
    public static final int RES_ERROR_PRICECHANGE = 181;
    /**
     * Value : {@value}<br>
     *The itemPrice data given the item id was not found.
     */
    public static final int RES_ERROR_ITEMIDNOTFOUND = 182;
  //Error ExtCA
    /**
     * Value : {@value}<br>
     *External CA success result.
     */
    public static final int RESEXTCA_OK = 0;
    /**
     * Value : {@value}<br>
     *External CA data is not found.
     */
    public static final int RESEXTCA_ERROR_NOTFOUND = 200;
    /**
     * Value : {@value}<br>
     *External CA data is already being processed.
     */
    public static final int RESEXTCA_ERROR_INPROG = 201;
    /**
     * Value : {@value}<br>
     *External CA processing has ended normally.
     */
    public static final int RESEXTCA_ERROR_NORMEND = 202;
    /**
     * Value : {@value}<br>
     *External CA processing has ended abnormally.
     */
    public static final int RESEXTCA_ERROR_ABEND = 203;
    /**
     * Value : {@value}<br>
     *Failed to lock External CA data.
     */
    public static final int RESEXTCA_ERROR_LOCKFAIL = 204;
    /**
     * Value : {@value}<br>
     *Pastel port authorization failed.
     */
    public static final int RESEXTCA_ERROR_PASTELPORTERR = 205;
    /**
     * Value : {@value}<br>
     *Invalid Date on External CA Data.
     */
    public static final int RESEXTCA_ERROR_DATEINVALID = 206;
    //Peripheral Device Control Results
    /**
     * Value : {@value}<br>
     *Peripheral device control success result.
     */
    public static final int RESDEVCTL_OK = 0x00;
    /**
     * Value : {@value}<br>
     *The POS Link of the device was not found.
     */
    public static final int RESDEVCTL_NOPOSLINK = 220;
    /**
     * Value : {@value}<br>
     *The POS Terminal Link of the device was not found.
     */
    public static final int RESDEVCTL_NOPOSTERMINALLINK = 221;
    /**
     * Value : {@value}<br>
     *The printer  was not found.
     */
    public static final int RESDEVCTL_NOPRINTERFOUND = 222;
    /**
     * Value : {@value}<br>
     *The device data was not found.
     */
    public static final int RESDEVCTL_NOTFOUND = 223;
    /**
     * Value : {@value}<br>
     * Parameter is invalid.
     */
    public static final int RESDEVCTL_INVALIDPARAMETER = 224;
    /**
     * Value : {@value}<br>
     *The device data already exist in the database.
     */
    public static final int RESDEVCTL_ALREADY_EXIST = 225;
    /**
     * Value : {@value}<br>
     *The device data already exist in the database.
     */
    public static final int RESDEVCTL_INVALID_DEVICEID = 226;
    /**
     * Value : {@value}<br>
     *The POSLINK ID for the device is Invalid.
     */
    public static final int RESDEVCTL_INVALID_POSLINKID = 227;
    /**
     * Value : {@value}<br>
     *The Store ID for the device is Invalid.
     */
    public static final int RESDEVCTL_INVALID_STOREID = 228;
    
    /**
     * Value : {@value}<br>
     *PrinterInfo is not updated.
     */
    public static final int RES_PRINTER_NO_UPDATE = 229;
   
    /**
     * Value : {@value}<br>
     *PrinterInfo is not deleted.
     */
    public static final int RES_PRINTER_NOT_DELETED =230;
        
    
    /**
     * Value : {@value}<br>
     *PrinterInfo is existing but is not Active.
     */
    public static final int RES_PRINTER_IS_DELETED =231;
    
    /**
     * Value : {@value}<br>
     *PrinterInfo is existing and with 'Active' status.
     */
    public static final int RES_PRINTER_IS_ACTIVE =232;
    
    /**
     * Value : {@value}<br>
     *Device is in currently used.
     */
    public static final int RES_DEVICE_IS_IN_USE =233;   
    /**
     * Value : {@value}<br>
     *The printer port was not found.
     */
    public static final int RES_PRINTER_PORT_NOT_FOUND = 234;
    /**
     * Value : {@value}<br>
     *Malformed URL Exception.
     */
    public static final int RES_MALFORMED_URL_EXCEPTION = 235;
    //Credential Results
    /**
     * Value : {@value}<br>
     *Credential success result code.
     */
    public static final int RESCREDL_OK = 0x00;
    /**
     * Value : {@value}<br>
     *The operator is already online.
     *Deprecated.
     */
    public static final int RESCREDL_OPERATOR_ONLINE = 301;
    /**
     * Value : {@value}<br>
     *The operator is offline.
     */
    public static final int RESCREDL_OPERATOR_OFFLINE = 302;
    /**
     * Value : {@value}<br>
     *No data retrieved.
     */
    public static final int RESCREDL_ZERO_RESULTSET = 303;
    /**
     * Value : {@value}<br>
     *Credential parameters are maybe invalid.
     */
    public static final int RESCREDL_ERROR_NG = 304;
    /**
     * Value : {@value}<br>
     *Operator data was not found.
     */
    public static final int RESCREDL_ERROR_OPERATOR_NOTFOUND = 305;
    /**
     * Value : {@value}<br>
     *Sign On error. Invalid passcode.
     */
    public static final int RESCREDL_ERROR_PASSCODE_INVALID = 306;
    /**
     * Value : {@value}<br>
     *Operator Passcode reset failed.
     */
    public static final int RESCREDL_RESET_FAIL = 307;
    /**
     * Value : {@value}<br>
     *The operator is already existing in the database.
     */
    public static final int RESCREDL_ERROR_EXISTS = 308;
    /**
     * Value : {@value}<br>
     *The operator data update operation failed.
     */
    public static final int RESCREDL_ERROR_NO_UPDATE = 309;
    /**
     * Value : {@value}<br>
     *One of the parameters is invalid.
     */
    public static final int RESCREDL_ERROR_INVALID_PARAM = 310;
    /**
     * Value : {@value}<br>
     *The operator to update is currently online.
     */
    public static final int RESCREDL_ERROR_OPERATOR_ONLINE = 311;
    /**
     * Value : {@value}<br>
     *The Spart Operator's password has expired.
     */
    public static final int RESCREDL_ERROR_OPERATOR_EXPIRED = 312;
    /**
     * Value : {@value}<br>
     *The Spart Operator SecLevel2 status is inactive.
     */
    public static final int RESCREDL_ERROR_OPERATOR_INACTIVE = 313;
    /**
     * Value : {@value}<br>
     *The Spart Operator SecLevel2 status is deleted.
     */
    public static final int RESCREDL_ERROR_OPERATOR_DELETED = 314;
    //System Setting
    /**
     * Value : {@value}<br>
     *Failed to retrieve SystemSettings dataset.
     */
    public static final int RESSYS_ERROR_DATESET_FAIL = 320;
    /**
     * Value : {@value}<br>
     *SystemSettings date is invalid.
     */
    public static final int RESSYS_ERROR_INVALID_DATE = 321;
    /**
     * Value : {@value}<br>
     *SystemSettings time is invalid.
     */
    public static final int RESSYS_ERROR_INVALID_TIME = 322;
    /**
     * Value : {@value}<br>
     *SystemSettings skip data is invalid.
     */
    public static final int RESSYS_ERROR_INVALID_SKIP = 323;
    /**
     * Value : {@value}<br>
     *SystemSettings data is not found.
     */
    public static final int RESSYS_ERROR_NO_SETTINGS_FOUND = 324;
    //Queue Buster
    /**
     * Value : {@value}<br>
     *Transaction was not found from the QueueBuster.
     */
    public static final int RESSYS_ERROR_QB_TXNOTFOUND = 340;
    /**
     * Value : {@value}<br>
     *QueueBuster parameter was not found.
     */
    public static final int RESSYS_ERROR_QB_INVLDPRM = 341;
    /**
     * Value : {@value}<br>
     *QueueBuster transaction list is empty.
     */
    public static final int RESSYS_ERROR_QB_LISTEMPTY = 342;
    /**
     * Value : {@value}<br>
     *QueueBuster transaction request is invalid.
     */
    public static final int RESSYS_ERROR_QB_REQINVALID = 343;
    /**
     * Value : {@value}<br>
     *QueueBuster transaction date is invalid.
     */
    public static final int RESSYS_ERROR_QB_DATEINVALID = 344;
    /**
     * Value : {@value}<br>
     *Transaction was already resumed.
     */
    public static final int RESSYS_ERROR_QB_TXALREADYRESUMED = 345;
    /**
     * Value : {@value}<br>
     *Queue full per a store.
     */
    public static final int RESSYS_ERROR_QB_QUEUEFULL = 346;
    //NetworkReceipt Results
    /**
     * Value : {@value}<br>
     *NetworkReceipt success result code.
     */
    public static final int RESNETRECPT_OK = 0;
    /**
     * Value : {@value}<br>
     *Error occured in network receipt print.
     */
    public static final int RESNETRECPT_ERROR_NG = 400;
    /**
     * Value : {@value}<br>
     *Error occured in network receipt print.
     */
    public static final int RESNETRECPT_ERROR = 401;
    /**
     * Value : {@value}<br>
     *Transaction printer was not found.
     */
    public static final int RESNETRECPT_ERROR_NOTFOUND = 402;
    /**
     * Value : {@value}<br>
     *Drawer open has failed.
     */
    public static final int RESNETRECPT_ERROR_DRAWER = 403;
    /**
     * Value : {@value}<br>
     *Customer email address is invalid.
     */
    public static final int RESNETRECPT_ERROR_CUST_EMAIL = 404;
    /**
     * Value : {@value}<br>
     *Specified drawer was not found.
     */
    public static final int RESNETRECPT_ERROR_DRAWERNOTFOUND = 405;
    /**
     * Value : {@value}<br>
     *Printer cover is open. Cannot proceed to printing.
     */
    public static final int RESNETRECPT_ERROR_COVER_OPEN = 406;
    /**
     * Value : {@value}<br>
     *Printer paper scrolld is already empty.
     */
    public static final int RESNETRECPT_ERROR_PAPER_EXHAUSTED = 407;
    /**
     * Value : {@value}<br>
     *Printer general error.
     */
    public static final int RESNETRECPT_ERROR_OTHER = 408;
    /**
     * Invalid Business Date for Credit Slip. 
     */
    public static final int RESNETRECPT_CREDSLIP_INVALID_DATE = 409;
    //Signature Activation
    /**
     * Value : {@value}<br>
     *Signature Activation success result.
     */
    public static final int RES_SA_OK = 0;
    /**
     * Value : {@value}<br>
     *Signature Activation data retrieval failed.
     */
    public static final int RES_SA_ERROR = 501;
    /**
     * Value : {@value}<br>
     *Signature Activation data was not found.
     */
    public static final int RES_SA_ERROR_NOT_FOUND = 502;
    //Department Maintenance
    /**
     * Value : {@value}<br>
     * Result OK.
     */
    public static final int RES_DPTMT_OK = 0x00;
    /**
     * Value : {@value}<br>
     * Department already exists.
     */
    public static final int RES_DPTMT_EXISTS = 600;
    /**
     * Value : {@value}<br>
     * Store that contains the department does not exist.
     */
    public static final int RES_DPTMT_STORE_NOT_EXIST = 601;
    /**
     * Value : {@value}<br>
     * Department does not exist.
     */
    public static final int RES_DPTMT_NOT_EXIST = 602;
    /**
     * Value : {@value}<br>
     * Extended Result Code.
     * Signifies that no update was performed.
     */
    public static final int RES_DPTMT_NO_UPDATE = 603;
    /**
     * Value : {@value}<br/>
     * Extended Result Code.
     * Signifies that the department was deleted.
     */
    public static final int RES_DPTMT_DELETED = 604;
    /**
     * Value : {@value}<br/>
     * Extended Result Code.
     * Signifies that the department was neither Active or Deleted.
     */
    public static final int RES_DPTMT_NOTACTIVE = 605;
    //Store Maintenance
    /**
     * Value : {@value}<br>
     * Store already exists.
     */
    public static final int RES_STORE_EXISTS = 700;
    /**
     * Value : {@value}<br>
     * Invalid parameters for store.
     */
    public static final int RES_STORE_INVALIDPARAMS = 701;
    /**
     * Value : {@value}<br>
     * Store not found in the database.
     */
    public static final int RES_STORE_NOT_EXIST = 710;
    /**
     * Value : {@value}<br>
     * Failed to update Store.
     */
    public static final int RES_STORE_NO_UPDATE = 711;
    /**
     * Value : {@value}<br>
     * Result OK.
     */
    public static final int RES_STORE_OK = 0;
    /**
     * Value : {@value}<br>
     * Store Logo saved in the WebStoreServer File System is invalid.
     */
    public static final int RES_STORE_LOGO_INVALID = 712;

    //Item Maintenance
    /**
     * Value : {@value}<br>
     * Item does not exist.
     */
    public static final int RES_ITEM_NOT_EXIST = 801;
    /**
     * Value : {@value}<br>
     * Store that consist the item not found.
     */
    public static final int RES_ITEM_STORE_NOT_EXIST = 802;
    /**
     * Value : {@value}<br>
     * Store that consist the item not found.
     */
    public static final int RES_ITEM_ALREADY_EXIST = 803;
    /**
     * Value : {@value}<br>
     * Store that consist the item not found.
     */
    public static final int RES_ITEM_INSRT_EMPTYNAME = 804;
    /**
     * Value : {@value}<br>
     * Store that consist the item not found.
     */
    public static final int RES_ITEM_NO_UPDATE = 805;
    /**
     * Value : {@value}<br>
     * Department that consist the item not found.
     */
    public static final int RES_ITEM_DPT_NOT_EXIST = 806;

    /**
     * Value : {@value}
     * Pricing is not normal. It means device's pricingtype is not 0.
     */
    public static final int RES_ITEM_MANUAL_SEARCH_NOTALLOWED = 807;

    /**
     * Value : {@value}<br>
     * Invalid parameter in the pricing service.
     */
    public static final int RES_ITEM_INVALIDPARAMETER = 808;

    /**
     * Value : {@value}<br>
     * Duplicate entry in for link.
     */
    public static final int RES_LINK_ALREADYEXISTS = 901;

    /**
     * Value : {@value}<br>
     * Invalid store id.
     */
    public static final int RES_LINK_STOREINVALID = 902;
    /**
     * Value : {@value}<br>
     * Invalid link id.
     */
    public static final int RES_LINK_LINKIDINVALID = 903;

    /**
     * Value : {@value}<br>
     * Invalid link info.
     */
    public static final int RES_LINK_LINKINFOINVALID = 904;

    /**
     * Value : {@value}<br>
     * Invalid link type.
     */
    public static final int RES_LINK_TYPEINVALID = 905;

    /**
     * Value : {@value}<br>
     * Link List is Empty.
     */
    public static final int RES_LINK_LISTEMPTY = 906;
    /**
     * Value : {@value}<br>
     * QueueBuster Link not found.
     */
    public static final int RES_LINK_NOTFOUND = 907;

    /**
     * Value : {@value}<br>
     * Group is already existing.
     */
    public static final int RES_GROUP_EXISTS = 1001;
    /**
     * Value : {@value}<br>
     * User Group not found.
     */
    public static final int RES_GROUP_NOTFOUND = 1002;
    /**
     * Value : {@value}<br>
     * Promotion Date and Time Invalid.
     */
    public static final int RES_PROMOTION_DATE_INVALID = 1101;
    /**
     * value : {@value}<br>
     * Failed to end transaction.
     */
    public static final int RES_PROMOTION_ENDTRANSACTION_FAILED = 1103;

    //corpstore maintenance
    /**
     * Value : {@value}<br>
     * CorpStore already exists.
     */
    public static final int RES_CORPSTORE_EXISTS = 1201;
    /**
     * Value : {@value}<br>
     * CorpStore not found in the database.
     */
    public static final int RES_CORPSTORE_NOT_EXIST = 1202;
    /**
     * Value : {@value}<br>
     * Failed to update CorpStore.
     */
    public static final int RES_CORPSTORE_NO_UPDATE = 1203;
    /**
     * Value : {@value}<br>
     * Invalid parameter passed to resource.
     */
    public static final int RES_CORPSTORE_INVALID_PARAM = 1204;
    /**
     * Value : {@value}<br>
     * Unable to get cash balance
     */
    public static final int RES_CASH_ACCOUNT_NO_CASH_BALANCE = 1301;
    /**
     * value : {@value}<br>
     * The row from SPART can't be imported to Web Store Server.
     */
    public static final int RES_MAINTENACE_IMPORT_ERROR = -1;
    /**
     * value : {@value}<br>
     * The row from SPART can't be imported to Web Store Server.
     */
    public static final int RES_DISCOUNT_BUTTONS_DATA_ERROR = -5;
    //forwarditem
    /**
     * value : {@value}<br>
     * Failed to insert forward item data.
     */
    public static final int RES_FORWARD_ITEM_NO_INSERT = 1301;    
    //line info maintenance
    /**
     * value : {@value}<br>
     * Line info not found.
     */
    public static final int RES_LINE_INFO_NOT_EXIST = 1400;
    /**
     * value : {@value}<br>
     * Line info already existing.
     */
    public static final int RES_LINE_INFO_ALREADY_EXIST = 1401;
    /**
     * Value : {@value}<br>
     * Invalid parameter in the line service.
     */
    public static final int RES_LINE_INFO_INVALID_PARAMETER = 1402;
    /**
     * Value : {@value}<br>
     * Store of the Line not found
     */
    public static final int RES_LINE_INFO_STORE_NOT_EXIST = 1403;
    /**
     * Value : {@value}<br>
     * Department of the Line not found
     */
    public static final int RES_LINE_INFO_DPT_NOT_EXIST = 1404;
    /**
     * Value : {@value}<br>
     * Line not not updated
     */
    public static final int RES_LINE_INFO_NOT_UPDATED = 1405;
    //class info maintenance
    /**
     * value : {@value}<br>
     * Class info not found.
     */
    public static final int RES_CLASS_INFO_NOT_EXIST = 1450;
    /**
     * value : {@value}<br>
     * Class info already existing.
     */
    public static final int RES_CLASS_INFO_ALREADY_EXIST = 1451;
    /**
     * Value : {@value}<br>
     * Invalid parameter in the class service.
     */
    public static final int RES_CLASS_INFO_INVALID_PARAMETER = 1452;
    /**
     * Value : {@value}<br>
     * Store of the Class not found
     */
    public static final int RES_CLASS_INFO_STORE_NOT_EXIST = 1453;
    /**
     * Value : {@value}<br>
     * Department of the Class not found
     */
    public static final int RES_CLASS_INFO_DPT_NOT_EXIST = 1454;
    /**
     * Value : {@value}<br>
     * Line of the Class not found
     */
    public static final int RES_CLASS_INFO_LINE_NOT_EXIST = 1455;
    /**
     * Value : {@value}<br>
     * Line of the Class not found
     */
    public static final int RES_CLASS_INFO_NOT_UPDATED = 1456;
    /**
     * value : {@value}<br>
     * Till does not exist.
     */
    public static final int RES_TILL_NOT_EXIST = 1500;    
    /**
     * value : {@value}<br>
     * Till ID is not valid.
     */
    public static final int RES_TILL_INVALIDPARAMS = 1501;        
    /**
     * value : {@value}<br>
     * Till already exists.
     */
    public static final int RES_TILL_EXISTS = 1502;     
    /**
     * Value : {@value}<br>
     * Failed to update Till.
     */
    public static final int RES_TILL_NO_UPDATE = 1503;
    /**
     * Value : {@value}<br>
     * Other terminal is already processing SOD. SODFlag is 9.
     */
    public static final int RES_TILL_SOD_PROCESSING = 1504;
    /**
     * Value : {@value}<br>
     * Other terminal is already finished SOD. SODFlag is 1.
     */
    public static final int RES_TILL_SOD_FINISHED = 1505;
    /**
     * Value : {@value}<br>
     * Invalid sod flag value. Must be 0, 9, or 1.
     */
    public static final int RES_TILL_INVALID_SOD_FLAG_VAL = 1506;
    /**
     * Value : {@value}<br>
     * MST_BIZDAY.TodayDate is less than or equal to MST_TILLIDINFO.BusinessDayDate
     */
    public static final int RES_TILL_INVALID_BIZDATE = 1507;
    /**
     * Value : {@value}<br>
     * SOD has not been triggered yet.
     */
    public static final int RES_TILL_SOD_UNFINISHED = 1508;
    /**
     * Value : {@value}<br>
     * Other terminal is already finished EOD. EODFlag is 1.
     */
    public static final int RES_TILL_EOD_FINISHED = 1509;
    /**
     * Value : {@value}<br>
     * Other terminal is already processing EOD. EODFlag is 9.
     */
    public static final int RES_TILL_EOD_PROCESSING = 1510;
    /**
     * Value : {@value}<br>
     * Invalid eod flag value. Must be 0, 9, or 1.
     */
    public static final int RES_TILL_INVALID_EOD_FLAG_VAL = 1511;
    /**
     * Value : {@value}<br>
     * EOD has not been triggered yet.
     */
    public static final int RES_TILL_EOD_UNFINISHED = 1512;
    /**
     * Value : {@value}<br>
     * Other users connected to till are still signed on
     */
    public static final int RES_TILL_OTHER_USERS_SIGNED_ON = 1513;
    /**
     * Value : {@value}<br>
     * Failed to update the pevious amount of SOD.
     */
    public static final int RES_NO_UPDATE_PREVIOUS_AMOUNT = 1514;
    /**
     * Value : {@value}<br>
     * Getting the value for SERVERTYPE system property failed.
     */
    public static final int RES_SYSTEM_PROP_ERROR = 1600;
    /**
     * Value : {@value}<br>
     * Credit summary for a specific business day date is not found.
     */
    public static final int RES_CREDIT_SUMMARY_NOT_FOUND = 1700;
    /** True string for UI */
    public static final String TRUE = "True";

    /** False string for UI */
    public static final String FALSE = "False";

    public static final String toString(boolean b) {
        return (b) ? TRUE : FALSE;
    }
    
    /**
     * The default constructor.
     */
    public ResultBase() {
    }

    /**
     * ResultBase constructor.
     *
     * @param resultCode
     *        - resultcode value for the operation
     * @param messageToSet
     *        - message data
     */
    public ResultBase(final int resultCode, final String messageToSet) {
        this.ncrwssResultCode = resultCode;
        this.message = messageToSet;
    }

	/**
	 * ResultBase constructor.
	 * 
	 * @param resultCode
	 *            -Code that causes the error.
	 * @param extendedResultCode
	 *            -Code that defines the resultcode.
	 * @param throwable
	 *            -An exception.
	 */
	public ResultBase(final int resultCode, final int extendedResultCode,
			final Throwable throwable) {
		this.ncrwssResultCode = resultCode;
		this.ncrwssExtendedResultCode = extendedResultCode;
		this.message = StringUtility.printStackTrace(throwable);
	}
    
    /**
     * Constructor.
     * @param resultCode
     */
    public ResultBase(final int resultCode){
    	this.ncrwssResultCode = resultCode;
    }
    
    /**
     * Getter for the message value.
     *
     * @return String
     *         - value of the message
     */
    @XmlElement(name = "message")
    public final String getMessage() {
        return message;
    }
    /**
     * Getter for the resultcode value.
     *
     * @return int
     *         - value of the resultcode
     */
    @XmlElement(name = "NCRWSSResultCode")
    public final int getNCRWSSResultCode() {
        return ncrwssResultCode;
    }
    /**
     * Setter for the resultcode value.
     *
     * @param resultCode
     *        - value of the resultCode
     */
    public final void setNCRWSSResultCode(final int resultCode) {
        this.ncrwssResultCode = resultCode;
    }
    /**
     * Getter for the extendedresultcode value.
     *
     * @return int
     *         - value of the extendedresultcode
     */
    @XmlElement(name = "NCRWSSExtendedResultCode")
    public final int getNCRWSSExtendedResultCode() {
        return ncrwssExtendedResultCode;
    }
    /**
     * Setter for the extendedresultcode value.
     *
     * @param extendedResultCode
     *        - value of the extendedresultcode
     */
    public final void
           setNCRWSSExtendedResultCode(final int extendedResultCode) {
        this.ncrwssExtendedResultCode = extendedResultCode;
    }
    /** Setter for the message value.
    *
    * @param messageToSet
    *        - value of the message
    */
    public final void setMessage(final String messageToSet) {
        this.message = messageToSet;
    }
    /**
     * Group of result codes for promotion.
     * These codes are based on UE.
     */
    public static final class PROMOTION {
        /** The Default Constructor. */
        private PROMOTION() {        	
        }
        /**
         * value : {@value}<br>
         * Item exception. The specified
         * ItemEntryId was not found.
         */
        public static final int ITEM_ENTRYID_NOTFOUND = 1110;
        /**
         * value : {@value}<br>
         * The identifier for this message is
         * already present in the transaction.
         */
        public static final int DUPLICATE_ENTRY = 93;
        /**
         * value : {@value}<br>
         * One of the data fields in the request message
         * contained an invalid or unsupported value.
         */
        public static final int INVALID_CONTENT = 94;
        /**
         * value : {@value}<br>
         * The transaction specified in the header of
         * the request could not be matched with an
         * existing transaction (and an existing
         * transaction is required for the message type).
         */
        public static final int NO_MATCHING_TRANSACTION = 1102;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResultCode: ").append(this.getNCRWSSResultCode());
        sb.append("; ExtendedResultCode: ")
            .append(this.getNCRWSSExtendedResultCode());
        sb.append("; Message: ").append(this.getMessage());
        return sb.toString();
    }
}
