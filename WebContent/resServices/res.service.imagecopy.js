/** * c8: start of update ** */
var res = res || {};
res.service = res.service || {};
/** * c8: start of update ** */

/*
 * File Service
 */

$(document).ready(function() {
	res.service.imagecopy = new Service_imagecopy();
});

(function() {
	// Constructor
	Service_imagecopy = function() {
		this.filepath = undefined;
		this.filename = undefined;
	};

	Service_imagecopy.prototype = {
		copyStart : function(data, callback) {
			requestURL = res.config.baseURL + "imageCopy";
			$.ajax({
				type : 'POST',
				url : requestURL,
				data : {
					'folderCopyFrom' : data.folderCopyFrom,
					'folderCopyTo' : data.folderCopyTo,
					'recursive' : data.recursive,
					'targetWidth':data.targetWidth,
					'targetHeight':data.targetHeight					
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

		getFolder : function(data, callback) {
			requestURL = res.config.baseURL + "folderList";
			$.ajax({
				type : 'POST',
				url : requestURL,
				data : {
					'recursive' : data.recursive,
					'folder' : data.folder,
					'event' : "getfolder",
					'action' : data.action
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
		getTargetFolder : function(data, callback) {
			requestURL = res.config.baseURL + "folderList";
			$.ajax({
				type : 'POST',
				url : requestURL,
				data : {
					'recursive' : data.recursive,
					'folder' : data.folder,
					'event' : "gettargetfolder",
					'action' : data.action
				},
				datatype : 'json',
				success : function(data) {
					callback.success(data);
				},
				error : function(data) {
					callback.error(data);
				}
			});
		}
	};
})();