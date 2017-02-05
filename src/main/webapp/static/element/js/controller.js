var controllersM= angular.module('controllersM', ['servicesM', 'ui.bootstrap']);

//------------------------------------CORE

controllersM.controller('CoreController', function($scope, $http, $location, $rootScope, alphaplusService){
    alphaplusService.core.get({
            action: "getBannerData"
        }, 
        function(response){
            $scope.bannerData= response;
            if($scope.bannerData.navData.configNavData.profile){
                $scope.bannerData.navData.configNavData.profile.title= $scope.bannerData.USER_DATA.basicDetail.name;
                $scope.bannerData.navData.configNavData.profile.subNav.user.path= $scope.bannerData.navData.configNavData.profile.subNav.user.path+"/update/"+$scope.bannerData.USER_DATA.id;                
            }
        }, 
        function(){ 
            alert('getBannerData failed');
        }
    );
    $scope.footerData= {};
    $rootScope.$on("$locationChangeSuccess", function(event, newUrl, oldUrl, newState, oldState){ 
        var xTabName= $location.path().split("/")[1];
        if(xTabName == 'home'){
            $scope.showHome= true;
        }else{
            $scope.showHome= false;
        }
    });
});

//------------------------------------BANNER

controllersM.controller('BannerController', function($scope, alphaplusService){

});


//------------------------------------HOME

controllersM.controller('HomeController', function($scope){
});


//------------------------------------SIGN

controllersM.controller('SignController', function($scope, $location, alphaplusService, $route, $routeParams){
    $scope.user= {};
    $scope.isEmailTaken= false;
    $scope.isPasswordMatching= true;
    $scope.submitBaseURL= alphaplusService.rootPath+"/login";
    $scope.signUp= function(){
    	if($scope.user.basicDetail 
    			&& $scope.user.basicDetail.password 
    			&& $scope.user.basicDetail.confirmPassword 
    			&& $scope.user.basicDetail.emailID){
            if($scope.user.basicDetail.password != $scope.user.basicDetail.confirmPassword){
                $scope.isPasswordMatching= false;
            }else{
                $scope.isPasswordMatching= true;
                //server call: check if email id not already taken
                alphaplusService.core.save({
                    action: "isEmailIdTaken",
                    emailID: $scope.user.basicDetail.emailID
                },{},
                function(response){
                    if(response && response.IS_EMAILID_TAKEN){
                        $scope.isEmailTaken= true;
                    }else{
                        $scope.isEmailTaken= false;
                        //server call: save user
                        alphaplusService.core.save({
                            action: "signUp"
                        }, 
                        $scope.user, 
                        function(userDetail){
                           $location.path($scope.$parent.bannerData.navData.configNavData.signIn.path);
                        }, 
                        function(){
                            alert("User save failure");
                        });
                    }
                }, 
                function(){
                    alert("isEmailIdTaken call failed");
                });
            }    		
    	}
    };
    $scope.forgotPwInitiate= function(){
        $location.path("/user/forgotPassword");
    }
    if($routeParams.error){
        alert("Please enter valid Credentials!");
    }
});

//------------------------------------ABOUT-US

controllersM.controller('AboutUsController', function ($scope) {});

//------------------------------------CONTACT-US

controllersM.controller('ContactUsController', function($scope, alphaplusService){
    $scope.isMsgSubmitted= false;
    $scope.isMsgMessage= false;    
    $scope.alerts= [];
    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };        
    $scope.submitMessage = function(message){ 
        $scope.alerts= [];
        $scope.isMsgSubmitted= false;
        $scope.isMsgMessage= false;
        if(message && message.message){
            console.log("Loggin:"+message);
            if(!message.name){
                message.name= $scope.$parent.bannerData.USER_DATA.basicDetail.name;
            }
            if(!message.emailID){
                message.emailID= $scope.$parent.bannerData.USER_DATA.basicDetail.emailID;
            }   
            alphaplusService.core.save({
                    action: "saveMessage"
                },
                message,
                function(persistedMessage){
                    $scope.message.message= "";
                    $scope.isMsgSubmitted= true;
                    /*
                    $scope.alerts.push({ 
                        type: "success", 
                        msg: "We got your message and shortly will get back to you on it"
                    });
                    */
                },
                function(){
                    alert("Message send failure");
                }
            );
        }else{
            $scope.isMsgMessage= true;
            /*
            $scope.alerts.push({
                type: "danger", 
                msg: "Please enter the Message."
            });
            */
        }
    };
});

//------------------------------------MESSAGE

controllersM.controller('MessageListController', function($scope, alphaplusService, $uibModal){
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
                action: "listBySeach",
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
                alert("Message listBySeach by ip failure");
            }
        );
    };
});

