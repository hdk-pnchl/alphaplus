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

controllersM.controller('ReconController', function($scope, $http, alphaplusService){
    $scope.rAnalysis= {};


    $scope.apData= {};
    $scope.apData.service= "recon";
    $scope.apData.boDetailKey= "boData";

    $scope.exec= {};
    $scope.exec.op= {};
    $scope.exec.op.recon= {};
    $scope.exec.op.recon.populateSubmitObj= function(){
        var fd = new FormData();
        var formData= $scope[$scope.apData.boDetailKey];
        angular.forEach(formData.fieldAry, function(field){
            var fieldVal= formData.data[field.modalData]
            if(fieldVal){
                fd.append(field.modalData, fieldVal);
            }            
        }); 
        formData.multipartData= fd;
    };

    alphaplusService.business.processForm($scope);

    $scope.update= function(formData){
        var uploadUrl = "http://localhost:8080/alphaplus/ctrl/recon/saveOrUpdate";
        $http.post(uploadUrl, $scope[$scope.apData.boDetailKey].multipartData, {
            headers: {'Content-Type': undefined},
            transformRequest: angular.identity
        }).then(function successCallback(response){
            $scope.resultData= response;

            //err
            $scope.rAnalysis.err= {};
            $scope.rAnalysis.err.grid= {}
            alphaplusService.recon.query({
                action: "getErrColumnData"
            },
            function(response){
                $scope.rAnalysis.err.grid.columnData= response;
            });
            $scope.rAnalysis.err.grid.totalRowCount= $scope.resultData.data.responseEntity.errors.length;
            $scope.rAnalysis.err.grid.currentPageNo= 1;
            $scope.rAnalysis.err.grid.rowsPerPage= $scope.resultData.data.responseEntity.errors.length;
            $scope.rAnalysis.err.grid.pageAry= 1;
            $scope.rAnalysis.err.grid.rowData= $scope.resultData.data.responseEntity.errors;

            //txn
            if($scope.resultData.data.responseEntity.txns.length>0){
                $scope.rAnalysis.txn= {};
                $scope.rAnalysis.txn.grid= {}
                alphaplusService.recon.query({
                    action: "getTxnColumnData"
                },
                function(response){
                    $scope.rAnalysis.txn.grid.columnData= response;
                });        
                $scope.rAnalysis.err.grid.totalRowCount= $scope.resultData.data.responseEntity.txns.length;
                $scope.rAnalysis.err.grid.currentPageNo= 1;
                $scope.rAnalysis.err.grid.rowsPerPage= $scope.resultData.data.responseEntity.txns.length;
                $scope.rAnalysis.err.grid.pageAry= 1;            
                $scope.rAnalysis.txn.grid.rowData= $scope.resultData.data.responseEntity.txns;
            }

            //exceptions
            if($scope.resultData.data.responseEntity.execs.length>0){
                $scope.rAnalysis.excep= {};
                $scope.rAnalysis.excep.grid= {}
                alphaplusService.recon.query({
                    action: "getExcepColumnData"
                },
                function(response){
                    $scope.rAnalysis.excep.grid.columnData= response;
                });   
                $scope.rAnalysis.excep.grid.totalRowCount= $scope.resultData.data.responseEntity.execs.length;
                $scope.rAnalysis.excep.grid.currentPageNo= 1;
                $scope.rAnalysis.excep.grid.rowsPerPage= $scope.resultData.data.responseEntity.execs.length;
                $scope.rAnalysis.excep.grid.pageAry= 1;            
                $scope.rAnalysis.excep.grid.rowData= $scope.resultData.data.responseEntity.execs;
            }
        })
    };
});

