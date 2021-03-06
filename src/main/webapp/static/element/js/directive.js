var directiveM= angular.module('directiveM', ['ui.bootstrap', 'servicesM']);

/* -----------------BANNER-----------------*/

directiveM.directive('portalBanner', function(alphaplusService, $uibModal){
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalBanner.html',
        scope: {
            bannerData: '='
        },
        controller: function($scope, $rootScope, $location, $uibModal){
            $scope.selectTab = function(tab) {
                alphaplusService.business.selectBannerDirectiveTab(tab);
            };

            $scope.processTab= function(){
                var xTabName= $location.path().split("/")[1];
                if(xTabName == 'home'){
                    $scope.showHome= true;
                }else{
                    var xTab= $scope.bannerData.navData.mainNavData[xTabName];
                    $scope.selectTab(xTab);
                }
            };

            $rootScope.$on("$locationChangeSuccess", function(event, newUrl, oldUrl, newState, oldState){ 
                $scope.processTab();
            });
            
            $scope.selectHome = function() {
                angular.forEach($scope.bannerData.navData.mainNavData, function(tab){
                  tab.active = false;
                });
                angular.forEach($scope.bannerData.navData.configNavData, function(tab){
                  tab.active = false;
                });
                $scope.bannerData.navData.mainNavData[0].active = true;
            };

            $scope.openModal = function(modalData){ 
                $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: modalData.html,
                    controller: modalData.controller,
                    size: modalData.modalSize,
                    resolve: {
                        data: function(){
                            return modalData.data;
                        }
                    }
                });
            };
        }
        /*
        ,
        compile: function compile(tElement, tAttributes){
            return {
                pre: function preLink($scope, element, attributes){
                },
                post: function postLink($scope, element, attributes){
                    $scope.processTab();
                }
            };
        }
        ,
        link: function($scope, element, attrs, controllers){
            $scope.processTab();
        }
        */
    };
});

/* -----------------TABLE-----------------*/

directiveM.directive("portalTable",function($sce, $uibModal, alphaplusService){
    return {
        restrict: "E",
        templateUrl: "element/html/directive/portalTable.html",
        scope: {
            pdata: "=",
            searchfn: '&',
            editfn: '&',
            viewfn: '&',
            deletefn: '&'
        },
        controller: function($scope) {
            $scope.searchRow= {};
            $scope.selectedRow = null;
            $scope.summary= {};

            $scope.processTRClass = function(row){
                return {
                    "active" : row.selected, 
                    "danger" : row.valid==false, 
                    "success" : row.valid==true
                };
            };
            $scope.initInternalGrid = function(row){
                if(row[$scope.pdata.internalGrid.rowDataSource] && row[$scope.pdata.internalGrid.rowDataSource].length>0){
                    row.internalGrid= {};
                    row.internalGrid.columnData= $scope.pdata.internalGrid.columnData;
                    row.internalGrid.rowData=row[$scope.pdata.internalGrid.rowDataSource];
                    row.internalGrid.totalRowCount= row[$scope.pdata.internalGrid.rowDataSource].length;
                    row.internalGrid.currentPageNo= 1;
                    row.internalGrid.rowsPerPage= row[$scope.pdata.internalGrid.rowDataSource].length;
                    row.internalGrid.pageAry= 1;
                    row.internalGrid.avoidPagination= $scope.pdata.internalGrid.avoidPagination;                    
                    row.internalGrid.hover= true; 
                    row.internalGrid.condensed= true; 
                    row.internalGrid.striped= true; 
                }
            };
            //$scope.active=   false;
            $scope.viewColumn = function(propObj, column){
                var ipObj= {
                    modalData: {
                        viewRow: propObj,
                        primaryKey: propObj.id
                    },
                    templateURL: column.templateURL,
                    controller: column.controller,
                    uibModalService: $uibModal
                };
                alphaplusService.business.viewBO(ipObj);
            };
            $scope.sort= function(sortCol) {
                $scope.sortCol = sortCol;
                $scope.sortOrder = !$scope.sortOrder;
            };        
            $scope.selectRow = function(selectedRow){
                $scope.selectedRow= selectedRow;
                angular.forEach($scope.pdata.rowData, function(currentRow){
                  currentRow.selected = false;
                });
                if(selectedRow.expanded){
                    selectedRow.expanded = false;
                }else{
                    selectedRow.expanded = true;
                }
                
            };
            $scope.fetchSummary= function(row) {
                //if($scope.rowSelectionCheck()){
                $scope.viewRowUpdate($scope.selectedRow);
                //}
            }           
            //--row actions 
            $scope.rowSelectionCheck= function(){
                if(!$scope.selectedRow){
                    alert("Please select 1 row!");
                    return false;
                }
                return true;
            };               
            $scope.editRow= function(row){
                console.log(row);
                $scope.editRowUpdate(row);
            };
            $scope.viewRow= function(row){
                $scope.viewRowUpdate(row);
            };
            $scope.deleteRow= function(row){
                $scope.deleteRowUpdate(row);
            };
            $scope.searchData= function(pageNo, rowsPerPage){
                var searchIp= {};
                searchIp.pageNo= pageNo;
                searchIp.rowsPerPage= rowsPerPage;
                $scope.pdata.columnData.forEach(function(col){
                    if($scope.searchRow[col.name]){
                        col.value= $scope.searchRow[col.name];
                    }
                });
                searchIp.searchData= $scope.pdata.columnData;
                $scope.searchDataUpdate(searchIp);
            };
            $scope.enterSearchData = function(keyEvent) {
                if (keyEvent.which === 13){
                    $scope.searchData(1, $scope.pdata.rowsPerPage);
                }
            }

            $scope.fetchObjPropBykey= function(o, s){
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
            }
        },
        link: function($scope, element, attrs, controllers){
            $scope.editRowUpdate = function(editRow) {
                //alert("editRowUpdate");
                $scope.editfn({
                    "editRow": editRow,
                });
            };
             $scope.viewRowUpdate = function(viewRow) {
                //alert("viewRowUpdate");
                $scope.viewfn({
                    "viewRow":viewRow,
                });
            };
            $scope.deleteRowUpdate = function(deleteRow) {
                //alert("deleteRowUpdate");
                $scope.deletefn({
                    "deleteRow":deleteRow,
                });
            };
            $scope.searchDataUpdate = function(searchIp) {
                //alert("deleteRowUpdate");
                $scope.searchfn({
                    "searchIp": searchIp,
                });
            };
        }
    }; 
});

