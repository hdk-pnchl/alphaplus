var serviceM= angular.module('servicesM', ['ngResource', 'ui.bootstrap']);

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

    /*
    :::: 1. scope.gridData.columnData ==> Server Json ColumnData
    :::: 2. If, Child-Grid, put defauts for scope.gridData(rowData, totalRowCount, currentPageNo, rowsPerPage, pageAry)
    :::: 3. Else, "fetchBOList"
    */
    webResource.business.processColumnData= function(service, scope, columnDataResp, searchIp){
        scope.gridData= {};
        scope.gridData.columnData= columnDataResp;
        scope.gridData.columnData.action= true;
        if(scope.$parent.parentForm){
            //Its a child-grid and will dynamically be created.
            //Here grid will be updated with "$emit and $on" AJ event.
            scope.gridData.rowData= [];
            scope.gridData.totalRowCount= 0;
            scope.gridData.currentPageNo= webResource.obj.searchIp.pageNo;
            scope.gridData.rowsPerPage= webResource.obj.searchIp.rowsPerPage;
            scope.gridData.pageAry= 1;

            angular.forEach(scope.gridFutureData, function(futureRow){
                scope.gridData.rowData.push(futureRow);
            });

            return;
        }else{
            return webResource.business.fetchBOList(service, scope, searchIp);
        }
    };

    /*
    :::: For each, Wizzard-Step ==> processFormNewBOInternal
    */
    webResource.business.processFormNewBO= function(scope, boDetailKey){
        //"scope[boDetailKey]" will hold the entire wizzard-object.
        //following will update form.data with required default values.
        angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
            webResource.business.processFormNewBOInternal(formIpData, scope, boDetailKey);
        });
    };

    /*
    :::: 1. Default values for "form.data"
    :::: 2. Init Default values for "hierarchical-initernal-prop" of "form.data".
    :::: 3. Process "field-feature"(ReadOnly) if any.
    */
    webResource.business.processFormNewBOInternal= function(formIpData, scope, boDetailKey){
        formIpData.data= {};
        angular.forEach(formIpData.fieldAry, function(field){
            var exprn="data."+field.modalData;
            //field-type
            if(field.type=="date" || field.type=="time"){
                formIpData.newDate=  new Date();
                exprn=exprn+"=newDate";
            }else if(field.type=="modal"){
                exprn=exprn+"= {}";
            }else if(field.type=="object"){
                webResource.business.processFormNewBOInternal(field.object, scope, boDetailKey);
            }else{
                exprn=exprn+"= ''";

            }
            scope.$eval(exprn, formIpData);

            //field-feature
            if(field.readOnly && !scope[boDetailKey][field.name]){
                formIpData.data[field.name]= "Will be auto populated.";
                field.dummyVal= true;
            }else{
                field.readOnly= false;
            }
        }); 
    };

    /*
    :::: 1. scope[boDetailKey] ==> PersisteEntity
    :::: 2. Form might have Collection-Prop. 
            For each Element in Collection-Prop, Emit "process-**" event. 
            Listner will push that row in "scope.wizzard.wizzardData.form.data.prop" 
    :::: 3. Call: processFormExistingBOInternal
    */
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
                alert("["+scope.wizzard.commonData.wizzard+"] : GET failure");
            }
        );
    }; 

    /*
    ----"scope[boDetailKey]" will hold the entire wizzard-object. 
    ----Fetched persisted object has already updated in "scope[boDetailKey]".
    ----"processFormExistingBOInternal" will update all of "wizzard-prop" with "Fetched-persisted-object"

    :::: 1. form.data ==> scope[boDetailKey]
    :::: 2. Field-specific(radio, search, select) processing
    */
    webResource.business.processFormExistingBOInternal= function(scope, boDetailKey){
        angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
            angular.forEach(formIpData.fieldAry, function(field){
                //scope[boDetailKey] ==> form.data
                if(!formIpData.data){
                    formIpData.data= {};
                }
                if(scope[boDetailKey][field.name]){
                    formIpData.data[field.name]= scope[boDetailKey][field.name];
                }

                //this is to convert the boolean into string and make the "compare" logic work "portalForm"
                if(field.type==="radio"){
                    formIpData.data[field.name]= formIpData.data[field.name]+"";
                }

                //search-obj isnt stored in modal. rather its stored in form.field.val. 
                //Following will update form.field.val with persisted-modal.name.
                //persisted-modal.name ==> form.field.val
                if(field.type==="search"){
                    if(formIpData.data[field.name]){
                        field.val= formIpData.data[field.name].name;
                    }
                }

                //----build dynamic dropdown. Example: Job.deliveryAddress will hold one among the address from  Job.Client.addresses.
                if(field.type=="select" && field.source){
                    var sourcePathEles= field.source.split("."); 
                    if(field.values.length==0 && sourcePathEles.length>=3){
                        //Example: $scope.jobDetail.client.addressDetail
                        var ipAry= scope[boDetailKey][sourcePathEles[0]][sourcePathEles[1]];
                        if(ipAry){
                            angular.forEach(ipAry, function(ele, key){
                                var newEle= {};
                                newEle.label= ele[sourcePathEles[2]];
                                newEle.val= ele.id;
                                //check if value from dropdown already selected and required to show as preselected on form
                                if(scope[boDetailKey][field.name] && scope[boDetailKey][field.name].id==newEle.val){
                                    newEle.selected= true;
                                }
                                field.values.push(newEle);
                            });
                            //without following, there will be one  extra dropdown option. 
                            //That is because, modalData value will not be one among the dropdown value.
                            if(scope[boDetailKey][field.name]){
                                scope[boDetailKey][field.name]= scope[boDetailKey][field.name].id;
                            }
                        }
                    }
                }
            });
        });
        scope.wizzard.wizzardData[scope.wizzard.commonData.wizzard].data.id= scope[boDetailKey]["id"];
    }; 

    /*
    ----data.tableRow= Row
    ----data.parentForm= Points to the parent of the Row.?
    ----parentForm= Points to the parent of the Row.? Child is instatiated(From "PortalForm.Modal" and "portalDynamicCtrl") with "parentForm" info.
    
    :::: 1. If child-grid-view isnt yet ready, store "row" in cache i.e. "scope.gridFutureData"
    :::: 2. Else, push row to "scope.gridData.rowData"
    */
    webResource.business.processInternalGrid= function(scope, data, parentForm){
        if(data.parent && data.parent===parentForm){
            if(scope.gridData && scope.gridData.rowData){
                scope.gridData.rowData.push(data.tableRow);
            }else{
                //if code reached here, grid-view isnt ready yet but the row arrived. Thus, we re storing the row in cashe for future.
                //these future-rows will be pushed to "$scope.gridData.rowData" from "alphaplusService.business.processColumnData"
                if(!scope.gridFutureData){
                    scope.gridFutureData= [];
                }
                scope.gridFutureData.push(data.tableRow);
            }
        }
    };

    /*
    :::: Once "row" from "Modal-form" submitted, "processInternalObj" will push that row in "scope.wizzard.wizzardData.form.data.prop"
    */ 
    webResource.business.processInternalObj = function(scope, form, prop, collectionPropKey, ipData, isAry){ 
        //collection could either be list or map.
        if(!scope.wizzard.wizzardData[form].data[prop]){
            if(isAry){
                scope.wizzard.wizzardData[form].data[prop]= [];
            }else{
                scope.wizzard.wizzardData[form].data[prop]= {};
            }
        }
        if(isAry){
            scope.wizzard.wizzardData[form].data[prop].push(ipData.tableRow);
        }else{
            if(!ipData.tableRow[collectionPropKey]){
                ipData.tableRow[collectionPropKey]= new Date().getTime();
            }
            var collectionKey= ipData.tableRow[collectionPropKey];
            scope.wizzard.wizzardData[form].data[prop][collectionKey]= ipData.tableRow;
        }
    };

    //We always submit the whole object i.e."wizzard".
    /*
    :::: 1. formData.data ==> scope[boDetailKey]
    :::: 2. Server-call: Submit form
    :::: 3. persistedData ==> scope[boDetailKey]
    :::: 4. Manage Wizzard-Step: If Current-Step is last step, move to List-View. Or mark Current-Step is last and move to Next-Step.
    */
    webResource.business.submitForm = function(formData, scope, boDetailKey){
        //formData.data ==> scope[boDetailKey]
        angular.forEach(formData.fieldAry, function(field){
            if(field.type==="radio"){
                //this is required for proper radio-select which works with str-compare.
                formData.data[field.name]= formData.data[field.name]+"";
            }
            //dummy value such as "will be auto-gen" will be created by server.
            if(field.dummyVal){
                formData.data[field.name]= "";
            }
            //copy app.data to scope.boDetailKey.
            scope[boDetailKey][field.name]= formData.data[field.name];
        });

        var ipObj= scope[boDetailKey];
        console.log(formData.service+" : "+angular.toJson(ipObj));
        //server call
        webResource[formData.service].save({
                action: "saveOrUpdate"
            }, 
            ipObj, 
            function(persistedData){
                //any server-error
                if(persistedData.responseData && persistedData.responseData.ERROR && persistedData.responseData.ERROR==="true"){
                    if(persistedData.alertData){
                        angular.forEach(persistedData.alertData, function(alertData){
                            alert("["+alertData.type+"] :: ["+alertData.desc+"]");
                        });
                    }
                }else{
                    //persistedData ==> scope[boDetailKey]
                    //update scope.boDetailKey properties which came from current-submitted-form.
                    angular.forEach(formData.fieldAry, function(field){
                        scope[boDetailKey][field.name]= persistedData.responseEntity[field.name]
                    });
                    //scope[boDetailKey].id= persistedData.responseEntity.id;

                    //if its last step, redirect to grid
                    if(webResource.business.isLastWizzardStep(scope, formData.form)){
                        $location.path(scope.$parent.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.list.path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= scope.wizzard.wizzardStepData[formData.form];
                        currentWizzardStep.submitted= true;
                        //if obj wasmt persisted already, now cahrge URL hash from "new" ==> "update". 
                        //After this, it will still be on the same wizzard-step.
                        if(currentWizzardStep.isCoreStep){
                            var path= scope.$parent.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.update.path+"/"+persistedData.responseEntity.id;
                            $location.path(path); 
                        }else{
                            //move to next step in the wizzard
                            var nextWizzardStep= scope.wizzard.wizzardStepData[currentWizzardStep.next];
                            webResource.business.selectWizzardStep(scope, nextWizzardStep, boDetailKey);
                        }
                    }
                }
            },
            function(error){
                console.log("Error : ["+error.message+"]");
                console.log(error.stack);
                alert("["+formData.service+"] : SAVE failure");
            }
        );
    };

    /*
    :::: PersistedData(From server) ==> scope[boDetailKey]
    */
    webResource.business.fetchBO = function(service, id, idKey, scope, boDetailKey){
         webResource[service].get({
            action: "get",
            idKey: id
        }, function(respData){
            scope[boDetailKey]= respData.responseEntity;
        }, function(){
            alert("["+service+"] : GET failure");
        });
    };

    /*
    :::: 1. PersistedData/Fun-Arg-Data  ==> scope[boDetailKey]
    :::: 2. formdata(From server)       ==> scope[boDetailKey].formData
    */
    webResource.business.processSummary = function(service, id, idKey, scope, boDetailKey, ipObj){
        //bo
        if(!ipObj){
            webResource[service].get({
                action: "get",
                idKey: id
            }, function(respData){
                scope[boDetailKey].data= $scope.clientDetail;
            }, function(){
                alert("["+service+"] : GET failure");
            });
        }else{
            scope[boDetailKey].data= ipObj;
        }
        //formdata
        webResource[service].get({
            action: "getFormData"
        }, function(formResp){
            scope[boDetailKey].formData= formResp;
        }, function(){
            alert("["+service+"] FormData GET failure");
        });
    };

    /*
    :::: Persisted-List ==> scope.gridData.rowData
    */
    webResource.business.fetchBOList = function(service, scope, searchIp){
        if(!searchIp){
            searchIp= webResource.obj.searchIp;
        }
        return webResource[service].save({
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
                alert("["+service+"] : SEARCH failure");
            }
        );
    };

    /*
    :::: $uibModal
    */
    webResource.business.viewBO = function(ipID, ipObj, templateURL, controller, $uibModal){ 
        var resolveObj= {};
        resolveObj.ipID= function(){
            return ipID;
        };
        resolveObj.ipObj= function(){
            return ipObj;
        };

        $uibModal.open({
            templateUrl: templateURL,
            controller: controller,
            size: 'lg',
            resolve: resolveObj
        });
    };

    /*
    :::: 1. All-WizzardStep (From scope.wizzard.wizzardStepData), "wizzardStep.active= false"
    :::: 2. All-Wizzard (From scope.wizzard.wizzardData), "wizzard.isHidden= true"
    :::: 3. Next-WizzardStep, "wizzardStep.active= true"
    :::: 4. Next-Wizzard, "wizzard.isHidden= false"
    */
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

    webResource.business.fetchObjPropBykey= function(o, s){
        s = s.replace(/\[(\w+)\]/g, '.$1'); // convert indexes to properties
        s = s.replace(/^\./, '');           // strip a leading dot
        var a = s.split('.');
        for (var i = 0, n = a.length; i < n; ++i) {
            var k = a[i];
            if (k in o) {
                o = o[k];
            } else {
                return;
            }
        }
        return o;
    };

    return webResource;
});