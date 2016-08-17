/*
 * Controller
 */
res.ui.controller("PopupMessage", ["$scope", "$rootScope", "$timeout", "$filter", function ($scope, $rootScope, $timeout, $filter) {

	$scope.cancel = function(resultCode) {
		$rootScope.popupMessage = "";
		if (resultCode == "success") {
			$rootScope.model.resultCode = undefined;
			$rootScope.model.pageIndex = undefined;
			$rootScope.dialog = "editStore";
		}
	};

} ]);