/* -----------------FORM-----------------*/

/*
    There are 4-ways to manage internal-object-prop (Example: 'job.studio').
        1. Wizzard-Form specfic to internal-object-prop. 
                -'wizzardData.form' should have prop named 'parent'
        2. field-type: 'obj'
                -inline form will appear
        3. field-type: 'search'. 
                -Used if internal-object-prop is already built and just need to be assigned.
        4. field-type: 'model-obj'
                -Not ready.
                -Model will be open to create internal-object-prop.

    If existing,
        :::: 1. $scope.wizzard       ==> WizzardData
        :::: 2. $scope.boDetailKey   ==> PersisteEntity
        :::: 3. Form might have Collection-Prop. 
                    :::: For each Collection-Prop-Element from $scope.boDetailKey, Emit "process-**" event. 
                    :::: Listner will push that row in "$scope.wizzard.wizzardData.XXX-form.data.Collection-Prop" 
        :::: 4. ForEach "Prop" in $scope.wizzard.wizzardData.XXX-form.data, 
                    :::: prop ==> $scope.boDetailKey.prop
                ForEach "field" in $scope.wizzard.wizzardData.XXX-form.fieldAry, 
                    :::: Field-specific(radio, search, select) processing
        :::: 5. Process the Wizzard that need work [Example: Wizzard.selectField hold the ID for dropdown to work. In form, we need to submit the object.]
    On Wizzard-Form Submit,
        :::: 6. scope[boDetailKey] ==> formData.data
    After Submit-Form Server-call: 
        :::: 7. scope[boDetailKey] ==> persistedData
        :::: 8. Manage Wizzard-Step: If Current-Step is last step, move to List-View. Or mark Current-Step is last and move to Next-Step.

    How "Model" field work ?
        --- Same  
        #1. On-AddModel-Click, 
                Open "Model" with $uibModal. 
                Pass "ParentForm" as argument to $uibModal. 
                    * Why? 
                        * Internal-Grid is child of Parent-Form. When UI-Model-Form is submitted, new row is added in Internal-Grid.
                        * Internal-Grid type could be 'Instruction, Address, Contact'. There could multiple Internal-Grid of the same Type in different Parent-Form(Client and User both have Address.)
                        * Internal-Grid works with event's i.e All Internal-Grid is listening for incoming-row event. 
                        * Internal-Grid has a UniqueGridID i.e. 'Parent-Form'. Incoming-Row as well is having the UniqueGridID. 
                        * Once Internal-Grid gets the Row, depending on matching of Row-ID and Grid-ID, Internal-Grid decides if row belongs to it.

                        *  When UI-Model-Form is submitted, 
                                * Internal-Grid is a Map.
                                * Submited form is Row for Internal-Grid.
                                * For Directive-FormData with 'ParentForm' prop, Directive-SubmitForm() checks if Row with same Key is already there in Directive-Grid
                                    * How does check ?
                                        * 'alphaplusService.obj' keeps all internal-grid [CodeLogic in 'alphaplusService.processColumnInternal']
                                            * Against "ParentForm" key, the grid is kept in 'alphaplusService.obj' 
                                        * It iterates all existing-row's in Intenral-Grid and metches for existing-row-key against new-row-key. If matched, its duplicate.

        #2. "ParentForm"
                * Init on DirectiveScope.FormData in 'portal-form.html'
                    Values:
                        $scope.formData.wizzard+"."+form.form+"."+field.name;
                        form.form+"."+field.name;
                * Both, 'form' and 'list' controller get their identity "parentForm" 
                    FormController
                        'ParentForm' build with $DirectiveScope.processModel()
                    ListController
                    'parentForm' ==> form.service + form.modelData.
                * "ng-init" populates "parentForm" in portal-form-directive's $scope.
                * Where 'directive-scope.parentForm' be used ?
                    It will be used in Grid/Form-Address/Contact/Inst-Ctrl to know to whom the incoming row belong to. 'Row' is processed with '$on' event.
                * How will Grid/Form-Address/Contact/Inst-Ctrl would get 'parentForm prop' ?
                    With 'portal-dynamic-ctrl' directive.
        #3. Model-Field is having 2 ele's in it 
                AddRowButton
                    When you click this, $uibmodel opens model with 'FormController'.
                    'FormController' gets 'parentForm' from processForm().
                    When form submitted i.e. new row arrives, FormControllers Emit's New-RowArrival event. 
                        Its heard by grid and collection-prop in scope.boDetailKey.
                Grid. 
                    Grid is having 'portal-table and' 'portal-dynamic-ctrl' directive in it. 
                        'portal-dynamic-ctrl' directive provides 'parentForm' to GridController. 

        #4. $Emit/$On
                $Emit
                    formUpdateFn()
                        New-Row
                $On
                    processColumn()
                        processInternalGrid()
                            $GridCtrlrScope.ParentForm === 
                    processWizzard()
                        processInternalObj()
*/
directiveM.directive('portalForm', function ($compile, $parse, $uibModal, $interpolate, $rootScope, $http, alphaplusService) {
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalForm.html',
        scope: {
            formData: '=',
            formService: '=',
            actionfn: '&',
            exec: '='
        },
        controller: function($scope, $element, $attrs, $transclude){
            $scope.multiSelectDataHolder= [];
            $scope.multiSelectSettings= { 
                scrollable: true, 
                showCheckAll: false, 
                showUncheckAll: false,
                scrollableHeight: '80px' 
            };

            $scope.initMultiSelectSettings= function(field){
                //add default setting for field-type 'multiSelect'
                if(field.settings){
                    //formData.fieldAry.field.settings will only have specific settings. the remaining default settings will come from $scope.multiSelectSettings.
                    angular.forEach($scope.multiSelectSettings, function(value, key){
                        if(!field.settings[key]){
                            field.settings[key]= value;
                        }
                    });
                }else{
                    field.settings= $scope.multiSelectSettings;
                }
                if(!$scope.formData.data[field.modalData]){
                    $scope.formData.data[field.modalData]= [];    
                }
            };

            $scope.removeMultiselect= function(item, field){
                var multiSelectVal= $scope.formData.data[field.name];
                var idx;
                for(var i=0; i<multiSelectVal.length; i++){
                    var ele= multiSelectVal[i];
                    if(ele[field.matchFieldKey]===item[field.matchFieldKey]){
                        idx= i; break;
                    }
                }
                //remove data
                multiSelectVal.splice(idx, 1);
                //do respective work on target-fields.
                angular.forEach(field.targets, function(targetFieldName){
                    //execute fields callbacks (defined in controller)
                    angular.forEach($scope.exec.fn[targetFieldName].fn, function(fn, fnName){
                        //pass data to field's callback
                        //scope.tempData.field.tools.removeMultiselect.whichField=field;
                        //which options removed from which field ?
                        $scope.tempData= {};
                        $scope.tempData.field= {};
                        $scope.tempData.field[targetFieldName]= {};
                        $scope.tempData.field[targetFieldName].removeMultiselect= {};
                        $scope.tempData.field[targetFieldName].removeMultiselect.whichField=field;
                        $scope.tempData.field[targetFieldName].removeMultiselect.whichOption=item;
                        fn($scope);
                    });
                });
            };

            $scope.fetchField= function(ipFieldName){
                var opField;
                angular.forEach($scope.formData.fieldAry, function(field){
                    if(!opField && field.name===ipFieldName){
                        opField= field;
                    }
                });
                return opField;
            };

            $scope.onIpValChange_exp= function(field){
                field.isNotValid= $scope[$scope.formData.form][field.modalData].$invalid;
                return field.isNotValid;
            };

            $scope.submitForm= function(){
                $rootScope.isLoading= true;
                var isFormValid= true;
                //validation
                angular.forEach($scope.formData.fieldAry, function(field){
                    //custom validation
                    if($scope.exec && $scope.exec.valdn && $scope.exec.valdn[field.name]){
                        var valdnExec= $scope.exec.valdn[field.name];
                        angular.forEach(valdnExec, function(vFn, key){
                            var result= vFn($scope);
                            if(!result.isSuccess){
                                if(!field.errors){
                                    field.errors= {};
                                }
                                field.errors[key]= result.errStr;
                                isFormValid= false;
                            }else{
                                if(field.errors && field.errors[key]){
                                    delete field.errors[key];
                                }
                            }
                        });
                    }
                    // 'Required' value validation
                    if(field.required && !$scope.formData.data[field.name]){
                        if(!field.errors){
                            field.errors= {};
                        }
                        field.errors.portalRequiredV= field.label+" is required.!";
                        isFormValid= false;
                    }else{
                        if(field.errors && field.errors.portalRequiredV){
                            delete field.errors.portalRequiredV;
                        }
                    }
                });

                /*
                if(!isFormValid){
                    isFormValid= $scope[$scope.formData.form].$valid;
                }
                */
                if(isFormValid){
                    //field operation
                    angular.forEach($scope.formData.fieldAry, function(field){
                        if($scope.exec && $scope.exec.op && $scope.exec.op[field.name]){
                            var opExec= $scope.exec.op[field.name];
                            angular.forEach(opExec, function(vFn, key){
                                vFn($scope);
                            });
                        }
                    });
                    //form operation
                    if($scope.formData.type && $scope.formData.type==="multi-part"){
                        if($scope.exec && $scope.exec.op && $scope.exec.op[$scope.formData.form]){
                            var formExec= $scope.exec.op[$scope.formData.form];
                            angular.forEach(formExec, function(vFn, key){
                                vFn($scope);
                            });
                        }
                    }

                    /*
                    grid-row-key:
                        Most of grid row arnt array but map(in a context how that they are represented in Java) i.e. it has a unique key.
                        If, Grid-Raw, the row is getting edited, it comes in a portal-form.
                        here, its to make sure grid-row-key isnt duplicated while its getting editting.
                    */
                    $scope.formData.isFormUniqueKeyTaken= false;
                    //if its a internal-collection-prop
                    if($scope.formData.parentForm){
                        var newFormUniqueKey= $scope.formData.data[$scope.formData.parentForm.name];
                        var grid= alphaplusService.obj[$scope.formData.parentForm.data];
                        angular.forEach(grid.rowData, function(row){
                            if(!$scope.formData.isFormUniqueKeyTaken){
                                //check if loop-grid-row, is same as current-in-edit-row. If yes, skip the check.
                                if($scope.formData.parentForm.editRow.portalId!=row.portalId){
                                    var existingGridRowIDName= row[$scope.formData.parentForm.name];
                                    if(existingGridRowIDName == newFormUniqueKey){
                                        $scope.formData.isFormUniqueKeyTaken= true;
                                        alert($scope.formData.parentForm.name+" ["+newFormUniqueKey+"] already taken. Please choose different one.");
                                    }
                                }
                            }
                        });
                    }
                    //1. Call Controller's form-submit-function 
                    //2. Close the modal-popup
                    if(!$scope.formData.isFormUniqueKeyTaken){
                        $scope.actionfn({
                            "formData": $scope.formData
                        });

                        //This Form is CollectionProp and Form is submitted from Model.
                        if($scope.formData.parentForm){
                            var modalInstances= $rootScope.modalInstances[$scope.formData.parentForm.data];
                            if(modalInstances){
                                modalInstances.close();
                            }
                        }
                        $rootScope.isLoading= false;
                    }
                }else{
                    $rootScope.isLoading= false;
                }
            };

            $scope.dateOptions= {
                dateDisabled: function(data){
                    return data.mode === 'day' && (data.date.getDay() === 0 || data.date.getDay() === 6);
                },
                formatYear: 'yy',
                maxDate: new Date(2020, 5, 22),
                minDate: new Date(),
                startingDay: 1
            };
            $scope.fetchTypeaheadData= function(searchEle, field){
                var reqURL= "/alphaplus/ctrl/"+field.service+"/"+field.api;
                var reqParam= {};
                reqParam.params= {};
                reqParam.params[field.paramKey]= searchEle;
                return $http.get(reqURL, reqParam).then(function(response){
                    return response.data.responseEntity;
                });
            };
            $scope.processModel= function(form, field){
                var ipObj= {
                    modalData: {
                        parentForm: field.parentForm,
                        editRow: "",
                    },
                    templateURL: field.templateUrl, 
                    controller: field.formController,
                    uibModalService: $uibModal
                };
                alphaplusService.business.viewBO(ipObj);
            };
            $scope.processSearch= function($item, $model, $label, form, field){
                form.data[field.name]= $item;
            };
            $scope.getModel = function(path){
                var segs = path.split('.');
                var root = $scope.formData.data;
                while (segs.length > 0){
                    var pathStep = segs.shift();
                    if (typeof root[pathStep] === 'undefined'){
                        root[pathStep] = segs.length === 0 ? [ '' ] : {};
                    }
                    root = root[pathStep];
                }
                return root;
            };

            $scope.typeOf = function(value){
                return typeof value;
            };
        },
        link: function($scope, element, attrs, ctrl){
        }
    };
});

