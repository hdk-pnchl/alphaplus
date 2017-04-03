var jobInstControllersM= angular.module('jobInstControllersM', ['servicesM', 'ui.bootstrap']);

var jobInstListController= jobInstControllersM.controller('JobInstListController', function($scope, $rootScope, alphaplusService, $uibModal){
    $scope.fetchJobInstColumnData = function(){
        alphaplusService.jobInst.query({
            action: "getColumnData"
        }, 
        function(response){
            $scope.gridData= {};
            $scope.gridData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= [];

            $scope.fetchJobInst(searchIp); 
        }, 
        function(){ 
            alert('JobInst ColumnData failed');
        });
    };
    $scope.editJobInst = function(editRow){
        alert("Op not implemented!");
    };
    $scope.viewJobInst = function(viewRow){ 
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'element/html/business/job/instruction/summary.html',
            controller: 'JobInstSummaryController',
            size: 'lg',
            resolve:{
                jobInstID: function (){
                    return viewRow.id;
                }
            }
        });        
    };    
    $scope.deleteJobInst = function(deleteRow){ 
        alert("Op not implemented!");
    };
    $scope.fetchJobInst = function(searchIp){
        alphaplusService.jobInst.save({
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
                alert("JobInst search failed");
            }
        );
    };

    $scope.fetchJobInstColumnData();
});

var jobInstController= jobInstControllersM.controller('JobInstController', function($scope, $rootScope, alphaplusService, $routeParams){
    $scope.jobInstData= {};
    alphaplusService.jobInst.get({
            action: "getFormData"
        }, 
        function(jobInstFormResp){
            $scope.jobInstData= jobInstFormResp;
            if($routeParams.jobInstID){
                alphaplusService.jobInst.get({
                    action: "get",
                    jobInstID: $routeParams.jobInstID
                }, function(jobInstResp){
                    $scope.jobInstData.data= jobInstResp.responseEntity;
                }, function(){
                    alert("JobInst get failure");
                });
            }
        }, 
        function(){
            alert("getFormData get failure");
    });

    $scope.update = function(data){
        alphaplusService.jobInst.save({
            action: "update"
        }, 
        data,
        function(jobInstResp){
            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= [];

            $scope.fetchJobInst(searchIp);
        }, function(){
            alert("JobInst save failure");
        });
    };
});

var jobInstSummaryController= jobInstControllersM.controller('JobInstSummaryController', function($scope, alphaplusService, jobInstID){
    $scope.jobInstData= {};
    if(jobInstID){
         alphaplusService.jobInst.get({
            action: "get",
            jobInstID: jobInstID
        }, function(jobInstResp){
            $scope.jobInstData= jobInstResp;
        }, function(){
            alert("JobInst get failure");
        });
    }
});



var jobInstService= {};
jobInstService.jobInstSummaryController= jobInstSummaryController;
jobInstService.jobInstController= jobInstController;
jobInstService.jobInstListController= jobInstListController;

jobInstControllersM.constant('jobInstService', jobInstService);