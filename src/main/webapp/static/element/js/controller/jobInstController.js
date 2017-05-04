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
});


var jobInstController= jobInstControllersM.controller('JobInstController', function($scope, $rootScope, $route, $routeParams, $location, $http, alphaplusService){
    $scope.formService= alphaplusService;
    $scope.jobInstDetail= {};

    alphaplusService.jobInst.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.wizzard= response;
            if($routeParams.jobInstID){
                alphaplusService.business.processFormExistingBO($scope, "jobInstDetail", $routeParams.jobInstID, "jobInstID");
            }else{
                alphaplusService.business.processFormNewBO($scope, "jobInstDetail");
            }
            $scope.jobInstDetail.isReady= true;
        }, 
        function(){ 
            alert('JobInst GET WizzardData failure');
        }
    );

    $scope.submit = function(formData){
        alphaplusService.business.submitForm(formData, $scope, "jobInstDetail");
    };

    $scope.selectWizzardStep = function(wizzardStep){
        alphaplusService.business.selectWizzardStep($scope, wizzardStep, "jobInstDetail");
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