/* -----------------FORM - MAPPING-FIELD-----------------*/

directiveM.directive('portalMapping', function($uibModal, alphaplusService){
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalMapping.html',
        scope: {
            form: '=',
            field: '='
        },
        controller: function($scope, $rootScope){
            $scope.processMapping= function(){
                if($scope.form.data[$scope.field.dependent.sourceField] && $scope.form.data[$scope.field.dependent.sourceField].length>0){
                    //1. empty 'field.dependent.values'
                    $scope.field.target.values= [];
                    //2. empty 'field.target.values';
                    $scope.field.dependent.values= [];
                    //open modal
                    var resolveObj= {};
                    resolveObj.form= $scope.form;
                    resolveObj.field= $scope.field;
                    var modalInstance= $uibModal.open({
                        templateUrl: "element/html/directive/portalMappingModal.html",
                        size: 'lg',
                        resolve: resolveObj,
                        controller: $scope.mappingCtrl
                    });
                    $rootScope.modalInstances[$scope.field.parentForm]= modalInstance;
                }else{
                    alert("Please first select '"+$scope.field.dependent.label+"'");
                }
            };
            $scope.removeMapping= function(key){
                //1. delete 'form.data.mappingProp.key'
                delete $scope.form.data[$scope.field.modalData][key];
                //2. empty 'field.dependent.values'
                $scope.field.target.values= [];
                //3. empty 'field.target.values';
                $scope.field.dependent.values= [];
            };
            $scope.mappingCtrl= function($scope, alphaplusService, form, field){
                $scope.form= form;
                $scope.field= field;
                //events
                $scope.dependentEvents = {
                    onItemSelect: function(item){
                    },
                    onItemDeselect: function(item){
                        $scope.removeDependentItemInternal(item);
                    }
                };
                $scope.targetEvents = {
                    onItemSelect: function(item){
                        if(!$scope.form.data[$scope.field.modalData]){
                            $scope.form.data[$scope.field.modalData]= {};
                        }
                        var key= $scope.field.dependent.values[0][$scope.field.dependent.sourceFieldKey];
                        if(!$scope.form.data[$scope.field.modalData][key]){
                            $scope.form.data[$scope.field.modalData][key]= {};
                        }
                        if(!$scope.form.data[$scope.field.modalData][key][$scope.field.resultKey]){
                            $scope.form.data[$scope.field.modalData][key][$scope.field.resultKey]= [];
                        }                        
                        $scope.form.data[$scope.field.modalData][key][$scope.field.dependent.sourceFieldKey]= key;
                        $scope.form.data[$scope.field.modalData][key][$scope.field.resultKey].push(item);
                    },
                    onSelectAll: function(){
                        // init 'form.data.prop'
                        if(!$scope.form.data[$scope.field.modalData]){
                            $scope.form.data[$scope.field.modalData]= {};
                        }
                        // init 'form.data.prop.key'
                        var key= $scope.field.dependent.values[0][$scope.field.dependent.sourceFieldKey];
                        $scope.form.data[$scope.field.modalData][key]= {};
                        $scope.form.data[$scope.field.modalData][key][$scope.field.resultKey]= [];
                        //add all options in 'form.data.prop.key'
                        angular.forEach($scope.field.target.options, function(item){
                            $scope.form.data[$scope.field.modalData][key][$scope.field.dependent.sourceFieldKey]= key;
                            $scope.form.data[$scope.field.modalData][key][$scope.field.resultKey].push(item);
                        });
                    },
                    onItemDeselect: function(item){
                        $scope.removeTargetItemInternal(item);
                    },
                    onDeselectAll: function(item){
                        $scope.removeTargetItemInternal(item, false, true);
                    }
                };

                //init 'field.dependent.options'
                angular.forEach($scope.form.fieldAry, function(fField){
                    if(!$scope.field.dependent.options && fField.modalData===$scope.field.dependent.sourceField){
                        $scope.field.dependent.options= $scope.form.data[$scope.field.dependent.sourceField];
                    }
                });

                //init 'field.target.options'
                alphaplusService[$scope.field.target.service].query({
                    action: $scope.field.target.api
                },
                function(response){ 
                    $scope.field.target.options= response;
                },
                function(response){
                    alert("["+$scope.field.service+"] ["+$scope.field.api+"] GET failure");
                });

                /*call-backs*/

                //called from html
                $scope.removeDependentItem= function(item){
                    $scope.field.dependent.values= [];
                    $scope.removeDependentItemInternal(item);
                };
                $scope.removeDependentItemInternal= function(item){
                    //1. 'field.dependent.values' got empty automatically.
                    //2. empty field.target.values 
                    $scope.field.target.values= [];
                    //3. remove 'target' from 'form.data.mappingProp'.
                    var key= item[$scope.field.dependent.sourceFieldKey];
                    var prop= $scope.form.data[$scope.field.modalData];
                    if(prop && prop[key]){
                        delete prop[key];
                    }
                };
                //called from html
                $scope.removeTargetItem= function(item){
                    $scope.removeTargetItemInternal(item, true);
                };
                $scope.removeTargetItemInternal= function(item, cleanTargetValue, deleteAll){
                    //'key' of  'mappingProp.key'
                    var sourceKey= $scope.field.dependent.values[0][$scope.field.dependent.sourceFieldKey];
                    var prop= $scope.form.data[$scope.field.modalData];
                    if(deleteAll){
                        prop[sourceKey]= [];
                    }else{
                        //1. item automatically got remove from 'field.target.values'
                        //2. remove 'item' from 'form.data.mappingProp.key'.
                        if(prop && prop[sourceKey] && prop[sourceKey][$scope.field.resultKey]){
                            var idx;
                            for(var i=0; i<prop[sourceKey][$scope.field.resultKey].length; i++){
                                var ele= prop[sourceKey][$scope.field.resultKey][i];
                                if(ele[$scope.field.target.matchFieldKey]===item[$scope.field.target.matchFieldKey]){
                                    idx= i;
                                    break;
                                }
                            }
                            prop[sourceKey][$scope.field.resultKey].splice(idx, 1);
                            //optionally also clean 'field.target.values'
                            if(cleanTargetValue){
                                $scope.field.target.values.splice(idx, 1);
                            }
                        }
                    }
                };

                //submit-mapping
                $scope.processMappingSubmit= function(){
                    console.log($scope.form.data[$scope.field.modalData]);
                    var modalInstances= $rootScope.modalInstances[$scope.field.parentForm];
                    if(modalInstances){
                        modalInstances.close();
                    }
                };
            };
        }//end controller
        /*
        ,
        compile: function compile(tElement, tAttributes){
            return {
                pre: function preLink($scope, element, attributes){
                },
                post: function postLink($scope, element, attributes){
                    if(!angular.isUndefined($scope.apData.wizzardStep)){
                        var nextWizzardStep= $scope.wizzard.wizzardStepData[$scope.apData.wizzardStep];
                        alphaplusService.business.selectWizzardStep($scope, nextWizzardStep, $scope.apData.boDetailKey);
                    }
                }
            };
        },
        link: function($scope, element, attrs, controllers){
            if(!angular.isUndefined($scope.apData.wizzardStep)){
                var nextWizzardStep= $scope.wizzard.wizzardStepData[$scope.apData.wizzardStep];
                alphaplusService.business.selectWizzardStep($scope, nextWizzardStep, $scope.apData.boDetailKey);
            }
        },
        */
    }; //end - return
});
/* -----------------WIZZARD-----------------*/


