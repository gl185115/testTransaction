/*** c8: start of update ***/
var res = res || {};
res.service = res.service || {};
/*** c8: start of update ***/

/*
 * File Service
 */

$(document).ready(function() {
    res.service.settings = new Service_settings();
    res.service.provider = new Provider_settings();
});

(function() {
    var $ = jQuery;
    // Constructor
    Service_settings = function() {

    };

    Provider_settings = function() {

    };

    Provider_settings.prototype = {
        profile: function(data, callback) {
            $.ajax({
                type: 'POST',
                url: res.config.baseURL + "setprofile",
                data: {
                    'workstationid': data.workstationid,
                    'companyid': data.companyid,
                    'storeid': data.storeid
                },
                datatype: 'json',
                success: function(data) {
                    callback.success(data);
                },
                error: function(data) {
                    callback.error(data);
                }
            });
        },
        getSoftwareVersionsService: function(callback) {
            $.ajax({
                type: 'GET',
                url: res.config.baseURL + 'softwareinfo/' + 'getallsoftwareinfo',
                data: {},
                datatype: 'json',
                success: function(data) {
                    var dataInfo = JSON.stringify(data);
                    callback.success(dataInfo);
                },
                error: function(code) {
                    callback.error(code);
                }
            });
        }
    }

    Service_settings.prototype = {
    	profile: function(data, callback) {
            $.ajax({
                type: 'POST',
                url: res.config.baseURL + "setprofile",
                data: {
                    'workstationid': data.workstationid,
                    'companyid': data.companyid,
                    'storeid': data.storeid
                },
                datatype: 'json',
                success: function(data) {
                    callback.success(data);
                },
                error: function(data) {
                    callback.error(data);
                }
            });
        },

        getTransactionInfo: function() {
            var deferred = $.Deferred();
            res.service.provider.getSoftwareVersionsService({
                success: function(data) {
                    var jsonResult = JSON.parse(data);
                    deferred.resolve(jsonResult);
                },
                error: function(code) {
                    deferred.reject(code);
                }
            });
            return deferred.promise();
        },
        getSoftwareVersions: function(callback) {
            var versionName, webStoreVersion, tomcatVersion, j2EEversion, osVersion;
            var dataResult = {},
                combinedPromise,
                requestList = [
                    this.getTransactionInfo()
                ];
            combinedPromise = $.when.apply(this, requestList);
            combinedPromise.done(function(transactionInfoResponse) {
                if (transactionInfoResponse) {
                    if (transactionInfoResponse.ServiceInfo) {
                        webStoreVersion = transactionInfoResponse.ServiceInfo.Version;
                    }
                    if (transactionInfoResponse.ServerInfo) {
                        tomcatVersion = transactionInfoResponse.ServerInfo.Version;
                    }
                    if (transactionInfoResponse.JavaInfo) {
                        j2EEversion = transactionInfoResponse.JavaInfo.Version;
                    }
                    if (transactionInfoResponse.OperatingSystemInfo) {
                        osVersion = transactionInfoResponse.OperatingSystemInfo.Version;
                    }
                }

                dataResult = {
                    webStore: webStoreVersion,
                    tomcat: tomcatVersion,
                    j2EE: j2EEversion,
                    operatingSystem: osVersion,
                };
                callback.success(dataResult);
            });

        }
    }

})();