var res = res || {};
res.core = res.core || {};
res.core.imagecopy = res.core.imagecopy || {};

/*
 * File Access Core Logic
 */
$(document).ready(function() {

	// connect socket
	res.core.events.imagecopy = res.core.imagecopy.onmessage;

});

(function() {

	res.core.imagecopy.start = function() {
	}, res.core.imagecopy.send = function(message) {
		if (!message.context)
			message.context = context;
		res.core.socket.send(message);
	};
	res.core.imagecopy.onmessage = function(message) {
		context = message.context;
		switch (message.event) {
		case "imagecopy.copystart":
			res.service.imagecopy.copyStart(message.data, {
				success : function(result) {
					res.core.imagecopy.send({
						context : context,
						event : "imagecopy.copystart",
						data : result
					});
				},
				error : function(result) {
					res.core.imagecopy.send({
						context : context,
						event : "imagecopy.copystart",
						data : result
					});
				}
			});
			break;
		case "imagecopy.getfolder":
			res.service.imagecopy.getFolder(message.data, {
				success : function(result) {
					result["action"] = message.data.action;
					res.core.imagecopy.send({
						context : context,
						event : "imagecopy.getfolder",
						data : result
					});
				},
				error : function(result) {
					result["action"] = message.data.action;
					res.core.imagecopy.send({
						context : context,
						event : "imagecopy.getfolder",
						data : result
					});
				}
			});
			break;
		case "imagecopy.gettargetfolder":
			res.service.imagecopy.getTargetFolder(message.data, {
				success : function(result) {
					res.core.imagecopy.send({
						context : context,
						event : "imagecopy.gettargetfolder",
						data : result
					});
				},
				error : function(result) {
					res.core.imagecopy.send({
						context : context,
						event : "imagecopy.gettargetfolder",
						data : result
					});
				}
			});
			break;
		default:
			alert("res.core.transaction.onmessage: event handler not available\nmessage.event = "
					+ message.event);
			break;
		}
	};

})();

(function() {

})();