directiveM.directive('portalWizzard', function (alphaplusService) {
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalForm.html',
        scope: {
            form: '=',
            field: '=',
        },
        controller: function($scope){
            $scope.submit = function(formData){
                alphaplusService.business.submitForm(formData, $scope);
            };
            $scope.selectWizzardStep = function(wizzardStep){
                alphaplusService.business.selectWizzardStep($scope, wizzardStep, $scope.apData.boDetailKey);
            };
        }
        /*
        ,
        compile: function compile(tElement, tAttributes){
            return {
                pre: function preLink($scope, element, attributes){
                },
                post: function postLink($scope, element, attributes){
                    if(!angular.isUndefined($scope.apData.wizzardStep)){
                        var nextWizzardStep= $scope.wizzard.wizzardStepData[$scope.apData.wizzardStep];
                        alphaplusService.business.selectWizzardStep($scope, nextWizzardStep, $scope.apData.boDetailKey);
                    }
                }
            };
        },
        link: function($scope, element, attrs, controllers){
            if(!angular.isUndefined($scope.apData.wizzardStep)){
                var nextWizzardStep= $scope.wizzard.wizzardStepData[$scope.apData.wizzardStep];
                alphaplusService.business.selectWizzardStep($scope, nextWizzardStep, $scope.apData.boDetailKey);
            }
        },
        */
    };
});

