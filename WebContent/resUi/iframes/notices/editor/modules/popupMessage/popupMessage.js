/*
 * Controller
 */
res.ui.controller("PopupMessage", ["$scope", "$rootScope", "$timeout", "$filter", function ($scope, $rootScope, $timeout, $filter) {
	
	$scope.cancel = function (resultCode) {
        $rootScope.popupMessage = "";
    };
    }]);
