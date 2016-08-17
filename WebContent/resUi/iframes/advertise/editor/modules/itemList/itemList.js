/*
 * Controller itemList
 */
res.ui.controller("itemList", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout) {

	var scroll = undefined;
	$scope.$on("resIncludeLoaded", function(){	// wait for res-include complete
		scroll = new IScroll("#itemListwrapperItems", {	// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true,	// or "zoom"
		});
	});

	$scope.$watch(
		function() {
			return $rootScope.model.editor.files.length;
		},
		function(newValue, oldValue) {
			$timeout(function(){ scroll.refresh(); }, 200);
		}
	);

	$scope.$watch(
		function() {
			return $rootScope.model.editor.indexEdit;
		},
		function(newValue, oldValue) {
			if (res.ui.root.context == "Advertise" && $rootScope.model.editor.indexEdit == "itemList") {
				res.ui.send({
					context : res.ui.root.context,
					event : "file.list",
					data : {
						folder : "advertise"
					}
				});
			}

			$timeout(function(){ scroll.refresh(); }, 200);

//			res.ui.send({
//				context : res.ui.root.context,
//				event : "schedule.get",
//				data : {}
//			});
//
//			res.ui.send({
//				context : res.ui.root.context,
//				event : "file.list",
//				data : {
//					folder : "pickList"
//				}
//			});

//			$timeout(function(){ scroll.refresh(); }, 200);
		}
	);

//	$scope.$watch(
//		function() {
//			return $rootScope.model.editor.indexEdit; },
//		function(newValue, oldValue) {
//			if (!newValue || (newValue == "")) {
//				$rootScope.model.editor.indexEdit = "itemList";
//			}
//			if (newValue != "itemList") return;
//			$scope.indexFile = undefined;
//			$rootScope.model.editor.title = undefined;
//			$rootScope.model.editor.selectedFile = undefined;
//
//			res.ui.send({
//				context : res.ui.root.context,
//				event : "schedule.get",
//				data : {}
//			});
//
//			res.ui.send({
//				context : res.ui.root.context,
//				event : "file.list",
//				data : {
//					folder : "pickList"
//				}
//			});
//
//			$timeout(function(){ scroll.refresh(); }, 200);
//		}
//	);

	$scope.select = function(index) {
		$scope.indexFile = index;
		$rootScope.model.editor.selectedIndex = index;
		$rootScope.model.editor.title = $rootScope.model.editor.files[$scope.indexFile];
		$rootScope.model.editor.selectedFile = $rootScope.model.editor.files[$scope.indexFile] + ".js" ;
	};

}]);