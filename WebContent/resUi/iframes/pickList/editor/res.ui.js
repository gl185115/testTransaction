var res = res || {};
res.ui = res.ui || {};
/*
 *	NCR RES UI Configurator - PickList Editor
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize", "ngAnimate", "ngTouch" ]);

angular.element(document).ready(function(){
	res.ui.root = angular.element(document).scope();	// get angularJS $rootScope
	res.ui.root.context = "PickList";
	// inherit parent language setting for angularJS filter
	res.ui.root.language = window.parent.res.ui.root.language;

	// connect socket
	res.ui.send = function(message){
		if (!message.context) message.context = res.ui.root.context;
		window.parent.res.main.socket.send(message);
	};

	window.parent.res.main.socket.onmessage[res.ui.root.context] = res.ui.onmessage;

	res.model.init();

	$(window).keypress(function(e){
		window.parent.res.main.keyboard(e.which);
	});

	res.event.open();

});

(function(){

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
			res.ui.root.language = window.parent.res.bridge.get("root", "language"); // inherit parent language setting for angularJS filter
			current = 0;
			res.model.init();
			res.ui.next();
			res.ui.root.model.editor.indexEdit = "";
		});
	};

	res.event.close = function(){
		apply(function(){
			res.ui.root.section = "";
		});
	};

	var sections = [ "editItems", "editCategories", "editLayout" ];
	var current = 0;

	res.ui.next = function(){
		if (current < sections.length){
			res.ui.root.section = sections[current];
			current++;
		}else{
			res.event.close();
			window.parent.res.main.page("home");
		}
	};

	res.ui.onmessage = function(message) {
		apply(function(){
			res.model.update(message.event, message.data);
		});
	};

})();

