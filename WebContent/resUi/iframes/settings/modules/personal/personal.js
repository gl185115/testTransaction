/*
 * Personal Settings
 */
res.ui.controller("personal", ["$scope", "$rootScope", function($scope, $rootScope){
	
	$scope.trainingToggle = function(){
		res.ui.send({ event: "settings.training.setMode", data: !$rootScope.model.training });
	};
	
}]);

