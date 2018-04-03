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
    $scope.emailIsTaken= false;
    //init
    $scope.initClient= function(respData){
        $scope.formData= respData.responseEntity;
        $scope.dataData.disableTabs= false;
        $scope.addressTableParams = new NgTableParams({}, { dataset: $scope.formData.addresses });
        $scope.contactTableParams = new NgTableParams({}, { dataset: $scope.formData.contacts });  
    };    
    if($routeParams.clientID){
        if($routeParams.step){
            $scope.dataData.activeTab= parseInt($routeParams.step);
        }        
        alphaplusService.client.get({
            "id": $routeParams.clientID,
            "action": "get"
        }, function(respData){
            $scope.initClient(respData);
        });
    }else{
        alphaplusService.client.get({
            "action": "empty"
        }, function(respData){
            $scope.initClient(respData);
            $scope.dataData.disableTabs= true;
        });               
    }
    $scope.fetchBasicData= function(form){       
        var basicData= {};
        basicData.id= $scope.formData.id;
        basicData.name= $scope.formData.name;
        basicData.emailID= $scope.formData.emailID;
        return basicData;       
    };
    //submit
    $scope.submitBasic= function(form){      
        if(form.$valid){
            var basicData= $scope.fetchBasicData();
            var command= "new";
            if($scope.formData.id){
                command= "basic";
            }
            alphaplusService.client.save({action: command}, basicData, 
            function(response){
                if(response.responseData.ERROR){
                    angular.forEach(response.alertData, function(alert){
                        if(alert.desc=="EMAIL_ID_TAKEN"){
                            $scope.emailIsTaken= true;
                        }
                    });
                }else{
                    //isNOTfirstStep
                    if($scope.formData.id){
                        $scope.formData= response.responseEntity;
                        $scope.processTabs();                           
                    }else{
                        $scope.formData= response.responseEntity;
                        $location.path(alphaplusService.obj.bannerData.navData.mainNavData.client.subNav.update.path+"/"+$scope.formData.id+"/"+1);
                    } 
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
        resolveObj.service= {};
        resolveObj.service.value= "client";
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
        resolveObj.service= {};
        resolveObj.service.value= "client";
        //$uibModal
        $uibModal.open({
            templateUrl: "element/html/business/common/contact.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "ContactControllerN"           
        });
    };    
});