/* -----------------SUMMARY-----------------*/

directiveM.directive('portalSummaryPage', function($compile, $parse, $uibModal){
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalSummaryPage.html',
        scope: {
            summaryData: '=',
            actionfn: '&'
        },
        controller: function($scope, $element, $attrs, $transclude) {
            $scope.isObjProp= function(val){
                var isObj= angular.isObject(val);
                return isObj;
            }
        },
        link: function($scope, element, attrs, controllers){
        }
    };
});

/* -----------------Key-Val-----------------*/

directiveM.directive('portalKeyVal', function($uibModal){
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalKeyVal.html',
        scope: {
            key: '=',
            dataval: '='
        },
        controller: function($scope, $element, $attrs, $transclude) {
            $scope.isObjProp= function(val){
                var isObj= angular.isObject(val);
                return isObj;
            }
        },
        link: function($scope, element, attrs, controllers){
        }
    };
});

/* -----------------FOOTER-----------------*/

directiveM.directive('portalFooter', ['$compile', '$parse', function ($compile, $parse) {
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalFooter.html',
        scope: {
            data: '='
        },
        controller: function($scope, $element, $attrs, $transclude) {
        },
        link: function($scope, element, attrs, controllers){
        }
    };
}]);

/* -----------------DATE-PICKER-----------------*/

