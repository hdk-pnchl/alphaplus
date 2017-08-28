var jobInstControllersM= angular.module('jobInstControllersM', ['servicesM', 'ui.bootstrap']);

var jobInstListController= jobInstControllersM.controller('JobInstListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("jobInst", $scope, "processinstructions");

    $scope.edit= function(editRow){
        var ipObj= {
            parentForm: $scope.$parent.parentForm,
            editRow: editRow
        };
        alphaplusService.business.viewBO("element/html/business/job/instruction/jobInst.html", "JobInstController", $uibModal, ipObj);
    };

    $scope.view= function(viewRow){
        var ipObj= {
            ipID: viewRow.id,
            ipObj: viewRow
        };
        alphaplusService.business.viewBO("element/html/business/job/instruction/summary.html", "JobInstSummaryController", $uibModal, ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var jobInstController= jobInstControllersM.controller('JobInstController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "jobInst", "jobInstData", editRow, parentForm, "name");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processcontactDetail", "jobInstData", editRow, parentForm);
    };
});

var jobInstSummaryController= jobInstControllersM.controller('JobInstSummaryController', function($scope, alphaplusService, ipID, ipObj){
    alphaplusService.business.processSummary("jobInst", "id", ipID, $scope, "jobInstDetail", ipObj);
});

var jobInstService= {};
jobInstService.jobInstSummaryController= jobInstSummaryController;
jobInstService.jobInstController= jobInstController;
jobInstService.jobInstListController= jobInstListController;

jobInstControllersM.constant('jobInstService', jobInstService);