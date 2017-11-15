var serviceM= angular.module('servicesM', ['ngResource', 'ui.bootstrap']);

serviceM.factory('alphaplusGlobleDataService', function($resource){
    var alphaplusGlobleDataService= {};
    return alphaplusGlobleDataService;
});

serviceM.factory('alphaplusService', function($rootScope, $resource, $location, $filter, $http, 
    addressService,
    clientService,
    contactService,
    jobService,
    instService,
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
    webResource.inst= $resource(webResource.rootPath+'/ctrl/inst/:action',{
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

    webResource.recon= $resource(webResource.rootPath+'/ctrl/recon/:action',{
        action: '@action'
    });  

    webResource.services= {};
    webResource.services.address= addressService;
    webResource.services.client= clientService;
    webResource.services.contact= contactService;
    webResource.services.job= jobService;
    webResource.services.inst= instService;
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
    Example Method Arguments:
        service:    'inst'
        scope:      '$scope'
                        $scope.parentForm >> $scope.$parent.parentForm;
                            $parent >> FormDirective's $scope
        eventName:  'processinstructions'
            
    :::: 1. Fetch 'getColumnData'.
    :::: 2. processColumnInternal()
    :::: 3. If Form-Internal-Grid, listen for the Event.
    */
    webResource.business.processColumn= function(scope){
        webResource[scope.service].query({
            action: "getColumnData"
        },
        function(response){
            webResource.business.processColumnInternal(scope, response);
        },
        function(){
            alert("["+scope.service+"] - ColumnData failed");
        });
        if(scope.dPropData && scope.dPropData.parentForm){
            $rootScope.$on(scope.dPropData.parentForm, function(event, data){
                /*
                In EditMode, Grid will be auto-populated with Dynamic-Binding.
                    On Edit-Click, model will be opened and this model will have reference of the same row(i.e. Object not fetched from server.). 
                */ 
                if(data.isCreateMode){
                    webResource.business.processInternalGrid(scope, data);
                }
            });
        }
    };

    /*
    :::: 1. scope.gridData.columnData ==> Server Json ColumnData
    :::: 2. If, Child-Grid, put defauts for scope.gridData(rowData, totalRowCount, currentPageNo, rowsPerPage, pageAry)
    :::: 3. Else, "fetchBOList"
    */
    webResource.business.processColumnInternal= function(scope, columnDataResp, searchIp){
        if(!scope.gridData){
            scope.gridData= {};
        }
        scope.gridData.columnData= columnDataResp;
        scope.gridData.columnData.action= true;
        //$scope.dPropData
        if(scope.dPropData && scope.dPropData.parentForm){
            //Its a child-grid and will dynamically be updated with "$emit and $on" AJ event.
            scope.gridData.rowData= [];
            scope.gridData.totalRowCount= 0;
            scope.gridData.currentPageNo= webResource.obj.searchIp.pageNo;
            scope.gridData.rowsPerPage= webResource.obj.searchIp.rowsPerPage;
            scope.gridData.pageAry= 1;

            if(!scope.dPropData.gridData){
                //ctrlScope > dynamicCtrlScope > ng-repeat > directiveCtrlScope
               scope.dPropData.gridData= scope.$parent.$parent.$parent.formData.data[scope.$parent.field.name]; 
            }
            angular.forEach(scope.dPropData.gridData, function(existingRow){
                scope.gridData.rowData.push(existingRow);
            });
            /*
            angular.forEach(scope.gridFutureData, function(futureRow){
                scope.gridData.rowData.push(futureRow);
            });
            */
            /*
                For internal-grid-prop, we also keep the row in webResource.obj. 
                Why? Answer: We need GridData for logic i.e. In future while adding a new row, to avoid any 'name' conflict.
            */
            webResource.obj[scope.dPropData.parentForm]= scope.gridData;
            return;
        }else{
            return webResource.business.procesColumnBOList(scope, searchIp);
        }
    };

    /*
    :::: Persisted-List ==> scope.gridData.rowData
    */
    webResource.business.procesColumnBOList = function(scope, searchIp){
        if(!searchIp){
            searchIp= webResource.obj.searchIp;
        }
        return webResource[scope.service].save({
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
                alert("["+scope.service+"] : SEARCH failure");
            }
        );
    };

    /*
    ----Non-Wizzard-Form--Submit (Address, Contact, Plate, Instruction, Message).

    :::: 1. Fetch "Form"
                >> scope[boDetailKey]= formResp;
    :::: 2. Fetch-Existing-BO/Process-New-BO
    :::: 3. Process Conditional Internal-Collection-Prop
    */
    webResource.business.processForm= function(scope){
        scope[scope.apData.boDetailKey]= {};

        scope.afterProcess= function(formResp){
            scope[scope.apData.boDetailKey]= formResp;

            angular.forEach(scope[scope.apData.boDetailKey].fieldAry, function(field){
                if(field.type==="multiSelect" && field.dynamicValues){
                    webResource[field.service].query({
                        action: field.api
                    },
                    function(response){
                        field.values= response;
                    },
                    function(response){
                        alert("["+field.service+"] ["+field.api+"] GET failure");
                    });
                }
            });
            /*
                # 2. If editRow is there, either the entire grid-raw came (from form's-collection-prop) -or- 
                primaryID came(to fetch the respective BO from server)
            */
            if(scope.apData.editRow){
                if(scope.apData.editRow.type && scope.apData.editRow.type==="PK"){
                    if(scope.apData.editRow.pkId){
                        webResource.business.processFormExistingBO(scope);
                    }else{
                        webResource.business.processNewBO(scope, scope[scope.apData.boDetailKey]);
                    }
                }else{
                    scope[scope.apData.boDetailKey].data= scope.apData.editRow;
                }
            }else{
                webResource.business.processNewBO(scope, scope[scope.apData.boDetailKey]);
            }
            //# 3. If its a internal-collection-prop
            if(scope.apData.parentForm){ 
                scope[scope.apData.boDetailKey].parentForm= {};
                scope[scope.apData.boDetailKey].parentForm.data= scope.apData.parentForm;
                scope[scope.apData.boDetailKey].parentForm.name= scope.apData.idKey;
                scope[scope.apData.boDetailKey].parentForm.editRow= scope.apData.editRow;
            }
        };

        //# 1.
        if(scope.apData.formData){
            scope.afterProcess(scope.apData.formData);
        }else{
            webResource[scope.apData.service].get({
                action: "getFormData"
            }, function(formResp){
                scope.afterProcess(formResp);
            }, function(){
                alert("["+scope.apData.service+"] FormData GET failure");
            });
        }
    };

    /*
    ---- 'processWizzard()' called from Wizzard-Form(Client, Job, User)

    :::: 1. Fetch Wizzard-Data
    :::: 2. Process BO New/Existing
    :::: 3. Hightlight Correct-Selected WizzardStep
    :::: 4. Set Listner for Incoming-Collection-Prop-Row to update it into scope.boDetailkey 
    */
    webResource.business.processWizzard= function(scope){
        scope.formService= webResource;
        scope[scope.apData.boDetailKey]= {};

        // #1. Fetch Wizzard-Data
        webResource[scope.apData.service].get({
                action: "getWizzardData"
            }, 
            function(response){
                scope.wizzard= response;

                /*
                scope.apData.eventData= [];
                angular.forEach(scope.wizzard.commonData.modalProperties, function(collectionProp){
                    var event= {};
                    event.form= collectionProp.form;
                    event.collectionPropName= collectionProp.propKey;
                    event.eventName= "process_"+collectionProp.propKey;
                    event.idKeyPropName= collectionProp.idKeyPropName;

                    scope.apData.eventData.push(event);
                });
                */
                //#2. Process BO New/Existing
                if(scope.apData.primaryKeyData && scope.apData.primaryKeyData.val){
                    webResource.business.processWizzardExistingBO(scope);
                }else{
                    angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
                        webResource.business.processNewBO(scope, formIpData);
                    });
                }
                scope[scope.apData.boDetailKey].isReady= true;

                //#3. Select Relevent-WizzardStep
                if(!angular.isUndefined(scope.apData.wizzardStep)){
                    var nextWizzardStep= scope.wizzard.wizzardStepData[scope.apData.wizzardStep];
                    webResource.business.selectWizzardStep(scope, nextWizzardStep, scope.apData.boDetailKey, true);
                }

                /*
                    #4. Set Listner for Incoming-Collection-Prop-Row to update it into scope.boDetailkey .
                    Set listners for events(Mainly, for form with parent i.e. parent-form which is having collection prop, send and event that new grid-raw came)
                
                    Example: eventEle ---[{
                            "form": "addressDetail",
                            "collectionPropName": "addressDetail",
                            "eventName": "processaddressDetail",
                            "idKeyPropName": "title"
                        },{
                        ...
                        }
                    ]
                */

                angular.forEach(scope.wizzard.wizzardData, function(form, formName){
                    angular.forEach(form.fieldAry, function(field){
                        if(field.type==="modal"){
                            var eventEle= {};
                            eventEle.form= formName;
                            eventEle.collectionPropName= field.name;
                            eventEle.eventName= field.parentForm;
                            eventEle.idKeyPropName= field.idKeyPropName;
                            /*
                                'data'>> {
                                    "tableRow": incomingData,
                                    "parent": job.studio.instructions,
                                    "isCreateMode": true
                                }
                            */
                            $rootScope.$on(eventEle.eventName, function(event, eventData){
                                webResource.business.processInternalObj(scope, eventEle, eventData, false);
                            });
                        }
                    });
                });
            }, 
            function(){ 
                alert("["+scope.apData.service+"] GET WizzardData failure");
            }
        );
    };

    /*
    :::: 1. Default values for "form.data"
    :::: 2. Init Default values for "hierarchical-internal-prop" of "form.data".
    :::: 3. Process "field-feature"(ReadOnly) if any.
    */
    webResource.business.processNewBO= function(scope, formIpData){
        formIpData.data= {};
        angular.forEach(formIpData.fieldAry, function(field){
            var exprn="data."+field.modalData;
            //field-type
            //date-time
            if(field.type=="date" || field.type=="time"){
                formIpData.newDate=  new Date();
                exprn=exprn+"=newDate";
            }
            //number
            else if(field.type=="number"){
                formIpData.newNumber= 0;
                exprn=exprn+"=newNumber";
            }
            //radio
            else if(field.type=="radio"){
                formIpData.newRadio= field.options[0].value;
                exprn=exprn+"=newRadio";
            }
            //select
            else if(field.type=="select"){
                if(field.values && field.values.length>0){
                    formIpData.defaultOption= field.values[0].val;
                    exprn=exprn+"=defaultOption";
                }else{
                    exprn=exprn+"= ''";
                }
            }
            //modal
            else if(field.type=="modal"){
                exprn=exprn+"= {}";
            }
            //object
            else if(field.type=="object"){
                webResource.business.processNewBO(scope, field.object);
            }
            //all other
            else{
                exprn=exprn+"= ''";
            }
            scope.$eval(exprn, formIpData);

            //field-feature
            if(field.readOnly && field.type!=="number" && !scope[scope.apData.boDetailKey][field.name]){
                formIpData.data[field.name]= "Will be auto populated.";
                field.dummyVal= true;
            }

            /*
            Default-Value:
                scope.valueData contains default value for the form.
                    Example: message.name, message.emailID. Both of these have default value (current logged-in-user)
            */
            if(scope.valueData && scope.valueData[field.name]){
                formIpData.data[field.name]= scope.valueData[field.name];
            }
        });
        return formIpData; 
    };

    /*
    Target: 
        From:   scope.boDetailKey
        To:     'Form.Data.Field'

    Facts:
    ----"scope[boDetailKey]" will hold the entire wizzard-object. 
    ----Fetched persisted object has already updated in "scope[boDetailKey]".
    ----"processFormExistingBOInternal" will update all of "wizzard-prop" with "Fetched-persisted-object".

    Flow:
    :::: 1. Fetch value from "scope[boDetailKey]" and put it in "formIpData.data".
    :::: 2. Field-specific(radio, search, select) processing.
    :::: 3. Set 'Primary-ID'.
    */
    webResource.business.processExistingBO= function(scope, formIpData){
        // #1. Fetch value from 'scope.boDetailKey' and put it in 'form.data'
        //---#1.A. 'Form.Data' Init
        if(angular.isUndefined(formIpData.data)){
            formIpData.data= {};
        }
        angular.forEach(formIpData.fieldAry, function(field){
            //scope[scope.apData.boDetailKey] ==> form.data
            //---#1.B. 'Form.Data.Field' Init
            if(!angular.isUndefined(scope[scope.apData.boDetailKey][field.name]) || 
                !angular.isUndefined(scope[scope.apData.boDetailKey][formIpData.parent])){
                if(formIpData.parent){
                    if(scope[scope.apData.boDetailKey][formIpData.parent]){
                        formIpData.data[field.name]= scope[scope.apData.boDetailKey][formIpData.parent][field.name];
                    }else{
                        //TODO: Put default value
                        console.log("No default value for: "+scope.apData.boDetailKey+">"+formIpData.parent+">"+field.name);
                    }
                }else{
                    formIpData.data[field.name]= scope[scope.apData.boDetailKey][field.name];
                }
            }

            // #2. Field-specific(radio, search, select) processing

            //------ 'Form.Data.Field' is already populated by now
            //---1. Radio
            //this is to convert the boolean into string and make the "compare" logic work "portalForm"
            if(field.type==="radio"){
                formIpData.data[field.name]= formIpData.data[field.name]+"";
            }
            //---2. Search
            /*
            Search-Field-Value isnt stored in "Form.Data", rather its stored in Form.field.val. 
            Following will update form.field.val with persisted-modal.name.
                persisted-modal.name ==> form.field.val
            */
            if(field.type==="search"){
                if(formIpData.data[field.name]){
                    field.val= formIpData.data[field.name].name;
                }
            }
            //---3. Select: Dynamic-Dropdown
            /*
                Example: "Job.deliveryAddress" will hold one among the address from "Job.Client.addresses".
                Algo:
                    1. Split "field.source" by ','. Count should be >=3.
                    2. Fetch 'DropDown' values from scope[data.boDetailKey].
                    3. Build 'DropDown' Ele and push it in "Form.field.values"
                    4. "Form.data.field" will have 'id' of the 'DropDown' Ele.
            */
            if(field.type=="select"){
                if(field.source){
                    var sourcePathEles= field.source.split("."); 
                    if(field.values.length==0 && sourcePathEles.length>=3){
                        //Example: $scope.jobDetail.client.addressDetail
                        var ipAry= scope[scope.apData.boDetailKey][sourcePathEles[0]][sourcePathEles[1]];
                        if(ipAry){
                            var isOpSelected= false;
                            angular.forEach(ipAry, function(ele, key){
                                var newEle= {};
                                newEle.label= ele[sourcePathEles[2]];
                                newEle.val= ele.id;
                                //check if value from dropdown already selected and required to show as preselected on form
                                if(scope[scope.apData.boDetailKey][field.name] && scope[scope.apData.boDetailKey][field.name].id==newEle.val){
                                    newEle.selected= true;
                                    isOpSelected= true;
                                }
                                field.values.push(newEle);
                            });
                            //select the default-option
                            /**/
                            if(!isOpSelected && field.values.length>0){
                                field.values[0].selected= true;
                            }
                            //without following, there will be one extra dropdown option. i.e "scope.wizzard.wizzardData.delivery.data.addressDetail" should be "12" (addressID)
                            //That is because, "scope.wizzard.wizzardData.delivery.fieldAry[deliveryAddress].values hold [{'label': 'addDesc'}, {'val': 12}]
                            if(scope[scope.apData.boDetailKey][field.name]){
                                formIpData.data[field.name]= scope[scope.apData.boDetailKey][field.name].id;
                            }
                        }
                    }
                }else{
                    console.log("Select-Type-Value: "+formIpData.data[field.name]);
                }
            }
        });
    };

    /*
    :::: PersistedData(From server) ==> scope[boDetailKey]
    */
    webResource.business.processFormExistingBO= function(scope){
        var getReq= {};
        getReq[scope.apData.editRow.pkIdName]= scope.apData.editRow.pkId;
        getReq.action= "get";
        return webResource[scope.apData.service].get(getReq, function(respData){
            if(!scope.apData.ignoreStoringBO){
                scope[scope.apData.boDetailKey].data= respData.responseEntity;
            }
            webResource.business.processExistingBO(scope, scope[scope.apData.boDetailKey]); 
        }, function(){
            alert("["+scope.apData.service+"] : GET failure");
        }).$promise;
    };

    /*
    :::: 1. Fetch Existing-BO
                Existing-BO ==> scope[boDetailKey]
    :::: 2. Emit event for Ele's-in-collection-prop
                Form might have Collection-Prop. 
                    For each Element in Collection-Prop, Emit "process-**" event. 
                    Listner will push that row in "scope.wizzard.wizzardData.form.data.prop" 
    :::: 3. ProcessFormExistingBOInternal()
    */
    /* ---evolve this
    Target: 
        From:   scope.boDetailKey
        To:     'Form.Data.Field'

    Facts:
    ----"scope[boDetailKey]" will hold the entire wizzard-object. 
    ----Fetched persisted object has already updated in "scope[boDetailKey]".
    ----"processFormExistingBOInternal" will update all of "wizzard-prop" with "Fetched-persisted-object".

    Flow:
    :::: 1. Fetch value from "scope[boDetailKey]" and put it in "formIpData.data".
    :::: 2. Field-specific(radio, search, select) processing.
    :::: 3. Set 'Primary-ID'.
    */
    webResource.business.processWizzardExistingBO= function(scope){
        var requestData= {};
        requestData[scope.apData.primaryKeyData.propName]= scope.apData.primaryKeyData.val;
        requestData.action= "get";

        //#1. Fetch Existing-BO
        webResource[scope.wizzard.commonData.wizzard].get(requestData, 
            //success-callback
            function(dataResp){
                scope[scope.apData.boDetailKey]= dataResp.responseEntity
                //#2. Emit event's for collection-prop
                /*
                angular.forEach(scope.wizzard.commonData.modalProperties, function(modalPropObj){
                    var pathEleAry= modalPropObj.propKey.split(".");
                    if(pathEleAry.length>1){
                        var targetVal= webResource.business.fetchPropByPath(scope[scope.apData.boDetailKey], modalPropObj.propKey);
                        if(targetVal){
                            webResource.business.processCollectionProp(scope, modalPropObj, targetVal);
                        }
                    }else{
                        if(scope[scope.apData.boDetailKey][modalPropObj.propKey]){
                            webResource.business.processCollectionProp(scope, modalPropObj, scope[scope.apData.boDetailKey][modalPropObj.propKey]);
                        }
                    }
                });
                */
                angular.forEach(scope.wizzard.wizzardData, function(formIpData, formName){
                    webResource.business.processExistingBO(scope, formIpData); 
                });
                //Set 'Primary-ID'
                scope.wizzard.wizzardData[scope.wizzard.commonData.wizzard].data.id= scope[scope.apData.boDetailKey].id;
            }, 
            //fail-callback
            function(){
                alert("["+scope.wizzard.commonData.wizzard+"] : GET failure");
            }
        );
    };

    /*
    Not used anymore.
    */
    webResource.business.processCollectionProp= function(scope, modalPropObj, targetVal){
        //iterate over modal-type-prop map/list
        angular.forEach(targetVal, function(collectionObj, key){
            var evenName= "process"+modalPropObj.propKey;
            var modalparent= scope.wizzard.commonData.wizzard+"."+modalPropObj.form+"."+modalPropObj.propKey;
            console.log(evenName+" <<<-->>> "+modalparent);
            $rootScope.$emit(evenName, {
                "tableRow": collectionObj,
                "parent": modalparent,
                "isCreateMode": true
            });
        });
    };

    /*
        #1. process field of "Object" type
        .....................
        Portal-Form has multiple-types(Text, Email, Object, Select, Etc.. ) of fileds\properties. 
        Object-type field is Object within Object.
        Portal-Form is having a limitation, that it could only handle direct-property(single-level Example: User.Address) of an object.
            i.e. It cant handle internal multi-level property Example: User.Address.country

        #2. $scope.$eval
        ................
        There is no easy way to auto update deep-porperty. $scope.$eval helps in this.
            Example: User.Address.country
                Here, if we only know that some deep-prop "country" of "User" object, that we have to access/update without actually knowing the path, its not easy.

        #3. Copy values
        ..............
        All values will be stored directly in "User.XX" instead of "User.YY.XX"
        $scope.$eval will copy value from "User.XX" to "User.YY.XX".
    */
    webResource.business.processFormObjField= function(scope){
        //this field of type "object".
        //Procee deep prop
        angular.forEach(scope[scope.apData.boDetailKey].fieldAry, function(field){
            if(field.type === "object"){
                angular.forEach(field.object.fieldAry, function(internalField){
                    var exprn="data."+internalField.modalData+"="+scope.apData.submitFormData.data[internalField.modalData];
                    scope.$eval(exprn, scope.apData.submitFormData);
                    scope[scope.apData.boDetailKey].data[internalField.modalData]= scope[scope.apData.boDetailKey].data[field.name][internalField.name];
                });
            }
        });
    };

    /*
    ----eventData.tableRow      >>> Row
    ----eventData.parentForm    >>> Points to the parent of the Row.?
    ----parentForm              >>> Points to the parent of the Row.? Child is instatiated(From "PortalForm.Modal" and "portalDynamicCtrl") with "parentForm" info.
    ----eventData               >>> {
            "parent": form-scope.apData.parentForm, ---Example: "job.job.instructions"
            "isCreateMode": isCreateMode,
            "formData": form-scope[form-scope.apData.boDetailKey];
        }
    ----scope                   >>> grid-scope
    */
    webResource.business.processInternalGrid= function(scope, eventData){
        var isRowForThisGrid= (eventData.parent && eventData.parent===scope.dPropData.parentForm);
        //console.log(eventData.parent+" === "+scope.dPropData.parentForm+" --> "+isRowForThisGrid);
        if(isRowForThisGrid){
            //console.log(eventData.parent+" === "+sscope.dPropData.parentForm);
            if(scope.gridData && scope.gridData.rowData){
                scope.gridData.rowData.push(eventData.formData.data);
            }
            /*
            else{

                //if code reached here, grid-view isnt ready yet but the row arrived. Thus, we re storing the row in cashe for future.
                //these future-rows will be pushed to "$scope.gridData.rowData" from "webResource.business.processColumnData"
                if(!scope.gridFutureData){
                    scope.gridFutureData= [];
                }
                scope.gridFutureData.push(eventData.tableRow);
            }
            */
        }
    };
    /*
    ---- @scope       >>> wizzard-Scope
    ---- @eventEle    >>> {
            "form": "studio",
            "collectionPropName": "instructions",
            "eventName": "job.studio.instructions",
            "idKeyPropName": "title"
        }
    ---- @eventData   >>> {
            "parent": scope.apData.parentForm, ---Example: "job.job.instructions"
            "isCreateMode": isCreateMode,
            "formData": scope[scope.apData.boDetailKey];
        }
    ---- @isAry       >>> false(default)

    ---- * Form-Ctrl
    ---- * form-submit > 'formUpdateFn()' > emit-event (passing 'submitted-form')
    ---- * each 'ele' in 'wizzard-scope.wizzard.wizzard-data.form.data.collectionProp' will have 'portal-id' (form rep collectionProp with ng-tabel. ng-tabel adds 'portal-id' to eachr-row)

    :::: 1. Init's 'collectionProp' as ary/obj.
    :::: 2. delete/override existing 'ele'
    :::: 3. Init 'idKeyPropName'
    :::: 4. populate 'collectionProp'
    */ 
    webResource.business.processInternalObj = function(scope, eventEle, eventData, isAry){ 
        var form= scope.wizzard.wizzardData[eventEle.form];
        var collectionProp= form.data[eventEle.collectionPropName];

        // #1. Init --> 'wizzard.wizzardData.form.data.collectionProp'. 'collectionProp' could either be 'list' or 'map'.
        if(!collectionProp){
            if(isAry){
                collectionProp= [];
            }else{
                collectionProp= {};
            }
        }

        var existingElePath= webResource.business.processInternalObj_existingEle(collectionProp, eventData, isAry);

        //if-arry
        if(isAry){
            //existing-element
            if(existingElePath){
                collectionProp[existingElePath]= eventData.formData.data;
            }
            //new-element
            else{
                collectionProp.push(eventData.formData.data);
            }
        }
        //if-map
        else{
            //Remove this existing-element. Updated element will be addedlater by processInternalObj()
            if(existingElePath){
                delete collectionProp[existingElePath];
            }
            if(!eventData.formData.data[eventEle.idKeyPropName]){
                eventData.formData.data[eventEle.idKeyPropName]= new Date().getTime();
            }
            var collectionKey= eventData.formData.data[eventEle.idKeyPropName];
            collectionProp[collectionKey]= eventData.formData.data;
        }
    };
    /*
        collectionProp >>> wizzard.form.data.collectionProp
            wizzard.form.data --- {
                    name: "123",
                    AddressDetail: {
                        one: {}
                        two: {}
                    }
                }
    */
    webResource.business.processInternalObj_existingEle = function(collectionProp, eventData, isAry){
        var existingElePath;
        if(!angular.isUndefined(eventData.formData.data.portalId)){
            if(isAry){
                for(var i=0; i<collectionProp.length; i++){
                    var ele= collectionProp[i];
                    //'portalId' created from PortalTable
                    if(ele.portalId == eventData.formData.data.portalId){
                        existingElePath= i;
                        break;
                    }
                }
            }else{
                angular.forEach(collectionProp, function(eVal, eKey){
                    if(!existingElePath){
                        if(eVal.portalId == eventData.formData.data.portalId){
                            existingElePath= eKey;
                        }
                    }
                });
            }
        }
        return existingElePath;
    }; 

    /*
        # Non-Wizzard-Form-Submit
        scope >>> form-Scope
    */
    webResource.business.formUpdateFn= function(scope){
        if(scope.apData.parentForm){
            var isCreateMode= true;
            if(scope.apData.editRow){
                isCreateMode= false;
            }
            $rootScope.$emit(scope.apData.parentForm, {
                "parent": scope.apData.parentForm,
                "isCreateMode": isCreateMode,
                "formData": scope[scope.apData.boDetailKey]
            });
            /*
            var modalInstances= $rootScope.modalInstances[scope.apData.parentForm];
            if(modalInstances){
                modalInstances.close();
            }
            webResource.business.processFormObjField(scope);
            */
        }
        //multi-part
        else if(scope[scope.apData.boDetailKey].type && scope[scope.apData.boDetailKey].type === "multi-part"){
            var uploadUrl = "http://localhost:8080/alphaplus/ctrl/recon/saveOrUpdate";
            $http.post(uploadUrl, scope[scope.apData.boDetailKey].multipartData, {
                headers: {'Content-Type': undefined},
                transformRequest: angular.identity
            }).then(function successCallback(response){
                scope.resultData= response;
            })
        }else{
            webResource[scope.apData.service].save({
                action: "saveOrUpdate"
            }, 
            scope[scope.apData.boDetailKey].data,
            function(response){
                if(sscope.apData.nextView){
                    if(scope.apData.nextView==="samePage"){
                        $scope.resultData= response;
                    }
                }else{
                    $location.path(webResource.obj.bannerData.navData.mainNavData[scope.apData.service].subNav.list.path);
                }                
            }, function(){
                alert("Save/Update Error");
            });
        }
    };

    //We always submit the whole object i.e."wizzard".
    /*
    :::: 1. formData.data ==> scope[boDetailKey]
    :::: 2. Submit Form - Server-call
    :::: 4. persistedData ==> scope[boDetailKey]
    :::: 5. Manage Wizzard-Step: If Current-Step is last step, move to List-View. Or mark Current-Step is last and move to Next-Step.
    */
    webResource.business.submitForm = function(formData, scope){
        console.log("--------------------------Wizzard-Form-Data-Start--------------------------")
        console.log(formData.data)
        console.log("--------------------------Wizzard-Form-Data-End--------------------------")
        // #1. formData.data ==> scope[boDetailKey]
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

            // Push the value to scope.boDetailKey

            //Select field-type
            /*
                Example:
                    job.deliveryAddress <== job.client.addressDetail[idx]
                    from deliveryAddress dropdown we get the addresID. Here we fetch equivalent address from job.client.addressDetail and put it in job.deliveryAddress
            */
            if(field.type=="select" && field.dynamicValues){
                //client.addressDetail.addressStr
                var sourcePathEles= field.source.split("."); 
                //Example: scope.jobDetail.client.addressDetail
                var sourceAry= scope[scope.apData.boDetailKey][sourcePathEles[0]][sourcePathEles[1]];
                angular.forEach(sourceAry, function(ele, key){
                    if(wizzard.data[field.name] == ele.id){
                        scope[scope.apData.boDetailKey][field.name]= ele;
                        return;
                    }
                });
            }
            //boDetailKey.parent
            else if(formData.parent){
                if(!scope[scope.apData.boDetailKey][formData.parent]){
                    scope[scope.apData.boDetailKey][formData.parent]= {};
                }
                scope[scope.apData.boDetailKey][formData.parent][field.name]= val;
            }//direct boDetailKey
            else{
                scope[scope.apData.boDetailKey][field.name]= val;
            }
        });

        var ipObj= scope[scope.apData.boDetailKey];
        //console.log(formData.service+" : "+angular.toJson(ipObj));
        //#2. server call
        webResource[formData.service].save({
                action: "saveOrUpdate"
            }, 
            ipObj, 
            function(persistedData){
                //server-error
                if(persistedData.responseData && persistedData.responseData.ERROR && persistedData.responseData.ERROR==="true"){
                    if(persistedData.alertData){
                        angular.forEach(persistedData.alertData, function(alertData){
                            alert("["+alertData.type+"] :: ["+alertData.desc+"]");
                        });
                    }
                }
                //server-success
                else{
                    //# persistedData ==> scope[boDetailKey]
                    //update scope.boDetailKey properties which came from current-submitted-form.
                    angular.forEach(formData.fieldAry, function(field){
                        if(formData.parent){
                            scope[scope.apData.boDetailKey][formData.parent][field.name]= persistedData.responseEntity[formData.parent][field.name];
                        }else{
                            scope[scope.apData.boDetailKey][field.name]= persistedData.responseEntity[field.name];
                        }
                    });
                    //scope[boDetailKey].id= persistedData.responseEntity.id;

                    //# wizzard-step
                    //if its last step, redirect to grid
                    if(webResource.business.isLastWizzardStep(scope, formData.form)){
                        $location.path(webResource.obj.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.list.path);
                    }else{
                        //mark current step as complete
                        var currentWizzardStep= scope.wizzard.wizzardStepData[formData.form];
                        currentWizzardStep.submitted= true;
                        //if obj wasnt persisted already, now change URL hash from "new" ==> "update". 
                        //After this, it will still be on the same wizzard-step.
                        if(currentWizzardStep.isCoreStep && webResource.business.isInCreateMode()){
                            var path= webResource.obj.bannerData.navData.mainNavData[scope.wizzard.commonData.wizzard].subNav.update.path;
                            path= path+"/"+persistedData.responseEntity.id;
                            path= path+"/"+currentWizzardStep.next;
                            $location.path(path); 
                        }else{
                            //move to next step in the wizzard
                            var nextWizzardStep= scope.wizzard.wizzardStepData[currentWizzardStep.next];
                            webResource.business.selectWizzardStep(scope, nextWizzardStep, scope.apData.boDetailKey);
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
    :::: 1. PersistedData/Fun-Arg-Data  ==> scope[boDetailKey]
    :::: 2. formdata(From server)       ==> scope[boDetailKey].formData
    */
    webResource.business.processSummary = function(scope){
        scope[scope.apData.boDetailKey]= {};
        //bo
        if(!scope.apData.ipObj){
            var serverCallIp= {};
            serverCallIp.action= "get";
            serverCallIp[scope.apData.idKey]= scope.apData.id;

            webResource[scope.apData.service].get(serverCallIp, function(respData){
                scope[scope.apData.boDetailKey].data= respData.responseEntity;

                webResource[scope.apData.service].get({
                    action: "getFormData"
                }, function(formResp){
                    scope[scope.apData.boDetailKey]= formResp;

                }, function(){
                    alert("["+scope.apData.service+"] FormData GET failure");
                });

            }, function(){
                alert("["+scope.apData.service+"] : GET failure");
            });
        }else{
            scope[scope.apData.boDetailKey].data= scope.apData.ipObj;
        }
        //formdata
        webResource[scope.apData.service].get({
            action: "getFormData"
        }, function(formResp){
            scope[scope.apData.boDetailKey].formData= formResp;
        }, function(){
            alert("["+scope.apData.service+"] FormData GET failure");
        });
    };

    /*
    :::: $uibModal
    */
    webResource.business.viewBO= function(ipObj){ 
        //to show separate in banner tab. 
        if(ipObj.bannerTab){
            $location.path(webResource.obj.bannerData.navData.mainNavData[ipObj.bannerTab].subNav.update.path+"/"+ipObj.primaryKey);
        }
        //to show separate in modal
        else{
            var resolveObj= {};
            angular.forEach(ipObj.modalData, function(value, key){
                resolveObj[key]= function(){
                    return value;
                };
            });

            var modalInstance= ipObj.uibModalService.open({
                templateUrl: ipObj.templateURL,
                controller: ipObj.controller,
                size: 'lg',
                resolve: resolveObj
            });

            if(ipObj.modalData && ipObj.modalData.parentForm){
                $rootScope.modalInstances[ipObj.modalData.parentForm]= modalInstance;
            }
        }
    };

    /*
    :::: 1. All-WizzardStep (From scope.wizzard.wizzardStepData), "wizzardStep.active= false"
    :::: 2. All-Wizzard (From scope.wizzard.wizzardData), "wizzard.isHidden= true"
    :::: 3. Next-WizzardStep, "wizzardStep.active= true"
    :::: 4. Next-Wizzard, "wizzard.isHidden= false"
    */
    webResource.business.selectWizzardStep= function(scope, nextWizzardStep, boDetailKey, doItOnLoad){
        if((scope[boDetailKey] && scope[boDetailKey].id) || doItOnLoad){
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


    webResource.business.selectBannerDirectiveTab = function(tab) {
        if(tab){
            angular.forEach(webResource.obj.bannerData.navData.mainNavData, function(tab){
              tab.active = false;
            });
            angular.forEach(webResource.obj.bannerData.navData.configNavData, function(tab){
              tab.active = false;
            });
            tab.active = true;
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

    webResource.business.fetchPropByPath= function(obj, pathStr, pattern){
        if(!pattern){
            pattern= ".";
        }
        var pathAry= pathStr.split(pattern);
        if(pathAry.length>1){
            var newObj= obj;
            for(var i=0; i<pathAry.length; i++){
                if(newObj){
                    newObj= newObj[pathAry[i]]
                }
            }
            return newObj;
        }else{
            return obj[pathAry[0]];
        }
    };

    return webResource;
});