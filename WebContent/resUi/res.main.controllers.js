/*
 *  AngularJS Controllers on Main Page
 */

/*
 * Status Bar
 */
res.ui.controller("statusBar", ["$scope", "$interval", function($scope, $interval){
//	var panel = "#StatusBar";
	$scope.tick = function(){
		$scope.clock = new Date();
	};
	$scope.version = res.config.version;
	$scope.tick();
	$interval(function(){
		$scope.tick();
	}, 5000);
	$scope.battery = "100%";
}]);

/*
 * Navigation Bar
 */
res.ui.controller("navigationBar", ["$scope", "$rootScope", "$timeout", "$interval", function($scope, $rootScope, $timeout, $interval){
	$interval(function(){
		$scope.tick();
	}, 5000);
	$scope.tick = function(){
		if ($rootScope.model) $rootScope.model.date = new Date();
		if((!$scope.model)&&$rootScope.model){
			$scope.subject=$rootScope.subject;
			$scope.model=$rootScope.model;
		}
	};

	$rootScope.dialog = "";
	$scope.menu = function(){
		if ($rootScope.dialog){
			$rootScope.dialog = "";
		}else{
			$rootScope.dialog = "sidePanel";
		}
	};
	$scope.back = function(to){
		if ($rootScope.subject=="transaction.sales" || $rootScope.subject=="transaction.return"){
			var e = true;
			var elements = document.getElementsByName("transaction");
			if (elements[0].contentWindow.res && elements[0].contentWindow.res.event && elements[0].contentWindow.res.event.back) {
				e = elements[0].contentWindow.res.event.back();
			}
			if (e){
				res.main.page(to);
			}
		}else{
			res.main.page(to);
		}
	};

}]);

/*
 * Child Pages
 */
res.ui.controller("childPage", ["$scope", "$rootScope", function($scope, $rootScope){

	var loaded = {};
	$scope.pages = [];
	$scope.load = function(page){
		if (loaded[page]) return;
		$scope.pages.push({name: page, src: res.config.pages[page].src});
		loaded[page] = true;
	};
	$scope.preload = function(){
		for (var page in res.config.pages){
			if (res.config.pages[page].preload){
				$scope.load(page);
			}
		}
	};
	$scope.$watch(
		function(){ return $rootScope.pageOnBoard; },
		function(newValue, oldValue){
			if (oldValue){
				elements = document.getElementsByName(oldValue);
				if (elements[0].contentWindow.res && elements[0].contentWindow.res.event && elements[0].contentWindow.res.event.close) {
					elements[0].contentWindow.res.event.close();
				}
			}
			if (newValue){
				$scope.load(newValue);
				elements = document.getElementsByName(newValue);
				if (!elements[0]) return;
				if (elements[0].contentWindow.res && elements[0].contentWindow.res.event && elements[0].contentWindow.res.event.open) {
					elements[0].contentWindow.res.event.open();
				}
				setTimeout(function(){
					elements[0].focus();
				}, 200);
			}
		}
	);

	$scope.hiddenToolOpen = function(){
		$rootScope.hiddenToolOpened = false;
//		$rootScope.hiddenToolOpened = true;
	};
	$scope.preload();

}]);

/*
 * Side Panel
 */
res.ui.controller("sidePanel", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){

	$scope.$watch(
			function(){ return $rootScope.dialog; },
			function(newValue, oldValue){
				if (newValue != "sidePanel") return;
				$scope.items = res.config.sidePanel;
				$scope.autoLoginPasscode = (res.config.autoLoginPasscode)? true: false;
			}
	);
	$scope.touchOut = function(){
		$rootScope.dialog = "";
	};
	$scope.select = function(option){
		switch(option){
		case "lock":
			res.ui.send({ event: "credential.lock" });
			break;
		case "logOut":
			var ready = true;
			var elements = document.getElementsByName("transaction");
			if (elements[0] && elements[0].contentWindow.res && elements[0].contentWindow.res.event && elements[0].contentWindow.res.event.readySignOut) {
				ready = elements[0].contentWindow.res.event.readySignOut();
			}
			if (!ready) break;
			res.ui.send({ event: "credential.signOut" });
			break;
		default:
			res.main.page(option);
			break;
		}
		$rootScope.dialog = "";
	};

}]);

/*
 * Main Popup Message
 */
res.ui.controller("mainPopup", ["$scope", "$rootScope", function($scope, $rootScope){

	var opts = {
			lines: 12,					// # of lines
			length: 7,					// length of the lines
			width: 2,					// width of the lines
			radius: 3,					// radius of the lines
			color: 'hsl(7, 100%, 56%)',	// line color
			speed: 1,					// cycle time in second
			trail: 180,					// After glow percentage
			shadow: false,				// true to turn on shadow
			hwaccel: true,				// Whether to use hardware acceleration
	};

	var target = document.getElementById('Spinner');
	var spinner = new Spinner(opts).spin(target);

	$scope.$watch(
			function(){ return res.ui.root ? res.ui.root.model.popup : undefined; },
			function(popup, oldValue){
				if (popup){
					spinner.spin(target);
				}else{
					spinner.stop();
				}
			}
		);
}]);

