var res = res || {};
res.model = res.model || {};

/*
 *  Data Model for Home Page
 */
(function(){

    res.model.init = function() {
        res.ui.root.model = new Home();
    };

    res.model.update = function(event, data) {
        var model = res.ui.root.model;
        switch(event){
        case "deploy.advertise.invokeTransfer.successful":
        case "deploy.advertise.invokeTransfer.failed":
        case "deploy.pickList.invokeTransfer.successful":
        case "deploy.pickList.invokeTransfer.failed":
        case "deploy.notices.invokeTransfer.successful":
        case "deploy.notices.invokeTransfer.failed":
            model.resultCode = data.status;
            switch(model.resultCode){
            case "success":
                model.transfering = false;
                window.parent.res.ui.root.model.popup = "";
                if (!window.parent.res.ui.root.$$phase) window.parent.res.ui.root.$digest();
                //alert("Transfer invoke successfully");
                break;
            case "failed":
            case "error":
                model.transfering = false;
                window.parent.res.ui.root.model.popup = "";
                if (!window.parent.res.ui.root.$$phase) window.parent.res.ui.root.$digest();
                //alert("Transfer invoke failed");
                break;
            }

            break;
        default:
            alert("home: unknown message.event = " + event);
            break;
        }
    };

})();

/*
 * Data Model
 */
(function(){

    Home = function() {
        this.version = "UC " + res.config.version;
        this.applicationName = "かんたんPOS";
        this.transferOption = undefined;
    };

    Home.prototype = {};

})();