directiveM.directive('portalDatePicker', ['$compile', '$parse', function ($compile, $parse) {
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalDatePicker.html',
        scope: {
            data: '='
        },
        controller: function($scope, $element, $attrs, $transclude) {
        },
        link: function($scope, element, attrs, controllers){
        }
    };
}]);

/* -----------------DYNAMIC-CONTROLLER-----------------*/

directiveM.directive('portalDynamicCtrl', ['$compile', '$parse',function($compile, $parse) {
    return {
        restrict: 'A',
        terminal: true,
        priority: 100000,
        link: function(scope, elem){
            scope.dPropData= {};
            scope.dPropData.parentForm= scope.$parent.field.parentForm;
            scope.dPropData.gridData= scope.$parent.$parent.formData.data[scope.$parent.field.name];

            //:::: Fetch the "parentForm" (Example: 'client.addressDetail'), from portalForm to portalTable(ListController).
            //var parentName= $parse(elem.attr('parent-name'))(scope);
            //scope.gridData= {};
            //scope.gridData.parentForm= parentName;
            var name= $parse(elem.attr('portal-dynamic-ctrl'))(scope);
            elem.removeAttr('portal-dynamic-ctrl');
            //elem.removeAttr('parent-name');
            elem.attr('ng-controller', name);
            //elem.attr('ng-init', "parentForm="+parentName);
            //alert(scope.parentForm);
            $compile(elem)(scope);
        }
    };
}]);

