var res = res || {};
res.core = res.core || {};
res.core.settings = res.core.settings || {};

/*
 * Settings Core
 */
$(document).ready(function(){

	// connect socket
	res.core.events.settings = res.core.settings.onmessage;

});

(function(){

	var context = "Settings";

	res.core.settings.send = function(message){
		if (!message.context) message.context = context;
		res.core.socket.send(message);
	};

	res.core.settings.onmessage =  function(message) {
		context = message.context;

		switch (message.event) {
		case "settings.reload":
			res.core.close();
			window.location.reload();
			break;
		case "settings.terminate":
			res.core.close();
			var userAgent = window.navigator.userAgent.toLowerCase();
			if (userAgent.indexOf("openwebkitsharp") != -1){	// on RES UI Container
				window.location = "/terminate";	// for UI Container
			}else{
				window.opener = window;
				var win = window.open(location.href, "_self");
				win.close();
			}
			alert("res.main.close: failed to close the browser/container");
			break;
		case "settings.showCore":
			res.core.show("core");
			break;
		case "settings.language.change":
			var lang = message.data;
			res.storage.setItem("Language", lang);
			res.core.settings.send({ event:"settings.language.change.successful", data: lang});
			break;
		case "settings.training.setMode":
			var mode = message.data;
			res.core.settings.send({ event:"settings.training.setMode.successful", data: mode});
			break;
		case "settings.printers.getList":
			if (typeof message.data === 'undefined'){
				var printers = res.service.print.getPrinters();
				res.core.settings.send({ event:"settings.printers.getList.successful", data: printers});
			}
			break;
		case "settings.queues.getList":
			if (typeof message.data === 'undefined'){
				var queues = res.service.device.getTransfer();
				res.core.settings.send({ event:"settings.queues.getList.successful", data: queues});
			}
			break;
		case "settings.getSoftwareVersions":
			var resultData = {
				tomcat: 'unknown',
				webStore: ' ',
				j2EE: 'unknown'
			};
			if (!res.service.settings) {
				res.core.settings.send({ event: "settings.getSoftwareVersions.successful", data: resultData});
				return;
			}
			res.service.settings.getSoftwareVersions({
				success: function(result) {
					res.core.settings.send({ event: "settings.getSoftwareVersions.successful", data: result });
				},
				error: function() {
					res.core.settings.send({ event: "settings.getSoftwareVersions.failed", data: undefined});
				}
			});
			break;
		case "settings.profile.change":
			res.service.settings.profile(message.data, {
				success:function(result){
					res.core.settings.send({context:"Settings", event:"settings.profile.change.successful", data:result});
				},
				error:function(result){
					res.core.settings.send({context:"Settings", event:"settings.profile.change.failed", data:result});
				}
			});
			break;
		}
	};

})();
