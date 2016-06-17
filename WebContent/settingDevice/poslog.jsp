<%@ page language="java" pageEncoding="utf-8"%><%@page import="javax.xml.transform.*,javax.xml.transform.stream.*,java.io.*,java.sql.*"%><%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%><%!
final String ERR_01_NOTFOUND = "データが存在しません。";
%><%
if (request.getParameter("s") != null) {
   JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer)JndiDBManagerMSSqlServer.getInstance();
   Connection conn = dbManager.getConnection();
   PreparedStatement ps = conn.prepareStatement("select Tx "
     + "from TXL_SALES_JOURNAL "
     + "where CompanyId=? and RetailStoreId=? and WorkstationId=? and SequenceNumber=? and TrainingFlag=? "
     + "order by InsDateTime desc");
   String[] params = {"c", "s", "w", "x", "t" };
   for (int i = 0; i < params.length; i++) {
     ps.setString(i + 1, request.getParameter(params[i]));
   }
   ResultSet rs = ps.executeQuery();
   if (rs.next()) {
     StreamResult result = new StreamResult(out);
     StreamSource source = new StreamSource(new StringReader(rs.getString(1)));
     Transformer transformer = TransformerFactory.newInstance().newTransformer();
     transformer.setOutputProperty(OutputKeys.INDENT, "yes");
     transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
     try {
		transformer.transform(source, result);
     } catch (Exception e) {
     }
   }
   rs.close();
   ps.close();
   conn.close();
} else {
%>
<!DOCTYPE html>
<html >
<head>
  <title>NCR RES POSLog Viewer</title>
  <link rel="stylesheet" type="text/css" href="./css/default.css">
</head>
<body class="res-maincontent">
<div>
POSログ照会<br><br>
  <iframe name="storesearch" id="storeSearch" src="./StoreSearch.jsp"></iframe><br>
  <form  id="searchform" style="display:none" onsubmit="return false;">
   <table style="padding: 10px;">
   <tr>
   <td>　　端末番号 ： <input type="text" id="workstationID" value="" size="4" maxlength="4" required pattern="\d{4}"></td>
   <td>　　取引NO ： <input type="text" id="seqNum" size="5" maxlength="5" required pattern="\d{1,5}"></td>
   <td>　　タイプ ： 
    <select name="training" id="training" required>
      <option value="0" selected>本番</option>
      <option value="1">トレーニング</option>
    </select>
   </td>
   </tr>
   </table>
  <div>
    <button type="button" id="start" class="res-small-green">検索</button>
  </div>
    <button id="fakeButton" style="display:none"></button>
  <div class="panel" style="height:670px">
    <div id="resultData" class="htmlArea table-scroll-area-h table-scroll-area-v"></div>
  </div>
  </form>
</div>
</body>
<!-- jQuery -->
<script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
<!-- jQuery UI -->
<script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
<!-- ダイアログ共通 -->
<script type="text/javascript" src="./js/DialogMessage.js"></script>
<script type="text/javascript">
window.onload = function() {
    storesearch.document.getElementById('companyidlist').addEventListener('change', function(){
        if (storesearch.document.getElementById('storeidlist').selectedIndex != -1) {
            document.getElementById('searchform').style.display = "block";
        } else {
            document.getElementById('searchform').style.display = "none";
        }
    }, false );
};

(function(inValue) {
  document.getElementById('start').addEventListener('click', function() {
    var myform = document.getElementById('searchform');
    if (myform.checkValidity() == false) {
    	document.getElementById('fakeButton').click();
      return;
    }

    var companyId = storesearch.document.getElementById('companyidlist').value;
    var storeId = storesearch.document.getElementById('storeidlist').value;

    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
    try {
      if (!xhr.responseText || xhr.responseText.length == 0) {
        showDialog(
                  "タイトル：未使用",
                  <%='\'' + ERR_01_NOTFOUND + '\''%>,
                  ButtonOK,
                  function() {
                  }
              );
        return false;
      }
      var poslog = xhr.responseText;
      document.getElementById('resultData').innerHTML
      = '<pre>' + poslog.replace(/&/gm, '&amp;').replace(/</gm, '&lt;').replace(/>/gm, '&gt;') + '</pre>';
    } catch (e) {
      alert(e.name + ':' + e.message + ':' + e.stack);
    }
    };
    xhr.onerror = function(e) {
      alert(e);
    };

    try {
      xhr.open('GET', 'poslog.jsp?c=' + companyId
    		  		+ '&s=' + storeId
                    + '&w=' + document.getElementById('workstationID').value
                    + '&x=' + document.getElementById('seqNum').value
				    + '&t=' + document.getElementById('training').value, true);
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