/*
 * Controller
 */
res.ui.controller("editTask", ["$scope", "$rootScope", "$timeout", "$filter", function ($scope, $rootScope, $timeout, $filter) {
    var scroll = undefined;
    var storescroll = undefined;
    var deployTimeVar = 0;
//  var groups = ["pickLists", "notices", "options", "usability","advertise"];

    $scope.$on("resIncludeLoaded", function () {    // wait for res-include complete
        scroll = new IScroll("#wrapperFiles", {// iScroll5
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true, // or "zoom"
        });
        storeSelectorcroll = new IScroll("#wrapperStoreSelectorView", {// iScroll5
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true, // or "zoom"
        });
        res.ui.canvas("#EditTask");
    });

    $scope.$watch(
        function() {
            return $rootScope.dialog;
        },
        function(dialog, oldValue) {
            if (dialog != "editTask") return;
            var model = $rootScope.model;

            model.editor.files = [];
            res.ui.send({
                context : res.ui.context,
                event : "file.list",
                data : {
                    folder : model.active.resource
                }
            });

            $scope.editDate = "";
            $scope.editTime = "";
            $scope.indexLevel1 = -1;
            $scope.indexLevel2 = -1;
            $scope.taskFilename = undefined;
            $scope.warning = undefined;
            var nowDate = new Date();
            $scope.deployTimeHour = res.model.parseHHMM(nowDate.getHours());
            $scope.deployTimeMinute = res.model.parseHHMM(nowDate.getMinutes());

            if (model.editTask.deployCategories) {
                $scope.editTargetName = model.editTask.deployCategories[0].category;
            }

//          $scope.selectLevel1(0);
            if (model.active.taskIndex != undefined) {
                // editTask -> update
                var task = model.resources[model.active.resource][model.active.folder][model.active.taskIndex].task;
                res.console("res.ui deploy editTask : updated original task = " + JSON.stringify(task));
                if (task.target.store.toLowerCase() == "all" || task.target.store.toLowerCase() == "全店舗") {
                    for (var i = 0; i < model.editTask.deployCategories.length; i++) {
                        if (model.editTask.deployCategories[i].levelKey == "all") {
                            $scope.editTargetName = model.editTask.deployCategories[i].category;
                            $scope.indexLevel1 = i;
                            $scope.indexLevel2 = -1;
                            break;
                        }
                    }
                } else if (task.target.group) {
                    $scope.editTargetName = task.target.group;
                    for (var i = 0; i < model.editTask.deployCategories.length; i++) {
                        if (model.editTask.deployCategories[i].levelKey == "group") {
                            var iGroup = model.editTask.deployCategories[i].storeEntries;
                            for (var j = 0; j < iGroup.length; j++) {
                                if (task.target.group == iGroup[j].entryNameJa) {
                                    $scope.indexLevel1 = i;
                                    $scope.indexLevel2 = j;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                } else if (!(task.target.store.toLowerCase() == "all" || task.target.store.toLowerCase() == "全店舗")) {
                    $scope.editTargetName = task.target.storeNameJa;
                    for (var i = 0; i < model.editTask.deployCategories.length; i++) {
                        if (model.editTask.deployCategories[i].levelKey == "store") {
                            var iGroup = model.editTask.deployCategories[i].storeEntries;
                            for (var j = 0; j < iGroup.length; j++) {
                                if (task.target.store == iGroup[j].entryNameJa) {
                                    $scope.indexLevel1 = i;
                                    $scope.indexLevel2 = j;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

//              $scope.editTargetName = task.target.store;
                $scope.editDate = task.effective.slice(0, 4) + task.effective.slice(5, 7) + task.effective.slice(8, 10);
                // $scope.editTime = task.effective.slice(11, 13) + task.effective.slice(14, 16);
                $scope.deployTimeHour = res.model.parseHHMM(task.effective.slice(11, 13));
                $scope.deployTimeMinute = res.model.parseHHMM(task.effective.slice(14, 16));
                $scope.taskFilename = task.filename;

                if ($scope.taskFilename.endsWith(".js")) {
                    $scope.taskFilename = $scope.taskFilename.substring(0, $scope.taskFilename.length - 3);
                }
            }
        }
    );

    $scope.$watch(
        function () {
            return $rootScope.model.editor.files.length;
        },
        function (newValue, oldValue) {
            $timeout(function () {
                scroll.refresh();
            }, 100);
        }
    );

    $scope.$watch(
        function() {
            return $rootScope.model.indexEntry;
        },
        function(newValue, oldValue) {
            if (newValue == 0) {
                $timeout(function() { storeSelectorcroll.refresh(); }, 100);
            }
        }
    );

    $scope.$watch(
        function() {
            return $scope.indexLevel1;
        },
        function(newValue, oldValue) {
            $timeout(function() { storeSelectorcroll.refresh(); }, 10);
        }
    );

    $scope.$watch(
        function() {
            return $scope.doShowDetail;
        },
        function(newValue, oldValue) {
            $timeout(function() { storeSelectorcroll.refresh(); }, 10);
        }
    );

    $scope.$watch(
        function() {
            return $scope.indexLevel2;
        },
        function(newValue, oldValue) {
            $timeout(function() { storeSelectorcroll.refresh(); }, 10);
        }
    );

//    $scope.$watch(
//            function () {
//                return $rootScope.language;
//            },
//            function (newValue, oldValue) {
//                if ($rootScope.model.stores.length == 0 || $scope.indexStore < 0)
//                    return;
//                $scope.deployTarget = ($rootScope.language == "ja") ? ($rootScope.model.stores[$scope.indexStore].store.ja ||$rootScope.model.stores[$scope.indexStore].store.jp): $rootScope.model.stores[$scope.indexStore].store.en;
//            }
//    );

    $scope.selectTaskFile = function(file) {
        $scope.taskFilename = file;
        if ($rootScope.model.indexEntry == 0) {
            $rootScope.model.indexEntry = 1;
        }
    };

    $scope.selectEditInput = function(index) {
        $rootScope.model.indexEntry = index;
        // $scope.indexStore = -1;
        if (index == 0) {
            $("#deployStoreValue").focus();
        } else if (index == 1) {
            $("#DeployDateValue").focus();
        } else if (index == 2) {
            $scope.selectDeployTime(1);
        } else if (index == 3) {
            $scope.selectDeployTime(2);
        }
    };

    $scope.selectDeployTime = function(index) {
        $rootScope.model.indexEntry = index;
        // $scope.indexStore = -1;
        if (index == 1) {
            $("#deployTimeHour").focus();
        } else if (index == 2) {
            $("#deployTimeMinute").focus();
        }
    };

    $scope.selectLevel1 = function(index) {
        // $scope.indexStore = index;
        $scope.editTargetName = "";
        $scope.indexLevel2 = -1;
        if (index == 0) {
            $scope.editTargetName = $rootScope.model.editTask.deployCategories[index].category;
            $scope.indexLevel1 = index;
            return;
        }

        if ($scope.indexLevel1 == index) {
            if ($scope.doShowDetail) {
                $scope.doShowDetail = false;
            } else {
                $scope.doShowDetail = true;
            }
        } else {
            $scope.doShowDetail = true;
        }

        $scope.indexLevel1 = index;
//      $scope.indexLevel2 = -1;
    };

    $scope.selectLevel2 = function(index) {

        var selectedDeployStoreEntries = [];
        $scope.indexLevel2 = index;

        if ($scope.indexLevel1) {
            selectedDeployStoreEntries = $rootScope.model.editTask.deployCategories[$scope.indexLevel1].storeEntries[index];
            $scope.editTargetName = selectedDeployStoreEntries.StoreName;
        }
    };

    $scope.StoreOkay = function() {

        $rootScope.model.indexEntry = 1;
        $scope.selectEditInput($rootScope.model.indexEntry);
//      $scope.selectEntry($rootScope.model.indexEntry);
//      $("#DeployDateValue").focus();

    };

    $scope.StoreCancel = function() {
        $rootScope.model.indexEntry = -1;
        $scope.editTargetName = "";
    };

    $scope.submit = function() {
        var model = $rootScope.model;
        var task = {};
        var status = { total: 0, ready:0, opened:0 };
        var year = parseInt($scope.editDate.slice(0, 4), 10);
        var month = parseInt($scope.editDate.slice(4, 6), 10);
        var day = parseInt($scope.editDate.slice(6, 8), 10);
        var hour = 0, minute = 0;
        hour = parseInt($scope.deployTimeHour);
        minute = parseInt($scope.deployTimeMinute);
        var date = new Date(year, month - 1, day, hour, minute);
        var effectiveTask = $rootScope.model.resources[$rootScope.model.active.resource].effective;
        var scheduledTask = $rootScope.model.resources[$rootScope.model.active.resource].scheduled;
        
        if ((typeof $scope.editTargetName === "undefined") || $scope.editTargetName === "") {
//          alert("適用店舗を選択してください");
            $scope.warning = "missingTarget";
            return;
        }
        if (!res.model.isNumber($scope.deployTimeHour) || !res.model.isNumber($scope.deployTimeMinute)) {
//          alert("無効な日付または時刻です！もう一度確認してください。");
            $scope.warning = "invalidDateTime";
            return;
        }
        if(hour > 23){
            $scope.warning = "invalidDateTime";
            return;
        }
        if(minute > 59){
            $scope.warning = "invalidDateTime";
            return;
        }
        if (date === "Invalid Date" || (!$filter("resDateValidation")(month, "M") || !$filter("resDateValidation")(day, "D")
                || !$filter("resDateValidation")(hour, "H") || !$filter("resDateValidation")(minute, "m"))) {
//          alert("無効な日付または時刻です！もう一度確認してください。");
            $scope.warning = "invalidDateTime";
            return;
        }
        if (date <= new Date()) {
//          alert("時刻は既に過ぎってる！もう一度確認してください。");
            $scope.warning = "passedDateTime";
            return;
        }
        task.effective = fixDigits(year, 4) + "-" + fixDigits(month, 2) + "-" + fixDigits(day, 2) + "T" + fixDigits(hour, 2) + ":" + fixDigits(minute, 2);
        if (!$scope.taskFilename) {
//          alert("構成を選択してください。");
            $scope.warning = "missingFile";
            return;
        }
        for(var i=0;i<effectiveTask.length;i++){
            if ($scope.taskFilename==effectiveTask[i].task.filename.substring(0, effectiveTask[i].task.filename.length - 3)) {
//          alert("適用中のファイルが選択できません。");
            $scope.warning = "effectiveFile";
            return;
        }
         }

        if (!$scope.taskFilename.endsWith(".js")) {
            task.filename = $scope.taskFilename + ".js";
        } else {
            task.filename = $scope.taskFilename;
        }

        if ($scope.indexLevel1 == 0 || $scope.indexLevel1 == -1) {
            status.total = $rootScope.model.editTask.deployCategories[0].storeEntries.length;
            task.target = { store: "All", group: "", workstation: "All", storeNameJa: $scope.editTargetName };
        } else if ($scope.indexLevel1 == 1) {
            var entryEdit = $rootScope.model.editTask.deployCategories[1].storeEntries[$scope.indexLevel2];
            status.total = 1;
            task.target = { store: entryEdit.StoreId, group: "", workstation: "All", storeNameJa: entryEdit.StoreName };
        } /*else if ($scope.indexLevel1 == 2) {
            var entryEdit = $rootScope.model.editTask.deployCategories[2].storeEntries[$scope.indexLevel2];
            status.total = 1;
            task.target = { store: entryEdit.storeId, group: "", workstation: "All", storeNameJa: entryEdit.StoreName };
        }*/
        for(var i=0;i<scheduledTask.length;i++){
			if(task.effective ===scheduledTask[i].task.effective){
				if(task.target.store==scheduledTask[i].task.target.store){
					$scope.warning = "effectiveDate";
					return;
				}
			}
		}
        
        
        if (model.active.taskIndex != undefined) {
            $rootScope.model.remove();
        }
//      res.ui.send({ event: "schedule.getNotices", data: {} });
//      $scope.updateNewTask(task);

        $rootScope.model.add(task, status);
        $rootScope.model.sort();

        $rootScope.model.doRefreshSchedule = true;
        $rootScope.model.active.item="Modified";
        $rootScope.dialog = "";
//      if($rootScope.model.active.resource=="notices"){
//          res.ui.send({ event: "schedule.resetNoticeDate", data: {filename:task.filename ,effective:task.effective} });
//      }
        function fixDigits(number, digits) {
            number += "";
            if (number.length < digits) {
                number = "0" + number;
            }
            return number;
        }

        $scope.indexLevel1 = -1;
        $scope.indexLevel2 = -1;
        $scope.doShowDetail = false;
    };

//  $scope.updateNewTask = function(task) {
//      if ($rootScope.model.active.resource != "notices") return;
//
//      $rootScope.model.doRefreshSchedule = false;
//      res.ui.send({ event: "schedule.getNotices", data: {} });
//      for (var n = 0; n < $rootScope.model.noticesData.length; n++) {
//          var notice = $rootScope.model.noticesData[n];
//          if (!notice) continue;
//
//          if (notice.filename == task.filename) {
//              if (!notice.expire || notice.expire.toLowerCase() == "null") {
//                  task.expire = "";
//              } else {
//                  task.expire = notice.expire;
//              }
//              break;
//          }
//      }
//  };

//      $scope.$watch(
//          function(){ return window.parent.res.ui.root.pageOnBoard; },
//          function(newValue, oldValue){
//              if(newValue != "deploy") {
////                    clearInterval(res.ui.root.model.deployStatusTimer);
////                    res.ui.root.model.isRefresh = false;
//              }
//      });

    $scope.cancel = function () {
        if($rootScope.model.active.item!="Modified"){
            $rootScope.model.active.item=undefined;
        }
        $rootScope.dialog = "";
    };

    $scope.editDeployTime = function(event){
        deployTimeVar = event;
    };

    $scope.editTimeUp = function(){
        var deployTimeHourVar = $scope.deployTimeHour;
        var deployTimeMinuteVar = $scope.deployTimeMinute;
        if(deployTimeVar == "deployTimeHour"){
            if(deployTimeHourVar == "" || deployTimeHourVar == undefined){
                deployTimeHourVar = "23";
            }
            if(deployTimeMinuteVar == "" || deployTimeMinuteVar == undefined){
                deployTimeMinuteVar = "00";
            }
            deployTimeHourVar = parseInt(deployTimeHourVar) + 1;
        }else if (deployTimeVar == "deployTimeMinute"){
            if(deployTimeHourVar == "" || deployTimeHourVar == undefined){
                deployTimeHourVar = "00";
            }
            if(deployTimeMinuteVar == "" || deployTimeMinuteVar == undefined){
                deployTimeMinuteVar = "59";
            }
            deployTimeMinuteVar = parseInt(deployTimeMinuteVar) + 1;
        }
        if(parseInt(deployTimeHourVar) > 23){
            deployTimeHourVar = 0;
        }
        if(parseInt(deployTimeMinuteVar) > 59){
            deployTimeMinuteVar = 0;
        }
        $scope.deployTimeHour = res.model.parseHHMM(deployTimeHourVar);
        $scope.deployTimeMinute = res.model.parseHHMM(deployTimeMinuteVar);

    };

    $scope.editTimeDown = function(editTimeHour,editTimeMinute){
        var deployTimeHourVar = $scope.deployTimeHour;
        var deployTimeMinuteVar = $scope.deployTimeMinute;
        if(deployTimeVar == "deployTimeHour"){
            if(deployTimeHourVar == "" || deployTimeHourVar == undefined){
                deployTimeHourVar = "01";
            }
            if(deployTimeMinuteVar == "" || deployTimeMinuteVar == undefined){
                deployTimeMinuteVar = "00";
            }
            deployTimeHourVar = parseInt(deployTimeHourVar) - 1;
        }else if (deployTimeVar == "deployTimeMinute"){
            if(deployTimeHourVar == "" || deployTimeHourVar == undefined){
                deployTimeHourVar = "00";
            }
            if(deployTimeMinuteVar == "" || deployTimeMinuteVar == undefined){
                deployTimeMinuteVar = "01";
            }
            deployTimeMinuteVar = parseInt(deployTimeMinuteVar) - 1;
        }
        if(parseInt(deployTimeHourVar) < 0){
            deployTimeHourVar = 23;
        }
        if(parseInt(deployTimeMinuteVar) < 0){
            deployTimeMinuteVar = 59;
        }
        $scope.deployTimeHour = res.model.parseHHMM(deployTimeHourVar);
        $scope.deployTimeMinute = res.model.parseHHMM(deployTimeMinuteVar);
    };

    }]);
