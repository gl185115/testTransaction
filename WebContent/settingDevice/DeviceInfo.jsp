<%@ page language="java" pageEncoding="utf-8"%>
<%@page
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.Date"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"%>
<%!
// Err
// 0:DeviceDetail.jsp/SELECT（Data Not Found.）
final String ERR_01_01 = "対象データが存在しません。";
// 1:DeviceDetail.jsp/DELETE2
final String ERR_01_02 = "「AUT_DEVICES」レコードの削除に失敗しました。<br>システム担当者に確認してください。";
final String ERR_01_05 = "「AUT_DEVICES」レコードは既に削除されています。";
// 2:DeviceDetail.jsp/DELETE(UPDATE)
final String ERR_01_03 = "端末情報の削除に失敗しました。<br>システム担当者に確認してください。";
// 3:DeviceUpdate.jsp/SELECT（Data Not Found.）
final String ERR_02_01 = "対象データが存在しません。<br>システム担当者に確認してください。";
//　4:DeviceUpdate.jsp/UPDATE
final String ERR_02_02 = "端末情報の更新に失敗しました。<br>システム担当者に確認してください。";
// 5:DeviceDetail.jsp/UPDATE2
final String ERR_01_04 = "端末情報の再利用に失敗しました。<br>システム担当者に確認してください。";

