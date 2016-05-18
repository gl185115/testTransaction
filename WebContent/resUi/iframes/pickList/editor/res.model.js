var res = res || {};
res.model = res.model || {};

/*
 *	Data Model for PickList Editor
 */
(function(){

	res.model.init = function() {
		res.ui.root.model = {};
		res.ui.root.model.editor = new Editor();
		res.ui.root.model.pickList = new PickList();
		res.ui.root.model.failure = new Failure();
	};

	res.model.update = function(event, data) {
		var model = res.ui.root.model;
		switch(event){
		case "file.list":
			var files = data;
			model.editor.files = [];
			var result;
			for (var i = 0; i < files.length; i++){
				if (result = files[i].FileName.match(/(.+)\.js$/i)){
					model.editor.files.push(result[1]);
				}
			}

			model.editor.indexEdit = "itemList";
			model.editor.selectedFile = undefined;
			model.editor.selectedIndex = undefined;
			break;
		case "file.remove":
			model.editor.indexEdit = "itemList";
			model.editor.title = undefined;
			model.editor.selectedFile = undefined;
			model.editor.selectedIndex = undefined;
			break;
		case "file.download":
			if (data) {
				model.pickList = new PickList();
				res.config.pickList = {};
				eval(data.result);
				if (res.config.pickList.imageURL) model.pickList.imageURL = res.config.pickList.imageURL;
				if (res.config.pickList.items) model.pickList.items = res.config.pickList.items;
				$.extend(model.pickList.categories, res.config.pickList.categories);
				if (res.config.pickList.lists) model.pickList.lists = res.config.pickList.lists;
				// convert pickList.lists to pickList.items and pickList.layout
				model.pickList.convertFromLists();
				// refresh used locations
				model.pickList.locate(res.ui.root.language);

				model.pickList.OriginalItems = angular.copy(model.pickList.items);
				model.pickList.OriginalCategories = angular.copy(model.pickList.categories);
				model.pickList.OriginalLayout= angular.copy(model.pickList.layout);
				res.ui.root.pickListDisableClose = false;
				model.editor.selectedFile = undefined;
				model.editor.selectedIndex = undefined;
//				model.editor.indexEdit = "editItems";
				model.editor.indexEdit = "editLayout"

				model.printList= angular.copy(model.pickList.items);
				for(var i = 0;i< model.printList.length;i++) {
					var printItem = model.printList[i];
					var description = printItem.description[res.ui.root.language];
					if(typeof(description) == "undefined"){
						description = printItem.description["jp"];
					}
					model.printList[i].label = {
						line1: (description.indexOf("<br>") != -1)? description.slice(0, description.indexOf("<br>")) : description,
						line2: (description.indexOf("<br>") != -1)? description.slice(description.indexOf("<br>") + "<br>".length) : "",
					};
					if(printItem.background=='image' && printItem.picture){
						model.printList[i].isImage = "あり";
						model.printList[i].imageName = printItem.picture;
						model.printList[i].bgColor = "";
					}else if(printItem.background=='image' && !printItem.picture){
						model.printList[i].isImage = "あり";
						model.printList[i].imageName = "準備中";
						model.printList[i].bgColor = "";
					}else if(printItem.background!='image'){
						model.printList[i].isImage = "なし";
						model.printList[i].imageName = "";
						model.printList[i].bgColor = printItem.background;
					}
				}
			}
			break;
		case "file.upload":
			if (data == "success") {
				model.pickList.status = "successful";
				model.pickList.OriginalItems = angular.copy(model.pickList.items);
				model.pickList.OriginalCategories = angular.copy(model.pickList.categories);
				model.pickList.OriginalLayout= angular.copy(model.pickList.layout);
				break;
			} else if (data == "exist") {
				model.pickList.alreadyExists = true;
			} else if (data == "failed") {
				model.pickList.status = "failed";
			}
			break;
		case "file.picture.list":
			model.editor.pictures = [];
			if (data && data.result) {
				model.editor.pictures = data.result;
			}

			var scroll = angular.element(document.getElementById('EditLayout')).scope();
			var scrollPic = scroll.scrollPic;
            setTimeout(function(){
                scrollPic.refresh();
            },200);
			break;
		case "file.picture.upload.success":
			res.ui.root.itemSelected={};
			res.ui.root.itemSelected.background = "image";
			res.ui.root.itemSelected.picture = data.image;
			res.ui.root.dialog = "details";

			window.parent.res.ui.root.model.popup = "";

			var fileInput = document.getElementById('imagefileInput');
			fileInput.value="";
			var scroll = angular.element(document.getElementById('EditLayout')).scope();
			var scrollPic = scroll.scrollPic;
            setTimeout(function(){
                scrollPic.refresh();
            },200);
			break;
		case "file.picture.upload.failed":
			window.parent.res.ui.root.model.popup = "";
			break;
		default:
			alert("pickList: unknown message.event = " + event);
			break;
		}
	};

	res.model.isNumber = function(input) {
		if( typeof(input) != 'number' && typeof(input) != 'string' )
			return false;
		var reg = new RegExp(/^[0-9]+$/);
		if (!reg.test(input)) {
			return false;
		}
		return true;
	};

})();

