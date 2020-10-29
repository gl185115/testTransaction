<%@ page language="java" pageEncoding="utf-8"%><%@page
	import="java.sql.*"%><%@page
	import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
	import="java.util.Date"
    import="java.util.ArrayList"
	import="java.text.SimpleDateFormat"%>
<%!
final String ERR_01_TERMINALID = "端末が既に存在します。<br>端末番号を確認後、再度登録を実行してください。";
final String ERR_02_INTERNAL = "内部エラーが発生しました。<br>システム担当者に確認してください。";
final String INFO_01_INSERT = "端末の新規登録に成功しました。";
final String CONFIRM_01_INSERT = "端末を登録してよろしいですか。";

final static int USB_PRINTER_PORT = 8080;
final static int NETWORK_PRINTER_PORT = 9100;
int defaultPrinterPort = USB_PRINTER_PORT;

ArrayList<String> PRINTER_TYPE = new ArrayList<String>() {{add("USBプリンター"); add("ネットワークプリンター");}};
%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String sqlStr = "";
    String errString = "";
    String infoString = "";
    Boolean isUpdateAreaHide = true;
    String user = ""; //ログインユーザー名

    //ログインユーザー名取得
	try {
	    user = request.getRemoteUser() != null ? request.getRemoteUser() : "";
	} catch (Exception e) {
	}

    // attribute
    ArrayList<String> attributeIdList = new ArrayList<String>();
    ArrayList<String> attrDescriptionList = new ArrayList<String>();

    // queue
    ArrayList<String> queueIdList = new ArrayList<String>();
    ArrayList<String> queueNameList = new ArrayList<String>();

    // tillid
    ArrayList<String> tillIdList = new ArrayList<String>();

    // プリンタータイプの値をとる
    String formPrinterType = request.getParameter("printerType");
    if(PRINTER_TYPE.contains(formPrinterType)){
        switch(formPrinterType){
        case "USBプリンター":
            defaultPrinterPort = USB_PRINTER_PORT;
            break;
        case "ネットワークプリンター":
            defaultPrinterPort = NETWORK_PRINTER_PORT;
            break;
        default:
            defaultPrinterPort = USB_PRINTER_PORT;
        }
    }

    if (request.getParameter("searchCompanyID") != null && request.getParameter("searchCompanyID").length() != 0) {
        JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
        Connection connection = dbManager.getConnection();
        sqlStr = "SELECT att.AttributeId,att.Description FROM RESMaster.dbo.PRM_DEVICE_ATTRIBUTE att";
        PreparedStatement psSelect = connection.prepareStatement(sqlStr);
        ResultSet rsSelect = psSelect.executeQuery();
        while(rsSelect.next()) {
            attributeIdList.add(rsSelect.getString("AttributeId"));
            attrDescriptionList.add(rsSelect.getString("Description"));
        }
        psSelect.close();

        sqlStr = "SELECT q.Id ,q.DisplayName FROM RESMaster.dbo.PRM_QUEUEBUSTER_LINK q WHERE q.StoreId=? and q.CompanyId=? and q.Status<>'Deleted'";
        psSelect = connection.prepareStatement(sqlStr);
        psSelect.setString(1, request.getParameter("searchStoreID").toString());
        psSelect.setString(2, request.getParameter("searchCompanyID").toString());
        rsSelect = psSelect.executeQuery();
        while(rsSelect.next()) {
        	queueIdList.add(rsSelect.getString("Id"));
        	queueNameList.add(rsSelect.getString("DisplayName"));
        }
        psSelect.close();

        sqlStr = "SELECT TillId"
        		+ " FROM RESMaster.dbo.MST_TILLIDINFO"
        		+ " WHERE CompanyId=? and StoreId=?;";
        psSelect = connection.prepareStatement(sqlStr);
        psSelect.setString(1, request.getParameter("searchCompanyID").toString());
        psSelect.setString(2, request.getParameter("searchStoreID").toString());
        rsSelect = psSelect.executeQuery();
        while(rsSelect.next()) {
        	tillIdList.add(rsSelect.getString("TillId"));
        }
        psSelect.close();

        isUpdateAreaHide = false;

        psSelect.close();
        connection.close();

    } else if (request.getParameter("addCompanyID") != null && request.getParameter("addCompanyID").length() != 0) {
        JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
        Connection connection = dbManager.getConnection();
        if (request.getParameter("terminalID") != null) {

        	sqlStr = "SELECT COUNT(terminalid) terminalIdNum"
        			+ " FROM RESMaster.dbo.MST_DEVICEINFO"
        			+ " WHERE companyID=? and storeID=? and terminalId=?";
            PreparedStatement psSelect = connection.prepareStatement(sqlStr);
            psSelect.setString(1, request.getParameter("addCompanyID").toString());
            psSelect.setString(2, request.getParameter("addStoreID").toString());
            psSelect.setString(3, request.getParameter("terminalID").toString());
            ResultSet rsSelect = psSelect.executeQuery();

            if(!rsSelect.next() || rsSelect.getInt("terminalIdNum") > 0) {
                psSelect.close();
                errString = ERR_01_TERMINALID;
                connection.close();
            } else {
                psSelect.close();

                sqlStr = "SELECT TodayDate"
                        + " FROM RESMaster.dbo.MST_BIZDAY"
                        + " WHERE companyID=?";
                psSelect = connection.prepareStatement(sqlStr);
                psSelect.setString(1, request.getParameter("addCompanyID").toString());
                rsSelect = psSelect.executeQuery();

                Date nowDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String today = formatter.format(nowDate);
                if (rsSelect.next()) {
                    today = rsSelect.getString("TodayDate");
                }

                sqlStr = /* MST_DEVICEINFO */
                        "INSERT INTO RESMaster.dbo.MST_DEVICEINFO"
                        + " (CompanyId, StoreId, TerminalId, Training, DeviceName, AttributeId, PrinterId, TillId, LinkPosTerminalId, LinkQueueBuster, SendLogFile, SaveLogFile, AutoUpload, Status, subNum1, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode)"
                        + " VALUES (?, ?, ?, 0, ?, ?, ?, ?, '0', ?, 3, 40, 0, 'Active', 1, 0, CURRENT_TIMESTAMP, 'settingDevice', ?, 0, CURRENT_TIMESTAMP, 'settingDevice', ?),"
                        + "        (?, ?, ?, 1, ?, ?, ?, ?, '0', ?, 3, 40, 0, 'Active', 1, 0, CURRENT_TIMESTAMP, 'settingDevice', ?, 0, CURRENT_TIMESTAMP, 'settingDevice', ?)"
                        /* MST_TERMINALINFO */
                        + " UPDATE RESMaster.dbo.MST_TERMINALINFO"
                        + "  SET FloorId=?, TerminalName=?, IPAddress=?, StoreClass=?, TerminalType=?, TillType=?, RelationType=?, LogoFileName=?, InshiFileName=?, SubCode2=?, UpdCount=UpdCount+1,UpdDate=CURRENT_TIMESTAMP, UpdAppId='settingDevice',UpdOpeCode=?,SubCode1=?"
                        + "  WHERE CompanyId=? AND StoreId=? AND TerminalId=?"
                        + "  IF @@ROWCOUNT = 0"
                        + "  BEGIN"
                        + "  INSERT INTO RESMaster.dbo.MST_TERMINALINFO"
                        + "  (CompanyId, StoreId, TerminalId, FloorId, TerminalName, IPAddress, StoreClass, TerminalType, TillType, RelationType, LogoFileName, InshiFileName, SubCode2, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode,"
                        + "   ConnectionFlag1,ConnectionFlag2,ConnectionFlag3,ConnectionFlag4,ConnectionFlag5,ConnectionFlag6,ConnectionFlag7,ConnectionFlag8,ConnectionFlag9,ConnectionFlag10,ConnectionFlag11,ConnectionFlag12,ConnectionFlag13,ConnectionFlag14,ConnectionFlag15,SubCode1)"
                        + "  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '0', CURRENT_TIMESTAMP, 'settingDevice', ?, 0, CURRENT_TIMESTAMP, 'settingDevice', ?,"
                        + "         '0','0','0','0','0','0','0','0','0','0','0','0','0','0','0',?)"
                        + "  END"
                        /* MST_PRINTERINFO */
                        + " UPDATE RESMaster.dbo.MST_PRINTERINFO"
                        + "  SET PrinterName=?, Description=?, IpAddress=?, PortNumTcp=?, PortNumUdp='0', UpdCount=UpdCount+1, UpdDate=CURRENT_TIMESTAMP, UpdAppId='settingDevice', UpdOpeCode=?"
                        + "  WHERE CompanyId=? AND StoreId=? AND PrinterId=?"
                        + "  IF @@ROWCOUNT = 0"
                        + "  BEGIN"
                        + "  INSERT INTO RESMaster.dbo.MST_PRINTERINFO"
                        + "  (CompanyId, StoreId, PrinterId, PrinterName, Description, IpAddress, PortNumTcp, PortNumUdp, Status, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode)"
                        + "  VALUES (?, ?, ?, ?, ?, ?, ?, 0, 'Active', 0, CURRENT_TIMESTAMP,  'settingDevice', ?, 0, CURRENT_TIMESTAMP, 'settingDevice', ?)"
                        + "  END"
                        /* MST_TILLIDINFO */
                        + " UPDATE RESMaster.dbo.MST_TILLIDINFO"
                        + "  SET TerminalId=?, BusinessDayDate=?, SodFlag='1', EodFlag='0', DeleteFlag='0', UpdCount=UpdCount+1, UpdDate=CURRENT_TIMESTAMP, UpdAppId='settingDevice', UpdOpeCode=?"
                        + "  WHERE CompanyId=? AND StoreId=? AND TillId=?;";
                PreparedStatement psIns = connection.prepareStatement(sqlStr);
                // MST_DEVICEINFO(normal)
                psIns.setString(1, request.getParameter("addCompanyID"));
                psIns.setString(2, request.getParameter("addStoreID"));
                psIns.setString(3, request.getParameter("terminalID"));
                psIns.setString(4, request.getParameter("terminalName"));
                psIns.setString(5, request.getParameter("attributeList"));
                psIns.setString(6, request.getParameter("terminalID"));
                psIns.setString(7, request.getParameter("tillList"));
                psIns.setString(8, request.getParameter("queueList"));
                psIns.setString(9, user);
                psIns.setString(10, user);
                // MST_DEVICEINFO(trainning)
                psIns.setString(11, request.getParameter("addCompanyID"));
                psIns.setString(12, request.getParameter("addStoreID"));
                psIns.setString(13, request.getParameter("terminalID"));
                psIns.setString(14, request.getParameter("terminalName"));
                psIns.setString(15, request.getParameter("attributeList"));
                psIns.setString(16, request.getParameter("terminalID"));
                psIns.setString(17, request.getParameter("tillList"));
                psIns.setString(18, request.getParameter("queueList"));
                psIns.setString(19, user);
                psIns.setString(20, user);
                // MST_TERMINALINFO(update)
                psIns.setString(21, request.getParameter("floorId"));
                psIns.setString(22, request.getParameter("terminalName"));
                psIns.setString(23, request.getParameter("ipAddress"));
                psIns.setString(24, request.getParameter("storeClass"));
                psIns.setString(25, request.getParameter("terminalType"));
                psIns.setString(26, request.getParameter("tillType"));
                psIns.setString(27, request.getParameter("relationType"));
                psIns.setString(28, request.getParameter("logPath"));
                psIns.setString(29, request.getParameter("inshiPath"));
                psIns.setString(30, request.getParameter("salesPromotionBMPPath"));

                psIns.setString(31, user);
                psIns.setString(32, request.getParameter("ter_ReceiptCardInshiFilePath"));
                psIns.setString(33, request.getParameter("addCompanyID"));
                psIns.setString(34, request.getParameter("addStoreID"));
                psIns.setString(35, request.getParameter("terminalID"));
                // MST_TERMINALINFO(insert)
                psIns.setString(36, request.getParameter("addCompanyID"));
                psIns.setString(37, request.getParameter("addStoreID"));
                psIns.setString(38, request.getParameter("terminalID"));
                psIns.setString(39, request.getParameter("floorId"));
                psIns.setString(40, request.getParameter("terminalName"));
                psIns.setString(41, request.getParameter("ipAddress"));
                psIns.setString(42, request.getParameter("storeClass"));
                psIns.setString(43, request.getParameter("terminalType"));
                psIns.setString(44, request.getParameter("tillType"));
                psIns.setString(45, request.getParameter("relationType"));
                psIns.setString(46, request.getParameter("logPath"));
                psIns.setString(47, request.getParameter("inshiPath"));
                psIns.setString(48, request.getParameter("salesPromotionBMPPath"));
                psIns.setString(49, user);
                psIns.setString(50, user);
                psIns.setString(51, request.getParameter("ter_ReceiptCardInshiFilePath"));
                // MST_PRINTERINFO(update)
                psIns.setString(52, request.getParameter("printerName"));
                psIns.setString(53, request.getParameter("printerDescription"));
                if (request.getParameter("ipAddressPrint") == null) {
                    psIns.setString(54, "");
                } else {
                    psIns.setString(54, request.getParameter("ipAddressPrint"));
                }
                psIns.setInt(55, defaultPrinterPort);

                psIns.setString(56, user);
                psIns.setString(57, request.getParameter("addCompanyID"));
                psIns.setString(58, request.getParameter("addStoreID"));
                psIns.setString(59, request.getParameter("terminalID")); // printerid
                // MST_PRINTERINFO(insert)
                psIns.setString(60, request.getParameter("addCompanyID"));
                psIns.setString(61, request.getParameter("addStoreID"));
                psIns.setString(62, request.getParameter("terminalID")); // printerid
                psIns.setString(63, request.getParameter("printerName"));
                psIns.setString(64, request.getParameter("printerDescription"));
                if (request.getParameter("ipAddressPrint") == null) {
                    psIns.setString(65, "");
                } else {
                    psIns.setString(65, request.getParameter("ipAddressPrint"));
                }

                psIns.setInt(66, defaultPrinterPort);

                psIns.setString(67, user);
                psIns.setString(68, user);
                // MST_TILLIDINFO(update)
                psIns.setString(69, request.getParameter("terminalID"));
                psIns.setString(70, today);
                psIns.setString(71, user);
                psIns.setString(72, request.getParameter("addCompanyID"));
                psIns.setString(73, request.getParameter("addStoreID"));
                psIns.setString(74, request.getParameter("tillList"));

                try {
                    int rsIns = psIns.executeUpdate();
                    connection.commit();
                    infoString = INFO_01_INSERT;
                } catch (Exception e) {
                	errString = ERR_02_INTERNAL + e.getMessage();
                    //out.print(e.getMessage());
                } finally {
                    psSelect.close();
                    psIns.close();
                    connection.close();
                }
            }
        }
	} else {
		;
	}
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="./css/default.css">
    <title>新規登録</title>
