var res = res || {};
res.model = res.model || {};

/*
 *	Data Model for Usability
 */
(function(){

	res.model.init = function(){
		res.ui.root.model = new Usability();
	};
	res.model.update = function(event, data) {
//		var model = res.ui.root.model;
		switch(event){
		default:
			alert("home: unknown message.event = " + event);
			break;
		}			
	};

})();

/*
 * Data Model
 */
(function(){

	Usability = function(){
	};
	Usability.prototype = {
	};
	
})();
