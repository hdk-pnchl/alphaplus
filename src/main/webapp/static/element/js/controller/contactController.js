var contactControllersM= angular.module('contactControllersM', ['servicesM', 'ui.bootstrap']);

var contactListController= contactControllersM.controller('ContactListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.contact.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("contact", $scope, response);
        },
        function(){
            alert('Contact: GET ColumnData failed');
        }
    );
    $scope.edit= function(editRow){
        $location.path(scope.bannerdata.navData.hiddenNavData.contact.subNav.update.path);
    };
    $scope.view= function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, "contactID", "html/contact/summary.html", "ContactSummaryController")
    };
    $scope.delete= function(deleteRow){ 
        alert("Contact: Delete not possible yet. Work in progress.");
    };

    $rootScope.$on("processContact", function(event, contactData){
        if(contactData.parent && contactData.parent===$scope.$parent.parentForm){
            $scope.gridData.rowData.push(contactData.tableRow);
        }
    });
});

var contactController= contactControllersM.controller('ContactController', function($scope, alphaplusService, $routeParams, $rootScope, parentForm){
    $scope.contactDetail= {};
    $scope.contactData= {};
    alphaplusService.contact.get({
        action: "getFormData"
    }, function(formResp){
        $scope.contactData= formResp;
        if($routeParams.contactID){
            alphaplusService.business.fetchBO("contact", $routeParams.contactID, "contactID", $scope, "contactDetail");
            $scope.addressData.data= $scope.contactDetail;
        }
    }, function(){
        alert("Contact: FormData GET failure");
    });

    $scope.update = function(formData){
        $rootScope.$emit("processContact", {
            "tableRow": formData.data,
            "parent": parentForm
        });
    };    
});

var contactSummaryController= contactControllersM.controller('ContactSummaryController', function($scope, alphaplusService, contactID){
    $scope.contactDetail= {};
    if(contactID){
        alphaplusService.business.fetchBO("contact", "contactID", contactID, $scope.clientDetail);
    }
});

var contactService= {};
contactService.contactSummaryController= contactSummaryController;
contactService.contactController= contactController;
contactService.contactListController= contactListController;

contactControllersM.constant('contactService', contactService);