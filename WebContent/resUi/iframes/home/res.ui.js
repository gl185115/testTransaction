/*
 *	Home Page
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize"]);
res.ui.context = "Home";

/*
 *  Main application in pure JavaScript/jQuery
 */
$(document).ready(function(){
	// inherit parent language setting for angularJS filter
	res.bridge.set("root", "language", window.parent.res.bridge.get("root", "language"));

	// get angularJS $rootScope
	res.ui.root = angular.element(document).scope();

	// connect socket
	res.ui.send = function(message){
		if (!message.context) message.context = res.ui.context;
		window.parent.res.main.socket.send(message);
	};

	window.parent.res.main.socket.onmessage[res.ui.context] = res.ui.onmessage;

	res.model.init();
});

(function(){
	function apply(func) {
		if (!res.ui.root.$$phase) { // from outside non-AngularJS
			res.ui.root.$apply((func)());
		}else{
			(func)();
		};
	}

	res.event = {};
	res.event.open = function() { };

	res.event.skin = function(choice) {
		var url = undefined;
		switch(choice){
		case "default":
			url = "../../../resSkin/default.css";
			break;
		case "green":
			url = "../../../resSkin/green.css";
			break;
		case "orange":
			url = "../../../resSkin/orange.css";
			break;
		}

		if (url){
			document.getElementById("Skin").href = url;
		}
	};

	res.ui.onmessage = function(message) {
		switch(message.event){
		case "cashAccount.status.successful":
			res.bridge.run("#Menu", "status", message.data);
			break;
		default:
			apply(function(){
				res.model.update(message.event, message.data);
			});
			//alert("res.ui.onmessage: unknown message.event = " + message.event);
			break;
		}
	};

})();
