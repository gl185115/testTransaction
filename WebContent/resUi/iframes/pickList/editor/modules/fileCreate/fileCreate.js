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
			$rootScope.model.pickList = new PickList();
			$rootScope.model.pickList.inputEmptyName = false;
			$rootScope.model.pickList.inValidName = false;
			$rootScope.model.pickList.fileNameLengthOver = false;
		}
	);

	$scope.submit = function() {
		$rootScope.model.pickList.inputEmptyName = false;
		$rootScope.model.pickList.inValidName = false;
		$rootScope.model.pickList.fileNameLengthOver = false;

		if ($scope.title.trim() == "" || typeof($scope.title) == "undefined") {
			$rootScope.model.pickList.inputEmptyName = true;
			return;
		}

//		if ($scope.title == "") {
//			$rootScope.model.pickList.inputEmptyName = true;
//			return;
//		}

//		if (!$scope.title.match(/^[^\\\/\*\?\"\<\>\：\|]*$/)) {
		if (!$scope.title.match(/^[^\\\/\*\?\"\<\>\:\|]*$/)) {
			$rootScope.model.pickList.inValidName = true;
			return;
		}
		var tiltelength = res.string.getLength($scope.title);
		if (tiltelength > res.config.fileNameLength) {
			$rootScope.model.pickList.fileNameLengthOver = true;
			return;
		}
		$rootScope.model.editor.selectedFile = undefined;
		$rootScope.model.editor.title = $scope.title;
//		$rootScope.model.editor.indexEdit = "editItems";
		$rootScope.model.editor.indexEdit = "editLayout";
		$rootScope.model.pickList = new PickList();
		$rootScope.dialog = "";
		$rootScope.model.pickList.OriginalItems = new PickList().items;
		$rootScope.model.pickList.OriginalCategories = new PickList().categories;
		$rootScope.model.pickList.OriginalLayout = new PickList().layout;
		$rootScope.pickListDisableClose = false;
		$rootScope.pickListDisableOpen = true;
	};

	$scope.cancel = function() {
		$rootScope.dialog = "";
	};

}]);
