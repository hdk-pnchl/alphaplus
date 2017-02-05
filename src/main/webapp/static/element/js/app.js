var alphaplusM= angular.module('alphaplusM', ['ngRoute','ngAnimate','controllersM','clientControllersM','jobControllersM','messageControllersM','userControllersM','servicesM','directiveM','ui.bootstrap']);

alphaplusM.config(function($resourceProvider){});

alphaplusM.config(['$routeProvider', function($routeProvider){
	//Home
	$routeProvider.when('/home', {
		templateUrl: 'element/html/business/core/home.html',
		controller: 'HomeController'
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

	//singUp
	$routeProvider.when('/signUp', {
		templateUrl: 'element/html/business/core/signUp.html',
		controller: 'SignController'
	});

	//message
	$routeProvider.when('/message/list', {
		templateUrl: 'element/html/business/message/list.html',
		controller: 'MessageListController'
	});
	$routeProvider.when('/message/new', {
		templateUrl: 'element/html/business/message/message.html',
		controller: 'MessageController'
	});	
	$routeProvider.when('/message/update/:messageID', {
		templateUrl: 'element/html/business/message/message.html',
		controller: 'MessageController'
	});
	$routeProvider.when('/message/summary/:messageID', {
		templateUrl: 'element/html/business/message/summary.html',
		controller: 'MessageSummaryController'
	});
	
	//job
	$routeProvider.when('/job/list', {
		templateUrl: 'element/html/business/job/list.html',
		controller: 'JobListController'
	});
	$routeProvider.when('/job/new', {
		templateUrl: 'element/html/business/job/job.html',
		controller: 'JobController'
	});	
	$routeProvider.when('/job/update/:jobID', {
		templateUrl: 'element/html/business/job/job.html',
		controller: 'JobController'
	});
	$routeProvider.when('/job/summary/:jobID', {
		templateUrl: 'element/html/business/job/summary.html',
		controller: 'JobSummaryController'
	});

	//user
	$routeProvider.when('/user/list', {
		templateUrl: 'element/html/business/user/list.html',
		controller: 'UserListController'
	});
	$routeProvider.when('/user', {
		templateUrl: 'element/html/business/user/user.html',
		controller: 'UserController'
	});		
	$routeProvider.when('/user/new', {
		templateUrl: 'element/html/business/user/user.html',
		controller: 'UserController'
	});	
	$routeProvider.when('/user/update/:userID', {
		templateUrl: 'element/html/business/user/user.html',
		controller: 'UserController'
	});
	$routeProvider.when('/user/summary/:userID', {
		templateUrl: 'element/html/business/user/summary.html',
		controller: 'UserSummaryController'
	});

	//client
	$routeProvider.when('/client/list', {
		templateUrl: 'element/html/business/client/list.html',
		controller: 'ClientListController'
	});
	$routeProvider.when('/client', {
		templateUrl: 'element/html/business/client/client.html',
		controller: 'ClientController'
	});		
	$routeProvider.when('/client/new', {
		templateUrl: 'element/html/business/client/client.html',
		controller: 'ClientController'
	});	
	$routeProvider.when('/client/update/:clientID', {
		templateUrl: 'element/html/business/client/client.html',
		controller: 'ClientController'
	});
	$routeProvider.when('/client/summary/:clientID', {
		templateUrl: 'element/html/business/client/summary.html',
		controller: 'ClientSummaryController'
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
}]);

alphaplusM.config(['$locationProvider', function($locationProvider){}]);


alphaplusM.config(function($sceDelegateProvider) {
  $sceDelegateProvider.resourceUrlWhitelist(['**']);
});

