var contactControllersM= angular.module('contactControllersM', ['servicesM', 'ui.bootstrap']);

var contactController= contactControllersM.controller('ContactControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, contactTableParams, ipContact, editMode, service){
    $scope.newContact= ipContact;        
    $scope.isLabelTaken= false;   
    $scope.addNewContact= function(newContact, form){
        if(form.$valid && !$scope.isLabelTaken){
            $scope.processNewContact(angular.copy(newContact));
            $uibModalInstance.close();
        }
    };
    $scope.checkIfContactLabelTaken= function(newContact){
        newContact.$isp_ele= true
        if(alphaplusService.business.isThereProp(formData.contacts, "title", newContact.title)){
            $scope.isLabelTaken= true;
        }else{
            $scope.isLabelTaken= false;
        }
        delete newContact.$isp_ele;
    };
    $scope.processNewContact= function(newContact){
        $scope.buildNewContactStr(newContact);
        $scope.newContact.parentId= formData.id;
        alphaplusService[service.value].save({action: "contact"}, $scope.newContact, 
        function(response){
            if(response.responseData.ERROR){
            }else{
                formData= response.responseEntity;
                if(!editMode){
                    contactTableParams.data.push(response.responseData.contact); 
                }
            }
        });    
    };    
    $scope.buildNewContactStr= function(newContact){
        $scope.newContact.contactStr= "[ "+$scope.newContact.title+" : "+$scope.newContact.no+" ]";
    };
    $scope.closeModal= function(argument) {
        $uibModalInstance.close();
    };    
});