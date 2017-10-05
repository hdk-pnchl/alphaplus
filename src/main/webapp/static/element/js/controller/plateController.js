var plateControllersM= angular.module('plateControllersM', ['servicesM', 'ui.bootstrap']);

var plateListController= plateControllersM.controller('PlateListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("plate", $scope, "processplateDetail");

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                parentForm: $scope.$parent.parentForm,
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
                ipID: viewRow.id,
                editRow: viewRow
            },
            templateURL: "element/html/business/crud/form.html",
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
    $scope.exec.valdn= {};
    $scope.exec.valdn.total= {};
    $scope.exec.valdn.total.fbd= function(scope){
        var result= {}; 
        result.isSuccess= true;
        var total= scope.formData.data.theSet * scope.formData.data.theSetColour;
        var totalInternal= scope.formData.data.F_B + scope.formData.data.S_B + scope.formData.data.D_G + scope.formData.data.O_S;
        if(totalInternal != total){
            result.isSuccess= false;
            result.errStr= "["+totalInternal+"] (F_B + S_B + D_G + O_S) should equal ["+total+"] (set * colour)";
        }
        scope.formData.data.total= total;
        return result;
    };
    $scope.exec.valdn.bake= {};
    $scope.exec.valdn.bake.compareWithTotal= function(scope){
        var result= {}; 
        result.isSuccess= true;
        var total= scope.formData.data.theSet * scope.formData.data.theSetColour;
        if(scope.formData.bake > total){
            result.isSuccess= false;
            result.errStr= "["+scope.formData.bake+"] should not be greater than ["+total+"] (set * colour)";
        }
        return result;
    };
    alphaplusService.business.processForm($scope, "plate", "boData", editRow, parentForm, "title");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processplateDetail", "boData", editRow, parentForm);
    };
});

var plateSummaryController= plateControllersM.controller('PlateSummaryController', function($scope, alphaplusService, ipID, ipObj){
    alphaplusService.business.processSummary("client", "id", ipID, $scope, "boDetail", ipObj);
});

var plateService= {};
plateService.plateSummaryController= plateSummaryController;
plateService.plateController= plateController;
plateService.plateListController= plateListController;

plateControllersM.constant('plateService', plateService);