<%@page language="java" contentType="text/html; CharSet=utf-8" pageEncoding="utf-8"%>
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
<%!
//String型をsql.Date型に変換する
public static java.sql.Date toDate(String value) {
    if ( value == null || value.isEmpty() ) {
        return null;
    }
    String format = "yyyy/MM/dd";
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    dateFormat.setLenient(false);

    try {
        java.util.Date date = dateFormat.parse(value);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.sql.Date sqlStartDate = new java.sql.Date(cal.getTimeInMillis());
        return sqlStartDate;
    } catch (ParseException pe) {
        return null;
    }
}
%>
<%
String resCommit = "0"; //1:成功, 2:失敗
String err = ""; //エラー詳細
String user = ""; //ログインユーザー名
//ログインユーザー名取得
try {
    user = request.getRemoteUser() != null ? request.getRemoteUser() : "";
} catch (Exception e) {
}

String companyId = request.getParameter("companyId");
String bizCatId = request.getParameter("bizCatId");
String storeId = request.getParameter("storeId");
String terminalId = request.getParameter("terminalId");
String cmId = request.getParameter("cmId");
String previousCmId = request.getParameter("previousCmId");
String cmName = request.getParameter("cmName");
String ads1 = request.getParameter("ads1");
String ads2 = request.getParameter("ads2");
String ads3 = request.getParameter("ads3");
String ads4 = request.getParameter("ads4");
String ads5 = request.getParameter("ads5");
String ads6 = request.getParameter("ads6");
String ads7 = request.getParameter("ads7");
String ads8 = request.getParameter("ads8");
String ads9 = request.getParameter("ads9");
String ads10 = request.getParameter("ads10");
String startDate = request.getParameter("startDate");
String endDate = request.getParameter("endDate");
String deleteFlag = request.getParameter("deleteFlag");
String cmType = "4";

//開始日・終了日を取得
java.sql.Date sqlStartDate = toDate(startDate);
java.sql.Date sqlEndDate = toDate(endDate);

java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
//DB処理開始
JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
Connection conn = dbManager.getConnection();
conn.setAutoCommit(false);

PreparedStatement ps = null;
ResultSet rs = null;
String sql = "";