controllersM.controller('MessageController', function($scope, alphaplusService, $routeParams){
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

controllersM.controller('MessageSummaryController', function($scope, alphaplusService, messageID){
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

//------------------------------------JOB

controllersM.controller('JobListController', function($scope, $location, $uibModal, alphaplusService){ 
    alphaplusService.job.query({
            action: "getColumnData"
        },
        function(response){
            $scope.jobGridtData= {};
            $scope.jobGridtData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= {};

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
                action: "listBySeach",
                searchIp: searchIp
            },
            searchIp,
            function(response){
                $scope.jobGridtData.rowData= response.responseEntity;
                $scope.jobGridtData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                $scope.jobGridtData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                $scope.jobGridtData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);
                $scope.jobGridtData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));
            },
            function(response){
                alert("job getAllBySeach by IP failure");
            }
        );
    };
});

controllersM.controller('JobFormController', function($scope, alphaplusService, $routeParams){
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

controllersM.controller('JobController', function($scope, $route, $routeParams, $location, $http, alphaplusService){
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
                        formIpData.data= $scope.jobDetail[formName];
                    });                
                }, function(){
                    alert("Job get failure");
                });          
            }else{
                angular.forEach($scope.jobWizzard.wizzardData, function(formIpData, formName){
                    $scope.jobDetail[formName]= {};
                    angular.forEach(formIpData.fieldAry, function(field){
                        $scope.jobDetail[formName][field.name]= "";
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

controllersM.controller('JobSummaryController', function($scope, alphaplusService, jobID){
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

//------------------------------------CLIENT

controllersM.controller('ClientListController', function($scope, $location, $uibModal, alphaplusService){ 
    alphaplusService.client.query({
            action: "getColumnData"
        },
        function(response){
            $scope.clientGridtData= {};
            $scope.clientGridtData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= {};

            $scope.fetchClients(searchIp); 
        },
        function(){
            alert('Core getColumnData failed');
        }
    );
    $scope.editClient = function(editRow){
        var summaryPath= '/addUser/'+editRow.userID;
        $location.path(summaryPath);
    };
    $scope.viewClient = function(viewRow){ 
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'html/client/summary.html',
            controller: 'ClientSummaryController',
            size: 'lg',
            resolve:{
                clientID: function (){
                    return viewRow.clientID;
                }
            }
        });
    };
    $scope.deleteClient = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
    
    $scope.fetchClients = function(searchIp){
        alphaplusService.client.save({
                action: "listBySeach",
                searchIp: searchIp
            },
            searchIp,
            function(response){
                $scope.clientGridtData.rowData= response.responseEntity;
                $scope.clientGridtData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                $scope.clientGridtData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                $scope.clientGridtData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);
                $scope.clientGridtData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));
            },
            function(response){
                alert("Client listBySeach failed");
            }
        );
    };
});

controllersM.controller('ClientController', function($scope, alphaplusService, $routeParams, $location){
    alphaplusService.client.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.clientWizzard= response;
            $scope.clientDetail= {};
            if($routeParams.clientID){
                 alphaplusService.client.get({
                    action: "get",
                    clientID: $routeParams.clientID
                }, function(clientDataResp){
                    $scope.clientDetail= clientDataResp.responseEntity;
                    angular.forEach($scope.clientWizzard.wizzardData, function(formIpData, formName){
                        formIpData.data= $scope.clientDetail[formName];
                    });
                }, function(){
                    alert("Client get failure");
                });
            }else{
                angular.forEach($scope.clientWizzard.wizzardData, function(formIpData, formName){
                    $scope.clientDetail[formName]= {};
                    angular.forEach(formIpData.fieldAry, function(field){
                        $scope.clientDetail[formName][field.name]= "";
                    });
                    formIpData.data= $scope.clientDetail[formName];
                });
            }
            $scope.clientDetail.isReady= true;
        }, 
        function(){ 
            alert('Client getWizzardData failure');
        }
    );  
 
    $scope.selectWizzardStep= function(selectedWizzardStep){
        angular.forEach($scope.clientWizzard.wizzardStepData, function(wizzardStep){
            wizzardStep.active= false;
            wizzardStep.class= '';
        });    
        selectedWizzardStep.active= true;
        selectedWizzardStep.class= 'active';

        angular.forEach($scope.clientWizzard.wizzardData, function(value, key){
            value.isHidden = true;
        });    
        $scope.clientWizzard.wizzardData[selectedWizzardStep.name].isHidden=false;
    };
 
    $scope.isLastStep= function(step) {
       if(step == $scope.clientWizzard.commonData.lastStep){
            return true;
       }
       return false;
    }

    $scope.submitClient = function(clientDataType, clientData){
        var service= alphaplusService[clientDataType];
        var action= "save";
        if($scope.clientDetail[clientDataType] && $scope.clientDetail[clientDataType].id){
            action= "update";
            clientData["id"]= $scope.clientDetail[clientDataType]["id"];
        }
        //server call
        service.save({
                action: action,
                clientID: $scope.clientDetail.id
            }, 
            clientData, 
            function(persistedClientData){
                if(persistedClientData.responseData && persistedClientData.responseData.ERROR_MSG){
                    alert(persistedClientData.responseData.ERROR_MSG);
                }else{
                    $scope.clientDetail= persistedClientData.responseEntity;
                    if($scope.isLastStep(clientDataType)){
                        alert("Thank you for sharing your information. Your information is safe with duel-encryption and only you who can see it. Now, go ahead any file a complain!");
                        //$location.path($scope.$parent.bannerData.navData.mainNavData.client.subNav[0].path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= $scope.clientWizzard.wizzardStepData[clientDataType];
                        currentWizzardStep.submitted= true;
                        //move to next step in the wizzard
                        $scope.selectWizzardStep($scope.clientWizzard.wizzardStepData[currentWizzardStep.next]);
                    }
                }
            },
            function(){
                alert("Client save failure");
            }
        );
    };
});

