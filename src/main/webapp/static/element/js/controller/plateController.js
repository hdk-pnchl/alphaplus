var plateControllersM= angular.module('plateControllersM', ['servicesM', 'ui.bootstrap']);

var plateListController= plateControllersM.controller('PlateListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.plate.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("plate", $scope, response);
        },
        function(){
            alert('Plate GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path($scope.bannerdata.navData.hiddenNavData.plate.subNav.update.path);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/plate/summary.html", "PlateSummaryController", $uibModal);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };

    $rootScope.$on("processplates", function(event, plateData){
        if(plateData.parent && plateData.parent===$scope.$parent.parentForm){
            $scope.gridData.rowData.push(plateData.tableRow);
        }
    });     
});

var plateController= plateControllersM.controller('PlateController', function($scope, alphaplusService, $routeParams, $rootScope, parentForm){
    $scope.plateDetail= {};
    $scope.plateData= {};
    alphaplusService.plate.get({
        action: "getFormData"
    }, function(response){
        $scope.plateData= response;
        if($routeParams.plateID){
            alphaplusService.business.fetchBO("plate", $routeParams.plateID, "id", $scope, "plateDetail");
            $scope.plateData.data= $scope.plateDetail;

            //this field of type "object".
            //Procee deep prop
            angular.forEach($scope.plateData.fieldAry, function(field){
                if(field.type === "object"){
                    angular.forEach(field.object.fieldAry, function(fieldInternal){
                        var exprn="data."+fieldInternal.modalData+"="+formData.data[fieldInternal.modalData];
                        scope.$eval(exprn, formData);
                        $scope.plateData.data[fieldInternal.modalData]= $scope.plateData.data[field.name][fieldInternal.name];
                    });
                }
            });
        } 
    }, function(){
        alert("FormData GET failure");
    });

    $scope.update = function(formData){
        //this field of type "object".
        //Procee deep prop.
        angular.forEach(formData.fieldAry, function(field){
            if(field.type === "object"){
                angular.forEach(field.object.fieldAry, function(fieldInternal){
                    var exprn="data."+fieldInternal.modalData+"="+field.object.data[fieldInternal.modalData];
                    $scope.$eval(exprn, formData);
                });
            }
        });

        console.log($scope.plateData);
        $rootScope.$emit("processplates", {
            "tableRow": formData.data,
            "parent": parentForm
        });
    };
});

var plateSummaryController= plateControllersM.controller('PlateSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.plateDetail= {};
    alphaplusService.business.processSummary("client", "id", ipID, $scope, "plateDetail", ipObj);
});

var plateService= {};
plateService.plateSummaryController= plateSummaryController;
plateService.plateController= plateController;
plateService.plateListController= plateListController;

plateControllersM.constant('plateService', plateService);