var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressListController= addressControllersM.controller('AddressListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){
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
        $location.path(scope.bannerdata.navData.hiddenNavData.address.subNav.update.path);
    };
    $scope.view= function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, "addressID", "html/address/summary.html", "AddressSummaryController")
    };
    $scope.delete= function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };

    $rootScope.$on("processaddress", function(event, addressData){
        if(addressData.parent && addressData.parent===$scope.$parent.parentForm){
            $scope.gridData.rowData.push(addressData.tableRow);
        }
    });
});

var addressController= addressControllersM.controller('AddressController', function($scope, alphaplusService, $routeParams, $rootScope, parentForm){
    $scope.addressDetail= {};
    $scope.addressData= {};
    alphaplusService.address.get({
        action: "getFormData"
    }, function(addressFormResp){
        $scope.addressData= addressFormResp;
        if($routeParams.addressID){
            alphaplusService.business.fetchBO("address", $routeParams.addressID, "addressID", $scope, "addressDetail");
            $scope.addressData.data= $scope.addressDetail;
        }else{
            alphaplusService.business.processFormNewBOInternal($scope.addressData, $scope, "addressDetail");
        }        
    }, function(){
        alert("FormData GET failure");
    });

    $scope.update = function(formData){
        $rootScope.$emit("processaddress", {
            "tableRow": formData.data,
            "parent": parentForm
        });
    };
});

var addressSummaryController= addressControllersM.controller('AddressSummaryController', function($scope, alphaplusService, addressID){
    $scope.addressDetail= {};
    if(addressID){
        alphaplusService.business.fetchBO("address", "addressId", addressID, $scope.clientDetail);
    }
});

var addressService= {};
addressService.addressSummaryController= addressSummaryController;
addressService.addressController= addressController;
addressService.addressListController= addressListController;

addressControllersM.constant('addressService', addressService);