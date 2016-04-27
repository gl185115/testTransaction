
/*
 * Hidden Tool Contoller
 */
res.ui.controller("hiddenTool", ["$scope", "$rootScope", function($scope, $rootScope){
//	var panel = "#HiddenTool";

	$scope.$on("resIncludeLoaded", function(){	// wait for res-include complete
		res.main.socket.onmessage["HiddenTool"] = res.hiddenTool.onmessage;
		$scope.denominations = res.config.currency.denominations;
	});

	$scope.close = function(){
		$rootScope.hiddenToolOpened = false;
	};

	// Maintenance Tools

	$scope.reload = function(){
		res.hiddenTool.reload();
	};
	$scope.terminate = function(){
		res.hiddenTool.terminate();
	};
	$scope.messageLog = function(){
		res.hiddenTool.messageLog();
	};

	// demo tools

	$scope.scanId = function(){
		alert("res.ui.controller hiddenTool: reached $scope.scanId ");
	};
	$scope.scanItem = function(){
		res.hiddenTool.scan();	// simulate scanning item bar code
	};
	$scope.swipeCredit = function(){
		res.hiddenTool.swipe();	// simulate swiping credit card
	};
	$scope.waitPage = function(disp){
//		if (disp){
//			res.main.wait.stop();
//			$scope.waitDisp = undefined;
//			res.bridge.run("#Overlay", "setProcessList", 0);
//		}else{
//			var message = "WaitMessage";
//			res.main.wait.start(message);
//			$scope.waitDisp = true;
//			res.bridge.run("#Overlay", "setProcessList", 1);
//		}

		if ($rootScope.model.popup){
			$rootScope.model.popup = "";
		}else{
			$rootScope.model.popup = "Wait";
		}
	};
	$scope.ownersCard = function(){
		res.hiddenTool.ownersCard();
	};
	$scope.cashRead = function(subject){
		subject = subject.split(".");
		var Subject = subject[0].replace(subject[0].charAt(0),subject[0].substring(0,1).toUpperCase());
		res.main.socket.send({context: Subject, event:"cashAccount.dispenser.countAll" });
	};
	$scope.countDeposit = function(subject, cash){
		subject = subject.split(".");
		var Subject = subject[0].replace(subject[0].charAt(0),subject[0].substring(0,1).toUpperCase());
		var deposit = [cash, $scope.multiple];
		res.main.socket.send({context: Subject, event:"cashAccount.dispenser.countDeposit", data: deposit});
	};
	$scope.setMultiplier = function(n){
		$scope.multiple = n;
	};
	$scope.multiplier = 1;

	$scope.power = "off";
	$scope.switchPower = function(option){
		$scope.power = option;
		if (option == "on"){
			res.main.socket.send({context: "HiddenTool", event:"cashAccount.dispenser.turnOn", data:{}});
			res.main.socket.send({context: "HiddenTool", event:"cashAccount.dispenser.countAll", data:{}});
		}else{
			res.main.socket.send({context: "HiddenTool", event:"cashAccount.dispenser.turnOff", data:{}});
		}
	};
	$scope.deposit = function(index){
		res.main.socket.send({context: "HiddenTool", event:"cashAccount.dispenser.deposit", data:{index: index, count: $scope.multiplier}});
	};
	$scope.updateCashInside = function(counts){
		for (var i = 0; i < counts.length; i++){
			$scope.denominations[i].count = counts[i];
		}
	};
}]);

(function(){

	var itemIDs = [
	               "4901301762832", 	// Biore u Hand Soap
	               "4901655440509", 	// Scottie Cashmere Facial Tissues with mix match
	               "4901750401900", "4901777216846",
	               "4902102067041", "4902102084178", "4902102090599", "4902102090919", "4902102101486",
	               "4987123701716", "4987316030180",
	               "4901730080934", "4901730080989", "4901730081030", "4901730091084",	// Series of 'Listerine' with mix match

	               "4547597868386", "4548660711011", "4988075558090",
	               "2010000001011", "2010000001028", "2010000001035", "2010000001042", "2010000001059",
	               "2010000001066", "2010000001073", "2010000001080", "2010000001097", "2010000001103",
	               ];
	var itemIndex = 0;
	var cardData = {pan: "0000000000001234", goodThru: "0115", owner: "JOHN WHITE" };

	res.hiddenTool = {
		onmessage: function(message){
			switch(message.event){
			case "cashAccount.dispenser.countAll.successful":
				res.bridge.run("#HiddenTool", "updateCashInside", message.data);
				break;
			}
		},
		reload : function(){
			res.main.socket.send({context:"HiddenTool", event: "settings.reload"});
		},
		terminate : function(){
			res.main.socket.send({context:"HiddenTool", event: "settings.terminate"});
		},
		messageLog : function(){
			res.main.socket.send({context:"Main", event: "main.showCore"});
		},
		scan : function(){
			for (var i = 0; i < itemIDs[itemIndex].length; i++){
				res.main.keyboard(itemIDs[itemIndex].charCodeAt(i));
			}
			res.main.keyboard(13);
			itemIndex++;
			if (itemIndex >= itemIDs.length - 1 ) { itemIndex = 0; }
		},
		swipe : function(){
			res.main.msr.input(cardData);
		},
		ownersCard : function(){
			res.main.socket.send({context:"HiddenTool", event:"transaction.customer", data:{membership:"1234511111"}});
		}
	};

})();
