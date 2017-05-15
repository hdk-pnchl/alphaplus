var serviceM= angular.module('servicesM', ['ngResource']);

serviceM.factory('alphaplusGlobleDataService', function($resource){
    var alphaplusGlobleDataService= {};
    return alphaplusGlobleDataService;
});

serviceM.factory('alphaplusService', function($rootScope, $resource, $location, $filter,
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
    webResource.idDetail= $resource(webResource.rootPath+'/ctrl/user/idDetail/:action',{
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

    webResource.business.processColumnData= function(service, scope, columnDataResp, searchIp){
        scope.gridData= {};
        scope.gridData.columnData= columnDataResp;
        if(scope.$parent.parentForm){
            scope.gridData.rowData= [];
            scope.gridData.totalRowCount= 0;
            scope.gridData.currentPageNo= webResource.obj.searchIp.pageNo;
            scope.gridData.rowsPerPage= webResource.obj.searchIp.rowsPerPage;
            scope.gridData.pageAry= 1;
        }else{
            webResource.business.fetchBOList(service, scope, searchIp);
        }
    };

    webResource.business.processFormNewBO= function(scope, boDetailKey){
        //"scope[boDetailKey]" will hold the entire wizzard-object.
        //following will update "scope[boDetailKey]" with all of wizzard propeties.
        angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
            //If the prop isnt an object but primitive.
            if(formName === scope.wizzard.commonData.wizzard){
                angular.forEach(formIpData.fieldAry, function(field){
                    //field-type
                    if(field.type=="date" || field.type=="time"){
                        scope[boDetailKey][field.name]= new Date();
                    }else if(field.type=="modal"){
                        scope[boDetailKey][field.name]= {};
                    }else{
                        scope[boDetailKey][field.name]= "";
                    }
                    //field-feature
                    if(field.readOnly && !scope[boDetailKey][field.name]){
                        scope[boDetailKey][field.name]= "Will be auto populated.";
                        field.dummyVal= true;
                    }else{
                        field.readOnly= false;
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
                    }else if(field.type=="modal"){
                        scope[boDetailKey][field.name]= null;
                    }else{
                        scope[boDetailKey][formName][field.name]= "";
                    }
                    //field-feature
                    if(field.readOnly && !scope[boDetailKey][field.name]){
                        scope[boDetailKey][formName][field.name]= "Will be auto populated.";
                        field.dummyVal= true;
                    }else{
                        field.readOnly= false;
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

        //fetch existing BO
        webResource[scope.wizzard.commonData.wizzard].get(requestData, 
            //success-callback
            function(dataResp){
                scope[boDetailKey]= dataResp.responseEntity
                //update collection-prop form-table.
                angular.forEach(scope.wizzard.commonData.modalProperties, function(modalPropObj){
                    if(scope[boDetailKey][modalPropObj.propKey]){
                        //iterate over modal-type-prop map/list
                        angular.forEach(scope[boDetailKey][modalPropObj.propKey], function(collectionObj, key){
                            var evenName= "process"+modalPropObj.propKey;
                            var modalparent= modalPropObj.formService+"."+modalPropObj.propKey;
                            $rootScope.$emit(evenName, {
                                "tableRow": collectionObj,
                                "parent": modalparent
                            });
                        });
                    }
                });

                webResource.business.processFormExistingBOInternal(scope, boDetailKey);
            }, 
            //fail-callback
            function(){
                alert(scope.wizzard.commonData.wizzard+" GET failure");
            }
        );
    }; 

    //"scope[boDetailKey]" will hold the entire wizzard-object. 
    //Fetched persisted object has already updated "scope[boDetailKey]".
    //following will update all of "wizzard-prop" with "Fetched-persisted-object"
    webResource.business.processFormExistingBOInternal= function(scope, boDetailKey){
        angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
            if(scope[boDetailKey][formName]){
                formIpData.data= scope[boDetailKey][formName];
            }else{
                formIpData.data= scope[boDetailKey];
            }
            angular.forEach(formIpData.fieldAry, function(field){
                if(field.type==="radio"){
                    formIpData.data[field.name]= formIpData.data[field.name]+"";
                }
                if(field.type==="search"){
                    if(formIpData.data[field.name]){
                        field.val= formIpData.data[field.name].name;
                    }
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

    //We always submit the whole object i.e."wizzard".
    //here entire wizzard(with all the form's in it) is already bi-directionally linked with $scope[boDetailKey].
    webResource.business.submitForm = function(formData, scope, boDetailKey){
        angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
            angular.forEach(formIpData.fieldAry, function(field){
                if(field.type==="radio"){
                    formIpData.data[field.name]= formIpData.data[field.name]+"";
                }
                if(field.dummyVal){
                    formIpData.data[field.name]= "";
                }
                /*
                if(field.type==="time"){
                    formIpData.data[field.name]= $filter("date")(formIpData.data[field.name], "shortTime");
                }
                */
                if(field.type==="object"){
                    formIpData.data[field.name]= {};
                }
            });
        });

        var ipObj= scope[boDetailKey];
        var ipObjStr= angular.toJson(ipObj);
        console.log(formData.service+" : "+ipObjStr);
        //server call
        webResource[formData.service].save({
                action: "saveOrUpdate"
            }, 
            ipObj, 
            function(persistedData){
                if(persistedData.responseData && persistedData.responseData.ERROR && persistedData.responseData.ERROR==="true"){
                    if(persistedData.alertData){
                        angular.forEach(persistedData.alertData, function(alertData){
                            alert(alertData.type+":: "+alertData.desc);
                        });
                    }
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
                            webResource.business.selectWizzardStep(scope, nextWizzardStep, boDetailKey);
                        }
                    }
                }
            },
            function(error){
                console.log("Error:" +error.message);
                console.log(error.stack);
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

    webResource.business.processInternalObj = function(scope, boDetailKey, prop, propKey, ipData, isAry){ 
        if(!scope[boDetailKey][prop]){
            if(isAry){
                scope[boDetailKey][prop]= [];
            }else{
                scope[boDetailKey][prop]= {};
            }
        }
        if(isAry){
            scope[boDetailKey][prop].push(ipData.tableRow);
        }else{
            scope[boDetailKey][prop][ipData.tableRow[propKey]]= ipData.tableRow;
        }
        webResource.business.processFormExistingBOInternal(scope, boDetailKey);
    };

    return webResource;
});