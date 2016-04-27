var res = res || {};
res.model = res.model || {};

/*
 *	Data Model for Notices Editor
 */
(function () {

	res.model.init = function () {
		res.ui.root.model = {};
		res.ui.root.model.notice = new Notices();
		res.ui.root.model.editor = new Editor();
		res.ui.root.model.failure = new Failure();
	};

	res.model.update = function (event, data) {
		var model = res.ui.root.model;
		switch (event) {
		case "file.list":
			var files = data;
			model.editor.files = [];
			var result;
			for (var i = 0; i < files.length; i++) {
				if (result = files[i].match(/(.+)\.js$/i)) {
					model.editor.files.push(result[1]);
				}
			}

			model.editor.indexEdit = "itemList";
			model.editor.selectedFile = undefined;
			model.editor.selectedIndex = undefined;
			break;
		case "file.remove":
			model.editor.indexEdit ="itemList";
			model.editor.selectedFile = undefined;
			model.editor.selectedIndex = undefined;
			break;
		case "file.download":
			if (data) {
				model.editor.indexEdit = "editItems";
				var item = eval(data.result);
				model.editor.title="";
				model.editor.title2="";
				model.editor.expire="";
				model.editor.expireTemp="";
				model.notice.items="";
				model.notice.selectedImage="";
				model.editor.selectedFile=undefined;
				model.editor.selectedIndex = undefined;
				if(!item){
					break;
				}
				model.editor.title=item.title;
				model.editor.titlePrint = item.title;
				model.editor.title = model.editor.title.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");

				model.editor.title2 = item.title2 && item.title2.toLowerCase() != "null" ? item.title2 : "";
				model.editor.title2Print = model.editor.title2;
				model.editor.title2 = model.editor.title2.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");

				model.editor.expire = item.expire && item.expire.toLowerCase() != "null" ? item.expire : "";
				model.notice.items = decodeURIComponent(item.body);
				model.notice.items = model.notice.items.replace(/\&amp;/g, "&").replace(/\&lt;/g, "<").replace(/\&gt;/g, ">");

				var fileURL = verifyURL(item.attachment);
				model.notice.selectedImage = fileURL;
				model.notice.OriginalItems = model.notice.items;
				model.notice.orgTitle1 = model.editor.title;
				model.notice.orgTitle2 = model.editor.title2;
				model.notice.orgExpire = model.editor.expire;
				model.notice.orgImage = model.notice.selectedImage;
				model.notice.orgContent = model.notice.items;
				model.editor.selectedFile=undefined;
			}
			break;
		case "file.upload":
			if (data == "success") {
				model.notice.status = "successful";
				model.notice.OriginalItems = model.notice.items;
				break;
			} else if (data == "exist") {
				model.notice.alreadyExists = true;
			} else if (data == "failed") {
				model.notice.status = "failed";
			}
			model.notice.OriginalItems = model.notice.items;
			break;
		case "file.picture.list":
			res.ui.root.dialog  ="images";
			model.notice.pictures = [];
			if (data && data.result) {
				model.notice.pictures = data.result;
			}
			break;
		case "file.picture.upload.success":
			model.notice.selectedImage = data.image;
			res.ui.root.dialog = "";

			window.parent.res.ui.root.model.popup = "";

			var fileInput = document.getElementById('fileInput');
			fileInput.value="";
			break;
		case "file.picture.upload.failed":
			window.parent.res.ui.root.model.popup = "";
			break;
		case "deploy.notices.getNotices.successful":
		case "deploy.notices.getNotices.failed":
			model.editor.noticesData = data;
			if (isCanRemove(model.editor.noticesResource)) {
				res.ui.root.dialog = "fileRemove";
			} else {
				model.failure.active = true;
				model.failure.service = "notices";
				model.failure.cause = "scheduled";
			}
			break;
		default:
			alert("home: unknown message.event = " + event);
			break;
		}
	};

	function isCanRemove(tasks) {
		if (!tasks || tasks.length == 0) return null;
		if (tasks.length <= 1) return tasks;

		var storeTask = [];
		var filename = "";
			for (var i = 0; i < tasks.length; i++) {
			var task = tasks[i];
			if (task == undefined || task.target == undefined) continue;

			if (res.ui.root.model.editor.selectedFile.endsWith(".js")) {
				filename = res.ui.root.model.editor.selectedFile;
			} else {
				if (res.ui.root.model.editor.selectedFile + ".js" == task.filename) {
					filename = task.filename;
				}
			}

			if (filename == task.filename) {
				var today = new Date();
				var maxDate = new Date(task.effective);
//	            maxDate.setHours(maxDate.getHours() + (new Date()).getTimezoneOffset() / 60);
				maxDate.setHours(maxDate.getHours() + ((new Date()).getTimezoneOffset() / 60) + 9);
				if (maxDate <= today) {
					for (var n = 0; n < res.ui.root.model.editor.noticesData.length; n++) {
						var notice = res.ui.root.model.editor.noticesData[n];
						if (!notice) continue;
						if (!notice.expire || notice.expire.toLowerCase() == "null") continue;

							if (notice.filename == task.filename) {
								var dateExpire = new Date(notice.expire.substring(0, 4),
										parseInt(notice.expire.substring(4, 6)) - 1,
										notice.expire.substring(6, 8), "23", "59", "59");
								if (today > dateExpire) {
									return true;
								}
							}
					}
				}

				return false;
			}
		}

		return true;
	};

	function verifyURL(url) {
		url += "";
		if (url && url !== "undefined" && url !== "") {
			return decodeURIComponent(url);
		}
		return false;
	}

})();

/*
 * Data Model
 */
(function () {

	Editor = function () {
		this.title = "";
		this.indexEdit = undefined;
		this.selectedFile = undefined;
		this.selectedIndex = undefined;
		this.files = [];
		this.expireTemp = "";
	};

	Notices = function () {
		this.items = "";
		this.OriginalItems = "";
		this.orgTitle1 = undefined;
		this.orgTitle2 = undefined;
		this.orgExpire = undefined;
		this.orgImage = undefined;
		this.orgContent = undefined;
		this.pictures = [];
		this.imageURL = "/resUiConfig/custom/images/notices/";
		this.selectedImage = false;
	};

	Failure=function(){
		this.active= false,
		this.service= undefined,
		this.cause= undefined,
		this.description= undefined,
		this.retrying= false;
	};

})();
