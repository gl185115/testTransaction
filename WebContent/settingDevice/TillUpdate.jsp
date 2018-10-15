<%@ page language="java" pageEncoding="utf-8"%>
<%@page
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.Date"
    import="java.util.ArrayList"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"
%>
<%!
    final String ERR_01_TILL = "ドロワーが使用中のため削除できません。<br>削除したい場合、端末に登録されているドロワーを変更してから再度実行してください。";
    final String ERR_02_UPDATE = "ドロワー情報の更新に失敗しました。<br>システム担当者に確認してください。";
    final String ERR_03_DELETE = "ドロワー情報の削除に失敗しました。<br>システム担当者に確認してください。";
    final String ERR_04_INTERNAL = "内部エラーが発生しました。<br>システム担当者に確認してください。";
    final String ERR_05_REUSE = "ドロワー情報の再利用に失敗しました。<br>システム担当者に確認してください。";
    final String INFO_01_UPDATE = "ドロワー情報の更新に成功しました。";
    final String INFO_02_DELETE = "ドロワー情報の削除に成功しました。";
    final String INFO_03_REUSE = "ドロワー情報の再利用に成功しました。";
    final String BUTTON_01_DELETE = "削除";
    final String BUTTON_02_REUSE = "再利用";
    final String CONFIRM_01_UPDATE = "ドロワー情報を更新してよろしいですか。";
    final String CONFIRM_02_DELETE = "ドロワー情報を削除してよろしいですか。";
    final String CONFIRM_03_REUSE = "ドロワー情報を再利用してよろしいですか。";

    // SOD VAL
    ArrayList<String> SOD_VAL = new ArrayList<String>() {{add("0"); add("1");}};
    // EOD VAL
    ArrayList<String> EOD_VAL = new ArrayList<String>() {{add("0"); add("1");}};
