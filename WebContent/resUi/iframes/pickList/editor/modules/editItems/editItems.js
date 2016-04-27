/*
 * Controller editItems
 */
res.ui.controller("editItems", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout) {

	var scroll = {
		items: undefined,
		images: undefined,
	};

	$rootScope.itemIdUp = true;
	$rootScope.itemNameUp = true;
	$rootScope.itemLocationUp = true;

	$scope.$on("resIncludeLoaded", function() {	// wait for res-include complete
		scroll.items = new IScroll("#wrapperItems", {	// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true,	// or "zoom"
		});
		scroll.images = new IScroll("#wrapperImages", {	// iScroll5
			scrollX: false,
			scrollY: true,
			scrollbars: true,
			interactiveScrollbars: true,
			mouseWheel: true,	// or "zoom"
		});

		$scope.buttonColors = [ "silver", "red", "orange", "beige", "yellow", "green", "blue", "purple" ];
		$scope.insetCanvas();

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

	$scope.$watch(
		function() {
			return $rootScope.model.editor.indexEdit;
		},
		function(indexEdit, oldValue) {
			if (indexEdit != "editItems") return;

			$scope.folder = $rootScope.model.pickList.imageURL;
			$scope.folder = $scope.folder.lastIndexOf("/") ? $scope.folder += "/" : $scope.folder;
			$scope.indexSelected = undefined;
			$scope.itemSelected = undefined;
//			if ($rootScope.model.pickList.items.length > 0) {
//				$scope.select(0);
//			}
			$rootScope.model.pickList.items.sort($scope.getSort(true, "itemId"));
			$rootScope.model.pickList.OriginalItems = angular.copy($rootScope.model.pickList.items);
			res.ui.root.itemIdUp = false;
			res.ui.root.itemNameUp = true;
			res.ui.root.itemLocationUp = true;

			if ($rootScope.model.pickList.items.length > 0) {
				$scope.select(0);
			}

			$timeout(function() {
				scroll.items.refresh();
			}, 200);
		}
	);

	$scope.$watch(
		function() {
			return $rootScope.language;
		},
		function(newValue, oldValue) {
			if ($rootScope.model.editor.indexEdit != "editItems") return;
			$rootScope.model.pickList.locate($rootScope.language);
		}
	);

	$scope.$watch(
		function() {
			return $rootScope.itemSelected;
		},
		function(newValue, oldValue) {
			if (newValue) {
				$scope.itemSelected.picture = newValue.picture;
				$scope.itemSelected.background = newValue.background;
			}
		}
	);

	$scope.select = function(index) {
		if ($scope.indexSelected != index && $scope.itemSelected) { // leaving from the current selection?
			if (!$rootScope.model.pickList.items[index].isblank) {
				if ($scope.itemSelected.itemId == "" || $scope.itemSelected.label.line1 == ""){
					$rootScope.model.failure.active = true;
					$rootScope.model.failure.service = "picklist";
					$rootScope.model.failure.cause = "empty";
					return;
				}
				if ($scope.itemSelected.isblank) {
					$rootScope.model.failure.active = true;
					$rootScope.model.failure.service = "picklist";
					$rootScope.model.failure.cause = "blank";
					return;
				}
			}
		}
		$scope.doselect(index);
	};

	$scope.doselect = function(index) {
		$scope.itemSelected = angular.copy($rootScope.model.pickList.items[index]);
		var description = $scope.itemSelected.description[$rootScope.language];
		if(typeof(description) == "undefined"){
			description = $scope.itemSelected.description["jp"];
		}
		$scope.itemSelected.label = {
			line1: (description.indexOf("<br>") != -1)? description.slice(0, description.indexOf("<br>")) : description,
			line2: (description.indexOf("<br>") != -1)? description.slice(description.indexOf("<br>") + "<br>".length) : "",
		};
		var line1 = angular.copy($scope.itemSelected.label.line1);
		var line2 = angular.copy($scope.itemSelected.label.line2);
		$scope.itemSelected.inputLabel = {};
		$scope.itemSelected.inputLabel.line1 = line1.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");
		$scope.itemSelected.inputLabel.line2 = line2.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");

		$scope.indexItem = index;
	};

	$scope.doApplyCheck = function(option) {
		var itemIdLength = res.string.getLength($scope.itemSelected.itemId);

		$scope.itemSelected.label.line1 = $scope.itemSelected.inputLabel.line1;
		$scope.itemSelected.label.line2 = $scope.itemSelected.inputLabel.line2;
		var line1Length = res.string.getLength($scope.itemSelected.label.line1);
		var line2Length = res.string.getLength($scope.itemSelected.label.line2);

		if ((option=="all" || option=="itemId")&&(!res.model.isNumber($scope.itemSelected.itemId) || (itemIdLength > 13))) {
			$scope.itemSelected.itemId = res.string.truncate($scope.itemSelected.itemId, 13);
			$rootScope.model.failure.active = true;
			$rootScope.model.failure.service = "picklist";
			$rootScope.model.failure.cause = "maxNumbers13";
			return false;
		}

		if ((option=="all" || option=="line1")&&(line1Length > 16) || (option=="all" || option=="line2")&&(line2Length != 0 && line2Length > 16)) {
			if ((option=="all" || option=="line1")&&(line1Length > 16)) {
				$scope.itemSelected.label.line1 = res.string.truncate($scope.itemSelected.label.line1, 16);
			}
			if ((option=="all" || option=="line2")&&(line2Length != 0 && line2Length > 16)) {
				$scope.itemSelected.label.line2 = res.string.truncate($scope.itemSelected.label.line2, 16);
			}
			$rootScope.model.failure.active = true;
			$rootScope.model.failure.service = "picklist";
			$rootScope.model.failure.cause = "maxCharacters8";
			return false;
		}

		return true;
	};

	$scope.apply = function() {
		if (!$scope.doApplyCheck("all")) return;

		$scope.itemSelected.isblank = false;
		$rootScope.model.pickList.items[$scope.indexItem].isblank = false;
		$rootScope.model.pickList.items[$scope.indexItem].itemId = $scope.itemSelected.itemId;
		$rootScope.model.pickList.items[$scope.indexItem].background = $scope.itemSelected.background;
		$rootScope.model.pickList.items[$scope.indexItem].picture = $scope.itemSelected.picture;

		// Convert <br> for normal string.
		var line1 = $scope.itemSelected.label.line1;
		line1 = line1.replace(/\&/g,"&amp;").replace(/\</g,"&lt;").replace(/\>/g,"&gt;");

		if ($scope.itemSelected.label.line2) {
			var line2 = $scope.itemSelected.label.line2;
			line2 = line2.replace(/\&/g,"&amp;").replace(/\</g,"&lt;").replace(/\>/g,"&gt;");
			line1 += "<br>" + line2;
		}

		$rootScope.model.pickList.items[$scope.indexItem].description[$rootScope.language] = line1;

		if ($scope.indexItem <= $rootScope.model.pickList.items.length - 1) {
			if ($scope.itemSelected.isblank || $scope.indexItem == ($rootScope.model.pickList.items.length - 1)) {
				$scope.select($scope.indexItem);
			} else {
				$scope.select($scope.indexItem + 1);
			}
		}

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

	$scope.preapply=function(option) {
		if (!$scope.doApplyCheck(option)) return;
	};

	$scope.create = function() {
		var blank = new ItemBlank();
		if ($rootScope.model.pickList.items.length == 0) {
			$rootScope.model.pickList.items[0] = blank;
			$scope.select(0);
		} else {
			if ($scope.itemSelected) {
				if ($scope.itemSelected.itemId == "" || $scope.itemSelected.label.line1 == "") {
					$rootScope.model.failure.active = true;
					$rootScope.model.failure.service = "picklist";
					$rootScope.model.failure.cause = "empty";
					return;
				}
				if ($scope.itemSelected.isblank) {
					$rootScope.model.failure.active = true;
					$rootScope.model.failure.service = "picklist";
					$rootScope.model.failure.cause = "blank";
					return;
				}
			}

			$rootScope.model.pickList.items.splice($scope.indexItem + 1, 0, blank);
			$scope.select($scope.indexItem + 1);
		}
		$timeout(function() {
			scroll.items.refresh();
			if ($scope.indexItem == $rootScope.model.pickList.items.length - 1 ){
				scroll.items.scrollTo(0, scroll.items.maxScrollY - 10, 200);
			}
		}, 200);
	};

	$scope.sortItem = function(item) {
		switch (item) {
		case "itemId":
			$rootScope.model.pickList.items.sort($scope.getSort($rootScope.itemIdUp,item));
				$rootScope.model.pickList.OriginalItems.sort($scope.getSort($rootScope.itemIdUp,item));
				$rootScope.model.printList.sort($scope.getSort($rootScope.itemIdUp,item));
				
			$rootScope.itemIdUp=!$rootScope.itemIdUp;
			$rootScope.itemNameUp = true;
			$rootScope.itemLocationUp = true;
			break;
		case "description":
			$rootScope.model.pickList.items.sort($scope.getSortString($rootScope.itemNameUp,item));
				$rootScope.model.pickList.OriginalItems.sort($scope.getSortString($rootScope.itemNameUp,item));
				$rootScope.model.printList.sort($scope.getSortString($rootScope.itemNameUp,item));
				
			$rootScope.itemNameUp=!$rootScope.itemNameUp;
			$rootScope.itemIdUp=true;
			$rootScope.itemLocationUp = true;
			break;
		case "locations":
			$rootScope.model.pickList.items.sort($scope.getSortString($rootScope.itemLocationUp,item));
				$rootScope.model.pickList.OriginalItems.sort($scope.getSortString($rootScope.itemLocationUp,item));
				$rootScope.model.printList.sort($scope.getSortString($rootScope.itemLocationUp,item));
				
			$rootScope.itemLocationUp=!$rootScope.itemLocationUp;
			$rootScope.itemIdUp=true;
			$rootScope.itemNameUp = true;
			break;
		default:
			return;
		}

		$scope.doselect($scope.indexItem);
	};

	$scope.getSort = function (orderBy, item) {
		if (orderBy) {
			return function(set1, set2) {
				if (set1[item] < set2[item]) {
					return -1;
				} else if (set2[item] < set1[item]) {
					return 1;
				} else {
					return 0;
				}
			};
		} else {
			return function(set1, set2) {
				if (set1[item] < set2[item]) {
					return 1;
				} else if (set2[item] < set1[item]) {
					return -1;
				} else {
					return 0;
				}
			};
		}
	};

	$scope.getSortString = function (orderBy, item) {
		if (orderBy) {
			return function(set1, set2) {
				if (item == "description") {
					return set1[item][$rootScope.language].localeCompare(set2[item][$rootScope.language]);
				} else {
					if (typeof(set1[item]) == "undefined") {
						set1[item] = "";
					}
					if (typeof(set2[item]) == "undefined") {
						set2[item] = "";
					}
					return set1[item].localeCompare(set2[item]);
				}
			};
		} else {
			return function(set1, set2) {
				if (item == "description") {
					return set2[item][$rootScope.language].localeCompare(set1[item][$rootScope.language]);
				} else {
					if (typeof(set1[item]) == "undefined") {
						set1[item] = "";
					}
					if (typeof(set2[item]) == "undefined") {
						set2[item] = "";
					}
					return set2[item].localeCompare(set1[item]);
				}
			};
		}
	};

	$scope.insetCanvas = function() {
		$(".SortItemIdUp").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 30;
			canvas.height = 16;
			res.canvas.polygon(canvas, [[3,6],[21,6],[12,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
			$(this).append(canvas);
		});
		$(".SortItemIdDown").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 30;
			canvas.height = 16;
			res.canvas.polygon(canvas, [[12,6],[3,16],[21,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
			$(this).append(canvas);
		});
		$(".SortItemNameUp").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 30;
			canvas.height = 16;
			res.canvas.polygon(canvas, [[3,6],[21,6],[12,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
			$(this).append(canvas);
		});
		$(".SortItemNameDown").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 30;
			canvas.height = 16;
			res.canvas.polygon(canvas, [[12,6],[3,16],[21,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
			$(this).append(canvas);
		});
		$(".SortItemLocationUp").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 30;
			canvas.height = 16;
			res.canvas.polygon(canvas, [[3,6],[21,6],[12,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
			$(this).append(canvas);
		});
		$(".SortItemLocationDown").each(function(){
			var canvas = document.createElement('canvas');
			canvas.width = 30;
			canvas.height = 16;
			res.canvas.polygon(canvas, [[12,6],[3,16],[21,16]], [30,0,"hsl(0,0%,100%)"], [0,0,"hsl(0,0%,90%)"]);
			$(this).append(canvas);
		});
	};

	/*
	 * Popups
	 */
	$scope.popup = function(choice) {
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
					folder : "images/pickList"
				}
			});
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

	$scope.$watch(function() {
		return $rootScope.model.editor.pictures;
	}, function(pictures, oldValue) {
		$timeout(function() {
			scroll.images.refresh();
		}, 200);
	});

	$scope.cancel = function() {
		$rootScope.dialog = "";
	};

	$scope.setImage = function(image) {
		$scope.itemSelected.background = "image";
		$scope.itemSelected.picture = image;
		$rootScope.dialog = "";
	};

	$scope.setBackground = function(color) {
		$scope.itemSelected.background = color;
		$rootScope.dialog = "";
	};

	$scope.remove = function() {
		if ($rootScope.model.pickList.items.length == 0) {
			$scope.itemSelected = undefined;
			$rootScope.dialog = "";
			return;
		}
		var itemId = $rootScope.model.pickList.items[$scope.indexItem].itemId;
		if (itemId){
			$rootScope.model.pickList.removeFromLayout(itemId);
		}
		$rootScope.model.pickList.items.splice($scope.indexItem, 1);

		$scope.itemSelected = undefined;

		$scope.indexItem = $scope.indexItem == 0 ? 0 : $scope.indexItem - 1;
		if ($rootScope.model.pickList.items.length > 0) {
			if ($scope.indexItem <= $rootScope.model.pickList.items.length - 1) {
				$scope.select($scope.indexItem);
			}
		}
		$rootScope.model.printList = angular.copy($rootScope.model.pickList.items);
		$rootScope.dialog = "";
		$timeout(function(){ scroll.items.refresh(); }, 200);
	};

	$scope.clear = function() {
		$scope.doselect($scope.indexItem);
	};

}]);

