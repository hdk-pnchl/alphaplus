var clientControllersM= angular.module('clientControllersM', ['servicesM', 'ui.bootstrap']);

var ClientListController= clientControllersM.controller('ClientListController', function($scope, $uibModal, alphaplusService){ 
    alphaplusService.business.processColumn("client", $scope);
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
    var eventData= [{
            "form": "addressDetail",
            "collectionPropName": "addressDetail",
            "eventName": "processaddressDetail",
            "idKeyPropName": "name"
        },{
            "form": "contactDetail",
            "collectionPropName": "contactDetail",
            "eventName": "processcontactDetail",
            "idKeyPropName": "name"
        }
    ];

    var data= {};
    data.primaryKeyData= primaryKeyData;
    data.eventData= eventData;
    data.service= "client";
    data.boDetailKey= "clientDetail";
    data.wizzardStep= $routeParams.wizzardStep;

    $scope.data= data;

    alphaplusService.business.processWizzard($scope, data);
});

var ClientSummaryController= clientControllersM.controller('ClientSummaryController', function($scope, alphaplusService, primaryKey, viewRow){
    alphaplusService.business.processSummary("client", "id", primaryKey, $scope, "clientDetail", viewRow);
});

var clientService= {};
clientService.clientListController= ClientListController;
clientService.clientController= ClientController;
clientService.clientSummaryController= ClientSummaryController;

clientControllersM.constant('clientService', clientService);