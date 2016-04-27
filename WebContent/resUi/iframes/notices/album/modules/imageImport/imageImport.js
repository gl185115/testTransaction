/*
 * Controller imageImport
 */
res.ui.controller("imageImport", ["$scope", "$rootScope", "$timeout",function($scope, $rootScope, $timeout){

	var scroll = undefined;
	var targetscroll= undefined;
	$scope.$on("resIncludeLoaded", function(){	// wait for res-include complete
		scroll = new IScroll("#wrapperFiles", {	// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true,	// or "zoom"
		});
	});

	$scope.$watch(
		function() {
			return $rootScope.model.imageCopy.files.length;
		},
		function(newValue, oldValue) {
			if (newValue == 0) return;
			$timeout(function() { scroll.refresh(); }, 200);
		}
	);

	$scope.$watch(
		function() {
			return $rootScope.model.imageCopy.targetfiles.length;
		},
		function(newValue, oldValue) {
			if (newValue == 0) return;
			$scope.targetselect(0);
		}
	);

	$scope.initController = function(f) {
		if (f == true) {
			$rootScope.model.imageCopy.folderCopyFrom = "";
			$rootScope.model.imageCopy.indexFile = -1;
			$rootScope.model.imageCopy.indexFileName = "";
		} else {
			$rootScope.model.imageCopy.folderCopyTo = "";
			$rootScope.model.imageCopy.targetindexFile = -1;
			$rootScope.model.imageCopy.targetindexFileName = "";
		}

		$rootScope.model.imageCopy.successMsgShow = false;
		$rootScope.model.imageCopy.failedMsgShow = false;
	};

	$scope.select = function(index){
//		if ($rootScope.model.imageCopy.indexFile >= 0) {
//			$rootScope.model.imageCopy.files[$rootScope.model.imageCopy.indexFile].selected=false;
//		}

		$rootScope.model.imageCopy.indexFile = index;
		$rootScope.model.imageCopy.indexFileName = $rootScope.model.imageCopy.files[index].filename;
		$rootScope.model.imageCopy.folderCopyFrom = $rootScope.model.imageCopy.indexFileName;

		$rootScope.model.imageCopy.files[index].selected = true;
	};

	$scope.dbClickOpen = function(index) {
		if (index) {
			$rootScope.model.imageCopy.indexFile = -1;
			$scope.select(index);
		}

		var fn = $rootScope.model.imageCopy.indexFileName;
		if (fn && (fn.toLowerCase().endsWith('.jpg')
				|| fn.toLowerCase().endsWith('.png')
				|| fn.toLowerCase().endsWith('.bmp')
				|| fn.toLowerCase().endsWith('.gif')
				|| fn.toLowerCase().endsWith('.jpeg')))
			return;
		$scope.folderopen();
	};

	$scope.targetselect = function(index) {
		$rootScope.model.imageCopy.targetindexFile = index;
		$rootScope.model.imageCopy.targetindexFileName = $rootScope.model.imageCopy.targetfiles[index];
		$rootScope.model.imageCopy.folderCopyTo = $rootScope.model.imageCopy.targetBasePath
												+ $rootScope.model.imageCopy.targetindexFileName;
	};

	$scope.folderback = function() {
		res.ui.send({
			context: res.ui.root.context,
			event : "imagecopy.getfolder",
			data : {
				folder : $rootScope.model.imageCopy.BasePath,
				recursive : "false",
				action : "back"
			}
		});

		$scope.initController(true);
	};

	$scope.folderopen = function() {
		res.ui.send({
			context: res.ui.root.context,
			event : "imagecopy.getfolder",
			data : {
				folder : $rootScope.model.imageCopy.indexFileName,
				recursive : "false",
				action : "open"
			}
		});
		$scope.initController(true);
	};

	$scope.targetfolderback = function() {
		res.ui.send({
			context: res.ui.root.context,
			event : "imagecopy.gettargetfolder",
			data : {
				folder : $rootScope.model.imageCopy.targetBasePath,
				recursive : "false",
				action : "back"
			}
		});
		$scope.initController(false);
	};

	$scope.targetfolderopen = function() {
		res.ui.send({
			context: res.ui.root.context,
			event : "imagecopy.gettargetfolder",
			data : {
				folder : $rootScope.model.imageCopy.targetBasePath + $rootScope.model.imageCopy.targetindexFileName,
				recursive : "false",
				action : "open"
			}
		});
		$scope.initController(false);
	};

	$scope.imageCopy = function() {
		$rootScope.model.imageCopy.folderCopyFrom = "";
		$rootScope.model.imageCopy.successMsgShow = false;
		$rootScope.model.imageCopy.failedMsgShow = false;

		for (var i = 0; i < $rootScope.model.imageCopy.files.length; i++) {
			if ($rootScope.model.imageCopy.files[i].selected && $rootScope.model.imageCopy.indexFile == i) {
				$rootScope.model.imageCopy.folderCopyFrom = $rootScope.model.imageCopy.folderCopyFrom
													+ $rootScope.model.imageCopy.files[i].filename + ",";
			} else {
				$rootScope.model.imageCopy.files[i].selected = false;
			}
		}

		if ($rootScope.model.imageCopy.folderCopyFrom.endsWith(",")) {
			$rootScope.model.imageCopy.folderCopyFrom = $rootScope.model.imageCopy.folderCopyFrom.substring(0,
														$rootScope.model.imageCopy.folderCopyFrom.length - 1);
		}

		res.ui.send({
			context: res.ui.root.context,
			event : "imagecopy.copystart",
			data : {
				folderCopyFrom : $rootScope.model.imageCopy.folderCopyFrom,
				folderCopyTo : $rootScope.model.imageCopy.folderCopyTo,
				recursive : $rootScope.model.imageCopy.recursive,
				targetWidth : res.config.imagecopy.targetWidth,
				targetHeight : res.config.imagecopy.targetHeight
			}
		});

		$rootScope.dialog = "";
	};

	$scope.cancel = function() {
		$rootScope.dialog = "";
	};

}]);
