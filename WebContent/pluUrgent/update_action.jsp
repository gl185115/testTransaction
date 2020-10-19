<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
	import="java.util.ArrayList" import="java.util.UUID" import="java.sql.*"%>
<%
	//logininfo
	String p_userid = (String) session.getAttribute("usrId");
	String p_company = (String) session.getAttribute("companyId");
	String p_store = (String) session.getAttribute("storeId");
	String p_mdinternal = request.getParameter("prm_mdinternal");
	String p_dpt = request.getParameter("prm_dpt");
	String p_line = request.getParameter("prm_line");
	String p_class = request.getParameter("prm_class");
	String p_urgprice = request.getParameter("prm_salesprice");
	String p_instruct = request.getParameter("prm_instruct");
	//session alive confirm
	if(session == null || session.getAttribute("usrId") == null){
		response.sendRedirect("PluUrgentUpdate.jsp");
	}
	//ip adress取得
	String ip = request.getLocalAddr();

	//table表示用
	ArrayList<ArrayList<String>> MstPluUrgentLists = new ArrayList<ArrayList<String>>();
	JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
	Connection conn = null;
	conn = dbManager.getConnection();
	conn.setAutoCommit(false);

	if (p_instruct.equals("update")) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		//削除済みのレコードが存在していないか確認
		sql = "SELECT DeleteFlag" + " FROM RESMaster.dbo.MST_PLU_URGENT" + " WHERE CompanyId='" + p_company
				+ "'" + " AND StoreId='" + p_store + "'" + " AND MdInternal='" + p_mdinternal + "'";
		ps = conn.prepareStatement(sql);
		rs = ps.executeQuery();

		if (rs.next()) {

			//update
			if (rs.getString("DeleteFlag") == "1") {

				PreparedStatement psstr = null;
				sql = "";
				sql = "UPDATE RESMaster.dbo.MST_PLU_URGENT " + " SET DeleteFlag=0," + " UrgentPrice=?,"
						+ " UpdCount=UpdCount+1," + " UpdDate=GETDATE()," + " UpdAppId='PluUrgent',"
						+ " UpdOpeCode=?" + " WHERE CompanyId=?" + " AND StoreId=?" + " AND MdInternal=?";
				psstr = conn.prepareStatement(sql);
				psstr.setString(1, p_urgprice);
				psstr.setString(2, p_userid);
				psstr.setString(3, p_company);
				psstr.setString(4, p_store);
				psstr.setString(5, p_mdinternal);
				try {
					int rsIns = psstr.executeUpdate();
					if (rsIns > 0) {
						conn.commit();
						conn.close();
						psstr.close();
						//write maintenancelog
						WriteMaintenanceLog(p_userid,p_company,p_store,p_mdinternal,"2");
						response.sendRedirect("PluUrgentUpdate.jsp");
					} else {
						conn.rollback();
						conn.close();
						psstr.close();
						System.out.println("fail");
						response.sendRedirect("PluUrgentUpdate.jsp?false");
					}
					
				} catch (Exception e) {
					conn.close();
					psstr.close();
					response.sendRedirect("PluUrgentUpdate.jsp?error");
				}
			} else {

				PreparedStatement psstr = null;
				sql = "";
				//update
				sql = "UPDATE RESMaster.dbo.MST_PLU_URGENT " 
						+ " SET DeleteFlag=0," 
						+ " UrgentPrice=?,"
						+ " UpdCount=UpdCount+1," 
						+ " UpdDate=GETDATE()," 
						+ " UpdAppId='PluUrgent',"
						+ " UpdOpeCode=?" 
						+ " WHERE CompanyId=?" 
						+ " AND StoreId=?" 
						+ " AND MdInternal=?";
				psstr = conn.prepareStatement(sql);
				psstr.setString(1, p_urgprice);
				psstr.setString(2, p_userid);
				psstr.setString(3, p_company);
				psstr.setString(4, p_store);
				psstr.setString(5, p_mdinternal);
				try {
					int rsIns = psstr.executeUpdate();
					if (rsIns > 0) {
						conn.commit();
						conn.close();
						psstr.close();
						WriteMaintenanceLog(p_userid,p_company,p_store,p_mdinternal,"2");
						response.sendRedirect("PluUrgentUpdate.jsp");
					} else {
						conn.rollback();
						conn.close();
						psstr.close();
						System.out.println("fail");
						response.sendRedirect("PluUrgentUpdate.jsp?false");
					}
					
				} catch (Exception e) {
					conn.close();
					psstr.close();
					response.sendRedirect("PluUrgentUpdate.jsp?error");
				}
			}
		} else {
			//insert
			PreparedStatement psstr = null;
			sql = "";
			sql = "INSERT INTO RESMaster.dbo.MST_PLU_URGENT " 
					+ "SELECT " + "? " + ",? " + ",? " + ",? " + ",? "
					+ ",? " + ",? " + ",? " + ",'0' " + ",NULL " + ",NULL " + ",NULL " + ",GETDATE() "
					+ ",'PluUrgent' " + ",? " + ",0 " + ",GETDATE() " + ",'PluUrgent' " + ",? ";
			psstr = conn.prepareStatement(sql);
			psstr.setString(1, p_company);
			psstr.setString(2, p_store);
			psstr.setString(3, p_mdinternal);
			psstr.setString(4, p_dpt);
			psstr.setString(5, p_line);
			psstr.setString(6, p_class);
			psstr.setString(7, p_mdinternal);
			psstr.setString(8, p_urgprice);
			psstr.setString(9, p_userid);
			psstr.setString(10, p_userid);
			try {
				int rsIns = psstr.executeUpdate();
				if (rsIns > 0) {
					conn.commit();
					conn.close();
					psstr.close();
					WriteMaintenanceLog(p_userid,p_company,p_store,p_mdinternal,"1");
					response.sendRedirect("PluUrgentUpdate.jsp");
				} else {
					conn.rollback();
					conn.close();
					psstr.close();
					System.out.println("fail");
					response.sendRedirect("PluUrgentUpdate.jsp?false");
				}
				
			} catch (Exception e) {
				conn.close();
				psstr.close();
				response.sendRedirect("PluUrgentUpdate.jsp?error");
			}
		}
		ps.close();
		rs.close();
	} else {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		sql = "SELECT *" + " FROM RESMaster.dbo.MST_PLU_URGENT" + " WHERE CompanyId='" + p_company + "'"
				+ " AND StoreId='" + p_store + "'" + " AND MdInternal='" + p_mdinternal + "'";
		ps = conn.prepareStatement(sql);
		rs = ps.executeQuery();
		if (rs.next()) {
			PreparedStatement psstr = null;
			sql = "";
			//delete
			sql = "UPDATE RESMaster.dbo.MST_PLU_URGENT " + " SET DeleteFlag=1," + " UpdCount=UpdCount+1,"
					+ " DelDate=GETDATE()," + " DelAppId='PluUrgent'," + " DelOpeCode=?" + " WHERE CompanyId=?"
					+ " AND StoreId=?" + " AND MdInternal=?";
			psstr = conn.prepareStatement(sql);
			psstr.setString(1, p_userid);
			psstr.setString(2, p_company);
			psstr.setString(3, p_store);
			psstr.setString(4, p_mdinternal);
			try {
				int rsIns = psstr.executeUpdate();
				if (rsIns > 0) {
					conn.commit();
					conn.close();
					psstr.close();
					WriteMaintenanceLog(p_userid,p_company,p_store,p_mdinternal,"2");
					ps.close();
					rs.close();
					response.sendRedirect("PluUrgentUpdate.jsp");
				} else {
					conn.rollback();
					conn.close();
					psstr.close();
					System.out.println("fail");
					ps.close();
					rs.close();
					response.sendRedirect("PluUrgentUpdate.jsp?false");
				}
				
			} catch (Exception e) {
				conn.close();
				psstr.close();
				ps.close();
				rs.close();
				response.sendRedirect("PluUrgentUpdate.jsp?error");
			}
		}
		ps.close();
		rs.close();
	}
