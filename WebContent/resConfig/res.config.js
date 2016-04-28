var res = res || {};
res.config = res.config || {};
/*
 * Package Name
 */
res.config.version = "1.5.4";

$(document).ready(function(){
	var skin = document.getElementById("resSkin");
	if (skin) skin.href = res.config.baseURL + res.config.skin["normal"];
});


(function(){

	res.storage = localStorage;
//	res.storage = sessionStorage;	// sessionStorage or localStorage
    res.config.allstores=undefined;
    res.config.baseURL = "";
    var scripts = document.getElementsByTagName("script");
    for (var i = 0; i < scripts.length; i++) {
        var match = scripts[i].src.match(/(^|.*\/)resConfig\/res.config\.js$/);
        if (match) {
        	res.config.baseURL = match[1];
            break;
        }
    }

    if (window.opener && window.opener.res.config){
    	referTo(window.opener.res.config);
    }else if (window.parent != window && window.parent.res.config){
    	referTo(window.parent.res.config);
    }else{
    	configDefault();
    	res.config.update = configUpdate;
    }

	function referTo(obj){
//		$.extend(res.config, obj);
		res.config = obj;
	}

	function configDefault(){
    	document.write('<script src="' + res.config.baseURL + '/resConfig/res.config.notices.js" type="text/javascript"></script>');
		document.write('<script src="' + res.config.baseURL + '/resConfig/res.config.usability.js" type="text/javascript"></script>');
//		document.write('<script src="' + res.config.baseURL + '/resConfig/res.config.options.js" type="text/javascript"></script>');
		document.write('<script src="' + res.config.baseURL + '/resConfig/res.config.phrases.js" type="text/javascript"></script>');
//		document.write('<script src="' + res.config.baseURL + '/resConfig/res.config.pickList.js" type="text/javascript"></script>');

		document.write('<script src="' + res.config.baseURL + '/resConfig/res.config.storage.js" type="text/javascript"></script>');

		/*
		 * Configuration during development
		 */
		res.config.consoleEnabled = true;	// turn on console log
		res.config.childWindow = "iframe";	// window or iframe. iPad is fixed to iframe mode
		//res.config.childWindow = "window";	// window or iframe. iPad is fixed to iframe mode

		// the followings are to be shown in configuration section on Settings>Software
		res.config.userInterface = res.config.version;
		res.config.services = {};
		res.config.services.transaction = "NCR RES Transaction Service V3.3.0";
		res.config.services.inventory = "NCR RES Inventory Service V3.3.0";
		res.config.services.consumer = "NCR RES Consumer Service V3.3.0";

//		res.config.serverURL = "../../../../C9999/rest/AeonDemoPrint";

		res.storage.setItem("CompanyID", "01");
		res.config.companyID = res.storage.getItem("CompanyID");
//		res.config.companyID = "00000000";
		res.config.storeID = "";
		res.config.workstationID = "";

		// Define ImageSize used by Image Copy and Resize.
		res.config.imagecopy={};
		res.config.imagecopy.targetWidth=76;
		res.config.imagecopy.targetHeight=114;

	}

	function configUpdate(){
//		var companyID = "companyID=" + res.storage.getItem("CompanyID");
//		var storeID = "storeID=" + res.storage.getItem("RetailStoreID");
//		var workstationID = "workstationID=" + res.storage.getItem("WorkstationID");
//		$.getScript("/resUiConfig/custom/usability?" + companyID + "&" + storeID + "&" + workstationID, function(){
//			res.console("res.core.js: successfully loaded custom/usability");
//		});
//		$.getScript("/resUiConfig/custom/options?" + companyID + "&" + storeID + "&" + workstationID, function(){
//			res.console("res.core.js: successfully loaded custom/options");
//		});
//		$.getScript("/resUiConfig/custom/pickList?" + companyID + "&" + storeID + "&" + workstationID, function(){
//			res.console("res.core.js: successfully loaded custom/pickList");
//		});
	}


})();

