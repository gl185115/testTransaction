<%@ page language="java" pageEncoding="utf-8"%><%@page import="java.sql.*"%><%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%><%
if (request.getParameter("s") != null) {
   java.lang.StringBuilder sb = new java.lang.StringBuilder();
   JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer)JndiDBManagerMSSqlServer.getInstance();
   Connection conn = dbManager.getConnection();
   PreparedStatement ps = conn.prepareStatement("select Tx "
     + "from TXL_SALES_JOURNAL "
     + "where CompanyId=? and RetailStoreId=? and WorkstationId=? and SequenceNumber=? and TrainingFlag=? "
     + "order by InsDateTime asc");
   String[] params = {"c", "s", "w", "x", "t" };
   for (int i = 0; i < params.length; i++) {
     ps.setString(i + 1, request.getParameter(params[i]));
   }
   ResultSet rs = ps.executeQuery();
   if (rs.next()) {
     String tx = rs.getString(1);
     int begin = tx.indexOf("<EventLog>") + 10;
     int end = tx.indexOf("</EventLog>");
     sb.append(tx, begin, end);
   }
   rs.close();
   ps.close();
   conn.close();
   out.print(sb.toString());
} else {
%>
<!DOCTYPE html>
<html >
<head>
  <title>NCR RES EventLog Viewer</title>
</head>
<body>
<div>
  <label>企業番号
    <select name="companyID" id="companyID">
      <option value="01" selected>SAMPLE</option>
      <option value="02">SPORTSTORE02</option>
    </select>
  </label>
  <label>店番号
    <input type="text" id="storeID" value="" size="8" maxlength=16"/>
  </label>
  <label>ワークステーション番号
    <input type="text" id="workstationID" value="" size="4" maxlength=16"/>
  </label>
  <label>取引番号
    <input type="text" id="seqNum" value="" size="8" maxlength=8"/>
  </label>
  <label>タイプ
    <select name="training" id="training">
      <option value="0" selected>本番</option>
      <option value="1">トレーニング</option>
    </select>
  </label>
  <label>wsを隠す
    <input type="checkbox" id="showWs"/>
  </label>
  <label>ajaxを隠す
    <input type="checkbox" id="showAjax"/>
  </label>
  <div>
    <input type="button" id="start" value="検索"/>
    <input type="button" id="paste" value="貼付"/>
  </div>
</div>
<div>
  <textarea id="rawdata" rows="16" style="width:80%">
  </textarea>
</div>
<div id="resp">
</div>
</body>
<script type="text/javascript">
(function() {
  var currentLog = null;
  var to2 = function(t, n) {
    var s = t.toString();
    if (!n) n = 2;
    while (s.length < n) {
      s = '0' + s;
    }
    return s;
  };
  var showLog = function() {
    if (currentLog == null) return;
    var s = '<table border="1"><tr><th>時刻</th><th style="width:10%">種類</th><th style="width:60%">データ</th></tr>';
    for (var i = 0; i < currentLog.length; i++) {
      if (document.getElementById('showWs').checked) {
        if (currentLog[i].source) continue;
      }
      if (document.getElementById('showAjax').checked) {
        if (currentLog[i].to) continue;
      }
      var dt = new Date(currentLog[i].stamp);
      s += '<tr><td>' + to2(dt.getHours()) + ':' + to2(dt.getMinutes()) + ':' + to2(dt.getSeconds())
           + '.' + to2(dt.getMilliseconds(), 3) + '</td><td>';

      if (currentLog[i].to) {
        var ajresp = (typeof (currentLog[i].response) === 'object') ? JSON.stringify(currentLog[i].response) : currentLog[i].response;
        if (!ajresp) ajresp = currentLog[i].partial;
        s += 'ajax</td><td><div>' + ajresp + '</div><div>' + currentLog[i].to + '</div>';
      } else if (currentLog[i].keypress) {
        s += 'keybd</td><td>' + currentLog[i].keypress;
      } else if (currentLog[i].unsoli) {
        s += 'unsoli</td><td><div>' + currentLog[i].unsoli + '</div><div>' + JSON.stringify(currentLog[i].data) + '</div>';
      } else if (currentLog[i].popup) {
        s += 'popup</td><td>' + currentLog[i].popup;
      } else if (currentLog[i].file) {
        s += 'click</td><td><div>' + currentLog[i].text + '</div><div>' + currentLog[i].file + '</div>';
      } else {
        var data = (currentLog[i].data) ? currentLog[i].data : currentLog[i].partial;
        s += 'ws</td><td><div>' + currentLog[i].source + ':' + currentLog[i].context + ':' + currentLog[i].event
           + '</div><div>' + data + '</div>';
      }
      s += '</td></tr>';
    }
    document.getElementById('resp').innerHTML = s + '</table>';
  };
  document.getElementById('showWs').addEventListener('click', function() {
    showLog();
  });
  document.getElementById('showAjax').addEventListener('click', function() {
    showLog();
  });
  document.getElementById('paste').addEventListener('click', function() {
    currentLog = JSON.parse(document.getElementById('rawdata').value);
    showLog();
  });
  document.getElementById('start').addEventListener('click', function() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
    try {
      if (!xhr.responseText || xhr.responseText.length == 0) {
        alert('no result');
        document.getElementById('rawdata').innerHTML = '';
        document.getElementById('resp').innerHTML = '';
        return false;
      }
      document.getElementById('rawdata').innerHTML = xhr.responseText;
      currentLog = JSON.parse(xhr.responseText);
      showLog();
    } catch (e) {
      alert(e.name + ':' + e.message + ':' + e.stack);
    }
    };
    xhr.onerror = function(e) {
      alert(e);
    };
    try {
      xhr.open('GET', 'eventlog.jsp?c=' + document.getElementById('companyID').value 
                    + '&s=' + document.getElementById('storeID').value
                    + '&w=' + document.getElementById('workstationID').value
                    + '&x=' + document.getElementById('seqNum').value
                    + '&t=' + document.getElementById('training').value, true);
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