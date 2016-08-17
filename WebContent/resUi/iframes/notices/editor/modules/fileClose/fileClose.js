/*
 * Controller
 */
res.ui.controller("fileClose", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.$watch(
		function(){
			return $rootScope.dialog;
		},
		function(newValue, oldValue){
			if (newValue == "fileClose") {
				$rootScope.model.notice.alreadyChanged = false;
				if ($rootScope.model.notice.selectedImage != $rootScope.model.notice.orgImage
						|| $rootScope.model.notice.items != $rootScope.model.notice.orgContent) {
					$rootScope.model.notice.alreadyChanged = true;
				}

				if ($rootScope.model.editor.title != $rootScope.model.notice.orgTitle1
						|| $rootScope.model.editor.title2 != $rootScope.model.notice.orgTitle2
						|| $rootScope.model.editor.expire != $rootScope.model.notice.orgExpire) {
					$rootScope.model.notice.alreadyChanged = true;
				}
				$rootScope.model.notice.fromSaveOrClose = undefined;
			}
		}
	);

//	$scope.okay = function(){
//		$rootScope.model.notice.selectedImage = undefined;
//		if ($rootScope.model.notice.alreadyChanged == true) {
//			$rootScope.model.notice.fromSaveOrClose = "close";
//			$rootScope.dialog = "fileSave";
//		} else {
//			$rootScope.model.editor.title = undefined;
//			$rootScope.model.editor.title2 = undefined;
//			$rootScope.model.editor.expire = undefined;
//			$rootScope.model.editor.fileName = undefined;
//			$rootScope.dialog = undefined;
//			$rootScope.model.editor.indexEdit = undefined;
//
//			$rootScope.noticeDisableClose = true;
//			$rootScope.noticeDisableOpen = false;
//		}
//	};

	$scope.close = function(){
		$rootScope.model.editor.title = undefined;
		$rootScope.model.editor.title2 = undefined;
		$rootScope.model.editor.fileName = undefined;
		$rootScope.model.editor.expire = undefined;
		$rootScope.model.editor.selectedIndex = undefined;
		$rootScope.model.editor.indexEdit = "itemList";

		$rootScope.model.notice.orgTitle1 = undefined;
		$rootScope.model.notice.orgTitle2 = undefined;
		$rootScope.model.notice.orgExpire = undefined;
		$rootScope.model.notice.orgImage = undefined;
		$rootScope.model.notice.orgContent = undefined;
		$rootScope.model.notice.items = undefined;
		$rootScope.model.notice.selectedImage = undefined;

		$rootScope.dialog = undefined;

		$rootScope.noticeDisableClose = true;
		$rootScope.noticeDisableOpen = false;
	};
	$scope.cancel = function(){
		$rootScope.dialog = undefined;
	}

}]);

