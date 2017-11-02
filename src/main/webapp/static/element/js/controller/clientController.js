var clientControllersM= angular.module('clientControllersM', ['servicesM', 'ui.bootstrap']);

var ClientListController= clientControllersM.controller('ClientListController', function($scope, $uibModal, alphaplusService){ 
    $scope.service= "client";
    alphaplusService.business.processColumn($scope);

    $scope.edit = function(editRow){
        var ipObj= {
            bannerTab: "client",
            primaryKey: editRow.id
        };
        alphaplusService.business.viewBO(ipObj);
    };
    $scope.view = function(viewRow){
        var ipObj= {
            modalData: {
                viewRow: viewRow,
                primaryKey: viewRow.id
            },
            templateURL: "element/html/business/crud/summary.html", 
            controller: "ClientSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var ClientController= clientControllersM.controller('ClientController', function($scope, $routeParams, alphaplusService){
    var primaryKeyData= {
        val: $routeParams.clientID,
        propName: "id"
    };

    var data= {};
    data.primaryKeyData= primaryKeyData;
    data.service= "client";
    data.boDetailKey= "clientDetail";
    data.wizzardStep= $routeParams.wizzardStep;

    $scope.apData= data;

    alphaplusService.business.processWizzard($scope, data);
});

var ClientSummaryController= clientControllersM.controller('ClientSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){
    $scope.apData= {};
    $scope.apData.service= "client";
    $scope.apData.id= primaryKey;
    $scope.apData.idKey= "id";
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var clientService= {};
clientService.clientListController= ClientListController;
clientService.clientController= ClientController;
clientService.clientSummaryController= ClientSummaryController;

clientControllersM.constant('clientService', clientService);