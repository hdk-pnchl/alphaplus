var addressControllersM= angular.module('addressControllersM', ['servicesM', 'ui.bootstrap']);

var addressListController= addressControllersM.controller('AddressListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.address.query({
            action: "getColumnData"
        },
        function(response){
            $scope.gridData= {};
            $scope.gridData.columnData= response;
            
            alphaplusService.business.fetchBOList("address", $scope);
        },
        function(){
            alert('ADDRESS getColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path(scope.bannerdata.navData.hiddenNavData.address.subNav.update.path);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, "addressID", "html/address/summary.html", "AddressSummaryController")
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };

    $rootScope.$on("processAddress", function(event, address){
       $scope.gridData.rowData.push(address)
    });
});

var addressController= addressControllersM.controller('AddressController', function($scope, alphaplusService, $routeParams, $rootScope){
    $scope.addressDetail= {};
    $scope.addressData= {};
    alphaplusService.address.get({
        action: "getFormData"
    }, function(addressFormResp){
        $scope.addressData= addressFormResp;
        if($routeParams.addressID){
            alphaplusService.business.fetchBO("address", $routeParams.addressID, "addressID", $scope, "addressDetail");
            $scope.addressData.data= $scope.addressDetail;
        }
    }, function(){
        alert("FormData GET failure");
    });

    $scope.update = function(data){
        $rootScope.$emit("processAddress", data);
    };
});

var addressSummaryController= addressControllersM.controller('AddressSummaryController', function($scope, alphaplusService, addressID){
    $scope.addressDetail= {};
    if(clientID){
        alphaplusService.business.fetchBO("address", "addressId", addressID, $scope.clientDetail);
    }
});

var addressService= {};
addressService.addressSummaryController= addressSummaryController;
addressService.addressController= addressController;
addressService.addressListController= addressListController;

addressControllersM.constant('addressService', addressService);