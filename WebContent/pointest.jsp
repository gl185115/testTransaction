<%@page language="java" pageEncoding="utf-8"%><%@page import="java.io.*"%><%@page import="ncr.res.pointserver.model.message.*"%>
<%@page language="java" import="org.skyscreamer.jsonassert.*"%>
<%@page language="java" import="org.json.*"%>
<%@page language="java" import="java.util.*"%>

<%
  ByteArrayOutputStream bao = new ByteArrayOutputStream();
  byte[] buff = new byte[4096];
  for (InputStream is = request.getInputStream();;) {
    int len = is.read(buff);
    if (len < 0) break;
    if (len > 0) {
      bao.write(buff, 0, len);
    }
  }
  String req = bao.toString("UTF-8");
  JSONObject json= (JSONObject) JSONParser.parseJSON(req);
  JSONObject header =  json.getJSONObject("HEADER");
  JSONObject body = json.getJSONObject("BODY");
    Iterator<String> itr = (Iterator) body.keys();
        if (body.has("KAIIN_CD")) {
            System.out.println("KAIIN_CD Found "+ body.get("KAIIN_CD"));
        }
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
  // include membershipId, exclude items(history), update request
  if (req.indexOf("KAIIN_CD") >= 0 && req.indexOf("MEISAI") < 0 && req.indexOf("UPD_PGID") < 0) {
    if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("0000000000002")) { %>
{ 
"BODY": {},
"ERROR":{
  "ERRORCD":10
  ,"ERRORS" : [ {
    "ERRORID": "W700"
    ,"ERRORKEY": "700"
    ,"ERRORMESSAGE": "会員コードがマスタ未存在です。"
  }]
}
}
    <% } else { %>
{
"BODY": {
 "TKAIIN_CD": "00000000000000000002"
,"KAIIN_CD1": "0008800000011103"
,"KAIIN_CD2": "8800000011103"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 2
,"CARD_TYPE_NO": 1
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20060802
,"NYUKAI_TENCD": "0047"
,"DM_KBN": "1"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "1"
,"MBMAIL_KBN": "1"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 1
,"RAITEN_KAISYA_CD": "988669384"
,"RAITEN_YMD": 20150226
,"RAITEN_TENCD": "0047"
,"YUKOU_YMD": 20170225
,"RUI_SU": 10
,"RUI_YMD": 20150226
,"RUI_KIN_SYOHIN": 500
,"RUI_KIN": 47005
,"KAIIN_KBN1": "0"
,"KAIIN_KBN2": "0"
,"KAIIN_KBN_YMD": 20151031
,"NEN_RUI_YMD": 20150226
,"RUI_SU1": 2
,"RUI_KIN1": 5496
,"RUI_SU2": 0
,"RUI_KIN2": 0
,"RUI_SU3": 4
,"RUI_KIN3": 35600
,"NENDO_YMD": 20150930
,"KAIIN_JYOTAI_CD": "0"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "現金"
,"KAIIN_NM_KANA": "ｹﾞﾝｷﾝ"
,"KAIIN_NM_EN": "v-card"
,"BIRTH_YMD": 20100830
,"SEX": "1"
,"YUBIN1": "310"
,"YUBIN2": "0842"
,"JYUSYO1": "埼玉県"
,"JYUSYO2": "川口市"
,"JYUSYO3": "富士通３丁目２６"
,"JYUSYO4": "富士コーポ２０３号"
,"TEL1": "048-287-9999"
,"TEL2": "090-8888-9999"
,"PCMAIL": null
,"MBMAIL": "v-card@docomo.ne.jp"
,"UPD_DATE": 20150226
,"UPD_TIME": 161812
,"SCHOOL_NM": null
,"SCHOOL_KBN": null
,"GRADE": null
,"TEAM_NM": null
,"KYOGI_SYUMOKU": null
,"MEMBER_SU": null
,"MEISAI": [{
 "MEISAI_KBN": null
,"MEISAI_NO": null
,"KYOGI": null
,"BRAND": null
,"SONOTA_KYOGI": null
,"SONOTA_BRAND": null
,"MEMBER_NM": null
,"MEMBER_NM_KANA": null
,"MEMBER_AGE": null
  }]
},
"ERROR": {
  "ERRORCD": 0
}
}
    <% } %>
