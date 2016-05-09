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
						$rootScope.model.failure.service = "advertise";
						$rootScope.model.failure.cause = "blank";
						$rootScope.dialog = "";
						return;
					}
				}

				if ($rootScope.model.advertise.doCanceled) return;

				$rootScope.model.advertise.alreadyChanged = false;
				if ($rootScope.model.advertise.items.length == $rootScope.model.advertise.OriginalItems.length) {
					for (var i = 0; i < $rootScope.model.advertise.items.length; i++) {
						if (JSON.stringify($rootScope.model.advertise.items[i]) != JSON.stringify($rootScope.model.advertise.OriginalItems[i])) {
							$rootScope.model.advertise.alreadyChanged = true;
							break;
						}
					}
				} else {
					$rootScope.model.advertise.alreadyChanged = true;
				}

				if (!$rootScope.model.advertise.alreadyChanged) {
					if ($rootScope.model.advertise.categories.length == $rootScope.model.advertise.OriginalCategories.length) {
						for (var i = 0; i < $rootScope.model.advertise.categories.length; i++) {
							if (JSON.stringify($rootScope.model.advertise.categories[i]) != JSON.stringify($rootScope.model.advertise.OriginalCategories[i])) {
								$rootScope.model.advertise.alreadyChanged = true;
								break;
							}
						}
					} else {
						$rootScope.model.advertise.alreadyChanged = true;
					}
				}
				if (!$rootScope.model.advertise.alreadyChanged) {
					if ($rootScope.model.advertise.layout.length == $rootScope.model.advertise.OriginalLayout.length) {
						for (var i = 0; i < $rootScope.model.advertise.layout.length; i++) {
							if (JSON.stringify($rootScope.model.advertise.layout[i]) != JSON.stringify($rootScope.model.advertise.OriginalLayout[i])) {
								$rootScope.model.advertise.alreadyChanged = true;
								break;
							}
						}
					} else {
						$rootScope.model.advertise.alreadyChanged = true;
					}
				}

				$rootScope.model.advertise.fromSaveOrClose = undefined;
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
		$rootScope.model.advertise = undefined;
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

