var jobControllersM= angular.module('jobControllersM', ['servicesM', 'ui.bootstrap']);

var jobListController= jobControllersM.controller('JobListController', function($scope, $location, $uibModal, alphaplusService){ 
    alphaplusService.job.query({
            action: "getColumnData"
        },
        function(response){
            $scope.gridData= {};
            $scope.gridData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= [];

            $scope.fetchJobs(searchIp); 
        },
        function(){
            alert('Core getColumnData failed');
        }
    );
    $scope.editJob = function(editRow){
        var summaryPath= '/add/'+editRow.jobId;
        $location.path(summaryPath);
    };
    $scope.viewJob = function(viewRow){ 
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'html/jobSummary.html',
            controller: 'jobSummaryController',
            size: 'lg',
            resolve:{
                    jobId: function (){
                    return viewRow.jobId;
                }
            }
        });
    };
    $scope.deleteJob = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
    
    $scope.fetchJobs = function(searchIp){
        alphaplusService.job.save({
                action: "search",
                searchIp: searchIp
            },
            searchIp,
            function(response){
                $scope.gridData.rowData= response.responseEntity;
                $scope.gridData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                $scope.gridData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                $scope.gridData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);
                $scope.gridData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));
            },
            function(response){
                alert("job getAllBySeach by IP failure");
            }
        );
    };
});

var jobFormController= jobControllersM.controller('JobFormController', function($scope, alphaplusService, $routeParams){
    $scope.jobData= {};
    alphaplusService.job.get({
        action: "getFormData"
    }, function(jobFormResp){
        $scope.jobData= jobFormResp;
        if($routeParams.jobID){
            alphaplusService.job.get({
                action: "get",
                jobID: $routeParams.jobID
            }, function(jobResp){
                $scope.jobData.data= jobResp.responseEntity;
            }, function(){
                alert("Job get failure");
            });
        }
    }, function(){
        alert("getFormData get failure");
    });

    $scope.update = function(data){
        alphaplusService.job.save({
            action: "update"
        }, 
        data,
        function(jobResp){
            alert("Job updated :)");
        }, function(){
            alert("Job updated failure");
        });        
    };
});

var jobController= jobControllersM.controller('JobController', function($scope, $route, $routeParams, $location, $http, alphaplusService){
    $scope.formService= alphaplusService;
    alphaplusService.job.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.jobWizzard= response;
            $scope.jobDetail= {};
            if($routeParams.jobID){
                 alphaplusService.job.get({
                    action: "get",
                    jobID: $routeParams.jobID
                }, function(jobDataResp){
                    $scope.jobDetail= jobDataResp;
                    angular.forEach($scope.jobWizzard.wizzardData, function(formIpData, formName){
                        /*
                        if($scope.jobDetail[formName]){
                            formIpData.data= $scope.jobDetail[formName];
                        }else{
                            formIpData.data= $scope.jobDetail;
                        }
                        */
                        angular.forEach(formIpData.fieldAry, function(field){
                            formIpData.data[field.name]= $scope.jobDetail[field.name];
                        });
                    });
                }, function(){
                    alert("Job get failure");
                });
            }else{
                angular.forEach($scope.jobWizzard.wizzardData, function(formIpData, formName){
                    $scope.jobDetail[formName]= {};
                    angular.forEach(formIpData.fieldAry, function(field){
                        if(field.type=="date"){
                            $scope.jobDetail[formName][field.name]= new Date();
                        }else if(field.type=="model"){
                            $scope.jobDetail[formName][field.name]= new Date();
                        }else{
                            $scope.jobDetail[formName][field.name]= "";
                        }
                        if(field.readOnly){
                            $scope.jobDetail[formName][field.name]= "Will be auto populated.";
                        }
                    });
                    formIpData.data= $scope.jobDetail[formName];
                });
            }
            $scope.jobDetail.isReady= true;
        }, 
        function(){ 
            alert('Job getWizzardData failure');
        }
    );  
 
    $scope.selectWizzardStep= function(selectedWizzardStep){
        angular.forEach($scope.jobWizzard.wizzardStepData, function(wizzardStep){
            wizzardStep.active= false;
            wizzardStep.class= '';
        });    
        selectedWizzardStep.active= true;
        selectedWizzardStep.class= 'active';

        angular.forEach($scope.jobWizzard.wizzardData, function(value, key){
            value.isHidden = true;
        });    
        $scope.jobWizzard.wizzardData[selectedWizzardStep.name].isHidden=false;
    };
 
    $scope.isLastStep= function(step) {
       if(step == $scope.jobWizzard.commonData.lastStep){
            return true;
       }
       return false;
    }

    $scope.submitJob = function(jobDataType, jobData){
        var service= alphaplusService[jobDataType];
        var action= "save";
        if($scope.jobDetail[jobDataType] && $scope.jobDetail[jobDataType].id){
            action= "update";
            jobData["id"]= $scope.jobDetail[jobDataType]["id"];
        }
        //server call
        service.save({
                action: action,
                patientId: $scope.jobDetail.id
            }, 
            jobData, 
            function(persistedJobData){
                if(persistedjobData.responseData && persistedJobData.responseData.ERROR_MSG){
                    alert(persistedJobData.responseData.ERROR_MSG);
                }else{
                    $scope.jobDetail= persistedJobData.responseEntity;
                    //if its last step, redirect to patient-grid
                    if($scope.isLastStep(jobDataType)){
                        $location.path($scope.$parent.bannerdata.navData.mainNavData.job.subNav[0].path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= $scope.jobWizzard.wizzardStepData[jobDataType];
                        currentWizzardStep.submitted= true;
                        //move to next step in the wizzard
                        $scope.selectWizzardStep($scope.jobWizzard.wizzardStepData[currentWizzardStep.next]);
                    }
                }
            },
            function(){
                alert("job save failure");
            }
        );
    };
});

var jobSummaryController= jobControllersM.controller('JobSummaryController', function($scope, alphaplusService, jobID){
    $scope.jobDetail= {};
    if(jobID){
         alphaplusService.job.get({
            action: "get",
            jobID: jobID
        }, function(jobDataResp){
            $scope.jobDetail= jobDataResp;
        }, function(){
            alert("job get failure");
        });
    }
});

var jobService= {};
jobService.jobSummaryController= jobSummaryController;
jobService.jobController= jobController;
jobService.jobFormController= jobFormController;
jobService.jobListController= jobListController;

jobControllersM.constant('jobService', jobService);