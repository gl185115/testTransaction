/*
 * Software Configuration
 */
res.ui.controller("software", ["$scope", "$rootScope",
    function($scope, $rootScope) {

        $scope.restore = function() {
            window.parent.location.reload();
        };
        $scope.close = function() {
            window.parent.res.main.close();
        };
        $scope.getSWConfig = function() {
            var model = $rootScope.model;
            var userAgent = window.navigator.userAgent.toLowerCase();
            if (userAgent.indexOf("ipad") != -1) {
                model.software.client.container = "Safari?";
                model.software.client.operatingSystem = "iOS";
            }
            if (userAgent.indexOf("chrome") != -1) {
                model.software.client.container = "Google Chrome";
            };
            res.ui.send({
                event: "settings.getSoftwareVersions"
            });
        };
        $scope.getSWConfig();

    }
]);