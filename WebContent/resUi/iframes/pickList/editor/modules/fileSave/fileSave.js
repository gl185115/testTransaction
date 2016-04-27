/*
 * Controller
 */
res.ui.controller("fileSave", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.$watch(
		function(){
			return $rootScope.dialog;
		},
		function(newValue, oldValue){
			if (newValue == "fileSave") {
				if ($rootScope.itemSelected) {
					if ($rootScope.itemSelected.isblank) {
						$rootScope.model.failure.active = true;
						$rootScope.model.failure.service = "picklist";
						$rootScope.model.failure.cause = "blank";
						$rootScope.dialog = "";
						return;
					}
				}

				$rootScope.model.pickList.status = "input";
				$rootScope.model.pickList.alreadyExists = false;
				$rootScope.model.pickList.inputEmptyName = false;
				$rootScope.model.pickList.inValidName = false;
				$rootScope.model.pickList.fileNameLengthOver = false;
//				$scope.filename = $rootScope.model.editor.title;
				$scope.filename = ($rootScope.model.editor.title + ".js").trim();
				$scope.filename = $scope.filename.substring(0, $scope.filename.length - 3);
				$scope.overwrite = false;
				//$rootScope.model.notice.fromSaveOrClose = undefined;
			}
		}
	);

	$scope.okay = function(){

		$rootScope.model.pickList.alreadyExists = false;
		$rootScope.model.pickList.inputEmptyName = false;
		$rootScope.model.pickList.inValidName = false;
		$rootScope.model.pickList.fileNameLengthOver = false;

		if (typeof($scope.filename) == "undefined" || $scope.filename.trim() == "") {
			$rootScope.model.pickList.inputEmptyName = true;
			return;
		}

//		if (!$scope.filename.match(/^[^\\\/\*\?\"\<\>\：\|]*$/)) {
		if (!$scope.filename.match(/^[^\\\/\*\?\"\<\>\:\|]*$/)) {
			$rootScope.model.pickList.inValidName = true;
			return;
		}

		var tiltelength = res.string.getLength($scope.filename);
		if (tiltelength > res.config.fileNameLength) {
			$rootScope.model.pickList.fileNameLengthOver = true;
			return;
		}
		if($rootScope.model.pickList.status == "input") {
			if($rootScope.language=="ja"){
				for(var i=0;i<$rootScope.model.pickList.categories.length;i++){
						if($rootScope.model.pickList.categories[i]["jp"]){
							$rootScope.model.pickList.categories[i]["ja"]=$rootScope.model.pickList.categories[i]["jp"];
							delete $rootScope.model.pickList.categories[i]["jp"];
						}
				}
				for(var i=0;i<$rootScope.model.pickList.items.length;i++){
					if($rootScope.model.pickList.items[i].description["jp"]){
						$rootScope.model.pickList.items[i].description["ja"]=$rootScope.model.pickList.items[i].description["jp"];
						delete $rootScope.model.pickList.items[i].description["jp"];
					}
				}
			}
			$rootScope.model.pickList.convertToLists();

			var imgURLStr  = $rootScope.model.pickList.imageURL;
			var imageURL   = "res.config.pickList.imageURL = " + angular.toJson(imgURLStr) + ";\r\n";
			var categories = "res.config.pickList.categories = [\r\n" + $scope.toJson($rootScope.model.pickList.categories) + "];\r\n";

			var p = "";
			for (var i = 0; i < $rootScope.model.pickList.lists.length; i++){
				p = p + "[    //Category " + (i+1) + "\r\n" + $scope.toJson($rootScope.model.pickList.lists[i]) + "],\r\n" + "\r\n";
			}
			var lists = "res.config.pickList.lists = [\r\n" + p + "];	// end of pick lists\r\n";
			var items = "res.config.pickList.items = [\r\n";
			for (i = 0; i < $rootScope.model.pickList.items.length; i++){
				items += angular.toJson({
					itemId: $rootScope.model.pickList.items[i].itemId,
					background: $rootScope.model.pickList.items[i].background,
					picture: $rootScope.model.pickList.items[i].picture,
					description: $rootScope.model.pickList.items[i].description,
				}) + ",\r\n";
			}
			items += "];\r\n";

			var savename = $scope.filename + ".js";

			res.ui.send({
				context : res.ui.root.context,
				event: "file.upload",
				data: {
					folder:"pickList",
					title: $scope.filename,
					desfilename: savename.trim(),
					overwrite: $scope.overwrite,
					contents: imageURL + "\r\n" + categories + "\r\n" + lists + "\r\n" + items
				}
			});
			$rootScope.model.pickList.fromSaveOrClose = "save";
		} else if ($rootScope.model.pickList.status == "successful"){
			$rootScope.model.pickList.doCanceled = false;
			if ($rootScope.model.pickList.fromSaveOrClose == "close" ||$rootScope.model.pickList.fromSaveOrClose == "save") {
				$rootScope.model.editor.title = undefined;
				$rootScope.model.editor.selectedIndex = undefined;
				$rootScope.model.editor.indexEdit = "itemList";
				$rootScope.model.pickList = undefined;
				$rootScope.itemSelected = undefined;
				$rootScope.indexCategory = undefined;

				$rootScope.pickListDisableClose = true;
				$rootScope.pickListDisableOpen = false;
			} else {
				$rootScope.model.editor.title = $scope.filename;
			}
			$rootScope.dialog = undefined;
		} else if ($rootScope.model.pickList.status == "failed") {
			$rootScope.dialog = undefined;
		}
	};
	$scope.cancel = function(){
		$rootScope.dialog = undefined;
//		$rootScope.model.pickList.doCanceled = true;
	};
	$scope.toJson = function(obj){
		var str = "";
		for (var i = 0; i < obj.length; i++) {
			str = str + angular.toJson(obj[i]) + ",\r\n";
		}
		return str;
	};
//	$scope.getLength=function strLength(strSrc){
//		len = 0;
//		strSrc = escape(strSrc);
//		for(var i = 0; i < strSrc.length; i++, len++){
//			if(strSrc.charAt(i) == "%"){
//				if(strSrc.charAt(++i) == "u"){
//					i += 3;
//					len++;
//				}
//				i++;
//			}
//		}
//		return len;
//	};
}]);