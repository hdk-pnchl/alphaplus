var userControllersM= angular.module('userControllersM', ['servicesM', 'ui.bootstrap']);

var userListController= userControllersM.controller('UserListController', function($scope, $uibModal, alphaplusService){ 
    alphaplusService.business.processColumn("user", $scope);

    $scope.edit = function(editRow){
        var ipObj= {
            bannerTab: "user",
            primaryKey: editRow.id
        };
        alphaplusService.business.viewBO(ipObj);
    };
    $scope.view = function(viewRow){ 
        var ipObj= {
            modalData: {
                viewRow: viewRow,
                primaryKey: viewRow.id
            },
            templateURL: "element/html/business/crud/summary.html",
            controller: "UserSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var userController= userControllersM.controller('UserController', function($scope, $routeParams, alphaplusService){
    var primaryKeyData= {
        val: $routeParams.userID,
        propName: "id"
    };
    var eventData= [{
            "form": "addressDetail",
            "collectionPropName": "addressDetail",
            "eventName": "processaddressDetail",
            "idKeyPropName": "name"
        },{
            "form": "contactDetail",
            "collectionPropName": "contactDetail",
            "eventName": "processcontactDetail",
            "idKeyPropName": "name"
        }
    ];
    var data= {};
    data.primaryKeyData= primaryKeyData;
    data.eventData= eventData;
    data.service= "user";
    data.boDetailKey= "userDetail";
    data.wizzardStep= $routeParams.wizzardStep;
    
    $scope.data= data;

    alphaplusService.business.processWizzard($scope);
});

var userSummaryController= userControllersM.controller('UserSummaryController', function($scope, alphaplusService, primaryKey, viewRow){
    alphaplusService.business.processSummary("user", "id", primaryKey, $scope, "boDetail", viewRow);
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
                        $location.path(alphaplusService.obj.bannerData.navData.configNavData.signIn.path);
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
