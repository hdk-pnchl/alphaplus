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
        $location.path($scope.bannerdata.navData.hiddenNavData.job.subNav.update.path);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, "jobID", "html/job/summary.html", "JobSummaryController")
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
                alphaplusService.business.processFormExistingBO($scope, "jobDetail", $routeParams.jobID, "jobID");
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
});

var jobSummaryController= jobControllersM.controller('JobSummaryController', function($scope, alphaplusService, jobID){
    $scope.jobDetail= {};
    if(jobID){
        alphaplusService.business.fetchBO("job", "jobID", jobID, $scope, "jobDetail");
    }
});

var jobService= {};
jobService.jobSummaryController= jobSummaryController;
jobService.jobController= jobController;
jobService.jobListController= jobListController;

jobControllersM.constant('jobService', jobService);