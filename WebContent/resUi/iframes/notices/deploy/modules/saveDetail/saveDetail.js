/*
 * #PopupCustomer : Pop Up for Deploy->saveDetail
 */
res.ui.controller("saveDetail", ["$scope", "$rootScope", "$timeout",function($scope, $rootScope, $timeout){

	$scope.cancel = function() {
		$rootScope.model.fileExplorer.indexSelected = -1;
		$rootScope.model.fileExplorer.inputFileName = "";
		$rootScope.model.fileExplorer.selectedFolder = "";
		$rootScope.dialog = "";
	};

	$scope.okay = function() {
		$rootScope.model.fileExplorer.indexSelected = -1;
		$rootScope.model.fileExplorer.inputFileName = "";
		$rootScope.model.fileExplorer.selectedFolder = "";
		$rootScope.dialog = "";
	};

	$scope.back = function() {
		$rootScope.model.fileExplorer.selectedFolder = "";
		$rootScope.model.saveDetail.isOverwrite == false;
		$rootScope.dialog = "fileExplorer";
	};

	$scope.overwrite = function() {
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
			event: "deploy.notices.saveDeployDetail",
			data: {
				deployName: activeTask.filename,
				deployEffective: activeTask.effective,
				deployType: taskStoreType,
				deployStoreEntry: taskStoreEntry,
				csvSavePath: $rootScope.model.fileExplorer.csvSavePath,
				csvSaveType: $rootScope.model.saveDetail.isOverwrite
			}
		});
	};

}]);
