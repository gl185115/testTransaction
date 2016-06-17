<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.ArrayList"
    import="java.sql.*"%>
<%!
final String MSG_UPDATE_INFO = "更新を実行します。";
final String MSG_UPDATE_ERR  = "「取引NO」の値が不適切ですが更新処理を行いますか。";
final String MSG_MAX_ERR     = "「取引NO」の値が入力可能な最大値を超えるため、更新を行うことが出来ません。システム担当者に確認してください。";
%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String g_storeID    = request.getParameter("s1");
    String g_terminalID = request.getParameter("t1");
    String g_companyID  = request.getParameter("r1");
    String g_UpFlg      = request.getParameter("UpFlg");
    //ログインユーザー名
    String user         = "";
    if (g_UpFlg == null) {
        g_UpFlg = "select";
    }
    //ログインユーザー名取得
    try {
        user = request.getRemoteUser() != null ? request.getRemoteUser() : "";
    } catch (Exception e) {
    }

    String g_MaxLen= "";
    String g_Journalization      = "Journalization";
    String g_MaxTransactionCount = "MaxTransactionCount";

    ArrayList<ArrayList<String>> MstDeviceinfoLists = new ArrayList<ArrayList<String>>();

    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
    Connection connection = dbManager.getConnection();

    String errStr = "";
    String infoStr = "";
    String sqlStr = "";

    if( ( null != g_storeID)
     && ( null != g_terminalID )
     && ( null != g_companyID )
     ){
        // Select
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.StringBuilder sbTraining = new java.lang.StringBuilder();
        if ( "Update".equals(g_UpFlg) ) {
            // Update
            // SQL設定
            sqlStr = 
                /* MST_DEVICEINFO */
                  "UPDATE RESMaster.dbo.MST_DEVICEINFO SET "
                + "  LastTxId=?                     "
                + " ,Status     = 'Active'          "
                + " ,DeleteFlag = '0'               "
                + " ,UpdCount   = UpdCount+1        "
                + " ,UpdDate    = CURRENT_TIMESTAMP "
                + " ,UpdAppId   = 'settingDevice'   "
                + " ,UpdOpeCode = " + "'" + user  + "'"
                + "WHERE "
                + "     CompanyId= " + "'" + g_companyID  + "'"
                + " AND StoreId=   " + "'" + g_storeID    + "'"
                + " AND TerminalId=" + "'" + g_terminalID + "'"
                + " AND Training  = '0' "
                /* MST_DEVICEINFO(トレーニング) */
                + "UPDATE RESMaster.dbo.MST_DEVICEINFO SET "
                + "  LastTxId=?                     "
                + " ,Status     = 'Active'          "
                + " ,DeleteFlag = '0'               "
                + " ,UpdCount   = UpdCount+1        "
                + " ,UpdDate    = CURRENT_TIMESTAMP "
                + " ,UpdAppId   = 'settingDevice'   "
                + " ,UpdOpeCode = " + "'" + user  + "'"
                + "WHERE "
                + "     CompanyId= " + "'" + g_companyID  + "'"
                + " AND StoreId=   " + "'" + g_storeID    + "'"
                + " AND TerminalId=" + "'" + g_terminalID + "'"
                + " AND Training  = '1' "
                ;
            PreparedStatement psUpd = connection.prepareStatement(sqlStr);

            // MST_DEVICEINFOの設定
               //DeviceName
            psUpd.setString(1, request.getParameter("dev_LastTxId_in"));
            psUpd.setString(2, request.getParameter("tra_LastTxId_in"));

            try {
               int iret = psUpd.executeUpdate();
               connection.commit();
               infoStr = "1";
            } catch (Exception e) {
               errStr = "1";
            } finally {
               // クローズ
               connection.close();
               psUpd.close();
            }
            // 画面遷移
            String s_msg ="DeviceDetail.jsp?"
                    + "s1=" + g_storeID
                    + "&t1=" + g_terminalID
                    + "&r1=" + g_companyID
                    + "&y1="
                    + "&err="+ errStr
                    + "&info="+ infoStr
                    ;
            response.sendRedirect(s_msg);
            connection.close();

            
        } else {
            // Select
            // SQL設定
            sqlStr = "SELECT";
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
            // テーブル定義
            sqlStr += " FROM RESMaster.dbo.MST_DEVICEINFO deviceinfo";
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
                MstDeviceinfoList.add(rs.getString("dev_LinkQueueBuster"));
                // ～List:9
                MstDeviceinfoList.add(rs.getString("dev_LastTxId"));
                MstDeviceinfoList.add(rs.getString("dev_Status"));
                MstDeviceinfoList.add(rs.getString("dev_DeleteFlag"));
                
                MstDeviceinfoLists.add(MstDeviceinfoList);
            }
            // クローズ
            rs.close();
            ps.close();
            
            // 最大値取得
            sqlStr = "";
            sqlStr = "SELECT";
            // MST_DEVICEINFO
            sqlStr += "  syscounfig.Value       AS cnf_Value           ";
            sqlStr += " FROM RESMaster.dbo.PRM_SYSTEM_CONFIG syscounfig";
            sqlStr += " WHERE ";
            sqlStr += "     syscounfig.Category  = LTRIM(RTRIM(?))";
            sqlStr += " AND syscounfig.KeyId     = LTRIM(RTRIM(?))";

            ps = connection.prepareStatement(sqlStr);
            ps.setString(1, g_Journalization      );
            ps.setString(2, g_MaxTransactionCount );
            rs = ps.executeQuery();

            while (rs.next()) {
                g_MaxLen = rs.getString("cnf_Value");
            }
            // クローズ
            rs.close();
            ps.close();
            // コネクションクローズ
            connection.close();

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

<form action="DeviceLastTxId.jsp" method="post" id="updateform" onsubmit="return false;">
<input type="hidden" name="add_dev_CompanyId" id="add_dev_CompanyId" value="<%=request.getParameter("dev_CompanyId")%>">
<input type="hidden" name="add_dev_StoreId" id="add_dev_StoreId" value="<%=request.getParameter("dev_StoreId")%>">
<input type="hidden" name="add_dev_TerminalId" id="add_dev_TerminalId" value="<%=request.getParameter("dev_TerminalId")%>">
<input type="hidden" name="UpFlg" id="UpFlg" value="">
<input type="hidden" name="s1" id="s1" value="<%=request.getParameter("s1")%>">
<input type="hidden" name="t1" id="t1" value="<%=request.getParameter("t1")%>">
<input type="hidden" name="r1" id="r1" value="<%=request.getParameter("r1")%>">
<input type="hidden" name="MaxLen" id="MaxLen" value="<%= g_MaxLen %>">

<div class="page-wrapper">
取引NOを更新します。
<br><br>
※「MST_DEVICEINFO」の「LastTxId(取引NO)」を更新することができます。<br><br>

    <div class="table-scroll-area-v table-scroll-area-h" id="tabletable">
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
                <td><input type="text" id="dev_LastTxId" name="dev_LastTxId" disabled style="width: 100%" value="<%=MstDeviceinfoLists.get(0).get(9)!= null ? MstDeviceinfoLists.get(0).get(9) : 0 %>"></td>
                <td><input type="text" id="tra_LastTxId" name="tra_LastTxId" disabled style="width: 100%" value="<%=MstDeviceinfoLists.get(1).get(9)!= null ? MstDeviceinfoLists.get(1).get(9) : 0 %>"></td>
            </tr>
        </table>
    </div>

    <div class="panel" id="panel">
        <table border="0" cellspacing="4" cellpadding="4">
            <tr>
                <td align="right">取引NO ： </td>
                <td align="left">
                <input maxlength="5" type="text" id="dev_LastTxId_in" name="dev_LastTxId_in" value="<%=MstDeviceinfoLists.get(0).get(9)!= null ? MstDeviceinfoLists.get(0).get(9) : 0 %>"
                 size=5 required pattern="^[0-9]+$">
                 <%
                 int iMaxLen = MstDeviceinfoLists.get(0).get(9)!=null ? Integer.parseInt(MstDeviceinfoLists.get(0).get(9)) : 0;
                 iMaxLen ++;
                 out.print("(半角数字 " + iMaxLen + " ～  " + g_MaxLen + " を入力してください。)");
                 %>
                </td>
            </tr>
            <tr>
                <td align="right">取引NO(トレーニング用) ： </td>
                <td align="left">
                <input maxlength="5" type="text" id="tra_LastTxId_in" name="tra_LastTxId_in" value="<%=MstDeviceinfoLists.get(1).get(9)!= null ? MstDeviceinfoLists.get(1).get(9) : 0 %>" 
                 size=5 required pattern="^[0-9]+$">
                 <%
                 iMaxLen = MstDeviceinfoLists.get(1).get(9)!=null ? Integer.parseInt(MstDeviceinfoLists.get(1).get(9)) : 0;
                 iMaxLen ++;
                 out.print("(半角数字 " + iMaxLen + " ～  " + g_MaxLen + " を入力してください。)");
                 %>
                </td>
            </tr>
        </table>
    </div>

    <div align="right">
        <input type="button" value="更新" id="Update" name="Update" class="res-big-green"> 
    </div>
</div>
<button id="fakeButton" style="display:none"></button>
</form>

<script type="text/javascript">

jQuery(function ($) {

    // ボタン・クリック時にダイアログを開く
    $('#Update').click(function(e){
        var message = "";
        var MaxLen = document.getElementById('MaxLen').value;
        var dev_OrgLen = document.getElementById('dev_LastTxId').value;
        var tra_OrgLen = document.getElementById('tra_LastTxId').value;
        var dev_InLen = document.getElementById('dev_LastTxId_in').value;
        var tra_InLen = document.getElementById('tra_LastTxId_in').value;

        // 更新処理
        var myform = document.getElementById('updateform');
        if (myform.checkValidity() == false) {
            document.getElementById('fakeButton').click();
            return;
        }
        message = <%='\'' + MSG_UPDATE_INFO + '\''%>;
        showDialog(
            "タイトル：未使用",
            message,
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

        // 最小値チェック
        if ( ( Number(dev_InLen) <= Number(dev_OrgLen) )
        ||   ( Number(tra_InLen.value) <= Number(tra_OrgLen) )
        ){
            // 異常範囲
            message = <%='\'' + MSG_UPDATE_ERR + '\''%>;
            showDialog(
                "タイトル：未使用",
                message,
                ButtonYesNo,
                function() {
                    //「いいえ」を押したときの処理
                    document.getElementById("UpFlg").value = "Select";
                    myform.submit();
                },
                function() {
                    //「はい」を押したとき更新処理を継続
                }
            );
        }
        
        // 最大値チェック
        if ( ( Number(dev_InLen) > Number(MaxLen) )
        ||   ( Number(tra_InLen) > Number(MaxLen) )
        ){
            message = <%='\'' + MSG_MAX_ERR + '\''%>;
            showDialog(
                "タイトル：未使用",
                message,
                ButtonOK,
                function() {
                    //「OK」を押したときの処理
                    document.getElementById("UpFlg").value = "Select";
                    myform.submit();
                }
            );
        }
    });

});
(function() {
})();
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
