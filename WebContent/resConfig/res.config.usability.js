var res = res || {};
res.config = res.config || {};

/*
 * Usability Configuration Settings
 */

res.config.initialLanguage = "ja";
res.config.skin = {
    "normal":       "resSkin/default.css",
    "itemReturn":   "resSkin/orange.css",
    "priceCheck":   "resSkin/beige.css",
    "training":     "resSkin/blue.css",
};
res.config.logo = {
    home:   "images/home/NCR-BB-Alt-128x128.jpg",
    cid:    "images/home/NCR-BB-Alt-64x64.png",
};
res.config.initialPage = "home";    // home page after sign-in
//res.config.initialPage = "transaction.sales"; // sales transaction after sign-in
res.config.fileNameLength = 50;

res.config.currencies = {
    JPY:    { name: "JPY", symbol: '￥', numOfBills: 4, coinsPerRoll: 50, denominations: [
                                { name:{en:"Yen 10,000", ja:"10,000円"}, unit:10000},
                                { name:{en:"Yen 5,000", ja:"5,000円"}, unit:5000},
                                { name:{en:"Yen 2,000", ja:"2,000円"}, unit:2000},
                                { name:{en:"Yen 1,000", ja:"1,000円"}, unit:1000},
                                { name:{en:"Yen 500", ja:"500円"}, unit:500},
                                { name:{en:"Yen 100", ja:"100円"}, unit:100},
                                { name:{en:"Yen 50", ja:"50円"}, unit:50},
                                { name:{en:"Yen 10", ja:"10円"}, unit:10},
                                { name:{en:"Yen 5", ja:"5円"}, unit:5},
                                { name:{en:"Yen 1", ja:"1円"}, unit:1},
                        ]},
    USD:    { name: "USD", sumbol: '$', numOfBills: 4, coinsPerRoll: 50, denominations: [
                                { name:{en:"$100.00", ja:"100ドル"}, unit:10000},
                                { name:{en:"$50.00", ja:"50ドル"}, unit:5000},
                                { name:{en:"$20.00", ja:"20ドル"}, unit:2000},
                                { name:{en:"$10.00", ja:"10ドル"}, unit:1000},
                                { name:{en:"$5.00", ja:"5ドル"}, unit:500},
                                { name:{en:"$1.00", ja:"1ドル"}, unit:100},
                                { name:{en:"$0.25", ja:"25セント"}, unit:25},
                                { name:{en:"$0.10", ja:"10セント"}, unit:10},
                                { name:{en:"$0.05", ja:"5セント"}, unit:5},
                                { name:{en:"$0.01", ja:"1セント"}, unit:1},
                        ]},
};
res.config.currency = res.config.currencies["JPY"];
res.config.taxRate = 8;

res.config.companies =
    [
    { label: { en: "NCR Default", ja: "NCR 標準設定" }, id: "00000000" },
    { label: { en: "My Basket", ja: "まいばすけっと" }, id: "00000001" },
//  { label: { en: "Grocery Demo", ja: "食品スーパー用デモ" }, id: "00000002" },
//  { label: { en: "Drug Store Demo", ja: "ドラッグストア用デモ" }, id: "00000003" },
//  { label: { en: "Coffee House Demo", ja: "コーヒーショップ用デモ" }, id: "00000004" },
    ];

res.config.devices = {
    dispenser: { model: "Fuji Electric ECS-77", connected: false, },
    eMoney: { model: "Panasonic", connected: false, }
};
res.config.touchToneVolume = 0.2;   // from 0.0 to 1.0

res.config.autoSignOutMinutes = 10;
res.config.itemConsolidationDisabled = false;
res.config.decimalPointEnabled = false;
//res.config.customerTier = true;
res.config.confirmBeforeFinalize = false;
res.config.emoney = ["Waon", "Id", "Quicpay", "Suica", "Pasmo"];
res.config.itemDetailFormat = "full";   // "full", "discount"

/*
 * the max total view of obsolete deploy module
 */
res.config.maxObsoleteDeploy = 100;

res.config.pages = {
    credential:    { src: "iframes/credential/index.html", preload: true },
    home:          { src: "iframes/home/index.html", preload: true },
//   startOfDay:    { src: "iframes/startOfDay/index.html", preload: true },
//   transaction:   { src: "iframes/transaction/index.html", preload: true },
//   endOfDay:      { src: "iframes/endOfDay/index.html", preload: true },
//   cashToDrawer:  { src: "iframes/cashToDrawer/index.html", preload: false },
//   loan:          { src: "iframes/loan/index.html", preload: false },
//   pickUp:        { src: "iframes/pickUp/index.html", preload: false },
//   balancing:     {src: "iframes/balancing/index.html", preload: false },
//  deploy:        { src: "iframes/deploy/index.html", preload: true },
//   pickList:      { src: "iframes/pickList/index.html", preload: true },

    pickListEditor : { src: "iframes/pickList/editor/index.html", preload: true },
    pickListDeploy : { src: "iframes/pickList/deploy/index.html", preload: true },
    pickListAlbum  : { src: "iframes/pickList/album/index.html", preload: true },
    noticesEditor :  { src: "iframes/notices/editor/index.html", preload: true },
    noticesDeploy :  { src: "iframes/notices/deploy/index.html", preload: true },
    noticesAlbum  : { src: "iframes/notices/album/index.html", preload: true },
    advertisementEditor :  { src: "iframes/advertise/editor/index.html", preload: true },
    advertisementDeploy :  { src: "iframes/advertise/deploy/index.html", preload: true },
    advertisementAlbum  : { src: "iframes/advertise/album/index.html", preload: true },

    options:       { src: "iframes/options/index.html", preload: true },
    usability:     { src: "iframes/usability/index.html", preload: true },

//   manual:        { src: "iframes/manual/index.html", preload: false },
//   catalog:       { src: "iframes/catalog/index.html", preload: false },
//   merchandise:   { src: "iframes/merchandise/index.html", preload: false },
//   customers:     { src: "iframes/customers/index.html", preload: false },
//   journal:       { src: "iframes/journal/index.html", preload: false },
//   reports:       { src: "iframes/reports/index.html", preload: false },
//   supervisor:    { src: "iframes/supervisor/index.html", preload: false },
    settings:       { src: "iframes/settings/index.html", preload: true },
//   waon:          { src: "iframes/waon/index.html" },
//   giftCard:      { src: "iframes/giftCard/index.html" },
    // 2014/09/12 Lee Add
//  imagecopy:     { src: "iframes/imagecopy/index.html",preload:true}
};

