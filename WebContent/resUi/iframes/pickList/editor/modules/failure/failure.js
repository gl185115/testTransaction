/*
 * Generic Error Popup with OK button
 */
res.ui.controller("failure", ["$scope", "$rootScope", function($scope, $rootScope) {

	angular.element(document).ready(function(){
		$scope.$watch(
			function() {
				return $rootScope.model.failure.cause;
			},
			function(cause, oldValue) {
				if (!cause) return;
				res.console("transaction failure: cause = " + cause);
			}
		);
	});

	$scope.recover = function() {
		$rootScope.model.failure.active = false;
		$rootScope.model.failure.service = "common";
		$rootScope.model.failure.cause = "cleared";
		$rootScope.model.failure.description = "";
	};

}]);