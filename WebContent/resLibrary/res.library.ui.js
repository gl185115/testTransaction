
(function(){
	
    var baseURL = "";
    var scripts = document.getElementsByTagName("script");
    for (var i = 0; i < scripts.length; i++) {
        var match = scripts[i].src.match(/(^|.*\/)resLibrary\/res.library.ui\.js$/);
        if (match) {
        	baseURL = match[1];
            break;
        }
    }
  
    document.write('<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">');
    document.write('<meta name="apple-mobile-web-app-status-bar-style" content="black">');
    document.write('<meta name="apple-mobile-web-app-capable" content="yes" />');

//  document.write('<script src="' + baseURL + '/external/spin.min.js" type="text/javascript"></script>');
//	document.write('<script src="' + baseURL + '/external/jquery-barcode/2.0.3/jquery-barcode.min.js" type="text/javascript"></script>');
    document.write('<script src="' + baseURL + '/external/jquery/1.11.1/jquery-1.11.1.min.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/external/angularjs/1.2.25/angular.min.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/external/angularjs/1.2.25/angular-sanitize.min.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/external/angularjs/1.2.25/angular-animate.min.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/external/angularjs/1.2.25/angular-touch.min.js" type="text/javascript"></script>');
//	document.write('<script src="' + baseURL + '/external/iscroll/iscroll-4/iscroll.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/external/iscroll/iscroll-5/iscroll.js" type="text/javascript"></script>');

	document.write('<link href="' + baseURL + '/resLibrary/res.styles.css" rel="stylesheet" type="text/css"></link>');
	
	document.write('<link href="' + baseURL + '/resLibrary/res.animate.css" rel="stylesheet" type="text/css"></link>');
	document.write('<script src="' + baseURL + '/resLibrary/res.console.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/resLibrary/res.directives.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/resLibrary/res.filters.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/resLibrary/res.canvas.js" type="text/javascript"></script>');
	document.write('<script src="' + baseURL + '/resLibrary/res.string.js" type="text/javascript"></script>');
	
	document.write('<script src="' + baseURL + '/resLibrary/res.bridge.js" type="text/javascript"></script>');

	document.addEventListener("keydown", function(event){
		var keycode = event.keyCode;
		var ctrl    = event.ctrlKey;
		var shift   = event.shiftKey;
		var alt 	= event.altKey;
		var srcElement=event.srcElement;
		var readOnly=false;
		var tagName=undefined;
		if(srcElement){
			readOnly=srcElement.readOnly
		}
		if (srcElement&&srcElement.tagName){
			tagName=srcElement.tagName.toLowerCase();
		}
        
		if (((tagName!="input"&&tagName!="textarea")||readOnly)&& keycode == 8){	// ignore back space
			event.preventDefault();
		}
	});
		
})();

