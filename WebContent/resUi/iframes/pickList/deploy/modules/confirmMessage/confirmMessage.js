/*
 * Controller
 */
res.ui.controller("confirmMessage", ["$scope", "$rootScope", "$timeout", "$filter", function ($scope, $rootScope, $timeout, $filter) {

	$scope.Okay = function() {
		$rootScope.model.save();
		$rootScope.model.active.item = undefined;
		$rootScope.model.message = "SaveInvoke";
		var jsonSchedule = JSON.stringify({ schedule : $rootScope.model.schedule });

		res.console("res.ui deploy menu save: model.schedule = " + jsonSchedule);

		res.ui.send({
			context : res.ui.context,
			event : "deploy.pickList.setSchedule",
			data : {
				schedule : jsonSchedule,
				resource : "pickList"
			}
		});

		$rootScope.confirmMessage = "";
	};

	$scope.voidSelect = function() {
		$rootScope.confirmMessage = "";
	};

}]);
