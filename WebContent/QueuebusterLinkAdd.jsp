<%@ page language="java" pageEncoding="utf-8"%>
<%@page
	import="java.sql.*"
	import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
	import="java.util.Date"
	import="java.util.ArrayList"
	import="java.text.SimpleDateFormat"%>
<%!
final String ERR_01_ID = "キューIDが既に存在します。";
final String ERR_02_INTERNAL = "内部エラーが発生しました。";
final String ERR_03_STOREID = "店番号を選択してください。";
final String INFO_01_INSERT = "キューの新規登録に成功しました。";
final String CONFIRM_01_INSERT = "キューを登録してよろしいですか。";%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");

	String sqlStr = "";
	String errString = "";
	String infoString = "";

	if (request.getParameter("searchStoreID") != null && request.getParameter("searchStoreID").length() > 0) {
		JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
		Connection connection = dbManager.getConnection();

		sqlStr =  "SELECT queuebuster_link.StoreId AS StoreId"
				+ ", queuebuster_link.Id AS Id"
				+ " FROM RESMaster.dbo.PRM_QUEUEBUSTER_LINK queuebuster_link"
                + " WHERE queuebuster_link.StoreId=? and queuebuster_link.Id=? and queuebuster_link.CompanyId=?"
//                + " WHERE queuebuster_link.StoreId=? and queuebuster_link.Id=?"
		;
		PreparedStatement psSelect = connection.prepareStatement(sqlStr);
        psSelect.setString(1, request.getParameter("searchStoreID").toString());
        psSelect.setString(2, request.getParameter("Id").toString());
        psSelect.setString(3, request.getParameter("searchCompanyID").toString());
		ResultSet rsSelect = psSelect.executeQuery();

        if (rsSelect.next()) {
            errString = ERR_01_ID;
            psSelect.close();
            connection.close();
        } else {
			sqlStr = "INSERT INTO RESMaster.dbo.PRM_QUEUEBUSTER_LINK"
                    + "(StoreId, ID, DisplayName, CompanyId, Status, UpdDate, UpdAppId, UpdOpeCode) "
                    + " VALUES (?, ?, ?, ?, 'Active', CURRENT_TIMESTAMP, 'system', 'system');";
//					+ "(StoreId, ID, DisplayName, Status, UpdDate, UpdAppId, UpdOpeCode) "
//					+ " VALUES (?, ?, ?, 'Active', CURRENT_TIMESTAMP, 'system', 'system');";

			Date nowDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String today = formatter.format(nowDate);
			PreparedStatement psIns = connection.prepareStatement(sqlStr);

			psIns.setString(1, request.getParameter("searchStoreID"));
			psIns.setString(2, request.getParameter("Id"));
			psIns.setString(3, request.getParameter("DisplayName"));
            psIns.setString(4, request.getParameter("searchCompanyID"));

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
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="./default.css">
	<title>新規登録</title>
</head>
<body class="res-maincontent">
前捌登録<br><br>
 ※下記すべて入力し、最後に登録を押下してください。　<br><br>
<iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp" frameborder="none" width="100%" height="40px"></iframe>
<!-- jQuery -->
<script type="text/javascript" src="./jquery.min.js"></script>
<!-- jQuery UI -->
<script type="text/javascript" src="./jquery-ui.min.js"></script>
<!-- ダイアログ共通 -->
<script type="text/javascript" src="./DialogMessage.js"></script>
<label class="res-err-msg"><%out.println(errString); %></label>
<label class="res-info-msg"><%out.println(infoString); %></label>
<form action="QueuebusterLinkAdd.jsp" method="post" id="QueuebusterLinkAdd.jsp">
    <div id="updateArea" style="display:none">
        <div class="panel">
            <input type="hidden" name="searchCompanyID" id="searchCompanyID">
	   	    <input type="hidden" name="searchStoreID" id="searchStoreID">
		    <table border="0" cellspacing="4" cellpadding="4">
			    <tr>
				    <td align="right">キューID： </td>
				    <td align="left"><input type="text" id="Id" name="Id" size=10 maxlength="10" required pattern="\d{0,10}">(半角数字10桁以内で入力してください)</td>
			    </tr>
			    <tr>
			    	<td align="right">キュー名称： </td>
				    <td align="left"><input type="text" id="DisplayName" name="DisplayName" value="" size=40 maxlength="20" required pattern=".{0,20}">(全角20文字以内で入力してください)</td>
			    </tr>
		    </table>
		    <br>
        </div>
        <div align="right">
             <input type="button" value="登録" id="start" name="start" class="res-big-green"> 
        </div>
    </div>
    <button id="fakeButton" style="display:none"></button>
</form>
</body>

<script type="text/javascript">
jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#start').click(function(e){
        var companyId = storesearch.document.getElementById('companyidlist').value;
    	var storeId = storesearch.document.getElementById('storeidlist').value;
        document.getElementById('searchCompanyID').value = companyId; 
    	document.getElementById('searchStoreID').value = storeId;

    	var myform = document.getElementById('QueuebusterLinkAdd.jsp');

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

window.onload = function() {
	storesearch.document.getElementById('companyidlist').addEventListener('change', function(){
		if (storesearch.document.getElementById('storeidlist').selectedIndex != -1) {
			document.getElementById('updateArea').style.display = "block";
		} else {
			document.getElementById('updateArea').style.display = "none";
		}
	}, false );
};

</script>

</html>
