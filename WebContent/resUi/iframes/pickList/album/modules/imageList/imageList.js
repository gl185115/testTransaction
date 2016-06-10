/*
 * Controller imageList
 */
res.ui.controller("imageList", ["$scope", "$rootScope", "$timeout",function($scope, $rootScope, $timeout){

	var scroll = undefined;
	$scope.$on("resIncludeLoaded", function(){	// wait for res-include complete
		scroll = new IScroll("#wrapperImages", {	// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true,	// or "zoom"
		});
	});

	$scope.$watch(function() {
		return $rootScope.model.imageCopy.pictures;
	}, function(indexEdit, oldValue) {
		$timeout(function() {
			scroll.refresh();
			$scope.random = Math.random();
		}, 200);
//		$rootScope.model.imageCopy.indexImage = 0;
	});

	$scope.setImage = function(index) {
		$rootScope.model.imageCopy.indexImage = index;
	};
	
}]);

