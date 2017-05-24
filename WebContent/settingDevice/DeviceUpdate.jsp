<%@ page language="java" pageEncoding="utf-8"%>
<%@page
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.Date"
    import="java.util.ArrayList"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"%>
<%

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    // ArrayList
        // PRM_DEVICE_ATTRIBUTE
    ArrayList<String> attrAttributeIdList = new ArrayList<String>();
    ArrayList<String> attrDescriptionList = new ArrayList<String>();
        // MST_PRINTERINFO
    ArrayList<String> attrPrinterIdList = new ArrayList<String>();
    ArrayList<String> attrPrinterNameList = new ArrayList<String>();
        // MST_TILLIDINFO
    ArrayList<String> attrTillIdList = new ArrayList<String>();
        // PRM_QUEUEBUSTER_LINK
    ArrayList<String> attrPqlIdList = new ArrayList<String>();
    ArrayList<String> attrPqlDisplayNameList = new ArrayList<String>();

    // 変数宣言
    String sqlStr = "";
    String g_storeID;
    String g_terminalID;
    String g_companyID;
    String g_UpFlg;
    String errStr = "";
    String infoStr = "";

    String user = ""; //ログインユーザー名
    //ログインユーザー名取得
	try {
	    user = request.getRemoteUser() != null ? request.getRemoteUser() : "";
	} catch (Exception e) {
	}

    String dev_CompanyId="",dev_StoreId="",dev_TerminalId="",dev_Training="",dev_DeviceName="",dev_AttributeId="",dev_PrinterId="",dev_TillId="",dev_LinkQueueBuster = "";
    String tra_CompanyId="",tra_StoreId="",tra_TerminalId="",tra_Training="",tra_DeviceName="",tra_AttributeId="",tra_PrinterId="",tra_TillId="",tra_LinkQueueBuster = "";
    String ter_CompanyId="",ter_StoreId="",ter_TerminalId="",ter_FloorId="",ter_TerminalName="",ter_IPAddress="",ter_StoreClass="",ter_TerminalType="",
    		ter_TillType="",ter_RelationType="",ter_LogoFileName="",ter_InshiFileName="",ter_SalesPromotionBMPPath="",ter_UpdCount = "";
    
    g_storeID    = request.getParameter("s1");
    g_terminalID = request.getParameter("t1");
    g_companyID  = request.getParameter("r1");
    g_UpFlg      = request.getParameter("UpFlg");


    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
    Connection connection = dbManager.getConnection();

    if( ( null != g_storeID)
     && ( null != g_terminalID )
     && ( null != g_companyID )
    ){
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.StringBuilder sbTraining = new java.lang.StringBuilder();

        if( g_UpFlg == null ){
            // PRM_DEVICE_ATTRIBUTE
            sqlStr = "SELECT att.AttributeId,att.Description FROM RESMaster.dbo.PRM_DEVICE_ATTRIBUTE att";
            PreparedStatement psSelect = connection.prepareStatement(sqlStr);
            ResultSet rsSelect = psSelect.executeQuery();
            while(rsSelect.next()) {
                attrAttributeIdList.add(rsSelect.getString("AttributeId"));
                attrDescriptionList.add(rsSelect.getString("Description"));
            }
            psSelect.close();

            // MST_PRINTERINFO
            sqlStr =
                      "SELECT "
                    + "  pri.PrinterId     "
                    + " ,pri.PrinterName   "
                    + " FROM RESMaster.dbo.MST_PRINTERINFO pri  "
                    + "WHERE "
                    + "     pri.CompanyId= " + "'" + g_companyID  + "'"
                    + " AND pri.StoreId=   " + "'" + g_storeID    + "'"
                    + " AND pri.DeleteFlag<>'1' AND pri.Status<>'Deleted'"
                    + "; "
                       ;
            psSelect = connection.prepareStatement(sqlStr);
            rsSelect = psSelect.executeQuery();
            while(rsSelect.next()) {
                attrPrinterIdList.add(rsSelect.getString("PrinterId"));
                attrPrinterNameList.add(rsSelect.getString("PrinterName"));
            }
            psSelect.close();
            // MST_TILLIDINFO
            sqlStr =
                      "SELECT "
                    + "  til.TillId     "
                    + " FROM RESMaster.dbo.MST_TILLIDINFO til  "
                    + "WHERE "
                    + "     til.CompanyId= " + "'" + g_companyID  + "'"
                    + " AND til.StoreId=   " + "'" + g_storeID    + "'"
                    + " AND til.DeleteFlag<>'1'"
                    + "; "
                       ;
            psSelect = connection.prepareStatement(sqlStr);
            rsSelect = psSelect.executeQuery();
            while(rsSelect.next()) {
                attrTillIdList.add(rsSelect.getString("TillId"));
            }
            psSelect.close();
            // PRM_QUEUEBUSTER_LINK
            sqlStr =
                      "SELECT "
                    + "  pql.Id            "
                    + " ,pql.DisplayName   "
                    + " FROM RESMaster.dbo.PRM_QUEUEBUSTER_LINK pql  "
                    + "WHERE "
                    + "     pql.StoreId=   " + "'" + g_storeID    + "'"
                    + " AND pql.CompanyId= " + "'" + g_companyID  + "'"
                    + " AND pql.Status<>'Deleted'"
                    + "; "
                       ;
            psSelect = connection.prepareStatement(sqlStr);
            rsSelect = psSelect.executeQuery();
            while(rsSelect.next()) {
                attrPqlIdList.add(rsSelect.getString("Id"));
                attrPqlDisplayNameList.add(rsSelect.getString("DisplayName"));
            }
            
            // select
            // SQL設定
            sqlStr = "SELECT";
            // MST_DEVICEINFO
            sqlStr += "  deviceinfo.CompanyId         AS dev_CompanyId           ";
            sqlStr += " ,deviceinfo.StoreId           AS dev_StoreId             ";
            sqlStr += " ,deviceinfo.TerminalId        AS dev_TerminalId          ";
            sqlStr += " ,deviceinfo.Training          AS dev_Training            ";
            sqlStr += " ,deviceinfo.DeviceName        AS dev_DeviceName          ";
            sqlStr += " ,deviceinfo.AttributeId       AS dev_AttributeId         ";
            sqlStr += " ,deviceinfo.PrinterId         AS dev_PrinterId           ";
            sqlStr += " ,deviceinfo.TillId            AS dev_TillId              ";
            sqlStr += " ,deviceinfo.LinkQueueBuster   AS dev_LinkQueueBuster     ";
            // MST_DEVICEINFO(トレーニング)
            sqlStr += " ,deviceinfo2.CompanyId        AS tra_CompanyId           ";
            sqlStr += " ,deviceinfo2.StoreId          AS tra_StoreId             ";
            sqlStr += " ,deviceinfo2.TerminalId       AS tra_TerminalId          ";
            sqlStr += " ,deviceinfo2.Training         AS tra_Training            ";
            sqlStr += " ,deviceinfo2.DeviceName       AS tra_DeviceName          ";
            sqlStr += " ,deviceinfo2.AttributeId      AS tra_AttributeId         ";
            sqlStr += " ,deviceinfo2.PrinterId        AS tra_PrinterId           ";
            sqlStr += " ,deviceinfo2.TillId           AS tra_TillId              ";
            sqlStr += " ,deviceinfo2.LinkQueueBuster  AS tra_LinkQueueBuster     ";

            //MST_TERMINALINFO
            sqlStr += " ,terminalinfo.CompanyId       AS ter_CompanyId           ";
            sqlStr += " ,terminalinfo.StoreId         AS ter_StoreId             ";
            sqlStr += " ,terminalinfo.TerminalId      AS ter_TerminalId          ";
            sqlStr += " ,terminalinfo.FloorId         AS ter_FloorId             ";
            sqlStr += " ,terminalinfo.TerminalName    AS ter_TerminalName        ";
            sqlStr += " ,terminalinfo.IPAddress       AS ter_IPAddress           ";
            sqlStr += " ,terminalinfo.StoreClass      AS ter_StoreClass          ";
            sqlStr += " ,terminalinfo.TerminalType    AS ter_TerminalType        ";
            sqlStr += " ,terminalinfo.TillType        AS ter_TillType            ";
            sqlStr += " ,terminalinfo.RelationType    AS ter_RelationType        ";
            sqlStr += " ,terminalinfo.LogoFileName    AS ter_LogoFileName        ";
            sqlStr += " ,terminalinfo.InshiFileName   AS ter_InshiFileName       ";
            sqlStr += " ,terminalinfo.SubCode2        AS ter_SalesPromotionBMPPath";
            sqlStr += " ,terminalinfo.UpdCount        AS ter_UpdCount            ";

            // テーブル定義
            sqlStr += " FROM RESMaster.dbo.MST_DEVICEINFO deviceinfo";
            sqlStr += " LEFT JOIN RESMaster.dbo.MST_PRINTERINFO printinfo";
            sqlStr += " ON  deviceinfo.CompanyId = printinfo.CompanyId";
            sqlStr += " AND deviceinfo.StoreId = printinfo.StoreId";
            sqlStr += " AND deviceinfo.PrinterId = printinfo.PrinterId";
            sqlStr += " AND deviceinfo.Training = '0'";
            // トレーニング
            sqlStr += " LEFT JOIN RESMaster.dbo.MST_DEVICEINFO deviceinfo2";
            sqlStr += " ON deviceinfo2.CompanyId = deviceinfo.CompanyId";
            sqlStr += " AND deviceinfo2.StoreId = deviceinfo.StoreId";
            sqlStr += " AND deviceinfo2.TerminalId = deviceinfo.TerminalId";
            sqlStr += " AND deviceinfo2.Training = '1'";
            //MST_TERMINALINFO
            sqlStr += " LEFT JOIN RESMaster.dbo.MST_TERMINALINFO terminalinfo";
            sqlStr += " ON  deviceinfo.CompanyId  = terminalinfo.CompanyId  ";
            sqlStr += " AND deviceinfo.StoreId    = terminalinfo.StoreId    ";
            sqlStr += " AND deviceinfo.TerminalId = terminalinfo.TerminalId ";

            sqlStr += " WHERE ";
            sqlStr += "     deviceinfo.StoreId    = LTRIM(RTRIM(?))";
            sqlStr += " AND deviceinfo.TerminalId = LTRIM(RTRIM(?))";
            sqlStr += " AND deviceinfo.CompanyId  = LTRIM(RTRIM(?))";

//out.print("</br>【" + sqlStr + "】");
            PreparedStatement ps = connection.prepareStatement(sqlStr);
            ps.setString(1, g_storeID   );
            ps.setString(2, g_terminalID);
            ps.setString(3, g_companyID );

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // MST_DEVICEINFO
                dev_CompanyId = rs.getString("dev_CompanyId");
                dev_StoreId = rs.getString("dev_StoreId");
                dev_TerminalId = rs.getString("dev_TerminalId");
                dev_Training = rs.getString("dev_Training");
                dev_DeviceName = rs.getString("dev_DeviceName");
                dev_AttributeId = rs.getString("dev_AttributeId");
                dev_PrinterId = rs.getString("dev_PrinterId");
                dev_TillId = rs.getString("dev_TillId");
                dev_LinkQueueBuster = rs.getString("dev_LinkQueueBuster");
                // MST_DEVICEINFO(トレーニング)
                tra_CompanyId = rs.getString("tra_CompanyId");
                tra_StoreId = rs.getString("tra_StoreId");
                tra_TerminalId = rs.getString("tra_TerminalId");
                tra_Training = rs.getString("tra_Training");
                tra_DeviceName = rs.getString("tra_DeviceName");
                tra_AttributeId = rs.getString("tra_AttributeId");
                tra_PrinterId = rs.getString("tra_PrinterId");
                tra_TillId = rs.getString("tra_TillId");
                tra_LinkQueueBuster = rs.getString("tra_LinkQueueBuster");

                // MST_TERMINALINFO
                ter_CompanyId = rs.getString("ter_CompanyId");
                ter_StoreId = rs.getString("ter_StoreId");
                ter_TerminalId = rs.getString("ter_TerminalId");
                ter_FloorId = rs.getString("ter_FloorId");
                ter_TerminalName = rs.getString("ter_TerminalName");
                ter_IPAddress = rs.getString("ter_IPAddress");
                ter_StoreClass = rs.getString("ter_StoreClass");
                ter_TerminalType = rs.getString("ter_TerminalType");
                ter_TillType = rs.getString("ter_TillType");
                ter_RelationType = rs.getString("ter_RelationType");
                ter_LogoFileName = rs.getString("ter_LogoFileName");
                ter_InshiFileName = rs.getString("ter_InshiFileName");
                ter_SalesPromotionBMPPath = rs.getString("ter_SalesPromotionBMPPath");
                ter_UpdCount = rs.getString("ter_UpdCount");
                session.setAttribute("ter_UpdCount", ter_UpdCount);

                rs.close();
                
            } else {
                errStr = "3";
            }
            ps.close();
       }else{
            // Update
            // SQL設定
            sqlStr = 
                /* MST_DEVICEINFO */
                  "UPDATE RESMaster.dbo.MST_DEVICEINFO SET "
                + "  DeviceName=?        "
                + " ,AttributeId=?       "
                + " ,PrinterId=?         "
                + " ,TillId=?            "
                + " ,LinkQueueBuster=?   "
                + " ,Status     = 'Active'          "
                + " ,DeleteFlag = '0'               "
                + " ,UpdCount   = UpdCount+1        "
                + " ,UpdDate    = CURRENT_TIMESTAMP "
                + " ,UpdAppId   = 'settingDevice'   "
                + " ,UpdOpeCode = ?                 "
                + "WHERE "
                + "     CompanyId= " + "'" + g_companyID  + "'"
                + " AND StoreId=   " + "'" + g_storeID    + "'"
                + " AND TerminalId=" + "'" + g_terminalID + "'"
                + " AND Training  = '0' "
                /* MST_DEVICEINFO(トレーニング) */
                + "UPDATE RESMaster.dbo.MST_DEVICEINFO SET "
                + "  DeviceName=?        "
                + " ,AttributeId=?       "
                + " ,PrinterId=?         "
                + " ,TillId=?            "
                + " ,LinkQueueBuster=?   "
                + " ,Status     = 'Active'          "
                + " ,DeleteFlag = '0'               "
                + " ,UpdCount   = UpdCount+1        "
                + " ,UpdDate    = CURRENT_TIMESTAMP "
                + " ,UpdAppId   = 'settingDevice'   "
                + " ,UpdOpeCode = ?                 "
                + "WHERE "
                + "     CompanyId= " + "'" + g_companyID  + "'"
                + " AND StoreId=   " + "'" + g_storeID    + "'"
                + " AND TerminalId=" + "'" + g_terminalID + "'"
                + " AND Training  = '1' "

                /* MST_TERMINALINFO */
                + " UPDATE RESMaster.dbo.MST_TERMINALINFO SET "
                + "  FloorId=?                      "
                + " ,TerminalName=?                 "
                + " ,IPAddress=?                    "
                + " ,StoreClass=?                   "
                + " ,TerminalType=?                 "
                + " ,TillType=?                     "
                + " ,RelationType=?                 "
                + " ,LogoFileName=?                 "
                + " ,InshiFileName=?                "
                + " ,SubCode2=?                "
                + " ,DeleteFlag = '0'               "
                + " ,UpdCount   = UpdCount+1        "
                + " ,UpdDate    = CURRENT_TIMESTAMP "
                + " ,UpdAppId   = 'settingDevice'   "
                + " ,UpdOpeCode = ?                 "
                + "WHERE "
                + "     CompanyId= " + "'" + g_companyID  + "'"
                + " AND StoreId=   " + "'" + g_storeID    + "'"
                + " AND TerminalId=" + "'" + g_terminalID + "'"
                + "  IF @@ROWCOUNT = 0"
                + "  BEGIN"
                + "  INSERT INTO RESMaster.dbo.MST_TERMINALINFO "
                + " (                    "
                + "   CompanyId          "
                + " , StoreId            "
                + " , TerminalId         "
                + " , FloorId            "
                + " , TerminalName       "
                + " , IPAddress          "
                + " , StoreClass         "
                + " , TerminalType       "
                + " , TillType           "
                + " , RelationType       "
                + " , LogoFileName       "
                + " , InshiFileName      "
                + " , SubCode2           "
                + " , DeleteFlag         "
                + " , InsDate            "
                + " , InsAppId           "
                + " , InsOpeCode         "
                + " , UpdCount           "
                + " , UpdDate            "
                + " , UpdAppId           "
                + " , UpdOpeCode         "
                + " , ConnectionFlag1    "
                + " ,ConnectionFlag2     "
                + " ,ConnectionFlag3     "
                + " ,ConnectionFlag4     "
                + " ,ConnectionFlag5     "
                + " ,ConnectionFlag6     "
                + " ,ConnectionFlag7     "
                + " ,ConnectionFlag8     "
                + " ,ConnectionFlag9     "
                + " ,ConnectionFlag10    "
                + " ,ConnectionFlag11    "
                + " ,ConnectionFlag12    "
                + " ,ConnectionFlag13    "
                + " ,ConnectionFlag14    "
                + " ,ConnectionFlag15    "
                + "  ) VALUES(           "
                + "'" + g_companyID  + "'"
                + ",'" + g_storeID    + "'"
                + ",'" + g_terminalID + "'"
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , ?                  "
                + " , '0'                "
                + " , CURRENT_TIMESTAMP  "
                + " , 'settingDevice'    "
                + " , ?                  "
                + " , 0                  "
                + " , CURRENT_TIMESTAMP  "
                + " , 'settingDevice'    "
                + " , ?                  "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " ,'0'                 "
                + " )                    "
                + "  END";
//out.print("</br>【" + sqlStr + "】");
            PreparedStatement psUpd = connection.prepareStatement(sqlStr);

            // MST_DEVICEINFOの設定
               //DeviceName
            psUpd.setString(1, request.getParameter("dev_DeviceName"));
               //AttributeId
            psUpd.setString(2, request.getParameter("dev_AttributeId"));
               //PrinterId
            psUpd.setString(3, request.getParameter("dev_PrinterId"));
               //TillId
            psUpd.setString(4, request.getParameter("dev_TillId"));
               //LinkQueueBuster
            psUpd.setString(5, request.getParameter("dev_LinkQueueBuster"));

            psUpd.setString(6, user);

            // MST_DEVICEINFOの設定(トレーニング)
            //DeviceName
            psUpd.setString(7, request.getParameter("tra_DeviceName"));
               //AttributeId
            psUpd.setString(8, request.getParameter("tra_AttributeId"));
               //PrinterId
            psUpd.setString(9, request.getParameter("tra_PrinterId"));
               //TillId
            psUpd.setString(10, request.getParameter("tra_TillId"));
               //LinkQueueBuster
            psUpd.setString(11, request.getParameter("tra_LinkQueueBuster"));

            psUpd.setString(12, user);

            // MST_TERMINALINFOの設定(update)
               //FloorId
            psUpd.setString(13, request.getParameter("ter_FloorId"));
               //TerminalName
            psUpd.setString(14, request.getParameter("ter_TerminalName"));
               //IPAddress
            psUpd.setString(15, request.getParameter("ter_IPAddress"));
               //StoreClass
            psUpd.setString(16, request.getParameter("ter_StoreClass"));
               //TerminalType
            psUpd.setString(17, request.getParameter("ter_TerminalType"));
               //TillType
            psUpd.setString(18, request.getParameter("ter_TillType"));
               //RelationType
            psUpd.setString(19, request.getParameter("ter_RelationType"));
            psUpd.setString(20, request.getParameter("ter_LogoFileName"));
            psUpd.setString(21, request.getParameter("ter_InshiFileName"));
            psUpd.setString(22, request.getParameter("ter_SalesPromotionBMPPath"));
            psUpd.setString(23, user);

            // MST_TERMINALINFOの設定(insert)
            psUpd.setString(24, request.getParameter("ter_FloorId"));
            psUpd.setString(25, request.getParameter("ter_TerminalName"));
            psUpd.setString(26, request.getParameter("ter_IPAddress"));
            psUpd.setString(27, request.getParameter("ter_StoreClass"));
            psUpd.setString(28, request.getParameter("ter_TerminalType"));
            psUpd.setString(29, request.getParameter("ter_TillType"));
            psUpd.setString(30, request.getParameter("ter_RelationType"));
            psUpd.setString(31, request.getParameter("ter_LogoFileName"));
            psUpd.setString(32, request.getParameter("ter_InshiFileName"));
            psUpd.setString(33, request.getParameter("ter_SalesPromotionBMPPath"));
            psUpd.setString(34, user);
            psUpd.setString(35, user);

            try {
               int iret = psUpd.executeUpdate();
               connection.commit();
               infoStr = "2";
            } catch (Exception e) {
               errStr = "4";
            } finally {
               // クローズ
               connection.close();
               psUpd.close();
            }
            // 画面遷移
            String s_msg ="DeviceInfo.jsp?"
                    + "s1="
                    + "&t1="
                    + "&r1="
                    + "&y1="
                    + "&err="+ errStr
                    + "&info="+ infoStr
                    ;
            response.sendRedirect(s_msg);
        }

    // 起動時
    connection.close();

