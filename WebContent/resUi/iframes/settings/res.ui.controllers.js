/*
 * User Interface controllers (in AngularJS)
 */

/*
 * Table Of Contents
 */
res.ui.controller("tableOfContents", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.select = function(choice){
		$rootScope.section = choice;
//		if (choice=="printer"){
//			res.ui.send({ event: "settings.printers.getList", data: undefined});
//			res.bridge.run("#Printers", "view");				
//		} else if (choice=="transfer"){
//			res.ui.send({ event: "settings.transfers.getList", data: undefined});
//			res.bridge.run("#Transfer", "view");
//		}
	};
	$scope.select("personal");
	
}]);

/*
 * Section Of Software
 */
res.ui.controller("software", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.restore = function(){
		window.parent.location.reload();
	};
	$scope.close = function(){
		window.parent.res.main.close();
	};
//	$scope.userInterface = res.config.userInterface;
//	$scope.services = res.config.services;

}]);

