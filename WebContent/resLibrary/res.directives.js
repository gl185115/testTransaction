var res = res || {};
/*
 *  Angular Directives
 */
res.directives = angular.module("res.directives", []);

/*
 * Load res.directives.css from library
 */
(function(){
    res.directives.path = "";
    var scripts = document.getElementsByTagName("script");
    for (var i = 0; i < scripts.length; i++) {
        var match = scripts[i].src.match(/(^|.*\/)res.directives\.js$/);
        if (match) {
            res.directives.path = match[1];
            break;
        }
    }
	document.write('<link href="'+ res.directives.path + 'res.directives.css" rel="stylesheet" type="text/css">');

})();

/*
 *  res-tenkey: 10 key
 */
res.directives.directive("resTenkey", ["$rootScope", function($rootScope){
	return {
		restrict: "A",	// A: only matches attribute name, E: only matches element name, AE: matches either A or E
		replace: false,	// true: replace the element, false: append inside the element
		templateUrl: res.directives.path + "resTenkey.html",
		scope: true,	// create new scope
		link: function(scope, element, attrs){
			var child = attrs.resTenkey;
			if (!child) child = "resTenkey";
			scope.$parent[child] = scope;
		},
		controller: ["$scope", function($scope){
//			$scope.$parent.resTenkey = $scope;
			$scope.maxDigits = 50;
			$scope.preset = "";
			$scope.baseline = 0;
			$scope.entry = "";
			$scope.digits = 0;
			$scope.target = "echo";
			$scope.isEntered = false;
			$scope.decimalPointEnabled = res.config.decimalPointEnabled;

			$scope.echo = function(target){
				$scope.target = target;
				$scope.isEntered = false;
				$scope.$parent.resIsEntered = false;
			};
			$scope.echo = function(target, length){
				$scope.target = target;
				$scope.isEntered = false;
				$scope.$parent.resIsEntered = false;
				if (length) {
					$scope.maxDigits = length;
				} else {
					$scope.maxDigits = 50;
				}
			};
			$scope.reset = function(num){
				if (num!=undefined) $scope.preset = num;
				echoback($scope.preset);
				$scope.baseline = 0;
				$scope.entry = "";
				$scope.digits = 0;
				$scope.isEntered = false;
				$scope.$parent.resIsEntered = false;
			};

			$scope.numeric = function(n){
				if (($scope.digits >= $scope.maxDigits) ||
					(n === "000" && ($scope.digits + 3) > $scope.maxDigits) ) {
					return;
				}
				if ($scope.digits==0) $scope.entry = "";
				$scope.entry += n;
				if ($scope.baseline > 0){
					echoback("" + (parseInt($scope.entry) + $scope.baseline));
				}else{
					echoback($scope.entry);
				}
				$scope.digits++;
				$scope.isEntered = true;
				$scope.$parent.resIsEntered = true;
			};
			$scope.addTo = function(amount){
				var bills;
				if (amount == 0) return;
				var bills = ($scope.digits > 0)? parseInt($scope.entry) : 1;
				if (bills == 0) bills = 1;	// even if operator enter 0, force it to 1
				$scope.baseline = amount * bills;
				$scope.entry = "";
				$scope.digits = 0;
				echoback($scope.baseline);
				$scope.isEntered = true;
				$scope.$parent.resIsEntered = true;
			};
			$scope.backspace = function(){
				if ($scope.digits == 0) return;
				$scope.entry = $scope.entry.slice(0, -1);
				$scope.digits--;
				if ($scope.baseline > 0){
					echoback("" + ((($scope.digits > 0)? parseInt($scope.entry) : 0) + $scope.baseline));
				}else{
					echoback($scope.entry);
				}
			};
			$scope.clear = function(){
				$scope.reset();
				$scope.$emit("resTenkeyCleared");
			};
			$scope.enter = function(){
				$scope.$emit("resTenkeyEntered", echoback());
			};
			$scope.$on("resTenkeyReset", function(event, value){
				$scope.reset(echoback());
			});

			function echoback(source){
				var buffer;
				var obj = $scope.$parent;
				var keys = $scope.target.split(".");
				for (var i = 0; i < keys.length - 1; i++){
					if (keys[i]=="$rootScope"){
						obj = $rootScope;
					}else{
						buffer = keys[i].match(/(\w+)\[(\w+)\]/);
						if (buffer==null){
							obj = obj[keys[i]];
						}else{
							obj = obj[buffer[1]][buffer[2]];
						}
					}
				}
				if (source!=undefined){
					obj[keys[i]] = source;
				}
				return obj[keys[i]];
			}
		}]
	};
}]);

/*
 *  res-click: event on click
 */
