/*** c8: start of update ***/
var res = res || {};
res.messaging = res.messaging || {};
/*** c8: end of update ***/

/*
 * Messaging between User Interface and Core
 */
(function(){
	
	res.messaging.sockets = {};
	res.messaging.sockets.local = {};
	res.messaging.sockets.remote = [];
	res.messaging.tracer = undefined;
	
	PseudoSocket = function(name){
		this.onmessage = undefined;
		res.messaging.sockets.local.receiver = this;
		res.messaging.sockets.local.name = name;
		if (window.opener){
			window.opener.res.messaging.connect(name, res.messaging.receive);
			res.messaging.connect(window.opener.res.messaging.sockets.local.name, window.opener.res.messaging.receive);
		}else if (window.parent != window){
			window.parent.res.messaging.connect(name, res.messaging.receive);
			res.messaging.connect(window.parent.res.messaging.sockets.local.name, window.parent.res.messaging.receive);
		}
	};
	
	PseudoSocket.prototype = {
		send: function(message){
			res.messaging.send(message);
		},
	};

	res.messaging.connect = function(name, receiver){
		for (var i = 0; i < res.messaging.sockets.remote.length; i++){
			if (res.messaging.sockets.remote[i].name == name){
				res.messaging.sockets.remote[i].receiver = receiver;
				res.console("res.messaging.connect: \"" + res.messaging.sockets.local.name + "\"-\"" + name + "\" socket re-connected");
				return;
			}
		}
		res.messaging.sockets.remote.push({name: name, receiver: receiver});
		res.console("res.messaging.connect: \"" + res.messaging.sockets.local.name + "\"-\"" + name + "\" socket connected");
	};
	res.messaging.send = function(message){
		for (var i = 0; i < res.messaging.sockets.remote.length; i++){
			if (res.messaging.tracer){
				(res.messaging.tracer)(res.messaging.sockets.local.name, res.messaging.sockets.remote[i].name, message);
			}
			var buffer = $.extend(true, {}, message);
			(res.messaging.sockets.remote[i].receiver)(res.messaging.sockets.local.name, buffer);
			delete buffer;
		}
	};
	res.messaging.receive = function(source, message){
		if (res.messaging.tracer){
			(res.messaging.tracer)(source, res.messaging.sockets.local.name, message);
		}
		return (res.messaging.sockets.local.receiver.onmessage.call)(window, message);
	};
	res.messaging.trace = function(callback){
		res.messaging.tracer = callback;
	};
	
})();