try{
    if (isEmpty(deleteFlag)) {
        if (sqlStartDate == null || sqlEndDate == null) {
            resCommit = "2";
            err = "正しい日付を入力してください。";
        } else {
            //期間重複する広告文がないかチェック
            if (isEmpty(cmId)) {
                //新規の場合
                sql = "SELECT CompanyId, CMId, UpdCount" +
                      " FROM RESMaster.dbo.MST_PRESET_CMINFO" +
                      " WHERE CompanyId = ? and BizCatId = ? and StoreId = ? and TerminalId = ?" +
                      " and ((? BETWEEN StartDate and EndDate) or (? BETWEEN StartDate and EndDate) or (StartDate > ? and EndDate < ?)) and DeleteFlag = 0";
                ps = conn.prepareStatement(sql);
                ps.setString(1, companyId);
                ps.setString(2, bizCatId);
                ps.setString(3, storeId);
                ps.setString(4, terminalId);
                ps.setDate(5, sqlStartDate);
                ps.setDate(6, sqlEndDate);
                ps.setDate(7, sqlStartDate);
                ps.setDate(8, sqlEndDate);
                rs = ps.executeQuery();
            } else {
                //更新の場合
                Integer nCmId = -1;
                if (!isEmpty(previousCmId)) {
                    nCmId = Integer.parseInt(previousCmId);
                }
                sql = "SELECT CompanyId, CMId, UpdCount" +
                      " FROM RESMaster.dbo.MST_PRESET_CMINFO" +
                      " WHERE CompanyId = ? and BizCatId = ? and StoreId = ? and TerminalId = ?" +
                      " and ((? BETWEEN StartDate and EndDate) or (? BETWEEN StartDate and EndDate) or (StartDate > ? and EndDate < ?)) and DeleteFlag = 0 and CMId != ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, companyId);
                ps.setString(2, bizCatId);
                ps.setString(3, storeId);
                ps.setString(4, terminalId);
                ps.setDate(5, sqlStartDate);
                ps.setDate(6, sqlEndDate);
                ps.setDate(7, sqlStartDate);
                ps.setDate(8, sqlEndDate);
                ps.setInt(9, nCmId);
                rs = ps.executeQuery();
            }
            if (rs.next()) {
                resCommit = "2";
                err = "期間が重複している広告があるため登録できません。";
                cmId = previousCmId;
            }
            rs.close();
            ps.close();
        }
    }

    if ("0".equals(resCommit) && !isEmpty(companyId)) {
        if (isEmpty(cmId)) {
            //新規
            if ("00".equals(bizCatId)) {
                cmType = "1";
            } else if ("0000".equals(storeId)) {
                cmType = "2";
            } else if ("0000".equals(terminalId)) {
                cmType = "3";
            }
            sql = "INSERT INTO RESMaster.dbo.MST_PRESET_CMINFO " +
                  "(CompanyId" +
                  ", CMName" +
                  ", CMType" +
                  ", BizCatId" +
                  ", StoreId" +
                  ", TerminalId" +
                  ", StartDate" +
                  ", EndDate" +
                  ", Top1Message" +
                  ", Top2Message" +
                  ", Top3Message" +
                  ", Top4Message" +
                  ", Top5Message" +
                  ", Bottom1Message" +
                  ", Bottom2Message" +
                  ", Bottom3Message" +
                  ", Bottom4Message" +
                  ", Bottom5Message" +
                  ", DeleteFlag" +
                  ", InsDate" +
                  ", InsAppId" +
                  ", InsOpeCode" +
                  ", UpdCount" +
                  ", UpdDate" +
                  ", UpdAppId" +
                  ", UpdOpeCode" +
                  ") " +
                  " VALUES " +
                  "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, companyId);
            ps.setString(2, cmName);
            ps.setString(3, cmType);
            ps.setString(4, bizCatId);
            ps.setString(5, storeId);
            ps.setString(6, terminalId);
            ps.setDate(7, sqlStartDate);
            ps.setDate(8, sqlEndDate);
            ps.setString(9, ads1);
            ps.setString(10, ads2);
            ps.setString(11, ads3);
            ps.setString(12, ads4);
            ps.setString(13, ads5);
            ps.setString(14, ads6);
            ps.setString(15, ads7);
            ps.setString(16, ads8);
            ps.setString(17, ads9);
            ps.setString(18, ads10);
            ps.setInt(19, 0);
            ps.setDate(20, currentDate);
            ps.setString(21, "settingAds");
            ps.setString(22, user);
            ps.setInt(23, 0);
            ps.setDate(24, currentDate);
            ps.setString(25, "settingAds");
            ps.setString(26, user);
            int num = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                Integer nCmId = rs.getInt(1);
                cmId = nCmId.toString();
            }
            conn.commit();
            rs.close();
            ps.close();

            resCommit = "1";
        } else {
            //更新
            if (isEmpty(deleteFlag)) {
                //更新
                //現在の広告文を取得
                int updCount = 0;
                sql = "SELECT CompanyId, CMId, UpdCount" +
                      " FROM RESMaster.dbo.MST_PRESET_CMINFO" +
                      " WHERE CompanyId = ? and CMId = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, companyId);
                ps.setInt(2, Integer.parseInt(cmId));
                rs = ps.executeQuery();

                if (rs.next()) {
                    updCount = rs.getInt("UpdCount");
                }
                rs.close();
                ps.close();

                sql = "UPDATE RESMaster.dbo.MST_PRESET_CMINFO SET " +
                            "CMName = ?" +
                            ", StartDate = ?" +
                            ", EndDate = ?" +
                            ", Top1Message = ?" +
                            ", Top2Message = ?" +
                            ", Top3Message = ?" +
                            ", Top4Message = ?" +
                            ", Top5Message = ?" +
                            ", Bottom1Message = ?" +
                            ", Bottom2Message = ?" +
                            ", Bottom3Message = ?" +
                            ", Bottom4Message = ?" +
                            ", Bottom5Message = ?" +
                            ", UpdCount = ?" +
                            ", UpdDate = ?" +
                            ", UpdAppId = ?" +
                            ", UpdOpeCode = ?" +
                            " WHERE CompanyID = ? and CMId = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, cmName);
                ps.setDate(2, sqlStartDate);
                ps.setDate(3, sqlEndDate);
                ps.setString(4, ads1);
                ps.setString(5, ads2);
                ps.setString(6, ads3);
                ps.setString(7, ads4);
                ps.setString(8, ads5);
                ps.setString(9, ads6);
                ps.setString(10, ads7);
                ps.setString(11, ads8);
                ps.setString(12, ads9);
                ps.setString(13, ads10);
                ps.setInt(14, updCount + 1);
                ps.setDate(15, currentDate);
                ps.setString(16, "settingAds");
                ps.setString(17, user);
                ps.setString(18, companyId);
                ps.setString(19, cmId);
            } else {
                //削除
                sql = "UPDATE RESMaster.dbo.MST_PRESET_CMINFO SET " +
                            "CMName = ?" +
                            ", StartDate = ?" +
                            ", EndDate = ?" +
                            ", Top1Message = ?" +
                            ", Top2Message = ?" +
                            ", Top3Message = ?" +
                            ", Top4Message = ?" +
                            ", Top5Message = ?" +
                            ", Bottom1Message = ?" +
                            ", Bottom2Message = ?" +
                            ", Bottom3Message = ?" +
                            ", Bottom4Message = ?" +
                            ", Bottom5Message = ?" +
                            ", DeleteFlag = ?" +
                            ", DelDate = ?" +
                            ", DelAppId = ?" +
                            ", DelOpeCode = ?" +
                            " WHERE CompanyID = ? and CMId = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, cmName);
                ps.setDate(2, sqlStartDate);
                ps.setDate(3, sqlEndDate);
                ps.setString(4, ads1);
                ps.setString(5, ads2);
                ps.setString(6, ads3);
                ps.setString(7, ads4);
                ps.setString(8, ads5);
                ps.setString(9, ads6);
                ps.setString(10, ads7);
                ps.setString(11, ads8);
                ps.setString(12, ads9);
                ps.setString(13, ads10);
                ps.setInt(14, 1);
                ps.setDate(15, currentDate);
                ps.setString(16, "settingAds");
                ps.setString(17, user);
                ps.setString(18, companyId);
                ps.setString(19, cmId);

                cmId = null;
            }
            int num = ps.executeUpdate();
            conn.commit();
            ps.close();
            resCommit = "1";
        }

        if (!isEmpty(cmId)) {
            //登録後の最新の広告文を取得
            sql = "SELECT CompanyId, CMId, CmName, StoreId, BizCatId, TerminalId" +
                    ", Top1Message, Top2Message, Top3Message, Top4Message, Top5Message" +
                    ", Bottom1Message, Bottom2Message, Bottom3Message, Bottom4Message, Bottom5Message" +
                    ", StartDate, EndDate" +
                    " FROM RESMaster.dbo.MST_PRESET_CMINFO" +
                    " WHERE CompanyId = ? and CMId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, companyId);
            ps.setInt(2, Integer.parseInt(cmId));
            rs = ps.executeQuery();

            if (rs.next()) {
                cmName = rs.getString("CmName");
                ads1 = rs.getString("Top1Message");
                ads2 = rs.getString("Top2Message");
                ads3 = rs.getString("Top3Message");
                ads4 = rs.getString("Top4Message");
                ads5 = rs.getString("Top5Message");
                ads6 = rs.getString("Bottom1Message");
                ads7 = rs.getString("Bottom2Message");
                ads8 = rs.getString("Bottom3Message");
                ads9 = rs.getString("Bottom4Message");
                ads10 = rs.getString("Bottom5Message");
            }
            rs.close();
            ps.close();
        }
    }
} catch (SQLException se) {
    err = "予期せぬエラーが発生しました。処理を中断します。";
    do {
        err += "\\n" + se.getSQLState();
        err += "\\n" + se.getErrorCode();
        err += "\\n" + se.getMessage();
        se = se.getNextException();
    } while (se != null);
    resCommit = "2";
    try {
        conn.rollback();
    } catch (SQLException se2) {
    }
} catch (Exception e) {
    err = "予期せぬエラーが発生しました。処理を中断します。";
    err += "\\n" + e.getMessage();
    resCommit = "2";
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
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title></title>
</head>
<body>
<jsp:forward page="index.jsp">
  <jsp:param name="resCommit" value="<%= resCommit %>" />
  <jsp:param name="err" value="<%= err %>" />
  <jsp:param name="companyId" value="<%= companyId %>" />
  <jsp:param name="bizCatId" value="<%= bizCatId %>" />
  <jsp:param name="storeId" value="<%= storeId %>" />
  <jsp:param name="terminalId" value="<%= terminalId %>" />
  <jsp:param name="cmId" value="<%= trimToEmpty(cmId) %>" />
  <jsp:param name="selectCompanyId" value="<%= trimToEmpty(companyId) %>" />
  <jsp:param name="selectBizCatId" value="<%= trimToEmpty(bizCatId) %>" />
  <jsp:param name="selectStoreId" value="<%= trimToEmpty(storeId) %>" />
  <jsp:param name="selectTerminalId" value="<%= trimToEmpty(terminalId) %>" />
  <jsp:param name="cmName" value="<%= trimToEmpty(cmName) %>" />
  <jsp:param name="message1" value="<%= trimToEmpty(ads1) %>" />
  <jsp:param name="message2" value="<%= trimToEmpty(ads2) %>" />
  <jsp:param name="message3" value="<%= trimToEmpty(ads3) %>" />
  <jsp:param name="message4" value="<%= trimToEmpty(ads4) %>" />
  <jsp:param name="message5" value="<%= trimToEmpty(ads5) %>" />
  <jsp:param name="message6" value="<%= trimToEmpty(ads6) %>" />
  <jsp:param name="message7" value="<%= trimToEmpty(ads7) %>" />
  <jsp:param name="message8" value="<%= trimToEmpty(ads8) %>" />
  <jsp:param name="message9" value="<%= trimToEmpty(ads9) %>" />
  <jsp:param name="message10" value="<%= trimToEmpty(ads10) %>" />
  <jsp:param name="startDate" value="<%= trimToEmpty(startDate) %>" />
  <jsp:param name="endDate" value="<%= trimToEmpty(endDate) %>" />
</jsp:forward>
</body>
</html>
