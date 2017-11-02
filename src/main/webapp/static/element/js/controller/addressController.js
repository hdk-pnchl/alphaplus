var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressListController= addressControllersM.controller('AddressListController', 
    function($scope, $rootScope, $uibModal, alphaplusService){
    $scope.dPropData= $scope.$parent.dPropData;

    $scope.service= "address";
    alphaplusService.business.processColumn($scope);

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                //addListCtrlScope.dynalicCtrlScope.form-field
                parentForm: $scope.dPropData.parentForm,
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
                primaryKey: viewRow.id,
                viewRow: viewRow
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

var addressController= addressControllersM.controller('AddressController', 
    function($scope, alphaplusService, parentForm, editRow){

    $scope.apData= {};
    $scope.apData.service= "address";
    $scope.apData.boDetailKey= "boData";
    $scope.apData.editRow= editRow;
    $scope.apData.parentForm= parentForm;
    $scope.apData.idKey= "title";
    alphaplusService.business.processForm($scope);
    
    $scope.update= function(formData){
        formData.data.addressStr=
        "[ "+formData.data.title+" : "
            +formData.data.addressLine1+", "
            +formData.data.addressLine2+", "
            +formData.data.addressLine3+", "
            +formData.data.city+", "
            +formData.data.pincode+", "
            +formData.data.state+", "
            +formData.data.country+" ]";

        $scope.apData.submitFormData= formData;
        $scope.apData.eventName= parentForm;
        $scope.apData.boDetailKey= "boData";
        $scope.apData.editRow= editRow;
        $scope.apData.parentForm= parentForm;

        alphaplusService.business.formUpdateFn($scope);
    };
});

var addressSummaryController= addressControllersM.controller('AddressSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){
    $scope.apData= {};
    $scope.apData.service= "address";
    $scope.apData.idKey= "id";
    $scope.apData.id= primaryKey;
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var addressService= {};
addressService.addressSummaryController= addressSummaryController;
addressService.addressController= addressController;
addressService.addressListController= addressListController;

addressControllersM.constant('addressService', addressService);