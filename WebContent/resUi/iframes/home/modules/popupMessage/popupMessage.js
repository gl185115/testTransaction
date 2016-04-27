/*
 * Controller
 */
res.ui.controller("PopupMessage", ["$scope", "$rootScope", "$timeout", "$filter", function ($scope, $rootScope, $timeout, $filter) {

	$scope.okaySet = function(selectFlag) {
		if (!$rootScope.model.transfering) {
			window.parent.res.ui.root.model.popup = "Wait";
			if (!window.parent.res.ui.root.$$phase) window.parent.res.ui.root.$digest();
			$rootScope.model.transfering = true;

			if ($rootScope.model.transferOption == "notices") {
				var msg = "deploy.notices.invokeTransfer";
			} else if ($rootScope.model.transferOption == "pickList") {
				var msg = "deploy.pickList.invokeTransfer";
			}

			res.ui.send({
				context : res.ui.context,
				event : msg,
				data : {
					transferOption : $rootScope.model.transferOption
				}
			});
		}

		$rootScope.PopupSetMessage = "";
	};

	$scope.voidSet = function() {
		$rootScope.PopupSetMessage = "";
	};

}]);
