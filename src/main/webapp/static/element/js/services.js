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

    webResource.business.processColumn= function(service, scope, eventName){
        webResource[service].query({
            action: "getColumnData"
        },
        function(response){
            webResource.business.processColumnData(service, scope, response);
        },
        function(){
            alert("["+service+"] - ColumnData failed");
        });
        if(eventName){
            $rootScope.$on(eventName, function(event, data){
                //for editMode, with dynamic-binding, thegrid raw will be auto updated.
                if(data.isCreateMode){
                    webResource.business.processInternalGrid(scope, data, scope.$parent.parentForm);
                }
            });
        }
    };

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
            //Here, grid will be updated with "$emit and $on" AJ event.
            scope.gridData.rowData= [];
            scope.gridData.totalRowCount= 0;
            scope.gridData.currentPageNo= webResource.obj.searchIp.pageNo;
            scope.gridData.rowsPerPage= webResource.obj.searchIp.rowsPerPage;
            scope.gridData.pageAry= 1;

            angular.forEach(scope.gridFutureData, function(futureRow){
                scope.gridData.rowData.push(futureRow);
            });
            webResource.obj[scope.$parent.parentForm]= scope.gridData;
            return;
        }else{
            return webResource.business.fetchBOList(service, scope, searchIp);
        }
    };

    webResource.business.processForm= function(scope, service, boDetailKey, editRow, parentForm, idKey, primaryKeyPropName){
        scope[boDetailKey]= {};
        webResource[service].get({
            action: "getFormData"
        }, function(formResp){
            scope[boDetailKey]= formResp;
            /*
                # If editRow is there, either the entire grid-raw came (from form's collection-prop) -or- 
                primaryID came (to fetch the respective BO from server)
            */
            if(editRow){
                if(editRow.type && editRow.type==="PK"){
                    if(editRow.pkId){
                        webResource.business.fetchBO(service, editRow.pkId, editRow.pkIdName, scope, boDetailKey);
                    }else{
                        webResource.business.processFormNewBOInternal(scope[boDetailKey], scope, boDetailKey);
                    }
                }else{
                    scope[boDetailKey].data= editRow;
                }
            }else{
                webResource.business.processFormNewBOInternal(scope[boDetailKey], scope, boDetailKey);
            }
            //if its a internal-collection-prop
            if(parentForm){
                scope[boDetailKey].parentForm= {};
                scope[boDetailKey].parentForm.data= parentForm;
                scope[boDetailKey].parentForm.name= idKey;
                scope[boDetailKey].parentForm.editRow= editRow;
            }
        }, function(){
            alert("["+service+"] FormData GET failure");
        });
    };

    webResource.business.formUpdateFn= function(scope, formData, eventName, boDetailKey, editRow, parentForm){
        if(eventName){
            var isCreateMode= true;
            if(editRow){
                isCreateMode= false;
            }
            $rootScope.$emit(eventName, {
                "tableRow": formData.data,
                "parent": parentForm,
                "isCreateMode": isCreateMode
            });
            var modalInstances= $rootScope.modalInstances[parentForm];
            if(modalInstances){
                modalInstances.close();
            }
            webResource.business.formObjFieldProcess(scope, boDetailKey, formData);
        }else{
            webResource[formData.name].save({
                action: "saveOrUpdate"
            }, 
            formData.data,
            function(response){
                $location.path(webResource.obj.bannerData.navData.mainNavData[formData.name].subNav.list.path);
            }, function(){
                alert("Save/Update Error");
            });
        }
    };

    /*
        #1. Object-type field
        .....................
        Portal-Form has multiple-types(Text, Email, Object, Select, Etc.. ) of properties. 
        Object-type field is Object within Object.
        Portal-Form is having a limitation that, it could only handle direct-property(single-level Example: User.Address) of an object.
            i.e. It cant handle internal multi-level property Example: User.Address.country

        #2. $scope.$eval
        ................
        There is no easy way to auto update deep-porperty. $scope.$eval helps in this.
            Example: User.Address.country
                Here, if we only know that some deep-prop "country" of "User" object, 
                that we have to access/update without actually jnowing the path, its isnt easy.

        #3. Copy values
        ..............
        All values will be stored directly in "User.XX" instead of "User.YY.XX"
        $scope.$eval will copy value from "User.XX" to "User.YY.XX".
    */
    webResource.business.formObjFieldProcess= function(scope, boDetailKey, formData){
        //this field of type "object".
        //Procee deep prop
        angular.forEach(scope[boDetailKey].fieldAry, function(field){
            if(field.type === "object"){
                angular.forEach(field.object.fieldAry, function(internalField){
                    var exprn="data."+internalField.modalData+"="+formData.data[internalField.modalData];
                    scope.$eval(exprn, formData);
                    scope[boDetailKey].data[internalField.modalData]= scope[boDetailKey].data[field.name][internalField.name];
                });
            }
        });
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
    :::: 2. Init Default values for "hierarchical-internal-prop" of "form.data".
    :::: 3. Process "field-feature"(ReadOnly) if any.
    */
    webResource.business.processFormNewBOInternal= function(formIpData, scope, boDetailKey, valueData){
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

            /*
                scope.valueData contains default value for the form
                Example: message.name, message.emailID. Both of these have default value (current logged-in-user)
            */
            if(scope.valueData && scope.valueData[field.name]){
                formIpData.data[field.name]= scope.valueData[field.name];
            }
        });
        return formIpData; 
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
                                "parent": modalparent,
                                "isCreateMode": true
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

                //---1.
                if(!formIpData.data){
                    formIpData.data= {};
                }

                //---2.
                if(scope[boDetailKey][field.name]){
                    formIpData.data[field.name]= scope[boDetailKey][field.name];
                }

                //---3.
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
                            //without following, there will be one extra dropdown option. i.e "scope.wizzard.wizzardData.delivery.data.addressDetail" should be "12" (addressID)
                            //That is because, "scope.wizzard.wizzardData.delivery.fieldAry[deliveryAddress].values hold [{'label': 'addDesc'}, {'val': 12}]
                            if(scope[boDetailKey][field.name]){
                                formIpData.data[field.name]= scope[boDetailKey][field.name].id;
                            }
                        }
                    }
                }
            });
        });
        //setting primary-id
        scope.wizzard.wizzardData[scope.wizzard.commonData.wizzard].data.id= scope[boDetailKey]["id"];
    }; 

    /*
    ----data.tableRow= Row
    ----data.parentForm= Points to the parent of the Row.?
    ----parentForm= Points to the parent of the Row.? Child is instatiated(From "PortalForm.Modal" and "portalDynamicCtrl") with "parentForm" info.
    
    :::: 0. This for GRID.
    :::: 1. If child-grid-view isnt yet ready, store the "row" in cache i.e. "scope.gridFutureData"
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
    :::: 0. This for FORM.
    :::: Once "row" from "Modal-form" submitted, following will push that row in "scope.wizzard.wizzardData.form.data.prop". To make it availbale in final server-submit.
    */ 
    webResource.business.processInternalObj = function(scope, form, prop, collectionPropKey, ipData, isAry){ 
        //collection could either be list or map.
        //init
        if(!scope.wizzard.wizzardData[form].data[prop]){
            if(isAry){
                scope.wizzard.wizzardData[form].data[prop]= [];
            }else{
                scope.wizzard.wizzardData[form].data[prop]= {};
            }
        }
        //if arry
        if(isAry){
            //check if its one new ele came or existing ele is getting editted.
            var existingEleIdx;
            for(var i=0; i<scope.wizzard.wizzardData[form].data[prop].length; i++){
                var ele= scope.wizzard.wizzardData[form].data[prop][i];
                if(ele.portalId == ipData.tableRow.portalId){
                    existingEleIdx= i;
                    break;
                }
            }
            //element is getting editted.
            if(existingEleIdx){
                scope.wizzard.wizzardData[form].data[prop][existingEleIdx]= ipData.tableRow;
            }
            //new element
            else{
                scope.wizzard.wizzardData[form].data[prop].push(ipData.tableRow);
            }
            
        }
        //if map
        else{
            webResource.business.processInternalObj_existingEle(scope, form, prop, collectionPropKey, ipData);
            if(!ipData.tableRow[collectionPropKey]){
                ipData.tableRow[collectionPropKey]= new Date().getTime();
            }
            var collectionKey= ipData.tableRow[collectionPropKey];
            scope.wizzard.wizzardData[form].data[prop][collectionKey]= ipData.tableRow;
        }
    };

    webResource.business.processInternalObj_existingEle = function(scope, form, prop, collectionPropKey, ipData){
        var existingEleKey;
        /*
            Example: User {
                name: "123",
                AddressDetail: {
                    one: {}
                    two: {}
                }
            }

            scope.wizzard.wizzardData[form].data[prop]  
                ===> User.AddressDetail
            ele                                         
                ===> one: {}
        */
        angular.forEach(scope.wizzard.wizzardData[form].data[prop], function(eVal, eKey){
            if(!existingEleKey){
                if(eVal.portalId == ipData.tableRow.portalId){
                    existingEleKey= eKey;
                }
            }
        });
        //Remove this existing element. Updated element will be addedlater by processInternalObj()
        if(existingEleKey){
            delete scope.wizzard.wizzardData[form].data[prop][existingEleKey];
        }
    }; 

    //We always submit the whole object i.e."wizzard".
    /*
    :::: 1. Process the Wizzard that need work [Example: Wizzard.selectField hold the ID for dropdown to work. In form, we need to submit the object.]
    :::: 2. formData.data ==> scope[boDetailKey]
    :::: 3. Server-call: Submit form
    :::: 4. persistedData ==> scope[boDetailKey]
    :::: 5. Manage Wizzard-Step: If Current-Step is last step, move to List-View. Or mark Current-Step is last and move to Next-Step.
    */
    webResource.business.submitForm = function(formData, scope, boDetailKey){
        //Process the wizzard-step that need the work
        angular.forEach(scope.wizzard.commonData.wizzardData, function(wizzardName){
            var wizzard= scope.wizzard.wizzardData[wizzardName];
            angular.forEach(wizzard.fieldAry, function(field){
                /*
                Example:
                job.deliveryAddress <== job.client.addressDetail[idx]
                from deliveryAddress dropdown we get the addresID. Here we fetch equivalent address from job.client.addressDetail and put it in job.deliveryAddress
                */
                if(field.type==="select"){
                    //client.addressDetail.addressStr
                    var sourcePathEles= field.source.split("."); 
                    //Example: $scope.jobDetail.client.addressDetail
                    var sourceAry= scope[boDetailKey][sourcePathEles[0]][sourcePathEles[1]];
                    angular.forEach(sourceAry, function(ele, key){
                        if(wizzard.data[field.name] == ele.id){
                            scope[boDetailKey][field.name]= ele;
                            return;
                        }
                    });
                }
            });
        });

        //[For only the form that's submitted from UI]
        //  formData.data ==> scope[boDetailKey]
        angular.forEach(formData.fieldAry, function(field){
            if(field.type==="radio"){
                //this is required for proper radio-select which works with str-compare.
                formData.data[field.name]= formData.data[field.name]+"";
            }

            //dummy value such as "will be auto-gen" will be created by server.
            if(field.dummyVal){
                formData.data[field.name]= "";
            }

            var val= formData.data[field.name];
            if(field.type!="select"){
                scope[boDetailKey][field.name]= val;
            }
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
                        $location.path(alphaplusService.obj.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.list.path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= scope.wizzard.wizzardStepData[formData.form];
                        currentWizzardStep.submitted= true;
                        //if obj wasmt persisted already, now cahrge URL hash from "new" ==> "update". 
                        //After this, it will still be on the same wizzard-step.
                        if(currentWizzardStep.isCoreStep && webResource.business.isInCreateMode()){
                            var path= alphaplusService.obj.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.update.path+"/"+persistedData.responseEntity.id;
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
    webResource.business.fetchBO = function(service, id, idKey, scope, boDetailKey, ignoreStoringBO){
        var getReq= {};
        getReq[idKey]= id;
        getReq.action= "get";
        return webResource[service].get(getReq, function(respData){
            if(!ignoreStoringBO){
                scope[boDetailKey].data= respData.responseEntity;
            }
        }, function(){
            alert("["+service+"] : GET failure");
        }).$promise;
    };

    /*
    :::: 1. PersistedData/Fun-Arg-Data  ==> scope[boDetailKey]
    :::: 2. formdata(From server)       ==> scope[boDetailKey].formData
    */
    webResource.business.processSummary = function(service, id, idKey, scope, boDetailKey, ipObj){
        scope[boDetailKey]= {};
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
    webResource.business.viewBO= function(templateURL, controller, $uibModal, ipObj){ 
        var resolveObj= {};
        angular.forEach(ipObj, function(value, key){
            resolveObj[key]= function(){
                return value;
            };
        });

        var modalInstance= $uibModal.open({
            templateUrl: templateURL,
            controller: controller,
            size: 'lg',
            resolve: resolveObj
        });

        if(resolveObj && ipObj.parentForm){
            $rootScope.modalInstances[ipObj.parentForm]= modalInstance;
        }
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

    webResource.business.isInCreateMode= function(){
        var xTabName= $location.path().split("/")[2];
        if(xTabName == 'new'){
            return true;
        }else{
            return false;
        }
    };

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