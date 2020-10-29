<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
	import="java.util.ArrayList"
	import="java.sql.*"%>
<%!
final String ERR_01_UPDATE = "属性の更新に失敗しました。<br>システム担当者に確認してください。";
final String ERR_02_INTERNAL = "内部エラーが発生しました。<br>システム担当者に確認してください。";
final String ERR_03_ATTRIBUTE = "属性１と属性１０<br>の整合性がとれていません。";
final String INFO_01_UPDATE = "属性の更新に成功しました。";
final String CONFIRM_01_UPDATE = "属性を更新してよろしいですか。";

ArrayList<String> PRINTER_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> PRINTER_NAME = new ArrayList<String>() {{add("接続されない"); add("接続される");}};
ArrayList<String> TILL_VAL = new ArrayList<String>() {{add("Manual"); add("Auto"); add("None");}};
ArrayList<String> CREDIT_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> CREDIT_NAME = new ArrayList<String>() {{add("クレジット処理不可"); add("クレジット処理可");}};
ArrayList<String> MSR_VAL = new ArrayList<String>() {{add("0"); add("1"); add("2"); add("3"); add("4"); add("5"); add("6"); add("7");}};
ArrayList<String> MSR_NAME = new ArrayList<String>() {{add("なし"); add("カードリーダー"); add("iSMP"); add("Infox"); add("ルミネ"); add("ららぽーと"); add("Ingenico"); add("CAFIS Arch");}};
ArrayList<String> CASH_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> CASH_NAME = new ArrayList<String>() {{add("なし"); add("あり");}};
ArrayList<String> ATT1_VAL = new ArrayList<String>() {{add("1"); add("2"); add("3");}};
ArrayList<String> ATT1_NAME = new ArrayList<String>() {{add("POSスキャナー"); add("Bluetoothスキャナー"); add("ScanTab");}};
ArrayList<String> ATT2_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT2_NAME = new ArrayList<String>() {{add("開設/精算を行わない"); add("開設/精算を行う");}};
ArrayList<String> ATT3_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT3_NAME = new ArrayList<String>() {{add("前捌きのみ"); add("決済端末");}};
ArrayList<String> ATT4_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT4_NAME = new ArrayList<String>() {{add("ＣＩＤなし"); add("ＣＩＤあり");}};
ArrayList<String> ATT5_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT5_NAME = new ArrayList<String>() {{add("Line Displayなし"); add("Line Displayあり");}};
ArrayList<String> ATT6_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT6_NAME = new ArrayList<String>() {{add("精算後シャットダウンしない"); add("精算後シャットダウンする");}};
ArrayList<String> ATT7_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT7_NAME = new ArrayList<String>() {{add("SDMC初期化しない"); add("SDMC初期化する");}};
ArrayList<String> ATT8_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT8_NAME = new ArrayList<String>() {{add("ボタン式"); add("スワイプ式");}};
ArrayList<String> ATT9_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT9_NAME = new ArrayList<String>() {{add("図書カードリーダーを接続しない"); add("図書カードリーダーを接続する");}};
ArrayList<String> ATT10_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT10_NAME = new ArrayList<String>() {{add("ハードウェアキーボードが付かない"); add("ハードウェアキーボードが付く");}};
ArrayList<String> ATT11_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT11_NAME = new ArrayList<String>() {{add("RFIDスキャナーを接続しない"); add("RFIDスキャナーを接続する");}};
ArrayList<String> ATT12_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT12_NAME = new ArrayList<String>() {{add("Selfモード無効"); add("Selfモード有効");}};
ArrayList<String> ATT13_VAL = new ArrayList<String>() {{add("0"); add("1");}};
ArrayList<String> ATT13_NAME = new ArrayList<String>() {{add("トーンインジケータ無効"); add("トーンインジケータ有効");}};
%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");

	// ATTAttributeId
	String ATTAttributeId = request.getParameter("AttributeId");
	// ATTDescription
	String ATTDescription = request.getParameter("ATTDescription");
	// ATTPrinter
	String ATTPrinter = request.getParameter("ATTPrinter");
	// ATTTill
	String ATTTill = request.getParameter("ATTTill");
	// ATTCreditTerminal
	String ATTCreditTerminal = request.getParameter("ATTCreditTerminal");
	// ATTMSR
	String ATTMSR = request.getParameter("ATTMSR");
	// ATTCashChanger
	String ATTCashChanger = request.getParameter("ATTCashChanger");
	// ATTAttribute1
	String ATTAttribute1 = request.getParameter("ATTAttribute1");
	// ATTAttribute2
	String ATTAttribute2 = request.getParameter("ATTAttribute2");
	// ATTAttribute3
	String ATTAttribute3 = request.getParameter("ATTAttribute3");
	// ATTAttribute4
	String ATTAttribute4 = request.getParameter("ATTAttribute4");
	// ATTAttribute5
	String ATTAttribute5 = request.getParameter("ATTAttribute5");
	// ATTAttribute6
	String ATTAttribute6 = request.getParameter("ATTAttribute6");
	// ATTAttribute7
	String ATTAttribute7 = request.getParameter("ATTAttribute7");
	// ATTAttribute8
	String ATTAttribute8 = request.getParameter("ATTAttribute8");
	// ATTAttribute9
	String ATTAttribute9 = request.getParameter("ATTAttribute9");
	// ATTAttribute10
	String ATTAttribute10 = request.getParameter("ATTAttribute10");
	// ATTAttribute11
	String ATTAttribute11 = request.getParameter("ATTAttribute11");
	// ATTAttribute12
	String ATTAttribute12 = request.getParameter("ATTAttribute12");
	// ATTAttribute13
	String ATTAttribute13 = request.getParameter("ATTAttribute13");
	String errString = "";
	String infoString = "";

    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
    Connection conn = dbManager.getConnection();
    if ("POST".equals(request.getMethod())) {
        String sqlStr =
                "UPDATE RESMaster.dbo.PRM_DEVICE_ATTRIBUTE SET"
                + " Description=?,"
                + " Printer=?,"
                + " Till=?,"
                + " CreditTerminal=?,"
                + " MSR=?,"
                + " CashChanger=?,"
                + " Attribute1=?,"
                + " Attribute2=?,"
                + " Attribute3=?,"
                + " Attribute4=?,"
                + " Attribute5=?,"
                + " Attribute6=?,"
                + " Attribute7=?,"
                + " Attribute8=?"
                + ", Attribute9=?"
                + ", Attribute10=?"
                + ", Attribute11=?"
                + ", Attribute12=?"
                + ", Attribute13=?"
                + " WHERE AttributeId=?; "
                ;
        PreparedStatement psIns = conn.prepareStatement(sqlStr);

        psIns.setString(1, ATTDescription);
        psIns.setString(2, ATTPrinter);
        psIns.setString(3, ATTTill);
        psIns.setString(4, ATTCreditTerminal);
        psIns.setString(5, ATTMSR);
        psIns.setString(6, ATTCashChanger);
        psIns.setString(7, ATTAttribute1);
        psIns.setString(8, ATTAttribute2);
        psIns.setString(9, ATTAttribute3);
        psIns.setString(10, ATTAttribute4);
        psIns.setString(11, ATTAttribute5);
        psIns.setString(12, ATTAttribute6);
        psIns.setString(13, ATTAttribute7);
        psIns.setString(14, ATTAttribute8);
        psIns.setString(15, ATTAttribute9);
        psIns.setString(16, ATTAttribute10);
        psIns.setString(17, ATTAttribute11);
        psIns.setString(18, ATTAttribute12);
        psIns.setString(19, ATTAttribute13);
        psIns.setString(20, ATTAttributeId);

        try {
            int rsIns = psIns.executeUpdate();
            if(rsIns > 0){
                infoString = INFO_01_UPDATE;
            } else {
                errString = ERR_01_UPDATE;
            }
            conn.commit();
        } catch (Exception e) {
             errString = ERR_02_INTERNAL + e.getMessage();
             conn.close();
        } finally {
            psIns.close();
        }
    }

    // select
    PreparedStatement ps = conn
            .prepareStatement(
                "SELECT"
                        + "  device_attribute.AttributeId AS AttributeId"
                        + " ,device_attribute.Description AS Description"
                        + " ,device_attribute.Printer AS Printer"
                        + " ,device_attribute.Till AS Till"
                        + " ,device_attribute.CreditTerminal AS CreditTerminal"
                        + " ,device_attribute.MSR AS MSR"
                        + " ,device_attribute.CashChanger AS CashChanger"
                        + " ,device_attribute.Attribute1 AS Attribute1"
                        + " ,device_attribute.Attribute2 AS Attribute2"
                        + " ,device_attribute.Attribute3 AS Attribute3"
                        + " ,device_attribute.Attribute4 AS Attribute4"
                        + " ,device_attribute.Attribute5 AS Attribute5"
                        + " ,device_attribute.Attribute6 AS Attribute6"
                        + " ,device_attribute.Attribute7 AS Attribute7"
                        + " ,device_attribute.Attribute8 AS Attribute8"
                        + " ,device_attribute.Attribute9 AS Attribute9"
                        + " ,device_attribute.Attribute10 AS Attribute10"
                        + " ,device_attribute.Attribute11 AS Attribute11"
                        + " ,device_attribute.Attribute12 AS Attribute12"
                        + " ,device_attribute.Attribute13 AS Attribute13"
                        + " ,device_attribute.Attribute14 AS Attribute14"
                        + " ,device_attribute.Attribute15 AS Attribute15"
                        + " ,device_attribute.Attribute16 AS Attribute16"
                        + " ,device_attribute.Attribute17 AS Attribute17"
                        + " ,device_attribute.Attribute18 AS Attribute18"
                        + " ,device_attribute.Attribute19 AS Attribute19"
                        + " ,device_attribute.Attribute20 AS Attribute20"
                        + " ,device_attribute.Attribute21 AS Attribute21"
                        + " ,device_attribute.Attribute22 AS Attribute22"
                        + " ,device_attribute.Attribute23 AS Attribute23"
                        + " ,device_attribute.Attribute24 AS Attribute24"
                        + " ,device_attribute.Attribute25 AS Attribute25"
                        + " ,device_attribute.Attribute26 AS Attribute26"
                        + " ,device_attribute.Attribute27 AS Attribute27"
                        + " ,device_attribute.Attribute28 AS Attribute28"
                        + " ,device_attribute.Attribute29 AS Attribute29"
                        + " ,device_attribute.Attribute30 AS Attribute30"

                        + " FROM RESMaster.dbo.PRM_DEVICE_ATTRIBUTE device_attribute"
                        );
    ResultSet rs = ps.executeQuery();
    java.util.List<String> SelectData = new java.util.ArrayList<String>();
    while (rs.next()) {
            String attributeId = rs.getString("AttributeId");
            String description = rs.getString("Description");
            String printer = rs.getString("Printer");
            String till = rs.getString("Till");
            String creditTerminal = rs.getString("CreditTerminal");
            String MSR = rs.getString("MSR");
            String cashChanger = rs.getString("CashChanger");
            String Attribute1 = rs.getString("Attribute1");
            String Attribute2 = rs.getString("Attribute2");
            String Attribute3 = rs.getString("Attribute3");
            String Attribute4 = rs.getString("Attribute4");
            String Attribute5 = rs.getString("Attribute5");
            String Attribute6 = rs.getString("Attribute6");
            String Attribute7 = rs.getString("Attribute7");
            String Attribute8 = rs.getString("Attribute8");
            String Attribute9 = rs.getString("Attribute9");
            String Attribute10 = rs.getString("Attribute10");
            String Attribute11 = rs.getString("Attribute11");
            String Attribute12 = rs.getString("Attribute12");
            String Attribute13 = rs.getString("Attribute13");
            String Attribute14 = rs.getString("Attribute14");
            String Attribute15 = rs.getString("Attribute15");
            String Attribute16 = rs.getString("Attribute16");
            String Attribute17 = rs.getString("Attribute17");
            String Attribute18 = rs.getString("Attribute18");
            String Attribute19 = rs.getString("Attribute19");
            String Attribute20 = rs.getString("Attribute20");
            String Attribute21 = rs.getString("Attribute21");
            String Attribute22 = rs.getString("Attribute22");
            String Attribute23 = rs.getString("Attribute23");
            String Attribute24 = rs.getString("Attribute24");
            String Attribute25 = rs.getString("Attribute25");
            String Attribute26 = rs.getString("Attribute26");
            String Attribute27 = rs.getString("Attribute27");
            String Attribute28 = rs.getString("Attribute28");
            String Attribute29 = rs.getString("Attribute29");
            String Attribute30 = rs.getString("Attribute30");

            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("{\"AttributeId\": \"").append(attributeId).append("\", ")
            .append("\"Description\": \"").append(description).append("\", ")
            .append("\"Printer\": \"").append(printer).append("\", ")
            .append("\"Till\": \"").append(till).append("\", ")
            .append("\"CreditTerminal\": \"").append(creditTerminal).append("\", ")
            .append("\"MSR\": \"").append(MSR).append("\", ")
            .append("\"CashChanger\": \"").append(cashChanger).append("\", ")
            .append("\"Attribute1\": \"").append(Attribute1).append("\", ")
            .append("\"Attribute2\": \"").append(Attribute2).append("\", ")
            .append("\"Attribute3\": \"").append(Attribute3).append("\", ")
            .append("\"Attribute4\": \"").append(Attribute4).append("\", ")
            .append("\"Attribute5\": \"").append(Attribute5).append("\", ")
            .append("\"Attribute6\": \"").append(Attribute6).append("\", ")
            .append("\"Attribute7\": \"").append(Attribute7).append("\", ")
            .append("\"Attribute8\": \"").append(Attribute8).append("\", ")
            .append("\"Attribute9\": \"").append(Attribute9).append("\", ")
            .append("\"Attribute10\": \"").append(Attribute10).append("\", ")
            .append("\"Attribute11\": \"").append(Attribute11).append("\", ")
            .append("\"Attribute12\": \"").append(Attribute12).append("\", ")
            .append("\"Attribute13\": \"").append(Attribute13).append("\", ")
            .append("\"Attribute14\": \"").append(Attribute14).append("\", ")
            .append("\"Attribute15\": \"").append(Attribute15).append("\", ")
            .append("\"Attribute16\": \"").append(Attribute16).append("\", ")
            .append("\"Attribute17\": \"").append(Attribute17).append("\", ")
            .append("\"Attribute18\": \"").append(Attribute18).append("\", ")
            .append("\"Attribute19\": \"").append(Attribute19).append("\", ")
            .append("\"Attribute20\": \"").append(Attribute20).append("\", ")
            .append("\"Attribute21\": \"").append(Attribute21).append("\", ")
            .append("\"Attribute22\": \"").append(Attribute22).append("\", ")
            .append("\"Attribute23\": \"").append(Attribute23).append("\", ")
            .append("\"Attribute24\": \"").append(Attribute24).append("\", ")
            .append("\"Attribute25\": \"").append(Attribute25).append("\", ")
            .append("\"Attribute26\": \"").append(Attribute26).append("\", ")
            .append("\"Attribute27\": \"").append(Attribute27).append("\", ")
            .append("\"Attribute28\": \"").append(Attribute28).append("\", ")
            .append("\"Attribute29\": \"").append(Attribute29).append("\", ")
            .append("\"Attribute30\": \"").append(Attribute30).append("\"}");
            SelectData.add(sb.toString());
    }
    ps.close();
    conn.close();
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="./css/default.css">
<!-- jQuery -->
<script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
<!-- jQuery UI -->
<script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
<!-- ダイアログ共通 -->
<script type="text/javascript" src="./js/DialogMessage.js"></script>
<!-- 属性チェック -->
<script type="text/javascript" src="./js/DeviceAttributeChk.js"></script>
</head>
<body class="res-maincontent">
<div class="page-wrapper">
属性照会
<br><br>
※属性を照会・更新することができます。<br><br>
<label class="res-err-msg"><%out.println(errString); %></label>
<label class="res-info-msg"><%out.println(infoString); %></label>
	<div class="table-scroll-area-v table-scroll-area-h" id="table" style="height: 180px;">
		<table class="res-tbl">
			<thead>
				<tr>
					<th></th>
					<th>属性番号(AttributeId)</th>
					<th>属性説明(Description)</th>
					<th>プリンター(Printer)</th>
					<th>ドロワー(Till)</th>
					<th>クレジット(CreditTerminal)</th>
					<th>MSR(MSR)</th>
					<th>釣銭機(CashChanger)</th>
					<th>属性１(Attribute1)</th>
					<th>属性２(Attribute2)</th>
					<th>属性３(Attribute3)</th>
					<th>属性４(Attribute4)</th>
					<th>属性５(Attribute5)</th>
					<th>属性６(Attribute6)</th>
					<th>属性７(Attribute7)</th>
					<th>属性８(Attribute8)</th>
					<th>属性９(Attribute9)</th>
					<th>属性１０(Attribute10)</th>
					<th>属性１１(Attribute11)</th>
					<th>属性１２(Attribute12)</th>
					<th>属性１３(Attribute13)</th>
					<th>属性１４(Attribute14)</th>
					<th>属性１５(Attribute15)</th>
					<th>属性１６(Attribute16)</th>
					<th>属性１７(Attribute17)</th>
					<th>属性１８(Attribute18)</th>
					<th>属性１９(Attribute19)</th>
					<th>属性２０(Attribute20)</th>
					<th>属性２１(Attribute21)</th>
					<th>属性２２(Attribute22)</th>
					<th>属性２３(Attribute23)</th>
					<th>属性２４(Attribute24)</th>
					<th>属性２５(Attribute25)</th>
					<th>属性２６(Attribute26)</th>
					<th>属性２７(Attribute27)</th>
					<th>属性２８(Attribute28)</th>
					<th>属性２９(Attribute29)</th>
					<th>属性３０(Attribute30)</th>
				</tr>
			</thead>
			<tbody id="logs">
			</tbody>
		</table>
	</div>
