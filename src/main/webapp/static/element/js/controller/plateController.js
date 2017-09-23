var plateControllersM= angular.module('plateControllersM', ['servicesM', 'ui.bootstrap']);

var plateListController= plateControllersM.controller('PlateListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("plate", $scope, "processplateDetail");

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                parentForm: $scope.$parent.parentForm,
                editRow: editRow
            },
            templateURL: "element/html/business/crud/form.html",
            controller: "PlateController",
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
            templateURL: "element/html/business/crud/form.html",
            controller: "PlateSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var plateController= plateControllersM.controller('PlateController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "plate", "boData", editRow, parentForm, "title");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processplateDetail", "boData", editRow, parentForm);
    };
});

var plateSummaryController= plateControllersM.controller('PlateSummaryController', function($scope, alphaplusService, ipID, ipObj){
    alphaplusService.business.processSummary("client", "id", ipID, $scope, "boDetail", ipObj);
});

var plateService= {};
plateService.plateSummaryController= plateSummaryController;
plateService.plateController= plateController;
plateService.plateListController= plateListController;

plateControllersM.constant('plateService', plateService);