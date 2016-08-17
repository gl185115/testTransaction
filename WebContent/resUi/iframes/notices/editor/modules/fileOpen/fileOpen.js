/*
 * Controller fileOpen
 */
res.ui.controller("fileOpen", ["$scope", "$rootScope", "$timeout", function ($scope, $rootScope, $timeout) {

	var scroll = undefined;
	$scope.$on("resIncludeLoaded", function () {	// wait for res-include complete
		scroll = new IScroll("#wrapperFiles", {// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true, // or "zoom"
		});
	});

	$scope.$watch(
		function () {
			return $rootScope.dialog;
		},
		function (newValue, oldValue) {
			if (newValue != "fileOpen") return;
			res.ui.send({
				context : res.ui.context,
				event : "file.list",
				data : {
					folder : "notices"
				}
			});
			$timeout(function () { scroll.refresh(); }, 200);
		}
	);

	$scope.select = function (index) {
		$scope.indexFile = index;
	};

	$scope.submit = function () {
			//$rootScope.model.editor.title = $rootScope.model.editor.files[$scope.indexFile];
		$rootScope.model.editor.fileName=$rootScope.model.editor.files[$scope.indexFile];
		$rootScope.model.editor.indexEdit = "editItems";
		$rootScope.dialog = "";

		res.ui.send({
			context : res.ui.context,
			event : "file.download",
			data : {
				folder : "notices",
				file : $rootScope.model.editor.files[$scope.indexFile] + ".js"
			}
		});
		$rootScope.noticeDisableClose = false;
		$rootScope.noticeDisableOpen = true;
	};

	$scope.cancel = function () {
		$rootScope.dialog = "";
	};

}]);
