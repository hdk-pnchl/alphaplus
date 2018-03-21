var contactControllersM= angular.module('contactControllersM', ['servicesM', 'ui.bootstrap']);

var contactController= contactControllersM.controller('ContactControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, contactTableParams, ipContact, editMode){
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
        if(!editMode){        
            formData.contacts.push(newContact);  
            contactTableParams.data.push(newContact); 
        }
    };    
    $scope.buildNewContactStr= function(newContact){
        newContact.contactStr= "[ "+newContact.title+" : "+newContact.no+" ]";
    };
});

var contactListController= addressControllersM.controller('ContactListController', 
    function($scope, $rootScope, $uibModal, alphaplusService){
    $scope.dPropData= $scope.$parent.dPropData;
    $scope.service= "contact";
    alphaplusService.business.processColumn($scope);

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                //addListCtrlScope.dynalicCtrlScope.form-field
                parentForm: $scope.dPropData.parentForm,
                editRow: editRow
            },
            templateURL: "element/html/business/crud/form.html",
            controller: "ContactController",
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
            controller: "ContactSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var contactController= contactControllersM.controller('ContactController', 
    function($scope, alphaplusService, parentForm, editRow){

    $scope.apData= {};
    $scope.apData.service= "contact";
    $scope.apData.boDetailKey= "boData";
    $scope.apData.editRow= editRow;
    $scope.apData.parentForm= parentForm;
    $scope.apData.idKey= "title";
    alphaplusService.business.processForm($scope);
    
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope);
    };
});

var contactSummaryController= contactControllersM.controller('ContactSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){
    $scope.apData= {};
    $scope.apData.service= "contact";
    $scope.apData.idKey= "id";
    $scope.apData.id= primaryKey;
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var contactService= {};
contactService.contactSummaryController= contactSummaryController;
contactService.contactController= contactController;
contactService.contactListController= contactListController;

contactControllersM.constant('contactService', contactService);