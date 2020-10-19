<%@page import="com.lowagie.text.Document"%>
<%@ page language="java" pageEncoding="UTF-8" 
    import="java.sql.*"
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
%>
<%
	//ResMaster担当者マスタとの接続SQL実行結果を取得しresultに結果を返す
    Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String sql = "";
	String result=ChkEmpInfo(request.getParameter("userid"), request.getParameter("password"));
	String[] recordSet = result.split(",",0);
	if(recordSet[0].equals("true")){
		session.setAttribute("usrId",request.getParameter("userid"));
		session.setAttribute("companyId",recordSet[1]);
		session.setAttribute("storeId",recordSet[2]);
// 		session.setMaxInactiveInterval(10);
		response.sendRedirect("PluUrgentUpdate.jsp");
	}
	 
	if(recordSet[0].equals("false")){
		response.sendRedirect("index.jsp?status=false");
	}
	
	if(recordSet[0].equals("blank")){
		response.sendRedirect("index.jsp?status=blank");
	}
	 
	if(recordSet[0].equals("error")){
		response.sendRedirect("index.jsp?status=error");
	}
%>
<%!
/* 
[担当者マスタ照会]
param:user,password(NULL許可)
target:MST_EMPINFO
result:レコード１件ならtrue,0件なら false,例外はerror
*/
 public static String ChkEmpInfo(String p_user, String p_pass){	
    String result="false";
    Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String sql = "";
	
	if (!p_user.isEmpty() && !p_pass.isEmpty()) {
	    //DB処理開始
	    try{
	        JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
	        conn = dbManager.getConnection();
	        conn.setAutoCommit(false);
	        //
	        if (!p_pass.isEmpty()) {
	            //担当者マスタを取得
	            sql = "SELECT EmpCode, CompanyId, StoreId " +
	                "FROM RESMaster.dbo.MST_EMPINFO " +
	                "WHERE EmpCode = ? and Password = ? ";
	            
	            ps = conn.prepareStatement(sql);
	            ps.setString(1, p_user);
	            ps.setString(2, p_pass);
	        }
// 	        else{
// 	        	 //担当者マスタを取得
// 	            sql = "SELECT EmpCode, CompanyId, StoreId " +
// 	                "FROM RESMaster.dbo.MST_EMPINFO " +
// 	                "WHERE EmpCode = ? and Password IS NULL ";
	            
// 	            ps = conn.prepareStatement(sql);
// 	            ps.setString(1, p_user);
// 	        }
			//execute query
            rs = ps.executeQuery();
            
           //if has match record only : result = true
           	if(rs.next()){
           		String CompanyId = rs.getString("CompanyId");
                String StoreId = rs.getString("StoreId");
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append("true").append(",").append(CompanyId).append(",").append(StoreId);
           		result = sb.toString();
           	}
          
            rs.close();
            ps.close();
            
	    } catch (Exception e) {
	    	//write ex : result =  error
	    	System.out.print(e);
	    	result = "error,,";
	    } 
	}else{
		result = "blank,,";
	}
	return result;
} 
%>