<%@page language="java" contentType="text/html; CharSet=UTF-8" pageEncoding="utf-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%!
public static boolean isEmpty(String str) {
    return (str == null) || (str.length() == 0);
}
public static String trimToEmpty(String str) {
    return str == null ? "" : str.trim();
}
%>
<%
String cmId = request.getParameter("cmId");
String companyId = request.getParameter("companyId");
String storeId = request.getParameter("storeId") == null ? "0000" : request.getParameter("storeId");
String bizCatId = request.getParameter("bizCatId") == null ? "00" : request.getParameter("bizCatId");
String terminalId = request.getParameter("terminalId") == null ? "0000" : request.getParameter("terminalId");
String selectCompanyId = companyId;
String selectStoreId = storeId;
String selectBizCatId = bizCatId;
String selectTerminalId = terminalId;
String cmName = "";
String[] messages = new String[20];
for (int i=0; i<20; i++) {
    messages[i] = "";
}
String startDate = "";
String endDate = "";
java.sql.Date sqlStartDate = null;
java.sql.Date sqlEndDate = null;

String resSearch = "0"; //0:HITなし,1:HITあり,2:DBエラー
String err = ""; //エラー詳細

Connection conn = null;
PreparedStatement ps = null;
ResultSet rs = null;
String sql = "";

if (companyId != null) {
    //DB処理開始
    try{
        JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
        conn = dbManager.getConnection();
        conn.setAutoCommit(false);

        if (!isEmpty(cmId)) {
            //広告文を取得
            sql = "SELECT CompanyId, CMId, CMName, StoreId, BizCatId, TerminalId" +
                ", Top1Message, Top2Message, Top3Message, Top4Message, Top5Message" +
                ", Top6Message, Top7Message, Top8Message, Top9Message, Top10Message" +
                ", Bottom1Message, Bottom2Message, Bottom3Message, Bottom4Message, Bottom5Message" +
                ", Bottom6Message, Bottom7Message, Bottom8Message, Bottom9Message, Bottom10Message" +
                ", StartDate, EndDate " +
                "FROM RESMaster.dbo.MST_PRESET_CMINFO " +
                "WHERE CompanyId = ? and CMId = ? and DeleteFlag = 0";
            ps = conn.prepareStatement(sql);
            ps.setString(1, companyId);
            ps.setString(2, cmId);
            rs = ps.executeQuery();

            if (rs.next()) {
                resSearch = "1";
                cmName = rs.getString("CMName");
                sqlStartDate = rs.getDate("StartDate");
                sqlEndDate = rs.getDate("EndDate");
                messages = new String[20];
                messages[0] = rs.getString("Top1Message");
                messages[1] = rs.getString("Top2Message");
                messages[2] = rs.getString("Top3Message");
                messages[3] = rs.getString("Top4Message");
                messages[4] = rs.getString("Top5Message");
                messages[5] = rs.getString("Top6Message");
                messages[6] = rs.getString("Top7Message");
                messages[7] = rs.getString("Top8Message");
                messages[8] = rs.getString("Top9Message");
                messages[9] = rs.getString("Top10Message");
                messages[10] = rs.getString("Bottom1Message");
                messages[11] = rs.getString("Bottom2Message");
                messages[12] = rs.getString("Bottom3Message");
                messages[13] = rs.getString("Bottom4Message");
                messages[14] = rs.getString("Bottom5Message");
                messages[15] = rs.getString("Bottom6Message");
                messages[16] = rs.getString("Bottom7Message");
                messages[17] = rs.getString("Bottom8Message");
                messages[18] = rs.getString("Bottom9Message");
                messages[19] = rs.getString("Bottom10Message");
                
                for(int i = 0; i < 20; i++){
                	if(messages[i] == null){
                		messages[i] = "";
                	}
                }
            }
            rs.close();
            ps.close();

            if ("1".equals(resSearch)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                startDate = sdf.format(sqlStartDate);
                endDate = sdf.format(sqlEndDate);
            }
        }
    } catch (SQLException se) {
        err = "予期せぬエラーが発生しました。処理を中断します。";
        do {
            err += "\\n" + se.getSQLState();
            err += "\\n" + se.getErrorCode();
            err += "\\n" + se.getMessage() ;
            se = se.getNextException();
        } while (se != null);
        resSearch = "2";
    } catch (Exception e) {
        err = "予期せぬエラーが発生しました。処理を中断します。";
        err += "\\n" + e.getMessage();
        resSearch = "2";
    } finally {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
}
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title></title>
</head>
<body>
<jsp:forward page="index.jsp">
  <jsp:param name="resSearch" value="<%= resSearch %>" />
  <jsp:param name="err" value="<%= err %>" />
  <jsp:param name="companyId" value="<%= companyId %>" />
  <jsp:param name="storeId" value="<%= storeId %>" />
  <jsp:param name="bizCatId" value="<%= bizCatId %>" />
  <jsp:param name="terminalId" value="<%= terminalId %>" />
  <jsp:param name="cmId" value="<%= trimToEmpty(cmId) %>" />
  <jsp:param name="selectCompanyId" value="<%= trimToEmpty(selectCompanyId) %>" />
  <jsp:param name="selectBizCatId" value="<%= trimToEmpty(selectBizCatId) %>" />
  <jsp:param name="selectStoreId" value="<%= trimToEmpty(selectStoreId) %>" />
  <jsp:param name="selectTerminalId" value="<%= trimToEmpty(selectTerminalId) %>" />
  <jsp:param name="cmName" value="<%= cmName %>" />
  <jsp:param name="message1" value="<%= messages[0] %>" />
  <jsp:param name="message2" value="<%= messages[1] %>" />
  <jsp:param name="message3" value="<%= messages[2] %>" />
  <jsp:param name="message4" value="<%= messages[3] %>" />
  <jsp:param name="message5" value="<%= messages[4] %>" />
  <jsp:param name="message6" value="<%= messages[5] %>" />
  <jsp:param name="message7" value="<%= messages[6] %>" />
  <jsp:param name="message8" value="<%= messages[7] %>" />
  <jsp:param name="message9" value="<%= messages[8] %>" />
  <jsp:param name="message10" value="<%= messages[9] %>" />
  <jsp:param name="message11" value="<%= messages[10] %>" />
  <jsp:param name="message12" value="<%= messages[11] %>" />
  <jsp:param name="message13" value="<%= messages[12] %>" />
  <jsp:param name="message14" value="<%= messages[13] %>" />
  <jsp:param name="message15" value="<%= messages[14] %>" />
  <jsp:param name="message16" value="<%= messages[15] %>" />
  <jsp:param name="message17" value="<%= messages[16] %>" />
  <jsp:param name="message18" value="<%= messages[17] %>" />
  <jsp:param name="message19" value="<%= messages[18] %>" />
  <jsp:param name="message20" value="<%= messages[19] %>" />
  <jsp:param name="startDate" value="<%= startDate %>" />
  <jsp:param name="endDate" value="<%= endDate %>" />
</jsp:forward>
</body>
</html>
