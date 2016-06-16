/*
 * Controller
 */
res.ui.controller("tasks", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){

	var scroll = undefined;
	var scrollTaskList = undefined;
	$scope.$on("resIncludeLoaded", function(){	// wait for res-include complete
		/*scroll = new IScroll("#wrapperViewList", {	// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true,	// or "zoom"
		});*/

		scrollTaskList = new IScroll("#wrapperTaskList", {	// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true,	// or "zoom"
		});
	});

   /* $scope.$watch(
        function () { return $rootScope.model.deployDetail.detailData; },
        function (newValue, oldValue) {
            $timeout(function () { scroll.refresh(); }, 100);
        }
    );*/

    $scope.$watch(
        function () { return $rootScope.model.active.folder; },
        function (newValue, oldValue) {
            $timeout(function () { scrollTaskList.refresh(); }, 100);
        }
    );

    $scope.$watch(
        function () { return $rootScope.model.doRefreshSchedule; },
        function (newValue, oldValue) {
            $timeout(function () { scrollTaskList.refresh(); }, 100);
        }
    );

    $scope.$watch(
        function () { return $rootScope.model.active.taskIndex; },
        function (newValue, oldValue) {
        	$timeout(function () { scrollTaskList.refresh(); }, 200);
        	if (newValue == undefined) $rootScope.model.deployDetail.detailView = false;
        }
    );

	$scope.selectTask = function(index){
		$scope.detailIndex = -1;
		$rootScope.model.active.taskIndex = index;
		$rootScope.model.deployDetail.detailView = false;

		var activeTask = $rootScope.model.resources[$rootScope.model.active.resource][$rootScope.model.active.folder][index].task;
		var taskStoreType = "";
		var taskStoreEntry = "";
		if (activeTask.target.store.toLowerCase() == "all" || activeTask.target.store.toLowerCase() == "全店舗") {
			taskStoreType = "all";
			taskStoreEntry = activeTask.target.store;
		} else if (activeTask.target.group) {
			taskStoreType = "group";
			taskStoreEntry = activeTask.target.group;
		} else if (!(activeTask.target.store.toLowerCase() == "all" || activeTask.target.store.toLowerCase() == "全店舗")) {
			taskStoreType = "store";
			taskStoreEntry = activeTask.target.store;
		}

		/*res.ui.send({
			context : res.ui.context,
			event: "deploy.pickList.getDeployDetail",
			data: {
				taskStoreType: taskStoreType,
				taskStoreEntry: taskStoreEntry,
				taskFileName: activeTask.filename,
				taskEffective: activeTask.effective
			}
		});*/
	};

	/*$scope.selectDetail = function(index){
		$scope.detailIndex = index;
	};

	$scope.saveDetailView = function() {

		$rootScope.dialog = "fileExplorer";

		res.ui.send({
			context : res.ui.context,
			event : "deploy.pickList.getSysFileTree",
			data : {
				basePath : $rootScope.model.fileExplorer.baseFolder,
				actionGo : "goOpen"
			}
		});
	};*/

}]);