%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="./css/default.css">
    <title>NCR RES DeviceUpdate Viewer</title>
    <!-- jQuery -->
    <script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
    <!-- ダイアログ共通 -->
    <script type="text/javascript" src="./js/DialogMessage.js"></script>
</head>
<body class="res-maincontent">

<form action="DeviceUpdate.jsp" method="post" id="updateform" onsubmit="return false;">

<input type="hidden" name="add_dev_CompanyId" id="add_dev_CompanyId" value="<%=request.getParameter("dev_CompanyId")%>">
<input type="hidden" name="add_dev_StoreId" id="add_dev_StoreId" value="<%=request.getParameter("dev_StoreId")%>">
<input type="hidden" name="add_dev_TerminalId" id="add_dev_TerminalId" value="<%=request.getParameter("dev_TerminalId")%>">
<input type="hidden" name="UpFlg" id="UpFlg" value="">
<input type="hidden" name="s1" id="s1" value="<%=request.getParameter("s1")%>">
<input type="hidden" name="t1" id="t1" value="<%=request.getParameter("t1")%>">
<input type="hidden" name="r1" id="r1" value="<%=request.getParameter("r1")%>">

<div class="table-scroll-area-v table-scroll-area-h" id="tabletable" style="height:780px">
  <div class="panel" id="updateArea">
    <table class="res-tbl" style="width:100%">
      <tr>
        <th colspan="4" align="center">【MST_DEVICEINFO】</th>
      </tr>
    </table>

    <table cellspacing="4" cellpadding="4" style="width:100%">
      <tr>
        <td align="right" style="width: 20%"></td>
        <td align="left" style="width: 40%">設定値</td>
        <td align="left" style="width: 40%">設定値(トレーニング用)</td>
      </tr>
      <tr>
        <td align="right">企業番号 ： </td>
        <td><input type="text" id="dev_CompanyId" name="dev_CompanyId" disabled value="<%= dev_CompanyId%>"/></td>
        <td><input type="text" id="tra_CompanyId" name="tra_CompanyId" disabled value="<%= tra_CompanyId%>"/></td>
      </tr>
      <tr>
        <td align="right">店番号 ： </td>
        <td><input type="text" id="dev_StoreId" name="dev_StoreId" disabled style="width: 100%" value="<%= dev_StoreId%>" /></td>
        <td><input type="text" id="tra_StoreId" name="tra_StoreId" disabled style="width: 100%" value="<%= tra_StoreId%>" /></td>
      </tr>
      <tr>
        <td align="right">端末番号 ： </td>
        <td><input type="text" id="dev_TerminalId" name="dev_TerminalId" disabled style="width: 100%" value="<%= dev_TerminalId%>" /></td>
        <td><input type="text" id="tra_TerminalId" name="tra_TerminalId" disabled style="width: 100%" value="<%= tra_TerminalId%>" /></td>
      </tr>
