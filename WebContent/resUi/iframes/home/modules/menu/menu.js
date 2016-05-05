/*
 * Main Menu
 */
res.ui.controller("menu", ["$scope", "$rootScope", function($scope, $rootScope) {

    $scope.popupMessage = "";

    $scope.page = function(choice) {
        window.parent.res.main.page(choice);

        switch (choice) {
        case "noticesEditor":
            res.ui.send({
                context : "Notice",
                event : "file.list",
                data : {
                    folder : "notices"
                }
            });
            break;
        case "pickListEditor":
            res.ui.send({
                context : "PickList",
                event : "file.list",
                data : {
                    folder : "pickList"
                }
            });
            break;
        case "pickListAlbum":
            res.ui.send({
                context : "PickList.Album",
                event : "file.picture.list",
                data : {
                    folder : "images/pickList"
                }
            });
            break;
        case "noticesAlbum":
            res.ui.send({
                context : "Notices.Album",
                event : "file.picture.list",
                data : {
                    folder : "images/notices"
                }
            });
            break;
        case "advertisementEditor":
            res.ui.send({
                context : "Advertise",
                event : "file.list",
                data : {
                    folder : "advertisement"
                }
            });
            break;
        case "advertisementAlbum":
            res.ui.send({
                context : "Advertise.Album",
                event : "file.picture.list",
                data : {
                    folder : "images/advertisement"
                }
            });
            break;
        default:
            break;
        }

    };

    $scope.demoSetUp = function() {
        // demo setup configuration
        $.ajax({
            type : 'POST',
            url : res.config.baseURL + "demoSetup",
            success : function() {
                res.console("resUiConfig: successfully copied demo sample config files under /custom!");
            },
            error : function() {
                res.console("resUiConfig: failed to copy demo sample config files under /custom!");
            }
        });
    };

    $scope.transfer = function(option) {
        res.ui.root.PopupSetMessage = "PopupSetMessage";
        res.ui.root.model.transferOption = option;
    };

}]);
