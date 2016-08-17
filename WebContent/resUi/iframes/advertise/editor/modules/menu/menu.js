/*
 * Controller menu
 */
res.ui.controller("menu", ["$scope", "$rootScope", function($scope, $rootScope){

	$scope.$watch("$routeChangeSuccess", function() {
		$rootScope.pickListDisableClose = true;
		$rootScope.pickListDisableOpen = false;
	});

	$scope.exit = function() {
		if ($rootScope.itemSelected) {
			if ($rootScope.itemSelected.isblank) {
				$rootScope.model.failure.active = true;
				$rootScope.model.failure.service = "advertise";
				$rootScope.model.failure.cause = "blank";
				return;
			}
		}

		window.parent.res.main.page("home");
	};

	$scope.create = function() {
		if ($rootScope.pickListDisableOpen) {
			return;
		}
		$rootScope.dialog = "fileCreate";
	};

	$scope.open = function() {
		if ($rootScope.pickListDisableOpen) {
			return;
		}
		if ($rootScope.model.editor.selectedFile) {
			res.ui.send({
				context : res.ui.root.context,
				event : "file.download",
				data : {
					folder : "advertise",
					file : $rootScope.model.editor.selectedFile
				}
			});
			$rootScope.pickListDisableOpen = true;
		}
	};

	$scope.save = function() {
		if ($rootScope.pickListDisableClose) {
			return;
		}
		if (isNaN($rootScope.model.editor.interval)){
            $rootScope.model.failure.active = true;
            $rootScope.model.failure.service = "advertise";
            $rootScope.model.failure.cause = "InputError";
            return;
        }
		if ($rootScope.model.editor.interval > 60 || $rootScope.model.editor.interval < 1) {
		    $rootScope.model.failure.active = true;
            $rootScope.model.failure.service = "advertise";
            $rootScope.model.failure.cause = "timeError";
            return;
		}
		$rootScope.dialog = "fileSave";
	};

	$scope.close = function() {
		if ($rootScope.pickListDisableClose) {
			return;
		}
		$rootScope.dialog = "fileClose";
	};

	$scope.remove = function() {
		var pickListResource = $scope.getPickListResource();
		if (pickListResource == null) {
			$rootScope.dialog = "fileRemove";
		} else {
			var advertise = resetPickListEffective(pickListResource);
			if ($scope.isCanRemove(advertise)) {
				$rootScope.dialog = "fileRemove";
			} else {
				$rootScope.model.failure.active = true;
				$rootScope.model.failure.service = "advertise";
				$rootScope.model.failure.cause = "scheduled";
			}
		}
	};

	$scope.getPickListResource = function() {
		if (!res.ui.root.model.schedule) return null;

		var dataDeploy = res.ui.root.model.schedule.deploy;
		if (!dataDeploy && dataDeploy.length == 0) return null;

		for (var i = 0; i < dataDeploy.length; i++) {
			if (!dataDeploy[i].company) continue;
			if (dataDeploy[i].company.id != window.parent.res.ui.root.model.companyId) continue;
			if (!dataDeploy[i].config && dataDeploy[i].config.length == 0) return null;

			for (var j = 0; j < dataDeploy[i].config.length; j++) {
				if (dataDeploy[i].config[j].resource == "advertise") {
					return dataDeploy[i].config[j].task;
				}
			}
		}

		return null;
	};

	$scope.isCanRemove = function(pickListResources) {
		if (!pickListResources) return true;

		var pickListFolder = [ "scheduled", "effective", "obsolete" ];
		for (var i = 0; i < pickListFolder.length; i++) {
			if (pickListFolder[i] == "obsolete") continue;

			var tasks = pickListResources[pickListFolder[i]];
			for (var i = 0; i < tasks.length; i++) {
				if (!tasks) continue;

				if (!$rootScope.model.editor.selectedFile.endsWith(".js")) {
					$rootScope.model.editor.selectedFile += ".js";
				}

				if ($rootScope.model.editor.selectedFile == tasks[i].filename) {
					return false;
				}
			}
		}

		return true;
	};

	$scope.edit = function(section) {
		if ($rootScope.pickListDisableClose) {
			return;
		}
		if ($rootScope.itemSelected) {
			if ($rootScope.itemSelected.isblank) {
				$rootScope.model.failure.active = true;
				$rootScope.model.failure.service = "advertise";
				$rootScope.model.failure.cause = "blank";
				return;
			}
		}

		$rootScope.model.editor.indexEdit = section;
	};

	$scope.filePrint = function() {
		if ($rootScope.pickListDisableClose) {
			return;
		}
		var divObj = document.getElementById("PickUpFilePrint");
		var printWindow = window.open("", "_blank", "innerHeight = 600,innerWidth = 800, scrollbars = yes");
		printWindow.document.write("<!DOCTYPE html>");
		printWindow.document.write("<html><head>");
		printWindow.document.write("<link  href='modules/filePrint/filePrint.css' rel='stylesheet' type='text/css' />");
		printWindow.document.write("</head>");
		printWindow.document.write(divObj.innerHTML);
		printWindow.document.write("</html>");

		var doPrint = function() {
			// Finish initialization.
			printWindow.print();
			printWindow.document.close();
			printWindow.close();
		};

		setTimeout(doPrint, 500);
	};

	resetPickListEffective = function(tasks) {
		if (!tasks) return;
		tasks.sort(function(task1, task2) {
			return (new Date(task2.effective)) - (new Date(task1.effective));
		});

		var pickListResources = [];
		var effectiveSample = tasks;
		var indexTask;
		if (effectiveSample.length <= 1) return;

		pickListResources = { "scheduled" :[], "effective":[], "obsolete":[] };
		for (var k = 0; k < effectiveSample.length; k++) {
			indexTask = effectiveSample[k];
			if (indexTask == undefined || indexTask.target == undefined) continue;
			var iTarget = indexTask.target;

			var today = new Date();
			var date = this.getEffectiveDate(indexTask.effective);

			if (date <= today) {
				folder = "effective";
			} else if (date > today) {
				folder = "scheduled";
			}

			pickListResources[folder].push(indexTask);
		}

		effectiveSample = pickListResources["effective"];
		for (var k = 0; k < effectiveSample.length; k++) {
			indexTask = effectiveSample[k];
			if (indexTask == undefined || indexTask.target == undefined) continue;
			var iTarget = indexTask.target;
			if (iTarget.store.toLowerCase() == "all" || iTarget.store == "全店舗") {
				for (var j = k + 1; j < effectiveSample.length; j++) {
					pickListResources["obsolete"].push(effectiveSample[j]);
					pickListResources["effective"].splice(j, 1);
					--j;
					}
				break;

			} else if (iTarget.group) {
				/*
				 * 1. Select active group's store entries
				 * 2. Delete store task when if it contains by group
				 */
				var groupEntries = [];
				var storeEntries = [];
				var deployCategories = res.ui.root.model.advertise.deployCategories;
				for (var x = 0; x < deployCategories.length; x++) {
					if (deployCategories[x].levelKey == "group") {
						var iGroup = deployCategories[x].storeEntries;
						for (var y = 0; y < iGroup.length; y++) {
							if (iTarget.group == iGroup[y].entryNameJa) {
								groupEntries = iGroup[y].entryStores;
								break;
							}
						}
					} else if (deployCategories[x].levelKey == "store") {
						storeEntries = deployCategories[x].storeEntries;
					}
				}

				for (var m = 0; m < groupEntries.length; m++) {
					for (var n = 0; n < storeEntries.length; n++) {
						if (groupEntries[m] == storeEntries[n].storeId) {
							for (var d = k + 1; d < effectiveSample.length; d++) {
								var deleteTask = effectiveSample[d];
								if (!(deleteTask.target.store.toLowerCase() == "all" || deleteTask.target.store == "全店舗")
										&& deleteTask.target.store == storeEntries[n].entryNameJa) {
									pickListResources["obsolete"].push(effectiveSample[d]);
									pickListResources["effective"].splice(d, 1);
									--d;
								}
							}
							break;
						}
					}
				}
			}
		}

		tasks.sort(function(task1, task2){
			return (new Date(task2.effective)) - (new Date(task1.effective));
		});

		var effectiveFolder = effectiveSample;
			var hash = {};
			var type = "";

			for (var i = 0; i < effectiveFolder.length; i++) {
			indexTask = effectiveFolder[i];
			if (indexTask.target.group) {
				type = indexTask.target.group;
			} else {
				type = indexTask.target.store;
			}

			if (hash[type] !== 1) {
				hash[type] = 1;
			} else {
				pickListResources["obsolete"].push(effectiveFolder[i]);
				pickListResources["effective"].splice(i, 1);
				--i;
			}
		}

		return pickListResources;
	};

	getEffectiveDate = function(effective) {
		var today = new Date();
		var effectiveDate = new Date(effective);
		if (effectiveDate) {
			effectiveDate.setHours(effectiveDate.getHours() + ((new Date()).getTimezoneOffset() / 60) + 9);
		}
		return effectiveDate;
	};

}]);

