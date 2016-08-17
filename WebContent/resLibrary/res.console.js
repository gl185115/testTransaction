/*
 * Messaging between User Interface and Core
 */

var res = res || {};

(function(){
	
	res.console = function(log) {
		if (!res.config.consoleEnabled) return;
		console.log(log);
	};
	
})();


