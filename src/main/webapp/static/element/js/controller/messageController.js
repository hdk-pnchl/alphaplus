var messageControllersM= angular.module('messageControllersM', ['servicesM', 'ui.bootstrap']);

var messageListController= messageControllersM.controller('MessageListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.message.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("message", $scope, response);
        },
        function(){
            alert('Message GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path(alphaplusService.obj.bannerData.navData.mainNavData.message.subNav.update.path+"/"+editRow.id);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/message/summary.html", "MessageSummaryController", $uibModal);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var messageController= messageControllersM.controller('MessageController', function($scope, alphaplusService, $routeParams){
    $scope.messageDetail= {};
    $scope.messageData= {};

    alphaplusService.message.get({
        action: "getFormData"
    }, function(messageFormResp){
        $scope.messageData= messageFormResp;
        if($routeParams.messageID){
            alphaplusService.business.fetchBO("message", $routeParams.messageID, "id", $scope, "messageDetail");
            $scope.messageData.data= $scope.messageDetail;
        }else{
            alphaplusService.business.processFormNewBOInternal($scope.addressData, $scope, "addressDetail");
        }
    }, function(){
        alert("getFormData get failure");
    });

    $scope.update = function(formData){
        alphaplusService.message.save({
            action: "saveOrUpdate"
        }, 
        formData.data,
        function(messageResp){
            alert("Message answered :)");
        }, function(){
            alert("Message save failure");
        });
    };
});

var messageSummaryController= messageControllersM.controller('MessageSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.messageDetail= {};
    alphaplusService.business.processSummary("message", "id", ipID, $scope, "messageDetail", ipObj);
});

var messageService= {};
messageService.messageSummaryController= messageSummaryController;
messageService.messageController= messageController;
messageService.messageListController= messageListController;

messageControllersM.constant('messageService', messageService);