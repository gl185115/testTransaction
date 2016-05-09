/*
 * Controller
 */
res.ui.controller("editLayout", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){
//	var numOfCategories = 9;
//	var numOfItems = 60;
//	var scrolls = [];
//	var scrollItems=undefined;
    $scope.scrollItems = new IScroll("#wrapperPickList", { // iScroll5
        scrollX: true,
        scrollY: true,
        scrollbars: true,
        interactiveScrollbars: true,
        mouseWheel: true,   // or "zoom"
    });
	$scope.folder = res.config.baseURL + "rest/uiconfig/custom/advertise/images/";
	$scope.position = { x: undefined, y: undefined, };
	
	$scope.$on("resIncludeLoaded", function() {
	    var fileInput = document.getElementById('fileInput');
	    fileInput.addEventListener('change', function(e) {
	        var file = fileInput.files[0];
	        var reader = new FileReader();
	        reader.filename = file.name;

	        reader.onload = function(e) {
	            var result = reader.result;
	            var dataFileName = reader.filename;
	            if ($rootScope.model.advertise.pictureChoice == 'imagesPart'){
	                $rootScope.model.advertise.sizeType = 1;
	            } else if ($rootScope.model.advertise.pictureChoice == 'imagesFull'){
	                $rootScope.model.advertise.sizeType = 2;
	            }
	            res.ui.send({
	                context : res.ui.root.context,
	                event : "file.picture.upload",
	                data : {
	                    filename : dataFileName,
	                    filecontent : file,
	                    folder : "advertise/images",
	                    sizeType : $rootScope.model.advertise.sizeType
	                }
	            });
	            $rootScope.model.advertise.sizeType = 0;
	        };
	        reader.readAsDataURL(file);
	    });
	});
	
	    $scope.$watch(
	        function() {
	            return $rootScope.language;
	        },
	        function(newValue, oldValue) {
	            if ($rootScope.model.editor.indexEdit != "editLayout") return;
	            $rootScope.model.advertise.locate($rootScope.language);
	        }
	    );

	    $scope.$watch(
	        function() {
	            return $rootScope.itemSelected;
	        },
	        function(newValue, oldValue) {
	            if (newValue) {
	                $scope.itemSelected.picturePart = newValue.picturePart;
	                $scope.itemSelected.pictureFull = newValue.pictureFull;
	                /*$scope.itemSelected.Part.fileName = newValue.Part.fileName;
	                $scope.itemSelected.Full.fileName = newValue.Full.fileName;*/
	                $scope.itemSelected.startOfDay = newValue.startOfDay;
	                $scope.itemSelected.endOfDay = newValue.endOfDay;
	                $scope.itemSelected.companyName = newValue.companyName;
	                $scope.itemSelected.adName = newValue.adName;
	                $scope.itemSelected.description = newValue.description;
	            }
	        }
	    );
	    
	$scope.selectPosition = function(x, y) {

		$rootScope.dialog = "mountItem";
		$scope.position = { x: x, y: y};
		$scope.selectedItem= $rootScope.model.advertise.layout[$scope.position.x][$scope.position.y];

		$scope.indexItem = 0;
		if ($rootScope.model.advertise.layout[$scope.position.x][$scope.position.y].isblank == false) {
		    $scope.itemSelected = angular.copy($rootScope.model.advertise.layout[$scope.position.x][$scope.position.y]);
		} else {
		    $scope.itemSelected = {};
		}
	};

	/*$scope.itemsShow= function(target){
		if(!scrollItems)return;
		var visible = {};
		visible.top = -scrollItems.y;
		visible.height = 390;
		visible.bottom = visible.top + visible.height;
		var element = document.querySelector(target);
		if (element.offsetTop < visible.top)
			scrollItems.scrollTo(0, -element.offsetTop, 200);
		else if ( (element.offsetTop+element.offsetHeight) > visible.bottom)
			scrollItems.scrollTo(0, visible.height - (element.offsetTop+element.offsetHeight), 200);
	};*/

	$scope.setImage = function(image) {
        //$scope.itemSelected.background = "image";
	    if ($rootScope.model.advertise.pictureChoice == "imagesPart"){
            $scope.itemSelected.picturePart = image;
        } else if ($rootScope.model.advertise.pictureChoice == "imagesFull") {
            $scope.itemSelected.pictureFull = image;
        }
        $rootScope.dialog = "mountItem";
    };
	
	$scope.selectItem = function(index){
		$scope.indexItem = index;
		$scope.selectedItem = $scope.items[index];
	};

	$scope.submit = function(){
		if ($scope.items[$scope.indexItem].itemId != "") {
		    $rootScope.model.advertise.layout[$scope.position.x][$scope.position.y] = $scope.items[$scope.indexItem];
		} else {
			$rootScope.model.advertise.layout[$scope.position.x][$scope.position.y] = new ItemBlank();

		}
		$rootScope.model.advertise.locate($rootScope.language);
		$rootScope.dialog = "";
	};

	/*$scope.delItem = function() {
		if ($scope.indexItem == 0) return;
		var itemId = $scope.items[$scope.indexItem].itemId;
		if (!itemId) return;

		$rootScope.model.pickList.removeFromLayout(itemId);
		$rootScope.model.pickList.initItemFromItems(itemId);
		$scope.items = [{ itemId: "", background:"image" }].concat($rootScope.model.pickList.items);

		if ($scope.doDelete) {
			$rootScope.model.pickList.removeFromItems(itemId);

			if ($scope.indexItem >= 1) {
				if ($scope.items.length > $scope.indexItem + 1) {
					$scope.selectItem($scope.indexItem + 1);
				} else if ($scope.items.length == $scope.indexItem + 1){
					$scope.selectItem($scope.indexItem - 1);
				}

			} else {
				$scope.selectItem(0);
			}

		} else {
			$scope.selectItem($scope.indexItem);
		}

		$rootScope.model.pickList.locate($rootScope.language);
		$rootScope.dialog = "mountItem";
		$timeout(function() { scrollItems.refresh(); }, 200);
	};*/

	$scope.back = function(){
		$rootScope.dialog = "mountItem";
	};

	$scope.clearAllItems = function() {
		$rootScope.dialog = "clearTabAllItems";
	};

	$scope.clearItems = function() {
	    $rootScope.model.advertise.layout = [];
		$rootScope.dialog = "";
	};
	
	$scope.popup = function(choice) {
	    if (choice == 'imagesPart'){
	        $rootScope.model.advertise.sizeType = 1;
	        $rootScope.model.advertise.pictureChoice = choice;
	        choice = 'images';
	    } else if (choice == 'imagesFull'){
	        $rootScope.model.advertise.sizeType = 2;
	        $rootScope.model.advertise.pictureChoice = choice;
	        choice = 'images';
	    }
        $rootScope.dialog = choice;
    };

    $scope.$watch(function() {
        return $rootScope.dialog;
    }, function(dialog, oldValue) {
        switch (dialog) {
        case "images":
            res.ui.send({
                context : res.ui.root.context,
                event : "file.picture.list",
                data : {
                    folder : "advertise/images",
                    sizeType : $rootScope.model.advertise.sizeType
                }
            });
            $rootScope.model.advertise.sizeType = 0;
            break;
        case "fileCreate":
            $scope.indexItem = undefined;
            $scope.itemSelected = undefined;
            break;
        case "fileSave":
            $scope.indexItem = 0;
            $scope.itemSelected = undefined;
            break;
        case "fileClose":
            break;
        default:
            break;

        }
    });

    $scope.apply = function() {
        //if (!$scope.doApplyCheck("all")) return;

        var items = new ItemBlank;
        $scope.itemSelected.isblank = false;
        items.isblank = false;
        items.picturePart = $scope.itemSelected.picturePart;
        items.pictureFull = $scope.itemSelected.pictureFull;
        /*var partArr = items.picturePart && items.picturePart.split(".");
        var partLeng = partArr[partArr.length - 1].length;
        items.Part.fileName = items.Part.picture ? items.Part.picture.substring(0, items.Part.picture.length - partLeng + 1) : "";
        var fullArr = items.Full.picture && items.Full.picture.split(".");
        var fullLeng = fullArr[fullArr.length-1].length;
        items.Full.fileName = items.Full.picture ? items.Full.picture.substring(0, items.Full.picture.length - fullLeng + 1) : "";*/
        items.startOfDay = $scope.itemSelected.startOfDay;
        items.endOfDay = $scope.itemSelected.endOfDay;
        items.companyName = $scope.itemSelected.companyName;
        items.adName = $scope.itemSelected.adName;
        items.description = $scope.itemSelected.description;
        
        $rootScope.model.advertise.layout[$scope.position.x][$scope.position.y] = angular.copy(items);
        $rootScope.dialog = "";
    };
      
    $scope.clear = function() {
        $scope.itemSelected = angular.copy($rootScope.model.advertise.layout[$scope.position.x][$scope.position.y]);
    };
    
    $scope.cancel = function() {
        $rootScope.dialog = "";
    };
    
    $scope.addItems = function() {
        var layout = $rootScope.model.advertise.layout;
        var scroll = $scope.scrollItems;
        if(layout.length > 0) {
            var i = layout.length -1;
            var k = layout[i].length ? layout[i].length - 1 : 0;
            if(layout[i].length > 0 && layout[i].length > 4) {
                i++;
                layout.push([]);
                layout[i].push([]);
                layout[i][0] = new ItemBlank();
            } else {
                layout[i][k+1] = new ItemBlank();
            }
        } else {
            layout.push([]);
            layout[0].push([]);
            layout[0][0] = new ItemBlank();
        }
        setTimeout(function(){
            scroll.refresh();
        },200);
    };
    
    $scope.remove = function() {
        $rootScope.model.advertise.layout[$scope.position.x][$scope.position.y] = {};
        $rootScope.dialog = "";
    };
    
    $scope.selectEditInput = function(index) {
        $rootScope.model.indexEntry = index;
        // $scope.indexStore = -1;
        if (index == 0) {
            $("#deployStoreValue").focus();
        } else if (index == 1) {
            $("#DeployDateValue").focus();
        } else if (index == 2) {
            $scope.selectDeployTime(1);
        } else if (index == 3) {
            $scope.selectDeployTime(2);
        }
    };
    
    $scope.doApplyCheck = function(option) {
        if (!$scope.itemSelected) {
            $rootScope.dialog = "";
            return false;
        }
        var startOfDayLength = res.string.getLength($scope.itemSelected.startOfDay);
        var endOfDayLength = res.string.getLength($scope.itemSelected.endOfDay);

        if ((option=="all" || option=="itemId") && (startOfDayLength > 8 || endOfDayLength > 8)) {
            $scope.itemSelected.startOfDayLength = res.string.truncate($scope.itemSelected.startOfDayLength, 8);
            $scope.itemSelected.startOfDayLength = res.string.truncate($scope.itemSelected.startOfDayLength, 8);
            $rootScope.model.failure.active = true;
            $rootScope.model.failure.service = "advertise";
            $rootScope.model.failure.cause = "maxDayNumbers8";
            return false;
        }

        /*if ((option=="all" || option=="line1")&&(filePartNameLength > 16 || fileFullNameLength >16 )) {
            if ((option=="all" || option=="line1")&&(filePartNameLength > 16)) {
                $scope.itemSelected.Part.fileName = res.string.truncate($scope.itemSelected.Part.fileName, 16);
            }
            if ((option=="all" || option=="line2")&&(fileFullNameLength > 16)) {
                $scope.itemSelected.Full.fileName = res.string.truncate($scope.itemSelected.Full.fileName, 16);
            }
            $rootScope.model.failure.active = true;
            $rootScope.model.failure.service = "advertise";
            $rootScope.model.failure.cause = "maxCharacters8";
            return false;
        }*/

        return true;
    };
    
}]);

