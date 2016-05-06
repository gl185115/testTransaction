/*
 * Controller
 */
res.ui.controller("editLayout", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){
//	var numOfCategories = 9;
//	var numOfItems = 60;
	var scrolls = [];
	var scrollItems=undefined;
	$scope.folder = res.config.baseURL + "rest/uiconfig/custom/pickList/images/";
	$scope.position = { category: undefined, x: undefined, y: undefined, };

	$scope.$watch(
		function(){ return $rootScope.model.editor.indexEdit; },
		function(indexEdit, oldValue){
			if (indexEdit != "editLayout") return;

			for (var i = 0; i < $rootScope.model.pickList.categories.length; i++){
				scrolls[i] = new IScroll("#wrapperPickList" + i, {	// iScroll5
					scrollX: true,
					scrollY: false,
					scrollbars: true,
					interactiveScrollbars: true,
					mouseWheel: true,	// or "zoom"
				});
			}

			$scope.items = $rootScope.model.pickList.items;
//			$scope.isShowAllClearBtn = false;
			$scope.selectCategory(0);
//			$scope.indexItem = 0;
		}
	);

	$scope.$watch(function() {
        return $rootScope.model.editor.pictures;
    }, function(pictures, oldValue) {
        $timeout(function() {
            scroll.images.refresh();
        }, 200);
    });

	$scope.$watch(
	        function() {
	            return $rootScope.selectedItem;
	        },
	        function(newValue, oldValue) {
	            if (newValue) {
	                $scope.selectedItem.picture = newValue.picture;
	                $scope.selectedItem.background = newValue.background;
	            }
	        }
	    );

	$scope.$watch(function() {
        return $rootScope.dialog;
    }, function(dialog, oldValue) {
        switch (dialog) {
        case "images":
            res.ui.send({
                context : res.ui.root.context,
                event : "file.picture.list",
                data : {
//                    folder : "images/pickList"
                    folder : "pickList/images",
                    sizeType : 0,
                }
            });
            break;
        case "fileCreate":
            $scope.indexItem = undefined;
            $scope.selectedItem = undefined;
            break;
        case "fileSave":
            $scope.indexItem = 0;
            $scope.selectedItem = undefined;
            break;
        case "fileClose":
            break;
        default:
            break;

        }
    });

	$scope.selectCategory = function(index){

		$rootScope.model.pickList.selectedCategory = index;
		$scope.indexCategory = index;
		$timeout(function(){
			scrolls[$scope.indexCategory].refresh();
		}, 500);

//		$scope.showAllClearBtn(index);
	};

	$scope.selectPosition = function(category, x, y) {

	    $rootScope.dialog = "details";
		$scope.position = { category: category, x: x, y: y};
		$scope.selectedItem= $rootScope.model.pickList.layout[$scope.position.category][$scope.position.x][$scope.position.y];
		$scope.create();
	};

	$scope.create = function() {
	    if ($scope.selectedItem.itemId){
	      //商品編集
	        for (var i = 0; i < $scope.items.length; i++) {
              if ($scope.items[i].itemId == $scope.selectedItem.itemId) {
                  $scope.indexItem = i;
                  break;
              }
          }
	      $scope.doselect($scope.indexItem);
//	      $scope.select($scope.indexItem);
	    }
	    else {
	      //未使用商品時、新規商品追加
	        var blank = new ItemBlank();
	        if ($rootScope.model.pickList.items.length == 0) {
	            $rootScope.model.pickList.items[0] = blank;
	            $scope.doselect(0);
	        } else {
	            $scope.indexItem = $rootScope.model.pickList.items.length;
	            $rootScope.model.pickList.items.splice($scope.indexItem, 0, blank);
	            $scope.doselect($scope.indexItem);
	        }
	    }
    };

    $scope.doselect = function(index) {
        $scope.selectedItem = angular.copy($rootScope.model.pickList.items[index]);
        var description = $scope.selectedItem.description[$rootScope.language];
        if(typeof(description) == "undefined"){
            description = $scope.selectedItem.description["jp"];
        }
        $scope.selectedItem.label = {
            line1: (description.indexOf("<br>") != -1)? description.slice(0, description.indexOf("<br>")) : description,
            line2: (description.indexOf("<br>") != -1)? description.slice(description.indexOf("<br>") + "<br>".length) : "",
        };
        var line1 = angular.copy($scope.selectedItem.label.line1);
        var line2 = angular.copy($scope.selectedItem.label.line2);
        $scope.selectedItem.inputLabel = {};
        $scope.selectedItem.inputLabel.line1 = line1.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");
        $scope.selectedItem.inputLabel.line2 = line2.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");

        $scope.indexItem = index;
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

/*	$scope.selectItem = function(index){
		$scope.indexItem = index;
		$scope.selectedItem = $scope.items[index];
	};
*/
	$scope.submit = function(){
        if ($scope.selectedItem.itemId == "" || $scope.selectedItem.inputLabel.line1 == ""){
            $rootScope.model.failure.active = true;
            $rootScope.model.failure.service = "picklist";
            $rootScope.model.failure.cause = "empty";
            return;
        }

	    if($scope.doApplyCheck("all")){
	        $scope.apply();
	    }else{
	        return;
	    }

		if ($scope.items[$scope.indexItem].itemId != "") {
		    $rootScope.model.pickList.layout[$scope.position.category][$scope.position.x][$scope.position.y] = $scope.items[$scope.indexItem];
		} else {
			$rootScope.model.pickList.layout[$scope.position.category][$scope.position.x][$scope.position.y] = new ItemBlank();

		}
		$rootScope.model.pickList.locate($rootScope.language);
		$rootScope.dialog = "";
	};

//	$scope.confirmRemove = function(){
//		$rootScope.dialog = "confirmRemove";
//	};

	$scope.apply = function() {
        $scope.selectedItem.isblank = false;
        $rootScope.model.pickList.items[$scope.indexItem].isblank = false;
        $rootScope.model.pickList.items[$scope.indexItem].itemId = $scope.selectedItem.itemId;
        $rootScope.model.pickList.items[$scope.indexItem].background = $scope.selectedItem.background;
        $rootScope.model.pickList.items[$scope.indexItem].picture = $scope.selectedItem.picture;

        // Convert <br> for normal string.
        var line1 = $scope.selectedItem.label.line1;
        line1 = line1.replace(/\&/g,"&amp;").replace(/\</g,"&lt;").replace(/\>/g,"&gt;");

        if ($scope.selectedItem.label.line2) {
            var line2 = $scope.selectedItem.label.line2;
            line2 = line2.replace(/\&/g,"&amp;").replace(/\</g,"&lt;").replace(/\>/g,"&gt;");
            line1 += "<br>" + line2;
        }

        $rootScope.model.pickList.items[$scope.indexItem].description[$rootScope.language] = line1;

        /*if ($scope.indexItem <= $rootScope.model.pickList.items.length - 1) {
            if ($scope.selectedItem.isblank || $scope.indexItem == ($rootScope.model.pickList.items.length - 1)) {
                $scope.select($scope.indexItem);
            } else {
                $scope.select($scope.indexItem + 1);
            }
        }*/

        $rootScope.model.printList= angular.copy($rootScope.model.pickList.items);
        for(var i = 0;i< $rootScope.model.printList.length;i++){
            var printItem = $rootScope.model.printList[i];
            var description = printItem.description[res.ui.root.language];
            if(typeof(description) == "undefined"){
                description = printItem.description["jp"];
            }
            $rootScope.model.printList[i].label = {
                line1: (description.indexOf("<br>") != -1)? description.slice(0, description.indexOf("<br>")) : description,
                line2: (description.indexOf("<br>") != -1)? description.slice(description.indexOf("<br>") + "<br>".length) : "",
            };
            if(printItem.background=='image' && printItem.picture){
                $rootScope.model.printList[i].isImage = "あり";
                $rootScope.model.printList[i].imageName = printItem.picture;
                $rootScope.model.printList[i].bgColor = "";
            }else if(printItem.background=='image' && !printItem.picture){
                $rootScope.model.printList[i].isImage = "あり";
                $rootScope.model.printList[i].imageName = "準備中";
                $rootScope.model.printList[i].bgColor = "";
            }else if(printItem.background!='image'){
                $rootScope.model.printList[i].isImage = "なし";
                $rootScope.model.printList[i].imageName = "";
                $rootScope.model.printList[i].bgColor = printItem.background;
            }
        }
    };

    $scope.doApplyCheck = function(option) {
        var itemIdLength = res.string.getLength($scope.selectedItem.itemId);

        $scope.selectedItem.label.line1 = $scope.selectedItem.inputLabel.line1;
        $scope.selectedItem.label.line2 = $scope.selectedItem.inputLabel.line2;
        var line1Length = res.string.getLength($scope.selectedItem.label.line1);
        var line2Length = res.string.getLength($scope.selectedItem.label.line2);

        if ((option=="all" || option=="itemId")&&(!res.model.isNumber($scope.selectedItem.itemId) || (itemIdLength > 13))) {
            $scope.selectedItem.itemId = res.string.truncate($scope.selectedItem.itemId, 13);
            $rootScope.model.failure.active = true;
            $rootScope.model.failure.service = "picklist";
            $rootScope.model.failure.cause = "maxNumbers13";
            return false;
        }

        if ((option=="all" || option=="line1")&&(line1Length > 16) || (option=="all" || option=="line2")&&(line2Length != 0 && line2Length > 16)) {
            if ((option=="all" || option=="line1")&&(line1Length > 16)) {
                $scope.selectedItem.label.line1 = res.string.truncate($scope.selectedItem.label.line1, 16);
            }
            if ((option=="all" || option=="line2")&&(line2Length != 0 && line2Length > 16)) {
                $scope.selectedItem.label.line2 = res.string.truncate($scope.selectedItem.label.line2, 16);
            }
            $rootScope.model.failure.active = true;
            $rootScope.model.failure.service = "picklist";
            $rootScope.model.failure.cause = "maxCharacters8";
            return false;
        }

        return true;
    };

    $scope.clear = function() {
        $scope.doselect($scope.indexItem);
    };

    /*
     * Popups
     */
    $scope.popup = function(choice) {
        $rootScope.dialog = choice;
    };

    $scope.cancel = function() {
//        $rootScope.dialog = "";
        $rootScope.dialog = "details";
    };

    $scope.setImage = function(image) {
        $scope.selectedItem.background = "image";
        $scope.selectedItem.picture = image;
//        $rootScope.dialog = "";
        $rootScope.dialog = "details";
    };

    $scope.setBackground = function(color) {
        $scope.selectedItem.background = color;
//        $rootScope.dialog = "";
        $rootScope.dialog = "details";
    };

	/*$scope.remove = function(){
		if ($scope.indexItem == 0) return;
//		var itemId = $scope.items[$scope.indexItem].itemId;
//		//$scope.items.splice($scope.indexItem, 1);
//		if (!itemId) return;
		$rootScope.dialog = "confirmRemove";

//		$rootScope.model.pickList.removeFromLayout(itemId);
//		//$rootScope.model.pickList.removeFromItems(itemId);
//		$scope.selectItem(0);
//		$rootScope.model.pickList.locate($rootScope.language);
////		$rootScope.dialog = "";
//
//		$rootScope.dialog = "confirmRemove";
	};*/


	$scope.delItem = function() {
		var itemId = $scope.items[$scope.indexItem].itemId;
		if (!itemId) return;

		$rootScope.model.pickList.removeFromLayout(itemId);
		$rootScope.model.pickList.initItemFromItems(itemId);
		$scope.items = $rootScope.model.pickList.items;

		$rootScope.model.pickList.removeFromItems(itemId);

		$rootScope.model.pickList.locate($rootScope.language);
		$rootScope.dialog = "";
	};

	/*$scope.back = function(){
		$rootScope.dialog = "mountItem";
	};*/

	/*$scope.clearTabAllItems = function() {
		$rootScope.dialog = "clearTabAllItems";
	};*/

	/*$scope.clearItems = function(lTabindex) {

		if (lTabindex != undefined) {
			var clearItems = $rootScope.model.pickList.layout[lTabindex];
			$rootScope.model.pickList.deletedTabItems = [];
			$rootScope.model.pickList.deletedTabItems = clearItems;
			for (var i = 0; i < clearItems.length; i++) {
				var itemCol = clearItems[i];
				for (var j = 0; j < itemCol.length; j++) {
					if (itemCol[j].itemId) {
						itemCol[j] = new ItemBlank();
					}
				}
			}
		}

		$rootScope.model.pickList.locate($rootScope.language);
//		$scope.isShowAllClearBtn = false;
		$rootScope.dialog = "";
	};*/

//	$scope.showAllClearBtn = function(index) {
//
////		$scope.isShowAllClearBtn = false;
//        var tabItems = $rootScope.model.pickList.layout[index];
//        for (var i = 0; i < tabItems.length; i++) {
//        	for (var j = 0; j < tabItems[i].length; j++) {
//        		var item = tabItems[i][j];
//        		if (item.itemId && item.description.ja) {
////        			$scope.isShowAllClearBtn = true;
//        			return;
//        		}
//        	}
//        }
//	};

	$scope.$on("resIncludeLoaded", function() {    // wait for res-include complete
        scroll.images = new IScroll("#wrapperImages", { // iScroll5
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true,   // or "zoom"
        });

        $scope.buttonColors = [ "silver", "red", "orange", "beige", "yellow", "green", "blue", "purple" ];

        var fileInput = document.getElementById('fileInput');
        fileInput.addEventListener('change', function(e) {
            var file = fileInput.files[0];
            var reader = new FileReader();
            reader.filename = file.name;

            reader.onload = function(e) {
                var result = reader.result;
                var dataFileName = reader.filename;
                res.ui.send({
                    context : res.ui.root.context,
                    event : "file.picture.upload",
                    data : {
                        filename : dataFileName,
                        filecontent : file,
                        folder : "pickList"
                    }
                });
            };
            reader.readAsDataURL(file);
        });
    });

}]);

