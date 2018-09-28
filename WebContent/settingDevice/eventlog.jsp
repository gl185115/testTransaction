<%@ page language="java" pageEncoding="utf-8"%><%@page import="java.sql.*"%><%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%><%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%><%!
final String ERR_01_NOTFOUND = "データが存在しません。";
final String ERR_02_NOTFOUND = "貼付データ形式が正しくありません。";
final String ERR_01_NOTDATE = "貼付データが入力されていません。";
%><%
if (request.getParameter("s") != null) {
   java.lang.StringBuilder sb = new java.lang.StringBuilder();
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
     String tx = rs.getString(1);
     if (tx.length() > 0 && tx.indexOf("<EventLog>") >= 0) {
         int begin = tx.indexOf("<EventLog>") + 10;
         int end = tx.indexOf("</EventLog>");

         if (end >= 0) {
             sb.append(tx, begin, end);
         }
     } else {
    	 ps.close();
    	 ps = conn.prepareStatement("select Log "
    			 + "from TXL_OPE_EVENT "
    			 + "where CompanyId=? and RetailStoreId=? and WorkstationId=? and SequenceNumber=? and TrainingFlag=? "
    			 + "order by InsDateTime desc");
    	 for (int i = 0; i < params.length; i++) {
    		 ps.setString(i + 1, request.getParameter(params[i]));
    	 }
    	 rs = ps.executeQuery();
    	 if(rs.next()){
    		 String log = rs.getString(1);
    		 if (log.length() > 0) {
    				 sb.append(log);
    		 }
    	 }
     }
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
  <link rel="stylesheet" type="text/css" href="./css/default.css">
</head>
<body class="res-maincontent">
<div>
イベントログ照会<br><br>
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
	   <tr>
		   <td>　　wsを隠す ： <input type="checkbox" id="showWs"></td>
		   <td>　　ajaxを隠す ： <input type="checkbox" id="showAjax"></td>
	   </tr>
   </table>
  <div>
    <button type="button" id="start" class="res-small-green">検索</button>
    <button type="button" id="paste" class="res-small-green">貼付</button>
<button id="fakeButton" style="display:none"></button>
  </div>
<div>
  <textarea id="rawdata" class="rawdata" style="height: 120px;">
  </textarea>
</div>
<div id="resp" style="height:540px; overflow-y:scroll;">
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
    var s = '<table class="res-tbl"><tr><th>時刻</th><th style="width:10%">種類</th><th style="width:60%">データ</th></tr>';
    for (var i = 0; i < currentLog.length; i++) {
      if (document.getElementById('showWs').checked) {
        if (currentLog[i].source) continue;
      }
      if (document.getElementById('showAjax').checked) {
        if (currentLog[i].to) continue;
      }
      if (currentLog[i].stamp != null) {
      var dt = new Date(currentLog[i].stamp);
      s += '<tr><td>' + to2(dt.getHours()) + ':' + to2(dt.getMinutes()) + ':' + to2(dt.getSeconds())
           + '.' + to2(dt.getMilliseconds(), 3) + '</td><td>';
      } else {
          s += '<tr><td></td><td>';
      }

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
      } else if (currentLog[i].source) {
        var data = (currentLog[i].data) ? currentLog[i].data : currentLog[i].partial;
        s += 'ws</td><td><div>' + currentLog[i].source + ':' + currentLog[i].context + ':' + currentLog[i].event
           + '</div><div>' + data + '</div>';
      } else {
         s += 'other</td><td><div>' + JSON.stringify(currentLog[i]);
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

	if (document.getElementById('rawdata').value <= 0) {
		showDialog(
				"タイトル：未使用",
				<%='\'' + ERR_01_NOTDATE + '\''%>,
				ButtonOK,
				function() {
					}
				);
		return false;
	}

	try {
		currentLog = JSON.parse(document.getElementById('rawdata').value);
		var type = typeof currentLog
		if (type != "object" || currentLog == null) {
			showDialog(
					"タイトル：未使用",
					<%='\'' + ERR_02_NOTFOUND + '\''%>,
					ButtonOK,
					function() {
						}
					);
			return false;
		}
	} catch (e) {
		showDialog(
				"タイトル：未使用",
				<%='\'' + ERR_02_NOTFOUND + '\''%>,
				ButtonOK,
				function() {
					}
				);
		return false;
	}
    showLog();
  });
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
      document.getElementById('rawdata').innerHTML = xhr.responseText;
      $('#rawdata').val(xhr.responseText);
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
      xhr.open('GET', 'eventlog.jsp?c=' + companyId
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