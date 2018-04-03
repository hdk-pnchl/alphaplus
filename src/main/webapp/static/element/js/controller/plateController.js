var plateControllersM= angular.module('plateControllersM', ['servicesM', 'ui.bootstrap']);

plateControllersM.controller('PlateControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, plateTableParams, ipPlate, editMode){
    $scope.newPlate= ipPlate;        
    $scope.isValidRequest= true;
    $scope.validation= {};
    $scope.validation.set_fsdo= true;
    $scope.validation.set_bake= true;
    $scope.total_fsdo= 0;

    //validation
    $scope.validate_set_fsdo= function(){
        $scope.total_fsdo= $scope.newPlate.frontBack + $scope.newPlate.selfBack + $scope.newPlate.doubleGripper + $scope.newPlate.oneSide;
        if($scope.total_fsdo != $scope.newPlate.total){
            $scope.validation.set_fsdo= false;
        }else{
            $scope.validation.set_fsdo= true;
        }
    };
    $scope.validate_set_bake= function(){
        if($scope.newPlate.bake > $scope.newPlate.total){
            $scope.validation.set_bake= false;
        }else{
            $scope.validation.set_bake= true;
        }
    };
    $scope.validate= function(argument) {
        $scope.validate_set_bake();
        $scope.validate_set_fsdo();
        if($scope.validation.set_fsdo && $scope.validation.set_bake){
            $scope.isValidRequest= true;
        }else{
            $scope.isValidRequest= false;
        }
        return $scope.isValidRequest;
    };
    //form
    $scope.calculateTotalSet= function(){
        $scope.newPlate.total= $scope.newPlate.theSet * $scope.newPlate.theSetColour;
    };
    //submit
    $scope.addNewPlate= function(form){
        if(form.$valid && $scope.validate()){
            $scope.processNewPlate(angular.copy($scope.newPlate));
            $uibModalInstance.close();                
        }
    };   
    $scope.processNewPlate= function(){
        $scope.buildPlatePropTostr();
        $scope.newPlate.jobID= formData.id;
        alphaplusService.job.save({
            action: "plate",
        }, 
        $scope.newPlate, 
        function(response){
            if(response.responseData.ERROR){
            }else{
                formData= response.responseEntity; 
                if(!editMode){
                    plateTableParams.data.push(response.responseData.plate); 
                }                             
            }            
        });         
    };
    $scope.buildPlatePropTostr= function() {
        $scope.newPlate.fb_sb_dg_os= $scope.newPlate.frontBack+"/"+$scope.newPlate.selfBack+"/"+$scope.newPlate.doubleGripper+"/"+$scope.newPlate.oneSide;            
        $scope.newPlate.plateSize= $scope.newPlate.plateHeight+"/"+$scope.newPlate.plateWidth+" "+$scope.newPlate.plateUnit;
        $scope.newPlate.paperSize= $scope.newPlate.paperHeight+"/"+$scope.newPlate.paperWidth+" "+$scope.newPlate.paperUnit;  
        $scope.newPlate.setStr= $scope.newPlate.bake+"/"+$scope.newPlate.total+"("+$scope.newPlate.theSet+"*"+$scope.newPlate.theSetColour+")"; 
        $scope.newPlate.gripper_screen= $scope.newPlate.gripper+"|"+$scope.newPlate.screen
    }; 
    $scope.closeModal= function(argument) {
        $uibModalInstance.close();
    };
});