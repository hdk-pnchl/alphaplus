var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressListController= addressControllersM.controller('AddressListController', 
    function($scope, $rootScope, $location, $uibModal, alphaplusService){
    alphaplusService.address.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("address", $scope, response);
        },
        function(){
            alert('ADDRESS ColumnData failed');
        }
    );
    $scope.edit= function(editRow){
        //$location.path(alphaplusService.obj.bannerData.navData.mainNavData.address.subNav.update.path+"/"+editRow.id);
        var modalInstance= $uibModal.open({
            templateUrl: "element/html/business/address/address.html",
            controller: "AddressController",
            size: 'lg',
            resolve: {
                parentForm: function (){
                    return $scope.$parent.parentForm;
                },
                editRow: function (){
                    return editRow;
                }
            }
        });
        $rootScope.modalInstances[$scope.$parent.parentForm]= modalInstance;
    };
    $scope.view= function(viewRow){
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/address/summary.html", "AddressSummaryController", $uibModal);
    };
    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
    $rootScope.$on("processaddressDetail", function(event, addressData){
        alphaplusService.business.processInternalGrid($scope, addressData, $scope.$parent.parentForm);
    });
});

var addressController= addressControllersM.controller('AddressController', 
    function($scope, alphaplusService, $routeParams, $rootScope, parentForm, editRow){
    $scope.addressDetail= {};
    $scope.addressData= {};
    alphaplusService.address.get({
        action: "getFormData"
    }, function(addressFormResp){
        $scope.addressData= addressFormResp;
        if(editRow){
            var promise= alphaplusService.business.fetchBO("address", editRow.id, "id", $scope, "addressDetail");
            promise.then(function(resp) {
              $scope.addressData.data= resp;
            });
        }else{
            alphaplusService.business.processFormNewBOInternal($scope.addressData, $scope, "addressDetail");
        }
        $scope.addressData.parentForm= {};
        $scope.addressData.parentForm.data= parentForm;
        $scope.addressData.parentForm.name= "name";
        $scope.addressData.parentForm.editRow= editRow;
    }, function(){
        alert("FormData GET failure");
    });

    $scope.update = function(formData){
        $rootScope.$emit("processaddressDetail", {
            "tableRow": formData.data,
            "parent": parentForm
        });
        var modalInstances= $rootScope.modalInstances[parentForm];
        if(modalInstances){
            modalInstances.close();
        }
    };
});


var addressSummaryController= addressControllersM.controller('AddressSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.addressDetail= {};
    alphaplusService.business.processSummary("address", "id", ipID, $scope, "addressDetail", ipObj);
});

var addressService= {};
addressService.addressSummaryController= addressSummaryController;
addressService.addressController= addressController;
addressService.addressListController= addressListController;

addressControllersM.constant('addressService', addressService);