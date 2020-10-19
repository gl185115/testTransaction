<%@page import="com.lowagie.text.Document"%>
<%@ page language="java" pageEncoding="UTF-8" 
    import="java.sql.*"
%>
<%!
	final String ERR_01_INPBLANK = "ログインID・パスワードは必須入力です。";
	final String ERR_02_NOMATCHDATA = "ログインIDまたはパスワードが違います。";
%>
<%
	request.setCharacterEncoding("UTF-8");
	String status = request.getParameter("status");
	String user = request.getParameter("userid");
	String errorMessage = "";
	if(status!=null){
		if(status.equals("false")){
			errorMessage = ERR_02_NOMATCHDATA;
		}else if(status.equals("blank")){
			errorMessage = ERR_01_INPBLANK;
		}
		else{
			errorMessage = "some error";
		}
	}
	
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="./css/style.css">
    <link rel="stylesheet" type="text/css" href="./css/login.css">
    <link rel="stylesheet" type="text/css" href="./css/wo_login.css">
<title>Login</title>
<!-- jQuery -->
<script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
<!-- jQuery UI -->
<script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	if($("#status").val()!="true" && $("#errString").val()!=""){
		alert("<%= errorMessage %>");
		return false;
	}
});
</script>
</head>
<body>
  	<h1 class="top">緊急売変</h1>
	<div class="loginbox">
		<form method="POST" action="login_action.jsp">
			<table style="width: 100%; height: 100%;" border="0">
				<tbody>
					<tr id="header">
						<td style="padding: 0px 50px 0px 5px; height: 65px; text-align: right; vertical-align: bottom;"	colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td class="login_cell"><span class="login_label" id="lbl-usr">ユーザID</span></td>
						<td>
							<div class="login_text" style="padding: 10px 0px 0px 10px;">
								<input id="frm-usr" name="userid" tabindex="1" maxlength="20" style="width: 243px; font-size: 11pt; font-weight: bold; background-color: transparent; border-style: none;"> 
							</div>
						</td>
					</tr>
					<tr>
						<td class="login_cell">
						<span class="login_label" id="lblPassword">パスワード</span> 
						<td>
							<div class="login_text" style="padding: 10px 0px 0px 10px;">
								<input type="password" id="frm-pass" name="password" tabindex="2" maxlength="100" style="width: 243px; font-size: 11pt; font-weight: bold; background-color: transparent; border-style: none;"> 
							</div>
						</td>
					</tr>
					<tr id="action">
						<td class="auto-style1"></td>
						<td class="auto-style1" style="text-align: center; padding-right: 80px; vertical-align: top;">
							<div  id="button" style="width: 100%; height: 100%;">
							<input type="submit" class="login_button_active" id="btn-login" value="ログイン" tabindex="3" >
							</div>
						</td>
					</tr>
					<tr id="footer">
						<td style="height: 100px; vertical-align: middle;" colspan="2">
							<table class="login_footer_display">
								<tbody>
									<tr>
										<td style="padding-left: 2px;">
											<p class="login_footer_p"></p>
										</td>
										<td
											style="width: 24px; text-align: center; padding-left: 5px;"></td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<td></td>
						<td></td>
					</tr>
				</tbody>
			</table>
			<div class="messagearea">
				<input type="hidden" id="errString" value=<%= errorMessage %>>
				<input type="hidden" id="status" value=<%= status %>>
			</div>
		</form>
	</div>
</body>
</html>