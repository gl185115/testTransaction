/*
 * Section Of Transaction Queue (Suspend/Recall group)
 */
res.ui.controller("queues", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){

//	var scroll = new iScroll("wrapperQueueList", {
//		hScroll: false,
//		vScroll: true,
//		hScrollbar: false,
//		vScrollbar: true,
//		hideScrollbar: false
//	});	
	var scroll = new IScroll("#wrapperQueueList", {	// iScroll5
		scrollX: false,
		scrollY: true,
		scrollbars: true,
		interactiveScrollbars: true,
		mouseWheel: true,	// or "zoom"
	});
	
	$scope.$watch(
			function(){ return $rootScope.section; },
			function(section, oldValue){
				if (section != "queues") return;
				res.ui.send({ event:"settings.queues.getList", data:undefined });
			}
		);
	$scope.$watch( 
			function(){ return $rootScope.model.queues; }, 
			function(printers, oldValue){
				$timeout(function(){scroll.refresh();}, 200);
				$scope.selectedIndex = 0;
			}
		);
	$scope.select = function(index){
		$scope.selectedIndex = index;
	};	

}]);
