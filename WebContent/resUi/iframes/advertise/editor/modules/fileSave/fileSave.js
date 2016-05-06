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
						$rootScope.model.failure.service = "advertise";
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
			/*if($rootScope.language=="ja"){
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
			$rootScope.model.pickList.convertToLists();*/

			var imgURLStr  = $rootScope.model.pickList.imageURL;
			var imageURL   = "res.config.advertise.folder = " + angular.toJson(imgURLStr) + ";\r\n";
			var interval = "res.config.advertise.interval = " + angular.toJson($rootScope.model.editor.interval) + ";\r\n";

			var items = "res.config.advertise.rules = [\r\n";
			for (i = 0; i < $rootScope.model.pickList.layout.length; i++){
			    for (j = 0; j< $rootScope.model.pickList.layout[i].length; j++){
			        items += angular.toJson({
			            fileName: $rootScope.model.pickList.layout[i][j].Part.fileName + ".jpg",
			            fileNameFullScreen: $rootScope.model.pickList.layout[i][j].Full.fileName + "_1020*640.jpg",
			            start: $rootScope.model.pickList.layout[i][j].startOfDay,
			            end: $rootScope.model.pickList.layout[i][j].endOfDay,
	                    description: $rootScope.model.pickList.layout[i][j].companyId + "," +
	                                 $rootScope.model.pickList.layout[i][j].companyName + "," +
	                                 $rootScope.model.pickList.layout[i][j].Tel + "," +
	                                 $rootScope.model.pickList.layout[i][j].adName + "," +
	                                 $rootScope.model.pickList.layout[i][j].description ,
	                }) + ",\r\n";
			    }
			}
			items += "];\r\n";

			var savename = $scope.filename + ".js";

			res.ui.send({
				context : res.ui.root.context,
				event: "file.upload",
				data: {
					folder:"advertise",
					title: $scope.filename,
					desfilename: savename.trim(),
					overwrite: $scope.overwrite,
					contents: imageURL + "\r\n" + interval + "\r\n" + items
				}
			});
			$rootScope.model.pickList.fromSaveOrClose = "save";
		} else if ($rootScope.model.pickList.status == "successful"){
			$rootScope.model.pickList.doCanceled = false;
			if ($rootScope.model.pickList.fromSaveOrClose == "close" ||$rootScope.model.pickList.fromSaveOrClose == "save") {
				$rootScope.model.editor.title = undefined;
				$rootScope.model.editor.interval = "";
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