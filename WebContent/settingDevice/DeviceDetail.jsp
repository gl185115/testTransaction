<%@ page language="java" pageEncoding="utf-8"%>
<%@page
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.Date"
    import="java.util.ArrayList"
    import="java.text.SimpleDateFormat"
    import="java.sql.*"%>
<%!
final String MSG_UPDATE_INFO = "「MST_DEVICEINFO」テーブルの「LastTxId(取引NO)」の更新に成功しました。";
final String MSG_UPDATE_ERR  = "「MST_DEVICEINFO」テーブルの「LastTxId(取引NO)」の更新に失敗しました。";
%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String g_storeID    = request.getParameter("s1");
    String g_terminalID = request.getParameter("t1");
    String g_companyID  = request.getParameter("r1");
    String g_UpFlg      = request.getParameter("UpFlg");
    if (g_UpFlg == null) {
        g_UpFlg = "select";
    }

    String user = ""; //ログインユーザー名
    //ログインユーザー名取得
	try {
	    user = request.getRemoteUser() != null ? request.getRemoteUser() : "";
	} catch (Exception e) {
	}

    ArrayList<ArrayList<String>> MstDeviceinfoLists = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> MstTerminalinfoLists = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> PrmDeviceAttributeLists = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> MstPrinterinfoLists = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> MstTillidinfoLists = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> PRMQueuebusterLinkLists = new ArrayList<ArrayList<String>>();

    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
    Connection connection = dbManager.getConnection();

    String errStr = "";
    String infoStr = "";
    if( ( null != g_storeID)
     && ( null != g_terminalID )
     && ( null != g_companyID )
     ){
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.StringBuilder sbTraining = new java.lang.StringBuilder();
        if ( "delete2".equals(g_UpFlg) ) {
            // Delete
            int rsDel = 0;
            String sqlStr =
                   "DELETE FROM RESMaster.dbo.AUT_DEVICES "
                   + "WHERE "
                   + "     CompanyId= " + "'" + g_companyID  + "'"
                   + " AND StoreId=   " + "'" + g_storeID    + "'"
                   + " AND TerminalId=" + "'" + g_terminalID + "'"
                   + ";"
                      ;
//out.print(sqlStr);

            PreparedStatement psDel = connection.prepareStatement(sqlStr);
            try {
                rsDel = psDel.executeUpdate();
                if( rsDel > 0) {
                    connection.commit();
                    infoStr = "0";
                }else {
                    errStr = "6";
                }
            } catch (Exception e) {
                e.printStackTrace();
                errStr = "1";
            } finally {
                psDel.close();

                // クローズ
                connection.close();
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

        } else if ( "delete".equals(g_UpFlg) ) {
            // Delete(AUT_DEVICES)
            int rsDel = 0;
            String sqlStr =
                   "DELETE FROM RESMaster.dbo.AUT_DEVICES "
                   + "WHERE "
                   + "     CompanyId= " + "'" + g_companyID  + "'"
                   + " AND StoreId=   " + "'" + g_storeID    + "'"
                   + " AND TerminalId=" + "'" + g_terminalID + "'"
                   + ";"
                   ;

            PreparedStatement psDel = connection.prepareStatement(sqlStr);
            try {
                rsDel = psDel.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                e.printStackTrace();
                connection.close();
                errStr = "1";
            } finally {
                psDel.close();
            }

           // Delete(Update)
           rsDel = 0;
           sqlStr =
                   "UPDATE RESMaster.dbo.MST_DEVICEINFO SET "
                   + "  Status=?                  "
                   + " ,DeleteFlag=?              "
                   + " ,DelDate=CURRENT_TIMESTAMP "
                   + " ,DelAppId=?                "
                   + " ,DelOpeCode=?              "
                   + "WHERE "
                   + "     CompanyId= " + "'" + g_companyID  + "'"
                   + " AND StoreId=   " + "'" + g_storeID    + "'"
                   + " AND TerminalId=" + "'" + g_terminalID + "'"
                   + "; "
                      ;

            sqlStr +=
                   "UPDATE RESMaster.dbo.MST_TERMINALINFO SET "
                   + "  DeleteFlag=?             "
                   + " ,DelDate=CURRENT_TIMESTAMP"
                   + " ,DelAppId=?               "
                   + " ,DelOpeCode=?             "
                   + "WHERE "
                   + "     CompanyId= " + "'" + g_companyID  + "'"
                   + " AND StoreId=   " + "'" + g_storeID    + "'"
                   + " AND TerminalId=" + "'" + g_terminalID + "'"
                   + ";"
                      ;

//out.print(sqlStr);

           PreparedStatement psIns = connection.prepareStatement(sqlStr);

           // MST_DEVICEINFOの設定
           psIns.setString(1, "Deleted");
           psIns.setString(2, "1");

           psIns.setString(3, "settingDevice");
           psIns.setString(4, user);

           // MST_TERMINALINFOの設定
           psIns.setString(5, "1");
           psIns.setString(6, "settingDevice");
           psIns.setString(7, user);

           try {
               rsDel = psIns.executeUpdate();
               if(rsDel > 0){
                   request.setAttribute("msg", "update successful");
               }else{
                   request.setAttribute("msg", "update successful");
               }
               connection.commit();
               infoStr = "1";
           } catch (Exception e) {
               errStr = "2";
           } finally {
               psIns.close();
               connection.close();
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

        } else if ( "Update2".equals(g_UpFlg) ) {
            // 再利用
            int rsUp = 0;
            /* MST_DEVICEINFO */
            String sqlStr =
                    "UPDATE RESMaster.dbo.MST_DEVICEINFO SET "
                    + "  Status     = 'Active'          "
                    + " ,DeleteFlag = '0'               "
                    + " ,UpdCount   = UpdCount+1        "
                    + " ,UpdDate    = CURRENT_TIMESTAMP "
                    + " ,UpdAppId   = ?                 "
                    + " ,UpdOpeCode = ?                 "
                    + "WHERE "
                    + "     CompanyId= " + "'" + g_companyID  + "'"
                    + " AND StoreId=   " + "'" + g_storeID    + "'"
                    + " AND TerminalId=" + "'" + g_terminalID + "'"
                    + " AND Training  = '0' "
                    + "; "
                       ;
               /* MST_DEVICEINFO(トレーニング) */
               sqlStr +=
                    "UPDATE RESMaster.dbo.MST_DEVICEINFO SET "
                    + "  Status     = 'Active'          "
                    + " ,DeleteFlag = '0'               "
                    + " ,UpdCount   = UpdCount+1        "
                    + " ,UpdDate    = CURRENT_TIMESTAMP "
                    + " ,UpdAppId   = ?                 "
                    + " ,UpdOpeCode = ?                 "
                    + "WHERE "
                    + "     CompanyId= " + "'" + g_companyID  + "'"
                    + " AND StoreId=   " + "'" + g_storeID    + "'"
                    + " AND TerminalId=" + "'" + g_terminalID + "'"
                    + " AND Training  = '1' "
                    + "; "
                       ;
             /* MST_TERMINALINFO */
             sqlStr +=
                    "UPDATE RESMaster.dbo.MST_TERMINALINFO SET "
                    + "  DeleteFlag = '0'               "
                    + " ,UpdCount   = UpdCount+1        "
                    + " ,UpdDate    = CURRENT_TIMESTAMP "
                    + " ,UpdAppId   = ?                 "
                    + " ,UpdOpeCode = ?                 "
                    + "WHERE "
                    + "     CompanyId= " + "'" + g_companyID  + "'"
                    + " AND StoreId=   " + "'" + g_storeID    + "'"
                    + " AND TerminalId=" + "'" + g_terminalID + "'"
                    + ";"
                       ;
 //out.print(sqlStr);

            PreparedStatement psUp = connection.prepareStatement(sqlStr);

            // MST_DEVICEINFOの設定
            psUp.setString(1, "settingDevice");
            psUp.setString(2, user);

            // MST_TERMINALINFO(トレーニング)の設定
            psUp.setString(3, "settingDevice");
            psUp.setString(4, user);

            // MST_TERMINALINFOの設定
            psUp.setString(5, "settingDevice");
            psUp.setString(6, user);

            try {
                rsUp = psUp.executeUpdate();
                connection.commit();
                infoStr = "3";
            } catch (Exception e) {
                errStr = "5";
            } finally {
                psUp.close();
                connection.close();
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
        } else if ( "Update".equals(g_UpFlg) ){
            // Update
            //connection.close();
            String s_msg ="DeviceUpdate.jsp?"
                    + "s1=" + g_storeID
                    + "&t1=" + g_terminalID
                    + "&r1="+  g_companyID
                    + "&y1="
                    + "&err="
                    + "&info="
                    ;
            response.sendRedirect(s_msg);

        } else if ( "Update3".equals(g_UpFlg) ){
            // Update3
            //connection.close();
            String s_msg ="DeviceLastTxId.jsp?"
                    + "s1=" + g_storeID
                    + "&t1=" + g_terminalID
                    + "&r1="+  g_companyID
                    + "&y1="
                    + "&err="
                    + "&info="
                    ;
            response.sendRedirect(s_msg);

        } else {
            // Select
            // SQL設定
            String sqlStr = "SELECT";
            // MST_DEVICEINFO
            sqlStr += "  deviceinfo.CompanyId       AS dev_CompanyId           ";
            sqlStr += " ,deviceinfo.StoreId         AS dev_StoreId             ";
            sqlStr += " ,deviceinfo.TerminalId      AS dev_TerminalId          ";
            sqlStr += " ,deviceinfo.Training        AS dev_Training            ";
            sqlStr += " ,deviceinfo.DeviceName      AS dev_DeviceName          ";
            sqlStr += " ,deviceinfo.AttributeId     AS dev_AttributeId         ";
            sqlStr += " ,deviceinfo.PrinterId       AS dev_PrinterId           ";
            sqlStr += " ,deviceinfo.TillId          AS dev_TillId              ";
            sqlStr += " ,deviceinfo.LinkQueueBuster AS dev_LinkQueueBuster     ";
            sqlStr += " ,deviceinfo.LastTxId        AS dev_LastTxId            ";
            sqlStr += " ,deviceinfo.Status          AS dev_Status              ";
            sqlStr += " ,deviceinfo.DeleteFlag      AS dev_DeleteFlag          ";
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
            sqlStr += " ,terminalinfo.SubCode1        AS ter_ReceiptCardInshiFilePath";
            // PRM_DEVICE_ATTRIBUTE
            sqlStr += " ,device_attribute.AttributeId    AS pda_AttributeId    ";
            sqlStr += " ,device_attribute.Description    AS pda_Description    ";
            sqlStr += " ,device_attribute.Printer        AS pda_Printer        ";
            sqlStr += " ,device_attribute.Till           AS pda_Till           ";
            sqlStr += " ,device_attribute.CreditTerminal AS pda_CreditTerminal ";
            sqlStr += " ,device_attribute.MSR            AS pda_MSR            ";
            sqlStr += " ,device_attribute.CashChanger    AS pda_CashChanger    ";
            sqlStr += " ,device_attribute.Attribute1     AS pda_Attribute1     ";
            sqlStr += " ,device_attribute.Attribute2     AS pda_Attribute2     ";
            sqlStr += " ,device_attribute.Attribute3     AS pda_Attribute3     ";
            sqlStr += " ,device_attribute.Attribute4     AS pda_Attribute4     ";
            sqlStr += " ,device_attribute.Attribute5     AS pda_Attribute5     ";
            sqlStr += " ,device_attribute.Attribute6     AS pda_Attribute6     ";
            sqlStr += " ,device_attribute.Attribute7     AS pda_Attribute7     ";
            sqlStr += " ,device_attribute.Attribute8     AS pda_Attribute8     ";
            sqlStr += " ,device_attribute.Attribute9     AS pda_Attribute9     ";
            sqlStr += " ,device_attribute.Attribute10    AS pda_Attribute10    ";
            sqlStr += " ,device_attribute.Attribute11    AS pda_Attribute11    ";
            sqlStr += " ,device_attribute.Attribute12    AS pda_Attribute12    ";
            sqlStr += " ,device_attribute.Attribute13    AS pda_Attribute13    ";
            // MST_PRINTERINFO
            sqlStr += " ,printinfo.CompanyId   AS mpi_CompanyId   ";
            sqlStr += " ,printinfo.StoreId     AS mpi_StoreId     ";
            sqlStr += " ,printinfo.PrinterId   AS mpi_PrinterId   ";
            sqlStr += " ,printinfo.PrinterName AS mpi_PrinterName ";
            sqlStr += " ,printinfo.IpAddress   AS mpi_IpAddress   ";
            sqlStr += " ,printinfo.PortNumTcp  AS mpi_PortNumTcp  ";
            sqlStr += " ,printinfo.Description AS mpi_Description ";
            sqlStr += " ,printinfo.DeleteFlag  AS mpi_DeleteFlag  ";
            sqlStr += " ,printinfo.Status      AS mpi_Status      ";
            // MST_TILLIDINFO
            sqlStr += " ,tillidinfo.CompanyId       AS mti_CompanyId       ";
            sqlStr += " ,tillidinfo.StoreId         AS mti_StoreId         ";
            sqlStr += " ,tillidinfo.TillId          AS mti_TillId          ";
            sqlStr += " ,tillidinfo.TerminalId      AS mti_TerminalId      ";
            sqlStr += " ,tillidinfo.BusinessDayDate AS mti_BusinessDayDate ";
            sqlStr += " ,tillidinfo.SodFlag         AS mti_SodFlag         ";
            sqlStr += " ,tillidinfo.EodFlag         AS mti_EodFlag         ";
            sqlStr += " ,tillidinfo.DeleteFlag      AS mti_DeleteFlag      ";
            // PRM_QUEUEBUSTER_LINK
            sqlStr += " ,queuebustr_link.CompanyId     AS pql_CompanyId ";
            sqlStr += " ,queuebustr_link.StoreId     AS pql_StoreId     ";
            sqlStr += " ,queuebustr_link.Id          AS pql_Id          ";
            sqlStr += " ,queuebustr_link.DisplayName AS pql_DisplayName ";
            sqlStr += " ,queuebustr_link.Status      AS pql_Status      ";
            // テーブル定義
            sqlStr += " FROM RESMaster.dbo.MST_DEVICEINFO deviceinfo";
            sqlStr += " LEFT JOIN RESMaster.dbo.MST_PRINTERINFO printinfo";
            sqlStr += " ON  deviceinfo.CompanyId = printinfo.CompanyId";
            sqlStr += " AND deviceinfo.StoreId = printinfo.StoreId";
            sqlStr += " AND deviceinfo.PrinterId = printinfo.PrinterId";
 //           sqlStr += " AND deviceinfo.Training = '0'";
//            // トレーニング
//            sqlStr += " LEFT JOIN RESMaster.dbo.MST_DEVICEINFO deviceinfo2";
//            sqlStr += " ON deviceinfo2.CompanyId = deviceinfo.CompanyId";
//            sqlStr += " AND deviceinfo2.StoreId = deviceinfo.StoreId";
//            sqlStr += " AND deviceinfo2.TerminalId = deviceinfo.TerminalId";
//            sqlStr += " AND deviceinfo2.Training = '1'";
            //MST_TERMINALINFO
            sqlStr += " LEFT JOIN RESMaster.dbo.MST_TERMINALINFO terminalinfo";
            sqlStr += " ON  deviceinfo.CompanyId  = terminalinfo.CompanyId  ";
            sqlStr += " AND deviceinfo.StoreId    = terminalinfo.StoreId    ";
            sqlStr += " AND deviceinfo.TerminalId = terminalinfo.TerminalId ";

            sqlStr += " LEFT JOIN RESMaster.dbo.MST_TILLIDINFO tillidinfo";
            sqlStr += " ON deviceinfo.CompanyId = tillidinfo.CompanyId";
            sqlStr += " AND deviceinfo.StoreId = tillidinfo.StoreId";
            sqlStr += " AND deviceinfo.TillId = tillidinfo.TillId";
            sqlStr += " LEFT JOIN RESMaster.dbo.PRM_QUEUEBUSTER_LINK queuebustr_link";
            sqlStr += " ON  deviceinfo.StoreId = queuebustr_link.StoreId";
            sqlStr += " AND deviceinfo.CompanyId = queuebustr_link.CompanyId";
            sqlStr += " AND deviceinfo.LinkQueueBuster = queuebustr_link.Id";
            sqlStr += " LEFT JOIN RESMaster.dbo.PRM_DEVICE_ATTRIBUTE device_attribute";
            sqlStr += " ON  deviceinfo.AttributeId = device_attribute.AttributeId";
            sqlStr += " LEFT JOIN RESMaster.dbo.AUT_DEVICES aut_devices";
            sqlStr += " ON  aut_devices.StoreId = deviceinfo.StoreId";
            sqlStr += " AND aut_devices.TerminalId = deviceinfo.TerminalId";
            sqlStr += " WHERE ";
            sqlStr += "     deviceinfo.StoreId    = LTRIM(RTRIM(?))";
            sqlStr += " AND deviceinfo.TerminalId = LTRIM(RTRIM(?))";
            sqlStr += " AND deviceinfo.CompanyId  = LTRIM(RTRIM(?))";
            sqlStr += " ORDER BY deviceinfo.Training";

            PreparedStatement ps = connection.prepareStatement(sqlStr);
            ps.setString(1, g_storeID   );
            ps.setString(2, g_terminalID);
            ps.setString(3, g_companyID );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ArrayList<String> MstDeviceinfoList = new ArrayList<String>();
                // MST_DEVICEINFO
                // List:0　～
                MstDeviceinfoList.add(rs.getString("dev_CompanyId"));
                MstDeviceinfoList.add(rs.getString("dev_StoreId"));
                MstDeviceinfoList.add(rs.getString("dev_TerminalId"));
                MstDeviceinfoList.add(rs.getString("dev_Training"));
                MstDeviceinfoList.add(rs.getString("dev_DeviceName"));
                MstDeviceinfoList.add(rs.getString("dev_AttributeId"));
                MstDeviceinfoList.add(rs.getString("dev_PrinterId"));
                MstDeviceinfoList.add(rs.getString("dev_TillId"));
                // ～List:8
                MstDeviceinfoList.add(rs.getString("dev_LinkQueueBuster"));
                MstDeviceinfoList.add(rs.getString("dev_LastTxId"));
                MstDeviceinfoList.add(rs.getString("dev_Status"));
                MstDeviceinfoList.add(rs.getString("dev_DeleteFlag"));

                // MST_TERMINALINFO
                ArrayList<String> MstTerminalinfoList = new ArrayList<String>();
                // List:0　～
                MstTerminalinfoList.add(rs.getString("ter_CompanyId"));
                MstTerminalinfoList.add(rs.getString("ter_StoreId"));
                MstTerminalinfoList.add(rs.getString("ter_TerminalId"));
                MstTerminalinfoList.add(rs.getString("ter_FloorId"));
                MstTerminalinfoList.add(rs.getString("ter_TerminalName"));
                MstTerminalinfoList.add(rs.getString("ter_IPAddress"));
                MstTerminalinfoList.add(rs.getString("ter_StoreClass"));
                MstTerminalinfoList.add(rs.getString("ter_TerminalType"));
                MstTerminalinfoList.add(rs.getString("ter_TillType"));
                // ～List:9
                MstTerminalinfoList.add(rs.getString("ter_RelationType"));
                MstTerminalinfoList.add(rs.getString("ter_LogoFileName"));
                MstTerminalinfoList.add(rs.getString("ter_InshiFileName"));
                MstTerminalinfoList.add(rs.getString("ter_SalesPromotionBMPPath"));
                MstTerminalinfoList.add(rs.getString("ter_ReceiptCardInshiFilePath"));

                // PRM_DEVICE_ATTRIBUTE
                ArrayList<String> PrmDeviceAttributeList = new ArrayList<String>();
                // List:0　～
                PrmDeviceAttributeList.add(rs.getString("pda_AttributeId"));
                PrmDeviceAttributeList.add(rs.getString("pda_Description"));
                PrmDeviceAttributeList.add(rs.getString("pda_Printer"));
                PrmDeviceAttributeList.add(rs.getString("pda_Till"));
                PrmDeviceAttributeList.add(rs.getString("pda_CreditTerminal"));
                PrmDeviceAttributeList.add(rs.getString("pda_MSR"));
                PrmDeviceAttributeList.add(rs.getString("pda_CashChanger"));
                // List:7
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute1"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute2"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute3"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute4"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute5"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute6"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute7"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute8"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute9"));
                // ～List:16
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute10"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute11"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute12"));
                PrmDeviceAttributeList.add(rs.getString("pda_Attribute13"));

                // MST_PRINTERINFO
                ArrayList<String> MstPrinterinfoList = new ArrayList<String>();
                // List:0　～
                MstPrinterinfoList.add(rs.getString("mpi_CompanyId"));
                MstPrinterinfoList.add(rs.getString("mpi_StoreId"));
                MstPrinterinfoList.add(rs.getString("mpi_PrinterId"));
                MstPrinterinfoList.add(rs.getString("mpi_PrinterName"));
                MstPrinterinfoList.add(rs.getString("mpi_Description"));
                MstPrinterinfoList.add(rs.getString("mpi_IpAddress"));
                // ～List:6
                MstPrinterinfoList.add(rs.getString("mpi_DeleteFlag"));
                MstPrinterinfoList.add(rs.getString("mpi_Status"));
                MstPrinterinfoList.add(rs.getString("mpi_PortNumTcp"));

                // MST_TILLIDINFO
                ArrayList<String> MstTillidinfoList = new ArrayList<String>();
                // List:0　～
                MstTillidinfoList.add(rs.getString("mti_CompanyId"));
                MstTillidinfoList.add(rs.getString("mti_StoreId"));
                MstTillidinfoList.add(rs.getString("mti_TillId"));
                MstTillidinfoList.add(rs.getString("mti_TerminalId"));
                MstTillidinfoList.add(rs.getString("mti_BusinessDayDate"));
                MstTillidinfoList.add(rs.getString("mti_SodFlag"));
                MstTillidinfoList.add(rs.getString("mti_EodFlag"));
                // ～List:7
                MstTillidinfoList.add(rs.getString("mti_DeleteFlag"));

                // PRM_QUEUEBUSTER_LINK
                // List:0　～
                ArrayList<String> PRMQueuebusterLinkList = new ArrayList<String>();
                PRMQueuebusterLinkList.add(rs.getString("pql_StoreId"));
                PRMQueuebusterLinkList.add(rs.getString("pql_Id"));
                PRMQueuebusterLinkList.add(rs.getString("pql_DisplayName"));
                // ～List:3
                PRMQueuebusterLinkList.add(rs.getString("pql_Status"));


                MstDeviceinfoLists.add(MstDeviceinfoList);
                MstTerminalinfoLists.add(MstTerminalinfoList);
                PrmDeviceAttributeLists.add(PrmDeviceAttributeList);
                MstPrinterinfoLists.add(MstPrinterinfoList);
                MstTillidinfoLists.add(MstTillidinfoList);
                PRMQueuebusterLinkLists.add(PRMQueuebusterLinkList);
            }

            rs.close();
            ps.close();
            connection.close();

        // 起動時
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="./css/default.css">
    <title>NCR RES DeviceDetail Viewer</title>
    <!-- jQuery -->
    <script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
    <!-- ダイアログ共通 -->
    <script type="text/javascript" src="./js/DialogMessage.js"></script>
</head>
<body class="res-maincontent">
<form action="DeviceDetail.jsp" method="post" id="updateform" onsubmit="return false;">

<input type="hidden" name="add_dev_CompanyId" id="add_dev_CompanyId" value="<%=request.getParameter("dev_CompanyId")%>">
<input type="hidden" name="add_dev_StoreId" id="add_dev_StoreId" value="<%=request.getParameter("dev_StoreId")%>">
<input type="hidden" name="add_dev_TerminalId" id="add_dev_TerminalId" value="<%=request.getParameter("dev_TerminalId")%>">
<input type="hidden" name="UpFlg" id="UpFlg" value="">
<input type="hidden" name="s1" id="s1" value="<%=request.getParameter("s1")%>">
<input type="hidden" name="t1" id="t1" value="<%=request.getParameter("t1")%>">
<input type="hidden" name="r1" id="r1" value="<%=request.getParameter("r1")%>">
<input type="hidden" name="err" id="err" value="<%=request.getParameter("err")%>">
<input type="hidden" name="info" id="info" value="<%=request.getParameter("info")%>">

        <div class="table-scroll-area-v table-scroll-area-h" id="tabletable" style="height:780px">
            <%
            String str_info = request.getParameter("info");
            String str_err  = request.getParameter("err");

            if ( "".equals(str_info) != true && "".equals(str_err) != true){
            }else{
                out.print("<div " + "id=\"" + "panel\"" + "><tr><td>");
                if ("1".equals(str_info)){
                    // 成功
                out.print(" <label class=\"" + "res-info-msg\"");
                out.print(" id=\"" + "infoMsg\"");
                    out.print(">" + MSG_UPDATE_INFO);
                }else{
                    // 異常
                    out.print(" <label class=\"" + "res-err-msg\"");
                    out.print(" id=\"" + "infoMsg\"");
                    out.print(">" + MSG_UPDATE_ERR);
                }
                out.print("</label>");
                out.print("</td></tr></div>");
            }
            %>
            <table class="res-tbl" style="width:100%">
                <tr>
                    <th style="width: 50%" colspan="2">MST_DEVICEINFO 項目</th>
                    <th style="width: 25%">設定値</th>
                    <th style="width: 25%">設定値(トレーニング用)</th>
                </tr>
                <tr>
                    <td colspan="2">企業番号(CompanyId)</td>
                    <td><input type="text" id="dev_CompanyId" name="dev_CompanyId" disabled style="width: 100%" value="<%=MstDeviceinfoLists.get(0).get(0)%>"/></td>
                    <td><input type="text" id="tra_CompanyId" name="tra_CompanyId" disabled style="width: 100%" value="<%=MstDeviceinfoLists.get(1).get(0)%>"/></td>
                </tr>
                <tr>
                    <td colspan="2">店番号(StoreID)</td>
                    <td><input type="text" id="dev_StoreId" name="dev_StoreId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(1)%>" /></td>
                    <td><input type="text" id="tra_StoreId" name="tra_StoreId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(1)%>" /></td>
                </tr>
                <tr>
                    <td colspan="2">端末番号(TerminalId)</td>
                    <td><input type="text" id="dev_TerminalId" name="dev_TerminalId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(2)%>" /></td>
                    <td><input type="text" id="tra_TerminalId" name="tra_TerminalId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(2)%>" /></td>
                </tr>
<!--
                <tr>
                    <td colspan="2">トレーニング(Training)</td>
                    <td><input type="text" id="dev_Training" name="dev_Training" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(3)%>" /></td>
                    <td><input type="text" id="tra_Training" name="tra_Training" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(3)%>" /></td>
                </tr>
 -->
                <tr>
                    <td colspan="2" class="orangetd">端末名(DeviceName)</td>
                    <td><input type="text" id="dev_DeviceName" name="dev_DeviceName" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(4)%>"></td>
                    <td><input type="text" id="tra_DeviceName" name="tra_DeviceName" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(4)%>"></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性番号(AttributeId)</td>
                    <td><input type="text" id="dev_AttributeId" name="dev_AttributeId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(5)%>"></td>
                    <td><input type="text" id="tra_AttributeId" name="tra_AttributeId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(5)%>"></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">プリンターID(PrinterId)</td>
                    <td><input type="text" id="dev_PrinterId" name="dev_PrinterId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(6)%>"></td>
                    <td><input type="text" id="tra_PrinterId" name="tra_PrinterId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(6)%>"></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ドロワーID(TillId)</td>
                    <td><input type="text" id="dev_TillId" name="dev_TillId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(7)%>"></td>
                    <td><input type="text" id="tra_TillId" name="tra_TillId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(7)%>"></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">キューID(LinkQueueBuster)</td>
                    <td><input type="text" id="dev_LinkQueueBuster" name="dev_LinkQueueBuster" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(8)%>"></td>
                    <td><input type="text" id="tra_LinkQueueBuster" name="tra_LinkQueueBuster" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(8)%>"></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">取引NO(LastTxId)</td>
                    <td><input type="text" id="dev_LastTxId" name="dev_LastTxId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(0).get(9)!= null ? MstDeviceinfoLists.get(0).get(9) : 0 %>"></td>
                    <td><input type="text" id="tra_LastTxId" name="tra_LastTxId" disabled style="width: 100%" value="<%= MstDeviceinfoLists.get(1).get(9)!= null ? MstDeviceinfoLists.get(1).get(9) : 0%>"></td>
                </tr>

                <tr>
                    <th style="width: 50%" colspan="2">MST_TERMINALINFO 項目</th>
                    <th style="width: 50%" colspan="2">設定値</th>
                </tr>
                <tr style="display:none">
                    <td colspan="2" class="orangetd">企業番号（CompanyId）</td>
                    <td colspan="2">
                    <input type="text" id="ter_CompanyId" name="ter_CompanyId" value="<%=MstTerminalinfoLists.get(0).get(0)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr style="display:none">
                    <td colspan="2" class="orangetd">店番号（StoreID）</td>
                    <td colspan="2">
                    <input type="text" id="ter_StoreId" name="ter_StoreId" value="<%=MstTerminalinfoLists.get(0).get(1)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr style="display:none">
                    <td colspan="2" class="orangetd">端末番号(TerminalId)</td>
                    <td colspan="2">
                    <input type="text" id="ter_TerminalId" name="ter_TerminalId" value="<%=MstTerminalinfoLists.get(0).get(2)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">フロアID(FloorId)</td>
                    <td colspan="2">
                    <input type="text" id="ter_FloorId" name="ter_FloorId" value="<%=MstTerminalinfoLists.get(0).get(3)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">端末名(TerminalName)</td>
                    <td colspan="2">
                    <input type="text" id="ter_TerminalName" name="ter_TerminalName" value="<%=MstTerminalinfoLists.get(0).get(4)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">IPアドレス(IpAddress)</td>
                    <td colspan="2">
                    <input type="text" id="ter_IPAddress" name="ter_IPAddress" value="<%=MstTerminalinfoLists.get(0).get(5)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">店舗種別(StoreClass)</td>
                    <td colspan="2">
                    <input type="text" id="ter_StoreClass" name="ter_StoreClass" value="<%=MstTerminalinfoLists.get(0).get(6)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">端末タイプ(TerminalType)</td>
                    <td colspan="2">
                    <input type="text" id="ter_TerminalType" name="ter_TerminalType" value="<%=MstTerminalinfoLists.get(0).get(7)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ドロワータイプ(TillType)</td>
                    <td colspan="2">
                    <input type="text" id="ter_TillType" name="ter_TillType" value="<%=MstTerminalinfoLists.get(0).get(8)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">関連タイプ(RelationType)</td>
                    <td colspan="2">
                    <input type="text" id="ter_RelationType" name="ter_RelationType" value="<%=MstTerminalinfoLists.get(0).get(9)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ロゴファイルパス(LogoFilePath)</td>
                    <td colspan="2">
                    <input type="text" id="ter_LogoFilePath" name="ter_LogoFilePath" value="<%=MstTerminalinfoLists.get(0).get(10)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">印紙ファイルパス(InshFilePath)</td>
                    <td colspan="2">
                    <input type="text" id="ter_InshFilePath" name="ter_InshFilePath" value="<%=MstTerminalinfoLists.get(0).get(11)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">領収証印紙ファイルパス(SubCode1)</td>
                    <td colspan="2">
                    <input type="text" id="ter_ReceiptCardInshiFilePath" name="ter_ReceiptCardInshiFilePath" value="<%=MstTerminalinfoLists.get(0).get(13)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">販促ビットマップファイルパス(SubCode2)</td>
                    <td colspan="2">
                        <input type="text" id="ter_SalesPromotionBMPPath" name="ter_SalesPromotionBMPPath" value="<%=MstTerminalinfoLists.get(0).get(12)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <th style="width: 50%" colspan="2">PRM_DEVICE_ATTRIBUTE 項目</th>
                    <th style="width: 25%">設定値</th>
                    <th style="width: 25%">設定値(トレーニング用)</th>
                </tr>
<!--
                <tr>
                    <td colspan="2" class="orangetd">属性番号(AttributeId)</td>
                    <td colspan="2">
                    <input type="text" id="pda_AttributeId" name="pda_AttributeId" value="<%=PrmDeviceAttributeLists.get(0).get(0)%>"  disabled style="width: 100%" ></td>
                </tr>
-->
                <tr>
                    <td colspan="2" class="orangetd">属性説明(Description)</td>
                    <td><input type="text" id="pda_Description" name="pda_Description" value="<%=PrmDeviceAttributeLists.get(0).get(1)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Description" name="tra_pda_Description" value="<%=PrmDeviceAttributeLists.get(1).get(1)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">プリンター(Printer)</td>
                    <td><input type="text" id="pda_Printer" name="pda_Printer" value="<%=PrmDeviceAttributeLists.get(0).get(2)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Printer" name="tra_pda_Printer" value="<%=PrmDeviceAttributeLists.get(1).get(2)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ドロワー(Till)</td>
                    <td><input type="text" id="pda_Till" name="pda_Till" value="<%=PrmDeviceAttributeLists.get(0).get(3)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Till" name="tra_pda_Till" value="<%=PrmDeviceAttributeLists.get(1).get(3)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">クレジット(CreditTerminal)</td>
                    <td><input type="text" id="pda_CreditTerminal" name="pda_CreditTerminal" value="<%=PrmDeviceAttributeLists.get(0).get(4)%>" disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_CreditTerminal" name="tra_pda_CreditTerminal" value="<%=PrmDeviceAttributeLists.get(1).get(4)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">MSR(MSR)</td>
                    <td><input type="text" id="pda_MSR" name="pda_MSR" value="<%=PrmDeviceAttributeLists.get(0).get(5)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_MSR" name="tra_pda_MSR" value="<%=PrmDeviceAttributeLists.get(1).get(5)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">釣銭機(CashChanger)</td>
                    <td><input type="text" id="pda_CashChanger" name="pda_CashChanger" value="<%=PrmDeviceAttributeLists.get(0).get(6)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_CashChanger" name="tra_pda_CashChanger" value="<%=PrmDeviceAttributeLists.get(1).get(6)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性１(Attribute1)</td>
                    <td><input type="text" id="pda_Attribute1" name="pda_Attribute1" value="<%=PrmDeviceAttributeLists.get(0).get(7)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute1" name="tra_pda_Attribute1" value="<%=PrmDeviceAttributeLists.get(1).get(7)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性２(Attribute2)</td>
                    <td><input type="text" id="pda_Attribute2" name="pda_Attribute2" value="<%=PrmDeviceAttributeLists.get(0).get(8)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute2" name="tra_pda_Attribute2" value="<%=PrmDeviceAttributeLists.get(1).get(8)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性３(Attribute3)</td>
                    <td><input type="text" id="pda_Attribute3" name="pda_Attribute3" value="<%=PrmDeviceAttributeLists.get(0).get(9)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute3" name="tra_pda_Attribute3" value="<%=PrmDeviceAttributeLists.get(1).get(9)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性４(Attribute4)</td>
                    <td><input type="text" id="pda_Attribute4" name="pda_Attribute4" value="<%=PrmDeviceAttributeLists.get(0).get(10)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute4" name="tra_pda_Attribute4" value="<%=PrmDeviceAttributeLists.get(1).get(10)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性５(Attribute5)</td>
                    <td><input type="text" id="pda_Attribute5" name="pda_Attribute5" value="<%=PrmDeviceAttributeLists.get(0).get(11)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute5" name="tra_pda_Attribute5" value="<%=PrmDeviceAttributeLists.get(1).get(11)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性６(Attribute6)</td>
                    <td><input type="text" id="pda_Attribute6" name="pda_Attribute6" value="<%=PrmDeviceAttributeLists.get(0).get(12)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute6" name="tra_pda_Attribute6" value="<%=PrmDeviceAttributeLists.get(1).get(12)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性７(Attribute7)</td>
                    <td><input type="text" id="pda_Attribute7" name="pda_Attribute7" value="<%=PrmDeviceAttributeLists.get(0).get(13)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute7" name="tra_pda_Attribute7" value="<%=PrmDeviceAttributeLists.get(1).get(13)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性８(Attribute8)</td>
                    <td><input type="text" id="pda_Attribute8" name="pda_Attribute8" value="<%=PrmDeviceAttributeLists.get(0).get(14)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute4" name="tra_pda_Attribute4" value="<%=PrmDeviceAttributeLists.get(1).get(14)%>"  disabled style="width: 100%" ></td>
                </tr>
				<tr>
                    <td colspan="2" class="orangetd">属性９(Attribute9)</td>
                    <td><input type="text" id="pda_Attribute9" name="pda_Attribute9" value="<%=PrmDeviceAttributeLists.get(0).get(15)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute9" name="tra_pda_Attribute9" value="<%=PrmDeviceAttributeLists.get(1).get(15)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性１０(Attribute10)</td>
                    <td><input type="text" id="pda_Attribute10" name="pda_Attribute10" value="<%=PrmDeviceAttributeLists.get(0).get(16)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute10" name="tra_pda_Attribute10" value="<%=PrmDeviceAttributeLists.get(1).get(16)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性１１(Attribute11)</td>
                    <td><input type="text" id="pda_Attribute11" name="pda_Attribute11" value="<%=PrmDeviceAttributeLists.get(0).get(17)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute11" name="tra_pda_Attribute11" value="<%=PrmDeviceAttributeLists.get(1).get(17)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性１２(Attribute12)</td>
                    <td><input type="text" id="pda_Attribute12" name="pda_Attribute12" value="<%=PrmDeviceAttributeLists.get(0).get(18)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute12" name="tra_pda_Attribute12" value="<%=PrmDeviceAttributeLists.get(1).get(18)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">属性１３(Attribute13)</td>
                    <td><input type="text" id="pda_Attribute13" name="pda_Attribute13" value="<%=PrmDeviceAttributeLists.get(0).get(19)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pda_Attribute13" name="tra_pda_Attribute13" value="<%=PrmDeviceAttributeLists.get(1).get(19)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <th style="width: 50%" colspan="2">MST_PRINTERINFO 項目</th>
                    <th style="width: 25%">設定値</th>
                    <th style="width: 25%">設定値(トレーニング用)</th>
                </tr>
<!--
                <tr>
                    <td colspan="2" class="orangetd">会社番号(CompanyId)</td>
                    <td colspan="2">
                    <input type="text" id="mpi_CompanyId" name="mpi_CompanyId" value="<%=MstPrinterinfoLists.get(0).get(0)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">店舗番号(StoreID)</td>
                    <td colspan="2">
                    <input type="text" id="mpi_StoreId" name="mpi_StoreId" value="<%= MstPrinterinfoLists.get(0).get(1)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">プリンターID(PrinterId)</td>
                    <td colspan="2">
                    <input type="text" id="mpi_PrinterId" name="mpi_PrinterId" value="<%= MstPrinterinfoLists.get(0).get(2)%>"  disabled style="width: 100%" ></td>
                </tr>
-->
                <tr>
                    <td colspan="2" class="orangetd">プリンター名(PrinterName)</td>
                    <td><input type="text" id="mpi_PrinterName" name="mpi_PrinterName" value="<%= MstPrinterinfoLists.get(0).get(3)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_mpi_PrinterName" name="tra_mpi_PrinterName" value="<%= MstPrinterinfoLists.get(1).get(3)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">プリンター説明(Description)</td>
                    <td><input type="text" id="mpi_Description" name="mpi_Description" value="<%= MstPrinterinfoLists.get(0).get(4)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_mpi_Description" name="tra_mpi_Description" value="<%= MstPrinterinfoLists.get(1).get(4)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">プリンターIPアドレス(PrinterIPAddress)</td>
                    <td><input type="text" id="mpi_IpAddress" name="mpi_IpAddress" value="<%= MstPrinterinfoLists.get(0).get(5)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_mpi_IpAddress" name="tra_mpi_IpAddress" value="<%= MstPrinterinfoLists.get(1).get(5)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">プリンターTCPポート番号(PortNumTcp)</td>
                    <td><input type="text" id="mpi_PortNumTcp" name="mpi_PortNumTcp" value="<%= MstPrinterinfoLists.get(0).get(8)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_mpi_PortNumTcp" name="tra_mpi_PortNumTcp" value="<%= MstPrinterinfoLists.get(1).get(8)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ステータス(Status)</td>
                    <td>
                    <%
                    String mpi_DeleteFlag = MstPrinterinfoLists.get(0).get(6);
                    String mpi_Status = MstPrinterinfoLists.get(0).get(7);
                    out.print("<input type=\"" + "text\"");
                    out.print(" id=\"" + "mpi_Status\"");
                    out.print(" name=\"" + "mpi_Status\"");
                    if ("1".equals(mpi_DeleteFlag) || "Deleted".equals(mpi_Status)){
                        // 無効
                        out.print(" value=\"" + "Deleted\"");
                        out.print(" disabled style=\"" + "width: 100%;");
                        out.print(" color:hsl(0,100%,50%)\"");
                    }else{
                        // 有効
                        out.print(" disabled style=\"" + "width: 100%\"");
                    }
                    out.print(" >");
                    %>
                    </td>
                    <td>
                    <%
                    String tra_mpi_DeleteFlag = MstPrinterinfoLists.get(1).get(6);
                    String tra_mpi_Status = MstPrinterinfoLists.get(1).get(7);
                    out.print("<input type=\"" + "text\"");
                    out.print(" id=\"" + "tra_mpi_Status\"");
                    out.print(" name=\"" + "tra_mpi_Status\"");
                    if ("1".equals(tra_mpi_DeleteFlag) || "Deleted".equals(tra_mpi_Status)){
                        // 無効
                        out.print(" value=\"" + "Deleted\"");
                        out.print(" disabled style=\"" + "width: 100%;");
                        out.print(" color:hsl(0,100%,50%)\"");
                    }else{
                        // 有効
                        out.print(" disabled style=\"" + "width: 100%\"");
                    }
                    out.print(" >");
                    %>
                    </td>
                </tr>
                <tr>
                    <th style="width: 50%" colspan="2">MST_TILLIDINFO 項目</th>
                    <th style="width: 25%">設定値</th>
                    <th style="width: 25%">設定値(トレーニング用)</th>
                </tr>
<!--
                <tr>
                    <td colspan="2" class="orangetd">会社番号(CompanyId)</td>
                    <td colspan="2">
                    <input type="text" id="mti_CompanyId" name="mti_CompanyId" value="<%= MstTillidinfoLists.get(0).get(0)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">店舗番号(StoreID)</td>
                    <td colspan="2">
                    <input type="text" id="mti_StoreId" name="mti_StoreId" value="<%= MstTillidinfoLists.get(0).get(1)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ドロワーID(TillId)</td>
                    <td colspan="2">
                    <input type="text" id="mti_TillId" name="mti_TillId" value="<%= MstTillidinfoLists.get(0).get(2)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">端末番号(TerminalId)</td>
                    <td><input type="text" id="mti_TillId" name="mti_TillId" value="<%= MstTillidinfoLists.get(0).get(3)%>"  disabled style="width: 100%" ></td>
                </tr>
-->
                <tr>
                    <td colspan="2" class="orangetd">稼働営業日(BusinessDayDate)</td>
                    <td><input type="text" id="mti_BusinessDayDate" name="mti_BusinessDayDate" value="<%= MstTillidinfoLists.get(0).get(4)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_mti_BusinessDayDate" name="tra_mti_BusinessDayDate" value="<%= MstTillidinfoLists.get(1).get(4)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">開設(SodFlag)</td>
                    <td><input type="text" id="mti_SodFlag" name="mti_SodFlag" value="<%=MstTillidinfoLists.get(0).get(5)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_mti_SodFlag" name="tra_mti_SodFlag" value="<%=MstTillidinfoLists.get(1).get(5)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">閉設(EodFlag)</td>
                    <td><input type="text" id="mti_EodFlag" name="mti_EodFlag" value="<%=MstTillidinfoLists.get(0).get(6)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_mti_EodFlag" name="tra_mti_EodFlag" value="<%=MstTillidinfoLists.get(1).get(6)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ステータス(Status)</td>
                    <td>
                    <%
                    String mti_DeleteFlag = MstTillidinfoLists.get(0).get(7);
                    out.print("<input type=\"" + "text\"");
                    out.print(" id=\"" + "mpi_DeleteFlag\"");
                    out.print(" name=\"" + "mti_DeleteFlag\"");
                    if ("1".equals(mti_DeleteFlag)){
                        // 無効
                        out.print(" value=\"" + "Deleted\"");
                        out.print(" disabled style=\"" + "width: 100%;");
                        out.print(" color:hsl(0,100%,50%)\"");
                    }else{
                        // 有効
                        out.print(" disabled style=\"" + "width: 100%\"");
                    }
                    out.print(" >");
                    %>
                    </td>
                    <td>
                    <%
                    String tra_mti_DeleteFlag = MstTillidinfoLists.get(1).get(7);
                    out.print("<input type=\"" + "text\"");
                    out.print(" id=\"" + "tra_mpi_DeleteFlag\"");
                    out.print(" name=\"" + "tra_mti_DeleteFlag\"");
                    if ("1".equals(tra_mti_DeleteFlag)){
                        // 無効
                        out.print(" value=\"" + "Deleted\"");
                        out.print(" disabled style=\"" + "width: 100%;");
                        out.print(" color:hsl(0,100%,50%)\"");
                    }else{
                        // 有効
                        out.print(" disabled style=\"" + "width: 100%\"");
                    }
                    out.print(" >");
                    %>
                    </td>
                </tr>

                <tr>
                    <th style="width: 50%" colspan="2">PRM_QUEUEBUSTER_LINK 項目</th>
                    <th style="width: 25%">設定値</th>
                    <th style="width: 25%">設定値(トレーニング用)</th>
                </tr>
<!--
                <tr>
                    <td colspan="2" class="orangetd">店舗番号(StoreID)</td>
                    <td colspan="2">
                    <input type="text" id="pql_StoreId" name="pql_StoreId" value="<%=PRMQueuebusterLinkLists.get(0).get(0)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ID(ID)</td>
                    <td colspan="2">
                    <input type="text" id="pql_Id" name="pql_Id" value="<%= PRMQueuebusterLinkLists.get(0).get(1)%>"  disabled style="width: 100%" ></td>
                </tr>
-->
                <tr>
                    <td colspan="2" class="orangetd">キュー名称(DisplayName)</td>
                    <td><input type="text" id="pql_DisplayName" name="pql_DisplayName" value="<%=PRMQueuebusterLinkLists.get(0).get(2)%>"  disabled style="width: 100%" ></td>
                    <td><input type="text" id="tra_pql_DisplayName" name="tra_pql_DisplayName" value="<%=PRMQueuebusterLinkLists.get(1).get(2)%>"  disabled style="width: 100%" ></td>
                </tr>
                <tr>
                    <td colspan="2" class="orangetd">ステータス(Status)</td>
                    <td>
                    <%
                    String pql_Status = PRMQueuebusterLinkLists.get(0).get(3);
                    out.print("<input type=\"" + "text\"");
                    out.print(" id=\"" + "pql_Status\"");
                    out.print(" name=\"" + "pql_Status\"");
                    if ("Deleted".equals(pql_Status)){
                        // 無効
                        out.print(" value=\"" + pql_Status + "\"");
                        out.print(" disabled style=\"" + "width: 100%;");
                        out.print(" color:hsl(0,100%,50%)\"");
                    }else{
                        // 有効
                        out.print(" disabled style=\"" + "width: 100%\"");
                    }
                    out.print(" >");
                    %>
                    </td>
                    <td>
                    <%
                    String tra_pql_Status = PRMQueuebusterLinkLists.get(1).get(3);
                    out.print("<input type=\"" + "text\"");
                    out.print(" id=\"" + "tra_pql_Status\"");
                    out.print(" name=\"" + "tra_pql_Status\"");
                    if ("Deleted".equals(tra_pql_Status)){
                        // 無効
                        out.print(" disabled style=\"" + "width: 100%;");
                        out.print(" color:hsl(0,100%,50%)\"");
                        out.print(" value=\"" + tra_pql_Status + "\"");
                    }else{
                        // 有効
                        out.print(" disabled style=\"" + "width: 100%\"");
                    }
                    out.print(" >");
                    %>
                    </td>
                </tr>
            </table>
        </div>

        <div align="right">
          <table>
            <tr>
              <%
              String dev_Status = MstDeviceinfoLists.get(0).get(10);
              String dev_DeleteFlag = MstDeviceinfoLists.get(0).get(11);
              String tra_Status = MstDeviceinfoLists.get(1).get(10);
              String tra_DeleteFlag = MstDeviceinfoLists.get(1).get(11);
              if (!( "Deleted".equals(dev_Status)
                  || "Deleted".equals(tra_Status)
                  || "1".equals(dev_DeleteFlag)
                  || "1".equals(tra_DeleteFlag)
              )){
                  // 取引NO更新
                  out.print("<td style=\"" + "padding:10px;\"" + ">");
                  out.print("<input type=\"" + "button\"");
                  out.print(" value=\"" + "取引NO更新\"");
                  out.print(" onclick=\"" + "Update3(this)\"");
                  out.print(" class=\"" + "res-big-green\"");
                  out.print(" style=\"" + "width:150px\"");
                  out.print(" >");
                  out.print("</td>");
                  // 削除(AUT_DEVICES)
                  out.print("<td style=\"" + "padding:10px;\"" + ">");
                  out.print("<input type=\"" + "button\"");
                  out.print(" value=\"" + "削除(AUT_DEVICES)\"");
                  out.print(" id=\"" + "Delete2\"");
                  out.print(" class=\"" + "res-big-yellow\"");
                  out.print(" style=\"" + "width:220px\"");
                  out.print(" >");
                  out.print("</td>");
                  // 削除
                  out.print("<td style=\"" + "padding:10px;\"" + ">");
                  out.print("<input type=\"" + "button\"");
                  out.print(" value=\"" + "削除\"");
                  out.print(" id=\"" + "Delete\"");
                  out.print(" class=\"" + "res-big-yellow\"");
                  out.print(" >");
                  out.print("</td>");
                  // 更新画面へ
                  out.print("<td style=\"" + "padding:10px;\"" + ">");
                  out.print("<input type=\"" + "button\"");
                  out.print(" value=\"" + "更新画面へ\"");
                  out.print(" onclick=\"" + "Update(this)\"");
                  out.print(" class=\"" + "res-big-green\"");
                  out.print(" style=\"" + "width:200px\"");
                  out.print(" >");
                  out.print("</td>");
              }else{
                  // 削除（再利用）
                  out.print("<td style=\"" + "padding:10px;\"" + ">");
                  out.print("<input type=\"" + "button\"");
                  out.print(" value=\"" + "再利用\"");
                  out.print(" id=\"" + "Update2\"");
                  out.print(" class=\"" + "res-big-yellow\"");
                  out.print(" >");
                  out.print("</td>");
              }
              %>
            </tr>
          </table>
        </div>
    </form>
</body>

<script type="text/javascript">

function Update(obj) {
    var myform = document.getElementById('updateform');
    document.getElementById("UpFlg").value = "Update"
    // 画面遷移を行う
    myform.submit();
}

function Update3(obj) {
    var myform = document.getElementById('updateform');
    document.getElementById("UpFlg").value = "Update3"
    // 画面遷移を行う
    myform.submit();
}

(function() {
    var showLog = function() {
    };
})();

jQuery(function ($) {

    // ボタン・クリック時にダイアログを開く
    $('#Update2').click(function(e){
      showDialog(
            "タイトル：未使用",
            "再利用を実行します。",
            ButtonYesNo,
            function() {
                //「いいえ」を押したときの処理
            },
            function() {
                //「はい」を押したときの処理
                // 処理
                var myform = document.getElementById('updateform');
                document.getElementById("UpFlg").value = "Update2";
                myform.submit();
            }
        );
    });
    // ボタン・クリック時にダイアログを開く
    $('#Delete').click(function(e){
      showDialog(
            "タイトル：未使用",
            "削除を実行します。",
            ButtonYesNo,
            function() {
                //「いいえ」を押したときの処理
            },
            function() {
                //「はい」を押したときの処理
                // 処理
                var myform = document.getElementById('updateform');
                document.getElementById("UpFlg").value = "delete";
                myform.submit();
            }
        );
    });
    // ボタン・クリック時にダイアログを開く
    $('#Delete2').click(function(e){
      showDialog(
            "タイトル：未使用",
            "【AUT_DEVICES】レコードの削除を実行します。",
            ButtonYesNo,
            function() {
                //「いいえ」を押したときの処理
            },
            function() {
                //「はい」を押したときの処理
                // 処理
                var myform = document.getElementById('updateform');
                document.getElementById("UpFlg").value = "delete2";
                myform.submit();
            }
        );
    });
});

</script>
<HEAD>
<meta http-equiv=”Pragma” content=”no-cache”>
<meta http-equiv=”Cache-Control” content=”no-cache”>
</HEAD>
</html>
<%
    }
}
%>