<%@page language="java" contentType="text/html; CharSet=UTF-8" pageEncoding="utf-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"%>
<%!
public static boolean isEmpty(String str) {
    return (str == null) || (str.length() == 0);
}
public static String trimToEmpty(String str) {
    return str == null ? "" : str.trim();
}
public static String escapeHtml(String pData){
   String s = "";
   for (int i = 0; i < pData.length(); i++) {
       Character c = new Character(pData.charAt(i));
       switch (c.charValue()) {
       case '&' :
           s = s.concat("&amp;");
           break;
       case '<' :
           s = s.concat("&lt;");
           break;
       case '>' :
           s = s.concat("&gt;");
           break;
       case '"' :
           s = s.concat("&quot;");
           break;
       case '\'' :
           s = s.concat("&#39;");
           break;
       default :
           s = s.concat(c.toString());
           break;
       }
   }
   return s;
}
%>
<%
request.setCharacterEncoding("UTF-8");

String resSearch = request.getParameter("resSearch");
String resCommit = request.getParameter("resCommit");
String err = request.getParameter("err");

String companyId = request.getParameter("companyId");
String storeId = request.getParameter("storeId");
String bizCatId = request.getParameter("bizCatId");
String terminalId = request.getParameter("terminalId");
String cmId = request.getParameter("cmId");

String selectCompanyName = request.getParameter("selectCompanyName");
String selectStoreName = request.getParameter("selectStoreName");
String selectBizCatName = request.getParameter("selectBizCatName");
String selectTerminalName = request.getParameter("selectTerminalName");
String selectCompanyId = request.getParameter("selectCompanyId");
String selectStoreId = request.getParameter("selectStoreId");
String selectBizCatId = request.getParameter("selectBizCatId");
String selectTerminalId = request.getParameter("selectTerminalId");

String cmName = request.getParameter("cmName");
String message1 = request.getParameter("message1");
String message2 = request.getParameter("message2");
String message3 = request.getParameter("message3");
String message4 = request.getParameter("message4");
String message5 = request.getParameter("message5");
String message6 = request.getParameter("message6");
String message7 = request.getParameter("message7");
String message8 = request.getParameter("message8");
String message9 = request.getParameter("message9");
String message10 = request.getParameter("message10");
String startDate = request.getParameter("startDate");
String endDate = request.getParameter("endDate");

String previousCmName = cmName;
String previousMessage1 = message1;
String previousMessage2 = message2;
String previousMessage3 = message3;
String previousMessage4 = message4;
String previousMessage5 = message5;
String previousMessage6 = message6;
String previousMessage7 = message7;
String previousMessage8 = message8;
String previousMessage9 = message9;
String previousMessage10 = message10;
String previousStartDate = startDate;
String previousEndDate = endDate;

Map<String, String> companyMap = new TreeMap<String, String>();
Map<String, String> storeMap = new TreeMap<String, String>();
Map<String, String> bizCatMap = new TreeMap<String, String>();
Map<String, String> terminalMap = new TreeMap<String, String>();
Map<String, String> dateMap = new TreeMap<String, String>();

Connection conn = null;
PreparedStatement ps = null;
ResultSet rs = null;

