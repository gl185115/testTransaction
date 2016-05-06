var res = res || {};
res.model = res.model || {};

/*
 *	Data Model for Image Copy
 */
(function() {

	res.model.init = function() {
		res.ui.root.model = {};
		res.ui.root.model.imageCopy = new ImageCopy();
		res.ui.root.model.imageRemove = new ImageRemove();
	};

	res.model.update = function(event, data) {
		var model = res.ui.root.model;
		switch (event) {
		case "imagecopy.copystart":
			if (data.status == "OK") {
				model.imageCopy.successMsgShow = true;
				model.imageCopy.failedMsgShow = false;
				model.imageCopy.pictures = [];

				model.imageCopy.imageURL = model.imageCopy.baseURL + "pickList/images/";
				res.ui.send({
					event : "file.picture.list",
					data : {
//						folder : "images/pickList"
					    folder : "pickList/images",
					    sizeType : 0,
					}
				});
				break;

			} else {
				model.imageCopy.successMsgShow = false;
				model.imageCopy.failedMsgShow = true;
				break;
			}
		case "imagecopy.getfolder":
			var basePath = model.imageCopy.BasePath;
			if ((basePath != null && basePath.length > 0)
					&& data.targetBasePath == "" && data.action == "open") {
				break;
			}

			if (data.folders && data.folders.length > 0) {
				model.imageCopy.files.length = 0;
				for (var i = 0; i < data.folders.length; i++) {
					model.imageCopy.files.push({filename : data.folders[i], selected : undefined});
				}

				model.imageCopy.contextPath = data.contextPath;
				model.imageCopy.BasePath = data.targetBasePath;
			}
			break;
		case "imagecopy.gettargetfolder":
			if (data.folders.length > 0) {
				model.imageCopy.targetfiles.length = 0;
				model.imageCopy.disableTargetBack = false;
				for (var i = 0; i < data.folders.length; i++) {
					if (data.folders[i] == model.imageCopy.targetRoot && (model.imageCopy.targetRoot != "")) {
						model.imageCopy.targetfiles.length = 0;
						model.imageCopy.targetfiles.push(data.folders[i]);
						model.imageCopy.disableTargetBack = true;
						break;
					} else if (data.folders[i].indexOf(model.imageCopy.targetRoot) == 0) {
						model.imageCopy.targetfiles.push(data.folders[i]);
					}
				}

				if (model.imageCopy.targetfiles.length == 0) {
					model.imageCopy.disableTargetBack = true;
				}

				model.imageCopy.contextPath = data.contextPath;
				model.imageCopy.targetBasePath = data.targetBasePath;
			}
			break;
		case "file.picture.list":
			res.config.imagecopy.targetWidth = 114;
			res.config.imagecopy.targetHeight = 76;

			model.imageCopy.pictures = [];
			if (data && data.result) {
				model.imageCopy.pictures = data.result;
			}

			if (model.imageCopy.indexImage >= 1) {
				--model.imageCopy.indexImage;
			}

			model.imageCopy.targetRoot = "\\pickList";
			model.imageCopy.imageURL = model.imageCopy.baseURL+ "pickList/images/";
			break;
		case "file.remove":
			window.parent.res.ui.root.model.popup = "";
			res.ui.root.dialog = "imageRemove";

			if (data && data.status) {
				if (data.status == "success") {
					model.imageRemove.delProcess = data.status;
					model.imageCopy.pictures = [];

					res.ui.send({
						context: res.ui.root.context,
						event : "file.picture.list",
						data : {
//							folder : "images/pickList"
						    folder : "pickList/images",
						    sizeType : 0,
						}
					});

				} else if (data.status == "exist") {
					model.imageRemove.delProcess = data.status;
					model.imageRemove.delFileList = data.description;

				} else if (data.status == "failed") {
					model.imageRemove.delProcess = data.status;
					model.imageRemove.delFileList = data.description;
				}
			}

//			model.imageCopy.pictures = [];
//			res.ui.send({
//				context: res.ui.root.context,
//				event : "file.picture.list",
//				data : {
//					folder : "images/pickList"
//				}
//			});
			break;
		case "file.picture.upload.success":
            model.imageCopy.successMsgShow = true;
            model.imageCopy.failedMsgShow = false;
            model.imageCopy.pictures = [];
            model.imageCopy.imageURL = model.imageCopy.baseURL + "pickList/images/";

            res.ui.root.itemSelected={};
            res.ui.root.itemSelected.background = "image";
            res.ui.root.itemSelected.picture = data.image;
            res.ui.root.dialog = "";

            window.parent.res.ui.root.model.popup = "";

            var fileInput = document.getElementById('picklistImgfileInput');
            fileInput.value="";
            res.ui.send({
                event : "file.picture.list",
                data : {
//                    folder : "images/pickList"
                    folder : "pickList/images",
                    sizeType : 0,
                }
            });
            break;
        case "file.picture.upload.failed":
        	window.parent.res.ui.root.model.popup = "";
            break;
		default:
			alert("imagecopy: unknown message.event = " + event);
			break;
		}
	};

})();

/*
 * Data Model
 */
(function(){

	ImageCopy = function() {
		this.files = [];
		this.targetfiles = [];
		this.indexFileName = "";
		this.recursive = true;
//		this.baseURL = "/resUiConfig/custom/images/";
		this.baseURL = res.config.baseURL + "rest/uiconfig/custom/";
		this.imageURL = "";
		this.successMsgShow = false;
		this.failedMsgShow = false;
		this.folderCopyFrom = "";
		this.folderCopyTo = "";
		this.contextPath = "";
		this.targetBasePath = "";
		this.targetSelected = "none";
		this.targetRoot = "";
		this.disableTargetBack = false;
	};

	ImageRemove = function() {
		this.imageRemove = {
			delProcess : "",
			delFileList : [{
				filename : "",
				fullName : ""
			}],
			delFilePath : ""
		};
	};
})();
