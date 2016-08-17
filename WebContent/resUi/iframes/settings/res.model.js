var res = res || {};
res.model = res.model || {};

/*
 *  Data Model for 'Settings'
 */
(function() {

    res.model.init = function() {
        res.ui.root.model = new Model();
    };
    res.model.update = function(event, data) {
        var model = res.ui.root.model;
        switch (event) {
            case "device.deregister.successful":
                window.parent.location.href = "../../../resAuthenticate/";
                break;
            case "settings.training.setMode.successful":
                model.training = data;
                window.parent.res.bridge.set("root", "model.training", model.training);
                window.parent.res.main.training(model.training);
                break;
            case "settings.printers.getList.successful":
                model.printers = data;
                break;
            case "settings.queues.getList.successful":
                model.queues = data;
                break;
            case "settings.language.change.successful":
                window.parent.res.main.language(data);
                model.language = data;
                break;
            case "settings.profile.change.successful":
                break;
            case "settings.getSoftwareVersions.successful":
                model.software.client.contents = (typeof model.software.server.services != 'undefined') ? "NCR RES MEX 1 UI Configurator " + data.webStore : "unknown";
                model.software.server.applicationServer = data.tomcat || '';
                model.software.server.java = data.j2EE !== 'unknown' ? 'J2EE ' + data.j2EE : data.j2EE;
                model.software.server.operatingSystem = typeof(data.operatingSystem) !== 'undefined' ? data.operatingSystem : 'unknown';
                model.software.client.operatingSystem = data.operatingSystem || '';
                break;
            default:
                alert("settings: unknown message.event = " + event);
                break;
        }
    };

})();

/*
 * Data Model
 */
(function() {

    Model = function() {
        //      this.training = false;
        //      this.passcode = "";
        //      this.printers = [];
        //      this.queues = [];
        this.companyID = res.storage.getItem("CompanyID");
        this.storeID = res.storage.getItem("RetailStoreID");
        this.workstationID = res.storage.getItem("WorkstationID");
        this.language = res.ui.root.language;
        this.volume = undefined;
        this.cursorHidden = false;
        this.software = {
            client: {
                contents: 'unknown',
                container: "unknown",
                operatingSystem: "unknown",
            },
            server: {
                url: "",
                services: {
                    transaction: "not connected",
                    inventory: "not connected",
                    consumer: "not connected",
                },
                applicationServer: "unknown",
                java: "unknown",
                operatingSystem: "unknown",
            }
        };

    };
    Model.prototype = {};

})();