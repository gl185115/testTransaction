
// when DOM content loaded, send api request
window.addEventListener('DOMContentLoaded', function() {

    // var APIDATA_NONE_MESSAGE = '端末ステータスを取得できませんでした。'
    var INTERVAL = 1000;
    var isCounting = false;
    var countNum = 3;
    var refreshFlag = false;
    var count = countNum;
    var childWindow = null;
    var configWindow = null;
    var strageKey = 'selfModeConfiguration';
    var BASE_URL = '/resTransaction/rest/callExternal/get';
    try{
        var HOSTNAME_LIST = (localStorage.getItem(strageKey)) ? JSON.parse(localStorage.getItem(strageKey)) : [];
    }catch(error){
        console.log(error);
    }
    var METHOD = 'POST';
    var clientId = 'entsvr';
    var clientSecret = 'ncr';
    var htmlEscape = function(str)  {
        if (!str) return;
        return str.replace(/[<>&"'`]/g, function(match) {
            var escape = {
                '<': '&lt;',
                '>': '&gt;',
                '&': '&amp;',
                '"': '&quot;',
                "'": '&#39;',
                '`': '&#x60;'
            };
            return escape[match];
        });
    }
    var isNumber = function (val) {
        var regex = new RegExp(/^[-+]?[0-9]+(\.[0-9]+)?$/);
        return regex.test(val);
    }
    var strage = localStorage;
    var strageViewStateKey = 'selfModeViewState';

    // api unconnected
    var errDataCount = 1000;
    var errData = function (desc) {
        errDataCount--;
        return {
            "CompanyId": "error",
            "RetailStoreId": desc || errDataCount.toString(),
            "WorkstationId": desc || 'error' + errDataCount.toString(),
            "Training": "99",
            "Status": "1",
            "Detail": "99",
            "Printer": "99",
            "CashChanger": "99",
            "CashChangerCount": '{ "amount": 0, "counts": [0,0,0,0,0,0,0,0,0,0] }',
            "CashChangerCountStatus": '{ "CashChangerCountStatus": [0,0,0,0,0,0,0,0,0,0] }',
            "Message": "オフライン",
            "Alert": "99",
            "UpdateDatetime": "2000-01-01 00:00:00",
            "ItemCounts": "0",
            "Total": "0"
        }
    }
    // codes mapping list
    var codeMapping = {
        Training: ['通常モード', 'トレーニングモード'],
        Status: ['未設定', '未開設', '有人モード中', 'セルフモード', '係員呼び出し中'],
        Detail: ['正常', '係員呼出', '取引中止', 'プリンター異常', '自動釣銭機異常', 'その他デバイス異常', '商品登録失敗'
        // , 'クレジット決済失敗', '電子マネー引去未了', '電子マネー引去失敗', 'CAT端末異常', 'QR決済状況確認', 'QR決済失敗'
       ],
        Printer: ['正常'],
        CashChanger: ['正常'],
        CashChangerCountStatus: ['正常', 'フル', 'ニアフル', 'ニアエンプティ', 'エンプティ'],
        Alert: ['正常', '異常'],
    }
    var displayMapping = {
        WorkstationId: 'レジ番号',
        Status: '端末ステータス',
        Training: 'POSモード',
        Printer: 'プリンター',
        CashChanger: '自動釣銭機'
    }
    var alertMapping = function (num) {
        num = +num;
        switch (num) {
            case 0:
            case 99: return '';
            case 1:
            case 4: return 'Alert';
            case 2:
            case 3: return 'Warning';
            default: return 'Unexpected';
        };
    }

    var myWindowOpen = function(url, windowName, windowFeature, option) {
        if(!windowName) windowName = '_blank'
        if(!option) option = {}
        var prm = '';
        Object.keys(option).forEach(function(v, i) {
            i === 0 ? prm += '?' + v + '=' + encodeURI(JSON.stringify(option[v]))
                    : prm += '&' + v + '=' + encodeURI(JSON.stringify(option[v]))
        });
        var urlWithParam = url + prm
        return window.open(urlWithParam, windowName, windowFeature);
    }
    var LogCreateRows = function(data) {
        // TODO Add ItemCounts , Total
        var logTr = document.createElement('tr');
        var keys = ['WorkstationId', 'Status', 'Message'];
        for (i in keys) {
            i = +i
            var td = document.createElement('td');
            td.innerHTML = keys[i] === 'Status' ? codeMapping[keys[i]][data[keys[i]]]
                                                : data[keys[i]] || '';
            td.className = keys[i];
            var alertFlg = +data['Alert'];
            switch(alertFlg) {
                case 0 : break;
                case 1 : logTr.className = 'alert'; break;
                default : logTr.className = 'unconnected'; break;
            }
            logTr.appendChild(td);
        }
        var detailButtonTd = document.createElement('td');
        var detailButton = document.createElement('input');
        detailButtonTd.className = 'linkDetailPage'
        detailButton.type = 'button';
        detailButton.className = 'res-small-green';
        detailButton.value = '詳細';
        detailButton.onclick = function() { toggleDetailButton(data['WorkstationId']) };
        detailButtonTd.appendChild(detailButton);
        logTr.appendChild(detailButtonTd);
        return logTr;
    }
    var toggleCloseButton = function() {
        isCounting = true;
        if (childWindow) childWindow.window.close();
        if (configWindow) configWindow.window.close();
    }
    var toggleDetailButton = function (WorkstationId) {
        [].slice.call(document.getElementsByClassName('res-detail-tbl'))
            .forEach(function (v) { v.classList.add('hide') });
        var detailWordstationIdElement = document.getElementById('detail_' + WorkstationId);
        if(detailWordstationIdElement && detailWordstationIdElement.classList) detailWordstationIdElement.classList.remove('hide');
        strage.setItem(strageViewStateKey, WorkstationId);
    }
    var detailCreateRowsPre = function (data) {
        var ret = []
        var keys = ['WorkstationId', 'Status', 'Training', 'Printer', 'CashChanger'];
        for (var i = 0; i < keys.length; i++) {
            var detailTr = document.createElement('tr');
            detailTr.className = keys[i];
            var detailTh = document.createElement('th');
            var detailTd = document.createElement('td');
            detailTh.innerHTML = displayMapping[keys[i]];
            if (keys[i] === 'WorkstationId') { detailTd.innerHTML = data[keys[i]] || ''; }
            else { detailTd.innerHTML = isNumber(data[keys[i]]) ? codeMapping[keys[i]][data[keys[i]]] || '-' : ''; }
            detailTr.appendChild(detailTh);
            detailTr.appendChild(detailTd);
            ret.push(detailTr);
        }
        return ret;
    };
    var createInnerTable = function (td, data, status) {
        var table = document.createElement('table');
        var dispCashList = [
            '1円', '5円', '10円', '50円', '100円',
            '500円', '千円', '2千円', '5千円', '1万円'
        ];
        var upperTr = document.createElement('tr');
        var lowerTr = document.createElement('tr');
        for (var i = 0; i < 5; i++) {
            var upperTd = document.createElement('td');
            var lowerTd = document.createElement('td');
            var upperspan = document.createElement('span');
            var lowerspan = document.createElement('span');
            var upperP = document.createElement('p');
            var lowerP = document.createElement('p');
            upperspan.innerHTML = dispCashList[i];
            lowerspan.innerHTML = dispCashList[i + 5];
            upperTd.className = (status && status.CashChangerCountStatus) ? alertMapping(status.CashChangerCountStatus[9 - i]) : '';
            lowerTd.className = (status && status.CashChangerCountStatus) ? alertMapping(status.CashChangerCountStatus[4 - i]) : '';
            upperP.innerHTML = (data && data.counts) ? data.counts[9 - i] : '0';
            lowerP.innerHTML = (data && data.counts) ? data.counts[4 - i] : '0';
            upperTd.appendChild(upperspan);
            upperTd.appendChild(upperP);
            lowerTd.appendChild(lowerspan);
            lowerTd.appendChild(lowerP);
            upperTr.appendChild(upperTd);
            lowerTr.appendChild(lowerTd);
        }
        table.appendChild(upperTr);
        table.appendChild(lowerTr);
        table.className = 'innerTable';
        td.appendChild(table);
    };
    var detailCreateRowsSuf = function (data) {
        var ret = [];
        var hash = {
            CashChangerAmount: '釣銭在高枚数',
            CashChangerCount: '釣銭在高金額'
        };
        var ChangerCountData, CashChangerCountStatus;
        try {
            ChangerCountData = JSON.parse(data['CashChangerCount']);
            ChangerCountStatus = JSON.parse(data['CashChangerCountStatus']);
        } catch (error) {
            console.log(error);
        }
        for (var key in hash) {
            var tr = document.createElement('tr');
            var th = document.createElement('th');
            var td = document.createElement('td');
            tr.className = key;
            th.innerHTML = hash[key];
            if (key === 'CashChangerCount' && ChangerCountData && ChangerCountStatus) {
                createInnerTable(td, ChangerCountData, ChangerCountStatus);
            }
            else if (key !== 'CashChangerCount' && ChangerCountData) {
                td.innerHTML = ChangerCountData.amount || '0';
            }
            tr.appendChild(th);
            tr.appendChild(td);
            ret.push(tr);
        }
        return ret;
    };
    var toggleConfigButton = function(data) {
        var width = 450;
        var height = 450;
        var x = window.screenX + (window.innerWidth / 2) - (width / 2);
        var y = window.screenY + (window.innerHeight / 2) - (height / 2);
        configWindow = myWindowOpen(
            './configuration.html' ,
            'config' ,
            'width=' + width + ',height=' + height + ',top=' + y + ',left=' + x ,
            data
        );
        isCounting = false;
    }
    var setJsonData = function(jsonData) {
        if(jsonData) {
            var posNum = jsonData.WorkstationId;
            var tbody = document.getElementById('logs');
            var tr = LogCreateRows(jsonData);
            var detailTableDiv = document.getElementById('detailTable');
            var detailTable = document.createElement('table');
            if(!tbody && !detailTableDiv) return;
            if(refreshFlag){
                detailTableDiv.innerHTML = "";
                tbody.innerHTML = "";
                refreshFlag = false;
            }
            var tbodyLen = tbody.childNodes.length;
            detailTable.id = 'detail_' + jsonData['WorkstationId'];
            detailTable.classList.add('res-list-tbl');
            detailTable.classList.add('res-detail-tbl');
            if (strage.getItem(strageViewStateKey) !== jsonData['WorkstationId']) {
                detailTable.classList.add('hide');
            }
            var detailTrPre = detailCreateRowsPre(jsonData);
            var detailTrSuf = detailCreateRowsSuf(jsonData);
            [].slice.call(detailTrPre).forEach(function (v) { detailTable.appendChild(v) });
            [].slice.call(detailTrSuf).forEach(function (v) { detailTable.appendChild(v) });
            if(tbodyLen === 0){
                tbody.appendChild(tr);
                detailTableDiv.appendChild(detailTable);
            }else{
                for(var i = 0; i < tbodyLen; i++){
                    var tbodyNode = tbody.childNodes[i];
                    var tableNode = detailTableDiv.childNodes[i];
                    if(!tbodyNode || !tableNode) return;
                    if(tbodyNode.getElementsByClassName("WorkstationId")[0] && tbodyNode.getElementsByClassName("WorkstationId")[0].innerHTML > posNum){
                        tbody.insertBefore(tr, tbodyNode);
                        detailTableDiv.insertBefore(detailTable, tableNode);
                    }else if(i === tbodyLen - 1){
                        tbody.appendChild(tr);
                        detailTableDiv.appendChild(detailTable);
                    }
                }
            }
        }
    }
    var getJsonDataFromAPI = function(url, desc, callback) {
        var xhttp = new XMLHttpRequest();
        xhttp.onerror = function () {
            console.log("error", xhttp);
            if (callback && typeof callback === 'function') {
                callback(errData(desc));
            }
        }
        xhttp.onload = function () {
            var str = xhttp.responseText;
            str.slice(1);str.slice(0, -1);
            try {
                var json = JSON.parse(str);
            }
            catch (error) {
                console.log("parse error", str);
            }
            if (callback && typeof callback === 'function') {
                if (json && json.ResultData) {
                    try{
                        var jsonRsultData = JSON.parse(JSON.parse(json.ResultData).SelfModeInfo)[0];
                    }catch(error){
                        console.log(error);
                    }
                    callback(jsonRsultData);
                }
                else if (json && json.WorkstationId) {
                    var selfInfo = eval(json);
                    callback(selfInfo);
                }
                else {
                    console.log("json is undefined", json);
                    callback(errData(desc));
                }
            }
            else {
                console.log("callback is not defined", callback);
                callback(errData(desc));
            }
        }
        xhttp.open('GET', url, true);
        xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        xhttp.responseType = 'text';
        xhttp.send(null);
    }

    var loadApiData = function() {
        if(Object.prototype.toString.call(HOSTNAME_LIST) !== "[object Array]") return;
        HOSTNAME_LIST.map(function (v) {
            if (v.host && v.desc) {
                getJsonDataFromAPI(
                    BASE_URL + '?HttpMethod=' + METHOD + "&Resource=" + v.host,
                    v.desc,
                    setJsonData
                );
            }
        });
    }

// initialize
init = (function () {
    // bind click event
    if(document.getElementById('configButton')) document.getElementById('configButton').onclick = function () { toggleConfigButton() };
    if(document.getElementById('hiddenButton')) document.getElementById('hiddenButton').onclick = function () { toggleCloseButton() };

    // load API data
    loadApiData();
    isCounting = true;

    // refresh page
    setInterval(function() {
        if (isCounting) count --;
        if (count <= 0){
            refreshFlag = true;
            loadApiData();
            count = countNum;
        }
    },INTERVAL);

})();


    window.onbeforeunload = function(e) {
        if (childWindow) childWindow.window.close();
        if (configWindow) configWindow.window.close();
    }
});