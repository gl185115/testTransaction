/*
 * Controller
 */
res.ui.controller("editLayout", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){
//	var numOfCategories = 9;
//	var numOfItems = 60;
	var scrolls = [];
	var scrollItems=undefined;
	$scope.folder = res.config.baseURL + "custom/images/pickList/";
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

			$scope.items = [{ itemId: "", background:"image" }].concat($rootScope.model.pickList.items);
//			$scope.isShowAllClearBtn = false;
			$scope.selectCategory(0);
			$scope.indexItem = 0;
		}
	);

	$scope.selectCategory = function(index){

		$rootScope.model.pickList.selectedCategory = index;
		$scope.indexCategory = index;
		$timeout(function(){
			scrolls[$scope.indexCategory].refresh();
		}, 500);

//		$scope.showAllClearBtn(index);
	};

	$scope.selectPosition = function(category, x, y) {

		$rootScope.dialog = "mountItem";
		$scope.position = { category: category, x: x, y: y};
		$scope.selectedItem= $rootScope.model.pickList.layout[$scope.position.category][$scope.position.x][$scope.position.y];

		$scope.indexItem = 0;
//		if ($scope.selectedItem.itemId) {
//			for (var i = 0; i < $scope.items.length; i++) {
//				if ($scope.items[i].itemId == $scope.selectedItem.itemId) {
//					$scope.indexItem = i;
//					break;
//				}
//			}
//		}
	};

	$scope.$watch(
		function(){ return $rootScope.dialog; },
		function(dialog, oldValue){
			if (dialog != "mountItem") return;
			$scope.doDelete = true;
			$scope.items = [{ itemId: "", background:"image" }].concat($rootScope.model.pickList.items);

			if ($scope.indexItem == undefined) {
				$scope.indexItem = 0;
			}
//			$scope.indexItem = 0;
//			for(var i=0;i<$scope.items.length;i++){
//				if($scope.selectedItem && ($scope.selectedItem.itemId==$scope.items[i].itemId)){
//					$scope.indexItem=i;
//				}
//			}
			if(!scrollItems){
				scrollItems = new IScroll("#wrapperMount", {	// iScroll5
					scrollX: false,
					scrollY: true,
					scrollbars: true,
					interactiveScrollbars: true,
					mouseWheel: true,	// or "zoom"
				});
			}
			$timeout(function(){
				$scope.itemsShow("#wrapperMount .Line:nth-child(" + ($scope.indexItem+1) + ")");
				scrollItems.refresh();
			}, 200);
		}
	);
	$scope.itemsShow= function(target){
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
	};

	$scope.selectItem = function(index){
		$scope.indexItem = index;
		$scope.selectedItem = $scope.items[index];
	};

	$scope.submit = function(){
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

	$scope.remove = function(){
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
	};

	$scope.cancel = function(){
		$rootScope.dialog = "";
	};

	$scope.delItem = function() {
		if ($scope.indexItem == 0) return;
		var itemId = $scope.items[$scope.indexItem].itemId;
		if (!itemId) return;

		$rootScope.model.pickList.removeFromLayout(itemId);
//		$rootScope.model.pickList.initItemFromItems(itemId);
//		$scope.items = [{ itemId: "", background:"image" }].concat($rootScope.model.pickList.items);

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
	};

	$scope.back = function(){
		$rootScope.dialog = "mountItem";
	};

	$scope.clearTabAllItems = function() {
		$rootScope.dialog = "clearTabAllItems";
	};

	$scope.clearItems = function(lTabindex) {

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
	};

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

}]);

