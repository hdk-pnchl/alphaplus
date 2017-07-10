var clientControllersM= angular.module('clientControllersM', ['servicesM', 'ui.bootstrap']);

var ClientListController= clientControllersM.controller('ClientListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.client.query({
            action: "getColumnData"
        },
        function(response){
            var promise= alphaplusService.business.processColumnData("client", $scope, response);
            promise.$promise.then(function(data){
                angular.forEach($scope.gridData.rowData, function(client){
                    if(client.addressDetail){
                        angular.forEach(client.addressDetail, function(adds, key){
                            if(!client.addressDetailStrAry){
                                client.addressDetailStrAry= [];
                            }
                            client.addressDetailStrAry.push(adds.addressStr);
                        });
                    }
                    if(client.contactDetail){
                        angular.forEach(client.contactDetail, function(contact, key){
                            if(!client.contactDetailStrAry){
                                client.contactDetailStrAry= [];
                            }
                            client.contactDetailStrAry.push(contact.contactStr);
                        });
                    }
                });
            });
        },
        function(){
            alert('Client GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path($scope.$parent.bannerData.navData.mainNavData.client.subNav.update.path+"/"+editRow.id);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/client/summary.html", "ClientSummaryController", $uibModal);
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
            if($routeParams.clientID){
                alphaplusService.business.processFormExistingBO($scope, "clientDetail", $routeParams.clientID, "id");
            }else{
                alphaplusService.business.processFormNewBO($scope, "clientDetail");
            }
            $scope.clientDetail.isReady= true;
        }, 
        function(){ 
            alert('Client GET WizzardData failure');
        }
    );

    $scope.submit = function(formData){
        alphaplusService.business.submitForm(formData, $scope, "clientDetail");
    };

    $scope.selectWizzardStep = function(wizzardStep){
        alphaplusService.business.selectWizzardStep($scope, wizzardStep, "clientDetail");
    };

    $rootScope.$on("processaddressDetail", function(event, addressData){
        alphaplusService.business.processInternalObj($scope, "addressDetail", "addressDetail", "name", addressData, false);
    });

    $rootScope.$on("processcontactDetail", function(event, contactData){
        alphaplusService.business.processInternalObj($scope, "contactDetail", "contactDetail", "name", contactData, false);
    });
});

var ClientSummaryController= clientControllersM.controller('ClientSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.clientDetail= {};
    alphaplusService.business.processSummary("client", "id", ipID, $scope, "clientDetail", ipObj);
});

var clientService= {};
clientService.clientListController= ClientListController;
clientService.clientController= ClientController;
clientService.clientSummaryController= ClientSummaryController;

clientControllersM.constant('clientService', clientService);