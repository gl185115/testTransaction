/*
 * Controller
 */
res.ui.controller("menu", ["$scope", "$rootScope", function($scope, $rootScope){

    $scope.exit = function() {

        $rootScope.model.imageCopy.indexFile = -1;
        $rootScope.model.imageCopy.indexFileName = "";
        $rootScope.model.imageCopy.targetindexFile = -1;
        $rootScope.model.imageCopy.targetindexFileName = "";
        $rootScope.model.imageCopy.folderCopyFrom = "";
        $rootScope.model.imageCopy.folderCopyTo = "";
        $rootScope.model.imageCopy.successMsgShow = false;
        $rootScope.model.imageCopy.failedMsgShow = false;
        $rootScope.model.imageCopy.targetfiles.length = 0;
        window.parent.res.main.page("home");
    };

    $scope.$on("resIncludeLoaded", function() {
        var advertiseImgfileInput = document.getElementById('advertiseImgfileInput');
        var advertiseImgfileInputFull = document.getElementById('advertiseImgfileInputFull');

        advertiseImgfileInput.addEventListener('change', function(e){

            window.parent.res.ui.root.model.popup = "Wait";

            for (var i = 0; i < advertiseImgfileInput.files.length; i++) {
                var file = advertiseImgfileInput.files[i];
                var reader = new FileReader();
                reader.filename = file.name;
                reader.readAsDataURL(file);
                res.ui.send({
                    context : res.ui.root.context,
                    event : "file.picture.upload",
                    data : {
                        filename : reader.filename,
                        filecontent : file,
                        folder : "advertise/images",
                        sizeType : 1
                    }
                });
            }
        });
        
        advertiseImgfileInputFull.addEventListener('change', function(e){

            window.parent.res.ui.root.model.popup = "Wait";

            for (var i = 0; i < advertiseImgfileInputFull.files.length; i++) {
                var file = advertiseImgfileInputFull.files[i];
                var reader = new FileReader();
                reader.filename = file.name;
                reader.readAsDataURL(file);
                res.ui.send({
                    context : res.ui.root.context,
                    event : "file.picture.upload",
                    data : {
                        filename : reader.filename,
                        filecontent : file,
                        folder : "advertise/images",
                        sizeType : 2
                    }
                });
            }
        });
    });

    $scope.imageDelete = function() {

        window.parent.res.ui.root.model.popup = "Wait";
        var picPath = $rootScope.model.imageCopy.pictures[$rootScope.model.imageCopy.indexImage];

        if (picPath) {
            res.ui.send({
                event: "file.remove",
                data: {
                    companyID : res.storage.getItem("CompanyID"),
                    folder : "advertise/images",
                    file : picPath,
                    confirmDel : false
                }
            });
        }

        $rootScope.model.imageCopy.successMsgShow = false;
        $rootScope.model.imageCopy.failedMsgShow = false;
    };

//  $scope.imageImport = function() {
//      $rootScope.dialog = "imageImport";
//      $rootScope.model.imageCopy.successMsgShow = false;
//      $rootScope.model.imageCopy.failedMsgShow = false;
//
//      res.ui.send({
//          context: res.ui.root.context,
//          event : "imagecopy.getfolder",
//          data : {
//              folder : !$rootScope.model.imageCopy.BasePath ? "" : $rootScope.model.imageCopy.BasePath,
//              recursive : "false"
//          }
//      });
//
//      res.ui.send({
//          context: res.ui.root.context,
//          event : "imagecopy.gettargetfolder",
//          data : {
//              folder : !$rootScope.model.imageCopy.targetBasePath ? "" : $rootScope.model.imageCopy.targetBasePath,
//              recursive : "false"
//          }
//      });
//  };

//  $scope.imageDelete = function() {
//      $rootScope.dialog = "imageRemove";
//      $rootScope.model.imageCopy.successMsgShow = false;
//      $rootScope.model.imageCopy.failedMsgShow = false;
//  };
}]);

