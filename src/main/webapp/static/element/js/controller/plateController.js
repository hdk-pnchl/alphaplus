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
});

var plateController= plateControllersM.controller('PlateController', function($scope, $rootScope, $route, $routeParams, $location, $http, alphaplusService){
    $scope.formService= alphaplusService;
    $scope.plateDetail= {};

    alphaplusService.plate.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.wizzard= response;

            if($routeParams.plateID){
                alphaplusService.business.processFormExistingBO($scope, "plateDetail", $routeParams.plateID, "plateID");
            }else{
                alphaplusService.business.processFormNewBO($scope, "plateDetail");
            }
            $scope.plateDetail.isReady= true;
        }, 
        function(){ 
            alert('Plate GET WizzardData failure');
        }
    );

    $scope.submit = function(formData){
        alphaplusService.business.submitForm(formData, $scope, "plateDetail");
    };

    $scope.selectWizzardStep = function(wizzardStep){
        alphaplusService.business.selectWizzardStep($scope, wizzardStep, "plateDetail");
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