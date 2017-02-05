var clientControllersM= angular.module('clientControllersM', ['servicesM', 'ui.bootstrap']);

clientControllersM.controller('ClientListController', function($scope, $location, $uibModal, alphaplusService){ 
    alphaplusService.client.query({
            action: "getColumnData"
        },
        function(response){
            $scope.clientGridtData= {};
            $scope.clientGridtData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= {};

            $scope.fetchClients(searchIp); 
        },
        function(){
            alert('Core getColumnData failed');
        }
    );
    $scope.editClient = function(editRow){
        var summaryPath= '/addUser/'+editRow.userID;
        $location.path(summaryPath);
    };
    $scope.viewClient = function(viewRow){ 
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'html/client/summary.html',
            controller: 'ClientSummaryController',
            size: 'lg',
            resolve:{
                clientID: function (){
                    return viewRow.clientID;
                }
            }
        });
    };
    $scope.deleteClient = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
    
    $scope.fetchClients = function(searchIp){
        alphaplusService.client.save({
                action: "search",
                searchIp: searchIp
            },
            searchIp,
            function(response){
                $scope.clientGridtData.rowData= response.responseEntity;
                $scope.clientGridtData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                $scope.clientGridtData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                $scope.clientGridtData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);
                $scope.clientGridtData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));
            },
            function(response){
                alert("Client search failed");
            }
        );
    };
});

clientControllersM.controller('ClientController', function($scope, alphaplusService, $routeParams, $location){
    alphaplusService.client.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.clientWizzard= response;
            $scope.clientDetail= {};
            if($routeParams.clientID){
                 alphaplusService.client.get({
                    action: "get",
                    clientID: $routeParams.clientID
                }, function(clientDataResp){
                    $scope.clientDetail= clientDataResp.responseEntity;
                    angular.forEach($scope.clientWizzard.wizzardData, function(formIpData, formName){
                        formIpData.data= $scope.clientDetail[formName];
                    });
                }, function(){
                    alert("Client get failure");
                });
            }else{
                angular.forEach($scope.clientWizzard.wizzardData, function(formIpData, formName){
                    $scope.clientDetail[formName]= {};
                    angular.forEach(formIpData.fieldAry, function(field){
                        $scope.clientDetail[formName][field.name]= "";
                    });
                    formIpData.data= $scope.clientDetail[formName];
                });
            }
            $scope.clientDetail.isReady= true;
        }, 
        function(){ 
            alert('Client getWizzardData failure');
        }
    );  
 
    $scope.selectWizzardStep= function(selectedWizzardStep){
        angular.forEach($scope.clientWizzard.wizzardStepData, function(wizzardStep){
            wizzardStep.active= false;
            wizzardStep.class= '';
        });    
        selectedWizzardStep.active= true;
        selectedWizzardStep.class= 'active';

        angular.forEach($scope.clientWizzard.wizzardData, function(value, key){
            value.isHidden = true;
        });    
        $scope.clientWizzard.wizzardData[selectedWizzardStep.name].isHidden=false;
    };
 
    $scope.isLastStep= function(step) {
       if(step == $scope.clientWizzard.commonData.lastStep){
            return true;
       }
       return false;
    }

    $scope.submitClient = function(clientDataType, clientData){
        var service= alphaplusService[clientDataType];
        var action= "save";
        if($scope.clientDetail[clientDataType] && $scope.clientDetail[clientDataType].id){
            action= "update";
            clientData["id"]= $scope.clientDetail[clientDataType]["id"];
        }
        //server call
        service.save({
                action: action,
                clientID: $scope.clientDetail.id
            }, 
            clientData, 
            function(persistedClientData){
                if(persistedClientData.responseData && persistedClientData.responseData.ERROR_MSG){
                    alert(persistedClientData.responseData.ERROR_MSG);
                }else{
                    $scope.clientDetail= persistedClientData.responseEntity;
                    if($scope.isLastStep(clientDataType)){
                        alert("Thank you for sharing your information. Your information is safe with duel-encryption and only you who can see it. Now, go ahead any file a complain!");
                        //$location.path($scope.$parent.bannerData.navData.mainNavData.client.subNav[0].path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= $scope.clientWizzard.wizzardStepData[clientDataType];
                        currentWizzardStep.submitted= true;
                        //move to next step in the wizzard
                        $scope.selectWizzardStep($scope.clientWizzard.wizzardStepData[currentWizzardStep.next]);
                    }
                }
            },
            function(){
                alert("Client save failure");
            }
        );
    };
});

clientControllersM.controller('ClientSummaryController', function($scope, alphaplusService, clientID){
    $scope.clientDetail= {};
    if(clientID){
         alphaplusService.client.get({
            action: "get",
            clientID: clientID
        }, function(clientDataResp){
            $scope.clientDetail= clientDataResp;
        }, function(){
            alert("Client get failure");
        });
    }
});