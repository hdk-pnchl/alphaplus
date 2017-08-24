var jobInstControllersM= angular.module('jobInstControllersM', ['servicesM', 'ui.bootstrap']);

var jobInstListController= jobInstControllersM.controller('JobInstListController', 
    function($scope, $location, $uibModal, alphaplusService, $rootScope){
    alphaplusService.jobInst.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("jobInst", $scope, response);
        },
        function(){
            alert('JobInst GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        var modalInstance= $uibModal.open({
            templateUrl: "element/html/business/job/instruction/jobInst.html",
            controller: "JobInstController",
            size: 'lg',
            resolve: {
                parentForm: function (){
                    return "job.instructions";
                },
                jobInst: function (){
                    return editRow;
                }
            }
        });
        $rootScope.modalInstances["job.instructions"]= modalInstance;
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/job/instruction/summary.html", "JobInstSummaryController", $uibModal);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };

    $rootScope.$on("processinstructions", function(event, jobInstData){
        alphaplusService.business.processInternalGrid($scope, jobInstData, $scope.$parent.parentForm);
    });    
});

var jobInstController= jobInstControllersM.controller('JobInstController', 
    function($scope, alphaplusService, $routeParams, $rootScope, parentForm, jobInst){

    $scope.jobInstDetail= {};
    $scope.jobInstData= {};
    alphaplusService.jobInst.get({
        action: "getFormData"
    }, function(response){
        $scope.jobInstData= response;
        if(jobInst || $routeParams.jobInstID){
            if(jobInst){
                $scope.jobInstDetail= jobInst;
            }else{
                alphaplusService.business.fetchBO("jobInst", jobInstID, "id", $scope, "jobInstDetail", jobInst);
            }
            $scope.jobInstData.data= $scope.jobInstDetail;
        }else{
            alphaplusService.business.processFormNewBOInternal($scope.jobInstData, $scope, "jobInstDetail");
        }
    }, function(){
        alert("FormData GET failure");
    });

    $scope.update = function(formData){
        if(!jobInst && !$routeParams.jobInstID){
            $rootScope.$emit("processinstructions", {
                "tableRow": formData.data,
                "parent": parentForm
            });
        }else{
            var modalInstances= $rootScope.modalInstances["job.instructions"];
            if(modalInstances){
                modalInstances.close();
            }
        }
    };
});

var jobInstSummaryController= jobInstControllersM.controller('JobInstSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.jobInstDetail= {};
    alphaplusService.business.processSummary("jobInst", "id", ipID, $scope, "jobInstDetail", ipObj);
});

var jobInstService= {};
jobInstService.jobInstSummaryController= jobInstSummaryController;
jobInstService.jobInstController= jobInstController;
jobInstService.jobInstListController= jobInstListController;

jobInstControllersM.constant('jobInstService', jobInstService);