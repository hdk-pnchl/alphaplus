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
    $scope.dataData.disableTabs= true;
    $scope.dataData.activeTab= 0;
    /* ------------------|init|------------------*/
    $scope.initJob= function(respData){
        $scope.formData= respData.responseEntity;
        $scope.dataData.disableTabs= true;
        $scope.plateTableParams = new NgTableParams({}, { dataset: $scope.formData.plates });  
        $scope.formData.clientSelect= $scope.formData.client.name;
        $scope.formData.docketBySelect= $scope.formData.docketBy.name;
        //exeBy
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
        //TableParams
        $scope.instructionTableParams = new NgTableParams({}, { dataset: $scope.formData.instructions });
        $scope.studioInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.studio.instructions });
        $scope.ctpInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.ctp.instructions });
        $scope.deliveryInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.delivery.instructions });
        $scope.billInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.bill.instructions });
        $scope.challanInstructionTableParams = new NgTableParams({}, { dataset: $scope.formData.challan.instructions });
    };    
    /* ------------------|JOB|------------------*/    
    if($routeParams.jobID){      
        alphaplusService.job.get({
            "id": $routeParams.jobID,
            "action": "get"
        }, function(respData){
            $scope.initJob(respData);
            $scope.dataData.disableTabs= false;
            if($routeParams.step){
                $scope.dataData.activeTab= parseInt($routeParams.step);
            }              
        });
    }else{
        alphaplusService.job.get({
            "action": "empty"
        }, function(respData){
            $scope.initJob(respData);
        });               
    }
    /* ------------------|Fetch|------------------*/
    $scope.fetchDocket= function(){
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
        return docket;
    };
    $scope.fetchStudio= function(){
        var studio= {};
        studio.exeByID= $scope.formData.studio.exeBy.id;
        studio.status= $scope.formData.studio.status;  
        studio.jobID= $scope.formData.id;
        return studio;
    };
    $scope.fetchCtp= function(){
        var ctp= {};
        ctp.exeByID= $scope.formData.ctp.exeBy.id;
        ctp.status= $scope.formData.ctp.status;        
        ctp.jobID= $scope.formData.id;
        return ctp;
    };
    $scope.fetchDelivery= function(){
        var delivery= {};
        delivery.exeByID= $scope.formData.delivery.exeBy.id;
        delivery.status= $scope.formData.delivery.status;
        delivery.date= $scope.formData.delivery.date;
        delivery.jobID= $scope.formData.id;
        return delivery;
    };
    $scope.fetchBill= function(){
        var bill= {};
        bill.exeByID= $scope.formData.bill.exeBy.id;
        bill.status= $scope.formData.bill.status;
        bill.date= $scope.formData.bill.date;
        bill.jobID= $scope.formData.id;
        return bill;
    };
    $scope.fetchChallan= function(){
        var challan= {};
        challan.exeByID= $scope.formData.challan.exeBy.id;
        challan.status= $scope.formData.challan.status;     
        challan.date= $scope.formData.challan.date;   
        challan.jobID= $scope.formData.id;
        return challan;
    };

    /* ------------------|submit|------------------*/
    //docket
    $scope.saveDocket= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.client && $scope.formData.docketBy){
            var docket= $scope.fetchDocket();
            var command= "docket";
            if(!docket.jobID){
                command= "new";
            }
            alphaplusService.job.save({
                action: command
            }, docket, function(response){
                if(response.responseData.ERROR){
                }else{
                    //isNOTfirstStep
                    if($scope.formData.id){
                        $scope.formData= response.responseEntity;
                        $scope.processTabs();                           
                    }else{
                        $scope.formData= response.responseEntity;
                        $location.path(alphaplusService.obj.bannerData.navData.mainNavData.job.subNav.update.path+"/"+$scope.formData.id+"/"+1);
                    }                    
                }                             
            });                    
        }  
    };
    //studio
    $scope.saveStudio= function(form){      
        //form is-valid and as well client/dockerBy are selected.
        if(form.$valid && $scope.formData.studio.exeBy){
            var studio= $scope.fetchStudio();
            alphaplusService.job.save({
                action: "studio",
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
            var ctp= $scope.fetchCtp();
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
            var delivery= $scope.fetchDelivery();
            alphaplusService.job.save({
                action: "delivery",
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
            var bill= $scope.fetchBill();
            alphaplusService.job.save({
                action: "bill",
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
            var challan= $scope.fetchChallan();
            alphaplusService.job.save({
                action: "challan",
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
    $scope.processPlate= function(editMode, plate){  
        var resolveObj= {};        
        resolveObj.editMode= editMode;
        resolveObj.formData= $scope.formData;
        resolveObj.plateTableParams= $scope.plateTableParams;
        resolveObj.ipPlate= plate;
        $uibModal.open({
            templateUrl: "element/html/business/plate/plate.html",
            size: 'lg',
            resolve: resolveObj,
            controller: "PlateControllerN"           
        });     
    };
    $scope.openPlate= function(plate){
        if(plate){
            $scope.processPlate(true, plate);
        }else{
            alphaplusService.plate.get({
                "action": "empty"
            }, function(respData){
                $scope.processPlate(false, respData.responseEntity);
            });  
        }
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