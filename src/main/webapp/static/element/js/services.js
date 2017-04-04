var serviceM= angular.module('servicesM', ['ngResource']);

serviceM.factory('alphaplusGlobleDataService', function($resource){
    var alphaplusGlobleDataService= {};
    return alphaplusGlobleDataService;
});

serviceM.factory('alphaplusService', function($resource, $location,
    addressService,
    clientService,
    contactService,
    jobService,
    jobInstService,
    messageService,
    plateService,
    userService){
    var webResource= {};
    
    //webResource.rootPath= 'http://104.238.126.194:8080/alphaplus';
    webResource.rootPath= 'http://localhost:8080/alphaplus';

    webResource.core= $resource(webResource.rootPath+'/ctrl/core/:action',{
        action: '@action'
    });
    webResource.message= $resource(webResource.rootPath+'/ctrl/message/:action',{
        action: '@action'
    });
    webResource.job= $resource(webResource.rootPath+'/ctrl/job/:action',{
        action: '@action'
    });
    webResource.client= $resource(webResource.rootPath+'/ctrl/client/:action',{
        action: '@action'
    });  
    webResource.plate= $resource(webResource.rootPath+'/ctrl/plate/:action',{
        action: '@action'
    });  
    webResource.jobInst= $resource(webResource.rootPath+'/ctrl/jobInst/:action',{
        action: '@action'
    });  
    
    // User
    webResource.user= $resource(webResource.rootPath+'/ctrl/user/:action',{
        action: '@action'
    });    
    webResource.address= $resource(webResource.rootPath+'/ctrl/address/:action',{
        action: '@action'
    });  
    webResource.contact= $resource(webResource.rootPath+'/ctrl/contact/:action',{
        action: '@action'
    });      
    webResource.basicDetail= $resource(webResource.rootPath+'/ctrl/user/basicDetail/:action',{
        action: '@action'
    });  

    webResource.services= {};
    webResource.services.address= addressService;
    webResource.services.client= clientService;
    webResource.services.contact= contactService;
    webResource.services.job= jobService;
    webResource.services.jobInst= jobInstService;
    webResource.services.message= messageService;    
    webResource.services.plate= plateService;    
    webResource.services.user= userService;

//-------------------------------------------------------------------------------
//--------------------------------------OBJ--------------------------------------
//-------------------------------------------------------------------------------

    webResource.obj= {}
    webResource.obj.searchIp= {};
    webResource.obj.searchIp.pageNo= 1;
    webResource.obj.searchIp.rowsPerPage= 30;
    webResource.obj.searchIp.searchData= [];

//-------------------------------------------------------------------------------
//--------------------------------------BSUINESS---------------------------------
//-------------------------------------------------------------------------------


    webResource.business= {};
    webResource.business.processFormNewBO= function(scope, boDetailKey){
        angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
            //If the prop isnt an object but primitive.
            if(formName === scope.wizzard.commonData.wizzard){
                angular.forEach(formIpData.fieldAry, function(field){
                    //field-type
                    if(field.type=="date"){
                        scope[boDetailKey][field.name]= new Date();
                    }else if(field.type=="model"){
                        scope[boDetailKey][field.name]= {};
                    }else{
                        scope[boDetailKey][field.name]= "";
                    }
                    //field-feature
                    if(field.readOnly){
                        scope[boDetailKey][field.name]= "Will be auto populated.";
                    }
                });
                formIpData.data= scope[boDetailKey];
            }
            //If the prop is an object
            else{
                scope[boDetailKey][formName]= {};
                angular.forEach(formIpData.fieldAry, function(field){
                    //field-type
                    if(field.type=="date"){
                        scope[boDetailKey][formName][field.name]= new Date();
                    }else if(field.type=="model"){
                        scope[boDetailKey][field.name]= null;
                    }else{
                        scope[boDetailKey][formName][field.name]= "";
                    }
                    //field-feature
                    if(field.readOnly){
                        scope[boDetailKey][formName][field.name]= "Will be auto populated.";
                    }
                });
                formIpData.data= scope[boDetailKey][formName];
            }
        });      
    };

    webResource.business.processFormExistingBO= function(scope, boDetailKey, boID, boIDKey){
        var requestData= {};
        requestData[boIDKey]= boID;
        requestData.action= "get";

        webResource[scope.wizzard.commonData.wizzard].get(requestData, function(dataResp){
            scope[boDetailKey]= dataResp.responseEntity
            webResource.business.processFormExistingBOInternal(scope, boDetailKey);
        }, function(){
            alert(scope.wizzard.commonData.wizzard+" GET failure");
        });
    }; 

    webResource.business.processFormExistingBOInternal= function(scope, boDetailKey){
        angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
            angular.forEach(formIpData.fieldAry, function(field){
                if(scope[boDetailKey][formName]){
                    formIpData.data= scope[boDetailKey][formName];
                }else{
                    formIpData.data= scope[boDetailKey];
                }
            });
        });
    }; 

    webResource.business.selectWizzardStep= function(scope, nextWizzardStep, boDetailKey){
        if(scope[boDetailKey] && scope[boDetailKey].id){
            angular.forEach(scope.wizzard.wizzardStepData, function(wizzardStep){
                wizzardStep.active= false;
                wizzardStep.class= '';
            });    
            nextWizzardStep.active= true;
            nextWizzardStep.class= 'active';

            angular.forEach(scope.wizzard.wizzardData, function(value, key){
                value.isHidden = true;
            });
            scope.wizzard.wizzardData[nextWizzardStep.name].isHidden=false;
        }
    };
 
    webResource.business.isLastWizzardStep= function(scope, step) {
       if(step === scope.wizzard.commonData.lastStep){
            return true;
       }
       return false;
    }

    webResource.business.submitForm = function(formData, scope, boDetailKey){
        //server call
        webResource[formData.service].save({
                action: "saveOrUpdate"
            }, 
            scope[boDetailKey], 
            function(persistedData){
                if(persistedData.responseData && persistedData.responseData.ERROR_MSG){
                    alert(persistedData.responseData.ERROR_MSG);
                }else{
                    scope[boDetailKey]= persistedData.responseEntity;
                    //boDetail= persistedData.responseEntity;
                    //if its last step, redirect to patient-grid
                    if(webResource.business.isLastWizzardStep(scope, formData.form)){
                        $location.path(scope.$parent.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.list.path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= scope.wizzard.wizzardStepData[formData.form];
                        currentWizzardStep.submitted= true;
                        if(currentWizzardStep.isCoreStep){
                            var path= scope.$parent.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.update.path+"/"+persistedData.responseEntity.id;
                            $location.path(path); 
                        }else{
                            var nextWizzardStep= scope.wizzard.wizzardStepData[currentWizzardStep.next];
                            //move to next step in the wizzard
                            webResource.business.selectWizzardStep(scope, nextWizzardStep);
                        }
                    }
                }
            },
            function(){
                alert(formData.service+": SAVE failure");
            }
        );
    };

    webResource.business.fetchBO = function(service, id, idKey, scope, boDetailKey){
         webResource[service].get({
            action: "get",
            idKey: id
        }, function(respData){
            scope[boDetailKey]= respData.responseEntity;
        }, function(){
            alert(service+" GET failure");
        });
    };

    webResource.business.fetchBOList = function(service, scope, searchIp){
        if(!searchIp){
            searchIp= webResource.obj.searchIp;
        }
        webResource[service].save({
                action: "search",
                searchIp: searchIp
            },
            searchIp,
            function(response){
                scope.gridData.rowData= response.responseEntity;
                scope.gridData.totalRowCount= parseInt(response.responseData.ROW_COUNT);
                scope.gridData.currentPageNo= parseInt(response.responseData.CURRENT_PAGE_NO);
                scope.gridData.rowsPerPage= parseInt(response.responseData.ROWS_PER_PAGE);
                scope.gridData.pageAry= new Array(parseInt(response.responseData.TOTAL_PAGE_COUNT));
            },
            function(response){
                alert(service+": SEARCH failure");
            }
        );
    };

    webResource.business.viewBO = function(id, idKey, templateURL, controller){ 
        $uibModal.open({
            templateUrl: templateURL,
            controller: controller,
            size: 'lg',
            resolve:{
                idKey: function (){
                    return id;
                }
            }
        });
    };

    return webResource;
});