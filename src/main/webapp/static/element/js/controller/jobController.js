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
        //from deliveryAddress dropdown we get addresID. 
        //Here we fetch equivalent address from job.client.addressDetail and put it in job.deliveryAddress
        if($scope.jobDetail && $scope.jobDetail.client && $scope.jobDetail.client.addressDetail){
            angular.forEach($scope.jobDetail.client.addressDetail, function(address, key){
                if(formData.data.deliveryAddress && formData.data.deliveryAddress == address.id){
                    formData.data.deliveryAddress= address;
                    return;
                }
            });
        }
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