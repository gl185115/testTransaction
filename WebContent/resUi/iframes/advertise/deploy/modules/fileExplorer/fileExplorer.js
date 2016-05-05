/*
 * #PopupCustomer : Pop Up for Deploy->fileExplorer
 */
res.ui.controller("fileExplorer", ["$scope", "$rootScope", "$timeout",function($scope, $rootScope, $timeout){

    var scroll = undefined;
    $scope.$on("resIncludeLoaded", function(){  // wait for res-include complete
        scroll = new IScroll("#wrapperExportDetails", { // iScroll5
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true,   // or "zoom"
        });
    });

    $scope.$watch(
        function() { return $rootScope.model.fileExplorer.fileTree.length; },
        function(newValue, oldValue) {
            if (newValue == 0) return;
            $timeout(function() { scroll.refresh(); }, 200);
        }
    );

    $scope.$watch(
        function() { return $rootScope.model.fileExplorer.inputFileName; },
        function(newValue, oldValue) {
            if (newValue) {
                var filename = $rootScope.model.fileExplorer.indexFilePath;
                if (filename.endsWith(".csv")) {
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    if (newValue != filename) {
                        $rootScope.model.fileExplorer.indexSelected = -1;
                        $timeout(function() { scroll.refresh(); }, 100);
                    }
                }
            }
        }
    );

    $scope.initController = function() {
        $rootScope.model.fileExplorer.indexSelected = -1;
        $rootScope.model.fileExplorer.indexFilePath = "";
    };


    $scope.select = function(index) {
        $rootScope.model.fileExplorer.indexSelected = index;
        $rootScope.model.fileExplorer.indexFilePath = $rootScope.model.fileExplorer.fileTree[index].filename;

        var selectItem = $rootScope.model.fileExplorer.indexFilePath;
        if (selectItem && (selectItem.toLowerCase().endsWith('.csv'))) {
            $rootScope.model.fileExplorer.selectedFolder = selectItem.substring(0, selectItem.lastIndexOf("\\"));
            $rootScope.model.fileExplorer.inputFileName = selectItem.substring(selectItem.lastIndexOf("\\") + 1);
            $rootScope.model.fileExplorer.isLastNode = true;
        } else {
            $rootScope.model.fileExplorer.selectedFolder = selectItem;
            $rootScope.model.fileExplorer.isLastNode = false;
        }
    };

    $scope.folderOpen = function(index) {
        $scope.getFolderList($rootScope.model.fileExplorer.indexFilePath, "goOpen");
        $scope.initController();
    };

    $scope.folderback = function() {
        $scope.getFolderList($rootScope.model.fileExplorer.baseFolder, "goBack");
        $scope.initController();
    };

    $scope.getFolderList = function(base, actionType) {
        res.ui.send({
            context : res.ui.context,
            event : "deploy.advertise.getSysFileTree",
            data : {
                basePath : base,
                actionGo : actionType
            }
        });
    };

    $scope.lockingSave = function() {
        var inputName = $rootScope.model.fileExplorer.inputFileName;
        var folderPath = $rootScope.model.fileExplorer.baseFolder;
//      var folderPath = $rootScope.model.fileExplorer.selectedFolder;
//      if ($rootScope.model.fileExplorer.indexSelected == -1) {
//          folderPath = $rootScope.model.fileExplorer.baseFolder;
//      }

        $rootScope.model.saveDetail.isEmptyName = false;
        $rootScope.model.saveDetail.inValidName = false;
        $rootScope.model.saveDetail.isLengthOver = false;

//      if ($rootScope.model.fileExplorer.isFirstNode) {
        var splitEle = folderPath.split("\\");
        if ($rootScope.model.fileExplorer.isFirstNode || (splitEle.length == 2 && splitEle[1] == "")) {
            $rootScope.model.fileExplorer.csvSavePath = folderPath + inputName;
        } else {
            $rootScope.model.fileExplorer.csvSavePath = folderPath + "\\" + inputName;
        }

        if (inputName == "" || inputName === undefined) {
            $rootScope.model.saveDetail.isEmptyName = true;
            $rootScope.model.saveDetail.returnValue = "warning";
            $rootScope.dialog = "saveDetail";
            return;
        }

//      if (!inputName.match(/^[^\\\/\*\?\"\<\>\：\|]*$/)) {
        if (!inputName.match(/^[^\\\/\*\?\"\<\>\:\|]*$/)) {
            $rootScope.model.saveDetail.inValidName = true;
            $rootScope.model.saveDetail.returnValue = "warning";
            $rootScope.dialog = "saveDetail";
            return;
        }

        var nameLength = res.string.getLength(inputName);
        if (inputName.endsWith(".csv")) {
            nameLength = nameLength - ".csv".length;
        }
        if (nameLength > res.config.fileNameLength) {
            $rootScope.model.saveDetail.isLengthOver = true;
            $rootScope.model.saveDetail.returnValue = "warning";
            $rootScope.dialog = "saveDetail";
            return;
        }

        if (!$rootScope.model.fileExplorer.csvSavePath.toLowerCase().endsWith(".csv")) {
            $rootScope.model.fileExplorer.csvSavePath += ".csv";
        }

        var activeTask = $rootScope.model.resources[$rootScope.model.active.resource][$rootScope.model.active.folder][$rootScope.model.active.taskIndex].task;
        var taskStoreType = "";
        var taskStoreEntry = "";
        if (activeTask.target.store.toLowerCase() == "all" || activeTask.target.store.toLowerCase() == "全店") {
            taskStoreType = "all";
            taskStoreEntry = activeTask.target.store;
        } else if (activeTask.target.group) {
            taskStoreType = "group";
            taskStoreEntry = activeTask.target.group;
        } else if (!(activeTask.target.store.toLowerCase() == "all" || activeTask.target.store.toLowerCase() == "全店")) {
            taskStoreType = "store";
            taskStoreEntry = activeTask.target.store;
        }

        res.ui.send({
            context : res.ui.context,
            event: "deploy.advertise.saveDeployDetail",
            data: {
                deployName: activeTask.filename,
                deployEffective: activeTask.effective,
                deployType: taskStoreType,
                deployStoreEntry: taskStoreEntry,
                csvSavePath: $rootScope.model.fileExplorer.csvSavePath,
                csvSaveType: false
            }
        });

//      var activeTask = $rootScope.model.resources[$rootScope.model.active.resource][$rootScope.model.active.folder][$rootScope.model.active.taskIndex];
//
//      res.ui.send({ event: "deploy.detail.saveDeployDetail",
//          data: {
//              deployName: activeTask.task.filename,
//              deployEffective: activeTask.task.effective,
//              deployType: activeTask.task.target.store,
//              csvSavePath: $rootScope.model.fileExplorer.csvSavePath,
//              csvSaveType: false
//          }
//      });
        $rootScope.dialog = "";
    };

    $scope.cancel = function() {
        $rootScope.model.fileExplorer.indexSelected = -1;
        $rootScope.model.fileExplorer.inputFileName = "";
        $rootScope.dialog = "";
    };

}]);
