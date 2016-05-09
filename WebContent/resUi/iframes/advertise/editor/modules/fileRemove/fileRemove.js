/*
 * Controller fileRemove
 */
res.ui.controller("fileRemove", ["$scope", "$rootScope", function($scope, $rootScope) {

	$scope.okay = function() {
		$rootScope.dialog = undefined;
		$rootScope.model.editor.indexEdit = "";
		if ($rootScope.model.editor.selectedFile) {
			res.ui.send({
				context : res.ui.root.context,
				event : "file.remove",
				data : {
					companyID : res.storage.getItem("CompanyID"),
					folder : "advertise",
					file : $rootScope.model.editor.selectedFile
				}
			});
		}
	};

	$scope.cancel = function() {
		$rootScope.dialog = undefined;
	};

}]);