%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String errString = "";
    String infoString = "";

    String user = ""; //ログインユーザー名
    //ログインユーザー名取得
	try {
	    user = request.getRemoteUser() != null ? request.getRemoteUser() : "";
	} catch (Exception e) {
	}

    if ("GET".equals(request.getMethod()) && "select".equals(request.getParameter("y1"))) {
        JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
        Connection connection = dbManager.getConnection();

        String sqlStr =
            "SELECT"
          + "  tillinfo.CompanyId"
          + " ,tillinfo.StoreId"
          + " ,tillinfo.TillId"
          + " ,tillinfo.BusinessDayDate"
          + " ,tillinfo.SodFlag"
          + " ,tillinfo.EodFlag"
          + " ,tillinfo.DeleteFlag"
          + " FROM RESMaster.dbo.MST_TILLIDINFO tillinfo"
          + " WHERE CompanyId=? AND StoreId=?"
          + ";"
        ;
        PreparedStatement ps = connection.prepareStatement(sqlStr);
        ps.setString(1, request.getParameter("r1").toString());
        ps.setString(2, request.getParameter("s1").toString());
        ResultSet rs = ps.executeQuery();

        java.util.List<String> SelectData = new java.util.ArrayList<String>();
        while (rs.next()) {
            String tillId = rs.getString("TillId");
            String businessDayDate = rs.getString("BusinessDayDate");
            String sodFlag = rs.getString("SodFlag");
            String eodFlag = rs.getString("EodFlag");
            String DeleteFlag = rs.getString("DeleteFlag");

            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("{\"TillId\": \"").append(tillId).append("\", ")
              .append("\"BusinessDayDate\": \"").append(businessDayDate).append("\", ")
              .append("\"SodFlag\": \"").append(sodFlag).append("\", ")
              .append("\"EodFlag\": \"").append(eodFlag).append("\", ")
              .append("\"TillDeleteFlag\": \"").append(DeleteFlag).append("\"}");

            SelectData.add(sb.toString());
         }
         out.print(SelectData.toString());
         rs.close();
         ps.close();
         connection.close();
    } else{
         if ("POST".equals(request.getMethod())) {
             if (request.getParameter("action") != null) {
                 if (request.getParameter("action").toString().equals("update")) {
                     JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
                     Connection connection = dbManager.getConnection();

                     String sqlStr = "UPDATE RESMaster.dbo.MST_TILLIDINFO"
                                    + " SET BusinessDayDate=?,SodFlag=?,EodFlag=?,"
                                    + " UpdCount=UpdCount+1,UpdDate=CURRENT_TIMESTAMP, UpdAppId='settingDevice', UpdOpeCode=?"
                                    + " WHERE CompanyId=? and StoreId=? and TillId=?";
                     PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                     psUpd.setString(1, request.getParameter("CheckedBusinessDayDate"));
                     psUpd.setString(2, request.getParameter("CheckedSodFlag"));
                     psUpd.setString(3, request.getParameter("CheckedEodFlag"));
                     psUpd.setString(4, user);
                     psUpd.setString(5, request.getParameter("companyID"));
                     psUpd.setString(6, request.getParameter("storeID"));
                     psUpd.setString(7, request.getParameter("tillID"));

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
                 } else if (request.getParameter("action").toString().equals("delete")) {
                     JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
                     Connection connection = dbManager.getConnection();

                     String sqlStr = "SELECT COUNT(devinfo.companyid) AS useCount"
                                    + " FROM RESMaster.dbo.MST_DEVICEINFO devinfo"
                                    + " WHERE devinfo.CompanyId=? AND devinfo.StoreId=? AND devinfo.TillId=?"
                                    + " AND devinfo.DeleteFlag<>'1' AND devinfo.Status<>'Deleted'";
                     PreparedStatement psSelect = connection.prepareStatement(sqlStr);
                     psSelect.setString(1, request.getParameter("companyID"));
                     psSelect.setString(2, request.getParameter("storeID"));
                     psSelect.setString(3, request.getParameter("tillID"));
                     ResultSet rsSelect = psSelect.executeQuery();

                     if(!rsSelect.next() || rsSelect.getInt("useCount") > 0) {
                         psSelect.close();
                         errString = ERR_01_TILL;
                         connection.close();
                     } else {
                         sqlStr = "UPDATE RESMaster.dbo.MST_TILLIDINFO"
                                 + " SET DeleteFlag=1,"
                                 + " DelDate=CURRENT_TIMESTAMP, DelAppId='settingDevice', DelOpeCode=?"
                                 + " WHERE CompanyId=? and StoreId=? and TillId=?";
                         PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                         psUpd.setString(1, user);
                         psUpd.setString(2, request.getParameter("companyID"));
                         psUpd.setString(3, request.getParameter("storeID"));
                         psUpd.setString(4, request.getParameter("tillID"));

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
                 } else if (request.getParameter("action").toString().equals("reuse")) {
                     JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
                     Connection connection = dbManager.getConnection();

                     String sqlStr = "UPDATE RESMaster.dbo.MST_TILLIDINFO"
                             + " SET DeleteFlag=0,"
                             + " UpdCount=UpdCount+1,UpdDate=CURRENT_TIMESTAMP, UpdAppId='settingDevice', UpdOpeCode=?"
                             + " WHERE CompanyId=? and StoreId=? and TillId=?";
                     PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                     psUpd.setString(1, user);
                     psUpd.setString(2, request.getParameter("companyID"));
                     psUpd.setString(3, request.getParameter("storeID"));
                     psUpd.setString(4, request.getParameter("tillID"));

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
    <title>NCR RES TillUpdate Viewer</title>
    <link rel="stylesheet" type="text/css" href="./css/default.css">
</head>
<body class="res-maincontent">
    <!-- jQuery -->
    <script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
    <!-- ダイアログ共通 -->
    <script type="text/javascript" src="./js/DialogMessage.js"></script>
ドロワー照会<br><br>
※ドロワーを照会・更新・削除することができます。<br><br>

  <div>
    <iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp"></iframe>
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
          <th>ドロワーID(TillId)</th>
          <th>稼働営業日(BusinessDayDate)</th>
          <th>開設(SodFlag)</th>
          <th>閉設(EodFlag)</th>
          <th>ステータス(TillStatus)</th>
        </tr>
        </thead>
      <tbody id="logs">
      </tbody>
    </table>
  </div>

  <form action="TillUpdate.jsp" method="post" id="updateform" onsubmit="return false;">
    <input type="hidden" name="action" id="action">
    <input type="hidden" name="companyID" id="companyID">
    <input type="hidden" name="storeID" id="storeID">
    <input type="hidden" name="tillID" id="tillID">
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
            <td align="right">ドロワーID ： </td>
            <td>
              <input type="text" disabled name="CheckedTillId" id="CheckedTillId"></td>
          </tr>
          <tr>
            <td align="right">稼働営業日 ： </td>
            <td>
              <input maxlength="10" type="text" name="CheckedBusinessDayDate" id="CheckedBusinessDayDate" size=10 required pattern="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)">('yyyy-mm-dd'形式で入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">開設 ： </td>
            <td>
              <select name="CheckedSodFlag" id="CheckedSodFlag" onChange="changeAnotherSelect(this)" required style="width:50%">
                <%
                for (int i=0;i<SOD_VAL.size();i++) {
                    out.print("<option value=\"" + SOD_VAL.get(i) + "\"");
                    out.println(">" + SOD_VAL.get(i) + "</option>");
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
                for (int i=0;i<EOD_VAL.size();i++) {
                    out.print("<option value=\"" + EOD_VAL.get(i) + "\"");
                    out.println(">" + EOD_VAL.get(i) + "</option>");
                }
                %>
              </select>
            </td>
          </tr>
        </table>
      </div>
      <table align="right">
        <tr>
          <td align="right" style="padding:10px;"><button type="button" id="DeleteButton" name="DeleteButton" class="res-big-yellow">削除</button></td>
          <td align="right" style="padding:10px;"><button type="button" id="UpdateButton" name="UpdateButton" class="res-big-green">更新</button></td>
        </tr>
      </table>
    </div>
    <button id="fakeButton" style="display:none"></button>
  </form>
</body>
<script type="text/javascript">
function check(inValue) {
    // TillId
    var strId = 'TillId' + inValue;
    document.getElementById('CheckedTillId').value = document.getElementById(strId).value || false;
    document.getElementById('tillID').value = document.getElementById(strId).value || false;

    // BusinessDayDate
    strId = 'BusinessDayDate' + inValue;
    document.getElementById('CheckedBusinessDayDate').value = document.getElementById(strId).value || false;

    // SodFlag
    strId = 'SodFlag' + inValue;
    document.getElementById('CheckedSodFlag').value = document.getElementById(strId).value || false;

    // EodFlag
    strId = 'EodFlag' + inValue;
    document.getElementById('CheckedEodFlag').value = document.getElementById(strId).value || false;

    strId = 'TillDeleted' + inValue;
    var deleted = document.getElementById(strId).value || false;
    if (deleted == 'Deleted') {
        document.getElementById('DeleteButton').innerText = <%='\''+ BUTTON_02_REUSE + '\''%>;
        document.getElementById('UpdateButton').style.display = "none";

        var input_tags = document.getElementById("panel").getElementsByTagName("input");
        for(var i=0;i<input_tags.length;i++){
            input_tags[i].disabled = true;
        }
        var select_tags = document.getElementById("panel").getElementsByTagName("select");
        for(var i=0;i<select_tags.length;i++){
        	select_tags[i].disabled = true;
        }
    } else {
        document.getElementById('DeleteButton').innerText =  <%='\''+ BUTTON_01_DELETE + '\''%>;
        document.getElementById('UpdateButton').style.display = "block";

        var input_tags = document.getElementById("panel").getElementsByTagName("input");
        for(var i=0;i<input_tags.length;i++){
            if (input_tags[i].id == "CheckedCompanyID" || input_tags[i].id == "CheckedStoreID" || input_tags[i].id == "CheckedTillId") {
                continue;
            } else {
                input_tags[i].disabled = false;
            }
        }
        var select_tags = document.getElementById("panel").getElementsByTagName("select");
        for(var i=0;i<select_tags.length;i++){
            select_tags[i].disabled = false;
        }
    }


    document.getElementById('updateArea').style.display = "block";
}

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
    $('#UpdateButton').click(function(e){
        var myform = document.getElementById('updateform');
        if (myform.checkValidity() == false) {
        	document.getElementById('fakeButton').click();
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
            if (currentLog[i].TillDeleteFlag=='1') {
                log += '<tr class="deleted-row">'
            } else {
                log += '<tr>'
            }
            log += '<td align="center">'+ '<input type="radio" name="q1" id="'
                    + i + '" onclick="check(id)">' + '</td>';
            log += '<td><input type="text" id="TillId' + i
                    + '" name="TillId' + i + '" disabled value="'
                    + (currentLog[i].TillId || '&nbsp;' ) + '" >' + '</td>';
            log += '<td><input type="text" id="BusinessDayDate' + i
                + '" name="BusinessDayDate' + i + '" disabled value="'
                + (currentLog[i].BusinessDayDate || '&nbsp;' ) + '">' + '</td>';
            log += '<td><input type="text" id="SodFlag' + i
                + '" name="SodFlag' + i + '" disabled value="'
                + (currentLog[i].SodFlag || '&nbsp;' ) + '">' + '</td>';
                log += '<td><input type="text" id="EodFlag' + i
                + '" name="EodFlag' + i + '" disabled value="'
                + (currentLog[i].EodFlag || '&nbsp;' ) + '">' + '</td>';
            if (currentLog[i].TillDeleteFlag=='1') {
                log += '<td><input type="text" id="TillDeleted' + i
                + '" name="TillDeleted' + i + '" disabled value="'
                + 'Deleted' + '">';
            } else {
                log += '<td><input type="text" id="TillDeleted' + i
                + '" name="TillDeleted' + i + '" disabled value="'
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
            xhr.open('GET','TillUpdate.jsp?'
                    + 's1=' + storesearch.document.getElementById('storeidlist').value
                 + '&t1='
                 + '&del=flase'
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
