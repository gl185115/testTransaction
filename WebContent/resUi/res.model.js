var res = res || {};
res.model = res.model || {};

/*
 *	Data Model Access for 'Loan' cash
 */
(function(){

	var model = undefined;

	res.model.init = function(){
		res.ui.root.model = new MainWindow();
		model = res.ui.root.model;
	};
	res.model.update = function(event, data) {
		switch(event){
//		case "device.authenticate.successful":
//			res.main.page("credential");
//			break;
//		case "device.authenticate.failed":
//			location.href = "../resAuthenticate/";
//			break;
		case "device.profile.successful":
			if (!data.company){
				res.main.page("settings");
				return;
			}
			model.companyId = data.company;
			model.storeId = data.store;
			model.workstationId = data.device;
			model.screen2nd = data.screen2nd;
			model.workstationName = data.name;
			model.printer = data.printer;
			model.transferLink = data.transferLink;

			res.ui.send({
				event : "device.schedule.balance",
				data : {
					companyID : model.companyId
				}
			});

			res.main.page("home");

//			res.ui.send({event:"settings.profile.change",
//				data:{
//					'workstationid':res.storage.getItem("WorkstationID"),
//					'storeid':res.storage.getItem("RetailStoreID") ,
//					'companyid':res.storage.getItem("CompanyID")
//				}});
//			res.main.page("credential");
			break;
		case "company.registration.successful":
		    if(data) {
		        model.companies = data;
		        res.main.page("settings");
		    }
		    break;
//		case "device.deregister.successful":
//			window.parent.location.href = "../../../resAuthenticate/";
//			break;
		case "credential.signOut.successful":
			model.user = { ID: "", name: ""};
			res.ui.root.model.menuEnabled = false;
//			res.main.page("credential");
			break;
		case "credential.lock.successful":
			res.ui.root.model.menuEnabled = false;
//			res.main.page("credential");
			break;
		case "device.schedule.balance.successful":
			res.console("resUiConfig: successful to integration schedule.xml");
			break;
		case "device.schedule.balance.failed":
			res.console("resUiConfig: failed to integration schedule.xml");
			break;
		}
	};

})();

(function(){

	MainWindow = function(){
		this.date = undefined;
		this.companies = undefined;
		this.companyId = undefined;
		this.storeId = undefined;
		this.workstationId = undefined;
		this.screen2nd = undefined;
		this.workstationName = undefined;
		this.autoLoginPasscode = undefined;
		this.user = { ID:"", name:"" };
		this.menuEnabled = false;
		this.transactionType = undefined;
		this.priceChecking = false;
		this.training = false;
		this.buttonLeft="(subject!='home') || model.active";
		this.buttonRight="subject=='credential'";
		this.popup = "";
	};
	MainWindow.prototype = {
	};

})();