%>

<%!public static void WriteMaintenanceLog(String p_userid, String p_company, String p_store, String p_mdinternal, String p_action) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement psstr = null;
		try {
			String sql = "";
			String referenceCondition = "CompanyId = '" + p_company + "' AND StoreId = '" + p_store + "' AND MdInternal = '" + p_mdinternal + "'";
			String maintenanceLogId = UUID.randomUUID().toString();
			JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
			conn = dbManager.getConnection();
			conn.setAutoCommit(false);
			
			sql = "";
			sql = "INSERT INTO RESMaster.dbo.REC_MAINT_LOG_URGENT_STORE "
			        + "(MaintenanceLogId, CompanyId, StoreId, InsDate, InsAppId, InsOpeCode, UpdCount, UpdDate, UpdAppId, UpdOpeCode)"
			        + " VALUES (?, ?, ?, GETDATE(), 'PluUrgent', ?, 0, GETDATE(), 'PluUrgent', ?);";
			ps = conn.prepareStatement(sql);
			ps.setString(1, maintenanceLogId);
			ps.setString(2, p_company);
			ps.setString(3, p_store);
			ps.setString(4, p_userid);
			ps.setString(5, p_userid);
			
			int rsIns = ps.executeUpdate();
			if (rsIns > 0) {
			    System.out.println("success");
			} else {
			    System.out.println("fail");
			}

			sql = "";
			sql = "INSERT INTO RESMaster.dbo.REC_MAINT_LOG_URGENT" + "("
			        + "ReceivedMaintenanceId" + ",MaintenanceLogId" + ",MaintenanceType" + ",SyncGroupId" + ",ReferenceCondition"
					+ ",MasterUpdDate" + ",MasterUpdAppId" + ",MasterUpdOpeCode" + ",TargetStoreType"
			        + ",InsDate" + ",InsAppId" + ",InsOpeCode" + ",UpdCount" + ",UpdDate" + ",UpdAppId" + ",UpdOpeCode"
					+ ")"
					+ "VALUES(0,?,?,90,?,GETDATE(),'PluUrgent',?,1,GETDATE(),'PluUrgent',?,0,GETDATE(),'PluUrgent',?);";
			psstr = conn.prepareStatement(sql);
			psstr.setString(1, maintenanceLogId);
			psstr.setString(2, p_action);
			psstr.setString(3, referenceCondition);
			psstr.setString(4, p_userid);
			psstr.setString(5, p_userid);
			psstr.setString(6, p_userid);

			rsIns = psstr.executeUpdate();
			if (rsIns > 0) {
				System.out.println("success");
			} else {
				System.out.println("fail");
			}
			conn.commit();
		} catch (SQLException e) {
		    conn.rollback();
		    throw e;
		} finally {
			ps.close();
			psstr.close();
			conn.close();
		}
	}%>