<!-- 
      <tr>
        <td align="right">トレーニング(Training)</td>
        <td><input type="text" id="dev_Training" name="dev_Training" disabled style="width: 100%" value="<%= dev_Training%>" /></td>
        <td><input type="text" id="tra_Training" name="tra_Training" disabled style="width: 100%" value="<%= tra_Training%>" /></td>
      </tr>
 -->
      <tr>
        <td align="right">端末名： </td>
        <td><input maxlength="15" type="text" id="dev_DeviceName" name="dev_DeviceName" size=30 value="<%= dev_DeviceName%>" required pattern=".{0,15}"></td>
        <td><input maxlength="15" type="text" id="tra_DeviceName" name="tra_DeviceName" size=30 value="<%= tra_DeviceName%>" required pattern=".{0,15}"></td>
      </tr>
      <tr>
        <td colspan="3" align="right">(15文字以内で入力してください。)</td>
      </tr>
      <tr>
        <td align="right" class="orangetd">属性番号： </td>
        <td><select id="dev_AttributeId" name="dev_AttributeId" style="width: 100%" required>
        <%
          for (int i=0;i<attrAttributeIdList.size();i++) {
              if ( attrAttributeIdList.get(i).equals(dev_AttributeId)) {
                  out.print("<option value=\"" + attrAttributeIdList.get(i) + "\"");
                  out.println(" selected>" + attrAttributeIdList.get(i) + " : " + attrDescriptionList.get(i) + "</option>");
              } else {
                  out.print("<option value=\"" + attrAttributeIdList.get(i) + "\"");
                  out.println(">" + attrAttributeIdList.get(i) + " : " + attrDescriptionList.get(i) + "</option>");
              }
          }
        %>
        </select></td>
        <td><select id="tra_AttributeId" name="tra_AttributeId" style="width: 100%" required>
          <%
            for (int i=0;i<attrAttributeIdList.size();i++) {
                if ( attrAttributeIdList.get(i).equals(tra_AttributeId)) {
                    out.print("<option value=\"" + attrAttributeIdList.get(i) + "\"");
                    out.println(" selected>" + attrAttributeIdList.get(i) + " : " + attrDescriptionList.get(i) + "</option>");
                } else {
                    out.print("<option value=\"" + attrAttributeIdList.get(i) + "\"");
                    out.println(">" + attrAttributeIdList.get(i) + " : " + attrDescriptionList.get(i) + "</option>");
                }
            }
          %>
        </select></td>
      </tr>
      <tr>
        <td align="right" class="orangetd">プリンターID： </td>
        <td><select id="dev_PrinterId" name="dev_PrinterId" style="width: 100%" required>
          <%
            for (int i=0;i<attrPrinterIdList.size();i++) {
                if ( attrPrinterIdList.get(i).equals(dev_PrinterId)) {
                    out.print("<option value=\"" + attrPrinterIdList.get(i) + "\"");
                    out.println(" selected>" + attrPrinterIdList.get(i) + " : " + attrPrinterNameList.get(i) + "</option>");
                } else {
                    out.print("<option value=\"" + attrPrinterIdList.get(i) + "\"");
                    out.println(">" + attrPrinterIdList.get(i) + " : " + attrPrinterNameList.get(i) + "</option>");
                }
            }
          %>
          </select></td>
          <td><select id="tra_PrinterId" name="tra_PrinterId" style="width: 100%" required>
            <%
            for (int i=0;i<attrPrinterIdList.size();i++) {
                if ( attrPrinterIdList.get(i).equals(tra_PrinterId)) {
                    out.print("<option value=\"" + attrPrinterIdList.get(i) + "\"");
                    out.println(" selected>" + attrPrinterIdList.get(i) + " : " + attrPrinterNameList.get(i) +"</option>");
                } else {
                    out.print("<option value=\"" + attrPrinterIdList.get(i) + "\"");
                    out.println(">" + attrPrinterIdList.get(i) + " : " + attrPrinterNameList.get(i) + "</option>");
                }
            }
            %>
        </select></td>
      </tr>
      <tr>
        <td align="right" class="orangetd">ドロワーID： </td>
        <td><select id="dev_TillId" name="dev_TillId" style="width: 100%" required required>
          <%
            for (int i=0;i<attrTillIdList.size();i++) {
                if ( attrTillIdList.get(i).equals(dev_TillId)) {
                    out.print("<option value=\"" + attrTillIdList.get(i) + "\"");
                    out.println(" selected>" + attrTillIdList.get(i) + "</option>");
                } else {
                    out.print("<option value=\"" + attrTillIdList.get(i) + "\"");
                    out.println(">" + attrTillIdList.get(i) + "</option>");
                }
            }
          %>
        </select></td>
        <td><select id="tra_TillId" name="tra_TillId" style="width: 100%" required>
          <%
            for (int i=0;i<attrTillIdList.size();i++) {
                if ( attrTillIdList.get(i).equals(tra_TillId)) {
                    out.print("<option value=\"" + attrTillIdList.get(i) + "\"");
                    out.println(" selected>" + attrTillIdList.get(i) + "</option>");
                } else {
                    out.print("<option value=\"" + attrTillIdList.get(i) + "\"");
                    out.println(">" + attrTillIdList.get(i) + "</option>");
                }
            }
          %>
        </select></td>
      </tr>
      <tr>
        <td align="right" class="orangetd">キューID： </td>
        <td><select id="dev_LinkQueueBuster" name="dev_LinkQueueBuster" style="width: 100%" required>
          <%
            for (int i=0;i<attrPqlIdList.size();i++) {
                if ( attrPqlIdList.get(i).equals(dev_LinkQueueBuster)) {
                    out.print("<option value=\"" + attrPqlIdList.get(i) + "\"");
                    out.println(" selected>" + attrPqlIdList.get(i) + " : " + attrPqlDisplayNameList.get(i) + "</option>");
                } else {
                    out.print("<option value=\"" + attrPqlIdList.get(i) + "\"");
                    out.println(">" + attrPqlIdList.get(i) + " : " + attrPqlDisplayNameList.get(i) + "</option>");
                }
            }
          %>
        </select></td>
        <td><select id="tra_LinkQueueBuster" name="tra_LinkQueueBuster" style="width: 100%" required>
          <%
            for (int i=0;i<attrPqlIdList.size();i++) {
                if ( attrPqlIdList.get(i).equals(tra_LinkQueueBuster)) {
                    out.print("<option value=\"" + attrPqlIdList.get(i) + "\"");
                    out.println(" selected>" + attrPqlIdList.get(i) + " : " + attrPqlDisplayNameList.get(i) + "</option>");
                } else {
                    out.print("<option value=\"" + attrPqlIdList.get(i) + "\"");
                    out.println(">" + attrPqlIdList.get(i) + " : " + attrPqlDisplayNameList.get(i) + "</option>");
                }
            }
          %>
        </select></td>
      </tr>
    </table>
  </div>

  <div class="panel" id="updateArea">
    <table class="res-tbl" style="width:100%">
      <tr>
        <th colspan="4" align="center">【MST_TERMINALINFO】</th>
      </tr>
    </table>

    <table cellspacing="4" cellpadding="4" style="width:100%">
