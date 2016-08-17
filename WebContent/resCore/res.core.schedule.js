var res = res || {};
res.core = res.core || {};
res.core.schedule = res.core.schedule || {};

/*
 *	File Access Core Logic
 */
$(document).ready(function(){

	// connect socket
	res.core.events.schedule = res.core.schedule.onmessage;

});

(function(){

	res.core.schedule.start = function() {
	},

	res.core.schedule.send = function(message) {
		if (!message.context) message.context = context;
		res.core.socket.send(message);
	};

	res.core.schedule.onmessage = function(message) {
		context = message.context;
//		switch (message.event) {
//		case "schedule.get":
//			res.service.schedule.get(message.data, {
//				success:function(result) {
//					res.core.schedule.send({context: context, event:"schedule.get.successful", data: eval(result) });
//				},
//				error:function(result){
//					res.core.schedule.send({context: context, event:"schedule.get.failed", data:result});
//				}
//			});
//			break;
//		case "schedule.set":
//			res.service.schedule.set(JSON.stringify(message.data, null, "  "), {
//				success:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.set.successful", data: undefined});
//				},
//				error:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.set.failed", data: undefined});
//				}
//			});
//			break;
//		case "schedule.getNotices":
//			res.service.schedule.getNotices(message.data, {
//				success:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.getNotices.successful", data:result});
//				},
//				error:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.getNotices.failed", data:result});
//				}
//			});
//			break;
//		case "schedule.getDeployStatus":
//			res.service.schedule.getDeployStatus(message.data, {
//				success:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.getDeployStatus.successful", data:result});
//				},
//				error:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.getDeployStatus.failed", data:result});
//				}
//				});
//			break;
//		case "schedule.getDeployDetail":
//			res.service.schedule.getDeployDetail(message.data, {
//				success:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.getDeployDetail.successful", data:result});
//				},
//				error:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.getDeployDetail.failed", data:result});
//				}
//				});
//			break;
//		case "schedule.invokeTransfer":
//			res.service.schedule.invokeTransfer(message.data, {
//				success:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.invokeTransfer.successful", data:result});
//				},
//				error:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.invokeTransfer.failed", data:result});
//				}
//			});
//			break;
//		case "schedule.resetNoticeDate":
//			res.service.schedule.resetNoticeDate(message.data, {
//				success:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.resetNoticeDate.successful", data:{}});
//				},
//				error:function(result) {
//					res.core.schedule.send({context:context, event:"schedule.resetNoticeDate.failed", data:{}});
//				}
//			});
//			break;
//		default:
//			res.console("res.core.schedule.onmessage: event handler not available\nmessage.event = " + message.event);
//			break;
//		}
	};

})();

(function(){

})();

