/*
 * Controller
 */
res.ui.controller("menu", ["$scope", "$rootScope", function($scope, $rootScope){
	$scope.$watch(
            function () {
                return $rootScope.model.active.item;
            },
            function (n, oldValue) {
            	if(oldValue && n){
            		$rootScope.model.active.item="Modified";
            	}
            	window.parent.res.ui.root.model.active=n;
            	if (!window.parent.res.ui.root.$$phase) window.parent.res.ui.root.$digest();
            }
    );
	$scope.$watch(
            function () {
                return $rootScope.model.saveFinished;
            },
            function (n, oldValue) {
            	if(n==true){
            		$rootScope.model.saveFinished=undefined;
            		$rootScope.model.active.resource=undefined;
            		$scope.exit();
            	}
            }
    );
	$scope.exit = function() {
		if (!$rootScope.model.active.resource) {
			window.parent.res.main.page("home");
			$rootScope.model.message = "";
			$rootScope.dialog = "";
			return;
		}
		$rootScope.model.active.item = undefined;
		$rootScope.model.load();
		res.console("res.ui deploy menu save: model.schedule = " + JSON.stringify({ schedule : $rootScope.model.schedule }));

		res.ui.send({
			context : res.ui.context,
			event : "deploy.pickList.setSchedule",
			data : {
				schedule : JSON.stringify({ schedule : $rootScope.model.schedule }),
				resource : "pickList"
			}
		});
	};
	$scope.file = {
		save: function(){
			res.ui.root.confirmMessage = "confirmMessage";
			/*if (!$rootScope.model.active.resource) {return;}
			$rootScope.model.save();
			$rootScope.model.active.item=undefined;
			res.console("res.ui deploy menu save: model.schedule = " + JSON.stringify({schedule: $rootScope.model.schedule}) );
			res.ui.send({ event: "schedule.set", data: {schedule:$rootScope.model.schedule} });*/
		},
		undo: function(){
			if (!$rootScope.model.active.resource) {return;}
			$rootScope.model.message = "Undo";
			$rootScope.dialog = "question";
		},
		exit: function(){
			$rootScope.model.message = "Exit";
			/*$rootScope.dialog = "question";*/
			$scope.exit();
		}
	};
	$scope.task = {
		create: function(){
			if (!$rootScope.model.active.resource) {return;}
			$rootScope.model.active.taskIndex = undefined;
			$rootScope.model.active.item="create";
			$rootScope.dialog = "editTask";

			res.ui.send({
				context : res.ui.context,
				event: "deploy.pickList.getDeployStoreAndGroup",
				data: { companyID : res.storage.getItem("CompanyID") }
			});
			$rootScope.model.doRefreshSchedule = false;
		},
		update: function(){
			if ((!$rootScope.model.active.resource) ||
				($rootScope.model.active.folder == 'effective') ||
				($rootScope.model.active.folder == 'obsolete') ||
			 	($rootScope.model.active.taskIndex == undefined)) {return;}
			$rootScope.model.active.item="update";
			$rootScope.dialog = "editTask";
		},
		remove: function(){
			if ((!$rootScope.model.active.resource) ||
				($rootScope.model.active.folder == 'effective') ||
				($rootScope.model.active.taskIndex==undefined)) {return;}
			$rootScope.model.message = "Remove";
			$rootScope.dialog = "question";
			$rootScope.model.doRefreshSchedule = false;
		}
	};
	$scope.okay = function(){
		$rootScope.model.message = "";
		$rootScope.dialog = "";
		window.parent.res.main.page("home");
	};
	$scope.address = function(){
		$rootScope.dialog = "editStore";
		$rootScope.model.pageIndex=undefined;
	};
	$scope.answer = {
		no: function(){
			$rootScope.dialog = "";
		},
		yes: function(){
			switch ($rootScope.model.message){
			case "Undo":
				$rootScope.model.active.item=undefined;
				$rootScope.model.load();
				break;
			case "Remove":
				$rootScope.model.active.item="Remove";
				$rootScope.model.remove();
				$rootScope.model.doRefreshSchedule = true;
				break;
			case "Exit":
				$rootScope.model.save();
				/*$scope.exit();*/
				break;
			}
			$rootScope.model.message = "";
			$rootScope.dialog = "";
		}
	};

	$scope.collectLogs = function() {
		window.parent.res.ui.root.model.popup = "Wait";
		$rootScope.model.deployLogsBtn = true;
		$rootScope.model.active.item = undefined;

		$rootScope.model.save();
		$rootScope.model.load();
		res.ui.send({
			context : res.ui.context,
			event: "deploy.pickList.getDeployStatus",
			data: {
				companyID : res.storage.getItem("CompanyID"),
				resource : "pickList"
			}
		});
	};

}]);

