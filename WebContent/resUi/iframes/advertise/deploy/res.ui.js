/*
 *	UI Configuration Deployment Schedule and Status
 */
res.ui = angular.module("res.ui", ["res.filters", "res.directives", "ngSanitize", "ngAnimate","720kb.datepicker"]);
res.ui.context = "Advertise.Deploy";

/*
 * Main application in pure JavaScript/jQuery
 */
$(document).ready(function(){
	// get angularJS $rootScope reference
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

	var sections = [ "deploy" ];
	var indexSection = 0;

	function apply(func){
		// from outside non-AngularJS
		if (!res.ui.root.$$phase) {
			res.ui.root.$apply((func)());
		}else{
			(func)();
		};
	}

	res.event = {};
	res.event.open = function(){
		apply(function(){
			// inherit parent language setting for angularJS filter
			res.ui.root.language = window.parent.res.bridge.get("root", "language");
			res.ui.root.context = res.ui.context;
			res.ui.root.dialog="";
			res.model.init();

			res.ui.send({
				context : res.ui.context,
				event: "deploy.pickList.getDeployStoreAndGroup",
				data: { companyID : res.storage.getItem("CompanyID") }
			});

			res.ui.next();

			indexSection = 0;
			res.ui.root.model.doRefreshSchedule = true;
			window.parent.res.ui.root.model.popup = "Wait";
		});
	};

	res.event.close = function() {
		apply(function() {
			res.ui.root.section = "";
		});
	};

	res.ui.next = function() {
		if (indexSection < sections.length) {
			res.ui.root.section = sections[indexSection];
			indexSection++;
		} else {
			indexSection = 0;
			window.parent.res.main.page("home");
		}
	};

	res.ui.onmessage = function(message) {
		apply(function(){
			res.model.update(message.event, message.data);
		});
	};

})();

(function(){

	res.ui.canvas = function(id){

		$(id + " .IconFolder").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 32;
			canvas.height = 32;
			var context = canvas.getContext('2d');
			context.beginPath();
			context.strokeStyle = 'hsl(0, 0%,30%)';
			context.lineWidth = 1;
			context.lineCap = 'round';
			context.moveTo(1,5);
			context.lineTo(5,25); context.lineTo(31,25); context.lineTo(27,5); context.lineTo(1,5);
			context.moveTo(5,5);
			context.lineTo(5,1); context.lineTo(31,1); context.lineTo(31,25);
			context.stroke();
			$(this).append(canvas);
		});

		$(id + " .PointLeft").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 20;
			canvas.height = 60;
			var context = canvas.getContext('2d');
			context.fillStyle = 'hsl(34,30%,90%)';
			context.beginPath();
			var grad  = context.createLinearGradient(0,0, 20,0);
			grad.addColorStop(0, 'hsl(34,30%,80%)');
			grad.addColorStop(1, 'hsl(34,30%,100%)');
			context.fillStyle = grad;
			context.moveTo(20, 0);
			context.lineTo(20, 60);
			context.lineTo(0, 30);
			context.closePath();
			context.fill();
			$(this).append(canvas);
		});

	};

})();
