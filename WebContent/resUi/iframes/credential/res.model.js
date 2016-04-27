var res = res || {};
res.model = res.model || {};

/*
 *	Data Model Access for 'Home' page
 */
(function(){

	var model = undefined;
	
	res.model.init = function(){
		res.ui.root.model = new Credential();
		model = res.ui.root.model;
	};
	res.model.update = function(event, data) {
		switch(event){
		case "credential.signIn.passcodeRequired":
			model.status = "passcode";
			if (res.config.autoLoginPasscode){
				res.ui.send({ event: "credential.signIn.passcode", data: res.config.autoLoginPasscode });
			}
			break;
		case "credential.signIn.successful":
			model.status = "authenticated";
			var today = new Date();
			var yyyy = "" + today.getFullYear();
			var mm = "" + (today.getMonth() + 1);
			if (mm.length < 2) mm = "0" + mm;
			var dd = "" + today.getDate();
			if (dd.length < 2) dd = "0" + dd;
			var hh = "" + today.getHours();
			if (hh.length < 2) hh = "0" + hh;
			var min = "" + today.getMinutes();
			if (min.length < 2) min = "0" + min;
			var ss = "" + today.getSeconds();
			if (ss.length < 2) ss = "0" + ss;
			window.parent.res.ui.root.model.businessDate = yyyy + "-" + mm + "-" + dd;
			window.parent.res.ui.root.model.user = data;
			window.parent.res.ui.root.model.menuEnabled = true;
			window.parent.res.main.page(res.config.initialPage);			
			break;
		case "credential.signIn.failed":
			model.status = "rejected";
			break;
		default:
			alert("home: unknown message.event = " + event);
			break;
		}
	};

})();

/*
 * Data Model
 */
(function(){

	Credential = function(){
		this.status = "initial";
	};
	Credential.prototype = {
	};
	
})();
