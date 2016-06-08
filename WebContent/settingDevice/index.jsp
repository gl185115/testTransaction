<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="./css/default.css">
<script type="text/javascript">
function treeMenu(tName) {
	  tMenu = document.all[tName].style;
	  if(tMenu.display == 'none') tMenu.display = "block";
	  else tMenu.display = "none";
}
function selectPage(id){
    var mainiframe = document.getElementById("maincontent");
    switch(id){
    case "Device":
        mainiframe.src = "DeviceInfo.jsp";
        break;
    case "AddDevice":
        mainiframe.src = "DeviceAdd.jsp";
        break;
    case "PosLog":
        mainiframe.src = "poslog.jsp";
        break;
    case "EventLog":
        mainiframe.src = "eventlog.jsp";
        break;
    case "DeviceAttributeUpdate":
        mainiframe.src = "DeviceAttributeUpdate.jsp";
        break;
    case "PrinterUpdate":
        mainiframe.src = "PrinterUpdate.jsp";
        break;
    case "TillUpdate":
        mainiframe.src = "TillUpdate.jsp";
        break;
    case "QueuebusterUpd":
        mainiframe.src = "QueuebusterUpdate.jsp";
        break;
    case "DeviceAttributeAdd":
        mainiframe.src = "DeviceAttributeAdd.jsp";
        break;
    case "QueuebusterAdd":
        mainiframe.src = "QueuebusterLinkAdd.jsp";
        break;
    default:
    	break;
	}
}
</script>
<title>NCR</title>
</head>
<body class="indexBody">
  <div class="indexTop">NCR</div>
  <div class="indexMiddle">
    <div class="indexLeft">

      <a href="javascript:void(0)" title="Information" onclick="treeMenu('indexUpdate')">照会</a>
      <div id=indexUpdate style="display:none; text-indent:1em">
        <a href="javascript:void(0)" title="Device Information" onclick="selectPage('Device')">端末照会</a>
        <a href="javascript:void(0)" title="PrinterUpdate" onclick="selectPage('PrinterUpdate')">プリンター照会</a>
        <a href="javascript:void(0)" title="TillUpdate" onclick="selectPage('TillUpdate')">ドロワー照会</a>
        <a href="javascript:void(0)" title="DeviceAttributeUpdate" onclick="selectPage('DeviceAttributeUpdate')">属性照会</a>
        <a href="javascript:void(0)" title="QueuebusterUpd" onclick="selectPage('QueuebusterUpd')">前捌照会</a>
      </div>

      <a href="javascript:void(0)" title="Add" onclick="treeMenu('indexAdd')">登録</a>
      <div id="indexAdd" style="display:none; text-indent:1em">
        <a href="javascript:void(0)" title="Add Device" onclick="selectPage('AddDevice')">端末登録</a>
        <a href="javascript:void(0)" title="DeviceAttributeAdd" onclick="selectPage('DeviceAttributeAdd')">属性登録</a>
        <a href="javascript:void(0)" title="QueuebusterAdd" onclick="selectPage('QueuebusterAdd')">前捌登録</a>
      </div>

      <a href="javascript:void(0)" title="Log" onclick="treeMenu('indexLog')">ログ照会</a>
      <div id="indexLog" style="display:none; text-indent:1em">
        <a href="javascript:void(0)" title="PosLog" onclick="selectPage('PosLog')">ＰＯＳログ照会</a>
        <a href="javascript:void(0)" title="EventLog" onclick="selectPage('EventLog')">イベントログ照会</a>
      </div>

    </div>
    <div class="indexRight">
      <iframe id="maincontent" name="maincontent"></iframe>
    </div>
  </div>
</body>
</html>