</div>
</body>
<form action="DeviceAttributeUpdate.jsp" method="post" id="updateform" onsubmit="return false;">
  <input type="hidden" name="AttributeId" id="AttributeId">
<div id="updateArea" style="display:none">
	<div class="panel">
		<table border="0" cellspacing="4" cellpadding="4" id="tablearea" style="display:none; width:100%;">
			<tr>
				<td align="right">属性番号(AttributeId) ： </td>
				<td align="left"><input type="text" id="ATTAttributeId" name="ATTAttributeId" disabled="disabled" value=""/></td>
			</tr>
			<tr>
				<td align="right">属性説明(Description) ： </td>
				<td align="left"><input type="text" id="ATTDescription" name="ATTDescription" size=50 maxlength="25" required pattern=".{0,25}"></td>
			</tr>
            <tr>
                <td colspan="2" align="right">(25文字以内で入力してください)</td>
            </tr>
			<tr>
				<td align="right">プリンター(Printer) ： </td>
				<td align="left">
					<select name="ATTPrinter" id="ATTPrinter" required>
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
				<td align="right">ドロワー(Till) ： </td>
				<td align="left">
					<select name="ATTTill" id="ATTTill" required>
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
				<td align="right">クレジット(CreditTerminal) ： </td>
				<td align="left">
					<select name="ATTCreditTerminal" id="ATTCreditTerminal" required>
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
				<td align="right">MSR(MSR) ： </td>
				<td align="left">
					<select name="ATTMSR" id="ATTMSR" required>
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
				<td align="right">釣銭機(CashChanger) ： </td>
				<td align="left">
					<select name="ATTCashChanger" id="ATTCashChanger" required>
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
				<td align="right">属性１(Attribute1) ： </td>
				<td align="left">
					<select name="ATTAttribute1" id="ATTAttribute1" required>
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
				<td align="right">属性２(Attribute2) ： </td>
				<td align="left">
					<select name="ATTAttribute2" id="ATTAttribute2" required>
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
				<td align="right">属性３(Attribute3) ： </td>
				<td align="left">
					<select name="ATTAttribute3" id="ATTAttribute3" required>
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
				<td align="right">属性４(Attribute4) ： </td>
				<td align="left">
					<select name="ATTAttribute4" id="ATTAttribute4" required>
					<%
						for (int i=0;i<ATT4_VAL.size();i++) {
							out.print("<option value=\"" + ATT4_VAL.get(i) + "\"");
							out.println(">" + ATT4_VAL.get(i) +" : " + ATT4_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性５(Attribute5) ： </td>
				<td align="left">
					<select name="ATTAttribute5" id="ATTAttribute5" required>
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
				<td align="right">属性６(Attribute6) ： </td>
				<td align="left">
					<select name="ATTAttribute6" id="ATTAttribute6" required>
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
				<td align="right">属性７(Attribute7) ： </td>
				<td align="left">
					<select name="ATTAttribute7" id="ATTAttribute7" required>
					<%
						for (int i=0;i<ATT7_VAL.size();i++) {
							out.print("<option value=\"" + ATT7_VAL.get(i) + "\"");
							out.println(">" + ATT7_VAL.get(i) +" : " + ATT7_NAME.get(i) +"</option>");
						}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">属性８(Attribute8) ：</td>
				<td align="left"><select name="ATTAttribute8"
					id="ATTAttribute8" required>
						<%
						    for (int i = 0; i < ATT8_VAL.size(); i++) {
						        out.print("<option value=\"" + ATT8_VAL.get(i) + "\"");
						        out.println(">" + ATT8_VAL.get(i) + " : " + ATT8_NAME.get(i) + "</option>");
						    }
						%>
				</select></td>
			</tr>
			<tr>
				<td align="right">属性９(Attribute9) ：</td>
				<td align="left"><select name="ATTAttribute9"
					id="ATTAttribute9" required>
						<%
						    for (int i = 0; i < ATT9_VAL.size(); i++) {
						        out.print("<option value=\"" + ATT9_VAL.get(i) + "\"");
						        out.println(">" + ATT9_VAL.get(i) + " : " + ATT9_NAME.get(i) + "</option>");
						    }
						%>
				</select></td>
			</tr>
			<tr>
				<td align="right">属性１０(Attribute10) ：</td>
				<td align="left"><select name="ATTAttribute10"
					id="ATTAttribute10" required>
						<%
						    for (int i = 0; i < ATT10_VAL.size(); i++) {
						        out.print("<option value=\"" + ATT10_VAL.get(i) + "\"");
						        out.println(">" + ATT10_VAL.get(i) + " : " + ATT10_NAME.get(i) + "</option>");
						    }
						%>
				</select></td>
			</tr>
			 <tr>
				<td align="right">属性１１(Attribute11) ：</td>
				<td align="left"><select name="ATTAttribute11"
					id="ATTAttribute11" required>
						<%
							for (int i = 0; i < ATT11_VAL.size(); i++) {
								out.print("<option value=\"" + ATT11_VAL.get(i) + "\"");
								out.println(">" + ATT11_VAL.get(i) + " : " + ATT11_NAME.get(i) + "</option>");
							}
						%>
				</select></td>
			</tr>
			<tr>
                <td align="right">属性１２(Attribute12) ：</td>
                <td align="left"><select name="ATTAttribute12"
                    id="ATTAttribute12" required>
                        <%
                            for (int i = 0; i < ATT12_VAL.size(); i++) {
                                out.print("<option value=\"" + ATT12_VAL.get(i) + "\"");
                                out.println(">" + ATT12_VAL.get(i) + " : " + ATT12_NAME.get(i) + "</option>");
                            }
                        %>
                </select></td>
			</tr>
			<tr>
                <td align="right">属性１３(Attribute13) ：</td>
                <td align="left"><select name="ATTAttribute13"
                    id="ATTAttribute13" required>
                        <%
                            for (int i = 0; i < ATT13_VAL.size(); i++) {
                                out.print("<option value=\"" + ATT13_VAL.get(i) + "\"");
                                out.println(">" + ATT13_VAL.get(i) + " : " + ATT13_NAME.get(i) + "</option>");
                            }
                        %>
                </select></td>
            </tr>
<!--
			<tr>
				<td align="right">属性８(Attribute8) ： </td>
				<td align="left"><input type="text" id="ATTAttribute8" name="ATTAttribute8" value=""/></td>
			</tr>
			<tr>
				<td>属性９(Attribute9)</td>
				<td><input type="text" id="ATTAttribute9" name="ATTAttribute9" value=""/></td>
			</tr>
			<tr>
				<td>属性１０(Attribute10)</td>
				<td><input type="text" id="ATTAttribute10" name="ATTAttribute10" value=""/></td>
			</tr>
-->
		</table>
	</div>
	<div align="right">
      <input type="button" value="更新" id="start" name="start" class="res-big-green">
	</div>

</div>
<button id="fakeButton" style="display:none"></button>
</form>

<script type="text/javascript">
	function Check(InValue) {
		// ATTAttributeId
		var StrId = 'attattributeid' + InValue;
        document.getElementById('AttributeId').value = document.getElementById(StrId).value || false;
		document.getElementById('ATTAttributeId').value = document.getElementById(StrId).value || false;
		// ATTDescription
		StrId = 'description' + InValue;
		document.getElementById('ATTDescription').value = document.getElementById(StrId).value || false;
		// ATTPrinter
		StrId = 'printer' + InValue;
		document.getElementById('ATTPrinter').value = document.getElementById(StrId).value || false;
		// ATTTill
		StrId = 'till' + InValue;
		document.getElementById('ATTTill').value = document.getElementById(StrId).value || false;
		// ATTCreditTerminal
		StrId = 'creditterminal' + InValue;
		document.getElementById('ATTCreditTerminal').value = document.getElementById(StrId).value || false;
		// ATTMSR
		StrId = 'msr' + InValue;
		document.getElementById('ATTMSR').value = document.getElementById(StrId).value || false;
		// ATTCashChanger
		StrId = 'cashchanger' + InValue;
		document.getElementById('ATTCashChanger').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute1
		StrId = 'attribute1' + InValue;
		document.getElementById('ATTAttribute1').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute2
		StrId = 'attribute2' + InValue;
		document.getElementById('ATTAttribute2').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute3
		StrId = 'attribute3' + InValue;
		document.getElementById('ATTAttribute3').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute4
		StrId = 'attribute4' + InValue;
		document.getElementById('ATTAttribute4').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute5
		StrId = 'attribute5' + InValue;
		document.getElementById('ATTAttribute5').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute6
		StrId = 'attribute6' + InValue;
		document.getElementById('ATTAttribute6').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute7
		StrId = 'attribute7' + InValue;
		document.getElementById('ATTAttribute7').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute8
		StrId = 'attribute8' + InValue;
		document.getElementById('ATTAttribute8').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute9
		StrId = 'attribute9' + InValue;
		document.getElementById('ATTAttribute9').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute10
		StrId = 'attribute10' + InValue;
		document.getElementById('ATTAttribute10').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute11
		StrId = 'attribute11' + InValue;
		document.getElementById('ATTAttribute11').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute12
		StrId = 'attribute12' + InValue;
		document.getElementById('ATTAttribute12').value = document.getElementById(StrId).value || false;
		// ATTATTAttribute13
		StrId = 'attribute13' + InValue;
		document.getElementById('ATTAttribute13').value = document.getElementById(StrId).value || false;
		document.getElementById('updateArea').style.display = "block";
	}

jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#start').click(function(e){
        var myform = document.getElementById('updateform');
        if (myform.checkValidity() == false) {
        	document.getElementById('fakeButton').click();
            return;
        }

        var valueList=[];
        valueList.push(document.getElementById('ATTPrinter').value);
        valueList.push(document.getElementById('ATTTill').value);
        valueList.push(document.getElementById('ATTCreditTerminal').value);
        valueList.push(document.getElementById('ATTMSR').value);
        valueList.push(document.getElementById('ATTCashChanger').value);
        valueList.push(document.getElementById('ATTAttribute1').value);
        valueList.push(document.getElementById('ATTAttribute2').value);
        valueList.push(document.getElementById('ATTAttribute3').value);
        valueList.push(document.getElementById('ATTAttribute4').value);
        valueList.push(document.getElementById('ATTAttribute5').value);
        valueList.push(document.getElementById('ATTAttribute6').value);
        valueList.push(document.getElementById('ATTAttribute7').value);
        valueList.push(document.getElementById('ATTAttribute8').value);
        valueList.push(document.getElementById('ATTAttribute9').value);
        valueList.push(document.getElementById('ATTAttribute10').value);
        valueList.push(document.getElementById('ATTAttribute11').value);
        valueList.push(document.getElementById('ATTAttribute12').value);
        valueList.push(document.getElementById('ATTAttribute13').value);
        var checkResult = checkAttributeRelation(valueList);
        if(checkResult != '') {
            showDialog(
                    "タイトル：未使用",
                    checkResult,
                    ButtonOK,
                    function() {
                        //「はい」を押したときの処理
                    }
                );
            return;
        }
        if(document.getElementById('ATTAttribute1').value !='1' && document.getElementById('ATTAttribute10').value == '1') {
            showDialog(
                    "タイトル：未使用",
                    <%='\'' + ERR_03_ATTRIBUTE + '\''%>,
                    ButtonOK,
                    function() {
                        //「はい」を押したときの処理
                    }
                );
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
                myform.submit();
            }
        );
    });
});

