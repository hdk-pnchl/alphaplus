var alphaplusM= angular.module('alphaplusM', ['ngRoute','ngAnimate','servicesM','directiveM','ui.bootstrap', 'angularjs-dropdown-multiselect', 'ngTable',
	'controllersM',
	'clientControllersM', 
	'instControllersM', 
	'jobControllersM',
	'messageControllersM',
	'userControllersMN',
	'plateControllersM',
	'addressControllersM',
	'contactControllersM',
	'testControllersM']);

alphaplusM.config(['$qProvider', function ($qProvider) {
	$qProvider.errorOnUnhandledRejections(false);
}]);

alphaplusM.config(function($routeProvider, $locationProvider, $sceDelegateProvider){
	$sceDelegateProvider.resourceUrlWhitelist(['**']);
	$locationProvider.html5Mode({
		enabled: false,
		requireBase: true
	}).hashPrefix("");


	//Home
	$routeProvider.when('/home', {
		templateUrl: 'element/html/business/core/home.html',
		controller: 'HomeController'
	});
	$routeProvider.when('/listUser', {
		templateUrl: 'element/html/business/core/listUser.html',
		controller: 'CITUserListController'
	});

	//aboutUs
	$routeProvider.when('/aboutUs', {
		templateUrl: 'element/html/business/core/aboutUs.html',
		controller: 'AboutUsController'
	});

	//contactUs
	$routeProvider.when('/contactUs', {
		templateUrl: 'element/html/business/core/contactUs.html',
		controller: 'ContactUsController'
	});
	$routeProvider.when('/messages', {
		templateUrl: 'html/messages.html',
		controller: 'MessageController'
	});

	//singIn
	$routeProvider.when('/signIn', {
		templateUrl: 'element/html/business/core/signIn.html',
		controller: 'SignController'
	});
	$routeProvider.when('/signIn/:error', {
		templateUrl: 'element/html/business/core/signIn.html',
		controller: 'SignController'
	});


	//message
	$routeProvider.when('/message/list', {
		templateUrl: 'element/html/business/crud/list.html',
		controller: 'MessageListController'
	});
	$routeProvider.when('/message/new', {
		templateUrl: 'element/html/business/crud/form.html',
		controller: 'MessageController'
	});	
	$routeProvider.when('/message/update/:messageID', {
		templateUrl: 'element/html/business/crud/form.html',
		controller: 'MessageController'
	});
	$routeProvider.when('/message/summary/:messageID', {
		templateUrl: 'element/html/business/crud/summary.html',
		controller: 'MessageSummaryController'
	});

	//plate
	$routeProvider.when('/plate/list', {
		templateUrl: 'element/html/business/crud/list.html',
		controller: 'PlateListController'
	});
	$routeProvider.when('/plate/new', {
		templateUrl: 'element/html/business/crud/form.html',
		controller: 'PlateController'
	});
	$routeProvider.when('/plate/update/:plateID', {
		templateUrl: 'element/html/business/crud/form.html',
		controller: 'PlateController'
	});
	$routeProvider.when('/plate/summary/:plateID', {
		templateUrl: 'element/html/business/crud/summary.html',
		controller: 'PlateSummaryController'
	});

	//user
	$routeProvider.when('/user/list', {
		templateUrl: 'element/html/business/user/user-list.html',
		controller: 'UserListControllerN'
	});
	$routeProvider.when('/user/new', {
		templateUrl: 'element/html/business/user/user.html',
		controller: 'UserControllerN'
	});	
	$routeProvider.when('/user/update/:userID', {
		templateUrl: 'element/html/business/user/user.html',
		controller: 'UserControllerN'
	});
	$routeProvider.when('/user/update/:userID/:step', {
		templateUrl: 'element/html/business/user/user.html',
		controller: 'UserControllerN'
	});	
	$routeProvider.when('/user/summary/:userID', {
		templateUrl: 'element/html/business/user/user.html',
		controller: 'UserControllerN'
	});

	//client
	$routeProvider.when('/client/list', {
		templateUrl: 'element/html/business/client/client-list.html',
		controller: 'ClientListControllerN'
	});
	$routeProvider.when('/client/new', {
		templateUrl: 'element/html/business/client/client.html',
		controller: 'ClientControllerN'
	});	
	$routeProvider.when('/client/update/:clientID', {
		templateUrl: 'element/html/business/client/client.html',
		controller: 'ClientControllerN'
	});
	$routeProvider.when('/client/update/:clientID/:step', {
		templateUrl: 'element/html/business/client/client.html',
		controller: 'ClientControllerN'
	});	
	$routeProvider.when('/client/summary/:clientID', {
		templateUrl: 'element/html/business/client/client.html',
		controller: 'ClientControllerN'
	});

	//job
	$routeProvider.when('/job/list', {
		templateUrl: 'element/html/business/job/job-list.html',
		controller: 'JobListControllerN'
	});
	$routeProvider.when('/job/new', {
		templateUrl: 'element/html/business/job/job.html',
		controller: 'JobControllerN'
	});	
	$routeProvider.when('/job/update/:jobID', {
		templateUrl: 'element/html/business/job/job.html',
		controller: 'JobControllerN'
	});
	$routeProvider.when('/job/update/:jobID/:step', {
		templateUrl: 'element/html/business/job/job.html',
		controller: 'JobControllerN'
	});		
	$routeProvider.when('/job/summary/:jobID', {
		templateUrl: 'element/html/business/job/job.html',
		controller: 'JobControllerN'
	});

	//address
	$routeProvider.when('/address/list', {
		templateUrl: 'element/html/business/crud/list.html',
		controller: 'AddressListController'
	});
	$routeProvider.when('/address/new', {
		templateUrl: 'element/html/business/crud/form.html',
		controller: 'AddressController'
	});	
	$routeProvider.when('/address/update/:addressID', {
		templateUrl: 'element/html/business/crud/form.html',
		controller: 'AddressController'
	});
	$routeProvider.when('/address/summary/:addressID', {
		templateUrl: 'element/html/business/crud/summary.html',
		controller: 'AddressSummaryController'
	});

	//password
	$routeProvider.when('/user/changePassword', {
		templateUrl: 'element/html/business/user/password/changePassword.html',
		controller: 'ChangePasswordController'
	});
	$routeProvider.when('/user/forgotPassword', {
		templateUrl: 'element/html/business/user/password/forgotPassword.html',
		controller: 'ChangePasswordController'
	});
	$routeProvider.when('/user/updateForgottenPassword/:token', {
		templateUrl: 'element/html/business/user/password/updateForgottenPassword.html',
		controller: 'ChangePasswordController'
	});

	//otherwise
	$routeProvider.otherwise({
		redirectTo: '/home'
	});	
});

alphaplusM.filter('isEmpty', function(){
    var bar;
    return function (obj) {
        for (bar in obj) {
            if (obj.hasOwnProperty(bar)) {
                return false;
            }
        }
        return true;
    };
});