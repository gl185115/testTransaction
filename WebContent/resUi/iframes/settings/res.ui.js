var res = res || {};
res.ui = res.ui || {};
/*
 * Settings Page
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize", "ngAnimate", "ngTouch","ui.slider" ]);

angular.element(document).ready(function(){
	res.ui.root = angular.element(document).scope();	// get angularJS $rootScope
	res.ui.root.context = "Settings";
	// inherit parent language setting for angularJS filter
	res.ui.root.language = window.parent.res.ui.root.language;
	
	// connect socket

	res.ui.send = function(message){
		if (!message.context) message.context = res.ui.root.context;
		window.parent.res.main.socket.send(message);
	};
	window.parent.res.main.socket.onmessage[res.ui.root.context] = res.ui.onmessage;

	res.model.init();

	window.addEventListener("keypress", function(e){
		window.parent.res.main.keyboard(e.which);
//		return false;
	});
	
	res.event.open();

});

/*
 * external events
 */
(function(){
	
	function apply(func){
		if (!res.ui.root.$$phase) { // from outside non-AngularJS
			res.ui.root.$apply((func)());	
		}else{		
			(func)();
		};
	}

	res.event = {};
	res.event.open = function(){
		apply(function(){
			res.ui.root.section = "device";
		});
	};
	res.event.close = function(){
		apply(function(){

		});
	};

	res.ui.onmessage = function(message){
		apply(function(){
			res.model.update(message.event, message.data);
		});
	};

})();

/*
 * drawing images on canvas
 */
(function(){
	
	res.ui.canvas = function(id){

		var list, i, canvas;

		list = document.querySelectorAll(id + " .ColumnArrow");
		for (i = 0; i < list.length; i++){
			canvas = document.createElement('canvas');
			canvas.width = 15;
			canvas.height = 30;
			res.canvas.stroke(canvas, [[1,1],[10,15],[1,29]], 2, "hsl(0,0%,50%)");
			list[i].appendChild(canvas);
		}
		
	};
	
})();
