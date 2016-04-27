/*** c8: start of update ***/
var res = res || {};
res.core = res.core || {};
/*** c8: end of update ***/

/*
 *  Core Application Main
 */
$(document).ready(function(){
	userAgent = window.navigator.userAgent.toLowerCase();
	if (userAgent.indexOf("ipad") != -1){ // on iPad Safari 
		res.config.childWindow = "iframe";
		$("#StatusBar").hide();
//		$("#UiFrame").css("height", 748);
	}else{
		$("#UiFrame").css("height", 768);
		if (userAgent.indexOf("mobileshop") != -1){	// on RES MobileShop. it overlaps with iOS status bar
			res.config.childWindow = "iframe";
			$("#StatusBar, #StatusBar *").css("opacity", 0);
		}else if (userAgent.indexOf("openwebkitsharp") != -1){	// on RES UI Container 
			res.config.childWindow = "iframe";
		}		
	} 
	res.core.init();

	// connect socket
	
	res.core.socket = new PseudoSocket("core");
	res.core.socket.onmessage = function(message){
		if(message.context == "Main"){	         
			switch (message.event) {
			case "main.page":			
		      res.core.settings.send({context:"Main",event: "main.page.successful", data: message.data });
		      return;
			case "main.showCore":
				res.core.show("core");
				return;
		    }
			
		}
		var keys = message.event.split(".");
		if (res.core.events[keys[0]]){
			res.core.events[keys[0]](message);
		}else{
			alert("res.core.socket.onmessage: message.event = " + message.event + " not available");
		}
	};
	res.messaging.trace(res.core.log);
	
	setTimeout("res.core.open()", 200);
		
});

(function(){
	
	var display1 = undefined;
	var display2 = undefined;
	var display1Url = "../resUi/";
	var display2Url = undefined;
	
	res.core = {
		events: {},
		
		init: function(){
			$("#Status .userAgent").append("<span>" + window.navigator.userAgent + "</span>");
			$("#Controls button.Open").removeAttr("disabled");
			$("#Controls button.Close").attr("disabled", "");
			$("#Controls button.Show").attr("disabled", "");
			$("#UiFrame").hide();
					
			$(window).keypress(function(e){
				if (display1){
					display1.contentWindow.res.main.keyboard(e.which);				
				}
			});

		},
		open: function(){
			var param = 
			"width=640,height=480," +
		//	"titlebar=no," +
		//	"manubar=no," +
		// 	"status=no," +
		//	"createnew=yes," +	// not supported???
		//	"toolbar=no," +		// IE and Firefox onlyl
		// 	"scrollbars=no," +	// IE, Firefox & Opera only
		// 	"location=no," +	// Opera only
		// 	"resizable=no," +	// IE only
		// 	"directories=no," +	// IE only
		// 	"fullscreen=yes," +	// IE only
			"";
			
			if (!display1){
				if (res.config.childWindow == "iframe"){
					$("#UiFrame").attr("src", display1Url);
					$("#UiFrame").show();
					display1 = document.getElementById("UiFrame");
				}else if (res.config.childWindow == "window"){
					display1 = window.open(display1Url, "display1", param);
					setTimeout(function(){
						display1.resizeTo(1024 + (display1.outerWidth - display1.innerWidth), 768 + (display1.outerHeight - display1.innerHeight));						
					}, 1000);
				}				
				if (!display1){
					alert("res.core.open: window.open was blocked");
				}else{
					$("#Status .Display1 td:nth-child(2)").text("Open");				
//					display1.moveTo(0, 0);		// not supported by Chrome	
				}
			}
//			if (!display2 && res.storage.getItem("Screen2nd")=="true") {
//				display2 = window.open(display2Url, "display2", param);
//				if (!display2){
//					alert("res.core.open: window.open was blocked");
//				}else{
//					setTimeout(function(){	// wait for the window gets ready
//						display2.resizeTo(1024 + (display2.outerWidth - display2.innerWidth), 768 + (display2.outerHeight - display2.innerHeight));
//						display2.moveTo(1024, 0);
//					}, 2000);	
//					$("#Status .Display2 td:nth-child(2)").text("Open");				
//				}
//			}
			if (display1){
				$("#Controls button.Open").attr("disabled", "");
				$("#Controls button.Close").removeAttr("disabled");
				$("#Controls button.Show").removeAttr("disabled");				
			}
			res.core.show("ui");
		},
		close: function(){
			$("#Controls button.Open").removeAttr("disabled");
			$("#Controls button.Close").attr("disabled", "");
			$("#Controls button.Show").attr("disabled", "");
			if (display1){
				if (res.config.childWindow == "iframe"){
					$("#UiFrame").hide();			
				}else{
					display1.close();
				}				
			}
			display1 = undefined;
			$("#Status .Display1 td").eq(1).text("Closed");
			if (display2) {
				display2.close();		
			}				
			display2 = undefined;
			$("#Status .Display2 td:nth-child(2)").text("Closed");
		},
		show: function(win){
			if (!display1) return;
			if (win == "ui"){
				if (res.config.childWindow == "iframe"){
					$("#UiFrame").show();
				}else{
					display1.focus();
				}				
			}else if (win == "core"){
				if (res.config.childWindow == "iframe"){
					$("#UiFrame").hide();
				}else{
					alert("Switching to Core Application...");
//					window.focus();
				}
			}
		},
		sendTo: function(target){
			var msg = $("#ManualEntry textarea.Message").val();
			if (target == "ui"){
				res.messaging.send(JSON.parse(msg));				
			}else{
				res.messaging.receive("manual", JSON.parse(msg));
			}
		},
		log: function(from, to, message){
			var now = new Date();
			var hh = "" + now.getHours();
			if (hh.length < 2) hh = "0" + hh;
			var mm = "" + now.getMinutes();
			if (mm.length < 2) mm = "0" + mm;
			var ss = "" + now.getSeconds();
			if (ss.length < 2) ss = "0" + ss;
			var msec = "" + now.getMilliseconds();
			if (msec.length < 3) msec = "0" + ss;
			var time = hh + ":" + mm + ":" + ss + "." + msec;
			var line = $("<tr>" +
					"<td>" + time + "</td>" +
					"<td>" + from + "</td>" + 
					"<td>" + to + "</td>" +
					"<td>" + message.context + "</td>" +
					"<td>" + message.event + "</td>" +
					"<td>" + JSON.stringify(message.data) + "</td>" +
			"</tr>");
			line.addClass(( from == "core")? "forward" : "backward");
			$("#Log .Messages").append(line);
		},
		
	};
	
})();

