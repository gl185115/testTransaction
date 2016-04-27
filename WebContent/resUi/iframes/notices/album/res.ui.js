var res = res || {};
res.ui = res.ui || {};
/*
 *	Pick List Editor
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize", "ngAnimate", "ngTouch" ]);

angular.element(document).ready(function(){
	// get angularJS $rootScope
	res.ui.root = angular.element(document).scope();
	res.ui.root.context = "Notices.Album";

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
		// from outside non-AngularJS
		if (!res.ui.root.$$phase) {
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
			current = 0;
			res.ui.next();
			res.model.init();
		});
	};
	res.event.close = function(){
		apply(function(){
			res.ui.root.section = "";
		});
	};

	var sections = [ "imageList" ];
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

