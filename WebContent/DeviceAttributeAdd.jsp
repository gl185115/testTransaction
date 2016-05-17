<%@ page language="java" pageEncoding="utf-8"%>
<%@page
	import="java.sql.*"
	import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
	import="java.util.Date"
	import="java.util.ArrayList"
	import="java.text.SimpleDateFormat"%>
<%!
final String ERR_01_TERMINALID = "属性番号が既に存在します。";
final String ERR_02_INTERNAL = "内部エラーが発生しました。";
final String INFO_01_INSERT = "属性の新規登録に成功しました。";
final String CONFIRM_01_INSERT = "属性を登録してよろしいですか。";

ArrayList<String> PRINTER_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> PRINTER_NAME = new ArrayList<String>() {{add("接続されない"); add("接続される");}};
ArrayList<String> TILL_VAL = new ArrayList<String>() {{add("Manual"); add("Auto"); add("None");}};
ArrayList<String> CREDIT_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> CREDIT_NAME = new ArrayList<String>() {{add("クレジット処理不可"); add("クレジット処理可");}};
ArrayList<String> MSR_VAL = new ArrayList<String>() {{add("0"); add("1"); add("2");}};
ArrayList<String> MSR_NAME = new ArrayList<String>() {{add("付属なし"); add("カードリーダー"); add("iSMR");}};
ArrayList<String> CASH_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> CASH_NAME = new ArrayList<String>() {{add("付属なし"); add("付属あり");}};
ArrayList<String> ATT1_VAL = new ArrayList<String>() {{add("1"); add("2"); add("3");}};
ArrayList<String> ATT1_NAME = new ArrayList<String>() {{add("POSスキャナー"); add("Bluetoothスキャナー"); add("ScanTab");}};
ArrayList<String> ATT2_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT2_NAME = new ArrayList<String>() {{add("開設/精算を行わない"); add("開設/精算を行う");}};
ArrayList<String> ATT3_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT3_NAME = new ArrayList<String>() {{add("前捌きのみ"); add("決済端末");}};
ArrayList<String> ATT4_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT4_NAME = new ArrayList<String>() {{add("ＣＩＤ付属なし"); add("ＣＩＤ付属あり");}};
ArrayList<String> ATT5_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT5_NAME = new ArrayList<String>() {{add("Line Display付属なし"); add("Line Display付属あり");}};
ArrayList<String> ATT6_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT6_NAME = new ArrayList<String>() {{add("精算後シャットダウンしない"); add("精算後シャットダウンする");}};
ArrayList<String> ATT7_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT7_NAME = new ArrayList<String>() {{add("SDMC初期化しない"); add("SDMC初期化する");}};
%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");

	String sqlStr = "";
	String errString = "";
	String infoString = "";

	if (request.getParameter("AttributeId") != null) {
		JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
		Connection connection = dbManager.getConnection();
		sqlStr =  "SELECT device_attribute.AttributeId AS AttributeId"
				+ " FROM RESMaster.dbo.PRM_DEVICE_ATTRIBUTE device_attribute"
				+ " WHERE AttributeId=?";
		PreparedStatement psSelect = connection.prepareStatement(sqlStr);
		psSelect.setString(1, request.getParameter("AttributeId").toString());
		ResultSet rsSelect = psSelect.executeQuery();

		if (rsSelect.next()) {
			errString = ERR_01_TERMINALID;
			psSelect.close();
			connection.close();
		} else {
			sqlStr = "INSERT INTO RESMaster.dbo.PRM_DEVICE_ATTRIBUTE"
					+ "(AttributeId, Description, Printer, Till, CreditTerminal, MSR, CashChanger, "
					+ "Attribute1, Attribute2, Attribute3, Attribute4, Attribute5, "
					+ "Attribute6, Attribute7)"
//					+ "Attribute6, Attribute7, Attribute8, Attribute9, Attribute10)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
//					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			Date nowDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String today = formatter.format(nowDate);
			PreparedStatement psIns = connection.prepareStatement(sqlStr);

			psIns.setString(1, request.getParameter("AttributeId"));
			psIns.setString(2, request.getParameter("Description"));
			psIns.setString(3, request.getParameter("Printer"));
			psIns.setString(4, request.getParameter("Till"));
			psIns.setString(5, request.getParameter("CreditTerminal"));
			psIns.setString(6, request.getParameter("MSR"));
			psIns.setString(7, request.getParameter("CashChanger"));
			psIns.setString(8, request.getParameter("Attribute1"));
			psIns.setString(9, request.getParameter("Attribute2"));
			psIns.setString(10, request.getParameter("Attribute3"));
			psIns.setString(11, request.getParameter("Attribute4"));
			psIns.setString(12, request.getParameter("Attribute5"));
			psIns.setString(13, request.getParameter("Attribute6"));
			psIns.setString(14, request.getParameter("Attribute7"));
//			psIns.setString(15, request.getParameter("Attribute8"));
//			psIns.setString(16, request.getParameter("Attribute9"));
//			psIns.setString(17, request.getParameter("Attribute10"));

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
    <!-- jQuery -->
    <script type="text/javascript" src="./jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="./jquery-ui.min.js"></script>
    <!-- ダイアログ共通 -->
    <script type="text/javascript" src="./DialogMessage.js"></script>
</head>
<body class="res-maincontent">
属性登録<br><br>
 ※下記すべて入力し、最後に登録を押下してください。　<br><br>
<label class="res-err-msg"><%out.println(errString); %></label>
<label class="res-info-msg"><%out.println(infoString); %></label>

	<form action="DeviceAttributeAdd.jsp" method="post" id="DeviceAttributeAdd">
	<div class="panel">
		<table border="0" cellspacing="4" cellpadding="4">
			<tr>
				<td align="right">属性番号 ： </td>
				<td align="left"><input type="text" name="AttributeId" id="AttributeId" size=3 maxlength="3" required pattern="\d{1,3}">(半角数字3桁以内で入力してください)</td>
			</tr>
			<tr>
				<td align="right">属性説明 ： </td>
				<td align="left"><input type="text" id="Description" name="Description" size=50 maxlength="25" required pattern=".{0,25}">(全角25文字以内で入力してください)</td>
			</tr>
			<tr>
				<td align="right">プリンター ： </td>
				<td align="left">
					<select name="Printer" id="Printer" required>
					<%
						for (int i=0;i<PRINTER_VAL.size();i++) {
							out.print("<option value=\"" + PRINTER_VAL.get(i) + "\"");
							out.println(">" + PRINTER_VAL.get(i) +" : " + PRINTER_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">ドロワー ： </td>
				<td align="left">
					<select name="Till" id="Till" required>
					<%
						for (int i=0;i<TILL_VAL.size();i++) {
							out.print("<option value=\"" + TILL_VAL.get(i) + "\"");
							out.println(">" + TILL_VAL.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">クレジット ： </td>
				<td align="left">
					<select name="CreditTerminal" id="CreditTerminal" required>
					<%
						for (int i=0;i<CREDIT_VAL.size();i++) {
							out.print("<option value=\"" + CREDIT_VAL.get(i) + "\"");
							out.println(">" + CREDIT_VAL.get(i) +" : " + CREDIT_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">MSR ： </td>
				<td align="left">
					<select name="MSR" id="MSR" required>
					<%
						for (int i=0;i<MSR_VAL.size();i++) {
							out.print("<option value=\"" + MSR_VAL.get(i) + "\"");
							out.println(">" + MSR_VAL.get(i) +" : " + MSR_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">釣銭機 ： </td>
				<td align="left">
					<select name="CashChanger" id="CashChanger" required>
					<%
						for (int i=0;i<CASH_VAL.size();i++) {
							out.print("<option value=\"" + CASH_VAL.get(i) + "\"");
							out.println(">" + CASH_VAL.get(i) +" : " + CASH_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性１ ： </td>
				<td align="left">
					<select name="Attribute1" id="Attribute1" required>
					<%
						for (int i=0;i<ATT1_VAL.size();i++) {
							out.print("<option value=\"" + ATT1_VAL.get(i) + "\"");
							out.println(">" + ATT1_VAL.get(i) +" : " + ATT1_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性２ ： </td>
				<td align="left">
					<select name="Attribute2" id="Attribute2" required>
					<%
						for (int i=0;i<ATT2_VAL.size();i++) {
							out.print("<option value=\"" + ATT2_VAL.get(i) + "\"");
							out.println(">" + ATT2_VAL.get(i) +" : " + ATT2_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性３ ： </td>
				<td align="left">
					<select name="Attribute3" id="Attribute3" required>
					<%
						for (int i=0;i<ATT3_VAL.size();i++) {
							out.print("<option value=\"" + ATT3_VAL.get(i) + "\"");
							out.println(">" + ATT3_VAL.get(i) +" : " + ATT3_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性４ ： </td>
				<td align="left">
					<select name="Attribute4" id="Attribute4" required>
					<%
						for (int i=0;i<ATT4_VAL.size();i++) {
							out.print("<option value=\"" + ATT4_VAL.get(i) + "\"");
							out.println(">" + ATT4_VAL.get(i) +" : " + ATT4_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			<tr>
				<td align="right">属性５ ： </td>
				<td align="left">
					<select name="Attribute5" id="Attribute5" required>
					<%
						for (int i=0;i<ATT5_VAL.size();i++) {
							out.print("<option value=\"" + ATT5_VAL.get(i) + "\"");
							out.println(">" + ATT5_VAL.get(i) +" : " + ATT5_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性６ ： </td>
				<td align="left">
					<select name="Attribute6" id="Attribute6" required>
					<%
						for (int i=0;i<ATT6_VAL.size();i++) {
							out.print("<option value=\"" + ATT6_VAL.get(i) + "\"");
							out.println(">" + ATT6_VAL.get(i) +" : " + ATT6_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性７ ： </td>
				<td align="left">
					<select name="Attribute7" id="Attribute7" required>
					<%
						for (int i=0;i<ATT7_VAL.size();i++) {
							out.print("<option value=\"" + ATT7_VAL.get(i) + "\"");
							out.println(">" + ATT7_VAL.get(i) +" : " + ATT7_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
<!--			<tr>
				<td class="center">属性８</td>
				<td><input maxlength="4" type="text" name="Attribute8" id="Attribute89" size=4></td>
			</tr>
			<tr>
				<td class="center">属性９</td>
				<td><input maxlength="4" type="text" name="Attribute9" id="Attribute9" size=4></td>
			</tr>
			<tr>
				<td class="center">属性１０</td>
				<td><input maxlength="4" type="text" name="Attribute10" id="Attribute10" size=4></td>
			</tr>	-->
		</table>
		<br>
	</div>
	<div align="right">
		<input type="button" value="登録" id="insertDev" name="insertDev" class="res-big-green"> 
	</div>
	<br>
    <button id="fakeButton" style="display:none"></button>
	</form>
</body>

<script type="text/javascript">
jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#insertDev').click(function(e){
        var myform = document.getElementById('DeviceAttributeAdd');
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
</script>

</html>
