var instControllersM= angular.module('instControllersM', ['servicesM', 'ui.bootstrap']);

instControllersM.controller('InstructionControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, instructionTableParams, ipInstruction, editMode, ipPart){
    $scope.newInstruction= ipInstruction;        
    $scope.isValidRequest= true;

    $scope.addNewInstruction= function(form){
        if(form.$valid){
            $scope.processNewInstruction(angular.copy($scope.newInstruction));
            $uibModalInstance.close();                
        }
    };   
    $scope.processNewInstruction= function(){
        alphaplusService.job.save({
            action: "instruction",
            jobID: formData.id,
            part: ipPart.value
        }, 
        $scope.newInstruction, 
        function(response){
            if(response.responseData.ERROR){
            }else{
                formData= response.responseEntity;
                if(!editMode){
                    instructionTableParams.data.push(response.responseData.instruction); 
                }                
            }            
        });         
    };
    $scope.closeModal= function(argument) {
        $uibModalInstance.close();
    };
});

var instListController= instControllersM.controller('InstListController', 
    function($scope, $rootScope, $uibModal, alphaplusService){
    $scope.dPropData= $scope.$parent.dPropData;
    $scope.service= "inst";
    alphaplusService.business.processColumn($scope);

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                //addListCtrlScope.dynalicCtrlScope.form-field
                parentForm: $scope.dPropData.parentForm,
                editRow: editRow
            },
            templateURL: "element/html/business/crud/form.html",
            controller: "InstController",
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
            controller: "InstSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var instController= instControllersM.controller('InstController', 
    function($scope, alphaplusService, parentForm, editRow){

    $scope.apData= {};
    $scope.apData.service= "inst";
    $scope.apData.boDetailKey= "boData";
    $scope.apData.editRow= editRow;
    $scope.apData.parentForm= parentForm;
    $scope.apData.idKey= "title";
    alphaplusService.business.processForm($scope);

    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope);
    };
});

var instSummaryController= instControllersM.controller('InstSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){

    $scope.apData= {};
    $scope.apData.service= "inst";
    $scope.apData.idKey= "id";
    $scope.apData.id= primaryKey;
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var instService= {};
instService.instSummaryController= instSummaryController;
instService.instController= instController;
instService.instListController= instListController;

instControllersM.constant('instService', instService);