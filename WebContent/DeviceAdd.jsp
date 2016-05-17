<%@ page language="java" pageEncoding="utf-8"%><%@page
	import="java.sql.*"%><%@page
	import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
	import="java.util.Date"
    import="java.util.ArrayList"
	import="java.text.SimpleDateFormat"%>
<%!
final String ERR_01_TERMINALID = "端末が既に存在します。";
final String ERR_02_INTERNAL = "内部エラーが発生しました。";
final String INFO_01_INSERT = "端末の新規登録に成功しました。";
final String CONFIRM_01_INSERT = "端末を登録してよろしいですか。";
%>
<%
    String sqlStr = "";
    String errString = "";
    String infoString = "";
    Boolean isUpdateAreaHide = true;
    
    // attribute
    ArrayList<String> attributeIdList = new ArrayList<String>();
    ArrayList<String> attrDescriptionList = new ArrayList<String>();

    // queue
    ArrayList<String> queueIdList = new ArrayList<String>();
    ArrayList<String> queueNameList = new ArrayList<String>();

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

//        sqlStr = "SELECT q.Id ,q.DisplayName FROM RESMaster.dbo.PRM_QUEUEBUSTER_LINK q WHERE q.StoreId=? and q.Status<>'Deleted'";
        sqlStr = "SELECT q.Id ,q.DisplayName FROM RESMaster.dbo.PRM_QUEUEBUSTER_LINK q WHERE q.StoreId=? and q.CompanyId=? and q.Status<>'Deleted'";
        psSelect = connection.prepareStatement(sqlStr);
        psSelect.setString(1, request.getParameter("searchStoreID").toString());
        psSelect.setString(2, request.getParameter("searchCompanyID").toString());
        rsSelect = psSelect.executeQuery();
        while(rsSelect.next()) {
        	queueIdList.add(rsSelect.getString("Id"));
        	queueNameList.add(rsSelect.getString("DisplayName"));
        }
        
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
                sqlStr = /* MST_DEVICEINFO */
                        "INSERT INTO RESMaster.dbo.MST_DEVICEINFO"
                        + " (CompanyId, StoreId, TerminalId, Training, DeviceName, AttributeId, PrinterId, TillId, LinkPosTerminalId, LinkQueueBuster, SendLogFile, SaveLogFile, AutoUpload, Status, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode)"
                        + " VALUES (?, ?, ?, 0, ?, ?, ?, ?, '0', ?, 3, 40, 0, 'Active', 0, CURRENT_TIMESTAMP, 'system', 'system', 0, CURRENT_TIMESTAMP, 'system', 'system'),"
                        + "        (?, ?, ?, 1, ?, ?, ?, ?, '0', ?, 3, 40, 0, 'Active', 0, CURRENT_TIMESTAMP, 'system', 'system', 0, CURRENT_TIMESTAMP, 'system', 'system')"
                        /* MST_TERMINALINFO */
                        + " UPDATE RESMaster.dbo.MST_TERMINALINFO"
                        + "  SET FloorId=?, TerminalName=?, IPAddress=?, StoreClass=?, TerminalType=?, TillType=?, RelationType=?, LogoFileName=?, InshiFileName=?, UpdCount=UpdCount+1,UpdDate=CURRENT_TIMESTAMP, UpdAppId='system',UpdOpeCode='system'"
                        + "  WHERE CompanyId=? AND StoreId=? AND TerminalId=?"
                        + "  IF @@ROWCOUNT = 0"
                        + "  BEGIN"
                        + "  INSERT INTO RESMaster.dbo.MST_TERMINALINFO"
                        + "  (CompanyId, StoreId, TerminalId, FloorId, TerminalName, IPAddress, StoreClass, TerminalType, TillType, RelationType, LogoFileName, InshiFileName, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode,"
                        + "   ConnectionFlag1,ConnectionFlag2,ConnectionFlag3,ConnectionFlag4,ConnectionFlag5,ConnectionFlag6,ConnectionFlag7,ConnectionFlag8,ConnectionFlag9,ConnectionFlag10,ConnectionFlag11,ConnectionFlag12,ConnectionFlag13,ConnectionFlag14,ConnectionFlag15)"
                        + "  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '0', CURRENT_TIMESTAMP, 'system', 'system', 0, CURRENT_TIMESTAMP, 'system', 'system',"
                        + "         '0','0','0','0','0','0','0','0','0','0','0','0','0','0','0')"
                        + "  END"
                        /* MST_PRINTERINFO */
                        + " UPDATE RESMaster.dbo.MST_PRINTERINFO"
                        + "  SET PrinterName=?, IpAddress=?, PortNumTcp='8080', PortNumUdp='0', UpdCount=UpdCount+1, UpdDate=CURRENT_TIMESTAMP, UpdAppId='system', UpdOpeCode='system'"
                        + "  WHERE CompanyId=? AND StoreId=? AND PrinterId=?"
                        + "  IF @@ROWCOUNT = 0"
                        + "  BEGIN"
                        + "  INSERT INTO RESMaster.dbo.MST_PRINTERINFO"
                        + "  (CompanyId, StoreId, PrinterId, PrinterName, IpAddress, PortNumTcp, PortNumUdp, Status, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode)"
                        + "  VALUES (?, ?, ?, ?, ?, 8080, 0, 'Active', 0, CURRENT_TIMESTAMP, 'system', 'system', 0, CURRENT_TIMESTAMP, 'system', 'system')"
                        + "  END"
                        /* MST_TILLIDINFO */
                        + " UPDATE RESMaster.dbo.MST_TILLIDINFO"
                        + "  SET TerminalId=?, BusinessDayDate=?, SodFlag='1', EodFlag='0', DeleteFlag='0', UpdCount=UpdCount+1, UpdDate=CURRENT_TIMESTAMP, UpdAppId='system', UpdOpeCode='system'"
                        + "  WHERE CompanyId=? AND StoreId=? AND TillId=?"
                        + "  IF @@ROWCOUNT = 0"
                        + "  BEGIN"
                        + "  INSERT INTO RESMaster.dbo.MST_TILLIDINFO"
                        + "  (CompanyId, StoreId, TillId, TerminalId, BusinessDayDate, SodFlag, EodFlag, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode)"
                        + "  VALUES (?, ?, ?, ?, ?, '1', '0', '0', CURRENT_TIMESTAMP, 'system', 'system', 0, CURRENT_TIMESTAMP, 'system', 'system')"
                        + "  END";
                Date nowDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String today = formatter.format(nowDate);
                PreparedStatement psIns = connection.prepareStatement(sqlStr);
                // MST_DEVICEINFO(normal)
                psIns.setString(1, request.getParameter("addCompanyID"));
                psIns.setString(2, request.getParameter("addStoreID"));
                psIns.setString(3, request.getParameter("terminalID"));
                psIns.setString(4, request.getParameter("terminalName"));
                psIns.setString(5, request.getParameter("attributeList"));
                psIns.setString(6, request.getParameter("terminalID"));
                psIns.setString(7, request.getParameter("addStoreID") + request.getParameter("terminalID"));
                psIns.setString(8, request.getParameter("queueList"));
                // MST_DEVICEINFO(trainning)
                psIns.setString(9, request.getParameter("addCompanyID"));
                psIns.setString(10, request.getParameter("addStoreID"));
                psIns.setString(11, request.getParameter("terminalID"));
                psIns.setString(12, request.getParameter("terminalName"));
                psIns.setString(13, request.getParameter("attributeList"));
                psIns.setString(14, request.getParameter("terminalID"));
                psIns.setString(15, request.getParameter("addStoreID") + request.getParameter("terminalID"));
                psIns.setString(16, request.getParameter("queueList"));
                // MST_TERMINALINFO(update)
                psIns.setString(17, request.getParameter("floorId"));
                psIns.setString(18, request.getParameter("terminalName"));
                psIns.setString(19, request.getParameter("ipAddress"));
                psIns.setString(20, request.getParameter("storeClass"));
                psIns.setString(21, request.getParameter("terminalType"));
                psIns.setString(22, request.getParameter("tillType"));
                psIns.setString(23, request.getParameter("relationType"));
                psIns.setString(24, request.getParameter("logPath"));
                psIns.setString(25, request.getParameter("inshiPath"));
                psIns.setString(26, request.getParameter("addCompanyID"));
                psIns.setString(27, request.getParameter("addStoreID"));
                psIns.setString(28, request.getParameter("terminalID"));
                // MST_TERMINALINFO(insert)
                psIns.setString(29, request.getParameter("addCompanyID"));
                psIns.setString(30, request.getParameter("addStoreID"));
                psIns.setString(31, request.getParameter("terminalID"));
                psIns.setString(32, request.getParameter("floorId"));
                psIns.setString(33, request.getParameter("terminalName"));
                psIns.setString(34, request.getParameter("ipAddress"));
                psIns.setString(35, request.getParameter("storeClass"));
                psIns.setString(36, request.getParameter("terminalType"));
                psIns.setString(37, request.getParameter("tillType"));
                psIns.setString(38, request.getParameter("relationType"));
                psIns.setString(39, request.getParameter("logPath"));
                psIns.setString(40, request.getParameter("inshiPath"));
                // MST_PRINTERINFO(update)
                psIns.setString(41, request.getParameter("printerName"));
                psIns.setString(42, request.getParameter("ipAddressPrint"));
                psIns.setString(43, request.getParameter("addCompanyID"));
                psIns.setString(44, request.getParameter("addStoreID"));
                psIns.setString(45, request.getParameter("terminalID")); // printerid
                // MST_PRINTERINFO(insert)
                psIns.setString(46, request.getParameter("addCompanyID"));
                psIns.setString(47, request.getParameter("addStoreID"));
                psIns.setString(48, request.getParameter("terminalID")); // printerid
                psIns.setString(49, request.getParameter("printerName"));
                psIns.setString(50, request.getParameter("ipAddressPrint"));
                // MST_TILLIDINFO(update)
                psIns.setString(51, request.getParameter("terminalID"));
                psIns.setString(52, today);
                psIns.setString(53, request.getParameter("addCompanyID"));
                psIns.setString(54, request.getParameter("addStoreID"));
                psIns.setString(55, request.getParameter("addStoreID") + request.getParameter("terminalID"));
                // MST_TILLIDINFO(insert)
                psIns.setString(56, request.getParameter("addCompanyID"));
                psIns.setString(57, request.getParameter("addStoreID"));
                psIns.setString(58, request.getParameter("addStoreID") + request.getParameter("terminalID"));
                psIns.setString(59, request.getParameter("terminalID"));
                psIns.setString(60, today);

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
    <link rel="stylesheet" type="text/css" href="./default.css">
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
端末登録<br><br>
 ※下記すべて入力し、最後に登録を押下してください。<br><br>
