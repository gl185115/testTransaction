var res = res || {};
res.service = res.service || {};
/*
 * store csv Service
 */
$(document).ready(function(){
	res.service.device = new Service_device();
});
(function(){
	// Constructor
	Service_device = function(){

	};

	Service_device.prototype = {
		profile: function(data,callback){
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "profile",
				data: { },
				dataType: 'json',
				success: function(data){
					callback.success(data);
				},
				error: function(data){
					callback.error(data);
				}
			});
		},
		registration: function(data,callback){
            $.ajax({
                type: 'POST',
                url: res.config.baseURL + "rest/uiconfigMaintenance/getcompanyinfo",
                data: { },
                dataType: 'json',
                success: function(data){
                    if(data) {
                        callback.success(data.CompanyInfoList);
                    }
                },
                error: function(data){
                    callback.error(data);
                }
            });
        },
		balanceSchedule : function(data, callback) {
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "balanceSchedule",
				data: {
					companyID : data.companyID
				},
				datatype: 'json',
				success: function(data) {
					callback.success(data);
				},
				error: function(data) {
					callback.error(data);
				}
			});
		},
	};
})();