<%
} else if (req.indexOf("TEL") >= 0 || req.indexOf("KAIIN_NM_KANA") >= 0 || req.indexOf("MAIL") >= 0 || req.indexOf("BIRTH_YMD") >= 0) {
%>
{
"BODY": {
 "MEISAI": [{
 "TKAIIN_CD": "00000000000000000003"
,"KAIIN_CD1": "0008800000000008"
,"KAIIN_CD2": "8800000000008"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 2
,"CARD_TYPE_NO": 1
,"SYU": 2
,"POINT": 1000
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20060802
,"NYUKAI_TENCD": "0047"
,"DM_KBN": "1"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "1"
,"MBMAIL_KBN": "1"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
,"RAITEN_KAISYA_CD": "988669384"
,"RAITEN_YMD": 20150222
,"RAITEN_TENCD": "0047"
,"YUKOU_YMD": 20170221
,"RUI_SU": 5
,"RUI_YMD": 20150226
,"RUI_KIN": 20000
,"KAIIN_KBN1": "0"
,"KAIIN_KBN2": "0"
,"KAIIN_KBN_YMD": 20151031
,"NEN_RUI_YMD": 20150226
,"RUI_SU1": 1
,"RUI_KIN1": 2000
,"RUI_SU2": 0
,"RUI_KIN2": 0
,"RUI_SU3": 3
,"RUI_KIN3": 32400
,"NENDO_YMD": 20150930
,"KAIIN_JYOTAI_CD": "0"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "現金２"
,"KAIIN_NM_KANA": "ﾌﾞｲｹﾞﾝｷﾝ2"
,"KAIIN_NM_EN": "v-card2"
,"BIRTH_YMD": 0
,"SEX": "1"
,"YUBIN1": "310"
,"YUBIN2": "0842"
,"JYUSYO1": "埼玉県"
,"JYUSYO2": "川口市"
,"JYUSYO3": "富士通３丁目２６"
,"JYUSYO4": "富士コーポ２０３号"
,"TEL1": "048-287-9999"
,"TEL2": "090-7777-9999"
,"PCMAIL": null
,"MBMAIL": null
,"UPD_DATE": 20150222
,"UPD_TIME": 151230
  }
]},
"ERROR": {
  "ERRORCD": 0
}
}
<%
} else if (req.indexOf("KOUBAI_SYUBETSU") >= 0 || req.indexOf("SEL_FROM") >= 0) {
%>
{"BODY":{ "MEISAI_RIREKI":[
{
  "KAISYA_CD": "190203484"
  ,"MEISYO_EN": "XEBIO"
  ,"UNYOYMD": 20150601
  ,"SOSIKI": "0001"
  ,"TENCD": "0460"
  ,"TENNM": "ＳＳさいたま新都心店"
  ,"POSNO": "0003"
  ,"TRANNO": 9205
  ,"JANLNO": 36
  ,"SYORIYMD": 20150601
  ,"SYORIHMS": 125122
  ,"TKAIIN_CD": "00000000000011339673"
  ,"CARD_KAISYA_CD": "190203484"
  ,"KAIIN_CD1": "0006005000693799"
  ,"KAIIN_CD2": "6005000693799"
  ,"CARD_KBN": "94"
  ,"CARD_TYPE_NO": 94
  ,"KAIIN_KBN": 0
  ,"POINTSYUBETSU": 0
  ,"POINT_MEISYO_NM": "購買"
  ,"TRANKBN": 0
  ,"TRANMODE": 0
  ,"TORI_MEISYO_NM": "売上"
  ,"GOPOINTKBN": 0
  ,"GOPOINTKBNNM": null
  ,"HENPINKOUKANF": 0
  ,"URISUU": 2
  ,"URIGAK": 8314
  ,"HINGAK": 8314
  ,"POINTGAK": 8200
  ,"KPOINT": 82
  ,"SPOINT": 100
  ,"KSMPOINT": 128
  ,"POINT": 110
  ,"BIKOU": null
  ,"TANCD": "3000749"
  ,"TANNM": "Ｘ　担当者"
  ,"KAIIN_RECEIPT_FLG": 1
}
,{
  "KAISYA_CD": "190203484"
  ,"MEISYO_EN": "XEBIO"
  ,"UNYOYMD": 20150601
  ,"SOSIKI": "0001"
  ,"TENCD": "0460"
  ,"TENNM": "ＳＳさいたま新都心店"
  ,"POSNO": "0003"
  ,"TRANNO": 9206
  ,"JANLNO": 37
  ,"SYORIYMD": 20150601
  ,"SYORIHMS": 125222
  ,"TKAIIN_CD": "00000000000011339673"
  ,"CARD_KAISYA_CD": "190203484"
  ,"KAIIN_CD1": "0006005000693799"
  ,"KAIIN_CD2": "6005000693799"
  ,"CARD_KBN": "94"
  ,"CARD_TYPE_NO": 94
  ,"KAIIN_KBN": 0
  ,"POINTSYUBETSU": 0
  ,"POINT_MEISYO_NM": "購買"
  ,"TRANKBN": 0
  ,"TRANMODE": 1
  ,"TORI_MEISYO_NM": "取消"
  ,"GOPOINTKBN": 0
  ,"GOPOINTKBNNM": null
  ,"HENPINKOUKANF": 0
  ,"URISUU": -2
  ,"URIGAK": -8314
  ,"HINGAK": -8314
  ,"POINTGAK": -8200
  ,"KPOINT": -82
  ,"SPOINT": -100
  ,"KSMPOINT": 110
  ,"POINT": 128
  ,"BIKOU": null
  ,"TANCD": "3000749"
  ,"TANNM": "Ｘ　担当者"
  ,"KAIIN_RECEIPT_FLG": 1
}
,{
  "KAISYA_CD": "190203484"
  ,"MEISYO_EN": "XEBIO"
  ,"UNYOYMD": 20150601
  ,"SOSIKI": "0001"
  ,"TENCD": "0266"
  ,"TENNM": "ＳＳ八戸"
  ,"POSNO": "0001"
  ,"TRANNO": 7232
  ,"JANLNO": 160
  ,"SYORIYMD": 20150601
  ,"SYORIHMS": 190640
  ,"TKAIIN_CD": "00000000000011827923"
  ,"CARD_KAISYA_CD": "190203484"
  ,"KAIIN_CD1": "0006005001581743"
  ,"KAIIN_CD2": "6005001581743"
  ,"CARD_KBN": "94"
  ,"CARD_TYPE_NO": 94
  ,"KAIIN_KBN": 0
  ,"POINTSYUBETSU": 0
  ,"POINT_MEISYO_NM": "購買"
  ,"TRANKBN": 0
  ,"TRANMODE": 2
  ,"TORI_MEISYO_NM": "返品"
  ,"GOPOINTKBN": 0
  ,"GOPOINTKBNNM": null
  ,"HENPINKOUKANF": 0
  ,"URISUU": -7
  ,"URIGAK": -34960
  ,"HINGAK": -34960
  ,"POINTGAK": -34600
  ,"KPOINT": -346
  ,"SPOINT": -100
  ,"KSMPOINT": 446
  ,"POINT": 0
  ,"BIKOU": null
  ,"TANCD": "3203723"
  ,"TANNM": "Ｘ　担当者"
  ,"KAIIN_RECEIPT_FLG": 1
}
,{
  "KAISYA_CD": "190203484"
  ,"MEISYO_EN": "XEBIO"
  ,"UNYOYMD": 20150601
  ,"SOSIKI": "0001"
  ,"TENCD": "0266"
  ,"TENNM": "ＳＳ八戸"
  ,"POSNO": "0001"
  ,"TRANNO": 7233
  ,"JANLNO": 161
  ,"SYORIYMD": 20150601
  ,"SYORIHMS": 190740
  ,"TKAIIN_CD": "00000000000011827923"
  ,"CARD_KAISYA_CD": "190203484"
  ,"KAIIN_CD1": "0006005001581743"
  ,"KAIIN_CD2": "6005001581743"
  ,"CARD_KBN": "94"
  ,"CARD_TYPE_NO": 94
  ,"KAIIN_KBN": 0
  ,"POINTSYUBETSU": 0
  ,"POINT_MEISYO_NM": "購買"
  ,"TRANKBN": 0
  ,"TRANMODE": 3
  ,"TORI_MEISYO_NM": "返品取消"
  ,"GOPOINTKBN": 0
  ,"GOPOINTKBNNM": null
  ,"HENPINKOUKANF": 0
  ,"URISUU": 7
  ,"URIGAK": 34960
  ,"HINGAK": 34960
  ,"POINTGAK": 34600
  ,"KPOINT": 346
  ,"SPOINT": 100
  ,"KSMPOINT": 0
  ,"POINT": 446
  ,"BIKOU": null
  ,"TANCD": "3203723"
  ,"TANNM": "Ｘ　担当者"
  ,"KAIIN_RECEIPT_FLG": 1
}
]},
 "ERROR":{
  "ERRORCD":0
 }
}
<%
} else if (req.indexOf("MEISAI_TORIHIKI") >= 0) {
%>
{"BODY":{ "MEISAI_TORIHIKI":[
{
  "KAISYA_CD": "190203484"
  ,"MEISYO_EN": "XEBIO"
  ,"UNYOYMD": 20150601
  ,"SOSIKI": "0001"
  ,"TENCD": "0460"
  ,"TENNM": "ＳＳさいたま新都心店"
  ,"POSNO": "0003"
  ,"TRANNO": 9279
  ,"JANLNO": 128
  ,"SYORIYMD": "20150601"
  ,"SYORIHMS": "182335"
  ,"TKAIIN_CD": "00000000000011830088"
  ,"CARD_KAISYA_CD": "190203484"
  ,"KAIIN_CD1": "0006005000714593"
  ,"KAIIN_CD2": "6005000714593"
  ,"CARD_KBN": "94"
  ,"CARD_TYPE_NO": 94
  ,"KAIIN_KBN": 0
  ,"POINTSYUBETSU": 0
  ,"POINT_MEISYO_NM": "購買"
  ,"TRANKBN": 0
  ,"TRANMODE": 0
  ,"TORI_MEISYO_NM": "売上"
  ,"GOPOINTKBN": 0
  ,"GOPOINTKBNNM": null
  ,"HENPINKOUKANF": 0
  ,"URISUU": 2
  ,"URIGAK": 8880
  ,"HINGAK": 8880
  ,"POINTGAK": 8800
  ,"KPOINT": 88
  ,"SPOINT": 0
  ,"KSMPOINT": 0
  ,"POINT": 88
  ,"BIKOU": null
  ,"TANCD": "3204001"
  ,"TANNM": "X　担当者"
  ,"KAIIN_RECEIPT_FLG": 1
  , "MEISAI": [
    {"TRANDTLNO": 1
    ,"GRPCD": null
    ,"BMNCD": null
    ,"BURANDO_CD": null
    ,"SKU": null
    ,"XEBIO_CD": null
    ,"SYONM": "セット"
    ,"IRO_NM": null
    ,"SIZE_MEI": null
    ,"MEI_URISU": 2
    ,"MEI_URIGAK": 8880
    },
    {"TRANDTLNO": 2
    ,"GRPCD": "12122"
    ,"BMNCD": "566"
    ,"BURANDO_CD": "004569"
    ,"SKU": "2600116309134"
    ,"XEBIO_CD": "0000000000"
    ,"SYONM": "Ｃ１＿０２Ｓ　ＢＲＮ"
    ,"IRO_NM": "BRN"
    ,"SIZE_MEI": "28.0"
    ,"MEI_URISU": 1
    ,"MEI_URIGAK": 7999
    },
    {"TRANDTLNO": 3
    ,"GRPCD": "41115"
    ,"BMNCD": "820"
    ,"BURANDO_CD": "006055"
    ,"SKU": "2600112137656"
    ,"XEBIO_CD": "0000000000"
    ,"SYONM": "インソール"
    ,"IRO_NM": "BEG"
    ,"SIZE_MEI": "L"
    ,"MEI_URISU": 1
    ,"MEI_URIGAK": 881
  }]
}
,{
  "KAISYA_CD": "988669384"
  ,"MEISYO_EN": "VICTORIA"
  ,"UNYOYMD": 20150601
  ,"SOSIKI": "0001"
  ,"TENCD": "0221"
  ,"TENNM": "ＡＭ東戸塚店"
  ,"POSNO": "0004"
  ,"TRANNO": 7753
  ,"JANLNO": 2
  ,"SYORIYMD": "20150601"
  ,"SYORIHMS": "100630"
  ,"TKAIIN_CD": "00000000000001049189"
  ,"CARD_KAISYA_CD": "988669384"
  ,"KAIIN_CD1": "0008800013924087"
  ,"KAIIN_CD2": "8800013924087"
  ,"CARD_KBN": "2"
  ,"CARD_TYPE_NO": 1
  ,"KAIIN_KBN": 0
  ,"POINTSYUBETSU": 0
  ,"POINT_MEISYO_NM": "購買"
  ,"TRANKBN": 0
  ,"TRANMODE": 2
  ,"TORI_MEISYO_NM": "返品"
  ,"GOPOINTKBN": 0
  ,"GOPOINTKBNNM": null
  ,"HENPINKOUKANF": 0
  ,"URISUU": -4
  ,"URIGAK": -7781
  ,"HINGAK": -7781
  ,"POINTGAK": -7700
  ,"KPOINT": -369
  ,"SPOINT": -114
  ,"KSMPOINT": 369
  ,"POINT": 114
  ,"BIKOU": null
  ,"TANCD": "3000640"
  ,"TANNM": "Ｖ　担当者"
  ,"KAIIN_RECEIPT_FLG": 1
  ,"MEISAI": [
  {"TRANDTLNO": 1
  ,"GRPCD": "51115"
  ,"BMNCD": "841"
  ,"BURANDO_CD": "006324"
  ,"SKU": "2600110996941"
  ,"XEBIO_CD": "0000000000"
  ,"SYONM": "ﾖｶﾞﾏｯﾄ4mm SAX"
  ,"IRO_NM": "SAX"
  ,"SIZE_MEI": "FF"
  ,"MEI_URISU": -7781
  ,"MEI_URIGAK": -369
  }]
}
,{
  "KAISYA_CD": "190203484"
  ,"MEISYO_EN": "XEBIO"
  ,"UNYOYMD": 20150601
  ,"SOSIKI": "0001"
  ,"TENCD": "0453"
  ,"TENNM": "ＳＳ黒崎店"
  ,"POSNO": "0006"
  ,"TRANNO": 6521
  ,"JANLNO": 20
  ,"SYORIYMD": "20150601"
  ,"SYORIHMS": "194803"
  ,"TKAIIN_CD": "00000000000001049189"
  ,"CARD_KAISYA_CD": "988669384"
  ,"KAIIN_CD1": "0008800013924087"
  ,"KAIIN_CD2": "8800013924087"
  ,"CARD_KBN": "2"
  ,"CARD_TYPE_NO": 1
  ,"KAIIN_KBN": 0
  ,"POINTSYUBETSU": 0
  ,"POINT_MEISYO_NM": "購買"
  ,"TRANKBN": 0
  ,"TRANMODE": 2
  ,"TORI_MEISYO_NM": "返品"
  ,"GOPOINTKBN": 0
  ,"GOPOINTKBNNM": null
  ,"HENPINKOUKANF": 0
  ,"URISUU": -10
  ,"URIGAK": -45747
  ,"HINGAK": -45747
  ,"POINTGAK": -45400
  ,"KPOINT": -1362
  ,"SPOINT": 0
  ,"KSMPOINT": 1470
  ,"POINT": 108
  ,"BIKOU": null
  ,"TANCD": "3000640"
  ,"TANNM": "Ｖ　担当者"
  ,"KAIIN_RECEIPT_FLG": 1
  ,"MEISAI": [
  {"TRANDTLNO": 1
  ,"GRPCD": "21112"
  ,"BMNCD": "407"
  ,"BURANDO_CD": "008703"
  ,"SKU": "4580382252298"
  ,"XEBIO_CD": "0000000000"
  ,"SYONM": "ネップカノコポロ"
  ,"IRO_NM": "NVY"
  ,"SIZE_MEI": "L"
  ,"MEI_URISU": -45747
  ,"MEI_URIGAK": -1362
 }]
}]},
 "ERROR":{
  "ERRORCD":0
 }
}
<%
} else if (req.indexOf("UPD_PGID") >= 0) {
%>
{ "ERROR": { "ERRORCD": 0 } }
<%
} else {
  throw new IllegalArgumentException("wrong input:\"" + req + "\"");
}
%>