<%--
out.println("</br>searchCompanyID:"+request.getParameter("searchCompanyID"));
out.println("</br>searchStoreID:"+request.getParameter("searchStoreID"));
out.println("</br>addCompanyID:"+request.getParameter("addCompanyID"));
out.println("</br>addStoreID:"+request.getParameter("addStoreID"));
out.println("</br>terminalID:"+request.getParameter("terminalID"));
out.println("</br>attributeList:"+request.getParameter("attributeList"));
out.println("</br>queueList:"+request.getParameter("queueList"));
out.println("</br>:"+sqlStr);
--%>
  <iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp"></iframe>
  <!-- jQuery -->
  <script type="text/javascript" src="./jquery.min.js"></script>
  <!-- jQuery UI -->
  <script type="text/javascript" src="./jquery-ui.min.js"></script>
  <!-- ダイアログ共通 -->
  <script type="text/javascript" src="./DialogMessage.js"></script>
  <form action="DeviceAdd.jsp" method="post" id="searchform">
    <input type="hidden" name="searchCompanyID" id="searchCompanyID">
    <input type="hidden" name="searchStoreID" id="searchStoreID">
    <button class="res-small-green" onclick="searchDev(this)">検索</button>
  </form>
  <label class="res-err-msg"><%out.println(errString); %></label>
  <label class="res-info-msg"><%out.println(infoString); %></label>
  <form action="DeviceAdd.jsp" method="post" id="updateform">
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
              <input type="text" disabled name="storeID" id="storeID" size=4 value="<%=request.getParameter("searchStoreID")%>">
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
              <input maxlength="15" type="text" name="terminalName" id="terminalName" size=30 required pattern=".{0,15}">(全角15文字以内で入力してください。)
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
            <td align="right">関連タイプ ： </td>
            <td align="left">
              <input maxlength="1" type="text" name="relationType" id="relationType" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
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
            <td align="right">プリンター名 ： </td>
            <td align="left">
              <input maxlength="40" type="text" name="printerName" id="printerName" size=40 pattern=".{0,20}">(全角20文字以内で入力してください。)
            </td>
          </tr>
          <tr>
              <td align="right">プリンターIPアドレス ： </td>
              <td align="left">
                  <input maxlength="15" type="text" name="ipAddressPrint" id="ipAddressPrint" size=15 required pattern="^\d{1,3}(\.\d{1,3}){3}$"></td>
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
jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#insertDev').click(function(e){
        var myform = document.getElementById('updateform');
        if (myform.checkValidity() == false) {
        	fakeButton.click();
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
</html>
<%--
	}
--%>