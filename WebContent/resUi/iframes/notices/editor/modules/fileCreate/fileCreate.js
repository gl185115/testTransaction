/*
 * Controller
 */
res.ui.controller("fileCreate", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.$watch(
		function(){ return $rootScope.dialog; },
		function(newValue, oldValue){
			if (newValue != "fileCreate") return;
			$scope.fileName = "";
			$rootScope.model.notice.inputEmptyName = false;
			$rootScope.model.notice.inValidName = false;
			$rootScope.model.notice.fileNameLengthOver = false;
		}
	);
	$scope.submit = function(){
		$rootScope.model.notice.inputEmptyName = false;
		$rootScope.model.notice.inValidName = false;
		$rootScope.model.notice.fileNameLengthOver = false;

		if ($scope.fileName.trim() == "" || typeof($scope.fileName) == "undefined") {
			$rootScope.model.notice.inputEmptyName = true;
			return;
		}

//		if ($scope.fileName == "") {
//			$rootScope.model.notice.inputEmptyName = true;
//			return;
//		}

//		if (!$scope.fileName.match(/^[^\\\/\*\?\"\<\>\：\|]*$/)) {
		if (!$scope.fileName.match(/^[^\\\/\*\?\"\<\>\:\|]*$/)) {
			 $rootScope.model.notice.inValidName = true;
			 return;
		}

		var tiltelength = res.string.getLength($scope.fileName);
		if (tiltelength > res.config.fileNameLength) {
			 $rootScope.model.notice.fileNameLengthOver = true;
			 return;
		}
		$rootScope.model.editor.selectedFile=undefined;
		$rootScope.model.editor.title = res.string.truncate($scope.fileName, 20);
		$rootScope.model.editor.fileName = $scope.fileName;
		$rootScope.dialog = "";
		$rootScope.model.editor.indexEdit = "editItems";
		$rootScope.model.notice.items = "";
		$rootScope.model.notice.OriginalItems = "";
		$rootScope.model.notice.selectedImage="";

		$rootScope.noticeDisableClose = false;
		$rootScope.noticeDisableOpen = true;
	};
	$scope.cancel = function(){
		$rootScope.dialog = undefined;
	};

}]);
