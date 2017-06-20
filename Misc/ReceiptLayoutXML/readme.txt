These XML files are format receipt by XML layout files.
RES-5861 on JIRA.
The path of these XML files are defined in web.xml as following.
If the file exist in the path, receipt service will use XML layout file to format receipt.
If it is failed to find XML file, receipt service will use current method (format in source code) to format receipt.
<env-entry>
    <env-entry-name>nrRcptFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\ncr\res\cust\para\NormalReceiptFormat.xml</env-entry-value>
  </env-entry>
  <env-entry>
    <env-entry-name>creditSlipRcptFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\ncr\res\cust\para\CreditSlipReceiptFormat.xml</env-entry-value>
  </env-entry>
  <env-entry>
    <env-entry-name>vdRcptFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\ncr\res\cust\para\VoidReceiptFormat.xml</env-entry-value>
  </env-entry>
  <env-entry>
    <env-entry-name>rnRcptForCustFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\ncr\res\cust\para\ReturnReceiptCustFormat.xml</env-entry-value>
  </env-entry>
  <env-entry>
    <env-entry-name>rnRcptForShopFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\ncr\res\cust\para\ReturnReceiptShopFormat.xml</env-entry-value>
  </env-entry>
  <env-entry>
    <env-entry-name>rnRcptForCardCompanyFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\ncr\res\cust\para\ReturnReceiptCardCompFormat.xml</env-entry-value>
  </env-entry>
 <!-- 売上表レポート利用のファーマットXMLファイル -->
  <env-entry>
    <env-entry-name>ReportFormatNewPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\software\ncr\res\custom\ReportFormatNew.xml</env-entry-value>
  </env-entry>
 <!-- クレジットカード会社控レシート利用のファーマットXMLファイル -->
  <env-entry>
    <env-entry-name>creditSlipRcptCardCompFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\software\ncr\res\custom\CreditSlipReceiptCardCompFormat.xml</env-entry-value>
  </env-entry>
 <!-- クレジット商品控レシート利用のファーマットXMLファイル -->
  <env-entry>
    <env-entry-name>creditSlipRcptShopFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\software\ncr\res\custom\CreditSlipReceiptShopFormat.xml</env-entry-value>
  </env-entry>
 <!-- 前受金商品貼付レシート利用のファーマットXMLファイル  -->
  <env-entry>
    <env-entry-name>advRcptCommodityFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\software\ncr\res\custom\AdvanceReceiptCommodityFormat.xml</env-entry-value>
  </env-entry>
<!-- COMMON_前受金一括取消レシート利用のファーマットXMLファイル -->
   <env-entry>
    <env-entry-name>advanceVoidRcptFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\software\ncr\res\custom\AdvanceVoidReceiptFormat.xml</env-entry-value>
  </env-entry>
<!-- 領収証利用のファーマットXMLファイル -->
  <env-entry>
    <env-entry-name>rnRcptForSummaryFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\software\ncr\res\custom\SummaryReceiptFormat.xml</env-entry-value>
  </env-entry>
<!-- 出金と入金と両替利用のファーマットXMLファイル -->
  <env-entry>
    <env-entry-name>tcRcptFormatPath</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>d:\software\ncr\res\custom\TenderControlReceiptFormat.xml</env-entry-value>
  </env-entry>