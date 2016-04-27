/*
 * Controller
 */
res.ui.controller("menu", ["$scope", "$rootScope","$filter", function($scope, $rootScope,$filter){
	$scope.$watch("$routeChangeSuccess", function() {
    	$rootScope.noticeDisableClose = true;
    	$rootScope.noticeDisableOpen = false;
    });
	$scope.$watch(
			function(){ return $rootScope.model.notice.selectedImage; },
			function(newValue, oldValue){
				if(newValue){
					if(newValue=="false"||newValue=="undefined"){$scope.pictureAdd = true;}else{
						$scope.pictureAdd = false;
					}
				}else{
					$scope.pictureAdd = true;
				}
			}
		);
	$scope.exit = function(){
		window.parent.res.main.page("home");
	};
	$scope.create = function(){
		if ($rootScope.noticeDisableOpen) {return;}
		$rootScope.dialog = "fileCreate";
	};
	$scope.open = function(){
		if ($rootScope.noticeDisableOpen) {return;}
		//$rootScope.dialog = "fileOpen";
		if($rootScope.model.editor.selectedFile){
			$rootScope.dialog = "";
			$rootScope.model.editor.fileName=$rootScope.model.editor.selectedFile;
			$rootScope.model.editor.indexEdit = "editItems";
			$rootScope.model.editor.title = "";
			$rootScope.model.editor.title2 = "";
			$rootScope.model.editor.expire = "";

			res.ui.send({
				context : res.ui.context,
				event : "file.download",
				data : {
					"folder" : "notices",
					"file" : $rootScope.model.editor.selectedFile
							+ ".js"
				}
			});

			$rootScope.noticeDisableClose = false;
            $rootScope.noticeDisableOpen = true;
		}
	};

	$scope.remove = function() {
		$rootScope.model.editor.noticesResource = undefined;
		$rootScope.model.editor.noticesResource = $scope.getNoticesResource();
		if (!$rootScope.model.editor.noticesResource) {
			$rootScope.dialog = "fileRemove";
		} else {
			res.ui.send({
				context : res.ui.context,
				event : "deploy.notices.getNotices",
				data : {}
			});
		}
	};

	$scope.getNoticesResource = function() {
		if (!res.ui.root.model.schedule) return null;

		var dataDeploy = res.ui.root.model.schedule.deploy;
		if (!dataDeploy && dataDeploy.length == 0) return null;

		for (var i = 0; i < dataDeploy.length; i++) {
			if (!dataDeploy[i].company) continue;
			if (dataDeploy[i].company.id != window.parent.res.ui.root.model.companyId) continue;
			if (!dataDeploy[i].config && dataDeploy[i].config.length == 0) return null;

			for (var j = 0; j < dataDeploy[i].config.length; j++) {
				if (dataDeploy[i].config[j].resource == "notices") {
					return dataDeploy[i].config[j].task;
				}
			}
		}

		return null;
	};

//	$scope.remove = function(){
//        if($scope.checkScheduled()){
//        	$rootScope.model.failure.active = true;
//    		$rootScope.model.failure.service = "notices";
//    		$rootScope.model.failure.cause = "scheduled";
//        }else{
//    		$rootScope.dialog = "fileRemove";
//        }
//	};
//	$scope.checkScheduled = function(){
//		var removeFlag = false;
//		if (res.ui.root.model.schedule != undefined) {
//			var dataDeploy = res.ui.root.model.schedule.deploy;
//			if (dataDeploy != undefined && dataDeploy.length != 0) {
//				for (var i = 0; i < dataDeploy.length; i++) {
//					if (dataDeploy[i].config != undefined && dataDeploy[i].config.length > 0) {
//						for (var j = 0; j < dataDeploy[i].config.length; j++) {
//							if (dataDeploy[i].config[j].resource != undefined && dataDeploy[i].config[j].resource == "notices") {
//								if (dataDeploy[i].config[j].task != undefined) {
//									if (dataDeploy[i].config[j].task.length > 0) {
//										for (var k = 0; k < dataDeploy[i].config[j].task.length; k++) {
//											if ($rootScope.model.editor.selectedFile + ".js" == dataDeploy[i].config[j].task[k].filename) {
//												removeFlag = true;
//												return removeFlag;
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//	};
	$scope.save = function(){
		if ($rootScope.noticeDisableClose) {return;}
		if(!$scope.check()){return;}
		$rootScope.dialog = "fileSave";
	};
	$scope.close = function(){
		if ($rootScope.noticeDisableClose) {return;}
		$rootScope.dialog = "fileClose";
	};
	$scope.edit = function(section){
		$rootScope.indexEdit = section;
	};
	$scope.attach = function(){
		if ($rootScope.noticeDisableClose) {return;}
		if ($scope.pictureAdd) {
			res.ui.send({
				context : res.ui.context,
				event : "file.picture.list",
				data : {
					folder : "images/notices"
				}
			});

		} else {
			$rootScope.model.notice.selectedImage = "";
		}
	};
	$scope.check=function(){
		if(typeof ($rootScope.model.editor.title)=="undefined"||$rootScope.model.editor.title==""){
//			alert("タブ1行目を入力してください。");
			failure("missingTab");
			return false;
		}
		if(res.string.getLength($rootScope.model.editor.title)>20){
//			alert("タブ1行目は最大全角10文字以内に入力してください。");
			failure("tab1limit");
			return false;
		}
		if(res.string.getLength($rootScope.model.editor.title2)>20){
//			alert("タブ2行目は最大全角10文字以内に入力してください。");
			failure("tab2limit");
			return false;
		}
		if($rootScope.model.editor.expire&&$rootScope.model.editor.expire.length<8){
//			alert("無効な日付または時刻です！もう一度確認してください。");
			failure("invalidDate");
			return false;
		}

		if ($rootScope.model.editor.expire == "20000101") {
			$rootScope.model.editor.expire = $rootScope.model.editor.expireTemp;
			$rootScope.model.editor.expireTemp = "";
		}

		if($rootScope.model.editor.expire&&$rootScope.model.editor.expire.length==8){
			var year = parseInt($rootScope.model.editor.expire.slice(0, 4), 10);
	        var month = parseInt($rootScope.model.editor.expire.slice(4, 6), 10);
	        var day = parseInt($rootScope.model.editor.expire.slice(6, 8), 10);
	        var hour = 23, minute = 59;
	        var date = new Date(year, month - 1, day, hour, minute);
	        if (date === "Invalid Date" || (
	        		!$filter("resDateValidation")(month, "M") ||
	        		!$filter("resDateValidation")(day, "D") ||
	        		!$filter("resDateValidation")(hour, "H") ||
	        		!$filter("resDateValidation")(minute, "m")
	        	) || year < 2015) {
//	            alert("無効な日付または時刻です！もう一度確認してください。");
				failure("invalidDate");
	            return false;
	        }
//	        if (date <= new Date()) {
//	            alert("時刻は既に過ぎってる！もう一度確認してください。");
//	            return false;
//	        }
		}
        return true;
	};
//	$scope.stringWidth = function(str) {
//	    var r = 0;
//	    if(!str)return 0;
//	    for (var i = 0; i < str.length; i++) {
//	        var c = str.charCodeAt(i);
//	        // Shift_JIS: 0x0 ～ 0x80, 0xa0 , 0xa1 ～ 0xdf , 0xfd ～ 0xff
//	        // Unicode : 0x0 ～ 0x80, 0xf8f0, 0xff61 ～ 0xff9f, 0xf8f1 ～ 0xf8f3
//	        if ( (c >= 0x0 && c < 0x81) || (c == 0xf8f0) || (c >= 0xff61 && c < 0xffa0) || (c >= 0xf8f1 && c < 0xf8f4)) {
//	            r += 1;
//	        } else {
//	            r += 2;
//	        }
//	    }
//	    return r;
//	};

	function failure(cause){
		$rootScope.model.failure.active = true;
		$rootScope.model.failure.service = "menu";
		$rootScope.model.failure.cause = cause;
	}

	$scope.selectEntry = function () {

//		$("#editorExpiry").focus();

		if ($rootScope.model.editor.expire == "20000101") {
			$("#editorExpiryTemp").focus();
		} else {
			$("#editorExpiry").focus();
		}
    };

    $scope.dateClear = function() {
    	$rootScope.model.editor.expire = "";
    }

    //zhuxingnuo--begin
    $scope.filePrint = function(){
		if ($rootScope.noticeDisableClose) {return;}
		if(!$scope.check()){return;}
		var printWindow = window.open("", "printWindow", "innerHeight = 600,innerWidth = 800, scrollbars = yes");
		 printWindow.document.write("<!DOCTYPE html>");
                printWindow.document.write("<html><head>");
		printWindow.document.write("<link  href='modules/filePrint/filePrint.css' rel='stylesheet' type='text/css' />");
		printWindow.document.write("</head>");
		printWindow.document.write(document.getElementById("MenuFilePrint").innerHTML);
		printWindow.document.getElementById("MenuFilePrintFileName1").innerText = $rootScope.model.editor.title?$rootScope.model.editor.title:"";
		printWindow.document.getElementById("MenuFilePrintFileName2").innerText = $rootScope.model.editor.title2?$rootScope.model.editor.title2:"";
		printWindow.document.getElementById("MenuFilePrintExpiry").innerText = $rootScope.model.editor.expire?$rootScope.model.editor.expire:"";
		printWindow.document.getElementById("MenuFilePrintContent").innerText=document.getElementById("editNotice").value.replace(new RegExp("\n","gm"),"\r\n");
		printWindow.document.write("</html>");
		setTimeout(function(){
			printWindow.window.print();
			printWindow.window.close();
		},1000);

	};
    //zhuxingnuo--end

}]);

