var userControllersM= angular.module('userControllersM', ['servicesM', 'ui.bootstrap']);

var userListController= userControllersM.controller('UserListController', function($scope, $location, $uibModal, alphaplusService, $rootScope){ 
    alphaplusService.user.query({
            action: "getColumnData"
        },
        function(response){
            alphaplusService.business.processColumnData("user", $scope, response);
        },
        function(){
            alert('User GET ColumnData failed');
        }
    );
    $scope.edit = function(editRow){
        $location.path($scope.$parent.bannerData.navData.mainNavData.user.subNav.update.path+"/"+editRow.id);
    };
    $scope.view = function(viewRow){ 
        alphaplusService.business.viewBO(viewRow.id, viewRow, "element/html/business/user/summary.html", "UserSummaryController", $uibModal);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var userController= userControllersM.controller('UserController', function($scope, $rootScope, $route, $routeParams, $location, $http, alphaplusService){
    $scope.formService= alphaplusService;
    $scope.userDetail= {};

    alphaplusService.user.get({
            action: "getWizzardData"
        }, 
        function(response){
            $scope.wizzard= response;
            if($routeParams.userID){
                alphaplusService.business.processFormExistingBO($scope, "userDetail", $routeParams.userID, "id");
            }else{
                alphaplusService.business.processFormNewBO($scope, "userDetail");
            }
            $scope.userDetail.isReady= true;
        }, 
        function(){ 
            alert('User GET WizzardData failure');
        }
    );

    $scope.submit = function(formData){
        alphaplusService.business.submitForm(formData, $scope, "userDetail");
    };

    $scope.selectWizzardStep = function(wizzardStep){
        alphaplusService.business.selectWizzardStep($scope, wizzardStep, "userDetail");
    };

    $rootScope.$on("processaddressDetail", function(event, addressData){
        alphaplusService.business.processInternalObj($scope, "addressDetail", "addressDetail", "name", addressData, false);
    });

    $rootScope.$on("processcontactDetail", function(event, contactData){
        alphaplusService.business.processInternalObj($scope, "contactDetail", "contactDetail", "name", contactData, false);
    });    
});

var userSummaryController= userControllersM.controller('UserSummaryController', function($scope, alphaplusService, ipID, ipObj){
    $scope.userDetail= {};
    alphaplusService.business.processSummary("user", "id", ipID, $scope, "userDetail", ipObj);
});

var changePasswordController= userControllersM.controller('ChangePasswordController', function($scope, $location, $routeParams, alphaplusService){
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

var userService= {};
userService.changePasswordController= changePasswordController;
userService.userSummaryController= userSummaryController;
userService.userController= userController;
userService.userListController= userListController;

userControllersM.constant('userService', userService);
