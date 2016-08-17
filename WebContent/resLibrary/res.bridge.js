/*** c8: start of update ***/
var res = res || {};
/*** c8: start of update ***/
/*
 * External Interface to AngularJS
 */
$(document).ready(function(){
	res.bridge.rootScope = angular.element(document).scope(); // to get $rootScope
});

(function(){
	
	res.bridge = {};
	res.bridge.model = function(selector, obj){
		if (!res.bridge.rootScope) return;	// angularJS not constructed
		var scope;
		if (!selector || selector == "root") {		
			scope = res.bridge.rootScope;
		}else{
			scope = angular.element(selector).scope();				
		}
		if (!scope){
			alert("res.bridge.model: targeted scope not found (\"" + selector + "\")");
			return;
		}
		
		if (!scope.$$phase) { // from outside non-AngularJS
			scope.$apply(set(obj));	
		}else{		
			set(obj);
		};
		
		function set(obj){
//			for (property in obj){
//				scope[property] = obj[property];
//			}
			$.extend(true, scope, obj);
		}
	};
	
	res.bridge.set = function(selector, property, value){
		if (!res.bridge.rootScope) return;	// angularJS not constructed
		var scope;
		if (!selector || selector == "root") {		
			scope = res.bridge.rootScope;
		}else{
			scope = angular.element(selector).scope();				
		}
		if (!scope.$$phase) { // from outside non-AngularJS
			return scope.$apply(set(property, value));	
		}else{		
			return set(property, value);
		};
		
		function set(p, v){
			var obj = scope;
			var keys = p.split(".");
			for (var i = 0; i < keys.length - 1; i++){
				obj = obj[keys[i]];
			}
			obj[keys[i]] = v;					
			return value;
		}
	};
	
	res.bridge.get = function(selector, model){
		if (!res.bridge.rootScope) return;	// angularJS not constructed
		var scope;
		if (!selector || selector == "root") {		
			scope = res.bridge.rootScope;
		}else{
			scope = angular.element(selector).scope();				
		}
		return scope[model];
	};
	
	res.bridge.run = function(selector, func, arg){
		if (!res.bridge.rootScope) return;	// angularJS not constructed
		if (!selector || selector == "root") {		
			scope = res.bridge.rootScope;
		}else{
			scope = angular.element(selector).scope();				
		}
		var keys = func.split(".");
		var obj = scope;
		for (var i = 0; i < keys.length; i++){
			obj = obj[keys[i]];
		}
		if (scope.$$phase || scope.$root.$$phase) {
				(obj)(arg);				
		}else{		// from outside non-AngularJS
			scope.$apply((obj)(arg));	
		}
	};
	
})();
