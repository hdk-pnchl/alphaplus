var jobInstControllersM= angular.module('jobInstControllersM', ['servicesM', 'ui.bootstrap']);

var jobInstListController= jobInstControllersM.controller('JobInstListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.jobInst.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("jobInst", $scope, response);
        },
        function(){
            alert('JobInst GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path($scope.bannerdata.navData.hiddenNavData.jobInst.subNav.update.path);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, "jobInstID", "html/jobInst/summary.html", "JobInstSummaryController")
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };

    $rootScope.$on("processinstructions", function(event, jobInstData){
        if(jobInstData.parent && jobInstData.parent===$scope.$parent.parentForm){
            $scope.gridData.rowData.push(jobInstData.tableRow);
        }
    });    
});

var jobInstController= jobInstControllersM.controller('JobInstController', function($scope, alphaplusService, $routeParams, $rootScope, parentForm){
    $scope.jobInstDetail= {};
    $scope.jobInstData= {};
    alphaplusService.jobInst.get({
        action: "getFormData"
    }, function(response){
        $scope.jobInstData= response;
        if($routeParams.jobInstID){
            alphaplusService.business.fetchBO("jobInst", $routeParams.jobInstID, "jobInstID", $scope, "jobInstDetail");
            $scope.jobInstData.data= $scope.jobInstDetail;
        }
    }, function(){
        alert("FormData GET failure");
    });

    $scope.update = function(formData){
        $rootScope.$emit("processinstructions", {
            "tableRow": formData.data,
            "parent": parentForm
        });
    };
});

var jobInstSummaryController= jobInstControllersM.controller('JobInstSummaryController', function($scope, alphaplusService, jobInstID){
    $scope.jobInstDetail= {};
    if(jobInstID){
        alphaplusService.business.fetchBO("jobInst", "jobInstID", jobInstID, $scope, "jobInstDetail");
    }
});

var jobInstService= {};
jobInstService.jobInstSummaryController= jobInstSummaryController;
jobInstService.jobInstController= jobInstController;
jobInstService.jobInstListController= jobInstListController;

jobInstControllersM.constant('jobInstService', jobInstService);