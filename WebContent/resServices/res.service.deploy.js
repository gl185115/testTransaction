var res = res || {};
res.service = res.service || {};
/*
 * store csv Service
 */
$(document).ready(function() {
	res.service.deploy = new Service_deploy();
});

(function() {
	// Constructor
	Service_deploy = function() {

	};

	Service_deploy.prototype = {

		getDeployStoreAndGroup : function(data, callback) {
			$.ajax({
				type : 'POST',
				url : res.config.baseURL + "rest/uiconfigMaintenance/getDeployStoreAndGroup",
				data : {
					companyID : data.companyID
				},
				dataType : 'json',
				success : function(data) {
					callback.success(data.tableStore);
				},
				error : function(data) {
					callback.error(data.tableStore);
				}
			});
		},

		getSchedule: function(data, callback) {
			var companyID = res.storage.getItem("CompanyID");
			var parameter = "?companyID=" + companyID;

			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "getSchedule" + parameter,
				data: {
					resource : data.resource
				},
				datatype: 'json',
				success: function(data){
					callback.success(data);
				},
				error: function(data){
					callback.error(data);
				}
			});
		},

		setSchedule: function(data, callback) {
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "setSchedule",
				data: {
					filename: "schedule.xml",
					schedulejson : data.schedule,
					resource : data.resource
				},
				datatype: 'json',
				success: function(data){
					callback.success(data);
				},
				error: function(data){
					callback.error(data);
				}
			});
		},

		getDeployStatus: function(data, callback) {
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "getDeployStatus",
				data: {
					companyID : data.companyID,
					resource : data.resource
				},
				datatype: 'json',
				success : function(data) {
					callback.success(data);
				},
				error: function(data) {
					callback.error(data);
				}
			});
		},

		getDeployDetail: function(data, callback) {
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "getDeployDetail",
				data: {
					taskStoreType: data.taskStoreType,
					taskStoreEntry: data.taskStoreEntry,
					taskFileName: data.taskFileName,
					taskEffective: data.taskEffective
				},
				datatype: 'json',
				success : function(data) {
					callback.success(data);
				},
				error: function(data) {
					callback.error(data);
				}
			});
		},

		saveDeployDetail : function(data, callback) {
			$.ajax({
				type : 'POST',
				url : res.config.baseURL + "saveDeployDetail",
				data : {
					"itemFilename" : data.deployName,
					"itemEffective" : data.deployEffective,
					"itemDeployType" : data.deployType,
					"itemStoreEntry" : data.deployStoreEntry,
					"csvSavePath" : data.csvSavePath,
					"csvSaveType" : data.csvSaveType
				},
				datatype : 'json',
				success : function(data) {
					callback.success(data);
				},
				error : function(data) {
					callback.error(data);
				}
			});
		},

		viewTableStore : function(data, callback) {
			$.ajax({
				type : 'POST',
				url : res.config.baseURL + "viewTableStore",
				data : {},
				dataType : 'json',
				success : function(data) {
					callback.success(data);
				},
				error : function(data) {
					callback.error(data);
				}
			});
		},

		editTableStore : function(data, callback) {
			$.ajax({
				type : 'POST',
				url : res.config.baseURL + "editTableStore",
				data : {
					editType : data.editType,
					storeInfo : data.storeInfo
				},
				dataType : 'json',
				success : function(data) {
					callback.success(data);
				},
				error : function(data) {
					callback.error(data);
				}
			});
		},

		viewTableGroup : function(data, callback) {
			$.ajax({
				type : 'POST',
				url : res.config.baseURL + "viewTableGroup",
				data : {},
				dataType : 'json',
				success : function(data) {
					callback.success(data);
				},
				error : function(data) {
					callback.error(data);
				}
			});
		},

		editTableGroup : function(data, callback) {
			$.ajax({
				type : 'POST',
				url : res.config.baseURL + "editTableGroup",
				data : {
					editType : data.editType,
					groupData : data.groupData
				},
				dataType : 'json',
				success : function(data) {
					callback.success(data);
				},
				error : function(data) {
					callback.error(data);
				}
			});
		},

		getSysFileTree : function(data, callback) {
			requestURL = res.config.baseURL + "getSysFileTree";
			$.ajax({
				type : 'POST',
				url : requestURL,
				data : {
					'basePath' : data.basePath,
					'actionType' : data.actionGo
				},
				datatype : 'json',
				success : function(data) {
					callback.success(data);
				},
				error : function(data) {
					callback.error(data);
				}
			});
		},

		invokeTransfer: function(data, callback) {
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "invokeTransfer",
				data: {
					transferOption : data.transferOption
				},
				datatype: 'json',
				success : function(data) {
					callback.success(data);
				},
				error: function(data) {
					callback.error(data);
				}
			});
		},

		resetNotice:function(data, callback) {
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "resetNotice",
				data: {
					filename:data.filename,
					effective:data.effective,
					expire : JSON.stringify(data.expire),
					updateKey : data.updateKey
				},
				datatype: 'json',
				success : function(data) {
					callback.success(data);
				},
				error: function(data) {
					callback.error(data);
				}
			});
		},

		getNotices: function(data, callback) {
			$.ajax({
				type: 'POST',
				url: res.config.baseURL + "getNotices",
				data: {},
				datatype: 'json',
				success : function(data) {
					callback.success(data);
				},
				error: function(data) {
					callback.error(data);
				}
			});
		},

//		profile : function(data, callback) {
//			$.ajax({
//				type : 'POST',
//				url : res.config.baseURL + "storeCsv",
//				data : {
//					'lineType' : data.lineType,
//					'commandType' : data.commandType,
//					'line' : data.line,
//					'lineDetail' : data.lineDetail
//				},
//				dataType : 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error : function(data) {
//					callback.error(data);
//				}
//			});
//		},
//		viewStores : function(data, callback) {
//			$.ajax({
//				type : 'POST',
//				url : res.config.baseURL + "getAllStoresCsv",
//				data : {},
//				dataType : 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error : function(data) {
//					callback.error(data);
//				}
//			});
//		},

//		viewStoreGroups : function(data, callback) {
//			$.ajax({
//				type : 'POST',
//				url : res.config.baseURL + "getAllStoreGroupCsv",
//				data : {},
//				dataType : 'json',
//				success : function(data) {
//					callback.success(data);
//				},
//				error : function(data) {
//					callback.error(data);
//				}
//			});
//		},

	};
})();