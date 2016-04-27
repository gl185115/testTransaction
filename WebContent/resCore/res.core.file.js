var res = res || {};
res.core = res.core || {};
res.core.file = res.core.file || {};

/*
 *	File Access Core Logic
 */
$(document).ready(function(){

	// connect socket
	res.core.events.file = res.core.file.onmessage;

});

(function(){

	res.core.file.start = function() {
//		var config = res.service.pickList.get();
//		res.core.transaction.send({ context:"Transaction", event:"PickListConfig", data: config });
	},

	res.core.file.send = function(message) {
		if (!message.context)
			message.context = context;
		res.core.socket.send(message);
	};

	res.core.file.onmessage = function(message) {
		context = message.context;
		switch (message.event){
		case "file.list":
			res.service.file.list(message.data, {
				success:function(result) {
					res.core.file.send({context: context, event:"file.list", data:result});
				},
				error:function(result){
					res.core.file.send({context: context, event:"file.list", data:result});
				}
			});
			break;
		case "file.download":
			res.service.file.download(message.data, {
				success:function(result) {
					 res.core.file.send({context:context, event:"file.download", data:result});
				},
				error:function(result) {
					res.core.file.send({context:context, event:"file.download", data:result});
				}
			});
			break;
		case "file.remove":
			res.service.file.remove(message.data, {
				success:function(result) {
					res.core.file.send({context:context, event:"file.remove", data:result});
				},
				error:function(result) {
					res.core.file.send({context:context, event:"file.remove", data:result});
				}
			});
			break;
		case "file.upload":
			res.service.file.upload(message.data, {
				success:function(result) {
					res.core.file.send({context:context, event:"file.upload", data:result});
				},
				error:function(result) {
					res.core.file.send({context:context, event:"file.upload", data:result});
				}
			});
			break;
		case "file.picture.list":
			res.service.file.pictureList(message.data, {
				success:function(result){
					res.core.file.send({context:context, event:"file.picture.list", data:result});
				},
				error:function(result){
					res.core.file.send({context:context, event:"file.picture.list", data:result});
				}
			});
			break;
		case "file.picture.upload":
			res.service.file.pictureUpload(message.data, {
				success:function(result){
					res.core.file.send({context:context, event:"file.picture.upload.success", data:result});
				},
				error:function(result){
					res.core.file.send({context:context, event:"file.picture.upload.failed", data:{}});
				}
			});
			break;
		default:
			res.console("res.core.file.onmessage: event handler not available\nmessage.event = " + message.event);
			break;
		}
	};

})();

(function(){

})();

