package ncr.res.mobilepos.networkreceipt.helper;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.ReceiptFormatter;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.networkreceipt.dao.IReceiptDAO;
import ncr.res.mobilepos.networkreceipt.model.PaperReceipt;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptContent;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptHeader;
import ncr.res.mobilepos.networkreceipt.model.ReceiptMixMatchBlock;
import ncr.res.mobilepos.networkreceipt.model.ReceiptProductItem;

/**
 * handles email sending and formatting of email receipts.
 *
 */
public class EMailReceipt {
	/*
	 *  Logging handler.
	 */
   private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

   private static final String PROG_NAME = "EMailReceipt";
    /**
     * the class instance of the xml document.
     */
    private Document doc = null;
    /**
     * the class instance of the PaperReceipt class.
     */
    private PaperReceipt rec = null;
    /**
     * the class instance of the debug trace printer.
     */
    private Trace.Printer tp;
    /**
     * the constant for html body tag.
     */
    private static final String BODY_TAG = "body";
    /**
     * the constant for getter functions prefix.
     */
    private static final String GETTER_PREFIX = "get";
    /**
     * the constant for currency attribute.
     */
    private static final String CURRENCY = "currency";
    /**
     * the constant for query string symbol for direct child.
     */
    private static final String HTML_DIRECT_CHILD = " > ";
    /**
     * the constant for query string symbol for AND.
     */
    private static final String HTML_AND = ",";
    /**
     * the constant for attibute value.
     */
    private static final String ATTR_VALUE = "data-receipt-value";
    /**
     * the constant for attribute caption.
     */
    private static final String ATTR_CAPTION = "data-receipt-caption";
    /**
     * the constant for attribute type.
     */
    private static final String ATTR_TYPE = "data-receipt-type";
    /**
     * the constant for query string for divs with the value attribute.
     */
    private static final String ALL_ATTR_VALUE = "div[data-receipt-value]";
    /**
     * the constant for query string for divs with the caption attribute.
     */
    private static final String ALL_ATTR_CAPTION = "div[data-receipt-caption]";
    /**
     * the constant for query string for divs with the ads group attribute.
     */
    private static final String ATTR_GROUP_ADS = "div[data-receipt-group=ads]";
    /**
     * the constant for query string for divs with the items group attribute.
     */
    private static final String ATTR_GROUP_ITEMS =
        "div[data-receipt-group=items]";
    /**
     * the constant for query string for divs with the mix match group.
     */
    private static final String ATTR_GROUP_MMBLOCKS =
        "div[data-receipt-group=mmBlocks]";
    /**
     * the constant for query string for divs with the mix match group.
     */
    private static final String ATTR_GROUP_MMBLOCKNAME =
        "div[data-receipt-group=mmBlockName]";
    /**
     * the constant for query string for divs with the mix match items.
     */
    private static final String ATTR_GROUP_MMITEMS =
        "div[data-receipt-group=mmItems]";
    /**
     * the constant for query string for divs with the mix match financial.
     */
    private static final String ATTR_GROUP_MMFINAN =
        "div[data-receipt-group=mmFinancial]";
    /**
     * the constant for query string for divs with the credit group attribute.
     */
    private static final String ATTR_GROUP_CREDIT =
        "div[data-receipt-group=credit]";
    /**
     * Training mode message div
     */
    private static final String ATTR_FROUP_TRANTYPE =
        "div[data-receipt-group=transactiontype]";
    /**
     * the constant for query string for divs with the discount group attribute.
     */
    private static final String ATTR_GROUP_ITEM_DISCOUNT =
        "div[data-receipt-group=ItemDiscount]";
    /**
     * the constant for the path to default receipt template file.
     */
    private static final String DEFAULT_TEMPLATE_FILE =
        "defaultreceiptemailtemplate";
    /**
     * the constant for default key.
     */
    private static final String DEFAULT_TEMPLATE_KEY = "default";
    /**
     * the constant for character encoding.
     */
    private static final String HTML_FORMAT_UTF_8 = "utf-8";
    /**
     * the constant for the locale files.
     */
    private static final String RECEIPT_LOCALE = "receiptlabel";
    /**
     * the constant for html empty value.
     */
    private static final String EMPTY_VALUE = "----";
    /**
     * the constant for empty string.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Sets the class variable doc.
     * @param document - HTML document to set
     */
    public final void setDoc(final Document document) {
        this.doc = document;
    }

