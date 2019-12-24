
// when DOM content loaded, send api request
window.addEventListener('DOMContentLoaded', function() {

    // var APIDATA_NONE_MESSAGE = '端末ステータスを取得できませんでした。'
    var INTERVAL = 1000;
    var isCounting = false;
    var count = 3;
    var childWindow = null;
    var configWindow = null;
    var strageKey = 'selfModeConfiguration';
    var BASE_URL = '/resTransaction/rest/callExternal/get';
    var HOSTNAME_LIST = (localStorage.getItem(strageKey)) ? JSON.parse(localStorage.getItem(strageKey)) : [];
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

    // expected api data
    // TODO Add ItemCounts , Total
    var testJson0 = {
        CompanyId: "test-company",
        RetailStoreId: "0001",
        WorkstationId: "0001",
        Training: 0,
        Status: 0,
        Detail: 0,
        Printer: 0,
        CashChanger: 0,
        CashChangerCount:  '{ "amount": 550000, "count": [40,13,0,183,24,45,27,84,94,115] }',
        CashChangerCountStatus:   '{ "CashChangerCountStatus": [0,0,1,2,3,4,0,0,0,0] }',
        Message: "",
        Alert: 0,
        UpdateDatetime: "2019-08-01 00:00:00"
    };
    var testJson1 = {
        CompanyId: "test-company",
        RetailStoreId: "0001",
        WorkstationId: "0002",
        Training: 1,
        Status: 0,
        Detail: 0,
        Printer: 0,
        CashChanger: 0,
        CashChangerCount:  '{ "amount": 550000, "count": [40,13,0,183,24,45,27,84,94,115] }',
        CashChangerCountStatus:   '{ "CashChangerCountStatus": [1,2,3,4,0,0,4,0,0,4] }',
        Message: "",
        Alert: 0,
        UpdateDatetime: "2019-08-01 00:00:00"
    };
    var testJson2 = {
        CompanyId: "test-company",
        RetailStoreId: "0001",
        WorkstationId: "0003",
        Training: 1,
        Status: 3,
        Detail: 3,
        Printer: 1,
        CashChanger: 1,
        CashChangerCount:  '{ "amount": 550000, "count": [40,13,0,183,24,45,27,84,94,115] }',
        CashChangerCountStatus:   '{ "CashChangerCountStatus": [0,0,0,0,0,0,4,0,0,4] }',
        Message: "係員呼び出し",
        Alert: 1,
        UpdateDatetime: "2019-08-01 00:00:00"
    };
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
    // var toggleDetailButton = function(data) {
    //     var width = 450;
    //     var height = 450;
    //     var x = window.screenX + (window.innerWidth / 2) - (width / 2);
    //     var y = window.screenY + (window.innerHeight / 2) - (height / 2);
    //     childWindow = myWindowOpen(
    //         './childView.html' ,
    //         'child' ,
    //         'width=' + width + ',height=' + height + ',top=' + y + ',left=' + x + ',menubar=no, toolbar=no, location=no, status=no',
    //         data
    //     );
    //     isCounting = false;
    // }
    var toggleDetailButton = function (WorkstationId) {
        [].slice.call(document.getElementsByClassName('res-detail-tbl'))
            .forEach(function (v) { v.classList.add('hide') });
        document.getElementById('detail_' + WorkstationId).classList.remove('hide');
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
            // (key === 'CashChangerCount' && ChangerCountData) ? createInnerTable(td, ChangerCountData) : td.innerHTML = ChangerCountData.amount || '';
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
            var tbody = document.getElementById('logs');
            var tr = LogCreateRows(jsonData);
            tbody.appendChild(tr);
            var detailTableDiv = document.getElementById('detailTable');
            var detailTable = document.createElement('table');
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
            detailTableDiv.appendChild(detailTable);
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
            console.log("load", xhttp);
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
                    callback(JSON.parse(JSON.parse(json.ResultData).SelfModeInfo)[0]);
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

// initialize
init = (function () {
    // bind click event
    document.getElementById('configButton').onclick = function () { toggleConfigButton() };
    document.getElementById('hiddenButton').onclick = function () { toggleCloseButton() };

    // load test data
    // var testDataSet = [testJson0, testJson1, testJson2];
    // testDataSet.forEach(function(json, i) { setJsonData(json, i) });

    // load API data
    HOSTNAME_LIST.map(function (v) {
        if (v.host && v.desc) {
            getJsonDataFromAPI(
                BASE_URL + '?HttpMethod=' + METHOD + "&Resource=" + v.host,
                v.desc,
                setJsonData
            );
        }
        isCounting = true;
    });

    // refresh page
    setInterval(function() {
        if (isCounting) count --;
        if (count <= 0) location.reload();
    },INTERVAL);

})();


    window.onbeforeunload = function(e) {
        if (childWindow) childWindow.window.close();
        if (configWindow) configWindow.window.close();
    }
});