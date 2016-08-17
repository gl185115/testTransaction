var res = res || {};
res.service = res.service || {};

/*
 * File Service
 */

(function(){

	res.service.schedule = {

//		get: function(data, callback) {
//			var companyID = res.storage.getItem("CompanyID");
//			var parameter = "?companyID=" + companyID;
//
//			$.ajax({
//				type: 'POST',
//				url: res.config.baseURL + "getschedule" + parameter,
//				data: { },
//				datatype: 'json',
//				success: function(data){
//					callback.success(data);
//				},
//				error: function(data){
//					callback.error(data);
//				}
//			});
//		},
//
//		set: function(data, callback) {
//			$.ajax({
//				type: 'POST',
//				url: res.config.baseURL + "setschedule",
//				data: {
//					filename: "schedule.xml",
//					schedulejson : data,
//					saveType : data.saveType
//				},
//				datatype: 'json',
//				success: function(data){
//					callback.success(data);
//				},
//				error: function(data){
//					callback.error(data);
//				}
//			});
//		},

//		getNotices: function(data, callback) {
//			$.ajax({
//				type: 'POST',
//				url: res.config.baseURL + "getNotices",
//				data: {},
//				datatype: 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error: function(data) {
//					callback.error(data);
//				}
//			});
//		},

//		getDeployStatus: function(data, callback) {
//			$.ajax({
//				type: 'POST',
//				url: res.config.baseURL + "getDeployStatus",
//				data: {
//					companyID : data.companyID
//				},
//				datatype: 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error: function(data) {
//					callback.error(data);
//				}
//			});
//		},
//
//		getDeployDetail: function(data, callback) {
//			$.ajax({
//				type: 'POST',
//				url: res.config.baseURL + "getDeployDetail",
//				data: {
//					taskStoreType: data.taskStoreType,
//					taskStoreEntry: data.taskStoreEntry,
//					taskFileName: data.taskFileName,
//					taskEffective: data.taskEffective
//				},
//				datatype: 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error: function(data) {
//					callback.error(data);
//				}
//			});
//		},

//		invokeTransfer: function(data, callback) {
//			$.ajax({
//				type: 'POST',
//				url: res.config.baseURL + "invokeTransfer",
//				data: {
//					transferOption : data.transferOption
//				},
//				datatype: 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error: function(data) {
//					callback.error();
//				}
//			});
//		},
//
//		resetNoticeDate:function(data, callback) {
//			$.ajax({
//				type: 'POST',
//				url: res.config.baseURL + "resetNoticeDate",
//				data: {
//					filename:data.filename,
//					effective:data.effective
//				},
//				datatype: 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error: function(data) {
//					callback.error();
//				}
//			});
//		},
	};
})();