controllersM.controller('ClientSummaryController', function($scope, alphaplusService, clientID){
    $scope.clientDetail= {};
    if(clientID){
         alphaplusService.client.get({
            action: "get",
            clientID: clientID
        }, function(clientDataResp){
            $scope.clientDetail= clientDataResp;
        }, function(){
            alert("Client get failure");
        });
    }
});

//------------------------------------USER

controllersM.controller('UserListController', function($scope, $location, $uibModal, alphaplusService){ 
    alphaplusService.user.query({
            action: "getColumnData"
        },
        function(response){
            $scope.userGridtData= {};
            $scope.userGridtData.columnData= response;

            var searchIp= {};
            searchIp.pageNo= 1;
            searchIp.rowsPerPage= 30;
            searchIp.searchData= {};

            $scope.fetchUsers(searchIp); 
        },
        function(){
            alert('Core getColumnData failed');
        }
    );
    $scope.editUser = function(editRow){
        var summaryPath= '/addUser/'+editRow.userID;
        $location.path(summaryPath);
    };
    $scope.viewUser = function(viewRow){ 
        $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'html/user/summary.html',
            controller: 'UserSummaryController',
            size: 'lg',
            resolve:{
                userID: function (){
                    return viewRow.userID;
                }
            }
        });
    };
    $scope.deleteUser = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
    
    $scope.fetchUsers = function(searchIp){
        alphaplusService.user.save({
                action: "listBySeach",
                searchIp: searchIp
            },
            searchIp,
            function(response){
                $scope.userGridtData.rowData= response.responseEntity;
                $scope.userGridtData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                $scope.userGridtData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                $scope.userGridtData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);
                $scope.userGridtData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));
            },
            function(response){
                alert("User listBySeach failed");
            }
        );
    };
});

controllersM.controller('UserController', function($scope, alphaplusService, $routeParams, $location){
    alphaplusService.user.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.userWizzard= response;
            $scope.userDetail= {};
            if($routeParams.userID){
                 alphaplusService.user.get({
                    action: "get",
                    userID: $routeParams.userID
                }, function(userDataResp){
                    $scope.userDetail= userDataResp.responseEntity;
                    angular.forEach($scope.userWizzard.wizzardData, function(formIpData, formName){
                        formIpData.data= $scope.userDetail[formName];
                    });
                }, function(){
                    alert("User get failure");
                });
            }else{
                angular.forEach($scope.userWizzard.wizzardData, function(formIpData, formName){
                    $scope.userDetail[formName]= {};
                    angular.forEach(formIpData.fieldAry, function(field){
                        $scope.userDetail[formName][field.name]= "";
                    });
                    formIpData.data= $scope.userDetail[formName];
                });
            }
            $scope.userDetail.isReady= true;
        }, 
        function(){ 
            alert('User getWizzardData failure');
        }
    );  
 
    $scope.selectWizzardStep= function(selectedWizzardStep){
        angular.forEach($scope.userWizzard.wizzardStepData, function(wizzardStep){
            wizzardStep.active= false;
            wizzardStep.class= '';
        });    
        selectedWizzardStep.active= true;
        selectedWizzardStep.class= 'active';

        angular.forEach($scope.userWizzard.wizzardData, function(value, key){
            value.isHidden = true;
        });    
        $scope.userWizzard.wizzardData[selectedWizzardStep.name].isHidden=false;
    };
 
    $scope.isLastStep= function(step) {
       if(step == $scope.userWizzard.commonData.lastStep){
            return true;
       }
       return false;
    }

    $scope.submitUser = function(userDataType, userData){
        var service= alphaplusService[userDataType];
        var action= "save";
        if($scope.userDetail[userDataType] && $scope.userDetail[userDataType].id){
            action= "update";
            userData["id"]= $scope.userDetail[userDataType]["id"];
        }
        //server call
        service.save({
                action: action,
                userID: $scope.userDetail.id
            }, 
            userData, 
            function(persistedUserData){
                if(persistedUserData.responseData && persistedUserData.responseData.ERROR_MSG){
                    alert(persistedUserData.responseData.ERROR_MSG);
                }else{
                    $scope.userDetail= persistedUserData.responseEntity;
                    if($scope.isLastStep(userDataType)){
                        alert("Thank you for sharing your information. Your information is safe with duel-encryption and only you who can see it. Now, go ahead any file a complain!");
                        //$location.path($scope.$parent.bannerData.navData.mainNavData.user.subNav[0].path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= $scope.userWizzard.wizzardStepData[userDataType];
                        currentWizzardStep.submitted= true;
                        //move to next step in the wizzard
                        $scope.selectWizzardStep($scope.userWizzard.wizzardStepData[currentWizzardStep.next]);
                    }
                }
            },
            function(){
                alert("User save failure");
            }
        );
    };
});

