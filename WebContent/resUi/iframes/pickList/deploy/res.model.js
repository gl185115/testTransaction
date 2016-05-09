var res = res || {};
res.model = res.model || {};

/*
 *  Data Model for Deployment Schedule
 */
(function() {

    res.model.init = function() {
        res.ui.root.model = new Model();
        res.ui.root.model.active.resource = "pickList";
        res.ui.root.model.active.folder = "scheduled";
    };

    res.model.update = function(event, data) {
        var model = res.ui.root.model;
        switch (event) {
        case "deploy.pickList.getDeployStoreAndGroup.successful":
        case "deploy.pickList.getDeployStoreAndGroup.failed":
            if (data != undefined || data != null) {
                model.editTask.deployCategories = data;
            }

            if (model.doRefreshSchedule) {
                res.ui.send({
                    context : res.ui.context,
                    event : "deploy.pickList.getSchedule",
                    data : {
                        resource : "pickList"
                    }
                });
            } else {
                model.doRefreshSchedule = true;
            }
            break;
        case "deploy.pickList.getSchedule.successful":
        case "deploy.pickList.getSchedule.failed":
            model.schedule = data.schedule;
            model.load();

            res.ui.send({
                context : res.ui.context,
                event: "deploy.pickList.getDeployStatus",
                data: {
                    companyID : res.storage.getItem("CompanyID"),
                    resource : "pickList"
                }
            });

            break;
//      case "deploy.pickList.getSchedule.failed":
//          alert("Failed to load schedule xml file!");
//          break;
        case "deploy.pickList.setSchedule.successful":
            if (model.message == "SaveInvoke") {
                window.parent.res.ui.root.model.popup = "Wait";
                res.ui.send({
                    context : res.ui.context,
                    event : "deploy.pickList.invokeTransfer",
                    data : {
                        transferOption : res.ui.root.model.active.resource
                    }
                });
                model.message = "";
                break;
            }
            model.saveFinished = true;
            break;
        case "deploy.pickList.setSchedule.failed":
            model.message = "Save.Failed";
            res.ui.root.dialog = "feedback";
            break;
        case "deploy.pickList.getDeployStatus.successful":
            model.deployLogsBtn = false;
            window.parent.res.ui.root.model.popup = "";
            model.deployStatus = data;
            model.update();
            break;
        case "deploy.pickList.getDeployStatus.failed":
            model.deployLogsBtn = false;
            window.parent.res.ui.root.model.popup = "";
            break;
        case "deploy.pickList.getDeployDetail.successful":
            model.deployDetail.detailData = data.storeEntries;
            if (model.deployDetail.detailData.length >= 1 && model.active.taskIndex != undefined) {
                model.deployDetail.detailView = true;
            }
            break;
        case "deploy.pickList.getDeployDetail.failed":
            model.deployLogsBtn = false;
            break;
        case "deploy.pickList.saveDeployDetail.successful":
        case "deploy.pickList.saveDeployDetail.failed":
            model.saveDetail.returnValue = data;
            if (model.saveDetail.returnValue == "1") {
                model.saveDetail.returnValue = "success";
            } else if (model.saveDetail.returnValue == "0") {
                model.saveDetail.returnValue = "exist";
                model.saveDetail.existNotice = true;
            } else if (model.saveDetail.returnValue = "-1") {
                model.saveDetail.returnValue = "error";
            }

            model.saveDetail.isOverwrite = false;
            res.ui.root.dialog = "saveDetail";
            break;
        case "deploy.pickList.viewTableStore.failed":
        case "deploy.pickList.viewTableStore.successful":
            if (model.editStore.actionType == "StoreUpdate") {
                model.pageIndex ='StoreEntryDetail';
                    model.storesCsv = data.storeEntries;
                    model.storesCsvBak = data.storeEntries;
                    model.searchFlag = false;

            } else if (model.editStore.actionType == "GroupCreate") {
                model.storesAllIdCsv = data.storeEntries;
                    model.pageIndex ='StoreGroupCreate';

            } else if (model.editStore.actionType == "GroupUpdate") {
                model.storesAllIdCsv = data.storeEntries;
                    model.pageIndex ='StoreGroupCreate';
                    if (model.storesGroupCsv == undefined || model.storesAllIdCsv == undefined) break;

                    var selectedIndex = model.storesGroupCsv.selectIndex;
                    if (model.storesGroupCsv.length >= selectedIndex && selectedIndex >= 0) {
                            model.editStore.groupEntryNameJa = model.storesGroupCsv[selectedIndex].entryNameJa;

                        var groupEntryStoreIdList = model.storesGroupCsv[selectedIndex].entryStores;
                        var allStores = model.storesAllIdCsv;
                        var x = 0;
                        for(var i = 0 ; i < groupEntryStoreIdList.length ; i++){
                            for(var j = 0 ; j < allStores.length ; j++){
                                if(groupEntryStoreIdList[i] == allStores[j].storeId){
                                    res.ui.root.model.storesGroupSelectedId[x] = allStores[j];
                                    res.ui.root.model.storesAllIdCsv.splice(j,1);
                                    x = x + 1;
                                    break;
                                }
                            }
                        }
                    }
            }
            break;
        case "deploy.pickList.editTableStore.failed":
        case "deploy.pickList.editTableStore.successful":
            model.resultCode = data.status;
            switch (model.resultCode) {
            case "success":
                res.ui.root.prompt = "success";
                res.ui.send({
                    context : res.ui.context,
                    event: "deploy.pickList.getDeployStoreAndGroup",
                    data: { companyID : res.storage.getItem("CompanyID") }
                });
                model.doRefreshSchedule = false;
                break;
            case "exist":
                res.ui.root.prompt = "exist";
                break;
            case "failed":
                res.ui.root.prompt = "failed";
                break;
            default:
                res.ui.root.prompt = "failed";
            }
            res.ui.root.popupMessage = "popupMessage";
            break;
        case "deploy.pickList.viewTableGroup.successful":
        case "deploy.pickList.viewTableGroup.failed":
            model.storesGroupCsv = data.storeEntries;
            break;
        case "deploy.pickList.editTableGroup.failed":
        case "deploy.pickList.editTableGroup.successful":
            model.resultCode = data.status;

            switch(model.resultCode){
            case "success":
                res.ui.root.prompt = "success";
            res.ui.send({
                context : res.ui.context,
                event: "deploy.pickList.getDeployStoreAndGroup",
                data: { companyID : res.storage.getItem("CompanyID") }
            });
                model.doRefreshSchedule = false;
                break;
            case "exist":
                res.ui.root.prompt = "exist";
                break;
            case "failed":
                res.ui.root.prompt = "failed";
                break;
            default:
                res.ui.root.prompt = "failed";
            }

            res.ui.root.popupMessage = "popupMessage";
            break;
        case "deploy.pickList.getSysFileTree.successful":
        case "deploy.pickList.getSysFileTree.failed":
            if (!data || !data.sysUserHome) {
                model.fileExplorer.isFirstNode = true;
                model.fileExplorer.isLastNode = false;
                model.fileExplorer.baseFolder = "";
                break;
            }

            model.fileExplorer.isFirstNode = data.nodeFirst;
            model.fileExplorer.isLastNode = data.nodeLast;
            model.fileExplorer.baseFolder = data.baseFolder;
            model.fileExplorer.systemDisk = data.sysUserHome;

            if (data.currentFolder && data.currentFolder.length > 0) {
                model.fileExplorer.fileTree.length = 0;
                for (var i = 0; i < data.currentFolder.length; i++) {
                    model.fileExplorer.fileTree.push({ filename: data.currentFolder[i], selected: undefined });
                }
            }
            break;
        case "deploy.pickList.invokeTransfer.successful":
        case "deploy.pickList.invokeTransfer.failed":
            model.resultCode = data.status;
            switch(model.resultCode){
            case "success":
                window.parent.res.ui.root.model.popup = "";
                model.saveFinished = true;
                if (!window.parent.res.ui.root.$$phase) window.parent.res.ui.root.$digest();
                break;
            case "failed":
            case "error":
                model.message = "InvokeTransfer.Failed";
                window.parent.res.ui.root.model.popup = "";
                if (!window.parent.res.ui.root.$$phase) window.parent.res.ui.root.$digest();
                res.ui.root.dialog = "feedback";
                break;
            }

            break;
        case "file.list":
            model.editor.files = [];

            if (Array.isArray(data)) {
                var files = data;
            } else {
                var files = [];
            }

            for (var i = 0; i < files.length; i++){
                if (result = files[i].FileName.match(/(.+)\.js$/i)){
                    model.editor.files.push(result[1]);
                }
            }
            break;
        default:
            res.console("res.ui deploy model: unknown message.event = " + event);
            break;
        }
    };

    res.model.isNumber = function(input) {
        var reg = new RegExp(/^[0-9]*$/);
        if (!reg.test(input)) {
            return false;
        }
        return true;
    };

    res.model.parseHHMM = function(time) {
        if (time.toString().length < 2) {
            time = "0" + time;
        }
        return time;
    };

})();

