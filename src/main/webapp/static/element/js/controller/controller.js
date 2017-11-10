var controllersM= angular.module('controllersM', ['servicesM', 'ui.bootstrap', 'angularjs-dropdown-multiselect']);

//------------------------------------CORE

controllersM.controller('CoreController', function($scope, $http, $location, $rootScope, alphaplusService){
    /*
    breadcrumb:
    $scope.anyClick= function($event){
        $rootScope.$emit("anyClick", {
            "name": $event.toElement.innerText
        });
    };
    */
    alphaplusService.core.get({
            action: "getBannerData"
        }, 
        function(response){
            alphaplusService.obj.bannerData= response;
            if(alphaplusService.obj.bannerData.navData.configNavData.profile){
                alphaplusService.obj.bannerData.navData.configNavData.profile.title= alphaplusService.obj.bannerData.USER_DATA.name;
                alphaplusService.obj.bannerData.navData.configNavData.profile.subNav.user.path= alphaplusService.obj.bannerData.navData.configNavData.profile.subNav.user.path+"/update/"+alphaplusService.obj.bannerData.USER_DATA.id;
            }
            $scope.bannerData= alphaplusService.obj.bannerData;

            var xTabName= $location.path().split("/")[1];
            var xTab= alphaplusService.obj.bannerData.navData.mainNavData[xTabName];
            alphaplusService.business.selectBannerDirectiveTab(xTab);
        }, 
        function(){ 
            alert('getBannerData failed');
        }
    ).$promise.then(function(data){
        $scope.footerData= {};
        $rootScope.modalInstances= {};
    });
});

//------------------------------------BANNER

controllersM.controller('BannerController', function($scope, alphaplusService){
});


//------------------------------------HOME

controllersM.controller('HomeController', function($scope, alphaplusService){
    $scope.apData= {};
    $scope.apData.service= "core";
    $scope.apData.boDetailKey= "boData";

    $scope.exec= {};
    $scope.exec.fn= {};
    $scope.exec.fn.tools= {};
    $scope.exec.fn.tools.fn= {};
    //$scope.exec.data
    //$scope.exec.fn.tools.data
    $scope.exec.fn.tools.fn.removeNetwork= function(scope){
        if(scope.formData.data.tools){
            delete scope.formData.data.tools[scope.tempData.field.tools.removeMultiselect.whichOption.networkCode];
        }
    };


    alphaplusService.business.processForm($scope);

    $scope.update= function(formData){
        alphaplusService.business.formUpdateFn($scope);
    };
});


//------------------------------------SIGN

controllersM.controller('SignController', function($scope, $location, alphaplusService, $route, $routeParams){
    $scope.user= {};
    $scope.isEmailTaken= false;
    $scope.isPasswordMatching= true;
    $scope.submitBaseURL= alphaplusService.rootPath+"/login";
    $scope.signUp= function(){
    	if($scope.user 
    			&& $scope.user.password 
    			&& $scope.user.confirmPassword 
    			&& $scope.user.emailID){
            if($scope.user.password != $scope.user.confirmPassword){
                $scope.isPasswordMatching= false;
            }else{
                $scope.isPasswordMatching= true;
                //server call: check if email id not already taken
                alphaplusService.core.save({
                    action: "isEmailIdTaken",
                    emailID: $scope.user.emailID
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
                           $location.path(alphaplusService.obj.bannerData.navData.configNavData.signIn.path);
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
    $scope.bannerData=alphaplusService.obj.bannerData;
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
                message.name= alphaplusService.obj.bannerData.USER_DATA.name;
            }
            if(!message.emailID){
                message.emailID= alphaplusService.obj.bannerData.USER_DATA.emailID;
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

//------------------------------------PROFILE

controllersM.controller('ProfileController', function($scope, $route, $routeParams, $location){

});