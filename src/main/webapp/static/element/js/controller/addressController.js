var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressController= addressControllersM.controller('AddressControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, addressTableParams, ipAddress, editMode, service){
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
    $scope.processNewAddress= function(){
        $scope.buildNewAddressStr();
        $scope.newAddress.parentId= formData.id;
        alphaplusService[service.value].save({action: "address"}, $scope.newAddress, 
        function(response){
            if(response.responseData.ERROR){
            }else{
                formData= response.responseEntity;
                if(!editMode){
                    addressTableParams.data.push(response.responseData.address); 
                }
            }
        });                
    };
    $scope.buildNewAddressStr= function() {
        $scope.newAddress.addressStr=
        "[ "+$scope.newAddress.title+" : "
            +$scope.newAddress.addressLine1+", "
            +$scope.newAddress.addressLine2+", "   
            +$scope.newAddress.addressLine3+", "
            +$scope.newAddress.city+", "
            +$scope.newAddress.pincode+", "
            +$scope.newAddress.state+", "
            +$scope.newAddress.country+" ]";            
    };   
    $scope.closeModal= function(argument) {
        $uibModalInstance.close();
    };       
});