</head>
<script type="text/javascript">

function searchDev(obj) {
    var companyId = storesearch.document.getElementById('companyidlist').value;
    var storeId = storesearch.document.getElementById('storeidlist').value;
    document.getElementById('searchCompanyID').value = companyId;
    document.getElementById('searchStoreID').value = storeId;

    var myform = document.getElementById('searchform');
    myform.submit();
}
window.onload = function() {
	<%
	if (request.getParameter("searchCompanyID") != null && request.getParameter("searchCompanyID").length() != 0) {
	    out.print("storesearch.setSelectIndex(" + request.getParameter("searchCompanyID").toString() + ", " + request.getParameter("searchStoreID").toString() + ");");
	}
	%>
};
</script>
<body class="res-maincontent">
    <!-- jQuery -->
    <script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
    <!-- ダイアログ共通 -->
    <script type="text/javascript" src="./js/DialogMessage.js"></script>
端末登録<br><br>
 ※下記すべて入力し、最後に登録を押下してください。<br><br>
  <iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp"></iframe>
  <form action="DeviceAdd.jsp" method="post" id="searchform" onsubmit="return false;">
    <input type="hidden" name="searchCompanyID" id="searchCompanyID">
    <input type="hidden" name="searchStoreID" id="searchStoreID">
    <button class="res-small-green" onclick="searchDev(this)">次へ</button>
  </form>
  <label class="res-err-msg"><%out.println(errString); %></label>
  <label class="res-info-msg"><%out.println(infoString); %></label>
  <form action="DeviceAdd.jsp" method="post" id="updateform" onsubmit="return false;">
    <input type="hidden" name="addCompanyID" id="addCompanyID" value="<%=request.getParameter("searchCompanyID")%>">
    <input type="hidden" name="addStoreID" id="addStoreID" value="<%=request.getParameter("searchStoreID")%>">
    <div id="updateArea"<%if(isUpdateAreaHide) {out.print("style=\"display:none\""); }%>>
      <div class="panel">
        <table border="0" cellspacing="4" cellpadding="4" style="width:100%">
          <tr>
            <td align="right">企業番号 ： </td>
            <td align="left">
              <input type="text" disabled name="companyID" id="companyID" size=4 value="<%=request.getParameter("searchCompanyID")%>">
            </td>
          </tr>
          <tr>
            <td align="right">店番号 ： </td>
            <td align="left">
              <input type="text" disabled name="storeID" id="storeID" size=6 value="<%=request.getParameter("searchStoreID")%>">
            </td>
          </tr>
          <tr>
            <td align="right">端末番号 ： </td>
            <td align="left">
              <input maxlength="4" type="text" name="terminalID" id="terminalID" size=4 required pattern="\d{4}">(半角数字4桁を入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">端末名 ： </td>
            <td align="left">
              <input maxlength="15" type="text" name="terminalName" id="terminalName" size=30 required pattern=".{0,15}">(15文字以内で入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">フロアID ： </td>
            <td align="left">
              <input maxlength="2" type="text" name="floorId" id="floorId" size=2 required pattern="\d{2}">(半角数字2桁で入力してください)
            </td>
          </tr>
          <tr>
            <td align="right">IPアドレス ： </td>
            <td align="left">
              <input maxlength="15" type="text" name="ipAddress" id="ipAddress" size=15 required pattern="^\d{1,3}(\.\d{1,3}){3}$"></td>
          </tr>
          <tr>
            <td align="right">店舗種別 ： </td>
            <td align="left">
              <input maxlength="1" type="text" name="storeClass" id="storeClass" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
            </td>
          </tr>
          <tr>
            <td align="right">端末タイプ ： </td>
            <td align="left">
              <input maxlength="1" type="text" name="terminalType" id="terminalType" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
            </td>
          </tr>
          <tr>
            <td align="right">ドロワータイプ ： </td>
            <td align="left">
              <input maxlength="1" type="text" name="tillType" id="tillType" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
            </td>
          </tr>
          <tr>
            <td align="right">ドロワーID ： </td>
            <td align="left">
              <select name="tillList" id="tillList" required style="width:50%">
              <%
                for (int i=0;i<tillIdList.size();i++) {
                  out.print("<option value=\"" + tillIdList.get(i) + "\"");
                  out.println(">" + tillIdList.get(i) + "</option>");
                }
              %>
              </select>
            </td>
          </tr>
          <tr>
            <td align="right">関連タイプ ： </td>
            <td align="left">
              <input maxlength="1" type="text" name="relationType" id="relationType" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
            </td>
          </tr>

          <tr>
            <td align="right">プリンター名 ： </td>
            <td align="left">
              <input maxlength="40" type="text" name="printerName" id="printerName" size=40 pattern=".{0,20}">(20文字以内で入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">プリンター説明 ： </td>
            <td align="left">
              <input maxlength="40" type="text" name="printerDescription" id="printerDescription" size=40 required pattern=".{0,20}">(20文字以内で入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">プリンター種別 ： </td>
            <td>
              <select name="printerType" id="printerType" onChange="changePrinterType(this)" required style="width:50%">
              <%
                for (int i=0;i<PRINTER_TYPE.size();i++) {
                  out.print("<option value=\"" + PRINTER_TYPE.get(i) + "\"");
                  out.println(">" + PRINTER_TYPE.get(i) + "</option>");
                }
              %>
              </select>
            </td>
          </tr>
          <tr id="printerIpRow" style="display:none">
              <td align="right">プリンターIPアドレス ： </td>
              <td align="left">
                  <input maxlength="15" type="text" name="ipAddressPrint" id="ipAddressPrint" size=15 disabled required pattern="^\d{1,3}(\.\d{1,3}){3}$">
              </td>
          </tr>
          <tr>
            <td align="right">属性番号 ： </td>
            <td>
              <select name="attributeList" id="attributeList" required style="width:50%">
              <%
                for (int i=0;i<attributeIdList.size();i++) {
                  out.print("<option value=\"" + attributeIdList.get(i) + "\"");
                  out.println(">" + attributeIdList.get(i) +" : " + attrDescriptionList.get(i) +"</option>");
                }
              %>
              </select>
            </td>
          </tr>
          <tr>
            <td align="right">キューID ： </td>
            <td align="left">
              <select name="queueList" id="queueList" required style="width:50%">
              <%
                for (int i=0;i<queueIdList.size();i++) {
                  out.print("<option value=\"" + queueIdList.get(i) + "\"");
                  out.println(">" + queueIdList.get(i) +" : " + queueNameList.get(i) +"</option>");
                }
              %>
              </select>
            </td>
          </tr>
          
          <tr>
            <td align="right">ロゴファイルパス ： </td>
            <td align="left">
              <input maxlength="255" type="text" name="logPath" id="logPath" size=40 required pattern=".{1,255}">(255文字以内で入力してください)
            </td>
          </tr>
          <tr>
            <td align="right">印紙ファイルパス ： </td>
            <td align="left">
              <input maxlength="255" type="text" name="inshiPath" id="inshiPath" size=40 required pattern=".{1,255}">(255文字以内で入力してください)
            </td>
          </tr>
          <tr>
            <td align="right" width="175px">領収証印紙ファイルパス ： </td>
            <td align="left">
                <input maxlength="100" type="text" name="ter_ReceiptCardInshiFilePath" id="ter_ReceiptCardInshiFilePath"
                       size=40 required pattern=".{1,100}">(100文字以内で入力してください)
            </td>
          </tr>
          <tr>
            <td align="right" valign="top">販促ビットマップパス ： </td>
            <td align="left">
                <input maxlength="100" type="text" name="salesPromotionBMPPath" id="salesPromotionBMPPath"
                       size=40 required pattern=".{1,98}\\\\">(100文字以内で最後に\\をつけて入力してください)
            </td>
          </tr>
        </table>
      </div>
      <div align="right">
        <input type="button" value="登録" id="insertDev" name="insertDev" class="res-big-green">
      </div>
    </div>
    <button id="fakeButton" style="display:none"></button>
  </form>


</body>
<script type="text/javascript">
function changePrinterType(obj) {
    if (obj.value == 'USBプリンター') {
        document.getElementById('ipAddressPrint').disabled = true;
        document.getElementById('printerIpRow').style.display = 'none';
    } else {
        document.getElementById('ipAddressPrint').disabled = false;
        document.getElementById('printerIpRow').style.display = '';
	}
}

jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#insertDev').click(function(e){
        var myform = document.getElementById('updateform');
        if (myform.checkValidity() == false) {
        	document.getElementById('fakeButton').click();
            return;
        }
        showDialog(
            "タイトル：未使用",
            <%='\'' + CONFIRM_01_INSERT + '\''%>,
            ButtonYesNo,
            function() {
                //「いいえ」を押したときの処理
            },
            function() {
                //「はい」を押したときの処理
                myform.submit();
            }
        );
    });
});
</script>
<HEAD>
<meta http-equiv=”Pragma” content=”no-cache”>
<meta http-equiv=”Cache-Control” content=”no-cache”>
</HEAD>
</html>
<%--
	}
--%>