    /**
     * Sets the class variable doc.
     * @param filepath - path of the file that contains HTML string
     * @throws DaoException - thrown when error occurs
     */
    public final void setDoc(final String filepath) throws DaoException {
        try {
            File file = new File(filepath);
            this.doc = Jsoup.parse(file, HTML_FORMAT_UTF_8);
        } catch (Exception e) {
            throw new DaoException("Failed to parse HTML file.\n"
                    + e.getMessage());
        }
    }

    /**
     * Sets the class variable receipt.
     * @param receipt - PaperReceipt object to set
     */
    public final void setReceipt(final PaperReceipt receipt) {
        this.rec = receipt;
    }

    /**
     * Sets the class variable receipt.
     * @param poslog - Poslog of Transaction to send in Email
     * @param txid - Transaction Number of the poslog.
     * @param deviceNo - Device Number of the poslog
     * @param operatorNo - Operator Number of the poslog
     * @throws DaoException - thrown when exception occurs
     */
    public final void setReceipt(final PosLog poslog, final String txid,
            final String deviceNo, final String operatorNo)
            throws DaoException {
        IReceiptDAO iReceiptDAO =
            DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getReceiptDAO();
        this.rec = iReceiptDAO.getPaperReceipt(
                poslog, txid, deviceNo, operatorNo, null);
    }

