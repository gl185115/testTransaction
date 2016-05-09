/*
 * Controller fileCreate
 */
res.ui.controller("fileCreate", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.$watch(
		function() {
			return $rootScope.dialog;
		},
		function(newValue, oldValue) {
			if (newValue != "fileCreate") return;
			$scope.title = "";
			$rootScope.model.advertise = new Advertise();
			$rootScope.model.advertise.inputEmptyName = false;
			$rootScope.model.advertise.inValidName = false;
			$rootScope.model.advertise.fileNameLengthOver = false;
		}
	);

	$scope.submit = function() {
		$rootScope.model.advertise.inputEmptyName = false;
		$rootScope.model.advertise.inValidName = false;
		$rootScope.model.advertise.fileNameLengthOver = false;

		if ($scope.title.trim() == "" || typeof($scope.title) == "undefined") {
			$rootScope.model.advertise.inputEmptyName = true;
			return;
		}

//		if ($scope.title == "") {
//			$rootScope.model.pickList.inputEmptyName = true;
//			return;
//		}

//		if (!$scope.title.match(/^[^\\\/\*\?\"\<\>\：\|]*$/)) {
		if (!$scope.title.match(/^[^\\\/\*\?\"\<\>\:\|]*$/)) {
			$rootScope.model.advertise.inValidName = true;
			return;
		}
		var tiltelength = res.string.getLength($scope.title);
		if (tiltelength > res.config.fileNameLength) {
			$rootScope.model.advertise.fileNameLengthOver = true;
			return;
		}
		$rootScope.model.editor.selectedFile = undefined;
		$rootScope.model.editor.title = $scope.title;
		//$rootScope.model.editor.indexEdit = "editItems";
		$rootScope.model.editor.indexEdit = "editLayout";
		$rootScope.model.advertise = new Advertise();
		$rootScope.model.editor.interval = "";
		$rootScope.dialog = "";
		$rootScope.model.advertise.OriginalItems = new Advertise().items;
		$rootScope.model.advertise.OriginalCategories = new Advertise().categories;
		$rootScope.model.advertise.OriginalLayout = new Advertise().layout;
		$rootScope.pickListDisableClose = false;
		$rootScope.pickListDisableOpen = true;
	};

	$scope.cancel = function() {
		$rootScope.dialog = "";
	};

}]);
