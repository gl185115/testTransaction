/*
 * Controller
 */
res.ui.controller("fileSave", ["$scope", "$rootScope", function ($scope, $rootScope) {

	$scope.$watch(
		function () {
			return $rootScope.dialog;
		},
		function (newValue, oldValue) {
			if (newValue === "fileSave") {
				$rootScope.model.notice.status = "input";
				$rootScope.model.notice.alreadyExists = false;
				$rootScope.model.notice.inputEmptyName = false;
				$rootScope.model.notice.inValidName = false;
				$rootScope.model.notice.fileNameLengthOver = false;
//				$scope.filename=$rootScope.model.editor.fileName;
//				if($scope.filename==""||typeof ($scope.filename)=="undefined")
//				$scope.filename = $rootScope.model.editor.title;
				$scope.filename = ($rootScope.model.editor.fileName + ".js").trim();
				$scope.filename = $scope.filename.substring(0, $scope.filename.length - 3);
				$scope.overwrite = false;
			}
		}
	);

	$scope.okay = function () {

		$rootScope.model.notice.alreadyExists = false;
		$rootScope.model.notice.inputEmptyName = false;
		$rootScope.model.notice.inValidName = false;
		$rootScope.model.notice.fileNameLengthOver = false;
		var picturename = (typeof $rootScope.model.notice.selectedImage === 'undefined') ? '' : encodeURIComponent($rootScope.model.notice.selectedImage);

		if (typeof($scope.filename) == "undefined" || $scope.filename.trim() == "") {
			$rootScope.model.notice.inputEmptyName = true;
			return;
		}
//		if (typeof ($scope.filename) === "undefined" || $scope.filename === "") {
//			$rootScope.model.notice.inputEmptyName = true;
//			return;
//		}

//		if (!$scope.filename.match(/^[^\\\/\*\?\"\<\>\：\|]*$/)) {
		if (!$scope.filename.match(/^[^\\\/\*\?\"\<\>\:\|]*$/)) {
			$rootScope.model.notice.inValidName = true;
			return;
		}

		var tiltelength = res.string.getLength($scope.filename);
		if (tiltelength > res.config.fileNameLength) {
			$rootScope.model.notice.fileNameLengthOver = true;
			return;
		}

		if ($rootScope.model.notice.status === "input") {
			if ($rootScope.model.notice.items) {
				$rootScope.model.notice.items = $rootScope.model.notice.items.replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;");
			}

			if ($rootScope.model.editor.title) {
				$rootScope.model.editor.title = $rootScope.model.editor.title.replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;");
				$rootScope.model.editor.title = $rootScope.model.editor.title.replace(/\\n/g, "\\\\n");
				$rootScope.model.editor.title = $rootScope.model.editor.title.replace(/\\r/g, "\\\\r");
			}

			if ($rootScope.model.editor.title2) {
				$rootScope.model.editor.title2 = $rootScope.model.editor.title2.replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;");
				$rootScope.model.editor.title2 = $rootScope.model.editor.title2.replace(/\\n/g, "\\\\n");
				$rootScope.model.editor.title2 = $rootScope.model.editor.title2.replace(/\\r/g, "\\\\r");
			}

			var savename = $scope.filename + ".js";

			res.ui.send({
				context : res.ui.context,
				event : "file.upload",
				data : {
					folder : "notices",
					title : $rootScope.model.editor.title,
					title2 : $rootScope.model.editor.title2 ? $rootScope.model.editor.title2 : "",
					desfilename : savename.trim(),
					despicturename : picturename,
					overwrite : $scope.overwrite,
					expire : $rootScope.model.editor.expire ? $rootScope.model.editor.expire : "",
					contents : encodeURIComponent($rootScope.model.notice.items)
				}
			});

			$rootScope.model.notice.fromSaveOrClose = "save";
		} else if ($rootScope.model.notice.status === "successful") {
			if ($rootScope.model.notice.fromSaveOrClose === "close"||$rootScope.model.notice.fromSaveOrClose === "save") {
				$rootScope.model.editor.title = undefined;
				$rootScope.model.editor.title2 = undefined;
				$rootScope.model.editor.expire = undefined;
				$rootScope.model.editor.fileName = undefined;
				$rootScope.model.editor.selectedIndex = undefined;
				$rootScope.model.editor.indexEdit = "itemList";
				$rootScope.dialog = undefined;
				$rootScope.model.notice.fromSaveOrClose = undefined;

				$rootScope.noticeDisableClose = true;
				$rootScope.noticeDisableOpen = false;
			} else {
				$rootScope.model.editor.title = $scope.filename;
			}
			$rootScope.dialog = undefined;
		} else if ($rootScope.model.notice.status == "failed") {
			$rootScope.dialog = undefined;
		}
	};

	$scope.cancel = function () {
		$rootScope.dialog = undefined;
	};

}]);
