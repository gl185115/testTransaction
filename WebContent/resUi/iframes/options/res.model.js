var res = res || {};
res.model = res.model || {};

/*
 *	Data Model for On-Screen Options
 */
(function(){

	res.model.init = function(){
		res.ui.root.model = new Options();
	};
	res.model.update = function(event, data) {
//		var model = res.ui.root.model;
		switch(event){
		default:
			alert("options: unknown message.event = " + event);
			break;
		}			
	};

})();

/*
 * Data Model
 */
(function(){

	Options = function(){
	};
	Options.prototype = {
	};
	
})();
