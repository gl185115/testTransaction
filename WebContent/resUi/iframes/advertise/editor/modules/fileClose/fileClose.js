/*
 * Controller
 */
res.ui.controller("fileClose", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.$watch(
		function() {
			return $rootScope.dialog;
		},
		function(newValue, oldValue) {
			if (newValue == "fileClose") {
				if ($rootScope.itemSelected) {
					if ($rootScope.itemSelected.isblank) {
						$rootScope.model.failure.active = true;
						$rootScope.model.failure.service = "picklist";
						$rootScope.model.failure.cause = "blank";
						$rootScope.dialog = "";
						return;
					}
				}

				if ($rootScope.model.pickList.doCanceled) return;

				$rootScope.model.pickList.alreadyChanged = false;
				if ($rootScope.model.pickList.items.length == $rootScope.model.pickList.OriginalItems.length) {
					for (var i = 0; i < $rootScope.model.pickList.items.length; i++) {
						if (JSON.stringify($rootScope.model.pickList.items[i]) != JSON.stringify($rootScope.model.pickList.OriginalItems[i])) {
							$rootScope.model.pickList.alreadyChanged = true;
							break;
						}
					}
				} else {
					$rootScope.model.pickList.alreadyChanged = true;
				}

				if (!$rootScope.model.pickList.alreadyChanged) {
					if ($rootScope.model.pickList.categories.length == $rootScope.model.pickList.OriginalCategories.length) {
						for (var i = 0; i < $rootScope.model.pickList.categories.length; i++) {
							if (JSON.stringify($rootScope.model.pickList.categories[i]) != JSON.stringify($rootScope.model.pickList.OriginalCategories[i])) {
								$rootScope.model.pickList.alreadyChanged = true;
								break;
							}
						}
					} else {
						$rootScope.model.pickList.alreadyChanged = true;
					}
				}
				if (!$rootScope.model.pickList.alreadyChanged) {
					if ($rootScope.model.pickList.layout.length == $rootScope.model.pickList.OriginalLayout.length) {
						for (var i = 0; i < $rootScope.model.pickList.layout.length; i++) {
							if (JSON.stringify($rootScope.model.pickList.layout[i]) != JSON.stringify($rootScope.model.pickList.OriginalLayout[i])) {
								$rootScope.model.pickList.alreadyChanged = true;
								break;
							}
						}
					} else {
						$rootScope.model.pickList.alreadyChanged = true;
					}
				}

				$rootScope.model.pickList.fromSaveOrClose = undefined;
			}
		}
	);

//	$scope.okay = function(){
//		if ($rootScope.model.pickList.alreadyChanged) {
//			$rootScope.model.pickList.fromSaveOrClose = "close";
//			$rootScope.dialog = "fileSave";
//		} else {
//			$rootScope.model.editor.title = undefined;
//			$rootScope.model.editor.indexEdit = "";
//			$rootScope.model.pickList = undefined;
//			$rootScope.dialog = undefined;
//			$rootScope.itemSelected = undefined;
//			$rootScope.indexCategory = undefined;
//
//			$rootScope.pickListDisableClose = true;
//			$rootScope.pickListDisableOpen = false;
//		}
//	};

	$scope.close = function() {
		$scope.doCanceled = true;
		$rootScope.model.editor.title = undefined;
		$rootScope.model.editor.selectedFile = undefined;
		$rootScope.model.editor.selectedIndex = undefined;
		$rootScope.model.editor.indexEdit = "itemList";
		$rootScope.model.pickList = undefined;
		$rootScope.dialog = undefined;
		$rootScope.itemSelected = undefined;
		$rootScope.indexCategory = undefined;

		$rootScope.pickListDisableClose = true;
		$rootScope.pickListDisableOpen = false;
	};

	$scope.cancel = function() {
		$rootScope.dialog = undefined;
	}
}]);

