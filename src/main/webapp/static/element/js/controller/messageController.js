var messageControllersM= angular.module('messageControllersM', ['servicesM', 'ui.bootstrap']);

//------------------------------------MESSAGE

messageControllersM.controller('MessageListController', function($scope, alphaplusService, $uibModal){
    alphaplusService.message.query({
            action: "getColumnData"
        }, 
        function(response){
            $scope.messageGridtData= {};
            $scope.messageGridtData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= {};

            $scope.fetchMessages(searchIp); 
        }, 
        function(){ 
            alert('Core geColumnData failed');
        }
    );
    $scope.editMessage = function(editRow){
        alert("Op not implemented!");
    };
    $scope.viewMessage = function(viewRow){ 
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'html/message/summary.html',
            controller: 'MessageSummaryController',
            size: 'lg',
            resolve:{
                messageID: function (){
                    return viewRow.id;
                }
            }
        });        
    };    
    $scope.deleteMessage = function(deleteRow){ 
        alert("Op not implemented!");
    };
    $scope.fetchMessages = function(searchIp){
        alphaplusService.message.save({
                action: "search",
                searchIp: searchIp
            }, 
            searchIp, 
            function(response){
                $scope.messageGridtData.rowData= response.responseEntity;
                $scope.messageGridtData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                $scope.messageGridtData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                $scope.messageGridtData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);                
                $scope.messageGridtData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));                
            },
            function(response){
                alert("Message search by ip failure");
            }
        );
    };
});

messageControllersM.controller('MessageController', function($scope, alphaplusService, $routeParams){
    $scope.messageData= {};
    alphaplusService.message.get({
        action: "getFormData"
    }, function(messageFormResp){
        $scope.messageData= messageFormResp;
        if($routeParams.messageID){
            alphaplusService.message.get({
                action: "get",
                messageID: $routeParams.messageID
            }, function(messageResp){
                $scope.messageData.data= messageResp.responseEntity;
            }, function(){
                alert("Message get failure");
            });
        }
    }, function(){
        alert("getFormData get failure");
    });

    $scope.update = function(data){
        alphaplusService.message.save({
            action: "update"
        }, 
        data,
        function(messageResp){
             alert("Message answered :)");
        }, function(){
            alert("Message save failure");
        });        
    };
});

messageControllersM.controller('MessageSummaryController', function($scope, alphaplusService, messageID){
    $scope.messageDetail= {};
    if(messageID){
         alphaplusService.message.get({
            action: "get",
            messageID: messageID
        }, function(messageDataResp){
            $scope.messageDetail= messageDataResp;
        }, function(){
            alert("Message get failure");
        });
    }
});