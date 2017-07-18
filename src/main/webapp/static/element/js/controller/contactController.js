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
        $location.path($scope.$parent.bannerData.navData.mainNavData.contact.subNav.update.path+"/"+editRow.id);
    };
    $scope.view= function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/contact/summary.html", "ContactSummaryController", $uibModal);
    };
    $scope.delete= function(deleteRow){ 
        alert("Contact: Delete not possible yet. Work in progress.");
    };

    $rootScope.$on("processcontactDetail", function(event, contactData){
        alphaplusService.business.processInternalGrid($scope, contactData, $scope.$parent.parentForm);
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
            alphaplusService.business.fetchBO("contact", $routeParams.contactID, "id", $scope, "contactDetail");
            $scope.contactData.data= $scope.contactDetail;
        }else{
            alphaplusService.business.processFormNewBOInternal($scope.contactData, $scope, "contactDetail");
        }
        $scope.contactData.parentForm= {};
        $scope.contactData.parentForm.data= parentForm;
        $scope.contactData.parentForm.name= "name";
    }, function(){
        alert("Contact: FormData GET failure");
    });

    $scope.update = function(formData){
        $rootScope.$emit("processcontactDetail", {
            "tableRow": formData.data,
            "parent": parentForm
        });
    };    
});

var contactSummaryController= contactControllersM.controller('ContactSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.contactDetail= {};
    alphaplusService.business.processSummary("contact", "id", ipID, $scope, "contactDetail", ipObj);
});

var contactService= {};
contactService.contactSummaryController= contactSummaryController;
contactService.contactController= contactController;
contactService.contactListController= contactListController;

contactControllersM.constant('contactService', contactService);