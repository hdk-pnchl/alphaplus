var jobInstControllersM= angular.module('jobInstControllersM', ['servicesM', 'ui.bootstrap']);

var jobInstListController= jobInstControllersM.controller('JobInstListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("jobInst", $scope, "processinstructions");

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                parentForm: $scope.$parent.parentForm,
                editRow: editRow
            },
            templateURL: "element/html/business/crud/form.html",
            controller: "JobInstController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.view= function(viewRow){
        var ipObj= {
            modalData: {
                ipID: viewRow.id,
                editRow: viewRow
            },
            templateURL: "element/html/business/crud/summary.html",
            controller: "JobInstSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var jobInstController= jobInstControllersM.controller('JobInstController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "jobInst", "boData", editRow, parentForm, "name");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processinstructions", "boData", editRow, parentForm);
    };
});

var jobInstSummaryController= jobInstControllersM.controller('JobInstSummaryController', function($scope, alphaplusService, ipID, ipObj){
    alphaplusService.business.processSummary("jobInst", "id", ipID, $scope, "boDetail", ipObj);
});

var jobInstService= {};
jobInstService.jobInstSummaryController= jobInstSummaryController;
jobInstService.jobInstController= jobInstController;
jobInstService.jobInstListController= jobInstListController;

jobInstControllersM.constant('jobInstService', jobInstService);