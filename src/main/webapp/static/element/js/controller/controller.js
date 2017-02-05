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

//------------------------------------PROFILE

controllersM.controller('ProfileController', function($scope, $route, $routeParams, $location){

});