<%@ page language="java" pageEncoding="utf-8"%>
<%@page
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.Date"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"%>
<%!
    final String ERR_01_QUEUE = "キューが使用中のため削除できません。";
    final String ERR_02_UPDATE = "キュー情報の更新に失敗しました。";
    final String ERR_03_DELETE = "キュー情報の削除に失敗しました。";
    final String ERR_04_INTERNAL = "内部エラーが発生しました。";
    final String ERR_05_REUSE = "キュー情報の再利用に失敗しました。";
    final String INFO_01_UPDATE = "キュー情報の更新に成功しました。";
    final String INFO_02_DELETE = "キュー情報の削除に成功しました。";
    final String INFO_03_REUSE = "キュー情報の再利用に成功しました。";
    final String BUTTON_01_DELETE = "削除";
    final String BUTTON_02_REUSE = "再利用";
    final String CONFIRM_01_UPDATE = "キュー情報を更新してよろしいですか。";
    final String CONFIRM_02_DELETE = "キュー情報を削除してよろしいですか。";
    final String CONFIRM_03_REUSE = "キュー情報を再利用してよろしいですか。";
%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String errString = "";
    String infoString = "";

    if (request.getMethod() == "GET" && "select".equals(request.getParameter("y1"))) {
        JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
        Connection connection = dbManager.getConnection();

        String sqlStr = 
            "SELECT"
          + " queuebuster.StoreId AS StoreId"
          + " ,queuebuster.Id AS QueueId"
          + " ,queuebuster.DisplayName AS DisplayName"
          + " ,queuebuster.Status AS QueueStatus"
          + " FROM RESMaster.dbo.PRM_QUEUEBUSTER_LINK queuebuster"
          + " WHERE queuebuster.StoreId=? and queuebuster.CompanyId=?"
//          + " WHERE queuebuster.StoreId=?"
          + ";"
        ;
        PreparedStatement ps = connection.prepareStatement(sqlStr);
        ps.setString(1, request.getParameter("s1").toString());
        ps.setString(2, request.getParameter("r1").toString());
        ResultSet rs = ps.executeQuery();
        
        java.util.List<String> SelectData = new java.util.ArrayList<String>();
        while (rs.next()) {
            String QueueId = rs.getString("QueueId");
            String DisplayName = rs.getString("DisplayName");
            String QueueStatus = rs.getString("QueueStatus");

            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("{\"QueueId\": \"").append(QueueId).append("\", ")
              .append("\"DisplayName\": \"").append(DisplayName).append("\", ")
              .append("\"QueueStatus\": \"").append(QueueStatus).append("\"}");

            SelectData.add(sb.toString());
         }
         out.print(SelectData.toString());
         rs.close();
         ps.close();
         connection.close();
    } else {
        if (request.getMethod() == "POST") {
/*
            out.println(request.getParameter("CheckedDisplayName"));
            out.println(request.getParameter("companyID"));
            out.println(request.getParameter("storeID"));
            out.println(request.getParameter("queueID"));
            out.println(request.getParameter("action").toString());
*/
            if (request.getParameter("action") != null) {
                if ("update".equals(request.getParameter("action").toString())) {
                    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
                    Connection connection = dbManager.getConnection();

                    String sqlStr = "UPDATE RESMaster.dbo.PRM_QUEUEBUSTER_LINK"
                                   + " SET DisplayName=?,"
                                   + " UpdDate=CURRENT_TIMESTAMP, UpdAppId='system',UpdOpeCode='system'"
                                   + " WHERE StoreId=? and Id=? and CompanyId=?";
//                                   + " WHERE StoreId=? and Id=?";
                    PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                    psUpd.setString(1, request.getParameter("CheckedDisplayName"));
                    psUpd.setString(2, request.getParameter("storeID"));
                    psUpd.setString(3, request.getParameter("queueID"));
                    psUpd.setString(4, request.getParameter("companyID"));

                    try {
                        int rsUpd = psUpd.executeUpdate();
                        if(rsUpd > 0){
                             infoString = INFO_01_UPDATE;
                        }else{
                             errString = ERR_02_UPDATE;
                        }
                        connection.commit();
                    } catch (Exception e) {
                        errString = ERR_04_INTERNAL + e.getMessage();
                    } finally {
                        psUpd.close();
                        connection.close();
                    }
                } else if ("delete".equals(request.getParameter("action").toString())) {
                    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
                    Connection connection = dbManager.getConnection();
                    
                    String sqlStr = "SELECT COUNT(devinfo.companyid) AS useCount"
                                   + " FROM RESMaster.dbo.MST_DEVICEINFO devinfo"
                                   + " WHERE devinfo.StoreId=? AND devinfo.LinkQueueBuster=? AND devinfo.CompanyId=?";
//                                   + " WHERE devinfo.StoreId=? AND devinfo.LinkQueueBuster=?";
                    PreparedStatement psSelect = connection.prepareStatement(sqlStr);
                    psSelect.setString(1, request.getParameter("storeID"));
                    psSelect.setString(2, request.getParameter("queueID"));
                    psSelect.setString(3, request.getParameter("companyID"));
                    ResultSet rsSelect = psSelect.executeQuery();
                    
                    if(!rsSelect.next() || rsSelect.getInt("useCount") > 0) {
                        psSelect.close();
                        errString = ERR_01_QUEUE;
                        connection.close();
                    } else {
                        sqlStr = "UPDATE RESMaster.dbo.PRM_QUEUEBUSTER_LINK"
                                + " SET Status='Deleted'"
                                + " WHERE StoreId=? and Id=? AND CompanyId=?";
//                                + " WHERE StoreId=? and Id=?";
                        PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                        psUpd.setString(1, request.getParameter("storeID"));
                        psUpd.setString(2, request.getParameter("queueID"));
                        psUpd.setString(3, request.getParameter("companyID"));
                 
                        try {
                            int rsUpd = psUpd.executeUpdate();
                            if(rsUpd > 0){
                                infoString = INFO_02_DELETE;
                            }else{
                                errString = ERR_03_DELETE;
                            }
                            connection.commit();
                        } catch (Exception e) {
                             errString = ERR_04_INTERNAL + e.getMessage();
                        } finally {
                            psSelect.close();
                            psUpd.close();
                            connection.close();
                        }
                    }
                } else if ("reuse".equals(request.getParameter("action").toString())) {
                    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
                    Connection connection = dbManager.getConnection();
                    
                    String sqlStr = "UPDATE RESMaster.dbo.PRM_QUEUEBUSTER_LINK"
                            + " SET Status='Active',"
                            + " UpdDate=CURRENT_TIMESTAMP, UpdAppId='system',UpdOpeCode='system'"
                            + " WHERE StoreId=? and Id=? and CompanyId=?";
//                            + " WHERE StoreId=? and Id=?";
                    PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                    psUpd.setString(1, request.getParameter("storeID"));
                    psUpd.setString(2, request.getParameter("queueID"));
                    psUpd.setString(3, request.getParameter("companyID"));
                 
                    try {
                        int rsUpd = psUpd.executeUpdate();
                        if(rsUpd > 0){
                            infoString = INFO_03_REUSE;
                        }else{
                            errString = ERR_05_REUSE;
                        }
                        connection.commit();
                    } catch (Exception e) {
                         errString = ERR_04_INTERNAL + e.getMessage();
                    } finally {
                         psUpd.close();
                         connection.close();
                    }
                }
            }
        }
%>
<!DOCTYPE html>
<html>
<head>
    <title>NCR RES LinkQueueBusterUpdate Viewer</title>
    <link rel="stylesheet" type="text/css" href="./default.css">
</head>
<body class="res-maincontent">
前捌照会<br><br>
※キューを照会・更新・削除することができます。<br><br>

  <div>
    <iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp"></iframe>
    <!-- jQuery -->
    <script type="text/javascript" src="./jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="./jquery-ui.min.js"></script>
    <!-- ダイアログ共通 -->
    <script type="text/javascript" src="./DialogMessage.js"></script>
    <input type="hidden" name="searchCompanyID" id="searchCompanyID">
    <input type="hidden" name="searchStoreID" id="searchStoreID">
    <button id="select" class="res-small-green">検索</button>
  </div>
  <div id="messageArea">
    <label class="res-err-msg"><%out.println(errString); %></label>
    <label class="res-info-msg"><%out.println(infoString); %></label>
  </div>
  
  <div style="padding:10px 10px;"></div>
  
  <div class="table-scroll-area-v table-scroll-area-h" id="tablearea" style="display:none; height:338px">
    <table class="res-tbl">
      <thead>
        <tr>
          <th></th>
          <th>キューID(Id)</th>
          <th>キュー名称(DisplayName)</th>
          <th>ステータス(QueueStatus)</th>
        </tr>
      </thead>
      <tbody id="logs">
      </tbody>
    </table>
  </div>

  <form action="QueuebusterUpdate.jsp" method="post" id="updateform">
    <input type="hidden" name="action" id="action">
    <input type="hidden" name="companyID" id="companyID">
    <input type="hidden" name="storeID" id="storeID">
    <input type="hidden" name="queueID" id="queueID">
    <div id="updateArea" style="display:none">
      <div class="panel" id="panel">
        <table border="0" cellspacing="4" cellpadding="4">
          <tr>
            <td align="right">企業番号 ： </td>
            <td align="left">
              <input type="text" disabled name="CheckedCompanyID" id="CheckedCompanyID" size="4">
            </td>
          </tr>
          <tr>
            <td align="right">店番号 ： </td>
            <td align="left">
              <input type="text" disabled name="CheckedStoreID" id="CheckedStoreID" size=4">
            </td>
          </tr>
          <tr>
            <td align="right">キューID ： </td>
            <td>
              <input type="text" disabled name="CheckedQueueID" id="CheckedQueueID"></td>
          </tr>
          <tr>
            <td align="right">キュー名称 ： </td>
            <td>
              <input maxlength="20" type="text" name="CheckedDisplayName" id="CheckedDisplayName" size=40 required pattern=".{0,20}">(全角20文字以内で入力してください。)
            </td>
          </tr>
        </table>
      </div>
      <table align="right">
        <tr>
          <td align="right" style="padding:10px;"><button type="button" id="DeleteButton" name="DeleteButton" class="res-big-yellow"">削除</button></td>
          <td align="right" style="padding:10px;"><button type="button" id="UpdateButton" name="UpdateButton" class="res-big-green">更新</button></td>
        </tr>
      </table>
    </div>
    <button id="fakeButton" style="display:none"></button>
  </form>
</body>
<script type="text/javascript">
function check(inValue) {
    // QueueId
    var strId = 'QueueId' + inValue;
    document.getElementById('CheckedQueueID').value = document.getElementById(strId).value || false;
    document.getElementById('queueID').value = document.getElementById(strId).value || false;
    
    // DisplayName
    strId = 'DisplayName' + inValue;
    document.getElementById('CheckedDisplayName').value = document.getElementById(strId).value || false;

    strId = 'QueueDeleted' + inValue;
    var deleted = document.getElementById(strId).value || false;
    if (deleted == 'Deleted') {
        document.getElementById('DeleteButton').innerText = <%='\''+ BUTTON_02_REUSE + '\''%>;
        document.getElementById('UpdateButton').style.display = "none";

        var input_tags = document.getElementById("panel").getElementsByTagName("input");
        for(var i=0;i<input_tags.length;i++){
            input_tags[i].disabled = true;
        }
    } else {
        document.getElementById('DeleteButton').innerText =  <%='\''+ BUTTON_01_DELETE + '\''%>;
        document.getElementById('UpdateButton').style.display = "block";

        var input_tags = document.getElementById("panel").getElementsByTagName("input");
        for(var i=0;i<input_tags.length;i++){
            if (input_tags[i].id == "CheckedCompanyID" || input_tags[i].id == "CheckedStoreID" || input_tags[i].id == "CheckedQueueID") {
                continue;
            } else {
                input_tags[i].disabled = false;
            }
        }
    }

    document.getElementById('updateArea').style.display = "block";
}

jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#UpdateButton').click(function(e){
        var myform = document.getElementById('updateform');
        if (myform.checkValidity() == false) {
        	fakeButton.click();
            return;
        }
        showDialog(
            "タイトル：未使用",
            <%='\'' + CONFIRM_01_UPDATE + '\''%>,
            ButtonYesNo,
            function() {
                //「いいえ」を押したときの処理
            },
            function() {
                //「はい」を押したときの処理
                document.getElementById('action').value = "update";
                myform.submit();
            }
        );
    });

    $('#DeleteButton').click(function(e){
        var myform = document.getElementById('updateform');
        var action = document.getElementById('DeleteButton').innerText;
        var message = "";
        if (action == <%='\''+ BUTTON_02_REUSE + '\''%>) {
        	// 
            document.getElementById('action').value = "reuse";
            message = <%='\'' + CONFIRM_03_REUSE + '\''%>;
        } else {
            if (myform.checkValidity() == false) {
                fakeButton.click();
                return;
            }
        	document.getElementById('action').value = "delete";
            message = <%='\'' + CONFIRM_02_DELETE + '\''%>;
        }
        showDialog(
                "タイトル：未使用",
                message,
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

(function() {
    var currentLog = null;
    
    var SelectLog = function() {
        if (currentLog === null) return;

        var log = '';
        for (var i = 0; i < currentLog.length; i++) {
            if (currentLog[i].QueueStatus == 'Deleted') {
                log += '<tr class="deleted-row">' 
            } else {
                log += '<tr>' 
            }
            log += '<td align="center">'+ '<input type="radio" name="q1" id="' 
                    + i + '" onclick="check(id)">' + '</td>';
            log += '<td><input type="text" id="QueueId' + i 
                    + '" name="QueueId' + i + '" disabled value="'
                    + (currentLog[i].QueueId || '&nbsp;' ) + '" >' + '</td>';
            log += '<td><input type="text" id="DisplayName' + i 
                + '" name="DisplayName' + i + '" disabled value="'
                + (currentLog[i].DisplayName || '&nbsp;' ) + '">' + '</td>';
                if (currentLog[i].QueueStatus == 'Deleted') {
                log += '<td><input type="text" id="QueueDeleted' + i  
                + '" name="QueueDeleted' + i + '" disabled value="'
                + 'Deleted' + '">';
            } else {
                log += '<td><input type="text" id="QueueDeleted' + i  
                + '" name="QueueDeleted' + i + '" disabled value="'
                + '&nbsp;' + '">';
            }
            log += '</td>'; 
        }
        document.getElementById('logs').innerHTML = log;
        
        if (log == '') {
            document.getElementById('tablearea').style.display = "none";
        } else {
            document.getElementById('tablearea').style.display = "block";
        }
    };
    
    document.getElementById('select').addEventListener('click', function() {
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
        document.getElementById('updateArea').style.display = "none";
        
        document.getElementById('CheckedCompanyID').value = storesearch.document.getElementById('companyidlist').value;
        document.getElementById('CheckedStoreID').value = storesearch.document.getElementById('storeidlist').value;
        document.getElementById('companyID').value = storesearch.document.getElementById('companyidlist').value;
        document.getElementById('storeID').value = storesearch.document.getElementById('storeidlist').value;

        document.getElementById('messageArea').style.display = "none";

        try {
            xhr.open('GET','QueuebusterUpdate.jsp?'
                    + 's1=' + storesearch.document.getElementById('storeidlist').value
                 + '&t1='
                 + '&del=flase'
                 + '&r1='+ storesearch.document.getElementById('companyidlist').value
                 + '&y1='+ 'select'
                 , true);
            xhr.send();
        } catch (e) {
                alert(e.name + ':' + e.message + ':' + e.stack);
        }
    });
})();
</script>
</html>
<%
}
%>