res.config.sidePanel =
    [
//    { page: "home", label: "Home" },
//    { page: "deploy", label: "Deploy" },
//    { page: "waon", label: "Waon" },
//    { page: "giftCard", label: "GiftCard" },
//    { page: "catalog", label: "Catalog" },
//    { page: "merchandise", label: "Merchandise" },
//    { page: "customers", label: "Customers" },
//    { page: "journal", label: "Journal" },
//    { page: "manual", label: "Manual" },
//    { page: "reports", label: "Reports" },
//    { page: "settings", label: "Settings" },
    ];

res.config.buttons = {
    tender:
        [
        { label: {en:"Discount", ja:"小計割引"}, color: "orange", handler: "popDiscount()" },
        { label: {en:"E-Money", ja:"電子マネー"}, color: "blue", handler: "otherTenders('EMoney')" },
        { label: {en:"Credit Card", ja:"クレジットカード"}, color: "blue", handler: "otherTenders('CreditAuth')" },
        { label: {en:"Others", ja:"その他の支払い"}, color: "gray", handler: "otherTenders()" },
        ],
};

res.config.toolBar =
    [
    { tool: "cancel", show: true, label: {en:"Cancel", ja:"中止"}, image: "toolbar/cancel.png",   enabled: "auto" },
    { tool: "attributes", show: true, label: {en:"Attributes", ja:"取引属性"}, image: "", enabled: true },
    { tool: "priceCheck", show: "auto", label: {en:"Price", ja:"価格参照"}, image: "toolbar/Search-64x64.png", enabled: true },
    { tool: "priceCheckOff", show: "auto", label: {en:"Price End", ja:"価格参照終了"}, image: "toolbar/Search-64x64.png", enabled: true },
    { tool: "recall", show: "auto", label: {en:"Recall", ja:"呼出"}, image: "toolbar/recall.png", enabled: "auto" },
    { tool: "suspend", show: "auto", label: {en:"Suspend", ja:"保留"}, image: "toolbar/suspend.png", enabled: true },
    { tool: "transactionReturn", show: true, label: {en:"Return", ja:"返品"}, image: "toolbar/itemReturn.png", enabled: "auto" },
    { tool: "summaryReceipt", show: true, label: {en:"Receipt", ja:"領収証"}, image: "toolbar/receipt-48x32.png", enabled: "auto" },
    { tool: "notes", show: true, label: {en:"Notes", ja:"補足情報"}, image: "toolbar/notes.png", enabled: true },
    ];

res.config.guide = {
        "closed": {en:"Please bring change fund from back office and put them into the dispense.<br>Then touch 'Open' to register change fund.", ja:"最初に、事務所から釣銭準備金を預かってきて、釣銭機に投入します。<br>次に、開設ボタンを押して、釣り銭準備金を登録します。"},
        "open": {en:"Plase touch 'Transaction' to start transactions.", ja:"「売上登録」ボタンを押して、取引を開始します。"},
        "start": {en:"Enter membership first.<br>Then start scanning items.", ja:"最初に会員カードをお預かりし、読み取ります。<br>次に、バーコードをスキャンします。"},
        "itemizing": {en:"At scanning complete, touch 'Check Out'", ja:"バーコードのスキャンが終わったら、「お支払いへ」ボタンを押します。"},
        "tendering": {en:"Deposit cash to the acceptor.<br>Then, touch \"Enter\"", ja:"現金をお預かりして、釣銭機に投入します。終わったら、「投入金額の確定」ボタンを押します。"},
        "finalized": {en:"Hand over the changes.<br>Then, touch 'Next Transaction'", ja:"お釣りをお渡しします。終わったら、「次の取引へ」ボタンを押します。"},
        "pickList": {en:"Select +1/-1/quantity mode, select category from left menu, then touch one of the items.", ja:"上の「増やす/減らす/点数」を選び、左の商品分類を切り換えて、商品を見つけたらタッチします。"},
        "keyPad": {en:"Enter the number on the bar code, and touch 'Enter'", ja:"商品やクーポンのバーコードに付いている数字を入力し、「登録」を押します。"},
    };

