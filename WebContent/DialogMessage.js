//ボタン定数（ボタン文字配列のlengthと整合させる）
var ButtonNone = 0;
var ButtonOK = 1;
var ButtonYesNo = 2;
var ButtonYesNoCancel = 3;

//ボタン表示の文字配列（ボタン定数と整合させる）
var ButtonCaptions = [
    [],
    ["OK"],
    ["いいえ", "はい"],
    ["いいえ", "はい", "キャンセル"]
];

//第4引数以降、コールバック(ボタン押下時の処理)
function showDialog(title, message, buttonType, callback) {
    //jQuery UIのdialogに渡すためのオブジェクト
    var buttons = {};

    //showDialogの引数退避
    var args = arguments;
    // ボタン設定のループ
    for (var i = 0; i < ButtonCaptions[buttonType].length; i++) {
        (function(i){
            var caption = ButtonCaptions[buttonType][i];
            if (caption) {
                buttons[caption] = function() {
                    if ($.isFunction(args[i + 3])) {
                        //ボタン押下時の処理を実行
                        args[i + 3]();
                    }
                    //ボタン押下時のダイアログ終了
                    $(this).dialog("close"); 
                }
            }
        })(i);
    }

    // dialog定義
    $("<div>" + message + "</div>").dialog({
        // オプション
        title: title,
//        autoOpen: false,           // 自動的に開かないように設定
        width:  420,               // 横幅のサイズを設定(auto)
        height: 345,               // 縦幅のサイズを設定(auto)
        show: 'fade',              // ダイアログの表示アニメーション
        hide: 'fade',              // ダイアログの終了アニメーション
        modal:true,                // モーダルダイアログにする
        position: 'center',        // ダイアログ表示位置(中央)
        dialogClass: 'ui-dialog2', // Class指定(CSS読み込み用)
        buttons: buttons,          // 処理を格納
        close: function(event) {
            //ダイアログ終了時に
            //メッセージのDOMオブジェクトを破棄
            $(this).dialog('destroy');
            $(event.target).remove();
        }
    });
}
