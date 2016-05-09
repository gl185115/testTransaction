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
				res.config.advertise = {};
				eval(data.result);
				if (res.config.advertise.folder) model.pickList.imageURL = res.config.advertise.folder;
				if (res.config.advertise.interval) model.editor.interval = res.config.advertise.interval;
				if (res.config.advertise.rules) {
				    //model.pickList.layout = res.config.advertise.rules;
				    var rules = res.config.advertise.rules,
				        layout = model.pickList.layout;
                        var i = layout.length;
                        for (j = 0; j < rules.length; j++){
                            if (j == 0) layout.push([]);
                                if (j == 5*(i+1)){
                                   i++;
                                   layout.push([]);
                                }
                            layout[i].push([]);
                            layout[i][j%5] = new ItemBlank();
                            layout[i][j%5].Part.fileName = rules[j].fileName.substring(0,rules[j].fileName.length-4);
                            layout[i][j%5].Full.fileName = rules[j].fileNameFullScreen.substring(0,rules[j].fileNameFullScreen.length-13);
                            layout[i][j%5].startOfDay = rules[j].start;
                            layout[i][j%5].endOfDay = rules[j].end;
                            var temp = rules[j].description;
                            temp = temp.substring(1, temp.length - 1);
                            var tempArray = temp.split(',');
                            layout[i][j%5].companyId = tempArray[0];
                            layout[i][j%5].companyName = tempArray[1];
                            layout[i][j%5].Tel = tempArray[2];
                            layout[i][j%5].adName = tempArray[3];
                            layout[i][j%5].description = tempArray[4];
                            layout[i][j%5].isblank = false;
                        }
				}
				
				res.ui.root.pickListDisableClose = false;
				model.editor.selectedFile = undefined;
				model.editor.selectedIndex = undefined;
				model.editor.indexEdit = "editLayout";
			}
			break;
		case "file.upload":
			if (data == "success") {
				model.pickList.status = "successful";
				//model.pickList.OriginalItems = angular.copy(model.pickList.items);
				//model.pickList.OriginalCategories = angular.copy(model.pickList.categories);
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

			break;
		case "file.picture.upload.success":
			res.ui.root.itemSelected={};
			res.ui.root.itemSelected.background = "image";
			res.ui.root.itemSelected.picture = data.image;
			res.ui.root.dialog = "";

			window.parent.res.ui.root.model.popup = "";

			var fileInput = document.getElementById('fileInput');
			fileInput.value="";
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

	var numOfCategories = 1;
//	var numOfItems = 24*2;		// number of items per category
	var numOfItems = 36;		// number of items per category
	var numOfRows = 9;

	Editor = function(){
		this.title = "";
		this.indexEdit = undefined;
		this.selectedFile = undefined;
		this.selectedIndex = undefined;
		this.files = [];
//		this.pictures = ["", "vegetables/cucumber.jpg", "vegetables/lettuce.jpg", "vegetables/broccoli.jpg","vegetables/celery.jpg","vegetables/asparagus.jpg"];
		this.pictures = [];
		this.interval = "";
	};
	ItemBlank = function(){
		this.Part = {
		        fileName : "",
		        picture : ""
		};
		this.Full = {
                fileName : "",
                picture : ""
        };
		this.startOfDay = "";
		this.endOfDay = "";
		this.companyName = "";
		this.adName = "";
		this.Tel = " ";
		this.companyId = " ";
		this.description = "";
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
		this.imageURL = res.config.baseURL + "rest/uiconfig/custom/advertise/images/";
		this.items = [];
		this.categories = [];
		this.layout = [];   //repeat list
		this.lists = [];
		this.deployCategories = [];
		this.selectedCategory = undefined;
		this.deletedTabItems = [];
		this.sizeType = 0;

		var i, j, x, y;
		/*for (i = 0; i < numOfCategories; i++){
			this.categories[i] = { ja: "", en: "" };
//			this.layout.push([]);	// add one list
			this.layout[i] = [];	// empty columns
			x = y = 0;
			for (j = 0; j < numOfItems; j++){
				if (y == 0){
//					this.layout[i].push([]);	// add a column
					this.layout[i][x] = [];
				}
				this.layout[x][y] = new ItemBlank;
				y++;
				if (y > numOfRows - 1){
					y = 0;
					x++;
				}
			}
		}*/

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
