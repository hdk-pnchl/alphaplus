var controllersM= angular.module('userControllersM', ['servicesM', 'ui.bootstrap']);

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
                action: "search",
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
                alert("User search failed");
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