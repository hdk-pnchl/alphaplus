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
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/crud/summary.html", "MessageSummaryController", $uibModal);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var messageController= messageControllersM.controller('MessageController', function($scope, alphaplusService, $routeParams){
    var searchIp= {};
    searchIp.type= "PK";
    searchIp.pkId= $routeParams.messageID;
    searchIp.pkIdName= "id";
    //valueData is used for new-message, to override name and value in form.
    if(!$routeParams.messageID){
        $scope.valueData= {};
        $scope.valueData.name= alphaplusService.obj.bannerData.USER_DATA.name;
        $scope.valueData.emailID= alphaplusService.obj.bannerData.USER_DATA.emailID;
    }
    alphaplusService.business.processForm($scope, "message", "boData", searchIp, "", "");

    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope, formData);
    };
});

var messageSummaryController= messageControllersM.controller('MessageSummaryController', function($scope, alphaplusService, primaryKey, ipObj){
    $scope.boDetail= {};
    alphaplusService.business.processSummary("message", "id", primaryKey, $scope, "boDetail", ipObj);
});

var messageService= {};
messageService.messageSummaryController= messageSummaryController;
messageService.messageController= messageController;
messageService.messageListController= messageListController;

messageControllersM.constant('messageService', messageService);