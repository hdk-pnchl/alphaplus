var plateControllersM= angular.module('plateControllersM', ['servicesM', 'ui.bootstrap']);

plateControllersM.controller('PlateControllerN', 
    function($scope, alphaplusService, $uibModalInstance, formData, plateTableParams, ipPlate, editMode){
    $scope.newPlate= ipPlate;        
    $scope.isValidRequest= true;
    $scope.validation= {};
    $scope.validation.set_fsdo= true;
    $scope.validation.set_bake= true;
    $scope.total_fsdo= 0;

    //request validation
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
    //plate-form
    $scope.addNewPlate= function(form){
        if(form.$valid && $scope.validate()){
            $scope.processNewPlate(angular.copy($scope.newPlate));
            $uibModalInstance.close();                
        }
    };   
    $scope.processNewPlate= function(){
        $scope.buildPlatePropTostr();
        alphaplusService.job.save({
            action: "plate",
            jobID: formData.id
        }, 
        $scope.newPlate, 
        function(response){
            if(response.responseData.ERROR){
            }else{
                formData= response.responseEntity;
                if(!editMode){
                    formData.plates.push(response.responseData.plate);  
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

var plateListController= plateControllersM.controller('PlateListController', 
    function($scope, $rootScope, $uibModal, alphaplusService){
    $scope.dPropData= $scope.$parent.dPropData;
    $scope.service= "plate";
    alphaplusService.business.processColumn($scope);
    
    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                //addListCtrlScope.dynalicCtrlScope.form-field
                parentForm: $scope.dPropData.parentForm,
                editRow: editRow
            },
            templateURL: "element/html/business/crud/form.html",
            controller: "PlateController",
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
            controller: "PlateSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var plateController= plateControllersM.controller('PlateController', function($scope, alphaplusService, parentForm, editRow){
    $scope.exec= {};
    $scope.exec.op= {};
    $scope.exec.op.fb_sb_dg_os= {};
    $scope.exec.op.fb_sb_dg_os.toString= function(scope){
        scope.formData.data.fb_sb_dg_os= scope.formData.data.frontBack+"/"+scope.formData.data.selfBack+"/"+scope.formData.data.doubleGripper+"/"+scope.formData.data.oneSide;
    };
    $scope.exec.op.setStr= {};
    $scope.exec.op.setStr.toString= function(scope){
        scope.formData.data.setStr= scope.formData.data.theSet+"*"+scope.formData.data.theSetColour;
    };
    $scope.exec.op.paperSize= {};
    $scope.exec.op.paperSize.toString= function(scope){
        scope.formData.data.paperSize= scope.formData.data.paperHeight+"*"+scope.formData.data.paperWidth+" "+scope.formData.data.paperUnit;
    };
    $scope.exec.op.plateSize= {};
    $scope.exec.op.plateSize.toString= function(scope){
        scope.formData.data.plateSize= scope.formData.data.plateHeight+"*"+scope.formData.data.plateWidth+" "+scope.formData.data.plateUnit;
    };
    $scope.exec.valdn= {};
    $scope.exec.valdn.total= {};
    $scope.exec.valdn.total.fbd= function(scope){
        var result= {}; 
        result.isSuccess= true;
        var total= scope.formData.data.theSet * scope.formData.data.theSetColour;
        var totalInternal= scope.formData.data.frontBack + scope.formData.data.selfBack + scope.formData.data.doubleGripper + scope.formData.data.oneSide;
        if(totalInternal != total){
            result.isSuccess= false;
            result.errStr= "['"+totalInternal+"': (Front-Back + Self Back + Double Gripper + One Side)] should be equal to ['"+total+"': Total: (Set * Colour)]";
        }
        scope.formData.data.total= total;
        return result;
    };
    $scope.exec.valdn.bake= {};
    $scope.exec.valdn.bake.compareWithTotal= function(scope){
        var result= {}; 
        result.isSuccess= true;
        var total= scope.formData.data.theSet * scope.formData.data.theSetColour;
        if(scope.formData.data.bake > total){
            result.isSuccess= false;
            result.errStr= "['"+scope.formData.data.bake+"': Bake] should not be greater than [Total: Set * Colour: '"+total+"']";
        }
        return result;
    };

    $scope.apData= {};
    $scope.apData.service= "plate";
    $scope.apData.boDetailKey= "boData";
    $scope.apData.editRow= editRow;
    $scope.apData.parentForm= parentForm;
    $scope.apData.idKey= "title";

    alphaplusService.business.processForm($scope);
    
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope);
    };
});

var plateSummaryController= plateControllersM.controller('PlateSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){
        
    $scope.apData= {};
    $scope.apData.service= "plate";
    $scope.apData.idKey= "id";
    $scope.apData.id= primaryKey;
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var plateService= {};
plateService.plateSummaryController= plateSummaryController;
plateService.plateController= plateController;
plateService.plateListController= plateListController;

plateControllersM.constant('plateService', plateService);