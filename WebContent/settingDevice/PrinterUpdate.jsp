<%@ page language="java" pageEncoding="utf-8"%>
<%@page
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.Date"
    import="java.util.ArrayList"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"%>
<%!
    final String ERR_01_PRINTER = "プリンターが使用中のため削除できません。";
    final String ERR_02_UPDATE = "プリンター情報の更新に失敗しました。";
    final String ERR_03_DELETE = "プリンター情報の削除に失敗しました。";
    final String ERR_04_INTERNAL = "内部エラーが発生しました。";
    final String ERR_05_REUSE = "プリンター情報の再利用に失敗しました。";
    final String INFO_01_UPDATE = "プリンター情報の更新に成功しました。";
    final String INFO_02_DELETE = "プリンター情報の削除に成功しました。";
    final String INFO_03_REUSE = "プリンター情報の再利用に成功しました。";
    final String BUTTON_01_DELETE = "削除";
    final String BUTTON_02_REUSE = "再利用";
    final String CONFIRM_01_UPDATE = "プリンター情報を更新してよろしいですか。";
    final String CONFIRM_02_DELETE = "プリンター情報を削除してよろしいですか。";
    final String CONFIRM_03_REUSE = "プリンター情報を再利用してよろしいですか。";

    ArrayList<String> PRINTER_TYPE = new ArrayList<String>() {{add("USBプリンター"); add("ネットワークプリンター");}};
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
          + "  printerinfo.CompanyId AS CompanyId"
          + " ,printerinfo.StoreId AS StoreId"
          + " ,printerinfo.PrinterId AS PrinterId"
          + " ,printerinfo.PrinterName AS PrinterName"
          + " ,printerinfo.Description AS PrinterDescription"
          + " ,printerinfo.IpAddress AS IpAddress"
          + " ,printerinfo.Status AS PrinterStatus"
          + " ,printerinfo.DeleteFlag AS PrinterDeleteFlag"
          + " FROM RESMaster.dbo.MST_PRINTERINFO printerinfo"
          + " WHERE printerinfo.CompanyId=? AND printerinfo.StoreId=?"
          + ";"
        ;
        PreparedStatement ps = connection.prepareStatement(sqlStr);
        ps.setString(1, request.getParameter("r1").toString());
        ps.setString(2, request.getParameter("s1").toString());
        ResultSet rs = ps.executeQuery();
        
        java.util.List<String> SelectData = new java.util.ArrayList<String>();
        while (rs.next()) {
            String CompanyId = rs.getString("CompanyId");
            String PrinterId = rs.getString("PrinterId");
            String PrinterName = rs.getString("PrinterName");
            String PrinterDescription = rs.getString("PrinterDescription");
            String IpAddress = rs.getString("IpAddress");
            String PrinterStatus = rs.getString("PrinterStatus");
            String DeleteFlag = rs.getString("PrinterDeleteFlag");
            
            if (PrinterName == null) PrinterName = "";
            if (PrinterDescription == null) PrinterDescription = "";
            if (IpAddress == null) IpAddress = "";

            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("{\"CompanyId\": \"").append(CompanyId).append("\", ")
              .append("\"PrinterId\": \"").append(PrinterId).append("\", ")
              .append("\"PrinterName\": \"").append(PrinterName).append("\", ")
              .append("\"PrinterDescription\": \"").append(PrinterDescription).append("\", ")
              .append("\"IpAddress\": \"").append(IpAddress).append("\", ")
              .append("\"PrinterStatus\": \"").append(PrinterStatus).append("\", ")
              .append("\"PrinterDeleteFlag\": \"").append(DeleteFlag).append("\"}");
            
            SelectData.add(sb.toString());
         }
         out.print(SelectData.toString());
         rs.close();
         ps.close();
         connection.close();
    } else {
        if (request.getMethod() == "POST") {
/*
            out.println(request.getParameter("CheckedPrinterName"));
            out.println(request.getParameter("CheckedIpAddress"));
            out.println(request.getParameter("companyID"));
            out.println(request.getParameter("storeID"));
            out.println(request.getParameter("printerID"));
            out.println(request.getParameter("action").toString());
*/
            if (request.getParameter("action") != null) {
                if ("update".equals(request.getParameter("action").toString())) {
                    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
                    Connection connection = dbManager.getConnection();

                    String sqlStr = "UPDATE RESMaster.dbo.MST_PRINTERINFO"
                                   + " SET PrinterName=?,Description=?,IpAddress=?,"
                                   + " UpdCount=UpdCount+1,UpdDate=CURRENT_TIMESTAMP, UpdAppId='system',UpdOpeCode='system'"
                                   + " WHERE CompanyId=? and StoreId=? and PrinterId=?";
                    PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                    psUpd.setString(1, request.getParameter("CheckedPrinterName"));
                    psUpd.setString(2, request.getParameter("CheckedPrinterDescription"));
                    if (request.getParameter("CheckedIpAddress") == null) {
                        psUpd.setString(3, "");
                    } else {
                        psUpd.setString(3, request.getParameter("CheckedIpAddress"));
                    }
                    psUpd.setString(4, request.getParameter("companyID"));
                    psUpd.setString(5, request.getParameter("storeID"));
                    psUpd.setString(6, request.getParameter("printerID"));

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
                                   + " WHERE devinfo.CompanyId=? AND devinfo.StoreId=? AND devinfo.PrinterId=?"
                                   + " AND devinfo.DeleteFlag<>'1' AND devinfo.Status<>'Deleted'";
                    PreparedStatement psSelect = connection.prepareStatement(sqlStr);
                    psSelect.setString(1, request.getParameter("companyID"));
                    psSelect.setString(2, request.getParameter("storeID"));
                    psSelect.setString(3, request.getParameter("printerID"));
                    ResultSet rsSelect = psSelect.executeQuery();
                    
                    if(!rsSelect.next() || rsSelect.getInt("useCount") > 0) {
                        psSelect.close();
                        errString = ERR_01_PRINTER;
                        connection.close();
                    } else {
                        sqlStr = "UPDATE RESMaster.dbo.MST_PRINTERINFO"
                                + " SET Status='Deleted',DeleteFlag=1,"
                                + " DelDate=CURRENT_TIMESTAMP, DelAppId='system',DelOpeCode='system'"
                                + " WHERE CompanyId=? and StoreId=? and PrinterId=?";
                        PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                        psUpd.setString(1, request.getParameter("companyID"));
                        psUpd.setString(2, request.getParameter("storeID"));
                        psUpd.setString(3, request.getParameter("printerID"));
                 
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
                    
                    String sqlStr = "UPDATE RESMaster.dbo.MST_PRINTERINFO"
                            + " SET Status='Active',DeleteFlag=0,"
                            + " UpdCount=UpdCount+1,UpdDate=CURRENT_TIMESTAMP, UpdAppId='system',UpdOpeCode='system'"
                            + " WHERE CompanyId=? and StoreId=? and PrinterId=?";
                    PreparedStatement psUpd = connection.prepareStatement(sqlStr);
                    psUpd.setString(1, request.getParameter("companyID"));
                    psUpd.setString(2, request.getParameter("storeID"));
                    psUpd.setString(3, request.getParameter("printerID"));
                 
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
    <title>NCR RES PrinterUpdate Viewer</title>
    <link rel="stylesheet" type="text/css" href="./css/default.css">
    <!-- jQuery -->
    <script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
    <!-- ダイアログ共通 -->
    <script type="text/javascript" src="./js/DialogMessage.js"></script>
</head>
<body class="res-maincontent">
プリンター照会<br><br>
※プリンターを照会・更新・削除することができます。<br><br>

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
  
  <div class="table-scroll-area-v table-scroll-area-h" id="tablearea" style="display:none; height:315px">
    <table class="res-tbl">
      <thead>
        <tr>
          <th></th>
          <th>プリンターID(PrinterId)</th>
          <th>プリンター名(PrinterName)</th>
          <th>プリンター説明(Description)</th>
          <th>プリンターIPアドレス(IpAddress)</th>
          <th>ステータス(PrinterStatus)</th>
        </tr>
      </thead>
      <tbody id="logs">
      </tbody>
    </table>
  </div>

  <form action="PrinterUpdate.jsp" method="post" id="updateform">
    <input type="hidden" name="action" id="action">
    <input type="hidden" name="companyID" id="companyID">
    <input type="hidden" name="storeID" id="storeID">
    <input type="hidden" name="printerID" id="printerID">
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
            <td align="right">プリンターID ： </td>
            <td>
              <input type="text" disabled name="CheckedPrinterId" id="CheckedPrinterId"></td>
          </tr>
          <tr>
            <td align="right">プリンター名 ： </td>
            <td>
              <input maxlength="20" type="text" name="CheckedPrinterName" id="CheckedPrinterName" size=40 pattern=".{0,20}">(全角20文字以内で入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">プリンター説明 ： </td>
            <td align="left">
              <input maxlength="40" type="text" name="CheckedPrinterDescription" id="CheckedPrinterDescription" size=40 required pattern=".{0,20}">(全角20文字以内で入力してください。)
            </td>
          </tr>
          <tr>
            <td align="right">プリンター種別 ： </td>
            <td>
              <select name="CheckedPrinterType" id="CheckedPrinterType" onChange="changePrinterType(this)" required style="width:50%">
              <%
                for (int i=0;i<PRINTER_TYPE.size();i++) {
                  out.print("<option value=\"" + PRINTER_TYPE.get(i) + "\"");
                  out.println(">" + PRINTER_TYPE.get(i) + "</option>");
                }
              %>
              </select>
            </td>
          </tr>
          <tr id="printerIpRow">
            <td align="right">プリンターIPアドレス ： </td>
            <td>
              <input maxlength="15" type="text" name="CheckedIpAddress" id="CheckedIpAddress" size=15 required pattern="^\d{1,3}(\.\d{1,3}){3}$">
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
function changePrinterType(obj) {
    if (obj.value == 'USBプリンター') {
        document.getElementById('CheckedIpAddress').disabled = true;
        document.getElementById('printerIpRow').style.display = 'none';
    } else {
        document.getElementById('CheckedIpAddress').disabled = false;
        document.getElementById('printerIpRow').style.display = '';
    }
}

function check(inValue) {
    // PrinterId
    var strId = 'PrinterId' + inValue;
    document.getElementById('CheckedPrinterId').value = document.getElementById(strId).value || '';
    document.getElementById('printerID').value = document.getElementById(strId).value || false;
    
    // PrinterName
    strId = 'PrinterName' + inValue;
    document.getElementById('CheckedPrinterName').value = document.getElementById(strId).value || '';
    
    // PrinterDescription
    strId = 'PrinterDescription' + inValue;
    document.getElementById('CheckedPrinterDescription').value = document.getElementById(strId).value || '';

    // IpAddress
    strId = 'IpAddress' + inValue;
    document.getElementById('CheckedIpAddress').value = document.getElementById(strId).value || '';
    if (!document.getElementById('CheckedIpAddress').value) {
        document.getElementById('CheckedPrinterType').value = 'USBプリンター';
    } else {
        document.getElementById('CheckedPrinterType').value = 'ネットワークプリンター';
    }
    
    // PrinterStatus
    //strId = 'PrinterStatus' + inValue;
    //document.getElementById('CheckedPrinterStatus').value = document.getElementById(strId).value || false;
    
    strId = 'PrinterDeleted' + inValue;
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
            if (input_tags[i].id == "CheckedCompanyID" || input_tags[i].id == "CheckedStoreID" || input_tags[i].id == "CheckedPrinterId") {
                continue;
            } else {
                input_tags[i].disabled = false;
            }
        }
        var select_tags = document.getElementById("panel").getElementsByTagName("select");
        for(var i=0;i<select_tags.length;i++){
            select_tags[i].disabled = false;
        }
        
        changePrinterType(document.getElementById('CheckedPrinterType'));
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
            if (currentLog[i].PrinterStatus == 'Deleted' || currentLog[i].PrinterDeleteFlag=='1') {
                log += '<tr class="deleted-row">' 
            } else {
                log += '<tr>' 
            }
            log += '<td align="center">'+ '<input type="radio" name="q1" id="' 
                    + i + '" onclick="check(id)">' + '</td>';
            log += '<td><input type="text" id="PrinterId' + i 
                    + '" name="PrinterId' + i + '" disabled value="'
                    + (currentLog[i].PrinterId || '' ) + '" >' + '</td>';
            log += '<td><input type="text" id="PrinterName' + i 
                + '" name="PrinterName' + i + '" disabled value="'
                + (currentLog[i].PrinterName || '' ) + '">' + '</td>';
            log += '<td><input type="text" id="PrinterDescription' + i 
                + '" name="PrinterDescription' + i + '" disabled value="'
                + (currentLog[i].PrinterDescription || '' ) + '">' + '</td>';
            log += '<td><input type="text" id="IpAddress' + i  
                + '" name="IpAddress' + i + '" disabled value="'
                + (currentLog[i].IpAddress || '' ) + '">' + '</td>';
                
            if (currentLog[i].PrinterStatus == 'Deleted' || currentLog[i].PrinterDeleteFlag=='1') {
                log += '<td><input type="text" id="PrinterDeleted' + i  
                + '" name="PrinterDeleted' + i + '" disabled value="'
                + 'Deleted' + '">';
            } else {
                log += '<td><input type="text" id="PrinterDeleted' + i  
                + '" name="PrinterDeleted' + i + '" disabled value="'
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
            xhr.open('GET','PrinterUpdate.jsp?'
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