controllersM.controller('UserSummaryController', function($scope, alphaplusService, userID){
    $scope.userDetail= {};
    if(userID){
         alphaplusService.user.get({
            action: "get",
            userID: userID
        }, function(userDataResp){
            $scope.userDetail= userDataResp;
        }, function(){
            alert("User get failure");
        });
    }
});

controllersM.controller('ChangePasswordController', function($scope, $location, $routeParams, alphaplusService){
    $scope.pwData= {};
    $scope.pwData.ERR_USER_DOESNT_EXISTS= false;
    $scope.pwData.missingEmailID= false;
    $scope.pwData.isPasswordMatching= true;
    $scope.updatePW= function(){
        if($scope.pwData.newPassword == $scope.pwData.repeatPassword){
            alphaplusService.user.save({
                action: "updatePassword",
                newPassword: $scope.pwData.newPassword,
                currentPassword: $scope.pwData.currentPassword
            },{},
            function(response){
                if(response.responseData && response.responseData.SUCCESS && (response.responseData.SUCCESS == "true")){
                    alert("Password is successfuly updated. You will be logged-out now. Please Login again with new password.")
                    window.location = '/alphaplus/logout';
                }else{
                    alert("We are sorry, password update failed. Please try again in sometime.!");
                }
            }, 
            function(){
                alert("We are sorry, password update failed. Please try again in sometime.!");
            });
        }else{
            $scope.isPasswordMatching= false;
            //alert("Repeat-password does not match with New-password! Please make sure to enter both the password same.")
        }
    };

    $scope.forgotPW= function(){
        $scope.pwData.missingEmailID= false;
        if($scope.pwData.emailID){
            alphaplusService.core.save({
                action: "initiatePasswordUpdate",
                emailID: $scope.pwData.emailID,
            },{},
            function(response){
                if(response.responseData && response.responseData.SUCCESS && (response.responseData.SUCCESS == "true")){
                    $scope.pwData.forgotPWLink= response.responseData.PW_UPDATE_URL;
                    $scope.pwData.ERR_USER_DOESNT_EXISTS= false;
                    alert("Password update link successfuly sent your registered email id.")
                }else if(response.responseData && response.responseData.ERR_USER_DOESNT_EXISTS && (response.responseData.ERR_USER_DOESNT_EXISTS == "true")){
                    $scope.pwData.ERR_USER_DOESNT_EXISTS= true;
                }else{
                    alert("We are sorry, Forgot-password flow failed. Please try again in sometime.!");
                }
            }, 
            function(){
                alert("We are sorry, Forgot-password flow failed. Please try again in sometime.!");
            });
        }else{
            $scope.pwData.missingEmailID= true;
        }
    };

    $scope.updateForgottenPassword= function(){
        if($routeParams.token){
            if($scope.pwData.newPassword == $scope.pwData.repeatPassword){
                alphaplusService.core.save({
                    action: "updateForgottenPassword",
                    token: $routeParams.token,
                    newPassword: $scope.pwData.newPassword
                },{},
                function(response){
                    var responseData= response.responseData;
                    if(responseData && responseData.SUCCESS && (responseData.SUCCESS == "true")){
                        alert("Password is successfuly update. Please login to your account with new password.")
                        $location.path($scope.$parent.bannerData.navData.configNavData.signIn.path);
                    }else{
                        alert("Update password link is faulty -No account associated against givem Email ID. Please re-initiate the forget password");
                    }
                }, 
                function(){
                    alert("updateForgottenPassword call failed");
                });
            }else{
                alert("Repeat-password does not match with New-password! Please make sure to enter both the password same.")
            }            
        }else{
            alert("Update password link is faulty. Please re-initiate the forget password");
        }
    };
});

//------------------------------------PROFILE

controllersM.controller('ProfileController', function($scope, $route, $routeParams, $location){

});