<!-- 
      <tr>
        <td align="right"></td>
        <td align="left">設定値</td>
      </tr>
 -->
      <tr>
        <td align="right">企業番号 ： </td>
        <td align="left">
          <input type="text" id="ter_CompanyId" name="ter_CompanyId" value="<%= ter_CompanyId%>" disabled style="width: 100%" >
        </td>
      </tr>
      <tr>
        <td align="right">店番号 ： </td>
        <td align="left">
          <input type="text" id="ter_StoreId" name="ter_StoreId" value="<%= ter_StoreId%>" disabled style="width: 100%" >
        </td>
      </tr>
      <tr>
        <td align="right">端末番号 ： </td>
        <td align="left">
          <input type="text" id="ter_TerminalId" name="ter_TerminalId" value="<%= ter_TerminalId%>" disabled style="width: 100%" >
        </td>
      </tr>
      <tr>
        <td align="right">フロアID ： </td>
        <td align="left">
          <input maxlength="2" type="text" id="ter_FloorId" name="ter_FloorId" value="<%= ter_FloorId%>"  size=2 required pattern="\d{2}">(半角数字2桁を入力してください。)
        </td>
      </tr>
      <tr>
        <td align="right">端末名 ： </td>
        <td align="left">
          <input maxlength="15" type="text" id="ter_TerminalName" name="ter_TerminalName" value="<%= ter_TerminalName%>" size=30 required pattern=".{0,15}">(15文字以内で入力してください。)
        </td>
      </tr>
      <tr>
        <td align="right">IPアドレス ： </td>
        <td align="left">
          <input maxlength="15" type="text" id="ter_IPAddress" name="ter_IPAddress" value="<%= ter_IPAddress%>" size=15 required pattern="^\d{1,3}(\.\d{1,3}){3}$">
        </td>
      </tr>
      <tr>
        <td align="right">店舗種別 ： </td>
        <td align="left">
        <input maxlength="1" type="text" id="ter_StoreClass" name="ter_StoreClass" value="<%= ter_StoreClass%>" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
        </td>
      </tr>
      <tr>
        <td align="right">端末タイプ ： </td>
        <td align="left">
          <input maxlength="1" type="text" id="ter_TerminalType" name="ter_TerminalType" value="<%= ter_TerminalType%>" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
        </td>
      </tr>
      <tr>
        <td align="right">ドロワータイプ ： </td>
        <td align="left">
          <input maxlength="1" type="text" id="ter_TillType" name="ter_TillType" value="<%= ter_TillType%>" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
        </td>
      </tr>
      <tr>
        <td align="right">関連タイプ ： </td>
        <td align="left">
          <input maxlength="1" type="text" id="ter_RelationType" name="ter_RelationType" value="<%= ter_RelationType%>" size=1 required pattern="\d{1}">(半角数字1桁で入力してください)
        </td>
      </tr>
      <tr>
        <td align="right">ロゴファイルパス ： </td>
        <td align="left">
          <input maxlength="255" type="text" id="ter_LogoFileName" name="ter_LogoFileName" value="<%= ter_LogoFileName%>" size=40 required pattern=".{1,255}">(255文字以内で入力してください)
        </td>
      </tr>
      <tr>
        <td align="right">印紙ファイルパス ： </td>
        <td align="left">
          <input maxlength="255" type="text" id="ter_InshiFileName" name="ter_InshiFileName" value="<%= ter_InshiFileName%>" size=40 required pattern=".{1,255}">(255文字以内で入力してください)
        </td>
      </tr>
      <tr>
        <td align="right">販促ビットマップパス ： </td>
        <td align="left">
          <input maxlength="100" type="text" id="ter_SalesPromotionBMPPath" name="ter_SalesPromotionBMPPath" value="<%= ter_SalesPromotionBMPPath%>" size=40 required
                 pattern=".{1,98}\\\\">(100文字以内で最後に\\をつけて入力してください)
        </td>
      </tr>
    </table>
  </div>
</div>
<button id="fakeButton" style="display:none"></button>
</form>
<div align="right">
  <table>
    <tr>
      <td style="padding:10px;"><input type="button" value="更新" id="Update" name="Update" class="res-big-green"></td> 
    </tr>
  </table>
</div>
</body>

<script type="text/javascript">
jQuery(function ($) {
    // ボタン・クリック時にダイアログを開く
    $('#Update').click(function(e){
        var myform = document.getElementById('updateform');
        if (myform.checkValidity() == false) {
        	document.getElementById('fakeButton').click();
            return;
        }
        showDialog(
            "タイトル：未使用",
            "更新を実行します。",
            ButtonYesNo,
            function() {
                //「いいえ」を押したときの処理
            },
            function() {
                //「はい」を押したときの処理
                document.getElementById("UpFlg").value = "Update";
                myform.submit();
            }
        );
    });
});
(function() {
    var showLog = function() {
    };
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
