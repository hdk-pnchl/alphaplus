var jobControllersM= angular.module('jobControllersM', ['servicesM', 'ui.bootstrap']);

var jobListController= jobControllersM.controller('JobListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.job.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("job", $scope, response);
        },
        function(){
            alert('Job GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path($scope.$parent.bannerData.navData.mainNavData.job.subNav.update.path+"/"+editRow.id);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/job/summary.html", "JobSummaryController", $uibModal);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var jobController= jobControllersM.controller('JobController', function($scope, $rootScope, $route, $routeParams, $location, $http, alphaplusService){
    $scope.formService= alphaplusService;
    $scope.jobDetail= {};

    alphaplusService.job.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.wizzard= response;
            if($routeParams.jobID){
                alphaplusService.business.processFormExistingBO($scope, "jobDetail", $routeParams.jobID, "id");
            }else{
                alphaplusService.business.processFormNewBO($scope, "jobDetail");
            }
            $scope.jobDetail.isReady= true;
        }, 
        function(){
            alert('Job GET WizzardData failure');
        }
    );

    $scope.submit = function(formData){
        alphaplusService.business.submitForm(formData, $scope, "jobDetail");
    };

    $scope.selectWizzardStep = function(wizzardStep){
        alphaplusService.business.selectWizzardStep($scope, wizzardStep, "jobDetail");
    };

    $rootScope.$on("processinstructions", function(event, jobInstData){
        alphaplusService.business.processInternalObj($scope, "instructions", "instructions", "title", jobInstData, false);
    });

    $rootScope.$on("processplates", function(event, plateData){
        alphaplusService.business.processInternalObj($scope, "plateDetail", "plates", "title", plateData, false);
    });
});

var jobSummaryController= jobControllersM.controller('JobSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.jobDetail= {};
    alphaplusService.business.processSummary("job", "id", ipID, $scope, "jobDetail", ipObj);
});

var jobService= {};
jobService.jobSummaryController= jobSummaryController;
jobService.jobController= jobController;
jobService.jobListController= jobListController;

jobControllersM.constant('jobService', jobService);