var res = res || {};
res.core = res.core || {};
res.core.credential = res.core.credential || {};

/*
 * Core - Credential
 */
$(document).ready(function(){
	res.core.events.credential = res.core.credential.onmessage;
	res.core.credential.send = res.core.socket.send;
});

(function(){

	var context = undefined;
	var userID = undefined;
	var passcode = undefined;

	res.core.credential.onmessage =  function(message) {
		context = message.context;
		switch (message.event){
		case "credential.init":
			userID = undefined;
			var lang = res.storage.getItem("Language");
			if (!lang) lang = res.config.initialLanguage;
//			res.core.credential.send({context:"Main", event:"settings.language", data: lang});
//			var profile = res.service.device.profile();
//			res.core.credential.send({context:"Main", event:"credential.profile", data: {
//				company: profile.company,
//				store: profile.store,
//				device: profile.device,
//				screen2nd: profile.screen2nd,
//				name: profile.name
//			}});
//			res.core.credential.send({context:"Main", event:"credential.profile", data: {
//					company: undefined,
//					store: undefined,
//					device: undefined,
//					screen2nd: undefined,
//					name: undefined
//				}});
			break;
		case "credential.signIn.userId":
			userID = message.data;
			passcode = undefined;
//			if (!res.config.autoLoginPasscode){
				res.core.credential.send({context:context, event:"credential.signIn.passcodeRequired", data: undefined});
//			}else{
//				passcode = res.config.autoLoginPasscode;
//				res.core.credential.trySignIn();
//			}
			break;
		case "credential.signIn.passcode":
			passcode = message.data;
			res.core.credential.trySignIn();
			break;
		case "credential.signOut":
			res.core.credential.send( {context:context, event:"credential.signOut.successful", data: undefined});
			break;
		case "credential.lock":
			res.core.credential.send( {context:context, event:"credential.lock.successful", data: undefined});
			break;
		default:
			alert("res.core.credential.onmessage: event not defined\n message.event = " + message.event );
		break;
		}
	};

	res.core.credential.trySignIn = function(){
		var user = res.service.credential.login(userID, passcode);
		if (user == undefined){
			res.core.credential.send({context:context, event:"credential.signIn.failed", data: undefined});
			return;
		}
		user.changeFundReady = false;
		res.core.credential.send({context:context, event:"credential.signIn.successful", data: user});
	};

})();