res.directives.directive("resClick", function(){
//	var isTouchDevice = !!("ontouchstart" in window);
	return function(scope, element, attr){
		element.bind("click", function(e){
			if (element.hasClass("disabled")) return;
			if (typeof res.audio != "undefined" && typeof res.audio.play != "undefined")
				res.audio.play("click");
			else if (typeof window.parent.res.audio != "undefined" && typeof window.parent.res.audio.play != "undefined")
				window.parent.res.audio.play("click");
			scope.$apply(attr.resClick);
			e.preventDefault();
		});
	};
});

/*
 *  res-tap: event at 50 msec after touch start
 */
res.directives.directive("resTap", function(){
	var isTouchDevice = !!("ontouchstart" in window);
	return function(scope, element, attr){
		if (isTouchDevice){
			element.bind("touchstart", function(e){
				if (element.hasClass("disabled")) return;
				if (typeof res.audio != "undefined" && typeof res.audio.play != "undefined")
					res.audio.play("tap");
				else if (typeof window.parent.res.audio != "undefined" && typeof window.parent.res.audio.play != "undefined")
					window.parent.res.audio.play("tap");
				e.preventDefault();
				setTimeout(function(){
					element.trigger("click");
					scope.$apply(attr.resTap);
				}, 50);
			});
		}else{
			element.bind("mousedown", function(e){
				if (element.hasClass("disabled")) return;
				if (typeof res.audio != "undefined" && typeof res.audio.play != "undefined")
					res.audio.play("tap");
				else if (typeof window.parent.res.audio != "undefined" && typeof window.parent.res.audio.play != "undefined")
					window.parent.res.audio.play("tap");
				e.preventDefault();
				setTimeout(function(){
					element.trigger("click");
					scope.$apply(attr.resTap);
				}, 50);
			});
		}
	};
});

/*
 *  res-touch: event on touch start
 */
res.directives.directive("resTouch", function(){
	var isTouchDevice = !!("ontouchstart" in window);
	return function(scope, element, attr){
		if (isTouchDevice){
			element.bind("touchstart", function(e){
				if (element.hasClass("disabled")) return;
				if (typeof res.audio != "undefined" && typeof res.audio.play != "undefined")
					res.audio.play("touch");
				else if (typeof window.parent.res.audio != "undefined" && typeof window.parent.res.audio.play != "undefined")
					window.parent.res.audio.play("touch");
				e.preventDefault();
				scope.$apply(attr.resTouch);
			});
		}else{
			element.bind("mousedown", function(e){
				if (element.hasClass("disabled")) return;
				if (typeof res.audio != "undefined" && typeof res.audio.play != "undefined")
					res.audio.play("touch");
				else if (typeof window.parent.res.audio != "undefined" && typeof window.parent.res.audio.play != "undefined")
					window.parent.res.audio.play("touch");
				e.preventDefault();
				scope.$apply(attr.resTouch);
			});
		}
	};
});

/*
 * res-include: to include external html template
 */
res.directives.directive("resInclude", function(){
	return {
		restrict: "A",	// A: only matches attribute name, E: only matches element name, AE: matches either A or E
		replace: false,	// true: replace the element, false: append inside the element
		templateUrl: function(scope, attr){
			return attr.resInclude;
		},
		link: function link(scope, element, attr){
			scope.$broadcast("resIncludeLoaded");	// res-include loading complete
		},
	};
});

/*
 * res-maxLength: limit maximum length of text in <input> entry
 * 	ASCII characters are counted by 1
 * 	Japanese 2-byte characters are counted by 2
 */
res.directives.directive("resMaxlength", function(){
	return {
		restrict: "A",	// A: only matches attribute name, E: only matches element name, AE: matches either A or E
		replace: false,	// true: replace the element, false: append inside the element
		link: function(scope, element, attr){
			var max = parseInt(attr.resMaxlength);
			element.bind("keydown", function(event){
				if (event.keyCode==8 || event.keyCode==46) return; 	// backspace or del key
				if (element[0] && stringWidth(element[0].value + String.fromCharCode(event.keyCode)) > max){
					event.preventDefault();
				}
			});

			function stringWidth(str) {
				var r = 0;
				for (var i = 0; i < str.length; i++) {
					var c = str.charCodeAt(i);
					// Shift_JIS: 0x0 ～ 0x80, 0xa0 , 0xa1 ～ 0xdf , 0xfd ～ 0xff
					// Unicode : 0x0 ～ 0x80, 0xf8f0, 0xff61 ～ 0xff9f, 0xf8f1 ～ 0xf8f3
					if ( (c >= 0x0 && c < 0x81) || (c == 0xf8f0) || (c >= 0xff61 && c < 0xffa0) || (c >= 0xf8f1 && c < 0xf8f4)) {
						r += 1;
					} else {
						r += 2;
					}
				}
				return r;
			}
		},

	};
});