/* -----------------BreadCrumb-----------------*/
/*
    Limitations:
        1.  What if 2 different "Crumb-Names", points to same "hash" ?
        2.  What if one single "Crumb-Name", points to 2 different "hash" ?
*/

directiveM.directive('breadCrumb', function ($compile, $parse){
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalBreadCrumb.html',
        scope: {
            summaryData: '=',
            actionfn: '&'
        },
        controller: function($rootScope, $scope, $element, $attrs, $transclude){
            $scope.crumbs= [];
            $scope.crumbs.push({
                "name": "Home",
                "path": "http://localhost:8080/alphaplus-static/#/home"
            });
            //this will hold current-clciked-element(This could be anything with some text in it)
            $scope.currentClickSpace;
            //there we have on-click event registred on html-body (Having on-click over html-body, allows us to cature any click-event from anywhere around visible page.). 
            //i.e. this "anyClick" event is triggred on anywhere click of html-page.
            $rootScope.$on("anyClick", function(event, data){
                $scope.currentClickSpace= data.name;
            });
            //if hash changed, view changed. 
            //Here we create Crumb out of new-hash and "currentClickSpace".
            $rootScope.$on("$locationChangeSuccess", function(event, newUrl, oldUrl, newState, oldState){
                var crumbIdx= $scope.isCrumbThere();
                //if crumb already present, just remove all the crumb's after it.
                if(crumbIdx || crumbIdx==0){
                    $scope.crumbs.splice(crumbIdx+1, $scope.crumbs.length);
                }else{
                    $scope.crumbs.push({
                        "name": $scope.currentClickSpace,
                        "path": newUrl
                    });
                    $scope.currentClickSpace= null;
                }
            });

            $scope.isCrumbThere= function(){
                for(var i=0; i<$scope.crumbs.length; i++){
                    if($scope.crumbs[i].name == $scope.currentClickSpace){
                        return i;
                    }
                }
            };
        },
        link: function($scope, element, attrs, controllers){
        }
    };
});