// info
//　0:DeviceDetail.jsp/DELETE2の成功
final String INFO_01_01 = "「AUT_DEVICES」レコードの削除に成功しました。";
//　1:DeviceDetail.jsp/DELETEの成功
final String INFO_01_02 = "端末情報の削除に成功しました。";
// 2:DeviceUpdate.jsp/UPDATE
final String INFO_02_01 = "端末情報の更新に成功しました。";
// 3:DeviceDetail.jsp/UPDATE2の成功
final String INFO_01_03 = "端末情報の再利用に成功しました。";
%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String errFlg = "";
    String infoFlg = "";
    String errString = "";
    String infoString = "";

    // メッセージ設定
    errFlg = request.getParameter("err");
    if(null != errFlg){
    	if( "0".equals(errFlg)) errString = ERR_01_01;
    	if( "1".equals(errFlg)) errString = ERR_01_02;
    	if( "2".equals(errFlg)) errString = ERR_01_03;
    	if( "3".equals(errFlg)) errString = ERR_02_01;
    	if( "4".equals(errFlg)) errString = ERR_02_02;
    	if( "5".equals(errFlg)) errString = ERR_01_04;
        if( "6".equals(errFlg)) errString = ERR_01_05;
    }
    infoFlg = request.getParameter("info");
    if(null != infoFlg){
    	if( "0".equals(infoFlg)) infoString = INFO_01_01;
    	if( "1".equals(infoFlg)) infoString = INFO_01_02;
    	if( "2".equals(infoFlg)) infoString = INFO_02_01;
    	if( "3".equals(infoFlg)) infoString = INFO_01_03;
    }
    
    if ("GET".equals(request.getMethod()) && "select".equals(request.getParameter("y1"))) {
        JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
        Connection connection = dbManager.getConnection();
        String sqlStr = "SELECT"
                + "  deviceinfo.CompanyId AS CompanyId"
                + ", deviceinfo.StoreId AS StoreId"
                + ", deviceinfo.TerminalId AS TerminalId"
                + ", deviceinfo.Status AS Status"
                + ", deviceinfo.DeleteFlag AS DeleteFlag"
                + ", companyinfo.CompanyName AS CompanyName"
                + ", storeinfo.StoreName AS StoreName"
                + ", terminalinfo.TerminalName AS TerminalName"
                + " FROM RESMaster.dbo.MST_DEVICEINFO deviceinfo"
                + " LEFT JOIN RESMaster.dbo.MST_COMPANYINFO companyinfo"
                + " ON deviceinfo.CompanyId = companyinfo.CompanyId"
                + " LEFT JOIN RESMaster.dbo.MST_STOREINFO storeinfo"
                + " ON deviceinfo.CompanyId = storeinfo.CompanyId AND"
                + "    deviceinfo.StoreId = storeinfo.StoreId"
                + " LEFT JOIN RESMaster.dbo.MST_TERMINALINFO terminalinfo"
                + " ON deviceinfo.CompanyId = terminalinfo.CompanyId AND"
                + "    deviceinfo.StoreId = terminalinfo.StoreId AND"
                + "    deviceinfo.TerminalId = terminalinfo.TerminalId"
                + " WHERE deviceinfo.StoreId = LTRIM(RTRIM(?)) AND deviceinfo.CompanyId = LTRIM(RTRIM(?))"
                + " AND deviceinfo.Training<>'1'"
          ;
          PreparedStatement ps = connection.prepareStatement(sqlStr);
          ps.setString(1, request.getParameter("s1").toString());
          ps.setString(2, request.getParameter("r1").toString());
          ResultSet rs = ps.executeQuery();
         java.util.List<String> SelectData = new java.util.ArrayList<String>();
         while (rs.next()) {
             String CompanyId = rs.getString("CompanyId");
             String CompanyName = rs.getString("CompanyName");
             String StoreId = rs.getString("StoreId");
             String StoreName = rs.getString("StoreName");
             String TerminalId = rs.getString("TerminalId");
             String TerminalName = rs.getString("TerminalName");
             String Status = rs.getString("Status");
             String DeleteFlag = rs.getString("DeleteFlag");
             java.lang.StringBuilder sb = new java.lang.StringBuilder();
             sb.append("{\"CompanyId\": \"").append(CompanyId).append("\", ")
                 .append("\"CompanyName\": \"").append(CompanyName).append("\", ")
                 .append("\"StoreId\": \"").append(StoreId).append("\", ")
                 .append("\"StoreName\": \"").append(StoreName).append("\", ")
                 .append("\"TerminalId\": \"").append(TerminalId).append("\", ")
                 .append("\"TerminalName\": \"").append(TerminalName).append("\", ")
                 .append("\"Status\": \"").append(Status).append("\", ")
                 .append("\"DeleteFlag\": \"").append(DeleteFlag).append("\"}");
             SelectData.add(sb.toString());
         }
         out.print(SelectData.toString());
         rs.close();
         ps.close();
         connection.close();
    } else {
        // 起動時

%>
<!DOCTYPE html>
<html>
<head>
    <title>NCR RES DeviceInfo Viewer</title>
    <link rel="stylesheet" type="text/css" href="./css/default.css">
</head>
<body class="res-maincontent">
端末照会
<br><br>
 「登録内容変更」
<br><br>
 ※「企業番号、店番号」を選択し、「検索」ボタンを押し、 <br>
　「詳細」を確認したい「企業番号」を選択し、「詳細画面へ」ボタンを押してください。 <br><br>

  <div>
    <iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp"></iframe>
    <input type="hidden" name="searchCompanyID" id="searchCompanyID">
    <input type="hidden" name="searchStoreID" id="searchStoreID">
    <button id="select" class="res-small-green">検索</button>
  </div>
  <div id="messageArea">
    <label class="res-err-msg" id="ErrMsg"><%out.println(errString); %></label>
    <label class="res-info-msg" id="infoMsg"><%out.println(infoString); %></label>
  </div>
  
  <div style="padding:10px 10px;"></div>
  
  <div class="table-scroll-area-v table-scroll-area-h" id="tablearea" style="display:none; height:490px">
    <table class="res-tbl" style="overflow-x:auto;overflow-y:scroll">
      <thead>
        <tr>
          <th></th>
          <th>端末番号(TerminalId)</th>
          <th>端末名(DeviceName)</th>
          <th>ステータス(Status)</th>
        </tr>
      </thead>
      <tbody id="logs"></tbody>
    </table>
  </div>
 <br>
 <div align="right">
   <input id="update" type="button" value="詳細画面へ" class="res-big-green" style="display:none; width:260px" onclick="update()">
 </div>
<!--   <input id="update" type="button" value="「詳細」画面へ" class="res-big-green" style="width:260px" onclick="(this)"> -->
</body>
<script type="text/javascript">

function check(inValue) {
    document.getElementById("update").style.display="block";
}

function update(){

	var radioList = document.getElementsByName("q1");
	var len = radioList.length;
	for(var i=0; i<len; i++){
		if (radioList[i].checked) {
			break;
		}
	}
    try {
    	var storeId = storesearch.document.getElementById("storeidlist").value;
    	var Work = 'TerminalId' + i;
        var terminalID = document.getElementById(Work).value || false;
        var companyId = storesearch.document.getElementById("companyidlist").value;
        
        if(storeId != "" && terminalID != "" && companyId != ""){
            window.location.href="DeviceDetail.jsp?"
           + "s1=" + storeId
           + "&t1=" + terminalID
           + "&r1="+  companyId
           ;
         }else{
            alert("please input　companyId or storeId or terminalID");
        }
    } catch (e) {
        alert("please input terminalID");
    }
}

(function() {
    var currentLog = null;

    var SelectLog = function() {
        if (currentLog === null) return;

        var log = '';
        for (var i = 0; i < currentLog.length; i++) {
//            log += '<tr>' 
            if (currentLog[i].Status == 'Deleted' || currentLog[i].DeleteFlag=='1') {
                log += '<tr class="deleted-row">' 
            } else {
                log += '<tr>' 
            }
            log += '<td>'+ '<input type="radio" name="q1" id="' + i + '"'
                    + 'onclick=check(id) style="width:100%">' + '</td>';
	         log += '<td><input type="text" id="TerminalId' + i  
	             + '" name="TerminalId' + i + '" disabled value="'
	             + (currentLog[i].TerminalId || '&nbsp;' ) + '">' + '</td>'; 
	         log += '<td><input type="text" id="TerminalName' + i  
	             + '" name="TerminalName' + i + '" disabled value="'
	             + (currentLog[i].TerminalName || '&nbsp;' ) + '">' + '</td>'; 
             if (currentLog[i].Status == 'Deleted' || currentLog[i].DeleteFlag=='1') {
                 log += '<td><input type="text" id="Deleted' + i  
                 + '" name="Deleted' + i + '" disabled value="'
                 + 'Deleted' + '">';
             } else {
                 log += '<td><input type="text" id="Deleted' + i  
                 + '" name="Delete' + i + '" disabled value="'
                 + '&nbsp;' + '">';
             }
	         log += '</tr>'; 
        }
        document.getElementById('logs').innerHTML = log;

        if (log == '') {
            document.getElementById('tablearea').style.display = "none";
            document.getElementById("update").style.display="none";
        } else {
            document.getElementById('tablearea').style.display = "block";
        }
    };
    
    document.getElementById('select').addEventListener('click', function() {
        document.getElementById("ErrMsg").innerHTML = ""
        document.getElementById("infoMsg").innerHTML = ""

    	var xhr = new XMLHttpRequest();

        xhr.onload = function() {
            try {
                if (!xhr.responseText || xhr.responseText.length === 0) {
                    document.getElementById('logs').innerHTML = '';
                    return false;
                }
                
                currentLog = eval("("+xhr.responseText+")");
                if (currentLog.ErrorMsg == undefined) {
                    SelectLog();
                } else {
                    alert(currentLog.ErrorMsg);
                    return false;
                }
            } catch (e) {
                alert(e.name + ':' + e.message + ':' + e.stack);
            }
        };

        xhr.onerror = function(e) {
            alert(e);
        };

        try {
            xhr.open('GET','DeviceInfo.jsp?'
                 + 's1=' + storesearch.document.getElementById('storeidlist').value
                 + '&t1='
                 + '&r1='+ storesearch.document.getElementById('companyidlist').value
                 + '&y1='+ 'select'
                 , true);
            xhr.setRequestHeader('Pragma', 'no-cache');
            xhr.setRequestHeader('Cache-Control', 'no-cache');
            xhr.send();
        } catch (e) {
                alert(e.name + ':' + e.message + ':' + e.stack);
        }
    });
})();
</script>
<HEAD>
<meta http-equiv=”Pragma” content=”no-cache”>
<meta http-equiv=”Cache-Control” content=”no-cache”>
</HEAD> 
</html>
<%
}
%>
