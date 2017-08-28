var plateControllersM= angular.module('plateControllersM', ['servicesM', 'ui.bootstrap']);

var plateListController= plateControllersM.controller('PlateListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("plate", $scope, "processplates");

    $scope.edit= function(editRow){
        var ipObj= {
            parentForm: $scope.$parent.parentForm,
            editRow: editRow
        };
        alphaplusService.business.viewBO("element/html/business/plate/plate.html", "PlateController", $uibModal, ipObj);
    };

    $scope.view= function(viewRow){
        var ipObj= {
            ipID: viewRow.id,
            ipObj: viewRow
        };
        alphaplusService.business.viewBO("element/html/business/plate/plate.html", "PlateSummaryController", $uibModal, ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var plateController= plateControllersM.controller('PlateController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "plate", "plateData", editRow, parentForm, "name");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processplates", "plateData", editRow, parentForm);
    };
});

var plateSummaryController= plateControllersM.controller('PlateSummaryController', function($scope, alphaplusService, ipID, ipObj){
    alphaplusService.business.processSummary("client", "id", ipID, $scope, "plateDetail", ipObj);
});

var plateService= {};
plateService.plateSummaryController= plateSummaryController;
plateService.plateController= plateController;
plateService.plateListController= plateListController;

plateControllersM.constant('plateService', plateService);