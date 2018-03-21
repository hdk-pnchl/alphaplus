var userControllersM= angular.module('userControllersMN', ['servicesM', 'ui.bootstrap']);

userControllersM.controller('UserListControllerN', function($scope, $uibModal, NgTableParams, alphaplusService){ 
    $scope.tableParams = new NgTableParams(alphaplusService.obj.grid.initialParams, {      
        getData: function(params){
            var searchIp= {};
            searchIp.pageNo= params.page();
            searchIp.rowsPerPage= params.count();
            searchIp.searchData= [];
            searchIp.searchData.push(params.filter());   
                    
            // ajax request to api
            return alphaplusService.user.save({
                action: "search"
            }, searchIp).$promise.then(function(response) {
                params.total(response.responseData.ROW_COUNT); // recal. page nav controls
                return response.responseEntity;
            });         
        }
    }); 
});

userControllersM.controller('UserControllerN', function($scope, $routeParams, $location, alphaplusService, NgTableParams, $uibModal){
    $scope.dateOptions= alphaplusService.obj.from.dateOptions;
    $scope.dataData= {};
    $scope.dataData.disableTabs= true;
    $scope.dataData.activeTab= 0;

    if($routeParams.userID){
        alphaplusService.user.get({
            "id": $routeParams.userID,
            "action": "get"
        }, function(respData){
            $scope.formData= respData.responseEntity;
            $scope.formData.married= $scope.formData.married+""; 
            $scope.formData.repeatPassword= $scope.formData.password;
            $scope.dataData.disableTabs= false;
            $scope.addressTableParams = new NgTableParams({}, { dataset: $scope.formData.addresses });
            $scope.contactTableParams = new NgTableParams({}, { dataset: $scope.formData.contacts });            
        });
    }else{
        $scope.formData= {};
        $scope.formData.gender= "MALE"; 
        $scope.formData.married= "false";
        $scope.formData.dob= new Date(); 
        $scope.formData.contacts= [];
        $scope.formData.addresses= [];
        $scope.addressTableParams = new NgTableParams({}, { dataset: $scope.formData.addresses });
        $scope.contactTableParams = new NgTableParams({}, { dataset: $scope.formData.contacts });          
    }

    //submit
    $scope.submitForm= function(isValidRequest){      
        if(isValidRequest){
            alphaplusService.user.save({
                action: "saveOrUpdate"
            }, $scope.formData, function(response){
                if(response.responseData.ERROR){

                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };

    //Tabs
    $scope.processTabs= function(){
        $scope.dataData.disableTabs= false;
        $scope.nextTabs();        
    };
    $scope.nextTabs= function(){
        if(!$scope.dataData.disableTabs){
            var nextTabIdx= $scope.dataData.activeTab + 1;
            if(nextTabIdx < 4){
                $scope.dataData.activeTab= nextTabIdx;
            }else{
                $location.path(alphaplusService.obj.bannerData.navData.mainNavData.user.subNav.list.path);
            }            
        }
    };

    //Address
    $scope.openAddress= function(address){
        //resolveObj
        var resolveObj= {};        
        resolveObj.editMode= true;
        //address
        if(!address){
            address= {};
            resolveObj.editMode= false;
        }
        resolveObj.formData= $scope.formData;
        resolveObj.addressTableParams= $scope.addressTableParams;
        resolveObj.ipAddress= address;        
        //$uibModal
        $uibModal.open({
            templateUrl: "element/html/business/common/address.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "AddressControllerN"           
        });
    };

    //Contact 
    $scope.openContact= function(contact){
        //resolveObj        
        var resolveObj= {}; 
        resolveObj.editMode= true;       
        //address
        if(!contact){
            contact= {};
            resolveObj.editMode= false;
        }
        resolveObj.formData= $scope.formData;
        resolveObj.contactTableParams= $scope.contactTableParams;
        resolveObj.ipContact= contact;
        //$uibModal
        $uibModal.open({
            templateUrl: "element/html/business/common/contact.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "ContactControllerN"           
        });
    };    
});

userControllersM.controller('ChangePasswordControllerN', function($scope, $location, $routeParams, alphaplusService){
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
