var res = res || {};
res.ui = res.ui || {};

/*
 *  Main Page Module in AngularJS
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize", "ngAnimate"]);

var userAgent = undefined;

$(document).ready(function(){
	document.getElementById("resSkin").href = res.config.baseURL + res.config.skin["normal"];			
});

angular.element(document).ready(function(){
	userAgent = window.navigator.userAgent.toLowerCase();
	if (userAgent.indexOf("ipad") != -1){	// on iPad browser 
		$("#StatusBar").hide();
	}else if (userAgent=="mobileshop"){ // on RES mobileshop. the window overlaps with iOS status bar
		$("#StatusBar, #StatusBar *").css("opacity", 0);
	}

	res.ui.root = angular.element(document).scope();	// get angularJS $rootScope
	// inherit parent language setting for angularJS filter
	res.ui.root.context = "Main";
	res.ui.root.language = res.storage.getItem("Language");
	if (!res.ui.root.language) res.ui.root.language = res.config.initialLanguage;

	// connect socket

	res.main.socket = new PseudoSocket("UI-1");
	res.main.socket.onmessage = function(message){
		if (res.main.socket.onmessage[message.context]){
			res.main.socket.onmessage[message.context](message);
		}
	};
	res.ui.send = function(message){
		if (!message.context) message.context = res.ui.root.context;
		res.main.socket.send(message);
	};
	res.main.socket.onmessage[res.ui.root.context] = res.ui.onmessage;

	$(window).keypress(function(e){
		res.main.keyboard(e.which);
	});
	
	res.model.init();
	res.ui.canvas();
	
	res.ui.send({ event: "device.profile" });
	
	window.addEventListener('beforeunload', function(event){
		if (res.ui.root.subject !== 'home'){
			var confirmation = 'このまま画面を閉じると、保存していないデータが失われることがあります。';
			event.returnValue = confirmation;
			return confirmation;			
		}
	});
	
});

/*
 * External Events
 */
(function(){

	res.main = {};
	res.main.close = function(){
		res.ui.send({context:"settings", event: "settings.terminate"});
	};
	res.main.minimize = function(){
		res.ui.send({context:"settings", event: "settings.minimize"});
	};

	function apply(func){
		if (!res.ui.root.$$phase) { // from outside non-AngularJS
			res.ui.root.$apply((func)());
		}else{
			(func)();
		};
	}

	res.main.page = function(choice){
		apply(function(){
			res.ui.root.subject = choice;
			res.ui.root.pageOnBoard = choice.split(".")[0];
		});
	};

	function apply(func){
		if (!res.ui.root.$$phase) { // from outside non-AngularJS
			res.ui.root.$apply((func)());	
		}else{		
			(func)();
		};
	}

	res.main.language = function(choice){
		apply(function(){
			res.ui.root.language = choice;
			var elements = document.getElementsByTagName("iframe");
			for (var i = 0; i < elements.length; i++){
				if (elements[i].contentWindow.res.bridge){
					elements[i].contentWindow.res.bridge.set("root", "language", res.ui.root.language);
				}
				if (elements[i].contentWindow.res.locale){ // for non-AngularJS iframes
					elements[i].contentWindow.res.locale.refresh();
				}
			}		
		});
	};
	
	var buffer = "";
	var entered = false;

	res.main.keyboard = function(c){
		switch (c){
		case 8:  	// back space
		case 98: 	// 'b'
			buffer = buffer.slice(0, -1);
			entered = false;
			break;
		case 13:	// Enter
		case 101:	// 'e'
			var elements = document.getElementsByName(res.ui.root.pageOnBoard);
			if (elements[0].contentWindow.res && elements[0].contentWindow.res.event && elements[0].contentWindow.res.event.keyboard) {
				elements[0].contentWindow.res.event.keyboard(buffer);		
			}
			entered = true;
			return;
		case 99: 	// 'c'
			buffer = "";
			entered = false;
			break;
		default:
			if (entered){
				buffer = "";
				entered = false;
			}
			buffer += String.fromCharCode(c);
			break;
		}
		$("#PageIndicators .PageIndicator .Value").text(buffer);
	};
	
	res.main.msr = {
		activate : function(){
			if (userAgent=="mobileshop"){
				document.location = "res:activateMSR:res.main.msr.input";
			}
		},			
		deactivate: function(){
			if (userAgent=="mobileshop"){
				document.location = "res:deactivateMSR";
			}
		},
		input : function(card){
			var elements = document.getElementsByName(res.ui.root.pageOnBoard);
			if (elements[0].contentWindow.res && elements[0].contentWindow.res.event && elements[0].contentWindow.res.event.msr) {
				elements[0].contentWindow.res.event.msr(card);
			}
		},
	};

	res.main.cursor = {
		hide : function(){
			document.body.style.cursor = "url('../images/main/transparent-2x2.png'),'.'";
			elements = document.getElementsByTagName("iframe");
			for (var i = 0; i < elements.length; i++ ){
				elements[i].contentWindow.document.body.style.cursor = "url('../../images/main/transparent-2x2.png'),'.'";
			}
		},
		show : function(){
			document.body.style.cursor = "auto";
			elements = document.getElementsByTagName("iframe");
			for (var i = 0; i < elements.length; i++ ){
				elements[i].contentWindow.document.body.style.cursor = "auto";
			}
		},
	};
	
	res.main.skin = function(choice){
		document.getElementById("resSkin").href = res.config.baseURL + res.config.skin[choice];			
		var elements = document.getElementsByTagName("iframe");
		for (var i = 0; i < elements.length; i++ ){
			var childSkin = elements[i].contentWindow.document.getElementById("resSkin");
			if (childSkin) childSkin.href = res.config.baseURL + res.config.skin[choice];
		}
	};

	/*
	 * Message handler
	 */
	res.ui.onmessage = function(message){
		apply(function(){
			res.model.update(message.event, message.data);
		});
	};

})();

(function(){
	
	res.ui.canvas = function(){
		
		$(".IconMenu").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 32;
			canvas.height = 32;
			var context = canvas.getContext('2d');
			context.beginPath();
			context.strokeStyle = 'hsl(112,45%,95%)';
			context.lineWidth = 4;
			context.lineCap = 'round';
			context.moveTo(2,8);
			context.lineTo(30,8);
			context.moveTo(2,16);
			context.lineTo(30,16);
			context.moveTo(2,24);
			context.lineTo(30,24);
			context.stroke();
			$(this).append(canvas);
		});

		$(".IconBack").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 30;
			canvas.height = 30;
			var context = canvas.getContext('2d');
			context.beginPath();
			var grad  = context.createLinearGradient(30,0, 0,0);
			grad.addColorStop(0, 'hsl(0,0%,100%)');
			grad.addColorStop(1, 'hsl(0,0%,90%)');
			context.fillStyle = grad;
			context.moveTo(0, 15); 
			context.lineTo(20, 3); 
			context.lineTo(20, 27); 
			context.closePath(); 
			context.fill(); 			
			$(this).append(canvas);
		});
		
	};
})();
