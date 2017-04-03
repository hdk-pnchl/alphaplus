var clientControllersM= angular.module('clientControllersM', ['servicesM', 'ui.bootstrap']);

var ClientListController= clientControllersM.controller('ClientListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.client.query({
            action: "getColumnData"
        },
        function(response){
            $scope.gridData= {};
            $scope.gridData.columnData= response;

            alphaplusService.business.fetchBOList("client", $scope)
            console.log($scope.gridData);
        },
        function(){
            alert('Client GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path(scope.bannerdata.navData.hiddenNavData.client.subNav.update.path);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, "clientID", "html/client/summary.html", "ClientSummaryController")
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var ClientController= clientControllersM.controller('ClientController', function($scope, $rootScope, $route, $routeParams, $location, $http, alphaplusService){
    $scope.formService= alphaplusService;
    $scope.clientDetail= {};

    alphaplusService.client.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.wizzard= response;

            if($routeParams.clientId){
                alphaplusService.business.processFormExistingBO($scope, "clientDetail", $routeParams.clientId, "clientId");
            }else{
                alphaplusService.business.processFormNewBO($scope, "clientDetail");
            }
            $scope.clientDetail.isReady= true;
        }, 
        function(){ 
            alert('Client GET WizzardData failure');
        }
    );

    $scope.submit = function(formData, data){
        alphaplusService.business.submitForm(formData, $scope, "clientDetail");
    };

    $rootScope.$on("processAddress", function(event, address){
        if(!$scope.clientDetail.addressDetail){
            $scope.clientDetail.addressDetail= {};
        }
       $scope.clientDetail.addressDetail[address.name]= address;

       alphaplusService.business.processFormExistingBOInternal($scope, "clientDetail");
    });
});

var ClientSummaryController= clientControllersM.controller('ClientSummaryController', function($scope, alphaplusService, clientID){
    $scope.clientDetail= {};
    if(clientID){
        alphaplusService.business.fetchBO("client", "clientId", clientID, $scope, "clientDetail");
    }
});

var clientService= {};
clientService.clientListController= ClientListController;
clientService.clientController= ClientController;
clientService.clientSummaryController= ClientSummaryController;

clientControllersM.constant('clientService', clientService);