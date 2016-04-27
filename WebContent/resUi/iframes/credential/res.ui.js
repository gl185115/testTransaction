var res = res || {};
res.ui = res.ui || {};
/*
 *	Credential 
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize"]);

angular.element(document).ready(function(){
	res.ui.root = angular.element(document).scope();	// get angularJS $rootScope
	// inherit parent language setting for angularJS filter
	res.ui.root.language = window.parent.res.ui.root.language;
	res.ui.root.context = "Credential";
	
	// connect socket

	res.ui.send = function(message){
		if (!message.context) message.context = res.ui.root.context;
		window.parent.res.main.socket.send(message);
	};
	window.parent.res.main.socket.onmessage[res.ui.root.context] = res.ui.onmessage;

	res.model.init();
	if (!res.config.pages.credential.preload){
		res.event.open();		
	}

	$(window).keypress(function(e){
		window.parent.res.main.keyboard(e.which);
	});

	setTimeout(function(){	// wait for resInclude="pickList.html" loaded 
		res.ui.send( { event: "credential.init"} );	
	}, 1000);

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
			window.parent.res.main.skin("normal");
			var user = window.parent.res.ui.root.model.user;
			if (user.ID){
				res.ui.root.model.status = "passcode";
			}else{
				res.ui.root.model.status = "initial";
			}
		});
	};
	res.event.close = function(){
		
	};
	res.event.keyboard = function(entry){
		res.ui.send({ event: "credential.signIn.userId", data: entry});		
	};
	
	res.ui.onmessage = function(message){
		apply(function(){
			res.model.update(message.event, message.data);
		});
	};
	
})();

