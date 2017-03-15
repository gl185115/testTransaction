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
    if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8800000000046")) { %>
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8800000000206")){ %>
//エラー
{ 
"BODY": {},
"ERROR":{
  "ERRORCD":10
  ,"ERRORS" : [ {
    "ERRORID": "E752"
    ,"ERRORKEY": "700"
    ,"ERRORMESSAGE": "会員検索エラーです。"
  }]
}
}
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("6050000000030")){ %>
//マスタ未存在
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8800000000039")){ %>
//オフライン新規

    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("6050000000023")){ %>
//オフラインNG

    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8800000000022")){ %>
//プレミア会員
{
"BODY": {
 "TKAIIN_CD": "00000008800000000022"
,"KAIIN_CD1": "0008800000000022"
,"KAIIN_CD2": "8800000000022"
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
,"KAIIN_KBN1": "1"
,"KAIIN_KBN2": "1"
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
,"KAIIN_NM": "プレミア　花子"
,"KAIIN_NM_KANA": "ﾌﾟﾚﾐｱ ﾊﾅｺ"
,"KAIIN_NM_EN": "PREMIERE HANAKO"
,"BIRTH_YMD": 20100830
,"SEX": "2"
,"YUBIN1": "150"
,"YUBIN2": "0036"
,"JYUSYO1": "東京都"
,"JYUSYO2": "渋谷区"
,"JYUSYO3": "南平台町16-16"
,"JYUSYO4": "渋谷タワー"
,"TEL1": "03-1234-5678"
,"TEL2": "090-8888-9999"
,"PCMAIL": null
,"MBMAIL": "shibuya@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8810000000038")){ %>
//ロイヤル会員
{
"BODY": {
 "TKAIIN_CD": "00000008810000000038"
,"KAIIN_CD1": "0008810000000038"
,"KAIIN_CD2": "8810000000038"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 2
,"CARD_TYPE_NO": 1
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_KBN1": "2"
,"KAIIN_KBN2": "2"
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
,"KAIIN_NM": "ロイヤル　太郎"
,"KAIIN_NM_KANA": "ﾛｲﾔﾙ ﾀﾛｳ"
,"KAIIN_NM_EN": "ROYAL TARO"
,"BIRTH_YMD": 19560114
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1801"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "royal@ncr.com"
,"MBMAIL": "royal@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000013")){ %>
//ヴィクトリアUC
{
"BODY": {
 "TKAIIN_CD": "00000008820000000013"
,"KAIIN_CD1": "0008820000000013"
,"KAIIN_CD2": "8820000000013"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_NM": "ヴィクトリア　三郎"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000020")){ %>
//移行元
{
"BODY": {
 "TKAIIN_CD": "00000008820000000020"
,"KAIIN_CD1": "0008820000000020"
,"KAIIN_CD2": "8820000000020"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 0
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "2"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　移行元"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000037")){ %>
//移行先
{
"BODY": {
 "TKAIIN_CD": "00000008820000000037"
,"KAIIN_CD1": "0008820000000037"
,"KAIIN_CD2": "8820000000037"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "3"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　移行先"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000044")){ %>
//付与不可
{
"BODY": {
 "TKAIIN_CD": "00000008820000000044"
,"KAIIN_CD1": "0008820000000044"
,"KAIIN_CD2": "8820000000044"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "4"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　付与不可4"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000051")){ %>
//付与不可5
{
"BODY": {
 "TKAIIN_CD": "00000008820000000051"
,"KAIIN_CD1": "0008820000000051"
,"KAIIN_CD2": "8820000000051"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "5"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　付与不可5"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000068")){ %>
//利用打ち切り
{
"BODY": {
 "TKAIIN_CD": "00000008820000000068"
,"KAIIN_CD1": "0008820000000068"
,"KAIIN_CD2": "8820000000068"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "6"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　利用打ち切り"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000075")){ %>
//一時停止
{
"BODY": {
 "TKAIIN_CD": "00000008820000000075"
,"KAIIN_CD1": "0008820000000075"
,"KAIIN_CD2": "8820000000075"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "7"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　付与不可5"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000082")){ %>
//テスト
{
"BODY": {
 "TKAIIN_CD": "00000008820000000082"
,"KAIIN_CD1": "0008820000000082"
,"KAIIN_CD2": "8820000000082"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "8"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　テスト"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000099")){ %>
//退会
{
"BODY": {
 "TKAIIN_CD": "00000008820000000099"
,"KAIIN_CD1": "0008820000000099"
,"KAIIN_CD2": "8820000000099"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "9"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　退会"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8820000000105")){ %>
//一時停止
{
"BODY": {
 "TKAIIN_CD": "00000008820000000105"
,"KAIIN_CD1": "0008820000000105"
,"KAIIN_CD2": "8820000000105"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 9
,"CARD_TYPE_NO": 3
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_JYOTAI_CD": "10"
,"STOP_YMD": 0
,"DEL_YMD": 0
,"TAIKAI_YMD": 0
,"PIN_NBR": null
,"WEB_KAIIN_ID": null
,"WEB_KAIIN_PASSWD": null
,"KAIIN_NM": "ヴィクトリア　エラー"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｻﾌﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA SABURO"
,"BIRTH_YMD": 19601231
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー6601"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8825000000018")){ %>
//ヴィクトリア仮カード（P引当不可）
{
"BODY": {
 "TKAIIN_CD": "00000008825000000018"
,"KAIIN_CD1": "0008825000000018"
,"KAIIN_CD2": "8825000000018"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 90
,"CARD_TYPE_NO": 4
,"SYU": 2
,"POINT": 0
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20150101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
,"RAITEN_KAISYA_CD": "988669384"
,"RAITEN_YMD": 20150101
,"RAITEN_TENCD": "0047"
,"YUKOU_YMD": 20170101
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
,"KAIIN_NM": "ヴィクトリア　次郎"
,"KAIIN_NM_KANA": "ｳﾞｨｸﾄﾘｱ ｼﾞﾛｳ"
,"KAIIN_NM_EN": "VICTORIA JIRO"
,"BIRTH_YMD": 19560114
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー5501"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("6020000000019")){ %>
//ゼビオメンバーズ
{
"BODY": {
 "TKAIIN_CD": "00000006020000000019"
,"KAIIN_CD1": "0006020000000019"
,"KAIIN_CD2": "6020000000019"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 1
,"CARD_TYPE_NO": 21
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
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
,"KAIIN_NM": "ゼビオ　太郎"
,"KAIIN_NM_KANA": "ｾﾞﾋﾞｵ ﾀﾛｳ"
,"KAIIN_NM_EN": "SAMPLE TARO"
,"BIRTH_YMD": 19560114
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー5501"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("0397700106504521")){ %>
//プレイヤーズカード（16桁）
{
"BODY": {
 "TKAIIN_CD": "00000397700106504521"
,"KAIIN_CD1": "0397700106504521"
,"KAIIN_CD2": "0397700106504521"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 5
,"CARD_TYPE_NO": 24
,"SYU": 2
,"POINT": 24000
,"SIKOU_YOTEI_POINT": 2400
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_NM": "プレイヤーズ　太郎"
,"KAIIN_NM_KANA": "ﾌﾟﾚｲﾔｰｽﾞ ﾀﾛｳ"
,"KAIIN_NM_EN": "PLAYERS TARO"
,"BIRTH_YMD": 19691024
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1801"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "royal@ncr.com"
,"MBMAIL": "royal@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("6040000000017")){ %>
//チームカード
{
"BODY": {
 "TKAIIN_CD": "000000006040000000017"
,"KAIIN_CD1": "0006040000000017"
,"KAIIN_CD2": "6040000000017"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 80
,"CARD_TYPE_NO": 80
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 800
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_NM": "チーム　カード"
,"KAIIN_NM_KANA": "ﾁｰﾑ ｶｰﾄﾞ"
,"KAIIN_NM_EN": "TEAM CARD"
,"BIRTH_YMD": 19591024
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1024"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "jba3@ncr.com"
,"MBMAIL": "jba3@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("0397702010000950")){ %>
//ホープス磁気カード
{
"BODY": {
 "TKAIIN_CD": "000000397702010000950"
,"KAIIN_CD1": "0397702010000950"
,"KAIIN_CD2": "7702010000950"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 30
,"CARD_TYPE_NO": 30
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 300
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_NM": "ホープス　磁気"
,"KAIIN_NM_KANA": "ﾎｰﾌﾟｽ ｼﾞｷ"
,"KAIIN_NM_EN": "HOPES MSR"
,"BIRTH_YMD": 19591024
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1024"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "jba3@ncr.com"
,"MBMAIL": "jba3@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("8500000000014")){ %>
//ホープスJAN
{
"BODY": {
 "TKAIIN_CD": "000000008500000000014"
,"KAIIN_CD1": "0008500000000014"
,"KAIIN_CD2": "8500000000014"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 31
,"CARD_TYPE_NO": 31
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 310
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_NM": "ホープス　JAN"
,"KAIIN_NM_KANA": "ﾎｰﾌﾟｽ ｼﾞｬﾝ"
,"KAIIN_NM_EN": "HOPES JAN"
,"BIRTH_YMD": 19591024
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1024"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "jba3@ncr.com"
,"MBMAIL": "jba3@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("9611000000016")){ %>
//JBA（役員審判）
{
"BODY": {
 "TKAIIN_CD": "000000009611000000016"
,"KAIIN_CD1": "0009611000000016"
,"KAIIN_CD2": "9611000000016"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 80
,"CARD_TYPE_NO": 80
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 8000
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_NM": "JBA　役員"
,"KAIIN_NM_KANA": "JBA ﾔｸｲﾝ"
,"KAIIN_NM_EN": "JBA YAKUIN"
,"BIRTH_YMD": 19591024
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1024"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "jba3@ncr.com"
,"MBMAIL": "jba3@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("9612000000013")){ %>
//JBA（責任者）
{
"BODY": {
 "TKAIIN_CD": "000000009612000000013"
,"KAIIN_CD1": "0009612000000013"
,"KAIIN_CD2": "9612000000013"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 81
,"CARD_TYPE_NO": 81
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 8100
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_NM": "JBA　責任者"
,"KAIIN_NM_KANA": "JBA ｾｷﾆﾝｼｬ"
,"KAIIN_NM_EN": "JBA SEKININ"
,"BIRTH_YMD": 19691024
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1024"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "jba3@ncr.com"
,"MBMAIL": "jba3@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("9613000000010")){ %>
//JBA（競技者）
{
"BODY": {
 "TKAIIN_CD": "000000009613000000010"
,"KAIIN_CD1": "0009613000000010"
,"KAIIN_CD2": "9613000000010"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 82
,"CARD_TYPE_NO": 82
,"SYU": 2
,"POINT": 50000
,"SIKOU_YOTEI_POINT": 8200
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20100101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
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
,"KAIIN_NM": "JBA　競技者"
,"KAIIN_NM_KANA": "JBA ｷｮｳｷﾞｼｬ"
,"KAIIN_NM_EN": "JBA KYOUGI"
,"BIRTH_YMD": 19791024
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー1024"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "jba3@ncr.com"
,"MBMAIL": "jba3@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("6005000000016")){ %>
//X仮カード（P引当不可）
{
"BODY": {
 "TKAIIN_CD": "00000006005000000016"
,"KAIIN_CD1": "0006005000000016"
,"KAIIN_CD2": "6005000000016"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 94
,"CARD_TYPE_NO": 94
,"SYU": 2
,"POINT": 10
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20150101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
,"RAITEN_KAISYA_CD": "988669384"
,"RAITEN_YMD": 20150101
,"RAITEN_TENCD": "0047"
,"YUKOU_YMD": 20170101
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
,"KAIIN_NM": "ゼビオ　次郎"
,"KAIIN_NM_KANA": "ｾﾞﾋﾞｵ ｼﾞﾛｳ"
,"KAIIN_NM_EN": "SAMPLE JIRO"
,"BIRTH_YMD": 19560114
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー5501"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
    <% } else if (body.has("KAIIN_CD") && body.get("KAIIN_CD").equals("6055000000011")){ %>
//X仮カードNEXT（P引当不可）
{
"BODY": {
 "TKAIIN_CD": "00000006055000000011"
,"KAIIN_CD1": "0006055000000011"
,"KAIIN_CD2": "6055000000011"
,"KAISYA_CD": "988669384"
,"CARD_KBN": 95
,"CARD_TYPE_NO": 95
,"SYU": 2
,"POINT": 100
,"SIKOU_YOTEI_POINT": 0
,"NYUKAI_KAISYA_CD": "988669384"
,"NYUKAI_YMD": 20150101
,"NYUKAI_TENCD": "0299"
,"DM_KBN": "0"
,"DM_YUSOU_KBN": "0"
,"PCMAIL_KBN": "0"
,"MBMAIL_KBN": "0"
,"DM_YMD": 20150501
,"RANK": null
,"RANK2": 0
,"RAITEN_KAISYA_CD": "988669384"
,"RAITEN_YMD": 20150101
,"RAITEN_TENCD": "0047"
,"YUKOU_YMD": 20170101
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
,"KAIIN_NM": "ゼビオ　ネクスト"
,"KAIIN_NM_KANA": "ｾﾞﾋﾞｵ ﾈｸｽﾄ"
,"KAIIN_NM_EN": "SAMPLE NEXT"
,"BIRTH_YMD": 19560114
,"SEX": "1"
,"YUBIN1": "104"
,"YUBIN2": "0033"
,"JYUSYO1": "東京都"
,"JYUSYO2": "中央区"
,"JYUSYO3": "新川1-21-2"
,"JYUSYO4": "茅場町レジデンスタワー5501"
,"TEL1": "03-6759-6000"
,"TEL2": "090-8888-9999"
,"PCMAIL": "sample@ncr.com"
,"MBMAIL": "sample@docomo.ne.jp"
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
,"YUKOU_YMD": 20170226
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
,"KAIIN_NM": "現金　太郎"
,"KAIIN_NM_KANA": "ｹﾞﾝｷﾝ ﾀﾛｳ"
,"KAIIN_NM_EN": "GENKIN TARO"
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
  ,"MEISYO_EN": "SAMPLE"
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
  ,"MEISYO_EN": "SAMPLE"
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
  ,"MEISYO_EN": "SAMPLE"
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
  ,"MEISYO_EN": "SAMPLE"
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
  ,"MEISYO_EN": "SAMPLE"
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
    ,"SAMPLE_CD": null
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
    ,"SAMPLE_CD": "0000000000"
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
    ,"SAMPLE_CD": "0000000000"
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
  ,"SAMPLE_CD": "0000000000"
  ,"SYONM": "ﾖｶﾞﾏｯﾄ4mm SAX"
  ,"IRO_NM": "SAX"
  ,"SIZE_MEI": "FF"
  ,"MEI_URISU": -7781
  ,"MEI_URIGAK": -369
  }]
}
,{
  "KAISYA_CD": "190203484"
  ,"MEISYO_EN": "SAMPLE"
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
  ,"SAMPLE_CD": "0000000000"
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
