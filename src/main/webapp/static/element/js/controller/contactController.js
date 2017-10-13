var contactControllersM= angular.module('contactControllersM', ['servicesM', 'ui.bootstrap']);

var contactListController= addressControllersM.controller('ContactListController', function($scope, $rootScope, $uibModal, alphaplusService){
    alphaplusService.business.processColumn("contact", $scope, "processcontactDetail");

    $scope.edit= function(editRow){
        var ipObj= {
            modalData: {
                parentForm: $scope.$parent.parentForm,
                editRow: editRow
            },
            templateURL: "element/html/business/crud/form.html",
            controller: "ContactController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.view= function(viewRow){
        var ipObj= {
            modalData: {
                primaryKey: viewRow.id,
                editRow: viewRow
            },
            templateURL: "element/html/business/crud/summary.html",
            controller: "ContactSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };

    $scope.delete= function(deleteRow){
        alert("Delete not possible yet. Work in progress.");
    };
});

var contactController= contactControllersM.controller('ContactController', function($scope, alphaplusService, parentForm, editRow){
    alphaplusService.business.processForm($scope, "contact", "boData", editRow, parentForm, "name");
    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData, "processcontactDetail", "boData", editRow, parentForm);
    };
});

var contactSummaryController= contactControllersM.controller('ContactSummaryController', 
    function($scope, alphaplusService, primaryKey, ipObj){
    alphaplusService.business.processSummary("contact", "id", primaryKey, $scope, "boDetail", ipObj);
});

var contactService= {};
contactService.contactSummaryController= contactSummaryController;
contactService.contactController= contactController;
contactService.contactListController= contactListController;

contactControllersM.constant('contactService', contactService);