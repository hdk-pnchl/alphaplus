var directiveM= angular.module('directiveM', ['ui.bootstrap', 'servicesM']);

/* -----------------BANNER-----------------*/

directiveM.directive('portalBanner', function(alphaplusService){
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

directiveM.directive("portalTable",function(){
    return {
        restrict: "E",
        templateUrl: "element/html/directive/portalTable.html",
        scope: {
            data: "=",
            searchfn: '&',
            editfn: '&',
            viewfn: '&',
            deletefn: '&'
        },
        controller: function($scope) {
            $scope.searchRow= {};
            $scope.selectedRow = null;
            $scope.summary= {};
            //$scope.active=   false;            
            $scope.sort= function(sortCol) {
                $scope.sortCol = sortCol;
                $scope.sortOrder = !$scope.sortOrder;
            };        
            $scope.selectRow = function(selectedRow){
                $scope.selectedRow= selectedRow;
                angular.forEach($scope.data.rowData, function(currentRow){
                  currentRow.selected = false;
                });
                selectedRow.selected = true;
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
                $scope.data.columnData.forEach(function(col){
                    if($scope.searchRow[col.name]){
                        col.value= $scope.searchRow[col.name];
                    }
                });
                searchIp.searchData= $scope.data.columnData;
                $scope.searchDataUpdate(searchIp);
            };
            $scope.enterSearchData = function(keyEvent) {
                if (keyEvent.which === 13){
                    $scope.searchData(1, $scope.data.rowsPerPage);
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
*/
directiveM.directive('portalForm', function ($compile, $parse, $uibModal, $interpolate, $rootScope, $http, alphaplusService) {
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalForm.html',
        scope: {
            formData: '=',
            formService: '=',
            actionfn: '&'
        },
        controller: function($scope, $element, $attrs, $transclude){
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

            $scope.submitForm= function(isFormValid){
                /*
                if(!isFormValid){
                    isFormValid= $scope[$scope.formData.form].$valid;
                }
                */
                if(isFormValid){
                    /*
                    grid-row-key:
                        Most of grid row arnt array but map(in a context how that they are represented in Java) i.e. it has a unique key.
                        if, grid-raw, the row is getting edited, it comes in a portal-form.
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
                        var modalInstances= $rootScope.modalInstances[$scope.formData.form];
                        if(modalInstances){
                            modalInstances.close();
                        }
                    }
                }
                //check for form-field validation
                angular.forEach($scope.formData.fieldAry, function(field){
                    field.error= false;
                    if(field.required && !$scope.formData.data[field.name]){
                        field.error=!isFormValid;
                    }
                });
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
                        parentForm: form.service+"."+field.name,
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
        },
        link: function($scope, element, attrs, controllers){
        }
    };
});

/* -----------------WIZZARD-----------------*/


directiveM.directive('portalWizzard', function (alphaplusService) {
    return {
        restrict: 'E',
        templateUrl: 'element/html/directive/portalWizzard.html',
        scope: true,
        controller: function($scope){
            $scope.submit = function(formData){
                alphaplusService.business.submitForm(formData, $scope, $scope.data.boDetailKey);
            };
            $scope.selectWizzardStep = function(wizzardStep){
                alphaplusService.business.selectWizzardStep($scope, wizzardStep, $scope.data.boDetailKey);
            };
        }
        /*
        ,
        compile: function compile(tElement, tAttributes){
            return {
                pre: function preLink($scope, element, attributes){
                },
                post: function postLink($scope, element, attributes){
                    if(!angular.isUndefined($scope.data.wizzardStep)){
                        var nextWizzardStep= $scope.wizzard.wizzardStepData[$scope.data.wizzardStep];
                        alphaplusService.business.selectWizzardStep($scope, nextWizzardStep, $scope.data.boDetailKey);
                    }
                }
            };
        },
        link: function($scope, element, attrs, controllers){
            if(!angular.isUndefined($scope.data.wizzardStep)){
                var nextWizzardStep= $scope.wizzard.wizzardStepData[$scope.data.wizzardStep];
                alphaplusService.business.selectWizzardStep($scope, nextWizzardStep, $scope.data.boDetailKey);
            }
        },
        */
    };
});

/* -----------------SUMMARY-----------------*/

directiveM.directive('portalSummaryPage', ['$compile', '$parse', function ($compile, $parse) {
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
}]);

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
            //:::: Fetch the "parentForm" (Example: 'client.addressDetail'), from portalForm to portalTable(ListController).
            var name = $parse(elem.attr('portal-dynamic-ctrl'))(scope);
            elem.removeAttr('portal-dynamic-ctrl');
            elem.attr('ng-controller', name);
            elem.attr('ng-init', "parentForm="+scope.parentForm);
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
