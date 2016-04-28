/*
 * Device Settings
 */
res.ui.controller("companyLogon", ["$scope", "$rootScope", "$timeout", function($scope, $rootScope, $timeout){

    $scope.$on("resIncludeLoaded", function(){  // wait for res-include complete
        $timeout(function(){ res.ui.canvas("#companyLogon"); }, 200);
        $scope.companies = res.config.companies;
        $scope.volumes=[];
    });

    $scope.$watch(
            function(){ return $rootScope.section; },
            function(section, oldValue){
                if (section != "device") return;
                $scope.changePage('top');
            }
        );
    $scope.$watch(
            function(){ return $rootScope.model.volume; },
            function(volume, oldValue){
                if (oldValue == volume) return;
                $scope.volumes.push(volume*1.0);
                $scope.setVolume();
            }
        );
    $scope.$watch(
            "page",
            function(page, oldValue){
                switch(page){
                case "companyID":
                    $scope.companyID = $rootScope.model.companyID;
                    break;
                case "storeID":
                    $scope.storeID = $rootScope.model.storeID;
                    break;
                case "workstationID":
                    $scope.workstationID = $rootScope.model.workstationID;
                    break;
                }
                $scope.$broadcast("resTenkeyReset");
            }
        );
    $scope.$on("resTenkeyCleared", function(event,value){
        $scope[$scope.page]="";
    });
    $scope.$on("resTenkeyEntered", function(){
        $scope.set($scope.page, $scope[$scope.page]);
//      switch($scope.page){
//      case "companyID":
//          $rootScope.model.companyID = $scope.companyID;
//          res.storage.setItem("CompanyID", $scope.companyID);
//          break;
//      case "storeID":
//          $rootScope.model.storeID = $scope.storeID;
//          res.storage.setItem("RetailStoreID", $scope.storeID);
//          break;
//      case "workstationID":
//          $rootScope.model.workstationID = $scope.workstationID;
//          res.storage.setItem("WorkstationID", $scope.workstationID);
//          break;
//      }
//      $scope.changePage("top");
    });
    $scope.set = function(property, value){
        $rootScope.model[property] = value;
        switch(property){
        case "companyID":
            res.storage.setItem("CompanyID", value);
//          res.ui.send({event:"settings.profile.change",
//              data:{
//                  'workstationid':$rootScope.model.workstationID,
//                  'storeid':$rootScope.model.storeID ,
//                  'companyid':$rootScope.model.companyID
//              }});
            break;
        case "storeID":
            res.storage.setItem("RetailStoreID", value);
//          res.ui.send({event:"settings.profile.change",
//              data:{
//                  'workstationid':$rootScope.model.workstationID,
//                  'storeid':$rootScope.model.storeID ,
//                  'companyid':$rootScope.model.companyID
//              }});
            break;
        case "workstationID":
            res.storage.setItem("WorkstationID", value);
//          res.ui.send({event:"settings.profile.change",
//              data:{
//                  'workstationid':$rootScope.model.workstationID,
//                  'storeid':$rootScope.model.storeID ,
//                  'companyid':$rootScope.model.companyID
//              }});
            break;
        }
        $scope.changePage("top");
    };
    $scope.back = function(){
        if ($scope.tenkey){
            $scope.tenkey = false;
        }else{
            $scope.changePage("top");
        };
    };
    $scope.cidToggle = function(){
        $rootScope.model.screen2nd = !$rootScope.model.screen2nd;
        res.storage.setItem("Screen2nd", $rootScope.model.screen2nd);
    };
    $scope.changePage = function(page){
        $scope.companies = window.parent.res.ui.root.model.companies;
        $scope.page = page;
    };
    $scope.setLanguage = function(lang){
        res.ui.send({ event: "settings.language.change", data: lang});
        $scope.changePage("top");
    };
    $scope.cursorToggle = function(){
        $scope.cursorHidden = !$scope.cursorHidden;
        if ($scope.cursorHidden){
            window.parent.res.main.cursor.hide();
        }else{
            window.parent.res.main.cursor.show();
        }
    };
    $scope.restart = function(){
        window.parent.res.main.page("home");
//        res.ui.send({ event: "settings.reload" });
    };
    $scope.setVolume=function(){
        var stoped=$scope.volumes.length;
        $timeout(function(){
            if($scope.volumes.length>0 && stoped==$scope.volumes.length){
                var volume=$scope.volumes[$scope.volumes.length-1];
                window.parent.res.audio.volume(volume);
                window.parent.res.audio.play("tap");
                res.storage.setItem("Volume",volume);
                $scope.volumes.length=0;
            }
        }, 500);
    };
    $rootScope.model.volume=res.storage.getItem("Volume")? res.storage.getItem("Volume"):res.config.touchToneVolume;
}]);


