var res = res || {};
res.core = res.core || {};
res.core.device = res.core.device || {};

/*
 * Core - Credential
 */
$(document).ready(function(){
	res.core.events.device = res.core.device.onmessage;
	res.core.device.send = res.core.socket.send;
});

(function(){

	var context = undefined;
	var userID = undefined;
	var passcode = undefined;

	res.core.device.onmessage =  function(message) {
		context = message.context;
		switch (message.event){
		case "device.profile":
			res.service.device.profile(message.data, {
				success:function(result){
					if(result.CompanyID){
						res.storage.setItem("CompanyID",result.CompanyID);
					}
					if(result.RetailStoreID){
						res.storage.setItem("RetailStoreID",result.RetailStoreID);
					}
					if(result.WorkstationID){
						res.storage.setItem("WorkstationID",result.WorkstationID);
					}
					res.core.device.send({ context: context, event: "device.profile.successful", data: {
						company: res.storage.getItem("CompanyID"),
						store: res.storage.getItem("RetailStoreID"),
						device: res.storage.getItem("WorkstationID"),
						screen2nd: res.storage.getItem("Screen2nd"),
						name: res.storage.getItem("DeviceName"),
					} });
				},
				error:function(result){
					res.core.device.send({ context: context, event: "device.profile.failed", data: {} });
				}
			});
			break;
		case "device.schedule.balance":
			res.service.device.balanceSchedule(message.data, {
				success : function(result) {
					res.core.device.send({context: context, event:"device.schedule.balance.successful", data: eval(result) });
				},
				error : function(result) {
					res.core.device.send({context: context, event:"device.schedule.balance.failed", data: eval(result) });
				}
			});
			break;
		default:
			alert("res.core.device.onmessage: event not defined\n message.event = " + message.event );
			break;
		}
	};

})();
