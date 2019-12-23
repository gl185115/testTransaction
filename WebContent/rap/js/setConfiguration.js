
document.addEventListener('DOMContentLoaded', function() {

    const strageKey = 'selfModeConfiguration';
    const keys =[
        ['host', 'text'],
        ['desc', 'text'],
        //['add', 'button'],
        ['del', 'button']
    ];
    let strage = localStorage;
    let confTBody = document.getElementById('confTable');

    const assign = function (target, varArgs) { // .length of function is 2
        'use strict';
        if (target == null) { // TypeError if undefined or null
            throw new TypeError('Cannot convert undefined or null to object');
        }
        
        var to = Object(target);
        for (var index = 1; index < arguments.length; index++) {
            var nextSource = arguments[index];
            if (nextSource != null) { 
                for (var nextKey in nextSource) {
                    if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) 
                    to[nextKey] = nextSource[nextKey];
                }
            }
            return to;
        };
    }
    const insertConfRow = function(data) {
        let newRow = document.createElement('tr');
        keys.map(function(key) {
            let newCell = document.createElement('td');
            let newInput = document.createElement('input');
            newInput.className = key[0];
            newInput.type = key[1];
            newInput.value = data ? data[key[0]] : '';
            if (key[0] === 'del') {
                newInput.onclick = toggleDelButton;
                newInput.value = '削除';
            }
            newCell.appendChild(newInput);
            newRow.appendChild(newCell);
        });
        confTBody.appendChild(newRow);
    }
    const deleteConfRow = function(rownum) {
        let tgtRow = confTBody.getElementsByTagName('tr')[rownum-1];
        tgtRow.parentNode.deleteRow(tgtRow.sectionRowIndex);
    }

    const toggleDelButton = function(e) {
        e = e || window.event;
        tgt = e.srcElement || e.target;
        deleteConfRow(tgt.parentNode.parentNode.rowIndex);
    }
    const toggleAddButton = function() {
        let trs = confTBody.getElementsByTagName('tr');
        if(!trs[0] || trs[trs.length-1].firstChild.firstChild.value) insertConfRow();
    }
    const toggleSaveButton = function() {
        let saveArr = [];
        let trs = confTBody.getElementsByTagName('tr');
        for(let i = 0; i < trs.length; i++) {
            saveObj = {};
            keys.map(function(key) {
                let val = trs[i].getElementsByClassName(key[0])[0].value || '';
                let assignObj = {};
                assignObj[key[0]] = val;
                assign(saveObj, assignObj );
            });
            if(saveObj) saveArr.push(saveObj);
        }
        strage.setItem(strageKey, JSON.stringify(saveArr));
    }

    init = (function() {
        if(strage && strage.getItem(strageKey)) {
            let dataArray = JSON.parse(strage.getItem(strageKey));
            for(let i = 0; i < dataArray.length; i++) {
                insertConfRow(dataArray[i]);
            }
        }
        toggleAddButton();
        document.getElementById('closeButton').onclick = function() {
            window.opener.document.getElementById('hiddenButton').click();
        }
        document.getElementById('saveButton').onclick = function() {
            toggleSaveButton();
        }
        document.getElementById('addButton').onclick = function() {
            toggleAddButton();
        }
    })();
    window.onbeforeunload = function(e) {
        window.opener.location.reload();
    }
})