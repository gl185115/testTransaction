<%@ page language="java" pageEncoding="utf-8"%><%@page import="java.sql.*"%><%@page import="java.text.*"%><%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%><%
    StringBuilder sb = new StringBuilder();
    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer)JndiDBManagerMSSqlServer.getInstance();
    try (Connection conn = dbManager.getConnection()) {
        try (PreparedStatement ps = conn.prepareStatement("select TodayDate from resMaster.dbo.MST_BizDay "
                                                        + "where companyId=? and storeId=?")) {
            ps.setString(1, "0");
            ps.setString(2, "0");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    sb.append(fmt.format(rs.getDate(1)));
                }
            }
        }
    }
    String sdate = sb.toString();
    response.setHeader("Cache-Control", "no-store, no-cache, max-age=0");
    if (request.getHeader("Accept").toLowerCase().indexOf("application/json") >= 0) {
        response.setHeader("Content-Type", "application/json");
        if (sdate.length() == 0) {
%>
{}
<%
        } else {
%>
{ "year": <%= sdate.substring(0, 4) %>, "month": <%= Integer.parseInt(sdate.substring(5, 7)) %>, "day": <%= Integer.parseInt(sdate.substring(8, 10)) %> }
<%
        }
    } else {
%>
<!DOCTYPE html>
<html >
<head>
  <title>NCR RES BizDate Viewer</title>
</head>
<body>
<div>
 <span>営業日</span>
 <span><%= sdate %></span>
</div>
</body>
</html>
<%
    }
%>
