var plateControllersM= angular.module('plateControllersM', ['servicesM', 'ui.bootstrap']);

var plateListController= plateControllersM.controller('PlateListController', function($scope, $rootScope, alphaplusService, $uibModal){
    $scope.fetchPlateColumnData = function(){
        alphaplusService.plate.query({
            action: "getColumnData"
        }, 
        function(response){
            $scope.gridData= {};
            $scope.gridData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= [];

            $scope.fetchPlate(searchIp); 
        }, 
        function(){ 
            alert('Plate ColumnData failed');
        });
    };
    $scope.editPlate = function(editRow){
        alert("Op not implemented!");
    };
    $scope.viewPlate = function(viewRow){ 
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'element/html/business/plate/summary.html',
            controller: 'PlateSummaryController',
            size: 'lg',
            resolve:{
                plateID: function (){
                    return viewRow.id;
                }
            }
        });        
    };    
    $scope.deletePlate = function(deleteRow){ 
        alert("Op not implemented!");
    };
    $scope.fetchPlate = function(searchIp){
        alphaplusService.plate.save({
                action: "search",
                searchIp: searchIp
            }, 
            searchIp, 
            function(response){
                $scope.gridData.rowData= response.responseEntity;
                $scope.gridData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                $scope.gridData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                $scope.gridData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);
                $scope.gridData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));
            },
            function(response){
                alert("Plate search failed");
            }
        );
    };

    $scope.fetchPlateColumnData();
});

var plateController= plateControllersM.controller('PlateController', function($scope, $rootScope, alphaplusService, $routeParams){
    $scope.plateData= {};
    alphaplusService.plate.get({
            action: "getFormData"
        }, 
        function(plateFormResp){
            $scope.plateData= plateFormResp;
            if($routeParams.plateID){
                alphaplusService.plate.get({
                    action: "get",
                    plateID: $routeParams.plateID
                }, function(plateResp){
                    $scope.plateData.data= plateResp.responseEntity;
                }, function(){
                    alert("Plate get failure");
                });
            }
        }, 
        function(){
            alert("getFormData get failure");
    });

    $scope.update = function(data){
        alphaplusService.plate.save({
            action: "update"
        }, 
        data,
        function(plateResp){
            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= [];

            $scope.fetchPlate(searchIp);
        }, function(){
            alert("Plate save failure");
        });
    };
});

var plateSummaryController= plateControllersM.controller('PlateSummaryController', function($scope, alphaplusService, plateID){
    $scope.plateData= {};
    if(plateID){
         alphaplusService.plate.get({
            action: "get",
            plateID: plateID
        }, function(plateResp){
            $scope.plateData= plateResp;
        }, function(){
            alert("Plate get failure");
        });
    }
});

var plateService= {};
plateService.plateSummaryController= plateSummaryController;
plateService.plateController= plateController;
plateService.plateListController= plateListController;

plateControllersM.constant('plateService', plateService);