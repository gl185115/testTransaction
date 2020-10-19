<%@ page language="java" pageEncoding="utf-8"%>
<%@page
    import="ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer"
    import="java.util.ArrayList"
    import="java.text.NumberFormat"
    import="java.sql.*"
%>
<%!
	final String ERR_01_PLUFORMAT = "商品コードの形式に誤りがあります。";
	final String ERR_02_NODATA = "商品コードは、マスタに登録されていません。";
	final String ERR_03_PRICEFORMAT = "新売価の形式に誤りがあります。";
	final String WRN_01_BEFOREUPDATE = "売価の更新は入力した商品コードの検索を行った後に実行してください。";
%>
<%
	String user = "";

	//logininfo
	String p_bizDate = "";
	String p_bizDateTime = "";
	String p_company = (String)session.getAttribute("companyId");
	String p_store   = (String)session.getAttribute("storeId");
	String userId = (String)session.getAttribute("usrId");
	user = userId;

	JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String sql = "";    
	
	try{
        conn = dbManager.getConnection();
        conn.setAutoCommit(false);
        //
        //担当者マスタを取得
        sql = "SELECT TodayDate " +
            "FROM RESMaster.dbo.MST_BIZDAY ";
        
        ps = conn.prepareStatement(sql);

		//execute query
        rs = ps.executeQuery();
        
       //if has match record only : result = true
       	if(rs.next()){
       		p_bizDate = rs.getString("TodayDate");	
       	}
      
        rs.close();
        ps.close();
        
    } catch (Exception e) {
    	//write ex : result =  error
    	System.out.print(e);
    } 

%>


<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="./css/style.css">
	<link rel="stylesheet" type="text/css" href="./css/PluUrgentUpdate.css">
	<title>PluUrgentUpdate</title>
	<!-- jQuery -->
    <script type="text/javascript" src="../sharedlib/jquery.min.js"></script>
    <!-- jQuery UI -->
    <script type="text/javascript" src="../sharedlib/jquery-ui.min.js"></script>
    <script src="../resConfig/res.config.js" type="text/javascript"></script>
	<script type="text/javascript">
	//TODO:decide request param, url, when done json parse
	jQuery(function ($) {
		$(document).ready(function() {
			if($("#prm_user").val()=="" || $("#prm_user").val()=="null"){
				alert("セッションタイムアウトが発生しました。\nお手数ですがログイン画面より再度ログインしてください。");
				window.location.href='./index.jsp';
			}
		});
		
		$('#frm_sho_search').on('blur',function(){
			$("#search_result").val(0);
		})
		
		$('#btn_search').on('click', function() {
			if($("#prm_user").val()==""){
				alert("<%= ERR_01_PLUFORMAT %>");
				return false;
			}
			if($("#frm_sho_search").val()==""){
				alert("<%= ERR_01_PLUFORMAT %>");
				return false;
			}
			var nowDate= new Date();
			var strDatetime = ('00' + nowDate.getHours()).slice(-2)
			    + ':' + ('00' + nowDate.getMinutes()).slice(-2)
			    + ':' + ('00' + nowDate.getSeconds()).slice(-2)
			var params = {
				storecd : $('#prm_store').val(),
				bizdatetime : $('#prm_bizDate').val(),
				bizdate: strDatetime,
				mdinternal:$("#frm_sho_search").val()
			};
			var host = res.config.baseURL.match(/(^http|^https):\/\/(.*)(:\d+)\/(.*)\/$/);
//			var transactionurl = "https://" + host[2] + ":8443/temp";
			var transactionurl = "https://" + host[2] + ":8443/resTransaction";
			$.ajax({
				headers:{
					'Authorization':'Basic ZW50c3ZyOm5jcg=='
				},
				type: 'POST',
				url : transactionurl+'/rest/pricing/getUrgentChangeItemInfo',
				data: params,
				dataType: 'json'
			}).done(function(json){
				var dataArray = json;
				if(dataArray['NCRWSSExtendedResultCode']!=0 || dataArray['NCRWSSResultCode']!=0){
					alert("<%= ERR_02_NODATA %>");
					return false;
				}
// 				for(var count in dataArray){
					var shoname = dataArray['mdname'];
					var shocurrentprice = dataArray['current_salesprice']-0;
					var shosalesprice = dataArray['salesprice']-0;
					var shodpt = dataArray['dpt'];
					var shoclass = dataArray['class'];
					var shocategory = dataArray['category'];
// 				}
				$("#txt_shoname").val(shoname);
				$("#txt_shorealsalesprice").val("\\"+String(shocurrentprice).replace(/(\d)(?=(\d\d\d)+(?!\d))/g,'$1,' ));
				$("#txt_shodefaultsalesprice").val("\\"+String(shosalesprice).replace(/(\d)(?=(\d\d\d)+(?!\d))/g,'$1,' ));
				$("#txt_shoDpt").val(shodpt);
				$("#txt_shoClass").val(shoclass);
				$("#txt_shoCategory").val(shocategory);
				$("#search_result").val(1);
			}).fail(function(jqXHR, textStatus, errorThrown){
				alert("request failed");
			});
		});
		
		$('#btn_update').on('click', function() {
			if(confirm('売価を更新します。\nよろしいですか？')){
				if($("#search_result").val()=="0"){
					alert("<%= WRN_01_BEFOREUPDATE %>");
					return false;
				}
				if($("#frm_SalesPrice").val()==""){
					alert("<%= ERR_03_PRICEFORMAT %>");
					return false;
				}
				var v_mdinternal = $("#frm_sho_search").val();
				var v_dpt = $("#txt_shoDpt").val();
				var v_line = $("#txt_shoClass").val();
				var v_class = $("#txt_shoCategory").val();
				var v_instruct = "update";
				if($("#frm_sho_search").val()=="" || $("#txt_shoDpt").val()=="" || $("#txt_shoClass").val()=="" || $("#txt_shoCategory").val()==""){
					alert("<%= ERR_02_NODATA %>");
					return false;
				}
				$("#prm_mdinternal").val(v_mdinternal);
				$("#prm_dpt").val(v_dpt);
				$("#prm_line").val(v_line);
				$("#prm_class").val(v_class);
				$("#prm_instruct").val(v_instruct);
					
				$('#form_update').submit();
			}
			
		});
		
		$(document).on('click',"#btn_delete", function() {
			
			var v_mdinternal = $(this).closest('tr').children("td#item_mdinternal").text();
			var v_instruct = "delete";
			if(confirm('レコードを一覧から削除します。\nよろしいですか？')){
				$("#prm_mdinternal").val(v_mdinternal);
				$("#prm_instruct").val(v_instruct);
				$('#form_update').submit();
			}
		});
	});
	</script>
