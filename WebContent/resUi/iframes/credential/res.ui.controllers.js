/*
 * Credential Controllers
 */
res.ui.controller("operatorId", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){
	$timeout(function(){	// need to wait for model setup
		$scope.$watch(
			function(){ return $rootScope.model.status; },
			function(status, oldValue){
				if (status != "initial" && status != "rejected") return;
				$scope.number = "";
				$scope.$broadcast("resTenkeyReset");
			});
		}, 500);
	$scope.$on("resTenkeyEntered", function(){
		res.ui.send({ event: "credential.signIn.userId", data: $scope.number});
	});
}]);

/*
 * Passcode Form
 */
res.ui.controller("passcode", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){
	$timeout(function(){	// need to wait for model setup
		$scope.$watch(
			function(){ return $rootScope.model.status; },
			function(status, oldValue){
				if (status != "initial" && status != "rejected") return;
				$scope.number = "";
				$scope.$broadcast("resTenkeyReset");
			});
		}, 500);
	$scope.$on("resTenkeyEntered", function(){
		res.ui.send({ event: "credential.signIn.passcode", data: $scope.number});
	});
}]);
