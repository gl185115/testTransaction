/*
 * Controller
 */
res.ui.controller("editNotice", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){
	$scope.$watch(
		function(){ return $rootScope.editFileName; },
		function(newValue, oldValue){
			if (!newValue) return;
		}
	);
}]);