directiveM.directive('stringToNumber', function(){
    return{
        require: 'ngModel',
        link: function(scope, element, attrs, ngModel){
            ngModel.$parsers.push(function(value){
                return '' + value;
            });
            ngModel.$formatters.push(function(value){
                return parseFloat(value);
            });
        }
    };
});

directiveM.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);


directiveM.directive('loadingPane', function ($timeout, $window) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            var directiveId = 'loadingPane';

            var targetElement;
            var paneElement;
            var throttledPosition;

            function init(element) {
                targetElement = element;

                paneElement = angular.element('<div>');
                paneElement.addClass('loading-pane');

                if (attr['id']) {
                    paneElement.attr('data-target-id', attr['id']);
                }

                var spinnerImage = angular.element('<div>');
                spinnerImage.addClass('spinner-image');
                spinnerImage.appendTo(paneElement);

                angular.element('body').append(paneElement);

                setZIndex();

                //reposition window after a while, just in case if:
                // - watched scope property will be set to true from the beginning
                // - and initial position of the target element will be shifted during page rendering
                $timeout(position, 100);
                $timeout(position, 200);
                $timeout(position, 300);

                throttledPosition = _.throttle(position, 50);
                angular.element($window).scroll(throttledPosition);
                angular.element($window).resize(throttledPosition);
            }

            function updateVisibility(isVisible) {
                if (isVisible) {
                    show();
                } else {
                    hide();
                }
            }

            function setZIndex() {                
                var paneZIndex = 1051;

                paneElement.css('zIndex', paneZIndex).find('.spinner-image').css('zIndex', paneZIndex + 1);
            }

            function position() {
                paneElement.css({
                    'left': targetElement.offset().left,
                    'top': targetElement.offset().top - $(window).scrollTop(),
                    'width': targetElement.outerWidth(),
                    'height': targetElement.outerHeight()
                });
            }

            function show() {
                paneElement.show();
                position();
            }

            function hide() {
                paneElement.hide();
            }

            init(element);

            scope.$watch(attr[directiveId], function (newVal) {
                updateVisibility(newVal);
            });

            scope.$on('$destroy', function cleanup() {
                paneElement.remove();
                $(window).off('scroll', throttledPosition);
                $(window).off('resize', throttledPosition);
            });
        }
    };
});
