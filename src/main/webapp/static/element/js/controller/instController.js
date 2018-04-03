var instControllersM= angular.module('instControllersM', ['servicesM', 'ui.bootstrap']);

instControllersM.controller('InstructionControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, instructionTableParams, ipInstruction, editMode, ipPart){
    $scope.newInstruction= ipInstruction;        

    $scope.addNewInstruction= function(form){
        if(form.$valid){
            $scope.processNewInstruction(angular.copy($scope.newInstruction));
            $uibModalInstance.close();                
        }
    };   
    $scope.processNewInstruction= function(){
        $scope.newInstruction.part= ipPart.value;
        $scope.newInstruction.jobID= formData.id;
        alphaplusService.job.save({
            action: "instruction"
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