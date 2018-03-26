var jobControllersM= angular.module('jobControllersM', ['servicesM', 'ui.bootstrap']);

jobControllersM.controller('JobListControllerN', function($scope, $uibModal, NgTableParams, alphaplusService){ 
    $scope.tableParams = new NgTableParams(alphaplusService.obj.grid.initialParams, {      
        getData: function(params){
            var searchIp= {};
            searchIp.pageNo= params.page();
            searchIp.rowsPerPage= params.count();
            searchIp.searchData= [];
            searchIp.searchData.push(params.filter());   
                    
            // ajax request to api
            return alphaplusService.job.save({
                action: "search"
            }, searchIp).$promise.then(function(response) {
                params.total(response.responseData.ROW_COUNT); // recal. page nav controls
                return response.responseEntity;
            });         
        }
    }); 
});

jobControllersM.controller('JobControllerN', function($scope, $routeParams, $location, alphaplusService, NgTableParams, $uibModal){
    $scope.dateOptions= alphaplusService.obj.from.dateOptions;
    $scope.dataData= {};
    $scope.dataData.disableTabs= false;
    $scope.dataData.activeTab= 0;

    /* ------------------|JOB|------------------*/

    if($routeParams.jobID){
        alphaplusService.job.get({
            "id": $routeParams.jobID,
            "action": "get"
        }, function(respData){
            $scope.formData= respData.responseEntity;
            $scope.formData.clientSelect= $scope.formData.client.name;
            $scope.formData.docketBySelect= $scope.formData.docketBy.name;
            if($scope.formData.studio.exeBy){
                $scope.formData.studioBySelect= $scope.formData.studio.exeBy.name;
            }            
            if($scope.formData.ctp.exeBy){
                $scope.formData.ctpBySelect= $scope.formData.ctp.exeBy.name;
            }    
            if($scope.formData.delivery.exeBy){
                $scope.formData.deliveryBySelect= $scope.formData.delivery.exeBy.name;
            }    
            if($scope.formData.bill.exeBy){
                $scope.formData.billBySelect= $scope.formData.bill.exeBy.name;
            }    
            if($scope.formData.challan.exeBy){
                $scope.formData.challanBySelect= $scope.formData.challan.exeBy.name;
            }                                        
            $scope.dataData.disableTabs= false;  
            $scope.plateTableParams = new NgTableParams({}, { dataset: $scope.formData.plates });  
            $scope.instructionTableParams = new NgTableParams({}, { dataset: $scope.formData.instructions });
            $scope.studioInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.studio.instructions });
            $scope.ctpInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.ctp.instructions });
            $scope.deliveryInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.delivery.instructions });
            $scope.billInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.bill.instructions });
            $scope.challanInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.challan.instructions });
        });
    }else{
        $scope.formData= {};      
        $scope.formData.receivedDate= new Date();
        $scope.formData.targetDate= new Date();
        $scope.formData.bindingStyle= "CENTER";
        $scope.formData.colorCopySize= "A3";
        $scope.formData.docketStatus= "New";
        $scope.formData.cut= 1;
        $scope.formData.open= 1;
        $scope.formData.page= 1;
        $scope.formData.plates= [];
        //TableParams
        $scope.plateTableParams = new NgTableParams({}, { dataset: $scope.formData.plates });
        $scope.formData.instructions= [];
        $scope.instructionTableParams = new NgTableParams({}, { dataset: $scope.formData.instructions });
        //studio
        $scope.formData.studio= {};
        $scope.formData.studio.instructions= [];
        $scope.studioInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.studio.instructions });
        //ctp
        $scope.formData.ctp= {};
        $scope.formData.ctp.instructions= [];
        $scope.ctpInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.ctp.instructions });
        //challan
        $scope.formData.challan= {};
        $scope.formData.challan.instructions= [];
        $scope.challanInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.challan.instructions });
        //delivery
        $scope.formData.delivery= {};
        $scope.formData.delivery.instructions= [];
        $scope.deliveryInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.delivery.instructions });
        //bill
        $scope.formData.bill= {};
        $scope.formData.bill.instructions= [];
        $scope.billInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.bill.instructions });
    }

    /* ------------------|submit|------------------*/
    //docket
    $scope.saveDocket= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.client && $scope.formData.docketBy){
            var docket= {};
            docket.jobID= $scope.formData.id;
            docket.name= $scope.formData.name;
            docket.receivedDate= $scope.formData.receivedDate;
            docket.targetDate= $scope.formData.targetDate;
            docket.cut= $scope.formData.cut;
            docket.open= $scope.formData.open;
            docket.page= $scope.formData.page;
            docket.bindingStyle= $scope.formData.bindingStyle;
            docket.colorCopySize= $scope.formData.colorCopySize;
            docket.docketStatus= $scope.formData.docketStatus;
            docket.clientID= $scope.formData.client.id;
            docket.docketByID= $scope.formData.docketBy.id;
            alphaplusService.job.save({
                action: "docket"
            }, docket, function(response){
                if(response.responseData.ERROR){
                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };
    //studio
    $scope.saveStudio= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.studio.exeBy){
            var studio= {};
            studio.exeByID= $scope.formData.studio.exeBy.id;
            studio.status= $scope.formData.studio.status;
            alphaplusService.job.save({
                action: "studio",
                jobID: $scope.formData.id
            }, studio, function(response){
                if(response.responseData.ERROR){
                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };    
    //ctp
    $scope.saveCTP= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.ctp.exeBy){
            var ctp= {};
            ctp.exeByID= $scope.formData.ctp.exeBy.id;
            ctp.status= $scope.formData.ctp.status;
            alphaplusService.job.save({
                action: "ctp",
                jobID: $scope.formData.id
            }, ctp, function(response){
                if(response.responseData.ERROR){
                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };  
    //delivery
    $scope.saveDelivery= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.delivery.exeBy){
            var delivery= {};
            delivery.exeByID= $scope.formData.delivery.exeBy.id;
            delivery.status= $scope.formData.delivery.status;
            alphaplusService.job.save({
                action: "delivery",
                jobID: $scope.formData.id
            }, delivery, function(response){
                if(response.responseData.ERROR){
                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };  
    //bill
    $scope.saveBill= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.bill.exeBy){
            var bill= {};
            bill.exeByID= $scope.formData.bill.exeBy.id;
            bill.status= $scope.formData.bill.status;
            alphaplusService.job.save({
                action: "bill",
                jobID: $scope.formData.id
            }, bill, function(response){
                if(response.responseData.ERROR){
                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };  
    //challan
    $scope.saveChallan= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.challan.exeBy){
            var challan= {};
            challan.exeByID= $scope.formData.challan.exeBy.id;
            challan.status= $scope.formData.challan.status;
            alphaplusService.job.save({
                action: "challan",
                jobID: $scope.formData.id
            }, challan, function(response){
                if(response.responseData.ERROR){
                }else{
                    $scope.processTabs();    
                }            
            });                    
        }  
    };  

    /* ------------------|Tabs|------------------*/

    //Tabs
    $scope.processTabs= function(){
        $scope.dataData.disableTabs= false;
        $scope.nextTabs();        
    };
    $scope.nextTabs= function(){
        if(!$scope.dataData.disableTabs){
            var nextTabIdx= $scope.dataData.activeTab + 1;
            if(nextTabIdx < 8){
                $scope.dataData.activeTab= nextTabIdx;
            }else{
                $location.path(alphaplusService.obj.bannerData.navData.mainNavData.job.subNav.list.path);
            }            
        }
    };  

    /* ------------------|Type-Head|------------------*/

    //Client
    $scope.fetchClients= function(searchEle){
        return alphaplusService.business.fetchTypeaheadData(searchEle, "client", "seachByName", "name");    
    };
    $scope.selectClient= function($item){
        $scope.formData.client= $item;
    };
    //User
    $scope.fetchUsers= function(searchEle){
        return alphaplusService.business.fetchTypeaheadData(searchEle, "user", "seachByName", "name");    
    };
    $scope.selectUser= function($item, module){
        if(module){
            $scope.formData[module].exeBy= $item;    
        }else{
            $scope.formData.docketBy= $item;
        }
    };    

    /* ------------------|Modal|------------------*/

    //Plates
    $scope.openPlate= function(plate){
        //resolveObj
        var resolveObj= {};        
        resolveObj.editMode= true;
        //plate
        if(!plate){
            plate= {};
            //plate-size            
            plate.plateHeight= 0;
            plate.plateWidth= 0;            
            plate.plateUnit= "CM";
            //paper-size
            plate.paperHeight= 0;
            plate.paperWidth= 0;            
            plate.paperUnit= "CM";
            //set
            plate.theSet= 1;
            plate.theSetColour= 1;
            plate.total= 0;
            //gripper/screen
            plate.gripper= 1;
            plate.screen= 1;
            //fb_sb_dg_os
            plate.frontBack= 0;
            plate.selfBack= 0;
            plate.doubleGripper= 0;
            plate.oneSide= 0;

            plate.status= "New";

            resolveObj.editMode= false;
        }
        resolveObj.formData= $scope.formData;
        resolveObj.plateTableParams= $scope.plateTableParams;
        resolveObj.ipPlate= plate;        
        //$uibModal
        $uibModal.open({
            templateUrl: "element/html/business/plate/plate.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "PlateControllerN"           
        });
    };    
    //Instruction
    $scope.openInstruction= function(part, instruction){
        //----| resolveObj
        var resolveObj= {};        
        resolveObj.editMode= true;
        //instruction
        if(!instruction){
            instruction= {};
            resolveObj.editMode= false;
        }
        resolveObj.ipInstruction= instruction;
        //part
        resolveObj.ipPart= {"value": part};        
        //job
        resolveObj.formData= $scope.formData;
        //TableParams
        if(part == "docket"){
            resolveObj.instructionTableParams= $scope.instructionTableParams;
        }else if(part == "studio"){
            resolveObj.instructionTableParams= $scope.studioInstructionTableParams;
        }else if(part == "ctp"){
            resolveObj.instructionTableParams= $scope.ctpInstructionTableParams;
        }else if(part == "bill"){
            resolveObj.instructionTableParams= $scope.billInstructionTableParams;
        }else if(part == "challan"){
            resolveObj.instructionTableParams= $scope.challanInstructionTableParams;
        }else if(part == "delivery"){
            resolveObj.instructionTableParams= $scope.deliveryInstructionTableParams;
        }        
        //----| $uibModal
        $uibModal.open({
            templateUrl: "element/html/business/instruction/instruction.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "InstructionControllerN"           
        });
    };        
});

var jobListController= jobControllersM.controller('JobListController', function($scope, $uibModal, alphaplusService){ 
    $scope.service= "job";
    alphaplusService.business.processColumn($scope);
    
    $scope.edit = function(editRow){
        var ipObj= {
            bannerTab: "job",
            primaryKey: editRow.id
        };
        alphaplusService.business.viewBO(ipObj);
    };
    $scope.view = function(viewRow){
        var ipObj= {
            modalData: {
                viewRow: viewRow,
                primaryKey: viewRow.id
            },
            templateURL: "element/html/business/crud/summary.html",
            controller: "JobSummaryController",
            uibModalService: $uibModal
        };
        alphaplusService.business.viewBO(ipObj);
    };
    $scope.delete = function(deleteRow){ 
        alert("Delete not possible yet. Work in progress.");
    };
});

var jobController= jobControllersM.controller('JobController', function($scope, $routeParams, alphaplusService){
    var primaryKeyData= {
        val: $routeParams.jobID,
        propName: "id"
    };
    var data= {};
    data.primaryKeyData= primaryKeyData;
    data.service= "job";
    data.boDetailKey= "boDetail";
    data.wizzardStep= $routeParams.wizzardStep;

    $scope.apData= data;

    alphaplusService.business.processWizzard($scope);
});

var jobSummaryController= jobControllersM.controller('JobSummaryController', 
    function($scope, alphaplusService, primaryKey, viewRow){
        
    $scope.apData= {};
    $scope.apData.service= "job";
    $scope.apData.idKey= "id";
    $scope.apData.id= primaryKey;
    $scope.apData.boDetailKey= "boDetail";
    $scope.apData.viewRow= viewRow;

    alphaplusService.business.processSummary($scope);
});

var jobService= {};
jobService.jobSummaryController= jobSummaryController;
jobService.jobController= jobController;
jobService.jobListController= jobListController;

jobControllersM.constant('jobService', jobService);