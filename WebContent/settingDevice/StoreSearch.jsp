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
    
    JndiDBManagerMSSqlServer dbManager = (JndiDBManagerMSSqlServer) JndiDBManagerMSSqlServer.getInstance();
    Connection connection = dbManager.getConnection();

    String sqlStr;

    // [企業番号] = [企業番号+企業番号名称]
    ArrayList<String> attCompanyIdList = new ArrayList<String>();
    ArrayList<String> attCompanyNameList = new ArrayList<String>();
    sqlStr = "SELECT"
            + "  companyinfo.CompanyId AS CompanyId"
            + ", companyinfo.CompanyName AS CompanyName"
            + " FROM RESMaster.dbo.MST_COMPANYINFO companyinfo"
            ;
    PreparedStatement psState = connection.prepareStatement(sqlStr);
    ResultSet rsCompany = psState.executeQuery();
    while(rsCompany.next()) {
        attCompanyIdList.add(rsCompany.getString("CompanyId"));
        attCompanyNameList.add(rsCompany.getString("CompanyName"));
    }
    psState.close();

    // 格納処理
    String[][] attCompanyAllList = new String[attCompanyIdList.size()][attCompanyIdList.size()+1];
    int it =0;
    for(it =0; it < attCompanyIdList.size(); it++ ) {
        // 格納処理
        attCompanyAllList[it][0] = attCompanyIdList.get(it);
        attCompanyAllList[it][1] = attCompanyNameList.get(it);
    }
    
    // [店番号] = [企業番号+店番号+店番号名称]
    ArrayList<String> attCompanyIdList2 = new ArrayList<String>();
    ArrayList<String> attStoreIdList = new ArrayList<String>();
    ArrayList<String> attStoreNameList = new ArrayList<String>();
    sqlStr = "SELECT"
            + "  storeinfo.CompanyId AS CompanyId"
            + ", companyinfo.CompanyName AS CompanyName"
            + ", storeinfo.StoreId AS StoreId"
            + ", storeinfo.StoreName AS StoreName"
            + " FROM RESMaster.dbo.MST_COMPANYINFO companyinfo"
            + " LEFT JOIN RESMaster.dbo.MST_STOREINFO storeinfo"
            + " ON companyinfo.CompanyId = storeinfo.CompanyId"
            ;
    psState = connection.prepareStatement(sqlStr);
    ResultSet rsStore = psState.executeQuery();
    while(rsStore.next()) {
      attCompanyIdList2.add(rsStore.getString("CompanyId"));
      attStoreIdList.add(rsStore.getString("StoreId"));
      attStoreNameList.add(rsStore.getString("StoreName"));
    }
    
    psState.close();
    connection.close();

%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" type="text/css" href="./css/default.css">
</head>
<script type="text/javascript">
var selectedIndex = -1;
function changeChildSelect(obj) {
    var attCompanyIdList2 = [<%
                            for (int l=0;l<attCompanyIdList2.size(); l++) {
                                if (l!=0) {
                                    out.print(",");
                                }
                                out.print("\"" + attCompanyIdList2.get(l) + "\"");
                            } %>];
    var attStoreIdList = [<%
                            for (int l=0;l<attStoreIdList.size(); l++) {
                                if (l!=0)
                                {
                                    out.print(",");
                                }
                                out.print("\"" + attStoreIdList.get(l) + "\"");
                            }
                            %>];
    var attStoreNameList = [<%
                          for (int l=0;l<attStoreNameList.size(); l++) {
                              if (l!=0)
                              {
                                  out.print(",");
                              }
                              out.print("\"" + attStoreNameList.get(l) + "\"");
                          }
                          %>];

    if (selectedIndex == obj.selectedIndex) {
        return;
    }

    // delete
    if (document.getElementById("storeidlist").hasChildNodes()) {
        while (document.getElementById("storeidlist").childNodes.length > 0) {
        	document.getElementById("storeidlist").removeChild(document.getElementById("storeidlist").firstChild)
        }
    }

    document.getElementById("storeidlist").selectedIndex = -1;

    // add
    for ( i=0; i<attCompanyIdList2.length; i++){
        if( attCompanyIdList2[i] == obj.value ) {
            var opt = document.createElement("option");
            opt.value = attStoreIdList[i];
            opt.text = attStoreIdList[i] + " : " + attStoreNameList[i];
            document.getElementById("storeidlist").add(opt);
        }
    }
}

function setSelectIndex(searchCompanyID, searchStoreID) {
    var pulldown_option = document.getElementById("companyidlist").getElementsByTagName('option');
    for(i=0; i<pulldown_option.length;i++){
        if(pulldown_option[i].value == searchCompanyID){
            pulldown_option[i].selected = true;
            break;
        }
    }
    
    changeChildSelect(document.getElementById("companyidlist"));
    
    pulldown_option = document.getElementById("storeidlist").getElementsByTagName('option');
    for(i=0; i<pulldown_option.length;i++){
        if(pulldown_option[i].value == searchStoreID){
            pulldown_option[i].selected = true;
            break;
        }
    }
}
</script>
<body class="res-maincontent">
  <table>
    <tr>
      <td width="12%" align="right">企業番号：</td>
      <td width="38%">
        <select name="companyidlist" id="companyidlist" onChange="changeChildSelect(this)" style="width:100%">
          <option value="" selected></option>
          <%
          for (int i=0; i<attCompanyIdList.size();i++) {
              out.print("<option value=\"" + attCompanyIdList.get(i));
              if(attCompanyIdList.get(i).equals(request.getAttribute("CompanyId"))) {
                  out.print(" selected");
              }
              out.println("\">" + attCompanyIdList.get(i) + " : " + attCompanyNameList.get(i) + "</option>");
          }
          %>
        </select>
      </td>
      <td width="12%" align="right">店番号 ： </td>
      <td width="38%">
        <select name = "storeidlist" id="storeidlist" style="width:100%"></select>
      </td>
    </tr>
  </table>
</body>
<HEAD>
<meta http-equiv=”Pragma” content=”no-cache”>
<meta http-equiv=”Cache-Control” content=”no-cache”>
</HEAD> 
</html>