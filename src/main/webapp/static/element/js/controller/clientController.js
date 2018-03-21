var clientControllersM= angular.module('clientControllersM', ['servicesM', 'ui.bootstrap']);

clientControllersM.controller('ClientListControllerN', function($scope, $uibModal, NgTableParams, alphaplusService){ 
    $scope.tableParams = new NgTableParams(alphaplusService.obj.grid.initialParams, {      
        getData: function(params){
            var searchIp= {};
            searchIp.pageNo= params.page();
            searchIp.rowsPerPage= params.count();
            searchIp.searchData= [];
            searchIp.searchData.push(params.filter());   
                    
            // ajax request to api
            return alphaplusService.client.save({
                action: "search"
            }, searchIp).$promise.then(function(response) {
                params.total(response.responseData.ROW_COUNT); // recal. page nav controls
                return response.responseEntity;
            });         
        }
    }); 
});

clientControllersM.controller('ClientControllerN', function($scope, $routeParams, $location, alphaplusService, NgTableParams, $uibModal){
    $scope.dateOptions= alphaplusService.obj.from.dateOptions;
    $scope.dataData= {};
    $scope.dataData.disableTabs= true;
    $scope.dataData.activeTab= 0;

    if($routeParams.clientID){
        alphaplusService.client.get({
            "id": $routeParams.clientID,
            "action": "get"
        }, function(respData){
            $scope.formData= respData.responseEntity;
            $scope.dataData.disableTabs= false;
            $scope.addressTableParams = new NgTableParams({}, { dataset: $scope.formData.addresses });
            $scope.contactTableParams = new NgTableParams({}, { dataset: $scope.formData.contacts });            
        });
    }else{
        $scope.formData= {};
        $scope.formData.contacts= [];
        $scope.formData.addresses= [];
        $scope.addressTableParams = new NgTableParams({}, { dataset: $scope.formData.addresses });
        $scope.contactTableParams = new NgTableParams({}, { dataset: $scope.formData.contacts });          
    }

    //submit
    $scope.submitForm= function(isValidRequest){      
        if(isValidRequest){
            alphaplusService.client.save({
                action: "saveOrUpdate"
            }, $scope.formData, function(response){
                if(response.responseData.ERROR){

                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };

    //Tabs
    $scope.processTabs= function(){
        $scope.dataData.disableTabs= false;
        $scope.nextTabs();        
    };
    $scope.nextTabs= function(){
        if(!$scope.dataData.disableTabs){
            var nextTabIdx= $scope.dataData.activeTab + 1;
            if(nextTabIdx < 3){
                $scope.dataData.activeTab= nextTabIdx;
            }else{
                $location.path(alphaplusService.obj.bannerData.navData.mainNavData.client.subNav.list.path);
            }            
        }
    };

    //Address
    $scope.openAddress= function(address){
        //resolveObj
        var resolveObj= {};        
        resolveObj.editMode= true;
        //address
        if(!address){
            address= {};
            resolveObj.editMode= false;
        }
        resolveObj.formData= $scope.formData;
        resolveObj.addressTableParams= $scope.addressTableParams;
        resolveObj.ipAddress= address;        
        //$uibModal
        $uibModal.open({
            templateUrl: "element/html/business/common/address.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "AddressControllerN"           
        });
    };

    //Contact 
    $scope.openContact= function(contact){
        //resolveObj        
        var resolveObj= {}; 
        resolveObj.editMode= true;       
        //address
        if(!contact){
            contact= {};
            resolveObj.editMode= false;
        }
        resolveObj.formData= $scope.formData;
        resolveObj.contactTableParams= $scope.contactTableParams;
        resolveObj.ipContact= contact;
        //$uibModal
        $uibModal.open({
            templateUrl: "element/html/business/common/contact.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "ContactControllerN"           
        });
    };    
});

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
    data.boDetailKey= "boDetail";
    data.wizzardStep= $routeParams.wizzardStep;

    $scope.apData= data;

    alphaplusService.business.processWizzard($scope, data);
});

var ClientSummaryController= clientControllersM.controller('ClientSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){
    
    $scope.apData= {};
    $scope.apData.service= "client";
    $scope.apData.idKey= "id";
    $scope.apData.id= primaryKey;
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var clientService= {};
clientService.clientListController= ClientListController;
clientService.clientController= ClientController;
clientService.clientSummaryController= ClientSummaryController;

clientControllersM.constant('clientService', clientService);