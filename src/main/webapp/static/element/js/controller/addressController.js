var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressListController= addressControllersM.controller('AddressListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("address", $scope, "processaddressDetail");

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                parentForm: $scope.$parent.parentForm,
                editRow: editRow
            },
            templateURL: "element/html/business/crud/form.html", 
            controller: "AddressController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.view= function(viewRow){
        var ipObj= {
            modalData: {
                viewRow: viewRow,
                ipID: viewRow.id
            },
            templateURL: "element/html/business/crud/summary.html", 
            controller: "AddressSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var addressController= addressControllersM.controller('AddressController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "address", "boData", editRow, parentForm, "name");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processaddressDetail", "boData", editRow, parentForm);
    };
});

var addressSummaryController= addressControllersM.controller('AddressSummaryController', function($scope, alphaplusService, ipID, viewRow){
    alphaplusService.business.processSummary("address", "id", ipID, $scope, "boDetail", viewRow);
});

var addressService= {};
addressService.addressSummaryController= addressSummaryController;
addressService.addressController= addressController;
addressService.addressListController= addressListController;

addressControllersM.constant('addressService', addressService);