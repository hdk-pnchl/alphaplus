var testControllersM= angular.module('testControllersM', ['servicesM', 'ui.bootstrap', 'angularjs-dropdown-multiselect', 'ngTable']);

testControllersM.controller('TestController', function($scope, $resource, NgTableParams){
	//filters
    $scope.filters= {};
	$scope.addFilter= function(prop, filters) {
		$scope.filters[prop]=[];
		angular.forEach(filters, function(filter){
			$scope.filters[prop].push({
		        "id": filter,
		        "title": filter				
			});
		});
	}
	var txnType = ['charge','refund','reversal','chargeback','credit'];
	var txnStatus = ['success','failed','unknown','reversed'];
	$scope.addFilter("transactiontype", txnType);
	$scope.addFilter("bokutransactionstatus", txnStatus);
	$scope.addFilter("operatortransactionstatus", txnStatus);
	//row selection
	$scope.processSelectRow= function(idx, exception) {
		if(exception.selected==true){
			exception.selected= false;
		}else{
			exception.selected= true;	
		}
	};
	//all row selected
	$scope.allRowsSelected= false;
	$scope.processSelectAll= function() {
		if($scope.allRowsSelected==true){
			$scope.allRowsSelected= false;
			angular.forEach($scope.tableParams.data, function(exception){
				exception.selected= false;
			});			
		}else{
			$scope.allRowsSelected= true
			angular.forEach($scope.tableParams.data, function(exception){
				exception.selected= true;
			});			
		}
	};
	//process row
	$scope.processRow= function(exception) {
		//alert("Lets do something.!");
	};

	//initialParams
	$scope.initialParams = {
        page: 1,    // show first page
        count: 10  	// count per page
    };
    //Api
    var Api = $resource('/alphaplus/ctrl/test/:action', {
        action: '@action'
    });                
    //tableParams
    $scope.tableParams = new NgTableParams($scope.initialParams, {		
		getData: function(params){
			var searchIp= {};
			searchIp.pageNo= params.page();
			searchIp.rowsPerPage= params.count();
			searchIp.searchData= params.filter(); 	
					
			// ajax request to api
			return Api.save({
			    action: "exceptions"
			}, searchIp).$promise.then(function(response) {
				params.total(response.responseData.total); // recal. page nav controls
				return response.responseEntity;
			});			
		}
    });	
});