    /**
     * Constructor.
     * @param receipt - PaperReceipt containing the values of the transaction
     * @param document - Document of the HTML
     */
    public EMailReceipt(final PaperReceipt receipt, final Document document) {
        setDoc(document);
        setReceipt(receipt);
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Constructor.
     * @param receipt - PaperReceipt containing the values of the transaction
     * @param filepath - path of the file that contains HTML string
     * @throws DaoException - thrown when an exception occurs
     */
    public EMailReceipt(final PaperReceipt receipt, final String filepath)
    throws DaoException {
        setReceipt(receipt);
        setDoc(filepath);
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Construtor.
     * @param poslog - Poslog of Transaction to send in Email
     * @param txid - Transaction Number of the poslog.
     * @param deviceNo - Device Number of the poslog
     * @param operatorNo - Operator Number of the poslog
     * @param filepath - path of the file that contains HTML string
     * @throws DaoException - thrown when an exception occurs
     */
    public EMailReceipt(final PosLog poslog, final String txid,
            final String deviceNo, final String operatorNo,
            final String filepath) throws DaoException {
        setReceipt(poslog, txid, deviceNo, operatorNo);
        setDoc(filepath);
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Constructor.
     * @param poslog - Poslog of Transaction to send in Email
     * @param txid - Transaction Number of the poslog.
     * @param deviceNo - Device Number of the poslog
     * @param operatorNo - Operator Number of the poslog
     * @param document - Document of the HTML
     * @throws DaoException - thrown when exception occurs
     */
    public EMailReceipt(final PosLog poslog, final String txid,
            final String deviceNo, final String operatorNo,
            final Document document) throws DaoException {
        setReceipt(poslog, txid, deviceNo, operatorNo);
        setDoc(document);
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Default constructor set to private.
     */
    private EMailReceipt() {    	
    }

    /**
     * Format the contained PaperReceipt object into the
     *  HTML file in the contained Document.
     * @param language - Language code string that defines
     *  language to be used (eg. "en", "ja")
     * @param customerId - Id of the customer to send to
     * @return String - html format of the receipt
     * @throws DaoException - thrown when exception occurs
     */
    public final String formatToHtmlReceipt(final String language,
            final String customerId) throws DaoException {
        //initialize the logger
        tp.methodEnter("formatToHtmlReceipt")
        .println("language", language).println("customerId", customerId);
        if (this.rec == null) {
            tp.println("Nothing to convert.");
            return "";
        }
        ResourceBundle resourceCaptions = getLocaleResource(language);
        //get the proper date format
        String receiptDate = this.rec.getReceiptContent().getReceiptDateTime();
        this.rec.getReceiptContent().setReceiptDateTime(
                ReceiptFormatter.getReceiptDateTime(receiptDate, language));
        if (this.doc == null) {
            ResourceBundle resourceDefaultTemplate =
                ResourceBundle.getBundle(DEFAULT_TEMPLATE_FILE);
            this.doc = Jsoup.parse(resourceDefaultTemplate
                    .getString(DEFAULT_TEMPLATE_KEY));
            tp.println("Using default template.");
        }
        //clone the Document (precaution taken to avoid changes to the template)
        Document documentClone = this.doc.clone();
        Element elementBody = documentClone.body();
        //if the transaction did not use Credit Card
        //PaperReceiptPayment is null)
        // remove the credit group
        if (null == this.rec.getReceiptPayment()) {
            Elements elementCredit = elementBody.select(ATTR_GROUP_CREDIT);
            elementCredit.remove();
        }
        if (!this.rec.getReceiptContent().isTrainingModeFlag()) {
            Elements elementTranType = elementBody.select(ATTR_FROUP_TRANTYPE);
            elementTranType.remove();
        }
        //Set the text of the direct children of the body and
        //the direct children of the credit group
        //    with attributes data-receipt-caption to
        //the captions in the resource
        setCaptions(elementBody.select(BODY_TAG + HTML_DIRECT_CHILD
          + ALL_ATTR_CAPTION + HTML_AND + ATTR_GROUP_CREDIT  + HTML_DIRECT_CHILD
          + ALL_ATTR_CAPTION), resourceCaptions);
        //initialize the returnedValue
        String returnedValue = EMPTY_STRING;
        //get the tags that are direct children of the body
        //and the direct children of the credit group
        // with attributes data-receipt-value
        Elements elementsValue = elementBody.select(BODY_TAG + HTML_DIRECT_CHILD
                + ALL_ATTR_VALUE + HTML_AND + ATTR_GROUP_CREDIT
                + HTML_DIRECT_CHILD + ALL_ATTR_VALUE);
        //initialize the method
        Method method = null;
        //get the objects in paper receipt in preparation
        //for traversing the elements_value
        List<Object> paperReceiptObjs = getObjectsFromPaperReceipt();
        //traverse the elements_value to set the text of each element
        for (Element element : elementsValue) {
            //get the functionName of the method to call
            // must be a getter function + value of the
            //data-receipt-value attribute
            String functionName = GETTER_PREFIX + element.attr(ATTR_VALUE);
            //Special Case: CustomerID is not found in the PaperReceipt model
            // check if the tag is supposed to contain the Customer id
            if ((customerId != null)
                    && "CustomerID".equals(element.attr(ATTR_VALUE))) {
                //set the element text to customer id and continue the loop
                element.text(customerId);
                continue;
            }
            //traverse through the paperReceiptObjs
            // to find the Object that has the function to be called
            for (Object object : paperReceiptObjs) {
                //try and get the method from the object
                method = getMethodFromObject(object, functionName);
                //if method is not null, the getter function has been found
                if (method != null) {
                    //invoke the method from the object and get its return value
                    returnedValue = invokeMethod(object, method);
                    //if the object has an attribute data-receipt-type
                    // that is equal to currency, format the string as currency
                    if (element.attr(ATTR_TYPE).equals(CURRENCY)
                            && !returnedValue.isEmpty()) {
                        returnedValue = String.valueOf(
                                ReceiptFormatter.getCurrencySymbol(
                                        language,
                                        Double.valueOf(returnedValue)));
                    }
                    //break the loop if the function has been found.
                    break;
                }
            }
            //set the text of the element
            setElementText(element, returnedValue);
            //reinitialize returnedValue to empty string
            //    in preparation to continue to traverse elements_value
            returnedValue = EMPTY_STRING;
        }
        //get the elements with attribute data-receipt-group='items'
        Elements elementsItemsGroup = elementBody.select(ATTR_GROUP_ITEMS);
        //traverse the elements_items_group
        for (Element element : elementsItemsGroup) {
            //since elements_items_group is expected to have children
            // with attributes of data-receipt-caption and data-receipt-value
            // get its children similar to what was done to the body tag
            Elements elementsItems = element.children();
            //remove the children from the element
            // this is done because the children in the group will be added
            // over and over depending on the number of items
            element.children().remove();
            //get the PaperReceiptContent from the PaperReceipt first
            // to be able to check if it is null
            PaperReceiptContent paperContent = this.rec.getReceiptContent();
            if (paperContent == null) {
                continue;
            }
            //initialize clones
            // to avoid changing the elements that will be copied more than once
            Elements elementsItemsClone = null;
            Elements elementsValueClone = null;
            //get the list of items in the paperContent
            List<ReceiptProductItem> itemlist =
                paperContent.getProductItemList();
            if (itemlist == null) {
                continue;
            }
            //traverse the itemlist
            for (ReceiptProductItem item : itemlist) {
                //clone the elements_items to avoid changing it
                // serves as a template for the item block in the receipt
                elementsItemsClone = elementsItems.clone();
                //check if there is an item discount,
                //if none remove the caption and the value
                if (item.getDiscountAmount() <= 0) {
                    elementsItemsClone
                        .select(ATTR_GROUP_ITEM_DISCOUNT).remove();
                }
                //set the text of the elements in the elements_items
                // with the attribute data-receipt-caption
                setCaptions(elementsItemsClone.select(ALL_ATTR_CAPTION),
                        resourceCaptions);
                //get all the elements in the elements_items
                // with the attribute data-receipt-value 
                elementsValueClone =
                    elementsItemsClone.select(ALL_ATTR_VALUE);
                //traverse the elements_value_clone
                for (Element elementItemValue : elementsValueClone) {
                    //get function name of the getter function
                    String functionName = GETTER_PREFIX
                        + elementItemValue.attr(ATTR_VALUE);
                    //get method from the item in the itemlist
                    method = getMethodFromObject(item, functionName);
                    //if method is not null, the function is found
                    if (method != null) {
                        //invoke the method from the item object
                        returnedValue = invokeMethod(item, method);
                        //if element has an attribute
                        //data-receipt-type='currency'
                        // format the string as a number with commas
                        if (elementItemValue.attr(ATTR_TYPE).equals(CURRENCY)) {
                            returnedValue = String.valueOf(
                                    ReceiptFormatter.getNumberWithComma(
                                            language,
                                            Double.valueOf(returnedValue)));
                        }
                    } else {
                        //if the method is null,
                        //log that the method was not found
                        tp.println("method : "
                                + functionName + " was not found.");
                    }
                    //set the text of the element
                    //to the value returned from the method
                    setElementText(elementItemValue, returnedValue);
                    //reinitialize returnedValue before continuing the loop
                    returnedValue = EMPTY_STRING;
                }
                //add each element from the elements_items_clone
                //when looping of the items is done
                // this is done one by one since Element class
                // does not support adding of Elements class
                //only another Element
                for (Element elementToAdd : elementsItemsClone) {
                    element.appendChild(elementToAdd);
                }
            }
        }
        
        // Add Mix match information to mail receipt.
        Elements elementsMMBlockGroup = elementBody.select(ATTR_GROUP_MMBLOCKS);
        for (Element element : elementsMMBlockGroup) {
            Elements elementsMMBlock = element.children();
            element.children().remove();
            
            PaperReceiptContent content = this.rec.getReceiptContent();
            if (content == null) {
                continue;
            }
            Elements elementsMMBlockClone = null;
            
            List<ReceiptMixMatchBlock> mmBlockList =
                    content.getMmBlockList();
            
            if (mmBlockList == null) {
                continue;
            }
            for (ReceiptMixMatchBlock mmBlock : mmBlockList) {
                elementsMMBlockClone = elementsMMBlock.clone();
                
                // Add mix match name
                Elements mmBlockName = elementsMMBlockClone.select(ATTR_GROUP_MMBLOCKNAME);
                for (Element mmName : mmBlockName){
                    Elements elementsName = mmName.children();
                    mmName.children().remove();
                    Elements elementsMMNameClone = null;
                    Elements elementsMMNameValueClone = null;
                    
                    elementsMMNameClone = elementsName.clone();
                    
                    setCaptions(elementsMMNameClone.select(ALL_ATTR_CAPTION),
                            resourceCaptions);
                    elementsMMNameValueClone =
                            elementsMMNameClone.select(ALL_ATTR_VALUE);
                    for (Element elementMMItemValue : elementsMMNameValueClone) {
                        addValueToElement(mmBlock, elementMMItemValue, language);
                    }
                    
                    for (Element elementToAdd1 : elementsMMNameClone) {
                        element.appendChild(elementToAdd1);
                    }
                }
                
                // Add mix match item list
                Elements elementsMMItems = elementsMMBlockClone.select(ATTR_GROUP_MMITEMS);
                for (Element mmItem : elementsMMItems) {
                    Elements elementsMMItem = mmItem.children();
                    mmItem.children().remove();
                    
                    Elements elementsMMItemClone = null;
                    Elements elementsMMItemValueClone = null;
                    
                    List<ReceiptProductItem> mmItemList = mmBlock.getProductItemList();
                    if (mmItemList == null) {
                        continue;
                    }
                    for (ReceiptProductItem item : mmItemList) {
                        elementsMMItemClone = elementsMMItem.clone();
                        // If item discount is 0, remove this element.
                        if (item.getDiscountAmount() <= 0) {
                            elementsMMItemClone
                                .select(ATTR_GROUP_ITEM_DISCOUNT).remove();
                        }
                        setCaptions(elementsMMItemClone.select(ALL_ATTR_CAPTION),
                                resourceCaptions);
                        elementsMMItemValueClone =
                                elementsMMItemClone.select(ALL_ATTR_VALUE);
                        
                        for (Element elementMMItemValue : elementsMMItemValueClone) {
                            addValueToElement(item, elementMMItemValue, language);
                        }
                        
                        for (Element elementToAdd1 : elementsMMItemClone) {
                            element.appendChild(elementToAdd1);
                        }
                    }
                }
                
                // Add mix match block financial information.
                Elements elementsMMFinan = elementsMMBlockClone.select(ATTR_GROUP_MMFINAN);
                for (Element mmFina : elementsMMFinan){
                    Elements elementsFinan = mmFina.children();
                    mmFina.children().remove();
                    Elements elementsMMFinanClone = null;
                    Elements elementsMMFinanValueClone = null;
                    
                    elementsMMFinanClone = elementsFinan.clone();
                    
                    setCaptions(elementsMMFinanClone.select(ALL_ATTR_CAPTION),
                            resourceCaptions);
                    elementsMMFinanValueClone =
                            elementsMMFinanClone.select(ALL_ATTR_VALUE);
                    for (Element elementMMItemValue : elementsMMFinanValueClone) {
                        addValueToElement(mmBlock, elementMMItemValue, language);
                    }
                    
                    for (Element elementToAdd1 : elementsMMFinanClone) {
                        element.appendChild(elementToAdd1);
                    }
                }
            }
        }
        //get the elements with attribute data-receipt-group='ads'
        Elements elementsAdsGroup = elementBody.select(ATTR_GROUP_ADS);
        //traverse the elements_ads_group
        for (Element element : elementsAdsGroup) {
            //since elements_items_group is expected to have children
            // with attributes of data-receipt-caption and data-receipt-value
            // get its children similar to what was done to the body tag
            Elements elementsAds = element.children();
            //remove the children from the element
            // this is done because the children in the group will be added
            // over and over depending on the number of ads
            element.children().remove();
            //get the PaperReceiptHeader from the PaperReceipt first
            // to be able to check if it is null
            PaperReceiptHeader paperHeader = this.rec.getReceiptHeader();
            if (paperHeader == null) {
                continue;
            }
            //initialize clones
            // to avoid changing the elements that will be copied more than once
            Elements elementsAdsClone = null;
            Elements elementsValueClone = null;
            //get the list of ads in the paperHeader
            List<String> adsList = paperHeader.getCommercialList();
            if (adsList == null) {
                continue;
            }
            //traverse the adsList
            for (String ad : adsList) {
                //clone the elements_ads to avoid changing it
                // serves as a template for the item block in the receipt
                elementsAdsClone = elementsAds.clone();
                //set the text of the elements in the elements_ads
                // with the attribute data-receipt-caption
                setCaptions(elementsAdsClone.select(ALL_ATTR_CAPTION),
                        resourceCaptions);
                //get all the elements in the elements_ads
                // with the attribute data-receipt-value
                elementsValueClone =
                    elementsAdsClone.select(ALL_ATTR_VALUE);
                //traverse the elements_value_clone
                for (Element elementAdsValue : elementsValueClone) {
                    //set the text to the ad
                    elementAdsValue.text(ad);
                }
                //add each element from the elements_items_clone when
                //looping of the items is done
                // this is done one by one since Element class
                // does not support adding of Elements class only
                // another Element
                for (Element elementToAdd : elementsAdsClone) {
                    element.appendChild(elementToAdd);
                }
            }
        }
        tp.methodExit();
        //return the HTML format of the document clone
        return documentClone.html();
    }

    private void addValueToElement(Object obj, Element element, String language) throws DaoException{
        String functionName = GETTER_PREFIX
                + element.attr(ATTR_VALUE);
        
        Method method = getMethodFromObject(obj, functionName);
        
        String returnedValue = EMPTY_STRING;
        if (method != null) {
            returnedValue = invokeMethod(obj, method);
            if (element.attr(ATTR_TYPE).equals(CURRENCY)) {
                returnedValue = String.valueOf(
                        ReceiptFormatter.getNumberWithComma(
                                language,
                                Double.valueOf(returnedValue)));
            }
        } else {
            tp.println("method : "
                    + functionName + " was not found.");
        }
        setElementText(element, returnedValue);
    }
    /**
     * get the Resource with the corresponding Captions to use.
     * @param language - Language code string of the language to use
     * @return Resource that contains Caoptions. NULL if not found.
     */
    private ResourceBundle getLocaleResource(final String language) {
        Locale locale = null;
        if (language == null || !"en".equals(language)) {
            locale = Locale.JAPANESE;
        } else {
            locale = Locale.ENGLISH;
        }

        return ResourceBundle.getBundle(RECEIPT_LOCALE, locale);
    }

    /**
     * Sets the captions.
     * @param elementCaptions - Element array that contains only the
     *                              tags with attribute data-receipt-caption
     * @param captionResource - Resource that contains caption labels
     * @throws DaoException - thrown when exception occurs
     */
    private void setCaptions(final Elements elementCaptions,
            final ResourceBundle captionResource)throws DaoException {

        //If resource is null, throw an exception
        if (captionResource == null) {
            throw new DaoException("Resource Bundle for captions is null");
        }

        for (Element elementItemCaption : elementCaptions) {

            try {
                elementItemCaption.text(captionResource.getString(
                        elementItemCaption.attr(ATTR_CAPTION)));
            } catch (NullPointerException e) {
                //if no key is found, throw an exception
                throw new DaoException("Caption Key not found.\n"
                        + e.getMessage());
            }
        }
    }
    /**
     * sets the text of the given element.
     * @param element - Element in HTML to set the string of
     * @param text - text to set
     */
    private void setElementText(final Element element, final String text) {
        String textToSet = text;
        //check if the text contains only spaces or is an empty string
        if (text.trim().equals(EMPTY_STRING)) {
            //set the text of to that of an empty value
            // -cannot be empty string because HTML format will be destroyed
            textToSet = EMPTY_VALUE;
        }
        element.text(textToSet);
    }

    /**
     * Calls the given method of the object.
     * @param obj - object in which to call the method from
     * @param method - method to be called
     * @return    String value of the return of the method
     * @throws DaoException - thrown when exception occurs
     */
    private String invokeMethod(final Object obj, final Method method)
    throws DaoException {
        //check if object or method is null
        if (obj == null || method == null) {
            return EMPTY_STRING;
        }
        String retString = EMPTY_STRING;
        try {
             Object returnObj = method.invoke(obj);
             if (null != returnObj) {
                 retString = returnObj.toString();
             }
        } catch (Exception e) {
            throw new DaoException("Failed to invoke Method.\n"
                    + e.getMessage());
        }

        //if return is null, return empty string
        // if return is not null, return string value of the object
        return retString;
    }

    /**
     * gets an array of the direct objects contained in the PaperReceipt.
     * @return array of direct objects in the PaperReceipt
     * @throws DaoException - thrown when exception occurs
     */
    private List<Object> getObjectsFromPaperReceipt() throws DaoException {
        //get class methods of PaperReceipt
        Method[] methods = this.rec.getClass().getMethods();

        List<Object> paperreceiptobjects = new ArrayList<Object>();

        for (Method method : methods) {

            //if method is not a getter function, skip
            if (!method.getName().startsWith("get")) {
                continue;
            }

            //if method is getClass, skip
            if ("getClass".equals(method.getName())) {
                continue;
            }

            //invoke the getter function and add it to the array
            try {
                paperreceiptobjects.add(method.invoke(this.rec));
            } catch (Exception e) {
                throw new DaoException("failed to get Class Methods.\n"
                        + e.getMessage());
            }
        }

        return paperreceiptobjects;
    }

    /**
     * gets a Method from an object with the specified function name.
     * @param obj - object to find the method in
     * @param functionName - name of the function to retrieve
     * @return    Method object of the found method
     * @throws DaoException - thrown when exception occurs
     */
    private Method getMethodFromObject(final Object obj,
            final String functionName)
    throws DaoException {
        if (obj == null || functionName == null) {
            return null;
        }

        Method method = null;
        try {
            method = obj.getClass().getMethod(functionName);

        } catch (NoSuchMethodException e) {
        	LOGGER.logAlert(PROG_NAME, PROG_NAME+".getMethodFromObject", Logger.RES_EXCEP_GENERAL, "Failed to get method. "+e.getMessage());
        } catch (Exception e) {
            throw new DaoException("failed to retrieve Function Method.\n"
                    + e.getMessage());
        }

        return method;
    }
}