/*
 * Data Model
 */
(function() {

    Deploy = function(id, name) {
        this.company = {
            id: id,
            name: name
        };
//      this.ID = "";
        config = []; // array of Config()
    };

    Config = function(resource) {
        this.resource = resource; // example: "pickList", "notices", "options", "usability"
        this.task = []; //  { target: { store:"All", workstation:"All" }, effective:"2014-12-31T00:00", filename:"abc.js" }
    };

    Task = function(target, effective, filename) {
        this.target = target;
        this.effective = effective;
        this.filename = filename;
    };

    Folders = function() {
        this.effective = []; // array of tasks { task:task, status:{total: 999, ready:999, opened:999}}
        this.scheduled = []; // array of tasks
        this.obsolete = [];  // array of tasks
    };

    Model = function() {
        this.searchFlag = false;
        this.schedule = undefined; // original object converted from JSON from the server
        this.index = undefined;    // current companyID's config under this.schedule
        this.active = {
            resource: undefined,
            folder: undefined,
            taskIndex: undefined,
            item:undefined
        };
        this.resources = {
            pickList: undefined  // new Folders(),
//          notices: undefined,  // new Folders(),
//          options: undefined,  // new Folders(),
//          usability: undefined // new Folders(),
        };
//      this.directory = ["pickList", "notices"];
        this.directory = ["pickList"];
        this.folders = ["scheduled", "effective", "obsolete"];
        this.editor = {
            files: [],
        };
        this.message = undefined;
        this.stores = [];
        this.storesCsv=[];
        this.storesCsvBak=[];
        this.OriginalStoresCsv=[];
        this.storesAllIdCsv=[];
        this.storesGroupSelectedId=[];
        this.storesGroupCsv = [];
        this.pageIndex=undefined;
        this.deployLogsBtn = false;
        this.deployDetail = {
            detailView: false,
            detailData: [],
            detailInit: []
        };
        this.noticesData = undefined;
        this.doRefreshSchedule = true;

        this.editTask = {
            deployCategories : []
        }

        this.editStore = {
            actionType : "",
            groupEntryNameJa : ""
        };

        this.fileExplorer = {
            fileTree : [],
            indexSelected : -1,
            indexFilePath : "",
            selectedFolder : "",
            csvSavePath : "",
            inputFileName : "",
            baseFolder : "",
            isFirstNode : false,
            isLastNode : false,
            systemDisk : "C",
        };

        this.saveDetail = {
            returnValue : undefined,
            isOverwrite : false,
            existNotice : false,
            isEmptyName : false,
            inValidName : false,
            isLengthOver : false,
        }

    };

    Model.prototype = {
        clear: function() {
            for (var key in this.resources) {
                this.resources[key] = new Folders();
            }
        },

        load: function() {
            var company = res.storage.getItem("CompanyID");
            this.clear();

            if (this.schedule && this.schedule.deploy) {
                for (var i = 0; i < this.schedule.deploy.length; i++) {
                    if (this.schedule.deploy[i].company.id == company) {
                        this.schedule.deploy[i].company.name = "XEBIO";
                        break;
                    }
                }
                if (i == this.schedule.deploy.length) {
                    res.console("res.ui deploy model: config for companyID: " + res.config.companyID + " is newly created.");
                    this.schedule.deploy[i] = new Deploy(company);
                }
                this.index = i;

                for (var i = 0; this.schedule.deploy[this.index].config && i < this.schedule.deploy[this.index].config.length; i++) {
                    var resource = this.schedule.deploy[this.index].config[i].resource;
                    if (resource == "pickList") {
                        var tasks = this.schedule.deploy[this.index].config[i].task;
                        if (tasks && tasks.length > 0) {
                            for (var j = 0; j < tasks.length; j++) {
                                this.add(tasks[j], null, resource);
                            }
                        }
                    }
                }

                this.resetPickListEffective();
                this.initObsoleteMaxDeploy();

                for (key in this.resources) {
                    this.sort(key);
                }
            }

//          this.resetPickListEffective();
//          this.initObsoleteMaxDeploy();
//
//          for (key in this.resources) {
//              this.sort(key);
//          }
        },

        save: function() {
            if (!this.schedule || !this.schedule.deploy || !this.resources) return;

            this.schedule.deploy[this.index].config = [];
            var n = 0;
            for (var resource in this.resources) {
                this.schedule.deploy[this.index].config[n] = new Config(resource);
                for (var folder in this.resources[resource]) {
                    for (var i = 0; i < this.resources[resource][folder].length; i++) {
                        this.schedule.deploy[this.index].config[n].task.push(this.resources[resource][folder][i].task);
                    }
                }
                n++;
            }
        },

        add: function(task, status, resource) {
            if (!task || !task.target) return;

            if (!resource) resource = this.active.resource;
            if (!status) {
                var status = { total: 0, ready:0, opened:0 };
                this.initTaskTargetStatus(task, status);
            }

            var today = new Date();
            var date = this.getEffectiveDate(task.effective);

            if (date <= today) {
                folder = "effective";
            } else if (date > today) {
                folder = "scheduled";
                status.opened = 0;
                status.ready = status.total - status.opened;
            }

            if (task.effective && task.filename) {
                var set = { task: task, status: status };
                this.resources[resource][folder].push(set);
            }
        },

        remove: function(taskIndex) {
            if (!taskIndex) taskIndex = this.active.taskIndex;
            var task = this.resources && this.resources[this.active.resource] && this.resources[this.active.resource][this.active.folder];
            task.splice(taskIndex, 1);
            this.active.taskIndex = undefined;
        },

        sort: function(resource) {
            if (!resource) resource = this.active.resource;
            for (folder in this.resources[resource]) {
                this.resources[resource][folder].sort(compare);
            }

            function compare(set1, set2) {
                // DESC
                return (new Date(set2.task.effective)) - (new Date(set1.task.effective));
            }
        },

        getEffectiveDate: function(effective) {
            if (!effective) return;

            var today = new Date();
            if (effective.indexOf("T") != -1) {
                var effectiveDate = new Date(effective.replace("T", " "));
            } else {
                var effectiveDate = new Date(effective);
            }

            if (effectiveDate) {
                effectiveDate.setHours(effectiveDate.getHours() + ((new Date()).getTimezoneOffset() / 60) + 9);
            }

            return effectiveDate;
        },

        update: function() {
            var taskOld = undefined;
            var taskNew = undefined;
            var statusOld = undefined;
            var toUpdate = false;

            if (!this.deployStatus || this.deployStatus.length == 0) return;
            for (var resource in this.resources) {
                for (var folder in this.resources[resource]) {
                    for (var i = 0; i < this.resources[resource][folder].length; i++) {
                        taskOld = this.resources[resource][folder][i].task;
                        statusOld = this.resources[resource][folder][i].status;
                        if (statusOld.total <= 0) continue;

                        for (var j = 0; j < this.deployStatus.length; j++) {
                            taskNew = this.deployStatus[j];

                            if (taskOld.effective == taskNew.effective && taskOld.filename == taskNew.filename) {
                                var targetOld = taskOld.target;
                                if ((targetOld.store.toLowerCase() == "all" || targetOld.store.toLowerCase() == "全店") && taskNew.taskType == "all") {
                                    toUpdate = true;
                                } /*else if (targetOld.group && taskNew.taskType == "group") {
                                    if (targetOld.group == taskNew.taskEntryName) {
                                        toUpdate = true;
                                    }
                                } */else if (!(targetOld.store.toLowerCase() == "all" || targetOld.store.toLowerCase() == "全店") && taskNew.taskType == "store") {
                                    if (targetOld.store == taskNew.taskEntryName) {
                                        toUpdate = true;
                                    }
                                }

                                if (toUpdate = true) {
                                    if (parseInt(taskNew.opened) <= parseInt(statusOld.total)) {
                                        statusOld.opened = taskNew.opened;
                                    } else {
                                        statusOld.opened = statusOld.total;
                                    }
                                    statusOld.ready = statusOld.total - statusOld.opened;
                                    toUpdate = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        },

        initObsoleteMaxDeploy : function() {
            for (var resource in this.resources) {
                if (this.resources[resource]["obsolete"].length > res.config.maxObsoleteDeploy) {
                    var firstTask = this.resources[resource]["obsolete"][0].task;
                    var lastTask = this.resources[resource]["obsolete"][res.config.maxObsoleteDeploy - 1].task;
                    var mixDate = this.getEffectiveDate(firstTask.effective);
                    var maxDate = this.getEffectiveDate(lastTask.effective);

                    if (mixDate <= maxDate) {
                        this.resources[resource]["obsolete"] = this.resources[resource]["obsolete"].slice(
                                    this.resources[resource]["obsolete"].length - res.config.maxObsoleteDeploy);
                    } else {
                        this.resources[resource]["obsolete"] = this.resources[resource]["obsolete"].slice(0,
                                    res.config.maxObsoleteDeploy);
                    }
                }
            }
        },

        resetPickListEffective : function() {
            if (!this.resources["pickList"]["effective"]) return;
            if (this.resources["pickList"]["effective"].length <= 1) return;

            this.resources["pickList"]["effective"].sort(function(task1, task2) {
                return (new Date(task2.task.effective)) - (new Date(task1.task.effective));
            });

            var effectiveSample = this.resources["pickList"]["effective"];
            var indexTask;

            for (var k = 0; k < effectiveSample.length; k++) {
                indexTask = effectiveSample[k].task;
                if (indexTask == undefined || indexTask.target == undefined) continue;
                var iTarget = indexTask.target;

                if (iTarget.store.toLowerCase() == "all" || iTarget.store == "全店") {
                    for (var j = k + 1; j < effectiveSample.length; j++) {
                        this.resources["pickList"]["obsolete"].push(effectiveSample[j]);
                        this.resources["pickList"]["effective"].splice(j, 1);
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
                    var deployCategories = res.ui.root.model.editTask.deployCategories;
                    for (var x = 0; x < deployCategories.length; x++) {
                        /*if (deployCategories[x].levelKey == "group") {
                            var iGroup = deployCategories[x].storeEntries;
                            for (var y = 0; y < iGroup.length; y++) {
                                if (iTarget.group == iGroup[y].entryNameJa) {
                                    groupEntries = iGroup[y].entryStores;
                                    break;
                                }
                            }
                        } else*/ if (deployCategories[x].levelKey == "store") {
                            storeEntries = deployCategories[x].storeEntries;
                        }
                    }

                    for (var m = 0; m < groupEntries.length; m++) {
                        for (var n = 0; n < storeEntries.length; n++) {
                            if (groupEntries[m] == storeEntries[n].storeId) {
                                for (var d = k + 1; d < effectiveSample.length; d++) {
                                    var deleteTask = effectiveSample[d].task;
                                    if (!(deleteTask.target.store.toLowerCase() == "all" || deleteTask.target.store == "全店")
                                            && deleteTask.target.store == storeEntries[n].entryNameJa) {
                                        this.resources["pickList"]["obsolete"].push(effectiveSample[d]);
                                        this.resources["pickList"]["effective"].splice(d, 1);
                                        --d;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }

            this.resources["pickList"]["effective"].sort(function(task1, task2) {
                return (new Date(task2.task.effective)) - (new Date(task1.task.effective));
            });

            var effectiveFolder = this.resources["pickList"]["effective"];
            var hash = {};
            var type = "";

            for (var i = 0; i < effectiveFolder.length; i++) {
                indexTask = effectiveFolder[i].task;
                /*if (indexTask.target.group) {
                    type = indexTask.target.group;
                } */if (indexTask.target.store) {
                    type = indexTask.target.store;
                }

                if (hash[type] !== 1) {
                    hash[type] = 1;
                } else {
                    this.resources["pickList"]["obsolete"].push(effectiveFolder[i]);
                    this.resources["pickList"]["effective"].splice(i, 1);
                    --i;
                }
            }
        },

        initTaskTargetStatus: function(task, status) {
            if (!task || !status) return;
            if (!task.target.store && !task.target.group) return;

            if (task.target.store && (task.target.store.toLowerCase() == "all" || task.target.store == "全店")) {
                status.total = res.ui.root.model.editTask.deployCategories[0].storeEntries.length;
                task.target.storeNameJa = "全店";
            } else if (!(task.target.store && (task.target.store.toLowerCase() == "all" || task.target.store == "全店"))) {
                status.total = 1;
                var storeEntries = res.ui.root.model.editTask.deployCategories[1].storeEntries;
                for (var i = 0; i < storeEntries.length; i++) {
                    if (task.target.store == storeEntries[i].StoreId
                            || task.target.store == storeEntries[i].StoreId) {
                        task.target.storeNameJa = storeEntries[i].StoreName;
                    }
                }
            }
        },

    };
             

})();