/*
 *	NCR RES UI Configurator - Notice Editor
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize", "ngAnimate","720kb.datepicker"]);
res.ui.context = "Notice";

/*
 *  Main application in pure JavaScript/jQuery
 */
$(document).ready(function(){
	// get angularJS $rootScope reference
	res.ui.root = angular.element(document).scope();
	res.ui.root.context = res.ui.context;

	// connect socket
	res.ui.send = function(message){
		if (!message.context) message.context = res.ui.context;
		window.parent.res.main.socket.send(message);
	};

	window.parent.res.main.socket.onmessage[res.ui.context] = res.ui.onmessage;

	res.model.init();
});

(function(){

	var sections = [ "notice" ];
	var indexSection = 0;

	function apply(func){
		if (!res.ui.root.$$phase) { // from outside non-AngularJS
			res.ui.root.$apply((func)());
		}else{
			(func)();
		};
	}

	res.event ={};

	res.event.open = function(){
		apply(function(){
			// inherit parent language setting for angularJS filter
			res.ui.root.language = window.parent.res.bridge.get("root", "language");
			indexSection = 0;
			res.ui.next();
			res.ui.root.model.editor.indexEdit="";
		});
	};

	res.event.close = function(){
		apply(function(){
			res.ui.root.section = "";
		});
	};

	res.ui.next = function(){
		if (indexSection < sections.length){
			res.ui.root.section = sections[indexSection];
			indexSection++;
		}else{
			indexSection = 0;
			window.parent.res.main.page("home");
		}
	};

	res.ui.onmessage = function(message) {
		apply(function(){
			res.ui.root.message = message;
			res.model.update(message.event, message.data);
		});
	};

})();
