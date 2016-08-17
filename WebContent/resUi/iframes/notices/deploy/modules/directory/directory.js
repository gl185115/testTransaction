/*
 * Controller directory
 */
res.ui.controller("directory", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout) {

	$scope.$on("resIncludeLoaded", function(){	// wait for res-include complete
		$timeout(function(){ res.ui.canvas("#Directory"); }, 200);
	});

	$scope.selectFolder = function(folder) {
		$rootScope.model.active.folder = folder;
		$rootScope.model.active.taskIndex = undefined;
		$rootScope.model.deployDetail.detailView = false;
	};

}]);

