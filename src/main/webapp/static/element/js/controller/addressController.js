var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressListController= addressControllersM.controller('AddressListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("address", $scope, "processaddressDetail");

    $scope.edit= function(editRow){
        var ipObj= {
            parentForm: $scope.$parent.parentForm,
            editRow: editRow
        };
        alphaplusService.business.viewBO("element/html/business/address/address.html", "AddressController", $uibModal, ipObj);
    };

    $scope.view= function(viewRow){
        var ipObj= {
            ipID: viewRow.id,
            ipObj: viewRow
        };
        alphaplusService.business.viewBO("element/html/business/address/summary.html", "AddressSummaryController", $uibModal, ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var addressController= addressControllersM.controller('AddressController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "address", "addressData", editRow, parentForm, "name");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processaddressDetail", "addressData", editRow, parentForm);
    };
});

var addressSummaryController= addressControllersM.controller('AddressSummaryController', function($scope, alphaplusService, ipID, ipObj){
    alphaplusService.business.processSummary("address", "id", ipID, $scope, "addressDetail", ipObj);
});

var addressService= {};
addressService.addressSummaryController= addressSummaryController;
addressService.addressController= addressController;
addressService.addressListController= addressListController;

addressControllersM.constant('addressService', addressService);