</head>
<body>
<h1 class="top">
		<label class="title-left">緊急売変</label>
		<div class="title-right">
		<label id = "lbl_user">ユーザID:<%= user %></label>
		<input type="hidden" id="prm_user" value=<%= user %>>
		<input type="hidden" id="prm_bizDate" value=<%= p_bizDate %>>
		<a id="lnk_logout" href="logout_action.jsp" tabindex="6">ログアウト</a>
		</div>
</h1>
	<form>
		<div class="middle">
			<div class="head">
				<label>商品コード
					<input type="number" class="num-format" id="frm_sho_search" tabindex="1" oninput="if(value.length>32)value=value.slice(0,32)">
				</label>
				<input type="hidden" id="prm_company" value=<%= p_company %>>
				<input type="hidden" id="prm_store" value=<%= p_store %>>
				<input type="button" id="btn_search" class="fnc_button" tabindex="2" value="検索">
				<input type="reset" class="fnc_button" tabindex="3" value="クリア">
			</div>
			<div class="border">
				<div class="border-flex">
					<div class="flex-item">
						<label id = "lbl_shoname">商品名
						<input readonly="readonly" id="txt_shoname" value="">
						</label>
					</div>
					<div class="flex-item">
						<label id = "lbl_shorealsalesprice">現在売価
						<input readonly="readonly" class="money" id="txt_shorealsalesprice" value="">
						</label>
					</div>
					<div class="flex-item">
						<label class="contentright"  id = "lbl_shodefaultsalesprice">標準売価
						<input readonly="readonly" class="money" id="txt_shodefaultsalesprice" value="">
						</label><br>
					</div>
					<div class="flex-item">
						<label class="content-left"  id = "lbl_shoDpt">部門
						<input readonly="readonly" id="txt_shoDpt" value="">
						</label>
					</div>
					<div class="flex-item">
						<label class="content-center"  id = "lbl_shoClass">中分類
						<input readonly="readonly" id="txt_shoClass" value="">
						</label>
					</div>
					<div class="flex-item">
						<label class="content-right"  id = "lbl_shoCategory">小分類
						<input readonly="readonly" id="txt_shoCategory"  value="">
						</label>
					</div>
				</div>
			</div>
		</div>
	</form>
	<form method="POST" action="update_action.jsp" id="form_update">
			<input type="hidden" id="prm_company" name="prm_company" value=<%= p_company %>>
			<input type="hidden" id="prm_store" name="prm_store" value=<%= p_store %>>
			<input type="hidden" id="search_result" value="0">
			<input type="hidden" id="prm_mdinternal" name="prm_mdinternal">
			<input type="hidden" id="prm_dpt" name="prm_dpt">
			<input type="hidden" id="prm_line" name="prm_line">
			<input type="hidden" id="prm_class" name="prm_class">
			<input type="hidden" id="prm_instruct" name="prm_instruct">
			<label class="border" id="lbl_SalesPrice">新売価</label>
			<input type="number" class="money" id="frm_SalesPrice" name="prm_salesprice" tabindex="4" oninput="if(value.length>10)value=value.slice(0,10)" >
			<input type="button" class="fnc_button" id="btn_update" tabindex="5" value="更新">
		<div class="bottom">
			<div class="scroll-table">
				<table id="tbl_plu">
					<tr id="header_plu">
						<th id="header_mdinternal">商品コード</th>
						<th id="header_mdname">商品名</th>
						<th id="header_urgentprice">緊急売価</th>
						<th id="header_salesprice">標準売価</th>
						<th id="header_buttonarea"></th>
					</tr>
					<%
	 					NumberFormat nf = NumberFormat.getCurrencyInstance(); 
						conn = dbManager.getConnection();
					    conn.setAutoCommit(false);
					    sql = "SELECT URGENT.MdInternal AS urg_MdInternal,"+
					          " CASE WHEN PLU.StoreId != 0 THEN PLU.MdNameLocal ELSE PLUALL.MdNameLocal END AS urg_MdName, "+
					          " URGENT.UrgentPrice AS urg_UrgentPrice,"+ 
					          " CASE WHEN PLU.StoreId != 0 THEN PLU.SalesPrice1 ELSE PLUALL.SalesPrice1 END AS urg_SalesPrice"+
					          " FROM RESMaster.dbo.MST_PLU_URGENT URGENT"+
					          " LEFT JOIN RESMaster.dbo.MST_PLU PLU"+
					          " ON URGENT.CompanyId = PLU.CompanyId"+
					          " AND URGENT.StoreId = PLU.StoreId"+
					          " AND URGENT.MdInternal = PLU.MdInternal"+
			        		  " LEFT JOIN RESMaster.dbo.MST_PLU PLUALL"+
					          " ON URGENT.CompanyId = PLUALL.CompanyId"+
					          " AND PLUALL.StoreId = 0"+
					          " AND URGENT.MdInternal = PLUALL.MdInternal"+
					          " WHERE"+
					          " URGENT.DeleteFlag = 0"+
					          ";";
					    ps = conn.prepareStatement(sql);
					
						//execute query
					    rs = ps.executeQuery();
					         
					    //if has match record only : result = true
					    while (rs.next()){
					%>
					<tr id="item_plu_<%= rs.getRow()%>">
					    <td id="item_mdinternal"><%= rs.getString("urg_MdInternal") %></td>
					    <td id="item_mdname"><%= rs.getString("urg_MdName") %></td>
						<td class="money" id="item_urgentprice"><%= nf.format(rs.getInt("urg_UrgentPrice")) %></td>
						<td class="money" id="item_salesprice"><%= nf.format(rs.getInt("urg_SalesPrice")) %></td>
						<td id="btn_buttonarea">
							<input type="button" class="fnc_button" id="btn_delete" value="削除">
						</td>
					</tr>
					<%
					    }
					    
					    rs.close();
					    ps.close();
					%>
				</table>
			</div>
			
		</div>
	</form>
</body>
</html>