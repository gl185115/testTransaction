/*
 * Controller
 */
res.ui.controller("deConfirm", ["$scope", "$rootScope", "$timeout", "$filter", function ($scope, $rootScope, $timeout, $filter) {

	$scope.deleteStoreOkay = function () {
		var entry = angular.element(document.getElementById("EditStore")).scope();
		$rootScope.secondPhrase = "DeleteStore.";

     	var pStoreId = entry.editStoreId.trim();
     	var pStoreNameJ = entry.editStoreNmJp.trim();
     	var pStorePriIp = entry.editStoreIP == "" ? " " : entry.editStoreIP.trim();
     	var pStoreSecIp = entry.editStoreSecondaryIP == "" ? " " : entry.editStoreSecondaryIP.trim();
     	var pStorePhone = entry.editStoreTel == "" ? " " : entry.editStoreTel.trim();

       	res.ui.send({
       		context : res.ui.context,
    		event : "deploy.notices.editTableStore",
			data : {
				editType: "delete" ,
				storeInfo: pStoreId + "," + pStoreNameJ + "," + pStorePriIp + "," + pStoreSecIp + "," + pStorePhone
			}
    	});

		$rootScope.deConfirm = "";
		$rootScope.dialog = "";
		$rootScope.model.pageIndex = "";
    };

//	$scope.deleteStoreOkay = function () {
//		var obj = angular.element(document.getElementById("EditStore")).scope();
//		$rootScope.secondPhrase = "DeleteStore.";
//		obj.storeLine.commandType = "D";
//		res.ui.send({event:"deploy.store.editStore.deleOkay",
//			data:{
//				'lineType': obj.storeLine.lineType,
//				'commandType': obj.storeLine.commandType ,
//				'line': obj.editStoreNmEn + "," + obj.editStoreNmJp + "," + obj.editStoreId+","+ obj.editStoreIP,
//				'lineDetail' : obj.editStoreId+","+ obj.editStoreSecondaryIP
//			}});
//		$rootScope.deConfirm = "";
//		$rootScope.dialog = "";
//		$rootScope.model.pageIndex = "";
//    };

	$scope.voidDelete = function () {
		$rootScope.deConfirm = "";
		$rootScope.dialog = "editStore";
    };
}]);