try{
    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
    conn = dbManager.getConnection();
    conn.setAutoCommit(false);

    //Masterから会社一覧を取得
    String sql = "SELECT CompanyId" +
            ", CompanyName" +
            ", CompanyShortName" +
            " FROM RESMaster.dbo.MST_COMPANYINFO WHERE DeleteFlag = 0 ORDER BY CompanyId asc";
    ps = conn.prepareStatement(sql);
    rs = ps.executeQuery();

    while (rs.next()) {
        companyMap.put(rs.getString("CompanyId"), rs.getString("CompanyName"));
    }
    rs.close();
    ps.close();

    if (companyId != null) {
        //Masterから業態一覧を取得
        sql = "SELECT CompanyId" +
            ", StoreId" +
            ", BizCatId" +
            ", BizCatName" +
            ", BizCatShortName" +
            " FROM RESMaster.dbo.MST_BIZCATEGORYINFO" +
            " WHERE CompanyId = ? and StoreId = 0 and DeleteFlag = 0 ORDER BY BizCatId asc";
        ps = conn.prepareStatement(sql);
        ps.setString(1, companyId);
        rs = ps.executeQuery();

        while (rs.next()) {
            bizCatMap.put(rs.getString("BizCatId"), rs.getString("BizCatName"));
        }
        rs.close();
        ps.close();
    }

     if (bizCatId != null) {
        //Masterから店舗一覧を取得
        sql = "SELECT CompanyId" +
            ", StoreId" +
            ", StoreName" +
            ", StoreShortName" +
            " FROM RESMaster.dbo.MST_STOREINFO" +
            " WHERE CompanyId = ? and DeleteFlag = 0 ORDER BY StoreId asc";
        ps = conn.prepareStatement(sql);
        ps.setString(1, companyId);
        rs = ps.executeQuery();

        while (rs.next()) {
            storeMap.put(rs.getString("StoreId"), rs.getString("StoreName"));
        }
        rs.close();
        ps.close();
    }

    if (storeId != null) {
        //Masterから端末一覧を取得
        sql = "SELECT CompanyId" +
            ", StoreId" +
            ", TerminalId" +
            ", TerminalName" +
            " FROM RESMaster.dbo.MST_TERMINALINFO" +
            " WHERE CompanyId = ? and StoreId = ? and DeleteFlag = 0 ORDER BY TerminalId asc";
        ps = conn.prepareStatement(sql);
        ps.setString(1, companyId);
        ps.setString(2, storeId);
        rs = ps.executeQuery();

        while (rs.next()) {
            terminalMap.put(rs.getString("TerminalId"), rs.getString("TerminalName"));
        }
        rs.close();
        ps.close();
    }

    //広告文一覧（日付）を検索
    java.sql.Date sqlStartDate = null;
    java.sql.Date sqlEndDate = null;
    sql = "SELECT CompanyId, CMId, StoreId, BizCatId, TerminalId" +
        ", Top1Message, Top2Message, Top3Message, Top4Message, Top5Message" +
        ", Bottom1Message, Bottom2Message, Bottom3Message, Bottom4Message, Bottom5Message" +
        ", StartDate, EndDate" +
        " FROM RESMaster.dbo.MST_PRESET_CMINFO" +
        " WHERE CompanyId = ? and StoreId = ? and BizCatId = ? and TerminalId = ?" +
        " and DeleteFlag = 0 ORDER BY CMId desc";
    ps = conn.prepareStatement(sql);
    ps.setString(1, companyId);
    ps.setString(2, storeId);
    ps.setString(3, bizCatId);
    ps.setString(4, terminalId);
    rs = ps.executeQuery();

    while (rs.next()) {
        sqlStartDate = rs.getDate("StartDate");
        sqlEndDate = rs.getDate("EndDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        dateMap.put(rs.getString("CMId"), sdf.format(sqlStartDate) + "~" + sdf.format(sqlEndDate));
    }
    rs.close();
    ps.close();

    if (!isEmpty(selectCompanyId)) {
        //広告条件を選択済みの場合
        //Masterから会社情報を取得
        sql = "SELECT CompanyId" +
            ", CompanyName" +
            ", CompanyShortName" +
            " FROM RESMaster.dbo.MST_COMPANYINFO WHERE CompanyId = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, companyId);
        rs = ps.executeQuery();

        if (rs.next()) {
            selectCompanyName = rs.getString("CompanyName");
        }
        rs.close();
        ps.close();

        if ("00".equals(selectBizCatId)) {
            selectBizCatName = "業態共通";
        } else {
            //Masterから業態情報を取得
            sql = "SELECT CompanyId" +
                ", StoreId" +
                ", BizCatId" +
                ", BizCatName" +
                ", BizCatShortName" +
                " FROM RESMaster.dbo.MST_BIZCATEGORYINFO" +
                " WHERE CompanyId = ? and StoreId = 0 and BizCatId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, companyId);
            ps.setString(2, bizCatId);
            rs = ps.executeQuery();

            if (rs.next()) {
                selectBizCatName = rs.getString("BizCatName");
            }
            rs.close();
            ps.close();
        }

        if ("0000".equals(selectStoreId)) {
            selectStoreName = "店舗共通";
        } else {
            //Masterから店舗情報を取得
            sql = "SELECT CompanyId" +
                ", StoreId" +
                ", StoreName" +
                ", StoreShortName" +
                " FROM RESMaster.dbo.MST_STOREINFO" +
                " WHERE CompanyId = ? and StoreId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, companyId);
            ps.setString(2, storeId);
            rs = ps.executeQuery();

            if (rs.next()) {
                selectStoreName = rs.getString("StoreName");
            }
            rs.close();
            ps.close();
        }

        if ("0000".equals(selectTerminalId)) {
            selectTerminalName = "POS共通";
        } else {
            //Masterから端末情報を取得
            sql = "SELECT CompanyId" +
                ", StoreId" +
                ", TerminalId" +
                ", TerminalName" +
                " FROM RESMaster.dbo.MST_TERMINALINFO" +
                " WHERE CompanyId = ? and StoreId = ? and TerminalId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, companyId);
            ps.setString(2, storeId);
            ps.setString(3, terminalId);
            rs = ps.executeQuery();

            if (rs.next()) {
                selectTerminalName = rs.getString("TerminalName");
            }
            rs.close();
            ps.close();
        }

        if (resCommit != null && "2".equals(resCommit) && !isEmpty(cmId)) {
            //登録失敗の場合、元の広告文を再取得
            sql = "SELECT CompanyId, CMId, CMName, StoreId, BizCatId, TerminalId" +
                ", Top1Message, Top2Message, Top3Message, Top4Message, Top5Message" +
                ", Bottom1Message, Bottom2Message, Bottom3Message, Bottom4Message, Bottom5Message" +
                ", StartDate, EndDate " +
                "FROM RESMaster.dbo.MST_PRESET_CMINFO " +
                "WHERE CompanyId = ? and CMId = ? and DeleteFlag = 0";
            ps = conn.prepareStatement(sql);
            ps.setString(1, companyId);
            ps.setString(2, cmId);
            rs = ps.executeQuery();

            if (rs.next()) {
                previousCmName = rs.getString("CMName");
                previousMessage1 = rs.getString("Top1Message");
                previousMessage2 = rs.getString("Top2Message");
                previousMessage3 = rs.getString("Top3Message");
                previousMessage4 = rs.getString("Top4Message");
                previousMessage5 = rs.getString("Top5Message");
                previousMessage6 = rs.getString("Bottom1Message");
                previousMessage7 = rs.getString("Bottom2Message");
                previousMessage8 = rs.getString("Bottom3Message");
                previousMessage9 = rs.getString("Bottom4Message");
                previousMessage10 = rs.getString("Bottom5Message");
                previousStartDate = rs.getString("StartDate");
                previousEndDate = rs.getString("EndDate");
            }
            rs.close();
            ps.close();
        }
    }
} catch (SQLException se) {
    err = "予期せぬエラーが発生しました。処理を中断します21。";
    do {
        err += "\\n" + se.getSQLState();
        err += "\\n" + se.getErrorCode();
        err += "\\n" + se.getMessage() ;
        se = se.getNextException();
    } while (se != null);
    resCommit = "2";
} catch (Exception e) {
    err = "予期せぬエラーが発生しました。処理を中断します22。";
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
<html lang="ja">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="./css/bootstrap.min.css">
<link rel="stylesheet" href="./css/select2.min.css">
<link rel="stylesheet" href="./css/jquery-ui.min.css">
<link rel="stylesheet" href="./css/jquery-ui.theme.min.css">
<link rel="stylesheet" href="./css/style.css">
<script src="../sharedlib/jquery.min.js"></script>
<script src="./js/bootstrap.min.js"></script>
<script src="../sharedlib/jquery-ui.min.js"></script>
<script src="./js/datepicker-ja.js"></script>
<script src="./js/select2.min.js"></script>
<script src="./js/jquery.validate.min.js"></script>
<script src="./js/messages_ja.js"></script>
<title>レシート広告 メンテナンス</title>
<script>
<!--
$(document).ready(function() {

    $("#frmAds").validate({
        errorClass: "error",
        rules:{
            startDate:{
                required: true,
                date: true,
                date2: true
            },
            endDate:{
                required: true,
                date: true,
                date2: true
            }
        },
        messages:{
            startDate:{
                date2:'有効な日付を入力してください。'
            },
            endDate:{
                date2:'有効な日付を入力してください。'
            }
        }
    });
    $.validator.addMethod("date2", function(val,elem){
        reg = new RegExp("^[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}$");
        return this.optional(elem) || reg.test(val);
    },"有効な日付を入力してください。");

    $('#modal-trigger').hide();

    <% if (resSearch != null) { %>
    var resSearch = <%= resSearch %>;
    if (resSearch === "2"){
        $(".modal-footer>#btnOk").show();
        $(".modal-dialog").find(".modal-title").html('警告');
        $(".modal-dialog").find(".modal-body").html('<%= err %>');
        $(".modal").modal("show");
    }
    <% } %>

    <% if (resCommit != null) { %>
    var resCommit = "<%= resCommit %>";
    var  err = "<%= err %>";
    if (resCommit === "1") {
        $(".modal-footer>#btnOk").show();
        $(".modal-dialog").find(".modal-title").html('正常');
        $(".modal-dialog").find(".modal-body").html('<span class="glyphicon glyphicon-ok" aria-hidden="true"></span><span>成功しました。</span>');
        $(".modal").modal("show");
    } else if (resCommit === "2") {
        $(".modal-footer>#btnOk").show();
        $(".modal-dialog").find(".modal-title").html('警告');
        $(".modal-dialog").find(".modal-body").html('<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span><span>' + err + '</span>');
        $(".modal").modal("show");
    }
    <% } %>

    $("select").select2();

    $.datepicker.setDefaults({
        dataFormat: 'yyyy/mm/dd',
        firstDay: 1,
        changeYear: true,
        changeMonth: true
    });
    $("#startDate").datepicker();
    $("#endDate").datepicker();

    $(".datepicker").css("z-index","100");

    if ($("#companyId").val() === "") {
        $("#bizCatId").attr("disabled", "disabled");
        $("#btnSet").attr("disabled", "disabled");
    }
    if ($("#companyId").val() === "" || $("#bizCatId").val() === "" || $("#bizCatId").val() === "00") {
        $("#storeId").attr("disabled", "disabled");
    }
    if ($("#companyId").val() === "" || $("#bizCatId").val() === "" || $("#bizCatId").val() === "00" || $("#storeId").val() === "" || $("#storeId").val() === "0000") {
        $("#terminalId").attr("disabled", "disabled");
    }
    $("#companyId").change(function() {
        $("#cId").val($(this).val());
        $("#bId").val("00");
        $("#sId").val("0000");
        $("#tId").val("0000");
        $("#frm").submit();
    });
    $("#bizCatId").change(function() {
        $("#cId").val($("#companyId").val());
        $("#bId").val($(this).val());
        $("#sId").val("0000");
        $("#tId").val("0000");
        $("#frm").submit();
    });
    $("#storeId").change(function() {
        $("#cId").val($("#companyId").val());
        $("#bId").val($("#bizCatId").val());
        $("#sId").val($(this).val());
        $("#tId").val("0000");
        $("#frm").submit();
    });
    $("#terminalId").change(function() {
        $("#cId").val($("#companyId").val());
        $("#bId").val($("#bizCatId").val());
        $("#sId").val($("#storeId").val());
        $("#tId").val($(this).val());
        $("#frm").submit();
    });
    $("#adsDates>a").click(function() {
        $("#adsDates>a").removeClass("active");
        $(this).addClass("active");
    });
    $("#btnSet").click(function() {
        if ($("#adsDates .active").attr("data-cmid") != undefined) {
            $("#cmId").val($("#adsDates .active").attr("data-cmid"));
        } else {
            $("#cmId").val("");
        }
        $("#frmSearch").submit();
    });
    $("#btnUpd").click(function() {
        if ($("#frmAds").valid()) {
        var sTime = Date.parse($("#startDate").val());
        var eTime = Date.parse($("#endDate").val());
        if (sTime > eTime) {
            $(".modal-footer>#btnOk").show();
            $(".modal-dialog").find(".modal-title").html('警告');
            $(".modal-dialog").find(".modal-body").html('<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span><span>日時が不正です</span>');
            $('.modal').modal('show');
            return false;
        }
        $(".modal-footer>#btnOk").hide();
        $(".modal-footer>#btnInsYes").hide();
        $(".modal-footer>#btnDelYes").hide();
        $(".modal-footer>#btnUpdYes").show();
        $(".modal-footer>#btnNo").show();
        $(".modal-dialog").find(".modal-title").html("確認");
        $(".modal-dialog").find(".modal-body").html('<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span><span>元のメッセージは上書きされます。登録してもよろしいですか？</span>');
        $(".modal").modal("show");
        return false;
        }
    });
    $("#btnIns").click(function() {
        if ($("#frmAds").valid()) {
        var sTime = Date.parse($("#startDate").val());
        var eTime = Date.parse($("#endDate").val());
        if (sTime > eTime) {
            $(".modal-footer>#btnOk").show();
            $(".modal-dialog").find(".modal-title").html("警告");
            $(".modal-dialog").find(".modal-body").html('<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span><span>日時が不正です</span>');
            $(".modal").modal("show");
            return false;
        }
        $(".modal-footer>#btnOk").hide();
        $(".modal-footer>#btnUpdYes").hide();
        $(".modal-footer>#btnDelYes").hide();
        $(".modal-footer>#btnInsYes").show();
        $(".modal-footer>#btnNo").show();
        $(".modal-dialog").find(".modal-title").html("確認");
        $(".modal-dialog").find(".modal-body").html('<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span><span>登録してもよろしいですか？</span>');
        $(".modal").modal("show");
        return false;
        }
    });
    $("#btnDel").click(function() {
        if ($("#frmAds").valid()) {
        $(".modal-footer>#btnOk").hide();
        $(".modal-footer>#btnUpdYes").hide();
        $(".modal-footer>#btnInsYes").hide();
        $(".modal-footer>#btnDelYes").show();
        $(".modal-footer>#btnNo").show();
        $(".modal-dialog").find(".modal-title").html("確認");
        $(".modal-dialog").find(".modal-body").html('<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span><span>削除してもよろしいですか？</span>');
        $(".modal").modal("show");
        return false;
        }
    });
    $("#myModal").on("click", ".modal-footer>#btnDelYes", function() {
        $("#regCmId").val($("#cmId").val());
        $("#regDeleteFlag").val("1");
        $("#frmAds").submit();
    });
    $("#myModal").on("click", ".modal-footer>#btnUpdYes", function() {
        $("#regCmId").val($("#cmId").val());
        $("#previousCmId").val($("#cmId").val());
        $("#frmAds").submit();
    });
    $("#myModal").on("click", ".modal-footer>#btnInsYes", function() {
        $("#regCmId").val(null);
        $("#previousCmId").val($("#cmId").val());
        $("#frmAds").submit();
    });

    if ($("#cmId").val() !== "") {
        $("#previousAds").show();
    } else {
        $("#btnUpd").attr("disabled", "disabled");
        $("#btnDel").attr("disabled", "disabled");
    }
    if ($("#regCompanyId").val() === "") {
        $("#btnUpd").attr("disabled", "disabled");
        $("#btnIns").attr("disabled", "disabled");
    }
});
//-->
</script>
</head>
<body>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/resTransaction/settingAds/">レシート広告 メンテナンス</a>
    </div>
  </div>
</nav>
<div class="container">
  <div class="row">
    <div class="col-md-3" role="menu">
      <form id="frm" action="index.jsp" method="POST" class="form-horizontal">
        <input type="submit" style="display: none;">
        <input type="hidden" id="cId" name="companyId">
        <input type="hidden" id="bId" name="bizCatId">
        <input type="hidden" id="sId" name="storeId">
        <input type="hidden" id="tId" name="terminalId">
      </form>
      <form id="frmSearch" action="search.jsp" method="POST" class="form-horizontal">
        <input type="hidden" id="cmId" name="cmId" value="<%= trimToEmpty(cmId) %>">
        <input type="submit" style="display: none;">
        <div class="form-group">
          <label for="companyId" class="col-sm-3 control-label">会社</label>
          <div class="col-sm-9">
            <select name="companyId" id="companyId" class="form-control select2">
              <option value="">選択してください</option>
           <% if (!companyMap.isEmpty()) {
                Iterator<String> it = companyMap.keySet().iterator();
                while (it.hasNext()) {
                  String key = it.next();
                  String value = companyMap.get(key); %>
              <option value="<%= key %>" <% if (companyId != null && companyId.equals(key)) {%> selected <%}%>><%= key %>:<%= value %></option>
             <% }
              } %>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="bizCatId" class="col-sm-3 control-label">業態</label>
          <div class="col-sm-9">
            <select name="bizCatId" id="bizCatId" class="form-control select2">
              <option value="00" <% if (bizCatId != null && bizCatId.equals("00")) {%> selected <%}%>>00:業態共通</option>
           <% if (!bizCatMap.isEmpty()) {
                Iterator<String> it = bizCatMap.keySet().iterator();
                while (it.hasNext()) {
                  String key = it.next();
                  String value = bizCatMap.get(key); %>
              <option value="<%= key %>" <% if (bizCatId != null && bizCatId.equals(key)) {%> selected <%}%>><%= key %>:<%= value %></option>
             <% }
              } %>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="storeId" class="col-sm-3 control-label">店舗</label>
          <div class="col-sm-9">
            <select name="storeId" id="storeId" class="form-control select2">
              <option value="0000" <% if (storeId != null && storeId.equals("0000")) {%> selected <%}%>>0000:店舗共通</option>
           <% if (!storeMap.isEmpty()) {
                Iterator<String> it = storeMap.keySet().iterator();
                while (it.hasNext()) {
                  String key = it.next();
                  String value = storeMap.get(key); %>
              <option value="<%= key %>" <% if (storeId != null && storeId.equals(key)) {%> selected <%}%>><%= key %>:<%= value %></option>
             <% }
              } %>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="terminalId" class="col-sm-3 control-label">POS</label>
          <div class="col-sm-9">
            <select name="terminalId" id="terminalId" class="form-control select2">
              <option value="0000" <% if (terminalId != null && terminalId.equals("0000")) {%> selected <%}%>>0000:POS共通</option>
           <% if (!terminalMap.isEmpty()) {
                Iterator<String> it = terminalMap.keySet().iterator();
                  while (it.hasNext()) {
                  String key = it.next();
                  String value = terminalMap.get(key); %>
              <option value="<%= key %>" <% if (terminalId != null && terminalId.equals(key)) {%> selected <%}%>><%= key %>:<%= value %></option>
               <% }
              } %>
            </select>
          </div>
        </div>
        <div style="margin-top: 30px;">
          <span class="glyphicon glyphicon-list"></span><label style="margin-left: 10px;">広告文一覧<span class="badge" style="margin-left: 10px;"><%= dateMap.size() %></span></label>
       <% if (dateMap.isEmpty()) { %>
          <p>広告文はありません</p>
       <% } else { %>
          <div id="adsDates" class="list-group">
         <% Iterator<String> it = dateMap.keySet().iterator();
            while (it.hasNext()) {
              String key = it.next();
              String value = dateMap.get(key);
            %>
            <a data-cmid="<%= key %>" class="list-group-item <%if (trimToEmpty(cmId).equals(key)) {%> active <%}%>"><%= value %></a>
         <% } %>
          </div>
       <% } %>
        </div>
        <input type="button" id="btnSet" class="btn btn-primary btn-lg" value="選択">
      </form>
    </div><!-- /.col-md-3 -->
    <div class="col-md-9" role="main">
      <form id="frmAds" name="form" action="result.jsp" method="POST">
        <input type="hidden" id="regCompanyId" name="companyId" value="<%= trimToEmpty(selectCompanyId) %>">
        <input type="hidden" id="previousCmId" name="previousCmId">
        <input type="hidden" id="regCmId" name="cmId">
        <input type="hidden" id="regbzCatId" name="bizCatId" value="<%= trimToEmpty(selectBizCatId) %>">
        <input type="hidden" id="regStopreId" name="storeId" value="<%= trimToEmpty(selectStoreId) %>">
        <input type="hidden" id="regTerminalId" name="terminalId" value="<%= trimToEmpty(selectTerminalId) %>">
        <input type="hidden" id="regDeleteFlag" name="deleteFlag">
        <div class="panel panel-default">
          <div class="panel-body">
            <h3><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span><label style="margin-left: 10px;">広告文編集</label></h3>
         <% if (!isEmpty(selectCompanyId)) { %>
            <ol class="breadcrumb">
              <li><span class="glyphicon glyphicon-home" aria-hidden="true" style="margin-right: 10px;"></span><span><%= selectCompanyId %>:<%= selectCompanyName %></span></li>
              <li><span><%= selectBizCatId %>:<%= selectBizCatName %></span></li>
              <li><span><%= selectStoreId %>:<%= selectStoreName %></span></li>
              <li class="active"><span><%= selectTerminalId %>:<%= selectTerminalName %></span></li>
            </ol>
         <% } %>
            <div id="previousAds" align="center" style="display: none;">
              <div class="page-header">
                <h1><small><span class="glyphicon glyphicon-edit" aria-hidden="true"></span>元の広告文<% if (!isEmpty(previousStartDate)) { %> (<%= previousStartDate %> ~ <%= previousEndDate %>) <% } %></small></h1>
              </div>
              <div class="page-body">
                <div class="input-group" style="margin-bottom: 5px;">
                  <span class="input-group-addon" id="basic-addon1-preCmName">CM名称</span>
                  <input type="text" value="<%= escapeHtml(trimToEmpty(previousCmName)) %>" class="form-control" readonly="readonly" aria-describedby="basic-addon1-preCmName">
                </div>
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage1)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage2)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage3)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage4)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage5)) %>" class="form-control" readonly="readonly" style="margin-bottom: 5px;">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage6)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage7)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage8)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage9)) %>" class="form-control" readonly="readonly">
                <input type="text" value="<%= escapeHtml(trimToEmpty(previousMessage10)) %>" class="form-control" readonly="readonly">
              </div>
            </div>
            <div id="inputform" align="center">
              <div class="page-header">
                <h1><small><span class="glyphicon glyphicon-edit" aria-hidden="true"></span>作成中の広告文</small></h1>
              </div>
              <div class="page-body">
                <div class="input-group" style="margin-bottom: 5px;">
                  <span class="input-group-addon" id="basic-addon-cmName">CM名称</span>
                  <input type="text" name="cmName" maxlength="40" value="<%= escapeHtml(trimToEmpty(cmName)) %>" class="form-control" aria-describedby="basic-addon-cmName">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon1">上段1行目</span>
                  <input type="text" name="ads1" maxlength="40" value="<%= escapeHtml(trimToEmpty(message1)) %>" class="form-control" aria-describedby="basic-addon1">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon2">上段2行目</span>
                  <input type="text" name="ads2" maxlength="40" value="<%= escapeHtml(trimToEmpty(message2)) %>" class="form-control" aria-describedby="basic-addon2">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon3">上段3行目</span>
                  <input type="text" name="ads3" maxlength="40" value="<%= escapeHtml(trimToEmpty(message3)) %>" class="form-control" aria-describedby="basic-addon3">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon4">上段4行目</span>
                  <input type="text" name="ads4" maxlength="40" value="<%= escapeHtml(trimToEmpty(message4)) %>" class="form-control" aria-describedby="basic-addon4">
                </div>
                <div class="input-group" style="margin-bottom: 5px;">
                  <span class="input-group-addon" id="basic-addon5">上段5行目</span>
                  <input type="text" name="ads5" maxlength="40" value="<%= escapeHtml(trimToEmpty(message5)) %>" class="form-control" aria-describedby="basic-addon5">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon6">下段1行目</span>
                  <input type="text" name="ads6" maxlength="40" value="<%= escapeHtml(trimToEmpty(message6)) %>" class="form-control" aria-describedby="basic-addon6">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon7">下段2行目</span>
                  <input type="text" name="ads7" maxlength="40" value="<%= escapeHtml(trimToEmpty(message7)) %>" class="form-control" aria-describedby="basic-addon7">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon8">下段3行目</span>
                  <input type="text" name="ads8" maxlength="40" value="<%= escapeHtml(trimToEmpty(message8)) %>" class="form-control" aria-describedby="basic-addon8">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon9">下段4行目</span>
                  <input type="text" name="ads9" maxlength="40" value="<%= escapeHtml(trimToEmpty(message9)) %>" class="form-control" aria-describedby="basic-addon9">
                </div>
                <div class="input-group">
                  <span class="input-group-addon" id="basic-addon10">下段5行目</span>
                  <input type="text" name="ads10" maxlength="40" value="<%= escapeHtml(trimToEmpty(message10)) %>" class="form-control" aria-describedby="basic-addon10">
                </div>
              </div>
              <br>
              <div class="form-inline">
                <label for="startDate">反映開始日：</label>
                <div class="col-xs-4 input-group">
                  <input type="text" name="startDate" id="startDate" maxlength="10" value="<%= trimToEmpty(startDate) %>" class="form-control" required>
                </div>
                <label for="endDate" style="margin-left: 10px;">反映終了日：</label>
                <div class="col-xs-4 input-group">
                  <input type="text" name="endDate" id="endDate" maxlength="10" value="<%= trimToEmpty(endDate) %>" class="form-control" required>
                </div>
              </div>
              <br>
              <div class="form-group">
                <button type="submit" id="btnUpd" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> 更新登録</button>
                <button type="submit" id="btnIns" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> 新規登録</button>
                <button type="submit" id="btnDel" class="btn btn-danger btn-lg"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> 削除</button>
              </div>
            </div>
          </div><!-- /#inputform -->
        </div><!-- /.panel -->
      </form>
    </div><!-- /.col-md-9 -->
  </div><!-- /.row -->
  <!-- Button trigger modal -->
  <button type="button" id="modal-trigger" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
  </button>
  <!-- Modal -->
  <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog modal-md">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
          <h4 class="modal-title" id="myModalLabel"></h4>
        </div>
        <div class="modal-body">
        </div>
        <div class="modal-footer">
          <button type="button" id="btnOk" class="btn btn-primary" data-dismiss="modal" style="display: none;">OK</button>
          <button type="button" id="btnNo" class="btn btn-default" data-dismiss="modal" style="display: none;">いいえ</button>
          <button type="button" id="btnDelYes" class="btn btn-primary" style="display: none;">はい</button>
          <button type="button" id="btnUpdYes" class="btn btn-primary" style="display: none;">はい</button>
          <button type="button" id="btnInsYes" class="btn btn-primary" style="display: none;">はい</button>
       </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
