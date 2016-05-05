/*
 * Controller
 */
res.ui.controller("editStore", ["$scope", "$rootScope", "$timeout", "$filter", function ($scope, $rootScope, $timeout, $filter) {

    var scroll = undefined;
    var scrollAllStoreId = undefined;
    var scrollSelectedId = undefined;
    var scrollStoresGroup = undefined;
    var searchStoresCsv = [];
    $scope.errorFlag = "";
    $scope.searchArea = "";

    $rootScope.storeIdUp=true;
    $rootScope.storeJaNameUp = true;

    $scope.$on("resIncludeLoaded", function () {    // wait for res-include complete

        scroll = new IScroll("#wrapperStoresList", {// iScroll5
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true, // or "zoom"
        });
        scrollAllStoreId = new IScroll("#wrapperGroupStoreEntry",{
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true, // or "zoom"
        });
        scrollSelectedId = new IScroll("#wrapperGroupStoreList",{
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true, // or "zoom"
        });
        scrollStoresGroup = new IScroll("#wrapperStoresGroupList",{
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true, // or "zoom"
        });
        $scope.insetCanvas();
    });

    $scope.$watch(
        function () {
            return $rootScope.model.pageIndex;
        },
        function (newValue, oldValue) {
//              if (newValue != "storeDetail") return;
            if (newValue != "StoreEntryDetail") return;
            if (!$rootScope.model.storesCsv) return;

            if ($rootScope.model.storesCsv.length > 0) {
                $rootScope.model.storesCsv.sort($scope.getSort(true, "storeId"));
                $rootScope.model.OriginalStoresCsv = angular.copy($rootScope.model.storesCsv);
                $scope.selectStore(0);
            }
            res.ui.root.storeIdUp = false;
            res.ui.root.storeJaNameUp = true;
            if( $rootScope.model.searchFlag == false){
                $scope.searchArea = "";
            }
            $timeout(function () {
                scroll.refresh();
            }, 100);
        }
    );

    $scope.$watch('searchArea',function(newValue,oldValue){

        $rootScope.model.searchFlag = true;
        if(newValue == oldValue) {return;}
        if( newValue =="") {
            $rootScope.model.storesCsv = angular.copy($rootScope.model.storesCsvBak);
            $rootScope.model.storesCsv.sort($scope.getSort(true, "storeId"));
            $rootScope.model.OriginalStoresCsv = angular.copy($rootScope.model.storesCsv);
            $scope.selectStore(0);
            res.ui.root.storeIdUp = false;
            res.ui.root.storeJaNameUp = true;
            return;
        }
        searchStoresCsv = [];
        for(var i=0;i< $rootScope.model.storesCsvBak.length;i++){
            if( $rootScope.model.storesCsvBak[i].storeId.match($scope.searchArea) != null){
                searchStoresCsv.push($rootScope.model.storesCsvBak[i]);
            }else if($rootScope.model.storesCsvBak[i].entryNameJa.match($scope.searchArea) != null){
                searchStoresCsv.push($rootScope.model.storesCsvBak[i]);
            }

        }
        $rootScope.model.storesCsv = searchStoresCsv;
        if ($rootScope.model.storesCsv.length > 0) {
            $rootScope.model.storesCsv.sort($scope.getSort(true, "storeId"));
            $rootScope.model.OriginalStoresCsv = angular.copy($rootScope.model.storesCsv);
            $scope.selectStore(0);
            res.ui.root.storeIdUp = false;
            res.ui.root.storeJaNameUp = true;
        }
    });

    $scope.$watch(
        function () {
            return $rootScope.model.storesCsv.length;
        },
        function (newValue, oldValue) {
            $timeout(function () {
                scroll.refresh();
            }, 100);
        }
    );
    $scope.$watch(
        function () {
            return $rootScope.model.storesAllIdCsv.length;
//          if ($rootScope.model.storesAllIdCsv) {
//              return $rootScope.model.storesAllIdCsv.length;
//          } else {
//              return null;
//          }
        },
        function (newValue, oldValue) {
            $timeout(function () {
                scrollAllStoreId.refresh();
            }, 100);
        }
    );
    $scope.$watch(
        function () {
            return $rootScope.model.storesGroupSelectedId.length;
//
//          if ($rootScope.model.storesGroupSelectedId) {
//              return $rootScope.model.storesGroupSelectedId.length;
//          } else {
//              return null;
//          }
        },
        function (newValue, oldValue) {
            $timeout(function () {
                scrollSelectedId.refresh();
            }, 100);
        }
    );
    $scope.$watch(
        function () {
            return $rootScope.model.storesGroupCsv.length;

//          if ($rootScope.model.storesGroupCsv) {
//              return $rootScope.model.storesGroupCsv.length;
//          } else {
//              return null;
//          }
        },
        function (newValue, oldValue) {
            $timeout(function () {
                scrollStoresGroup.refresh();
            }, 100);
        }
    );

    $scope.selectStore = function(index) {
        if ($rootScope.model.storesCsv.length == 0) {
            return;
        }

        $rootScope.model.storesCsv.selectIndex = index;
        $scope.editStoreId = $rootScope.model.storesCsv[index].storeId;
        // $scope.editStoreNmEn =
        // $rootScope.model.storesCsv[index].storeNameEn;
        $scope.editStoreNmJp = $rootScope.model.storesCsv[index].entryNameJa;
        $scope.editStoreIP = $rootScope.model.storesCsv[index].storeIP;
        $scope.editStoreSecondaryIP = $rootScope.model.storesCsv[index].storeSecondaryIP;
        $scope.editStoreTel = $rootScope.model.storesCsv[index].storeTel;
    };

    $scope.selectStoreGroup = function(index) {
        $rootScope.model.storesGroupCsv.selectIndex = index;
        // $scope.storeGroupNameEn =
        // $rootScope.model.storesGroupCsv[index].storeGroupNameEn;
//      $scope.storeGroupNameJa = $rootScope.model.storesGroupCsv[index].groupNameJa;
        $scope.groupNameJa = $rootScope.model.storesGroupCsv[index].entryNameJa;
    };

    $scope.selectStoreToGroup = function (index) {
        $rootScope.model.storesAllIdCsv.selectIndex = index;
    };

    $scope.selectStoreToAll = function (index) {
        $rootScope.model.storesGroupSelectedId.selectIndex = index;
    };


    $scope.toEditStore = function(index) {
        if (index == undefined || index < 0) {
            return;
        }
        // $rootScope.model.pageIndex ="storeAdd";
        $rootScope.model.pageIndex = "StoreEntryCreate";
    };

    $scope.toEditStoreGroup = function(index) {
        if (index == undefined) {
            return;
        }

        $rootScope.model.storesGroupSelectedId = [];
        // $scope.editStoreGroupNameEn =
        // $rootScope.model.storesGroupCsv[index].storeGroupNameEn;
        $rootScope.model.editStore.groupEntryNameJa = $rootScope.model.storesGroupCsv[index].entryNameJa;
        // res.ui.send({event:"deploy.store.viewStoresAllId",data:{}});

        res.ui.send({
            context : res.ui.context,
            event : "deploy.advertise.viewTableStore"
        });
    };

    $scope.toRight = function (index){
        if(index == undefined){
            return;
        }
        var currentLength = $rootScope.model.storesGroupSelectedId.length;
        var leftLength = $rootScope.model.storesAllIdCsv.length;
        if(leftLength <= 0 || index >= leftLength){
            return;
        }
        var leftStoreId = $rootScope.model.storesAllIdCsv[index].storeId;
        var rightStoreId;
        for(var i = 0 ; i < currentLength ; i ++){
            rightStoreId = $rootScope.model.storesGroupSelectedId[i].storeId;
            if(leftStoreId == rightStoreId){
                return;
            }
        }
        $rootScope.model.storesGroupSelectedId[currentLength] = $rootScope.model.storesAllIdCsv[index];
        $rootScope.model.storesAllIdCsv.splice(index,1);
    };
    $scope.toLeft = function (index){
        if(index == undefined){
            return;
        }
        var currentLength = $rootScope.model.storesAllIdCsv.length;
        var rightLength = $rootScope.model.storesGroupSelectedId.length;
        if(rightLength <= 0 || index >= rightLength){
            return;
        }
        var rightStoreId= $rootScope.model.storesGroupSelectedId[index].storeId;
        var leftStoreId;
        for(var i = 0 ; i < currentLength ; i ++){
            leftStoreId = $rootScope.model.storesAllIdCsv[i].storeId;
            if(leftStoreId == rightStoreId){
                return;
            }
        }
        $rootScope.model.storesAllIdCsv[currentLength] = $rootScope.model.storesGroupSelectedId[index];
        $rootScope.model.storesGroupSelectedId.splice(index,1);
    };

    $scope.select = function (option) {
        switch (option){
        case "addStore":
//          res.ui.send({event:"deploy.store.viewStores",
//              data:{}});
//          $rootScope.model.pageIndex = "storeAdd";
            $rootScope.model.pageIndex ="StoreEntryCreate";
//          $rootScope.editStoreCreateOrUpdate = "create";
            $rootScope.model.editStore.actionType = "StoreCreate";
//          $rootScope.dialogStore = "editStoreWatch";
            $scope.editStoreId = "";
//              $scope.editStoreNmEn = "";
            $scope.editStoreNmJp = "";
            $scope.editStoreIP = "";
            $scope.editStoreSecondaryIP = "";
            $scope.editStoreTel = "";
            res.ui.send({
                context : res.ui.context,
                event : "deploy.advertise.viewTableStore"
            });
            break;
        case "viewStores":
//          $rootScope.editStoreCreateOrUpdate = "update";
            $rootScope.model.editStore.actionType = "StoreUpdate";
            res.ui.send({
                context : res.ui.context,
                event : "deploy.advertise.viewTableStore"
            });
//              res.ui.send({event:"deploy.store.viewStores",
//                  data:{}});
            break;
        case "addStoreGroup":
//              $scope.editStoreGroupNameEn = "";
            $rootScope.model.editStore.groupEntryNameJa = "";
            $rootScope.model.storesGroupSelectedId = [];
//          $rootScope.editStoreGroupCreateOrUpdate = "create";
            $rootScope.model.editStore.actionType = "GroupCreate";
            res.ui.send({
                context : res.ui.context,
                event : "deploy.advertise.viewTableStore"
            });
//          res.ui.send({event:"deploy.store.viewStoresAllId",
//              data:{}});
            break;
        case "viewStoreGroups":
            $rootScope.model.pageIndex = "StoreGroupDetail";
            $rootScope.model.storesGroupCsv = [];
//          $rootScope.editStoreGroupCreateOrUpdate = "update";
            $rootScope.model.editStore.actionType = "GroupUpdate";
            res.ui.send({
                context : res.ui.context,
                event : "deploy.advertise.viewTableGroup"
            });
//          res.ui.send({event:"deploy.store.viewStoreGroups",
//              data:{}});
            break;
        }
    };

    $scope.storeRedirect = function(option) {
//      $rootScope.dialogStore = "";

        switch (option) {
        case "add":
            $scope.errorFlag = "";
//          if ($rootScope.editStoreCreateOrUpdate == "update") {
            if ($rootScope.model.editStore.actionType == "StoreUpdate") {
                if (!$scope.editeStoreCheck()) {
                    res.ui.root.prompt = $scope.errorFlag;
                    res.ui.root.secondPhrase = "AddStore.";
                    return;
                }

                $rootScope.secondPhrase = "UpdateStore.";
                $scope.commandType = "update";
            } else {
                if (!$scope.editeStoreCheck()) {
                    res.ui.root.prompt = $scope.errorFlag;
                    res.ui.root.secondPhrase = "AddStore.";
                    return;
                }

                $rootScope.secondPhrase = "AddStore.";
                $scope.commandType = "create";
            }
            break;
        case "delete":
//          if ($rootScope.editStoreCreateOrUpdate == "create") {
            if ($rootScope.model.editStore.actionType != "StoreCreate") {
                res.ui.root.deConfirm = "deConfirm";
            }
            return;
        default:
            $scope.commandType = "";
            return;
        }

        $scope.errorFlag = "";

        var pStoreId = $scope.editStoreId.trim();
        var pStoreNameJ = $scope.editStoreNmJp.trim();
        var pStorePriIp = $scope.editStoreIP.trim() == "" ? " " : $scope.editStoreIP.trim();
        var pStoreSecIp = $scope.editStoreSecondaryIP.trim() == "" ? " " : $scope.editStoreSecondaryIP.trim();
        var pStorePhone = $scope.editStoreTel.trim() == "" ? " " : $scope.editStoreTel.trim();

        var recordLine = "";
        recordLine = pStoreId + "," + pStoreNameJ + "," + pStorePriIp + "," + pStoreSecIp + "," + pStorePhone
                              + "," + " " + "," + " " + "," + " " + "," + " " + "," + " ";

        res.ui.send({
        context : res.ui.context,
            event : "deploy.advertise.editTableStore",
            data : {
                editType: $scope.commandType,
                storeInfo: recordLine
            }
        });
    }

    $scope.editeStoreCheck = function() {

        if (!$scope.editStoreId.trim()) {
            $scope.errorFlag = "emptyId";
            return false;
        } else {
            if (!res.model.isNumber($scope.editStoreId.trim())) {
                $scope.errorFlag = "isNotNum";
                return false;
            }
        }

        // $scope.editStoreNmEn = "MYB"+
        // $scope.editStoreId.trim();
        // if (!$scope.editStoreNmEn) {
        // $scope.errorFlag ="emptyNmEn";
        // return false;
        // }

        if (!$scope.editStoreNmJp.trim()) {
            $scope.errorFlag = "emptyNmJp";
            return false;
        }

        if (!$scope.editStoreIP.trim()) {
            // $scope.errorFlag ="emptyIP";
            // return false;
        } else {
            var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            if (!reg.test($scope.editStoreIP.trim())) {
                $scope.errorFlag = "errorIP";
                return false;
            }
        }

        if (!$scope.editStoreSecondaryIP.trim()) {
            // $scope.errorFlag ="emptySecondaryIP";
            // return false;
        } else {
            var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            if (!reg.test($scope.editStoreSecondaryIP
                    .trim())) {
                $scope.errorFlag = "errorSecondaryIP";
                return false;
            }
        }

        if (!res.model.isNumber($scope.editStoreTel.replace(new RegExp(/-/g), "").trim())) {
            $scope.errorFlag = "errorTel";
            return false;
        }

//      if ($rootScope.editStoreCreateOrUpdate != "update" && $scope.checkStoreId()) {
        if ($rootScope.model.editStore.actionType != "StoreUpdate" && $scope.checkStoreId()) {
            $scope.errorFlag = "ExistStoreId";
            return false;
        }

        return true;
    };

    $scope.checkStoreId = function() {
        var storeIdFlag = false;
        if (res.ui.root.model.storesCsv.length != 0) {
            for (var i = 0; i < res.ui.root.model.storesCsv.length; i++) {
                var storeId = res.ui.root.model.storesCsv[i].storeId;
                if ($scope.editStoreId.trim() == storeId) {
                    storeIdFlag = true;
                }
            }
        }

        return storeIdFlag;
    };

    $scope.groupEntryEdit = function (option) {

//      $scope.storeLine = {};
        // "2" regarded as a type of store group
//      $scope.storeLine.lineType = 2;
        var groupItems = "";
        var groupNameJa = "";
        $scope.errorFlag = "";
//      var storeGroupLength;
//          var storeGroupNameEn;
        var index = $rootScope.model.storesGroupCsv.selectIndex;

        // do check
        if (option != "delete") {
//              storeGroupNameEn = $scope.editStoreGroupNameEn;
            groupNameJa = $rootScope.model.editStore.groupEntryNameJa.trim();
            if (groupNameJa == undefined || groupNameJa.trim() == "") {
                $scope.errorFlag = "emptyGroupNameJa";
                return;
            }

//          storeGroupLength = $rootScope.model.storesGroupSelectedId.length;
//          if (storeGroupLength <= 1) {
//              return;
//          }

            for (var i = 0 ; i < $rootScope.model.storesGroupSelectedId.length; i++) {
                groupItems += $rootScope.model.storesGroupSelectedId[i].storeId + ",";
            }
        } else {
            if (index == undefined) {
                return;
            }
//              storeGroupNameEn = $rootScope.model.storesGroupCsv[index].storeGroupNameEn;
            groupNameJa = $rootScope.model.storesGroupCsv[index].entryNameJa;
            for (var i = 0 ; i < $rootScope.model.storesGroupCsv[index].entryStores.length ; i++) {
                groupItems += $rootScope.model.storesGroupCsv[index].entryStores[i] + ",";
            }
        }

        if (groupItems.lastIndexOf(",") != -1) {
            groupItems = groupItems.substring(0, groupItems.length - 1);
        }

//      $scope.storeLine.lineData = storeGroupNameJa + "," + storesIdStr;
        switch (option) {
        case "create":
//          if($rootScope.editStoreGroupCreateOrUpdate == "update"){
            if ($rootScope.model.editStore.actionType == "GroupUpdate") {
                $rootScope.secondPhrase = "UpdateStoreGroup.";
                $scope.commandType = "update";
                // $scope.storeLine.commandType = "U";
            } else {
                $rootScope.secondPhrase = "AddStoreGroup.";
                $scope.commandType = "create";
                // $scope.storeLine.commandType = "C";
            }
            break;
        case "delete":
            $rootScope.secondPhrase = "DeleteStoreGroup.";
//          $scope.storeLine.commandType = "D";
            $scope.commandType = "delete";
            break;
        default:
            $scope.commandType = "";
            return;
        }

        res.ui.send({
            context : res.ui.context,
            event:"deploy.advertise.editTableGroup",
            data:{
//              'lineType': $scope.storeLine.lineType,
                editType: $scope.commandType,
                groupData: groupNameJa + "," + groupItems
            }
        });

//      res.ui.send({event:"deploy.store.editStoreGroup",
//          data:{
////                'lineType': $scope.storeLine.lineType,
//              'editeType': $scope.commandType,
//              'groupDate': storeGroupNameJa + "," + storesIdStr
//          }
//      });
    };

        $scope.cancel = function () {
            $rootScope.dialog = "";
        };
        $scope.goBackDeploy = function () {
            $scope.errorFlag = "";
            $rootScope.model.pageIndex = undefined;
            $rootScope.model.storesAllIdCsv = [];
            $rootScope.model.storesGroupSelectedId = [];
            $rootScope.model.storesGroupCsv = [];
//          $rootScope.editStoreGroupCreateOrUpdate = "";
            $scope.editStoreId = "";
//          $scope.editStoreNmEn = "";
            $scope.editStoreNmJp = "";
            $scope.editStoreIP = "";
            $scope.editStoreTel ="";
        };
        /*
         * sort
         */
        $scope.sortStore = function(store){
            switch (store) {
            case "storeId":
                $rootScope.model.storesCsv.sort($scope.getSort($rootScope.storeIdUp,store));
                $rootScope.model.OriginalStoresCsv.sort($scope.getSort($rootScope.storeIdUp,store));

                $scope.selectStore(0);
                $rootScope.storeIdUp=!$rootScope.storeIdUp;
                $rootScope.storeJaNameUp = true;
                break;
            case "entryNameJa":
                $rootScope.model.storesCsv.sort($scope.getSortString($rootScope.storeJaNameUp,store));
                $rootScope.model.OriginalStoresCsv.sort($scope.getSortString($rootScope.storeJaNameUp,store));

                $scope.selectStore(0);
                $rootScope.storeJaNameUp=!$rootScope.storeJaNameUp;
                $rootScope.storeIdUp=true;
                break;
            default:
                return;
            }
        };
        $scope.getSort = function (orderBy, item) {
            if (orderBy) {
                return function(set1, set2) {
                    if (set1[item] < set2[item]) {
                        return -1;
                    } else if (set2[item] < set1[item]) {
                        return 1;
                    } else {
                        return 0;
                    }
                };
            } else {
                return function(set1, set2) {
                    if (set1[item] < set2[item]) {
                        return 1;
                    } else if (set2[item] < set1[item]) {
                        return -1;
                    } else {
                        return 0;
                    }
                };
            }
        };
        $scope.getSortString = function (orderBy, item) {
            if (orderBy) {
                return function(set1, set2) {
                    if (item == "entryNameJa") {
                        return set1[item].localeCompare(set2[item]);
                    } else {
                        if (typeof(set1[item]) == "undefined") {
                            set1[item] = "";
                        }
                        if (typeof(set2[item]) == "undefined") {
                            set2[item] = "";
                        }
                        return set1[item].localeCompare(set2[item]);
                    }
                };
            } else {
                return function(set1, set2) {
                    if (item == "entryNameJa") {
                        return set2[item].localeCompare(set1[item]);
                    } else {
                        if (typeof(set1[item]) == "undefined") {
                            set1[item] = "";
                        }
                        if (typeof(set2[item]) == "undefined") {
                            set2[item] = "";
                        }
                        return set2[item].localeCompare(set1[item]);
                    }
                };
            }
        };
        $scope.insetCanvas = function() {
            $(".SortStoreIdUp").each(function(){
                var canvas = document.createElement('canvas');
                canvas.width = 30;
                canvas.height = 16;
                res.canvas.polygon(canvas, [[3,6],[21,6],[12,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
                $(this).append(canvas);
            });
            $(".SortStoreIdDown").each(function(){
                var canvas = document.createElement('canvas');
                canvas.width = 30;
                canvas.height = 16;
                res.canvas.polygon(canvas, [[12,6],[3,16],[21,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
                $(this).append(canvas);
            });
            $(".SortStoreJaNameUp").each(function(){
                var canvas = document.createElement('canvas');
                canvas.width = 30;
                canvas.height = 16;
                res.canvas.polygon(canvas, [[3,6],[21,6],[12,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
                $(this).append(canvas);
            });
            $(".SortStoreJaNameDown").each(function(){
                var canvas = document.createElement('canvas');
                canvas.width = 30;
                canvas.height = 16;
                res.canvas.polygon(canvas, [[12,6],[3,16],[21,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
                $(this).append(canvas);
            });
        };
    }]);
