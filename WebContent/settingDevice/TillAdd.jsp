<%@ page language="java" pageEncoding="utf-8"%><%@page
	import="java.sql.*"%><%@page
	import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
	import="java.util.Date"
    import="java.util.ArrayList"
	import="java.text.SimpleDateFormat"%>
<%!
final String ERR_01_TERMINALID = "ドロワーIDが既に存在します。<br>ドロワーIDを確認後、再度登録を実行してください。";
final String ERR_02_INTERNAL = "内部エラーが発生しました。<br>システム担当者に確認してください。";
final String INFO_01_INSERT = "ドロワーIDの新規登録に成功しました。";
final String CONFIRM_01_INSERT = "ドロワーIDを登録してよろしいですか。";

final static int USB_PRINTER_PORT = 8080;
final static int NETWORK_PRINTER_PORT = 9100;
int defaultPrinterPort = USB_PRINTER_PORT;

	// SodFlag
	ArrayList<String> SODFLAG = new ArrayList<String>() {{add("0"); add("1");}};

	// EodFlag
	ArrayList<String> EODFLAG = new ArrayList<String>() {{add("0"); add("1");}};
%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String sqlStr = "";
    String errString = "";
    String infoString = "";
    Boolean isAddAreaHide = true;
	String user = ""; //ログインユーザー名

     //ログインユーザー名取得
 	try {
 	    user = request.getRemoteUser() != null ? request.getRemoteUser() : "";
 	} catch (Exception e) {
 	}

    if (request.getParameter("searchCompanyID") != null && request.getParameter("searchCompanyID").length() != 0) {
    		isAddAreaHide = false;
    }else if(request.getParameter("addCompanyID") != null && request.getParameter("addCompanyID").length() != 0){
    		JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
	        Connection connection = dbManager.getConnection();

			if(request.getParameter("CheckTillId") != null){
			sqlStr = "SELECT TillId"
	        		+ " FROM RESMaster.dbo.MST_TILLIDINFO"
					+ " WHERE CompanyId =? AND StoreId =? AND TillId =?;";
	        PreparedStatement psSelect = connection.prepareStatement(sqlStr);
	        psSelect.setString(1, request.getParameter("addCompanyID").toString());
	        psSelect.setString(2, request.getParameter("addStoreID").toString());
	        psSelect.setString(3, request.getParameter("CheckTillId").toString());
	        ResultSet rsSelect = psSelect.executeQuery();

	        if (rsSelect.next()) {
	        	errString = ERR_01_TERMINALID;
				psSelect.close();
				connection.close();
	        }else{
	        	psSelect.close();

	        	sqlStr = "  INSERT INTO RESMaster.dbo.MST_TILLIDINFO"
	                    + "  (CompanyId, StoreId, TillId, BusinessDayDate, SodFlag, EodFlag, DeleteFlag, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode)"
	                    + "  VALUES (?, ?, ?, ?, ?, ?, '0', CURRENT_TIMESTAMP, 'settingDevice', ?, 0, CURRENT_TIMESTAMP, 'settingDevice', ?);";

				PreparedStatement psIns = connection.prepareStatement(sqlStr);

				psIns.setString(1, request.getParameter("addCompanyID"));
	            psIns.setString(2, request.getParameter("addStoreID"));
	            psIns.setString(3, request.getParameter("CheckTillId"));
	            psIns.setString(4, request.getParameter("CheckedBusinessDayDate"));
	            psIns.setString(5, request.getParameter("CheckedSodFlag"));
	            psIns.setString(6, request.getParameter("CheckedEodFlag"));
	            psIns.setString(7, user);
	            psIns.setString(8, user);

				try {
					int rsIns = psIns.executeUpdate();
					connection.commit();
					infoString = INFO_01_INSERT;
				} catch (Exception e) {
					errString = ERR_02_INTERNAL + e.getMessage();
				} finally {
					psSelect.close();
					psIns.close();
					connection.close();
	        	}
	        }
		}
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

    var myform = document.getElementById('searchtillform');
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
ドロワー登録<br><br>
 ※下記すべて入力し、最後に登録を押下してください。<br><br>
  <iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp"></iframe>
  <form action="TillAdd.jsp" method="post" id="searchtillform" onsubmit="return false;">
    <input type="hidden" name="searchCompanyID" id="searchCompanyID">
    <input type="hidden" name="searchStoreID" id="searchStoreID">
    <button class="res-small-green" onclick="searchDev(this)">次へ</button>
  </form>
  <label class="res-err-msg"><%out.println(errString); %></label>
  <label class="res-info-msg"><%out.println(infoString); %></label>
  <form action="TillAdd.jsp" method="post" id="adddtillidform" onsubmit="return false;">
    <input type="hidden" name="addCompanyID" id="addCompanyID" value="<%=request.getParameter("searchCompanyID")%>">
    <input type="hidden" name="addStoreID" id="addStoreID" value="<%=request.getParameter("searchStoreID")%>">
    <div id="addArea"<%if(isAddAreaHide) {out.print("style=\"display:none\""); }%>>
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
            <td align="right">ドロワーID ： </td>
            <td align="left">
              <input maxlength="8" type="text"  name="CheckTillId" id="CheckTillId" size=8 required pattern="\d{8}">(半角数字8桁を入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">稼働営業日 ： </td>
            <td>
              <input maxlength="10" type="text" name="CheckedBusinessDayDate" id="CheckedBusinessDayDate" size=10 required pattern="\d{4}-\d{2}-\d{2}">('yyyy-mm-dd'形式で入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">開設 ： </td>
            <td>
              <select name="CheckedSodFlag" id="CheckedSodFlag" onChange="changeAnotherSelect(this)" required style="width:50%">
                <%
                for (int i=0;i<SODFLAG.size();i++) {
                    out.print("<option value=\"" + SODFLAG.get(i) + "\"");
                    out.println(">" + SODFLAG.get(i) + "</option>");
                }
                %>
              </select>
            </td>
           </tr>
           <tr>
            <td align="right">閉設 ： </td>
            <td>
              <select name="CheckedEodFlag" id="CheckedEodFlag" onChange="changeAnotherSelect(this)" required style="width:50%">
                <%
                for (int i=0;i<EODFLAG.size();i++) {
                    out.print("<option value=\"" + EODFLAG.get(i) + "\"");
                    out.println(">" + EODFLAG.get(i) + "</option>");
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
function changeAnotherSelect(obj) {
    var anotherObjId = 'CheckedSodFlag';
    if (obj.id == 'CheckedSodFlag') {
        var anotherObjId = 'CheckedEodFlag';
    }

    var anotherValue = '0';
    if(obj.value == '0') {
        anotherValue = '1';
    }

    var pulldown_option = document.getElementById(anotherObjId).getElementsByTagName('option');
    for(i=0; i<pulldown_option.length;i++){
        if(pulldown_option[i].value == anotherValue){
            pulldown_option[i].selected = true;
            break;
        }
    }
}

jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#insertDev').click(function(e){
        var myform = document.getElementById('adddtillidform');
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