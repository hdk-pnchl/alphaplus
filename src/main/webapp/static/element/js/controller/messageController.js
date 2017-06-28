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
        $location.path($scope.bannerdata.navData.hiddenNavData.message.subNav.update.path);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, "messageID", "html/message/summary.html", "MessageSummaryController")
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var messageController= messageControllersM.controller('MessageController', function($scope, alphaplusService, $routeParams){
    $scope.messageData= {};
    alphaplusService.message.get({
        action: "getFormData"
    }, function(messageFormResp){
        $scope.messageData= messageFormResp;
        if($routeParams.messageID){
            alphaplusService.business.fetchBO("message", $routeParams.messageID, "messageID", $scope, "messageDetail");
            $scope.messageData.data= $scope.messageDetail;
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

var messageSummaryController= messageControllersM.controller('MessageSummaryController', function($scope, alphaplusService, messageID){
    $scope.messageDetail= {};
    if(messageID){
        alphaplusService.business.fetchBO("message", "messageID", messageID, $scope, "messageDetail");
    }
});

var messageService= {};
messageService.messageSummaryController= messageSummaryController;
messageService.messageController= messageController;
messageService.messageListController= messageListController;

messageControllersM.constant('messageService', messageService);