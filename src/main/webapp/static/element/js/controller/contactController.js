var contactControllersM= angular.module('contactControllersM', ['servicesM', 'ui.bootstrap']);

var contactListController= addressControllersM.controller('ContactListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("contact", $scope, "processcontactDetail");

    $scope.edit= function(editRow){
        var ipObj= {
            parentForm: $scope.$parent.parentForm,
            editRow: editRow
        };
        alphaplusService.business.viewBO("element/html/business/contact/contact.html", "ContactController", $uibModal, ipObj);
    };

    $scope.view= function(viewRow){
        var ipObj= {
            ipID: viewRow.id,
            ipObj: viewRow
        };
        alphaplusService.business.viewBO("element/html/business/contact/summary.html", "ContactSummaryController", $uibModal, ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var contactController= contactControllersM.controller('ContactController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "contact", "contactData", editRow, parentForm, "name");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processcontactDetail", "contactData", editRow, parentForm);
    };
});

var contactSummaryController= contactControllersM.controller('ContactSummaryController', function($scope, alphaplusService, ipID, ipObj){
    alphaplusService.business.processSummary("contact", "id", ipID, $scope, "contactDetail", ipObj);
});

var contactService= {};
contactService.contactSummaryController= contactSummaryController;
contactService.contactController= contactController;
contactService.contactListController= contactListController;

contactControllersM.constant('contactService', contactService);