(function() {
		var selectText = <%="'"+SelectData+"'"%>;
		var currentLog =eval("("+selectText+")");
		var showLog = function() {
			if (currentLog === null)
				return;

			var log = '';
			for (var i = 0; i < currentLog.length; i++) {
				log += '<tr><td align="center"><input type="radio" name="q1" id="' + i
						+ '" onclick="Check(id)"></td>';
				log += '<td><input type="text" id="attattributeid' + i
						+ '" name="attattributeid' + i + '" disabled value="'
						+ (currentLog[i].AttributeId || '&nbsp;') + '" ></td>';
				log += '<td><input type="text" id="description' + i
						+ '" name="description' + i + '" disabled value="'
						+ (currentLog[i].Description || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="printer' + i
						+ '" name="printer' + i + '" disabled value="'
						+ (currentLog[i].Printer || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="till' + i + '" name="till'
						+ i + '" disabled value="'
						+ (currentLog[i].Till || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="creditterminal' + i
						+ '" name="creditterminal' + i + '" disabled value="'
						+ (currentLog[i].CreditTerminal || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="msr' + i + '" name="msr' + i
						+ '" disabled value="'
						+ (currentLog[i].MSR || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="cashchanger' + i
						+ '" name="cashchanger' + i + '" disabled value="'
						+ (currentLog[i].CashChanger || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute1' + i
						+ '" name="attribute1' + i + '" disabled value="'
						+ (currentLog[i].Attribute1 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute2' + i
						+ '" name="attribute2' + i + '" disabled value="'
						+ (currentLog[i].Attribute2 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute3' + i
						+ '" name="attribute3' + i + '" disabled value="'
						+ (currentLog[i].Attribute3 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute4' + i
						+ '" name="attribute4' + i + '" disabled value="'
						+ (currentLog[i].Attribute4 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute5' + i
						+ '" name="attribute5' + i + '" disabled value="'
						+ (currentLog[i].Attribute5 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute6' + i
						+ '" name="attribute6' + i + '" disabled value="'
						+ (currentLog[i].Attribute6 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute7' + i
						+ '" name="attribute7' + i + '" disabled value="'
						+ (currentLog[i].Attribute7 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute8' + i
						+ '" name="attribute8' + i + '" disabled value="'
						+ (currentLog[i].Attribute8 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute9' + i
						+ '" name="attribute9' + i + '" disabled value="'
						+ (currentLog[i].Attribute9 || '&nbsp;' ) + '"></td>';
				log += '<td><input type="text" id="attribute10' + i
						+ '" name="attribute10' + i + '" disabled value="'
						+ (currentLog[i].Attribute10 || '&nbsp;' ) + '"></td>';
				log += '<td><input type="text" id="attribute11' + i
						+ '" name="attribute11' + i + '" disabled value="'
						+ (currentLog[i].Attribute11 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute12' + i
						+ '" name="attribute12' + i + '" disabled value="'
						+ (currentLog[i].Attribute12 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute13' + i
						+ '" name="attribute13' + i + '" disabled value="'
						+ (currentLog[i].Attribute13 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute14' + i
						+ '" name="attribute14' + i + '" disabled value="'
						+ (currentLog[i].Attribute14 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute15' + i
						+ '" name="attribute15' + i + '" disabled value="'
						+ (currentLog[i].Attribute15 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute16' + i
						+ '" name="attribute16' + i + '" disabled value="'
						+ (currentLog[i].Attribute16 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute17' + i
						+ '" name="attribute17' + i + '" disabled value="'
						+ (currentLog[i].Attribute17 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute18' + i
						+ '" name="attribute18' + i + '" disabled value="'
						+ (currentLog[i].Attribute18 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="attribute19' + i
						+ '" name="attribute19' + i + '" disabled value="'
						+ (currentLog[i].Attribute19 || '&nbsp;' ) + '"></td>';
				log += '<td><input type="text" id="attribute20' + i
						+ '" name="attribute20' + i + '" disabled value="'
						+ (currentLog[i].Attribute20 || '&nbsp;' ) + '"></td>';
				log += '<td><input type="text" id="Attribute21' + i
						+ '" name="Attribute21' + i + '" disabled value="'
						+ (currentLog[i].Attribute21 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute22' + i
						+ '" name="Attribute22' + i + '" disabled value="'
						+ (currentLog[i].Attribute22 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute23' + i
						+ '" name="Attribute23' + i + '" disabled value="'
						+ (currentLog[i].Attribute23 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute24' + i
						+ '" name="Attribute24' + i + '" disabled value="'
						+ (currentLog[i].Attribute24 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute25' + i
						+ '" name="Attribute25' + i + '" disabled value="'
						+ (currentLog[i].Attribute25 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute26' + i
						+ '" name="Attribute26' + i + '" disabled value="'
						+ (currentLog[i].Attribute26 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute27' + i
						+ '" name="Attribute27' + i + '" disabled value="'
						+ (currentLog[i].Attribute27 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute28' + i
						+ '" name="Attribute28' + i + '" disabled value="'
						+ (currentLog[i].Attribute28 || '&nbsp;') + '"></td>';
				log += '<td><input type="text" id="Attribute29' + i
						+ '" name="Attribute29' + i + '" disabled value="'
						+ (currentLog[i].Attribute29 || '&nbsp;' ) + '"></td>';
				log += '<td><input type="text" id="Attribute30' + i
						+ '" name="Attribute30' + i + '" disabled value="'
						+ (currentLog[i].Attribute30 || '&nbsp;' ) + '"></td>';


			}
			document.getElementById('logs').innerHTML = log;

			if (log == '') {
				document.getElementById('tablearea').style.display = "none";
			} else {
				document.getElementById('tablearea').style.display = "block";
			}
		};

        showLog();
	})();
</script>
<HEAD>
<meta http-equiv=”Pragma” content=”no-cache”>
<meta http-equiv=”Cache-Control” content=”no-cache”>
</HEAD>
</html>
