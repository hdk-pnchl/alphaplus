var jobControllersM= angular.module('jobControllersM', ['servicesM', 'ui.bootstrap']);

var jobListController= jobControllersM.controller('JobListController', function($scope, $uibModal, alphaplusService){ 
    alphaplusService.business.processColumn("job", $scope);
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
            templateURL: "element/html/business/crud/form.html",
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
    var eventData= [{
            "form": "instructions",
            "collectionPropName": "instructions",
            "eventName": "processinstructions",
            "idKeyPropName": "title"
        },{
            "form": "plateDetail",
            "collectionPropName": "plateDetail",
            "eventName": "processplates",
            "idKeyPropName": "title"
        }
    ];
    var data= {};
    data.primaryKeyData= primaryKeyData;
    data.eventData= eventData;
    data.service= "job";
    data.boDetailKey= "jobDetail";

    $scope.data= data;

    alphaplusService.business.processWizzard($scope);
});

var jobSummaryController= jobControllersM.controller('JobSummaryController', function($scope, alphaplusService, primaryKey, viewRow){
    alphaplusService.business.processSummary("job", "id", primaryKey, $scope, "jobDetail", viewRow);
});

var jobService= {};
jobService.jobSummaryController= jobSummaryController;
jobService.jobController= jobController;
jobService.jobListController= jobListController;

jobControllersM.constant('jobService', jobService);