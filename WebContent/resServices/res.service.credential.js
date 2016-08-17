/*** c8: start of update ***/
var res = res || {};
res.service = res.service || {};
/*** c8: start of update ***/

/*
 * Credential Service
 */

$(document).ready(function(){
	res.service.credential = new Service_credential();
});

(function(){
	// Constructor
	Service_credential = function(){
		this.userID = undefined;
	};
	
	Service_credential.prototype = {
		login: function(userID, password){
			for (var i = 0; i < res.table.users.length; i++ ){
				if (res.table.users[i][0] == userID){
					break;
				}
			}
			if ( i < res.table.users.length ){
				this.userID = userID;
				res.storage.setItem("OperatorID", userID);
				res.storage.setItem("OperatorName", res.table.users[i][1]);
				res.storage.setItem("TillID", "1");
				return {ID: userID, name: res.table.users[i][1]};
			}else {
				return undefined;
			}
		},
		logout: function(){
			this.userID = undefined;
		}
	};
})();


