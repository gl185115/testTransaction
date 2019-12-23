
document.addEventListener('DOMContentLoaded', function() {

    const codeMapping = {
        Status: ['未設定', '未開設', '有人モード中', 'セルフモード', '係員呼び出し中'],
        Training:['通常モード', 'トレーニングモード'],
        Printer:['正常'],
        CashChanger:['正常'],
    };
    const keys = ['WorkstationId', 'Status', 'Training', 'Printer', 'CashChanger'];
    const alertMapping = function(num) {
        num = +num;
        switch(num) {
            case 0: 
            case 99: return '';
            case 1: 
            case 4: return 'Alert';
            case 2: 
            case 3: return 'Warning';
            default: return 'Unexpected';
        };
    };
    let vals, CCC, CCCS;
    try {
        let val = decodeURI(window.location.search.substring(1, window.location.search.length));
        val = val.replace(/&/g, ';')
        eval(val);
        vals = [ WorkstationId, Status, Training, Printer, CashChanger ];
        for(let i = 0; i < keys.length; i ++) {
            let elem = document.getElementsByClassName('val' + i)[0];
            if(i === 0) elem.innerHTML = vals[i];
            else elem.innerHTML = codeMapping[keys[i]][vals[i]] || (vals[i] === "99" ? " " : '異常') || '異常';
        }
        if(CashChangerCount && CashChangerCountStatus) {
            CCC = JSON.parse(CashChangerCount);
            CCCS = JSON.parse(CashChangerCountStatus);
            document.getElementsByClassName('val5')[0].innerHTML = (CCC && !isNaN(CCC.amount)) ? '￥' + CCC.amount.toLocaleString() : '';
            for(let i = 0; i < 10; i ++) {
                let targetElem = document.getElementsByClassName('val'+(15-i))[0];
                targetElem.innerHTML = (CCC.counts && !isNaN(CCC.counts[i])) ? CCC.counts[i] : '';
                targetElem.parentElement.className = (CCCS.CashChangerCountStatus && CCCS.CashChangerCountStatus[i]) ? alertMapping(CCCS.CashChangerCountStatus[i]) : '';
            }
        }
    }
    catch (e) {
        console.log(e)
    }
    document.getElementById('closeButton').onclick = function() {
        window.opener.document.getElementById('hiddenButton').click()
    }
    window.onbeforeunload = function(e) {
        window.opener.location.reload();
    }
});