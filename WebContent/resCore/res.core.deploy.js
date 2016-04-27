var res = res || {};
res.core = res.core || {};
res.core.deploy = res.core.deploy || {};

/*
 * Settings Core
 */
$(document).ready(function(){

	// connect socket

	res.core.events.deploy = res.core.deploy.onmessage;

});

(function(){

	var context = "Deploy";

	res.core.deploy.send = function(message){
		if (!message.context) message.context = context;
		res.core.socket.send(message);
	};
	res.core.deploy.onmessage =  function(message) {
		context = message.context;
		switch (message.event){
		case "deploy.notices.getDeployStoreAndGroup":
			res.service.deploy.getDeployStoreAndGroup(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.getDeployStoreAndGroup.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.getDeployStoreAndGroup.failed", data:result});
				}
			});
			break;
		case "deploy.notices.getSchedule":
			res.service.deploy.getSchedule(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getSchedule.successful", data: eval(result) });
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.getSchedule.failed", data:result});
				}
			});
			break;
		case "deploy.notices.setSchedule":
			res.service.deploy.setSchedule(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.setSchedule.successful", data: undefined});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.setSchedule.failed", data: undefined});
				}
			});
			break;
		case "deploy.notices.getDeployStatus":
			res.service.deploy.getDeployStatus(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getDeployStatus.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getDeployStatus.failed", data:result});
				}
				});
			break;
		case "deploy.notices.getDeployDetail":
			res.service.deploy.getDeployDetail(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getDeployDetail.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getDeployDetail.failed", data:result});
				}
				});
			break;
		case "deploy.notices.saveDeployDetail":
			res.service.deploy.saveDeployDetail(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.saveDeployDetail.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.saveDeployDetail.failed", data:result});
				}
			});
			break;
		case "deploy.notices.viewTableStore":
			res.service.deploy.viewTableStore(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.viewTableStore.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.viewTableStore.failed", data:{}});
				}
			});
			break;
		case "deploy.notices.editTableStore":
			res.service.deploy.editTableStore(message.data, {
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.editTableStore.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.editTableStore.failed", data:result});
				}
			});
			break;
		case "deploy.notices.viewTableGroup":
			res.service.deploy.viewTableGroup(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.viewTableGroup.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.viewTableGroup.failed", data:result});
				}
			});
			break;
		case "deploy.notices.editTableGroup":
			res.service.deploy.editTableGroup(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.editTableGroup.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.notices.editTableGroup.failed", data:result});
				}
			});
			break;
		case "deploy.notices.getSysFileTree":
			res.service.deploy.getSysFileTree(message.data, {
				success: function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getSysFileTree.successful", data:result});
				},
				error: function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getSysFileTree.failed", data:result});
				}
			});
			break;
		case "deploy.notices.invokeTransfer":
			res.service.deploy.invokeTransfer(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.invokeTransfer.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.invokeTransfer.failed", data:result});
				}
			});
			break;
		case "deploy.notices.resetNotice":
			res.service.deploy.resetNotice(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.resetNotice.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.resetNotice.failed", data:result});
				}
			});
		break;
		case "deploy.notices.getNotices":
			res.service.deploy.getNotices(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getNotices.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.notices.getNotices.failed", data:result});
				}
			});
		break;



		case "deploy.pickList.getDeployStoreAndGroup":
			res.service.deploy.getDeployStoreAndGroup(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.getDeployStoreAndGroup.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.getDeployStoreAndGroup.failed", data:result});
				}
			});
			break;
		case "deploy.pickList.getSchedule":
			res.service.deploy.getSchedule(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.getSchedule.successful", data: eval(result) });
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.getSchedule.failed", data:result});
				}
			});
			break;
		case "deploy.pickList.setSchedule":
			res.service.deploy.setSchedule(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.setSchedule.successful", data: undefined});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.setSchedule.failed", data: undefined});
				}
			});
			break;
		case "deploy.pickList.getDeployStatus":
			res.service.deploy.getDeployStatus(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.getDeployStatus.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.getDeployStatus.failed", data:result});
				}
				});
			break;
		case "deploy.pickList.getDeployDetail":
			res.service.deploy.getDeployDetail(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.getDeployDetail.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.getDeployDetail.failed", data:result});
				}
				});
			break;
		case "deploy.pickList.saveDeployDetail":
			res.service.deploy.saveDeployDetail(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.saveDeployDetail.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.saveDeployDetail.failed", data:result});
				}
			});
			break;
		case "deploy.pickList.viewTableStore":
			res.service.deploy.viewTableStore(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.viewTableStore.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.viewTableStore.failed", data:{}});
				}
			});
			break;
		case "deploy.pickList.editTableStore":
			res.service.deploy.editTableStore(message.data, {
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.editTableStore.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.editTableStore.failed", data:result});
				}
			});
			break;
		case "deploy.pickList.viewTableGroup":
			res.service.deploy.viewTableGroup(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.viewTableGroup.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.viewTableGroup.failed", data:result});
				}
			});
			break;
		case "deploy.pickList.editTableGroup":
			res.service.deploy.editTableGroup(message.data,{
				success:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.editTableGroup.successful", data:result});
				},
				error:function(result){
					res.core.deploy.send({context:context, event:"deploy.pickList.editTableGroup.failed", data:result});
				}
			});
			break;
		case "deploy.pickList.getSysFileTree":
			res.service.deploy.getSysFileTree(message.data, {
				success: function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.getSysFileTree.successful", data:result});
				},
				error: function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.getSysFileTree.failed", data:result});
				}
			});
			break;
		case "deploy.pickList.invokeTransfer":
			res.service.deploy.invokeTransfer(message.data, {
				success:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.invokeTransfer.successful", data:result});
				},
				error:function(result) {
					res.core.deploy.send({context:context, event:"deploy.pickList.invokeTransfer.failed", data:result});
				}
			});
			break;

		}
	};

})();
