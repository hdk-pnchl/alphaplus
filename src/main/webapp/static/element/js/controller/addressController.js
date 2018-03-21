var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressController= addressControllersM.controller('AddressControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, addressTableParams, ipAddress, editMode){
    $scope.newAddress= ipAddress;        
    $scope.isLabelTaken= false;
    $scope.addNewAddress= function(newAddress, form){
        if(form.$valid && !$scope.isLabelTaken){
            $scope.processNewAddress(angular.copy(newAddress));
            $uibModalInstance.close();
        }
    };
    $scope.checkIfAddressLabelTaken= function(newAddress){
        newAddress.$isp_ele= true
        if(alphaplusService.business.isThereProp(formData.addresses, "title", newAddress.title)){
            $scope.isLabelTaken= true;
        }else{
            $scope.isLabelTaken= false;
        }
        delete newAddress.$isp_ele;
    };    
    $scope.processNewAddress= function(newAddress){
        $scope.buildNewAddressStr(newAddress);
        if(!editMode){
            formData.addresses.push(newAddress);  
            addressTableParams.data.push(newAddress); 
        }
    };
    $scope.buildNewAddressStr= function(newAddress) {
        newAddress.addressStr=
        "[ "+newAddress.title+" : "
            +newAddress.addressLine1+", "
            +newAddress.addressLine2+", "   
            +newAddress.addressLine3+", "
            +newAddress.city+", "
            +newAddress.pincode+", "
            +newAddress.state+", "
            +newAddress.country+" ]";            
    };      
});


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