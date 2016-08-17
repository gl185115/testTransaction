/*
 * Controller
 */
res.ui.controller("attachFile", [ "$scope", "$rootScope", "$timeout",function($scope, $rootScope, $timeout) {

	var scroll = undefined;
	$scope.$on("resIncludeLoaded", function() { // wait for res-include
		// complete
		scroll = new IScroll("#wrapperImages", { // iScroll5
			scrollX : false,
			scrollY : true,
			scrollbars : true,
			interactiveScrollbars : true,
			mouseWheel : true, // or "zoom"
		});
		var fileInput = document.getElementById('fileInput');
		fileInput.addEventListener('change', function(e) {
			var file = fileInput.files[0];
			var reader = new FileReader();

			window.parent.res.ui.root.model.popup = "Wait";
			reader.filename=file.name;
			reader.onload = function(e) {
				var result= reader.result;
				var dataFileName=reader.filename;
//					res.ui.send({context:"Notice", event:"file.picture.upload", data:{filename:dataFileName,filecontent:result,folder:"notices"}});
				res.ui.send({
					context : res.ui.context,
					event : "file.picture.upload",
					data : {
						filename : dataFileName,
						filecontent : file,
						folder : "notices"
					}
				});
			};
			reader.readAsDataURL(file);
		});
	});

	$scope.$watch(
		function() {
			return $rootScope.dialog;
		}, function(dialog, oldValue) {
			switch (dialog) {
			case "images":
				$timeout(function() {
					scroll.refresh();
				}, 200);
				break;
			}
		}
	);

	$scope.$watch(
		function(){ return $rootScope.model.notice.pictures; },
		function(pictures, oldValue){
			$timeout(function(){ scroll.refresh(); }, 200);
		}
	);

	$scope.cancel = function() {
		$rootScope.dialog = "";
	};

	$scope.setImage = function(image) {
		$rootScope.model.notice.selectedImage = image;
		$rootScope.dialog = "";
	};
} ]);