/*
 * Data Model
 */
(function(){

	var numOfCategories = 8;
//	var numOfItems = 24*2;		// number of items per category
	var numOfItems = 24;		// number of items per category
	var numOfRows = 4;

	Editor = function(){
		this.title = "";
		this.indexEdit = undefined;
		this.selectedFile = undefined;
		this.selectedIndex = undefined;
		this.files = [];
//		this.pictures = ["", "vegetables/cucumber.jpg", "vegetables/lettuce.jpg", "vegetables/broccoli.jpg","vegetables/celery.jpg","vegetables/asparagus.jpg"];
		this.pictures = [];
	};
	ItemBlank = function(){
		this.itemId = "";
		this.background = "image";
		this.picture = "";
		this.description = { ja: "", en: "" };
		this.isblank=true;
	};
	Failure=function(){
		this.active= false,
		this.service= undefined,
		this.cause= undefined,
		this.description= undefined,
		this.retrying= false;
	};
	PickList = function(){
		this.doCanceled = false;
		this.OriginalItems = [];
		this.OriginalCategories = [];
		this.OriginalLayout=[];
//		this.imageURL = "/resUiConfig/custom/images/pickList";
		this.imageURL = "resTransaction/rest/uiconfig/custom/pickList/images/";
		this.items = [];
		this.categories = [];
		this.layout = [];
		this.lists = [];
		this.deployCategories = [];
		this.selectedCategory = undefined;
		this.deletedTabItems = [];

		var i, j, x, y;
		for (i = 0; i < numOfCategories; i++){
			this.categories[i] = { ja: "", en: "" };
//			this.layout.push([]);	// add one list
			this.layout[i] = [];	// empty columns
			x = y = 0;
			for (j = 0; j < numOfItems; j++){
				if (y == 0){
//					this.layout[i].push([]);	// add a column
					this.layout[i][x] = [];
				}
				this.layout[i][x][y] = new ItemBlank;
				y++;
				if (y > numOfRows - 1){
					y = 0;
					x++;
				}
			}
		}

		this.convertFromLists = function(){
			for (var i = 0; i < this.lists.length; i++){
				for (var j = 0; j < this.lists[i].length; j++){
					var listItem = this.lists[i][j];
					if (j >= 24) break;

					for (var k = 0; k < this.items.length; k++){
						/*var item = this.items[k];
						if ((item.itemId == listItem.itemId &&
								item.description.ja == listItem.description.ja&&
								item.description.en == listItem.description.en)) {
							item.description.ja = (listItem.description.ja || listItem.description.jp);
							break;
						}*/

						if ((this.items[k].itemId == this.lists[i][j].itemId &&
								this.items[k].description.jp == this.lists[i][j].description.jp &&
								this.items[k].description.en == this.lists[i][j].description.en) ||
								this.lists[i][j].itemId == "") {
								break;
						}
					}

					if (k == this.items.length && listItem.itemId != "") {
						this.items[k] = {
							itemId: listItem.itemId,
							background: (listItem.background)? listItem.background : "image",
							picture: listItem.picture,
							description: listItem.description,
						}
					}
					if (listItem.itemId != "") {
						this.layout[i][Math.floor(j/4)][j%4] = this.items[k];
					}
				}
			}
		},

		this.convertToLists = function(){
			this.lists = [];
			for (var i = 0; i < this.layout.length; i++){	// categories
				var last = 0;
				for (var x = 0; x < this.layout[i].length; x++){	// columns
					for (var y = 0; y < this.layout[i][x].length; y++){	// rows
						if (this.layout[i][x][y].itemId){
							if (!this.lists[i]) this.lists[i] = [];
							this.lists[i][x*4+y] = {
								itemId: this.layout[i][x][y].itemId,
								background: this.layout[i][x][y].background,
								picture: this.layout[i][x][y].picture,
								description: this.layout[i][x][y].description,
							};
							last = this.lists[i].length;
						} else {
							if (!this.lists[i]) this.lists[i] = [];
							this.lists[i][x*4+y] = {
								itemId: "",
								background: "image",
								picture: "",
								description: { ja: "", en: "" },
							};
						}
					}
				}
				this.lists[i] = this.lists[i].slice(0, last);
			}
		},

		this.locate = function(type){
			var i, category, x, y;
			for (i = 0; i <this.items.length; i++){
				this.items[i].locations = "";
			}
			for (category = 0; category < this.layout.length; category++){
				for (x = 0; x < this.layout[category].length; x++){
					for (y = 0; y <this.layout[category][x].length; y++){
						for (i = 0; i < this.items.length; i++){
							if (this.items[i].itemId != "" && this.items[i].itemId == this.layout[category][x][y].itemId) {
								break;
							}
						}
						if (i < this.items.length){
							var cat = this.categories[category][res.ui.root.language];
							this.items[i].locations += cat ? cat + "(" + (x * 4 + y + 1) + ") " : "(" + (x * 4 + y + 1) + ") ";
						}
					}
				}
			}
		};

		this.removeFromLayout = function(itemId){
			for (var i = 0; i < this.layout.length; i++){
				for(var j = 0; j < this.layout[i].length; j++){
					for(var k = 0; k < this.layout[i][j].length; k++){
						if(itemId == this.layout[i][j][k].itemId){
							this.layout[i][j][k]=new ItemBlank();
						}
					}
				}
			}
		};

		this.removeFromItems = function(itemId){
			for (var i = 0; i < this.items.length; i++){
				if (itemId == this.items[i].itemId){
					this.items.splice(i, 1);
					i--;
				}
			}
		};

		this.initItemFromItems = function(itemId) {

			for (var i = 0; i < this.items.length; i++) {
				if (itemId == this.items[i].itemId) {
					this.items[i].locations = "";
					this.items[i].picture = "";
				}
			}
		};

//		this.removeFromTabLayout = function(itemId) {
//
//			// Layout lists
//			for (var j = 0; j < this.layout[this.selectedCategory].length; j++) {
//				for (var k = 0; k < this.layout[this.selectedCategory][j].length; k++) {
//					if (itemId == this.layout[this.selectedCategory][j][k].itemId) {
//						this.layout[this.selectedCategory][j][k] = new ItemBlank();
//					}
//				}
//			}
//
//			// items
//			for (var i = 0; i < this.items.length; i++) {
//				if (itemId == this.items[i].itemId) {
//					this.items[i] = new ItemBlank();
//				}
//			}
//		};
	};

})();
