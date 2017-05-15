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
        alphaplusService.business.viewBO(viewRow.id, "plateID", "html/plate/summary.html", "PlateSummaryController")
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
            alphaplusService.business.fetchBO("plate", $routeParams.plateID, "plateID", $scope, "plateDetail");
            $scope.plateData.data= $scope.plateDetail;
        }
    }, function(){
        alert("FormData GET failure");
    });

    $scope.update = function(formData){
        $rootScope.$emit("processplates", {
            "tableRow": formData.data,
            "parent": parentForm
        });
    };
});

var plateSummaryController= plateControllersM.controller('PlateSummaryController', function($scope, alphaplusService, plateID){
    $scope.plateDetail= {};
    if(plateID){
        alphaplusService.business.fetchBO("client", "plateID", plateID, $scope, "plateDetail");
    }
});


var plateService= {};
plateService.plateSummaryController= plateSummaryController;
plateService.plateController= plateController;
plateService.plateListController= plateListController;

plateControllersM.constant('plateService', plateService);