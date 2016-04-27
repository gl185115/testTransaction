/*
 * Section Of Printers
 */
res.ui.controller("printers", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){
	
//	var scroll = new iScroll("wrapperPrinterList", {
//		hScroll: false,
//		vScroll: true,
//		hScrollbar: false,
//		vScrollbar: true,
//		hideScrollbar: false
//	});
	var scroll = new IScroll("#wrapperPrinterList", {	// iScroll5
		scrollX: false,
		scrollY: true,
		scrollbars: true,
		interactiveScrollbars: true,
		mouseWheel: true,	// or "zoom"
	});

	$scope.$watch(
			function(){ return $rootScope.section; },
			function(section, oldValue){
				if (section != "printers") return;
				res.ui.send({ event:"settings.printers.getList", data:undefined });
			}
		);
	$scope.$watch( 
			function(){ return $rootScope.model.printers; }, 
			function(printers, oldValue){
				$timeout(function(){scroll.refresh();}, 200);
				$scope.selectedIndex = 0;
			}
		);
	$scope.select = function(index){
		$scope.selectedIndex = index;
	};

}]);
