/*
 * Controller editCategories
 */
res.ui.controller("editCategories", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout) {

	$scope.$watch(
		function() {
			return $rootScope.model.editor.indexEdit;
		},
		function(indexEdit, oldValue) {
			if (indexEdit != "editCategories")
				return;
			if (typeof ($rootScope.indexCategory) != "undefined") {
				$scope.selectList($rootScope.indexCategory);
			}
		}
	);

	$scope.$watch(
		function() {
			return $rootScope.language;
		},
		function(newValue, oldValue) {
			if ($rootScope.model.editor.indexEdit != "editCategories"
					|| typeof ($rootScope.indexCategory) == "undefined") {
				return;
			}
			$scope.selectList($rootScope.indexCategory);
		}
	);

	$scope.selectList = function(index) {
		$rootScope.indexCategory = index;
		$scope.categoryName = $rootScope.model.pickList.categories[$rootScope.indexCategory];
		var description = $scope.categoryName[$rootScope.language];
		if (typeof (description) === "undefined" && $rootScope.language == "ja") {
			description = $scope.categoryName["jp"];
		}

		$scope.categoryLabel = {
			line1: (description.indexOf("<br>") != -1)? description.slice(0, description.indexOf("<br>")) : description,
			line2: (description.indexOf("<br>") != -1)? description.slice(description.indexOf("<br>") + "<br>".length) : "",
		};

		var line1 = angular.copy($scope.categoryLabel.line1);
		var line2 = angular.copy($scope.categoryLabel.line2);
		$scope.categoryLabel.line1 = line1.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");
		$scope.categoryLabel.line2 = line2.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");
	};

	$scope.cancel = function() {
		$rootScope.indexCategory = undefined;
	};

//	$scope.preapply = function(){
//		if (!$scope.doApplyCheck()) return;
////		$rootScope.model.pickList.categories[$rootScope.indexCategory] = { ja: $scope.categoryName.ja, en: $scope.categoryName.en };
////		$rootScope.model.pickList.categories[$rootScope.indexCategory][$rootScope.language] = $scope.categoryLabel.line1;
////		if ($scope.categoryLabel.line2)
////			$rootScope.model.pickList.categories[$rootScope.indexCategory][$rootScope.language] += "\n" + $scope.categoryLabel.line2;
//	};

	$scope.apply = function() {
		if (!$scope.doApplyCheck()) return;

		var line1 = $scope.categoryLabel.line1;
		line1 = line1.replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;");

		if ($scope.categoryLabel.line2) {
			var line2 = $scope.categoryLabel.line2;
			line2 = line2.replace(/\&/g, "&amp;").replace(/\</g, "&lt;").replace(/\>/g, "&gt;");
			line1 += "<br>" + line2;
		}

		$rootScope.model.pickList.categories[$rootScope.indexCategory][$rootScope.language] = line1;
		$rootScope.model.pickList.locate($rootScope.language);

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
		$rootScope.indexCategory = undefined;
	};

//	$scope.checkLength = function(event) {
//		if (res.string.getLength($scope.categoryLabel.line1 + String.fromCharCode(event.keyCode)) > 10) {
//			event.preventDefault();
//		}
//	};

	$scope.doApplyCheck = function() {
		var line1Length = res.string.getLength($scope.categoryLabel.line1);
		var line2Length = 0;
		if ($scope.categoryLabel.line2) {
			line2Length = res.string.getLength($scope.categoryLabel.line2);
		}

		if (line1Length > 14 || line2Length > 14) {
			if (line1Length) $scope.categoryLabel.line1 = res.string.truncate($scope.categoryLabel.line1, 14);
			if (line2Length) $scope.categoryLabel.line2 = res.string.truncate($scope.categoryLabel.line2, 14);
			$rootScope.model.failure.active = true;
			$rootScope.model.failure.service = "picklist";
			$rootScope.model.failure.cause = "maxCharacters14";
			return false;
		}
		return true;
	};

}]);

