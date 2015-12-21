<%@ page language="java" pageEncoding="utf-8"%><%@page import="javax.xml.transform.*,javax.xml.transform.stream.*,java.io.*,java.sql.*"%><%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%><%
if (request.getParameter("s") != null) {
   JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer)JndiDBManagerMSSqlServer.getInstance();
   Connection conn = dbManager.getConnection();
   PreparedStatement ps = conn.prepareStatement("select Tx "
     + "from TXL_SALES_JOURNAL "
     + "where RetailStoreId=? and WorkstationId=? and SequenceNumber=? "
     + "order by InsDateTime asc");
   String[] params = {"s", "w", "x" };
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
     transformer.transform(source, result);
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
</head>
<body>
<div>
  <label>店番号
    <input type="text" id="storeID" value="" size="8" maxlength=16"/>
  </label>
  <label>ワークステーション番号
    <input type="text" id="workstationID" value="" size="4" maxlength=16"/>
  </label>
  <label>取引番号
    <input type="text" id="seqNum" value="" size="8" maxlength=8"/>
  </label>
  <div>
    <input type="button" id="start" value="検索"/>
  </div>
</div>
<div>
  <div id="resultData">
  </div>
</div>
</body>
<script type="text/javascript">
(function() {
  document.getElementById('start').addEventListener('click', function() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
    try {
      if (!xhr.responseText || xhr.responseText.length == 0) {
        alert('no result');
        document.getElementById('resultData').innerHTML = '';
        return false;
      }
      var poslog = xhr.responseText;
      /* in case of skip eventlog
      poslog = poslog.substring(0, poslog.indexOf('<EventLog>'))
          + poslog.substring(poslog.indexOf('</POSLog>'));
      */
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
      xhr.open('GET', 'poslog.jsp?s=' + document.getElementById('storeID').value
                    + '&w=' + document.getElementById('workstationID').value
                    + '&x=' + document.getElementById('seqNum').value, true);
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