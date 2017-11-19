var controllersM= angular.module('controllersM', ['servicesM', 'ui.bootstrap', 'angularjs-dropdown-multiselect']);

//------------------------------------CORE

controllersM.controller('CoreController', function($scope, $http, $location, $rootScope, alphaplusService){
    $rootScope.isLoading= false;
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

controllersM.controller('ReconAnalysisController', function($scope, $rootScope, $uibModal, alphaplusService, rAnalysis){
    $scope.rAnalysis= rAnalysis;
    $rootScope.isLoading= false;
});

controllersM.controller('ReconController', function($scope, $rootScope, $http, $sce, $uibModal, alphaplusService){
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
        $rootScope.isLoading= true;
        var uploadUrl = "http://localhost:8080/alphaplus/ctrl/recon/saveOrUpdate";
        $http.post(uploadUrl, $scope[$scope.apData.boDetailKey].multipartData, {
            headers: {'Content-Type': undefined},
            transformRequest: angular.identity
        }).then(function successCallback(response){
            $scope.resultData= response;
            console.log($scope.resultData);

            $scope.processInternalUpdate("txns")
            $scope.processInternalUpdate("execs")

            //err
            if($scope.resultData.data.responseEntity.errors && $scope.resultData.data.responseEntity.errors.length>0){
                $scope.rAnalysis.err= {};
                $scope.rAnalysis.err.grid= {}
                $scope.rAnalysis.err.grid.avoidPagination= true;
                alphaplusService.recon.query({
                    action: "getErrColumnData"
                },
                function(response){
                    $scope.rAnalysis.err.grid.columnData= response;
                    $scope.rAnalysis.err.grid.totalRowCount= $scope.resultData.data.responseEntity.errors.length;
                    $scope.rAnalysis.err.grid.currentPageNo= 1;
                    $scope.rAnalysis.err.grid.rowsPerPage= $scope.resultData.data.responseEntity.errors.length;
                    $scope.rAnalysis.err.grid.pageAry= 1;
                    $scope.rAnalysis.err.grid.rowData= $scope.resultData.data.responseEntity.errors; 
                    $scope.rAnalysis.err.grid.striped= true; 
                    $scope.rAnalysis.err.grid.hover= true; 
                    //txns internal-grid
                    if($scope.resultData.data.responseEntity.txns && $scope.resultData.data.responseEntity.txns.length>0){
                        if(!$scope.rAnalysis.txns){
                            $scope.rAnalysis.txns= {};
                        }
                        if(!$scope.rAnalysis.txns.grid){
                            $scope.rAnalysis.txns.grid= {}
                        }
                        $scope.rAnalysis.txns.grid.internalGrid= {};
                        $scope.rAnalysis.txns.grid.internalGrid.columnData= response;
                        $scope.rAnalysis.txns.grid.internalGrid.rowDataSource= "errors";
                        $scope.rAnalysis.txns.grid.internalGrid.avoidPagination= true;
                        $scope.rAnalysis.txns.grid.internalGrid.hover= true; 
                        $scope.rAnalysis.txns.grid.internalGrid.condensed= true; 
                        $scope.rAnalysis.txns.grid.internalGrid.striped= true; 
                    }
                    //excep internal-grid
                    if($scope.resultData.data.responseEntity.execs && $scope.resultData.data.responseEntity.execs.length>0){
                        if(!$scope.rAnalysis.execs){
                            $scope.rAnalysis.execs= {};
                        }
                        if(!$scope.rAnalysis.execs.grid){
                            $scope.rAnalysis.execs.grid= {}
                        }
                        $scope.rAnalysis.execs.grid.internalGrid= {};
                        $scope.rAnalysis.execs.grid.internalGrid.columnData= response;
                        $scope.rAnalysis.execs.grid.internalGrid.rowDataSource= "errors";
                        $scope.rAnalysis.execs.grid.internalGrid.avoidPagination= true;
                        $scope.rAnalysis.execs.grid.internalGrid.hover= true; 
                        $scope.rAnalysis.execs.grid.internalGrid.condensed= true; 
                        $scope.rAnalysis.execs.grid.internalGrid.striped= true;                         
                    }
                });
            }

            var resolveObj= {};
            resolveObj.rAnalysis= $scope.rAnalysis;

            var modalInstance= $uibModal.open({
                templateUrl: "element/html/business/core/reconAnalysis.html",
                controller: "ReconAnalysisController",
                resolve: resolveObj,
                windowClass: 'app-modal-window'
            });
        })
    };

    $scope.processInternalUpdate= function(dataKey){
        if($scope.resultData.data.responseEntity[dataKey] && $scope.resultData.data.responseEntity[dataKey].length>0){
            alphaplusService.recon.query({
                action: "getTxnColumnData"
            },
            function(response){
                //dataKey
                if(!$scope.rAnalysis[dataKey]){
                    $scope.rAnalysis[dataKey]= {};
                }
                //grid
                if(!$scope.rAnalysis[dataKey].grid){
                    $scope.rAnalysis[dataKey].grid= {}
                }
                $scope.rAnalysis[dataKey].grid.avoidPagination= true;
                $scope.rAnalysis[dataKey].grid.columnData= response;
                $scope.rAnalysis[dataKey].grid.totalRowCount= $scope.resultData.data.responseEntity[dataKey].length;
                $scope.rAnalysis[dataKey].grid.currentPageNo= 1;
                $scope.rAnalysis[dataKey].grid.rowsPerPage= $scope.resultData.data.responseEntity[dataKey].length;
                $scope.rAnalysis[dataKey].grid.pageAry= 1;            
                $scope.rAnalysis[dataKey].grid.rowData= $scope.resultData.data.responseEntity[dataKey];  
                $scope.rAnalysis[dataKey].grid.hover= true; 
                $scope.rAnalysis[dataKey].grid.condensed= true; 
                //metaData(cell: class, tootip)
                angular.forEach($scope.rAnalysis[dataKey].grid.rowData, function(row){
                    //row.metaData
                    if(!row.metaData){
                        row.metaData= {};
                    }
                    //row.metaData.cellData
                    if(!row.metaData.cellData){
                        row.metaData.cellData= {};
                    }
                    //row.metaData.cellData.cell
                    //row.metaData.cellData.cell.class
                    //row.metaData.cellData.cell.tooltip
                    if(row.errors){
                        angular.forEach(row.errors, function(err){
                            if(row[err.key]){
                                //row.metaData.cellData.cell
                                if(!row.metaData.cellData[err.key]){
                                    row.metaData.cellData[err.key]= {}
                                }
                                //row.metaData.cellData.cell.class
                                if(!row.metaData.cellData[err.key].class){
                                    row.metaData.cellData[err.key].class= {'label': true, 'label-warning': true};
                                }                                    
                                //row.metaData.cellData.cell.tooltip
                                var tooltip= "# "+err.summary+" : "+err.desc;
                                if(!row.metaData.cellData[err.key].tooltip){
                                    row.metaData.cellData[err.key].tooltip= tooltip;
                                }else{
                                    row.metaData.cellData[err.key].tooltip= row.metaData.cellData[err.key].tooltip+"<br>"+tooltip;
                                }
                                row.metaData.cellData[err.key].tooltip= $sce.trustAsHtml(row.metaData.cellData[err.key].tooltip);
                            }
                        });                            
                    }
                });
            });        
        }
    };
});