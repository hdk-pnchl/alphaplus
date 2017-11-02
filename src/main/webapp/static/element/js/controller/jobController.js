var jobControllersM= angular.module('jobControllersM', ['servicesM', 'ui.bootstrap']);

var jobListController= jobControllersM.controller('JobListController', function($scope, $uibModal, alphaplusService){ 
    $scope.service= "job";
    alphaplusService.business.processColumn($scope);
    
    $scope.edit = function(editRow){
        var ipObj= {
            bannerTab: "job",
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
            controller: "JobSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var jobController= jobControllersM.controller('JobController', function($scope, $routeParams, alphaplusService){
    var primaryKeyData= {
        val: $routeParams.jobID,
        propName: "id"
    };
    var data= {};
    data.primaryKeyData= primaryKeyData;
    data.service= "job";
    data.boDetailKey= "jobDetail";
    data.wizzardStep= $routeParams.wizzardStep;

    $scope.apData= data;

    alphaplusService.business.processWizzard($scope);
});

var jobSummaryController= jobControllersM.controller('JobSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){
        
    $scope.apData= {};
    $scope.apData.service= "job";
    $scope.apData.idKey= "id";
    $scope.apData.id= primaryKey;
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var jobService= {};
jobService.jobSummaryController= jobSummaryController;
jobService.jobController= jobController;
jobService.jobListController= jobListController;

jobControllersM.constant('jobService', jobService);