controllersM.controller('ReconController001', function($scope, $http, $location, $rootScope){
    $scope.searchRow= {};
    $scope.searchRow.valid= false;

    $scope.formData= {};
    $scope.formData.fileEtx= "csv";
    $scope.formData.fileType= "transactions";
    /*
    $scope.formData.country= "FR";
    $scope.formData.network= "02f802";
    $scope.formData.version= "v1_1-1";
    */
    $scope.buildCol= function(id, label){
        if(!label){
            label= id;
        }
        var col= {};
        col.id= id;
        col.label= label;
        return col;
    };

    $scope.processRecon = function(){
        var uploadUrl = "http://localhost:8080/alphaplus/ctrl/recon/saveOrUpdate";
        var fd = new FormData();
        fd.append('txnFile', $scope.txnFile);
        
        if($scope.exceptionFile){
            fd.append('excepFile', $scope.exceptionFile);
        }
        if($scope.formData.fileEtx){
            fd.append('fileEtx', $scope.formData.fileEtx);
        }
        if($scope.formData.fileType){
            fd.append('fileType', $scope.formData.fileType);
        }
        if($scope.formData.country){
            fd.append('country', $scope.formData.country);
        }
        if($scope.formData.network){
            fd.append('network', $scope.formData.network);
        }
        if($scope.formData.version){
            fd.append('version', $scope.formData.version);
        }

        if($scope.formData.txnDate){
            fd.append('txnDate', $scope.formData.txnDate);
        }

        $http.post(uploadUrl, fd, {
            headers: {'Content-Type': undefined},
            transformRequest: angular.identity
        }).then(
        function successCallback(response){
            $scope.reconProcessResult= response;

            $scope.data= {};
            $scope.data.columnData= [];
            $scope.data.columnData.push($scope.buildCol("bokuTransactionId"));
            $scope.data.columnData.push($scope.buildCol("operatorTransactionId"));
            $scope.data.columnData.push($scope.buildCol("transactionTimestamp"));
            $scope.data.columnData.push($scope.buildCol("transactionType"));
            $scope.data.columnData.push($scope.buildCol("operatorTransactionStatus"));
            $scope.data.columnData.push($scope.buildCol("operatorOriginalTransactionId"));
            $scope.data.columnData.push($scope.buildCol("totalAmount"));
            $scope.data.columnData.push($scope.buildCol("currencyCode"));
            $scope.data.columnData.push($scope.buildCol("mobileNumber"));
            $scope.data.columnData.push($scope.buildCol("acr"));
            $scope.data.rowData= $scope.reconProcessResult.data.responseEntity.txns;


            $scope.execData= {};
            $scope.execData.columnData= [];
            $scope.execData.columnData.push($scope.buildCol("merchantId"));
            $scope.execData.columnData.push($scope.buildCol("transactionType"));
            $scope.execData.columnData.push($scope.buildCol("bokuTransactionId"));
            $scope.execData.columnData.push($scope.buildCol("transactionDate"));
            $scope.execData.columnData.push($scope.buildCol("transactionTime"));
            $scope.execData.columnData.push($scope.buildCol("countryCode"));
            $scope.execData.columnData.push($scope.buildCol("networkCode"));            
            $scope.execData.columnData.push($scope.buildCol("merchantTransactionId"));
            $scope.execData.columnData.push($scope.buildCol("operatorTransactionId"));
            $scope.execData.columnData.push($scope.buildCol("productDescription"));
            $scope.execData.columnData.push($scope.buildCol("merchantData"));
            $scope.execData.columnData.push($scope.buildCol("currencyCode"));
            $scope.execData.columnData.push($scope.buildCol("totalAmount"));
            $scope.execData.columnData.push($scope.buildCol("bokuOriginalTransactionId"));
            $scope.execData.columnData.push($scope.buildCol("operatorOriginalTransactionId"));
            $scope.execData.columnData.push($scope.buildCol("merchantOriginalTransactionId"));
            $scope.execData.columnData.push($scope.buildCol("originalTransactionDate"));
            $scope.execData.columnData.push($scope.buildCol("originalTransactionTime"));
            $scope.execData.columnData.push($scope.buildCol("refundReasonCode"));
            $scope.execData.columnData.push($scope.buildCol("refundSource"));
            $scope.execData.columnData.push($scope.buildCol("reconciliationStatus"));
            $scope.execData.columnData.push($scope.buildCol("reconciliationStatusDate"));
            $scope.execData.columnData.push($scope.buildCol("reconciliationStatusTime"));
            $scope.execData.columnData.push($scope.buildCol("bokuTransactionStatus"));
            $scope.execData.columnData.push($scope.buildCol("operatorTransactionStatus"));
            $scope.execData.rowData= $scope.reconProcessResult.data.responseEntity.execs;
        },function errorCallback(response){
            console.log(response);
        });
    };
/*
 * merchantId,
 * transactionType,
 * bokuTransactionId,
 * transactionDate,
 * transactionTime,
 * countryCode,
 * networkCode,
 * merchantTransactionId,
 * operatorTransactionId,
 * productDescription,
 * merchantData,
 * currencyCode,
 * totalAmount,
 * bokuOriginalTransactionId,
 * operatorOriginalTransactionId,
 * merchantOriginalTransactionId,
 * originalTransactionDate,
 * originalTransactionTime,
 * refundReasonCode,
 * refundSource,
 * reconciliationStatus,
 * reconciliationStatusDate,
 * reconciliationStatusTime,
 * bokuTransactionStatus,
 * operatorTransactionStatus
 * */
});