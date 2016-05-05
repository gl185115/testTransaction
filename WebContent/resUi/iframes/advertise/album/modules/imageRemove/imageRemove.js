/*
 * Controller imageRemove
 */
res.ui.controller("imageRemove", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout) {

    var scroll = undefined;
    $scope.$on("resIncludeLoaded", function(){  // wait for res-include complete
        scroll = new IScroll("#wrapperViewList", {  // iScroll5
            scrollX: false,
            scrollY: true,
            scrollbars: true,
            interactiveScrollbars: true,
            mouseWheel: true,   // or "zoom"
        });

        $timeout(function(){ scroll.refresh(); }, 50);
    });

    $scope.$watch(
        function() {
            return $rootScope.model.imageRemove.delProcess;
        },
        function(newValue, oldValue) {
            if ($rootScope.model.imageRemove.delProcess == "exist") {
                $timeout(function(){ scroll.refresh(); }, 200);
            }
            $timeout(function(){ scroll.refresh(); }, 200);
        }
    );

    $scope.okay = function() {
        $rootScope.dialog = undefined;
        var picPath = $rootScope.model.imageCopy.pictures[$rootScope.model.imageCopy.indexImage];

        if (picPath) {
            window.parent.res.ui.root.model.popup = "Wait";

            res.ui.send({
                event: "file.remove",
                data: {
                    companyID : res.storage.getItem("CompanyID"),
                    folder : "advertisement",
                    file : picPath,
                    delFileList : $rootScope.model.imageRemove.delFileList,
                    confirmDel : true
                }
            });
        }
    };

    $scope.cancel = function() {
        $rootScope.dialog = undefined;
    };

    $scope.confirm = function() {
        $rootScope.dialog = undefined;
    }
}]);

